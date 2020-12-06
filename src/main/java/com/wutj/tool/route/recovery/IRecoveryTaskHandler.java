package com.wutj.tool.route.recovery;

import com.wutj.tool.route.constant.DRParam;

import java.util.Map;

/**
 * 恢复任务执行器接口.
 *
 * @author wutingjia
 */
public interface IRecoveryTaskHandler {

    /**
     * 注册恢复任务
     * @param param
     */
	void registerTask(TaskType type, Map<DRParam, Object> param);

}
