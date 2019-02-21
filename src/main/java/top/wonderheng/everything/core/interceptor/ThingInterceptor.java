package top.wonderheng.everything.core.interceptor;

import top.wonderheng.everything.core.model.Thing;

/**
 * Author: wonderheng
 * Created: 2019/2/16
 */
@FunctionalInterface
public interface ThingInterceptor {
    
    void apply(Thing thing);
}
