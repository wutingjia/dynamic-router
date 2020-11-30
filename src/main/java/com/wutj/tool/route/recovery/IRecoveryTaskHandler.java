package com.wutj.tool.route.recovery;

import com.wutj.tool.route.IRouterTemplate;
import com.wutj.tool.route.constant.DRParam;
import com.wutj.tool.route.consumer.IEventMessage;
import com.wutj.tool.route.model.IRouter;

import java.util.Map;

/**
 * 恢复任务执行器接口.
 *
 * @author wutingjia
 */
public interface IRecoveryTaskHandler<T> {

	/**
	 * 注册恢复任务
	 * @param template 路由模板
	 */
	void registerTask(IRouterTemplate<IEventMessage<T>, IRouter> template, Map<DRParam, Object> param);

	/**
	 * 触发恢复任务
	 */
	void invokeRecovery();
}
