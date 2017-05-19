/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company: 软通动力
 * </p>
 * 
 * @author 穆伟
 * @version v1.0
 * @date 
 */
package com.athena.authority;

import org.apache.shiro.authc.AuthenticationException;

public class AccountStopException extends AuthenticationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8501545086468324970L;
	
	 /**
     * Creates a new UnknownAccountException.
     */
    public AccountStopException() {
        super();
    }

    /**
     * Constructs a new UnknownAccountException.
     *
     * @param message the reason for the exception
     */
    public AccountStopException(String message) {
        super(message);
    }

    /**
     * Constructs a new UnknownAccountException.
     *
     * @param cause the underlying Throwable that caused this exception to be thrown.
     */
    public AccountStopException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new UnknownAccountException.
     *
     * @param message the reason for the exception
     * @param cause   the underlying Throwable that caused this exception to be thrown.
     */
    public AccountStopException(String message, Throwable cause) {
        super(message, cause);
    }

}
