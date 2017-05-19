package com.athena.ckx.entity.carry;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 外部物流路径详细
 * @author kong
 *
 */
public class CkxWaibwlxx extends PageableSupport  implements Domain{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2781792058158991023L;
	private String usercenter;
	private String lujbh;
	private String xuh;
	private String newXuh;
	private String wuldbh;
	private String wuldmc;
	private String shifjk;
	private String guanjdbs;
	private Double jihdhsysj;
	private String gcbh;
	private String yunsms;
	private Double beihzq;
	private Double yunssj;
	private String shengxbs;
	private String shengxrq;
	private String yaohlzkbs;
	private String creator;//创建人
	private String createTime;
	private String editor;
	private String editTime;
	
	private String shifqhbs;//0011509 - 是否取货标识
	
	public String getShifqhbs() {
		return shifqhbs;
	}
	public void setShifqhbs(String shifqhbs) {
		this.shifqhbs = shifqhbs;
	}
	
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	public String getNewXuh() {
		return newXuh;
	}
	public void setNewXuh(String newXuh) {
		this.newXuh = newXuh;
	}
	public String getLujbh() {
		return lujbh;
	}
	public void setLujbh(String lujbh) {
		this.lujbh = lujbh;
	}
	public String getXuh() {
		return xuh;
	}
	public void setXuh(String xuh) {
		this.xuh = xuh;
	}
	public String getWuldbh() {
		return wuldbh;
	}
	public void setWuldbh(String wuldbh) {
		this.wuldbh = wuldbh;
	}
	public String getWuldmc() {
		return wuldmc;
	}
	public void setWuldmc(String wuldmc) {
		this.wuldmc = wuldmc;
	}
	public String getShifjk() {
		return shifjk;
	}
	public void setShifjk(String shifjk) {
		this.shifjk = shifjk;
	}
	public String getGuanjdbs() {
		return guanjdbs;
	}
	public void setGuanjdbs(String guanjdbs) {
		this.guanjdbs = guanjdbs;
	}
	public Double getJihdhsysj() {
		return jihdhsysj;
	}
	public void setJihdhsysj(Double jihdhsysj) {
		this.jihdhsysj = jihdhsysj;
	}
	public String getGcbh() {
		return gcbh;
	}
	public void setGcbh(String gcbh) {
		this.gcbh = gcbh;
	}
	public String getYunsms() {
		return yunsms;
	}
	public void setYunsms(String yunsms) {
		this.yunsms = yunsms;
	}
	public Double getBeihzq() {
		return beihzq;
	}
	public void setBeihzq(Double beihzq) {
		this.beihzq = beihzq;
	}
	public Double getYunssj() {
		return yunssj;
	}
	public void setYunssj(Double yunssj) {
		this.yunssj = yunssj;
	}
	public String getShengxbs() {
		return shengxbs;
	}
	public void setShengxbs(String shengxbs) {
		this.shengxbs = shengxbs;
	}
	public String getShengxrq() {
		return shengxrq;
	}
	public void setShengxrq(String shengxrq) {
		this.shengxrq = shengxrq;
	}
	public String getYaohlzkbs() {
		return yaohlzkbs;
	}
	public void setYaohlzkbs(String yaohlzkbs) {
		this.yaohlzkbs = yaohlzkbs;
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
	public void setId(String id) {
		// TODO Auto-generated method stub
		
	}
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
