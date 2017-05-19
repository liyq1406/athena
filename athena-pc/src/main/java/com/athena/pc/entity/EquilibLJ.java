package com.athena.pc.entity;

import java.io.Serializable;

/**
 * @Author: 王冲
 * @Email:jonw@sina.com
 * @Date: 2012-6-13
 * @Time: 下午3:36:41
 * @version 1.0
 * @Description :零件
 */
public class EquilibLJ implements Comparable<EquilibLJ>, Serializable {
	/* description: 产线组编号 */
	private String CHANXZBH = null;
	/* description: 产线编号 */
	private String CHANXH = null;
	/* description: 零件编号 */
	private String LINGJBH = null;
	/* description: 零件数量 */
	private float LINGJSL = 0.00f;
	/* description:   经济批量*/
	private String JJPL ="0";
	/* description:   是否启用经济批量*/
	private int SFQYJJPL = 0;
	/* description:   零件数量*/
	private int LJSL = 0;
	
	
	

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-14
	 * @Time: 下午4:47:02
	 * @version 1.0
	 * @return  lJSL
	 * @Description: 返回lJSL的值
	 */
	public int getLJSL() {
		return LJSL;
	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-14
	 * @Time: 下午4:47:02
	 * @version 1.0
	 * @Description: 将参数lJSL的值赋值给成员变量lJSL
	 */
	public void setLJSL(int lJSL) {
		LJSL = lJSL;
	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-13
	 * @Time: 下午3:51:51
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
	 * @Time: 下午3:51:51
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
	 * @Time: 下午3:51:51
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
	 * @Time: 下午3:51:51
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
	 * @Time: 下午3:51:51
	 * @version 1.0
	 * @return lINGJBH
	 * @Description: 返回lINGJBH的值
	 */
	public String getLINGJBH() {
		return LINGJBH;
	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-13
	 * @Time: 下午3:51:51
	 * @version 1.0
	 * @Description: 将参数lINGJBH的值赋值给成员变量lINGJBH
	 */
	public void setLINGJBH(String lINGJBH) {
		LINGJBH = lINGJBH;
	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-15
	 * @Time: 下午2:42:36
	 * @version 1.0
	 * @return  sFQYJJPL
	 * @Description: 返回sFQYJJPL的值
	 */
	public int getSFQYJJPL() {
		return SFQYJJPL;
	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-15
	 * @Time: 下午2:42:36
	 * @version 1.0
	 * @Description: 将参数sFQYJJPL的值赋值给成员变量sFQYJJPL
	 */
	public void setSFQYJJPL(int sFQYJJPL) {
		SFQYJJPL = sFQYJJPL;
	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-13
	 * @Time: 下午3:51:51
	 * @version 1.0
	 * @return lINGJSL
	 * @Description: 返回lINGJSL的值
	 */
	public float getLINGJSL() {
		return LINGJSL;
	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-13
	 * @Time: 下午3:51:51
	 * @version 1.0
	 * @Description: 将参数lINGJSL的值赋值给成员变量lINGJSL
	 */
	public void setLINGJSL(Object lINGJSL) {
		if (lINGJSL instanceof Float) {
			LINGJSL = (Float) lINGJSL;
		}
		else {
			LINGJSL = Float.parseFloat(lINGJSL.toString());
		}
	}

	
	
	
	
	
	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-14
	 * @Time: 下午3:15:07
	 * @version 1.0
	 * @return  jJPL
	 * @Description: 返回jJPL的值
	 */
	public String getJJPL() {
		return JJPL;
	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-14
	 * @Time: 下午3:15:07
	 * @version 1.0
	 * @Description: 将参数jJPL的值赋值给成员变量jJPL
	 */
	public void setJJPL(String jJPL) {
		JJPL = jJPL;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(EquilibLJ o) {

		if (this.getLINGJSL() > o.getLINGJSL()) {
			return -1;
		}
		else if (this.getLINGJSL() < o.getLINGJSL()) {
			return 1;
		}
		else {
			return 0;
		}
	}

}
