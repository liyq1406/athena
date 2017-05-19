/**
 *
 */
package com.athena.authority.entity;

import java.util.Date;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;


/**
 * 实体: 0203菜单资源
 * @author
 * @version
 * 
 */
public class MenuResource extends PageableSupport implements Domain{
	
	private static final long serialVersionUID = 2157152997368223691L;
	
	private String id;//P_主键
	private String menuCode;//菜单编号
	private String menuName;//菜单名称
	private String menuPath;//菜单url
	private String mender;//P_修改人
	private Date modifyTime;//P_修改时间
	private String creator;//P_创建人
	private Date createTime;//P_创建时间
	private String active;//P_是否有效	
	
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
	 * menuCode getter方法
	 * @return
	 */
	public String getMenuCode() {
		return menuCode;
	}


	/**
	 * menuCode setter方法
	 * @param menuCode
	 */
	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}


	/**
	 * menuName getter方法
	 * @return
	 */
	public String getMenuName() {
		return menuName;
	}

	/**
	 * menuName setter方法
	 * @param menuName
	 */

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}


	/**
	 * menuPath getter方法
	 * @return
	 */
	public String getMenuPath() {
		return menuPath;
	}


	/**
	 * menuPath setter方法
	 * @param menuPath
	 */
	public void setMenuPath(String menuPath) {
		this.menuPath = menuPath;
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
	 * createTime getter方法
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
	 * active getter方法
	 * @return
	 */
	public String getActive() {
		return active;
	}


	/**
	 * active setter方法
	 * @param active
	 */
	public void setActive(String active) {
		this.active = active;
	}


}