package com.my9z.study.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description: RocketMQ消费者标签
 * @author: kim
 * @createTime: 2023-01-13  14:09
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MQConsumer {

    /**
     * 唯一Key
     * 一条消息可能会被不同消费者组消费，所以定义一个唯一key来区分。
     */
    String uniqueKey();

    /**
     * 最少消费线程数
     */
    int consumeThreadMin() default 4;

    /**
     * 最多消费线程数
     */
    int consumeThreadMax() default 6;

    /**
     * 主题
     */
    String topic();

    /**
     * 标签
     */
    String tag();

    /**
     * 消费组
     */
    String consumerGroup();

    /**
     * 是否集群消费
     */
    boolean isCluster() default true;

}
