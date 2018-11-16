package top.wonderheng.everything.core.index.impl;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import top.wonderheng.everything.config.HandlerPath;
import top.wonderheng.everything.core.common.FileConvertThing;
import top.wonderheng.everything.core.dao.FileIndexDao;
import top.wonderheng.everything.core.index.FileScan;
import top.wonderheng.everything.core.index.FileWatcher;
import top.wonderheng.everything.core.model.Thing;

import java.io.File;

/**
 * @BelongsProject: everything
 * @BelongsPackage: top.wonderheng.everything.core.index.impl
 * @Author: WonderHeng
 * @CreateTime: 2018-11-15 21:19
 */
public class FileWatcherImpl extends FileAlterationListenerAdaptor implements FileWatcher {

    private final FileAlterationMonitor monitor;

    private final FileScan fileScan;

    private final FileIndexDao fileIndexDao;

    public FileWatcherImpl(FileScan fileScan, FileIndexDao fileIndexDao, long interval) {
        this.fileScan = fileScan;
        this.fileIndexDao = fileIndexDao;
        this.monitor = new FileAlterationMonitor(interval);
    }


    @Override
    public void stop() {
        try {
            monitor.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start() {
        try {
            monitor.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void monitor(HandlerPath handlerPath) {
        for (String path : handlerPath.getIncludes()) {
            FileAlterationObserver observer = new FileAlterationObserver(new File(path), pathname -> {
                for (String p : handlerPath.getExcludes()) {
                    if (pathname.getAbsolutePath().startsWith(p)) {
                        return true;
                    }
                }
                return false;
            });
            observer.addListener(this);
            monitor.addObserver(observer);
        }
    }


    @Override
    public void onStart(FileAlterationObserver observer) {
//        System.out.println("onStart " + observer.toString());
    }

    @Override
    public void onDirectoryCreate(File directory) {
        this.fileScan.index(directory.getAbsolutePath());
    }

    @Override
    public void onDirectoryDelete(File directory) {
//        Thing thing = FileConvertThing.convert(directory);
//        this.fileIndexDao.delete(thing);
    }

    @Override
    public void onFileCreate(File file) {
        Thing thing = FileConvertThing.convert(file);
        this.fileIndexDao.insert(thing);
        System.out.println("onFileCreate "+file);
    }

    @Override
    public void onFileDelete(File file) {
//        Thing thing = FileConvertThing.convert(file);
//        this.fileIndexDao.delete(thing);
    }

    @Override
    public void onStop(FileAlterationObserver observer) {
//        System.out.println("onStop " + observer);
    }
}
