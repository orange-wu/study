package com.my9z.study.core.io;

import cn.hutool.core.lang.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * @description: url资源
 * @author: wczy9
 * @createTime: 2022-12-03  15:15
 */
public class UrlResource implements Resource {

    private final URL url;

    public UrlResource(URL url) {
        Assert.notNull(url, "URL must not be null");
        this.url = url;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        URLConnection connection = url.openConnection();
        try {
            return connection.getInputStream();
        } catch (IOException e) {
            if (connection instanceof HttpURLConnection) {
                ((HttpURLConnection) connection).disconnect();
            }
            throw e;
        }
    }
}