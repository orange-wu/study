package com.my9z.study.core.api;

import com.alibaba.fastjson.JSON;
import com.my9z.study.core.factory.MQProducerFactory;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * @description: RocketMQ生产者类
 * @author: kim
 * @createTime: 2023-01-12  10:08
 */
@Service
public class MQProducer {


    //-------------------sync-------------------//

    /**
     * 同步消息
     *
     * @param topic   消息topic
     * @param tag     消息tag
     * @param message 消息内容
     * @param key     消息key
     */
    protected void send(String topic, String tag, Object message, String key) throws Exception {
        DefaultMQProducer normalMQProducer = MQProducerFactory.getInstance().getNormalMQProducer();
        Message msg = new Message(topic, tag, key, JSON.toJSONString(message).getBytes(StandardCharsets.UTF_8));
        normalMQProducer.send(msg);
    }

    // TODO: 2023/1/12 发送消息方法封装

}
