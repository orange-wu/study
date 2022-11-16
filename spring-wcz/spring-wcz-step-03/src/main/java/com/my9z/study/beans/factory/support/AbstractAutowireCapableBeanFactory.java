package com.my9z.study.beans.factory.support;

import com.my9z.study.beans.BeansException;
import com.my9z.study.beans.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;

/**
 * @description: 实现默认bean创建的抽象bean工厂基类
 * 继承AbstractBeanFactory类，用于实现创建对象的具体功能
 * @author: wczy9
 * @createTime: 2022-11-15  22:03
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
                    if (!args[i].getClass().getName().equals(parameterTypes[i].getName())) {
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

    public InstantiationStrategy getInstantiationStrategy() {
        return instantiationStrategy;
    }

    public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
        this.instantiationStrategy = instantiationStrategy;
    }
}
