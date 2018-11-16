package top.wonderheng.everything.config;

import lombok.Data;

import java.util.Set;

/**
 * @BelongsProject: everything
 * @BelongsPackage: top.wonderheng.everything.config
 * @Author: WonderHeng
 * @CreateTime: 2018-11-15 21:10
 */
@Data
public class HandlerPath {

    private final Set<String> includes;

    private final Set<String> excludes;
}
