/**
 * 
 */
package com.athena.authority.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.athena.authority.AthenaUser;

/**
 * <p>Title:SDC 权限管理</p>
 *
 * <p>Description:登录用户</p>
 *
 * <p>Copyright:Copyright (c) 2011.11</p>
 *
 * <p>Company:iSoftstone</p>
 *
 * @author zhouyi
 * @version 1.0.0
 */
public class LoginUser implements AthenaUser,Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7908287536973621501L;

	private String username;//登录用户名
	
	private String caption;//用户描述信息
	
	private String password;//用户密码
	
	private String module;//当前模块,预留
	
	private String usercenter;//当前登录机构ID
	
	private String zuh;
	
	private String jihyz;
	
	private Set<String> menuAndButtonsIds;//用户可见的菜单和按钮
	private Set<String> postIds;//用户组集合
	private Set<String> roleIds;//用户组分类集合
	
	/*
	 * 权限体系中，用户组分类和用户组的关系和限制如下：
	 * 一个用户可以分配多个用户组，分配的用户组不能具有相同的用户组分类 
	 */
	private Map<String,String> postAndRoleMap;//用户组分类和用户组的对应关系
	
	private Map<String,Object> params = new HashMap<String,Object>();//其他参数
	
	private List<String> attrList = new ArrayList<String>();
	
	private List<String> ucList;
	/**
	 * username getter方法
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * usrename setter方法
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * password getter方法
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * password setter方法
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public Set<String> getMenuAndButtonsIds() {
		return menuAndButtonsIds;
	}

	public void setMenuAndButtonsIds(Set<String> menuAndButtonsIds) {
		this.menuAndButtonsIds = menuAndButtonsIds;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public Set<String> getPostIds() {
		return postIds;
	}

	public void setPostIds(Set<String> postIds) {
		this.postIds = postIds;
	}

	public Set<String> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(Set<String> roleIds) {
		this.roleIds = roleIds;
	}


	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public boolean hasRole(String roleId){
		return this.roleIds.contains(roleId);
	}
	
	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String toString(){
		//TODO  界面显示的登录用户信息
		return this.caption;
	}

	public Map<String, String> getPostAndRoleMap() {
		return postAndRoleMap;
	}

	public void setPostAndRoleMap(Map<String, String> postAndRoleMap) {
		this.postAndRoleMap = postAndRoleMap;
	}
	
	/**
	 * 添加其他参数
	 * @param key
	 * @param value
	 */
	public void addParam(String key,Object value){
		if(key!=null&&value!=null){
			this.params.put(key, value);
		}
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
	 * @return the attrList
	 */
	public List<String> getAttrList() {
		return attrList;
	}

	/**
	 * @param attrList the attrList to set
	 */
	public void setAttrList(List<String> attrList) {
		this.attrList = attrList;
	}

	/**
	 * @return the jihyz
	 */
	public String getJihyz() {
		return jihyz;
	}

	/**
	 * @param jihyz the jihyz to set
	 */
	public void setJihyz(String jihyz) {
		this.jihyz = jihyz;
	}

	/**
	 * @return the ucList
	 */
	public List<String> getUcList() {
		return ucList;
	}

	/**
	 * @param ucList the ucList to set
	 */
	public void setUcList(List<String> ucList) {
		this.ucList = ucList;
	}
	
	private Map dicCodeUcMap;
	/**
	 * @return the dicCodeUcMap
	 */
	public Map getDicCodeUcMap() {
		return dicCodeUcMap;
	}

	/**
	 * @param dicCodeUcMap the dicCodeUcMap to set
	 */
	public void setDicCodeUcMap(Map dicCodeUcMap) {
		this.dicCodeUcMap = dicCodeUcMap;
	}

	
}
