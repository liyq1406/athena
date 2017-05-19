package com.athena.truck.entity;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;
/**
 * 车位
 * @author wangliang
 * @String 2015-01-20
 */
public class Chew  extends PageableSupport implements Domain {

	private static final long serialVersionUID = 1L;
	
	
	private String usercenter; //用户中心
	private String chewbh;		//车位编号
	private String chewmc;		//车位名称
	private String quybh;		//区域编号
	private String chacbh;		
	private String chewxh;		//车位序号
	private String daztbh;		//大站台编号
	private String chewsx;		//车位属性
	private String chewzt;		//车位状态
	private String biaos;		//标识
	private String creator;		//创建人
	private String create_time;	//创建时间
	private String editor;		//修改人
	private String edit_time;	//修改时间
	private String chengysbh;	//承运商编号
	private String zhuangt;     //原车位状态
	
	public String getZhuangt() {
		return zhuangt;
	}

	public void setZhuangt(String zhuangt) {
		this.zhuangt = zhuangt;
	}

	public String getChengysbh() {
		return chengysbh;
	}

	public void setChengysbh(String chengysbh) {
		this.chengysbh = chengysbh;
	}

	public String getChacbh() {
		return chacbh;
	}

	public void setChacbh(String chacbh) {
		this.chacbh = chacbh;
	}

	public String getQuybh() {
		return quybh;
	}

	public void setQuybh(String quybh) {
		this.quybh = quybh;
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

	public String getChewmc() {
		return chewmc;
	}

	public void setChewmc(String chewmc) {
		this.chewmc = chewmc;
	}

	public String getChewxh() {
		return chewxh;
	}

	public void setChewxh(String chewxh) {
		this.chewxh = chewxh;
	}

	public String getDaztbh() {
		return daztbh;
	}

	public void setDaztbh(String daztbh) {
		this.daztbh = daztbh;
	}

	public String getChewsx() {
		return chewsx;
	}

	public void setChewsx(String chewsx) {
		this.chewsx = chewsx;
	}

	public String getChewzt() {
		return chewzt;
	}

	public void setChewzt(String chewzt) {
		this.chewzt = chewzt;
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
