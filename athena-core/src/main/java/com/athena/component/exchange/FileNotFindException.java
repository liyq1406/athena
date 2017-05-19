package com.athena.component.exchange;
/**
 * 文件没有异常
 * @author chenlei
 * @vesion 1.0
 * @date 2012-8-21
 */
public class FileNotFindException extends RuntimeException {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	public FileNotFindException(String msg) {
		super(msg);
	}
}
