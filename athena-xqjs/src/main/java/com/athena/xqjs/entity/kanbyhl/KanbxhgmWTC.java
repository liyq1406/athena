package com.athena.xqjs.entity.kanbyhl;


public class KanbxhgmWTC {
	
	
	private static final long serialVersionUID = 1L;
	/**
	 * 1位用户中心+1位车间代码+1位产线+5位顺序号     
	 */
	private   String     xunhbm        ;                      
	/**
	 * 供货模式                                      
	 */
	private   String     gonghms       ;
	/**
	 * 状态
	 */
	private String zhuangt;
	
	public String getXunhbm() {
		return xunhbm;
	}
	public void setXunhbm(String xunhbm) {
		this.xunhbm = xunhbm;
	}
	public String getGonghms() {
		return gonghms;
	}
	public void setGonghms(String gonghms) {
		this.gonghms = gonghms;
	}

	public String getZhuangt() {
		return zhuangt;
	}

	public void setZhuangt(String zhuangt) {
		this.zhuangt = zhuangt;
	}
	

}
