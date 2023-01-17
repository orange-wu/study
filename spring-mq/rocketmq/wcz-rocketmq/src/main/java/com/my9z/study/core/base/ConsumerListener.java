package com.my9z.study.core.base;

import com.my9z.study.common.enums.ConsumeModeEnum;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * @description: 消息消费监听
 * 范型S：有序消费或并行消费返回的消息状态，ConsumeOrderlyStatus/ConsumeConcurrentlyStatus
 * 范型C：有序消费或并行消费的消费上下文，ConsumeOrderlyContext/ConsumeConcurrentlyContext
 * @author: wczy9
 * @createTime: 2023-01-17  14:58
 */
public interface ConsumerListener<S, C> {


    /**
     * 指定消费模式 {@link com.my9z.study.common.enums.ConsumeModeEnum}
     *
     * @return 消费模式ORDERLY或CONCURRENTLY
     */
    ConsumeModeEnum consumeMode();

    /**
     * 具体消费逻辑
     *
     * @param messages 消息集合对象
     * @param context 消费上下文
     * @return 消息消费状态
     */
    S onMessage(List<MessageExt> messages, C context);

}