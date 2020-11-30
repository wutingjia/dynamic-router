package com.wutj.tool.route;

import com.wutj.tool.route.constant.EventMsgType;
import com.wutj.tool.route.consumer.IEventMessage;
import com.wutj.tool.route.consumer.IQueueContainer;
import com.wutj.tool.route.listener.IDeciderListener;
import com.wutj.tool.route.model.IRouter;
import com.wutj.tool.route.strategy.RouterStrategy;
import com.wutj.tool.route.util.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 选择器抽象类,开发具体的Decider时建议直接继承这个类进行开发.
 *
 * @author wutingjia
 */
public abstract class AbstractDecider<T> implements IDecider<IEventMessage<T>, IRouter> {

	private static final Logger log = LoggerFactory.getLogger(AbstractDecider.class);

	private final Random random  = new Random();

	/**
	 * 选择器名字
	 */
	protected String name;

	/**
	 * 是否启用该选择器
	 */
	protected boolean enable = true;

	/**
	 * 这个decider中可选的路由
	 */
	protected List<IRouter> routers = new LinkedList<>();

	/**
	 * 监听器
	 */
	protected List<IDeciderListener> listeners = new ArrayList<>();

	/**
	 * 路由策略，默认为线性
	 */
	protected RouterStrategy strategy = RouterStrategy.LINEAR;

	/**
	 * 监听的事件类型
	 */
	protected T listenType;

	/**
	n * 消息事件队列
	 */
	private final ArrayBlockingQueue<IEventMessage<T>> queue;

	private final IRouterTemplate<IEventMessage<EventMsgType>, IRouter> template;

	public AbstractDecider(T listenType, IQueueContainer<T> container) {
		this.listenType = listenType;
		this.queue = container.getQueueByType(listenType);
		this.template = BeanUtil.getTemplate();
	}

	@PostConstruct
	public void start() {
		Thread thread = new Thread(() -> {
			try {
				while (true) {
					IEventMessage<T> msg = queue.take();
					template.invokeSwitch(decide(msg), msg.getRouter());
				}
			} catch (InterruptedException e) {
				log.error("decider从消息队列中获取信息异常", e);
			}
		});
		thread.start();
	}

	/**
	 * 根据事件触发信息来决定使用的路由
	 * @param msg 触发信息
	 * @return 切换为的路由
	 */
	@Override
	public IRouter decide(IEventMessage<T> msg) {

		if (!allowSwitch(msg)) {
			log.info("因allowSwitch限制不允许限制");
			return null;
		}

		int index = routers.indexOf(msg.getRouter());

		notifyBeforeListener(this, msg);
		IRouter router = null;
		if (strategy == RouterStrategy.LINEAR) {
			if ((index + 1) < routers.size()) {
				return routers.get(index + 1);
			}else {
				log.error("当前路由策略为：线性，已使用了最后一个路由:" + msg.getRouter() + ",无法切换至下一个");
				return null;
			}
		}else if (strategy == RouterStrategy.RANDOM) {
			int newIndex = index;
			while (newIndex == index) {
				newIndex = random.nextInt(routers.size() - 1);
			}
			return routers.get(newIndex);
		}else if (strategy == RouterStrategy.APPOINT) {
			router = doDecide(msg);
		}

		notifyAfterListener(this, msg, router);
		if (router != null) {
			notifySwitchListener(this, msg, router);
		}
		return router;
	}

	/**
	 * 自定义判断逻辑
	 * @param msg 触发信息
	 * @return 切换的路由，如果为null代表不切换
	 */
	protected abstract IRouter doDecide(IEventMessage<T> msg);

	@Override
	public RouterStrategy getRouterStrategy() {
		return strategy;
	}

	/**
	 * 切换时的额外限制条件
	 * @param msg 触发信息
	 * @return true 允许，false不允许
	 */
	@Override
	public abstract boolean allowSwitch(IEventMessage<T> msg);

	@Override
	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	/**
	 * 添加Decider的监听器.
	 */
	public void addListener(IDeciderListener listener){
		this.listeners.add(listener);
	}

	public List<IDeciderListener> getListeners() {
		return listeners;
	}

	public void setListeners(List<IDeciderListener> listeners) {
		this.listeners = listeners;
	}

	@Override
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public List<IRouter> getRouters() {
		return routers;
	}

	@Override
	public void setRouters(List<IRouter> IRouters) {
		this.routers = IRouters;
	}

	public RouterStrategy getStrategy() {
		return strategy;
	}

	public void setStrategy(RouterStrategy strategy) {
		this.strategy = strategy;
	}

	private void notifyBeforeListener(AbstractDecider<T> decider, IEventMessage<T> msg) {
		RouterContext context = new RouterContext();
		context.setDecider(decider);
		context.setMsg(msg);
		for (IDeciderListener listener : this.listeners) {
			listener.beforeDecider(context);
		}
	}

	private void notifyAfterListener(AbstractDecider<T> decider, IEventMessage<T> msg, IRouter router) {
		RouterContext context = new RouterContext();
		context.setDecider(decider);
		context.setMsg(msg);
		context.setRouter(router);
		for (IDeciderListener listener : this.listeners) {
			listener.afterDecider(context);
		}
	}

	private void notifySwitchListener(AbstractDecider<T> decider, IEventMessage<T> msg, IRouter router) {
		RouterContext context = new RouterContext();
		context.setDecider(decider);
		context.setMsg(msg);
		context.setRouter(router);
		for (IDeciderListener listener : this.listeners) {
			listener.afterSwitch(context);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AbstractDecider<T> decider = (AbstractDecider<T>) o;
		return name.equals(decider.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
}
