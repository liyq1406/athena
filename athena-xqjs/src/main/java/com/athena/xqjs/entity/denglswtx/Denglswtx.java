package com.athena.xqjs.entity.denglswtx;

import com.toft.core3.support.PageableSupport;

/**
 * 登录事务提醒
 * @author WL
 *
 */
public class Denglswtx extends PageableSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2919563209242656191L;

	/**
	 * 主键ID
	 */
	private String id = "";
	
	/**
	 * 用户中心
	 */
	private String usercenter = "";
	
	/**
	 * 用户组
	 */
	private String yonghz = "";
	
	/**
	 * 状态
	 */
	private String zhuangt = "";
	
	/**
	 * 提醒类型
	 */
	private String tixlx = "";
	
	/**
	 * 关键字1
	 */
	private String guanjz1 = "";
	
	/**
	 * 关键字2
	 */
	private String guanjz2 = "";
	
	/**
	 * 提醒内容
	 */
	private String tixnr = "";
	
	/**
	 * 报警时间
	 */
	private String baojsj = "";
	
	/**
	 * 结束时间
	 */
	private String jiejsj = "";
	
	/**
	 * 处理时间
	 */
	private String chulsj = "";

	public String getChulsj() {
		return chulsj;
	}

	public void setChulsj(String chulsj) {
		this.chulsj = chulsj;
	}

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getYonghz() {
		return yonghz;
	}

	public void setYonghz(String yonghz) {
		this.yonghz = yonghz;
	}

	public String getZhuangt() {
		return zhuangt;
	}

	public void setZhuangt(String zhuangt) {
		this.zhuangt = zhuangt;
	}

	public String getTixlx() {
		return tixlx;
	}

	public void setTixlx(String tixlx) {
		this.tixlx = tixlx;
	}

	public String getGuanjz1() {
		return guanjz1;
	}

	public void setGuanjz1(String guanjz1) {
		this.guanjz1 = guanjz1;
	}

	public String getGuanjz2() {
		return guanjz2;
	}

	public void setGuanjz2(String guanjz2) {
		this.guanjz2 = guanjz2;
	}

	public String getTixnr() {
		return tixnr;
	}

	public void setTixnr(String tixnr) {
		this.tixnr = tixnr;
	}

	public String getBaojsj() {
		return baojsj;
	}

	public void setBaojsj(String baojsj) {
		this.baojsj = baojsj;
	}

	public String getJiejsj() {
		return jiejsj;
	}

	public void setJiejsj(String jiejsj) {
		this.jiejsj = jiejsj;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
}
