package com.my9z.study.app.producer;

import com.my9z.study.common.constant.RocketMqConstant;
import com.my9z.study.core.annotation.MQTransactionListener;
import com.my9z.study.core.base.AbstractMQTransactionListener;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Service;


/**
 * @description: RocketMQ DEMO事务消息监听类
 * @author: kim
 * @createTime: 2023-01-12  10:08
 */
@Service
@MQTransactionListener(producerGroup = RocketMqConstant.TEST_TRANSACTION_MESSAGE_PRODUCER_GROUP)
public class TestTransactionListener extends AbstractMQTransactionListener {

    @Override
    public LocalTransactionState getLocalTransaction(Message msg, Object arg) {
        return LocalTransactionState.UNKNOW;
    }

    @Override
    public LocalTransactionState inspectLocalTransaction(MessageExt msg) {
        return LocalTransactionState.COMMIT_MESSAGE;
    }

}
