package com.athena.print.entity.sys;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 仓库
 * @author denggq
 * @date 2012-1-12
 */
public class Cangk extends PageableSupport implements Domain{

	private static final long serialVersionUID = 2892190665367165024L;

	private String usercenter;	//用户中心
	
	private String cangkbh;		//仓库编号
	
	private String cangklx;		//仓库类型
	
	private String rongqcbh;	//容器场编号
	
	private String daoctqq;		//到车提前期(排产)
	
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

	public String getCangkbh() {
		return cangkbh;
	}

	public void setCangkbh(String cangkbh) {
		this.cangkbh = cangkbh;
	}

	public String getCangklx() {
		return cangklx;
	}

	public void setCangklx(String cangklx) {
		this.cangklx = cangklx;
	}

	public String getRongqcbh() {
		return rongqcbh;
	}

	public void setRongqcbh(String rongqcbh) {
		this.rongqcbh = rongqcbh;
	}

	public String getDaoctqq() {
		return daoctqq;
	}

	public void setDaoctqq(String daoctqq) {
		this.daoctqq = daoctqq;
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
		// TODO Auto-generated method stub
		
	}

	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

}
