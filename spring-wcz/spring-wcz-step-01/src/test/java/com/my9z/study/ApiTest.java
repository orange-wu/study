package com.my9z.study;

import com.my9z.study.bean.UserService;
import org.junit.Test;

/**
 * @description: 测试类
 * @author: wczy9
 * @createTime: 2022-11-10  20:27
 */
public class ApiTest {

    @Test
    public void testBeanFactory() {
        //1.初始化BeanFactory
        BeanFactory beanFactory = new BeanFactory();
        //2.注入bean
        BeanDefinition beanDefinition = new BeanDefinition(new UserService());
        beanFactory.registerBeanDefinition("userService",beanDefinition);
        //3.获取bean
        UserService userService = (UserService)beanFactory.getBean("userService");
        userService.queryUserInfo();
    }

}