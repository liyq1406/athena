/**
 *
 */
package com.athena.print.entity.sys;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;


/**
 * 实体: 09打印机状态表
 * @author
 * @version
 * 
 */
public class PrintDevicestatus extends PageableSupport implements Domain{
	
	private static final long serialVersionUID = 5037158050214433911L;
	
	//用户中心
	private String usercenter;
	
	//打印机编号
	private String spcode;
	//打印机组编号
	private String spcodes;
	//打印机名称
	private String sname;
	//打印机IP
	private String sip;
	//打印机端口
	private String sport;
	//打印机辅状态
	private int status;
	//打印机主状态
	private int enable;
	//最后使用打印的用户
	private String usercode;
	//最后打印作业的编号
	private String lastqid;
	//打印机优先级
	private String nlevel;
	//替代打印机编号
	private String replacespcode;
	
	
	
	
	public String getReplacespcode() {
		return replacespcode;
	}
	public void setReplacespcode(String replacespcode) {
		this.replacespcode = replacespcode;
	}
	public String getNlevel() {
		return nlevel;
	}
	public void setNlevel(String nlevel) {
		this.nlevel = nlevel;
	}
	private String sdevicecode;
	public String getSdevicecode() {
		return sdevicecode;
	}
	public void setSdevicecode(String sdevicecode) {
		this.sdevicecode = sdevicecode;
	}
	
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	//return the spcode
	public String getSpcode() {
		return spcode;
	}
	//the spcode to set
	public void setSpcode(String spcode) {
		this.spcode = spcode;
	}
	//return the spcodes
	public String getSpcodes() {
		return spcodes;
	}
	//the spcodes to set
	public void setSpcodes(String spcodes) {
		this.spcodes = spcodes;
	}
	//return the sname
	public String getSname() {
		return sname;
	}
	//the sname to set
	public void setSname(String sname) {
		this.sname = sname;
	}
	//return the sip
	public String getSip() {
		return sip;
	}
	//the sip to set
	public void setSip(String sip) {
		this.sip = sip;
	}
	//return the sport
	public String getSport() {
		return sport;
	}
	//the sport to set
	public void setSport(String sport) {
		this.sport = sport;
	}
	//return the status
	public int getStatus() {
		return status;
	}
	//the status to set
	public void setStatus(int status) {
		this.status = status;
	}
	//return the usercode
	public String getUsercode() {
		return usercode;
	}
	//return the enable
	public int getEnable() {
		return enable;
	}
	//the enable to set
	public void setEnable(int enable) {
		this.enable = enable;
	}
	//the usercode to set
	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}
	//return the lastqid
	public String getLastqid() {
		return lastqid;
	}
	//the lastqid to set
	public void setLastqid(String lastqid) {
		this.lastqid = lastqid;
	}
	//the id to set
	public void setId(String id) {
		// TODO Auto-generated method stub
		
	}
	//return the id
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	


}