package com.wutj.tool.route.util;

public interface Transformer<I, O> {

    O transform(I i);
}
