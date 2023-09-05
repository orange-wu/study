package com.my9z.study.structural.proxy.static_proxy;

/**
 * @description: 客户端实际使用代理
 * @author: wczy9
 * @createTime: 2023-09-05  16:27
 */
public class Client {
    public static void main(String[] args) {
        SendService sendService = new SendServiceImpl();
        SendServiceProxy sendServiceProxy = new SendServiceProxy(sendService);
        sendServiceProxy.sendMsg("my9z");
    }
}