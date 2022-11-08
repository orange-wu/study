package com.my9z.study.util;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description: RestHighLevelClient 查询常用API封装
 * @author: kim
 * @createTime: 2022-11-08  15:27
 */
@Slf4j
@Component
public class ESQueryUtil {

    @Autowired
    private RestHighLevelClient restHighLevelClient;


}
