package com.wutj.tool.route.recovery;

import org.springframework.lang.NonNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
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

    @Override
    public String toString() {
        return new StringJoiner(", ", RecoveryTask.class.getSimpleName() + "[", "]")
                .add("type=" + this.type.name())
                .add("time=" + LocalDateTime.ofInstant(Instant.ofEpochMilli(this.time), ZoneId.systemDefault()))
                .toString();
    }
}
