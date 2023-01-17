package com.my9z.study.core;

import cn.hutool.core.collection.CollUtil;
import com.my9z.study.common.enums.ErrorCodeEnum;
import com.my9z.study.core.annotation.MQConsumer;
import com.my9z.study.core.base.ConsumerListener;
import com.my9z.study.core.base.MQPushConsumer;
import com.my9z.study.core.factory.MQConsumerFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * @description: RocketMQ消费者初始化
 * @author: wczy9
 * @createTime: 2023-01-17  17:22
 */
@Slf4j
@Configuration
public class MQConsumerInit implements InitializingBean, ApplicationContextAware {

    @Value("${rocketmq.name-server}")
    private String nameSerAddress;

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@Nonnull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() {
        Map<String, ConsumerListener> consumerMap = applicationContext.getBeansOfType(ConsumerListener.class);
        if (CollUtil.isNotEmpty(consumerMap)) {
            consumerMap.forEach((beanName, consumerListener) -> {
                MQConsumer mqConsumerAnnotation = applicationContext.findAnnotationOnBean(beanName, MQConsumer.class);
                if (mqConsumerAnnotation == null) return;
                MQPushConsumer<?, ?> mqPushConsumer = MQConsumerFactory
                        .getInstance()
                        .createMqPushConsumer(mqConsumerAnnotation.uniqueKey(), nameSerAddress,
                                mqConsumerAnnotation.consumerGroup(), consumerListener, mqConsumerAnnotation.isCluster());
                try {
                    mqPushConsumer.setConsumeThreadMin(mqConsumerAnnotation.consumeThreadMin());
                    mqPushConsumer.setConsumeThreadMax(mqConsumerAnnotation.consumeThreadMax());
                    mqPushConsumer.subscribe(mqConsumerAnnotation.topic(), mqConsumerAnnotation.tag());
                    mqPushConsumer.start();
                    log.info("wcz rocketmq consumerGroup:{} mqPushConsumer start success", mqConsumerAnnotation.consumerGroup());
                } catch (MQClientException e) {
                    log.error("MQProducerInit consumerGroup:{} mqPushConsumer start error", mqConsumerAnnotation.consumerGroup(), e);
                    Runtime.getRuntime().addShutdownHook(new Thread(() -> MQConsumerFactory.getInstance().destroy()));
                    throw ErrorCodeEnum.CONSUMER_INIT_ERROR.buildException();
                }
            });
        }
    }

}