package com.athena.print.entity.sys;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 打印控制
 * @author wangyu
 * @date 2012-1-12
 */
public class PrintControl extends PageableSupport implements Domain{

	private static final long serialVersionUID = 2892190665367165024L;

	private String usercenter;	//用户中心
	
	private String cangkbh;		//仓库编号
	
	private String danjbh;		//单据编号
	
	private String jiaoym;	//交易码
	
	private String biaos;		//标识
	
	private String creator;		//增加人
	
	private String create_time;	//增加时间
	
	private String editor;		//修改人
	
	private String edit_time;	//修改时间
	
	
	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getCangkbh() {
		return cangkbh;
	}

	public void setCangkbh(String cangkbh) {
		this.cangkbh = cangkbh;
	}

	public String getDanjbh() {
		return danjbh;
	}

	public void setDanjbh(String danjbh) {
		this.danjbh = danjbh;
	}

	public String getJiaoym() {
		return jiaoym;
	}

	public void setJiaoym(String jiaoym) {
		this.jiaoym = jiaoym;
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

	public void setId(String id) {
		// TODO Auto-generated method stub
		
	}

	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

}
