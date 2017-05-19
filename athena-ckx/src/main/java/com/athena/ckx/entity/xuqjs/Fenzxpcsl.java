package com.athena.ckx.entity.xuqjs;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 *分装线排产数量
 */
public class Fenzxpcsl extends PageableSupport implements Domain{

	private static final long serialVersionUID = 3714569212845433581L;
	
	private String usercenter	;//用户中心
	
	private String daxxh		;//大线线号
	
	private String fenzxh		;//分装线号
	
	private String riq			;//日期
	
	private int jihsxl			;//计划上线量
	
	private int jihxxl			;//计划下线量
	
	private String beiz			;//备注
	
	private String creator		;//创建人
	
	private String create_time	;//创建时间
	
	private String editor		;//修改人
	
	private String edit_time	;//修改时间
	
	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getDaxxh() {
		return daxxh;
	}

	public void setDaxxh(String daxxh) {
		this.daxxh = daxxh;
	}

	public String getFenzxh() {
		return fenzxh;
	}

	public void setFenzxh(String fenzxh) {
		this.fenzxh = fenzxh;
	}

	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		this.riq = riq;
	}

	public int getJihsxl() {
		return jihsxl;
	}

	public void setJihsxl(int jihsxl) {
		this.jihsxl = jihsxl;
	}

	public int getJihxxl() {
		return jihxxl;
	}

	public void setJihxxl(int jihxxl) {
		this.jihxxl = jihxxl;
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

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getEdit_time() {
		return edit_time;
	}

	public void setEdit_time(String edit_time) {
		this.edit_time = edit_time;
	}

	@Override
	public void setId(String id) {
		
	}

	@Override
	public String getId() {
		return null;
	}

}
