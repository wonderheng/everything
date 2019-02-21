package top.wonderheng.everything.core.model;

import lombok.Data;

/**
 * Author: wonderheng
 * Created: 2019/2/14
 */
@Data
public class Condition {
    
    private String name;
    
    private String fileType;
    
    private Integer limit;
    
    /**
     * 检索结果的文件信息depth排序规则
     * 1.默认是true -> asc
     * 2.false -> desc
     */
    private Boolean orderByAsc;
}
