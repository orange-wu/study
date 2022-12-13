package com.my9z.study.transaction_message;

import com.alibaba.fastjson.JSON;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description: 事务监听接口实现类
 * @author: kim
 * @createTime: 2022-12-13  10:18
 */
public class TransactionListenerImpl implements TransactionListener {

    private final AtomicInteger transactionIndex = new AtomicInteger(0);
    private final ConcurrentHashMap<String, Integer> localTrans = new ConcurrentHashMap<>();

    /**
     * 当半事务消息发送成功时，将调用此方法来执行本地事务，并反馈给生产者本地事务执行状态
     *
     * @param message 半事务消息
     * @param o       自定义业务参数
     * @return 事务处理状态
     */
    @Override
    public LocalTransactionState executeLocalTransaction(Message message, Object o) {
        int value = transactionIndex.getAndIncrement();
        int status = value % 3;
        localTrans.put(message.getTransactionId(), status);
        System.out.println("executeLocalTransaction:message:" + JSON.toJSONString(message));
        System.out.println("executeLocalTransaction:Object:" + JSON.toJSONString(o));
        System.out.println("executeLocalTransaction:" + message.getTransactionId() + ":" + status);
        return LocalTransactionState.UNKNOW;
    }

    /**
     * 当broker没有收到本地事务的确认消息时，异步调用此方法回查本地事务状态
     *
     * @param messageExt 回查消息
     * @return 事务处理状态
     */
    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
        Integer status = localTrans.get(messageExt.getTransactionId());
        System.out.println("checkLocalTransaction:" + messageExt.getTransactionId() + ":" + status);
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
