package study;

import com.my9z.study.beans.factory.config.BeanDefinition;
import com.my9z.study.beans.factory.support.DefaultListableBeanFactory;
import org.junit.Test;
import study.bean.UserService;

/**
 * @description: 测试类
 * @author: wczy9
 * @createTime: 2022-11-10  20:27
 */
public class ApiTest {

    @Test
    public void testBeanFactory() {
        //1.初始化BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        //2.注入bean
        BeanDefinition beanDefinition = new BeanDefinition(UserService.class);
        beanFactory.registerBeanDefinition("userService",beanDefinition);
        //3.获取bean 验证是否创建单例对象
        UserService userService = (UserService)beanFactory.getBean("userService");
        userService.queryUserInfo();
        //4.再次获取bean 验证是否从缓存中获取单例对象
        UserService userService_singleton = (UserService)beanFactory.getBean("userService");
        userService_singleton.queryUserInfo();
    }

}