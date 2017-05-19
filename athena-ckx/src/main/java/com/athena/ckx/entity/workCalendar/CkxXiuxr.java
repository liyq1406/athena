/**
 *
 */
package com.athena.ckx.entity.workCalendar;

import com.toft.core3.support.PageableSupport;
import com.athena.component.entity.Domain;

/**
 * 实体: 休息日
 * @author
 * @version
 * 
 */
public class CkxXiuxr extends PageableSupport  implements Domain{
	
	private static final long serialVersionUID = -6404567646033936250L;
	
	
	private String riq;//日期
	private String beiz;//备注
	private String biaos;//
	private String creator;//创建人
	private String createTime;
	private String editor;
	private String editTime;
	
	private String beginDate;//开始日期
	private String endDate;//结束日期
	
	
	
	
	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
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

	public String getBeiz(){
		return this.beiz;
	}
	
	public void setBeiz(String beiz){
		this.beiz = beiz;
	}
	public String getRiq(){
		return this.riq;
	}
	
	public void setRiq(String riq){
		this.riq = riq;
	}
	
	
	
	public void setId(String id) {
		this.riq = id;
	}

	public String getId() {
		return this.riq;
	}
}