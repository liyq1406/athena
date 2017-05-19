/**
 *
 */
package com.athena.authority.entity;

import java.util.List;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;


/**
 * 实体: 岗位组
 * @author
 * @version
 * 
 */
public class PostGroup extends PageableSupport implements Domain{
	
	private static final long serialVersionUID = -2755639454189486970L;
	
	
	private String id;//P_主键
	private String dataId;
	private String postGroupName;//岗位组名称
	private String postAttrName;//岗位扩展属性
	private String mender;//P_修改人
	private String modifyTime;//P_修改时间
	private String creator;//P_创建人
	private String createTime;//P_创建时间
	private String biaos;//P_是否有效
	private String isAdmin;//是否管理员
	private String usercenter;
	private String postGroupId;
	
	private List<DataType> dataTypes;//数据权限类型
	
	/**
	 * id getter方法
	 */
	public String getId() {
		return id;
	}

	/**
	 * id setter方法
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * data getter方法
	 */
	public String getDataId() {
		return dataId;
	}

	/**
	 * data setter方法
	 */
	public void setDataId(String dataId) {
		this.dataId = dataId;
	}
	/**
	 * postGroupName getter方法
	 */
	public String getPostGroupName() {
		return postGroupName;
	}

	/**
	 * postGroupName setter方法
	 */
	public void setPostGroupName(String postGroupName) {
		this.postGroupName = postGroupName;
	}

	/**
	 * postAttrName getter方法
	 */
	public String getPostAttrName() {
		return postAttrName;
	}

	/**
	 * postAttrName setter方法
	 */
	public void setPostAttrName(String postAttrName) {
		this.postAttrName = postAttrName;
	}

	/**
	 * mender getter方法
	 */
	public String getMender() {
		return mender;
	}

	/**
	 * mender setter方法
	 */
	public void setMender(String mender) {
		this.mender = mender;
	}

	/**
	 * modifyTime getter方法
	 */
	public String getModifyTime() {
		return modifyTime;
	}

	/**
	 * modifyTime setter方法
	 */
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	/**
	 * creator getter方法
	 */
	public String getCreator() {
		return creator;
	}

	/**
	 * creator setter方法
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}

	/**
	 * createTime getter方法
	 */
	public String getCreateTime() {
		return createTime;
	}

	/**
	 * createTime setter方法
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

	public String getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(String isAdmin) {
		this.isAdmin = isAdmin;
	}


	public List<DataType> getDataTypes() {
		return dataTypes;
	}


	public void setDataTypes(List<DataType> dataTypes) {
		this.dataTypes = dataTypes;
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
	
	
}