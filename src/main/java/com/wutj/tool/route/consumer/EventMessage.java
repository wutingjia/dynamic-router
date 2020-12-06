package com.wutj.tool.route.consumer;

import com.wutj.tool.route.constant.EventMsgType;
import com.wutj.tool.route.model.IRouter;

import java.util.StringJoiner;

/**
 * 触发信息实体类
 */
public class EventMessage {

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

    public EventMsgType getMsgType() {
        return msgType;
    }

    public void setMsgType(EventMsgType msgType) {
        this.msgType = msgType;
    }

    public String getEventInfo() {
        return eventInfo;
    }

    public void setEventInfo(String eventInfo) {
        this.eventInfo = eventInfo;
    }

    public IRouter getRouter() {
        return router;
    }

    public void setRouter(IRouter router) {
        this.router = router;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", EventMessage.class.getSimpleName() + "[", "]")
                .add("msgType=" + msgType.name())
                .add("eventInfo='" + eventInfo + "'")
                .add("router=" + router.getName())
                .toString();
    }
}
