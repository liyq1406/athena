package com.athena.component.exchange;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: issuser
 * Date: 13-1-15
 * Time: 下午4:04
 * To change this template use File | Settings | File Templates.
 */
public class StaticInterFaceTaskExecutor {
	
	//有界队列，调用者饱和策略
   /* private static ExecutorService exec =  new CancellingExecutor(10, 
    		30,0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(30),new ThreadPoolExecutor.CallerRunsPolicy());*/
    
  //无界队列，使用SynchronousQueue来避免任务排队，线程之前的一种移交机制，有线程在等待就会将任务移交给等待的线程处理
   //CallerRuns是一种饱和策略，实现一种调节机制
	private static ExecutorService exec =  new CancellingExecutor(20, 
    		80,1L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(),new ThreadPoolExecutor.CallerRunsPolicy());
	
//	private static ExecutorService exec =  new CancellingExecutor(0, Integer.MAX_VALUE,
//            1L, TimeUnit.SECONDS,
//            new SynchronousQueue<Runnable>());
    
    private StaticInterFaceTaskExecutor() {

    }
    public static ExecutorService getPrintTaskExecutor() {
        return exec;
    }
    
}
