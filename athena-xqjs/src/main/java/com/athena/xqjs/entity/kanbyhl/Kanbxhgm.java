package com.athena.xqjs.entity.kanbyhl;

import java.math.BigDecimal;

import com.toft.core3.support.PageableSupport;

public class Kanbxhgm extends PageableSupport {

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
	
	/**
	 * 在冻结时，是否更新下发规模
	 */
	private String shifgxxfgm;
	
	private String yjfzl;
	

	public String getYjfzl() {
		return yjfzl;
	}

	public void setYjfzl(String yjfzl) {
		this.yjfzl = yjfzl;
	}

	public String getShifgxxfgm() {
		return shifgxxfgm;
	}

	public void setShifgxxfgm(String shifgxxfgm) {
		this.shifgxxfgm = shifgxxfgm;
	}

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

}
