package com.athena.ckx.entity.paicfj;

import java.util.Date;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

public class Ckx_kehb extends PageableSupport implements Domain {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void setId(String id) {

	}

	public String getId() {
		return null;
	}


	private String kehbh;                     //客户编号
	private String kehmc;                    //客户名称
	private String kehxz;                   //客户性质（内部0|外部1）
	private String lianxr;                 //联系人  
	private String diz;	                  //地址
	private String youzbh;               //邮政编号
	private String dianh;               //电话
	private String chuanz;             //传真
	private String beiz;              //备注
	private String biaos;           //标识
	private String creator;         //创建人
	private Date create_time;      //创建时间
	private String editor;        //修改人
	private Date edit_time;      //修改时间
	
	public String getKehbh() {
		return kehbh;
	}

	public void setKehbh(String kehbh) {
		this.kehbh = kehbh;
	}

	public String getKehmc() {
		return kehmc;
	}

	public void setKehmc(String kehmc) {
		this.kehmc = kehmc;
	}

	public String getKehxz() {
		return kehxz;
	}

	public void setKehxz(String kehxz) {
		this.kehxz = kehxz;
	}

	public String getLianxr() {
		return lianxr;
	}

	public void setLianxr(String lianxr) {
		this.lianxr = lianxr;
	}

	public String getDiz() {
		return diz;
	}

	public void setDiz(String diz) {
		this.diz = diz;
	}

	public String getYouzbh() {
		return youzbh;
	}

	public void setYouzbh(String youzbh) {
		this.youzbh = youzbh;
	}

	public String getDianh() {
		return dianh;
	}

	public void setDianh(String dianh) {
		this.dianh = dianh;
	}

	public String getChuanz() {
		return chuanz;
	}

	public void setChuanz(String chuanz) {
		this.chuanz = chuanz;
	}

	public String getBeiz() {
		return beiz;
	}

	public void setBeiz(String beiz) {
		this.beiz = beiz;
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

	public String getBiaos() {
		return biaos;
	}

	public void setBiaos(String biaos) {
		this.biaos = biaos;
	}

	

	
}
