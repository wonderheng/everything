package top.wonderheng.everything.cmd;

import top.wonderheng.everything.config.EverythingConfig;
import top.wonderheng.everything.core.EverythingManager;
import top.wonderheng.everything.core.model.Condition;
import top.wonderheng.everything.core.model.Thing;

import java.util.List;
import java.util.Scanner;

/**
 * Author: wonderheng
 * Created: 2019/2/14
 */
public class EverythingCmdApp {
    
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        
        //解析用户参数
        parseParams(args);
        
        //欢迎
        welcome();
        
        //统一调度器
        EverythingManager manager = EverythingManager.getInstance();
        
        //启动后台清理线程
        manager.startBackgroundClearThread();
        
        //启动监控
        manager.startFileSystemMonitor();
        
        //交互式
        interactive(manager);
        
    }
    
    private static void parseParams(String[] args) {
        EverythingConfig config = EverythingConfig.getInstance();
        /*
         处理参数：
         如果用户指定的参数格式不对，使用默认值即可
         */
        for (String param : args) {
            String maxReturnParam = "--maxReturn=";
            if (param.startsWith(maxReturnParam)) {
                //--maxReturn=value
                int index = param.indexOf("=");
                String maxReturnStr = param.substring(index + 1);
                try {
                    int maxReturn = Integer.parseInt(maxReturnStr);
                    config.setMaxReturn(maxReturn);
                } catch (NumberFormatException e) {
                    //如果用户指定的参数格式不对，使用默认值即可
                }
            }
            String deptOrderByAscParam = "--deptOrderByAsc=";
            if (param.startsWith(deptOrderByAscParam)) {
                //--deptOrderByAsc=value
                int index = param.indexOf("=");
                String deptOrderByAscStr = param.substring(index + 1);
                config.setDeptOrderAsc(Boolean.parseBoolean(deptOrderByAscStr));
                
            }
            
            String includePathParam = "--includePath=";
            if (param.startsWith(includePathParam)) {
                //--includePath=values (;)
                int index = param.indexOf("=");
                String includePathStr = param.substring(index + 1);
                String[] includePaths = includePathStr.split(";");
                if (includePaths.length > 0) {
                    config.getIncludePath().clear();
                }
                for (String p : includePaths) {
                    config.getIncludePath().add(p);
                }
            }
            String excludePathParam = "--excludePath=";
            if (param.startsWith(includePathParam)) {
                //--excludePath=values (;)
                int index = param.indexOf("=");
                String excludePathStr = param.substring(index + 1);
                String[] excludePaths = excludePathStr.split(";");
                config.getExcludePath().clear();
                for (String p : excludePaths) {
                    config.getExcludePath().add(p);
                }
            }
        }
    }
    
    private static void interactive(EverythingManager manager) {
        while (true) {
            System.out.print("everything >>");
            String input = scanner.nextLine();
            //优先处理search
            if (input.startsWith("search")) {
                //search name [file_type]
                String[] values = input.split(" ");
                if (values.length >= 2) {
                    if (!values[0].equals("search")) {
                        help();
                        continue;
                    }
                    Condition condition = new Condition();
                    String name = values[1];
                    condition.setName(name);
                    if (values.length >= 3) {
                        String fileType = values[2];
                        condition.setFileType(fileType.toUpperCase());
                    }
                    search(manager, condition);
                    continue;
                } else {
                    help();
                    continue;
                }
            }
            switch (input) {
                case "help":
                    help();
                    break;
                case "exit":
                    quit();
                    return;
                case "index":
                    index(manager);
                    break;
                default:
                    help();
            }
        }
    }
    
    private static void search(EverythingManager manager, Condition condition) {
        //name fileType limit orderByAsc
        condition.setLimit(EverythingConfig.getInstance().getMaxReturn());
        condition.setOrderByAsc(EverythingConfig.getInstance().getDeptOrderAsc());
        List<Thing> thingList = manager.search(condition);
        for (Thing thing : thingList) {
            System.out.println(thing.getPath());
        }
        
    }
    
    private static void index(EverythingManager manager) {
        //统一调度器中的index
        new Thread(manager::buildIndex).start();
    }
    
    private static void quit() {
        System.out.println("再见");
        System.exit(0);
    }
    
    private static void welcome() {
        System.out.println("欢迎使用，Everything");
    }
    
    private static void help() {
        System.out.println("命令列表：");
        System.out.println("退出：exit");
        System.out.println("帮助：help");
        System.out.println("索引：index");
        System.out.println("搜索：search <name> [<file-Type> img | doc | bin | archive | other]");
    }
    
}
