package com.athena.util.athenalog;

/**
 * 后台运行/系统调度 操作日志接口类
 * @author zhangl
 *
 */

public interface IBackgroundRunLog {
	
	/**
	 * 添加后台运行正常的正确日志 日志级别:1 日志类型:1正确日志
	 * @param operator 	           调用者/触发者
	 * @param usercenter 	用户中心
	 * @param module 		模块
	 * @param title 		标题/功能
	 * @param contents 		内容
	 * @throws RuntimeException
	 */
	public abstract void addCorrect( String operator, String usercenter, 
			String module, String title, String contents ) throws RuntimeException;
	
	/**
	 * 添加后台运行失败的日志  日志级别:5 日志类型:2错误日志 
	 * 谨慎使用，此级别发送报警中心
	 * @param operator 	           调用者/触发者
	 * @param usercenter 	用户中心
	 * @param module 		模块
	 * @param title 		标题/功能
	 * @param contents 		内容
	 * @param errorClass 	错误类地址/名称/方法名
	 * @param errorContent 	[自定义]出错原因
	 * @throws RuntimeException
	 */
	public abstract void addError( String operator, String usercenter, 
			String module, String title, String contents,
			String errorClass, String errorContent ) throws RuntimeException;
	
	/**
	 * 添加后台运行数据级错误日志  日志级别:3  日志类型:3系统日志
	 * 此级别甄别筛选后选择发送报警中心
	 * @param operator 	           调用者/触发者
	 * @param usercenter 	用户中心
	 * @param module 		模块
	 * @param title 		标题/功能
	 * @param contents 		内容
	 * @param errorClass 	错误类地址/名称/方法名
	 * @param errorContent 	[自定义]出错原因
	 * @throws RuntimeException
	 */
	public abstract void addDataError( String operator, String usercenter, 
			String module, String title, String contents,
			String errorClass, String errorContent ) throws RuntimeException;
	
	
	/**
	 * 添加后台运行异常级 错误日志  日志级别 4 日志类型:3系统日志
	 * 此级别甄别筛选后选择发送报警中心
	 * @param operator 	           调用者/触发者
	 * @param usercenter 	用户中心
	 * @param module 		模块
	 * @param title 		标题/功能
	 * @param contents 		内容
	 * @param errorClass 	错误类地址/名称/方法名
	 * @param errorContent 	[自定义]出错原因
	 * @throws RuntimeException
	 */
	public abstract void addExceptionError( String operator, String usercenter, 
			String module, String title, String contents,
			String errorClass, String errorContent ) throws RuntimeException;
}
