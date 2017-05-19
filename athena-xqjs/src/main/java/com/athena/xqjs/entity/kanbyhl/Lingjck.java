package com.athena.xqjs.entity.kanbyhl;

import java.math.BigDecimal;

import com.toft.core3.support.PageableSupport;

public class Lingjck extends PageableSupport{

	private static final long serialVersionUID = 1L;
    
	/**
	 *   用户中心                           
	 */
	private   String     usercenter  ;                                
	/**
	 *   零件编号                           
	 */
	private   String     lingjbh	    ;                        
	/**
	 *   仓库编号                           
	 */
	private   String     cangkbh	    ; 	                         
	/**
	 *   子仓库编号                         
	 */
	private   String     zickbh		    ;                               
	/**
	 *   卸货站台编号                       
	 */
	private   String     xiehztbh	    ;                               
	/**
	 *   安全库存天数                       
	 */
	private   BigDecimal  anqkcts	    ;                               
	/**
	 *   安全库存数量                       
	 */
	private   BigDecimal  anqkcsl	    ;                                   
	/**
	 *   最大库存天数                       
	 */
	private   BigDecimal  zuidkcts	    ;                               
	/**
	 *   最大库存数量                       
	 */
	private   BigDecimal  zuidkcsl	    ;                                
	/**
	 *   订单表达总量                       
	 */
	private   BigDecimal  dingdbdzl		;                           
	/**
	 *   已交付总量                         
	 */
	private   BigDecimal   yijfzl		;                                 
	/**
	 *   系统调整值(年末 表达总量-交付总量) 
	 */
	private   BigDecimal   xittzz		 ;            
	/**
	 *   单取库编号                         
	 */
	private   String     danqkbh	    ;                             
	/*
	 *   单取库位(面列层010001/010101)      
	 */
	private   String     danqkw		    ;                                    
	/**
	 *   单取库位最大上限(包装个数)         
	 */
	private   BigDecimal zuidsx		    ;                    
	/**
	 *   单取库位最小下限(包装个数)         
	 */
	private   BigDecimal zuixxx		    ;                    
	/**
	 *   计算调整数值                       
	 */
	private   BigDecimal jistzz		    ;                            
	/**
	 *   生效时间                           
	 */
	private   String     shengxsj	    ;                                           
	/**
	 *   最小起订量(仓库)                   
	 */
	private   BigDecimal  zuixqdl	    ;                          
	/**
	 *   是否有零件序列号                   
	 */
	private   String     shifxlh	    ;                                         
	/**
	 *   备用子仓库编号                     
	 */
	private   String     beiykbh	    ;                                   
	/**
	 *   定置库位                           
	 */
	private   String     dingzkw	    ;                               
	/**
	 *   包装类型                           
	 */
	private   String     baozlx		    ;                                      
	/**
	 *   仓库US的包装类型                   
	 */
	private   String     usbzlx		    ;                                   
	/**
	 *   仓库US的包装容量                   
	 */
	private   BigDecimal usbzrl		    ;                                
	/**
	 *   上线UC类型                         
	 */
	private   String     uclx		     ;                                    
	/**
	 *  上线UC容量                         
	 */
	private   BigDecimal ucrl		     ;
	
	/**
	 * 创建时间
	 */
	private String create_time = null;

	/**
	 * 创建人
	 */
	private String creator = null;

	/**
	 * 修改时间
	 */
	private String edit_time = null;

	/**
	 * 修改人
	 */
	private String editor = null;

	/**
	 * 当前修改时间
	 */
	private String newEditTime = null;

	/**
	 * 当前修改人
	 */
	private String newEditor = null;
	/**
	 * 自动发货标志
	 */
	private String zidfhbz;
	
	
	private BigDecimal dingdlj;  //订单累计
	private BigDecimal jiaoflj;  //交付累计
	private BigDecimal zhongzlj; //终止
	private String ziyhqrq; //资源获取日期
	private String dingdljStr;
	private String jiaofljStr;
	private String zhongzljStr;
	private String jistzzStr;
	private String chej;
	private String kuc;
	private String jihy;
	
