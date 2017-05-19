package com.athena.ckx.entity.xuqjs;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 零件消耗点变更
 * @author wangyu
 * @date 2012-1-12
 */
public class CkxLingjxhds extends PageableSupport implements Domain{

	private static final long serialVersionUID = 2892190665367165024L;

	private String usercenter;	//用户中心
	
	private String lingjbh;		//零件编号
	
	private String xinljbh;		//新零件编号
	
	private String xiaohd;		//新消耗点
	
	private String yuanxhd;	//原消耗点
	
	private String biaos;		//标识
	
    private String shengxr;        //消耗点起始日
	
	private String jiesr;          //结束日期
	
	private String creator;		//增加人
	
	private String create_time;	//增加时间
	
	private String editor;		//修改人
	
	private String edit_time;	//修改时间
	
	
	
	public String getXinljbh() {
		return xinljbh;
	}

	public void setXinljbh(String xinljbh) {
		this.xinljbh = xinljbh;
	}

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	

	public String getShengxr() {
		return shengxr;
	}

	public void setShengxr(String shengxr) {
		this.shengxr = shengxr;
	}

	public String getJiesr() {
		return jiesr;
	}

	public void setJiesr(String jiesr) {
		this.jiesr = jiesr;
	}

	public String getLingjbh() {
		return lingjbh;
	}

	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}


	public String getXiaohd() {
		return xiaohd;
	}

	public void setXiaohd(String xiaohd) {
		this.xiaohd = xiaohd;
	}

	public String getYuanxhd() {
		return yuanxhd;
	}

	public void setYuanxhd(String yuanxhd) {
		this.yuanxhd = yuanxhd;
	}

	public String getBiaos() {
		return biaos;
	}

	public void setBiaos(String biaos) {
		this.biaos = biaos;
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

	public void setId(String id) {
		// TODO Auto-generated method stub
		
	}

	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

}
