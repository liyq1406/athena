/**
 *
 */
package com.athena.print.entity.sys;


import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;


/**
 * 实体: 单据类型初始化
 * @author
 * @version
 * 
 */
public class PrintDictinfos extends PageableSupport implements Domain{
	
	private static final long serialVersionUID = -4310436063688027447L;
	
	
	//用户中心
	private String usercenter;
	//单据编号
	private String zidbm;
	//单据组编号
	private String danjzbh;
	//单据名称
	private String zidmc;
	//修改人
	private String editor;
	//修改时间
	private String edit_time;
	//操作标识
	private String biaos;
	//创建人
	private String creator;
	//创建时间
	private String create_time;
	//字典类型
	private String zidlx;
	//服务编号
	private String fuwbh;
	
	
	public String getFuwbh() {
		return fuwbh;
	}
	public void setFuwbh(String fuwbh) {
		this.fuwbh = fuwbh;
	}
	//return the zidlx
	public String getZidlx() {
		return zidlx;
	}
	//the zidlx to set
	public void setZidlx(String zidlx) {
		this.zidlx = zidlx;
	}
	//return the creator
	public String getCreator() {
		return creator;
	}
	//the creator to set
	public void setCreator(String creator) {
		this.creator = creator;
	}
	//return the create_time
	public String getCreate_time() {
		return create_time;
	}
	//the create_time to set
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	//return the biaos
	public String getBiaos() {
		return biaos;
	}
	//the biaos to set
	public void setBiaos(String biaos) {
		this.biaos = biaos;
	}
	//return the usercenter
	public String getUsercenter() {
		return usercenter;
	}
	//the usercenter to set
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	//return the zidbm
	public String getZidbm() {
		return zidbm;
	}
	//the zidbm to set
	public void setZidbm(String zidbm) {
		this.zidbm = zidbm;
	}
	//return the danjzbh
	public String getDanjzbh() {
		return danjzbh;
	}
	//the danjzbh to set
	public void setDanjzbh(String danjzbh) {
		this.danjzbh = danjzbh;
	}
	//return the zidmc
	public String getZidmc() {
		return zidmc;
	}
	//the zidmc to set
	public void setZidmc(String zidmc) {
		this.zidmc = zidmc;
	}
	//return the editor
	public String getEditor() {
		return editor;
	}
	//the editor to set
	public void setEditor(String editor) {
		this.editor = editor;
	}
	//return the edit_time
	public String getEdit_time() {
		return edit_time;
	}
	//the edit_time to set
	public void setEdit_time(String edit_time) {
		this.edit_time = edit_time;
	}
	//the id to set
	public void setId(String id) {
		// TODO Auto-generated method stub
		
	}
	//return the id
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}
	
	


	
}
