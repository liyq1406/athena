package com.athena.pc.module.service;

import com.athena.component.service.Message;

public class NoWorkDayException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static StringBuffer buf = new StringBuffer();
	public NoWorkDayException(String message) {  
		super(buf.append(message).append(new Message("pc.nowork.day","i18n.pc.pc").getMessage()).toString());
	}
}
