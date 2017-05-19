package com.athena.ckx.entity.transTime;

import java.util.Date;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 模拟
 * @author hj
 * @Date 2012-03-19
 */
public class CkxTempMon extends PageableSupport  implements Domain{
	

	private static final long serialVersionUID = -6790607714902341609L;
	private String usercenter;//用户中心
	private String xiehztbh;//卸货站台编号
	private String dingszt ;//定时状态（1：定时，2：即时）
	
	private String creator;//创建人
	private Date create_time;//创建时间
	
	public void setId(String id) {
		// TODO Auto-generated method stub
		
	}

	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getXiehztbh() {
		return xiehztbh;
	}

	public void setXiehztbh(String xiehztbh) {
		this.xiehztbh = xiehztbh;
	}

	public String getDingszt() {
		return dingszt;
	}

	public void setDingszt(String dingszt) {
		this.dingszt = dingszt;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	
	
}
