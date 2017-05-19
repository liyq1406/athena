package com.athena.xqjs.entity.anxorder;

import com.toft.core3.support.PageableSupport;

public class UsercenterSet extends PageableSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1904873014239913999L;
	private String usercenter;
	private String C1;
	private String M1;
	private String CD;
	private String MD;
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	public String getC1() {
		return C1;
	}
	public void setC1(String c1) {
		C1 = c1;
	}
	public String getM1() {
		return M1;
	}
	public void setM1(String m1) {
		M1 = m1;
	}
	public String getCD() {
		return CD;
	}
	public void setCD(String cD) {
		CD = cD;
	}
	public String getMD() {
		return MD;
	}
	public void setMD(String mD) {
		MD = mD;
	}
	
}
