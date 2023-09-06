package com.my9z.study.structural.proxy;

import com.my9z.study.structural.proxy.dynamic_proxy.jdk.SendServiceInvocationHandler;
import com.my9z.study.structural.proxy.static_proxy.SendServiceProxy;

import java.lang.reflect.Proxy;

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
        SendService sendProxyInstance = (SendService) Proxy.newProxyInstance(
                sendService.getClass().getClassLoader(),
                sendService.getClass().getInterfaces(),
                new SendServiceInvocationHandler(sendService));
        sendProxyInstance.sendMsg("jdk动态代理");
    }
}