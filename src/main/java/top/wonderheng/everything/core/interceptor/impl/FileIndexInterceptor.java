package top.wonderheng.everything.core.interceptor.impl;

import top.wonderheng.everything.core.common.FileConvertThing;
import top.wonderheng.everything.core.dao.FileIndexDao;
import top.wonderheng.everything.core.interceptor.FileInterceptor;
import top.wonderheng.everything.core.model.Thing;

import java.io.File;

/**
 * @BelongsProject: everything
 * @BelongsPackage: top.wonderheng.everything.core.interceptor.impl
 * @Author: WonderHeng
 * @CreateTime: 2018-11-15 21:22
 */
public class FileIndexInterceptor implements FileInterceptor {

    private final FileIndexDao fileIndexDao;

    public FileIndexInterceptor(FileIndexDao fileIndexDao) {
        this.fileIndexDao = fileIndexDao;
    }

    @Override
    public void applyFile(File file) {
        Thing thing = FileConvertThing.convert(file);
        fileIndexDao.insert(thing);
    }
}
