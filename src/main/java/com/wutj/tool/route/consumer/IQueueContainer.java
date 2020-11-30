package com.wutj.tool.route.consumer;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * 放置队列的容器.
 * @param <T> 信息类型
 *
 * @author wutingjia
 */
public interface IQueueContainer<T> {

	/**
	 * 根据消息类型获取响应的消息队列
	 * @param type 信息类型
	 * @return 消息队列
	 */
	ArrayBlockingQueue<IEventMessage<T>> getQueueByType(T type);
}
