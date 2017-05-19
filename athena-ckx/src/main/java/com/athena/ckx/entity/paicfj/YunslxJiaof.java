/**
 *
 */
package com.athena.ckx.entity.paicfj;

import java.util.Date;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 实体: 运输路线交付时刻
 * @author
 * @version
 * 
 */
public class YunslxJiaof extends PageableSupport  implements Domain{
	
	private static final long serialVersionUID = 8853581129441107819L;
	
	private String usercenter;//用户中心
	private String yunslxbh;//运输路线编号
	private String jiaofsk;//交付时刻
	private Date edit_time;//修改时间
	private String xingqxh;//星期序号
	private String editor;//修改人
	private String creator;//创建人
	private Date create_time;//创建时间
	
	
	public String getUsercenter(){
		return this.usercenter;
	}
	
	public void setUsercenter(String usercenter){
		this.usercenter = usercenter;
	}
	public String getYunslxbh(){
		return this.yunslxbh;
	}
	
	public void setYunslxbh(String yunslxbh){
		this.yunslxbh = yunslxbh;
	}
	public String getJiaofsk(){
		return this.jiaofsk;
	}
	
	public void setJiaofsk(String jiaofsk){
		this.jiaofsk = jiaofsk;
	}
	public Date getEdit_time(){
		return this.edit_time;
	}
	
	public void setEdit_time(Date edit_time){
		this.edit_time = edit_time;
	}
	public String getXingqxh(){
		return this.xingqxh;
	}
	
	public void setXingqxh(String xingqxh){
		this.xingqxh = xingqxh;
	}
	public String getEditor(){
		return this.editor;
	}
	
	public void setEditor(String editor){
		this.editor = editor;
	}
	public String getCreator(){
		return this.creator;
	}
	
	public void setCreator(String creator){
		this.creator = creator;
	}
	public Date getCreate_time(){
		return this.create_time;
	}
	
	public void setCreate_time(Date create_time){
		this.create_time = create_time;
	}
	
	public void setId(String id) {
		this.usercenter = id;
	}

	public String getId() {
		return this.usercenter;
	}
	
}