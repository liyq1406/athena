package com.athena.fj.entity;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author 王冲
 * @email jonw@sina.com
 * @date 2012-3-21
 * @time 上午10:11:37
 * @description 路线组交付时刻
 */
public class WarpJiaoFSK {

	/* description: 路线组编号 */
	private String lxzbh = "";
	/* description: 交付时刻 */
	private Set<String> jiaoFSK = new LinkedHashSet();
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-21
	 * @time 上午10:14:38
	 * @return  lxzbh
	 */
	public String getLxzbh() {
		return lxzbh;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-21
	 * @time 上午10:14:38
	 * @description 赋值lxzbh
	 */
	public void setLxzbh(String lxzbh) {
		this.lxzbh = lxzbh;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-21
	 * @time 上午10:14:38
	 * @return  jiaoFSK
	 */
	public Set<String> getJiaoFSK() {
		return jiaoFSK;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-21
	 * @time 上午10:14:38
	 * @description 赋值jiaoFSK
	 */
	public void setJiaoFSK(Set<String> jiaoFSK) {
		this.jiaoFSK = jiaoFSK;
	}

}
