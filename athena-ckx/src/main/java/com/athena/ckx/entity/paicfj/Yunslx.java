/**
 *
 */
package com.athena.ckx.entity.paicfj;

import java.util.Date;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 实体: 运输路线
 * @author
 * @version
 * 
 */
public class Yunslx extends PageableSupport  implements Domain{
	
	private static final long serialVersionUID = 1528381373476203251L;
	
	private String editor;//修改人
	private Date create_time;//创建时间
	private String usercenter;//用户中心
	private String yunslxbh;//运输路线编号
	private Double zuidtqfysj;//最大提前发运时间
	

	private Date edit_time;//修改时间
	private String creator;//创建人
	private String yunslxmc;//运输路线名称
	private String biaos;   //标识
	
	public String getBiaos() {
		return biaos;
	}

	public void setBiaos(String biaos) {
		this.biaos = biaos;
	}

	public Double getZuidtqfysj() {
		return zuidtqfysj;
	}

	public void setZuidtqfysj(Double zuidtqfysj) {
		this.zuidtqfysj = zuidtqfysj;
	}

	public String getEditor(){
		return this.editor;
	}
	
	public void setEditor(String editor){
		this.editor = editor;
	}
	public Date getCreate_time(){
		return this.create_time;
	}
	
	public void setCreate_time(Date create_time){
		this.create_time = create_time;
	}
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
	public Date getEdit_time(){
		return this.edit_time;
	}
	
	public void setEdit_time(Date edit_time){
		this.edit_time = edit_time;
	}
	public String getCreator(){
		return this.creator;
	}
	
	public void setCreator(String creator){
		this.creator = creator;
	}
	public String getYunslxmc(){
		return this.yunslxmc;
	}
	
	public void setYunslxmc(String yunslxmc){
		this.yunslxmc = yunslxmc;
	}
	
	public void setId(String id) {
		this.usercenter = id;
	}

	public String getId() {
		return this.usercenter;
	}
	
}