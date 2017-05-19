package com.athena.fj.entity;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * @author 王冲
 * @email jonw@sina.com
 * @date 2012-2-15
 * @time 下午04:17:39
 * @description 要贷令
 */
public class YaoHL extends PageableSupport implements Domain{
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = -8374913262179208250L;
	/* description:   UA包装型号*/
	private String BAOZXH ="";
	/* description:   包装组*/
	private String BAOZZBH = "";
	/* description:   要货令号*/
	private String YAOHLH = "";
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 下午04:21:02
	 * @return  bAOZXH
	 */
	public String getBAOZXH() {
		return BAOZXH;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 下午04:21:02
	 * @description 赋值bAOZXH
	 */
	public void setBAOZXH(String bAOZXH) {
		BAOZXH = bAOZXH;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 下午04:21:02
	 * @return  bAOZZBH
	 */
	public String getBAOZZBH() {
		return BAOZZBH;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 下午04:21:02
	 * @description 赋值bAOZZBH
	 */
	public void setBAOZZBH(String bAOZZBH) {
		BAOZZBH = bAOZZBH;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 下午04:21:02
	 * @return  yAOHLH
	 */
	public String getYAOHLH() {
		return YAOHLH;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 下午04:21:02
	 * @description 赋值yAOHLH
	 */
	public void setYAOHLH(String yAOHLH) {
		YAOHLH = yAOHLH;
	}
	@Override
	public void setId(String id) {
		
	}
	@Override
	public String getId() {
		return null;
	}
	
	
	

}
