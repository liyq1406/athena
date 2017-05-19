package com.athena.ckx.entity.carry;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 生产线-线边仓库物流路径
 * @author kong
 *
 */
public class CkxShengcxXianb extends PageableSupport  implements Domain{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3827141631596510816L;
	private String usercenter ;
	private String lingjbh;
	private String fenpqh;
	private String qid;
	private String zick;//起点+子仓库编码
	private String qidlx;
	private String mos;
	private String shengxbs;
	private String jianglms;
	private String jianglmssxsj;
	private String shengcxbh;
	private String creator;//创建人
	private String createTime;
	private String editor;
	private String editTime;
	
	
	public String getZick() {
		return zick;
	}
	public void setZick(String zick) {
		this.zick = zick;
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
	public String getFenpqh() {
		return fenpqh;
	}
	public void setFenpqh(String fenpqh) {
		this.fenpqh = fenpqh;
	}
	public String getQid() {
		return qid;
	}
	public void setQid(String qid) {
		this.qid = qid;
	}
	public String getQidlx() {
		return qidlx;
	}
	public void setQidlx(String qidlx) {
		this.qidlx = qidlx;
	}
	public String getMos() {
		return mos;
	}
	public void setMos(String mos) {
		this.mos = mos;
	}
	public String getShengxbs() {
		return shengxbs;
	}
	public void setShengxbs(String shengxbs) {
		this.shengxbs = shengxbs;
	}
	public String getJianglms() {
		return jianglms;
	}
	public void setJianglms(String jianglms) {
		this.jianglms = jianglms;
	}
	public String getJianglmssxsj() {
		return jianglmssxsj;
	}
	public void setJianglmssxsj(String jianglmssxsj) {
		this.jianglmssxsj = jianglmssxsj;
	}
	public String getShengcxbh() {
		return shengcxbh;
	}
	public void setShengcxbh(String shengcxbh) {
		this.shengcxbh = shengcxbh;
	}
	public void setId(String id) {
		// TODO Auto-generated method stub
		
	}
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
