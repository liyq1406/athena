/**
 * 
 */
package com.athena.xqjs.entity.lingjcx;

import java.math.BigDecimal;

/**
 * @author dsimedd001
 *
 */
public class LingjDingdhcxBean {
	private String usercenter; //hzg add 2015.9.30
	private String lingjbh;
	private String dingdh;
	private String wuld;
	private BigDecimal lingjzl;
	
	
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	/**
	 * 取得 lingjbh
	 * @return the lingjbh
	 */
	public String getLingjbh() {
		return lingjbh;
	}
	/**
	 * @param lingjbh the lingjbh to set
	 */
	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}
	/**
	 * 取得 dingdh
	 * @return the dingdh
	 */
	public String getDingdh() {
		return dingdh;
	}
	/**
	 * @param dingdh the dingdh to set
	 */
	public void setDingdh(String dingdh) {
		this.dingdh = dingdh;
	}
	/**
	 * 取得 wuld
	 * @return the wuld
	 */
	public String getWuld() {
		return wuld;
	}
	/**
	 * @param wuld the wuld to set
	 */
	public void setWuld(String wuld) {
		this.wuld = wuld;
	}
	/**
	 * 取得 lingjzl
	 * @return the lingjzl
	 */
	public BigDecimal getLingjzl() {
		return lingjzl;
	}
	/**
	 * @param lingjzl the lingjzl to set
	 */
	public void setLingjzl(BigDecimal lingjzl) {
		this.lingjzl = lingjzl;
	}
	
	
}
