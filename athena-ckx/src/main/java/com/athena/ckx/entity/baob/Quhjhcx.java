package com.athena.ckx.entity.baob;

import java.math.BigDecimal;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 取货计划查询
 * @author lc 
 */
public class Quhjhcx extends PageableSupport implements Domain{

	private static final long serialVersionUID = 1L;

	private String usercenter;     //用户中心
	
	private String yunsjhh;     //运输计划号
	
	private String yaohlh;     //要货令号
	
	private  String lingjbh;     //零件编号
	
    private String gongysdm;     //供应商代码
	
	private String chengysdm;     //承运商代码
	
	private String chanx;     //产线
	
	private String mudd;     //目的地
	
	private String startdhsj;     //最小到货时间
	 
	private String enddhsj;     //最大到货时间
	
	private BigDecimal lingjsl;     //零件数量
	
	private String cangkbh;     //仓库编号
	
    private String yujddsj;     //预计到达时间
	
    private String shijddsj;     //实际到达时间
	
	private String yujdhsj;     //预计到货时间
	
	private String cheph;     //车牌号
		
	private String wullj;     //物流路径
	
	private String flag;     //FLAG
	
	private String yicjhh;     //异常计划号
	
	private String yunszt;     //运输状态
	
	private String kach;     //卡车号
	
    private String wulddm;     //最新物理点
	
    private String daodwldsj;     //到达物理点时间
	
	private String shangcwld;     //上次物理点
	
    private String daodscwldsj;     //到达上次物理点时间
	
    private String quhd;     //取货点
	
	private String quhsj;     //取货时间
	
    private String year;     //年
	
	private String month;     //周期
	
	private String startmonth;     //起始月
	
	private String endmonth;     //结束月
	
	private BigDecimal zhengcsl;     //正常数量
	
	private BigDecimal yicsl;     //异常数量
	
	private Double wancl;      //供应商取货计划完成率
	
	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	
	public String getYunsjhh() {
		return yunsjhh;
	}

	public void setYunsjhh(String yunsjhh) {
		this.yunsjhh = yunsjhh;
	}
	
	public String getYaohlh() {
		return yaohlh;
	}
	
	public void setYaohlh(String yaohlh) {
		this.yaohlh = yaohlh;
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
	
	public String getChengysdm() {
		return chengysdm;
	}
	
	public void setChengysdm(String chengysdm) {
		this.chengysdm = chengysdm;
	}

	public String getChanx() {
		return chanx;
	}

	public void setChanx(String chanx) {
		this.chanx = chanx;
	}
	
	public String getMudd() {
		return mudd;
	}

	public void setMudd(String mudd) {
		this.mudd = mudd;
	}
	
	public String getStartdhsj() {
		return startdhsj;
	}

	public void setStartdhsj(String startdhsj) {
		this.startdhsj = startdhsj;
	}

	public String getEnddhsj() {
		return enddhsj;
	}

	public void setEnddhsj(String enddhsj) {
		this.enddhsj = enddhsj;
	}
	
	public BigDecimal getLingjsl() {
		return lingjsl;
	}

	public void setLingjsl(BigDecimal lingjsl) {
		this.lingjsl = lingjsl;
	}

	public String getCangkbh() {
		return cangkbh;
	}

	public void setCangkbh(String cangkbh) {
		this.cangkbh = cangkbh;
	}

	public String getYujddsj() {
		return yujddsj;
	}

	public void setYujddsj(String yujddsj) {
		this.yujddsj = yujddsj;
	}
		
	public String getShijddsj() {
		return shijddsj;
	}

	public void setShijddsj(String shijddsj) {
		this.shijddsj = shijddsj;
	}
	
	public String getYujdhsj() {
		return yujdhsj;
	}
	
	public void setYujdhsj(String yujdhsj) {
		this.yujdhsj = yujdhsj;
	}
	
	public String getCheph() {
		return cheph;
	}
	
	public void setCheph(String cheph) {
		this.cheph = cheph;
	}
	
	public String getWullj() {
		return wullj;
	}
	
	public void setWullj(String wullj) {
		this.wullj = wullj;
	}
	
	public String getFlag() {
		return flag;
	}
	
	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	public String getYicjhh() {
		return yicjhh;
	}
	
	public void setYicjhh(String yicjhh) {
		this.yicjhh = yicjhh;
	}
	
	public String getYunszt() {
		return yunszt;
	}
	
	public void setYunszt(String yunszt) {
		this.yunszt = yunszt;
	}
	
	public String getKach() {
		return kach;
	}
	
	public void setKach(String kach) {
		this.kach = kach;
	}
	
	public String getWulddm() {
		return wulddm;
	}
	
	public void setWulddm(String wulddm) {
		this.wulddm = wulddm;
	}
	
	public String getDaodwldsj() {
		return daodwldsj;
	}
	
	public void setdaodwldsj(String daodwldsj) {
		this.daodwldsj = daodwldsj;
	}
	
	public String getShangcwld() {
		return shangcwld;
	}
	
	public void setShangcwld(String shangcwld) {
		this.shangcwld = shangcwld;
	}
	
	public String getDaodscwldsj() {
		return daodscwldsj;
	}
	
	public void setDaodscwldsj(String daodscwldsj) {
		this.daodscwldsj = daodscwldsj;
	}
	
	public String getQuhd() {
		return quhd;
	}
	
	public void setQuhd(String quhd) {
		this.quhd = quhd;
	}
	
	public String getQuhsj() {
		return quhsj;
	}
	
	public void setQuhsj(String quhsj) {
		this.quhsj = quhsj;
	}
	
	public String getYear() {
		return year;
	}
	
	public void setYear(String year) {
		this.year = year;
	}
	
	public String getMonth() {
		return month;
	}
	
	public void setMonth(String month) {
		this.month = month;
	}
	
	public String getStartmonth() {
		return startmonth;
	}
	
	public void setStartmonth(String startmonth) {
		this.startmonth = startmonth;
	}
	
	public String getEndmonth() {
		return endmonth;
	}
	
	public void setEndmonth(String endmonth) {
		this.endmonth = endmonth;
	}
	
	public BigDecimal getZhengcsl() {
		return zhengcsl;
	}

	public void setZhengcsl(BigDecimal zhengcsl) {
		this.zhengcsl = zhengcsl;
	}
	
	public BigDecimal getYicsl() {
		return yicsl;
	}

	public void setYicsl(BigDecimal yicsl) {
		this.yicsl = yicsl;
	}
	
	public Double getWancl() {
		return wancl;
	}

	public void setWancl(Double wancl) {
		this.wancl = wancl;
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
