package com.wutj.tool.route.consumer;


/**
 * 信息消费接口,实现该接口然后在任何消息接受器中调用，例如kafka consumer，rabbitmq consumer.
 * @param <T> 消息实体类型
 *
 * @author wutingjia
 */
public interface IMessageConsumer<T> {

	/**
	 * 消费信息
	 * @param msg 信息
	 */
	void consume(T msg);
}


