/**
 * 
 */
package com.athena.component.runner;

/**
 * @author Administrator
 *
 */
public class CallResult {
	
	private long startTime;//线程执行开始时间
	
	private long endTime;//线程执行完成时间
	
	private String threadName;//进程名称
	
	private Command<?> command;
	
	public CallResult(
			Command<?> command,
			long startTime, 
			long endTime,
			String threadName) {
		super();
		this.command = command;
		this.startTime = startTime;
		this.endTime = endTime;
		this.threadName = threadName;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public String getThreadName() {
		return threadName;
	}

	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}
	
	public long getTime(){
		return endTime-startTime;
	}
	
	public Object getResult(){
		return command.result();
	}
	
	public String toString(){
		return command+",调用线程"+threadName+"耗时："+this.getTime()+"毫秒.";
	}
}
