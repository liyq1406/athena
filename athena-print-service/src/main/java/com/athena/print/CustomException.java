package com.athena.print;

public class CustomException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CustomException(String qId) {
		super("作业"+qId+"未占用到打印机");
	}

}
