package com.wutj.tool.route.decider;

import com.wutj.tool.route.AbstractDecider;
import com.wutj.tool.route.constant.EventMsgType;
import com.wutj.tool.route.consumer.IQueueContainer;
import com.wutj.tool.route.model.BasicRouter;
import com.wutj.tool.route.consumer.IEventMessage;
import com.wutj.tool.route.model.IRouter;

/**
 * decider Test.
 *
 * @author wutingjia
 */
public class MyDecider extends AbstractDecider<EventMsgType> {

	public MyDecider(EventMsgType listenType, IQueueContainer<EventMsgType> container) {
		super(listenType, container);
	}

	@Override
	protected IRouter doDecide(IEventMessage<EventMsgType> msg) {

		if ("money".equals(String.valueOf(msg.getMsgType()))) {
			return new BasicRouter("secondRouter");
		}
		if ("failed".equals(String.valueOf(msg.getMsgType()))) {
			return new BasicRouter("thirdRouter");
		}

		return new BasicRouter("defaultRouter");
	}

	@Override
	public boolean allowSwitch(IEventMessage<EventMsgType> msg) {
		return true;
	}
}
