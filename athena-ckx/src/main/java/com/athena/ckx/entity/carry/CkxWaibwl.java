package com.athena.ckx.entity.carry;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;


/**
 * 外部物流路径
 * @author kong
 *
 */
public class CkxWaibwl extends PageableSupport  implements Domain{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3918459081188893313L;
	private String lujbh;
	private String lujmc;
	private String usercenter;
	private String gongysbh;
	private String jiaofm;
	private String fahd;
	private String mudd;
	private Double beihzq;
	private Double panysj;
	private String creator;//创建人
	private String createTime;
	private String editor;
	private String editTime;
	
	private String xiehztbz; //卸货站台编组
	private String chengysbh; //承运商编号
	private String  beiz;     
	private String  beiz1;
	
	
	public String getBeiz() {
		return beiz;
	}
	public void setBeiz(String beiz) {
		this.beiz = beiz;
	}
	public String getBeiz1() {
		return beiz1;
	}
	public void setBeiz1(String beiz1) {
		this.beiz1 = beiz1;
	}
	public String getChengysbh() {
		return chengysbh;
	}
	public void setChengysbh(String chengysbh) {
		this.chengysbh = chengysbh;
	}
	public String getXiehztbz() {
		return xiehztbz;
	}
	public void setXiehztbz(String xiehztbz) {
		this.xiehztbz = xiehztbz;
	}
	public String getJiaofm() {
		return jiaofm;
	}
	public void setJiaofm(String jiaofm) {
		this.jiaofm = jiaofm;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getEditor() {
		return editor;
	}
	public void setEditor(String editor) {
		this.editor = editor;
	}
	public String getEditTime() {
		return editTime;
	}
	public void setEditTime(String editTime) {
		this.editTime = editTime;
	}
	public String getLujbh() {
		return lujbh;
	}
	public void setLujbh(String lujbh) {
		this.lujbh = lujbh;
	}
	public String getLujmc() {
		return lujmc;
	}
	public void setLujmc(String lujmc) {
		this.lujmc = lujmc;
	}
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	public String getGongysbh() {
		return gongysbh;
	}
	public void setGongysbh(String gongysbh) {
		this.gongysbh = gongysbh;
	}
	public String getFahd() {
		return fahd;
	}
	public void setFahd(String fahd) {
		this.fahd = fahd;
	}
	public String getMudd() {
		return mudd;
	}
	public void setMudd(String mudd) {
		this.mudd = mudd;
	}

	public Double getBeihzq() {
		return beihzq;
	}
	public void setBeihzq(Double beihzq) {
		this.beihzq = beihzq;
	}

	public Double getPanysj() {
		return panysj;
	}
	public void setPanysj(Double panysj) {
		this.panysj = panysj;
	}
	public void setId(String id) {
		// TODO Auto-generated method stub
		
	}
	public String getId() {
		// TODO Auto-generated method stub
		return "";
	}
	
}
