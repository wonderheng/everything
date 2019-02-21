package top.wonderheng.everything.core;

import top.wonderheng.everything.config.EverythingConfig;
import top.wonderheng.everything.core.common.HandlePath;
import top.wonderheng.everything.core.dao.DataSourceFactory;
import top.wonderheng.everything.core.dao.FileIndexDao;
import top.wonderheng.everything.core.dao.impl.FileIndexDaoImpl;
import top.wonderheng.everything.core.index.FileScan;
import top.wonderheng.everything.core.index.impl.FileScanImpl;
import top.wonderheng.everything.core.interceptor.impl.FileIndexInterceptor;
import top.wonderheng.everything.core.interceptor.impl.ThingClearInterceptor;
import top.wonderheng.everything.core.model.Condition;
import top.wonderheng.everything.core.model.Thing;
import top.wonderheng.everything.core.monitor.FileWatch;
import top.wonderheng.everything.core.monitor.impl.FileWatchImpl;
import top.wonderheng.everything.core.search.FileSearch;
import top.wonderheng.everything.core.search.impl.FileSearchImpl;

import javax.sql.DataSource;
import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Author: wonderheng
 * Created: 2019/2/16
 */
public final class EverythingManager {
    
    private static volatile EverythingManager manager;
    
    private FileSearch fileSearch;
    
    private FileScan fileScan;
    
    private ExecutorService executorService;
    
    /**
     * 清理删除的文件
     */
    private ThingClearInterceptor thingClearInterceptor;
    private Thread backgroundClearThread;
    private AtomicBoolean backgroundClearThreadStatus = new AtomicBoolean(false);
    
    /**
     * 文件监控
     */
    private FileWatch fileWatch;
    
    private EverythingManager() {
        this.initComponent();
    }
    
    private void initComponent() {
        //数据源对象
        DataSource dataSource = DataSourceFactory.dataSource();

        //检查数据库，初始化时先调用
        checkDatabase();
        
        //业务层的对象
        FileIndexDao fileIndexDao = new FileIndexDaoImpl(dataSource);
        
        this.fileSearch = new FileSearchImpl(fileIndexDao);
        
        this.fileScan = new FileScanImpl();
        // this.fileScan.interceptor(new FilePrintInterceptor());
        this.fileScan.interceptor(new FileIndexInterceptor(fileIndexDao));
        
        this.thingClearInterceptor = new ThingClearInterceptor(fileIndexDao);
        this.backgroundClearThread = new Thread(this.thingClearInterceptor);
        this.backgroundClearThread.setName("Thread-Thing-Clear");
        this.backgroundClearThread.setDaemon(true);
        
        //文件监控对象
        this.fileWatch = new FileWatchImpl(fileIndexDao);
        
    }

    private void checkDatabase() {
        String fileName = EverythingConfig.getInstance().getH2IndexPath() + ".mv.db";
//        System.out.println(fileName);
        File dbFile = new File(fileName);
        //初始化数据库
        if (dbFile.exists() && dbFile.isDirectory()) {
            throw new RuntimeException("The following path has the same folder as the database name, database creation failed!!\n"
                    + EverythingConfig.getInstance().getH2IndexPath() + ".mv.db\n"
                    + "Please delete this folder and restart the program!");
        } else if (!dbFile.exists()) {
            DataSourceFactory.initDatabase();
        }
    }
    
    public void initOrResetDatabase() {
        DataSourceFactory.initDatabase();
    }
    
    public static EverythingManager getInstance() {
        if (manager == null) {
            synchronized(EverythingManager.class) {
                if (manager == null) {
                    manager = new EverythingManager();
                }
            }
        }
        return manager;
    }
    
    
    /**
     * 检索
     */
    public List<Thing> search(Condition condition) {
        //Stream 流式处理 JDK8
        return this.fileSearch.search(condition)
                .stream()
                .filter(thing -> {
                    String path = thing.getPath();
                    File f = new File(path);
                    boolean flag = f.exists();
                    if (!flag) {
                        //做删除
                        thingClearInterceptor.apply(thing);
                    }
                    return flag;
                    
                }).collect(Collectors.toList());
    }
    
    /**
     * 索引
     */
    public void buildIndex() {
        initOrResetDatabase();
        Set<String> directories = EverythingConfig.getInstance().getIncludePath();
        if (this.executorService == null) {
            this.executorService =  Executors.newFixedThreadPool(directories.size(), new ThreadFactory() {
                private final AtomicInteger threadId = new AtomicInteger(0);
                
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setName("Thread-Scan-" + threadId.getAndIncrement());
                    return thread;
                }
            });
        }
        final CountDownLatch countDownLatch = new CountDownLatch(directories.size());
        System.out.println("Build index start ....");
        for (String path : directories) {
            this.executorService.submit(() -> {
                EverythingManager.this.fileScan.index(path);
                //当前任务完成，值-1
                countDownLatch.countDown();
            });
        }
        //阻塞，直到任务完成，值0
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Build index complete ...");
    }
    
    
    /**
     * 启动清理线程
     */
    public void startBackgroundClearThread() {
        if (this.backgroundClearThreadStatus.compareAndSet(false, true)) {
            this.backgroundClearThread.start();
        } else {
            System.out.println("Cant repeat start BackgroundClearThread");
        }
    }
    
    /**
     * 启动文件系统监听
     */
    public void startFileSystemMonitor() {
        EverythingConfig config = EverythingConfig.getInstance();
        HandlePath handlePath = new HandlePath();
        handlePath.setIncludePath(config.getIncludePath());
        handlePath.setExcludePath(config.getExcludePath());
        this.fileWatch.monitor(handlePath);
        new Thread(new Runnable() {
            @Override
            public void run() {
//                System.out.println("文件系统监控启动");
                fileWatch.start();
            }
        }).start();
    }
}

