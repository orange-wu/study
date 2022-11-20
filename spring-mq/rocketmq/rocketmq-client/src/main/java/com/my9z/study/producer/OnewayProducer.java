package com.my9z.study.producer;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;

/**
 * @description: 发送单向消息
 * @author: wczy9
 * @createTime: 2022-11-20  23:10
 */
public class OnewayProducer {

    public static void main(String[] args) throws MQClientException, UnsupportedEncodingException, RemotingException,
            InterruptedException {
        //实例化消息生产者
        DefaultMQProducer producer = new DefaultMQProducer("oneway-producer");
        //设置NameServer地址
        producer.setNamesrvAddr("43.139.118.53:9876");
        //启动Producer实例
        producer.start();
        for (int i = 0; i < 100; i++) {
            //创建消息
            Message message = new Message("TopicOneway",
                    "TagOneWay",
                    ("wczy-oneway:" + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            producer.sendOneway(message);
        }
        //关闭Producer实例。
        producer.shutdown();
    }

}