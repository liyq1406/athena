package com.athena.truck.entity;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

public class ChacChew  extends PageableSupport implements Domain{
	private static final long serialVersionUID = 1L;
	
	private String usercenter; //用户中心
	private String quybh;		//区域编号
	private String daztbh;		//大站台编号
	private String chacbh;		//叉车编号
	private String chewbh;		//车位编号
	private String creator;		//创建人
	
	private String create_time;	//创建时间
	
	private String editor;		//修改人
	
	private String edit_time;	//修改时间
	
	

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

	public String getDaztbh() {
		return daztbh;
	}

	public void setDaztbh(String daztbh) {
		this.daztbh = daztbh;
	}

	public String getChacbh() {
		return chacbh;
	}

	public void setChacbh(String chacbh) {
		this.chacbh = chacbh;
	}

	public String getChewbh() {
		return chewbh;
	}

	public void setChewbh(String chewbh) {
		this.chewbh = chewbh;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setId(String id) {
		// TODO Auto-generated method stub
		
	}

	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
