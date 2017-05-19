package com.athena.pc.entity;


public class LingjDayVolume {
	private String gongzbh;      //工作编号
	private String lingjbh;      //零件编号
	private int lingjsl;         //零件数量
	private String hour;         //工时
	private int qickc;        //期初库存
	
	public int getQickc() {
		return qickc;
	}
	public void setQickc(int qickc) {
		this.qickc = qickc;
	}
	public String getGongzbh() {
		return gongzbh;
	}
	public void setGongzbh(String gongzbh) {
		this.gongzbh = gongzbh;
	}
	public String getLingjbh() {
		return lingjbh;
	}
	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}
	public int getLingjsl() {
		return lingjsl;
	}
	public void setLingjsl(int lingjsl) {
		this.lingjsl = lingjsl;
	}
	public String getHour() {
		return hour;
	}
	public void setHour(String hour) {
		this.hour = hour;
	}
	
}
