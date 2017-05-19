/**
 * 
 */
package com.athena.component.runner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Administrator
 *
 */
public class Runner {
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private ExecutorService executor;
	
	private List<FutureTask<CallResult>> futures = 
		new ArrayList<FutureTask<CallResult>>();
	
	public Runner(int poolSize){
		executor = Executors.newFixedThreadPool(poolSize);
	}
	
	public Runner(){
		
	}
	
	public void addCommands(Command<?>[] commands){
		for(Command<?> command:commands){	
			this.addCommand(command);
		}
	}
	
	public void addCommand(Command<?> command){
		if(command==null)return;
		FutureTask<CallResult> future;
		future = new FutureTask<CallResult>(command);
		executor.execute(future);
		futures.add(future);
	}
	
	/**
	 * 获得命令执行结果
	 * @param i
	 * @return
	 */
	public CallResult getResult(int i){
		if(i<0||i>=futures.size())return null;
		FutureTask<CallResult> rFuture = futures.get(i);
		try {
			return rFuture.get();
		}  catch (InterruptedException e) {
			throw new RunnerException("线程调用异常。",e);
		} catch (ExecutionException e) {
			throw new RunnerException("执行程序异常。",e);
		}
	}
	
	/**
	 * 阻塞模式等待线程全部结束
	 */
	public List<Object> finish(boolean block){
		List<Object> callResults = new ArrayList<Object>();
		try {
			for(FutureTask<CallResult> future:futures){
				if(block==true){
					CallResult result = future.get();//阻塞式获取执行结果
					Object rObject = result.getResult();
					if(rObject!=null)callResults.add(rObject);
					log.debug(rObject);
				}else{
					future.run();
				}
			}
		} catch (InterruptedException e) {
			throw new RunnerException("线程调用异常。",e);
		} catch (ExecutionException e) {
			throw new RunnerException("执行程序异常。",e);
		}
		return callResults;
	}
	
	public void shutdown(){
		executor.shutdown();
	}
	
	public boolean isTerminated(){
		return executor.isTerminated();
	}
}
