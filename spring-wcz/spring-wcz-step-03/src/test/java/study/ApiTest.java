package study;

import com.my9z.study.beans.factory.config.BeanDefinition;
import com.my9z.study.beans.factory.support.DefaultListableBeanFactory;
import com.my9z.study.beans.factory.support.SimpleInstantiationStrategy;
import org.junit.Test;
import study.bean.UserService;

/**
 * @description: 测试类
 * @author: kim
 * @createTime: 2022-11-16  17:09
 */
public class ApiTest {

    @Test
    public void testBeanFactory() {
        //初始化beanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.setInstantiationStrategy(new SimpleInstantiationStrategy());
        //注册bean
        BeanDefinition beanDefinition = new BeanDefinition(UserService.class);
        beanFactory.registerBeanDefinition("userService", beanDefinition);
        //获取bean
        UserService userService = (UserService) beanFactory.getBean("userService", "my9z", 1);
        userService.queryUserInfo();
    }

}
