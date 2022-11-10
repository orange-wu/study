package com.my9z.study.beans.factory.support;

import com.my9z.study.beans.BeansException;
import com.my9z.study.beans.factory.BeanFactory;
import com.my9z.study.beans.factory.config.BeanDefinition;

/**
 * @description: 抽象的beanFactory基类 定义模版方法
 * @author: wczy9
 * @createTime: 2022-11-10  22:41
 */
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory {


    @Override
    public Object getBean(String name) throws BeansException {
        return null;
    }

    /**
     * 获取BeanDefinition对象的模版方法 供子类实现（DefaultListableBeanFactory）
     *
     * @param name BeanDefinition对象的名称
     * @return BeanDefinition对象
     * @throws BeansException 不能实例化bean时抛出异常
     */
    protected abstract BeanDefinition getBeanDefinition(String name);

    /**
     * 根据BeanDefinition对象实例化相应的bean的模版方法 供子类实现（AbstractAutowireCapableBeanFactory）
     *
     * @param beanName       bean的名称
     * @param beanDefinition BeanDefinition对象
     * @return 实例化之后的bean对象
     */
    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition);
}