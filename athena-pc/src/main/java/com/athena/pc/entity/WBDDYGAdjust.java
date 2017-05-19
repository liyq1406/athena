package com.athena.pc.entity;

/**
 * @Author: 王冲
 * @Email:jonw@sina.com
 * @Date: 2012-7-30
 * @Time: 下午01:07:42
 * @version 1.0
 * @Description :外部订单鱼告
 */
public class WBDDYGAdjust extends BasicAdjust {
	
	/* description:   订单号*/
	private String DINGDH = null;
	
	/* description:   零件编号*/
	private String LINGJBH = null;
	/* description:   订单日期*/
	private String DDRQ = null;
	/* description:   线号*/
	private String XIANH = null;
	/* description:   BJ GEVP*/
	private String LYXT = null;
	/* description:   零件数量*/
	private String SHUL = null;
	
	public String getDINGDH() {
		return DINGDH;
	}
	public void setDINGDH(String dINGDH) {
		DINGDH = dINGDH;
	}
	public String getLINGJBH() {
		return LINGJBH;
	}
	public void setLINGJBH(String lINGJBH) {
		LINGJBH = lINGJBH;
	}
	public String getDDRQ() {
		return DDRQ;
	}
	public void setDDRQ(String dDRQ) {
		DDRQ = dDRQ;
	}
	public String getXIANH() {
		return XIANH;
	}
	public void setXIANH(String xIANH) {
		XIANH = xIANH;
	}
	public String getLYXT() {
		return LYXT;
	}
	public void setLYXT(String lYXT) {
		LYXT = lYXT;
	}
	
	public String getSHUL() {
		return SHUL;
	}
	public void setSHUL(String sHUL) {
		SHUL = sHUL;
	}

	
	
	

}
