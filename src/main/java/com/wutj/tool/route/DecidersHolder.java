package com.wutj.tool.route;

import java.util.List;

public interface DecidersHolder<T> {

	List<AbstractDecider<T>> getDeciders();
}
