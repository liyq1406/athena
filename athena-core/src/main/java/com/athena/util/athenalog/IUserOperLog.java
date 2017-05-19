package com.athena.util.athenalog;

/**
 * 用户级操作日志接口类
 * @author zhangl
 *
 */

public interface IUserOperLog {
	
	/**
	 * 添加正确日志 日志级别:0 日志类型:1正确日志
	 * @param module 	模块
	 * @param title 	标题/功能
	 * @param contents 	操作内容
	 * @throws RuntimeException
	 */
	public abstract void addCorrect(String module, String title, String contents ) 
		throws RuntimeException;
	
	/**
	 * 添加错误日志 日志级别:2 日志类型2:错误日志
	 * @param module 	模块
	 * @param title 	标题/功能
	 * @param contents 	操作内容
	 * @param errorClass 	错误类地址/名称/方法名
	 * @param errorContent 	[自定义]出错原因
	 * @throws RuntimeException
	 */
	public abstract void addError(String module, String title, String contents, 
			String errorClass, String errorContent ) throws RuntimeException;
	
	
	/**
	 * 添加正确日志 日志级别:0 日志类型:1正确日志
	 * @param module 	模块
	 * @param title 	标题/功能
	 * @param contents 	操作内容
	 * @throws RuntimeException
	 */
	public abstract void addCorrectPDA(SysLog sysLog) 
		throws RuntimeException;
	
	/**
	 * 添加错误日志 日志级别:2 日志类型2:错误日志
	 * @param module 	模块
	 * @param title 	标题/功能
	 * @param contents 	操作内容
	 * @param errorClass 	错误类地址/名称/方法名
	 * @param errorContent 	[自定义]出错原因
	 * @throws RuntimeException
	 */
	public abstract void addErrorPDA(SysLog sysLog) throws RuntimeException;	
}
