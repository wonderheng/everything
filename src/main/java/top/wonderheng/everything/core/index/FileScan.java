package top.wonderheng.everything.core.index;

import top.wonderheng.everything.core.interceptor.FileInterceptor;

/**
 * Author: wonderheng
 * Created: 2019/2/15
 */
public interface FileScan {
    
    /**
     * 遍历Path
     *
     * @param path
     */
    void index(String path);
    
    /**
     * 遍历的拦截器
     *
     * @param interceptor
     */
    void interceptor(FileInterceptor interceptor);
}