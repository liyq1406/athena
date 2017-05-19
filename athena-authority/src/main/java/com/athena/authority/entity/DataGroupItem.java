/**
 *
 */
package com.athena.authority.entity;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;


/**
 * 实体: 0302数据权限组项
 * @author
 * @version
 * 
 */
public class DataGroupItem extends PageableSupport implements Domain{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5369171778120890941L;
	
	private String id;//P_主键
	
	private String groupId;
	
	private String value;//代码项
	
	private String text;//代码描述
	
	private String usercenter;
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
	 * value getter方法
	 * @return
	 */
	public String getValue() {
		return value;
	}


	/**
	 * value setter方法
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}


	/**
	 * text getter方法
	 * @return
	 */
	public String getText() {
		return text;
	}


	/**
	 * text setter方法
	 * @param text
	 */
	public void setText(String text) {
		this.text = text;
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