package com.my9z.study.core.io;

import cn.hutool.core.lang.Assert;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @description: 资源加载器实现类，决定使用哪种资源处理器
 * @author: wczy9
 * @createTime: 2022-12-03  15:30
 */
public class DefaultResourceLoader implements ResourceLoader{

    @Override
    public Resource getResource(String location) {
        Assert.notNull(location, "Location must not be null");
        if (location.startsWith(CLASSPATH_URL_PREFIX)) {
            return new ClassPathResource(location.substring(CLASSPATH_URL_PREFIX.length()));
        }
        try {
            URL url = new URL(location);
            return new UrlResource(url);
        } catch (MalformedURLException e) {
            return new FileSystemResource(location);
        }
    }

}