/**
 *
 */
package com.athena.ckx.entity.paicfj;

import java.util.Date;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 实体: 车型运输商关系
 * @author
 * @version
 * 
 */
public class ChexYunss extends PageableSupport  implements Domain{
	
	private static final long serialVersionUID = 8793224808642751079L;
	
	private String creator;//创建人
	private String yunssbh;//运输商编号
	private Date edit_time;//修改时间
	private String editor;//修改人
	private String zuidsl;//最大数量
	private String usercenter;//用户中心
	private Date create_time;//创建时间
	private String chexbh;//车型编号
	
	
	public String getCreator(){
		return this.creator;
	}
	
	public void setCreator(String creator){
		this.creator = creator;
	}
	public String getYunssbh(){
		return this.yunssbh;
	}
	
	public void setYunssbh(String yunssbh){
		this.yunssbh = yunssbh;
	}
	public Date getEdit_time(){
		return this.edit_time;
	}
	
	public void setEdit_time(Date edit_time){
		this.edit_time = edit_time;
	}
	public String getEditor(){
		return this.editor;
	}
	
	public void setEditor(String editor){
		this.editor = editor;
	}
	public String getZuidsl(){
		return this.zuidsl;
	}
	
	public void setZuidsl(String zuidsl){
		this.zuidsl = zuidsl;
	}
	public String getUsercenter(){
		return this.usercenter;
	}
	
	public void setUsercenter(String usercenter){
		this.usercenter = usercenter;
	}
	public Date getCreate_time(){
		return this.create_time;
	}
	
	public void setCreate_time(Date create_time){
		this.create_time = create_time;
	}
	public String getChexbh(){
		return this.chexbh;
	}
	
	public void setChexbh(String chexbh){
		this.chexbh = chexbh;
	}
	
	
	
	public void setId(String id) {
		this.yunssbh = id;
	}

	public String getId() {
		return this.yunssbh;
	}
	
}