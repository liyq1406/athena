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
	
	private String spcodes;//spcodes
	private String nlevel;//nlevel
	private String spcode;//spcode
	private String sname;//sname
	private String sdesc;//sdesc
	private String sport;//sport
	private String sip;//sip
	private String creator;
	private String create_time;
	private String editor;
	private String edit_time;
	//private String ip;//打印机IP
	private int socketPort = 9100;//打印机socket端口
	private String udpPort = "161";//打印机udp端口
	private int snmpVersion;//打印机snmp版本
	private String replacespcode;//替代打印机编号
	
	
	
	public String getReplacespcode() {
		return replacespcode;
	}

	public void setReplacespcode(String replacespcode) {
		this.replacespcode = replacespcode;
	}

	public String getSnmpAddress() {
		return "udp:"+getSip()+"/"+getUdpPort();
	}
	
	public int getSocketPort() {
		return socketPort;
	}
	public void setSocketPort(int socketPort) {
		this.socketPort = socketPort;
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

	public String getSpcodes() {
		return spcodes;
	}
	public void setSpcodes(String spcodes) {
		this.spcodes = spcodes;
	}
	public String getNlevel() {
		return nlevel;
	}
	public void setNlevel(String nlevel) {
		this.nlevel = nlevel;
	}
	public String getSpcode() {
		return spcode;
	}
	public void setSpcode(String spcode) {
		this.spcode = spcode;
	}
	public String getSname() {
		return sname;
	}
	public void setSname(String sname) {
		this.sname = sname;
	}
	public String getSdesc() {
		return sdesc;
	}
	public void setSdesc(String sdesc) {
		this.sdesc = sdesc;
	}
	public String getSport() {
		return sport;
	}
	public void setSport(String sport) {
		this.sport = sport;
	}
	public String getSip() {
		return sip;
	}
	public void setSip(String sip) {
		this.sip = sip;
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
	public String getEditor() {
		return editor;
	}
	public void setEditor(String editor) {
		this.editor = editor;
	}
	public String getEdit_time() {
		return edit_time;
	}
	public void setEdit_time(String edit_time) {
		this.edit_time = edit_time;
	}
	public void setId(String id) {
		// TODO Auto-generated method stub
		
	}
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	 
	
	
	
}