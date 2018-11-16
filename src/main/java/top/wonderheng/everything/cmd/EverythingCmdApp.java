package top.wonderheng.everything.cmd;

import top.wonderheng.everything.config.EverythingConfig;
import top.wonderheng.everything.core.EverythingManager;
import top.wonderheng.everything.core.dao.DataSourceFactory;
import top.wonderheng.everything.core.dao.FileIndexDao;
import top.wonderheng.everything.core.dao.impl.DatabaseFileIndexDao;
import top.wonderheng.everything.core.index.FileScan;
import top.wonderheng.everything.core.index.impl.FileScanImpl;
import top.wonderheng.everything.core.interceptor.impl.FileIndexInterceptor;
import top.wonderheng.everything.core.model.Condition;
import top.wonderheng.everything.core.model.Thing;
import top.wonderheng.everything.core.search.FileSearch;
import top.wonderheng.everything.core.search.impl.FileSearchImpl;

import javax.sql.DataSource;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * @BelongsProject: everything
 * @BelongsPackage: top.wonderheng.everything.cmd
 * @Author: WonderHeng
 * @CreateTime: 2018-11-15 21:06
 */
public class EverythingCmdApp {
    public static void main(String[] args) {
        //解析参数
        parseParam(args);

        //配置
        EverythingConfig config = EverythingConfig.getInstance();

        //初始化数据源
        DataSource dataSource = DataSourceFactory.dataSource(config);

        //准备数据处理组件
        FileIndexDao fileIndexDao = new DatabaseFileIndexDao(dataSource);
        FileSearch fileSearch = new FileSearchImpl(fileIndexDao);
        FileScan fileScan = new FileScanImpl();
        fileScan.interceptor(new FileIndexInterceptor(fileIndexDao));

        //管理器
        EverythingManager everythingManager = new EverythingManager(fileScan, fileSearch, fileIndexDao);

        //构架索引
        if (config.isRebuildIndex()) {
            File mvdb = new File(config.getH2IndexFile() + "mv.db");
            if (mvdb.isFile() && mvdb.exists()) {
                boolean mvrs = mvdb.delete();
            } else {
                DataSourceFactory.databaseInit();
            }
            File tracedb = new File(config.getH2IndexFile() + "trace.db");
            if (tracedb.exists()) {
                boolean tracers = tracedb.delete();
            }
            System.out.println("Rebuild File System Index");
            Thread buildIndexThread = new Thread(everythingManager::buildIndex);
            buildIndexThread.setDaemon(false);
            buildIndexThread.start();
        } else {
            System.out.println("File System Index Already Exists .");
        }

        //启动清理
        everythingManager.startBackgroundClear();

        //启动监控
        new Thread(everythingManager::startBackgroundMonitor).start();

        //交互式
        interactive(everythingManager);

    }

    private static void interactive(EverythingManager manager) {
        Scanner scanner = new Scanner(System.in);
        doWelcome();
        while (true) {
            System.out.print(">>");
            String line = scanner.nextLine();
            String[] params = line.split(" ");
            if (params.length == 2) {
                if (params[0].equals("search")) {
                    doSearch(params[1], manager);
                } else {
                    doHelp();
                }
            } else {
                switch (line) {
                    case "quit":
                        doQuit();
                        return;
                    case "help":
                        doHelp();
                        break;
                    case "index":
                        break;
                    default:
                        doHelp();
                        break;
                }
            }
        }
    }

    private static void parseParam(String[] args) {
        EverythingConfig config = EverythingConfig.defaultConfig();
        for (String paramLine : args) {
            if (paramLine.startsWith("--indexPaths=")) {
                int index = paramLine.indexOf("=");
                String indexPathsStr = paramLine.substring(index + 1);
                String[] indexPaths = indexPathsStr.split(",");
                config.getIndexPaths().addAll(Arrays.asList(indexPaths));
            }
            if (paramLine.startsWith("--excludePaths=")) {
                int index = paramLine.indexOf("=");
                String excludePathsStr = paramLine.substring(index + 1);
                String[] excludePaths = excludePathsStr.split(",");
                config.getExcludePaths().addAll(Arrays.asList(excludePaths));
            }

            if (paramLine.startsWith("--maxRecentlyFile=")) {
                int index = paramLine.indexOf("=");
                int maxRecentlyFile = 30;
                try {
                    maxRecentlyFile = Integer.parseInt(paramLine.substring(index + 1));
                } catch (NumberFormatException e) {
                    System.out.println("Warning: param maxRecentlyFile can't convert number");
                }
                config.setMaxRecentlyFile(maxRecentlyFile);
            }

            if (paramLine.startsWith("--maxReturnFile=")) {
                int index = paramLine.indexOf("=");
                int maxReturnFile = 30;
                try {
                    maxReturnFile = Integer.parseInt(paramLine.substring(index + 1));
                } catch (NumberFormatException e) {
                    System.out.println("Warning: param maxReturnFile can't convert number");
                }
                config.setMaxReturnFile(maxReturnFile);
            }
            if (paramLine.startsWith("--depthAsc=")) {
                int index = paramLine.indexOf("=");
                boolean depthAsc = true;
                try {
                    depthAsc = Boolean.parseBoolean(paramLine.substring(index + 1));
                } catch (NumberFormatException e) {
                    System.out.println("Warning: param depthAsc can't convert bool");
                }
                config.setDepthAsc(depthAsc);
            }

            if (paramLine.startsWith("--rebuildIndex=")) {
                int index = paramLine.indexOf("=");
                boolean rebuildIndex = true;
                try {
                    rebuildIndex = Boolean.parseBoolean(paramLine.substring(index + 1));
                } catch (NumberFormatException e) {
                    System.out.println("Warning: param rebuildIndex can't convert bool");
                }
                config.setRebuildIndex(rebuildIndex);
            }
        }
    }


    private static void doWelcome() {
        System.out.println("欢迎使用，Everything");
    }

    private static void doQuit() {
        System.out.println("欢迎使用，再见");
        System.exit(0);
    }

    private static void doHelp() {
        System.out.println("命令列表：");
        System.out.println("退出：quit");
        System.out.println("帮助：help");
        System.out.println("索引：index");
        System.out.println("搜索：search <name>");
    }

    private static void doSearch(String line, EverythingManager manager) {
        Condition condition = new Condition();
        condition.setName(line);
        List<Thing> thingList = manager.search(condition);
        print(thingList);
    }


    private static void print(List<Thing> things) {
        for (Thing thing : things) {
            System.out.println(thing.getPath());
        }
    }
}
