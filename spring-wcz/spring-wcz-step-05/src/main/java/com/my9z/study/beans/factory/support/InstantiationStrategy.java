package com.my9z.study.beans.factory.support;

import com.my9z.study.beans.BeansException;
import com.my9z.study.beans.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;

/**
 * @description: Bean实例化策略接口
 * @author: kim
 * @createTime: 2022-11-16  21:22
 */
public interface InstantiationStrategy {

    /**
     * bean实例化
     *
     * @param beanDefinition beanDefinition对象
     * @param beanName       bean名称
     * @param constructor    类的构造函数的相关信息
     * @param args           bean构造函数具体入参信息
     * @return 实例化的bean对象
     * @throws BeansException 不能实例化bean时抛出异常
     */
    Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor<?> constructor,
                       Object[] args) throws BeansException;

}
