package com.athena.xqjs.entity.ppl;

import java.math.BigDecimal;

import com.toft.core3.support.PageableSupport;
/*
 * 年度预告bean
 * author   夏晖
 * date     2011-12-7
 */
public class Niandygmx extends PageableSupport {
	private static final long serialVersionUID = 1L;
	private String id	        ;  // id  
	private String pplbc	    ;  // ppl版次
	private String usercenter ;    //用户中心   
	private String lingjbh	    ;  //零件号
	private String lingjmc    ;    //零件名称
	private String lingjdw	  ;    //零件单位
	private String baozlx	    ;  //包装类型  
	private BigDecimal baozrl	;  //包装容量
	private String jihydm	    ;  //计划员代码  
	private String gongysdm	  ;    //供应商代码  
	private String gongysmc	  ;    //供应商名称 
	private String dinghcj	  ;    //订货车间  
	private String zhizlx	    ;  //制造路线  
	private String yuanszzlx	;  //原始制造路线  
	private String p0xqzq	    ;  //po需求周期
	private BigDecimal p0sl	 ;   //   p0数量
	private BigDecimal p1sl	 ;   //   p1数量
	private BigDecimal p2sl	 ;   //   p2数量
	private BigDecimal p3sl	 ;   //   p3数量
	private BigDecimal p4sl	 ;   //   p4数量
	private BigDecimal p5sl	 ;   //   p5数量
	private BigDecimal p6sl	 ;   //   p6数量
	private BigDecimal p7sl	 ;   //   p7数量
	private BigDecimal p8sl	 ;   //   p8数量
	private BigDecimal p9sl	 ;   //   p9数量
	private BigDecimal p10sl ;   //   p10数量
	private BigDecimal p11sl ;   //   p11数量
	private String  creator  ;   //   创建人
	private String  editor  ;   //    修改人
	private String  create_time; //   创建时间
	private String  edit_time; //     修改时间
	private String  active  ;   //    删除标识
	private String  edittime;   //新修改时间
	private String   neweditor; //新修改人
	private String   fawmc;
	 
	
	private String xuqbc	    ;  // ppl版次
	private String xuqbc1	    ;  // ppl版次
	private String xuqbc2	    ;  // ppl版次
	private String jiz	    ;  // ppl版次
	
	private String gcbh 	;  
	private String chengysmc 	;  
	
	
	private BigDecimal p0sl1	 ;  
	private BigDecimal p0sl2	 ;  
	private BigDecimal p1sl1	 ;  
	private BigDecimal p1sl2	 ;  
	private BigDecimal p2sl1	 ;  
	private BigDecimal p2sl2	 ;  
	private BigDecimal p3sl1	 ;  
	private BigDecimal p3sl2	 ;  
	private BigDecimal p4sl1	 ;  
	private BigDecimal p4sl2	 ;  
	private BigDecimal p5sl1	 ;  
	private BigDecimal p5sl2	 ;  
	private BigDecimal p6sl1	 ;  
	private BigDecimal p6sl2	 ;  
	private BigDecimal p7sl1	 ;  
	private BigDecimal p7sl2	 ;  
	private BigDecimal p8sl1	 ;  
	private BigDecimal p8sl2	 ;  
	private BigDecimal p9sl1	 ;  
	private BigDecimal p9sl2	 ;  
	private BigDecimal p10sl1	 ;  
	private BigDecimal p10sl2	 ;  
	private BigDecimal p11sl1	 ;  
	private BigDecimal p11sl2	 ;  
	private BigDecimal p12sl1	 ;  
	private BigDecimal p12sl2	 ;  
	
	
	
	
	
