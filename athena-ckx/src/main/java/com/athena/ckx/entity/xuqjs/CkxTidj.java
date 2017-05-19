package com.athena.ckx.entity.xuqjs;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 替代件
 * @author qizhongtao
 * 2012-3-28
 */
public class CkxTidj extends PageableSupport implements Domain{

	private static final long serialVersionUID = -2930674423308970742L;

	private String usercenter;  //用户中心
	
	private String lingjbh;     //零件编号
	
	private String tidljh;      //替代零件号
	
	private String creator;     //创建人
	
	private String create_time; //创建时间
	
	private String editor;      //修改人
	
	private String edit_time;   //修改时间
	

	private String zuh;				//组号
	
	
	public void setZuh(String zuh) {
		this.zuh = zuh;
	}
	public String getZuh() {
		return zuh;
	}
	
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	public String getLingjbh() {
		return lingjbh;
	}
	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}
	public String getTidljh() {
		return tidljh;
	}
	public void setTidljh(String tidljh) {
		this.tidljh = tidljh;
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
