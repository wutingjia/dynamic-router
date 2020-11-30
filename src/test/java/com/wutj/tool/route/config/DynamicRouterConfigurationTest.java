package com.wutj.tool.route.config;

import com.wutj.tool.route.AbstractDecider;
import com.wutj.tool.route.DecidersHolder;
import com.wutj.tool.route.RouterContext;
import com.wutj.tool.route.constant.EventMsgType;
import com.wutj.tool.route.consumer.IQueueContainer;
import com.wutj.tool.route.decider.MyDecider;
import com.wutj.tool.route.listener.IDeciderListener;
import com.wutj.tool.route.model.BasicRouter;
import com.wutj.tool.route.model.IRouter;
import com.wutj.tool.route.strategy.RouterStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 模拟配置.
 *
 * @author wutingjia
 */
@Configuration
public class DynamicRouterConfigurationTest {

	private static final Logger log = LoggerFactory.getLogger(DynamicRouterConfigurationTest.class);

	@Autowired
	IQueueContainer<EventMsgType> container;

	@Bean
	public MyDecider decider() {
		MyDecider decider = new MyDecider(EventMsgType.DEFAULT, container);

		decider.setStrategy(RouterStrategy.LINEAR);
		decider.setListeners(new ArrayList<IDeciderListener>(){{add(deciderListener());}});
		List<IRouter> routers = new ArrayList<>();
		IRouter router1 = new BasicRouter("firstRouter");
		IRouter router2 = new BasicRouter("secondRouter");
		IRouter router3 = new BasicRouter("thirdRouter");
		routers.add(router1);
		routers.add(router2);
		routers.add(router3);
		decider.setRouters(routers);
		return decider;
	}

	@Bean
	public DecidersHolder<EventMsgType> holder() {
		return () -> new LinkedList<AbstractDecider<EventMsgType>>(){{add(decider());}};
	}

	@Bean
	public IDeciderListener deciderListener() {

		return new IDeciderListener() {
			@Override
			public void beforeDecider(RouterContext context) {
				log.info("在" + context.getDecider().getName() + "之前进行通知");
			}

			@Override
			public void afterDecider(RouterContext context) {
				log.info("在" + context.getDecider().getName() + "之后进行通知");
			}

			@Override
			public void afterSwitch(RouterContext context) {
				log.info("在" + context.getDecider().getName() + "切换时进行通知");
			}
		};
	}



}