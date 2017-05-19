package com.athena.ckx.entity.xuqjs;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 产线合并
 * @author qizhongtao
 * 2012-4-17
 */
public class Chanxhb extends PageableSupport implements Domain{

	private static final long serialVersionUID = 4565346885837612830L;
	
	private String usercenter;     //用户中心
	
	private String yuancx;         //原产线
	
	private String mubcx;          //目标产线
	
	private String creator;        //创建人
	
	private String create_time;    //创建时间
	
	private String editor;         //修改人
	
	private String edit_time;      //修改时间
	
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	public String getYuancx() {
		return yuancx;
	}
	public void setYuancx(String yuancx) {
		this.yuancx = yuancx;
	}
	public String getMubcx() {
		return mubcx;
	}
	public void setMubcx(String mubcx) {
		this.mubcx = mubcx;
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
