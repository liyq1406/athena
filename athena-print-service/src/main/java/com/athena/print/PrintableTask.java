package com.athena.print;

/**
 * Created with IntelliJ IDEA.
 * User: issuser
 * Date: 12-12-21
 * Time: 下午4:30
 * 打印任务接口
 */
public interface PrintableTask extends Runnable {
    void cancel();
}
