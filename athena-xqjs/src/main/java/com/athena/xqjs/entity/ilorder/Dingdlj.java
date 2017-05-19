package com.athena.xqjs.entity.ilorder;

import java.math.BigDecimal;

import com.toft.core3.support.PageableSupport;

/**
 * 实体: 订单零件
 * 
 * @author
 * @version
 * 
 */
public class Dingdlj extends PageableSupport {
	private String guanjljjb;//制造路线
	
	/*xss-0011533*/
	private String lingjmc;//零件名称
	
	public String getLingjmc() {
		return lingjmc;
	}
	public void setLingjmc(String lingjmc) {
		this.lingjmc = lingjmc;
	}
	
	private String baozrl;//包装容量
	public String getBaozrl() {
		return baozrl;
	}
	public void setBaozrl(String baozrl) {
		this.baozrl = baozrl;
	}
	
	
	
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * P5数量
	 */
	private BigDecimal p5sl;
	/**
	 * P6数量
	 */
	private BigDecimal p6sl;
	/**
	 *P7数量
	 */
	private BigDecimal p7sl;
	/**
	 * P8数量
	 */
	private BigDecimal p8sl;
	/**
	 * P9数量
	 */
	private BigDecimal p9sl;
	/**
	 * P1日期
	 */
	private String p1rq;
	/**
	 * P2日期
	 */
	private String p2rq;
	/**
	 *P3日期
	 */
	private String p3rq;
	/**
	 * P4日期
	 */
	private String p4rq;
	/**
	 * P5日期
	 */
	private String p5rq;
	/**
	 * P6日期
	 */
	private String p6rq;
	/**
	 * P7期
	 */
	private String p7rq;
	/**
	 * P8日期
	 */
	private String p8rq;
	/**
	 * P9日期
	 */
	private String p9rq;

	/**
	 * 资源获取日期
	 */
	private String ziyhqrq;
	/**
	 * UA包装类型
	 */
	private String uabzlx;
	/**
	 * 调整计算值/订货安全库存
	 */
	private BigDecimal tiaozjsz;
	/**
	 * 订单制作时间
	 */
	private String dingdzzsj;
	/**
	 * 既定要货数量
	 */
	private BigDecimal jidyhsl;
	/**
	 * 备货周期
	 */
	private BigDecimal beihzq;
	/**
	 * 用户中心
	 */
	private String usercenter;
	/**
	 * 路径代码
	 */
	private String lujdm;
	/**
	 * 系统调整值
	 */
	private BigDecimal xittzz;
	/**
	 * 安全库存
	 */
	private BigDecimal anqkc;
	/**
	 * 供应商份额
	 */
	private BigDecimal gongysfe;
	/**
	 * 计划员组
	 */
	private String jihyz;
	/**
	 * UA包装内UC类型
	 */
	private String uabzuclx;
	/**
	 * US包装类型
	 */
	private String usbaozlx;
	/**
	 * 包装类型
	 */
	private String bzlx;
	/**
	 * 包装容量
	 */
	private BigDecimal bzrl;
	/**
	 * US包装容量
	 */
	private BigDecimal usbaozrl;
	/**
	 * P0发运周期序号
	 */
	private String p0fyzqxh;
	/**
	 * 零件号
	 */
	private String lingjbh;
	/**
	 * 订单号
	 */
	private String dingdh;
	/**
	 * UA包装内UC容量
	 */
	private BigDecimal uabzucrl;
	
	private BigDecimal shul;
	/**
	 * 交付累计
	 */
	private BigDecimal jiaoflj;
	/**
	 * P1数量
	 */
	private BigDecimal p1sl;
	
	
	/**
	 * 合同号
	 */
	private String heth;
	/**
	 * 供应商编号别名
	 */
	private String gongysdmid;
	
