package com.my9z.study.util.builder.boolquery;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;

/**
 * @description: bool查询模板:must查询具体实现
 * @author: kim
 * @createTime: 2022-11-08  16:29
 */
public class MustBuilder extends BoolQueryAbstractBuilder {

    protected MustBuilder(BoolQueryBuilder boolQueryBuilder) {
        super(boolQueryBuilder);
    }

    @Override
    public void buildQuery(QueryBuilder queryBuilder) {
        boolQueryBuilder.must(queryBuilder);
    }
}
