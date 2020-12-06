package com.wutj.tool.route.util;

import com.wutj.tool.route.model.BasicRouter;
import com.wutj.tool.route.model.IRouter;

/**
 * 默认的从String到router的映射器.
 *
 * @author wutingjia
 */
public class SimpleRouterTransformer implements Transformer<String, IRouter> {

    @Override
    public IRouter transform(String i) {
        return new BasicRouter(i);
    }
}
