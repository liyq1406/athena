package com.athena.fj.entity;

import com.toft.core3.support.PageableSupport;

/**
 * @author 王冲
 * @email jonw@sina.com
 * @date 2012-2-15
 * @time 上午11:32:24
 * @description 0210要车计划总表
 */
public class YaoCJhZb extends PageableSupport {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = -342051453544659263L;
	private String YAOCJHXH = "";//要车计划序号
	private String CHEXMC = "";//车辆名称
	private int SHIFWC=0;//是否完成
	private int ZONGCC = 0;//总车次
	private String USERCENTER = "";//用户中心
	private String CREATOR = "";
	
	public String getCREATOR() {
		return CREATOR;
	}
	public void setCREATOR(String cREATOR) {
		CREATOR = cREATOR;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-18
	 * @time 下午02:42:46
	 * @return  uSERCENTER
	 */
	public String getUSERCENTER() {
		return USERCENTER;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-18
	 * @time 下午02:42:46
	 * @description 赋值uSERCENTER
	 */
	public void setUSERCENTER(String uSERCENTER) {
		USERCENTER = uSERCENTER;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-18
	 * @time 上午11:27:09
	 * @return  sHIFWC
	 */
	public int getSHIFWC() {
		return SHIFWC;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-18
	 * @time 上午11:27:09
	 * @description 赋值sHIFWC
	 */
	public void setSHIFWC(int sHIFWC) {
		SHIFWC = sHIFWC;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 上午11:39:15
	 * @return  yAOCJHXH
	 */
	public String getYAOCJHXH() {
		return YAOCJHXH;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 上午11:39:15
	 * @description 赋值yAOCJHXH
	 */
	public void setYAOCJHXH(String yAOCJHXH) {
		YAOCJHXH = yAOCJHXH;
	}


	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-18
	 * @time 上午11:33:29
	 * @return  cHEXMC
	 */
	public String getCHEXMC() {
		return CHEXMC;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-18
	 * @time 上午11:33:29
	 * @description 赋值cHEXMC
	 */
	public void setCHEXMC(String cHEXMC) {
		CHEXMC = cHEXMC;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 上午11:40:47
	 * @return zONGCC
	 */
	public int getZONGCC() {
		return ZONGCC;
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 上午11:40:47
	 * @description 赋值zONGCC
	 */
	public void setZONGCC(int zONGCC) {
		ZONGCC = zONGCC;
	}

}
