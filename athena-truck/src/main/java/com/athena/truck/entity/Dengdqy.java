package com.athena.truck.entity;

import com.athena.component.entity.Domain;

import com.toft.core3.support.PageableSupport;
/**
 * 等待区域
 * @author chenpeng
 * @String 2015-1-7
 */
public class Dengdqy extends PageableSupport implements Domain{

	private static final long serialVersionUID = 6237059231705528671L;

	private String usercenter   ;//  用户中心 
	
	private String quybh        ;//区域编号
	
	private String quymc        ;//区域名称
	
	private Integer dapzrxssj      ;//大站台刷新时间（分钟）
	
	private Integer shuaxsj      ;//大站台刷新时间（分钟）
	
	private String biaos        ;//  标识
	
	private String kacqyz		;//卡车区域组

	private String creator;       //创建人
	
	private String create_time;   //创建时间
	
	private String editor;        //修改人
	
	private String edit_time;      //修改时间

	public Integer getDapzrxssj() {
		return dapzrxssj;
	}


	public void setDapzrxssj(Integer dapzrxssj) {
		this.dapzrxssj = dapzrxssj;
	}


	public String getUsercenter() {
		return usercenter;
	}


	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}


	public String getQuybh() {
		return quybh;
	}


	public void setQuybh(String quybh) {
		this.quybh = quybh;
	}


	public String getQuymc() {
		return quymc;
	}


	public void setQuymc(String quymc) {
		this.quymc = quymc;
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


	public Integer getShuaxsj() {
		return shuaxsj;
	}


	public void setShuaxsj(Integer shuaxsj) {
		this.shuaxsj = shuaxsj;
	}


	public String getKacqyz() {
		return kacqyz;
	}


	public void setKacqyz(String kacqyz) {
		this.kacqyz = kacqyz;
	}


	public void setId(String id) {
		
	}


	public String getId() {
		return null;
	}
	

	
	
	
}
