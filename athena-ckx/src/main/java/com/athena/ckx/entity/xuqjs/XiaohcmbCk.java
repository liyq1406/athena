package com.athena.ckx.entity.xuqjs;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 小火车运输时刻模板
 * @author denggq
 * @date 2012-4-11
 */
public class XiaohcmbCk extends PageableSupport implements Domain{

	private static final long serialVersionUID = 2892190665367165024L;

	private String usercenter;	//用户中心
	
	private String chanx;	//生产线编号
	
	private Integer liush;	//流水号
	
	private String xiaohcbh;	//小火车编号
	
	private Integer tangc;		//趟次
	
	private String xiaohcrq;		//小火车日期
	
	private Integer qislsh ;     //起始流水号
	
	private Integer jieslsh ;    //结束流水号
	
	private Integer emonsxlsh;   //emon上线流水号
	
	private Integer smonsxlsh;   //smon上线流水号
	
	private Integer emonbhlsh;    //emon备货流水号
	
	private Integer smonbhlsh;    //smon备货流水号
	
	private String beihsj;       //备货时间
	
	private String flag ;         //标识
	
	private String zhuangt;       //状态
	
	private String creator;       //创建人
	
	private String create_time;   //创建时间
	
	private String editor;         //修改人
	
	private String edit_time;      //修改时间
	
	private String beiz1;          //备注1
	
	private String beiz2;          //备注2
	
	private String beiz3;          // 备注3
	
	private String beiz4;          //备注4
	
	private String beiz5;          //备注5
	
	private String beiz6;         //备注6
	
	private String beiz7;          //备注7
	
	private String emonzclsh;
	private String smonzclsh;
	private Integer dangqzcxh; 
	private String shangxsj;
	
	public void setId(String id) {
		
	}

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getChanx() {
		return chanx;
	}

	public void setChanx(String chanx) {
		this.chanx = chanx;
	}

	public Integer getLiush() {
		return liush;
	}

	public void setLiush(Integer liush) {
		this.liush = liush;
	}

	public String getXiaohcbh() {
		return xiaohcbh;
	}

	public void setXiaohcbh(String xiaohcbh) {
		this.xiaohcbh = xiaohcbh;
	}

	public Integer getTangc() {
		return tangc;
	}

	public void setTangc(Integer tangc) {
		this.tangc = tangc;
	}

	
	public String getXiaohcrq() {
		return xiaohcrq;
	}

	public void setXiaohcrq(String xiaohcrq) {
		this.xiaohcrq = xiaohcrq;
	}

	public Integer getQislsh() {
		return qislsh;
	}

	public void setQislsh(Integer qislsh) {
		this.qislsh = qislsh;
	}

	public Integer getJieslsh() {
		return jieslsh;
	}

	public void setJieslsh(Integer jieslsh) {
		this.jieslsh = jieslsh;
	}

	public Integer getEmonsxlsh() {
		return emonsxlsh;
	}

	public void setEmonsxlsh(Integer emonsxlsh) {
		this.emonsxlsh = emonsxlsh;
	}

	public Integer getSmonsxlsh() {
		return smonsxlsh;
	}

	public void setSmonsxlsh(Integer smonsxlsh) {
		this.smonsxlsh = smonsxlsh;
	}

	public Integer getEmonbhlsh() {
		return emonbhlsh;
	}

	public void setEmonbhlsh(Integer emonbhlsh) {
		this.emonbhlsh = emonbhlsh;
	}

	public Integer getSmonbhlsh() {
		return smonbhlsh;
	}

	public void setSmonbhlsh(Integer smonbhlsh) {
		this.smonbhlsh = smonbhlsh;
	}

	public String getBeihsj() {
		return beihsj;
	}

	public void setBeihsj(String beihsj) {
		this.beihsj = beihsj;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getZhuangt() {
		return zhuangt;
	}

	public void setZhuangt(String zhuangt) {
		this.zhuangt = zhuangt;
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

	public String getBeiz1() {
		return beiz1;
	}

	public void setBeiz1(String beiz1) {
		this.beiz1 = beiz1;
	}

	public String getBeiz2() {
		return beiz2;
	}

	public void setBeiz2(String beiz2) {
		this.beiz2 = beiz2;
	}

	public String getBeiz3() {
		return beiz3;
	}

	public void setBeiz3(String beiz3) {
		this.beiz3 = beiz3;
	}

	public String getBeiz4() {
		return beiz4;
	}

	public void setBeiz4(String beiz4) {
		this.beiz4 = beiz4;
	}

	public String getBeiz5() {
		return beiz5;
	}

	public void setBeiz5(String beiz5) {
		this.beiz5 = beiz5;
	}

	public String getBeiz6() {
		return beiz6;
	}

	public void setBeiz6(String beiz6) {
		this.beiz6 = beiz6;
	}

	public String getBeiz7() {
		return beiz7;
	}

	public void setBeiz7(String beiz7) {
		this.beiz7 = beiz7;
	}

	
	public String getEmonzclsh() {
		return emonzclsh;
	}

	public void setEmonzclsh(String emonzclsh) {
		this.emonzclsh = emonzclsh;
	}

	public String getSmonzclsh() {
		return smonzclsh;
	}

	public void setSmonzclsh(String smonzclsh) {
		this.smonzclsh = smonzclsh;
	}

	public Integer getDangqzcxh() {
		return dangqzcxh;
	}

	public void setDangqzcxh(Integer dangqzcxh) {
		this.dangqzcxh = dangqzcxh;
	}

	public String getShangxsj() {
		return shangxsj;
	}

	public void setShangxsj(String shangxsj) {
		this.shangxsj = shangxsj;
	}

	public String getId() {
		return null;
	}

}
