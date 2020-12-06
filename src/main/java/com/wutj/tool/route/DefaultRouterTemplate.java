package com.wutj.tool.route;

import com.wutj.tool.route.constant.DRParam;
import com.wutj.tool.route.model.IRouter;
import com.wutj.tool.route.recovery.IRecoveryTaskHandler;
import com.wutj.tool.route.recovery.RecoveryTask;
import com.wutj.tool.route.recovery.TaskType;
import com.wutj.tool.route.strategy.RecoveryIntervalStrategy;
import com.wutj.tool.route.strategy.RecoveryStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 路由配置模板，单例
 *
 * @author wutingjia
 */
public class DefaultRouterTemplate implements IRouterTemplate, InitializingBean {

	private static final Logger log = LoggerFactory.getLogger(DefaultRouterTemplate.class);

	/**
	 * 默认路由,也是初始路由
	 */
	private IRouter defaultRouter;

	/**
	 * 当前路由
	 */
	private IRouter router;

	/**
	 * 上一次路由
	 */
	private IRouter previousRouter;

	/**
	 * 路由恢复策略
	 */
	private RecoveryStrategy recoveryStrategy;

	/**
	 * 路由恢复数值
	 */
	private String period;

	/**
	 * 路由恢复时间间隔策略
	 */
	private RecoveryIntervalStrategy recoveryIntervalStrategy;

	/**
	 * 恢复任务执行器
	 */
	private final IRecoveryTaskHandler recoveryTaskHandler;

	/**
	 * 是否被锁定，如果被锁定则在此期间路由无法被修改
	 */
	private final AtomicBoolean lock = new AtomicBoolean(false);

    /**
     * 恢复任务队列
     */
    private final DelayQueue<RecoveryTask> queue;

	public DefaultRouterTemplate(IRecoveryTaskHandler recoveryTaskHandler, DelayQueue<RecoveryTask> queue) {
		this.recoveryTaskHandler = recoveryTaskHandler;
        this.queue = queue;
    }

	public IRouter getRouter() {
		return this.router;
	}

	@Override
	public void setRouter(IRouter router) {
	    if (this.lock.get()) {
	        log.warn("模板已上锁无法修改路由");
        }
		this.previousRouter = this.router;
		this.router = router;
	}

	public IRouter getDefaultRouter() {
		return defaultRouter;
	}

	public void setDefaultRouter(IRouter defaultRouter) {
		this.defaultRouter = defaultRouter;
	}

	public RecoveryStrategy getRecoveryStrategy() {
		return this.recoveryStrategy;
	}

	public void setRecoveryStrategy(RecoveryStrategy recoveryStrategy) {
		this.recoveryStrategy = recoveryStrategy;
	}

    public RecoveryIntervalStrategy getRecoveryIntervalStrategy() {
        return recoveryIntervalStrategy;
    }

    public void setRecoveryIntervalStrategy(RecoveryIntervalStrategy recoveryIntervalStrategy) {
		this.recoveryIntervalStrategy = recoveryIntervalStrategy;
	}

	@Override
	public void invokeSwitch(IRouter nextRouter, IRouter msgRouter) {
		if (this.lock.get()) {
			log.info("路由模板已被锁定无法切换路由, 该触发信息的路由为" + msgRouter.getName());
			return;
		}

		if (!msgRouter.getName().equals(this.router.getName())) {
			log.info("当前路由" + this.router.getName() + "与触发信息中的路由" + msgRouter.getName() + "不一致，忽略该次切换请求");
			return;
		}
		trySwitch(nextRouter);
	}

	private void trySwitch (IRouter nextRouter) {
		this.previousRouter = this.router;
		this.router = nextRouter;
		log.info("路由切换成功，从" + this.previousRouter.getName() + "切换到" + this.router.getName());
		String str = this.period;
		Map<DRParam, Object> map = new HashMap<>();
		map.put(DRParam.PERIOD, str);
		this.recoveryTaskHandler.registerTask(TaskType.ROUTER, map);
	}

	@Override
	public void invokeRecovery(TaskType type) {
		if (TaskType.UNLOCK == type) {
			log.info("路由模板已解锁");
			this.lock.set(false);
			return;
		}

		if (this.lock.get()) {
			log.info("路由模板已被锁定无法恢复路由");
			return;
		}

		if (RecoveryStrategy.DEFAULT == this.recoveryStrategy) {
			this.previousRouter = this.router;
			this.router = this.defaultRouter;
			log.info("路由恢复完成,策略为first，恢复为" + this.router.getName());
		}else if (RecoveryStrategy.PREVIOUS == this.recoveryStrategy) {
			IRouter temp = this.router;
			this.router = this.previousRouter;
			this.previousRouter = temp;
			log.info("路由恢复完成,策略为previous，恢复为" + this.router.getName());
		}else {
			log.error("未知的恢复路由策略,将使用第一个路由" + recoveryStrategy.name());
			this.previousRouter = this.router;
			this.router = this.defaultRouter;
		}
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public boolean lock() {
	    return this.lock.compareAndSet(false,true);
    }

    public boolean unlock() {
        return this.lock.compareAndSet(true,false);
    }

	@Override
	public void afterPropertiesSet() {
		checkInjection();
	}

	private void checkInjection() {

		if (this.router == null || this.defaultRouter == null) {
			throw new IllegalArgumentException("RouterTemplate未设置默认路由");
		}

		if (this.recoveryStrategy == null || this.recoveryIntervalStrategy == null) {
			throw new IllegalArgumentException("RouterTemplate未设置路由恢复策略");
		}
	}

    @PostConstruct
    public void invokeRecovery() {

        Thread t = new Thread(() -> {
            log.info("动态路由恢复队列线程已就绪");
            while (true) {
                RecoveryTask recoveryTask;
                try {
                    recoveryTask = this.queue.take();
                    invokeRecovery(recoveryTask.getType());
                } catch (InterruptedException e) {
                    log.error("从恢复队列中获取任务异常", e);
                }
            }
        });
        t.start();
    }
}
