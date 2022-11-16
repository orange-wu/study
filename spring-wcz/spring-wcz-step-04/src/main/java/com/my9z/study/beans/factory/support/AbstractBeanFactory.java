package com.my9z.study.beans.factory.support;

import com.my9z.study.beans.BeansException;
import com.my9z.study.beans.factory.BeanFactory;
import com.my9z.study.beans.factory.config.BeanDefinition;

/**
 * @description: 抽象的beanFactory基类 定义模版方法
 * AbstractBeanFactory：运用模版模式定义了一个流出标准的用于获取对象的抽象类，并采用职责分离的结构设计，
 * 继承DefaultSingletonBeanRegistry类，使用其提供的单例对象注册和获取功能，通过实现BeanFactory接口提供了一个功能单一的获取bean的方法，
 * 屏蔽了内部逻辑细节
 * @author: kim
 * @createTime: 2022-11-16  19:19
 */
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory {
    @Override
    public Object getBean(String name) throws BeansException {
        return getBean(name, (Object[]) null);
    }

    @Override
    public Object getBean(String name, Object... args) throws BeansException {
        return doGetBean(name, args);
    }


    protected Object doGetBean(String name, final Object[] args) {
        //从夫类DefaultSingletonBeanRegistry的单例bean容器中获取单例对象
        Object bean = getSingleton(name);
        //如果有对应的单例对象就直接返回
        if (bean != null) {
            return bean;
        }
        //如果没有对应的单例对象，则调用子类DefaultListableBeanFactory的getBeanDefinition方法来获取BeanDefinition对象
        BeanDefinition beanDefinition = getBeanDefinition(name);
        //再调用AbstractAutowireCapableBeanFactory的createBean方法创建bean对象并返回
        return createBean(name, beanDefinition, args);
    }

    /**
     * 获取BeanDefinition对象的模版方法 供子类实现（DefaultListableBeanFactory）
     *
     * @param beanName BeanDefinition对象的名称
     * @return BeanDefinition对象
     * @throws BeansException 不能实例化bean时抛出异常
     */
    protected abstract BeanDefinition getBeanDefinition(String beanName);

    /**
     * 根据BeanDefinition对象实例化相应的bean的模版方法 供子类实现（AbstractAutowireCapableBeanFactory）
     *
     * @param beanName       bean的名称
     * @param beanDefinition BeanDefinition对象
     * @param args           实例化构造函数所需的入参信息
     * @return 实例化之后的bean对象
     */
    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args);
}
