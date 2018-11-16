package top.wonderheng.everything.core.dao;

import top.wonderheng.everything.core.model.Condition;
import top.wonderheng.everything.core.model.Thing;

import java.util.List;

/**
 * @BelongsProject: everything
 * @BelongsPackage: top.wonderheng.everything.core.dao
 * @Author: WonderHeng
 * @CreateTime: 2018-11-15 21:17
 */
public interface FileIndexDao {

    void insert(Thing thing);

    void insert(List<Thing> thing);

    void update(Thing thing);

    void delete(Thing thing);

    List<Thing> query(Condition condition);
}
