package com.athena.xqjs.entity.ilorder;

import java.math.BigDecimal;

import com.toft.core3.support.PageableSupport;


/**
 * 实体: 订单明细
 * @author 李明
 * @version
 * 2011-12-01
 */

public class Dingdmx extends PageableSupport{
	private static final long serialVersionUID = 1L;
	private String id;
	private String dingdmxid;
	private String  dingdh;
	private String usercenter;
	private String lingjbh;
	private String  gongysdm;
	private String  gongyslx ;
	private String  yaohqsrq;
	private String  yaohjsrq;
	private String  cangkdm;
	private String  xiehzt;
	private String  fahd;
	private String   dinghcj;
	private BigDecimal   shul;
	private String   danw;
	private String  uabzlx;
	private String    uabzuclx;
	private BigDecimal   uabzucsl;
	private BigDecimal  uabzucrl;
	private String  fayrq;
	private String  jiaofrq;
	private String  zhuangt;
	private String   yugsfqz;
	private String   leix;
	private String   lujdm;
	private String    jihyz;
	private String    zuihwhr;
	private String  zuihwhsj;
	private BigDecimal   jissl;
	private String    gonghlx;
	private String gonghms;
	private BigDecimal   yijfl;
	private String    shid ;
	private String    zuizdhsj;
	private String    zuiwdhsj;
	private String    xiaohsj;
	private String   xiaohcbhsj;
	private String    xiaohcsxsj;
	private String    xiehd;
	private String    xianbyhlx;
	private String    usbaozlx;
	private BigDecimal    usbaozrl;
	private BigDecimal    yizzl;
	private String    xiaohch;
	private String    xiaohccxh;
	private String    chanx ;
	private String    xiaohd;
	private String    fenpxh;
	private BigDecimal uabzrl;
	private String fahzq ;
	private String fayzq ;
	private String faykssj ;
	private String fayjssj ;
	private String lingjmc;
	//-------新增字段 李智---------
	private String gongsmc;			//供应商名称
	private BigDecimal yaohlgszl;	//要货令个数总量（订单明细）
	private BigDecimal yaohlgsyjf;  //要货令个数已交付（订单明细）
	private BigDecimal yaohlgsyzz;  //要货令个数已终止（订单明细）
	private String fawmc;			//零件法文名称

	private String zhongwmc;
	
	private int    sign;  //标识是否参与均分  1参与均分 0不参与均分
	
	private String gongzr; //工作日
	
	/**
	 * 发运时间(M1MD模式发运日期带时分秒格式,用次字段)
	 */
	private String  faysj;
	
	private String shifht; //是否混托
	
	public String getShifht() {
		return shifht;
	}

	public void setShifht(String shifht) {
		this.shifht = shifht;
	}

	
	/**
	 * 取得 zhongwmc
	 * @return the zhongwmc
	 */
	public String getZhongwmc() {
		return zhongwmc;
	}

	/**
	 * @param zhongwmc the zhongwmc to set
	 */
	public void setZhongwmc(String zhongwmc) {
		this.zhongwmc = zhongwmc;
	}

	public String getGonghms() {
		return gonghms;
	}

	public void setGonghms(String gonghms) {
		this.gonghms = gonghms;
	}

	private BigDecimal allowGzsl;	//可终止数量
	private BigDecimal gongyfe;		//供应份额
	private String zhidgys;			//指定供应商
	private BigDecimal zuixqdl;		//最小起订量
	private String lingjsx;			//零件属性(A零件,K卷料,M辅料)
	
	/**
	 * 承运商编号
	 */
	private String gcbh;
	/**
	 * 仓库备货时间
	 */
	private BigDecimal beihsj2;
	//-------查询条件 李智 add ---------
	/**
	 * 用于临时订单
	 */
	private String gongyzq;
	/**
	 * 3大/2小/1等于交付比例
	 */
	private String searchSymbols;
	
	/**
	 * 交付比例
	 */
	private BigDecimal jfbl;
	
	/**
	 * 交付时间起始
	 */
	private String jiaofrqStart;
	private String jiaofrqEnd;
	
	/**
	 * 创建人
	 */
	private String creator = null;
	/**
	 * 创建人
	 */
	private String keh = null;

	/**
	 * 创建时间
	 */
	private String create_time = null;

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
	 * 删除标示
	 */
	private String active = null;
	
	private String ids = null;
	
	/**
	 * 趟次
	 */
	private BigDecimal tangc;
	
	/**
	 * 临时订单看板：看板循环编码
	 */
	private String kanbxhbm = null;
	
	
	private String neibyhzx;
	
