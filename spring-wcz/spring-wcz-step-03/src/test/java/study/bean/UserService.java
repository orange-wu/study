package study.bean;

/**
 * @description: 模拟userService bean对象
 * @author: kim
 * @createTime: 2022-11-16  17:11
 */
public class UserService {


    private String name;

    private Integer age;

    public UserService(String name) {
        this.name = name;
    }

    public UserService(String name, Integer age) {
        this.name = name;
        this.age = age;
    }


    public void queryUserInfo() {
        System.out.println("wcz query user info name:[" + name + "],age:[" + age + "]");
    }

}
