package com.athena.ckx.entity.carry;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 物理点类型
 * @author kong
 *
 */
public class CkxWuldlx extends PageableSupport  implements Domain{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1534998896709747871L;
	private String wuldlxbh;
	private String hany;
	private String qissxh;
	private String jiessxh;
	private String creator;//创建人
	private String biaos;
	private String createTime;
	private String editor;
	private String editTime;
	
	
	
	
	
	
	public String getBiaos() {
		return biaos;
	}
	public void setBiaos(String biaos) {
		this.biaos = biaos;
	}
	public String getWuldlxbh() {
		return wuldlxbh;
	}
	public void setWuldlxbh(String wuldlxbh) {
		this.wuldlxbh = wuldlxbh;
	}
	public String getHany() {
		return hany;
	}
	public void setHany(String hany) {
		this.hany = hany;
	}
	public String getQissxh() {
		return qissxh;
	}
	public void setQissxh(String qissxh) {
		this.qissxh = qissxh;
	}
	public String getJiessxh() {
		return jiessxh;
	}
	public void setJiessxh(String jiessxh) {
		this.jiessxh = jiessxh;
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
