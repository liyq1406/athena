package com.athena.component.monitor;

public class AlertInfo{
	private String code;//报警代码
	
	private String level;//报警级别
	
	public AlertInfo(String code, String level) {
		super();
		this.code = code;
		this.level = level;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}
}
