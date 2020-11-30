package com.wutj.tool.route.config;

import com.wutj.tool.route.strategy.RecoveryIntervalStrategy;
import com.wutj.tool.route.strategy.RecoveryStrategy;

public class TemplateConfig {

	/**
	 * 恢复路由的时长.
	 */
	private String period;

	/**
	 * 恢复策略
	 */
	private RecoveryStrategy recovery;

	/**
	 * 间隔策略
	 */
	private RecoveryIntervalStrategy interval;

	/**
	 * 初始路由
	 */
	private String router;

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public RecoveryStrategy getRecovery() {
		return recovery;
	}

	public void setRecovery(RecoveryStrategy recovery) {
		this.recovery = recovery;
	}

	public RecoveryIntervalStrategy getInterval() {
		return interval;
	}

	public void setInterval(RecoveryIntervalStrategy interval) {
		this.interval = interval;
	}

	public String getRouter() {
		return router;
	}

	public void setRouter(String router) {
		this.router = router;
	}
}
