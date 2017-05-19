package com.athena.component.exchange;

import java.util.concurrent.Callable;
import java.util.concurrent.RunnableFuture;

/**
 * Created with IntelliJ IDEA.
 * User: yufei
 * Date: 12-12-13
 * Time: 上午10:41
 * 实现可取消任务
 */
public interface CancellableTask <T> extends Callable<T> {
    /**
     * 当线程中断时，需要调用此方法
     */
    void cancel();

    /**
     *
     * @return   一个 RunnableFuture，在运行的时候，它将调用底层可调用任务，
     * 作为 Future 任务，它将生成可调用的结果作为其结果，并为底层任务提供取消操作。
     *
     */
    RunnableFuture<T> newTask();


}
