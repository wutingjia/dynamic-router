package com.wutj.tool.route.web;

import com.wutj.tool.route.DefaultRouterTemplate;
import com.wutj.tool.route.ITemplateClient;
import com.wutj.tool.route.constant.EventMsgType;
import com.wutj.tool.route.consumer.DefaultEventMessage;
import com.wutj.tool.route.consumer.IMessageConsumer;
import com.wutj.tool.route.model.BasicRouter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MockReceiveMessageTest {

	private static final Logger log = LoggerFactory.getLogger(MockReceiveMessageTest.class);

	@Autowired
	private IMessageConsumer<DefaultEventMessage> consumer;

	@Autowired
	private DefaultRouterTemplate template;

	@Autowired
	private ITemplateClient client;

	/**
	 * 模拟触发信息
	 */
	@PostMapping("/msg")
	public void msg(){
		DefaultEventMessage msg = new DefaultEventMessage();
		msg.setMsgType(EventMsgType.DEFAULT);
		msg.setEventInfo("11");
		msg.setRouter(template.getRouter());
		//msg.setRouter(new BasicRouter("firstRouter"));
		consumer.consume(msg);

		Thread t = new Thread(() -> {
			while (true) {
				System.out.println("当前路由为" + template.getRouter().getName());
				try {
					Thread.sleep(15000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		});
		t.start();
		System.out.println(1);
	}

	@PostMapping("/modify")
	public void modify(@RequestBody String routerName){
		log.info("后管触发路由修改，redis已更新");

		client.setCurrentRouter(new BasicRouter(routerName));
	}
}
