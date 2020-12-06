package com.wutj.tool.route.web;

import com.wutj.tool.route.DefaultRouterTemplate;
import com.wutj.tool.route.constant.DRParam;
import com.wutj.tool.route.decider.AbstractDecider;
import com.wutj.tool.route.decider.DecidersHolder;
import com.wutj.tool.route.model.BasicRouter;
import com.wutj.tool.route.model.IRouter;
import com.wutj.tool.route.recovery.IRecoveryTaskHandler;
import com.wutj.tool.route.recovery.TaskType;
import com.wutj.tool.route.util.SimpleRouterTransformer;
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

    private final SimpleRouterTransformer transformer;

	private static final Logger log = LoggerFactory.getLogger(WebControllerImpl.class);

	private final IRecoveryTaskHandler handler;

	private final DefaultRouterTemplate template;

	private final DecidersHolder decidersHolder;

	public WebControllerImpl(DefaultRouterTemplate template, IRecoveryTaskHandler handler, DecidersHolder decidersHolder, SimpleRouterTransformer transformer) {
		this.template = template;
		this.handler = handler;
		this.decidersHolder = decidersHolder;
        this.transformer = transformer;
    }

	@Override
	public boolean modifyRouter(@RequestBody Request request) {

		IRouter router = transformer.transform(request.getRouterName());
		template.setRouter(router);
		Long lockTime = request.getLockPeriod();
		if (lockTime != null) {
			try {
				log.info("路由模板已上锁");
				if (!template.lock()) {
				    log.warn("有别的程序正在执行上锁操作,请重试");
				    return false;
                }
				if (lockTime >= 0) {
					Map<DRParam,Object> map = new HashMap<>();
					handler.registerTask(TaskType.UNLOCK, map);
				}
			} catch (Exception e) {
				template.unlock();
				log.info("路由模板已解锁");
				log.error("锁定路由模板时出现错误", e);
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean unlockTemplate() {
        template.unlock();
		return true;
	}

	@Override
	public boolean setRouters(Request request) {
		Map<String, List<String>> routers = request.getRouters();
		for (AbstractDecider decider : decidersHolder.getDeciders()) {
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
