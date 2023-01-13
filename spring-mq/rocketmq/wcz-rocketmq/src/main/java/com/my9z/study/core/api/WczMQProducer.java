package com.my9z.study.core.api;

import cn.hutool.core.util.StrUtil;
import com.my9z.study.common.enums.DelayTimeLevelEnum;
import com.my9z.study.core.factory.MQProducerFactory;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.client.producer.selector.SelectMessageQueueByHash;
import org.apache.rocketmq.common.message.Message;
import org.springframework.stereotype.Service;

/**
 * @description: RocketMQ生产者类
 * @author: kim
 * @createTime: 2023-01-12  10:08
 */
@Service
public class WczMQProducer {


    //-------------------sync-------------------//

    /**
     * 同步消息
     *
     * @param topic   消息topic
     * @param tag     消息tag
     * @param message 消息内容
     * @param key     消息key
     */
    public void sendSync(String topic, String tag, Object message, String key)
            throws Exception {
        DefaultMQProducer normalMQProducer = MQProducerFactory.getInstance().getNormalMQProducer();
        Message msg = MessageBuilder.builder().topic(topic).tag(tag).message(message).key(key).build();
        normalMQProducer.send(msg);
    }

    /**
     * 同步延时消息
     *
     * @param topic   消息topic
     * @param tag     消息tag
     * @param message 消息内容
     * @param key     消息key
     * @param delay   消息延迟时间级别
     */
    public void sendSyncDelay(String topic, String tag, Object message, String key, DelayTimeLevelEnum delay)
            throws Exception {
        DefaultMQProducer normalMQProducer = MQProducerFactory.getInstance().getNormalMQProducer();
        Message msg = MessageBuilder.builder().topic(topic).tag(tag).message(message).key(key).delayTimeLevel(delay).build();
        normalMQProducer.send(msg);
    }

    /**
     * 同步顺序消息 保证相同sequentialKey的消息发到相同的队列
     *
     * @param topic         消息topic
     * @param tag           消息tag
     * @param message       消息内容
     * @param key           消息key
     * @param sequentialKey 选择队列的业务字段
     */
    public void sendSyncSequential(String topic, String tag, Object message, String key, Object sequentialKey)
            throws Exception {
        DefaultMQProducer normalMQProducer = MQProducerFactory.getInstance().getNormalMQProducer();
        Message msg = MessageBuilder.builder().topic(topic).tag(tag).message(message).key(key).build();
        normalMQProducer.send(msg, new SelectMessageQueueByHash(), sequentialKey);
    }

    //-------------------async-------------------//

    /**
     * 异步消息
     *
     * @param topic        消息topic
     * @param tag          消息tag
     * @param message      消息内容
     * @param key          消息key
     * @param sendCallback 异步消息结果回调
     */
    public void sendAsync(String topic, String tag, Object message, String key, SendCallback sendCallback)
            throws Exception {
        DefaultMQProducer normalMQProducer = MQProducerFactory.getInstance().getNormalMQProducer();
        Message msg = MessageBuilder.builder().topic(topic).tag(tag).message(message).key(key).build();
        normalMQProducer.send(msg, sendCallback);
    }

    /**
     * 异步延时消息
     *
     * @param topic        消息topic
     * @param tag          消息tag
     * @param message      消息内容
     * @param key          消息key
     * @param delay        消息延迟时间级别
     * @param sendCallback 异步消息结果回调
     */
    public void sendAsyncDelay(String topic, String tag, Object message, String key, DelayTimeLevelEnum delay, SendCallback sendCallback)
            throws Exception {
        DefaultMQProducer normalMQProducer = MQProducerFactory.getInstance().getNormalMQProducer();
        Message msg = MessageBuilder.builder().topic(topic).tag(tag).message(message).key(key).delayTimeLevel(delay).build();
        normalMQProducer.send(msg, sendCallback);
    }

    /**
     * 异步顺序消息 保证相同sequentialKey的消息发到相同的队列
     *
     * @param topic         消息topic
     * @param tag           消息tag
     * @param message       消息内容
     * @param key           消息key
     * @param sequentialKey 选择队列的业务字段
     * @param sendCallback  异步消息结果回调
     */
    public void sendAsyncSequential(String topic, String tag, Object message, String key, Object sequentialKey, SendCallback sendCallback)
            throws Exception {
        DefaultMQProducer normalMQProducer = MQProducerFactory.getInstance().getNormalMQProducer();
        Message msg = MessageBuilder.builder().topic(topic).tag(tag).message(message).key(key).build();
        normalMQProducer.send(msg, new SelectMessageQueueByHash(), sequentialKey, sendCallback);
    }

    //-------------------one-way-------------------//

    /**
     * 单向消息
     *
     * @param topic   消息topic
     * @param tag     消息tag
     * @param message 消息内容
     * @param key     消息key
     */
    public void sendOneway(String topic, String tag, Object message, String key)
            throws Exception {
        DefaultMQProducer normalMQProducer = MQProducerFactory.getInstance().getNormalMQProducer();
        Message msg = MessageBuilder.builder().topic(topic).tag(tag).message(message).key(key).build();
        normalMQProducer.sendOneway(msg);
    }

    //-------------------transaction-------------------//

    /**
     * 事务消息
     *
     * @param topic   消息topic
     * @param tag     消息tag
     * @param message 消息内容
     * @param key     消息key
     * @param x       业务参数，执行本地事务方法时需要的话可以传入
     */
    public void sendTransaction(String producerGroup, String topic, String tag, Object message, String key, Object x)
            throws Exception {
        if (StrUtil.isEmpty(producerGroup)) return;
        TransactionMQProducer transactionMQProducer = MQProducerFactory.getInstance().getTransactionMQProducer(producerGroup);
        Message msg = MessageBuilder.builder().topic(topic).tag(tag).message(message).key(key).build();
        transactionMQProducer.sendMessageInTransaction(msg, x);
    }

}
