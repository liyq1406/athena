package com.athena.util.cache;

import java.util.List;

public class PrintConfig {

	private String taskHeight;
	private String perPaperHeight;
	private String beginHeight;
	private String endHeight;
	private List areaList;
	private String pageInfo;
	
	
	
	public String getPageInfo() {
		return pageInfo;
	}
	public void setPageInfo(String pageInfo) {
		this.pageInfo = pageInfo;
	}
	public String getTaskHeight() {
		return taskHeight;
	}
	public void setTaskHeight(String taskHeight) {
		this.taskHeight = taskHeight;
	}
	public String getPerPaperHeight() {
		return perPaperHeight;
	}
	public void setPerPaperHeight(String perPaperHeight) {
		this.perPaperHeight = perPaperHeight;
	}
	public String getBeginHeight() {
		return beginHeight;
	}
	public void setBeginHeight(String beginHeight) {
		this.beginHeight = beginHeight;
	}
	public String getEndHeight() {
		return endHeight;
	}
	public void setEndHeight(String endHeight) {
		this.endHeight = endHeight;
	}
	public List getAreaList() {
		return areaList;
	}
	public void setAreaList(List areaList) {
		this.areaList = areaList;
	}
}
