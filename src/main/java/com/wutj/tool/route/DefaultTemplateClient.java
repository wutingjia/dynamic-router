package com.wutj.tool.route;

import com.wutj.tool.route.model.IRouter;
import org.springframework.stereotype.Component;

@Component
public class DefaultTemplateClient implements ITemplateClient{

	private final DefaultRouterTemplate template;

	public DefaultTemplateClient(DefaultRouterTemplate template) {
		this.template = template;
	}

	@Override
	public IRouter getCurrentRouter() {
		return template.getRouter();
	}

	@Override
	public void setCurrentRouter(IRouter router) {
		template.setRouter(router);
	}
}
