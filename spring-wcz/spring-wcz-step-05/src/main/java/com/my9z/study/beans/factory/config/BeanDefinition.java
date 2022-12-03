package com.my9z.study.beans.factory.config;

import com.my9z.study.beans.PropertyValues;

/**
 * @description: 定义Bean实例对象
 * @author: wczy9
 * @createTime: 2022-11-16  19:20
 */
public class BeanDefinition {

    /**
     * 定义bean的Class属性，将bean的实例化放到容器中处理
     */
    private Class<?> beanClass;

    /**
     * 定义bean对象中的属性和依赖对象
     */
    private PropertyValues propertyValues;

    public BeanDefinition(Class<?> beanClass) {
        this.beanClass = beanClass;
        this.propertyValues = new PropertyValues();
    }

    public BeanDefinition(Class<?> beanClass, PropertyValues propertyValues) {
        this.beanClass = beanClass;
        this.propertyValues = propertyValues != null ? propertyValues : new PropertyValues();
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public PropertyValues getPropertyValues() {
        return propertyValues;
    }

    public void setPropertyValues(PropertyValues propertyValues) {
        this.propertyValues = propertyValues;
    }

}
