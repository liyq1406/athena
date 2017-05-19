package com.athena.ckx.entity.carry;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 仓库循环时间
 * @author kong
 *
 */
public class CkxCangkxhsj extends PageableSupport  implements Domain{

	private static final long serialVersionUID = -7806263175923774742L;
	private String usercenter;//用户中心
	private String cangkbh;//6位仓库编号
	private String fenpqhck;
	private String fenpqhckLength;
	private String mos;
	private Integer cangkshpcf;
	private Double cangkshsj;
	private Double cangkfhsj;
	private Double beihsj;
	private Double ibeihsj;
	private Double pbeihsj;
	private String shifzdbh;
	private String shengxbs;
	private String creator;//创建人
	private String createTime;
	private String editor;
	private String editTime;
	private String wulgyyz;//物流工艺员数据查询权限
	
	
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
	public String getFenpqhck() {
		return fenpqhck;
	}
	public void setFenpqhck(String fenpqhck) {
		this.fenpqhck = fenpqhck;
	}
	public String getMos() {
		return mos;
	}
	public void setMos(String mos) {
		this.mos = mos;
	}
	public Integer getCangkshpcf() {
		return cangkshpcf;
	}
	public void setCangkshpcf(Integer cangkshpcf) {
		this.cangkshpcf = cangkshpcf;
	}
	public Double getCangkshsj() {
		return cangkshsj;
	}
	public void setCangkshsj(Double cangkshsj) {
		this.cangkshsj = cangkshsj;
	}
	public Double getCangkfhsj() {
		return cangkfhsj;
	}
	public void setCangkfhsj(Double cangkfhsj) {
		this.cangkfhsj = cangkfhsj;
	}
	public Double getBeihsj() {
		return beihsj;
	}
	public void setBeihsj(Double beihsj) {
		this.beihsj = beihsj;
	}
	public Double getIbeihsj() {
		return ibeihsj;
	}
	public void setIbeihsj(Double ibeihsj) {
		this.ibeihsj = ibeihsj;
	}
	public Double getPbeihsj() {
		return pbeihsj;
	}
	public void setPbeihsj(Double pbeihsj) {
		this.pbeihsj = pbeihsj;
	}
	public String getShifzdbh() {
		return shifzdbh;
	}
	public void setShifzdbh(String shifzdbh) {
		this.shifzdbh = shifzdbh;
	}
	public String getShengxbs() {
		return shengxbs;
	}
	public void setShengxbs(String shengxbs) {
		this.shengxbs = shengxbs;
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
	public void setWulgyyz(String wulgyyz) {
		this.wulgyyz = wulgyyz;
	}
	public String getWulgyyz() {
		return wulgyyz;
	}
	public void setFenpqhckLength(String fenpqhckLength) {
		this.fenpqhckLength = fenpqhckLength;
	}
	public String getFenpqhckLength() {
		return fenpqhckLength;
	}
	public void setId(String id) {
		// TODO Auto-generated method stub
		
	}
	public String getId() {
		// TODO Auto-generated method stub
		return "";
	}
}
