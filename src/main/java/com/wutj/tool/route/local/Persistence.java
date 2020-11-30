package com.wutj.tool.route.local;

/**
 * 持久化接口
 * @param <T> 需要持久化的信息
 */
public interface Persistence<T> {

	void store(T t);
}
