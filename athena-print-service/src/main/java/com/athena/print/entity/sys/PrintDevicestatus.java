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
	
	private String usercenter;//用户中心
	private String spcode;
	private String spcodes;
	private String sname;
	private String sip;
	private String sport;
	private int status;
	private int enable;
	private String nlevel;//nlevel
	private String usercode;//最后使用打印的用户
	private String lastqid;//最后打印作业的编号
	private int socketPort = 9100;//打印机socket端口
	private String udpPort = "161";//打印机udp端口
	private int snmpVersion;//打印机snmp版本
	private String replacespcode;//替代打印机
	
	
	
	
	public String getReplacespcode() {
		return replacespcode;
	}

	public void setReplacespcode(String replacespcode) {
		this.replacespcode = replacespcode;
	}

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getNlevel() {
		return nlevel;
	}

	public void setNlevel(String nlevel) {
		this.nlevel = nlevel;
	}
	public String getSnmpAddress() {
		return "udp:"+getSip()+"/"+getUdpPort();
	}
	
	public String getUdpPort() {
		return udpPort;
	}
	public void setUdpPort(String udpPort) {
		this.udpPort = udpPort;
	}
	public int getSnmpVersion() {
		return snmpVersion;
	}
	public void setSnmpVersion(int snmpVersion) {
		this.snmpVersion = snmpVersion;
	}
	public int getSocketPort() {
		return socketPort;
	}
	public void setSocketPort(int socketPort) {
		this.socketPort = socketPort;
	}
	public String getSpcode() {
		return spcode;
	}
	public void setSpcode(String spcode) {
		this.spcode = spcode;
	}
	public String getSpcodes() {
		return spcodes;
	}
	public void setSpcodes(String spcodes) {
		this.spcodes = spcodes;
	}
	public String getSname() {
		return sname;
	}
	public void setSname(String sname) {
		this.sname = sname;
	}
	public String getSip() {
		return sip;
	}
	public void setSip(String sip) {
		this.sip = sip;
	}
	public String getSport() {
		return sport;
	}
	public void setSport(String sport) {
		this.sport = sport;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	public int getEnable() {
		return enable;
	}

	public void setEnable(int enable) {
		this.enable = enable;
	}

	public String getUsercode() {
		return usercode;
	}
	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}
	public String getLastqid() {
		return lastqid;
	}
	public void setLastqid(String lastqid) {
		this.lastqid = lastqid;
	}
	public void setId(String id) {
		// TODO Auto-generated method stub
		
	}
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	


}