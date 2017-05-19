package com.athena.ckx.entity.paicfj;

import java.util.Date;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

public class Ckx_chanxz extends PageableSupport implements Domain {

	/**
	 * 
	 */
	private static final long serialVersionUID = 124512341234123414L;

	private String usercenter     ;//用户中心
	private String chanxzbh       ;//产线组编号
	private String chanxzmc       ;//产线组名称
	private String jihyzbh;		   //计划员组编号
	private String tessjxq1;       //特殊时间1星期
	private String tessjxs1;       //特殊时间1小时
	private String tessjxq2;       //特殊时间2星期
	private String tessjxs2;       //特殊时间2小时
	private String tessjxq3;       //特殊时间3星期
	private String tessjxs3;       //特殊时间3小时
	private String beiz;		   //备注
	private String creator;        //创建人
	private Date   create_time;    //创建时间
	private String editor;         //修改人
	private Date  edit_time;       //修改时间


	public String getJihyzbh() {
		return jihyzbh;
	}

	public void setJihyzbh(String jihyzbh) {
		this.jihyzbh = jihyzbh;
	}

	public String getBeiz() {
		return beiz;
	}

	public void setBeiz(String beiz) {
		this.beiz = beiz;
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


	

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getChanxzbh() {
		return chanxzbh;
	}

	public void setChanxzbh(String chanxzbh) {
		this.chanxzbh = chanxzbh;
	}

	public String getChanxzmc() {
		return chanxzmc;
	}

	public void setChanxzmc(String chanxzmc) {
		this.chanxzmc = chanxzmc;
	}

	public void setId(String id) {
	}

	public String getId() {
		return null;
	}

	public String getTessjxq1() {
		return tessjxq1;
	}

	public void setTessjxq1(String tessjxq1) {
		this.tessjxq1 = tessjxq1;
	}

	public String getTessjxs1() {
		return tessjxs1;
	}

	public void setTessjxs1(String tessjxs1) {
		this.tessjxs1 = tessjxs1;
	}

	public String getTessjxq2() {
		return tessjxq2;
	}

	public void setTessjxq2(String tessjxq2) {
		this.tessjxq2 = tessjxq2;
	}

	public String getTessjxs2() {
		return tessjxs2;
	}

	public void setTessjxs2(String tessjxs2) {
		this.tessjxs2 = tessjxs2;
	}

	public String getTessjxq3() {
		return tessjxq3;
	}

	public void setTessjxq3(String tessjxq3) {
		this.tessjxq3 = tessjxq3;
	}

	public String getTessjxs3() {
		return tessjxs3;
	}

	public void setTessjxs3(String tessjxs3) {
		this.tessjxs3 = tessjxs3;
	}
	
}
