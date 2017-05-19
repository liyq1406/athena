package com.athena.ckx.entity.cangk;

import com.athena.component.entity.Domain;

import com.toft.core3.support.PageableSupport;
/**
 * @description 容器总帐
 * @author wangyu
 * @String 2012-12-10
 */
public class Rongqzz extends PageableSupport implements Domain{

	private static final long serialVersionUID = -8255964247726895051L;

	private String usercenter;	//用户中心
	
	private String wuld;		//容器区编号
	
	private String jzlx;		//1-内部  2-外部
	
	private String chengys;		
	
	private String gongysdm;			
	
	private String rongqlb;  //容器类别
	
	private Integer rongqsl;//容器数量
	
	private String rongqzt;//容器状态
	
	private String edit_time;//	  修改时间
	

	
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


	public String getJzlx() {
		return jzlx;
	}


	public void setJzlx(String jzlx) {
		this.jzlx = jzlx;
	}


	public String getChengys() {
		return chengys;
	}


	public void setChengys(String chengys) {
		this.chengys = chengys;
	}


	public String getGongysdm() {
		return gongysdm;
	}


	public void setGongysdm(String gongysdm) {
		this.gongysdm = gongysdm;
	}


	public String getRongqlb() {
		return rongqlb;
	}


	public void setRongqlb(String rongqlb) {
		this.rongqlb = rongqlb;
	}


	

	public Integer getRongqsl() {
		return rongqsl;
	}


	public void setRongqsl(Integer rongqsl) {
		this.rongqsl = rongqsl;
	}


	public String getRongqzt() {
		return rongqzt;
	}


	public void setRongqzt(String rongqzt) {
		this.rongqzt = rongqzt;
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
