package com.wutj.tool.route.web;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 外部访问controller.
 *
 * @author wutingjia
 */
@RestController
public interface IWebController {

	/**
	 * 强制修改路由
	 * @param request 请求报文
	 * @return true 修改成功;false修改失败
	 */
	@PostMapping("/router/modify")
	boolean modifyRouter(@RequestBody Request request);

	/**
	 * 模板解锁
	 * @return true 解锁成功
	 */
	@PostMapping("/router/unlock")
	boolean unlockTemplate();

	@PostMapping("/router/setRouters")
	boolean setRouters(@RequestBody Request request);
}
