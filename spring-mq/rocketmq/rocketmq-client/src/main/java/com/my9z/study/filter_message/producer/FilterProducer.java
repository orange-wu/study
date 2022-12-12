package com.my9z.study.filter_message.producer;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

/**
 * @description: 消息过滤生产者
 * @author: kim
 * @createTime: 2022-12-12  15:08
 */
public class FilterProducer {

    public static void main(String[] args) throws MQClientException, MQBrokerException,
            RemotingException, InterruptedException {
        //实例化生产者对象
        DefaultMQProducer producer = new DefaultMQProducer("filter-producer");
        //设置nameserver地址
        producer.setNamesrvAddr("43.139.118.53:9876");
        //开启生产者
        producer.start();
        //构建消息
        String[] tags = new String[]{"TagA", "TagB", "TagC"};
        String topic = "TopicFilter";
        for (int i = 0; i < 15; i++) {
            Message message = new Message(topic, tags[i % tags.length], ("Hello world" + i).getBytes());
            //设置属性
            message.putUserProperty("a", String.valueOf(i));
            //发送消息
            producer.send(message);
        }
        //关闭生产者
        producer.shutdown();
    }

}
