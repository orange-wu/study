package com.my9z.study.structural.proxy;

/**
 * @description: SendService接口实现类
 * @author: wczy9
 * @createTime: 2023-09-05  16:15
 */
public class SendServiceImpl implements SendService {
    @Override
    public void sendMsg(String msg) {
        System.out.println(msg);
    }
}