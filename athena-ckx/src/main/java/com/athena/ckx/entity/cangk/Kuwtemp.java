package com.athena.ckx.entity.cangk;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 库位
 * @author denggq
 * @date 2012-2-8
 */
public class Kuwtemp extends PageableSupport implements Domain{

	private static final long serialVersionUID = 8160483530215429946L;

	private String usercenter;	//用户中心
	
	private String cangkbh;		//仓库编号
	
	private String zickbh;		//仓库编号
	
	private String kuwdjbh;		//库位等级编号
	
	private String dingykwgs;    //定义库位个数
	
	private String creator;		//创建人
	
	private String create_time;	//创建时间
	
	private String editor;		//修改人
	
	private String edit_time;	//修改时间

	

	public String getZickbh() {
		return zickbh;
	}

	public void setZickbh(String zickbh) {
		this.zickbh = zickbh;
	}

	public String getDingykwgs() {
		return dingykwgs;
	}

	public void setDingykwgs(String dingykwgs) {
		this.dingykwgs = dingykwgs;
	}


	public String getKuwdjbh() {
		return kuwdjbh;
	}

	public void setKuwdjbh(String kuwdjbh) {
		this.kuwdjbh = kuwdjbh;
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

	public void setId(String id) {
		
	}

	public String getId() {
		return null;
	}


}
