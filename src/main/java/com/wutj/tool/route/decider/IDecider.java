package com.wutj.tool.route.decider;

import com.wutj.tool.route.consumer.EventMessage;
import com.wutj.tool.route.model.IRouter;

import java.util.List;

/**
 * 决定器，由决定器来具体决定该切换成什么样的路由.
 * @author wutingjia
 */
public interface IDecider {

	/**
	 * 决定该切换成哪个路由.
	 * @param msg 触发信息
	 * @return 切换后的路由,如果返回null代表不切换路由
	 */
	IRouter decide(EventMessage msg);
}
