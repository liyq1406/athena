package com.athena.fj.entity;

import java.io.Serializable;


/**
 * @author 王冲
 * @email jonw@sina.com
 * @date 2012-3-21
 * @time 上午10:02:52
 * @description 路线组
 */
@SuppressWarnings("serial")
public class WarpLXZ  implements Serializable{

	/* description: 客户编号 */
	private String keh = "";
	/* description: 供应商编号 */
	private String gysdm = "";
	/* description: 仓库编号 */
	private String ckbh = "";
	/* description: 运输商编号 */
	private String cys = "";
	/* description: 要车提前期 */
	private int yaoctqq = 0;
	/* description: 路线组编号 */
	private String lzxbh = "";
	/* description: 路线线最大提前期 */
	private int lxzdztqq = 0;
	/* description:   车辆来源*/
	private String clly = "";
	
	
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-23
	 * @time 下午05:07:21
	 * @return  clly
	 */
	public String getClly() {
		return clly;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-23
	 * @time 下午05:07:21
	 * @description 赋值clly
	 */
	public void setClly(String clly) {
		this.clly = clly;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-21
	 * @time 上午10:10:50
	 * @return  keh
	 */
	public String getKeh() {
		return keh;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-21
	 * @time 上午10:10:50
	 * @description 赋值keh
	 */
	public void setKeh(String keh) {
		this.keh = keh;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-21
	 * @time 上午10:10:50
	 * @return  gysdm
	 */
	public String getGysdm() {
		return gysdm;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-21
	 * @time 上午10:10:50
	 * @description 赋值gysdm
	 */
	public void setGysdm(String gysdm) {
		this.gysdm = gysdm;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-21
	 * @time 上午10:10:50
	 * @return  ckbh
	 */
	public String getCkbh() {
		return ckbh;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-21
	 * @time 上午10:10:50
	 * @description 赋值ckbh
	 */
	public void setCkbh(String ckbh) {
		this.ckbh = ckbh;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-21
	 * @time 上午10:10:50
	 * @return  cys
	 */
	public String getCys() {
		return cys;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-21
	 * @time 上午10:10:50
	 * @description 赋值cys
	 */
	public void setCys(String cys) {
		this.cys = cys;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-21
	 * @time 上午10:10:50
	 * @return  yaoctqq
	 */
	public int getYaoctqq() {
		return yaoctqq;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-21
	 * @time 上午10:10:50
	 * @description 赋值yaoctqq
	 */
	public void setYaoctqq(int yaoctqq) {
		this.yaoctqq = yaoctqq;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-21
	 * @time 上午10:10:50
	 * @return  lzxbh
	 */
	public String getLzxbh() {
		return lzxbh;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-21
	 * @time 上午10:10:50
	 * @description 赋值lzxbh
	 */
	public void setLzxbh(String lzxbh) {
		this.lzxbh = lzxbh;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-21
	 * @time 上午10:10:50
	 * @return  lxzdztqq
	 */
	public int getLxzdztqq() {
		return lxzdztqq;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-21
	 * @time 上午10:10:50
	 * @description 赋值lxzdztqq
	 */
	public void setLxzdztqq(int lxzdztqq) {
		this.lxzdztqq = lxzdztqq;
	}

}
