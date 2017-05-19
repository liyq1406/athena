package com.athena.xqjs.entity.quhyuns;

import com.toft.core3.support.PageableSupport;

/**
 * 排产报警信息
 * @author dsimedd001
 *
 */
public class WarnMessage extends PageableSupport {

	private static final long serialVersionUID = -11798556390424168L;
	
	
	public WarnMessage() {
		super();
	}

	
	public WarnMessage(String xiaox, String creator) {
		super();
		this.xiaox = xiaox;
		this.creator = creator;
	}
	
	

	// 0013563 把报警信息放入异常报警查询 王朋
	public WarnMessage(String usercenter, String leix,String xiaox,String beiz1,String beiz2,
			String creator) {
		super();
		this.usercenter = usercenter;
		this.leix = leix;
		this.xiaox = xiaox;
		this.beiz1 = beiz1;
		this.beiz2 = beiz2;
		this.creator = creator;
	}
	
	

	



	
	

	/**
	 * 用户中心
	 */
	private String usercenter;
	

	private String leix;
	/**
	 * 消息
	 */
	private String xiaox;
	
	/**
	 * 备注1
	 */
	private String beiz1;
	
	/**
	 * 备注2
	 */
	private String beiz2;
	
	/**
	 * 备注3
	 */
	private String beiz3;
	
	/**
	 * 创建人
	 */
	private String creator;

	/**
	 * 创建时间
	 */
	private String create_time;

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}


	public String getXiaox() {
		return xiaox;
	}

	public void setXiaox(String xiaox) {
		this.xiaox = xiaox;
	}

	public String getBeiz1() {
		return beiz1;
	}

	public void setBeiz1(String beiz1) {
		this.beiz1 = beiz1;
	}

	public String getBeiz2() {
		return beiz2;
	}

	public void setBeiz2(String beiz2) {
		this.beiz2 = beiz2;
	}

	public String getBeiz3() {
		return beiz3;
	}

	public void setBeiz3(String beiz3) {
		this.beiz3 = beiz3;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}


	public String getLeix() {
		return leix;
	}


	public void setLeix(String leix) {
		this.leix = leix;
	}


	@Override
	public String toString() {
		return "添加报警信息，内容："+xiaox;
	}
	
}
