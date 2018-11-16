package top.wonderheng.everything.core.index.impl;

import top.wonderheng.everything.config.EverythingConfig;
import top.wonderheng.everything.core.index.FileScan;
import top.wonderheng.everything.core.interceptor.FileInterceptor;
import top.wonderheng.everything.core.interceptor.impl.FileIndexInterceptor;

import java.io.File;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

/**
 * @BelongsProject: everything
 * @BelongsPackage: top.wonderheng.everything.core.index.impl
 * @Author: WonderHeng
 * @CreateTime: 2018-11-15 21:18
 */
public class FileScanImpl implements FileScan {

    private List<FileInterceptor> interceptors = new LinkedList<>();

    private EverythingConfig everythingConfig = EverythingConfig.defaultConfig();

    @Override
    public void index(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        for (String exclude : everythingConfig.getExcludePaths()) {
            if (Paths.get(path).startsWith(exclude)) {
                return;
            }
        }
        if (file.isDirectory()) {
            File[] list = file.listFiles();
            if (list != null) {
                for (File f : list) {
                    index(f.getPath());
                }
            }
        }
        for (FileInterceptor interceptor : interceptors) {
            interceptor.applyFile(file);
        }
    }

    @Override
    public void interceptor(FileIndexInterceptor indexInterceptor) {
        this.interceptors.add(indexInterceptor);
    }
}
