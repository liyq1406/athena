/**
 *
 */
package com.athena.xqjs.entity.common;

import java.math.BigDecimal;

import com.toft.core3.support.PageableSupport;


// TODO: Auto-generated Javadoc
/**
 * 实体: 零件-供应商.
 *
 * @author
 * @version
 */
public class LingjGongys extends PageableSupport{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5540979941843164170L;

	//private static final long serialVersionUID = 1L;
	/** * 质检抽检比例 *. */
	private String zhijcjbl;//质检抽检比例
	
	/** * 失效时间 *. */
	private String shixsj;//失效时间
	
	/** * 是否验证批次号 *. */
	private String shifyzpch;//是否验证批次号
	
	/** * 订单内容 *. */
	private String dingdnr;//订单内容
	
	/** * 生效时间 *. */
	private String shengxsj;//生效时间
	
	/** * 有效期 *. */
	private String youxq;//有效期
	
	/** * 供应份额 *. */
	private BigDecimal gongyfe;//供应份额
	
	/** * 零件号 *. */
	private String lingjbh;//零件号
	
	/** * 参考阀值 *. */
	private String cankfz;//参考阀值
	
	/** * 最小起订量 *. */
	private BigDecimal zuixqdl;//最小起订量
	
	/** * 供应合同号 *. */
	private String gongyhth;//供应合同号
	
	/** * 发运地 *. */
	private String fayd;//发运地
	
	/** * 供应商编号 *. */
	private String gongysbh;//供应商编号
	
	/** * 用户中心 *. */
	private String usercenter;//用户中心
	
	/** * 包装类型 *. */
	private String baozlx;
	
	/** * 供应商UC的包装类型 *. */
	private String ucbzlx;
	
	/** * 供应商UC的容量 *. */
	private BigDecimal ucrl;
	
	/** * 供应商UA的包装类型 *. */
	private String uabzlx;
	
	/** * 供应商UA里UC的个数 *. */
	private BigDecimal uaucgs;
	
	/** * 盖板 *. */
	private String gaib;//盖板
	
	/** * 内衬 *. */
	private String neic;//内衬
	
	/** * 是否存在临时包装 1是 0否 *. */
	private String shifczlsbz;
	
	/**
	 * 供应商类型
	 */
	private String leix;
	
	/**
	 * 供应商名称
	 */
	private String gongsmc;
	
	
	
	public String getGongsmc() {
		return gongsmc;
	}

	public void setGongsmc(String gongsmc) {
		this.gongsmc = gongsmc;
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

	public String getZhijcjbl() {
		return zhijcjbl;
	}

	public void setZhijcjbl(String zhijcjbl) {
		this.zhijcjbl = zhijcjbl;
	}

	public String getShixsj() {
		return shixsj;
	}

	public void setShixsj(String shixsj) {
		this.shixsj = shixsj;
	}

	public String getShifyzpch() {
		return shifyzpch;
	}

	public void setShifyzpch(String shifyzpch) {
		this.shifyzpch = shifyzpch;
	}

	public String getDingdnr() {
		return dingdnr;
	}

	public void setDingdnr(String dingdnr) {
		this.dingdnr = dingdnr;
	}

	public String getShengxsj() {
		return shengxsj;
	}

	public void setShengxsj(String shengxsj) {
		this.shengxsj = shengxsj;
	}

	public String getYouxq() {
		return youxq;
	}

	public void setYouxq(String youxq) {
		this.youxq = youxq;
	}

	public BigDecimal getGongyfe() {
		return gongyfe;
	}

	public void setGongyfe(BigDecimal gongyfe) {
		this.gongyfe = gongyfe;
	}

	public String getLingjbh() {
		return lingjbh;
	}

	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}

	public String getCankfz() {
		return cankfz;
	}

	public void setCankfz(String cankfz) {
		this.cankfz = cankfz;
	}

	public BigDecimal getZuixqdl() {
		return zuixqdl;
	}

	public void setZuixqdl(BigDecimal zuixqdl) {
		this.zuixqdl = zuixqdl;
	}

	public String getGongyhth() {
		return gongyhth;
	}

	public void setGongyhth(String gongyhth) {
		this.gongyhth = gongyhth;
	}

	public String getFayd() {
		return fayd;
	}

	public void setFayd(String fayd) {
		this.fayd = fayd;
	}

	public String getGongysbh() {
		return gongysbh;
	}

	public void setGongysbh(String gongysbh) {
		this.gongysbh = gongysbh;
	}

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getBaozlx() {
		return baozlx;
	}

	public void setBaozlx(String baozlx) {
		this.baozlx = baozlx;
	}

	public String getLeix() {
		return leix;
	}

	public void setLeix(String leix) {
		this.leix = leix;
	}
	
}