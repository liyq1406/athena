/**
 *
 */
package com.athena.print.entity.sys;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;


/**
 * 实体: 04打印设备�?
 * @author
 * @version
 * 
 */
public class PrintDevice extends PageableSupport implements Domain{
	
	private static final long serialVersionUID = -2149526225845083747L;
	//用户中心
	private String usercenter;
	//打印机组编号
	private String spcodes;
	//优先级
	private String nlevel;
	//打印机编号
	private String spcode;
	//打印机名称
	private String sname;//sname
	//打印机描述
	private String sdesc;//sdesc
	//打印机端口
	private String sport;//sport
	//打印机IP
	private String sip;//sip
	//创建人
	private String creator;
	//创建时间
	private String create_time;
	//修改人
	private String editor;
	//修改时间
	private String edit_time;
	//打印机socket端口
	private int socketPort = 9100;
	//打印机udp端口
	private String udpPort = "161";
	//打印机snmp版本
	private int snmpVersion;
	//替代打印机编号
	private String replacespcode;
	
	
	
	public String getReplacespcode() {
		return replacespcode;
	}

	public void setReplacespcode(String replacespcode) {
		this.replacespcode = replacespcode;
	}

	//return the SnmpAddress
	public String getSnmpAddress() {
		return "udp:"+getSip()+"/"+getUdpPort();
	}
	
	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	//return the socketPort
	public int getSocketPort() {
		return socketPort;
	}
	//the socketPort to set
	public void setSocketPort(int socketPort) {
		this.socketPort = socketPort;
	}
	//return the udpPort
	public String getUdpPort() {
		return udpPort;
	}
	//the udpPort to set
	public void setUdpPort(String udpPort) {
		this.udpPort = udpPort;
	}
	//return the snmpVersion
	public int getSnmpVersion() {
		return snmpVersion;
	}
	//the snmpVersion to set
	public void setSnmpVersion(int snmpVersion) {
		this.snmpVersion = snmpVersion;
	}
	//return the spcodes
	public String getSpcodes() {
		return spcodes;
	}
	//the spcodes to set
	public void setSpcodes(String spcodes) {
		this.spcodes = spcodes;
	}
	//return the nlevel
	public String getNlevel() {
		return nlevel;
	}
	//the nlevel to set
	public void setNlevel(String nlevel) {
		this.nlevel = nlevel;
	}
	//return the spcode
	public String getSpcode() {
		return spcode;
	}
	//the spcode to set
	public void setSpcode(String spcode) {
		this.spcode = spcode;
	}
	//return the sname
	public String getSname() {
		return sname;
	}
	//the sname to set
	public void setSname(String sname) {
		this.sname = sname;
	}
	//return the sdesc
	public String getSdesc() {
		return sdesc;
	}
	//the sdesc to set
	public void setSdesc(String sdesc) {
		this.sdesc = sdesc;
	}
	//return the sport
	public String getSport() {
		return sport;
	}
	//the sport to set
	public void setSport(String sport) {
		this.sport = sport;
	}
	//return the sip
	public String getSip() {
		return sip;
	}
	//the sip to set
	public void setSip(String sip) {
		this.sip = sip;
	}
	//return the creator
	public String getCreator() {
		return creator;
	}
	//the creator to set
	public void setCreator(String creator) {
		this.creator = creator;
	}
	//return the create_time
	public String getCreate_time() {
		return create_time;
	}
	//the create_time to set
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	//return the editor
	public String getEditor() {
		return editor;
	}
	//the editor to set
	public void setEditor(String editor) {
		this.editor = editor;
	}
	//return the edit_time
	public String getEdit_time() {
		return edit_time;
	}
	//the edit_time to set
	public void setEdit_time(String edit_time) {
		this.edit_time = edit_time;
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