	public String getGcbh() {
		return gcbh;
	}
	public void setGcbh(String gcbh) {
		this.gcbh = gcbh;
	}
	public String getChengysmc() {
		return chengysmc;
	}
	public void setChengysmc(String chengysmc) {
		this.chengysmc = chengysmc;
	}
	public String getXuqbc() {
		return xuqbc;
	}
	public void setXuqbc(String xuqbc) {
		this.xuqbc = xuqbc;
	}
	public String getXuqbc1() {
		return xuqbc1;
	}
	public void setXuqbc1(String xuqbc1) {
		this.xuqbc1 = xuqbc1;
	}
	public String getXuqbc2() {
		return xuqbc2;
	}
	public void setXuqbc2(String xuqbc2) {
		this.xuqbc2 = xuqbc2;
	}
	public String getJiz() {
		return jiz;
	}
	public void setJiz(String jiz) {
		this.jiz = jiz;
	}
	public String getFawmc() {
		return fawmc;
	}
	public void setFawmc(String fawmc) {
		this.fawmc = fawmc;
	}
	public String getNeweditor() {
		return neweditor;
	}
	public void setNeweditor(String neweditor) {
		this.neweditor = neweditor;
	}
	public String getEdittime() {
		return edittime;
	}
	public void setEdittime(String edittime) {
		this.edittime = edittime;
	}
	public String getZhuangt() {
		return zhuangt;
	}
	public void setZhuangt(String zhuangt) {
		this.zhuangt = zhuangt;
	}
	
	private String   zhuangt;
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getEditor() {
		return editor;
	}
	public void setEditor(String editor) {
		this.editor = editor;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPplbc() {
		return pplbc;
	}
	public void setPplbc(String pplbc) {
		this.pplbc = pplbc;
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
	public BigDecimal getBaozrl() {
		return baozrl;
	}
	public void setBaozrl(BigDecimal baozrl) {
		this.baozrl = baozrl;
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
	public String getYuanszzlx() {
		return yuanszzlx;
	}
	public void setYuanszzlx(String yuanszzlx) {
		this.yuanszzlx = yuanszzlx;
	}
	public String getP0xqzq() {
		return p0xqzq;
	}
	public void setP0xqzq(String p0xqzq) {
		this.p0xqzq = p0xqzq;
	}
	public BigDecimal getP0sl() {
		return p0sl;
	}
	public void setP0sl(BigDecimal p0sl) {
		this.p0sl = p0sl;
	}
	public BigDecimal getP1sl() {
		return p1sl;
	}
	public void setP1sl(BigDecimal p1sl) {
		this.p1sl = p1sl;
	}
	public BigDecimal getP2sl() {
		return p2sl;
	}
	public void setP2sl(BigDecimal p2sl) {
		this.p2sl = p2sl;
	}
	public BigDecimal getP3sl() {
		return p3sl;
	}
	public void setP3sl(BigDecimal p3sl) {
		this.p3sl = p3sl;
	}
	public BigDecimal getP4sl() {
		return p4sl;
	}
	public void setP4sl(BigDecimal p4sl) {
		this.p4sl = p4sl;
	}
	public BigDecimal getP5sl() {
		return p5sl;
	}
	public void setP5sl(BigDecimal p5sl) {
		this.p5sl = p5sl;
	}
	public BigDecimal getP6sl() {
		return p6sl;
	}
	public void setP6sl(BigDecimal p6sl) {
		this.p6sl = p6sl;
	}
	public BigDecimal getP7sl() {
		return p7sl;
	}
	public void setP7sl(BigDecimal p7sl) {
		this.p7sl = p7sl;
	}
	public BigDecimal getP8sl() {
		return p8sl;
	}
	public void setP8sl(BigDecimal p8sl) {
		this.p8sl = p8sl;
	}
	public BigDecimal getP9sl() {
		return p9sl;
	}
	public void setP9sl(BigDecimal p9sl) {
		this.p9sl = p9sl;
	}
	public BigDecimal getP10sl() {
		return p10sl;
	}
	public void setP10sl(BigDecimal p10sl) {
		this.p10sl = p10sl;
	}
	public BigDecimal getP11sl() {
		return p11sl;
	}
	public void setP11sl(BigDecimal p11sl) {
		this.p11sl = p11sl;
	}


}
