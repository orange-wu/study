package com.my9z.study.core.api;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.my9z.study.common.enums.DelayTimeLevelEnum;
import com.my9z.study.common.enums.ErrorCodeEnum;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.Message;

import java.nio.charset.StandardCharsets;

/**
 * @description: RocketMQ消息对象Builder
 * @author: kim
 * @createTime: 2023-01-13  09:45
 */
@Getter
@Slf4j
public class MessageBuilder {

    private String topic;
    private String tag;
    private Object message;
    private String key;
    private Integer delayTimeLevel;

    public static MessageBuilder builder() {
        return new MessageBuilder();
    }

    public MessageBuilder topic(String topic) {
        this.topic = topic;
        return this;
    }

    public MessageBuilder tag(String tag) {
        this.tag = tag;
        return this;
    }

    public MessageBuilder message(Object message) {
        this.message = message;
        return this;
    }

    public MessageBuilder key(String key) {
        this.key = key;
        return this;
    }

    public MessageBuilder delayTimeLevel(DelayTimeLevelEnum delayTimeLevel) {
        this.delayTimeLevel = delayTimeLevel.getLevel();
        return this;
    }

    public Message build() {
        if (StrUtil.isEmpty(this.topic)) {
            log.error("build message error,because topic is empty");
            throw ErrorCodeEnum.BUILD_MESSAGE_ERROR.buildException();
        }

        if (null == this.message) {
            log.error("build message error,because msgBody is null");
            throw ErrorCodeEnum.BUILD_MESSAGE_ERROR.buildException();
        }

        String msg = JSON.toJSONString(this.message);

        Message message = new Message(this.topic, msg.getBytes(StandardCharsets.UTF_8));
        if (StrUtil.isNotEmpty(this.tag)) message.setTags(this.tag);

        if (StrUtil.isNotEmpty(this.key)) message.setKeys(this.key);

        if (delayTimeLevel != null) message.setDelayTimeLevel(delayTimeLevel);

        return message;
    }


}
