package com.my9z.study.util;

import com.my9z.study.util.builder.query.ESQuery;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

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

    public void search(ESQuery query) {
        SearchRequest searchRequest = new SearchRequest()
                .indices(query.getIndexes().toArray(new String[0]))
                .source(query.getSearchSourceBuilder());
        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            long value = hits.getTotalHits().value;
            log.info("total:{}",value);
            for (SearchHit hit : hits) {
                log.info("hit : {}", hit.getSourceAsString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
