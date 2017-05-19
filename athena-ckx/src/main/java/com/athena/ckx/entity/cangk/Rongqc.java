package com.athena.ckx.entity.cangk;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 容器场
 * @author denggq
 * @date 2012-2-2
 * @modify 2012-2-18
 */
public class Rongqc extends PageableSupport implements Domain{

	private static final long serialVersionUID = -4404892638962030092L;

	private String usercenter;	//用户中心
	
	private String rongqcbh;	//空容器场编号
	
	private String jizqbh;	//空容器场编号
	
	private String shiffk;	//空容器场编号
	
	private String miaos;		//描述
	
	private String biaos;		//标识

	private String creator;		//增加人
	
	private String create_time;	//增加时间
	
	private String editor;		//修改人
	
	private String edit_time;	//修改时间
	
	private String ycshiffk;	//修改时间

	
	
	
	public String getYcshiffk() {
		return ycshiffk;
	}

	public void setYcshiffk(String ycshiffk) {
		this.ycshiffk = ycshiffk;
	}

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getRongqcbh() {
		return rongqcbh;
	}

	
	public String getJizqbh() {
		return jizqbh;
	}

	public void setJizqbh(String jizqbh) {
		this.jizqbh = jizqbh;
	}

	public String getShiffk() {
		return shiffk;
	}

	public void setShiffk(String shiffk) {
		this.shiffk = shiffk;
	}

	public void setRongqcbh(String rongqcbh) {
		this.rongqcbh = rongqcbh;
	}

	public String getMiaos() {
		return miaos;
	}

	public void setMiaos(String miaos) {
		this.miaos = miaos;
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
		
	}

	public String getId() {
		return null;
	}

	
	
}
