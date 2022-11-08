package com.my9z.study.util.builder.boolquery;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

/**
 * @description: 模板模式, 子类通过覆写buildQuery方法来定义不同的查询连接方式:must,must_not,should,filter
 * @author: kim
 * @createTime: 2022-11-08  16:20
 */
public abstract class BoolQueryAbstractBuilder {

    protected BoolQueryBuilder boolQueryBuilder;

    protected BoolQueryAbstractBuilder(BoolQueryBuilder boolQueryBuilder) {
        this.boolQueryBuilder = boolQueryBuilder;
    }

    public abstract void buildQuery(QueryBuilder queryBuilder);

    /**
     * termQuery查询 完全匹配 字段类型建议为:keyword
     *
     * @param key   指定字段名
     * @param value 字段值
     * @return
     */
    public final BoolQueryAbstractBuilder termQuery(String key, Object value) {
        buildQuery(QueryBuilders.termQuery(key, value));
        return this;
    }

}
