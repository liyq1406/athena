/**
 *
 */
package com.athena.authority.entity;

import java.util.Date;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;


/**
 * 实体: 0301数据权限组
 * @author
 * @version
 * 
 */
public class DataGroup extends PageableSupport implements Domain{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2598237960317850441L;
	
	private String groupId;//组ID
	private String dataId;
	private String groupName;//数据权限组名称
	private String groupDesc;//数据组描述
	private String mender;//P_修改人
	private Date modifyTime;//P_修改时间
	private String creator;//P_创建人
	private Date createTime;//P_创建时间
	private String biaos;//P_是否有效
	
	private String usercenter;
	
	/**
	 * groupId getter方法
	 * @return
	 */
	public String getGroupId() {
		return groupId;
	}


	/**
	 * groupId setter方法
	 * @param groupId
	 */
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}


	/**
	 * dataId getter方法
	 * @return
	 */
	public String getDataId() {
		return dataId;
	}


	/**
	 * dataId setter方法
	 * @param dataId
	 */
	public void setDataId(String dataId) {
		this.dataId = dataId;
	}


	/**
	 * groupName getter方法
	 * @return
	 */
	public String getGroupName() {
		return groupName;
	}


	/**
	 * groupName setter方法
	 * @param groupName
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}


	/**
	 * groupDesc getter方法
	 * @return
	 */
	public String getGroupDesc() {
		return groupDesc;
	}


	/**
	 * groupDesc setter方法
	 * @param groupDesc
	 */
	public void setGroupDesc(String groupDesc) {
		this.groupDesc = groupDesc;
	}



	public String getMender() {
		return mender;
	}



	public void setMender(String mender) {
		this.mender = mender;
	}



	public Date getModifyTime() {
		return modifyTime;
	}



	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}



	public String getCreator() {
		return creator;
	}



	public void setCreator(String creator) {
		this.creator = creator;
	}



	public Date getCreateTime() {
		return createTime;
	}



	public void setCreateTime(Date createTime) {
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


	public String getId() {
		return groupId;
	}



	public void setId(String id) {
		this.groupId = id;
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

	
}