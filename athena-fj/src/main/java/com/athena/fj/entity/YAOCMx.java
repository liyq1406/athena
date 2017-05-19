package com.athena.fj.entity;

import com.toft.core3.support.PageableSupport;

/**
 * @author 王冲
 * @email jonw@sina.com
 * @date 2012-2-15
 * @time 上午11:31:10
 * @description 0213车辆明细
 */
public class YAOCMx  extends PageableSupport {
	
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = -7863135211327925506L;
	private String ID = "";//主键
	private String YAOCJHXH = "";//要车计划序号
	private String YAOCJHH = "";//要车计划号
	private String KEHBM = "";//客户编码
	private String JIHCX = "";//车辆型号
	private String DAOCSJ = "";//要车时间
	private String FAYSJ = "";//发运时间
	private String YAOCZT = "1";// 1.等待执行,2.配载状态,3.装车状态,4.物流故障
	private String CELBH = "";//策略编号
	private int SHIFMZ = 0;//0未满载  1.已装满
	private String USERCENTER ="";//用户中心

	private String GCBH = "";
	private String CREATOR = "";
	/* description:   运输路线*/
	private String YUNSLX ="";
	/* description:   仓库编号*/
	private String CANGKBH="";
	
	
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-27
	 * @time 上午09:46:25
	 * @return  yUNSLX
	 */
	public String getYUNSLX() {
		return YUNSLX;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-27
	 * @time 上午09:46:25
	 * @description 赋值yUNSLX
	 */
	public void setYUNSLX(String yUNSLX) {
		YUNSLX = yUNSLX;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-27
	 * @time 上午09:46:25
	 * @return  cANGKBH
	 */
	public String getCANGKBH() {
		return CANGKBH;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-27
	 * @time 上午09:46:25
	 * @description 赋值cANGKBH
	 */
	public void setCANGKBH(String cANGKBH) {
		CANGKBH = cANGKBH;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-5
	 * @time 下午04:42:09
	 * @return  gCBH
	 */
	public String getGCBH() {
		return GCBH;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-5
	 * @time 下午04:42:09
	 * @description 赋值gCBH
	 */
	public void setGCBH(String gCBH) {
		GCBH = gCBH;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-18
	 * @time 下午02:42:18
	 * @return  uSERCENTER
	 */
	public String getUSERCENTER() {
		return USERCENTER;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-18
	 * @time 下午02:42:18
	 * @description 赋值uSERCENTER
	 */
	public void setUSERCENTER(String uSERCENTER) {
		USERCENTER = uSERCENTER;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-16
	 * @time 下午03:53:45
	 * @return  cELBH
	 */
	public String getCELBH() {
		return CELBH;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-16
	 * @time 下午03:53:45
	 * @description 赋值cELBH
	 */
	public void setCELBH(String cELBH) {
		CELBH = cELBH;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-16
	 * @time 下午03:53:45
	 * @return  sHIFMZ
	 */
	public int getSHIFMZ() {
		return SHIFMZ;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-16
	 * @time 下午03:53:45
	 * @description 赋值sHIFMZ
	 */
	public void setSHIFMZ(int sHIFMZ) {
		SHIFMZ = sHIFMZ;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 上午11:37:15
	 * @return  iD
	 */
	public String getID() {
		return ID;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 上午11:37:15
	 * @description 赋值iD
	 */
	public void setID(String iD) {
		ID = iD;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 上午11:37:15
	 * @return  yAOCJHXH
	 */
	public String getYAOCJHXH() {
		return YAOCJHXH;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 上午11:37:15
	 * @description 赋值yAOCJHXH
	 */
	public void setYAOCJHXH(String yAOCJHXH) {
		YAOCJHXH = yAOCJHXH;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 上午11:37:15
	 * @return  yAOCJHH
	 */
	public String getYAOCJHH() {
		return YAOCJHH;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 上午11:37:15
	 * @description 赋值yAOCJHH
	 */
	public void setYAOCJHH(String yAOCJHH) {
		YAOCJHH = yAOCJHH;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 上午11:37:15
	 * @return  kEHBM
	 */
	public String getKEHBM() {
		return KEHBM;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 上午11:37:15
	 * @description 赋值kEHBM
	 */
	public void setKEHBM(String kEHBM) {
		KEHBM = kEHBM;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 上午11:37:15
	 * @return  jIHCX
	 */
	public String getJIHCX() {
		return JIHCX;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 上午11:37:15
	 * @description 赋值jIHCX
	 */
	public void setJIHCX(String jIHCX) {
		JIHCX = jIHCX;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 上午11:37:15
	 * @return  dAOCSJ
	 */
	public String getDAOCSJ() {
		return DAOCSJ;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 上午11:37:15
	 * @description 赋值dAOCSJ
	 */
	public void setDAOCSJ(String dAOCSJ) {
		DAOCSJ = dAOCSJ;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 上午11:37:15
	 * @return  fAYSJ
	 */
	public String getFAYSJ() {
		return FAYSJ;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 上午11:37:15
	 * @description 赋值fAYSJ
	 */
	public void setFAYSJ(String fAYSJ) {
		FAYSJ = fAYSJ;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 上午11:37:15
	 * @return  yAOCZT
	 */
	public String getYAOCZT() {
		return YAOCZT;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 上午11:37:15
	 * @description 赋值yAOCZT
	 */
	public void setYAOCZT(String yAOCZT) {
		YAOCZT = yAOCZT;
	}
	public String getCREATOR() {
		return CREATOR;
	}
	public void setCREATOR(String cREATOR) {
		CREATOR = cREATOR;
	}
	
}
