package top.wonderheng.everything.core.interceptor.impl;

import top.wonderheng.everything.core.interceptor.FileInterceptor;

import java.io.File;

/**
 * Author: wonderheng
 * Created: 2019/2/15
 */
public class FilePrintInterceptor implements FileInterceptor {
    @Override
    public void apply(File file) {
        System.out.println(file.getAbsolutePath());
    }
}
