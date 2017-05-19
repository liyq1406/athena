/**
 *
 */
package com.athena.print.entity.sys;



import java.util.Date;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;


/**
 * 实体: 10打印日志
 * @author
 * @version
 * 
 */
public class Printlog extends PageableSupport implements Domain{
	
	private static final long serialVersionUID = 1L;
	/**
	 * 打印日志
	 */
	//描述
	private String sdesc;
	//菜单名称
	private String smenuname;
	//编号
	private String lid;
	//操作时间
	private Date operatetime;
	//操作人
	private String soperater;
	//方法名称
	private String sfunctionname;
	//方法名称
	private String loginname;
	//状态
	private Long enabled;
	
	//return the sdesc
	public String getSdesc() {
		return sdesc;
	}
	//the sdesc to set
	public void setSdesc(String sdesc) {
		this.sdesc = sdesc;
	}
	//return the smenuname
	public String getSmenuname() {
		return smenuname;
	}
	//the smenuname to set
	public void setSmenuname(String smenuname) {
		this.smenuname = smenuname;
	}
	//return the lid
	public String getLid() {
		return lid;
	}
	//the lid to set
	public void setLid(String lid) {
		this.lid = lid;
	}
	//return the operatetime
	public Date getOperatetime() {
		return operatetime;
	}
	//the operatetime to set
	public void setOperatetime(Date operatetime) {
		this.operatetime = operatetime;
	}
	//return the soperater
	public String getSoperater() {
		return soperater;
	}
	//the soperater to set
	public void setSoperater(String soperater) {
		this.soperater = soperater;
	}
	//return the sfunctionname
	public String getSfunctionname() {
		return sfunctionname;
	}
	//the sfunctionname to set
	public void setSfunctionname(String sfunctionname) {
		this.sfunctionname = sfunctionname;
	}
	//return the loginname
	public String getLoginname() {
		return loginname;
	}
	//the loginname to set
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	//return the enabled
	public Long getEnabled() {
		return enabled;
	}
	//the enabled to set
	public void setEnabled(Long enabled) {
		this.enabled = enabled;
	}
	//the id to set
	public void setId(String id) {
		// TODO Auto-generated method stub
		
	}
	//return the scode
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}