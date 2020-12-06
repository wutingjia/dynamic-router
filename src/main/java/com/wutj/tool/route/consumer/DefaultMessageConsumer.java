package com.wutj.tool.route.consumer;

import com.wutj.tool.route.constant.EventMsgType;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * 信息消费类.
 *
 * @author wutingjia
 */

public class DefaultMessageConsumer implements IMessageConsumer {

	private final IQueueContainer queueContainer;

	public DefaultMessageConsumer(IQueueContainer queueContainer) {
		this.queueContainer = queueContainer;
	}

	@Override
	public void consume(EventMessage msg) {
		ArrayBlockingQueue<EventMessage> queue = queueContainer.getQueueByType(msg.getMsgType());
		queue.add(msg);
	}
}
