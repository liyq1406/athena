package com.athena.component.exchange.txt;

public class WarmBusinessException extends RuntimeException {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	public WarmBusinessException(String msg) {
		super("业务忽略错误:" + msg);
	}
	
}
