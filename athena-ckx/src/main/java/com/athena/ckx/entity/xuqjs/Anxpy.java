package com.athena.ckx.entity.xuqjs;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

public class Anxpy extends PageableSupport implements Domain{
	
	private static final long serialVersionUID = 1861676571164967555L;


	private String id;//id
	private String flag;//生效标识
	private String creator;//创建人
	private String createTime;
	private String editor;
	private String editTime;
	private String chanx;//产线时间
	private String usercenter;//用户中心	
	private String txsj;//停线时间
	private Integer pysj;//平移时间
	

	private String beginDate;//开始日期
	private String endDate;//结束日期
	
	private String riq;//日期
	
	
	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		this.riq = riq;
	}
	
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
	
	public String getTxsj() {
		return txsj;
	}

	public void setTxsj(String txsj) {
		this.txsj = txsj;
	}

	public Integer getPysj() {
		return pysj;
	}

	public void setPysj(Integer pysj) {
		this.pysj = pysj;
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

	
	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}
    
	
}
