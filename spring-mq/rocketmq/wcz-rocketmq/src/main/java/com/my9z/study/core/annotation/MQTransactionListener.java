package com.my9z.study.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description: RocketMQ事务消息监听标签
 * @author: kim
 * @createTime: 2023-01-12  09:32
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MQTransactionListener {

    /**
     * 生产者组名
     */
    String producerGroup() default "TRANSACTION_MESSAGE_PRODUCER_GROUP";

}
