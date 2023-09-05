package com.my9z.study.structural.proxy.static_proxy;

import java.util.Date;

/**
 * @description: SendService接口代理类
 * @author: wczy9
 * @createTime: 2023-09-05  16:16
 */
public class SendServiceProxy implements SendService {

    private final SendService sendService;

    public SendServiceProxy(SendService sendService) {
        this.sendService = sendService;
    }

    @Override
    public void sendMsg(String msg) {
        //执行方法之前
        System.out.println("before sendMsg " + new Date());
        //调用实际方法
        sendService.sendMsg(msg);
        //执行方法之前
        System.out.println("after sendMsg " + new Date());
    }
}