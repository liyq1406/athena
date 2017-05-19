package com.athena.xqjs.entity.ppl;

import com.toft.core3.support.PageableSupport;
/*
 * 年度预告bean
 * author   夏晖
 * date     2011-12-7
 */
public class Niandyg extends PageableSupport{
	private static final long serialVersionUID = 1L;
	  private String id;           //主键
	  private String pplbc       ;//版次编号
	  private String ppllx       ;//PPl类型     
	  private String maoxqbc     ;//毛需求版次  
	  private String xuqcfsj     ;//需求拆分时间
	  private String jihydm      ;//计划员代码  
	  private String jisnf       ;//计算年份    
	  private Integer zhuangt    ;//状态 
	  private String  creator    ;//创建人
	  private String create_time ;//创建时间    
	  private String jissj       ;//计算时间    
	  private String beiz        ;//备注        
	  private String shengxsj    ;//生效时间    
	  private String fassj       ;//发送时间
	  private String editor      ;//维护人
	  private String edit_time   ;//维护时间
      private String active;      //删除标识
      private String edittime;    //新修改时间
	private String xuqly; // 需求来源

	public String getXuqly() {
		return xuqly;
	}

	public void setXuqly(String xuqly) {
		this.xuqly = xuqly;
	}

	public String getNeweditor() {
		return neweditor;
	}
	public void setNeweditor(String neweditor) {
		this.neweditor = neweditor;
	}
	private String  neweditor;  //新修改人
      public String getId() {
  		return id;
  	}
  	public void setId(String id) {
  		this.id = id;
  	}
      public String getEdittime() {
		return edittime;
	}
	public void setEdittime(String edittime) {
		this.edittime = edittime;
	}
	public String getActive() {
  		return active;
  	}
  	public void setActive(String active) {
  		this.active = active;
  	}
	  public String getCreator() {
			return creator;
		}
		public void setCreator(String creator) {
			this.creator = creator;
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
	
	  public String getPplbc() {
		return pplbc;
	}
	public void setPplbc(String pplbc) {
		this.pplbc = pplbc;
	}
	public String getPpllx() {
		return ppllx;
	}
	public void setPpllx(String ppllx) {
		this.ppllx = ppllx;
	}
	public String getMaoxqbc() {
		return maoxqbc;
	}
	public void setMaoxqbc(String maoxqbc) {
		this.maoxqbc = maoxqbc;
	}
	public String getXuqcfsj() {
		return xuqcfsj;
	}
	public void setXuqcfsj(String xuqcfsj) {
		this.xuqcfsj = xuqcfsj;
	}
	public String getJihydm() {
		return jihydm;
	}
	public void setJihydm(String jihydm) {
		this.jihydm = jihydm;
	}
	public String getJisnf() {
		return jisnf;
	}
	public void setJisnf(String jisnf) {
		this.jisnf = jisnf;
	}
	public Integer getZhuangt() {
		return zhuangt;
	}
	public void setZhuangt(Integer zhuangt) {
		this.zhuangt = zhuangt;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getJissj() {
		return jissj;
	}
	public void setJissj(String jissj) {
		this.jissj = jissj;
	}
	public String getBeiz() {
		return beiz;
	}
	public void setBeiz(String beiz) {
		this.beiz = beiz;
	}
	public String getShengxsj() {
		return shengxsj;
	}
	public void setShengxsj(String shengxsj) {
		this.shengxsj = shengxsj;
	}
	public String getFassj() {
		return fassj;
	}
	public void setFassj(String fassj) {
		this.fassj = fassj;
	}
	    

}
