package com.athena.xqjs.entity.zygzbj;

import java.math.BigDecimal;
import java.util.List;

import com.toft.core3.support.PageableSupport;

/**
 * 资源跟踪报警汇总bean
 * @author WL
 * @date 2011-02-13
 */
public class ZiygzbjHz extends PageableSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8667840074116203473L;
	
	/**
	 * P_主键
	 */
	private String id;
	/**
	 * 用户中心
	 */
	private String usercenter;
	/**
	 * 仓库
	 */
	private String cangkdm;
	/**
	 * 零件号
	 */
	private String lingjbh;
	/**
	 * 零件名称
	 */
	private String lingjmc;
	/**
	 * 零件单位
	 */
	private String lingjdw;
	/**
	 * 零件类型
	 */
	private String lingjlx = "";
	/**
	 * 包装类型
	 */
	private String baozlx;
	/**
	 * 包装容量
	 */
	private BigDecimal baozrl= BigDecimal.ZERO;
	/**
	 * 期初库存
	 */
	private BigDecimal qickc= BigDecimal.ZERO;
	/**
	 * 待消耗量
	 */
	private BigDecimal daixhl= BigDecimal.ZERO;
	/**
	 * 安全库存
	 */
	private BigDecimal anqkc= BigDecimal.ZERO;
	/**
	 * 最大库存数量
	 */
	private BigDecimal zuidkcsl= BigDecimal.ZERO;
	/**
	 * MAF库存
	 */
	private BigDecimal mafkc= BigDecimal.ZERO;
	/**
	 * 报警类型
	 */
	private String baojlx;
	/**
	 * 计算类型
	 */
	private String jislx;
	/**
	 * 毛需求版次
	 */
	private String maoxqbc;
	/**
	 * 计划员代码
	 */
	private String jihydm;
	/**
	 * 否多用户中心使用
	 */
	private Integer shifdyhzxsy;
	/**
	 * 未发运数量
	 */
	private BigDecimal weifysl;
	/**
	 * 已交付0
	 */
	private BigDecimal yijf0= BigDecimal.ZERO;
	/**
	 * 需求0
	 */
	private BigDecimal xuq0= BigDecimal.ZERO;
	/**
	 * 年周序0
	 */
	private String nianzx0;
	/**
	 * 已交付1
	 */
	private BigDecimal yijf1 = BigDecimal.ZERO;
	/**
	 * 需求1
	 */
	private BigDecimal xuq1 = BigDecimal.ZERO;
	/**
	 * 年周序1
	 */
	private String nianzx1;
	/**
	 * 已交付2
	 */
	private BigDecimal yijf2= BigDecimal.ZERO;
	/**
	 * 需求2
	 */
	private BigDecimal xuq2= BigDecimal.ZERO;
	/**
	 * 年周序2
	 */
	private String nianzx2;
	/**
	 * 已交付3
	 */
	private BigDecimal yijf3= BigDecimal.ZERO;
	/**
	 * 需求3
	 */
	private BigDecimal xuq3= BigDecimal.ZERO;
	/**
	 * 年周序3
	 */
	private String nianzx3;
	/**
	 * 已交付4
	 */
	private BigDecimal yijf4= BigDecimal.ZERO;
	/**
	 * 需求4
	 */
	private BigDecimal xuq4= BigDecimal.ZERO;
	/**
	 * 年周序4
	 */
	private String nianzx4;
	/**
	 * 已交付5
	 */
	private BigDecimal yijf5= BigDecimal.ZERO;
	/**
	 * 需求5
	 */
	private BigDecimal xuq5= BigDecimal.ZERO;
	/**
	 * 年周序5
	 */
	private String nianzx5;
	/**
	 * 已交付6
	 */
	private BigDecimal yijf6= BigDecimal.ZERO;
	/**
	 * 年周序6
	 */
	private String nianzx6;
	/**
	 * 需求6
	 */
	private BigDecimal xuq6 = BigDecimal.ZERO;
	/**
	 * 已交付7
	 */
	private BigDecimal yijf7 = BigDecimal.ZERO;
	/**
	 * 需求7
	 */
	private BigDecimal xuq7 = BigDecimal.ZERO;
	/**
	 * 年周序8
	 */
	private String nianzx8;
	/**
	 * 已交付8
	 */
	private BigDecimal yijf8 = BigDecimal.ZERO;
	/**
	 * 需求8
	 */
	private BigDecimal xuq8 = BigDecimal.ZERO;
	


	
	
	/**
	 * 年周序8
	 */
	private String nianzx9;
	/**
	 * 已交付8
	 */
	private BigDecimal yijf9 = BigDecimal.ZERO;
	/**
	 * 需求8
	 */
	private BigDecimal xuq9= BigDecimal.ZERO;
	/**
	 * 年周序8
	 */
	private String nianzx10;
	/**
	 * 已交付8
	 */
	private BigDecimal yijf10 = BigDecimal.ZERO;
	/**
	 * 需求8
	 */
	private BigDecimal xuq10 = BigDecimal.ZERO;
	/**
	 * 年周序8
	 */
	private String nianzx11;
	/**
	 * 已交付8
	 */
	private BigDecimal yijf11= BigDecimal.ZERO;
	/**
	 * 需求8
	 */
	private BigDecimal xuq11= BigDecimal.ZERO;
	/**
	 * 年周序8
	 */
	private String nianzx12;
	/**
	 * 已交付8
	 */
	private BigDecimal yijf12= BigDecimal.ZERO;
	/**
	 * 需求8
	 */
	private BigDecimal xuq12= BigDecimal.ZERO;
	/**
	 * 年周序8
	 */
	private String nianzx13;
	/**
	 * 已交付8
	 */
	private BigDecimal yijf13= BigDecimal.ZERO;
	/**
	 * 需求8
	 */
	private BigDecimal xuq13= BigDecimal.ZERO;
	/**
	 * 年周序8
	 */
	private String nianzx14;
	/**
	 * 已交付8
	 */
	private BigDecimal yijf14= BigDecimal.ZERO;
	/**
	 * 需求8
	 */
	private BigDecimal xuq14= BigDecimal.ZERO;
	/**
	 * 年周序8
	 */
	private String nianzx15;
	/**
	 * 已交付8
	 */
	private BigDecimal yijf15= BigDecimal.ZERO;
	/**
	 * 需求8
	 */
	private BigDecimal xuq15= BigDecimal.ZERO;
	/**
	 * 年周序8
	 */
	private String nianzx16;
	/**
	 * 已交付8
	 */
	private BigDecimal yijf16= BigDecimal.ZERO;
	/**
	 * 需求8
	 */
	private BigDecimal xuq16= BigDecimal.ZERO;
	/**
	 * 年周序8
	 */
	private String nianzx17;
	/**
	 * 已交付8
	 */
	private BigDecimal yijf17= BigDecimal.ZERO;
	/**
	 * 需求8
	 */
	private BigDecimal xuq17= BigDecimal.ZERO;
	/**
	 * 年周序8
	 */
	private String nianzx18;
	/**
	 * 已交付8
	 */
	private BigDecimal yijf18= BigDecimal.ZERO;
	/**
	 * 需求8
	 */
	private BigDecimal xuq18= BigDecimal.ZERO;
	/**
	 * 年周序8
	 */
	private String nianzx19;
	/**
	 * 已交付8
	 */
	private BigDecimal yijf19= BigDecimal.ZERO;
	/**
	 * 需求8
	 */
	private BigDecimal xuq19= BigDecimal.ZERO;
	/**
	 * 年周序8
	 */
	private String nianzx20;
	/**
	 * 已交付8
	 */
	private BigDecimal yijf20= BigDecimal.ZERO;
	/**
	 * 需求8
	 */
	private BigDecimal xuq20= BigDecimal.ZERO;
	/**
	 * 年周序8
	 */
	private String nianzx21;
	/**
	 * 已交付8
	 */
	private BigDecimal yijf21= BigDecimal.ZERO;
	/**
	 * 需求8
	 */
	private BigDecimal xuq21= BigDecimal.ZERO;
	/**
	 * 年周序8
	 */
	private String nianzx22;
	/**
	 * 已交付8
	 */
	private BigDecimal yijf22= BigDecimal.ZERO;
	/**
	 * 需求8
	 */
	private BigDecimal xuq22= BigDecimal.ZERO;
	/**
	 * 年周序8
	 */
	private String nianzx23;
	/**
	 * 已交付8
	 */
	private BigDecimal yijf23= BigDecimal.ZERO;
	/**
	 * 需求8
	 */
	private BigDecimal xuq23= BigDecimal.ZERO;
	/**
	 * 年周序8
	 */
	private String nianzx24;
	/**
	 * 已交付8
	 */
	private BigDecimal yijf24= BigDecimal.ZERO;
	/**
	 * 需求8
	 */
	private BigDecimal xuq24= BigDecimal.ZERO;
	/**
	 * 年周序8
	 */
	private String nianzx25;
	/**
	 * 已交付8
	 */
	private BigDecimal yijf25= BigDecimal.ZERO;
	/**
	 * 需求8
	 */
	private BigDecimal xuq25= BigDecimal.ZERO;
	/**
	 * 年周序8
	 */
	private String nianzx26;
	/**
	 * 已交付8
	 */
	private BigDecimal yijf26= BigDecimal.ZERO;
	/**
	 * 需求8
	 */
	private BigDecimal xuq26= BigDecimal.ZERO;
	
	/**
	 * 年周序8
	 */
	private String nianzx27;
	/**
	 * 已交付8
	 */
	private BigDecimal yijf27= BigDecimal.ZERO;
	/**
	 * 需求8
	 */
	private BigDecimal xuq27= BigDecimal.ZERO;
	/**
	 * 年周序8
	 */
	private String nianzx28;
	/**
	 * 已交付8
	 */
	private BigDecimal yijf28= BigDecimal.ZERO;
	/**
	 * 需求8
	 */
	private BigDecimal xuq28= BigDecimal.ZERO;
	/**
	 * 年周序8
	 */
	private String nianzx29;
	/**
	 * 已交付8
	 */
	private BigDecimal yijf29= BigDecimal.ZERO;
	/**
	 * 需求8
	 */
	private BigDecimal xuq29= BigDecimal.ZERO;
	
	/**
	 * 总已交付0
	 */
	private BigDecimal zongyjf0= BigDecimal.ZERO;
	/**
	 * 总需求0
	 */
	private BigDecimal zongxq0= BigDecimal.ZERO;
	/**
	 * 总已交付1
	 */
	private BigDecimal zongyjf1= BigDecimal.ZERO;
	/**
	 * 总需求1
	 */
	private BigDecimal zongxq1= BigDecimal.ZERO;
	/**
	 * 总已交付2
	 */
	private BigDecimal zongyjf2= BigDecimal.ZERO;
	/**
	 * 总需求2
	 */
	private BigDecimal zongxq2= BigDecimal.ZERO;
	/**
	 * 总已交付3
	 */
	private BigDecimal zongyjf3= BigDecimal.ZERO;
	/**
	 * 总需求3
	 */
	private BigDecimal zongxq3= BigDecimal.ZERO;
	/**
	 * 总已交付4
	 */
	private BigDecimal zongyjf4= BigDecimal.ZERO;
	/**
	 * 总需求4
	 */
	private BigDecimal zongxq4= BigDecimal.ZERO;
	/**
	 * 总已交付5
	 */
	private BigDecimal zongyjf5= BigDecimal.ZERO;
	/**
	 * 总需求5
	 */
	private BigDecimal zongxq5= BigDecimal.ZERO;
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
	 * 制作路线
	 */
	private String zhizlx;
	
	/**
	 * 断点时间
	 */
	private String duandsj;
	
	/**
	 * 数量
	 */
	private int num;
	
	/**
	 * 明细信息0,第一周期
	 */
	private List<Ziygzbjmx> mx0;
	
	/**
	 * 明细信息1,第二周期
	 */
	private List<Ziygzbjmx> mx1;
	
	/**
	 * 明细信息2,第三周期
	 */
	private List<Ziygzbjmx> mx2;

	private String zygy = "1";
	
	/**
	 * 明细信息3,第四周期
	 */
	private List<Ziygzbjmx> mx3;
	
	public String getZhizlx() {
		return zhizlx;
	}
	public void setZhizlx(String zhizlx) {
		this.zhizlx = zhizlx;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	public String getCangkdm() {
		return cangkdm;
	}
	public void setCangkdm(String cangkdm) {
		this.cangkdm = cangkdm;
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
	public String getLingjlx() {
		return lingjlx;
	}
	public void setLingjlx(String lingjlx) {
		this.lingjlx = lingjlx;
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
	public BigDecimal getQickc() {
		return qickc;
	}
	public void setQickc(BigDecimal qickc) {
		this.qickc = qickc;
	}
	public BigDecimal getDaixhl() {
		return daixhl;
	}
	public void setDaixhl(BigDecimal daixhl) {
		this.daixhl = daixhl;
	}
	public BigDecimal getAnqkc() {
		return anqkc;
	}
	public void setAnqkc(BigDecimal anqkc) {
		this.anqkc = anqkc;
	}
	public BigDecimal getZuidkcsl() {
		return zuidkcsl;
	}
	public void setZuidkcsl(BigDecimal zuidkcsl) {
		this.zuidkcsl = zuidkcsl;
	}
	public BigDecimal getMafkc() {
		return mafkc;
	}
	public void setMafkc(BigDecimal mafkc) {
		this.mafkc = mafkc;
	}
	public String getBaojlx() {
		return baojlx;
	}
	public void setBaojlx(String baojlx) {
		this.baojlx = baojlx;
	}
	public String getJislx() {
		return jislx;
	}
	public void setJislx(String jislx) {
		this.jislx = jislx;
	}
	public String getMaoxqbc() {
		return maoxqbc;
	}
	public void setMaoxqbc(String maoxqbc) {
		this.maoxqbc = maoxqbc;
	}
	public String getJihydm() {
		return jihydm;
	}
	public void setJihydm(String jihydm) {
		this.jihydm = jihydm;
	}
	public Integer getShifdyhzxsy() {
		return shifdyhzxsy;
	}
	public void setShifdyhzxsy(Integer shifdyhzxsy) {
		this.shifdyhzxsy = shifdyhzxsy;
	}
	public BigDecimal getWeifysl() {
		return weifysl;
	}
	public void setWeifysl(BigDecimal weifysl) {
		this.weifysl = weifysl;
	}
	public BigDecimal getYijf0() {
		return yijf0;
	}
	public void setYijf0(BigDecimal yijf0) {
		this.yijf0 = yijf0;
	}
	public BigDecimal getXuq0() {
		return xuq0;
	}
	public void setXuq0(BigDecimal xuq0) {
		this.xuq0 = xuq0;
	}
	public BigDecimal getYijf1() {
		return yijf1;
	}
	public void setYijf1(BigDecimal yijf1) {
		this.yijf1 = yijf1;
	}
	public BigDecimal getXuq1() {
		return xuq1;
	}
	public void setXuq1(BigDecimal xuq1) {
		this.xuq1 = xuq1;
	}
	public BigDecimal getYijf2() {
		return yijf2;
	}
	public void setYijf2(BigDecimal yijf2) {
		this.yijf2 = yijf2;
	}
	public BigDecimal getXuq2() {
		return xuq2;
	}
	public void setXuq2(BigDecimal xuq2) {
		this.xuq2 = xuq2;
	}
	public BigDecimal getYijf3() {
		return yijf3;
	}
	public void setYijf3(BigDecimal yijf3) {
		this.yijf3 = yijf3;
	}
	public BigDecimal getXuq3() {
		return xuq3;
	}
	public void setXuq3(BigDecimal xuq3) {
		this.xuq3 = xuq3;
	}
	public BigDecimal getYijf4() {
		return yijf4;
	}
	public void setYijf4(BigDecimal yijf4) {
		this.yijf4 = yijf4;
	}
	public BigDecimal getXuq4() {
		return xuq4;
	}
	public void setXuq4(BigDecimal xuq4) {
		this.xuq4 = xuq4;
	}
	public BigDecimal getYijf5() {
		return yijf5;
	}
	public void setYijf5(BigDecimal yijf5) {
		this.yijf5 = yijf5;
	}
	public BigDecimal getXuq5() {
		return xuq5;
	}
	public void setXuq5(BigDecimal xuq5) {
		this.xuq5 = xuq5;
	}
	public BigDecimal getYijf6() {
		return yijf6;
	}
	public void setYijf6(BigDecimal yijf6) {
		this.yijf6 = yijf6;
	}
	public BigDecimal getXuq6() {
		return xuq6;
	}
	public void setXuq6(BigDecimal xuq6) {
		this.xuq6 = xuq6;
	}
	public BigDecimal getYijf7() {
		return yijf7;
	}
	public void setYijf7(BigDecimal yijf7) {
		this.yijf7 = yijf7;
	}
	public BigDecimal getXuq7() {
		return xuq7;
	}
	public void setXuq7(BigDecimal xuq7) {
		this.xuq7 = xuq7;
	}
	public BigDecimal getYijf8() {
		return yijf8;
	}
	public void setYijf8(BigDecimal yijf8) {
		this.yijf8 = yijf8;
	}
	public BigDecimal getXuq8() {
		return xuq8;
	}
	public void setXuq8(BigDecimal xuq8) {
		this.xuq8 = xuq8;
	}
	public BigDecimal getZongyjf0() {
		return zongyjf0;
	}
	public void setZongyjf0(BigDecimal zongyjf0) {
		this.zongyjf0 = zongyjf0;
	}
	public BigDecimal getZongxq0() {
		return zongxq0;
	}
	public void setZongxq0(BigDecimal zongxq0) {
		this.zongxq0 = zongxq0;
	}
	public BigDecimal getZongyjf1() {
		return zongyjf1;
	}
	public void setZongyjf1(BigDecimal zongyjf1) {
		this.zongyjf1 = zongyjf1;
	}
	public BigDecimal getZongxq1() {
		return zongxq1;
	}
	public void setZongxq1(BigDecimal zongxq1) {
		this.zongxq1 = zongxq1;
	}
	public BigDecimal getZongyjf2() {
		return zongyjf2;
	}
	public void setZongyjf2(BigDecimal zongyjf2) {
		this.zongyjf2 = zongyjf2;
	}
	public BigDecimal getZongxq2() {
		return zongxq2;
	}
	public void setZongxq2(BigDecimal zongxq2) {
		this.zongxq2 = zongxq2;
	}
	public BigDecimal getZongyjf3() {
		return zongyjf3;
	}
	public void setZongyjf3(BigDecimal zongyjf3) {
		this.zongyjf3 = zongyjf3;
	}
	public BigDecimal getZongxq3() {
		return zongxq3;
	}
	public void setZongxq3(BigDecimal zongxq3) {
		this.zongxq3 = zongxq3;
	}
	public BigDecimal getZongyjf4() {
		return zongyjf4;
	}
	public void setZongyjf4(BigDecimal zongyjf4) {
		this.zongyjf4 = zongyjf4;
	}
	public BigDecimal getZongxq4() {
		return zongxq4;
	}
	public void setZongxq4(BigDecimal zongxq4) {
		this.zongxq4 = zongxq4;
	}
	public BigDecimal getZongyjf5() {
		return zongyjf5;
	}
	public void setZongyjf5(BigDecimal zongyjf5) {
		this.zongyjf5 = zongyjf5;
	}
	public BigDecimal getZongxq5() {
		return zongxq5;
	}
	public void setZongxq5(BigDecimal zongxq5) {
		this.zongxq5 = zongxq5;
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
	public String getDuandsj() {
		return duandsj;
	}
	public void setDuandsj(String duandsj) {
		this.duandsj = duandsj;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	
	public List<Ziygzbjmx> getMx1() {
		return mx1;
	}
	public void setMx1(List<Ziygzbjmx> mx1) {
		this.mx1 = mx1;
	}
	public List<Ziygzbjmx> getMx2() {
		return mx2;
	}
	public void setMx2(List<Ziygzbjmx> mx2) {
		this.mx2 = mx2;
	}
	public List<Ziygzbjmx> getMx3() {
		return mx3;
	}
	public void setMx3(List<Ziygzbjmx> mx3) {
		this.mx3 = mx3;
	}
	public String getNianzx0() {
		return nianzx0;
	}
	public void setNianzx0(String nianzx0) {
		this.nianzx0 = nianzx0;
	}
	public String getNianzx1() {
		return nianzx1;
	}
	public void setNianzx1(String nianzx1) {
		this.nianzx1 = nianzx1;
	}
	public String getNianzx2() {
		return nianzx2;
	}
	public void setNianzx2(String nianzx2) {
		this.nianzx2 = nianzx2;
	}
	public String getNianzx3() {
		return nianzx3;
	}
	public void setNianzx3(String nianzx3) {
		this.nianzx3 = nianzx3;
	}
	public String getNianzx4() {
		return nianzx4;
	}
	public void setNianzx4(String nianzx4) {
		this.nianzx4 = nianzx4;
	}
	public String getNianzx5() {
		return nianzx5;
	}
	public void setNianzx5(String nianzx5) {
		this.nianzx5 = nianzx5;
	}
	public String getNianzx6() {
		return nianzx6;
	}
	public void setNianzx6(String nianzx6) {
		this.nianzx6 = nianzx6;
	}
	public String getNianzx8() {
		return nianzx8;
	}
	public void setNianzx8(String nianzx8) {
		this.nianzx8 = nianzx8;
	}
	public List<Ziygzbjmx> getMx0() {
		return mx0;
	}
	public void setMx0(List<Ziygzbjmx> mx0) {
		this.mx0 = mx0;
	}
	/**
	 * 取得 nianzx9
	 * @return the nianzx9
	 */
	public String getNianzx9() {
		return nianzx9;
	}
	/**
	 * @param nianzx9 the nianzx9 to set
	 */
	public void setNianzx9(String nianzx9) {
		this.nianzx9 = nianzx9;
	}
	/**
	 * 取得 yijf9
	 * @return the yijf9
	 */
	public BigDecimal getYijf9() {
		return yijf9;
	}
	/**
	 * @param yijf9 the yijf9 to set
	 */
	public void setYijf9(BigDecimal yijf9) {
		this.yijf9 = yijf9;
	}
	/**
	 * 取得 xuq9
	 * @return the xuq9
	 */
	public BigDecimal getXuq9() {
		return xuq9;
	}
	/**
	 * @param xuq9 the xuq9 to set
	 */
	public void setXuq9(BigDecimal xuq9) {
		this.xuq9 = xuq9;
	}
	/**
	 * 取得 nianzx10
	 * @return the nianzx10
	 */
	public String getNianzx10() {
		return nianzx10;
	}
	/**
	 * @param nianzx10 the nianzx10 to set
	 */
	public void setNianzx10(String nianzx10) {
		this.nianzx10 = nianzx10;
	}
	/**
	 * 取得 yijf10
	 * @return the yijf10
	 */
	public BigDecimal getYijf10() {
		return yijf10;
	}
	/**
	 * @param yijf10 the yijf10 to set
	 */
	public void setYijf10(BigDecimal yijf10) {
		this.yijf10 = yijf10;
	}
	/**
	 * 取得 xuq10
	 * @return the xuq10
	 */
	public BigDecimal getXuq10() {
		return xuq10;
	}
	/**
	 * @param xuq10 the xuq10 to set
	 */
	public void setXuq10(BigDecimal xuq10) {
		this.xuq10 = xuq10;
	}
	/**
	 * 取得 nianzx11
	 * @return the nianzx11
	 */
	public String getNianzx11() {
		return nianzx11;
	}
	/**
	 * @param nianzx11 the nianzx11 to set
	 */
	public void setNianzx11(String nianzx11) {
		this.nianzx11 = nianzx11;
	}
	/**
	 * 取得 yijf11
	 * @return the yijf11
	 */
	public BigDecimal getYijf11() {
		return yijf11;
	}
	/**
	 * @param yijf11 the yijf11 to set
	 */
	public void setYijf11(BigDecimal yijf11) {
		this.yijf11 = yijf11;
	}
	/**
	 * 取得 xuq11
	 * @return the xuq11
	 */
	public BigDecimal getXuq11() {
		return xuq11;
	}
	/**
	 * @param xuq11 the xuq11 to set
	 */
	public void setXuq11(BigDecimal xuq11) {
		this.xuq11 = xuq11;
	}
	/**
	 * 取得 nianzx12
	 * @return the nianzx12
	 */
	public String getNianzx12() {
		return nianzx12;
	}
	/**
	 * @param nianzx12 the nianzx12 to set
	 */
	public void setNianzx12(String nianzx12) {
		this.nianzx12 = nianzx12;
	}
	/**
	 * 取得 yijf12
	 * @return the yijf12
	 */
	public BigDecimal getYijf12() {
		return yijf12;
	}
	/**
	 * @param yijf12 the yijf12 to set
	 */
	public void setYijf12(BigDecimal yijf12) {
		this.yijf12 = yijf12;
	}
	/**
	 * 取得 xuq12
	 * @return the xuq12
	 */
	public BigDecimal getXuq12() {
		return xuq12;
	}
	/**
	 * @param xuq12 the xuq12 to set
	 */
	public void setXuq12(BigDecimal xuq12) {
		this.xuq12 = xuq12;
	}
	/**
	 * 取得 nianzx13
	 * @return the nianzx13
	 */
	public String getNianzx13() {
		return nianzx13;
	}
	/**
	 * @param nianzx13 the nianzx13 to set
	 */
	public void setNianzx13(String nianzx13) {
		this.nianzx13 = nianzx13;
	}
	/**
	 * 取得 yijf13
	 * @return the yijf13
	 */
	public BigDecimal getYijf13() {
		return yijf13;
	}
	/**
	 * @param yijf13 the yijf13 to set
	 */
	public void setYijf13(BigDecimal yijf13) {
		this.yijf13 = yijf13;
	}
	/**
	 * 取得 xuq13
	 * @return the xuq13
	 */
	public BigDecimal getXuq13() {
		return xuq13;
	}
	/**
	 * @param xuq13 the xuq13 to set
	 */
	public void setXuq13(BigDecimal xuq13) {
		this.xuq13 = xuq13;
	}
	/**
	 * 取得 nianzx14
	 * @return the nianzx14
	 */
	public String getNianzx14() {
		return nianzx14;
	}
	/**
	 * @param nianzx14 the nianzx14 to set
	 */
	public void setNianzx14(String nianzx14) {
		this.nianzx14 = nianzx14;
	}
	/**
	 * 取得 yijf14
	 * @return the yijf14
	 */
	public BigDecimal getYijf14() {
		return yijf14;
	}
	/**
	 * @param yijf14 the yijf14 to set
	 */
	public void setYijf14(BigDecimal yijf14) {
		this.yijf14 = yijf14;
	}
	/**
	 * 取得 xuq14
	 * @return the xuq14
	 */
	public BigDecimal getXuq14() {
		return xuq14;
	}
	/**
	 * @param xuq14 the xuq14 to set
	 */
	public void setXuq14(BigDecimal xuq14) {
		this.xuq14 = xuq14;
	}
	/**
	 * 取得 nianzx15
	 * @return the nianzx15
	 */
	public String getNianzx15() {
		return nianzx15;
	}
	/**
	 * @param nianzx15 the nianzx15 to set
	 */
	public void setNianzx15(String nianzx15) {
		this.nianzx15 = nianzx15;
	}
	/**
	 * 取得 yijf15
	 * @return the yijf15
	 */
	public BigDecimal getYijf15() {
		return yijf15;
	}
	/**
	 * @param yijf15 the yijf15 to set
	 */
	public void setYijf15(BigDecimal yijf15) {
		this.yijf15 = yijf15;
	}
	/**
	 * 取得 xuq15
	 * @return the xuq15
	 */
	public BigDecimal getXuq15() {
		return xuq15;
	}
	/**
	 * @param xuq15 the xuq15 to set
	 */
	public void setXuq15(BigDecimal xuq15) {
		this.xuq15 = xuq15;
	}
	/**
	 * 取得 nianzx16
	 * @return the nianzx16
	 */
	public String getNianzx16() {
		return nianzx16;
	}
	/**
	 * @param nianzx16 the nianzx16 to set
	 */
	public void setNianzx16(String nianzx16) {
		this.nianzx16 = nianzx16;
	}
	/**
	 * 取得 yijf16
	 * @return the yijf16
	 */
	public BigDecimal getYijf16() {
		return yijf16;
	}
	/**
	 * @param yijf16 the yijf16 to set
	 */
	public void setYijf16(BigDecimal yijf16) {
		this.yijf16 = yijf16;
	}
	/**
	 * 取得 xuq16
	 * @return the xuq16
	 */
	public BigDecimal getXuq16() {
		return xuq16;
	}
	/**
	 * @param xuq16 the xuq16 to set
	 */
	public void setXuq16(BigDecimal xuq16) {
		this.xuq16 = xuq16;
	}
	/**
	 * 取得 nianzx17
	 * @return the nianzx17
	 */
	public String getNianzx17() {
		return nianzx17;
	}
	/**
	 * @param nianzx17 the nianzx17 to set
	 */
	public void setNianzx17(String nianzx17) {
		this.nianzx17 = nianzx17;
	}
	/**
	 * 取得 yijf17
	 * @return the yijf17
	 */
	public BigDecimal getYijf17() {
		return yijf17;
	}
	/**
	 * @param yijf17 the yijf17 to set
	 */
	public void setYijf17(BigDecimal yijf17) {
		this.yijf17 = yijf17;
	}
	/**
	 * 取得 xuq17
	 * @return the xuq17
	 */
	public BigDecimal getXuq17() {
		return xuq17;
	}
	/**
	 * @param xuq17 the xuq17 to set
	 */
	public void setXuq17(BigDecimal xuq17) {
		this.xuq17 = xuq17;
	}
	/**
	 * 取得 nianzx18
	 * @return the nianzx18
	 */
	public String getNianzx18() {
		return nianzx18;
	}
	/**
	 * @param nianzx18 the nianzx18 to set
	 */
	public void setNianzx18(String nianzx18) {
		this.nianzx18 = nianzx18;
	}
	/**
	 * 取得 yijf18
	 * @return the yijf18
	 */
	public BigDecimal getYijf18() {
		return yijf18;
	}
	/**
	 * @param yijf18 the yijf18 to set
	 */
	public void setYijf18(BigDecimal yijf18) {
		this.yijf18 = yijf18;
	}
	/**
	 * 取得 xuq18
	 * @return the xuq18
	 */
	public BigDecimal getXuq18() {
		return xuq18;
	}
	/**
	 * @param xuq18 the xuq18 to set
	 */
	public void setXuq18(BigDecimal xuq18) {
		this.xuq18 = xuq18;
	}
	/**
	 * 取得 nianzx19
	 * @return the nianzx19
	 */
	public String getNianzx19() {
		return nianzx19;
	}
	/**
	 * @param nianzx19 the nianzx19 to set
	 */
	public void setNianzx19(String nianzx19) {
		this.nianzx19 = nianzx19;
	}
	/**
	 * 取得 yijf19
	 * @return the yijf19
	 */
	public BigDecimal getYijf19() {
		return yijf19;
	}
	/**
	 * @param yijf19 the yijf19 to set
	 */
	public void setYijf19(BigDecimal yijf19) {
		this.yijf19 = yijf19;
	}
	/**
	 * 取得 xuq19
	 * @return the xuq19
	 */
	public BigDecimal getXuq19() {
		return xuq19;
	}
	/**
	 * @param xuq19 the xuq19 to set
	 */
	public void setXuq19(BigDecimal xuq19) {
		this.xuq19 = xuq19;
	}
	/**
	 * 取得 nianzx20
	 * @return the nianzx20
	 */
	public String getNianzx20() {
		return nianzx20;
	}
	/**
	 * @param nianzx20 the nianzx20 to set
	 */
	public void setNianzx20(String nianzx20) {
		this.nianzx20 = nianzx20;
	}
	/**
	 * 取得 yijf20
	 * @return the yijf20
	 */
	public BigDecimal getYijf20() {
		return yijf20;
	}
	/**
	 * @param yijf20 the yijf20 to set
	 */
	public void setYijf20(BigDecimal yijf20) {
		this.yijf20 = yijf20;
	}
	/**
	 * 取得 xuq20
	 * @return the xuq20
	 */
	public BigDecimal getXuq20() {
		return xuq20;
	}
	/**
	 * @param xuq20 the xuq20 to set
	 */
	public void setXuq20(BigDecimal xuq20) {
		this.xuq20 = xuq20;
	}
	/**
	 * 取得 nianzx21
	 * @return the nianzx21
	 */
	public String getNianzx21() {
		return nianzx21;
	}
	/**
	 * @param nianzx21 the nianzx21 to set
	 */
	public void setNianzx21(String nianzx21) {
		this.nianzx21 = nianzx21;
	}
	/**
	 * 取得 yijf21
	 * @return the yijf21
	 */
	public BigDecimal getYijf21() {
		return yijf21;
	}
	/**
	 * @param yijf21 the yijf21 to set
	 */
	public void setYijf21(BigDecimal yijf21) {
		this.yijf21 = yijf21;
	}
	/**
	 * 取得 xuq21
	 * @return the xuq21
	 */
	public BigDecimal getXuq21() {
		return xuq21;
	}
	/**
	 * @param xuq21 the xuq21 to set
	 */
	public void setXuq21(BigDecimal xuq21) {
		this.xuq21 = xuq21;
	}
	/**
	 * 取得 nianzx22
	 * @return the nianzx22
	 */
	public String getNianzx22() {
		return nianzx22;
	}
	/**
	 * @param nianzx22 the nianzx22 to set
	 */
	public void setNianzx22(String nianzx22) {
		this.nianzx22 = nianzx22;
	}
	/**
	 * 取得 yijf22
	 * @return the yijf22
	 */
	public BigDecimal getYijf22() {
		return yijf22;
	}
	/**
	 * @param yijf22 the yijf22 to set
	 */
	public void setYijf22(BigDecimal yijf22) {
		this.yijf22 = yijf22;
	}
	/**
	 * 取得 xuq22
	 * @return the xuq22
	 */
	public BigDecimal getXuq22() {
		return xuq22;
	}
	/**
	 * @param xuq22 the xuq22 to set
	 */
	public void setXuq22(BigDecimal xuq22) {
		this.xuq22 = xuq22;
	}
	/**
	 * 取得 nianzx23
	 * @return the nianzx23
	 */
	public String getNianzx23() {
		return nianzx23;
	}
	/**
	 * @param nianzx23 the nianzx23 to set
	 */
	public void setNianzx23(String nianzx23) {
		this.nianzx23 = nianzx23;
	}
	/**
	 * 取得 yijf23
	 * @return the yijf23
	 */
	public BigDecimal getYijf23() {
		return yijf23;
	}
	/**
	 * @param yijf23 the yijf23 to set
	 */
	public void setYijf23(BigDecimal yijf23) {
		this.yijf23 = yijf23;
	}
	/**
	 * 取得 xuq23
	 * @return the xuq23
	 */
	public BigDecimal getXuq23() {
		return xuq23;
	}
	/**
	 * @param xuq23 the xuq23 to set
	 */
	public void setXuq23(BigDecimal xuq23) {
		this.xuq23 = xuq23;
	}
	/**
	 * 取得 nianzx24
	 * @return the nianzx24
	 */
	public String getNianzx24() {
		return nianzx24;
	}
	/**
	 * @param nianzx24 the nianzx24 to set
	 */
	public void setNianzx24(String nianzx24) {
		this.nianzx24 = nianzx24;
	}
	/**
	 * 取得 yijf24
	 * @return the yijf24
	 */
	public BigDecimal getYijf24() {
		return yijf24;
	}
	/**
	 * @param yijf24 the yijf24 to set
	 */
	public void setYijf24(BigDecimal yijf24) {
		this.yijf24 = yijf24;
	}
	/**
	 * 取得 xuq24
	 * @return the xuq24
	 */
	public BigDecimal getXuq24() {
		return xuq24;
	}
	/**
	 * @param xuq24 the xuq24 to set
	 */
	public void setXuq24(BigDecimal xuq24) {
		this.xuq24 = xuq24;
	}
	/**
	 * 取得 nianzx25
	 * @return the nianzx25
	 */
	public String getNianzx25() {
		return nianzx25;
	}
	/**
	 * @param nianzx25 the nianzx25 to set
	 */
	public void setNianzx25(String nianzx25) {
		this.nianzx25 = nianzx25;
	}
	/**
	 * 取得 yijf25
	 * @return the yijf25
	 */
	public BigDecimal getYijf25() {
		return yijf25;
	}
	/**
	 * @param yijf25 the yijf25 to set
	 */
	public void setYijf25(BigDecimal yijf25) {
		this.yijf25 = yijf25;
	}
	/**
	 * 取得 xuq25
	 * @return the xuq25
	 */
	public BigDecimal getXuq25() {
		return xuq25;
	}
	/**
	 * @param xuq25 the xuq25 to set
	 */
	public void setXuq25(BigDecimal xuq25) {
		this.xuq25 = xuq25;
	}
	/**
	 * 取得 nianzx26
	 * @return the nianzx26
	 */
	public String getNianzx26() {
		return nianzx26;
	}
	/**
	 * @param nianzx26 the nianzx26 to set
	 */
	public void setNianzx26(String nianzx26) {
		this.nianzx26 = nianzx26;
	}
	/**
	 * 取得 yijf26
	 * @return the yijf26
	 */
	public BigDecimal getYijf26() {
		return yijf26;
	}
	/**
	 * @param yijf26 the yijf26 to set
	 */
	public void setYijf26(BigDecimal yijf26) {
		this.yijf26 = yijf26;
	}
	/**
	 * 取得 xuq26
	 * @return the xuq26
	 */
	public BigDecimal getXuq26() {
		return xuq26;
	}
	/**
	 * @param xuq26 the xuq26 to set
	 */
	public void setXuq26(BigDecimal xuq26) {
		this.xuq26 = xuq26;
	}
	/**
	 * 取得 nianzx27
	 * @return the nianzx27
	 */
	public String getNianzx27() {
		return nianzx27;
	}
	/**
	 * @param nianzx27 the nianzx27 to set
	 */
	public void setNianzx27(String nianzx27) {
		this.nianzx27 = nianzx27;
	}
	/**
	 * 取得 yijf27
	 * @return the yijf27
	 */
	public BigDecimal getYijf27() {
		return yijf27;
	}
	/**
	 * @param yijf27 the yijf27 to set
	 */
	public void setYijf27(BigDecimal yijf27) {
		this.yijf27 = yijf27;
	}
	/**
	 * 取得 xuq27
	 * @return the xuq27
	 */
	public BigDecimal getXuq27() {
		return xuq27;
	}
	/**
	 * @param xuq27 the xuq27 to set
	 */
	public void setXuq27(BigDecimal xuq27) {
		this.xuq27 = xuq27;
	}
	/**
	 * 取得 nianzx28
	 * @return the nianzx28
	 */
	public String getNianzx28() {
		return nianzx28;
	}
	/**
	 * @param nianzx28 the nianzx28 to set
	 */
	public void setNianzx28(String nianzx28) {
		this.nianzx28 = nianzx28;
	}
	/**
	 * 取得 yijf28
	 * @return the yijf28
	 */
	public BigDecimal getYijf28() {
		return yijf28;
	}
	/**
	 * @param yijf28 the yijf28 to set
	 */
	public void setYijf28(BigDecimal yijf28) {
		this.yijf28 = yijf28;
	}
	/**
	 * 取得 xuq28
	 * @return the xuq28
	 */
	public BigDecimal getXuq28() {
		return xuq28;
	}
	/**
	 * @param xuq28 the xuq28 to set
	 */
	public void setXuq28(BigDecimal xuq28) {
		this.xuq28 = xuq28;
	}
	/**
	 * 取得 nianzx29
	 * @return the nianzx29
	 */
	public String getNianzx29() {
		return nianzx29;
	}
	/**
	 * @param nianzx29 the nianzx29 to set
	 */
	public void setNianzx29(String nianzx29) {
		this.nianzx29 = nianzx29;
	}
	/**
	 * 取得 yijf29
	 * @return the yijf29
	 */
	public BigDecimal getYijf29() {
		return yijf29;
	}
	/**
	 * @param yijf29 the yijf29 to set
	 */
	public void setYijf29(BigDecimal yijf29) {
		this.yijf29 = yijf29;
	}
	/**
	 * 取得 xuq29
	 * @return the xuq29
	 */
	public BigDecimal getXuq29() {
		return xuq29;
	}
	/**
	 * @param xuq29 the xuq29 to set
	 */
	public void setXuq29(BigDecimal xuq29) {
		this.xuq29 = xuq29;
	}
	/**
	 * 取得 zygy
	 * @return the zygy
	 */
	public String getZygy() {
		return zygy;
	}
	/**
	 * @param zygy the zygy to set
	 */
	public void setZygy(String zygy) {
		this.zygy = zygy;
	}

	
}
