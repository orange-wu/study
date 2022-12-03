package com.my9z.study.core.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * @description: 资源处理接口
 * @author: wczy9
 * @createTime: 2022-12-03  14:14
 */
public interface Resource {

    /**
     * 获取inputStream流
     *
     * @return InputStream
     * @throws IOException io异常
     */
    InputStream getInputStream() throws IOException;

}