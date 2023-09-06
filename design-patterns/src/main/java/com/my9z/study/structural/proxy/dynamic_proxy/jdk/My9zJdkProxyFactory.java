package com.my9z.study.structural.proxy.dynamic_proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * @description: 代理对象工厂类
 * @author: wczy9
 * @createTime: 2023-09-06  15:59
 */
public class My9zJdkProxyFactory {

    @SuppressWarnings("unchecked")
    public static <T> T getProxy(T target, InvocationHandler handler) {
        return (T) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces()
                , handler);
    }

}