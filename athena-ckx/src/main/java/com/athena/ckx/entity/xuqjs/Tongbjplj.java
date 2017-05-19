package com.athena.ckx.entity.xuqjs;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;


/**
 * 同步集配零件分类
 * @author qizhongtao
 * 2012-4-11
 */
public class Tongbjplj extends PageableSupport implements Domain{
	
	private static final long serialVersionUID = -1204293760182875433L;
	
	private String usercenter;      //用户中心
	    
	private String lingjbh;         //零件编号
	
	private String shengcxbh;       //生产线编号
	
	private String nclass;          //CLASS
	
	private String nvalue;           //VALUE
	
	private String lingjmc;         //零件名称
	
	private String peislx;          //配送类型
	
	private String qianhcbs;        //前后车标识
	
	private String jipbzwz;         //集配包装位置
	
	private String creator;         //创建人
	
	private String create_time;     //创建时间
	
	private String editor;          //修改人
	
	private String edit_time;       //修改时间
	

	private String zuh;				//组号
	
	
	public void setZuh(String zuh) {
		this.zuh = zuh;
	}
	public String getZuh() {
		return zuh;
	}	
	              
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

	public String getShengcxbh() {
		return shengcxbh;
	}

	public void setShengcxbh(String shengcxbh) {
		this.shengcxbh = shengcxbh;
	}

	public String getNclass() {
		return nclass;
	}

	public void setNclass(String nclass) {
		this.nclass = nclass;
	}

	public String getNvalue() {
		return nvalue;
	}

	public void setNvalue(String nvalue) {
		this.nvalue = nvalue;
	}

	public String getLingjmc() {
		return lingjmc;
	}

	public void setLingjmc(String lingjmc) {
		this.lingjmc = lingjmc;
	}

	public String getPeislx() {
		return peislx;
	}

	public void setPeislx(String peislx) {
		this.peislx = peislx;
	}

	public String getQianhcbs() {
		return qianhcbs;
	}

	public void setQianhcbs(String qianhcbs) {
		this.qianhcbs = qianhcbs;
	}

	public String getJipbzwz() {
		return jipbzwz;
	}

	public void setJipbzwz(String jipbzwz) {
		this.jipbzwz = jipbzwz;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
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
