package top.wonderheng.everything.config;

import lombok.Data;

import java.io.File;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

/**
 * @BelongsProject: everything
 * @BelongsPackage: top.wonderheng.everything.config
 * @Author: WonderHeng
 * @CreateTime: 2018-11-15 21:09
 */
@Data
public class EverythingConfig {

    private static EverythingConfig everythingConfig;

    /**
     * 建立索引和监控的路径
     */
    private final Set<String> indexPaths = new HashSet<>();

    /**
     * 排除的路径
     */
    private final Set<String> excludePaths = new HashSet<>();

    /**
     * 最大近期文件数
     */
    private Integer maxRecentlyFile = 1024;

    /**
     * 最大返回结果数
     */
    private Integer maxReturnFile = 30;

    /**
     * 检索结果按照深度升序/降序，升序=true，降序=false，默认true
     */
    private boolean depthAsc = true;


    /**
     * 程序启动是否重建索引，是=true，否=false，默认false
     */
    private boolean rebuildIndex = false;

    /**
     * 索引文件
     */
    private String h2IndexFile = System.getProperty("user.home") + File.separator + "everything";


    private final HandlerPath handlerPath;

    private EverythingConfig() {
        this.handlerPath = new HandlerPath(indexPaths, excludePaths);
    }

    /**
     * 获取默认的配置信息
     *
     * @return
     */
    public static EverythingConfig defaultConfig() {
        if (everythingConfig == null) {
            synchronized(EverythingConfig.class) {
                if (everythingConfig == null) {
                    everythingConfig = new EverythingConfig();
                    FileSystem fileSystems = FileSystems.getDefault();
                    Iterable<Path> iterable = fileSystems.getRootDirectories();
                    iterable.forEach(path -> everythingConfig.indexPaths.add(path.toString()));
                    String os = System.getProperty("os.name");
                    if (os.contains("Windows")) {
                        everythingConfig.excludePaths.add("C:\\Windows");
                        everythingConfig.excludePaths.add("C:\\Program Files (x86)");
                        everythingConfig.excludePaths.add("C:\\Program Files");
                        everythingConfig.excludePaths.add("C:\\ProgramData");
                    } else {
                        everythingConfig.excludePaths.add("/root");
                    }
                }
            }
        }
        return everythingConfig;
    }

    public static EverythingConfig getInstance() {
        return defaultConfig();
    }
}
