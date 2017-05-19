package com.athena.util.exception;

import org.apache.log4j.Logger;

//*****************************************************************************
/**
 * <p>Title:ServiceException类</p> 
 * <p>Description:捕捉Service类中方法的异常</p> 
 * @author 张黎
 * @version v1.0
 * @date 2011-11-07
 */
//*****************************************************************************
public class ServiceException extends RuntimeException {

	private static final Logger logger = Logger.getLogger(ServiceException.class);
	/**
	 * 对象版本编号
	 */
	private static final long serialVersionUID = -5523048498682060962L;
	
	/**
	 * 错误信息
	 */
	private String errorMessage = null;
	
	private Throwable error = null;
	
	//**************************************************************************
	/**
	 * ServiceException构造函数     Exception , RuntimeException , SQLException
	 */
	//**************************************************************************
	public ServiceException() {
		super();
		errorMessage = super.getCause().getMessage();
	}
	
    //**************************************************************************
	/**
	 * ServiceException构造函数根据传递的异常信息
	 * @param argMessage
	 */
    //**************************************************************************
	
	public ServiceException(String argMessage) {
		super( argMessage );
		errorMessage =  argMessage + "\n";
	}
    
	//**************************************************************************
	/**
	 * ServiceException构造函数通过传递异常对象
	 * @param argThr
	 */
    //**************************************************************************
	
	public ServiceException(Throwable argThr) {
		super(argThr);
		error = argThr;
		errorMessage =  argThr.getLocalizedMessage() + "\n";
	}
  
	//**************************************************************************
	/**
	 * ServiceException构造函数根据传递的异常信息
	 * @param argMessage
	 * @param argThr
	 */
	//**************************************************************************
	public ServiceException(String argMessage, Throwable argThr) {
		super(argMessage,argThr);
	}
	
	//**************************************************************************
	/**
	 * 当该异常被打印出来的时候执行
	 * @return String
	 */
    //**************************************************************************
	public String toString() {
		StringBuffer sqlString = new StringBuffer("ServiceException:");
		sqlString.append("\n**************ServiceException Start***********************");
		sqlString.append("msgError: \n");
		sqlString.append(errorMessage).append("\n");
		sqlString.append("error: ").append(error);
		sqlString.append("\n**************ServiceException End************************");
		logger.error(sqlString.toString());
		return sqlString.toString();
	}
}

