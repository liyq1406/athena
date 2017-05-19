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
public class Post extends PageableSupport implements Domain{
	
	private static final long serialVersionUID = 4671701387670255610L;
	
	private String id;//P_主键
	private String postCode;
	private String postName;//岗位名称
	private String postGroupId;//岗位分类ID
	private String attr;//岗位扩展属性
	private String mender;//P_修改人
	private String modifyTime;//P_修改时间
	private String creator;//P_创建人
	private String createTime;//P_创建时间
	private String biaos;//P_是否有效
	private String usercenter;//p_用户中心
	
	private String dicCode;
	
	private String postPageStyle;
	
	private PostGroup postGroup = new PostGroup();
	
	/**
	 * id Getter方法
	 */
	public String getId() {
		return id;
	}


	/**
	 *  id Settter方法 
	 */
	public void setId(String id) {
		this.id = id;
	}


	/**
	 * postCode getter方法
	 * @return
	 */
	public String getPostCode() {
		return postCode;
	}


	/**
	 * postCode setter方法
	 * @return
	 */
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}


	/**
	 * postName getter方法
	 * @return
	 */
	public String getPostName() {
		return postName;
	}


	/**
	 * postName setter方法
	 * @param postName
	 */
	public void setPostName(String postName) {
		this.postName = postName;
	}


	/**
	 * postGroupid getter方法
	 * @return
	 */
	public String getPostGroupId() {
		return postGroupId;
	}


	/**
	 * postGroupid setter方法
	 * @param postGroupId
	 */
	public void setPostGroupId(String postGroupId) {
		this.postGroupId = postGroupId;
	}


	/**
	 * attr getter方法
	 * @return
	 */
	public String getAttr() {
		return attr;
	}


	/**
	 * attr setter方法
	 */
	public void setAttr(String attr) {
		this.attr = attr;
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


	public PostGroup getPostGroup() {
		return postGroup;
	}



	public void setPostGroup(PostGroup postGroup) {
		this.postGroup = postGroup;
	}



	public String getUsercenter() {
		return usercenter;
	}



	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}


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
	 * @return the postPageStyle
	 */
	public String getPostPageStyle() {
		return postPageStyle;
	}


	/**
	 * @param postPageStyle the postPageStyle to set
	 */
	public void setPostPageStyle(String postPageStyle) {
		this.postPageStyle = postPageStyle;
	}

	
}