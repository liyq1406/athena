package com.athena.ckx.entity.xuqjs;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 资源获取日历
 * @author qizhongtao
 * 2012-4-16
 */
public class Ziyhqrl extends PageableSupport implements Domain{

	private static final long serialVersionUID = 8289884871037854510L;
	
	private String ziyhqrq;       //资源获取日期
	
	private String creator;       //创建人
	
	private String create_time;   //创建时间
	
	private String editor;        //修改人
	
	private String edit_time;     //修改时间
	
	public String getZiyhqrq() {
		return ziyhqrq;
	}
	public void setZiyhqrq(String ziyhqrq) {
		this.ziyhqrq = ziyhqrq;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Override
	public void setId(String id) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
