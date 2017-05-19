package com.athena.xqjs.entity.ppl;

import java.math.BigDecimal;

import com.toft.core3.support.PageableSupport;
/**
 * 需求计算xqmx表
 * @author Xiahui
 * @date  2012-1-11
 */
public class Xqmx extends PageableSupport {
	private static final long serialVersionUID = 1L;
	private String id          ;  //主键id
	private String usercenter  ;  //用户中心
	private String lingjbh     ;  //零件编号
	private String lingjmc     ;  //零件名称
	private String lingjdw     ;  //零件名称
	private String baozlx      ;  //包装类型
	private BigDecimal baozrl      ; //包装容量
	private String jihydm      ;  //计划员代码
	private String gongysdm    ;  //供应商代码
	private String gongysmc    ;  //供应商名称
	private String dinghcj     ;  //订货车间
	private String zhizlx      ;  //制造路线
	private BigDecimal beihzq      ;//备货周期
	private BigDecimal fayzq       ;//发运周期
	private String creator     ;  //创建人
	private String create_time ;  //创建时间
	private String editor      ;  //修改人
	private String edit_time   ;  //修改时间
	private String active      ;  //删除标识
	private String gongyfe     ;  //供应份额
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	public String getLingjbh() {
		return lingjbh;
	}
	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}
	public String getLingjmc() {
		return lingjmc;
	}
	public void setLingjmc(String lingjmc) {
		this.lingjmc = lingjmc;
	}
	public String getLingjdw() {
		return lingjdw;
	}
	public void setLingjdw(String lingjdw) {
		this.lingjdw = lingjdw;
	}
	public String getBaozlx() {
		return baozlx;
	}
	public void setBaozlx(String baozlx) {
		this.baozlx = baozlx;
	}
	
	public String getJihydm() {
		return jihydm;
	}
	public void setJihydm(String jihydm) {
		this.jihydm = jihydm;
	}
	public String getGongysdm() {
		return gongysdm;
	}
	public void setGongysdm(String gongysdm) {
		this.gongysdm = gongysdm;
	}
	public String getGongysmc() {
		return gongysmc;
	}
	public void setGongysmc(String gongysmc) {
		this.gongysmc = gongysmc;
	}
	public String getDinghcj() {
		return dinghcj;
	}
	public void setDinghcj(String dinghcj) {
		this.dinghcj = dinghcj;
	}
	public String getZhizlx() {
		return zhizlx;
	}
	public void setZhizlx(String zhizlx) {
		this.zhizlx = zhizlx;
	}
	
	public BigDecimal getBaozrl() {
		return baozrl;
	}
	public void setBaozrl(BigDecimal baozrl) {
		this.baozrl = baozrl;
	}
	public BigDecimal getBeihzq() {
		return beihzq;
	}
	public void setBeihzq(BigDecimal beihzq) {
		this.beihzq = beihzq;
	}
	public BigDecimal getFayzq() {
		return fayzq;
	}
	public void setFayzq(BigDecimal fayzq) {
		this.fayzq = fayzq;
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
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	public String getGongyfe() {
		return gongyfe;
	}
	public void setGongyfe(String gongyfe) {
		this.gongyfe = gongyfe;
	}

}
