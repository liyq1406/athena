package com.athena.xqjs.entity.anxorder;

import java.math.BigDecimal;
import java.util.Date;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;
/*
 * 供应商小火车
 * @author zbb
 */
public class Gongysxhc extends PageableSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6321255148848999379L;
	//用户中心
	private String usercenter;
	//供应商编号
	private String gongysbh; 
	//生产线编号
	private String shengcxbh;
	//小火车编号
	private String xiaohcbh; 
	//起始趟次
	private Integer qistc; 
	//合并趟次
	private Integer hebtc;
	//是否混托
	private String ishunt;
	//标识，0失效，1有效
	private String biaos;
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
	public String getGongysbh() {
		return gongysbh;
	}
	public void setGongysbh(String gongysbh) {
		this.gongysbh = gongysbh;
	}
	public String getXiaohcbh() {
		return xiaohcbh;
	}
	
	public String getShengcxbh() {
		return shengcxbh;
	}
	public void setShengcxbh(String shengcxbh) {
		this.shengcxbh = shengcxbh;
	}
	public void setXiaohcbh(String xiaohcbh) {
		this.xiaohcbh = xiaohcbh;
	}
	
	public Integer getQistc() {
		return qistc;
	}
	public void setQistc(Integer qistc) {
		this.qistc = qistc;
	}
	public Integer getHebtc() {
		return hebtc;
	}
	public void setHebtc(Integer hebtc) {
		this.hebtc = hebtc;
	}
	public String getIshunt() {
		return ishunt;
	}
	public void setIshunt(String ishunt) {
		this.ishunt = ishunt;
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
	
}
