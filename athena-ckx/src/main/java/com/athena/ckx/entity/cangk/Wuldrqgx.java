package com.athena.ckx.entity.cangk;

import com.athena.component.entity.Domain;

import com.toft.core3.support.PageableSupport;
/**
 * @description 物理点容器关系
 * @author wangyu
 * @String 2012-12-10
 */
public class Wuldrqgx extends PageableSupport implements Domain{

	private static final long serialVersionUID = -8255964247726895051L;

	private String usercenter;	//用户中心
	
	private String wuld;		//产线编号、其他编号
	
	private String wuldlx;		//1-产线  2-其他
	
	private String wuld2;		//容器区编号
	
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


	public String getWuld() {
		return wuld;
	}


	public void setWuld(String wuld) {
		this.wuld = wuld;
	}


	public String getWuldlx() {
		return wuldlx;
	}


	public void setWuldlx(String wuldlx) {
		this.wuldlx = wuldlx;
	}


	public String getWuld2() {
		return wuld2;
	}


	public void setWuld2(String wuld2) {
		this.wuld2 = wuld2;
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
