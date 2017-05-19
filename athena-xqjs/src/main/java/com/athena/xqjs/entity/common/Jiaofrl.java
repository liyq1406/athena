package com.athena.xqjs.entity.common;

import com.toft.core3.support.PageableSupport;
/**
 * <p>
 * Title:交付日历实体类
 * </p>
 * <p>
 * Description:交付日历实体类
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
 * @date 2011-12-20
 */
public class Jiaofrl extends PageableSupport{
	
	private static final long serialVersionUID = -6416990806688863683L;
	private String  usercenter	;		
	private String ri ;
	private String jiaofm ;
	private String nianzq ;
	
	public String getNianzq() {
		return nianzq;
	}
	public void setNianzq(String nianzq) {
		this.nianzq = nianzq;
	}
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	public String getRi() {
		return ri;
	}
	public void setRi(String ri) {
		this.ri = ri;
	}
	public String getJiaofm() {
		return jiaofm;
	}
	public void setJiaofm(String jiaofm) {
		this.jiaofm = jiaofm;
	}
	
}
