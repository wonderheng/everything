package top.wonderheng.everything.core.interceptor.impl;

import top.wonderheng.everything.core.interceptor.FileInterceptor;

import java.io.File;

/**
 * @BelongsProject: everything
 * @BelongsPackage: top.wonderheng.everything.core.interceptor.impl
 * @Author: WonderHeng
 * @CreateTime: 2018-11-15 21:22
 */
public class FilePrintInterceptor implements FileInterceptor {

    @Override
    public void applyFile(File file) {
        System.out.println(String.format("%s:%s", file.isDirectory() ? "d" : "f", file.getName()));
    }
}
