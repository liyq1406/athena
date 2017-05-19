/**
 *
 */
package com.athena.authority.entity;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;


/**
 * 实体: 用户组
 * @author
 * @version
 * 
 */
public class Dic extends PageableSupport implements Domain{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String dicCode;
	private String dicName;
	private String dicMemo;
	private String creator;
	private String createTime;
	private String mender;
	private String modifyTime;
	private String postGroupId;
	private String postGroupName;
	/**
	 * @return the dicCode
	 */
	public String getDicCode() {
		return dicCode;
	}
	/**
	 * @param dicCode the dicCode to set
	 */
	public void setDicCode(String dicCode) {
		this.dicCode = dicCode;
	}
	/**
	 * @return the dicName
	 */
	public String getDicName() {
		return dicName;
	}
	/**
	 * @param dicName the dicName to set
	 */
	public void setDicName(String dicName) {
		this.dicName = dicName;
	}
	/**
	 * @return the dicMemo
	 */
	public String getDicMemo() {
		return dicMemo;
	}
	/**
	 * @param dicMemo the dicMemo to set
	 */
	public void setDicMemo(String dicMemo) {
		this.dicMemo = dicMemo;
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
	 * @return the postGroupId
	 */
	public String getPostGroupId() {
		return postGroupId;
	}
	/**
	 * @param postGroupId the postGroupId to set
	 */
	public void setPostGroupId(String postGroupId) {
		this.postGroupId = postGroupId;
	}
	/**
	 * @return the postGroupName
	 */
	public String getPostGroupName() {
		return postGroupName;
	}
	/**
	 * @param postGroupName the postGroupName to set
	 */
	public void setPostGroupName(String postGroupName) {
		this.postGroupName = postGroupName;
	}
	
	
	
}