package com.my9z.study.transaction_message.producer;

import com.my9z.study.transaction_message.TransactionListenerImpl;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @description: 发送事务消息
 * @author: kim
 * @createTime: 2022-12-13  14:10
 */
public class TransactionProducer {

    public static void main(String[] args) throws MQClientException, InterruptedException {
        //实例化事务监听类
        TransactionListenerImpl transactionListener = new TransactionListenerImpl();
        //创建线程池
        ThreadPoolExecutor threadPoolExecutor =
                new ThreadPoolExecutor(2, 5, 100, TimeUnit.SECONDS,
                        new ArrayBlockingQueue<>(200), r -> {
                    Thread thread = new Thread(r);
                    thread.setName("client-transaction-msg-check-thread");
                    return thread;
                });
        //构建生产者
        TransactionMQProducer producer = new TransactionMQProducer("transaction-producer");
        producer.setTransactionListener(transactionListener);
        producer.setExecutorService(threadPoolExecutor);
        producer.setNamesrvAddr("43.139.118.53:9876");
        //启动生产者
        producer.start();
        //构建消息
        for (int i = 0; i < 10; i++) {
            Message msg =
                    new Message("TopicTransaction", "", "KEY" + i, ("Hello RocketMQ " + i).getBytes());
            producer.sendMessageInTransaction(msg, null);
            Thread.sleep(10);
        }
        //睡眠长时间，保证能顺利回查到本地事务状态
        for (int i = 0; i < 100000; i++) {
            Thread.sleep(1000);
        }
        //关闭生产者
        producer.shutdown();
    }

}
