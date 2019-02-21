package top.wonderheng.everything.core.search;

import top.wonderheng.everything.core.model.Condition;
import top.wonderheng.everything.core.model.Thing;

import java.util.List;

/**
 * Author: wonderheng
 * Created: 2019/2/15
 */
public interface FileSearch {
    
    /**
     * 根据condition条件进行数据库的检索
     *
     * @param condition
     * @return
     */
    List<Thing> search(Condition condition);
    
}
