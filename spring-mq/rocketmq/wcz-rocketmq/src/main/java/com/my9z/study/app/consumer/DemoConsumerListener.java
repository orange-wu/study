package com.my9z.study.app.consumer;

import com.my9z.study.common.enums.ConsumeModeEnum;
import com.my9z.study.core.annotation.MQConsumer;
import com.my9z.study.core.base.ConsumerListener;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description: 顺序消费监听器
 * @author: wczy9
 * @createTime: 2023-01-17  17:51
 */
@Service
@MQConsumer(uniqueKey = "demo", topic = "TopicSync", tag = "*", consumerGroup = "DEMO_CONSUMER")
public class DemoConsumerListener implements ConsumerListener<ConsumeOrderlyStatus, ConsumeOrderlyContext> {
    @Override
    public ConsumeModeEnum consumeMode() {
        return ConsumeModeEnum.ORDERLY;
    }

    @Override
    public ConsumeOrderlyStatus onMessage(List<MessageExt> messages, ConsumeOrderlyContext context) {
        for (MessageExt msg : messages) {
            System.out.println("demo consumer message[msgId=" + msg.getMsgId() + "] " +
                    (System.currentTimeMillis() - msg.getBornTimestamp()) + "ms later: " + new String(msg.getBody()));

        }
        return ConsumeOrderlyStatus.SUCCESS;
    }
}