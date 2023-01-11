package com.my9z.study.factory;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import com.my9z.study.enums.ErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;

import java.util.Map;

/**
 * @description: RocketMQ生产者工厂类
 * @author: kim
 * @createTime: 2023-01-11  15:40
 */
@Slf4j
public class MQProducerFactory {

    private MQProducerFactory() {
    }

    private static class SingletonInstance {
        private static final MQProducerFactory INSTANCE = new MQProducerFactory();
    }

    /**
     * 通过调用静态内部类属性获取MQProducerFactory的单例对象
     *
     * @return MQProducerFactory单例对象
     */
    public static MQProducerFactory getInstance() {
        return SingletonInstance.INSTANCE;
    }

    //默认生产者本地缓存
    private static final Map<String, DefaultMQProducer> defaultProducers = Maps.newConcurrentMap();

    /**
     * 创建一个新的producer并返回，producerGroup为keu，新建的producer为value存入默认生产者本地缓存。
     * 如果producerGroup键值已存在producer,则直接替换
     *
     * @param nameServerAddress rocketMQ nameServer 地址
     * @param producerGroup     生产者组名
     * @return DefaultMQProducer
     */
    public DefaultMQProducer createMQProducer(String nameServerAddress, String producerGroup) {
        if (StrUtil.isEmpty(nameServerAddress) || StrUtil.isEmpty(producerGroup)) {
            log.error("MQProducerFactory createMQProducer nameServerAddress or producerGroup is empty");
            throw ErrorCodeEnum.NAME_SERVER_OR_GROUP_IS_EMPTY.buildException();
        }
        DefaultMQProducer defaultMQProducer = defaultProducers.get(producerGroup);
        if (defaultMQProducer == null) {
            DefaultMQProducer producer = new DefaultMQProducer(producerGroup);
            producer.setNamesrvAddr(nameServerAddress);
            defaultProducers.put(producerGroup, producer);
            return producer;
        }
        return defaultMQProducer;
    }

    /**
     * 从默认生产者本地缓存中获取producerGroup对应的producer
     *
     * @param producerGroup 生产者组名
     * @return DefaultMQProducer
     */
    public DefaultMQProducer getMQProducer(String producerGroup) {
        if (StrUtil.isEmpty(producerGroup)) {
            log.error("MQProducerFactory getMQProducer producerGroup is empty");
            throw ErrorCodeEnum.GROUP_IS_EMPTY.buildException();
        }
        return defaultProducers.get(producerGroup);
    }

    /**
     * 停止producerGroup对应的producer，但不从默认生产者本地缓存中移除
     *
     * @param producerGroup 生产者组名
     */
    public void stopMQProducer(String producerGroup) {
        if (StrUtil.isEmpty(producerGroup)) {
            log.error("MQProducerFactory stopMQProducer producerGroup is empty");
            throw ErrorCodeEnum.GROUP_IS_EMPTY.buildException();
        }
        DefaultMQProducer defaultMQProducer = defaultProducers.get(producerGroup);
        if (defaultMQProducer != null) {
            defaultMQProducer.shutdown();
        }
    }

    /**
     * 停止producerGroup对应的producer，并从默认生产者本地缓存中移除
     *
     * @param producerGroup 生产者组名
     */
    public void removeMQProducer(String producerGroup) {
        if (StrUtil.isEmpty(producerGroup)) {
            log.error("MQProducerFactory removeMQProducer producerGroup is empty");
            throw ErrorCodeEnum.GROUP_IS_EMPTY.buildException();
        }
        DefaultMQProducer defaultMQProducer = defaultProducers.get(producerGroup);
        if (defaultMQProducer != null) {
            defaultMQProducer.shutdown();
            //确认成功将生产者关闭后，再从缓存中移除
            defaultProducers.remove(producerGroup);
        }
    }

}
