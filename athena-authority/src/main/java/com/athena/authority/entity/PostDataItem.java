/**
 *
 */
package com.athena.authority.entity;

import com.toft.core3.support.PageableSupport;
import com.athena.component.entity.Domain;

/**
 * 实体: 岗位数据
 * @author
 * @version
 * 
 */
public class PostDataItem extends PageableSupport  implements Domain{
	/**
	 * 
	 */
	private static final long serialVersionUID = 770863187173321528L;
	private String id;//P_主键
	private String postCode;
	private String value;//数据项值
	private String text;//数据项名称
	private String dataId;
	private String usercenter;
	private Post post = new Post();
	private DataType dataType = new DataType();
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
	 * @return the postCode
	 */
	public String getPostCode() {
		return postCode;
	}

	/**
	 * @param postCode the postCode to set
	 */
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	/**
	 * value getter方法
	 */
	public String getValue() {
		return value;
	}

	/**
	 * value setter方法
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * text getter方法
	 */
	public String getText() {
		return text;
	}

	/**
	 * text setter方法
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * dataId getter方法
	 */
	public String getDataId() {
		return dataId;
	}

	/**
	 * dataId setter方法
	 */
	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	/**
	 * post getter方法
	 */
	public Post getPost() {
		return post;
	}

	/**
	 * post setter方法
	 */
	public void setPost(Post post) {
		this.post = post;
	}

	/**
	 * dataType getter方法
	 */
	public DataType getDataType() {
		return dataType;
	}

	/**
	 * dataType setter方法
	 */
	public void setDataType(DataType dataType) {
		this.dataType = dataType;
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