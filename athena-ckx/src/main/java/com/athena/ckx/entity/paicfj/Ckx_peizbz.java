package com.athena.ckx.entity.paicfj;

import java.util.Date;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

public class Ckx_peizbz extends PageableSupport implements Domain {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void setId(String id) {

	}

	public String getId() {
		return null;
	}
	
	 private String  baozzbh           ;//包装组编号
	 private String  baozzmc           ;//包装组名称
	 private String  baozbsjs          ;//包装倍数基数
	 private String  biaos            ;//标识
	 private String  creator           ;//创建人
	 private Date    create_time       ;//创建时间
	 private String  editor            ;//修改人
	 private Date    edit_time         ;//修改时间

	public String getBaozzbh() {
		return baozzbh;
	}

	public void setBaozzbh(String baozzbh) {
		this.baozzbh = baozzbh;
	}

	public String getBaozzmc() {
		return baozzmc;
	}

	public void setBaozzmc(String baozzmc) {
		this.baozzmc = baozzmc;
	}

	public String getBaozbsjs() {
		return baozbsjs;
	}

	public void setBaozbsjs(String baozbsjs) {
		this.baozbsjs = baozbsjs;
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
