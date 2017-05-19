/**
 *
 */
package com.athena.print.entity.sys;


import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;


/**
 * 实体: 仓库-单据
 * @author
 * @version
 * 
 */
public class PrintStrogdict extends PageableSupport implements Domain{
	
	private static final long serialVersionUID = -4310436063688027447L;
	
	//用户中心
	private String usercenter;
	//仓库编号
	private String cangkbh;
	//单据编号
	private String zidbm;
	//单据组编号
	private String danjzbh;
	//单据名称
	private String zidmc;
	//打印份数
	private int printnumber;
	//启用标识
	private String biaos;
	//修改人
	private String editor;
	//修改时间
	private String edit_time;
	//创建人
	private String creator;
	//创建时间
	private String create_time;
	//标识
	private String active;
	
	
	//return the edit_time
	public String getBiaos() {
		return biaos;
	}
	//the editor to set
	public void setBiaos(String biaos) {
		this.biaos = biaos;
	}
	//return the creator
	public String getCreator() {
		return creator;
	}
	//return the active
	public String getActive() {
		return active;
	}
	//the active to set
	public void setActive(String active) {
		this.active = active;
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
	//return the usercenter
	public String getUsercenter() {
		return usercenter;
	}
	//the usercenter to set
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	//return the cangkbh
	public String getCangkbh() {
		return cangkbh;
	}
	//the cangkbh to set
	public void setCangkbh(String cangkbh) {
		this.cangkbh = cangkbh;
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
	//return the edit_time
	public int getPrintnumber() {
		return printnumber;
	}
	//the editor to set
	public void setPrintnumber(int printnumber) {
		this.printnumber = printnumber;
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
