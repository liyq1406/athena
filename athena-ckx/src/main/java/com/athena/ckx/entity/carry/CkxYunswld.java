package com.athena.ckx.entity.carry;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 运输物理点
 * @author kong 
 *
 */
public class CkxYunswld extends PageableSupport  implements Domain{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3706888236911303226L;
	private String wuldbh;
	private String wuldmc;
	private String wuldlx;
	private String shunxh;
	private String quy;
	private String biaos;
	private String creator;//创建人
	private String createTime;
	private String editor;
	private String editTime;
	
	
	public String getBiaos() {
		return biaos;
	}
	public void setBiaos(String biaos) {
		this.biaos = biaos;
	}
	public String getWuldbh() {
		return wuldbh;
	}
	public void setWuldbh(String wuldbh) {
		this.wuldbh = wuldbh;
	}

	public String getWuldmc() {
		return wuldmc;
	}
	public void setWuldmc(String wuldmc) {
		this.wuldmc = wuldmc;
	}
	public String getWuldlx() {
		return wuldlx;
	}
	public void setWuldlx(String wuldlx) {
		this.wuldlx = wuldlx;
	}
	public String getShunxh() {
		return shunxh;
	}
	public void setShunxh(String shunxh) {
		this.shunxh = shunxh;
	}
	public String getQuy() {
		return quy;
	}
	public void setQuy(String quy) {
		this.quy = quy;
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
