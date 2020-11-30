package com.wutj.tool.route;

import com.wutj.tool.route.model.IRouter;
import com.wutj.tool.route.recovery.TaskType;
import com.wutj.tool.route.strategy.RecoveryIntervalStrategy;

/**
 * 路由配置模版接口，动态路由操作的顶层接口.
 * @param <I> 路由切换触发信息
 * @param <O> 路由实体
 *
 * @author wutingjia
 */
public interface IRouterTemplate<I, O> {

	/**
	 * @return 获取当前所使用的路由
	 */
	O getRouter();

	/**
	 * 触发路由切换
	 * @param nextRouter 需要切换的下一个路由
	 * @param msgRouter 触发信息中的路由
	 */
	void invokeSwitch(IRouter nextRouter, IRouter msgRouter);

	/**
	 * 触发恢复任务
	 * @param type 任务类型
	 */
	void invokeRecovery(TaskType type);

	/**
	 * 设置当前路由
	 * @param router 需要设置的路由
	 */
	void setRouter(O router);

	/**
	 * 获取当前锁状态
	 * @return true 已锁;false 未锁
	 */
	Boolean getLock();

	/**
	 * 加/解锁模板
	 * @param isLock 是否锁定
	 */
	void setLock(boolean isLock);

	/**
	 * @return 获取RecoveryIntervalStrategy策略
	 */
	RecoveryIntervalStrategy getRecoveryIntervalStrategy();
}
