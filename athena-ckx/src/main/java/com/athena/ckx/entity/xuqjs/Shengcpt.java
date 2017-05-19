package com.athena.ckx.entity.xuqjs;

import com.athena.component.entity.Domain;

import com.toft.core3.support.PageableSupport;
/**
 * 生产平台
 * @author denggq
 * @String 2012-3-28
 */
public class Shengcpt extends PageableSupport implements Domain{

	private static final long serialVersionUID = 462812396261385771L;

	private String usercenter   ;//  用户中心 
	
	private String shengcxbh    ;//  生产线编号
	
	private String shengcptbh   ;//  生产平台编号
	
	private String shengcjp     ;//  整车生产节拍
	
	private String weilscjp     ;//  整车未来生产节拍
	
	private String qiehsj		;//  整车生产节拍切换时间
	
	private String biaos;		//标识
	
	private String creator;       //创建人
	
	private String create_time;   //创建时间
	
	private String editor;        //修改人
	
	private String edit_time;      //修改时间


	public String getUsercenter() {
		return usercenter;
	}


	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}


	public String getShengcxbh() {
		return shengcxbh;
	}


	public void setShengcxbh(String shengcxbh) {
		this.shengcxbh = shengcxbh;
	}


	public String getShengcjp() {
		return shengcjp;
	}


	public void setShengcjp(String shengcjp) {
		this.shengcjp = shengcjp;
	}


	public String getWeilscjp() {
		return weilscjp;
	}


	public void setWeilscjp(String weilscjp) {
		this.weilscjp = weilscjp;
	}


	public void setQiehsj(String qiehsj) {
		this.qiehsj = qiehsj;
	}


	public String getQiehsj() {
		return qiehsj;
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


	public void setShengcptbh(String shengcptbh) {
		this.shengcptbh = shengcptbh;
	}


	public String getShengcptbh() {
		return shengcptbh;
	}


	@Override
	public void setId(String id) {
		
	}


	@Override
	public String getId() {
		return null;
	}


	public void setBiaos(String biaos) {
		this.biaos = biaos;
	}


	public String getBiaos() {
		return biaos;
	}
	

	
	
	
}
