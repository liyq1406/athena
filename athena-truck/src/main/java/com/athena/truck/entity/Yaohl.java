package com.athena.truck.entity;

import com.toft.core3.support.PageableSupport;

public class Yaohl  extends PageableSupport{
	private static final long serialVersionUID = 1L;
	private String usercenter	;       //用户中心
	private String yaohlh	    ;       //要货令号
	private String zuizsj	    ;     	//最早交付时间
	private String zuiwsj     ;         //最晚交付时间
	private String chengysdm  ;         //承运商代码
	private String daztbh     ;         //大站台编号
	private String xiehd     ;          //卸货点
	private String cangkbh   ;          //仓库编号
	private String mudd      ;          //目的地
	private String shangxfs  ;          //上线方式
	private String editor     ;         //修改人
	private String edit_time  ;         //修改时间
	private String neweditor  ;         //新修改人
	private String yaohllx    ;         //供货模式
	private String lingjbh    ;         //零件编号
	private String faysj      ;         //预计发运时间
	private String yaohlzt    ;         //要货令状态
	public String getYaohlzt() {
		return yaohlzt;
	}
	public void setYaohlzt(String yaohlzt) {
		this.yaohlzt = yaohlzt;
	}
	public String getFaysj() {
		return faysj;
	}
	public void setFaysj(String faysj) {
		this.faysj = faysj;
	}
	public String getLingjbh() {
		return lingjbh;
	}
	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}
	public String getYaohllx() {
		return yaohllx;
	}
	public void setYaohllx(String yaohllx) {
		this.yaohllx = yaohllx;
	}
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	public String getYaohlh() {
		return yaohlh;
	}
	public void setYaohlh(String yaohlh) {
		this.yaohlh = yaohlh;
	}
	public String getZuizsj() {
		return zuizsj;
	}
	public void setZuizsj(String zuizsj) {
		this.zuizsj = zuizsj;
	}
	public String getZuiwsj() {
		return zuiwsj;
	}
	public void setZuiwsj(String zuiwsj) {
		this.zuiwsj = zuiwsj;
	}
	public String getChengysdm() {
		return chengysdm;
	}
	public void setChengysdm(String chengysdm) {
		this.chengysdm = chengysdm;
	}
	public String getDaztbh() {
		return daztbh;
	}
	public void setDaztbh(String daztbh) {
		this.daztbh = daztbh;
	}
	public String getXiehd() {
		return xiehd;
	}
	public void setXiehd(String xiehd) {
		this.xiehd = xiehd;
	}
	public String getCangkbh() {
		return cangkbh;
	}
	public void setCangkbh(String cangkbh) {
		this.cangkbh = cangkbh;
	}
	public String getMudd() {
		return mudd;
	}
	public void setMudd(String mudd) {
		this.mudd = mudd;
	}
	public String getShangxfs() {
		return shangxfs;
	}
	public void setShangxfs(String shangxfs) {
		this.shangxfs = shangxfs;
	}
	public String getEditor() {
		return editor;
	}
	public void setEditor(String editor) {
		this.editor = editor;
	}
	public String getEdit_time() {
		return edit_time;
	}
	public void setEdit_time(String edit_time) {
		this.edit_time = edit_time;
	}
	public String getNeweditor() {
		return neweditor;
	}
	public void setNeweditor(String neweditor) {
		this.neweditor = neweditor;
	}

}
