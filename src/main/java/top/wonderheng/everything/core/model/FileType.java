package top.wonderheng.everything.core.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @BelongsProject: everything
 * @BelongsPackage: top.wonderheng.everything.core.model
 * @Author: WonderHeng
 * @CreateTime: 2018-11-15 21:27
 */
public enum FileType {

    IMG("png", "jpeg", "jpg", "gif"),
    DOC("doc", "docx", "xls", "xlsx", "ppt", "pptx", "xls", "xlsx", "pdf", "txt"),
    RAR("zip", "rar", "7z"),
    BIN("exe", "msi", "sh", "jar"),
    OTHER("*");

    private Set<String> extend = new HashSet<>();

    FileType(String... extend) {
        this.extend.addAll(Arrays.asList(extend));
    }

    public static FileType lookup(String extend) {
        for (FileType fileType : values()) {
            if (fileType.extend.contains(extend)) {
                return fileType;
            }
        }
        return OTHER;
    }

    public static FileType lookupByName(String name) {
        for (FileType fileType : values()) {
            if (fileType.name().equalsIgnoreCase(name)) {
                return fileType;
            }
        }
        return OTHER;
    }
}
