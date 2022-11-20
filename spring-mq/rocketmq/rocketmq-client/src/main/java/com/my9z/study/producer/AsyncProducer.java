package com.my9z.study.producer;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @description: 发送异步消息
 * @author: wczy9
 * @createTime: 2022-11-19  17:26
 */
public class AsyncProducer {

    public static void main(String[] args) throws MQClientException, UnsupportedEncodingException, RemotingException, InterruptedException {
        //实例化消息生产者
        DefaultMQProducer producer = new DefaultMQProducer("async-producer");
        //设置NameServer地址
        producer.setNamesrvAddr("43.139.118.53:9876");
        //设置异步消息发送失败时重试次数为0
        producer.setRetryTimesWhenSendAsyncFailed(0);
        //启动Producer实例
        producer.start();
        //声明countDownLatch的count为100，与消息数量同步
        CountDownLatch countDownLatch = new CountDownLatch(100);
        for (int i = 0; i < 100; i++) {
            Message message = new Message("TopicAsync",
                    "TagAsync",
                    "wczy9527"
                    , ("wczy-sync:" + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            int index = i;
            //SendCallback接收异步返回结果的回调
            producer.send(message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    countDownLatch.countDown();
                    System.out.printf("%-10d OK %s %n", index, sendResult.getMsgId());
                }

                @Override
                public void onException(Throwable throwable) {
                    countDownLatch.countDown();
                    System.out.printf("%-10d Exception %s %n", index, throwable);
                    throwable.printStackTrace();
                }
            });
        }
        //主线程等待5秒，等Broker100条消息都回调之后唤醒
        countDownLatch.await(5, TimeUnit.SECONDS);
        //关闭Producer实例。
        producer.shutdown();
    }

}