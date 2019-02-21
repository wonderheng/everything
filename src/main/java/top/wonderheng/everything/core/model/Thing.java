package top.wonderheng.everything.core.model;

import lombok.Data;

/**
 * 文件属性信息索引之后的记录 Thing表示
 * Author: wonderheng
 * Created: 2019/2/14
 */
@Data
public class Thing {
    
    /**
     * 文件名称（保留名称）
     * File D:/a/b/hello.txt  -> hello.txt
     */
    private String name;
    
    /**
     * 文件路径
     */
    private String path;
    
    /**
     * 文件路径深度
     */
    private Integer depth;
    
    /**
     * 文件类型
     */
    private FileType fileType;
}
