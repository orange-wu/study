package com.my9z.study.beans.factory.support;

import com.my9z.study.beans.factory.config.BeanDefinition;

/**
 * @description: BeanDefinition对象注册的接口
 * @author: kim
 * @createTime: 2022-11-16  19:21
 */
public interface BeanDefinitionRegistry {

    /**
     * 向BeanDefinition注册表中注册BeanDefinition对象
     *
     * @param beanName       bean名称
     * @param beanDefinition bean定义
     */
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

    /**
     * 判断指定名称的BeanDefinition是否已经注册
     * @param beanName bean名称
     * @return beanName是否已经注册
     */
    boolean containsBeanDefinition(String beanName);

}
