package com.wutj.tool.route.listener;

import com.wutj.tool.route.RouterContext;

/**
 * 监听器接口.
 *
 * @author wutingjia
 */
public interface IDeciderListener {

	void beforeDecider(RouterContext context);

	void afterDecider(RouterContext context);

	void afterSwitch(RouterContext context);
}
