package com.athena.ckx.entity.xuqjs;
import java.math.BigDecimal;
import java.util.Date;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;
/*
 * 盈余修改记录
 * @author zbb
 */
		
		
public class Yingyxgjl extends PageableSupport implements Domain {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7554758297405767579L;
	//用户中心
	private String usercenter;

	//零件编号
	private String lingjbh;
	//消耗点/仓库
	private String mudd; 

	//开始盈余
	private BigDecimal oyingy; 
	
	//修改后盈余
	private BigDecimal nyingy; 
	
	//修改者
	private String editor; 
	//修改时间
	private String edit_time; 

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
	public String getMudd() {
		return mudd;
	}
	public void setMudd(String mudd) {
		this.mudd = mudd;
	}
	public BigDecimal getOyingy() {
		return oyingy;
	}
	public void setOyingy(BigDecimal oyingy) {
		this.oyingy = oyingy;
	}
	public BigDecimal getNyingy() {
		return nyingy;
	}
	public void setNyingy(BigDecimal nyingy) {
		this.nyingy = nyingy;
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
	@Override
	public void setId(String id) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}
}
