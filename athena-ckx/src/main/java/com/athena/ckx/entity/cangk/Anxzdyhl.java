package com.athena.ckx.entity.cangk;
import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;
//11976
public class Anxzdyhl extends PageableSupport implements Domain {

		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String usercenter;
	private String lingjbh;
	private String ckxhd;
	private String zuidyhl;
	private String biaodsl;
	public String getZuidyhl() {
		return zuidyhl;
	}
	public void setZuidyhl(String zuidyhl) {
		this.zuidyhl = zuidyhl;
	}
	public String getBiaodsl() {
		return biaodsl;
	}
	public void setBiaodsl(String biaodsl) {
		this.biaodsl = biaodsl;
	}
	private String  creator;
	private String  create_time;
	private String  editor;
	private String  edit_time;
	private String beiz;
	private String beiz1;
	private String beiz2;
	private String beiz3;
	private double beiz4;
	private int  PageSize;
	private int  CurrentPage;
	public String getUsercenter() {
		return usercenter;
	}
	public int getPageSize() {
		return PageSize;
	}
	public void setPageSize(int pageSize) {
		PageSize = pageSize;
	}
	public int getCurrentPage() {
		return CurrentPage;
	}
	public void setCurrentPage(int currentPage) {
		CurrentPage = currentPage;
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
	public String getCkxhd() {
		return ckxhd;
	}
	public void setCkxhd(String ckxhd) {
		this.ckxhd = ckxhd;
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
	public String getBeiz2() {
		return beiz2;
	}
	public void setBeiz2(String beiz2) {
		this.beiz2 = beiz2;
	}
	public String getBeiz3() {
		return beiz3;
	}
	public void setBeiz3(String beiz3) {
		this.beiz3 = beiz3;
	}
	public double getBeiz4() {
		return beiz4;
	}
	public void setBeiz4(double beiz4) {
		this.beiz4 = beiz4;
	}
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}
	public void setId(String arg0) {
		// TODO Auto-generated method stub
		
	}
}
