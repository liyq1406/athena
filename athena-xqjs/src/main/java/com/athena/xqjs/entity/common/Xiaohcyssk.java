package com.athena.xqjs.entity.common;

import java.math.BigDecimal;

import com.toft.core3.support.PageableSupport;
/**
 * Title:小火车运输时刻
 * Copyright: Copyright (c) 2011
 * Company: 软通动力
 * @author 李明
 * @version v1.0
 * @date 2012-3-23
 */
public class Xiaohcyssk extends PageableSupport{
	
	private static final long serialVersionUID = -6416990806688863683L;
	private String  usercenter	;		
	private String riq ;
	private BigDecimal tangc ;
	private String shengcxbh ;
	private String kaisbhsj ;
	private String chufsxsj ;
	private String creator ;	
	private String create_time	;	
	private String editor ;
	private String edit_time ;
	private String xiaohcbh ;
	
	public String getXiaohcbh() {
		return xiaohcbh;
	}
	public void setXiaohcbh(String xiaohcbh) {
		this.xiaohcbh = xiaohcbh;
	}
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	public String getRiq() {
		return riq;
	}
	public void setRiq(String riq) {
		this.riq = riq;
	}
	public BigDecimal getTangc() {
		return tangc;
	}
	public void setTangc(BigDecimal tangc) {
		this.tangc = tangc;
	}
	public String getShengcxbh() {
		return shengcxbh;
	}
	public void setShengcxbh(String shengcxbh) {
		this.shengcxbh = shengcxbh;
	}
	public String getKaisbhsj() {
		return kaisbhsj;
	}
	public void setKaisbhsj(String kaisbhsj) {
		this.kaisbhsj = kaisbhsj;
	}
	public String getChufsxsj() {
		return chufsxsj;
	}
	public void setChufsxsj(String chufsxsj) {
		this.chufsxsj = chufsxsj;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
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
	
}
