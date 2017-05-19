package com.log4j;

import org.apache.log4j.PatternLayout;

public class ThreadIDPatternLayout extends PatternLayout {
	
	public ThreadIDPatternLayout(String pattern) {
		super(pattern);
	}
	
	public ThreadIDPatternLayout() {
		super();
	}
		 
	/**
	 * 重写createPatternParser方法，返回PatternParser的子类
	 */
	@Override
	protected ThreadIDPatternParser createPatternParser(String pattern) {
		 return new ThreadIDPatternParser(pattern);
	}

}
