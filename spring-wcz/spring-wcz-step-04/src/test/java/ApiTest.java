import bean.UserDao;
import bean.UserService;
import com.my9z.study.beans.PropertyValue;
import com.my9z.study.beans.PropertyValues;
import com.my9z.study.beans.factory.config.BeanDefinition;
import com.my9z.study.beans.factory.config.BeanReference;
import com.my9z.study.beans.factory.support.DefaultListableBeanFactory;
import org.junit.Test;

/**
 * @description: 测试类
 * @author: wczy9
 * @createTime: 2022-11-17  21:20
 */
public class ApiTest {

    @Test
    public void testBeanFactory(){
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerBeanDefinition("userDao",new BeanDefinition(UserDao.class));
        PropertyValues propertyValues = new PropertyValues();
        PropertyValue uId = new PropertyValue("uId", "10002");
        PropertyValue userDao = new PropertyValue("userDao", new BeanReference("userDao"));
        propertyValues.addPropertyValue(uId);
        propertyValues.addPropertyValue(userDao);
        BeanDefinition userServiceDefinition = new BeanDefinition(UserService.class, propertyValues);
        beanFactory.registerBeanDefinition("userService",userServiceDefinition);

        UserService userService = (UserService) beanFactory.getBean("userService");
        userService.queryUserInfo();

    }

}