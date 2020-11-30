package com.wutj.tool.route.util;

import com.wutj.tool.route.IRouterTemplate;

public class BeanUtil {

	public static IRouterTemplate getTemplate() {
		return SpringContextUtil.getContext().getBean(IRouterTemplate.class);
	}
}
