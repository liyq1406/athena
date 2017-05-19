package com.athena.ckx.entity.carry;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

public class CkxWaibms extends PageableSupport  implements Domain{
	/**
	 * 
	 */
	private static final long serialVersionUID = -802223442408937416L;
	private String usercenter;
	private String lingjbh;
	private String mudd1;
	private String fenpqh;
	
	
	private String mos;
	private String zhidgys;
	private String jianglms;
	private String shengxsj;
	private String creator;//创建人
	private String createTime;
	private String editor;
	private String editTime;
	
	private String qid;//仓库起点（临时，模式切换变更记录专用）
	
	public String getQid() {
		return qid;
	}
	public void setQid(String qid) {
		this.qid = qid;
	}
	public String getFenpqh() {
		return fenpqh;
	}
	public void setFenpqh(String fenpqh) {
		this.fenpqh = fenpqh;
	}
	public String getMudd1() {
		return mudd1;
	}
	public void setMudd1(String mudd1) {
		this.mudd1 = mudd1;
	}
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	public String getLingjbh() {
		return lingjbh;
	}
	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}
	public String getMos() {
		return mos;
	}
	public void setMos(String mos) {
		this.mos = mos;
	}
	public String getZhidgys() {
		return zhidgys;
	}
	public void setZhidgys(String zhidgys) {
		this.zhidgys = zhidgys;
	}
	public String getJianglms() {
		return jianglms;
	}
	public void setJianglms(String jianglms) {
		this.jianglms = jianglms;
	}
	public String getShengxsj() {
		return shengxsj;
	}
	public void setShengxsj(String shengxsj) {
		this.shengxsj = shengxsj;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getEditor() {
		return editor;
	}
	public void setEditor(String editor) {
		this.editor = editor;
	}
	public String getEditTime() {
		return editTime;
	}
	public void setEditTime(String editTime) {
		this.editTime = editTime;
	}
	public void setId(String id) {
		// TODO Auto-generated method stub
		
	}
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}
}
