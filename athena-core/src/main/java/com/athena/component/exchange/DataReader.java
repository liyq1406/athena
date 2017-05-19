/**
 * 
 */
package com.athena.component.exchange;

import com.athena.component.runner.RunnerService;

/**
 * <p>Title:数据交换平台</p>
 *
 * <p>Description:数据读取接口</p>
 *
 * <p>Copyright:Copyright (c) 2011.9</p>
 *
 * <p>Company:iSoftstone</p>
 *
 * @author zhouyi
 * @version 1.0
 */
public interface DataReader {
	
	/**
	 * @param dataParserConfig
	 * @param runnerService
	 */
	public void open(DataWriter dataWriter,RunnerService runnerService);
	/**
	 * 按行读取数据
	 */
	public int readLine();
	

	/**
	 * 
	 */
	public void close();
		
}
