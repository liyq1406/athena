package com.athena.print;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import com.athena.component.UEHLogger;
/**
 * Created with IntelliJ IDEA.
 * User: yufei
 * Date: 12-12-21
 * Time: 下午4:34
 * 创建打印线程工厂
 */
public class PrintThreadFactory implements ThreadFactory {
    private String prefix;
    final AtomicInteger poolNumber = new AtomicInteger(1);
    private volatile int threadId;
    /**
     * 创建新线程
     * @param runnable
     * @return
     */
    @Override
    public Thread newThread(Runnable runnable) {
        Thread r = new Thread(runnable);
        if (prefix == null) {
            prefix = "pool-" +
                    poolNumber.getAndIncrement() +
                    "-thread-";
        }
        r.setName(prefix + (threadId++));
        r.setDaemon(false);
        r.setUncaughtExceptionHandler(new UEHLogger());
        return r;
    }
}
