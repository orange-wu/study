package com.my9z.study.producer;

import cn.hutool.json.JSONUtil;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;

/**
 * @description: 发送同步消息
 * @author: wczy9
 * @createTime: 2022-11-19  17:13
 */
public class SyncProducer {

    public static void main(String[] args) throws MQClientException, UnsupportedEncodingException, MQBrokerException,
            RemotingException, InterruptedException {
        //实例化消息生产者
        DefaultMQProducer producer = new DefaultMQProducer("sync-producer");
        //设置NameServer地址
        producer.setNamesrvAddr("43.139.118.53:9876");
        //启动Producer实例
        producer.start();
        for (int i = 0; i < 100; i++) {
            Message message = new Message("TopicSync",
                    "TagSync",
                    ("wczy-sync:" + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            //发送消息到一个Broker
            SendResult sendResult = producer.send(message);
            //查看返回信息
            System.out.printf("%s%n", JSONUtil.toJsonStr(sendResult));
        }
        //关闭Producer实例
        producer.shutdown();
    }

}