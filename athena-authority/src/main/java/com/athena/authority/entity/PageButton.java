/**
 *
 */
package com.athena.authority.entity;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 实体: 0204页面按钮
 * 
 * @author
 * @version
 * 
 */
public class PageButton extends PageableSupport implements Domain {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3418908660379994563L;
	
	private String id;
	private String buttonName;// 按钮名称
	private String buttonCaption;// 按钮描述
	private String buttonCode;// 按钮编号
	private String treeId;//权限树型id
	
	private com.athena.authority.entity.MenuDirectory menuDirectory;// 菜单目录ID

	public String getButtonName() {
		return this.buttonName;
	}

	public void setButtonName(String buttonName) {
		this.buttonName = buttonName;
	}

	public String getButtonCaption() {
		return this.buttonCaption;
	}

	public void setButtonCaption(String buttonCaption) {
		this.buttonCaption = buttonCaption;
	}

	public String getButtonCode() {
		return this.buttonCode;
	}

	public void setButtonCode(String buttonCode) {
		this.buttonCode = buttonCode;
	}

	public void setMenuDirectory(
			com.athena.authority.entity.MenuDirectory menuDirectory) {
		this.menuDirectory = menuDirectory;
	}

	public com.athena.authority.entity.MenuDirectory getMenuDirectory() {
		return this.menuDirectory;
	}

	public String getId() {
		return buttonCode;
	}

	public void setId(String id) {
		this.buttonCode = id;
	}

	public String getTreeId() {
		return treeId;
	}

	public void setTreeId(String treeId) {
		this.treeId = treeId;
	}
}