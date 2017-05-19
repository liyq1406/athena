package com.athena.ckx.entity.xuqjs;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 最大订货数量
 * @author qizhongtao
 * 2012-4-7
 */
public class CkxZuiddhsl extends PageableSupport implements Domain{

	private static final long serialVersionUID = 76443274637692318L;
	
	private String usercenter;     //用户中心
	private String lingjbh;        //零件编号
	private String gongysbh;       //供应商编号
	private String nianzq;         //年周期
	private Double zuiddhsl;       //最大订货数量
	private String creator;        //创建人
	private String create_time;    //创建时间
	private String editor;         //修改人
	private String edit_time;      //修改时间
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
	public String getGongysbh() {
		return gongysbh;
	}
	public void setGongysbh(String gongysbh) {
		this.gongysbh = gongysbh;
	}
	public String getNianzq() {
		return nianzq;
	}
	public void setNianzq(String nianzq) {
		this.nianzq = nianzq;
	}
	public Double getZuiddhsl() {
		return zuiddhsl;
	}
	public void setZuiddhsl(Double zuiddhsl) {
		this.zuiddhsl = zuiddhsl;
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
