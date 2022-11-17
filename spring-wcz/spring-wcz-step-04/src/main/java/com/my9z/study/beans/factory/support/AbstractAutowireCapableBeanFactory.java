package com.my9z.study.beans.factory.support;

import com.my9z.study.beans.BeansException;
import com.my9z.study.beans.PropertyValue;
import com.my9z.study.beans.PropertyValues;
import com.my9z.study.beans.factory.config.BeanDefinition;
import com.my9z.study.beans.factory.config.BeanReference;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

/**
 * @description: 实现默认bean创建的抽象bean工厂基类
 * 继承AbstractBeanFactory类，用于实现创建对象的具体功能
 * @author: kim
 * @createTime: 2022-11-16  19:22
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory {

    /**
     * 实例化Bean的默认策略为Cglib实现
     */
    private InstantiationStrategy instantiationStrategy = new CglibSubclassingInstantiationStrategy();

    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) {
        Object bean;
        try {
            //调用newInstance方法实例化bean
            bean = createBeanInstance(beanDefinition, beanName, args);
            //填充bean属性
            applyPropertyValues(beanName, bean, beanDefinition);
        } catch (Exception e) {
            throw new BeansException("Instantiation of " + beanName + " bean failed", e);
        }
        //注册进单例表（AbstractBeanFactory继承了DefaultSingletonBeanRegistry，所以该类有注册单例对象的能力）
        registerSingleton(beanName, bean);
        //返回实例化的bean对象
        return bean;
    }

    /**
     * 通过bean实例化具体策略创建Bean对象
     *
     * @param beanDefinition BeanDefinition对象
     * @param beanName       bean的名称
     * @param args           实例化入参
     * @return 实例化之后的bean对象
     */
    protected Object createBeanInstance(BeanDefinition beanDefinition, String beanName, Object[] args) {
        //获取需要实例化bean对象的class对象
        Class<?> beanClass = beanDefinition.getBeanClass();
        //获取所有的构造函数对象
        Constructor<?>[] declaredConstructors = beanClass.getDeclaredConstructors();
        //声明与入参信息匹配的构造函数对象
        Constructor<?> constructorToUse = null;
        //遍历查找args对应的构造函数
        findConstructor:
        for (Constructor<?> constructor : declaredConstructors) {
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            //先通过入参的个数来匹配构造函数
            if (null != args && parameterTypes.length == args.length) {
                //再通过构造函数入参类型比对来确定构造函数
                for (int i = 0; i < args.length; i++) {
                    //如果有一个入参类型和arg类型不对就跳出
                    if (!args[i].getClass().isAssignableFrom(parameterTypes[i])) {
                        break;
                    }
                    //比到最后一个还没跳出则认为当前构造函数是可以被选择的，结束找构造函数的流程
                    if (i == args.length - 1) {
                        constructorToUse = constructor;
                        break findConstructor;
                    }
                }
            }
        }
        //调用实例化具体策略创建bean
        return getInstantiationStrategy().instantiate(beanDefinition, beanName, constructorToUse, args);
    }

    /**
     * 注入bean对象的依赖属性和对象
     *
     * @param beanName       需要实例化的bean名称
     * @param bean           实例化后的bean
     * @param beanDefinition beanDefinition对象，包含PropertyValues信息
     */
    private void applyPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) {
        //获取bean对象定义的属性信息
        PropertyValues propertyValues = beanDefinition.getPropertyValues();
        if (propertyValues == null || propertyValues.getPropertyValues() == null
                || propertyValues.getPropertyValues().length == 0)
            return;
        //遍历bean对象依赖的属性信息
        for (PropertyValue propertyValue : propertyValues.getPropertyValues()) {
            Object value = propertyValue.getValue();
            //如果是引用对象则从容器中获取（相当于递归了，所以会有循环依赖）
            if (value instanceof BeanReference) {
                value = getBean(((BeanReference) value).getBeanName());
            }
            String name = propertyValue.getName();
            try {
                //对应的属性赋值
                setFieldValue(bean, name, value);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new BeansException("Error setting property values:" + beanName + "fieldName:" + name + "fieldValue:" + value, e);
            }
        }
    }

    /**
     * 给bean对象的某个属性赋值
     *
     * @param bean       实例化的bean对象
     * @param fieldName  字段名
     * @param fieldValue 字段值
     * @throws NoSuchFieldException   没有找到当前属性时报错
     * @throws IllegalAccessException 设置属性出错时抛出异常
     */
    private void setFieldValue(Object bean, String fieldName, Object fieldValue) throws NoSuchFieldException, IllegalAccessException {
        //获取class对象
        Class<?> clazz = bean.getClass();
        //根据fieldName获取field对象
        Field field = clazz.getDeclaredField(fieldName);
        //设置访问权限
        if (!field.isAccessible())
            field.setAccessible(true);
        //获取field的类型
        Class<?> fieldType = field.getType();
        //field类型和fieldValue类型相同时进行赋值
        if (fieldType.isAssignableFrom(fieldValue.getClass()))
            field.set(bean, fieldValue);
    }

    public InstantiationStrategy getInstantiationStrategy() {
        return instantiationStrategy;
    }

    public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
        this.instantiationStrategy = instantiationStrategy;
    }
}
