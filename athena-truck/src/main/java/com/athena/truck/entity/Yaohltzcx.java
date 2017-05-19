package com.athena.truck.entity;

import com.athena.component.entity.Domain;

import com.toft.core3.support.PageableSupport;
/**
 * 要货令调整查询
 * @author chenpeng
 * @String 2015-1-29
 */
public class Yaohltzcx extends PageableSupport implements Domain{

	private static final long serialVersionUID = 2977280608657066971L;

	private String usercenter   ;//  用户中心 
	
	private String daztbh        ;//大站台编号
	
	private String xiehzt        ;//卸货站台
	
	private String chengysbh		;//承运商编号
	
	private String tiaozkssj	;//调整开始时间
	
	private String tiaozjssj	;//要货令调整结束时间
	
	private Integer tiaozgs		;//调整要货令个数
	
	private double tiaozsj		;//调整时间
	
	private String operator		;//操作人
	
	private String operate_time 	;//操作时间
	
	private String xiehztbz;        //卸货站台编组
	
	private String yaohllx;         //要货令供货模式
	
	private String yaohlzt;         //要货令状态 

	public String getYaohlzt() {
		return yaohlzt;
	}

	public void setYaohlzt(String yaohlzt) {
		this.yaohlzt = yaohlzt;
	}

	public String getYaohllx() {
		return yaohllx;
	}

	public void setYaohllx(String yaohllx) {
		this.yaohllx = yaohllx;
	}

	public String getXiehztbz() {
		return xiehztbz;
	}

	public void setXiehztbz(String xiehztbz) {
		this.xiehztbz = xiehztbz;
	}

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getDaztbh() {
		return daztbh;
	}

	public void setDaztbh(String daztbh) {
		this.daztbh = daztbh;
	}

	public String getXiehzt() {
		return xiehzt;
	}

	public void setXiehzt(String xiehzt) {
		this.xiehzt = xiehzt;
	}

	
	public String getChengysbh() {
		return chengysbh;
	}

	public void setChengysbh(String chengysbh) {
		this.chengysbh = chengysbh;
	}

	public String getTiaozkssj() {
		return tiaozkssj;
	}

	public void setTiaozkssj(String tiaozkssj) {
		this.tiaozkssj = tiaozkssj;
	}

	public String getTiaozjssj() {
		return tiaozjssj;
	}

	public void setTiaozjssj(String tiaozjssj) {
		this.tiaozjssj = tiaozjssj;
	}

	public Integer getTiaozgs() {
		return tiaozgs;
	}

	public void setTiaozgs(Integer tiaozgs) {
		this.tiaozgs = tiaozgs;
	}

	public double getTiaozsj() {
		return tiaozsj;
	}

	public void setTiaozsj(double tiaozsj) {
		this.tiaozsj = tiaozsj;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}


	public String getOperate_time() {
		return operate_time;
	}

	public void setOperate_time(String operate_time) {
		this.operate_time = operate_time;
	}

	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setId(String arg0) {
		// TODO Auto-generated method stub
		
	}

	
	
}
