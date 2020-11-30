package com.wutj.tool.route.web;

import java.util.List;
import java.util.Map;

public class Request {

	/**
	 * 需要切换为的路由名
	 */
	private String routerName;

	/**
	 * 强制锁定期
	 */
	private Long lockPeriod;

	/**
	 * 候选路由
	 */
	private Map<String, List<String>> routers;

	public String getRouterName() {
		return routerName;
	}

	public void setRouterName(String routerName) {
		this.routerName = routerName;
	}

	public Long getLockPeriod() {
		return lockPeriod;
	}

	public void setLockPeriod(Long lockPeriod) {
		this.lockPeriod = lockPeriod;
	}

	public Map<String, List<String>> getRouters() {
		return routers;
	}

	public void setRouters(Map<String, List<String>> routers) {
		this.routers = routers;
	}
}
