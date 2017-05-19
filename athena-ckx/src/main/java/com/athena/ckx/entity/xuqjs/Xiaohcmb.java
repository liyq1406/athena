package com.athena.ckx.entity.xuqjs;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 小火车运输时刻模板
 * @author denggq
 * @date 2012-4-11
 */
public class Xiaohcmb extends PageableSupport implements Domain{

	private static final long serialVersionUID = 2892190665367165024L;

	private String usercenter;	//用户中心
	
	private String shengcxbh;	//生产线编号
	
	private String xiaohcbh;	//小火车编号
	
	private Integer tangc;		//趟次
	
	private Integer beihpysj;	//备货偏移时间(分钟)
	
	private Integer shangxpysj;	//上线距备货偏移时间(分钟)
	
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

	public String getShengcxbh() {
		return shengcxbh;
	}

	public String getXiaohcbh() {
		return xiaohcbh;
	}

	public void setShengcxbh(String shengcxbh) {
		this.shengcxbh = shengcxbh;
	}

	public Integer getTangc() {
		return tangc;
	}

	public Integer getBeihpysj() {
		return beihpysj;
	}

	public Integer getShangxpysj() {
		return shangxpysj;
	}

	public void setTangc(Integer tangc) {
		this.tangc = tangc;
	}

	public void setBeihpysj(Integer beihpysj) {
		this.beihpysj = beihpysj;
	}

	public void setShangxpysj(Integer shangxpysj) {
		this.shangxpysj = shangxpysj;
	}

	public void setXiaohcbh(String xiaohcbh) {
		this.xiaohcbh = xiaohcbh;
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
