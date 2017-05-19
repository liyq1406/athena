package com.athena.xqjs.entity.fenzxpc;

import com.toft.core3.support.PageableSupport;

/**
 * 焊装大线
 */
public class Dax extends PageableSupport{

	private static final long serialVersionUID = -290050400881856965L;

	private String usercenter	;//用户中心
	
	private String daxxh		;//大线线号
	
	private Integer paicfbq			;//排产封闭期
	
	private Integer chaifts			;//拆分天数
	
	private String beiz			;//备注
	
	private String biaos		;//标识
	
	private Integer chews;			;//车位数
	
	private Integer shengcjp		;//生产节拍
	
	private String creator		;//创建人
	
	private String create_time	;//创建时间
	
	private String editor		;//修改人
	
	private String edit_time	;//修改时间
	
	private String daxlx		;//大线类型	1：焊装	2：总装			

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getDaxxh() {
		return daxxh;
	}

	public void setDaxxh(String daxxh) {
		this.daxxh = daxxh;
	}

	public String getBeiz() {
		return beiz;
	}

	public void setBeiz(String beiz) {
		this.beiz = beiz;
	}

	public String getBiaos() {
		return biaos;
	}

	public void setBiaos(String biaos) {
		this.biaos = biaos;
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

	public String getDaxlx() {
		return daxlx;
	}

	public void setDaxlx(String daxlx) {
		this.daxlx = daxlx;
	}

	public Integer getPaicfbq() {
		return paicfbq;
	}

	public void setPaicfbq(Integer paicfbq) {
		this.paicfbq = paicfbq;
	}

	public Integer getChaifts() {
		return chaifts;
	}

	public void setChaifts(Integer chaifts) {
		this.chaifts = chaifts;
	}

	public Integer getChews() {
		return chews;
	}

	public void setChews(Integer chews) {
		this.chews = chews;
	}

	public Integer getShengcjp() {
		return shengcjp;
	}

	public void setShengcjp(Integer shengcjp) {
		this.shengcjp = shengcjp;
	}

}
