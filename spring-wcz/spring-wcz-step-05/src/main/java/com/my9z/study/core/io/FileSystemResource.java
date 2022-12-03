package com.my9z.study.core.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * @description: 文件资源
 * @author: wczy9
 * @createTime: 2022-12-03  14:42
 */
public class FileSystemResource implements Resource{

    private final File file;

    private final String path;

    public FileSystemResource(File file) {
        this.file = file;
        this.path = file.getPath();
    }

    public FileSystemResource(String path) {
        this.file = new File(path);
        this.path = path;
    }

    public final String getPath() {
        return this.path;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return Files.newInputStream(file.toPath());
    }
}