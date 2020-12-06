package com.wutj.tool.route.consumer;

import com.wutj.tool.route.constant.EventMsgType;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * 放置队列的容器.
 *
 * @author wutingjia
 */
public interface IQueueContainer {

	/**
	 * 根据消息类型获取响应的消息队列
	 * @param type 信息类型
	 * @return 消息队列
	 */
	ArrayBlockingQueue<EventMessage> getQueueByType(EventMsgType type);
}
