package com.my9z.study.common.constant;

/**
 * @description: RocketMQ固定信息
 * @author: kim
 * @createTime: 2023-01-12  10:55
 */
public interface RocketMqConstant {

    /**
     * rocket mq 普通消息(非事务消息)生产者组
     */
    String NORMAL_MESSAGE_PRODUCER_GROUP = "NORMAL_MESSAGE_PRODUCER_GROUP0";

    /**
     * rocket mq DEMO事务消息生产者组
     */
    String DEMO_TRANSACTION_MESSAGE_PRODUCER_GROUP = "DEMO_TRANSACTION_MESSAGE_PRODUCER_GROUP";

    /**
     * rocket mq TEST事务消息生产者组
     */
    String TEST_TRANSACTION_MESSAGE_PRODUCER_GROUP = "TEST_TRANSACTION_MESSAGE_PRODUCER_GROUP";

}
