package com.athena.truck.entity;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

public class Chac extends PageableSupport implements Domain{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3242498244528380931L;

	private String usercenter;	//用户中心
	
	private String chacbh;	//叉车编号
	
	private String chacmc;		//叉车名称
	
	private String quybh;	//区域编号
	
	private String daztbh;	//大站台编号
	
	private String chacz;  //叉车组
	
	private String biaos;		//标识
	
	private Integer beiz;	//备注
	
	private Integer beiz1;		//备注1
	
	private String beiz2;		//备注2
	
	private String beiz3;		//备注3
	
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

	

	public String getChacbh() {
		return chacbh;
	}

	public void setChacbh(String chacbh) {
		this.chacbh = chacbh;
	}

	public String getChacmc() {
		return chacmc;
	}

	public void setChacmc(String chacmc) {
		this.chacmc = chacmc;
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

	public String getBiaos() {
		return biaos;
	}

	public void setBiaos(String biaos) {
		this.biaos = biaos;
	}

	public Integer getBeiz() {
		return beiz;
	}

	public void setBeiz(Integer beiz) {
		this.beiz = beiz;
	}

	public Integer getBeiz1() {
		return beiz1;
	}

	public void setBeiz1(Integer beiz1) {
		this.beiz1 = beiz1;
	}

	public String getBeiz2() {
		return beiz2;
	}

	public void setBeiz2(String beiz2) {
		this.beiz2 = beiz2;
	}

	public String getBeiz3() {
		return beiz3;
	}

	public void setBeiz3(String beiz3) {
		this.beiz3 = beiz3;
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

	

	public String getId() {
		
		return null;
	}

	public void setId(String arg0) {
		
		
	}

	public void setChacz(String chacz) {
		this.chacz = chacz;
	}

	public String getChacz() {
		return chacz;
	}
	
	


}
