package com.athena.ckx.entity.paicfj;

import java.util.Date;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

public class Ckx_keh_chengpk extends PageableSupport implements Domain {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void setId(String id) {

	}

	public String getId() {
		return null;
	}
	 private String  usercenter     ;//用户中心
	 private String  cangkbh        ;//仓库编号
	 private String  kehbh          ;//客户编号
	 private String  chengysbh      ;//承运商编号
	 private String  kehtqq         ;//客户提前期
	 private String  shifpz         ;//是否需要配载
	 private String  beihlx         ;//备货类型
	 private String  dingdtqq       ;//订单提前期
	 private String  creator        ;//创建人
	 private Date  create_time    ;//创建时间
	 private String  editor         ;//修改人
	 private Date  edit_time      ;//修改时间

	 private String yaoctqq;         //要车提前期
	 private String yunslxbh;        //运输路线编号
	 
	 
	 
	 private String shifupdate;    //是否更新 ，1：是|0：否
	 
	public String getShifupdate() {
		return shifupdate;
	}

	public void setShifupdate(String shifupdate) {
		this.shifupdate = shifupdate;
	}

	public String getYaoctqq() {
		return yaoctqq;
	}

	public void setYaoctqq(String yaoctqq) {
		this.yaoctqq = yaoctqq;
	}

	public String getYunslxbh() {
		return yunslxbh;
	}

	public void setYunslxbh(String yunslxbh) {
		this.yunslxbh = yunslxbh;
	}

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

	public String getKehbh() {
		return kehbh;
	}

	public void setKehbh(String kehbh) {
		this.kehbh = kehbh;
	}

	public String getChengysbh() {
		return chengysbh;
	}

	public void setChengysbh(String chengysbh) {
		this.chengysbh = chengysbh;
	}

	public String getKehtqq() {
		return kehtqq;
	}

	public void setKehtqq(String kehtqq) {
		this.kehtqq = kehtqq;
	}

	public String getShifpz() {
		return shifpz;
	}

	public void setShifpz(String shifpz) {
		this.shifpz = shifpz;
	}

	public String getBeihlx() {
		return beihlx;
	}

	public void setBeihlx(String beihlx) {
		this.beihlx = beihlx;
	}

	public String getDingdtqq() {
		return dingdtqq;
	}

	public void setDingdtqq(String dingdtqq) {
		this.dingdtqq = dingdtqq;
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
	 

}
