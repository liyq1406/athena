package com.athena.ckx.entity.workCalendar;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

public class CkxCalendarGongyzx extends PageableSupport  implements Domain{

	/**
	 * 
	 */
	private static final long serialVersionUID = 315339813455695606L;
	
	
	
	private String gongyzx;//工业周期
	private String kaissj;//开始时间
	private String jiessj;//结束时间
	private String flag;//字段标识
	private String creator;//创建人
	private String createTime;
	private String editor;
	private String editTime;
	
	

	public void setId(String id) {
		// TODO Auto-generated method stub
		
	}

	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	

	public String getGongyzx() {
		return gongyzx;
	}

	public void setGongyzx(String gongyzx) {
		this.gongyzx = gongyzx;
	}

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

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
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
}