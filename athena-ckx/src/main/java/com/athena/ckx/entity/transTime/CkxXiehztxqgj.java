package com.athena.ckx.entity.transTime;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;


/**
 * 卸货站台需求归集
 * @author kong
 *
 */
public class CkxXiehztxqgj extends PageableSupport  implements Domain{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7676353230072864339L;
	
	private String xiehztbh;
	private String chengysbh;
	private String lingjbh;
	private Integer xuqsl;
	private String usercenter;
	private String maoxqbb;
	private String kaissj;
	private String jiessj;
	private String creator;//创建人
	private String createTime;
	private String editor;
	private String editTime;
	
	
	

	public String getKaissj() {
		return kaissj;
	}

	public void setKaissj(String kaissj) {
		this.kaissj = kaissj;
	}

	public String getJiessj() {
		return jiessj;
	}

	public void setJiessj(String jiessj) {
		this.jiessj = jiessj;
	}

	public String getXiehztbh() {
		return xiehztbh;
	}

	public void setXiehztbh(String xiehztbh) {
		this.xiehztbh = xiehztbh;
	}

	public String getChengysbh() {
		return chengysbh;
	}

	public void setChengysbh(String chengysbh) {
		this.chengysbh = chengysbh;
	}

	public String getLingjbh() {
		return lingjbh;
	}

	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}

	public Integer getXuqsl() {
		return xuqsl;
	}

	public void setXuqsl(Integer xuqsl) {
		this.xuqsl = xuqsl;
	}

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getMaoxqbb() {
		return maoxqbb;
	}

	public void setMaoxqbb(String maoxqbb) {
		this.maoxqbb = maoxqbb;
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
		return "";
	}

}
