package com.my9z.study;

import cn.hutool.json.JSONUtil;
import com.my9z.study.pojo.User;
import com.my9z.study.util.ESIndexUtil;
import com.my9z.study.util.ESQueryUtil;
import com.my9z.study.util.EsDocumentUtil;
import com.my9z.study.util.builder.query.ESQuery;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

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

    @Autowired
    private ESQueryUtil esQueryUtil;

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
    public void getDocByIdTest() {
        User user = esDocumentUtil.getDocById("user", "1", User.class);
        log.info("user:{}", JSONUtil.toJsonStr(user));
    }

    @Test
    public void existsDocTest() {
        boolean result = esDocumentUtil.existsDoc("user1", "1");
        log.info("result:{}", result);
    }

    @Test
    public void deleteDocByIdTest() {
        boolean result = esDocumentUtil.deleteDoc("user", "1");
        log.info("result:{}", result);
    }

    @Test
    public void updateDocByIdTest() {
        User user = User.builder().age(25).name("wczy").build();
        User result = esDocumentUtil.updateDocById("user", "1", user, User.class);
        log.info("result:{}", result);
    }

    @Test
    public void insertBatchTest() {
        ArrayList<User> users = new ArrayList<>();
        for (int i = 2; i <= 8; i++) {
            User user = User.builder().age(25).name("wczy==" + i).id(String.valueOf(i)).build();
            users.add(user);
        }
        Boolean insertBatch = esDocumentUtil.insertBatch(users, "user", User::getId);
        log.info("result:{}", insertBatch);
    }

    @Test
    public void searchTest(){
        ESQuery esQuery = ESQuery.builder()
                .must()
                .termQuery("name", "wczy")
                .done()
                .indexes("user")
                .trackTotalHits(true)
                .build();
        esQueryUtil.search(esQuery);
    }

}
