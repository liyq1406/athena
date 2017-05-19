package com.athena.print;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: yufei
 * Date: 12-12-21
 * Time: 下午4:50
 * 创建打印任务线程池单例
 */
public class StaticPrintTaskExecutor {
    private static PrintTaskExecutor printTaskExecutorExec = new PrintTaskExecutor(0, Integer.MAX_VALUE,
            60L, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>(),
            new PrintThreadFactory());
    private StaticPrintTaskExecutor(){}

    public static PrintTaskExecutor getPrintTaskExecutor() {
        return printTaskExecutorExec;
    }

}
