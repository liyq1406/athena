package com.athena.ckx.entity.paicfj;

import java.util.Date;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

public class Ckx_peizclzb extends PageableSupport implements Domain {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void setId(String id) {

	}

	public String getId() {
		return null;
	}
	 private String  baozzbh            ;//包装组编号
	 private String  usercenter         ;//用户中心
	 private String  celbh              ;//策略编号
	 private String  baozsl             ;//包装数量
	 private String  creator            ;//创建人
	 private Date    create_time        ;//创建时间
	 private String  editor             ;//修改人
	 private Date    edit_time          ;//修改时间

	public String getBaozzbh() {
		return baozzbh;
	}

	public void setBaozzbh(String baozzbh) {
		this.baozzbh = baozzbh;
	}

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getCelbh() {
		return celbh;
	}

	public void setCelbh(String celbh) {
		this.celbh = celbh;
	}

	public String getBaozsl() {
		return baozsl;
	}

	public void setBaozsl(String baozsl) {
		this.baozsl = baozsl;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public Date getEdit_time() {
		return edit_time;
	}

	public void setEdit_time(Date edit_time) {
		this.edit_time = edit_time;
	}

}
