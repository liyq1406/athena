package com.athena.pc.entity;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;
/**
 * <p>
 * Title:要货令实体类
 * </p>
 * <p>
 * Description:定义延迟要货令查询实体变量
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
 * @date 2012-6-15
 */
public class Yaonbhl extends PageableSupport  implements Domain{

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 8816930613134517972L;
	
	private String yaohlh;      //要货令号
	private String usercenter;  //用户中心
	private String jiaofj;      //交付时间
	private String lingjbh;     //零件编号
	private String yaohsl;      //要货数量
	private String chanx;       //产线
	
	private String jihyzbh;     //计划员组编号
	
	private String kaissj;      //开始时间
	private String jiessj;      //结束时间
	
	private String lingjsl;     //零件数量
	private String leix;        //类型
	
	
	
	public String getLingjsl() {
		return lingjsl;
	}

	public void setLingjsl(String lingjsl) {
		this.lingjsl = lingjsl;
	}

	public String getLeix() {
		return leix;
	}

	public void setLeix(String leix) {
		this.leix = leix;
	}

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

	public String getJiaofj() {
		return jiaofj;
	}

	public void setJiaofj(String jiaofj) {
		this.jiaofj = jiaofj;
	}

	public String getLingjbh() {
		return lingjbh;
	}

	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}

	public String getYaohsl() {
		return yaohsl;
	}

	public void setYaohsl(String yaohsl) {
		this.yaohsl = yaohsl;
	}

	public String getChanx() {
		return chanx;
	}

	public void setChanx(String chanx) {
		this.chanx = chanx;
	}

	public String getJihyzbh() {
		return jihyzbh;
	}

	public void setJihyzbh(String jihyzbh) {
		this.jihyzbh = jihyzbh;
	}
	
	public String getKaissj() {
		return kaissj;
	}

	public void setKaissj(String kaissj) {
		this.kaissj = kaissj;
	}

	public String getJiessj() {
		return jiessj;
	}

	public void setJiessj(String jiessj) {
		this.jiessj = jiessj;
	}

	public String getId() {
		return null;
	}

	public void setId(String arg0) {
		
	}
	
	

}
