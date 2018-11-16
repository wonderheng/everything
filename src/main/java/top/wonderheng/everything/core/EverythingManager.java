package top.wonderheng.everything.core;

import top.wonderheng.everything.config.EverythingConfig;
import top.wonderheng.everything.core.dao.FileIndexDao;
import top.wonderheng.everything.core.index.FileScan;
import top.wonderheng.everything.core.index.FileWatcher;
import top.wonderheng.everything.core.index.impl.FileWatcherImpl;
import top.wonderheng.everything.core.model.Condition;
import top.wonderheng.everything.core.model.Thing;
import top.wonderheng.everything.core.search.FileSearch;

import java.nio.file.Paths;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

/**
 * @BelongsProject: everything
 * @BelongsPackage: top.wonderheng.everything.core.search
 * @Author: WonderHeng
 * @CreateTime: 2018-11-15 21:31
 */
public class EverythingManager {

    private ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

    private final FileScan fileScan;

    private final FileSearch fileSearch;

    private final FileWatcher fileWatcher;

    private final Thread thread;

    private final Queue<Thing> queue = new LinkedBlockingDeque<>(1024);

    public EverythingManager(FileScan fileScan, FileSearch fileSearch, FileIndexDao fileIndexDao) {
        this.fileScan = fileScan;
        this.fileSearch = fileSearch;
        this.thread = new EverythingManager.ThingClearThread(this.queue, fileIndexDao);
        thread.setName("Clear-Thread");
        this.fileWatcher = new FileWatcherImpl(fileScan, fileIndexDao, 10000);//单位：毫秒
        this.fileWatcher.monitor(EverythingConfig.defaultConfig().getHandlerPath());
    }

    /**
     * 索引
     */
    public void buildIndex() {
        EverythingConfig config = EverythingConfig.defaultConfig();
        CountDownLatch downLatch = new CountDownLatch(config.getIndexPaths().size());
        for (String path : config.getIndexPaths()) {
            executorService.submit(() -> {
                fileScan.index(path);
                downLatch.countDown();
            });
        }
        try {
            downLatch.await();
            System.out.println("File System Build Index Complete");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (executorService.isShutdown()) {
            executorService.shutdown();
        }
    }

    /**
     * 检索
     *
     * @param condition
     * @return
     */
    public List<Thing> search(Condition condition) {
        return fileSearch.search(condition).stream()
                .filter(thing -> {
                            boolean exist = Paths.get(thing.getPath()).toFile().exists();
                            if (!exist) {
                                queue.add(thing);
                            }
                            return exist;
                        }
                ).collect(Collectors.toList());
    }

    /**
     * 启动后台清理
     */
    public void startBackgroundClear() {
        thread.start();
    }


    /**
     * 启动后台监控
     */
    public void startBackgroundMonitor() {
        this.fileWatcher.start();
    }

    private static class ThingClearThread extends Thread {

        private final Queue<Thing> queue;

        private final FileIndexDao fileIndexDao;

        private ThingClearThread(Queue<Thing> queue, FileIndexDao fileIndexDao) {
            this.queue = queue;
            this.fileIndexDao = fileIndexDao;
        }

        @Override
        public void run() {
            while (true) {
                Thing thing = queue.poll();
                if (thing != null) {
                    fileIndexDao.delete(thing);
                }
            }
        }
    }
}
