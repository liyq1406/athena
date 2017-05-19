/**
 * 
 */
package com.athena.component.entity;


import com.toft.core3.Pageable;

/**
 * <p>Title:SDC UI组件</p>
 *
 * <p>Description:</p>
 *
 * <p>Copyright:Copyright (c) 2011.11</p>
 *
 * <p>Company:iSoftstone</p>
 *
 * @author zhouyi
 * @version 1.0.0
 */
public interface Domain extends Pageable{
	
	/**
	 * 设置主键
	 * @param id
	 */
	public void setId(String id);
	
	/**
	 * 获取主键
	 * @return
	 */
	public String getId();
	
}
