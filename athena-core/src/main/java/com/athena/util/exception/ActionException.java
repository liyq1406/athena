package com.athena.util.exception;

import org.apache.log4j.Logger;

//*****************************************************************************
/**
 * <p>Title:ActionException类</p> 
 * <p>Description:捕捉Action类中方法的异常</p> 
 * @author 张黎
 * @version v1.0
 * @date 2011-11-07
 */
//*****************************************************************************
public class ActionException extends RuntimeException {

	private static final Logger logger = Logger.getLogger(ActionException.class);
	/**
	 * 对象版本编号
	 */
	private static final long serialVersionUID = 2574905265714826111L;
	
	/**
	 * 错误信息
	 */
	private String errorMessage = null;
	
	private Throwable error = null;
	
	//**************************************************************************
	/**
	 * ActionException构造函数     Exception , RuntimeException , SQLException
	 */
	//**************************************************************************
	public ActionException() {
		super();
		errorMessage =  super.getCause().getMessage();
	}
	
    //**************************************************************************
	/**
	 * ActionException构造函数根据传递的异常信息
	 * @param argMessage
	 */
    //**************************************************************************
	
	public ActionException(String argMessage) {
		super(argMessage );
		errorMessage =  argMessage + "\n";
	}
    
	//**************************************************************************
	/**
	 * ActionException构造函数通过传递异常对象
	 * @param argThr
	 */
    //**************************************************************************
	
	public ActionException(Throwable argThr) {
		super(argThr);
		error = argThr;
		errorMessage =  argThr.getLocalizedMessage()+ "\n";
	}
  
	//**************************************************************************
	/**
	 * ActionException构造函数根据传递的异常信息
	 * @param argMessage
	 * @param argThr
	 */
	//**************************************************************************
	public ActionException(String argMessage, Throwable argThr) {
		super(argMessage,argThr);
	}
	
	//**************************************************************************
	/**
	 * 当该异常被打印出来的时候执行
	 * @return String
	 */
    //**************************************************************************
	public String toString() {
		StringBuffer sqlString = new StringBuffer("ActionException:" );
		sqlString.append("\n**************ActionException Start***********************");
		sqlString.append("msgError: \n");
		sqlString.append(errorMessage).append("\n");
		sqlString.append("error: ").append(error);
		sqlString.append("\n**************ActionException End************************");
		logger.error(sqlString.toString());
		return sqlString.toString();
	}
}

