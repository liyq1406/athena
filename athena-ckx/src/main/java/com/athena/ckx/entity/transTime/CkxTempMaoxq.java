package com.athena.ckx.entity.transTime;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;


/**
 * 毛需求
 * @author kong
 *
 */
public class CkxTempMaoxq extends PageableSupport  implements Domain{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7799106292942284385L;
	private String usercenter;
	private String chanx;
	private String lingjbh;
	private Integer shul;
	private String kaissj;
	private String jiezsj;
	private String creator;//创建人
	private String createTime;
	private String editor;
	private String editTime;

	public void setId(String id) {
		// TODO Auto-generated method stub
		
	}

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getChanx() {
		return chanx;
	}

	public void setChanx(String chanx) {
		this.chanx = chanx;
	}

	public String getLingjbh() {
		return lingjbh;
	}

	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}

	public Integer getShul() {
		return shul;
	}

	public void setShul(Integer shul) {
		this.shul = shul;
	}

	public String getKaissj() {
		return kaissj;
	}

	public void setKaissj(String kaissj) {
		this.kaissj = kaissj;
	}

	public String getJiezsj() {
		return jiezsj;
	}

	public void setJiezsj(String jiezsj) {
		this.jiezsj = jiezsj;
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

	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

}
