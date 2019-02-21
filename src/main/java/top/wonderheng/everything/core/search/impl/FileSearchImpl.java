package top.wonderheng.everything.core.search.impl;

import top.wonderheng.everything.core.dao.FileIndexDao;
import top.wonderheng.everything.core.model.Condition;
import top.wonderheng.everything.core.model.Thing;
import top.wonderheng.everything.core.search.FileSearch;

import java.util.ArrayList;
import java.util.List;

/**
 * 业务层
 * Author: wonderheng
 * Created: 2019/2/15
 */
public class FileSearchImpl implements FileSearch {
    
    private final FileIndexDao fileIndexDao;
    
    public FileSearchImpl(FileIndexDao fileIndexDao) {
        this.fileIndexDao = fileIndexDao;
    }
    
    @Override
    public List<Thing> search(Condition condition) {
        if (condition == null) {
            return new ArrayList<>();
        }
        return this.fileIndexDao.search(condition);
    }
}