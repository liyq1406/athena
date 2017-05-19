package com.athena.print.core;

import java.sql.SQLException;




/**
 * @author dsimedd001
 *java 添加打印任务的接口
 */
public interface IAddtasksequence {

	/**
	 * 接受执行层传过来的  数据与  打印模板 来生成 打印机能识别的 文档 
	 * @return
	 * @throws Exception 
	 *//*
	public String createWord(String user,String filecode,String stoageid,String data,String modelcode) throws ServiceException;*/
	
	/**
	 * 通过 用户组、单据组、仓库编号得到打印机组 
	 * @return
	 * @throws Exception 
	 */
	public String getPrintGroupCode(String user,String filecode,String storagescode)throws NullPointerException;
	
	
	/**
	 * 插入作业队列表
	 * @return
	 * @throws Exception 
	 */
	public boolean addTaskTable(String user,String filecode,String stoageid,String data,String modelcode,String pgid)throws SQLException;
	
}
