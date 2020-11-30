package com.wutj.tool.route.consumer;

import com.wutj.tool.route.constant.EventMsgType;
import com.wutj.tool.route.model.IRouter;

/**
 * 触发信息实体类
 */
public class DefaultEventMessage implements IEventMessage<EventMsgType>{

	/**
	 * 触发事件类型
	 */
	private EventMsgType msgType;

	/**
	 * 附加信息
	 */
	private String eventInfo;

	/**
	 * 发出这条信息时的路由
	 */
	private IRouter router;

	public void setMsgType(EventMsgType msgType) {
		this.msgType = msgType;
	}

	public void setEventInfo(String eventInfo) {
		this.eventInfo = eventInfo;
	}

	public void setRouter(IRouter router) {
		this.router = router;
	}

	@Override
	public EventMsgType getMsgType() {
		return this.msgType;
	}

	@Override
	public String getEventInfo() {
		return this.eventInfo;
	}

	@Override
	public IRouter getRouter() {
		return this.router;
	}
}
