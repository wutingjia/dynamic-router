package com.wutj.tool.route;

import com.wutj.tool.route.constant.DRParam;
import com.wutj.tool.route.constant.EventMsgType;
import com.wutj.tool.route.consumer.IEventMessage;
import com.wutj.tool.route.model.IRouter;
import com.wutj.tool.route.recovery.IRecoveryTaskHandler;
import com.wutj.tool.route.recovery.TaskType;
import com.wutj.tool.route.strategy.RecoveryIntervalStrategy;
import com.wutj.tool.route.strategy.RecoveryStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 路由配置模板.
 *
 * @author wutingjia
 */
public class DefaultRouterTemplate implements IRouterTemplate<IEventMessage<EventMsgType>, IRouter>, InitializingBean {

	private static final Logger log = LoggerFactory.getLogger(DefaultRouterTemplate.class);

	/**
	 * 默认路由
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
	 * 该模板名字
	 */
	private String name;

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
	private final IRecoveryTaskHandler<EventMsgType> recoveryTaskHandler;

	/**
	 * 是否被锁定，如果被锁定则在此期间路由无法被修改
	 */
	private final AtomicBoolean lock = new AtomicBoolean(false);

	public DefaultRouterTemplate(String name, IRecoveryTaskHandler<EventMsgType> recoveryTaskHandler) {
		this.name = name;
		this.recoveryTaskHandler = recoveryTaskHandler;
	}

	public String getName() {
		return StringUtils.isEmpty(name) ? "default" : name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public IRouter getRouter() {
		return this.router;
	}

	@Override
	public void setRouter(IRouter router) {
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

	@Override
	public RecoveryIntervalStrategy getRecoveryIntervalStrategy() {
		return this.recoveryIntervalStrategy;
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
		map.put(DRParam.TYPE, TaskType.ROUTER);
		this.recoveryTaskHandler.registerTask(this, map);
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

	@Override
	public Boolean getLock() {
		return lock.get();
	}

	@Override
	public void setLock(boolean isLock) {
		this.lock.set(isLock);
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
}
