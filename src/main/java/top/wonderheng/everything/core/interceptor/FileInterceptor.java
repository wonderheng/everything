package top.wonderheng.everything.core.interceptor;

import java.io.File;

/**
 * Author: wonderheng
 * Created: 2019/2/15
 */
@FunctionalInterface
public interface FileInterceptor {

    void apply(File file);
}
