package com.athena.xqjs.entity.common;

import java.math.BigDecimal;

import com.toft.core3.support.PageableSupport;
/**
 * <p>
 * Title:最大订货量
 * </p>
 * <p>
 * Description:最大订货量
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
 * @date 2012-3-6
 */
public class Zuiddhsl extends PageableSupport{
	private static final long serialVersionUID = -6416990806688863683L;
	private String  usercenter	;	
	private String gongysbh ;
	private String lingjbh ;
	private String nianzq ;
	private BigDecimal zuiddhsl ;
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
	public String getLingjbh() {
		return lingjbh;
	}
	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}
	public String getNianzq() {
		return nianzq;
	}
	public void setNianzq(String nianzq) {
		this.nianzq = nianzq;
	}
	public BigDecimal getZuiddhsl() {
		return zuiddhsl;
	}
	public void setZuiddhsl(BigDecimal zuiddhsl) {
		this.zuiddhsl = zuiddhsl;
	}
	
}
