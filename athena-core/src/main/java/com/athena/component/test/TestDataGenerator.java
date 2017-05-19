/**
 * 
 */
package com.athena.component.test;

import java.util.List;

/**
 * <p>Title:SDC 核心组件</p>
 *
 * <p>Description:测试数据生成</p>
 *
 * <p>Copyright:Copyright (c) 2011.11</p>
 *
 * <p>Company:iSoftstone</p>
 *
 * @author zhouyi
 * @version 1.0.0
 */
public interface TestDataGenerator {

	void generate();

	public List<String> getClearTables();
	
	Object getInsertParams();
	
	/**
	 * 遍历表
	 * @param callBack
	 */
	public void visitTable(RowCallBack callBack);

}
