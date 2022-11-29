package com.my9z.study.batch_message.producer;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 发送批量消息
 * @author: kim
 * @createTime: 2022-11-29  15:48
 */
public class BatchProducer {

    public static void main(String[] args) throws MQClientException, MQBrokerException,
            RemotingException, InterruptedException {
        //实例化生产者对象
        DefaultMQProducer producer = new DefaultMQProducer("batch-producer");
        //设置nameserver地址
        producer.setNamesrvAddr("43.139.118.53:9876");
        //开启生产者
        producer.start();
        //构建批量消息
        String topic = "TopicBatch";
        List<Message> messages = new ArrayList<>();
        messages.add(new Message(topic, "TagA", "OrderID001", "Hello world 0".getBytes()));
        messages.add(new Message(topic, "TagA", "OrderID002", "Hello world 1".getBytes()));
        messages.add(new Message(topic, "TagA", "OrderID003", "Hello world 2".getBytes()));
        //发送批量消息
        producer.send(messages);
        //关闭生产者
        producer.shutdown();
    }

}
