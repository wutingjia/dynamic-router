package com.wutj.tool.route.consumer;

import com.wutj.tool.route.model.IRouter;

/**
 * 接收消息的实体所要实现的接口.
 * @param <T> 信息类型
 */
public interface IEventMessage<T> {

	/**
	 * 用于表明这个信息是属于哪一种类型的事件，暂未实用
	 * @return 消息类型
	 */
	T getMsgType();

	/**
	 * @return 返回这条具体信息
	 */
	String getEventInfo();

	/**
	 * @return 这条信息是由哪个路由生效时所发出的
	 */
	IRouter getRouter();
}
