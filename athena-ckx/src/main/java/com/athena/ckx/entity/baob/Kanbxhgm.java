package com.athena.ckx.entity.baob;

import java.math.BigDecimal;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

public class Kanbxhgm extends PageableSupport implements Domain{

	private static final long serialVersionUID = 1L;
	/**
	 * 1位用户中心+1位车间代码+1位产线+5位顺序号
	 */
	private String xunhbm;
	/**
	 * 用户中心
	 */
	private String usercenter;

	public String getDongjclbs() {
		return dongjclbs;
	}

	public void setDongjclbs(String dongjclbs) {
		this.dongjclbs = dongjclbs;
	}
	
	private String kanbkh;
	
	private String yaohlh;
	
	private String zuizsj;
	
	private String zuiwsj;
	
	private String danw;
	
	private String yaohsl;
	
	private String baozxh;
	
	private String usxh;
	
	private String ucxh;
	
	private String xiehd;
	
	private String xinxhd;
	
	private String cangkbh;
	
	private String zickbh;
	
	private String mudd;
	
	private String yaohlscsj;
	
	private String gongysdm;
	
	private String gongysmc;
	
	private String chengysdm;
	
	private String chengysmc;
	
	private String keh;
	
	private String shiflsk;
	
	public String getPianysj() {
		return pianysj;
	}

	public void setPianysj(String pianysj) {
		this.pianysj = pianysj;
	}

	private String pianysj;
	
	
	
	public String getKanbkh() {
		return kanbkh;
	}

	public void setKanbkh(String kanbkh) {
		this.kanbkh = kanbkh;
	}

	public String getYaohlh() {
		return yaohlh;
	}

	public void setYaohlh(String yaohlh) {
		this.yaohlh = yaohlh;
	}

	public String getZuizsj() {
		return zuizsj;
	}

	public void setZuizsj(String zuizsj) {
		this.zuizsj = zuizsj;
	}

	public String getZuiwsj() {
		return zuiwsj;
	}

	public void setZuiwsj(String zuiwsj) {
		this.zuiwsj = zuiwsj;
	}

	public String getDanw() {
		return danw;
	}

	public void setDanw(String danw) {
		this.danw = danw;
	}

	public String getYaohsl() {
		return yaohsl;
	}

	public void setYaohsl(String yaohsl) {
		this.yaohsl = yaohsl;
	}

	public String getBaozxh() {
		return baozxh;
	}

	public void setBaozxh(String baozxh) {
		this.baozxh = baozxh;
	}

	public String getUsxh() {
		return usxh;
	}

	public void setUsxh(String usxh) {
		this.usxh = usxh;
	}

	public String getUcxh() {
		return ucxh;
	}

	public void setUcxh(String ucxh) {
		this.ucxh = ucxh;
	}

	public String getXiehd() {
		return xiehd;
	}

	public void setXiehd(String xiehd) {
		this.xiehd = xiehd;
	}

	public String getXinxhd() {
		return xinxhd;
	}

