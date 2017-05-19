/**
 *
 */
package com.athena.ckx.entity.workCalendar;

import com.toft.core3.support.PageableSupport;
import com.athena.component.entity.Domain;

/**
 * 实体: 产线/仓库 -日历版次-工作时间编组
 * @author
 * @version
 * 
 */
public class CkxCalendarGroup extends PageableSupport  implements Domain{
	
	private static final long serialVersionUID = -6549930646468876531L;
	
	private String usercenter;//用户中心
	private String biaos;//标识
	private String rilbc;//日历版次
	private String shengxsj;//生效时间
	private String beiz;//备注
	private String bianzh;//工作时间编组号
	private String weilbzh;//未来编组号
	private String weilbzh2;//未来编组号2
	private String shengxsj2;//生效时间2
	private String weilbzh3;//未来编组号3
	private String shengxsj3;//生效时间3
	private String weilbzh4;//未来编组号4
	private String shengxsj4;//生效时间4
	private String appobj;//车间产线编号/仓库编号
	private String lxLength;//appobj字段长度
	
	private String uclist;
	public String getUclist() {
		return uclist;
	}

	public void setUclist(String uclist) {
		this.uclist = uclist;
	}
	public String getLxLength() {
		return lxLength;
	}

	public void setLxLength(String lxLength) {
		this.lxLength = lxLength;
	}

	private String creator;//创建人
	private String createTime;
	private String editor;
	private String editTime;
	
	private String wulgyyz;		//物流工艺员组编号
	
	
	public String getWulgyyz() {
		return wulgyyz;
	}

	public void setWulgyyz(String wulgyyz) {
		this.wulgyyz = wulgyyz;
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

	public String getUsercenter(){
		return this.usercenter;
	}
	
	public void setUsercenter(String usercenter){
		this.usercenter = usercenter;
	}
	public String getBiaos(){
		return this.biaos;
	}
	
	public void setBiaos(String biaos){
		this.biaos = biaos;
	}
	public String getRilbc(){
		return this.rilbc;
	}
	
	public void setRilbc(String rilbc){
		this.rilbc = rilbc;
	}
	public String getShengxsj(){
		return this.shengxsj;
	}
	
	public void setShengxsj(String shengxsj){
		this.shengxsj = shengxsj;
	}
	public String getBeiz(){
		return this.beiz;
	}
	
	public void setBeiz(String beiz){
		this.beiz = beiz;
	}
	public String getBianzh(){
		return this.bianzh;
	}
	
	public void setBianzh(String bianzh){
		this.bianzh = bianzh;
	}
	public String getWeilbzh(){
		return this.weilbzh;
	}
	
	public void setWeilbzh(String weilbzh){
		this.weilbzh = weilbzh;
	}
	public String getAppobj(){
		return this.appobj;
	}
	
	public void setAppobj(String appobj){
		this.appobj = appobj;
	}

	public String getWeilbzh2() {
		return weilbzh2;
	}

	public void setWeilbzh2(String weilbzh2) {
		this.weilbzh2 = weilbzh2;
	}

	public String getShengxsj2() {
		return shengxsj2;
	}

	public void setShengxsj2(String shengxsj2) {
		this.shengxsj2 = shengxsj2;
	}

	public String getWeilbzh3() {
		return weilbzh3;
	}

	public void setWeilbzh3(String weilbzh3) {
		this.weilbzh3 = weilbzh3;
	}

	public String getShengxsj3() {
		return shengxsj3;
	}

	public void setShengxsj3(String shengxsj3) {
		this.shengxsj3 = shengxsj3;
	}

	public String getWeilbzh4() {
		return weilbzh4;
	}

	public void setWeilbzh4(String weilbzh4) {
		this.weilbzh4 = weilbzh4;
	}

	public String getShengxsj4() {
		return shengxsj4;
	}

	public void setShengxsj4(String shengxsj4) {
		this.shengxsj4 = shengxsj4;
	}

	public void setId(String id) {
		this.usercenter = id;
	}

	public String getId() {
		return this.usercenter;
	}
}