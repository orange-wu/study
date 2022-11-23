package com.my9z.study.scheduled_message.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * @description: 消费延时消息
 * @author: wczy9
 * @createTime: 2022-11-23  22:34
 */
public class ScheduledConsumer {

    public static void main(String[] args) throws MQClientException {
        //实例化消费者
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("simple-consumer");
        //设置NameServer
        consumer.setNamesrvAddr("43.139.118.53:9876");
        //设置订阅的topic和过滤tag规则
        consumer.subscribe("TopicScheduled", "*");
        //注册消费者
        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            for (MessageExt msg : msgs) {
                System.out.println("Receive message[msgId=" + msg.getMsgId() + "] " +
                        (System.currentTimeMillis() - msg.getBornTimestamp()) + "ms later: " + new String(msg.getBody()));

            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        //启动消费者
        consumer.start();
    }

}