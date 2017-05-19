package com.athena.util.exception;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;

import com.toft.core3.container.annotation.Component;
import com.toft.core3.dao.DataAccessException;

@Component
public class ExceptionInterceptor implements MethodInterceptor {
	private  Logger logger =Logger.getLogger(ExceptionInterceptor.class);
    public Object invoke(MethodInvocation methodInvoke) throws Throwable {
    	
        Object result = null;
        try {
        	result = methodInvoke.proceed();
        } catch ( DataAccessException e ) {
        	int errorCode = e.getErrorCode();
        	logger.error(e+"----"+methodInvoke.getMethod().toString());
        	String message = e.getCause() == null?e.getMessage():e.getCause().getMessage();
        	String msg = OracleErrorHandle.getInstance().translateError(errorCode, message);
        	//dao操作数据库的异常
            //throw new DaoException( methodInvoke.getMethod() + "\n 错误原因：" + msg );
            throw new DaoException(  msg );
            
        } catch ( Exception e ) {									//服务层异常
        	logger.error(e+"----"+methodInvoke.getMethod().toString());
        	//throw new RuntimeException( methodInvoke.getMethod() + "\n 错误原因：" + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        
        return result;
    }
    




	

}
