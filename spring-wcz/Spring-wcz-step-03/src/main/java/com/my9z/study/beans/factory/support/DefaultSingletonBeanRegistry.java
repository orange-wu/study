package com.my9z.study.beans.factory.support;

import com.my9z.study.beans.factory.config.SingletonBeanRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: 默认单例注册表的实现
 * @author: wczy9
 * @createTime: 2022-11-15  22:30
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    private final Map<String, Object> singletonObjects = new HashMap<>();

    @Override
    public Object getSingleton(String name) {
        return singletonObjects.get(name);
    }

    @Override
    public void registerSingleton(String name, Object singletonObject) {
        singletonObjects.put(name, singletonObject);
    }
}