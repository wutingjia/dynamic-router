package com.wutj.tool.route.model;

/**
 * router实体类.
 *
 * @author wutingjia
 */
public class BasicRouter implements IRouter {

	/**
	 * router的唯一标识
	 */
	private String name;

	public BasicRouter(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof IRouter)) {
			return false;
		}else {
			return getName().equals(((IRouter) obj).getName());
		}
	}
}
