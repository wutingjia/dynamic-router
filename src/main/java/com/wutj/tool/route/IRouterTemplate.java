package com.wutj.tool.route;

import com.wutj.tool.route.model.IRouter;
import com.wutj.tool.route.recovery.TaskType;
import com.wutj.tool.route.strategy.RecoveryIntervalStrategy;

/**
 * 路由配置模版接口
 *
 * @author wutingjia
 */
public interface IRouterTemplate {

    /**
     * @return 获取当前所使用的路由
     */
    IRouter getRouter();

    /**
     * 触发路由切换
     *
     * @param nextRouter 需要切换的下一个路由
     * @param msgRouter  触发信息中的路由
     */
    void invokeSwitch(IRouter nextRouter, IRouter msgRouter);

    /**
     * 触发恢复任务
     *
     * @param type 任务类型
     */
    void invokeRecovery(TaskType type);

    /**
     * 设置当前路由
     *
     * @param router 需要设置的路由
     */
    void setRouter(IRouter router);
}
