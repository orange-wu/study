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
        //初始化BeanFactory接口
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        //注册userDao对象
        beanFactory.registerBeanDefinition("userDao",new BeanDefinition(UserDao.class));
        //userService对象设置属性
        PropertyValues propertyValues = new PropertyValues();
        PropertyValue uId = new PropertyValue("uId", "10002");
        PropertyValue userDao = new PropertyValue("userDao", new BeanReference("userDao"));
        propertyValues.addPropertyValue(uId);
        propertyValues.addPropertyValue(userDao);
        //注册userService对象
        BeanDefinition userServiceDefinition = new BeanDefinition(UserService.class, propertyValues);
        beanFactory.registerBeanDefinition("userService",userServiceDefinition);
        //获取userService对象
        UserService userService = (UserService) beanFactory.getBean("userService");
        userService.queryUserInfo();

    }

}