package top.wonderheng.everything.core.monitor;

import top.wonderheng.everything.core.common.HandlePath;

/**
 * Author: wonderheng
 * Created: 2019/2/17
 */
public interface FileWatch {
    
    /**
     * 监听启动
     */
    void start();
    
    /**
     * 监听的目录
     */
    void monitor(HandlePath handlePath);
    
    /**
     * 监听停止
     */
    void stop();
}
