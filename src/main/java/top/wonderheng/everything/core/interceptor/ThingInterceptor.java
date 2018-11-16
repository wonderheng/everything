package top.wonderheng.everything.core.interceptor;

import top.wonderheng.everything.core.model.Thing;

/**
 * @BelongsProject: everything
 * @BelongsPackage: top.wonderheng.everything.core.interceptor
 * @Author: WonderHeng
 * @CreateTime: 2018-11-15 21:25
 */
public interface ThingInterceptor {

    void applyFile(Thing thing);

}
