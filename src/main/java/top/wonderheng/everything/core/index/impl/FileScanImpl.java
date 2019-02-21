package top.wonderheng.everything.core.index.impl;

import top.wonderheng.everything.config.EverythingConfig;
import top.wonderheng.everything.core.index.FileScan;
import top.wonderheng.everything.core.interceptor.FileInterceptor;

import java.io.File;
import java.util.LinkedList;

/**
 * Author: wonderheng
 * Created: 2019/2/15
 */
public class FileScanImpl implements FileScan {
    
    private EverythingConfig config = EverythingConfig.getInstance();
    
    private LinkedList<FileInterceptor> interceptors = new LinkedList<>();
    
    @Override
    public void index(String path) {
        File file = new File(path);
        if (file.isFile()) {
            //D:\a\b\abc.pdf  ->  D:\a\b
            if (config.getExcludePath().contains(file.getParent())) {
                return;
            }
        } else {
            if (config.getExcludePath().contains(path)) {
                return;
            } else {
                File[] files = file.listFiles();
                if (files != null) {
                    for (File f : files) {
                        index(f.getAbsolutePath());
                    }
                }
            }
        }
        
        //File Directory
        for (FileInterceptor interceptor : this.interceptors) {
            interceptor.apply(file);
        }
    }
    
    @Override
    public void interceptor(FileInterceptor interceptor) {
        this.interceptors.add(interceptor);
    }
}
