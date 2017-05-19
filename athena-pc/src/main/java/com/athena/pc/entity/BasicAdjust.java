package com.athena.pc.entity;

import com.athena.authority.util.AuthorityUtils;
import com.athena.util.date.DateUtil;
import com.toft.core3.support.PageableSupport;

/**
 * @Author: 王冲
 * @Email:jonw@sina.com
 * @Date: 2012-7-30
 * @Time: 上午11:49:06
 * @version 1.0
 * @Description :调整基础类
 */
public  class BasicAdjust  extends PageableSupport{

	/* description:   */
	private static final long serialVersionUID = 1L;

	/* description: 用户中心 */
	private String USERCENTER = null;

	/* description: 排产开始时间 */
	private String PCKAISSJ = null;

	/* description: 排产结束时间 */
	private String PCJIESSJ = null;
	
	/* description:   开始时间是否可编辑 0为不可编辑,1为可编辑*/
	private String KSSJENBLE ="0";
	
	
	/* description:   排产修改人*/
	private String PCEDITOR = AuthorityUtils.getSecurityUser().getUsername();
	/* description:   修改时间*/
	private String PCEDIT_TIME = DateUtil.curDateTime();
	/* description: 排产开始时间 */
	private String KAISSJB = null;

	/* description: 排产结束时间 */
	private String JIESSJB = null;	
	
	public String getUSERCENTER() {
		return USERCENTER;
	}

	public void setUSERCENTER(String uSERCENTER) {
		USERCENTER = uSERCENTER;
	}

	public String getPCKAISSJ() {
		return PCKAISSJ;
	}

	public void setPCKAISSJ(String pCKAISSJ) {
		PCKAISSJ = pCKAISSJ;
	}

	public String getPCJIESSJ() {
		return PCJIESSJ;
	}

	public void setPCJIESSJ(String pCJIESSJ) {
		PCJIESSJ = pCJIESSJ;
	}

	public String getKSSJENBLE() {
		return KSSJENBLE;
	}

	public void setKSSJENBLE(String kSSJENBLE) {
		KSSJENBLE = kSSJENBLE;
	}

	public String getPCEDITOR() {
		return PCEDITOR;
	}

	public void setPCEDITOR(String pCEDITOR) {
		PCEDITOR = pCEDITOR;
	}

	public String getPCEDIT_TIME() {
		return PCEDIT_TIME;
	}

	public void setPCEDIT_TIME(String pCEDIT_TIME) {
		PCEDIT_TIME = pCEDIT_TIME;
	}

	public String getKAISSJB() {
		return KAISSJB;
	}

	public void setKAISSJB(String kAISSJB) {
		KAISSJB = kAISSJB;
	}

	public String getJIESSJB() {
		return JIESSJB;
	}

	public void setJIESSJB(String jIESSJB) {
		JIESSJB = jIESSJB;
	}

}
