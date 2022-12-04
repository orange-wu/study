package com.my9z.study.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: 模拟dao层
 * @author: wczy9
 * @createTime: 2022-11-17  21:21
 */
public class UserDao {

    private static final Map<String, String> hashMap = new HashMap<>();

    static {
        hashMap.put("10001", "wcz");
        hashMap.put("10002", "czy");
        hashMap.put("10003", "wczy");
    }

    public String queryUserName(String uId) {
        return hashMap.get(uId);
    }

}