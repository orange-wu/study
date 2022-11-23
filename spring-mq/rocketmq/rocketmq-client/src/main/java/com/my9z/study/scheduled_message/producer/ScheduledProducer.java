package com.my9z.study.scheduled_message.producer;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;

/**
 * @description: 发送延时消息
 * @author: wczy9
 * @createTime: 2022-11-23  22:26
 */
public class ScheduledProducer {

    public static void main(String[] args) throws MQClientException, UnsupportedEncodingException, MQBrokerException,
            RemotingException, InterruptedException {
        //实例化生产者对象
        DefaultMQProducer producer = new DefaultMQProducer("sequential-producer");
        //设置nameserver地址
        producer.setNamesrvAddr("43.139.118.53:9876");
        //开启生产者
        producer.start();
        for (int i = 0; i < 100; i++) {
            Message message = new Message("TopicScheduled", ("wczy-scheduled:" + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            //设置延时等级为3，消息将在十秒后发送
            //延时消息只支持几个固定时间：1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
            message.setDelayTimeLevel(3);
            producer.send(message);
        }
        //关闭生产者
        producer.shutdown();
    }

}