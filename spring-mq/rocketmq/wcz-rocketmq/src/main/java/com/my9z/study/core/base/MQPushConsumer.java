package com.my9z.study.core.base;

import com.my9z.study.common.enums.ErrorCodeEnum;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;

/**
 * @description: 消费者扩展类，扩展DefaultMQPushConsumer
 * @author: wczy9
 * @createTime: 2023-01-17  15:45
 */
public class MQPushConsumer<S, C> extends DefaultMQPushConsumer {

    private final ConsumerListener<S, C> consumerListener;

    @SuppressWarnings("unchecked")
    public MQPushConsumer(String consumerGroup, ConsumerListener<S, C> consumerListener) {
        super(consumerGroup);
        this.consumerListener = consumerListener;
        switch (consumerListener.consumeMode()) {
            case CONCURRENTLY:
                MessageListenerConcurrently messageListenerConcurrently = (msgs, context) ->
                        (ConsumeConcurrentlyStatus) consumerListener.onMessage(msgs, (C) context);
                this.registerMessageListener(messageListenerConcurrently);
                break;
            case ORDERLY:
                MessageListenerOrderly messageListenerOrderly = (msgs, context) ->
                        (ConsumeOrderlyStatus) consumerListener.onMessage(msgs, (C) context);
                this.registerMessageListener(messageListenerOrderly);
                break;
            default:
                throw ErrorCodeEnum.CONSUMER_MODE_IS_UN_KNOW.buildException();
        }
    }

    public ConsumerListener<S, C> getConsumerListener() {
        return this.consumerListener;
    }

}