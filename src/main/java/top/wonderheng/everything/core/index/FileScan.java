package top.wonderheng.everything.core.index;

import top.wonderheng.everything.core.interceptor.impl.FileIndexInterceptor;

/**
 * @BelongsProject: everything
 * @BelongsPackage: top.wonderheng.everything.core.index
 * @Author: WonderHeng
 * @CreateTime: 2018-11-15 21:20
 */
public interface FileScan {

    void index(String path);

    void interceptor(FileIndexInterceptor indexInterceptor);

}
