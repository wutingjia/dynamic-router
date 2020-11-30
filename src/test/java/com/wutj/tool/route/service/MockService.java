package com.wutj.tool.route.service;

import com.wutj.tool.route.ITemplateClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class MockService {

	private static final Logger log = LoggerFactory.getLogger(MockService.class);

	@Autowired
	ITemplateClient client;

	@PostConstruct
	public void mock() {


		Thread t = new Thread(() -> {
			while (true) {
				String name = client.getCurrentRouter().getName();

				if ("firstRouter".equals(name)) {
					log.info("发往路由firstRouter");
				}else if ("secondRouter".equals(name)) {
					log.info("发往路由secondRouter");
				}else if ("thirdRouter".equals(name)) {
					log.info("发往路由thirdRouter");
				}

				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		//t.start();
	}
}