	private String xiehzt;
	
	
	
	
	/*
	 * xss _0013097
	 * */
	private BigDecimal p10sl;
	private BigDecimal p11sl;
	private BigDecimal p12sl;
	private BigDecimal p13sl;
	private BigDecimal p14sl;
	private BigDecimal p15sl;
	private String p10rq;
	private String p11rq;
	private String p12rq;
	private String p13rq;
	private String p14rq;
	private String p15rq;
		
	
	public BigDecimal getP15sl() {
		return p15sl;
	}
	public void setP15sl(BigDecimal p15sl) {
		this.p15sl = p15sl;
	}
	public String getP15rq() {
		return p15rq;
	}
	public void setP15rq(String p15rq) {
		this.p15rq = p15rq;
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
	public BigDecimal getP12sl() {
		return p12sl;
	}
	public void setP12sl(BigDecimal p12sl) {
		this.p12sl = p12sl;
	}
	public BigDecimal getP13sl() {
		return p13sl;
	}
	public void setP13sl(BigDecimal p13sl) {
		this.p13sl = p13sl;
	}
	public BigDecimal getP14sl() {
		return p14sl;
	}
	public void setP14sl(BigDecimal p14sl) {
		this.p14sl = p14sl;
	}
	public String getP10rq() {
		return p10rq;
	}
	public void setP10rq(String p10rq) {
		this.p10rq = p10rq;
	}
	public String getP11rq() {
		return p11rq;
	}
	public void setP11rq(String p11rq) {
		this.p11rq = p11rq;
	}
	public String getP12rq() {
		return p12rq;
	}
	public void setP12rq(String p12rq) {
		this.p12rq = p12rq;
	}
	public String getP13rq() {
		return p13rq;
	}
	public void setP13rq(String p13rq) {
		this.p13rq = p13rq;
	}
	public String getP14rq() {
		return p14rq;
	}
	public void setP14rq(String p14rq) {
		this.p14rq = p14rq;
	}
	public String getXiehzt() {
		return xiehzt;
	}

	public void setXiehzt(String xiehzt) {
		this.xiehzt = xiehzt;
	}

	public String getGongysdmid() {
		return gongysdmid;
	}

	public void setGongysdmid(String gongysdmid) {
		this.gongysdmid = gongysdmid;
	}

	public BigDecimal getShul() {
		return shul;
	}

	public void setShul(BigDecimal shul) {
		this.shul = shul;
	}
	/**
	 * 是否依赖待消耗
	 */
	private String shifyldxh;
	/**
	 * 供应商类型
	 */
	private String gongyslx;
	/**
	 * P4数量
	 */
	private BigDecimal p4sl;
	/**
	 * P0数量
	 */
	private BigDecimal p0sl;
	/**
	 * 订单内容
	 */
	private String dingdnr;
	/**
	 * 是否依赖待交付
	 */
	private String shifyldjf;
	/**
	 * 发货地
	 */
	private String fahd;
	/**
	 * 待消耗
	 */
	private BigDecimal daixh;
	/**
	 * 单位
	 */
	private String danw;
	/**
	 * ID
	 */
	private String id;
	
	private String gonghms ;
	
	
	/**
	 * P3数量
	 */
	private BigDecimal p3sl;

	public String getBzlx() {
		return bzlx;
	}

	public void setBzlx(String bzlx) {
		this.bzlx = bzlx;
	}

	public BigDecimal getBzrl() {
		return bzrl;
	}

	public void setBzrl(BigDecimal bzrl) {
		this.bzrl = bzrl;
	}
	/**
	 * 是否依赖库存
	 */
	private String shifylkc;
	/**
	 * P2数量
	 */
	private BigDecimal p2sl;
	/**
	 * 是否依赖安全库存
	 */
	private String shifylaqkc;
	/**
	 * 订单累计
	 */
	private BigDecimal dingdlj;
	/**
	 * 发运周期
	 */
	private BigDecimal fayzq;

	/**
	 * 发运周期
	 */
	private BigDecimal zuixqdl;

	
	/**
	 * 订货车间
	 */
	private String dinghcj;
	/**
	 * UA包装内UC数量
	 */
	private BigDecimal uabzucsl;
	/**
	 * 已交付量
	 */
	private BigDecimal yijfl;
	/**
	 * 供应商代码
	 */
	private String gongysdm;

	/**
	 * 库存
	 */
	private BigDecimal kuc;
	/**
	 * 仓库
	 */
	private String cangkdm;
	/**
	 * 交付码
	 */
	private String jiaofm;
	/**
	 * 路径
	 */
	private String lujz;
	/**
	 * 预告是否取整
	 */
	private String yugsfqz;

	/**
	 * 预告是否取整
	 */
	private String zhidgys;

	private BigDecimal zhongzlj;

	private String editor;

	private String edit_time;

	private String creator;

	private String create_time;

	private String active;

	private String lingjsx;
	
	/**
	 * 制造线路
	 */
	private String zhizlx;

	/**
	 * 当前修改时间
	 */
	private String newEditTime = null;

	/**
	 * 当前修改人
	 */
	private String newEditor = null;

	// -------新增字段 李智---------
	/**
	 * 要货令个数总量
	 */
	private Integer yaohlgszl;
	/**
	 * 要货令个数累计已交付
	 */
	private Integer yaohlgsljyjf;
	/**
	 * 要货令个数累计已终止
	 */
	private Integer yaohlgsljyzz;
	/**
	 * 订单累计（订单零件）
	 */
	private BigDecimal dingdljddlj;
	/**
	 * 交付累计（订单零件）
	 */
	private BigDecimal jiaofljddlj;
	/**
	 * 终止累计（订单零件）
	 */
	private BigDecimal zhongzljddlj;
	/**
	 * 状态
	 */
	private String dingdzt;
	/**
	 * 可终止数量
	 */
	private BigDecimal allowGzsl;

	/**
	 * 零件中文名称
	 */
	private String zhongwmc;

	/**
	 * 零件法文名称
	 */
	private String fawmc;

	/**
	 * 供应商名称
	 */
	private String gongsmc;
	
	/**
	 * 基准数量0
	 */
	private BigDecimal jizhunSlp0;
	/**
	 * 基准数量1
	 */
	private BigDecimal jizhunSlp1;
	/**
	 * 基准数量2
	 */
	private BigDecimal jizhunSlp2;
	/**
	 * 基准数量3
	 */
	private BigDecimal jizhunSlp3;
	
	/**
	 * 基准数量4
	 */
	private BigDecimal jizhunSlp4;
	/**
	 * 比较数量0
	 */
	private BigDecimal bijiaoSlp0;
	/**
	 * 比较数量1
	 */
	private BigDecimal bijiaoSlp1;
	/**
	 * 比较数量2
	 */
	private BigDecimal bijiaoSlp2;
	/**
	 * 比较数量3
	 */
	private BigDecimal bijiaoSlp3;
	
	/**
	 * 比较数量4
	 */
	private BigDecimal bijiaoSlp4;
	
	/**
	 * 比较后的差异百分比0
	 */
	private BigDecimal bijiaop0;
	/**
	 * 比较后的差异百分比1
	 */
	private BigDecimal bijiaop1;
	/**
	 * 比较后的差异百分比2
	 */
	private BigDecimal bijiaop2;
	/**
	 * 比较后的差异百分比3
	 */
	private BigDecimal bijiaop3;
	/**
	 * 比较后的差异百分比4
	 */
	private BigDecimal bijiaop4;
	//订单主单字段
	/**
	 * 处理类型
	 */
	private String chullx;

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
	/**
	 * 毛需求版次
	 */
	private String maoxqbc;
	
	// -------查询条件 李智 add ---------
	/**
	 * 3大/2小/1等于交付比例
	 */
	private String searchSymbols;

	/**
	 * 交付比例
	 */
	private String jfbl;

	/**
	 * 订单制作时间起始
	 */
	private String dingdzzsj_start;
	private String dingdzzsj_end;
	
	//-------毛需求明细(年度预告明细)字段(订单分析功能使用)---------
	/**
	 * 零件编号
	 */
	private String mxLingjbh;
	/**
	 * 中文名称
	 */
	private String mxZhongwmc;
	/**
	 * 单位
	 */
	private String mxDanw;
	/**
	 * 制造线路
	 */
	private String mxZhizlx;
	/**
	 * 使用车间
	 */
	private String mxShiycj;
	/**
	 * 用户中心
	 */
	private String mxUsercenter;
	/**
	 * 供应商编号
	 */
	private String mxGongysbh;
	/**
	 * p0日期
	 */
	private String mxP0zhouqxh;
	/**
	 * p0sl
	 */
	private BigDecimal mxP0sl;
	/**
	 * p1sl
	 */
	private BigDecimal mxP1sl;
	/**
	 * p2sl
	 */
	private BigDecimal mxP2sl;
	/**
	 * p3sl
	 */
	private BigDecimal mxP3sl;
	/**
	 * p4sl
	 */
	private BigDecimal mxP4sl;
	/**
	 * p5sl
	 */
	private BigDecimal mxP5sl;
	/**
	 * p6sl
	 */
	private BigDecimal mxP6sl;
	/**
	 * p7sl
	 */
	private BigDecimal mxP7sl;
	/**
	 * p8sl
	 */
	private BigDecimal mxP8sl;
	/**
	 * p9sl
	 */
	private BigDecimal mxP9sl;
	/**
	 * p10sl
	 */
	private BigDecimal mxP10sl;
	/**
	 * p11sl
	 */
	private BigDecimal mxP11sl;
	
	//-------------end---------------------
	
	//--------订单2零件的信息（用在订单和订单比较功能）---
	/**
	 * P1日期
	 */
	private String p1rq2;
	/**
	 * P2日期
	 */
	private String p2rq2;
	/**
	 *P3日期
	 */
	private String p3rq2;
	/**
	 * P4日期
	 */
	private String p4rq2;
	/**
	 * 订单号
	 */
	private String dingdh2;
	/**
	 * 零件号
	 */
	private String lingjbh2;
	/**
	 * 单位
	 */
	private String danw2;
	/**
	 * 中文名称
	 */
	private String zhongwmc2;
	/**
	 * 制造线路
	 */
	private String zhizxl2;
	/**
	 * 供应商编码
	 */
	private String gongysdm2;
	/**
	 * 用户中心
	 */
	private String usercenter2;
	/**
	 * p0
	 */
	private String p0fyzqxh2;
	/**
	 * p0数量
	 */
	private BigDecimal p0sl2;
	/**
	 * p1数量
	 */
	private BigDecimal p1sl2;
	/**
	 * p2数量
	 */
	private BigDecimal p2sl2;
	/**
	 * p3数量
	 */
	private BigDecimal p3sl2;
	/**
	 * p4数量
	 */
	private BigDecimal p4sl2;
	
	
	/**
	 * @return the mxP5sl
	 */
	public BigDecimal getMxP5sl() {
		return mxP5sl;
	}

	/**
	 * @param mxP5sl the mxP5sl to set
	 */
	public void setMxP5sl(BigDecimal mxP5sl) {
		this.mxP5sl = mxP5sl;
	}

	/**
	 * @return the mxP6sl
	 */
	public BigDecimal getMxP6sl() {
		return mxP6sl;
	}

	/**
	 * @param mxP6sl the mxP6sl to set
	 */
	public void setMxP6sl(BigDecimal mxP6sl) {
		this.mxP6sl = mxP6sl;
	}

	/**
	 * @return the mxP7sl
	 */
	public BigDecimal getMxP7sl() {
		return mxP7sl;
	}

	/**
	 * @param mxP7sl the mxP7sl to set
	 */
	public void setMxP7sl(BigDecimal mxP7sl) {
		this.mxP7sl = mxP7sl;
	}

	/**
	 * @return the mxP8sl
	 */
	public BigDecimal getMxP8sl() {
		return mxP8sl;
	}

	/**
	 * @param mxP8sl the mxP8sl to set
	 */
	public void setMxP8sl(BigDecimal mxP8sl) {
		this.mxP8sl = mxP8sl;
	}

	/**
	 * @return the mxP9sl
	 */
	public BigDecimal getMxP9sl() {
		return mxP9sl;
	}

	/**
	 * @param mxP9sl the mxP9sl to set
	 */
	public void setMxP9sl(BigDecimal mxP9sl) {
		this.mxP9sl = mxP9sl;
	}

	/**
	 * @return the mxP10sl
	 */
	public BigDecimal getMxP10sl() {
		return mxP10sl;
	}

	/**
	 * @param mxP10sl the mxP10sl to set
	 */
	public void setMxP10sl(BigDecimal mxP10sl) {
		this.mxP10sl = mxP10sl;
	}

	/**
	 * @return the mxP11sl
	 */
	public BigDecimal getMxP11sl() {
		return mxP11sl;
	}

	/**
	 * @param mxP11sl the mxP11sl to set
	 */
	public void setMxP11sl(BigDecimal mxP11sl) {
		this.mxP11sl = mxP11sl;
	}

	/**
	 * @return the jizhunSlp4
	 */
	public BigDecimal getJizhunSlp4() {
		return jizhunSlp4;
	}

	/**
	 * @param jizhunSlp4 the jizhunSlp4 to set
	 */
	public void setJizhunSlp4(BigDecimal jizhunSlp4) {
		this.jizhunSlp4 = jizhunSlp4;
	}

	/**
	 * @return the bijiaoSlp4
	 */
	public BigDecimal getBijiaoSlp4() {
		return bijiaoSlp4;
	}

	/**
	 * @param bijiaoSlp4 the bijiaoSlp4 to set
	 */
	public void setBijiaoSlp4(BigDecimal bijiaoSlp4) {
		this.bijiaoSlp4 = bijiaoSlp4;
	}

	/**
	 * @return the bijiaop4
	 */
	public BigDecimal getBijiaop4() {
		return bijiaop4;
	}

	/**
	 * @param bijiaop4 the bijiaop4 to set
	 */
	public void setBijiaop4(BigDecimal bijiaop4) {
		this.bijiaop4 = bijiaop4;
	}

	/**
	 * @return the p1rq2
	 */
	public String getP1rq2() {
		return p1rq2;
	}

	/**
	 * @param p1rq2 the p1rq2 to set
	 */
	public void setP1rq2(String p1rq2) {
		this.p1rq2 = p1rq2;
	}

	/**
	 * @return the p2rq2
	 */
	public String getP2rq2() {
		return p2rq2;
	}

	/**
	 * @param p2rq2 the p2rq2 to set
	 */
	public void setP2rq2(String p2rq2) {
		this.p2rq2 = p2rq2;
	}

	/**
	 * @return the p3rq2
	 */
	public String getP3rq2() {
		return p3rq2;
	}

	/**
	 * @param p3rq2 the p3rq2 to set
	 */
	public void setP3rq2(String p3rq2) {
		this.p3rq2 = p3rq2;
	}

	/**
	 * @return the p4rq2
	 */
	public String getP4rq2() {
		return p4rq2;
	}

	/**
	 * @param p4rq2 the p4rq2 to set
	 */
	public void setP4rq2(String p4rq2) {
		this.p4rq2 = p4rq2;
	}

	/**
	 * @return the dingdh2
	 */
	public String getDingdh2() {
		return dingdh2;
	}

	/**
	 * @param dingdh2 the dingdh2 to set
	 */
	public void setDingdh2(String dingdh2) {
		this.dingdh2 = dingdh2;
	}

	/**
	 * @return the lingjbh2
	 */
	public String getLingjbh2() {
		return lingjbh2;
	}

	/**
	 * @param lingjbh2 the lingjbh2 to set
	 */
	public void setLingjbh2(String lingjbh2) {
		this.lingjbh2 = lingjbh2;
	}

	/**
	 * @return the danw2
	 */
	public String getDanw2() {
		return danw2;
	}

	/**
	 * @param danw2 the danw2 to set
	 */
	public void setDanw2(String danw2) {
		this.danw2 = danw2;
	}

	/**
	 * @return the zhongwmc2
	 */
	public String getZhongwmc2() {
		return zhongwmc2;
	}

	/**
	 * @param zhongwmc2 the zhongwmc2 to set
	 */
	public void setZhongwmc2(String zhongwmc2) {
		this.zhongwmc2 = zhongwmc2;
	}

	/**
	 * @return the zhizxl2
	 */
	public String getZhizxl2() {
		return zhizxl2;
	}

	/**
	 * @param zhizxl2 the zhizxl2 to set
	 */
	public void setZhizxl2(String zhizxl2) {
		this.zhizxl2 = zhizxl2;
	}

	/**
	 * @return the gongysdm2
	 */
	public String getGongysdm2() {
		return gongysdm2;
	}

	/**
	 * @param gongysdm2 the gongysdm2 to set
	 */
	public void setGongysdm2(String gongysdm2) {
		this.gongysdm2 = gongysdm2;
	}

	/**
	 * @return the usercenter2
	 */
	public String getUsercenter2() {
		return usercenter2;
	}

	/**
	 * @param usercenter2 the usercenter2 to set
	 */
	public void setUsercenter2(String usercenter2) {
		this.usercenter2 = usercenter2;
	}

	/**
	 * @return the p0fyzqxh2
	 */
	public String getP0fyzqxh2() {
		return p0fyzqxh2;
	}

	/**
	 * @param p0fyzqxh2 the p0fyzqxh2 to set
	 */
	public void setP0fyzqxh2(String p0fyzqxh2) {
		this.p0fyzqxh2 = p0fyzqxh2;
	}

	/**
	 * @return the p0sl2
	 */
	public BigDecimal getP0sl2() {
		return p0sl2;
	}

	/**
	 * @param p0sl2 the p0sl2 to set
	 */
	public void setP0sl2(BigDecimal p0sl2) {
		this.p0sl2 = p0sl2;
	}

	/**
	 * @return the p1sl2
	 */
	public BigDecimal getP1sl2() {
		return p1sl2;
	}

	/**
	 * @param p1sl2 the p1sl2 to set
	 */
	public void setP1sl2(BigDecimal p1sl2) {
		this.p1sl2 = p1sl2;
	}

	/**
	 * @return the p2sl2
	 */
	public BigDecimal getP2sl2() {
		return p2sl2;
	}

	/**
	 * @param p2sl2 the p2sl2 to set
	 */
	public void setP2sl2(BigDecimal p2sl2) {
		this.p2sl2 = p2sl2;
	}

	/**
	 * @return the p3sl2
	 */
	public BigDecimal getP3sl2() {
		return p3sl2;
	}

	/**
	 * @param p3sl2 the p3sl2 to set
	 */
	public void setP3sl2(BigDecimal p3sl2) {
		this.p3sl2 = p3sl2;
	}

	/**
	 * @return the p4sl2
	 */
	public BigDecimal getP4sl2() {
		return p4sl2;
	}

	/**
	 * @param p4sl2 the p4sl2 to set
	 */
	public void setP4sl2(BigDecimal p4sl2) {
		this.p4sl2 = p4sl2;
	}

	/**
	 * @return the zhizxl
	 */
	public String getZhizlx() {
		return zhizlx;
	}

	/**
	 * @param zhizxl the zhizxl to set
	 */
	public void setZhizlx(String zhizxl) {
		this.zhizlx = zhizxl;
	}

	/**
	 * @return the mxLingjbh
	 */
	public String getMxLingjbh() {
		return mxLingjbh;
	}

	/**
	 * @param mxLingjbh the mxLingjbh to set
	 */
	public void setMxLingjbh(String mxLingjbh) {
		this.mxLingjbh = mxLingjbh;
	}

	/**
	 * @return the mxZhongwmc
	 */
	public String getMxZhongwmc() {
		return mxZhongwmc;
	}

	/**
	 * @param mxZhongwmc the mxZhongwmc to set
	 */
	public void setMxZhongwmc(String mxZhongwmc) {
		this.mxZhongwmc = mxZhongwmc;
	}

	/**
	 * @return the mxDanw
	 */
	public String getMxDanw() {
		return mxDanw;
	}

	/**
	 * @param mxDanw the mxDanw to set
	 */
	public void setMxDanw(String mxDanw) {
		this.mxDanw = mxDanw;
	}

	/**
	 * @return the mxZhizlx
	 */
	public String getMxZhizlx() {
		return mxZhizlx;
	}

	/**
	 * @param mxZhizlx the mxZhizlx to set
	 */
	public void setMxZhizlx(String mxZhizlx) {
		this.mxZhizlx = mxZhizlx;
	}

	/**
	 * @return the mxShiycj
	 */
	public String getMxShiycj() {
		return mxShiycj;
	}

	/**
	 * @param mxShiycj the mxShiycj to set
	 */
	public void setMxShiycj(String mxShiycj) {
		this.mxShiycj = mxShiycj;
	}

	/**
	 * @return the mxUsercenter
	 */
	public String getMxUsercenter() {
		return mxUsercenter;
	}

	/**
	 * @param mxUsercenter the mxUsercenter to set
	 */
	public void setMxUsercenter(String mxUsercenter) {
		this.mxUsercenter = mxUsercenter;
	}

	/**
	 * @return the mxGongysbh
	 */
	public String getMxGongysbh() {
		return mxGongysbh;
	}

	/**
	 * @param mxGongysbh the mxGongysbh to set
	 */
	public void setMxGongysbh(String mxGongysbh) {
		this.mxGongysbh = mxGongysbh;
	}

	/**
	 * @return the mxP0zhouqxh
	 */
	public String getMxP0zhouqxh() {
		return mxP0zhouqxh;
	}

	/**
	 * @param mxP0zhouqxh the mxP0zhouqxh to set
	 */
	public void setMxP0zhouqxh(String mxP0zhouqxh) {
		this.mxP0zhouqxh = mxP0zhouqxh;
	}

	/**
	 * @return the mxP0sl
	 */
	public BigDecimal getMxP0sl() {
		return mxP0sl;
	}

	/**
	 * @param mxP0sl the mxP0sl to set
	 */
	public void setMxP0sl(BigDecimal mxP0sl) {
		this.mxP0sl = mxP0sl;
	}

	/**
	 * @return the mxP1sl
	 */
	public BigDecimal getMxP1sl() {
		return mxP1sl;
	}

	/**
	 * @param mxP1sl the mxP1sl to set
	 */
	public void setMxP1sl(BigDecimal mxP1sl) {
		this.mxP1sl = mxP1sl;
	}

	/**
	 * @return the mxP2sl
	 */
	public BigDecimal getMxP2sl() {
		return mxP2sl;
	}

	/**
	 * @param mxP2sl the mxP2sl to set
	 */
	public void setMxP2sl(BigDecimal mxP2sl) {
		this.mxP2sl = mxP2sl;
	}

	/**
	 * @return the mxP3sl
	 */
	public BigDecimal getMxP3sl() {
		return mxP3sl;
	}

	/**
	 * @param mxP3sl the mxP3sl to set
	 */
	public void setMxP3sl(BigDecimal mxP3sl) {
		this.mxP3sl = mxP3sl;
	}

	/**
	 * @return the mxP4sl
	 */
	public BigDecimal getMxP4sl() {
		return mxP4sl;
	}

	/**
	 * @param mxP4sl the mxP4sl to set
	 */
	public void setMxP4sl(BigDecimal mxP4sl) {
		this.mxP4sl = mxP4sl;
	}

	public BigDecimal getBijiaop0() {
		return bijiaop0;
	}

	public void setBijiaop0(BigDecimal bijiaop0) {
		this.bijiaop0 = bijiaop0;
	}

	public BigDecimal getBijiaop1() {
		return bijiaop1;
	}

	public void setBijiaop1(BigDecimal bijiaop1) {
		this.bijiaop1 = bijiaop1;
	}

	public BigDecimal getBijiaop2() {
		return bijiaop2;
	}

	public void setBijiaop2(BigDecimal bijiaop2) {
		this.bijiaop2 = bijiaop2;
	}

	public BigDecimal getBijiaop3() {
		return bijiaop3;
	}

	public void setBijiaop3(BigDecimal bijiaop3) {
		this.bijiaop3 = bijiaop3;
	}

	public BigDecimal getJizhunSlp0() {
		return jizhunSlp0;
	}

	public void setJizhunSlp0(BigDecimal jizhunSlp0) {
		this.jizhunSlp0 = jizhunSlp0;
	}

	public BigDecimal getJizhunSlp1() {
		return jizhunSlp1;
	}

	public void setJizhunSlp1(BigDecimal jizhunSlp1) {
		this.jizhunSlp1 = jizhunSlp1;
	}

	public BigDecimal getJizhunSlp2() {
		return jizhunSlp2;
	}

	public void setJizhunSlp2(BigDecimal jizhunSlp2) {
		this.jizhunSlp2 = jizhunSlp2;
	}

	public BigDecimal getJizhunSlp3() {
		return jizhunSlp3;
	}

	public void setJizhunSlp3(BigDecimal jizhunSlp3) {
		this.jizhunSlp3 = jizhunSlp3;
	}

	public BigDecimal getBijiaoSlp0() {
		return bijiaoSlp0;
	}

	public void setBijiaoSlp0(BigDecimal bijiaoSlp0) {
		this.bijiaoSlp0 = bijiaoSlp0;
	}

	public BigDecimal getBijiaoSlp1() {
		return bijiaoSlp1;
	}

	public void setBijiaoSlp1(BigDecimal bijiaoSlp1) {
		this.bijiaoSlp1 = bijiaoSlp1;
	}

	public BigDecimal getBijiaoSlp2() {
		return bijiaoSlp2;
	}

	public void setBijiaoSlp2(BigDecimal bijiaoSlp2) {
		this.bijiaoSlp2 = bijiaoSlp2;
	}

	public BigDecimal getBijiaoSlp3() {
		return bijiaoSlp3;
	}

	public void setBijiaoSlp3(BigDecimal bijiaoSlp3) {
		this.bijiaoSlp3 = bijiaoSlp3;
	}

	public String getChullx() {
		return chullx;
	}

	public void setChullx(String chullx) {
		this.chullx = chullx;
	}

	public String getMaoxqbc() {
		return maoxqbc;
	}

	public void setMaoxqbc(String maoxqbc) {
		this.maoxqbc = maoxqbc;
	}

	

	public String getLingjsx() {
		return lingjsx;
	}

	public void setLingjsx(String lingjsx) {
		this.lingjsx = lingjsx;
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

	public String getP1rq() {
		return p1rq;
	}

	public void setP1rq(String p1rq) {
		this.p1rq = p1rq;
	}

	public String getP2rq() {
		return p2rq;
	}

	public void setP2rq(String p2rq) {
		this.p2rq = p2rq;
	}

	public String getP3rq() {
		return p3rq;
	}

	public void setP3rq(String p3rq) {
		this.p3rq = p3rq;
	}

	public String getP4rq() {
		return p4rq;
	}

	public void setP4rq(String p4rq) {
		this.p4rq = p4rq;
	}

	public String getP5rq() {
		return p5rq;
	}

	public void setP5rq(String p5rq) {
		this.p5rq = p5rq;
	}

	public String getP6rq() {
		return p6rq;
	}

	public void setP6rq(String p6rq) {
		this.p6rq = p6rq;
	}

	public String getP7rq() {
		return p7rq;
	}

	public void setP7rq(String p7rq) {
		this.p7rq = p7rq;
	}

	public String getP8rq() {
		return p8rq;
	}

	public void setP8rq(String p8rq) {
		this.p8rq = p8rq;
	}

	public String getP9rq() {
		return p9rq;
	}

	public void setP9rq(String p9rq) {
		this.p9rq = p9rq;
	}

	/**
	 * @return the yaohlgszl
	 */
	public Integer getYaohlgszl() {
		return yaohlgszl;
	}

	/**
	 * @param yaohlgszl the yaohlgszl to set
	 */
	public void setYaohlgszl(Integer yaohlgszl) {
		this.yaohlgszl = yaohlgszl;
	}

	/**
	 * @return the yaohlgsljyjf
	 */
	public Integer getYaohlgsljyjf() {
		return yaohlgsljyjf;
	}

	/**
	 * @param yaohlgsljyjf the yaohlgsljyjf to set
	 */
	public void setYaohlgsljyjf(Integer yaohlgsljyjf) {
		this.yaohlgsljyjf = yaohlgsljyjf;
	}

	/**
	 * @return the yaohlgsljyzz
	 */
	public Integer getYaohlgsljyzz() {
		return yaohlgsljyzz;
	}

	/**
	 * @param yaohlgsljyzz the yaohlgsljyzz to set
	 */
	public void setYaohlgsljyzz(Integer yaohlgsljyzz) {
		this.yaohlgsljyzz = yaohlgsljyzz;
	}

	public BigDecimal getDingdljddlj() {
		return dingdljddlj;
	}

	public void setDingdljddlj(BigDecimal dingdljddlj) {
		this.dingdljddlj = dingdljddlj;
	}

	public BigDecimal getJiaofljddlj() {
		return jiaofljddlj;
	}

	public void setJiaofljddlj(BigDecimal jiaofljddlj) {
		this.jiaofljddlj = jiaofljddlj;
	}

	public BigDecimal getZhongzljddlj() {
		return zhongzljddlj;
	}

	public void setZhongzljddlj(BigDecimal zhongzljddlj) {
		this.zhongzljddlj = zhongzljddlj;
	}

	public String getZhongwmc() {
		return zhongwmc;
	}

	public void setZhongwmc(String zhongwmc) {
		this.zhongwmc = zhongwmc;
	}

	public String getFawmc() {
		return fawmc;
	}

	public void setFawmc(String fawmc) {
		this.fawmc = fawmc;
	}

	public String getGongsmc() {
		return gongsmc;
	}

	public void setGongsmc(String gongsmc) {
		this.gongsmc = gongsmc;
	}

	public BigDecimal getAllowGzsl() {
		return allowGzsl;
	}

	public void setAllowGzsl(BigDecimal allowGzsl) {
		this.allowGzsl = allowGzsl;
	}

	public String getDingdzt() {
		return dingdzt;
	}

	public void setDingdzt(String dingdzt) {
		this.dingdzt = dingdzt;
	}

	public String getSearchSymbols() {
		return searchSymbols;
	}

	public void setSearchSymbols(String searchSymbols) {
		this.searchSymbols = searchSymbols;
	}

	public String getJfbl() {
		return jfbl;
	}

	public void setJfbl(String jfbl) {
		this.jfbl = jfbl;
	}

	public String getDingdzzsj_start() {
		return dingdzzsj_start;
	}

	public void setDingdzzsj_start(String dingdzzsj_start) {
		this.dingdzzsj_start = dingdzzsj_start;
	}

	public String getDingdzzsj_end() {
		return dingdzzsj_end;
	}

	public void setDingdzzsj_end(String dingdzzsj_end) {
		this.dingdzzsj_end = dingdzzsj_end;
	}

	public BigDecimal getZhongzlj() {
		return zhongzlj;
	}

	public void setZhongzlj(BigDecimal zhongzlj) {
		this.zhongzlj = zhongzlj;
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

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getZhidgys() {
		return zhidgys;
	}

	public void setZhidgys(String zhidgys) {
		this.zhidgys = zhidgys;
	}

	/**
	 * 取得 - 预告是否取整.
	 * 
	 * @return the 预告是否取整
	 */
	public String getYugsfqz() {
		return yugsfqz;
	}

	/**
	 * 设置 - 预告是否取整.
	 * 
	 * @param baozrl
	 *            预告是否取整
	 */
	public void setYugsfqz(String yugsfqz) {
		this.yugsfqz = yugsfqz;
	}

	public String getLujz() {
		return lujz;
	}

	public void setLujz(String lujz) {
		this.lujz = lujz;
	}

	/**
	 * 取得 - 交付码.
	 * 
	 * @return the 交付码
	 */
	public String getJiaofm() {
		return jiaofm;
	}

	/**
	 * 设置 - 交付码.
	 * 
	 * @param jiaofm
	 *            交付码
	 */
	public void setJiaofm(String jiaofm) {
		this.jiaofm = jiaofm;
	}

	/**
	 * 取得 - 仓库.
	 * 
	 * @return the 仓库
	 */
	

	/**
	 * 取得 - 资源获取日期.
	 * 
	 * @return the 资源获取日期
	 */
	public String getZiyhqrq() {
		return ziyhqrq;
	}

	public String getCangkdm() {
		return cangkdm;
	}

	public void setCangkdm(String cangkdm) {
		this.cangkdm = cangkdm;
	}

	/**
	 * 设置 - 资源获取日期.
	 * 
	 * @param ziyhqrq
	 *            资源获取日期
	 */
	public void setZiyhqrq(String ziyhqrq) {
		this.ziyhqrq = ziyhqrq;
	}

	/**
	 * 取得 - uA包装类型.
	 * 
	 * @return the uA包装类型
	 */
	public String getUabzlx() {
		return uabzlx;
	}

	/**
	 * 设置 - uA包装类型.
	 * 
	 * @param uabzlx
	 *            uA包装类型
	 */
	public void setUabzlx(String uabzlx) {
		this.uabzlx = uabzlx;
	}

	/**
	 * 取得 - 调整计算值/订货安全库存.
	 * 
	 * @return the 调整计算值/订货安全库存
	 */
	public BigDecimal getTiaozjsz() {
		return tiaozjsz;
	}

	public String getGonghms() {
		return gonghms;
	}

	public void setGonghms(String gonghms) {
		this.gonghms = gonghms;
	}

	/**
	 * 设置 - 调整计算值/订货安全库存.
	 * 
	 * @param tiaozjsz
	 *            调整计算值/订货安全库存
	 */
	public void setTiaozjsz(BigDecimal tiaozjsz) {
		this.tiaozjsz = tiaozjsz;
	}

	/**
	 * 取得 - 订单制作时间.
	 * 
	 * @return the 订单制作时间
	 */
	public String getDingdzzsj() {
		return dingdzzsj;
	}

	/**
	 * 设置 - 订单制作时间.
	 * 
	 * @param dingdzzsj
	 *            订单制作时间
	 */
	public void setDingdzzsj(String dingdzzsj) {
		this.dingdzzsj = dingdzzsj;
	}

	/**
	 * 取得 - 既定要货数量.
	 * 
	 * @return the 既定要货数量
	 */
	public BigDecimal getJidyhsl() {
		return jidyhsl;
	}

	/**
	 * 设置 - 既定要货数量.
	 * 
	 * @param jidyhsl
	 *            既定要货数量
	 */
	public void setJidyhsl(BigDecimal jidyhsl) {
		this.jidyhsl = jidyhsl;
	}

	/**
	 * 取得 - 备货周期.
	 * 
	 * @return the 备货周期
	 */
	public BigDecimal getBeihzq() {
		return beihzq;
	}

	/**
	 * 设置 - 备货周期.
	 * 
	 * @param beihzq
	 *            备货周期
	 */
	public void setBeihzq(BigDecimal beihzq) {
		this.beihzq = beihzq;
	}

	/**
	 * 取得 - 用户中心.
	 * 
	 * @return the 用户中心
	 */
	public String getUsercenter() {
		return usercenter;
	}

	/**
	 * 设置 - 用户中心.
	 * 
	 * @param usercenter
	 *            用户中心
	 */
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	/**
	 * 取得 - 路径代码.
	 * 
	 * @return the 路径代码
	 */
	public String getLujdm() {
		return lujdm;
	}

	/**
	 * 设置 - 路径代码.
	 * 
	 * @param lujdm
	 *            路径代码
	 */
	public void setLujdm(String lujdm) {
		this.lujdm = lujdm;
	}

	/**
	 * 取得 - 系统调整值.
	 * 
	 * @return the 系统调整值
	 */
	public BigDecimal getXittzz() {
		return xittzz;
	}

	/**
	 * 设置 - 系统调整值.
	 * 
	 * @param xittzz
	 *            系统调整值
	 */
	public void setXittzz(BigDecimal xittzz) {
		this.xittzz = xittzz;
	}

	/**
	 * 取得 - 安全库存.
	 * 
	 * @return the 安全库存
	 */
	public BigDecimal getAnqkc() {
		return anqkc;
	}

	/**
	 * 设置 - 安全库存.
	 * 
	 * @param anqkc
	 *            安全库存
	 */
	public void setAnqkc(BigDecimal anqkc) {
		this.anqkc = anqkc;
	}

	/**
	 * 取得 - 供应商份额.
	 * 
	 * @return the 供应商份额
	 */
	public BigDecimal getGongysfe() {
		return gongysfe;
	}

	/**
	 * 设置 - 供应商份额.
	 * 
	 * @param gongysfe
	 *            供应商份额
	 */
	public void setGongysfe(BigDecimal gongysfe) {
		this.gongysfe = gongysfe;
	}

	/**
	 * 取得 - 计划员组.
	 * 
	 * @return the 计划员组
	 */
	public String getJihyz() {
		return jihyz;
	}

	/**
	 * 设置 - 计划员组.
	 * 
	 * @param jihyz
	 *            计划员组
	 */
	public void setJihyz(String jihyz) {
		this.jihyz = jihyz;
	}

	/**
	 * 取得 - uA包装内UC类型.
	 * 
	 * @return the uA包装内UC类型
	 */
	public String getUabzuclx() {
		return uabzuclx;
	}

	/**
	 * 设置 - uA包装内UC类型.
	 * 
	 * @param uabzuclx
	 *            uA包装内UC类型
	 */
	public void setUabzuclx(String uabzuclx) {
		this.uabzuclx = uabzuclx;
	}

	/**
	 * 取得 - p0发运周期序号.
	 * 
	 * @return the p0发运周期序号
	 */
	public String getP0fyzqxh() {
		return p0fyzqxh;
	}

	/**
	 * 设置 - p0发运周期序号.
	 * 
	 * @param p0fyzqxh
	 *            p0发运周期序号
	 */
	public void setP0fyzqxh(String p0fyzqxh) {
		this.p0fyzqxh = p0fyzqxh;
	}

	/**
	 * 取得 - 零件号.
	 * 
	 * @return the 零件号
	 */
	public String getLingjbh() {
		return lingjbh;
	}

	/**
	 * 设置 - 零件号.
	 * 
	 * @param lingjbh
	 *            零件号
	 */
	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}

	/**
	 * 取得 - 订单号.
	 * 
	 * @return the 订单号
	 */
	public String getDingdh() {
		return dingdh;
	}

	/**
	 * 设置 - 订单号.
	 * 
	 * @param dingdh
	 *            订单号
	 */
	public void setDingdh(String dingdh) {
		this.dingdh = dingdh;
	}

	/**
	 * 取得 - uA包装内UC容量.
	 * 
	 * @return the uA包装内UC容量
	 */
	public BigDecimal getUabzucrl() {
		return uabzucrl;
	}

	/**
	 * 设置 - uA包装内UC容量.
	 * 
	 * @param uabzucrl
	 *            uA包装内UC容量
	 */
	public void setUabzucrl(BigDecimal uabzucrl) {
		this.uabzucrl = uabzucrl;
	}

	/**
	 * 取得 - 交付累计.
	 * 
	 * @return the 交付累计
	 */
	public BigDecimal getJiaoflj() {
		return jiaoflj;
	}

	/**
	 * 设置 - 交付累计.
	 * 
	 * @param jiaoflj
	 *            交付累计
	 */
	public void setJiaoflj(BigDecimal jiaoflj) {
		this.jiaoflj = jiaoflj;
	}

	/**
	 * 取得 - p1数量.
	 * 
	 * @return the p1数量
	 */
	public BigDecimal getP1sl() {
		return p1sl;
	}

	/**
	 * 设置 - p1数量.
	 * 
	 * @param p1sl
	 *            p1数量
	 */
	public void setP1sl(BigDecimal p1sl) {
		this.p1sl = p1sl;
	}

	/**
	 * 取得 - 是否依赖待消耗.
	 * 
	 * @return the 是否依赖待消耗
	 */
	public String getShifyldxh() {
		return shifyldxh;
	}

	/**
	 * 设置 - 是否依赖待消耗.
	 * 
	 * @param shifyldxh
	 *            是否依赖待消耗
	 */
	public void setShifyldxh(String shifyldxh) {
		this.shifyldxh = shifyldxh;
	}

	/**
	 * 取得 - 供应商类型.
	 * 
	 * @return the 供应商类型
	 */
	public String getGongyslx() {
		return gongyslx;
	}

	/**
	 * 设置 - 供应商类型.
	 * 
	 * @param gongyslx
	 *            供应商类型
	 */
	public void setGongyslx(String gongyslx) {
		this.gongyslx = gongyslx;
	}

	/**
	 * 取得 - p4数量.
	 * 
	 * @return the p4数量
	 */
	public BigDecimal getP4sl() {
		return p4sl;
	}

	/**
	 * 设置 - p4数量.
	 * 
	 * @param p4sl
	 *            p4数量
	 */
	public void setP4sl(BigDecimal p4sl) {
		this.p4sl = p4sl;
	}

	/**
	 * 取得 - p0数量.
	 * 
	 * @return the p0数量
	 */
	public BigDecimal getP0sl() {
		return p0sl;
	}

	/**
	 * 设置 - p0数量.
	 * 
	 * @param p0sl
	 *            p0数量
	 */
	public void setP0sl(BigDecimal p0sl) {
		this.p0sl = p0sl;
	}

	/**
	 * 取得 - 订单内容.
	 * 
	 * @return the 订单内容
	 */
	public String getDingdnr() {
		return dingdnr;
	}

	/**
	 * 设置 - 订单内容.
	 * 
	 * @param dingdnr
	 *            订单内容
	 */
	public void setDingdnr(String dingdnr) {
		this.dingdnr = dingdnr;
	}

	/**
	 * 取得 - 是否依赖待交付.
	 * 
	 * @return the 是否依赖待交付
	 */
	public String getShifyldjf() {
		return shifyldjf;
	}

	/**
	 * 设置 - 是否依赖待交付.
	 * 
	 * @param shifyldjf
	 *            是否依赖待交付
	 */
	public void setShifyldjf(String shifyldjf) {
		this.shifyldjf = shifyldjf;
	}

	/**
	 * 取得 - 发货地.
	 * 
	 * @return the 发货地
	 */
	public String getFahd() {
		return fahd;
	}

	/**
	 * 设置 - 发货地.
	 * 
	 * @param fahd
	 *            发货地
	 */
	public void setFahd(String fahd) {
		this.fahd = fahd;
	}

	/**
	 * 取得 - 待消耗.
	 * 
	 * @return the 待消耗
	 */
	public BigDecimal getDaixh() {
		return daixh;
	}

	/**
	 * 设置 - 待消耗.
	 * 
	 * @param daixh
	 *            待消耗
	 */
	public void setDaixh(BigDecimal daixh) {
		this.daixh = daixh;
	}

	/**
	 * 取得 - 单位.
	 * 
	 * @return the 单位
	 */
	public String getDanw() {
		return danw;
	}

	/**
	 * 设置 - 单位.
	 * 
	 * @param danw
	 *            单位
	 */
	public void setDanw(String danw) {
		this.danw = danw;
	}

	/**
	 * 取得 - iD.
	 * 
	 * @return the iD
	 */
	public String getId() {
		return id;
	}

	/**
	 * 设置 - iD.
	 * 
	 * @param id
	 *            iD
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 取得 - p3数量.
	 * 
	 * @return the p3数量
	 */
	public BigDecimal getP3sl() {
		return p3sl;
	}

	/**
	 * 设置 - p3数量.
	 * 
	 * @param p3sl
	 *            p3数量
	 */
	public void setP3sl(BigDecimal p3sl) {
		this.p3sl = p3sl;
	}

	/**
	 * 取得 - 是否依赖库存.
	 * 
	 * @return the 是否依赖库存
	 */
	public String getShifylkc() {
		return shifylkc;
	}

	/**
	 * 设置 - 是否依赖库存.
	 * 
	 * @param shifylkc
	 *            是否依赖库存
	 */
	public void setShifylkc(String shifylkc) {
		this.shifylkc = shifylkc;
	}

	/**
	 * 取得 - p2数量.
	 * 
	 * @return the p2数量
	 */
	public BigDecimal getP2sl() {
		return p2sl;
	}

	/**
	 * 设置 - p2数量.
	 * 
	 * @param p2sl
	 *            p2数量
	 */
	public void setP2sl(BigDecimal p2sl) {
		this.p2sl = p2sl;
	}

	/**
	 * 取得 - 是否依赖安全库存.
	 * 
	 * @return the 是否依赖安全库存
	 */
	public String getShifylaqkc() {
		return shifylaqkc;
	}

	/**
	 * 设置 - 是否依赖安全库存.
	 * 
	 * @param shifylaqkc
	 *            是否依赖安全库存
	 */
	public void setShifylaqkc(String shifylaqkc) {
		this.shifylaqkc = shifylaqkc;
	}

	/**
	 * 取得 - 订单累计.
	 * 
	 * @return the 订单累计
	 */
	public BigDecimal getDingdlj() {
		return dingdlj;
	}

	/**
	 * 设置 - 订单累计.
	 * 
	 * @param dingdlj
	 *            订单累计
	 */
	public void setDingdlj(BigDecimal dingdlj) {
		this.dingdlj = dingdlj;
	}

	/**
	 * 取得 - 发运周期.
	 * 
	 * @return the 发运周期
	 */
	public BigDecimal getFayzq() {
		return fayzq;
	}

	/**
	 * 设置 - 发运周期.
	 * 
	 * @param fayzq
	 *            发运周期
	 */
	public void setFayzq(BigDecimal fayzq) {
		this.fayzq = fayzq;
	}


	/**
	 * 取得 - 订货车间.
	 * 
	 * @return the 订货车间
	 */
	public String getDinghcj() {
		return dinghcj;
	}

	/**
	 * 设置 - 订货车间.
	 * 
	 * @param dinghcj
	 *            订货车间
	 */
	public void setDinghcj(String dinghcj) {
		this.dinghcj = dinghcj;
	}

	/**
	 * 取得 - uA包装内UC数量.
	 * 
	 * @return the uA包装内UC数量
	 */
	public BigDecimal getUabzucsl() {
		return uabzucsl;
	}

	/**
	 * 设置 - uA包装内UC数量.
	 * 
	 * @param uabzucsl
	 *            uA包装内UC数量
	 */
	public void setUabzucsl(BigDecimal uabzucsl) {
		this.uabzucsl = uabzucsl;
	}

	/**
	 * 取得 - 已交付量.
	 * 
	 * @return the 已交付量
	 */
	public BigDecimal getYijfl() {
		return yijfl;
	}

	/**
	 * 设置 - 已交付量.
	 * 
	 * @param yijfl
	 *            已交付量
	 */
	public void setYijfl(BigDecimal yijfl) {
		this.yijfl = yijfl;
	}

	/**
	 * 取得 - 供应商代码.
	 * 
	 * @return the 供应商代码
	 */
	public String getGongysdm() {
		return gongysdm;
	}

	/**
	 * 设置 - 供应商代码.
	 * 
	 * @param gongysdm
	 *            供应商代码
	 */
	public void setGongysdm(String gongysdm) {
		this.gongysdm = gongysdm;
	}

	/**
	 * 取得 - 库存.
	 * 
	 * @return the 库存
	 */
	public BigDecimal getKuc() {
		return kuc;
	}

	/**
	 * 设置 - 库存.
	 * 
	 * @param kuc
	 *            库存
	 */
	public void setKuc(BigDecimal kuc) {
		this.kuc = kuc;
	}
	/**
	 * @param zuixqdl the zuixqdl to set
	 */
	public void setZuixqdl(BigDecimal zuixqdl) {
		this.zuixqdl = zuixqdl;
	}
	/**
	 * @return the zuixqdl
	 */
	public BigDecimal getZuixqdl() {
		return zuixqdl;
	}

	public String getGuanjljjb() {
		return guanjljjb;
	}

	public void setGuanjljjb(String guanjljjb) {
		this.guanjljjb = guanjljjb;
	}

	public String getHeth() {
		return heth;
	}

	public void setHeth(String heth) {
		this.heth = heth;
	}

	public String getUsbaozlx() {
		return usbaozlx;
	}

	public void setUsbaozlx(String usbaozlx) {
		this.usbaozlx = usbaozlx;
	}

	public BigDecimal getUsbaozrl() {
		return usbaozrl;
	}

	public void setUsbaozrl(BigDecimal usbaozrl) {
		this.usbaozrl = usbaozrl;
	}

}