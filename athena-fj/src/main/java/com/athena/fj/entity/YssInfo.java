package com.athena.fj.entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author 王冲
 * @email jonw@sina.com
 * @date 2012-3-2
 * @time 上午11:26:31
 * @description  车辆资源
 */
public class YssInfo {
	/* description:   运输商名称*/
	private String gysMc = "";
	/* description:   运输商编号*/
	private String gysbh = "";
	
	private Set<String> cex= new HashSet<String>() ;
	
	/* description:   车辆型号名称*/
	private Map<String ,CelInfo> cxmc = new HashMap<String, CelInfo>();
	
	
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-5
	 * @time 下午05:01:35
	 * @return  cex
	 */
	public Set<String> getCex() {
		return cex;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-5
	 * @time 下午05:01:35
	 * @description 赋值cex
	 */
	public void setCex(Set<String> cex) {
		this.cex = cex;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-2
	 * @time 上午11:29:14
	 * @return  gysMc
	 */
	public String getGysMc() {
		return gysMc;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-2
	 * @time 上午11:29:14
	 * @description 赋值gysMc
	 */
	public void setGysMc(String gysMc) {
		this.gysMc = gysMc;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-2
	 * @time 上午11:35:41
	 * @return  gysbh
	 */
	public String getGysbh() {
		return gysbh;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-2
	 * @time 上午11:35:41
	 * @description 赋值gysbh
	 */
	public void setGysbh(String gysbh) {
		this.gysbh = gysbh;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-2
	 * @time 上午11:41:56
	 * @return  cxmc
	 */
	public Map<String, CelInfo> getCxmc() {
		return cxmc;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-2
	 * @time 上午11:41:56
	 * @description 赋值cxmc
	 */
	public void setCxmc(Map<String, CelInfo> cxmc) {
		this.cxmc = cxmc;
	}

}
