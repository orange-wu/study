package com.my9z.study.sequential_message.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.concurrent.TimeUnit;

/**
 * @description: 消费顺序消息
 * @author: wczy9
 * @createTime: 2022-11-21  23:59
 */
public class SequentialConsumer {

    public static void main(String[] args) throws MQClientException {
        //实例化消费者
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("sequential-consumer");
        //设置nameserver
        consumer.setNamesrvAddr("43.139.118.53:9876");
        //从队列头部开始消费
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        //设置监听的消费者
        consumer.subscribe("TopicSequential", "*");
        //按顺序接收对象，一个队列一个线程来消费
        consumer.registerMessageListener((MessageListenerOrderly) (msgs, context) -> {
            for (MessageExt msg : msgs) {
                //可以看到每个queue有唯一的consume线程来消费, 订单对每个queue(分区)有序
                System.out.println("consumeThread=" + Thread.currentThread().getName()
                        + " queueId=" + msg.getQueueId()
                        + " content:" + new String(msg.getBody()));
            }
            try {
                //模拟实际业务
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return ConsumeOrderlyStatus.SUCCESS;
        });
        //启动消费者
        consumer.start();
    }

}