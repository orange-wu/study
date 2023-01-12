package com.my9z.study.core.base;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * @description: RocketMQ事务消息监听抽象类
 * @author: kim
 * @createTime: 2023-01-12  11:35
 */
@Slf4j
public abstract class AbstractMQTransactionListener implements TransactionListener {

    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        String key = msg.getKeys();
        log.info("start execute local transaction {}", key);
        LocalTransactionState localTransaction = getLocalTransaction(msg, arg);
        log.info("execute local transaction: {},execute state: {}", key, localTransaction);
        return localTransaction;
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
        String key = msg.getKeys();
        log.info("start check local transaction {}", key);
        LocalTransactionState localTransaction = inspectLocalTransaction(msg);
        log.info("execute check transaction: {},execute state: {}", key, localTransaction);
        return localTransaction;
    }


    /**
     * 执行本地事务方法，所有事务消息发送端都需要实现特定的事务内容
     *
     * @param msg 发送消息对象
     * @param arg 业务参数
     * @return LocalTransactionState 本地事务状态
     */
    public abstract LocalTransactionState getLocalTransaction(Message msg, Object arg);

    /**
     * 回调检查本地事务方法，所有事务消息发送端都需要实现，表示事务是否完成的标识
     *
     * @param msg 发送消息对象
     * @return LocalTransactionState 本地事务状态
     */
    public abstract LocalTransactionState inspectLocalTransaction(MessageExt msg);

}
