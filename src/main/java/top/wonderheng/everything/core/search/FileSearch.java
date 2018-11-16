package top.wonderheng.everything.core.search;

import top.wonderheng.everything.core.model.Condition;
import top.wonderheng.everything.core.model.Thing;

import java.util.List;

/**
 * @BelongsProject: everything
 * @BelongsPackage: top.wonderheng.everything.core.search
 * @Author: WonderHeng
 * @CreateTime: 2018-11-15 21:30
 */
public interface FileSearch {

    List<Thing> search(Condition condition);
}
