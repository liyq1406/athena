/**
 *
 */
package com.athena.ckx.entity.workCalendar;

import com.toft.core3.support.PageableSupport;
import com.athena.component.entity.Domain;

/**
 * 实体: 中心日历
 * @author
 * @version
 * 
 */
public class CkxCalendarCenter extends PageableSupport  implements Domain{
	
	private static final long serialVersionUID = 1053071494790617032L;
	
	private String nianzq;//年周期
	private String zhoux;//周序
	private String nianzx;//年周序
	private String xingq;//星期
	private String shifjfr;//是否交付日
	private String shifgzr;//是否工作日
	private String biaos;//标识
	private Double xis;//系数
	private String riq;//日期
	private String usercenter;//用户中心
	private String creator;//创建人
	private String createTime;//关键时间
	private String editor;
	private String editTime;
	
	private String beginDate;//开始日期
	private String endDate;//结束日期
	
	

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
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

	public String getNianzq() {
		return nianzq;
	}

	public void setNianzq(String nianzq) {
		this.nianzq = nianzq;
	}



	public String getZhoux() {
		return zhoux;
	}

	public void setZhoux(String zhoux) {
		this.zhoux = zhoux;
	}

	public String getNianzx() {
		return nianzx;
	}

	public void setNianzx(String nianzx) {
		this.nianzx = nianzx;
	}

	public String getXingq(){
		return this.xingq;
	}
	
	public void setXingq(String xingq){
		this.xingq = xingq;
	}
	public String getShifjfr(){
		return this.shifjfr;
	}
	
	public void setShifjfr(String shifjfr){
		this.shifjfr = shifjfr;
	}
	public String getShifgzr(){
		return this.shifgzr;
	}
	
	public void setShifgzr(String shifgzr){
		this.shifgzr = shifgzr;
	}
	public String getBiaos(){
		return this.biaos;
	}
	
	public void setBiaos(String biaos){
		this.biaos = biaos;
	}
	
	public Double getXis() {
		return xis;
	}

	public void setXis(Double xis) {
		this.xis = xis;
	}

	public String getRiq(){
		return this.riq;
	}
	
	public void setRiq(String riq){
		this.riq = riq;
	}
	public String getUsercenter(){
		return this.usercenter;
	}
	
	public void setUsercenter(String usercenter){
		this.usercenter = usercenter;
	}
	
	
	
	public void setId(String id) {
		this.riq = id;
	}

	public String getId() {
		return this.riq;
	}
}