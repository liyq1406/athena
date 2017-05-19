/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company: 软通动力
 * </p>
 * 
 * @author 穆伟
 * @version v1.0
 * @date 
 */
package com.athena.authority.entity;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

public class CkDataType extends PageableSupport implements Domain{
	private String id;
	private String dataParamName;
	private String dataSQLParam;
	private String cuncTableName;
	private String mender;
	private String modifyTime;
	private String creator;
	private String createTime;
	private String biaos;
	private String dicCode;
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
	 * @return the dataParamName
	 */
	public String getDataParamName() {
		return dataParamName;
	}
	/**
	 * @param dataParamName the dataParamName to set
	 */
	public void setDataParamName(String dataParamName) {
		this.dataParamName = dataParamName;
	}
	/**
	 * @return the dataSQLParam
	 */
	public String getDataSQLParam() {
		return dataSQLParam;
	}
	/**
	 * @param dataSQLParam the dataSQLParam to set
	 */
	public void setDataSQLParam(String dataSQLParam) {
		this.dataSQLParam = dataSQLParam;
	}
	/**
	 * @return the cuncTableName
	 */
	public String getCuncTableName() {
		return cuncTableName;
	}
	/**
	 * @param cuncTableName the cuncTableName to set
	 */
	public void setCuncTableName(String cuncTableName) {
		this.cuncTableName = cuncTableName;
	}
	/**
	 * @return the mender
	 */
	public String getMender() {
		return mender;
	}
	/**
	 * @param mender the mender to set
	 */
	public void setMender(String mender) {
		this.mender = mender;
	}
	/**
	 * @return the modifyTime
	 */
	public String getModifyTime() {
		return modifyTime;
	}
	/**
	 * @param modifyTime the modifyTime to set
	 */
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}
	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
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
	 * @return the biaos
	 */
	public String getBiaos() {
		return biaos;
	}
	/**
	 * @param biaos the biaos to set
	 */
	public void setBiaos(String biaos) {
		this.biaos = biaos;
	}
	public String getDicCode() {
		return dicCode;
	}
	public void setDicCode(String dicCode) {
		this.dicCode = dicCode;
	}

	
	

}
