/**
 *
 */
package com.athena.authority.entity;

import java.util.Date;
import java.util.List;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;
import com.toft.ui.utils.TreeDesc;


/**
 * 实体: 0202菜单目录
 * @author
 * @version
 * 
 */
@TreeDesc(idAttr = "id",parentIdAttr="parentId", 
		textAttr = "dirName" ,codeAttr="dirCode")
public class MenuDirectory extends PageableSupport implements Domain{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7184744944510546403L;
	
	private String id;//P_主键
	private String dirCode;//菜单目录编号
	private String dirName;//菜单目录名称
	private Long dirType;//菜单目录类型
	private String parentId;//父目录id
	private String isitem;//是否明细
	private String dirOrder;//序号
	private String mender;//P_修改人
	private Date modifyTime;//P_修改时间
	private String creator;//P_创建人
	private Date createTime;//P_创建时间
	private String biaos;//P_是否有效
	private String dirPath;
	private String usercenter;
	private String dirIsCK;
	
	private String postId;
	private List<PageButton> buttons;
	
	private String treeId;//权限树型id
	
	
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
	 * dirCode getter方法
	 * @return
	 */
	public String getDirCode() {
		return dirCode;
	}



	/**
	 * dirCode setter方法
	 * @param dirCode
	 */
	public void setDirCode(String dirCode) {
		this.dirCode = dirCode;
	}



	/**
	 * dirName getter方法
	 * @return
	 */
	public String getDirName() {
		return dirName;
	}



	/**
	 * dirName setter方法
	 * @param dirName
	 */
	public void setDirName(String dirName) {
		this.dirName = dirName;
	}



	/**
	 * dirType getter方法
	 * @return
	 */
	public Long getDirType() {
		return dirType;
	}



	/**
	 * dirType setter方法
	 * @param dirType
	 */
	public void setDirType(Long dirType) {
		this.dirType = dirType;
	}


	
	/**
	 * parentId getter方法
	 * @return
	 */
	public String getParentId() {
		return parentId;
	}



	/**
	 * parentId setter方法
	 * @param parentId
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}



	/**
	 * isitem getter方法
	 * @return
	 */
	public String getIsitem() {
		return isitem;
	}



	/**
	 * isitem setter方法
	 * @param isitem
	 */
	public void setIsitem(String isitem) {
		this.isitem = isitem;
	}



	/**
	 * dirOrder getter方法
	 * @return
	 */
	public String getDirOrder() {
		return dirOrder;
	}



	/**
	 * dirOrder setter方法
	 * @param dirOrder
	 */
	public void setDirOrder(String dirOrder) {
		this.dirOrder = dirOrder;
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
	public Date getModifyTime() {
		return modifyTime;
	}


	/**
	 * modifyTime setter方法
	 * @param modifyTime
	 */

	public void setModifyTime(Date modifyTime) {
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
	 * creatime getter方法
	 * @return
	 */
	public Date getCreateTime() {
		return createTime;
	}



	/**
	 * createTime setter方法
	 * @param createTime
	 */
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



	public List<PageButton> getButtons() {
		return buttons;
	}




	public void setButtons(List<PageButton> buttons) {
		this.buttons = buttons;
	}


	


	public String getPostId() {
		return postId;
	}




	public void setPostId(String postId) {
		this.postId = postId;
	}



	/**
	 * @return the dirPath
	 */
	public String getDirPath() {
		return dirPath;
	}



	/**
	 * @param dirPath the dirPath to set
	 */
	public void setDirPath(String dirPath) {
		this.dirPath = dirPath;
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
	 * @return the dirIsCK
	 */
	public String getDirIsCK() {
		return dirIsCK;
	}



	/**
	 * @param dirIsCK the dirIsCK to set
	 */
	public void setDirIsCK(String dirIsCK) {
		this.dirIsCK = dirIsCK;
	}



	public String getTreeId() {
		return treeId;
	}



	public void setTreeId(String treeId) {
		this.treeId = treeId;
	}
	
	
	
}