package com.athena.ckx.entity.xuqjs;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;


/**
 * 未来几日剔除休息时间
 * @author denggq
 * 2012-4-7
 */
public class Ticxxsj extends PageableSupport implements Domain{
	
	private static final long serialVersionUID = 6424375183626853203L;

	private String usercenter;	//用户中心
	
	private String chanxck;		//交付码
	
	private String lxLength;	//chanxck字段长度
	
	private String riq;			//日
	
	private Integer shunxh;		//顺序号
	
	private String shijdkssj;	//时间段开始时间
	
	private String shijdjssj;	//时间段结束时间
	
	private Integer shijcd;		//时间长度（分）
	
	private String gongzr;		//工作日
	
	private String tiaozsj;		//调整时间
	
	private String creator;		//增加人
	
	private String create_time;	//增加时间
	
	private String editor;		//修改人
	
	private String edit_time;	//修改时间
	
	
	private String xiehztbh;//卸货站台编号
	public String getXiehztbh() {
		return xiehztbh;
	}

	public void setXiehztbh(String xiehztbh) {
		this.xiehztbh = xiehztbh;
	}

	public String getGongzrl() {
		return gongzrl;
	}

	public void setGongzrl(String gongzrl) {
		this.gongzrl = gongzrl;
	}

	public String getGongzsjbz() {
		return gongzsjbz;
	}

	public void setGongzsjbz(String gongzsjbz) {
		this.gongzsjbz = gongzsjbz;
	}

	private String gongzrl;//卸货站台编号
	private String gongzsjbz;//卸货站台编号
	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getEdit_time() {
		return edit_time;
	}

	public void setEdit_time(String edit_time) {
		this.edit_time = edit_time;
	}

	public String getChanxck() {
		return chanxck;
	}

	public String getRiq() {
		return riq;
	}

	public Integer getShunxh() {
		return shunxh;
	}

	public String getShijdkssj() {
		return shijdkssj;
	}

	public String getShijdjssj() {
		return shijdjssj;
	}

	public String getGongzr() {
		return gongzr;
	}

	public void setChanxck(String chanxck) {
		this.chanxck = chanxck;
	}

	public void setRiq(String riq) {
		this.riq = riq;
	}

	public void setShunxh(Integer shunxh) {
		this.shunxh = shunxh;
	}

	public void setShijdkssj(String shijdkssj) {
		this.shijdkssj = shijdkssj;
	}

	public void setShijdjssj(String shijdjssj) {
		this.shijdjssj = shijdjssj;
	}

	public void setGongzr(String gongzr) {
		this.gongzr = gongzr;
	}

	public void setShijcd(Integer shijcd) {
		this.shijcd = shijcd;
	}

	public Integer getShijcd() {
		return shijcd;
	}

	public void setTiaozsj(String tiaozsj) {
		this.tiaozsj = tiaozsj;
	}

	public String getTiaozsj() {
		return tiaozsj;
	}

	public void setId(String id) {
		
	}

	public String getId() {
		return null;
	}

	public void setLxLength(String lxLength) {
		this.lxLength = lxLength;
	}

	public String getLxLength() {
		return lxLength;
	}
	
	
}
