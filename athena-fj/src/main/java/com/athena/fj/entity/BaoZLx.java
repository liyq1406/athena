package com.athena.fj.entity;

import java.util.HashSet;
import java.util.Set;

/**
 * @author 王冲
 * @email jonw@sina.com
 * @date 2012-2-10
 * @time 上午10:46:02
 * @description 包装类型
 */
public class BaoZLx {
	
	/* description:   包装类型*/
	private String baoZlx = "";
	/* description:   包装数量*/
	private int sl = 0;
	/* description:   包装所属组*/
	private Set<String> baoZZ = new HashSet<String>();
	/*要贷令编号*/
	private Set<String>  yhlbh = new HashSet<String>();
	
	
	
	
	
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-14
	 * @time 下午02:46:47
	 * @return  yhlbh
	 */
	public Set<String> getYhlbh() {
		return yhlbh;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-14
	 * @time 下午02:46:47
	 * @description 赋值yhlbh
	 */
	public void setYhlbh(Set<String> yhlbh) {
		this.yhlbh = yhlbh;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-10
	 * @time 上午10:48:19
	 * @return  baoZlx
	 */
	public String getBaoZlx() {
		return baoZlx;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-10
	 * @time 上午10:48:19
	 * @description 赋值baoZlx
	 */
	public void setBaoZlx(String baoZlx) {
		this.baoZlx = baoZlx;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-10
	 * @time 上午10:48:19
	 * @return  sl
	 */
	public int getSl() {
		return sl;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-10
	 * @time 上午10:48:19
	 * @description 赋值sl
	 */
	public void setSl(int sl) {
		this.sl = sl;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-10
	 * @time 上午10:48:19
	 * @return  baoZZ
	 */
	public Set<String> getBaoZZ() {
		return baoZZ;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-10
	 * @time 上午10:48:19
	 * @description 赋值baoZZ
	 */
	public void setBaoZZ(Set<String> baoZZ) {
		this.baoZZ = baoZZ;
	}
	

}
