package com.my9z.study.filter_message.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * @description: 消息过滤消费者
 * @author: kim
 * @createTime: 2022-12-12  15:18
 */
public class FilterConsumer {

    public static void main(String[] args) throws MQClientException {
        //实例化消费者
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("filter-consumer");
        //设置NameServer
        consumer.setNamesrvAddr("43.139.118.53:9876");
        //设置订阅的topic和过滤tag规则
        String sql = " (TAGS is not null and TAGS in ('TagA', 'TagB')) and (a is not null and a between 0 and 10)";
        consumer.subscribe("TopicFilter", MessageSelector.bySql(sql));
        //注册消费者
        consumer.registerMessageListener((MessageListenerConcurrently) (list, consumeConcurrentlyContext) -> {
            for (MessageExt messageExt : list) {
                System.out.println(new String(messageExt.getBody()));
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        //启动消费者
        consumer.start();
    }

}
