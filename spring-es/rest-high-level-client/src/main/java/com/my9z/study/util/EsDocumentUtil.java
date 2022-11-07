package com.my9z.study.util;

import cn.hutool.json.JSONUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;
import java.util.function.Function;

/**
 * @description: RestHighLevelClient document常用API封装
 * @author: kim
 * @createTime: 2022-11-07  17:19
 */
@Component
@Slf4j
public class EsDocumentUtil {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 向指定index中添加文档数据
     *
     * @param indexName  索引名词
     * @param docData    文档数据
     * @param idFunction id字段
     * @param <T>        文档类型
     * @param <R>        id字段类型
     * @return 是否添加成功
     */
    public <T extends Serializable, R> Boolean insert(@NonNull String indexName, @NonNull T docData, @NonNull Function<T, R> idFunction) {
        //保证id字段的值不为空
        R id = idFunction.apply(docData);
        if (Objects.isNull(id)) {
            log.warn("insert fail,because id is null,index:{},docData:{},idFunction:{}",
                    indexName, JSONUtil.toJsonStr(docData), idFunction);
        }
        //创建新增doc的请求对象
        IndexRequest indexRequest = new IndexRequest()
                //索引
                .index(indexName)
                //json格式的数据
                .source(JSONUtil.toJsonStr(docData), XContentType.JSON)
                //指定id
                .id(String.valueOf(id))
                //设置为true只做新增：若id已存在不做修改
                .create(true);
        //客户端发送请求 获取请求对象
        IndexResponse indexResponse;
        try {
            indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("insertDoc fail:ES请求超时或者服务器无响应", e);
            return false;
        } catch (ElasticsearchException e) {
            if (e.status() == RestStatus.CONFLICT) {
                //如果 opType 设置为 create 并且已经存在具有相同索引和 id 的文档 或者 存在版本冲突（这里没有设置版本，只能是id重复）
                log.warn("insertDoc fail:文档id重复,indexName:{},docData:{},idFunction:{},id:{}",
                        indexName, JSONUtil.toJsonStr(docData), idFunction, id);
            } else {
                log.warn("insertDoc fail:新增文档失败,indexName:{},doc:{}", indexName, JSONUtil.toJsonStr(docData), e);
            }
            return false;
        }
        boolean result = DocWriteResponse.Result.CREATED == indexResponse.getResult();
        log.info(result ? "insertDoc request success,indexName:{},doc:{},id:{}"
                        : "insertDoc request fail,indexName:{},doc:{},id:{}",
                indexName, JSONUtil.toJsonStr(docData), id);
        return result;
    }

    /**
     * 通过id在指定index中获取document
     *
     * @param indexName 索引名
     * @param id        id值
     * @param clazz     返回类型
     * @param <T>       文档类型
     * @return 查找到的document实体
     */
    public <T extends Serializable> T getDocById(@NonNull String indexName, @NonNull String id, @NonNull Class<T> clazz) {
        //指定index id构建请求对象
        GetRequest getRequest = new GetRequest().index(indexName).id(id);
        GetResponse getResponse;
        try {
            //客户端发送请求 获取响应
            getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("getDocById fail:ES请求超时或者服务器无响应", e);
            return null;
        } catch (ElasticsearchException e) {
            if (e.status() == RestStatus.NOT_FOUND) {
                //索引不存在
                log.warn("getDocById fail:索引不存在 indexName:{}", indexName, e);
            } else {
                log.warn("getDocById fail:查询失败 indexName:{},id:{}", indexName, id, e);
            }
            return null;
        }
        return JSONUtil.toBean(getResponse.getSourceAsString(), clazz);
    }

    /**
     * 查询指定index中某个id的document是否存在
     * @param indexName 索引名
     * @param id id值
     * @return index中某个id的数据是否存在
     */
    public Boolean existsDoc(@NonNull String indexName, @NonNull String id) {
        //指定index id构建请求对象
        GetRequest getRequest = new GetRequest().index(indexName).id(id);
        boolean result;
        try {
            //客户端发送请求 获取响应
            result = restHighLevelClient.exists(getRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("getDocById fail:ES请求超时或者服务器无响应", e);
            return null;
        }
        return result;
    }
}
