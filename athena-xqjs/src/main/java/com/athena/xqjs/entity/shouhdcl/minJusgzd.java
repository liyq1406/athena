package com.athena.xqjs.entity.shouhdcl;

import com.toft.core3.support.PageableSupport;

/**
 * 据收跟踪单信息
 * @author WL
 * @date 2012-04-01
 *
 */
public class minJusgzd extends PageableSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7914344649634321643L;

	/**
	 * 用户中心
	 */
	private String usercenter;
	/**
	 * 拒收跟踪单号
	 */
	private String jusgzdh;
	/**
	 * 零件编号
	 */
	private String lingjbh;
	
	/**
	 * 卸货点
	 */
	private String xiehd;
	

	private String zhuangt;
	
	public String getXiehd() {
		return xiehd;
	}
	public void setXiehd(String xiehd) {
		this.xiehd = xiehd;
	}
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	public String getJusgzdh() {
		return jusgzdh;
	}
	public void setJusgzdh(String jusgzdh) {
		this.jusgzdh = jusgzdh;
	}
	public String getLingjbh() {
		return lingjbh;
	}
	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}

	/**
	 * 取得 zhuangt
	 * @return the zhuangt
	 */
	public String getZhuangt() {
		return zhuangt;
	}
	/**
	 * @param zhuangt the zhuangt to set
	 */
	public void setZhuangt(String zhuangt) {
		this.zhuangt = zhuangt;
	}

	
}
