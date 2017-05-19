package com.athena.ckx.entity.xuqjs;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 制造路线转换
 * @author qizhongtao
 * 2012-4-17
 */
public class Zhizlxzh extends PageableSupport implements Domain{
		
	private static final long serialVersionUID = -9093183413606294194L;
	
	private String usercenter;      //用户中心
	
	private String zhizlxy;         //原制造路线
	
	private String zhizlxx;         //现制造路线
	
	private String creator;         //创建人
	
	private String create_time;     //创建时间
	
	private String editor;          //修改人
	
	private String edit_time;       //修改时间
	
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	public String getZhizlxy() {
		return zhizlxy;
	}
	public void setZhizlxy(String zhizlxy) {
		this.zhizlxy = zhizlxy;
	}
	public String getZhizlxx() {
		return zhizlxx;
	}
	public void setZhizlxx(String zhizlxx) {
		this.zhizlxx = zhizlxx;
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
