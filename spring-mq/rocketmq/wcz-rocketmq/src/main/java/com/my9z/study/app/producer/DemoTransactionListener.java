package com.my9z.study.app.producer;

import com.my9z.study.common.constant.RocketMqConstant;
import com.my9z.study.core.annotation.MQTransactionListener;
import com.my9z.study.core.base.AbstractMQTransactionListener;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @description: RocketMQ DEMO事务消息监听类
 * @author: kim
 * @createTime: 2023-01-12  10:08
 */
@Service
@MQTransactionListener(producerGroup = RocketMqConstant.DEMO_TRANSACTION_MESSAGE_PRODUCER_GROUP)
public class DemoTransactionListener extends AbstractMQTransactionListener {

    private final AtomicInteger transactionIndex = new AtomicInteger(0);
    private final ConcurrentHashMap<String, Integer> localTrans = new ConcurrentHashMap<>();

    @Override
    public LocalTransactionState getLocalTransaction(Message msg, Object arg) {
        int value = transactionIndex.getAndIncrement();
        int status = value % 3;
        localTrans.put(msg.getTransactionId(), status);
        return LocalTransactionState.UNKNOW;
    }

    @Override
    public LocalTransactionState inspectLocalTransaction(MessageExt msg) {
        Integer status = localTrans.get(msg.getTransactionId());
        if (null != status) {
            switch (status) {
                case 0:
                    return LocalTransactionState.UNKNOW;
                case 1:
                    return LocalTransactionState.COMMIT_MESSAGE;
                case 2:
                    return LocalTransactionState.ROLLBACK_MESSAGE;
            }
        }
        return LocalTransactionState.COMMIT_MESSAGE;
    }

}
