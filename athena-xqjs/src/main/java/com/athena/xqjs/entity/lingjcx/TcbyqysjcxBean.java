/**
 * 
 */
package com.athena.xqjs.entity.lingjcx;

/**
 * @author dsimedd001
 *
 */
public class TcbyqysjcxBean {
	private String tcNo;
	private String dinghcj;
	private String qiysj;
	private String zuiswld;
	private String usercenter; //hzg add 2015.9.30
	
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	/**
	 * 取得 tcNo
	 * @return the tcNo
	 */
	public String getTcNo() {
		return tcNo;
	}
	/**
	 * @param tcNo the tcNo to set
	 */
	public void setTcNo(String tcNo) {
		this.tcNo = tcNo;
	}
	/**
	 * 取得 dinghcj
	 * @return the dinghcj
	 */
	public String getDinghcj() {
		return dinghcj;
	}
	/**
	 * @param dinghcj the dinghcj to set
	 */
	public void setDinghcj(String dinghcj) {
		this.dinghcj = dinghcj;
	}
	/**
	 * 取得 qiysj
	 * @return the qiysj
	 */
	public String getQiysj() {
		return qiysj;
	}
	/**
	 * @param qiysj the qiysj to set
	 */
	public void setQiysj(String qiysj) {
		this.qiysj = qiysj;
	}
	/**
	 * 取得 zuiswld
	 * @return the zuiswld
	 */
	public String getZuiswld() {
		return zuiswld;
	}
	/**
	 * @param zuiswld the zuiswld to set
	 */
	public void setZuiswld(String zuiswld) {
		this.zuiswld = zuiswld;
	}
	
}
