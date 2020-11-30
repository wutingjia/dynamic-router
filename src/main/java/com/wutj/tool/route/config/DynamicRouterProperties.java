package com.wutj.tool.route.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "dr")
@Component
public class DynamicRouterProperties {

	private TemplateConfig template;

	public TemplateConfig getTemplate() {
		return template;
	}

	public void setTemplate(TemplateConfig template) {
		this.template = template;
	}
}
