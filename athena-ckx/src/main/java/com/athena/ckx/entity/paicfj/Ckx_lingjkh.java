package com.athena.ckx.entity.paicfj;



import java.util.Date;


import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

public class Ckx_lingjkh extends PageableSupport implements Domain {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void setId(String id) {

	}

	public String getId() {
		return null;
	}

	private String  usercenter      ;//用户中心      
	private String  lingjbh         ;// 零件编号     
	private String  kehbh           ;// 客户编号     
 
	private String  kehljh          ;// 客户零件号   
	private String  kehljmc         ;// 客户零件名称 
	private String  fahzt           ;// 发货站台     
	private String  uabaoz          ;// 零件包装UA   
	private String  ualjsl          ;// UA零件数量   
	private String  ucbaoz          ;// 零件包装UC   
	private String  ucljsl          ;// UC零件数量   
	private String  shengxrq        ;//生效日期      
	private String  shixrq          ;//失效日期      
	private String  creator         ;//创建人        
	private Date  create_time       ;//创建时间      
	private String  editor          ;//修改人        
	private Date  edit_time         ;//修改时间      

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

	public String getKehbh() {
		return kehbh;
	}

	public void setKehbh(String kehbh) {
		this.kehbh = kehbh;
	}

	
	public String getKehljh() {
		return kehljh;
	}

	public void setKehljh(String kehljh) {
		this.kehljh = kehljh;
	}

	public String getKehljmc() {
		return kehljmc;
	}

	public void setKehljmc(String kehljmc) {
		this.kehljmc = kehljmc;
	}

	public String getFahzt() {
		return fahzt;
	}

	public void setFahzt(String fahzt) {
		this.fahzt = fahzt;
	}

	public String getUabaoz() {
		return uabaoz;
	}

	public void setUabaoz(String uabaoz) {
		this.uabaoz = uabaoz;
	}

	public String getUaljsl() {
		return ualjsl;
	}

	public void setUaljsl(String ualjsl) {
		this.ualjsl = ualjsl;
	}

	public String getUcbaoz() {
		return ucbaoz;
	}

	public void setUcbaoz(String ucbaoz) {
		this.ucbaoz = ucbaoz;
	}

	public String getUcljsl() {
		return ucljsl;
	}

	public void setUcljsl(String ucljsl) {
		this.ucljsl = ucljsl;
	}

	public String getShengxrq() {
		return shengxrq;
	}

	public void setShengxrq(String shengxrq) {
		this.shengxrq = shengxrq;
	}

	public String getShixrq() {
		return shixrq;
	}

	public void setShixrq(String shixrq) {
		this.shixrq = shixrq;
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
