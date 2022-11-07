package com.my9z.study;

import com.my9z.study.util.ESIndexUtil;
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

    @Test
    public void createIndexTest() {
        esIndexUtil.createIndex("wczy", 3, 1, null);
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


}
