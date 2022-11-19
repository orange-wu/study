package com.my9z.study.message;

import cn.hutool.json.JSONUtil;
import com.my9z.study.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;


/**
 * @description: 消息消费者
 * @author: wczy9
 * @createTime: 2022-11-19  00:20
 */
@Component
@RocketMQMessageListener(topic = "WCZY-TEST",consumerGroup = "WCZY-TEST-CONSUMER-GROUP")
@Slf4j
public class MQMessageConsumer implements RocketMQListener<User> {

    @Override
    public void onMessage(User message) {
        log.info("userMessage:{}", JSONUtil.toJsonStr(message));
    }
}