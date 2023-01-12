package com.my9z.study.core.factory;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import com.my9z.study.common.enums.ErrorCodeEnum;
import com.my9z.study.core.base.AbstractMQTransactionListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.TransactionMQProducer;

import java.util.Map;

/**
 * @description: RocketMQ生产者工厂类。事务消息的生产组名称 ProducerGroupName不能随意设置。事务消息有回查机制，
 * 回查时Broker端如果发现原始生产者已经崩溃，则会联系同一生产者组的其他生产者实例回查本地事务执行情况以Commit或Rollback半事务消息。
 * 所以不同的事务消息生产者需要存在不同的生产者组中。这是事务消息生产者分多个的意义
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

    //普通消息生产者
    private static DefaultMQProducer normalProducer;

    //事务消息生产者本地缓存
    private static final Map<String, TransactionMQProducer> transactionProducers = Maps.newConcurrentMap();

    /**
     * 创建一个新的普通消息producer，如果已存在normalProducer,则直接替换
     *
     * @param nameServerAddress rocketMQ nameServer 地址
     * @param producerGroup     生产者组名
     * @param timeOut           发送消息超时时间（毫秒）
     * @return DefaultMQProducer
     */
    public DefaultMQProducer createNormalMQProducer(String nameServerAddress, String producerGroup, Integer timeOut) {
        if (StrUtil.isEmpty(nameServerAddress) || StrUtil.isEmpty(producerGroup)) {
            log.error("MQProducerFactory createNormalMQProducer nameServerAddress or producerGroup is empty");
            throw ErrorCodeEnum.NAME_SERVER_OR_GROUP_IS_EMPTY.buildException();
        }
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer(producerGroup);
        defaultMQProducer.setNamesrvAddr(nameServerAddress);
        defaultMQProducer.setSendMsgTimeout(timeOut);
        normalProducer = defaultMQProducer;
        return normalProducer;
    }

    /**
     * 获取normalProducer
     *
     * @return DefaultMQProducer
     */
    public DefaultMQProducer getNormalMQProducer() {
        return normalProducer;
    }

    /**
     * 停止normalProducer
     */
    public void stopNormalMQProducer() {
        normalProducer.shutdown();
        normalProducer = null;
    }


    /**
     * 创建一个新的事务消息producer并返回，producerGroup为keu，新建的producer为value存入默认生产者本地缓存。
     * 如果producerGroup键值已存在producer,则直接替换
     *
     * @param nameServerAddress rocketMQ nameServer 地址
     * @param producerGroup     生产者组名
     * @return TransactionMQProducer
     */
    public TransactionMQProducer createTransactionMQProducer(String nameServerAddress, String producerGroup, AbstractMQTransactionListener listener) {
        if (StrUtil.isEmpty(nameServerAddress) || StrUtil.isEmpty(producerGroup)) {
            log.error("MQProducerFactory createTransactionMQProducer nameServerAddress or producerGroup is empty");
            throw ErrorCodeEnum.NAME_SERVER_OR_GROUP_IS_EMPTY.buildException();
        }
        TransactionMQProducer transactionProducer = transactionProducers.get(producerGroup);
        if (transactionProducer == null) {
            TransactionMQProducer producer = new TransactionMQProducer(producerGroup);
            producer.setNamesrvAddr(nameServerAddress);
            producer.setTransactionListener(listener);
            transactionProducers.put(producerGroup, producer);
            return producer;
        }
        return transactionProducer;
    }

    /**
     * 从事务消息生产者本地缓存中获取producerGroup对应的producer
     *
     * @param producerGroup 生产者组名
     * @return TransactionMQProducer
     */
    public TransactionMQProducer getTransactionMQProducer(String producerGroup) {
        if (StrUtil.isEmpty(producerGroup)) {
            log.error("MQProducerFactory getMQProducer producerGroup is empty");
            throw ErrorCodeEnum.GROUP_IS_EMPTY.buildException();
        }
        return transactionProducers.get(producerGroup);
    }

    /**
     * 停止producerGroup对应的producer，但不从默认生产者本地缓存中移除
     *
     * @param producerGroup 生产者组名
     */
    public void stopTransactionMQProducer(String producerGroup) {
        if (StrUtil.isEmpty(producerGroup)) {
            log.error("MQProducerFactory stopMQProducer producerGroup is empty");
            throw ErrorCodeEnum.GROUP_IS_EMPTY.buildException();
        }
        TransactionMQProducer transactionProducer = transactionProducers.get(producerGroup);
        if (transactionProducer != null) {
            transactionProducer.shutdown();
        }
    }

    /**
     * 停止producerGroup对应的producer，并从默认生产者本地缓存中移除
     *
     * @param producerGroup 生产者组名
     */
    public void removeTransactionMQProducer(String producerGroup) {
        if (StrUtil.isEmpty(producerGroup)) {
            log.error("MQProducerFactory removeMQProducer producerGroup is empty");
            throw ErrorCodeEnum.GROUP_IS_EMPTY.buildException();
        }
        TransactionMQProducer transactionProducer = transactionProducers.get(producerGroup);
        if (transactionProducer != null) {
            transactionProducer.shutdown();
            //确认成功将生产者关闭后，再从缓存中移除
            transactionProducers.remove(producerGroup);
        }
    }

    /**
     * 停止并删除所有生产者
     */
    public void destroy() {
        normalProducer.shutdown();
        normalProducer = null;
        transactionProducers.forEach((producerGroup, producer) -> producer.shutdown());
        transactionProducers.clear();
    }

}
