package com.wutj.tool.route;

import com.wutj.tool.route.model.IRouter;
import com.wutj.tool.route.strategy.RouterStrategy;

import java.util.List;

/**
 * 决定器，由决定器来具体决定该切换成什么样的路由.
 * @param <I> 
 * @param <O>
 * @author wutingjia
 */
public interface IDecider<I, O> {

	/**
	 * @return decider的唯一ID
	 */
	String getName();

	/**
	 * 决定该切换成哪个路由.
	 * @param msg 触发信息
	 * @return 要切换成的理由,如果返回null代表不切换路由
	 */
	O decide(I msg);

	/**
	 * @return decider当前路由切换策略
	 */
	RouterStrategy getRouterStrategy();

	/**
	 * @return 这个decider中所有可以选择的路由
	 */
	List<O> getRouters();

	/**
	 * 额外自定义检查项，是否允许切换
	 * @param msg 触发信息
	 * @return true 允许切换；false 不允许切换
	 */
	boolean allowSwitch(I msg);

	/**
	 * 是否启用该decider
	 * @return true 启用; false 不启用
	 */
	boolean isEnable();

	void setRouters(List<IRouter> IRouters);

}
