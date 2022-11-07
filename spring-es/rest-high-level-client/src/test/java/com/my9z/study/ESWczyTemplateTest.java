package com.my9z.study;

import com.my9z.study.util.ESWczyTemplate;
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
public class ESWczyTemplateTest {

    @Autowired
    private ESWczyTemplate esWczyTemplate;

    @Test
    public void createIndex() {
        esWczyTemplate.createIndex("wczy", 3, 1, null);
    }
}
