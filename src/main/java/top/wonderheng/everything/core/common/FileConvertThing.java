package top.wonderheng.everything.core.common;

import top.wonderheng.everything.core.model.FileType;
import top.wonderheng.everything.core.model.Thing;

import java.io.File;

/**
 * @BelongsProject: everything
 * @BelongsPackage: top.wonderheng.everything.core.common
 * @Author: WonderHeng
 * @CreateTime: 2018-11-15 21:13
 */
public class FileConvertThing {

    public static Thing convert(File file) {
        Thing thing = new Thing();
        thing.setName(file.getName());
        thing.setPinyin(file.getName());
        thing.setDepth(computeDepth(file.getPath()));
        thing.setFileType(computeFileType(file.getPath()));
        thing.setPath(file.getAbsolutePath());
        return thing;
    }

    private static int computeDepth(String path) {
        int depth = -1;
        for (char ch : path.toCharArray()) {
            if (ch == File.separatorChar) {
                depth++;
            }
        }
        return depth;
    }

    private static FileType computeFileType(String path) {
        File file = new File(path);
        if (file.isDirectory()) {
            return FileType.OTHER;
        } else {
            int index = path.lastIndexOf(".");
            if (index == -1) {
                return FileType.OTHER;
            } else {
                return FileType.lookup(path.substring(index + 1));
            }
        }
    }
}