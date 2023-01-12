package com.my9z.study.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description: mq生产者组/消费者组枚举
 * @author: kim
 * @createTime: 2023-01-12  10:55
 */
@Getter
@AllArgsConstructor
public enum MQGroupEnum {

    NORMAL_MESSAGE_PRODUCER_GROUP("NORMAL_MESSAGE_PRODUCER_GROUP", "rocket mq 普通消息(非事务消息)生产者组"),
    ;

    private final String code;

    private final String msg;


}
