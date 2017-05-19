/**
 * 
 */
package com.athena.component.exchange;

import com.toft.core3.NestedRuntimeException;

/**
 * 输出异常
 * @author Administrator
 *
 */
public class WriteException extends NestedRuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1610093457879850545L;

	public WriteException(String msg) {
		super(msg);
	}

}
