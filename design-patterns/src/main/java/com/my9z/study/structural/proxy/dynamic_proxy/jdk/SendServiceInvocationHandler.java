package com.my9z.study.structural.proxy.dynamic_proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @description: jdk动态代理调用处理器实现类
 * @author: wczy9
 * @createTime: 2023-09-05  16:57
 */
public class SendServiceInvocationHandler implements InvocationHandler {

    //需要代理的真实对象
    private final Object target;

    public SendServiceInvocationHandler(Object target) {
        this.target = target;
    }

    /**
     * 集中处理动态代理类上的所有类方法调用
     * 调用处理器根据这三个参数进行预处理或分派到代理类实例上反射执行
     *
     * @param proxy  动态生成的代理类
     * @param method 代理类调用的方法
     * @param args   方法调用参数
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //执行方法之前
        System.out.println("before " + method.getName() + Arrays.toString(args));
        //调用实际方法
        Object result = method.invoke(target, args);
        //执行方法之前
        System.out.println("after " + method.getName() + Arrays.toString(args));
        return result;
    }
}