package com.my9z.study.core.factory;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import com.my9z.study.common.enums.ErrorCodeEnum;
import com.my9z.study.core.base.ConsumerListener;
import com.my9z.study.core.base.MQPushConsumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.util.Map;

/**
 * @description: RocketMQ消费者工厂类
 * @author: wczy9
 * @createTime: 2023-01-17  15:47
 */
@Slf4j
public class MQConsumerFactory {

    private MQConsumerFactory() {

    }

    private static class SingletonInstance {
        private static final MQConsumerFactory INSTANCE = new MQConsumerFactory();
    }

    /**
     * 通过调用静态内部类属性获取MQConsumerFactory的单例对象
     *
     * @return MQConsumerFactory单例对象
     */
    public static MQConsumerFactory getInstance() {
        return SingletonInstance.INSTANCE;
    }

    //消费者本地缓存
    private static final Map<String, MQPushConsumer<?, ?>> pushConsumers = Maps.newConcurrentMap();

    /**
     * 创建一个pushConsumer存入本地缓存，如果对应uniqueKey已存在对应consumer，则直接替换
     *
     * @param uniqueKey         唯一缓存key
     * @param nameServerAddress rocketMQ nameServer 地址
     * @param consumerGroup     消费者组
     * @param consumerListener  消费监听器
     * @param isCluster         是否集群消费
     * @param <S>               有序消费或并行消费返回的消息状态
     * @param <C>               有序消费或并行消费的消费上下文
     * @return MQPushConsumer
     */
    public <S, C> MQPushConsumer<S, C> createMqPushConsumer(String uniqueKey, String nameServerAddress, String consumerGroup,
                                                            ConsumerListener<S, C> consumerListener, Boolean isCluster) {
        if (StrUtil.isEmpty(nameServerAddress) || StrUtil.isEmpty(consumerGroup)) {
            log.error("MQConsumerFactory createMqPushConsumer nameServerAddress or consumerGroup is empty");
            throw ErrorCodeEnum.NAME_SERVER_OR_GROUP_IS_EMPTY.buildException();
        }
        MQPushConsumer<S, C> newConsumer = new MQPushConsumer<>(consumerGroup, consumerListener);
        newConsumer.setNamesrvAddr(nameServerAddress);
        newConsumer.setMessageModel(isCluster ? MessageModel.CLUSTERING : MessageModel.BROADCASTING);
        newConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        pushConsumers.put(uniqueKey, newConsumer);
        return newConsumer;
    }

    /**
     * 获取对应consumer
     *
     * @param uniqueKey 唯一缓存key
     * @return MQPushConsumer
     */
    public MQPushConsumer<?, ?> getMQPushConsumer(String uniqueKey) {
        if (StrUtil.isEmpty(uniqueKey)) {
            log.error("MQProducerFactory stopMQProducer producerGroup is empty");
            throw ErrorCodeEnum.UNIQUE_KEY_IS_EMPTY.buildException();
        }
        return pushConsumers.get(uniqueKey);
    }

    /**
     * 停止对应consumer
     *
     * @param uniqueKey 唯一缓存key
     */
    public void stopMQPushConsumer(String uniqueKey) {
        if (StrUtil.isEmpty(uniqueKey)) {
            log.error("MQProducerFactory stopMQProducer producerGroup is empty");
            throw ErrorCodeEnum.UNIQUE_KEY_IS_EMPTY.buildException();
        }
        MQPushConsumer<?, ?> mqPushConsumer = pushConsumers.get(uniqueKey);
        if (mqPushConsumer != null) {
            mqPushConsumer.shutdown();
        }

    }

    /**
     * 停止对应consumer并从本地缓存中移除
     *
     * @param uniqueKey 唯一缓存key
     */
    public void removeMQPushConsumer(String uniqueKey) {
        if (StrUtil.isEmpty(uniqueKey)) {
            log.error("MQProducerFactory stopMQProducer producerGroup is empty");
            throw ErrorCodeEnum.UNIQUE_KEY_IS_EMPTY.buildException();
        }
        MQPushConsumer<?, ?> mqPushConsumer = pushConsumers.get(uniqueKey);
        if (mqPushConsumer != null) {
            mqPushConsumer.shutdown();
            pushConsumers.remove(uniqueKey);
        }
    }

    /**
     * 停止并删除所有消费者
     */
    public void destroy() {
        pushConsumers.forEach((uniqueId, consumer) -> consumer.shutdown());
        pushConsumers.clear();
    }


}