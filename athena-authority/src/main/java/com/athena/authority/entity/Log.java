/**
 * 
 */
package com.athena.authority.entity;



import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * <p>Title:SDC UI组件</p>
 *
 * <p>Description:业务日记</p>
 *
 * <p>Copyright:Copyright (c) 2011.11</p>
 *
 * <p>Company:iSoftstone</p>
 *
 * @author zhouyi
 * @version 1.0.0
 */
public class Log extends PageableSupport implements Domain{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7224944564530693256L;
	
	private String id;
	private String cid ;//P_主键
	private String usercenter;//用户中心
	private String operators;//操作人
	private String operatorsIp;//操作人IP
	private String tranContent;//日记内容
	private String transDesc;//交易描述
	private String cflag;//日记类型,1正确日志,0错误日志,3系统日志
	private String transUrl;//交易地址
	private String clevel;//日记级别
	private String cclass;//异常类
	private String cexception;//异常内容
	private String createTime;//P_创建时间
	private String moduleName;//模块名称
	private String processName;//线程名称
	
	
	private String createTime_from;
	private String createTime_to;
	public String getCreateTime_from() {
		return createTime_from;
	}
	public void setCreateTime_from(String createTime_from) {
		this.createTime_from = createTime_from;
	}
	public String getCreateTime_to() {
		return createTime_to;
	}
	public void setCreateTime_to(String createTime_to) {
		this.createTime_to = createTime_to;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the cid
	 */
	public String getCid() {
		return cid;
	}
	/**
	 * @param cid the cid to set
	 */
	public void setCid(String cid) {
		this.cid = cid;
	}
	/**
	 * @return the usercenter
	 */
	public String getUsercenter() {
		return usercenter;
	}
	/**
	 * @param usercenter the usercenter to set
	 */
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	/**
	 * @return the operators
	 */
	public String getOperators() {
		return operators;
	}
	/**
	 * @param operators the operators to set
	 */
	public void setOperators(String operators) {
		this.operators = operators;
	}
	/**
	 * @return the operatorsIp
	 */
	public String getOperatorsIp() {
		return operatorsIp;
	}
	/**
	 * @param operatorsIp the operatorsIp to set
	 */
	public void setOperatorsIp(String operatorsIp) {
		this.operatorsIp = operatorsIp;
	}
	/**
	 * @return the tranContent
	 */
	public String getTranContent() {
		return tranContent;
	}
	/**
	 * @param tranContent the tranContent to set
	 */
	public void setTranContent(String tranContent) {
		this.tranContent = tranContent;
	}
	/**
	 * @return the transDesc
	 */
	public String getTransDesc() {
		return transDesc;
	}
	/**
	 * @param transDesc the transDesc to set
	 */
	public void setTransDesc(String transDesc) {
		this.transDesc = transDesc;
	}
	/**
	 * @return the cflag
	 */
	public String getCflag() {
		return cflag;
	}
	/**
	 * @param cflag the cflag to set
	 */
	public void setCflag(String cflag) {
		this.cflag = cflag;
	}
	/**
	 * @return the transUrl
	 */
	public String getTransUrl() {
		return transUrl;
	}
	/**
	 * @param transUrl the transUrl to set
	 */
	public void setTransUrl(String transUrl) {
		this.transUrl = transUrl;
	}
	/**
	 * @return the clevel
	 */
	public String getClevel() {
		return clevel;
	}
	/**
	 * @param clevel the clevel to set
	 */
	public void setClevel(String clevel) {
		this.clevel = clevel;
	}
	/**
	 * @return the cclass
	 */
	public String getCclass() {
		return cclass;
	}
	/**
	 * @param cclass the cclass to set
	 */
	public void setCclass(String cclass) {
		this.cclass = cclass;
	}
	/**
	 * @return the cexception
	 */
	public String getCexception() {
		return cexception;
	}
	/**
	 * @param cexception the cexception to set
	 */
	public void setCexception(String cexception) {
		this.cexception = cexception;
	}
	/**
	 * @return the createTime
	 */
	public String getCreateTime() {
		return createTime;
	}
	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	/**
	 * @return the moduleName
	 */
	public String getModuleName() {
		return moduleName;
	}
	/**
	 * @param moduleName the moduleName to set
	 */
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	/**
	 * @return the processName
	 */
	public String getProcessName() {
		return processName;
	}
	/**
	 * @param processName the processName to set
	 */
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	
	

}
