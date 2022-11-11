package com.my9z.study.beans.factory.support;

import com.my9z.study.beans.BeansException;
import com.my9z.study.beans.factory.config.BeanDefinition;

/**
 * @description: 实现默认bean创建的抽象bean工厂基类
 * 继承AbstractBeanFactory类，用于实现创建对象的具体功能
 * @author: wczy9
 * @createTime: 2022-11-11  22:03
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory {

    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition) {
        Object bean;
        try {
            //调用newInstance方法实例化bean
            bean = beanDefinition.getBeanClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new BeansException("Instantiation of " + beanName + " bean failed", e);
        }
        //注册进单例表（AbstractBeanFactory继承了DefaultSingletonBeanRegistry，所以该类有注册单例对象的能力）
        registerSingleton(beanName, bean);
        //返回实例化的bean对象
        return bean;
    }
}