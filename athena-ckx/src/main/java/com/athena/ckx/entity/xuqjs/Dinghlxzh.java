package com.athena.ckx.entity.xuqjs;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;
/**
 * 订货类型转换
 * @author qizhongtao
 * @date 2012-4-05
 */
public class Dinghlxzh extends PageableSupport implements Domain{
	
	private static final long serialVersionUID = 2896638247040798741L;
	
	private String usercenter;  //用户中心
	
	private String lingjbh;     //零件编号
	
	private String zhizlx;      //制造路线
	
	private String dinghlx;     //订货路线
	
	private String creator;	    //创建人
	
	private String create_time; //创建时间
	
	private String editor;      //修改人
	
	private String edit_time;   //修改时间
	
	
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
	public String getZhizlx() {
		return zhizlx;
	}
	public void setZhizlx(String zhizlx) {
		this.zhizlx = zhizlx;
	}
	public String getDinghlx() {
		return dinghlx;
	}
	public void setDinghlx(String dinghlx) {
		this.dinghlx = dinghlx;
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
		// TODO Auto-generated method stub
		
	}
	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}
}
