package com.athena.fj.entity;

/**
 * @author 王冲
 * @email jonw@sina.com
 * @date 2012-2-15
 * @time 上午11:32:59
 * @description 0213车辆明细
 */
public class ClMx {
	private String  ID    ="";   //车辆主键
	private String YAOCJHXH ="";//要车计划序号
	private String CHEX     ="";//车辆型号
	private int  ZONGCC   =0;//总车次
	private String CHEXMC = "";//车辆名称
	private String USERCENTER = "";//用户中心
	
	
	
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-20
	 * @time 上午10:06:54
	 * @return  cHEXMC
	 */
	public String getCHEXMC() {
		return CHEXMC;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-20
	 * @time 上午10:06:54
	 * @description 赋值cHEXMC
	 */
	public void setCHEXMC(String cHEXMC) {
		CHEXMC = cHEXMC;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-18
	 * @time 下午02:43:11
	 * @return  uSERCENTER
	 */
	public String getUSERCENTER() {
		return USERCENTER;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-18
	 * @time 下午02:43:11
	 * @description 赋值uSERCENTER
	 */
	public void setUSERCENTER(String uSERCENTER) {
		USERCENTER = uSERCENTER;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 上午11:41:05
	 * @return  iD
	 */
	public String getID() {
		return ID;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 上午11:41:05
	 * @description 赋值iD
	 */
	public void setID(String iD) {
		ID = iD;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 上午11:41:05
	 * @return  yAOCJHXH
	 */
	public String getYAOCJHXH() {
		return YAOCJHXH;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 上午11:41:05
	 * @description 赋值yAOCJHXH
	 */
	public void setYAOCJHXH(String yAOCJHXH) {
		YAOCJHXH = yAOCJHXH;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 上午11:41:05
	 * @return  cHEX
	 */
	public String getCHEX() {
		return CHEX;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 上午11:41:05
	 * @description 赋值cHEX
	 */
	public void setCHEX(String cHEX) {
		CHEX = cHEX;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 上午11:41:05
	 * @return  zONGCC
	 */
	public int getZONGCC() {
		return ZONGCC;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 上午11:41:05
	 * @description 赋值zONGCC
	 */
	public void setZONGCC(int zONGCC) {
		ZONGCC = zONGCC;
	}
	
}
