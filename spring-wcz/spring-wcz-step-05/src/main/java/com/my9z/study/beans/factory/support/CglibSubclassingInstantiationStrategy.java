package com.my9z.study.beans.factory.support;

import com.my9z.study.beans.BeansException;
import com.my9z.study.beans.factory.config.BeanDefinition;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

import java.lang.reflect.Constructor;

/**
 * @description: Cglib代理实例化bean策略
 * @author: kim
 * @createTime: 2022-11-16  19:23
 */
public class CglibSubclassingInstantiationStrategy implements InstantiationStrategy {
    @Override
    public Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor<?> constructor,
                              Object[] args) throws BeansException {
        //创建增强器Enhancer类对象
        Enhancer enhancer = new Enhancer();
        //设置代理目标
        enhancer.setSuperclass(beanDefinition.getBeanClass());
        //设置回调对象，在调用中拦截对目标方法的调用（NoOp.INSTANCE返回没有任何操作的拦截器）
        enhancer.setCallback(NoOp.INSTANCE);
        //如果传入的构造函数信息为空，直接创建对象
        if (null == constructor)
            return enhancer.create();
        //如果传入的构造函数信息对象不为空，调用对应参数的构造函数实例化对象
        return enhancer.create(constructor.getParameterTypes(), args);
    }
}
