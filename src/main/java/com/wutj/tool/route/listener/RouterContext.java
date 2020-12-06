package com.wutj.tool.route.listener;

import com.wutj.tool.route.consumer.EventMessage;
import com.wutj.tool.route.decider.AbstractDecider;
import com.wutj.tool.route.model.IRouter;

/**
 * 路由切换时的上下文.
 *
 * @author wutingjia
 */
public class RouterContext {

	/**
	 * 当前使用的selector
	 */
	private AbstractDecider decider;

	/**
	 * 触发信息
	 */
	private EventMessage msg;

	/**
	 * 当前路由
	 */
	private IRouter router;

	/**
	 * 附加信息
	 */
	private String message;

	public AbstractDecider getDecider() {
		return decider;
	}

	public void setDecider(AbstractDecider decider) {
		this.decider = decider;
	}

	public EventMessage getMsg() {
		return msg;
	}

	public void setMsg(EventMessage msg) {
		this.msg = msg;
	}

	public IRouter getRouter() {
		return router;
	}

	public void setRouter(IRouter router) {
		this.router = router;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}

