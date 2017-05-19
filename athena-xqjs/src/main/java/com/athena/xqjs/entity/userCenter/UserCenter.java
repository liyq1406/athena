package com.athena.xqjs.entity.userCenter;

import com.toft.core3.cache.core.Cacheable;

import com.toft.core3.support.PageableSupport;


/**
 * 
 * 项目名称：athena-ckx
 * 类名称：UserCenter
 * 类描述：用户中心实体bean
 * 创建人：niesy
 * 创建时间：2011-10-25 下午01:48:34
 * @version 
 * 
 */

public class UserCenter  extends PageableSupport implements Cacheable<String>{
	
	
	private static final long serialVersionUID = 1L;

	private String usercenter;//用户中心编号
	
	private String centername;//用户中心名称
	
    private  String    biaos; //状态标示
	
	private  String  creator;//创建人编号|姓名 
	
	private  String   create_time;//创建时间
	
	private  String   mender;//修改人编号|姓名
	
	private  String   modify_time;//修改时间
	
	public  UserCenter(){
		
	}
	
	public UserCenter(String usercenter)
	{
		this.usercenter=usercenter;
	}
	
	
	
	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getCentername() {
		return centername;
	}

	public void setCentername(String centername) {
		this.centername = centername;
	}

	
	
	public String getBiaos() {
		return biaos;
	}

	public void setBiaos(String biaos) {
		this.biaos = biaos;
	}

	

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getMender() {
		return mender;
	}

	public void setMender(String mender) {
		this.mender = mender;
	}

	public String getModify_time() {
		return modify_time;
	}

	public void setModify_time(String modify_time) {
		this.modify_time = modify_time;
	}

	
	
	public String getCachedKey() {
		return  this.usercenter;
	}

	
	
	

}
