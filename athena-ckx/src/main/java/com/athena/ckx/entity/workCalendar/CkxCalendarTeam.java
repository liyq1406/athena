/**
 *
 */
package com.athena.ckx.entity.workCalendar;

import com.toft.core3.support.PageableSupport;
import com.athena.component.entity.Domain;

/**
 * 实体: 工作时间
 * @author
 * @version
 * 
 */
public class CkxCalendarTeam extends PageableSupport  implements Domain{
	
	private static final long serialVersionUID = -8190490931296012561L;
	
	private String kaissj;//开始时间
	private String bianzh;//工作时间编组号
	private String xingqxh;//星期序号
	private Long tiaozsj;//调整时间
	private String ban;//班
	private String jiezsj;//截至时间
	private String xuh;//序号
	private String biaos;//标识
	private String creator;//创建人
	private String createTime;
	private String editor;
	private String editTime;
	private String uclist;
	public String getUclist() {
		return uclist;
	}

	public void setUclist(String uclist) {
		this.uclist = uclist;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getEditTime() {
		return editTime;
	}

	public void setEditTime(String editTime) {
		this.editTime = editTime;
	}

	public String getBiaos() {
		return biaos;
	}

	public void setBiaos(String biaos) {
		this.biaos = biaos;
	}

	public String getKaissj(){
		return this.kaissj;
	}
	
	public void setKaissj(String kaissj){
		this.kaissj = kaissj;
	}
	public String getBianzh(){
		return this.bianzh;
	}
	
	public void setBianzh(String bianzh){
		this.bianzh = bianzh;
	}
	public String getXingqxh(){
		return this.xingqxh;
	}
	
	public void setXingqxh(String xingqxh){
		this.xingqxh = xingqxh;
	}
	public Long getTiaozsj(){
		return this.tiaozsj;
	}
	
	public void setTiaozsj(Long tiaozsj){
		this.tiaozsj = tiaozsj;
	}
	public String getBan(){
		return this.ban;
	}
	
	public void setBan(String ban){
		this.ban = ban;
	}
	public String getJiezsj(){
		return this.jiezsj;
	}
	
	public void setJiezsj(String jiezsj){
		this.jiezsj = jiezsj;
	}
	public String getXuh(){
		return this.xuh;
	}
	
	public void setXuh(String xuh){
		this.xuh = xuh;
	}
	
	
	
	public void setId(String id) {
		this.bianzh = id;
	}

	public String getId() {
		return this.bianzh;
	}
}