	public BigDecimal getDingdlj() {
		return dingdlj;
	}
	public void setDingdlj(BigDecimal dingdlj) {
		this.dingdlj = dingdlj;
	}
	public BigDecimal getJiaoflj() {
		return jiaoflj;
	}
	public void setJiaoflj(BigDecimal jiaoflj) {
		this.jiaoflj = jiaoflj;
	}
	public BigDecimal getZhongzlj() {
		return zhongzlj;
	}
	public void setZhongzlj(BigDecimal zhongzlj) {
		this.zhongzlj = zhongzlj;
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
	public String getCangkbh() {
		return cangkbh;
	}
	public void setCangkbh(String cangkbh) {
		this.cangkbh = cangkbh;
	}
	public String getZickbh() {
		return zickbh;
	}
	public void setZickbh(String zickbh) {
		this.zickbh = zickbh;
	}
	public String getXiehztbh() {
		return xiehztbh;
	}
	public void setXiehztbh(String xiehztbh) {
		this.xiehztbh = xiehztbh;
	}
	public BigDecimal getAnqkcts() {
		return anqkcts;
	}
	public void setAnqkcts(BigDecimal anqkcts) {
		this.anqkcts = anqkcts;
	}
	public BigDecimal getAnqkcsl() {
		return anqkcsl;
	}
	public void setAnqkcsl(BigDecimal anqkcsl) {
		this.anqkcsl = anqkcsl;
	}
	public BigDecimal getZuidkcts() {
		return zuidkcts;
	}
	public void setZuidkcts(BigDecimal zuidkcts) {
		this.zuidkcts = zuidkcts;
	}
	public BigDecimal getZuidkcsl() {
		return zuidkcsl;
	}
	public void setZuidkcsl(BigDecimal zuidkcsl) {
		this.zuidkcsl = zuidkcsl;
	}
	public BigDecimal getDingdbdzl() {
		return dingdbdzl;
	}
	public void setDingdbdzl(BigDecimal dingdbdzl) {
		this.dingdbdzl = dingdbdzl;
	}
	public BigDecimal getYijfzl() {
		return yijfzl;
	}
	public void setYijfzl(BigDecimal yijfzl) {
		this.yijfzl = yijfzl;
	}
	public BigDecimal getXittzz() {
		return xittzz;
	}
	public void setXittzz(BigDecimal xittzz) {
		this.xittzz = xittzz;
	}
	public String getDanqkbh() {
		return danqkbh;
	}
	public void setDanqkbh(String danqkbh) {
		this.danqkbh = danqkbh;
	}
	public String getDanqkw() {
		return danqkw;
	}
	public void setDanqkw(String danqkw) {
		this.danqkw = danqkw;
	}

	public String getCreate_time() {
		return create_time;
	}

	public String getZidfhbz() {
		return zidfhbz;
	}

	public void setZidfhbz(String zidfhbz) {
		this.zidfhbz = zidfhbz;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getEdit_time() {
		return edit_time;
	}

	public void setEdit_time(String edit_time) {
		this.edit_time = edit_time;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getNewEditTime() {
		return newEditTime;
	}

	public void setNewEditTime(String newEditTime) {
		this.newEditTime = newEditTime;
	}

	public String getNewEditor() {
		return newEditor;
	}

	public void setNewEditor(String newEditor) {
		this.newEditor = newEditor;
	}
	public BigDecimal getZuidsx() {
		return zuidsx;
	}
	public void setZuidsx(BigDecimal zuidsx) {
		this.zuidsx = zuidsx;
	}
	public BigDecimal getZuixxx() {
		return zuixxx;
	}
	public void setZuixxx(BigDecimal zuixxx) {
		this.zuixxx = zuixxx;
	}
	public BigDecimal getJistzz() {
		return jistzz;
	}
	public void setJistzz(BigDecimal jistzz) {
		this.jistzz = jistzz;
	}
	public String getShengxsj() {
		return shengxsj;
	}
	public void setShengxsj(String shengxsj) {
		this.shengxsj = shengxsj;
	}
	public BigDecimal getZuixqdl() {
		return zuixqdl;
	}
	public void setZuixqdl(BigDecimal zuixqdl) {
		this.zuixqdl = zuixqdl;
	}
	public String getShifxlh() {
		return shifxlh;
	}
	public void setShifxlh(String shifxlh) {
		this.shifxlh = shifxlh;
	}
	public String getBeiykbh() {
		return beiykbh;
	}
	public void setBeiykbh(String beiykbh) {
		this.beiykbh = beiykbh;
	}
	public String getDingzkw() {
		return dingzkw;
	}
	public void setDingzkw(String dingzkw) {
		this.dingzkw = dingzkw;
	}
	public String getBaozlx() {
		return baozlx;
	}
	public void setBaozlx(String baozlx) {
		this.baozlx = baozlx;
	}
	public String getUsbzlx() {
		return usbzlx;
	}
	public void setUsbzlx(String usbzlx) {
		this.usbzlx = usbzlx;
	}
	public BigDecimal getUsbzrl() {
		return usbzrl;
	}
	public void setUsbzrl(BigDecimal usbzrl) {
		this.usbzrl = usbzrl;
	}
	public String getUclx() {
		return uclx;
	}
	public void setUclx(String uclx) {
		this.uclx = uclx;
	}
	public BigDecimal getUcrl() {
		return ucrl;
	}
	public void setUcrl(BigDecimal ucrl) {
		this.ucrl = ucrl;
	}
	public String getZiyhqrq() {
		return ziyhqrq;
	}
	public void setZiyhqrq(String ziyhqrq) {
		this.ziyhqrq = ziyhqrq;
	}
	public String getDingdljStr() {
		return dingdljStr;
	}
	public void setDingdljStr(String dingdljStr) {
		this.dingdljStr = dingdljStr;
	}
	public String getJiaofljStr() {
		return jiaofljStr;
	}
	public void setJiaofljStr(String jiaofljStr) {
		this.jiaofljStr = jiaofljStr;
	}
	public String getZhongzljStr() {
		return zhongzljStr;
	}
	public void setZhongzljStr(String zhongzljStr) {
		this.zhongzljStr = zhongzljStr;
	}
	public String getJistzzStr() {
		return jistzzStr;
	}
	public void setJistzzStr(String jistzzStr) {
		this.jistzzStr = jistzzStr;
	}
	public String getChej() {
		return chej;
	}
	public void setChej(String chej) {
		this.chej = chej;
	}
	public String getKuc() {
		return kuc;
	}
	public void setKuc(String kuc) {
		this.kuc = kuc;
	}
	public String getJihy() {
		return jihy;
	}
	public void setJihy(String jihy) {
		this.jihy = jihy;
	}                                 
      
	
}
