package com.my9z.study.structural.proxy.dynamic_proxy.cglib;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @description: cglib动态代理 方法拦截器
 * @author: wczy9
 * @createTime: 2023-09-06  16:47
 */
public class SendServiceMethodInterceptor implements MethodInterceptor {
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        //执行方法之前
        System.out.println("before " + method.getName() + Arrays.toString(args));
        //调用实际方法
        Object result = proxy.invokeSuper(obj, args);
        //执行方法之前
        System.out.println("after " + method.getName() + Arrays.toString(args));
        return obj;
    }
}