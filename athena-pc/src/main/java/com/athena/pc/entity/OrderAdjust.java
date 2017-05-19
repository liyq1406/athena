package com.athena.pc.entity;

/**
 * @Author: 王冲
 * @Email:jonw@sina.com
 * @Date: 2012-7-30
 * @Time: 上午11:52:43
 * @version 1.0
 * @Description :PP & PJ 订单
 */
public class OrderAdjust extends BasicAdjust {
	
	/* description:   ID号*/
	private String ID = null;
	
	/* description:   订单号*/
	private String DINGDH = null;
	
	/* description:   零件编号*/
	private String LINGJBH = null;
	
	/* description:   零件数量*/
	private String SHUL = null;

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

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

	public String getSHUL() {
		return SHUL;
	}

	public void setSHUL(String sHUL) {
		SHUL = sHUL;
	}
	
	
	

}
