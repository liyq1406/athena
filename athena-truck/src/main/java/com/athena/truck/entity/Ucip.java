package com.athena.truck.entity;

import java.util.Date;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 用户中心坐标IP
 * @author CSY
 * @date   20160907
 *
 */
public class Ucip extends PageableSupport implements Domain{

	private static final long serialVersionUID = 3196732868065228459L;
	
	private String usercenter;	//用户中心
	private String longitude;	//经度
	private String latitude;	//纬度
	private String ip;			//ip地址
	private String biaos;		//标识 0-无效 1-有效
	private String creator;		//创建人
	private Date create_time;	//创建时间
	private String editor;		//修改人
	private Date edit_time;		//修改时间
	private String beiz1;		//备注1
	private String beiz2;		//备注2
	private String beiz3;		//备注3
	private Date beiz4;			//备注4
	private int beiz5;			//备注5
	private String ucname;		//厂名
	private String shenbjl;		//申报距离
	
	public String getShenbjl() {
		return shenbjl;
	}
	public void setShenbjl(String shenbjl) {
		this.shenbjl = shenbjl;
	}
	public String getUcname() {
		return ucname;
	}
	public void setUcname(String ucname) {
		this.ucname = ucname;
	}
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
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
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public String getEditor() {
		return editor;
	}
	public void setEditor(String editor) {
		this.editor = editor;
	}
	public Date getEdit_time() {
		return edit_time;
	}
	public void setEdit_time(Date edit_time) {
		this.edit_time = edit_time;
	}
	public String getBeiz1() {
		return beiz1;
	}
	public void setBeiz1(String beiz1) {
		this.beiz1 = beiz1;
	}
	public String getBeiz2() {
		return beiz2;
	}
	public void setBeiz2(String beiz2) {
		this.beiz2 = beiz2;
	}
	public String getBeiz3() {
		return beiz3;
	}
	public void setBeiz3(String beiz3) {
		this.beiz3 = beiz3;
	}
	public Date getBeiz4() {
		return beiz4;
	}
	public void setBeiz4(Date beiz4) {
		this.beiz4 = beiz4;
	}
	public int getBeiz5() {
		return beiz5;
	}
	public void setBeiz5(int beiz5) {
		this.beiz5 = beiz5;
	}
	public String getId() {
		return null;
	}
	public void setId(String arg0) {
		
	}
	
}
