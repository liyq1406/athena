package com.athena.util.exception;

import org.apache.log4j.Logger;


//*****************************************************************************
/**
 * <p>Title:DaoException类</p> 
 * <p>Description:捕捉DAO类中方法的异常</p> 
 * @author 张黎
 * @version v1.0
 * @date 2011-11-07
 */
//*****************************************************************************
public class DaoException extends RuntimeException {
	private static final Logger logger = Logger.getLogger(DaoException.class);
	/**
	 * 对象版本编号
	 */
	private static final long serialVersionUID = 5759381550174495948L;
	
	/**
	 * 错误信息
	 */
	private String errorMessage = null;
	
	private Throwable error = null;
	
	//**************************************************************************
	/**
	 * DaoException构造函数     Exception , RuntimeException , SQLException
	 */
	//**************************************************************************
	public DaoException() {
		super();
		errorMessage =  super.getCause().getMessage();
	}
	
    //**************************************************************************
	/**
	 * DaoException构造函数根据传递的异常信息
	 * @param argMessage
	 */
    //**************************************************************************
	
	public DaoException(String argMessage) {
		super( argMessage);
		errorMessage =  argMessage + "\n";
	}
    
	//**************************************************************************
	/**
	 * DaoException构造函数通过传递异常对象
	 * @param argThr
	 */
    //**************************************************************************
	
	public DaoException(Throwable argThr) {
		super(argThr);
		error = argThr;
		errorMessage =  argThr.getLocalizedMessage() + "\n";
	}
  
	//**************************************************************************
	/**
	 * DaoException构造函数根据传递的异常信息
	 * @param argMessage
	 * @param argThr
	 */
	//**************************************************************************
	public DaoException(String argMessage, Throwable argThr) {
		super(argMessage,argThr);
	}
	
	//**************************************************************************
	/**
	 * 当该异常被打印出来的时候执行
	 * @return String
	 */
    //**************************************************************************
//	public String toString() {
//		StringBuffer sqlString = new StringBuffer("DaoException:");
//		sqlString.append("\n**************DaoException Start***********************");
//		sqlString.append("msgError: \n");
//		sqlString.append(errorMessage).append("\n");
//		sqlString.append("error: ").append(error);
//		sqlString.append("\n**************DaoException End************************");
//		logger.error(sqlString.toString());
//		return sqlString.toString();
//	}
}

