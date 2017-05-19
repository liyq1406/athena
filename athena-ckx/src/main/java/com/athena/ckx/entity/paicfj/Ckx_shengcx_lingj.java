package com.athena.ckx.entity.paicfj;

import java.util.Date;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

public class Ckx_shengcx_lingj extends PageableSupport implements Domain {

	/**
	 * 
	 */
	private static final long serialVersionUID = 122222222222222L;

	public void setId(String id) {

	}

	public String getId() {
		return null;
	}

	private String usercenter;         //用户中心
	private String shengcxbh;          //生产线编号
	private String lingjbh;            //零件编号
	private String zhuxfx;			   //主线辅线(0,1,2,3,4,5)
	private Double shengcbl;           //生产比例（%）
//	private Double shengcjp;           //生产节拍
	private String shifqyjjpl;         //是否启用经济批量
	private Double jingjpl;            //经济批量
	private String youxbc;             //优先班次
	private String zhizlx;          //制造路线
	private String cangkbh;  //仓库编号
	private String creator;            //创建人
	private Date create_time;        //创建时间
	private String editor;             //修改人
	private Date edit_time;          //修改时间
	private String lingjlx;				//零件类型


	public String getZhuxfx() {
		return zhuxfx;
	}

	public void setZhuxfx(String zhuxfx) {
		this.zhuxfx = zhuxfx;
	}

	public String getCangkbh() {
		return cangkbh;
	}

	public void setCangkbh(String cangkbh) {
		this.cangkbh = cangkbh;
	}

	public String getZhizlx() {
		return zhizlx;
	}

	public void setZhizlx(String zhizlx) {
		this.zhizlx = zhizlx;
	}

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getShengcxbh() {
		return shengcxbh;
	}

	public void setShengcxbh(String shengcxbh) {
		this.shengcxbh = shengcxbh;
	}

	public String getLingjbh() {
		return lingjbh;
	}

	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}

	public Double getShengcbl() {
		return shengcbl;
	}

	public void setShengcbl(Double shengcbl) {
		this.shengcbl = shengcbl;
	}

//	public Double getShengcjp() {
//		return shengcjp;
//	}
//
//	public void setShengcjp(Double shengcjp) {
//		this.shengcjp = shengcjp;
//	}

	public String getShifqyjjpl() {
		return shifqyjjpl;
	}

	public void setShifqyjjpl(String shifqyjjpl) {
		this.shifqyjjpl = shifqyjjpl;
	}

	public Double getJingjpl() {
		return jingjpl;
	}

	public void setJingjpl(Double jingjpl) {
		this.jingjpl = jingjpl;
	}

	public String getYouxbc() {
		return youxbc;
	}

	public void setYouxbc(String youxbc) {
		this.youxbc = youxbc;
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

	public String getLingjlx() {
		return lingjlx;
	}

	public void setLingjlx(String lingjlx) {
		this.lingjlx = lingjlx;
	}
	
}
