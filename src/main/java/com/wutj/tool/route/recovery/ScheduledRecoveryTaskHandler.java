package com.wutj.tool.route.recovery;

import com.wutj.tool.route.constant.DRParam;
import com.wutj.tool.route.strategy.RecoveryIntervalStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.concurrent.DelayQueue;

/**
 * 恢复任务执行器，要保证线程安全.
 *
 * @author wutingjia
 */
public class ScheduledRecoveryTaskHandler implements IRecoveryTaskHandler {

	private static final Logger log = LoggerFactory.getLogger(ScheduledRecoveryTaskHandler.class);

	private final DelayQueue<RecoveryTask> queue;

    public ScheduledRecoveryTaskHandler(DelayQueue<RecoveryTask> queue) {
        this.queue = queue;
    }

    @Override
	public void registerTask(TaskType type, Map<DRParam, Object> param) {
		RecoveryTask recoveryTask = new RecoveryTask();
        recoveryTask.setType(type);
		if (TaskType.UNLOCK == type) {
			LocalDateTime time = LocalDateTime.now().plusMinutes((long)param.get(DRParam.PERIOD));
			recoveryTask.setTime(time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
			log.info("添加解锁任务" + recoveryTask.toString());
			queue.add(recoveryTask);
		}else if (TaskType.ROUTER == type) {
			RecoveryIntervalStrategy intervalStrategy = (RecoveryIntervalStrategy)param.get(DRParam.INTERVAL_STRATEGY);
			if (RecoveryIntervalStrategy.NEXTDAY == intervalStrategy || RecoveryIntervalStrategy.NEXTWEEK == intervalStrategy
					|| RecoveryIntervalStrategy.NEXTMONTH == intervalStrategy || RecoveryIntervalStrategy.NEXTYEAR == intervalStrategy) {
				LocalDate date = null;
				switch (intervalStrategy) {
					case NEXTDAY: date = LocalDate.now().plusDays(1);break;
					case NEXTWEEK: date = LocalDate.now().plusWeeks(1);break;
					case NEXTMONTH: date = LocalDate.now().plusMonths(1);break;
					case NEXTYEAR: date = LocalDate.now().plusYears(1);break;
				}

				recoveryTask.setTime(date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());
				log.info("添加恢复任务" + recoveryTask.toString() + "任务启动时间:" + date.toString());
				queue.add(recoveryTask);
			}else if (RecoveryIntervalStrategy.DAY == intervalStrategy || RecoveryIntervalStrategy.HOUR == intervalStrategy
					|| RecoveryIntervalStrategy.MINUTE == intervalStrategy) {
				LocalDateTime time = null;
				int interval = (int)param.get(DRParam.PERIOD);
				switch (intervalStrategy) {
					case MINUTE: time = LocalDateTime.now().plusMinutes(interval);break;
					case HOUR: time = LocalDateTime.now().plusHours(interval);break;
					case DAY: time = LocalDateTime.now().plusDays(interval);break;
				}
				recoveryTask.setTime(time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
				log.info("添加恢复任务" + recoveryTask.toString() + "任务启动时间:" + time.toString());
				queue.add(recoveryTask);
			}
		}
	}
}