	//wuyichao   临时订单导入//
	private String shulStr;
	
	private String leixStr;
	
	private String fasgysStr;
	
	public String getShulStr() {
		return shulStr;
	}
	
	public void setShulStr(String shulStr) {
		this.shulStr = shulStr;
	}
	
	public String getLeixStr() {
		return leixStr;
	}
	
	public void setLeixStr(String leixStr) {
		this.leixStr = leixStr;
	}
	
	public String getFasgysStr() {
		return fasgysStr;
	}
	
	public void setFasgysStr(String fasgysStr) {
		this.fasgysStr = fasgysStr;
	}
	//wuyichao   临时订单导入//
	


	public String getNeibyhzx() {
		return neibyhzx;
	}

	public void setNeibyhzx(String neibyhzx) {
		this.neibyhzx = neibyhzx;
	}

	public BigDecimal getUabzrl() {
		return uabzrl;
	}

	public void setUabzrl(BigDecimal uabzrl) {
		this.uabzrl = uabzrl;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	/**
	 * @return the usbaozrl
	 */
	public BigDecimal getUsbaozrl() {
		return usbaozrl;
	}
	/**
	 * @param usbaozrl the usbaozrl to set
	 */
	public void setUsbaozrl(BigDecimal usbaozrl) {
		this.usbaozrl = usbaozrl;
	}
	/**
	 * @return the zuixqdl
	 */
	public BigDecimal getZuixqdl() {
		return zuixqdl;
	}

	public String getLingjmc() {
		return lingjmc;
	}

	public void setLingjmc(String lingjmc) {
		this.lingjmc = lingjmc;
	}
	/**
	 * @param zuixqdl the zuixqdl to set
	 */
	public void setZuixqdl(BigDecimal zuixqdl) {
		this.zuixqdl = zuixqdl;
	}
	/**
	 * @return the lingjsx
	 */
	public String getLingjsx() {
		return lingjsx;
	}
	/**
	 * @param lingjsx the lingjsx to set
	 */
	public void setLingjsx(String lingjsx) {
		this.lingjsx = lingjsx;
	}
	/**
	 * @return the zhidgys
	 */
	public String getZhidgys() {
		return zhidgys;
	}
	
	
	public BigDecimal getYizzl() {
		return yizzl;
	}

	public String getGongyzq() {
		return gongyzq;
	}

	public void setGongyzq(String gongyzq) {
		this.gongyzq = gongyzq;
	}

	public void setYizzl(BigDecimal yizzl) {
		this.yizzl = yizzl;
	}
	/**
	 * @param zhidgys the zhidgys to set
	 */
	public void setZhidgys(String zhidgys) {
		this.zhidgys = zhidgys;
	}
	/**
	 * @return the gongyfe
	 */
	public BigDecimal getGongyfe() {
		return gongyfe;
	}
	/**
	 * @param gongyfe the gongyfe to set
	 */
	public void setGongyfe(BigDecimal gongyfe) {
		this.gongyfe = gongyfe;
	}
	public String getFahzq() {
		return fahzq;
	}
	public void setFahzq(String fahzq) {
		this.fahzq = fahzq;
	}
	public BigDecimal getAllowGzsl() {
		return allowGzsl;
	}
	public void setAllowGzsl(BigDecimal allowGzsl) {
		this.allowGzsl = allowGzsl;
	}
	public String getGongsmc() {
		return gongsmc;
	}
	public void setGongsmc(String gongsmc) {
		this.gongsmc = gongsmc;
	}

	public String getKeh() {
		return keh;
	}

	public void setKeh(String keh) {
		this.keh = keh;
	}
	public BigDecimal getYaohlgszl() {
		return yaohlgszl;
	}
	public void setYaohlgszl(BigDecimal yaohlgszl) {
		this.yaohlgszl = yaohlgszl;
	}
	public BigDecimal getYaohlgsyjf() {
		return yaohlgsyjf;
	}

	public String getKanbxhbm() {
		return kanbxhbm;
	}

	public void setKanbxhbm(String kanbxhbm) {
		this.kanbxhbm = kanbxhbm;
	}

	public void setYaohlgsyjf(BigDecimal yaohlgsyjf) {
		this.yaohlgsyjf = yaohlgsyjf;
	}
	public BigDecimal getYaohlgsyzz() {
		return yaohlgsyzz;
	}
	public void setYaohlgsyzz(BigDecimal yaohlgsyzz) {
		this.yaohlgsyzz = yaohlgsyzz;
	}

	public String getFawmc() {
		return fawmc;
	}
	public void setFawmc(String fawmc) {
		this.fawmc = fawmc;
	}
	public String getSearchSymbols() {
		return searchSymbols;
	}
	public void setSearchSymbols(String searchSymbols) {
		this.searchSymbols = searchSymbols;
	}
	public BigDecimal getJfbl() {
		return jfbl;
	}
	public void setJfbl(BigDecimal jfbl) {
		this.jfbl = jfbl;
	}
	public String getJiaofrqStart() {
		return jiaofrqStart;
	}
	public void setJiaofrqStart(String jiaofrqStart) {
		this.jiaofrqStart = jiaofrqStart;
	}
	public String getJiaofrqEnd() {
		return jiaofrqEnd;
	}
	public void setJiaofrqEnd(String jiaofrqEnd) {
		this.jiaofrqEnd = jiaofrqEnd;
	}
	public String getFaykssj() {
		return faykssj;
	}
	public void setFaykssj(String faykssj) {
		this.faykssj = faykssj;
	}
	public String getFayjssj() {
		return fayjssj;
	}
	public void setFayjssj(String fayjssj) {
		this.fayjssj = fayjssj;
	}
	public String getFayzq() {
		return fayzq;
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

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}
	public void setFayzq(String fayzq) {
		this.fayzq = fayzq;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDingdh() {
		return dingdh;
	}
	public void setDingdh(String dingdh) {
		this.dingdh = dingdh;
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
	public String getGongysdm() {
		return gongysdm;
	}
	public void setGongysdm(String gongysdm) {
		this.gongysdm = gongysdm;
	}
	public String getGongyslx() {
		return gongyslx;
	}
	public void setGongyslx(String gongyslx) {
		this.gongyslx = gongyslx;
	}
	public String getYaohqsrq() {
		return yaohqsrq;
	}
	public void setYaohqsrq(String yaohqsrq) {
		this.yaohqsrq = yaohqsrq;
	}
	public String getYaohjsrq() {
		return yaohjsrq;
	}
	public void setYaohjsrq(String yaohjsrq) {
		this.yaohjsrq = yaohjsrq;
	}
	public String getCangkdm() {
		return cangkdm;
	}
	public void setCangkdm(String cangkdm) {
		this.cangkdm = cangkdm;
	}
	public String getXiehzt() {
		return xiehzt;
	}
	public void setXiehzt(String xiehzt) {
		this.xiehzt = xiehzt;
	}
	public String getFahd() {
		return fahd;
	}
	public void setFahd(String fahd) {
		this.fahd = fahd;
	}
	public String getDinghcj() {
		return dinghcj;
	}
	public void setDinghcj(String dinghcj) {
		this.dinghcj = dinghcj;
	}
	public BigDecimal getShul() {
		return shul;
	}
	public void setShul(BigDecimal shul) {
		this.shul = shul;
	}
	public String getDanw() {
		return danw;
	}
	public void setDanw(String danw) {
		this.danw = danw;
	}
	public String getUabzlx() {
		return uabzlx;
	}
	public void setUabzlx(String uabzlx) {
		this.uabzlx = uabzlx;
	}
	public String getUabzuclx() {
		return uabzuclx;
	}
	public void setUabzuclx(String uabzuclx) {
		this.uabzuclx = uabzuclx;
	}
	public BigDecimal getUabzucsl() {
		return uabzucsl;
	}
	public void setUabzucsl(BigDecimal uabzucsl) {
		this.uabzucsl = uabzucsl;
	}
	public BigDecimal getUabzucrl() {
		return uabzucrl;
	}
	public void setUabzucrl(BigDecimal uabzucrl) {
		this.uabzucrl = uabzucrl;
	}
	public String getFayrq() {
		return fayrq;
	}
	public void setFayrq(String fayrq) {
		this.fayrq = fayrq;
	}
	public String getJiaofrq() {
		return jiaofrq;
	}
	public void setJiaofrq(String jiaofrq) {
		this.jiaofrq = jiaofrq;
	}
	public String getZhuangt() {
		return zhuangt;
	}
	public void setZhuangt(String zhuangt) {
		this.zhuangt = zhuangt;
	}
	public String getYugsfqz() {
		return yugsfqz;
	}
	public void setYugsfqz(String yugsfqz) {
		this.yugsfqz = yugsfqz;
	}
	public String getLeix() {
		return leix;
	}
	public void setLeix(String leix) {
		this.leix = leix;
	}
	public String getLujdm() {
		return lujdm;
	}
	public void setLujdm(String lujdm) {
		this.lujdm = lujdm;
	}
	public String getJihyz() {
		return jihyz;
	}
	public void setJihyz(String jihyz) {
		this.jihyz = jihyz;
	}
	public String getZuihwhr() {
		return zuihwhr;
	}
	public void setZuihwhr(String zuihwhr) {
		this.zuihwhr = zuihwhr;
	}
	public String getZuihwhsj() {
		return zuihwhsj;
	}
	public void setZuihwhsj(String zuihwhsj) {
		this.zuihwhsj = zuihwhsj;
	}
	public BigDecimal getJissl() {
		return jissl;
	}
	public void setJissl(BigDecimal jissl) {
		this.jissl = jissl;
	}
	public String getGonghlx() {
		return gonghlx;
	}
	public void setGonghlx(String gonghlx) {
		this.gonghlx = gonghlx;
	}
	public BigDecimal getYijfl() {
		return yijfl;
	}
	public void setYijfl(BigDecimal yijfl) {
		this.yijfl = yijfl;
	}
	public String getShid() {
		return shid;
	}
	public void setShid(String shid) {
		this.shid = shid;
	}
	public String getZuizdhsj() {
		return zuizdhsj;
	}
	public void setZuizdhsj(String zuizdhsj) {
		this.zuizdhsj = zuizdhsj;
	}
	public String getZuiwdhsj() {
		return zuiwdhsj;
	}
	public void setZuiwdhsj(String zuiwdhsj) {
		this.zuiwdhsj = zuiwdhsj;
	}
	public String getXiaohsj() {
		return xiaohsj;
	}
	public void setXiaohsj(String xiaohsj) {
		this.xiaohsj = xiaohsj;
	}
	public String getXiaohcbhsj() {
		return xiaohcbhsj;
	}
	public void setXiaohcbhsj(String xiaohcbhsj) {
		this.xiaohcbhsj = xiaohcbhsj;
	}
	public String getXiaohcsxsj() {
		return xiaohcsxsj;
	}
	public void setXiaohcsxsj(String xiaohcsxsj) {
		this.xiaohcsxsj = xiaohcsxsj;
	}
	public String getXiehd() {
		return xiehd;
	}
	public void setXiehd(String xiehd) {
		this.xiehd = xiehd;
	}
	public String getXianbyhlx() {
		return xianbyhlx;
	}
	public void setXianbyhlx(String xianbyhlx) {
		this.xianbyhlx = xianbyhlx;
	}
	public String getUsbaozlx() {
		return usbaozlx;
	}
	public void setUsbaozlx(String usbaozlx) {
		this.usbaozlx = usbaozlx;
	}
	
	public String getXiaohch() {
		return xiaohch;
	}
	public void setXiaohch(String xiaohch) {
		this.xiaohch = xiaohch;
	}
	public String getXiaohccxh() {
		return xiaohccxh;
	}
	public void setXiaohccxh(String xiaohccxh) {
		this.xiaohccxh = xiaohccxh;
	}
	public String getChanx() {
		return chanx;
	}
	public void setChanx(String chanx) {
		this.chanx = chanx;
	}
	public String getXiaohd() {
		return xiaohd;
	}
	public void setXiaohd(String xiaohd) {
		this.xiaohd = xiaohd;
	}
	public String getFenpxh() {
		return fenpxh;
	}
	public void setFenpxh(String fenpxh) {
		this.fenpxh = fenpxh;
	}

	public BigDecimal getTangc() {
		return tangc;
	}

	public void setTangc(BigDecimal tangc) {
		this.tangc = tangc;
	}

	public String getGcbh() {
		return gcbh;
	}

	public void setGcbh(String gcbh) {
		this.gcbh = gcbh;
	}

	public BigDecimal getBeihsj2() {
		return beihsj2;
	}

	public void setBeihsj2(BigDecimal beihsj2) {
		this.beihsj2 = beihsj2;
	}

	/**
	 * 返回
	 * @return the dingdmxid
	 */
	public String getDingdmxid() {
		return dingdmxid;
	}

	/**
	 * 设置
	 * @param dingdmxid the dingdmxid to set
	 */
	public void setDingdmxid(String dingdmxid) {
		this.dingdmxid = dingdmxid;
	}

	public int getSign() {
		return sign;
	}

	public void setSign(int sign) {
		this.sign = sign;
	}

	public String getFaysj() {
		return faysj;
	}

	public void setFaysj(String faysj) {
		this.faysj = faysj;
	}

	public void setGongzr(String gongzr) {
		this.gongzr = gongzr;
	}

	public String getGongzr() {
		return gongzr;
	}
	
	
}