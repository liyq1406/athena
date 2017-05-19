

package com.athena.ckx.entity.baob;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 实时延误要货令  11458
 */
public class Shisywyhl extends PageableSupport implements Domain{

	private static final long serialVersionUID = 2892190665367165024L;

	private String usercenter;
	
	private String lingjbh;
	
	private String lingjmc;
	
	private String keh;
	
	private String mudd;
	
	private String yaohllx;
	
	private String yaohlzt;
	
	private String yaohlh;
	
	private String zuiwdhrq;
	
	private String shik;
	
	private String gongysdm;
	
	private String gongysmc;
	
	private String chengysdm;
	
//0008931 

	private String chengysmc;
	
	private String jihy;
	
	private String yaohsl;
	
	private String currentDate;
	
	private String zuiwsj;
	
	private String zuiwsj_from;
	
	private String zuiwsj_to;

	//11472
	private String yancyy;
	//11458
	private String factory;
	private String cangkbh;
	private String chanx;
	//延误时间，分钟
	private long  yanwsj;
	
	//huxy_12905
	private String shiflsk;
	private String shiffsgys;
	private String beiz3;
	
	public String getBeiz3() {
		return beiz3;
	}

	public void setBeiz3(String beiz3) {
		this.beiz3 = beiz3;
	}
	
	public long getYanwsj() {
		return yanwsj;
	}

	public void setYanwsj(long yanwsj) {
		this.yanwsj = yanwsj;
	}
	
	public String getShiflsk() {
		return shiflsk;
	}

	public void setShiflsk(String shiflsk) {
		this.shiflsk = shiflsk;
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

	public String getFactory() {
		return factory;
	}

	public void setFactory(String factory) {
		this.factory = factory;
	}

	public String getYancyy() {
		return yancyy;
	}

	public void setYancyy(String yancyy) {
		this.yancyy = yancyy;
	}

	public String getYaohlzt() {
		return yaohlzt;
	}

	public void setYaohlzt(String yaohlzt) {
		this.yaohlzt = yaohlzt;
	}

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getZuiwsj_from() {
		return zuiwsj_from;
	}

	public void setZuiwsj_from(String zuiwsj_from) {
		this.zuiwsj_from = zuiwsj_from;
	}

	public String getZuiwsj_to() {
		return zuiwsj_to;
	}

	public void setZuiwsj_to(String zuiwsj_to) {
		this.zuiwsj_to = zuiwsj_to;
	}

	public String getZuiwsj() {
		return zuiwsj;
	}

	public void setZuiwsj(String zuiwsj) {
		this.zuiwsj = zuiwsj;
	}

	public String getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}

	public String getLingjbh() {
		return lingjbh;
	}

	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}

	public String getKeh() {
		return keh;
	}

	public void setKeh(String keh) {
		this.keh = keh;
	}

	public String getMudd() {
		return mudd;
	}

	public void setMudd(String mudd) {
		this.mudd = mudd;
	}

	public String getYaohllx() {
		return yaohllx;
	}

	public void setYaohllx(String yaohllx) {
		this.yaohllx = yaohllx;
	}

	
	

	public String getZuiwdhrq() {
		return zuiwdhrq;
	}

	public void setZuiwdhrq(String zuiwdhrq) {
		this.zuiwdhrq = zuiwdhrq;
	}

	public String getShik() {
		return shik;
	}

	public void setShik(String shik) {
		this.shik = shik;
	}

	public String getGongysdm() {
		return gongysdm;
	}

	public void setGongysdm(String gongysdm) {
		this.gongysdm = gongysdm;
	}

	public String getGongysmc() {
		return gongysmc;
	}

	public void setGongysmc(String gongysmc) {
		this.gongysmc = gongysmc;
	}

	public String getJihy() {
		return jihy;
	}

	public void setJihy(String jihy) {
		this.jihy = jihy;
	}

	public String getYaohsl() {
		return yaohsl;
	}

	public void setYaohsl(String yaohsl) {
		this.yaohsl = yaohsl;
	}

	
	
	public String getYaohlh() {
		return yaohlh;
	}

	public void setYaohlh(String yaohlh) {
		this.yaohlh = yaohlh;
	}

	public String getChengysdm() {
		return chengysdm;
	}

	public void setChengysdm(String chengysdm) {
		this.chengysdm = chengysdm;
	}
	
	public String getLingjmc() {
		return lingjmc;
	}

	public void setLingjmc(String lingjmc) {
		this.lingjmc = lingjmc;
	}

	public String getChengysmc() {
		return chengysmc;
	}

	public void setChengysmc(String chengysmc) {
		this.chengysmc = chengysmc;
	}

	public void setId(String id) {
		
	}

	public String getId() {
		return null;
	}

}
