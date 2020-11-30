package com.wutj.tool.route;

import com.wutj.tool.route.model.IRouter;

/**
 * 用户使用的操作接口
 */
public interface ITemplateClient {

	IRouter getCurrentRouter();

	void setCurrentRouter(IRouter router);
}
