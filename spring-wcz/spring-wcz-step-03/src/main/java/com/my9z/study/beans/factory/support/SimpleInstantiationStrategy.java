package com.my9z.study.beans.factory.support;

import com.my9z.study.beans.BeansException;
import com.my9z.study.beans.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @description: JDK实例化策略
 * @author: wczy9
 * @createTime: 2022-11-15  21:30
 */
public class SimpleInstantiationStrategy implements InstantiationStrategy {

    @Override
    public Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor<?> constructor,
                              Object[] args) throws BeansException {
        //获取bean的Class对象
        Class<?> beanClass = beanDefinition.getBeanClass();
        try {
            if (null != constructor) {
                //如果传入的构造函数信息不为空，就根据入参类型获取构造函数，并传入args参数进行实例化
                return beanClass.getDeclaredConstructor(constructor.getParameterTypes()).newInstance(args);
            } else {
                //如果传入的构造函数信息为空，获取无参构造函数直接进行实例化
                return beanClass.getDeclaredConstructor().newInstance();
            }
        } catch (InstantiationException | IllegalAccessException
                 | InvocationTargetException | NoSuchMethodException e) {
            throw new BeansException("Failed to instantiate [" + beanClass.getName() + "]");
        }

    }

}