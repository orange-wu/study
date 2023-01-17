package com.my9z.study.app.consumer;

import com.my9z.study.common.enums.ConsumeModeEnum;
import com.my9z.study.core.annotation.MQConsumer;
import com.my9z.study.core.base.ConsumerListener;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description: 顺序消费监听
 * @author: wczy9
 * @createTime: 2023-01-17  17:56
 */
@Service
@MQConsumer(uniqueKey = "test", topic = "TopicSync", tag = "*", consumerGroup = "TEST_CONSUMER")
public class TestConsumerListener implements ConsumerListener<ConsumeConcurrentlyStatus, ConsumeConcurrentlyContext> {
    @Override
    public ConsumeModeEnum consumeMode() {
        return ConsumeModeEnum.CONCURRENTLY;
    }

    @Override
    public ConsumeConcurrentlyStatus onMessage(List<MessageExt> messages, ConsumeConcurrentlyContext context) {
        for (MessageExt msg : messages) {
            System.out.println("test consumer message[msgId=" + msg.getMsgId() + "] " +
                    (System.currentTimeMillis() - msg.getBornTimestamp()) + "ms later: " + new String(msg.getBody()));

        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}