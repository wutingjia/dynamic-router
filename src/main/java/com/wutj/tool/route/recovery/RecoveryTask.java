package com.wutj.tool.route.recovery;

import com.wutj.tool.route.IRouterTemplate;
import com.wutj.tool.route.constant.EventMsgType;
import com.wutj.tool.route.consumer.IEventMessage;
import com.wutj.tool.route.model.IRouter;
import org.springframework.lang.NonNull;

import java.util.StringJoiner;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 路由恢复任务.
 *
 * @author wutingjia
 */
public class RecoveryTask implements Delayed {

	/**
	 * 任务类型
	 */
	private TaskType type;

	/**
	 * 触发的时间,单位毫秒
	 */
	private long time;

	/**
	 * 需要执行恢复任务的路由模板
	 */
	private IRouterTemplate<IEventMessage<EventMsgType>, IRouter> template;


	@Override
	public long getDelay(@NonNull TimeUnit unit) {
		return time - System.currentTimeMillis();
	}

	@Override
	public int compareTo(Delayed o) {
		long l = this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS);
		if (l > 0) {
			return 1;
		}else if (l < 0) {
			return -1;
		}else {
			return 0;
		}
	}

	public TaskType getType() {
		return type;
	}

	public void setType(TaskType type) {
		this.type = type;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public IRouterTemplate<IEventMessage<EventMsgType>, IRouter> getTemplate() {
		return template;
	}

	public void setTemplate(IRouterTemplate<IEventMessage<EventMsgType>, IRouter> template) {
		this.template = template;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", RecoveryTask.class.getSimpleName() + "[", "]")
				.add("time=" + time)
				.add("template=" + template)
				.toString();
	}
}
