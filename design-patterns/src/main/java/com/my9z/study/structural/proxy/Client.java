package com.my9z.study.structural.proxy;

import com.my9z.study.structural.proxy.dynamic_proxy.cglib.My9zCglibProxyFactory;
import com.my9z.study.structural.proxy.dynamic_proxy.cglib.SendServiceMethodInterceptor;
import com.my9z.study.structural.proxy.dynamic_proxy.jdk.My9zJdkProxyFactory;
import com.my9z.study.structural.proxy.dynamic_proxy.jdk.SendServiceInvocationHandler;
import com.my9z.study.structural.proxy.static_proxy.SendServiceProxy;

/**
 * @description: 客户端实际使用代理
 * @author: wczy9
 * @createTime: 2023-09-05  16:27
 */
public class Client {
    public static void main(String[] args) {
        SendService sendService = new SendServiceImpl();
        //静态代理
        SendServiceProxy sendServiceProxy = new SendServiceProxy(sendService);
        sendServiceProxy.sendMsg("静态代理");
        //jdk动态代理
        SendService sendJdkProxy = My9zJdkProxyFactory.getProxy(sendService, new SendServiceInvocationHandler(sendService));
        sendJdkProxy.sendMsg("jdk动态代理");
        //cglib动态代理
        SendService sendCglibProxy = My9zCglibProxyFactory.getProxy(sendService, new SendServiceMethodInterceptor());
        sendCglibProxy.sendMsg("cglib动态代理");
    }
}