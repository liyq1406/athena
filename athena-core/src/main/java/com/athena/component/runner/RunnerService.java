package com.athena.component.runner;

import java.util.List;

import com.toft.core3.container.annotation.Component;

@Component
public class RunnerService {
	
	public final static int EXECUTOR_POOL_SIZE = 10;
	private Runner runner;
	
	public Runner getRunner() {
		return runner;
	}

	public void setRunner(Runner runner) {
		this.runner = runner;
	}

	/**
	 * @param commands
	 * @param block
	 */
	public List<Object> runBlock(Command<?>[] commands){
		return runBlock(EXECUTOR_POOL_SIZE, commands);
	}

	/**
	 * //阻塞模式运行，等待所有的操作结束
	 * @param poolSize
	 * @param commands
	 * @param block
	 */
	public List<Object> runBlock(int poolSize,Command<?>[] commands){
		Runner runner = new Runner(poolSize);
		this.runner =runner;
		runner.addCommands(commands);
		return runner.finish(true);
	}
	
	/**
	 * 非阻塞模式运行
	 * @param poolSize
	 * @param commands
	 * @return
	 */
	public List<Object> run(int poolSize,Command<?>[] commands){
		Runner runner = new Runner(poolSize);
		this.runner =runner;
		runner.addCommands(commands);
		return runner.finish(false);
	}
}
