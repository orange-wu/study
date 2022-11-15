package com.my9z.study.beans.factory.config;

/**
 * @description: 定义Bean实例对象
 * @author: wczy9
 * @createTime: 2022-11-15  22:19
 */
public class BeanDefinition {

    /**
     * 定义bean的Class属性，将bean的实例化放到容器中处理
     */
    private Class<?> beanClass;

    public BeanDefinition(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }
}