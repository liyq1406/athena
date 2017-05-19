package com.athena.xqjs.entity.maoxq;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

public class JLMaoxq extends PageableSupport implements Domain{

	private static final long serialVersionUID = -3545292223684562842L;
	
	private String usercenter;	//用户中心
	
	private String hanzbs		;//焊装总装标识
	
	private String lingjbh		;//零件编号
	
	private String danw			;//单位
	
	private String xiaohd		;//消耗点编号
	
	private String xiaohcbh		;//小火车编号
	
	private Integer tangc		;//趟次
	
	private String xiaohcrq		;//小火车日期
		
	private String xuqly		;//需求来源
	
	private String chejh		;//车间
	
	private String chanx		;//生产线编号
		
	private Double xiaohxs		;//消耗系数
	
	private String creator		;//创建人
	
	private String create_time	;//创建时间
	
	private String editor		;//修改人
	
	private String edit_time	;//修改时间
	
	private String xiaohcrqFrom	;
	
	private String xiaohcrqTo	;

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getLingjbh() {
		return lingjbh;
	}

	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}

	public String getDanw() {
		return danw;
	}

	public void setDanw(String danw) {
		this.danw = danw;
	}

	public String getXiaohd() {
		return xiaohd;
	}

	public void setXiaohd(String xiaohd) {
		this.xiaohd = xiaohd;
	}

	public String getXiaohcbh() {
		return xiaohcbh;
	}

	public void setXiaohcbh(String xiaohcbh) {
		this.xiaohcbh = xiaohcbh;
	}

	public Integer getTangc() {
		return tangc;
	}

	public void setTangc(Integer tangc) {
		this.tangc = tangc;
	}

	public String getXiaohcrq() {
		return xiaohcrq;
	}

	public void setXiaohcrq(String xiaohcrq) {
		this.xiaohcrq = xiaohcrq;
	}

	public String getXuqly() {
		return xuqly;
	}

	public void setXuqly(String xuqly) {
		this.xuqly = xuqly;
	}

	public Double getXiaohxs() {
		return xiaohxs;
	}

	public void setXiaohxs(Double xiaohxs) {
		this.xiaohxs = xiaohxs;
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

	public String getXiaohcrqFrom() {
		return xiaohcrqFrom;
	}

	public void setXiaohcrqFrom(String xiaohcrqFrom) {
		this.xiaohcrqFrom = xiaohcrqFrom;
	}

	public String getXiaohcrqTo() {
		return xiaohcrqTo;
	}

	public void setXiaohcrqTo(String xiaohcrqTo) {
		this.xiaohcrqTo = xiaohcrqTo;
	}

	public String getHanzbs() {
		return hanzbs;
	}

	public void setHanzbs(String hanzbs) {
		this.hanzbs = hanzbs;
	}

	public String getChejh() {
		return chejh;
	}

	public void setChejh(String chejh) {
		this.chejh = chejh;
	}

	public String getChanx() {
		return chanx;
	}

	public void setChanx(String chanx) {
		this.chanx = chanx;
	}

	@Override
	public void setId(String id) {
		
	}

	@Override
	public String getId() {
		return null;
	}
	
}
