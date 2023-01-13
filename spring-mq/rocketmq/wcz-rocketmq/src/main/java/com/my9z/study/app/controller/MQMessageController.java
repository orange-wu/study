package com.my9z.study.app.controller;

import com.my9z.study.core.api.WczMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 消息生产者接口
 * @author: wczy9
 * @createTime: 2023-01-13  11:26
 */
@RestController
@RequestMapping("/product")
public class MQMessageController {

    @Autowired
    private WczMQProducer mqProducer;

    @PostMapping("/sendMsg")
    public void sendMsg() {
        try {
            mqProducer.sendSync("WCZY-TEST", null, "hello world", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
