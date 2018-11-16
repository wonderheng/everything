package top.wonderheng.everything.core.search.impl;

import top.wonderheng.everything.core.dao.FileIndexDao;
import top.wonderheng.everything.core.model.Condition;
import top.wonderheng.everything.core.model.Thing;
import top.wonderheng.everything.core.search.FileSearch;

import java.util.List;

/**
 * @BelongsProject: everything
 * @BelongsPackage: top.wonderheng.everything.core.search.impl
 * @Author: WonderHeng
 * @CreateTime: 2018-11-15 21:29
 */
public class FileSearchImpl implements FileSearch {

    private final FileIndexDao fileIndexDao;

    public FileSearchImpl(FileIndexDao fileIndexDao) {
        this.fileIndexDao = fileIndexDao;

    }

    @Override
    public List<Thing> search(Condition condition) {
        return this.fileIndexDao.query(condition);
    }


}
