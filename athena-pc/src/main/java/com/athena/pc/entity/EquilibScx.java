package com.athena.pc.entity;

import java.io.Serializable;

/**
 * @Author: 王冲
 * @Email:jonw@sina.com
 * @Date: 2012-6-13
 * @Time: 下午3:21:55
 * @version 1.0
 * @Description :产线
 */
public class EquilibScx implements Comparable<EquilibScx>, Serializable {

	/* description: 产线组编号 */
	private String CHANXZBH = null;
	/* description: 产线编号 */
	private String CHANXH = null;
	/* description: 工时 */
	private float GS = 0.00f;
	/* description:   生产线节拍*/
	private String SCXJP = "0";
	

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-13
	 * @Time: 下午3:31:35
	 * @version 1.0
	 * @return cHANXZBH
	 * @Description: 返回cHANXZBH的值
	 */
	public String getCHANXZBH() {
		return CHANXZBH;
	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-13
	 * @Time: 下午3:31:35
	 * @version 1.0
	 * @Description: 将参数cHANXZBH的值赋值给成员变量cHANXZBH
	 */
	public void setCHANXZBH(String cHANXZBH) {
		CHANXZBH = cHANXZBH;
	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-13
	 * @Time: 下午3:31:35
	 * @version 1.0
	 * @return cHANXH
	 * @Description: 返回cHANXH的值
	 */
	public String getCHANXH() {
		return CHANXH;
	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-13
	 * @Time: 下午3:31:35
	 * @version 1.0
	 * @Description: 将参数cHANXH的值赋值给成员变量cHANXH
	 */
	public void setCHANXH(String cHANXH) {
		CHANXH = cHANXH;
	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-13
	 * @Time: 下午3:33:41
	 * @version 1.0
	 * @return gS
	 * @Description: 返回gS的值
	 */
	public float getGS() {
		return GS;
	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-13
	 * @Time: 下午3:33:41
	 * @version 1.0
	 * @Description: 将参数gS的值赋值给成员变量gS
	 */
	public void setGS(Object gS) {

		if (gS instanceof Float) {
			GS = (Float) gS;
		}
		else {
			GS = Float.parseFloat(gS.toString());
		}
	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-14
	 * @Time: 上午11:52:46
	 * @version 1.0
	 * @return  sCXJP
	 * @Description: 返回sCXJP的值
	 */
	public String getSCXJP() {
		return SCXJP;
	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-14
	 * @Time: 上午11:52:46
	 * @version 1.0
	 * @Description: 将参数sCXJP的值赋值给成员变量sCXJP
	 */
	public void setSCXJP(String sCXJP) {
		SCXJP = sCXJP;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(EquilibScx o) {

		if (this.getGS() > o.getGS()) {
			return 1;
		}
		else if (this.getGS() < o.getGS()) {
			return -1;
		}
		else {
			return 0;
		}
	}

}
