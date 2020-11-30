package com.wutj.tool.route.strategy;

/**
 * 路由恢复时间间隔策略.
 *
 * @author wutingjia
 */
public enum RecoveryIntervalStrategy {

	/**
	 * 下一天
	 */
	NEXTDAY,

	/**
	 * 下一周
	 */
	NEXTWEEK,

	/**
	 * 下一个月
	 */
	NEXTMONTH,

	/**
	 *下一年
	 */
	NEXTYEAR,

	/**
	 * 自切换以后一定分钟以后恢复，可配置
	 */
	MINUTE,

	/**
	 * 自切换以后一定小时以后恢复，可配置
	 */
	HOUR,

	/**
	 * 自切换以后一定天数以后恢复，可配置
	 */
	DAY
}
