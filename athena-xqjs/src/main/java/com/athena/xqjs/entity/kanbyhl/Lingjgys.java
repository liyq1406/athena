package com.athena.xqjs.entity.kanbyhl;

import java.math.BigDecimal;

import com.toft.core3.support.PageableSupport;

public class Lingjgys extends PageableSupport{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 用户中心	          
	 */
	private      String        usercenter	   ;         
	/**
	 * 供应商编号	         
	 */
	private      String        gongysbh	     ;        
	/**
	 * 零件编号	          
	 */
	private      String        lingjbh	       ;       
	/**
	 * 供应合同号	         
	 */
	private      String        gongyhth	     ;         
	/**
	 * 供应份额	          
	 */
	private     BigDecimal      gongyfe	       ;       
	/**
	 * 有效期	             
	 */
	private     BigDecimal       youxq	         ;     
	/**
	 * 发运地	             
	 */
	private      String        fayd	         ;        
	/**
	 * 生效时间	          
	 */
	private      String        shengxsj       ;      	    
	/**
	 * 失效时间	           
	 */
	private      String        shixsj	       ;       	      
	/**
	 * 最小起订量	         
	 */
	private      BigDecimal        zuixqdl	    ;        
	/**
	 * 订单内容	           
	 */
	private      String        dingdnr	       ;        
	/**
	 * 参考阀值	          
	 */
	private      BigDecimal        cankfz	    ;       
	/**
	 * 质检抽检比例	       
	 */
	private      BigDecimal        zhijcjbl	     ;          
	/**
	 * 是否验证批次号	     
	 */
	private      String        shifyzpch	     ;      
	/**
	 * 包装类型	           
	 */
	private      String        baozlx	       ;         
	/**
	 * 供应商UC的包装类型	 
	 */
	private      String        ucbzlx	       ;         
	/**
	 * 供应商UC的容量	     
	 */
	private     BigDecimal      ucrl	         ;        
	/**
	 * 供应商UA的包装类型	 
	 */
	private      String        uabzlx	       ;        
	/**
	 * 供应商UA里UC的个数	 
	 */
	private      BigDecimal    uaucgs	       ;        
	/**
	 * 盖板	               
	 */
	private      String        gaib	         ;           
	/**
	 * 内衬	               
	 */
	private      String        neic           ;          
	/**
	 * 是否存在临时包装	   
	 */
	private      String        shifczlsbz	   ;
	
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	public String getGongysbh() {
		return gongysbh;
	}
	public void setGongysbh(String gongysbh) {
		this.gongysbh = gongysbh;
	}
	public String getLingjbh() {
		return lingjbh;
	}
	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}
	public String getGongyhth() {
		return gongyhth;
	}
	public void setGongyhth(String gongyhth) {
		this.gongyhth = gongyhth;
	}
	public BigDecimal getGongyfe() {
		return gongyfe;
	}
	public void setGongyfe(BigDecimal gongyfe) {
		this.gongyfe = gongyfe;
	}
	public BigDecimal getYouxq() {
		return youxq;
	}
	public void setYouxq(BigDecimal youxq) {
		this.youxq = youxq;
	}
	public String getFayd() {
		return fayd;
	}
	public void setFayd(String fayd) {
		this.fayd = fayd;
	}
	public String getShengxsj() {
		return shengxsj;
	}
	public void setShengxsj(String shengxsj) {
		this.shengxsj = shengxsj;
	}
	public String getShixsj() {
		return shixsj;
	}
	public void setShixsj(String shixsj) {
		this.shixsj = shixsj;
	}
	public BigDecimal getZuixqdl() {
		return zuixqdl;
	}
	public void setZuixqdl(BigDecimal zuixqdl) {
		this.zuixqdl = zuixqdl;
	}
	public String getDingdnr() {
		return dingdnr;
	}
	public void setDingdnr(String dingdnr) {
		this.dingdnr = dingdnr;
	}
	public BigDecimal getCankfz() {
		return cankfz;
	}
	public void setCankfz(BigDecimal cankfz) {
		this.cankfz = cankfz;
	}
	public BigDecimal getZhijcjbl() {
		return zhijcjbl;
	}
	public void setZhijcjbl(BigDecimal zhijcjbl) {
		this.zhijcjbl = zhijcjbl;
	}
	public String getShifyzpch() {
		return shifyzpch;
	}
	public void setShifyzpch(String shifyzpch) {
		this.shifyzpch = shifyzpch;
	}
	public String getBaozlx() {
		return baozlx;
	}
	public void setBaozlx(String baozlx) {
		this.baozlx = baozlx;
	}
	public String getUcbzlx() {
		return ucbzlx;
	}
	public void setUcbzlx(String ucbzlx) {
		this.ucbzlx = ucbzlx;
	}
	public BigDecimal getUcrl() {
		return ucrl;
	}
	public void setUcrl(BigDecimal ucrl) {
		this.ucrl = ucrl;
	}
	public String getUabzlx() {
		return uabzlx;
	}
	public void setUabzlx(String uabzlx) {
		this.uabzlx = uabzlx;
	}
	public BigDecimal getUaucgs() {
		return uaucgs;
	}
	public void setUaucgs(BigDecimal uaucgs) {
		this.uaucgs = uaucgs;
	}
	public String getGaib() {
		return gaib;
	}
	public void setGaib(String gaib) {
		this.gaib = gaib;
	}
	public String getNeic() {
		return neic;
	}
	public void setNeic(String neic) {
		this.neic = neic;
	}
	public String getShifczlsbz() {
		return shifczlsbz;
	}
	public void setShifczlsbz(String shifczlsbz) {
		this.shifczlsbz = shifczlsbz;
	}       
       
}
