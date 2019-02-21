package top.wonderheng.everything.core.dao;

import top.wonderheng.everything.core.model.Condition;
import top.wonderheng.everything.core.model.Thing;

import java.util.List;

/**
 * 业务层访问数据库的CRUD
 * Author: wonderheng
 * Created: 2019/2/15
 */
public interface FileIndexDao {

    /**
     * 插入数据Thing
     *
     * @param thing
     */
    void insert(Thing thing);

    /**
     * 删除数据Thing
     *
     * @param thing
     */
    void delete(Thing thing);

    /**
     * 根据condition条件进行数据库的检索
     *
     * @param condition
     * @return
     */
    List<Thing> search(Condition condition);
}
