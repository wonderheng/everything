package top.wonderheng.everything.core.index;

import top.wonderheng.everything.config.HandlerPath;

/**
 * @BelongsProject: everything
 * @BelongsPackage: top.wonderheng.everything.core.index
 * @Author: WonderHeng
 * @CreateTime: 2018-11-15 21:21
 */
public interface FileWatcher {
    void start();

    void monitor(HandlerPath handlerPath);

    void stop();
}
