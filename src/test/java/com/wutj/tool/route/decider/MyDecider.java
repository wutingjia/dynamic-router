package com.wutj.tool.route.decider;

import com.wutj.tool.route.constant.EventMsgType;
import com.wutj.tool.route.consumer.EventMessage;
import com.wutj.tool.route.consumer.IQueueContainer;
import com.wutj.tool.route.model.BasicRouter;
import com.wutj.tool.route.model.IRouter;

/**
 * decider Test.
 *
 * @author wutingjia
 */
public class MyDecider extends AbstractDecider {

    public MyDecider(String name, EventMsgType listenType, IQueueContainer container) {
        super(name, listenType, container);
    }

    @Override
    protected IRouter doDecide(EventMessage msg) {
        return new BasicRouter("secondRouter");
    }

    @Override
    public boolean allowSwitch(EventMessage msg) {
        return true;
    }
}

