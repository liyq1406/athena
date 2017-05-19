package com.athena.truck.entity;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;
/**
 * 车位承运商
 * @author wangliag
 * @String 2015-01-20
 */
public class ChewChengys extends PageableSupport implements Domain{

	private static final long serialVersionUID = 1L;
	
	private String usercenter;	//用户中心
	private String chewbh;		//车位编号
	private String daztbh;		//大站台编号
	private String chengysbh;	//承运商编号
	private String gongsmc;		//供应商名称	
	private String biaos;		//标识
	private String creator;		//创建人
	private String creat_time;	//创建时间	
	private String editor;		//修改人
	private String edit_time;	//修改时间
	private String quybh; //区域编号
	private String chewsx; //车位属性
	
	
	
	
	

	
	public String getChewsx() {
		return chewsx;
	}

	public void setChewsx(String chewsx) {
		this.chewsx = chewsx;
	}

	public String getQuybh() {
		return quybh;
	}

	public void setQuybh(String quybh) {
		this.quybh = quybh;
	}

	public String getGongsmc() {
		return gongsmc;
	}

	public void setGongsmc(String gongsmc) {
		this.gongsmc = gongsmc;
	}

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getChewbh() {
		return chewbh;
	}

	public void setChewbh(String chewbh) {
		this.chewbh = chewbh;
	}

	public String getDaztbh() {
		return daztbh;
	}

	public void setDaztbh(String daztbh) {
		this.daztbh = daztbh;
	}

	public String getChengysbh() {
		return chengysbh;
	}

	public void setChengysbh(String chengysbh) {
		this.chengysbh = chengysbh;
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

	public String getCreat_time() {
		return creat_time;
	}

	public void setCreat_time(String creat_time) {
		this.creat_time = creat_time;
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

	public void setId(String id) {
		// TODO Auto-generated method stub
		
	}

	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

}
