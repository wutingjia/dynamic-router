package com.wutj.tool.route.strategy;

import com.wutj.tool.route.decider.IDecider;

/**
 * 路由策略,{@link IDecider}使用.
 *
 * @author wutingjia
 */
public enum RouterStrategy {

	/**
	 * 线性路由，当上一个路由失效时选择下一个.
	 */
	LINEAR,

	/**
	 * 随机路由，从剩下的路由中随机选择一个.
	 */
	RANDOM,

	/**
	 * 指定路由，指定一个路由.
	 */
	APPOINT
}
