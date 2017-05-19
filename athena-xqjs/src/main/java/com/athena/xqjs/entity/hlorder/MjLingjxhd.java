/**
 * 获取数据,模式为新PJ的零件消耗点
 */
package com.athena.xqjs.entity.hlorder;

/**
 * @author db2admin
 *
 */
public class MjLingjxhd {
	/**
	 * 零件编号
	 */
	private String lingjbh;
	
	/**
	 * 用户中心
	 */
	private String usercenter;
	
	/**
	 * 消耗点
	 */
	private String xiaohd;
	
	/**
	 * 循环
	 */
	private String fenpbh ;

	public String getLingjbh() {
		return lingjbh;
	}

	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getXiaohd() {
		return xiaohd;
	}

	public void setXiaohd(String xiaohd) {
		this.xiaohd = xiaohd;
	}

	public String getFenpbh() {
		return fenpbh;
	}

	public void setFenpbh(String fenpbh) {
		this.fenpbh = fenpbh;
	}
	
	
}
