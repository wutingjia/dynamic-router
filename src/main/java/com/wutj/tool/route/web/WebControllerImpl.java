package com.wutj.tool.route.web;

import com.wutj.tool.route.AbstractDecider;
import com.wutj.tool.route.DecidersHolder;
import com.wutj.tool.route.IRouterTemplate;
import com.wutj.tool.route.constant.DRParam;
import com.wutj.tool.route.constant.EventMsgType;
import com.wutj.tool.route.model.BasicRouter;
import com.wutj.tool.route.consumer.IEventMessage;
import com.wutj.tool.route.model.IRouter;
import com.wutj.tool.route.recovery.IRecoveryTaskHandler;
import com.wutj.tool.route.recovery.TaskType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class WebControllerImpl implements IWebController {

	private static final Logger log = LoggerFactory.getLogger(WebControllerImpl.class);

	private final IRecoveryTaskHandler<EventMsgType> handler;

	private final IRouterTemplate<IEventMessage<EventMsgType>, IRouter> template;

	private final DecidersHolder<EventMsgType> decidersHolder;

	public WebControllerImpl(IRouterTemplate<IEventMessage<EventMsgType>, IRouter> template, IRecoveryTaskHandler<EventMsgType> handler, DecidersHolder<EventMsgType> decidersHolder) {
		this.template = template;
		this.handler = handler;
		this.decidersHolder = decidersHolder;
	}

	@Override
	public boolean modifyRouter(@RequestBody Request request) {

		if (template.getLock()) {
			log.warn("路由模板已经被锁定，请先解锁再进行修改操作");
			return false;
		}
		log.info("强制切换路由");
		String routerName = request.getRouterName();
		IRouter router = new BasicRouter(routerName);
		template.setRouter(router);
		Long lockTime = request.getLockPeriod();
		if (lockTime != null) {
			try {
				log.info("路由模板已上锁");
				template.setLock(true);
				if (lockTime >= 0) {
					Map<DRParam,Object> map = new HashMap<>();
					map.put(DRParam.TYPE, TaskType.UNLOCK);
					map.put(DRParam.LOCKTIME, lockTime);
					//注册一个解锁任务
					handler.registerTask(template, map);
				}
			} catch (Exception e) {
				template.setLock(false);
				log.info("路由模板已解锁");
				log.error("锁定路由模板时出现错误", e);

				return false;
			}
		}
		return true;
	}

	@Override
	public boolean unlockTemplate() {
		template.setLock(false);
		return true;
	}

	@Override
	public boolean setRouters(Request request) {
		Map<String, List<String>> routers = request.getRouters();
		for (AbstractDecider<EventMsgType> decider : decidersHolder.getDeciders()) {

			for (Map.Entry<String, List<String>> router : routers.entrySet()) {
				if (router.getKey().equals(decider.getName())) {
					List<IRouter> list = new ArrayList<>();
					List<String> nameList = router.getValue();
					for (String str : nameList) {
						list.add(new BasicRouter(str));
					}
					decider.setRouters(list);
				}
			}
		}
		return true;
	}
}
