package com.athena.ckx.entity.cangk;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 发货站台
 * @author denggq
 * @date 2012-1-16
 */
public class Fahzt extends PageableSupport implements Domain{

	private static final long serialVersionUID = -4404892638962030092L;

	private String usercenter;	//用户中心
	
	private String fahztbh;		//发货站台编号
	
	private String fahztmc;		//发货站台名称
	
	private String cangkbh;		//仓库编号
	
	private String biaos;		//标识

	private String creator;		//增加人
	
	private String create_time;	//增加时间
	
	private String editor;		//修改人
	
	private String edit_time;	//修改时间

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getFahztbh() {
		return fahztbh;
	}

	public void setFahztbh(String fahztbh) {
		this.fahztbh = fahztbh;
	}

	public String getFahztmc() {
		return fahztmc;
	}

	public void setFahztmc(String fahztmc) {
		this.fahztmc = fahztmc;
	}

	public void setCangkbh(String cangkbh) {
		this.cangkbh = cangkbh;
	}

	public String getCangkbh() {
		return cangkbh;
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

	public void setId(String id) {
		
	}

	public String getId() {
		return null;
	}

	
	
}
