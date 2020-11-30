package com.wutj.tool.route.constant;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DefaultEventMsgTypeHolder implements IEventMsgTypeHolder<EventMsgType>{

	@Override
	public Set<EventMsgType> getAllTypes() {
		return new HashSet<>(Arrays.asList(EventMsgType.values()));
	}

	@Override
	public int getTypeAmount() {
		return EventMsgType.values().length;
	}
}
