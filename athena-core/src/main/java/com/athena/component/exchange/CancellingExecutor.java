package com.athena.component.exchange;
 
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: yufei
 * Date: 12-12-13
 * Time: 上午11:14
 * 实现自定义取消线程池
 */
public class CancellingExecutor  extends ThreadPoolExecutor { 
	private static final RejectedExecutionHandler defaultHandler =
        new AbortPolicy(); 

    public CancellingExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory); 
    }

    public CancellingExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler); 
    }

    public CancellingExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler); 
    }
    public CancellingExecutor(int corePoolSize, int maximumPoolSize,   long keepAliveTime,
	            TimeUnit unit,
	            BlockingQueue<Runnable> workQueue) {
				this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
				Executors.defaultThreadFactory(), defaultHandler);
	}
    
    
    /**
     * 为给定可调用任务返回一个 RunnableFuture,实际是个钩子程序，用来调用自定义取消任务的方法
     * @param callable    将包装的可调用任务
     * @param <T>
     * @return  一个 RunnableFuture，在运行的时候，它将调用底层可调用任务，作为 Future 任务，它将生成可调用的结果作为其结果，
     * 并为底层任务提供取消操作。
     */
    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        if (callable instanceof CancellableTask)
            return ((CancellableTask<T>) callable).newTask();
        else
            return super.newTaskFor(callable);
    }
    
    
    /*
     * newTask  hook.
     * If it is a cancellable task add to the list.
     */
/*    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
    	if (callable instanceof CancellableTask) {
    		RunnableFuture<T> future = ((CancellableTask<T>) callable).newTask();
    		synchronized (list) {
    			list.add(future);
    			logger.info("SocketTask Connection Count:" + Integer.toString(list.size()));
    		}
    		return future;
    	} else
    		return super.newTaskFor(callable);
    }*/
    
}