package com.athena.component;

import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: yufei
 * Date: 12-12-21
 * Time: 下午4:17
 * 捕获子线程异常
 */
public class UEHLogger implements Thread.UncaughtExceptionHandler {
    private static Logger logger=Logger.getLogger(UEHLogger.class);
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        logger.error("线程"+t.getName()+"抛出异常");
        logger.error(e);
    }
}
