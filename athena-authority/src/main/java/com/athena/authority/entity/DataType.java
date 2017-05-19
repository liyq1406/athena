/**
 *
 */
package com.athena.authority.entity;

import java.util.Date;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;


/**
 * 实体: 0303数据权限类型
 * @author
 * @version
 * 
 */
public class DataType extends PageableSupport implements Domain{
	
	private static final long serialVersionUID = 3976789601265001492L;
	
	private String dataId;//数据权限编码
	private String dataName;//数据权限名称
	private String dataDesc;//数据权限描述
	private String dataParam;//数据权限参数
	private String dataCode;//数据类型ID
	private String mender;//P_修改人
	private String modifyTime;//P_修改时间
	private String creator;//P_创建人
	private String createTime;//P_创建时间
	private String biaos;//P_是否有效
	
	private String usercenter;
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
	 * dataName getter方法
	 * @return
	 */
	public String getDataName() {
		return dataName;
	}

	/**
	 * dataName setter方法
	 * @param dataName
	 */
	public void setDataName(String dataName) {
		this.dataName = dataName;
	}

	/**
	 * dataDesc getter方法
	 * @return
	 */
	public String getDataDesc() {
		return dataDesc;
	}

	/**
	 * dataDesc setter方法
	 * @param dataDesc
	 */
	public void setDataDesc(String dataDesc) {
		this.dataDesc = dataDesc;
	}

	/**
	 * dataParam getteer方法
	 * @return
	 */
	public String getDataParam() {
		return dataParam;
	}

	/**
	 * dataParam setter方法
	 * @param dataParam
	 */
	public void setDataParam(String dataParam) {
		this.dataParam = dataParam;
	}

	/**
	 * dataCode getter方法
	 * @return
	 */
	public String getDataCode() {
		return dataCode;
	}

	/**
	 * dataCode setter方法
	 * @param dataCode
	 */
	public void setDataCode(String dataCode) {
		this.dataCode = dataCode;
	}

	/**
	 * mender getter方法
	 * @return
	 */
	public String getMender() {
		return mender;
	}

	/**
	 * mender setter方法
	 * @param mender
	 */
	public void setMender(String mender) {
		this.mender = mender;
	}

	/**
	 * modifyTime getter方法
	 * @return
	 */
	public String getModifyTime() {
		return modifyTime;
	}

	/**
	 * modifyTime setter方法
	 * @param modifyTime
	 */
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	/**
	 * creator getter方法
	 * @return
	 */
	public String getCreator() {
		return creator;
	}

	/**
	 * creator setter方法
	 * @param creator
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}

	/**
	 * createtime getter方法
	 * @return
	 */
	public String getCreateTime() {
		return createTime;
	}

	/**
	 * createtime setter方法
	 * @param createTime
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

	/**
	 * 继承于Domain中的id
	 */
	public String getId() {
		return dataId;
	}


	public void setId(String id) {
		this.dataId = id;
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