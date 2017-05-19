package com.athena.component.wtc;

/**
 * 请求信息
 * @author Administrator
 */
public class WtcRequest extends WtcBean{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4600075170625423256L;

	/**
	 * 
	 */
	private String transCode;//交易编号
	
	private String transCaption;//交易描述
	
	public WtcRequest(String transCode){
		this.transCode = transCode;
	}

	public String getTransCode() {
		return transCode;
	}

	public void setTransCode(String transCode) {
		this.transCode = transCode;
	}

	public String getTransCaption() {
		return transCaption;
	}

	public void setTransCaption(String transCaption) {
		this.transCaption = transCaption;
	}
}
