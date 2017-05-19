/**
 * 
 */
package com.athena.component.exchange;

import com.toft.core3.NestedRuntimeException;

/**
 * 解析异常
 * @author Administrator
 *
 */
public class ParserException extends NestedRuntimeException {

	public ParserException(){
		this("解析异常");
	}
	
	public ParserException(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -7517874530176365988L;

}
