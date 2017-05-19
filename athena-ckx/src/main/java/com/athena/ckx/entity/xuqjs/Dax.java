package com.athena.ckx.entity.xuqjs;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 焊装大线
 */
public class Dax extends PageableSupport implements Domain{

	private static final long serialVersionUID = -4414621682577320708L;
	
	private String usercenter	;//用户中心
	
	private String daxxh		;//大线线号
	
	private int paicfbq			;//排产封闭期
	
	private int chaifts			;//拆分天数
	
	private String beiz			;//备注
	
	private String biaos		;//标识
	
	private String creator		;//创建人
	
	private String create_time	;//创建时间
	
	private String editor		;//修改人
	
	private String edit_time	;//修改时间

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

	public int getPaicfbq() {
		return paicfbq;
	}

	public void setPaicfbq(int paicfbq) {
		this.paicfbq = paicfbq;
	}

	public int getChaifts() {
		return chaifts;
	}

	public void setChaifts(int chaifts) {
		this.chaifts = chaifts;
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

	@Override
	public void setId(String id) {
		
	}

	@Override
	public String getId() {
		return null;
	}

}
