package com.athena.print;

import org.apache.log4j.Logger;

import java.util.concurrent.*;

/**
 * Created with IntelliJ IDEA.
 * User: yufei
 * Date: 12-12-21
 * Time: 下午4:22
 * 执行打印任务线程池
 */
public class PrintTaskExecutor extends ThreadPoolExecutor {
    private static Logger logger=Logger.getLogger(PrintTaskExecutor.class);

    private final ConcurrentMap<String, String> cache = new ConcurrentHashMap<String,String>();
    public PrintTaskExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public PrintTaskExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public PrintTaskExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public PrintTaskExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    @Override
    protected  void beforeExecute(Thread t, Runnable r) {
        logger.info(t.getName() + "添加" + r.toString() + "任务");
        String key = cache.putIfAbsent(r.toString(),r.toString());
        if (key == null) {
            logger.info(t.getName() + "执行" + r.toString() + "任务");
        } else {
            logger.info("已经存在相同的任务" + r.toString());
            throw new RuntimeException("已经存在相同的任务"+r.toString());
        }

    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        if(t != null && r instanceof PrintableTask) {
            ((PrintableTask) r).cancel();
        }
        super.afterExecute(r, t);
        logger.info(r.toString() + "执行完成");
        String key = cache.remove(r.toString());
        logger.info("移除"+key);


    }

    @Override
    public void shutdown() {
        super.shutdown();
        cache.clear();
    }

}
