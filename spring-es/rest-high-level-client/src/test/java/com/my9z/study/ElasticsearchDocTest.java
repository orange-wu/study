package com.my9z.study;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @description: es配置单元测试
 * @author: wczy9
 * @createTime: 2022-11-03  23:38
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class ElasticsearchDocTest {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

}
