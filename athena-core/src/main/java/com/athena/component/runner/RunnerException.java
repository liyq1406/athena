/**
 * 
 */
package com.athena.component.runner;

import com.toft.core3.NestedRuntimeException;

/**
 * 输出异常
 * @author Administrator
 *
 */
public class RunnerException extends NestedRuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1610093457879850545L;

	public RunnerException(String msg,Throwable error) {
		super(msg,error);
	}
	
}
