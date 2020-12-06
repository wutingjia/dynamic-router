package com.wutj.tool.route.consumer;

import com.wutj.tool.route.constant.EventMsgType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 默认队列容器.
 *
 * @author wutingjia
 */

public class DefaultQueueContainer implements IQueueContainer{

	private final Map<EventMsgType,ArrayBlockingQueue<EventMessage>> map = new HashMap<>();

	/**
	 * 初始化所有队列
	 */
	public DefaultQueueContainer() {
		for (EventMsgType type : EventMsgType.values()) {
			this.map.put(type, new ArrayBlockingQueue<>(16, true));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayBlockingQueue<EventMessage> getQueueByType(EventMsgType type) {
		return this.map.get(type);
	}
}
