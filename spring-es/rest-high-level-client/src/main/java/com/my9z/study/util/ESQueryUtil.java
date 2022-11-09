package com.my9z.study.util;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.json.JSONUtil;
import com.my9z.study.pojo.Page;
import com.my9z.study.util.builder.query.ESQuery;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    /**
     * es查询封装
     * @param query ESQuery
     * @param clazz es返回的对象类型
     * @return 查询返回的对象集合
     */
    public <T> List<T> search(ESQuery query, Class<T> clazz) {
        //组装es查询请求对象
        SearchRequest searchRequest = getSearchRequest(query);
        //客户端发起请求 获取相应
        SearchResponse searchResponse = getSearchResponse(query, searchRequest);
        if (Objects.isNull(searchResponse))
            return ListUtil.empty();
        //将es查询响应中的结果反序列化为对应的实体对象
        return Stream.of(searchResponse.getHits().getHits())
                .map(hit -> JSONUtil.toBean(hit.getSourceAsString(), clazz))
                .collect(Collectors.toList());
    }

    /**
     * es分页查询
     * @param query ESQuery
     * @param clazz es返回的对象类型
     * @return Page对象
     */
    public <T> Page<T> searchPage(ESQuery query, Class<T> clazz) {
        //组装es查询请求对象
        SearchRequest searchRequest = getSearchRequest(query);
        //客户端发起请求 获取相应
        SearchResponse searchResponse = getSearchResponse(query, searchRequest);
        if (Objects.isNull(searchResponse))
            return new Page<>();
        //将es查询响应中的结果反序列化为对应的实体对象
        List<T> data = Stream.of(searchResponse.getHits().getHits())
                .map(hit -> JSONUtil.toBean(hit.getSourceAsString(), clazz))
                .collect(Collectors.toList());
        //获取命中的总数
        long totalCount = searchResponse.getHits().getTotalHits().value;
        return new Page<>(data, totalCount, query.getPageSize(), query.getCurrentPage());
    }

    /**
     * 构建es查询请求对象
     */
    private SearchRequest getSearchRequest(ESQuery query) {
        return new SearchRequest()
                .indices(query.getIndexes().toArray(new String[0]))
                .source(query.getSearchSourceBuilder());
    }

    /**
     * 客户端发送查询请求 返回查询响应
     */
    private SearchResponse getSearchResponse(ESQuery query, SearchRequest searchRequest) {
        SearchResponse searchResponse = null;
        try {
            searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.warn("es search request fail esQuery:{}", JSONUtil.toJsonStr(query), e);
        }
        return searchResponse;
    }
}
