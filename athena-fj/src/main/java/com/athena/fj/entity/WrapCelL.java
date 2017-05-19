package com.athena.fj.entity;

import java.io.Serializable;

/**
 * @author 王冲
 * @email jonw@sina.com
 * @date 2012-2-9
 * @time 下午02:03:48
 * @description 申报车辆资源
 */
public class WrapCelL  implements Serializable{

	/* description: 车辆型号 */
	private String clLx = "";
	/* description: 承运商 */
	private String cys = "";
	/* description: 车辆名称 */
	private String clName = "";
	/* description: 申报数量 */
	private int sbSl = 0;
	/* description: 使用数量 */
	private int sysl = 0;
	/* description: 最大数量 */
	private int zdsl = 0;

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-21
	 * @time 上午09:49:51
	 * @return sbSl
	 */
	public int getSbSl() {
		return sbSl;
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-21
	 * @time 上午09:49:51
	 * @description 赋值sbSl
	 */
	public void setSbSl(int sbSl) {
		this.sbSl = sbSl;
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-21
	 * @time 上午09:49:51
	 * @return sysl
	 */
	public int getSysl() {
		return sysl;
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-21
	 * @time 上午09:49:51
	 * @description 赋值sysl
	 */
	public void setSysl(int sysl) {
		this.sysl = sysl;
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-21
	 * @time 上午09:49:51
	 * @return zdsl
	 */
	public int getZdsl() {
		return zdsl;
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-21
	 * @time 上午09:49:51
	 * @description 赋值zdsl
	 */
	public void setZdsl(int zdsl) {
		this.zdsl = zdsl;
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 上午09:55:09
	 * @return clName
	 */
	public String getClName() {
		return clName;
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 上午09:55:09
	 * @description 赋值clName
	 */
	public void setClName(String clName) {
		this.clName = clName;
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-10
	 * @time 上午11:49:13
	 * @return clLx
	 */
	public String getClLx() {
		return clLx;
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-10
	 * @time 上午11:49:13
	 * @description 赋值clLx
	 */
	public void setClLx(String clLx) {
		this.clLx = clLx;
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-10
	 * @time 上午11:53:32
	 * @return cys
	 */
	public String getCys() {
		return cys;
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-10
	 * @time 上午11:53:32
	 * @description 赋值cys
	 */
	public void setCys(String cys) {
		this.cys = cys;
	}

}
