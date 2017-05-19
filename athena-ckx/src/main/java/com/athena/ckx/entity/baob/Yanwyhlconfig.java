package com.athena.ckx.entity.baob;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

public class Yanwyhlconfig extends PageableSupport implements Domain {

	/**
	 * 11458 延误要货令大屏配置ckx_ywyhldp_config
	 */
	private static final long serialVersionUID = 1L;
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setId(String arg0) {
		// TODO Auto-generated method stub

	}

	private String usercenter;
	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private int dapsxsj;
	private float yanwbjsj;
	private int yanwbjsl;
	private String cangkbh;
	private String chanx;
	private String lingjbh;
	private String yaohllx;
	private String yaohlzt;
	private String gongysdm;
	private String chengysdm;
	private String jihy;
	//工厂选择
	private String factory;
	private String currentDate;
	private String sqlInject;
	//工厂 2015-9-22日新增
	private String gongc;
	//0011894
	private String chengyslx;
	private String chengysmc;
	
	
	
	
	public String getChengyslx() {
		return chengyslx;
	}

	public void setChengyslx(String chengyslx) {
		this.chengyslx = chengyslx;
	}

	public String getChengysmc() {
		return chengysmc;
	}

	public void setChengysmc(String chengysmc) {
		this.chengysmc = chengysmc;
	}

	public String getGongc() {
		return gongc;
	}

	public void setGongc(String gongc) {
		this.gongc = gongc;
	}

	public String getSqlInject() {
		return sqlInject;
	}

	public void setSqlInject(String sqlInject) {
		this.sqlInject = sqlInject;
	}

	//11458
	private int currentPage;
	private int pageSize;
	
	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}

	public String getFactory() {
		return factory;
	}

	public void setFactory(String factory) {
		this.factory = factory;
	}

	public String getJihy() {
		return jihy;
	}

	public void setJihy(String jihy) {
		this.jihy = jihy;
	}

	public int getDapsxsj() {
		return dapsxsj;
	}

	public void setDapsxsj(int dapsxsj) {
		this.dapsxsj = dapsxsj;
	}

	public float getYanwbjsj() {
		return yanwbjsj;
	}

	public void setYanwbjsj(float yanwbjsj) {
		this.yanwbjsj = yanwbjsj;
	}

	public int getYanwbjsl() {
		return yanwbjsl;
	}

	public void setYanwbjsl(int yanwbjsl) {
		this.yanwbjsl = yanwbjsl;
	}

	public String getCangkbh() {
		return cangkbh;
	}

	public void setCangkbh(String cangkbh) {
		this.cangkbh = cangkbh;
	}

	public String getChanx() {
		return chanx;
	}

	public void setChanx(String chanx) {
		this.chanx = chanx;
	}

	public String getLingjbh() {
		return lingjbh;
	}

	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}

	public String getYaohllx() {
		return yaohllx;
	}

	public void setYaohllx(String yaohllx) {
		this.yaohllx = yaohllx;
	}

	public String getYaohlzt() {
		return yaohlzt;
	}

	public void setYaohlzt(String yaohlzt) {
		this.yaohlzt = yaohlzt;
	}

	public String getGongysdm() {
		return gongysdm;
	}

	public void setGongysdm(String gongysdm) {
		this.gongysdm = gongysdm;
	}

	public String getChengysdm() {
		return chengysdm;
	}

	public void setChengysdm(String chengysdm) {
		this.chengysdm = chengysdm;
	}
	
	
}
