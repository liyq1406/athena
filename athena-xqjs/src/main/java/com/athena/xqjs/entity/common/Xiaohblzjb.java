package com.athena.xqjs.entity.common;

import java.math.BigDecimal;

import com.toft.core3.support.PageableSupport;
/**
 * <p>
 * Title:消耗比例中间表实体类
 * </p>
 * <p>
 * Description:消耗比例中间表实体类
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
 * @date 2012-1-4
 */
public class Xiaohblzjb extends PageableSupport{
	
	private static final long serialVersionUID = -6416990806688863683L;

	private String  usercenter	;		
	private String  shengcxbh	;		
	private BigDecimal xiaohbl ;
	private String lingjbh ;
	
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	public String getShengcxbh() {
		return shengcxbh;
	}
	public void setShengcxbh(String shengcxbh) {
		this.shengcxbh = shengcxbh;
	}
	public BigDecimal getXiaohbl() {
		return xiaohbl;
	}
	public void setXiaohbl(BigDecimal xiaohbl) {
		this.xiaohbl = xiaohbl;
	}
	public String getLingjbh() {
		return lingjbh;
	}
	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}
	
}
