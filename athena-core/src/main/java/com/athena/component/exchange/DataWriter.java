/**
 * 
 */
package com.athena.component.exchange;

import com.athena.component.exchange.config.DataParserConfig;


/**
 * <p>Title:数据交换组件</p>
 *
 * <p>Description:数据解析回调接口类</p>
 *
 * <p>Copyright:Copyright (c) 2011.9</p>
 *
 * <p>Company:iSoftstone</p>
 *
 * @author Administrator
 * @version 1.0
 */
public interface DataWriter {
	
	/**
	 * 数据文件解析前回调
	 * @param dataParserConfig
	 * @return
	 */
	boolean before();

	/**
	 * 数据文件解析后
	 * @param dataParserConfig
	 * @param result
	 * @return
	 */
	void after();
	
	/**
	 * 行记录解析前
	 * @param dataParserConfig
	 * @param line
	 * @return
	 */
	boolean beforeRecord(int rowIndex,Object rowObject);

	/**
	 * 行记录解析后
	 * @param dataParserConfig
	 * @param record
	 * @return
	 */
	void afterRecord(int rowIndex,Record record,Object line);
	
	/**
	 * 提交数据
	 */
	void commit();
	
	/**
	 * 关闭输出
	 */
	void close();
	
	/**
	 * 回滚
	 */
	void rollback();
	
	/**
	 * 得到DataParserConfig
	 * @return
	 */
	DataParserConfig getDataParserConfig();

}
