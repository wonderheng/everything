package top.wonderheng.everything.core.common;

import top.wonderheng.everything.core.model.FileType;
import top.wonderheng.everything.core.model.Thing;

import java.io.File;

/**
 * 辅助工具类：将File对象转换成Thing对象
 * <p>
 * Author: wonderheng
 * Created: 2019/2/15
 */
public final class FileConvertThing {
    
    private FileConvertThing() {
    }
    
    public static Thing convert(File file) {
        Thing thing = new Thing();
        thing.setName(file.getName());
        thing.setPath(file.getAbsolutePath());
        thing.setDepth(computeFileDepth(file));
        thing.setFileType(computeFileType(file));
        return thing;
    }
    
    private static int computeFileDepth(File file) {
        String[] segments = file.getAbsolutePath().split("\\\\");
        return segments.length;
    }
    
    private static FileType computeFileType(File file) {
        if (file.isDirectory()) {
            return FileType.OTHER;
        }
        String fileName = file.getName();
        int index = fileName.lastIndexOf(".");
        if (index != -1 && index < fileName.length() - 1) {
            //abc.
            String extend = fileName.substring(index + 1);
            return FileType.lookup(extend);
        } else {
            return FileType.OTHER;
        }
    }
}