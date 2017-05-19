package com.athena.util.cache;

import java.util.List;

public class Area {
	
	private String id;
	private String type;
	private String autoSr;
	private List dataList;
	
	
	
	public String getAutoSr() {
		return autoSr;
	}
	public void setAutoSr(String autoSr) {
		this.autoSr = autoSr;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List getDataList() {
		return dataList;
	}
	public void setDataList(List dataList) {
		this.dataList = dataList;
	}
	
}
