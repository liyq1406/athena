package com.athena.pc.entity;

import java.io.Serializable;

/**
 * @Author: 王冲
 * @Email:jonw@sina.com
 * @Date: 2012-6-18
 * @Time: 下午3:51:53
 * @version 1.0
 * @Description : 排产消息
 */
public class EquilibMessage implements Serializable {

	/* description: 用户中心+产线号+年+月 */
	private String JIHH = null;
	/* description: 用户中心 */
	private String USERCENTER = null;
	/* description: 产线号 */
	private String CHANXH = null;
	/* description: 时间 */
	private String SHIJ = null;
	/* description: 消息 */
	private String XIAOX = null;
	/* description: 类型 */
	private String LEIX = "2";
	/* description: 标识 1.产线,2.日期产线,3.周产线 */
	private String BIAOS = "";
	/* description: 产线组编号 */
	private String CHANXZBH = "";
	/* description: 编辑人 */
	private String EDITOR=null;
	/* description: 编辑时间 */
	private String EDIT_TIME =null;
	/* description: 创建者 */
	private String CREATOR=null;
	/* description: 创建时间 */
	private String CREATE_TIME=null;
	

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-19
	 * @Time: 上午10:01:58
	 * @version 1.0
	 * @return jIHH
	 * @Description: 返回jIHH的值
	 */
	public String getJIHH() {
		return JIHH;
	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-19
	 * @Time: 上午10:01:58
	 * @version 1.0
	 * @Description: 将参数jIHH的值赋值给成员变量jIHH
	 */
	public void setJIHH(String jIHH) {
		JIHH = jIHH;
	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-19
	 * @Time: 上午10:01:58
	 * @version 1.0
	 * @return uSERCENTER
	 * @Description: 返回uSERCENTER的值
	 */
	public String getUSERCENTER() {
		return USERCENTER;
	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-19
	 * @Time: 上午10:01:58
	 * @version 1.0
	 * @Description: 将参数uSERCENTER的值赋值给成员变量uSERCENTER
	 */
	public void setUSERCENTER(String uSERCENTER) {
		USERCENTER = uSERCENTER;
	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-19
	 * @Time: 上午10:01:58
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
	 * @Date: 2012-6-19
	 * @Time: 上午10:01:58
	 * @version 1.0
	 * @Description: 将参数cHANXH的值赋值给成员变量cHANXH
	 */
	public void setCHANXH(String cHANXH) {
		CHANXH = cHANXH;
	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-19
	 * @Time: 上午10:01:58
	 * @version 1.0
	 * @return sHIJ
	 * @Description: 返回sHIJ的值
	 */
	public String getSHIJ() {
		return SHIJ;
	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-19
	 * @Time: 上午10:01:58
	 * @version 1.0
	 * @Description: 将参数sHIJ的值赋值给成员变量sHIJ
	 */
	public void setSHIJ(String sHIJ) {
		SHIJ = sHIJ;
	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-19
	 * @Time: 上午10:01:58
	 * @version 1.0
	 * @return xIAOX
	 * @Description: 返回xIAOX的值
	 */
	public String getXIAOX() {
		return XIAOX;
	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-19
	 * @Time: 上午10:01:58
	 * @version 1.0
	 * @Description: 将参数xIAOX的值赋值给成员变量xIAOX
	 */
	public void setXIAOX(String xIAOX) {
		XIAOX = xIAOX;
	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-19
	 * @Time: 上午10:01:58
	 * @version 1.0
	 * @return lEIX
	 * @Description: 返回lEIX的值
	 */
	public String getLEIX() {
		return LEIX;
	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-19
	 * @Time: 上午10:01:58
	 * @version 1.0
	 * @Description: 将参数lEIX的值赋值给成员变量lEIX
	 */
	public void setLEIX(String lEIX) {
		LEIX = lEIX;
	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-19
	 * @Time: 上午10:01:58
	 * @version 1.0
	 * @return bIAOS
	 * @Description: 返回bIAOS的值
	 */
	public String getBIAOS() {
		return BIAOS;
	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-19
	 * @Time: 上午10:01:58
	 * @version 1.0
	 * @Description: 将参数bIAOS的值赋值给成员变量bIAOS
	 */
	public void setBIAOS(String bIAOS) {
		BIAOS = bIAOS;
	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-19
	 * @Time: 上午10:01:58
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
	 * @Date: 2012-6-19
	 * @Time: 上午10:01:58
	 * @version 1.0
	 * @Description: 将参数cHANXZBH的值赋值给成员变量cHANXZBH
	 */
	public void setCHANXZBH(String cHANXZBH) {
		CHANXZBH = cHANXZBH;
	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-19
	 * @Time: 上午11:29:14
	 * @param jIHH 计划号
	 * @param uSERCENTER 用户中心
	 * @param cHANXH 产线号
	 * @param sHIJ 时间
	 * @param xIAOX 消息
	 * @param lEIX  类型
	 * @param bIAOS 标识
	 * @param cHANXZBH 产线组编号
	 * @Description:  构造函数
	 */
	public EquilibMessage(String jIHH, String uSERCENTER, String cHANXH,
					String sHIJ, String xIAOX, String lEIX, String bIAOS,
					String cHANXZBH) {
		super();
		JIHH = jIHH;
		USERCENTER = uSERCENTER;
		CHANXH = cHANXH;
		SHIJ = sHIJ;
		XIAOX = xIAOX;
		LEIX = lEIX;
		BIAOS = bIAOS;
		CHANXZBH = cHANXZBH;
	}

	/**
	 * @Author: 王冲
	 * @Email:jonw@sina.com
	 * @Date: 2012-6-19
	 * @Time: 上午11:30:12
	 * @Description:  构造函数
	 */
	public EquilibMessage() {
		super();
		
	}

	public String getEDITOR() {
		return EDITOR;
	}

	public void setEDITOR(String eDITOR) {
		EDITOR = eDITOR;
	}

	public String getEDIT_TIME() {
		return EDIT_TIME;
	}

	public void setEDIT_TIME(String eDIT_TIME) {
		EDIT_TIME = eDIT_TIME;
	}

	public String getCREATOR() {
		return CREATOR;
	}

	public void setCREATOR(String cREATOR) {
		CREATOR = cREATOR;
	}

	public String getCREATE_TIME() {
		return CREATE_TIME;
	}

	public void setCREATE_TIME(String cREATE_TIME) {
		CREATE_TIME = cREATE_TIME;
	}
	

}
