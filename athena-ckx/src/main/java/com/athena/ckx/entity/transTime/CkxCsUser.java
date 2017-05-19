package com.athena.ckx.entity.transTime;

import java.util.Date;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * CS初始化
 * @author xss
 * @Date 2014-09-16
 */


public class CkxCsUser extends PageableSupport
	implements Domain
{

	private static final long serialVersionUID = 0x60e0ab425f46bffL;
	private String id;
	private String loginname;
	private String name;
	private String password;
	private String cellphone;
	private String officephone;
	private String familyphone;
	private String fax;
	private String email;
	private String description;
	private String deptid;
	private String pwdmodtime;
	private String pwdresettime;
	private String mender;
	private String modifyTime;
	private String creator;
	private String createTime;
	private String biaos;
	private String zuh;
	private String usercenter;
	private String postId;
	private String userpost;

	public CkxCsUser()
	{
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getLoginname()
	{
		return loginname;
	}

	public void setLoginname(String loginname)
	{
		this.loginname = loginname;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getCellphone()
	{
		return cellphone;
	}

	public void setCellphone(String cellphone)
	{
		this.cellphone = cellphone;
	}

	public String getOfficephone()
	{
		return officephone;
	}

	public void setOfficephone(String officephone)
	{
		this.officephone = officephone;
	}

	public String getFamilyphone()
	{
		return familyphone;
	}

	public void setFamilyphone(String familyphone)
	{
		this.familyphone = familyphone;
	}

	public String getFax()
	{
		return fax;
	}

	public void setFax(String fax)
	{
		this.fax = fax;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getDeptid()
	{
		return deptid;
	}

	public void setDeptid(String deptid)
	{
		this.deptid = deptid;
	}

	public String getPwdmodtime()
	{
		return pwdmodtime;
	}

	public void setPwdmodtime(String pwdmodtime)
	{
		this.pwdmodtime = pwdmodtime;
	}

	public String getPwdresettime()
	{
		return pwdresettime;
	}

	public void setPwdresettime(String pwdresettime)
	{
		this.pwdresettime = pwdresettime;
	}

	public String getMender()
	{
		return mender;
	}

	public void setMender(String mender)
	{
		this.mender = mender;
	}

	public String getModifyTime()
	{
		return modifyTime;
	}

	public void setModifyTime(String modifyTime)
	{
		this.modifyTime = modifyTime;
	}

	public String getCreator()
	{
		return creator;
	}

	public void setCreator(String creator)
	{
		this.creator = creator;
	}

	public String getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(String createTime)
	{
		this.createTime = createTime;
	}

	public String getBiaos()
	{
		return biaos;
	}

	public void setBiaos(String biaos)
	{
		this.biaos = biaos;
	}

	public String getPostId()
	{
		return postId;
	}

	public void setPostId(String postId)
	{
		this.postId = postId;
	}

	public String getZuh()
	{
		return zuh;
	}

	public void setZuh(String zuh)
	{
		this.zuh = zuh;
	}

	public String getUsercenter()
	{
		return usercenter;
	}

	public void setUsercenter(String usercenter)
	{
		this.usercenter = usercenter;
	}

	public String getUserpost()
	{
		return userpost;
	}

	public void setUserpost(String userpost)
	{
		this.userpost = userpost;
	}
}