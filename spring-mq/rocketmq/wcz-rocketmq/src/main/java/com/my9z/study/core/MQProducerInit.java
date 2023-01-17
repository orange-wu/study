package com.my9z.study.core;

import cn.hutool.core.collection.CollUtil;
import com.my9z.study.common.constant.RocketMqConstant;
import com.my9z.study.common.enums.ErrorCodeEnum;
import com.my9z.study.core.annotation.MQTransactionListener;
import com.my9z.study.core.base.AbstractMQTransactionListener;
import com.my9z.study.core.factory.MQProducerFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * @description: RocketMQ生产者初始化
 * @author: kim
 * @createTime: 2023-01-12  09:32
 */
@Slf4j
@Configuration
public class MQProducerInit implements InitializingBean, ApplicationContextAware {

    @Value("${rocketmq.name-server}")
    private String nameSerAddress;

    @Value("${rocketmq.time-out:3000}")
    private Integer timeOut;

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@Nonnull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() {
        MQProducerFactory mqProducerFactory = MQProducerFactory.getInstance();
        //普通消息生产者
        DefaultMQProducer normalMQProducer = mqProducerFactory.createNormalMQProducer(nameSerAddress,
                RocketMqConstant.NORMAL_MESSAGE_PRODUCER_GROUP, timeOut);
        try {
            normalMQProducer.start();
            log.info("wcz rocketmq normalMQProducer start success");
        } catch (MQClientException e) {
            log.error("MQProducerInit normalMQProducer start error.nameSerAddress:{}", nameSerAddress, e);
            Runtime.getRuntime().addShutdownHook(new Thread(() -> MQProducerFactory.getInstance().destroy()));
            throw ErrorCodeEnum.PRODUCER_INIT_ERROR.buildException();
        }
        //事务消息生产者
        //1、根据AbstractMQTransactionListener子类获取事务消息监听类
        Map<String, AbstractMQTransactionListener> transactionListenerMap =
                applicationContext.getBeansOfType(AbstractMQTransactionListener.class);
        //2、遍历事务消息监听类，新建事务消息生产者
        if (CollUtil.isNotEmpty(transactionListenerMap)) {
            transactionListenerMap.forEach((beanName, listener) -> {
                MQTransactionListener transactionListenerAnno =
                        applicationContext.findAnnotationOnBean(beanName, MQTransactionListener.class);
                if (transactionListenerAnno == null) return;
                TransactionMQProducer transactionMQProducer =
                        mqProducerFactory.createTransactionMQProducer(nameSerAddress, transactionListenerAnno.producerGroup(), listener);
                try {
                    transactionMQProducer.start();
                    log.info("wcz rocketmq producerGroup:{} transactionMQProducer start success", transactionListenerAnno.producerGroup());
                } catch (MQClientException e) {
                    log.error("MQProducerInit producerGroup:{} transactionMQProducer start error", transactionListenerAnno.producerGroup(), e);
                    Runtime.getRuntime().addShutdownHook(new Thread(() -> MQProducerFactory.getInstance().destroy()));
                    throw ErrorCodeEnum.PRODUCER_INIT_ERROR.buildException();
                }
            });
        }
    }


}
