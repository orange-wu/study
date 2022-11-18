package com.my9z.study.message;

import com.my9z.study.pojo.User;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 消息生产者接口
 * @author: wczy9
 * @createTime: 2022-11-18  22:18
 */
@RestController
@RequestMapping("/product")
public class MQMessageController {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @PostMapping("/sendMsg")
    public void sendMsg(@RequestBody User user){
        String topic = "WCZY-TEST";
        rocketMQTemplate.convertAndSend(topic,user);
    }

}