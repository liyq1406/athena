package com.athena.authority.entity;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

public class SysRUserPost extends PageableSupport implements Domain{
	
	private static final long serialVersionUID = 7184749944510446403L;
	
	private String id;
	private String postId;
	private String funcId;
	private String usercenter;
	private String treeId;//权限树型id
	
	public String getId() {
		return id;
	}
	/**
	 * id setter方法
	 */
	public void setId(String id) {
		this.id = id;
	}
	public String getPostId() {
		return postId;
	}
	public void setPostId(String postId) {
		this.postId = postId;
	}

	public String getFuncId() {
		return funcId;
	}
	public void setFuncId(String funcId) {
		this.funcId = funcId;
	}
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	public String getTreeId() {
		return treeId;
	}
	public void setTreeId(String treeId) {
		this.treeId = treeId;
	}
}
