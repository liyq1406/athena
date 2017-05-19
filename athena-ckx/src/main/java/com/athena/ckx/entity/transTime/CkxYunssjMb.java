package com.athena.ckx.entity.transTime;

import java.util.Date;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 运输时间模板
 * @author hj
 * @Date 2012-03-19
 */
public class CkxYunssjMb extends PageableSupport  implements Domain{
	

	private static final long serialVersionUID = -6790607714902341609L;
	private String usercenter;//用户中心
	private String gcbh;//承运商	
	private String xiehztbh;//卸货站台编号
	private Double facsj;//发出时间偏量
	private Double daohsj;     //到货时间偏量
	private String shengcsj;     //生成时间
	private Double xuh;   //序号
	private String creator;    //创建人
	private Date create_time;    //创建时间
	private String editor;//修改人
	private Date edit_time;//修改时间
	
	private String gongzrl;//卸货站台编号
	private String gongzsjbz;//卸货站台编号

	public String getGongzrl() {
		return gongzrl;
	}
	public void setGongzrl(String gongzrl) {
		this.gongzrl = gongzrl;
	}
	public String getGongzsjbz() {
		return gongzsjbz;
	}
	public void setGongzsjbz(String gongzsjbz) {
		this.gongzsjbz = gongzsjbz;
	}
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	public String getGcbh() {
		return gcbh;
	}
	public void setGcbh(String gcbh) {
		this.gcbh = gcbh;
	}
	public String getXiehztbh() {
		return xiehztbh;
	}
	public void setXiehztbh(String xiehztbh) {
		this.xiehztbh = xiehztbh;
	}
	public Double getFacsj() {
		return facsj;
	}
	public void setFacsj(Double facsj) {
		this.facsj = facsj;
	}
	public Double getDaohsj() {
		return daohsj;
	}
	public void setDaohsj(Double daohsj) {
		this.daohsj = daohsj;
	}
	public String getShengcsj() {
		return shengcsj;
	}
	public void setShengcsj(String shengcsj) {
		this.shengcsj = shengcsj;
	}
	public Double getXuh() {
		return xuh;
	}
	public void setXuh(Double xuh) {
		this.xuh = xuh;
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

	public void setId(String id) {
		// TODO Auto-generated method stub
		
	}

	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
