package com.my9z.study;

import cn.hutool.json.JSONUtil;
import com.my9z.study.pojo.User;
import com.my9z.study.util.ESIndexUtil;
import com.my9z.study.util.EsDocumentUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @description: es操作封装类测试
 * @author: kim
 * @createTime: 2022-11-07  14:56
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class ESUtilTest {

    @Autowired
    private ESIndexUtil esIndexUtil;

    @Autowired
    private EsDocumentUtil esDocumentUtil;

    @Test
    public void createIndexTest() {
        String mapping = "\"properties\": {\n" +
                "      \"id\": {\n" +
                "        \"type\": \"keyword\",\n" +
                "        \"index\": true\n" +
                "      },\n" +
                "      \"name\": {\n" +
                "        \"type\": \"keyword\",\n" +
                "        \"index\": true\n" +
                "      },\n" +
                "      \"sex\": {\n" +
                "        \"type\": \"keyword\",\n" +
                "        \"index\": true\n" +
                "      },\n" +
                "      \"age\": {\n" +
                "        \"type\": \"long\",\n" +
                "        \"index\": true\n" +
                "      }\n" +
                "    }";
        esIndexUtil.createIndex("user", 3, 1, mapping);
    }

    @Test
    public void deleteIndexTest() {
        esIndexUtil.deleteIndex("q");
        esIndexUtil.deleteIndex("wczy");
    }

    @Test
    public void existsIndexTest() {
        log.info(esIndexUtil.existsIndex("q").toString());
        log.info(esIndexUtil.existsIndex("wczy").toString());
    }

    @Test
    public void insertDocTest() {
        User user = User.builder().id("1").name("wcz").age(12).sex("男").build();
        Boolean insert = esDocumentUtil.insert("user", user, User::getId);
        log.info("insert:{}", insert);
    }

    @Test
    public void getDocByIdTest(){
        User user = esDocumentUtil.getDocById("user", "1", User.class);
        log.info("user:{}", JSONUtil.toJsonStr(user));
    }


}
