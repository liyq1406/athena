package com.athena.xqjs.entity.ilorder;

import com.toft.core3.support.PageableSupport;

/**
 * 爱信订单装箱规则
 * @author WL
 * @version
 * 
 */
public class Aixddzxgz extends PageableSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8297671866671473628L;
	
	/**
	 * id
	 */
	private String id;
	
	/**
	 * UA堆数
	 */
	private String uads;
	
	/**
	 * UA个数
	 */
	private String uags;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUads() {
		return uads;
	}

	public void setUads(String uads) {
		this.uads = uads;
	}

	public String getUags() {
		return uags;
	}

	public void setUags(String uags) {
		this.uags = uags;
	}
	
	

}
