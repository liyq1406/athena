package com.athena.truck.entity;

import java.util.Map;

import com.athena.component.entity.Domain;

import com.toft.core3.support.PageableSupport;
/**
 * 实际大站台
 * @author chenpeng
 * @String 2015-1-7
 */
public class Shijdzt extends PageableSupport implements Domain{

	private static final long serialVersionUID = -2057102654650434035L;

	private String usercenter   ;//  用户中心 
	
	private String quybh        ;//区域编号
	
	private String daztbh       ;//大站台编号
	
	private String daztmc		;//大站台名称
	
	private Integer paidtqqsx    ;//排队提前期上限（分钟）
	
	private Integer paidtqqxx    ;//排队提前期下限（分钟）
	
	private String duiycmqy		 ;//对应出门区域
	
	private Integer kacwdbjsj	 ;//卡车未到报警时间
	
	private Integer fangkbzsj	 ;//车辆放空报警时间
	
	private String biaos        ;//标识
	
	private String yuanbiaos        ;//原标识
	
	private String creator;       //创建人
	
	private String create_time;   //创建时间
	
	private String editor;        //修改人
	
	private String edit_time;      //修改时间

	private Map params	;
	
	private String shenbtqsj;	//申报提前时间
	

	public String getShenbtqsj() {
		return shenbtqsj;
	}


	public void setShenbtqsj(String shenbtqsj) {
		this.shenbtqsj = shenbtqsj;
	}


	public String getYuanbiaos() {
		return yuanbiaos;
	}


	public void setYuanbiaos(String yuanbiaos) {
		this.yuanbiaos = yuanbiaos;
	}


	public String getUsercenter() {
		return usercenter;
	}


	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}


	public String getQuybh() {
		return quybh;
	}


	public void setQuybh(String quybh) {
		this.quybh = quybh;
	}


	public String getDaztbh() {
		return daztbh;
	}


	public void setDaztbh(String daztbh) {
		this.daztbh = daztbh;
	}


	public String getDaztmc() {
		return daztmc;
	}


	public void setDaztmc(String daztmc) {
		this.daztmc = daztmc;
	}


	public Integer getPaidtqqsx() {
		return paidtqqsx;
	}


	public void setPaidtqqsx(Integer paidtqqsx) {
		this.paidtqqsx = paidtqqsx;
	}


	public Integer getPaidtqqxx() {
		return paidtqqxx;
	}


	public void setPaidtqqxx(Integer paidtqqxx) {
		this.paidtqqxx = paidtqqxx;
	}


	public String getBiaos() {
		return biaos;
	}


	public void setBiaos(String biaos) {
		this.biaos = biaos;
	}


	
	public String getDuiycmqy() {
		return duiycmqy;
	}


	public void setDuiycmqy(String duiycmqy) {
		this.duiycmqy = duiycmqy;
	}


	public Integer getKacwdbjsj() {
		return kacwdbjsj;
	}


	public void setKacwdbjsj(Integer kacwdbjsj) {
		this.kacwdbjsj = kacwdbjsj;
	}


	public Integer getFangkbzsj() {
		return fangkbzsj;
	}


	public void setFangkbzsj(Integer fangkbzsj) {
		this.fangkbzsj = fangkbzsj;
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

	


	public Map getParams() {
		return params;
	}


	public void setParams(Map params) {
		this.params = params;
	}


	public void setId(String id) {
		
	}


	public String getId() {
		return null;
	}
	
}
