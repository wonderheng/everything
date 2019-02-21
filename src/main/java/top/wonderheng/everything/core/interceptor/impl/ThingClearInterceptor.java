package top.wonderheng.everything.core.interceptor.impl;

import top.wonderheng.everything.core.dao.FileIndexDao;
import top.wonderheng.everything.core.interceptor.ThingInterceptor;
import top.wonderheng.everything.core.model.Thing;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Author: wonderheng
 * Created: 2019/2/16
 */
public class ThingClearInterceptor implements ThingInterceptor, Runnable {
    
    private Queue<Thing> queue = new ArrayBlockingQueue<>(1024);
    
    private final FileIndexDao fileIndexDao;
    
    public ThingClearInterceptor(FileIndexDao fileIndexDao) {
        this.fileIndexDao = fileIndexDao;
    }
    
    @Override
    public void apply(Thing thing) {
        this.queue.add(thing);
    }
    
    @Override
    public void run() {
        while (true) {
            Thing thing = this.queue.poll();
            if (thing != null) {
                fileIndexDao.delete(thing);
            }
            //1.优化 批量删除
            //List<Thing> thingList = new ArrayList<>();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
