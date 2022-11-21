package com.my9z.study.sequential_message.producer;

import com.my9z.study.sequential_message.pojo.OrderStep;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @description: 发送顺序消息
 * @author: wczy9
 * @createTime: 2022-11-21  23:33
 */
public class SequentialProducer {

    public static void main(String[] args) throws MQBrokerException, RemotingException, InterruptedException,
            MQClientException {
        //实例化生产者对象
        DefaultMQProducer producer = new DefaultMQProducer("sequential-producer");
        //设置nameserver地址
        producer.setNamesrvAddr("43.139.118.53:9876");
        //开启生产者
        producer.start();
        //获取订单列表
        List<OrderStep> orderStepList = new OrderStep().buildOrders();
        for (OrderStep orderStep : orderStepList) {
            //消息体：时间前缀+订单
            String body = LocalDateTime.now().toString() + orderStep;
            //构建消息，tag为订单状态，key为订单号
            Message message = new Message("TopicSequential",
                    "Tag" + orderStep.getDesc(),
                    orderStep.getOrderId().toString(),
                    body.getBytes(StandardCharsets.UTF_8));
            //队列选择：订单号和队列数量取膜
            SendResult sendResult = producer.send(message, (mqs, msg, arg) -> {
                long id = (long) arg;
                long index = id % mqs.size();
                return mqs.get((int) index);
            }, orderStep.getOrderId());
            //打印结果
            System.out.printf("SendResult status:%s, queueId:%d, body:%s%n",
                    sendResult.getSendStatus(),
                    sendResult.getMessageQueue().getQueueId(),
                    body);
        }
        //关闭生产者
        producer.shutdown();
    }

}