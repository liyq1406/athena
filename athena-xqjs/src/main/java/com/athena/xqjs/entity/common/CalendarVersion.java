package com.athena.xqjs.entity.common;

import java.math.BigDecimal;

import com.toft.core3.support.PageableSupport;
/**
 * <p>
 * Title:日历版次实体类
 * </p>
 * <p>
 * Description:日历版次实体类
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company: 软通动力
 * </p>
 * 
 * @author 李明
 * @version v1.0
 * @date 2011-12-13
 */
public class CalendarVersion extends PageableSupport{
	
	private static final long serialVersionUID = -6416990806688863683L;
	private String  banc	;		
	private String  usercenter	;		
	private String riq ;
	private String zhoux ;
	private String nianzx ;
	private String xingq ;	
	private String nianzq	;	
	private BigDecimal xis ;			
	private String shifjfr ;	
	private String shifgzr;		
	private String biaos ;

	public String getZhoux() {
		return zhoux;
	}
	public void setZhoux(String zhoux) {
		this.zhoux = zhoux;
	}
	public String getNianzx() {
		return nianzx;
	}
	public void setNianzx(String nianzx) {
		this.nianzx = nianzx;
	}
	public String getBiaos() {
		return biaos;
	}
	public void setBiaos(String biaos) {
		this.biaos = biaos;
	}
	public String getBanc() {
		return banc;
	}
	public void setBanc(String banc) {
		this.banc = banc;
	}
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	public String getRiq() {
		return riq;
	}
	public void setRiq(String riq) {
		this.riq = riq;
	}
	public String getXingq() {
		return xingq;
	}
	public void setXingq(String xingq) {
		this.xingq = xingq;
	}
	
	public String getNianzq() {
		return nianzq;
	}
	public void setNianzq(String nianzq) {
		this.nianzq = nianzq;
	}
	
	public BigDecimal getXis() {
		return xis;
	}
	public void setXis(BigDecimal xis) {
		this.xis = xis;
	}
	public String getShifjfr() {
		return shifjfr;
	}
	public void setShifjfr(String shifjfr) {
		this.shifjfr = shifjfr;
	}
	public String getShifgzr() {
		return shifgzr;
	}
	public void setShifgzr(String shifgzr) {
		this.shifgzr = shifgzr;
	}
	
	

}
