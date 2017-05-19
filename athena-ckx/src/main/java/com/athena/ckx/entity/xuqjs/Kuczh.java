package com.athena.ckx.entity.xuqjs;

import java.math.BigDecimal;
import java.util.Date;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;
/*
 * 库存置换表
 * @author zbb
 */

public class Kuczh extends PageableSupport implements Domain{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8039502862021809080L;
	//用户中心
	private String usercenter;
	//仓库
	private String cangk; 
	//是否考虑仓库计算
	private String iskaolckjs;
	//创建者
	private String creator;
	//创建时间
	private String create_time; 
	//修改者
	private String editor;
	//修改时间
	private String edit_time; 
	//备注1
	private String beiz1;
	//备注2
	private String beiz2; 
	//备注3
	private BigDecimal beiz3;
	
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	public String getCangk() {
		return cangk;
	}
	public void setCangk(String cangk) {
		this.cangk = cangk;
	}
	public String getIskaolckjs() {
		return iskaolckjs;
	}
	public void setIskaolckjs(String iskaolckjs) {
		this.iskaolckjs = iskaolckjs;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	public String getEditor() {
		return editor;
	}
	public void setEditor(String editor) {
		this.editor = editor;
	}
	
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getEdit_time() {
		return edit_time;
	}
	public void setEdit_time(String edit_time) {
		this.edit_time = edit_time;
	}
	public String getBeiz1() {
		return beiz1;
	}
	public void setBeiz1(String beiz1) {
		this.beiz1 = beiz1;
	}
	public String getBeiz2() {
		return beiz2;
	}
	public void setBeiz2(String beiz2) {
		this.beiz2 = beiz2;
	}
	public BigDecimal getBeiz3() {
		return beiz3;
	}
	public void setBeiz3(BigDecimal beiz3) {
		this.beiz3 = beiz3;
	}
	@Override
	public void setId(String id) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	

}
