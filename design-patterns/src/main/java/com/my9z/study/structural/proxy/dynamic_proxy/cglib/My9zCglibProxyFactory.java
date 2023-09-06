package com.my9z.study.structural.proxy.dynamic_proxy.cglib;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

/**
 * @description: cglib代理对象工厂类
 * @author: wczy9
 * @createTime: 2023-09-06  16:51
 */
public class My9zCglibProxyFactory {

    @SuppressWarnings("unchecked")
    public static <T> T getProxy(T target, MethodInterceptor interceptor) {
        // 创建动态代理增强类
        Enhancer enhancer = new Enhancer();
        // 设置类加载器
        enhancer.setClassLoader(target.getClass().getClassLoader());
        // 设置被代理类
        enhancer.setSuperclass(target.getClass());
        // 设置方法拦截器
        enhancer.setCallback(interceptor);
        // 创建代理类
        return (T) enhancer.create();
    }

}