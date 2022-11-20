package com.my9z.study.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;

/**
 * @description: 简单消息消费者
 * @author: wczy9
 * @createTime: 2022-11-20  23:19
 */
public class SimpleConsumer {

    public static void main(String[] args) throws MQClientException {
        //实例化消费者
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("simple-consumer");
        //设置NameServer
        consumer.setNamesrvAddr("43.139.118.53:9876");
        //设置消费的topic和过滤tag规则
        consumer.subscribe("TopicSync","*");
        //注册回调类处理broker推来的消息
        consumer.registerMessageListener((MessageListenerConcurrently) (list, consumeConcurrentlyContext) -> {
            System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), list);
            // 标记该消息已经被成功消费
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        //启动消费者
        consumer.start();
        System.out.printf("Consumer Started.%n");
    }

}