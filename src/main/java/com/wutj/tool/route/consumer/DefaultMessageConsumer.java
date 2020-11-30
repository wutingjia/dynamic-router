package com.wutj.tool.route.consumer;

import com.wutj.tool.route.constant.EventMsgType;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * 信息消费类.
 *
 * @author wutingjia
 */

public class DefaultMessageConsumer implements IMessageConsumer<DefaultEventMessage> {

	private final IQueueContainer<EventMsgType> queueContainer;

	public DefaultMessageConsumer(IQueueContainer<EventMsgType> queueContainer) {
		this.queueContainer = queueContainer;
	}

	@Override
	public void consume(DefaultEventMessage msg) {
		ArrayBlockingQueue<IEventMessage<EventMsgType>> queue = queueContainer.getQueueByType(msg.getMsgType());
		queue.add(msg);
	}
}
