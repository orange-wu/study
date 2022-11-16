package com.my9z.study.beans.factory.config;

/**
 * @description: 单例Bean注册表
 * @author: wczy9
 * @createTime: 2022-11-15  22:23
 */
public interface SingletonBeanRegistry {

    /**
     * 返回在给定名称下注册的原始单列对象
     *
     * @param name 要查找的bean的名称
     * @return 返回注册的单例对象
     */
    Object getSingleton(String name);

    /**
     * 注册单例对象
     *
     * @param name            bean对象名称
     * @param singletonObject bean对象
     */
    void registerSingleton(String name, Object singletonObject);

}
