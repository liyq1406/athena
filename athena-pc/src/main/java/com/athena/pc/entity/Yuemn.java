package com.athena.pc.entity;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * <p>
 * Title:月模拟实体类
 * </p>
 * <p>
 * Description:定义月模拟实体变量
 * </p>
 * <p>
 * Copyright: Copyright (c) 2012
 * </p>
 * <p>
 * Company: 软通动力
 * </p>
 * 
 * @author 贺志国
 * @E-mail zghe@isoftstone.com
 * @version v1.0
 * @date 2012-2-13
 */
public class Yuemn extends PageableSupport implements Domain {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 4671986546862494712L;
	private String usercenter;    //用户中心
	private String appobj;        //车间产线编号/仓库编号
	private String riq;           //日期
	private String kaissj;        //开始时间
	private String jiezsj;        //截至时间
	private String hour;          //工时
	private String gundmngs;      //滚动模拟工时
	private String gongzbh;       //工作编号
	private int beat;			  //产线节拍
	private String biaos;       //标识	
	
	
	
	public String getGundmngs() {
		return gundmngs;
	}

	public void setGundmngs(String gundmngs) {
		this.gundmngs = gundmngs;
	}

	public int getBeat() {
		return beat;
	}

	public void setBeat(int beat) {
		this.beat = beat;
	}

	public String getGongzbh() {
		return gongzbh;
	}

	public void setGongzbh(String gongzbh) {
		this.gongzbh = gongzbh;
	}

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getAppobj() {
		return appobj;
	}

	public void setAppobj(String appobj) {
		this.appobj = appobj;
	}

	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		this.riq = riq;
	}

	public String getKaissj() {
		return kaissj;
	}

	public void setKaissj(String kaissj) {
		this.kaissj = kaissj;
	}

	public String getJiezsj() {
		return jiezsj;
	}

	public void setJiezsj(String jiezsj) {
		this.jiezsj = jiezsj;
	}

	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}

	public void setId(String id) {
		
	}

	public String getId() {
		
		return null;
	}

	public String getBiaos() {
		return biaos;
	}

	public void setBiaos(String biaos) {
		this.biaos = biaos;
	}

}
