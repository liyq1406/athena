package com.athena.xqjs.entity.diaobl;


import com.toft.core3.support.PageableSupport;
/**
 * <p>
 * 调拨申请实体
 * </p>
 * author   Niesy
 * date     2011-11-21
 */
public class Diaobsq extends PageableSupport{
	private static final long serialVersionUID = -6416990806688862783L;
	
	/**
	 * 用户中心
	 */
	private String usercenter = null; 
	
	/**
	 * 调拨申请单号 
	 */
	private String diaobsqdh = null;
	
	
	/**
	 * 调拨申请时间
	 */ 
	private String diaobsqsj = null;
	
	/**
	 * 版次
	 */ 
	private String banc = null;
	
	/**
	 * 会计科目
	 */ 
    private String huijkm = null;
    
    /**
	 * 成本中心
	 */
    private String chengbzx = null;
    
    /**
	 * 备注
	 */
	private String beiz = null;
	
	 /**
	 * 状态
	 */
	private String zhuangt = null;
	
	
	/* 类型-是否DFPV -0012338*/
	private String leix = null;
	
	
	/**
	 * 新状态
	 */
	private  String  newZhuangt = null;
	
	/**
	  * 创建时间
	  */
	 private String  create_time = null;
	 
	 /**
	  * 创建人
	  */
	 private String  creator = null; 
	 
	 /**
	  * 修改时间
	  */
	 private String  edit_time = null;
	 
	 /**
	  * 修改人
	  */
	 private String  editor = null;
     
	 /**
	  * 当前修改时间
	  */
	 private String  newEdit_time = null;
	 
	 /**
	  * 当前修改人
	  */
	 private String  newEditor = null;
	 
	 /**
		 * 标识
		 */
	private String active = null;
	 
	 /**
	  * 计划员组
	  */
	 private String   jihy = null;
	 
	 /**
	  * 零件编号供查询
	  */
	 private String   lingjbh = null;
	public String getLingjbh() {
		return lingjbh;
	}

	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getDiaobsqdh() {
		return diaobsqdh;
	}

	public void setDiaobsqdh(String diaobsqdh) {
		this.diaobsqdh = diaobsqdh;
	}

	

	public String getDiaobsqsj() {
		return diaobsqsj;
	}

	public void setDiaobsqsj(String diaobsqsj) {
		this.diaobsqsj = diaobsqsj;
	}

	public String getBanc() {
		return banc;
	}

	public void setBanc(String banc) {
		this.banc = banc;
	}

	public String getHuijkm() {
		return huijkm;
	}

	public void setHuijkm(String huijkm) {
		this.huijkm = huijkm;
	}

	public String getChengbzx() {
		return chengbzx;
	}

	public void setChengbzx(String chengbzx) {
		this.chengbzx = chengbzx;
	}

	public String getBeiz() {
		return beiz;
	}

	public void setBeiz(String beiz) {
		this.beiz = beiz;
	}

	public String getZhuangt() {
		return zhuangt;
	}

	public void setZhuangt(String zhuangt) {
		this.zhuangt = zhuangt;
	}

	public String getNewZhuangt() {
		return newZhuangt;
	}

	public void setNewZhuangt(String newZhuangt) {
		this.newZhuangt = newZhuangt;
	}
	

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getEdit_time() {
		return edit_time;
	}

	public void setEdit_time(String edit_time) {
		this.edit_time = edit_time;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getNewEdit_time() {
		return newEdit_time;
	}

	public String getJihy() {
		return jihy;
	}

	public void setJihy(String jihy) {
		this.jihy = jihy;
	}

	public void setNewEdit_time(String newEdit_time) {
		this.newEdit_time = newEdit_time;
	}

	public String getNewEditor() {
		return newEditor;
	}

	public void setNewEditor(String newEditor) {
		this.newEditor = newEditor;
	}
	
	public String getLeix() {
		return leix;
	}

	public void setLeix(String leix) {
		this.leix = leix;
	}
	
}
