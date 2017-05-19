package com.athena.ckx.entity.xuqjs;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 单位换算
 * @author qizhongtao
 * 2012-4-17
 */
public class Danwhs extends PageableSupport implements Domain{

	private static final long serialVersionUID = 2247474782222903090L;
	
	private String usercenter;      //用户中心
	
	private String beihdw;          //被换算单位编号
	
	private String mubdw;           //换算目标单位编号
	
	private Double huansbl;         //换算比例
	
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
	public String getBeihdw() {
		return beihdw;
	}
	public void setBeihdw(String beihdw) {
		this.beihdw = beihdw;
	}
	public String getMubdw() {
		return mubdw;
	}
	public void setMubdw(String mubdw) {
		this.mubdw = mubdw;
	}
	public Double getHuansbl() {
		return huansbl;
	}
	public void setHuansbl(Double huansbl) {
		this.huansbl = huansbl;
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
