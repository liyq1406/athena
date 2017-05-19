package com.athena.xqjs.entity.common;

import com.toft.core3.support.PageableSupport;
/**
 * 生产线bean
 * @Xiahui
 * 2012-2-1
 */
public class Shengcx extends PageableSupport{
	private static final long serialVersionUID = -6416990806688863683L;
	/**
	 * 用户中心
	 */
	private String  usercenter;
	/**
	 * 生产线编号
	 */
	private String  shengcxbh;
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

}
