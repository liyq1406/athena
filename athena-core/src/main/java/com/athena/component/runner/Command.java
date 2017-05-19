/**
 * 
 */
package com.athena.component.runner;

import java.util.concurrent.Callable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 执行的操作
 * @author Administrator
 *
 */
public abstract class Command<T> implements Callable<CallResult>{
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	/*
	 *  (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	public CallResult call() {
		long startTime = System.currentTimeMillis();
		execute();
		return buildCallResult(startTime);
	}
	
	/**
	 * CallResult
	 * @param startTime
	 * @return
	 */
	protected CallResult buildCallResult(long startTime){
		return new CallResult(
				this,
				startTime,
				System.currentTimeMillis(),
				Thread.currentThread().getName());
	}
	
	/**
	 * 需要子类实现的命令执行方法
	 */
	public abstract void execute();

	public abstract T result();
}
