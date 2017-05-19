package com.athena.ckx.entity.xuqjs;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

public class Tuopbzdygx extends PageableSupport implements Domain{
	
	private static final long serialVersionUID = 1861676571164967553L;
	//托盘包装对应关系
	private String usercenter;		//用户名称
	
	private String tuopxh;		//托盘型号
	
	private String baozxh;		//包装型号
	
	private Integer baozgs;		//包装个数
	
	
	private String creator;		//创建人
	
	private String create_time;		//创建时间
	
	
	private String editor;		//修改人
	
	private String edit_time;	//修改时间
	
	private String biaos;	//标识
	
	
	private String uclist; //用户组对应的有权限的用户中心
	
	
	
	public String getUclist() {
		return uclist;
	}

	public void setUclist(String uclist) {
		this.uclist = uclist;
	}



	public String getTuopxh() {
		return tuopxh;
	}

	public void setTuopxh(String tuopxh) {
		this.tuopxh = tuopxh;
	}

	public String getBaozxh() {
		return baozxh;
	}

	public void setBaozxh(String baozxh) {
		this.baozxh = baozxh;
	}



	public Integer getBaozgs() {
		return baozgs;
	}

	public void setBaozgs(Integer baozgs) {
		this.baozgs = baozgs;
	}

	public String getBiaos() {
		return biaos;
	}

	public void setBiaos(String biaos) {
		this.biaos = biaos;
	}

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
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
