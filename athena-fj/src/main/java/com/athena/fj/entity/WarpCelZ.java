package com.athena.fj.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
 
/**
 * @author 王冲
 * @email jonw@sina.com
 * @date 2012-2-9
 * @time 下午02:03:48
 * @description 申报车辆资源
 */
public class WarpCelZ  implements Serializable{

	/* description: 策略编号 */
	private String celH = "";
	/* description: 包装组数量 */
	private Map<String, Integer> bzzSl = new HashMap<String, Integer>();
	/* description:   包装组基数*/
	private Map<String, Integer> bzzJs = new HashMap<String, Integer>();
	/* description:  此策略对应的车型*/
	private String cx = "";
	/* description:   车型名称*/
	private String cxName="";
	
	
	
	
	
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-24
	 * @time 下午01:11:06
	 * @return  bzzJs
	 */
	public Map<String, Integer> getBzzJs() {
		return bzzJs;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-24
	 * @time 下午01:11:06
	 * @description 赋值bzzJs
	 */
	public void setBzzJs(Map<String, Integer> bzzJs) {
		this.bzzJs = bzzJs;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 上午10:22:26
	 * @return  cxName
	 */
	public String getCxName() {
		return cxName;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 上午10:22:26
	 * @description 赋值cxName
	 */
	public void setCxName(String cxName) {
		this.cxName = cxName;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-10
	 * @time 上午11:40:02
	 * @return  bzzSl
	 */
	public Map<String, Integer> getBzzSl() {
		return bzzSl;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-10
	 * @time 上午11:40:02
	 * @description 赋值bzzSl
	 */
	public void setBzzSl(Map<String, Integer> bzzSl) {
		this.bzzSl = bzzSl;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-10
	 * @time 上午11:40:02
	 * @return  cx
	 */
	public String getCx() {
		return cx;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-10
	 * @time 上午11:40:02
	 * @description 赋值cx
	 */
	public void setCx(String cx) {
		this.cx = cx;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-10
	 * @time 下午01:36:22
	 * @return  celH
	 */
	public String getCelH() {
		return celH;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-10
	 * @time 下午01:36:22
	 * @description 赋值celH
	 */
	public void setCelH(String celH) {
		this.celH = celH;
	}
	
	

	
}
