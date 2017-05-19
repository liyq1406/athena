package com.athena.ckx.entity.xuqjs;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

public class CkxShiwtx extends PageableSupport implements Domain{
	
	private static final long serialVersionUID = 1861676571164967553L;
	
	private String usercenter;//用户中心
	private String tixlx;     //  提醒类型
	private String guanjz1;   //关键字1
	private String guanjz2;   //关键字2
	private String yonghz;    //用户组
	private String tixnr;     //提醒内容
	private String zhuangt;   //状态
	private String baojsj;    //报警时间
	private String jiejsj;    //解决时间
	
	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
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

	public String getYonghz() {
		return yonghz;
	}

	public void setYonghz(String yonghz) {
		this.yonghz = yonghz;
	}

	public String getTixnr() {
		return tixnr;
	}

	public void setTixnr(String tixnr) {
		this.tixnr = tixnr;
	}

	public String getZhuangt() {
		return zhuangt;
	}

	public void setZhuangt(String zhuangt) {
		this.zhuangt = zhuangt;
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

	public void setId(String id) {
		
	}

	public String getId() {
		return null;
	}
	
	
	
}
