package com.athena.xqjs.entity.ppl;


import com.toft.core3.support.PageableSupport;

/**
 * PPL比较BEAN
 *
 */
public class ComparePpl extends PageableSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9002006104676387468L;
	 
	
	private String xuqbc	    ;  // ppl版次
	private String xuqbc1	    ;  // ppl版次
	private String xuqbc2	    ;  // ppl版次
	private String jiz	    ;  // ppl版次
	
	
	private String  creator  ;   //   创建人 
	private String  create_time; //   创建时间
	 
	private String  gongysmc;
	private String  gcbh;
	private String	chengysmc; 
	
	
	
	public String getGongysmc() {
		return gongysmc;
	}

	public void setGongysmc(String gongysmc) {
		this.gongysmc = gongysmc;
	}

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

	/**
	 * 用户中心
	 */
	private String usercenter;
	
	/**
	 * 零件编号
	 */
	private String lingjbh;
	
	/**
	 * 零件名称
	 */
	private String lingjmc;
	
	/**
	 * 供应商
	 */
	private String gongys;
	
	/**
	 * 制造路线
	 */
	private String zhizlx;
	
	/**
	 * 零件单位
	 */
	private String lingjdw;
	
	
	
	
	
	
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

	/**
	 * 一月数量1
	 */
	private String p1sl1;
	
	/**
	 * 一月数量2
	 */
	private String p1sl2;
	
	/**
	 * 一月差额
	 */
	private String p1;
	
	/**
	 * 二月数量1
	 */
	private String p2sl1;
	
	/**
	 * 二月数量2
	 */
	private String p2sl2;
	
	/**
	 * 二月差额
	 */
	private String p2;
	
	/**
	 * 三月数量1
	 */
	private String p3sl1;
	
	/**
	 * 三月数量2
	 */
	private String p3sl2;
	
	/**
	 * 三月差额
	 */
	private String p3;
	
	/**
	 * 四月数量1
	 */
	private String p4sl1;
	
	/**
	 * 四月数量2
	 */
	private String p4sl2;
	
	/**
	 * 四月差额
	 */
	private String p4;
	
	/**
	 * 五月数量1
	 */
	private String p5sl1;
	
	/**
	 * 五月数量2
	 */
	private String p5sl2;
	
	/**
	 * 五月差额
	 */
	private String p5;
	
	
	/**
	 * 六月数量1
	 */
	private String p6sl1;
	
	/**
	 * 六月数量2
	 */
	private String p6sl2;
	
	/**
	 * 六月差额
	 */
	private String p6;
	
	/**
	 * 七月数量1
	 */
	private String p7sl1;
	
	/**
	 * 七月数量2
	 */
	private String p7sl2;
	
	/**
	 * 七月差额
	 */
	private String p7;
	
	
	/**
	 * 八月数量1
	 */
	private String p8sl1;
	
	/**
	 * 八月数量2
	 */
	private String p8sl2;
	
	/**
	 * 八月差额
	 */
	private String p8;
	
	/**
	 * 九月数量1
	 */
	private String p9sl1;
	
	/**
	 * 九月数量2
	 */
	private String p9sl2;
	
	/**
	 * 九月差额
	 */
	private String p9;
	
	/**
	 * 十月数量1
	 */
	private String p10sl1;
	
	/**
	 * 十月数量2
	 */
	private String p10sl2;
	
	/**
	 * 十月差额
	 */
	private String p10;
	
	/**
	 * 十一月数量1
	 */
	private String p11sl1;
	
	/**
	 * 十一月数量2
	 */
	private String p11sl2;
	
	/**
	 * 十一月差额
	 */
	private String p11;
	
	/**
	 * 十二月数量1
	 */
	private String p12sl1;
	
	/**
	 * 十二月数量2
	 */
	private String p12sl2;
	
	/**
	 * 十二月差额
	 */
	private String p12;

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

	public String getGongys() {
		return gongys;
	}

	public void setGongys(String gongys) {
		this.gongys = gongys;
	}

	public String getZhizlx() {
		return zhizlx;
	}

	public void setZhizlx(String zhizlx) {
		this.zhizlx = zhizlx;
	}

	public String getLingjdw() {
		return lingjdw;
	}

	public void setLingjdw(String lingjdw) {
		this.lingjdw = lingjdw;
	}

	public String getP1sl1() {
		return p1sl1;
	}

	public void setP1sl1(String p1sl1) {
		this.p1sl1 = p1sl1;
	}

	public String getP1sl2() {
		return p1sl2;
	}

	public void setP1sl2(String p1sl2) {
		this.p1sl2 = p1sl2;
	}

	public String getP1() {
		return p1;
	}

	public void setP1(String p1) {
		if(p1 != null){
			this.p1 =p1+"%";
		}else{ 
			this.p1 = p1;
		}
	}

	public String getP2sl1() {
		return p2sl1;
	}

	public void setP2sl1(String p2sl1) {
		this.p2sl1 = p2sl1;
	}

	public String getP2sl2() {
		return p2sl2;
	}

	public void setP2sl2(String p2sl2) {
		this.p2sl2 = p2sl2;
	}

	public String getP2() {
		return p2;
	}

	public void setP2(String p2) {
		if(p2 != null){
			this.p2 = p2+"%";
		}else{ 
			this.p2 = p2;
	        }
	}		

	public String getP3sl1() {
		return p3sl1;
	}

	public void setP3sl1(String p3sl1) {
		this.p3sl1 = p3sl1;
	}

	public String getP3sl2() {
		return p3sl2;
	}

	public void setP3sl2(String p3sl2) {
		this.p3sl2 = p3sl2;
	}

	public String getP3() {
		return p3;
	}

	public void setP3(String p3) {
		if(p3 != null){
			this.p3 = p3+"%";
		}else{ 
			this.p3 = p3;
		}
	}

	public String getP4sl1() {
		return p4sl1;
	}

	public void setP4sl1(String p4sl1) {
		this.p4sl1 = p4sl1;
	}

	public String getP4sl2() {
		return p4sl2;
	}

	public void setP4sl2(String p4sl2) {
		this.p4sl2 = p4sl2;
	}

	public String getP4() {
		return p4;
	}

	public void setP4(String p4) {
		if(p4 != null){
			this.p4 = p4+"%";
		}else{ 
			this.p4 = p4;
		}
	}
		

	public String getP5sl1() {
		return p5sl1;
	}

	public void setP5sl1(String p5sl1) {
		this.p5sl1 = p5sl1;
	}

	public String getP5sl2() {
		return p5sl2;
	}

	public void setP5sl2(String p5sl2) {
		this.p5sl2 = p5sl2;
	}

	public String getP5() {
		return p5;
	}

	public void setP5(String p5) {
		if(p5 != null){
			this.p5 = p5+"%";
		}else{ 
			this.p5 = p5;
		}
	}

	public String getP6sl1() {
		return p6sl1;
	}

	public void setP6sl1(String p6sl1) {
		this.p6sl1 = p6sl1;
	}

	public String getP6sl2() {
		return p6sl2;
	}

	public void setP6sl2(String p6sl2) {
		this.p6sl2 = p6sl2;
	}

	public String getP6() {
		return p6;
	}

	public void setP6(String p6) {
		if(p6 != null){
			this.p6 = p6+"%";
		}else {
			this.p6 = p6;
		}
	}
		

	public String getP7sl1() {
		return p7sl1;
	}

	public void setP7sl1(String p7sl1) {
		this.p7sl1 = p7sl1;
	}

	public String getP7sl2() {
		return p7sl2;
	}

	public void setP7sl2(String p7sl2) {
		this.p7sl2 = p7sl2;
	}

	public String getP7() {
		return p7;
	}

	public void setP7(String p7) {
		if(p7 != null){
			this.p7 = p7+"%";
		}else{ 
			this.p7 = p7;
		}
	}

	public String getP8sl1() {
		return p8sl1;
	}

	public void setP8sl1(String p8sl1) {
		this.p8sl1 = p8sl1;
	}

	public String getP8sl2() {
		return p8sl2;
	}

	public void setP8sl2(String p8sl2) {
		this.p8sl2 = p8sl2;
	}

	public String getP8() {
		return p8;
	}

	public void setP8(String p8) {
		if(p8 != null){
			this.p8 = p8+"%";
		}else{ 
			this.p8 = p8;
		}
	}

	public String getP9sl1() {
		return p9sl1;
	}

	public void setP9sl1(String p9sl1) {
		this.p9sl1 = p9sl1;
	}

	public String getP9sl2() {
		return p9sl2;
	}

	public void setP9sl2(String p9sl2) {
		this.p9sl2 = p9sl2;
	}

	public String getP9() {
		return p9;
	}

	public void setP9(String p9) {
		if(p9 != null){
			this.p9 = p9+"%";
		}else{ 
			this.p9 = p9;
		}	
	}

	public String getP10sl1() {
		return p10sl1;
	}

	public void setP10sl1(String p10sl1) {
		this.p10sl1 = p10sl1;
	}

	public String getP10sl2() {
		return p10sl2;
	}

	public void setP10sl2(String p10sl2) {
		this.p10sl2 = p10sl2;
	}

	public String getP10() {
		return p10;
	}

	public void setP10(String p10) {
		if(p10 != null){
			this.p10 = p10+"%";
		}else{ 
			this.p10 = p10;
		}
	}

	public String getP11sl1() {
		return p11sl1;
	}

	public void setP11sl1(String p11sl1) {
		this.p11sl1 = p11sl1;
	}

	public String getP11sl2() {
		return p11sl2;
	}

	public void setP11sl2(String p11sl2) {
		this.p11sl2 = p11sl2;
	}

	public String getP11() {
		return p11;
	}

	public void setP11(String p11) {
		if(p11 != null){
			this.p11 =p11+"%";
		}else{ 
			this.p11 = p11;
		}
	}

	public String getP12sl1() {
		return p12sl1;
	}

	public void setP12sl1(String p12sl1) {
		this.p12sl1 = p12sl1;
	}

	public String getP12sl2() {
		return p12sl2;
	}

	public void setP12sl2(String p12sl2) {
		this.p12sl2 = p12sl2;
	}

	public String getP12() {
		return p12;
	}

	public void setP12(String p12) {
		if(p12 != null){
			this.p12 = p12+"%";
		}else{ 
			this.p12 = p12;
		}
	}

   //010724 xh 添加法文零件名称字段
	private String  fawmc;

	public String getFawmc() {
		return fawmc;
	}

	public void setFawmc(String fawmc) {
		this.fawmc = fawmc;
	}
	
	
}
