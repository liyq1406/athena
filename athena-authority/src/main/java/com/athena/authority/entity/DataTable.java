/**
 *
 */
package com.athena.authority.entity;

import java.util.Date;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;


/**
 * 实体: 0306业务数据表
 * @author
 * @version
 * 
 */
public class DataTable extends PageableSupport implements Domain{
	
	private static final long serialVersionUID = 4600578111666186591L;
	
	private String id;//P_主键

	private String postGroupId;//岗位分类ID
	
	private String dataId;//数据ID
	
	private String tableCode;//业务数据编码
	
	private String tableName;//数据表名
	
	private String postField;//岗位字段
	
	private String dataField;//数据权限字段
	
	private String mender;//P_修改人
	
	private String modifyTime;//P_修改时间
	
	private String creator;//P_创建人
	
	private String createTime;//P_创建时间
	
	private String biaos;//P_是否有效
	
	private String usercenter;
	
	private String tableCl;
	private PostGroup postGroup = new PostGroup();
	
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
	 * postGroupId getter方法
	 * @return
	 */
	public String getPostGroupId() {
		return postGroupId;
	}


	/**
	 * postGroupId setter方法
	 * @param postGroupId
	 */
	public void setPostGroupId(String postGroupId) {
		this.postGroupId = postGroupId;
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
	 * tableCode getter方法
	 * @return
	 */
	public String getTableCode() {
		return tableCode;
	}


	/**
	 * tableCode setter方法
	 * @param tableCode
	 */
	public void setTableCode(String tableCode) {
		this.tableCode = tableCode;
	}


	/**
	 * tableName getter方法
	 * @return
	 */
	public String getTableName() {
		return tableName;
	}


	/**
	 * tableName setter方法
	 * @param tableName
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}


	/**
	 * postField getter方法
	 * @return
	 */
	public String getPostField() {
		return postField;
	}


	/**
	 * postField setter方法
	 * @param postField
	 */
	public void setPostField(String postField) {
		this.postField = postField;
	}


	/**
	 * dataField getter方法
	 * @return
	 */
	public String getDataField() {
		return dataField;
	}


	/**
	 * dataField setter方法
	 * @param dataField
	 */
	public void setDataField(String dataField) {
		this.dataField = dataField;
	}



	public String getMender() {
		return mender;
	}



	public void setMender(String mender) {
		this.mender = mender;
	}



	public String getModifyTime() {
		return modifyTime;
	}



	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}



	public String getCreator() {
		return creator;
	}



	public void setCreator(String creator) {
		this.creator = creator;
	}



	public String getCreateTime() {
		return createTime;
	}



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
	 * @return the postGroup
	 */
	public PostGroup getPostGroup() {
		return postGroup;
	}


	/**
	 * @param postGroup the postGroup to set
	 */
	public void setPostGroup(PostGroup postGroup) {
		this.postGroup = postGroup;
	}


	/**
	 * @return the tableCl
	 */
	public String getTableCl() {
		return tableCl;
	}


	/**
	 * @param tableCl the tableCl to set
	 */
	public void setTableCl(String tableCl) {
		this.tableCl = tableCl;
	}

	

	
}