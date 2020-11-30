package com.wutj.tool.route.recovery;

import com.wutj.tool.route.IRouterTemplate;
import com.wutj.tool.route.constant.DRParam;
import com.wutj.tool.route.constant.EventMsgType;
import com.wutj.tool.route.consumer.IEventMessage;
import com.wutj.tool.route.model.IRouter;
import com.wutj.tool.route.strategy.RecoveryIntervalStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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
@Component
public class ScheduledRecoveryTaskHandler implements IRecoveryTaskHandler<EventMsgType> {

	private static final Logger log = LoggerFactory.getLogger(ScheduledRecoveryTaskHandler.class);

	private final DelayQueue<RecoveryTask> queue = new DelayQueue<>();

	@Override
	public void registerTask(IRouterTemplate<IEventMessage<EventMsgType>, IRouter> template, Map<DRParam, Object> param) {
		RecoveryTask recoveryTask = new RecoveryTask();
		if (TaskType.UNLOCK == param.get(DRParam.TYPE)) {
			recoveryTask.setType(TaskType.UNLOCK);
			LocalDateTime time = LocalDateTime.now().plusMinutes((long)param.get(DRParam.LOCKTIME));
			recoveryTask.setTime(time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
			recoveryTask.setTemplate(template);
			log.info("添加解锁任务" + recoveryTask.toString() + "任务启动时间:" + time.toString());
			queue.add(recoveryTask);
		}else if (TaskType.ROUTER == param.get(DRParam.TYPE)) {
			recoveryTask.setType(TaskType.ROUTER);
			RecoveryIntervalStrategy intervalStrategy = template.getRecoveryIntervalStrategy();
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
				recoveryTask.setTemplate(template);
				log.info("添加恢复任务" + recoveryTask.toString() + "任务启动时间:" + date.toString());
				queue.add(recoveryTask);
			}else if (RecoveryIntervalStrategy.DAY == intervalStrategy || RecoveryIntervalStrategy.HOUR == intervalStrategy
					|| RecoveryIntervalStrategy.MINUTE == intervalStrategy) {
				LocalDateTime time = null;
				int interval = Integer.parseInt((String) param.get(DRParam.PERIOD));
				switch (intervalStrategy) {
					case MINUTE: time = LocalDateTime.now().plusMinutes(interval);break;
					case HOUR: time = LocalDateTime.now().plusHours(interval);break;
					case DAY: time = LocalDateTime.now().plusDays(interval);break;
				}

				recoveryTask.setTemplate(template);
				recoveryTask.setTime(time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
				log.info("添加恢复任务" + recoveryTask.toString() + "任务启动时间:" + time.toString());
				queue.add(recoveryTask);
			}
		}
	}

	@Override
	@PostConstruct
	public void invokeRecovery() {

		Thread t = new Thread(() -> {
			log.info("动态路由恢复队列线程已就绪");
			while (true) {
				RecoveryTask recoveryTask;
				try {
					recoveryTask = queue.take();
				} catch (InterruptedException e) {
					log.error("从恢复队列中获取任务异常", e);
					continue;
				}
				IRouterTemplate<IEventMessage<EventMsgType>, IRouter> template = recoveryTask.getTemplate();
				template.invokeRecovery(recoveryTask.getType());
			}
		});
		t.start();
	}
}
