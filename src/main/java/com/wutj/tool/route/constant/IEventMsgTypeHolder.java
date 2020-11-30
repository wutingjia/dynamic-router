package com.wutj.tool.route.constant;

import java.util.Set;

/**
 * EventMsgType存放容器
 * @param <T> 类型
 */
public interface IEventMsgTypeHolder<T> {

	/**
	 * @return 所有类型枚举值
	 */
	Set<T> getAllTypes();

	/**
	 * @return 类型个数
	 */
	int getTypeAmount();
}
