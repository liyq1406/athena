/**
 *
 */
package com.athena.authority.entity;

import java.util.Date;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;


/**
 * 实体: 0101用户
 * @author
 * @version
 * 
 */
public class User extends PageableSupport implements Domain{
	
	private static final long serialVersionUID = 436297982748683263L;
	
	private String id;//P_主键
	private String loginname;//登录名
	private String name;//名称
	private String password;//密码
	private String cellphone;//手机号码
	private String officephone;//办公室电话
	private String familyphone;//家庭电话
	private String fax;//传真
	private String email;//EMAIL
	private String description;//描述
	private String deptid;//部门ID
	private String pwdmodtime;//密码修改时间
	private String pwdresettime;//密码重置时间
	private String mender;//P_修改人
	private String modifyTime;//P_修改时间
	private String creator;//P_创建人
	private String createTime;//P_创建时间
	private String biaos;//P_是否有效
	private String zuh;
	private String usercenter;
	
	private String postId;//postid页面属性
	private String userpost;//用户所属组
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
	 * loginname getter方法
	 */
	public String getLoginname() {
		return loginname;
	}
	/**
	 * loginname setter方法
	 */
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	/**
	 * name getter方法
	 */
	public String getName() {
		return name;
	}
	/**
	 * name setter方法
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * pwssword getter方法
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * pwssword setter方法
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * cellphone getter方法
	 */
	public String getCellphone() {
		return cellphone;
	}
	/**
	 * cellphone setter方法
	 */
	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}
	/**
	 * officephone getter方法
	 */
	public String getOfficephone() {
		return officephone;
	}
	/**
	 * officephone setter方法
	 */
	public void setOfficephone(String officephone) {
		this.officephone = officephone;
	}
	/**
	 * familyphone getter方法
	 */
	public String getFamilyphone() {
		return familyphone;
	}
	/**
	 * familyphone setter方法
	 */
	public void setFamilyphone(String familyphone) {
		this.familyphone = familyphone;
	}
	/**
	 * fax getter方法
	 */
	public String getFax() {
		return fax;
	}
	/**
	 * fax setter方法
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}
	/**
	 * email getter方法
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * email setter方法
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * description getter方法
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * description setter方法
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * deptId getter方法
	 */
	public String getDeptid() {
		return deptid;
	}
	/**
	 * deptId setter方法
	 */
	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}
	/**
	 * pwdmodtime getter方法
	 */
	public String getPwdmodtime() {
		return pwdmodtime;
	}
	/**
	 * pwdmodtime setter方法
	 */
	public void setPwdmodtime(String pwdmodtime) {
		this.pwdmodtime = pwdmodtime;
	}
	/**
	 * pwdresettime getter方法
	 */
	public String getPwdresettime() {
		return pwdresettime;
	}
	/**
	 * pwdresettime setter方法
	 */
	public void setPwdresettime(String pwdresettime) {
		this.pwdresettime = pwdresettime;
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
	 * createtime getter方法
	 */
	public String getCreateTime() {
		return createTime;
	}
	/**
	 * createtime setter方法
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
	 * postId getter方法
	 */
	public String getPostId() {
		return postId;
	}
	/**
	 * postId setter方法
	 */
	public void setPostId(String postId) {
		this.postId = postId;
	}
	/**
	 * @return the zuh
	 */
	public String getZuh() {
		return zuh;
	}
	/**
	 * @param zuh the zuh to set
	 */
	public void setZuh(String zuh) {
		this.zuh = zuh;
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
	public String getUserpost() {
		return userpost;
	}
	public void setUserpost(String userpost) {
		this.userpost = userpost;
	}
	
	
	
}