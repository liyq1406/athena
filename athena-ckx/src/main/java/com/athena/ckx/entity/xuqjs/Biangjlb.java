package com.athena.ckx.entity.xuqjs;

import com.athena.component.entity.Domain;

import com.toft.core3.support.PageableSupport;
/**
 * 变更记录表
 * @author denggq
 * @String 2012-4-26
 */
public class Biangjlb extends PageableSupport implements Domain{

	private static final long serialVersionUID = -4374131229646038963L;

	private String usercenter   ;//  用户中心 
	
	private String bianglx    	;//  变更类型（1 仓库 2 消耗点）
	
	private String lingjbh    	;//  零件编号
	
	private String yuanbh		;//  原编号
	
	private String xianbh       ;//  先编号
	
	private String creator		;//	  创建人
	
	private String create_time	;//	  创建时间
	
	private String editor		;//	  修改人
	
	private String edit_time	;//	  修改时间
	
	private String shifsy      ;//是否使用(看板)


	public String getShifsy() {
		return shifsy;
	}


	public void setShifsy(String shifsy) {
		this.shifsy = shifsy;
	}


	public String getUsercenter() {
		return usercenter;
	}


	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}


	public String getLingjbh() {
		return lingjbh;
	}


	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}


	public void setYuanbh(String yuanbh) {
		this.yuanbh = yuanbh;
	}


	public void setBianglx(String bianglx) {
		this.bianglx = bianglx;
	}


	public String getBianglx() {
		return bianglx;
	}


	public String getYuanbh() {
		return yuanbh;
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


	public void setXianbh(String xianbh) {
		this.xianbh = xianbh;
	}


	public String getXianbh() {
		return xianbh;
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
