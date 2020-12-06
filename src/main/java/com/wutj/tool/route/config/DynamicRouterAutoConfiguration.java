package com.wutj.tool.route.config;

import com.wutj.tool.route.DefaultRouterTemplate;
import com.wutj.tool.route.IRouterTemplate;
import com.wutj.tool.route.constant.EventMsgType;
import com.wutj.tool.route.consumer.*;
import com.wutj.tool.route.model.BasicRouter;
import com.wutj.tool.route.recovery.IRecoveryTaskHandler;
import com.wutj.tool.route.recovery.RecoveryTask;
import com.wutj.tool.route.recovery.ScheduledRecoveryTaskHandler;
import com.wutj.tool.route.strategy.RecoveryIntervalStrategy;
import com.wutj.tool.route.strategy.RecoveryStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.DelayQueue;

/**
 * 动态路由自动配置.
 *
 * @author wutingjia
 */
@Configuration
public class DynamicRouterAutoConfiguration {

	private static final Logger log = LoggerFactory.getLogger(DynamicRouterAutoConfiguration.class);

	private final DynamicRouterProperties properties;

	public DynamicRouterAutoConfiguration(DynamicRouterProperties properties) {
		this.properties = properties;
	}

	/**
	 * @return 装配一个默认的模板，配置了RecoveryStrategy,RecoveryIntervalStrategy,
	 * Period,Router，DefaultRouter
	 */
	@Bean
	@ConditionalOnMissingBean(IRouterTemplate.class)
	public DefaultRouterTemplate template() {

		DefaultRouterTemplate template = new DefaultRouterTemplate(taskHandler(), delayQueue());
		TemplateConfig config = properties.getTemplate();
		if (config.getRecovery() == null) {
			log.warn("未配置路由恢复策略,使用默认恢复路由first");
			template.setRecoveryStrategy(RecoveryStrategy.DEFAULT);
		}else {
			template.setRecoveryStrategy(config.getRecovery());
		}

		if (config.getInterval() == null) {
			log.warn("未配置路由恢复间隔策略,使用默认恢复策略nextDay");
			template.setRecoveryIntervalStrategy(RecoveryIntervalStrategy.NEXTDAY);
		}else {
			template.setRecoveryIntervalStrategy(config.getInterval());
			if (config.getInterval() == RecoveryIntervalStrategy.MINUTE || config.getInterval() == RecoveryIntervalStrategy.HOUR
					|| config.getInterval() == RecoveryIntervalStrategy.DAY) {
				if (config.getPeriod() == null) {
					log.warn("未配置路由恢复间隔数值");
					throw new IllegalArgumentException("未配置路由恢复间隔数值");
				}else {
					template.setPeriod(config.getPeriod());
				}
			}
		}
		template.setRouter(new BasicRouter(config.getRouter()));
		template.setDefaultRouter(new BasicRouter(config.getRouter()));
		return template;
	}

	/**
	 * @return 一个默认的MessageConsumer
	 */
	@Bean
	@ConditionalOnMissingBean(IMessageConsumer.class)
	public IMessageConsumer messageConsumer() {
		return new DefaultMessageConsumer(queueContainer());
	}

	/**
	 * @return 一个默认的队列容器
	 */
	@Bean
	@ConditionalOnMissingBean(IQueueContainer.class)
	public IQueueContainer queueContainer() {
		return new DefaultQueueContainer();
	}

	@Bean
    public DelayQueue<RecoveryTask> delayQueue() {
	    return new DelayQueue<>();
    }

    @Bean
    public IRecoveryTaskHandler taskHandler() {
	    return new ScheduledRecoveryTaskHandler(delayQueue());
    }
}
