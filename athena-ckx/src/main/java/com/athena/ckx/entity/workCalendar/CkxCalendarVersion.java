/**
 *
 */
package com.athena.ckx.entity.workCalendar;

import com.toft.core3.support.PageableSupport;
import com.athena.component.entity.Domain;

/**
 * 实体: 日历版次
 * @author
 * @version
 * 
 */
public class CkxCalendarVersion extends PageableSupport  implements Domain{
	
	private static final long serialVersionUID = -8455096438901626462L;
	
	private Double xis;//系数
	private String xingq;//星期
	private String banc;//版次
	private String usercenter;//用户中心
	private String biaos;//标识
	private String riq;//日期
	private String shifgzr;//是否工作日
	private String shifjfr;//是否交付日
	private String nianzq;//年周期
	private String nianzx;//年周序	
	private String zhoux;//周序
	private String creator;//创建人
	private String createTime;
	private String editor;
	private String editTime;
	
	
	private String beginDate;//开始日期
	private String endDate;//结束日期
	private String nianzqTemp;//原年周期
	private String zhouxTemp;//原周序
	private String nianzxTemp;//原年周序
	private String ynianzx;//原年周序
	
	private String strNA01Banc;//版次串

	private String uclist;
	public String getUclist() {
		return uclist;
	}

	public void setUclist(String uclist) {
		this.uclist = uclist;
	}

	
	public String getYnianzx() {
		return ynianzx;
	}

	public void setYnianzx(String ynianzx) {
		this.ynianzx = ynianzx;
	}

	public String getNianzqTemp() {
		return nianzqTemp;
	}

	public void setNianzqTemp(String nianzqTemp) {
		this.nianzqTemp = nianzqTemp;
	}

	public String getZhouxTemp() {
		return zhouxTemp;
	}

	public void setZhouxTemp(String zhouxTemp) {
		this.zhouxTemp = zhouxTemp;
	}

	public String getNianzxTemp() {
		return nianzxTemp;
	}

	public void setNianzxTemp(String nianzxTemp) {
		this.nianzxTemp = nianzxTemp;
	}

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


	public Double getXis() {
		return xis;
	}

	public void setXis(Double xis) {
		this.xis = xis;
	}

	public String getXingq(){
		return this.xingq;
	}
	
	public void setXingq(String xingq){
		this.xingq = xingq;
	}
	public String getBanc(){
		return this.banc;
	}
	
	public void setBanc(String banc){
		this.banc = banc;
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
	public String getRiq(){
		return this.riq;
	}
	
	public void setRiq(String riq){
		this.riq = riq;
	}
	public String getShifgzr(){
		return this.shifgzr;
	}
	
	public void setShifgzr(String shifgzr){
		this.shifgzr = shifgzr;
	}
	public String getShifjfr(){
		return this.shifjfr;
	}
	
	public void setShifjfr(String shifjfr){
		this.shifjfr = shifjfr;
	}
	
	
	
	
	public String getNianzq() {
		return nianzq;
	}

	public void setNianzq(String nianzq) {
		this.nianzq = nianzq;
	}

	public String getNianzx() {
		return nianzx;
	}

	public void setNianzx(String nianzx) {
		this.nianzx = nianzx;
	}


	public String getZhoux() {
		return zhoux;
	}

	public void setZhoux(String zhoux) {
		this.zhoux = zhoux;
	}

	public void setId(String id) {
		this.banc = id;
	}

	public String getId() {
		return this.banc;
	}

	public String getStrNA01Banc() {
		return strNA01Banc;
	}

	public void setStrNA01Banc(String strNA01Banc) {
		this.strNA01Banc = strNA01Banc;
	}
	
	
}