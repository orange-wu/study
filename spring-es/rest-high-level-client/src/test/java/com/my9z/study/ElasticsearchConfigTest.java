package com.my9z.study;

import cn.hutool.json.JSONUtil;
import com.my9z.study.properties.ESConfigProperties;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @description: es配置单元测试
 * @author: wczy9
 * @createTime: 2022-11-02  15:43
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class ElasticsearchConfigTest {

    @Autowired
    private ESConfigProperties esConfigProperties;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Test
    public void testESConfigProperties() {
        log.info("esConfigProperties：{}", JSONUtil.toJsonStr(esConfigProperties));
    }

    @SneakyThrows
    @Test
    public void testRestHighLevelClient(){
        log.info("getIndexResponse1:{}",JSONUtil.toJsonStr(restHighLevelClient));
    }
}
