package com.athena.fj.entity;

import java.util.Date;

import com.toft.core3.support.PageableSupport;

/**
 * 要货令归集bean类
 * @author hezhiguo
 * @date 2011-12-8
 * @Email:zghe@isoftstone.com
 */
public class Yaohlgj extends PageableSupport {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = -6835769891527923456L;

	//要货令表
	private String yaohlh;     //要货令号
	private String usercenter; //用户中心
	private String lingjbh;    //零件编号
	private String baozxh;     //UA包装型号
	private Date faysj;       //发运时间 
	private String keh;        //客户 
	public String getYaohlh() {
		return yaohlh;
	}
	public void setYaohlh(String yaohlh) {
		this.yaohlh = yaohlh;
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
	public String getBaozxh() {
		return baozxh;
	}
	public void setBaozxh(String baozxh) {
		this.baozxh = baozxh;
	}
	
 
	public Date getFaysj() {
		return faysj;
	}
	public void setFaysj(Date faysj) {
		this.faysj = faysj;
	}
	public String getKeh() {
		return keh;
	}
	public void setKeh(String keh) {
		this.keh = keh;
	}
 
	
}
