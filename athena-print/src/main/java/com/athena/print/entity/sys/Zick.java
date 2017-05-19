package com.athena.print.entity.sys;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 子仓库
 * @author denggq
 * @date 2012-1-10
 */
public class Zick extends PageableSupport implements Domain{

	private static final long serialVersionUID = -5496658355068498708L;

	private String usercenter;	//用户中心
	
	private String cangkbh;		//仓库编号
	
	private String zickbh;		//子仓库编号
	
	private Integer baohd;		//饱和度百分比
	
	private String shifelgl;	//是否按EL号管理
	
	private String guanlyzbh;	//管理员组编号
	
	private String zhantbh;		//站台编号
	
	private String biaos;		//标识
	
	private String creator;
	
	private String create_time;
	
	private String editor;
	
	private String edit_time;
	
	private String yuanzhantbh; //原站台编号
	
	
	public String getYuanzhantbh() {
		return yuanzhantbh;
	}

	public void setYuanzhantbh(String yuanzhantbh) {
		this.yuanzhantbh = yuanzhantbh;
	}

	public String getZickbh() {
		return zickbh;
	}

	public void setZickbh(String zickbh) {
		this.zickbh = zickbh;
	}

	public Integer getBaohd() {
		return baohd;
	}

	public void setBaohd(Integer baohd) {
		this.baohd = baohd;
	}

	public String getShifelgl() {
		return shifelgl;
	}

	public void setShifelgl(String shifelgl) {
		this.shifelgl = shifelgl;
	}

	public String getGuanlyzbh() {
		return guanlyzbh;
	}

	public void setGuanlyzbh(String guanlyzbh) {
		this.guanlyzbh = guanlyzbh;
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

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getCangkbh() {
		return cangkbh;
	}

	public void setCangkbh(String cangkbh) {
		this.cangkbh = cangkbh;
	}

	public String getZhantbh() {
		return zhantbh;
	}



	public void setZhantbh(String zhantbh) {
		this.zhantbh = zhantbh;
	}



	public void setId(String id) {
		
	}

	public String getId() {
		return null;
	}


}
