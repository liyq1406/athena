package com.athena.fj.entity;

import java.util.HashSet;
import java.util.Set;

/**
 * @author 王冲
 * @email jonw@sina.com
 * @date 2012-3-2
 * @time 上午11:29:29
 * @description 客户,供应商关系
 */
public class KehGysInfo {
	/* description:   供应商*/
	private String gys = "";
	/* description:   客户*/
	private Set<String> keh= new HashSet<String>();
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-5
	 * @time 上午10:44:17
	 * @return  gys
	 */
	public String getGys() {
		return gys;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-5
	 * @time 上午10:44:17
	 * @description 赋值gys
	 */
	public void setGys(String gys) {
		this.gys = gys;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-5
	 * @time 上午10:44:17
	 * @return  keh
	 */
	public Set<String> getKeh() {
		return keh;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-5
	 * @time 上午10:44:17
	 * @description 赋值keh
	 */
	public void setKeh(Set<String> keh) {
		this.keh = keh;
	}
	
	
	

}