	public void setXinxhd(String xinxhd) {
		this.xinxhd = xinxhd;
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

	public String getMudd() {
		return mudd;
	}

	public void setMudd(String mudd) {
		this.mudd = mudd;
	}

	public String getYaohlscsj() {
		return yaohlscsj;
	}

	public void setYaohlscsj(String yaohlscsj) {
		this.yaohlscsj = yaohlscsj;
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

	public String getChengysdm() {
		return chengysdm;
	}

	public void setChengysdm(String chengysdm) {
		this.chengysdm = chengysdm;
	}

	public String getChengysmc() {
		return chengysmc;
	}

	public void setChengysmc(String chengysmc) {
		this.chengysmc = chengysmc;
	}

	public String getKeh() {
		return keh;
	}

	public void setKeh(String keh) {
		this.keh = keh;
	}

	public String getShiflsk() {
		return shiflsk;
	}

	public void setShiflsk(String shiflsk) {
		this.shiflsk = shiflsk;
	}

	public String getXinmdd() {
		return xinmdd;
	}

	public void setXinmdd(String xinmdd) {
		this.xinmdd = xinmdd;
	}

	private String xinmdd;
	/**
	 * 零件号
	 */
	private String lingjbh;
	/**
	 * 供货模式
	 */
	private String gonghms;
	/**
	 * 供应商
	 */
	private String gongysbh;
	/**
	 * 线边仓库
	 */
	private String cangkdm;
	/**
	 * 消耗点
	 */
	private String xiaohd;
	/**
	 * UA类型
	 */
	private String umlx;
	/**
	 * UC类型
	 */
	private String uclx;
	/**
	 * UA零件数量
	 */
	private BigDecimal umljsl;
	/**
	 * UA中UC数量
	 */
	private BigDecimal umzucgs;
	/**
	 * UC容量
	 */
	private BigDecimal ucrl;
	/**
	 * CMJ
	 */
	private BigDecimal cmj;
	/**
	 * CJmax
	 */
	private BigDecimal cjmax;
	/**
	 * 当前循环规模
	 */
	private BigDecimal dangqxhgm;
	/**
	 * 计算循环规模
	 */
	private BigDecimal jisxhgm;
	/**
	 * 下发循环规模
	 */
	private BigDecimal xiafxhgm;
	/**
	 * 是否自动发送
	 */
	private Integer shifzdfs;
	/**
	 * 最大要货量
	 */
	private BigDecimal zuidyhl;
	/**
	 * 0：未生效，1：已生效
	 */
	private String shengxzt;
	/**
	 * 0：已冻结，1：未冻结
	 */
	private String dongjjdzt;
	/**
	 * 1：新生成，2：规模调整
	 */
	private String leix;
	/**
	 * P_创建人
	 */
	private String creator;
	/**
	 * P_创建时间
	 */
	private String create_time;
	/**
	 * P_计算时间
	 */
	private String jissj;
	/**
	 * 维护人
	 */
	private String weihr;
	/**
	 * 维护时间
	 */
	private String weihsj;

	/**
	 * 产线
	 */
	private String chanx;

	/**
	 * 计划员代码
	 */
	private String jihydm;

	/**
	 * 客户
	 */
	private String kehd;
	/**
	 * 修改人
	 */
	private String editor;
	/**
	 * 修改时间
	 */
	private String edit_time;
	/**
	 * 计算差异
	 */

	private String jiscy;
	/**
	 * 变化幅度
	 */
	private String bianhfd;
	/**
	 * 调整循环规模
	 */
	private String tiaozxhgm;

	/**
	 * 订货仓库
	 */
	private String dinghck;

	/**
	 * 路径代码
	 */
	private String lujdm;

	/**
	 * 线边要货类型
	 */
	private String xianbyhlx;

	/**
	 * 是否延迟上线
	 */
	private String yancsxbz;
	/**
	 * 将来模式
	 */
	private String jianglms;

	/**
	 * 零件名称
	 */
	private String lingjmc;

	/**
	 * 线边仓库
	 */
	private String xianbck;

	/**
	 * 冻结处理标示
	 */
	private String dongjclbs;

	/**
	 * 是否计算
	 */
	private String shifyjsyhl;

	public String getShifyjsyhl() {
		return shifyjsyhl;
	}

	public void setShifyjsyhl(String shifyjsyhl) {
		this.shifyjsyhl = shifyjsyhl;
	}

	public String getXianbck() {
		return xianbck;
	}

	public void setXianbck(String xianbck) {
		this.xianbck = xianbck;
	}

	public String getTiaozxhgm() {
		return tiaozxhgm;
	}

	public void setTiaozxhgm(String tiaozxhgm) {
		this.tiaozxhgm = tiaozxhgm;
	}

	public String getJiscy() {
		return jiscy;
	}

	public void setJiscy(String jiscy) {

		this.jiscy = jiscy;
	}

	public String getBianhfd() {
		return bianhfd;
	}

	public void setBianhfd(String bianhfd) {
		if (bianhfd != null) {
			this.bianhfd = bianhfd + "%";
		} else {
			this.bianhfd = bianhfd;
		}
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

	public String getXunhbm() {
		return xunhbm;
	}

	public void setXunhbm(String xunhbm) {
		this.xunhbm = xunhbm;
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

	public String getGonghms() {
		return gonghms;
	}

	public void setGonghms(String gonghms) {
		this.gonghms = gonghms;
	}

	public String getCangkdm() {
		return cangkdm;
	}

	public void setCangkdm(String cangkdm) {
		this.cangkdm = cangkdm;
	}

	public String getXiaohd() {
		return xiaohd;
	}

	public void setXiaohd(String xiaohd) {
		this.xiaohd = xiaohd;
	}

	public String getUmlx() {
		return umlx;
	}

	public void setUmlx(String umlx) {
		this.umlx = umlx;
	}

	public String getUclx() {
		return uclx;
	}

	public void setUclx(String uclx) {
		this.uclx = uclx;
	}

	public BigDecimal getUmljsl() {
		return umljsl;
	}

	public void setUmljsl(BigDecimal umljsl) {
		this.umljsl = umljsl;
	}

	public BigDecimal getUcrl() {
		return ucrl;
	}

	public String getJianglms() {
		return jianglms;
	}

	public void setJianglms(String jianglms) {
		this.jianglms = jianglms;
	}

	public String getXianbyhlx() {
		return xianbyhlx;
	}

	public void setXianbyhlx(String xianbyhlx) {
		this.xianbyhlx = xianbyhlx;
	}

	public String getLingjmc() {
		return lingjmc;
	}

	public void setLingjmc(String lingjmc) {
		this.lingjmc = lingjmc;
	}

	public String getYancsxbz() {
		return yancsxbz;
	}

	public void setYancsxbz(String yancsxbz) {
		this.yancsxbz = yancsxbz;
	}

	public String getDinghck() {
		return dinghck;
	}

	public void setDinghck(String dinghck) {
		this.dinghck = dinghck;
	}

	public String getLujdm() {
		return lujdm;
	}

	public void setLujdm(String lujdm) {
		this.lujdm = lujdm;
	}

	public String getShengxzt() {
		return shengxzt;
	}

	public void setShengxzt(String shengxzt) {
		this.shengxzt = shengxzt;
	}

	public String getDongjjdzt() {
		return dongjjdzt;
	}

	public void setDongjjdzt(String dongjjdzt) {
		this.dongjjdzt = dongjjdzt;
	}

	public String getLeix() {
		return leix;
	}

	public void setLeix(String leix) {
		this.leix = leix;
	}

	public void setUcrl(BigDecimal ucrl) {
		this.ucrl = ucrl;
	}

	public BigDecimal getCmj() {
		return cmj;
	}

	public void setCmj(BigDecimal cmj) {
		this.cmj = cmj;
	}

	public BigDecimal getCjmax() {
		return cjmax;
	}

	public void setCjmax(BigDecimal cjmax) {
		this.cjmax = cjmax;
	}

	public Integer getShifzdfs() {
		return shifzdfs;
	}

	public void setShifzdfs(Integer shifzdfs) {
		this.shifzdfs = shifzdfs;
	}

	public BigDecimal getZuidyhl() {
		return zuidyhl;
	}

	public void setZuidyhl(BigDecimal zuidyhl) {
		this.zuidyhl = zuidyhl;
	}

	public String getKehd() {
		return kehd;
	}

	public void setKehd(String kehd) {
		this.kehd = kehd;
	}

	public String getChanx() {
		return chanx;
	}

	public void setChanx(String chanx) {
		this.chanx = chanx;
	}

	public String getJihydm() {
		return jihydm;
	}

	public void setJihydm(String jihydm) {
		this.jihydm = jihydm;
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

	public String getJissj() {
		return jissj;
	}

	public void setJissj(String jissj) {
		this.jissj = jissj;
	}

	public String getWeihr() {
		return weihr;
	}

	public void setWeihr(String weihr) {
		this.weihr = weihr;
	}

	public String getWeihsj() {
		return weihsj;
	}

	public void setWeihsj(String weihsj) {
		this.weihsj = weihsj;
	}

	public String getGongysbh() {
		return gongysbh;
	}

	public void setGongysbh(String gongysbh) {
		this.gongysbh = gongysbh;
	}

	public BigDecimal getUmzucgs() {
		return umzucgs;
	}

	public void setUmzucgs(BigDecimal umzucgs) {
		this.umzucgs = umzucgs;
	}

	public BigDecimal getDangqxhgm() {
		return dangqxhgm;
	}

	public void setDangqxhgm(BigDecimal dangqxhgm) {
		this.dangqxhgm = dangqxhgm;
	}

	public BigDecimal getJisxhgm() {
		return jisxhgm;
	}

	public void setJisxhgm(BigDecimal jisxhgm) {
		this.jisxhgm = jisxhgm;
	}

	public BigDecimal getXiafxhgm() {
		return xiafxhgm;
	}

	public void setXiafxhgm(BigDecimal xiafxhgm) {
		this.xiafxhgm = xiafxhgm;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setId(String arg0) {
		// TODO Auto-generated method stub
		
	}

}
