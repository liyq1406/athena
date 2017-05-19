package com.athena.xqjs.entity.hlorder;

import java.math.BigDecimal;

public class MjjszjbTmp {
    private String id;//UUID主键

    private String usercenter;//用户中心

    private String lingjbh;//零件编号

    private String lujbh;//路径编号

    private String lujmc;//路径名称

    private String zhidgys;//指定供应商

    private String jiaofm;//交付码

    private BigDecimal beihzq;//备货周期

    private BigDecimal yunszq;//运输周期

    private String gcbh;//承运商\供应商

    private String xiehztbh;//卸货站台编号

    private BigDecimal songhpc;//送货频次

    private String mudd;//目的地

    private String dinghck;//定货库

    private String mos2;//模式2

    private BigDecimal cangkshpc2;//仓库送货频次2

    private BigDecimal cangkshsj2;//仓库送货时间2

    private BigDecimal cangkfhsj2;//仓库返回时间2

    private BigDecimal beihsj2;//备货时间2

    private String xianbck;//线边库

    private String mos;//模式

    private BigDecimal beihsj;//备货时间

    private BigDecimal beihsjc;//备货时间C

    private BigDecimal gongysfe;//供应份额

    private String dinghcklx;//定货库类型

    private String gongyslx;//供应商类型

    private String shengcxbh;//生产线编号

    private String gongysbh;//供应商

    private String ckusbzlx;//仓库US的包装类型

    private BigDecimal ckusbzrl;//仓库US的包装容量

    private String ckuclx;//上线UC类型

    private BigDecimal ckucrl;//上线UC容量

    private String zickbh;//子仓库编号

    private BigDecimal anqkcsl;//安全库存数量

    private BigDecimal ckxittzz;//系统调整值(年末 表达总量-交付总量)

    private String jianglms;//将来模式

    private String jianglms2;//将来模式2

    private BigDecimal ckyifyhlzl;//订单表达总量

    private BigDecimal ckzhongzzl;//订单终止总量

    private BigDecimal ckjiaofzl;//已交付总量

    private String zhongwmc;//零件中文名称

    private String jihy;//计划员组

    private String lingjsx;//零件属性(A零件,K卷料,M辅料)

    private String danw;//单位

    private String shengxsj;//将来模式生效时间

    private String shengxsj2;//将来模式生效时间2

    private String fahd;//发货地

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getUsercenter() {
        return usercenter;
    }

    public void setUsercenter(String usercenter) {
        this.usercenter = usercenter == null ? null : usercenter.trim();
    }

    public String getLingjbh() {
        return lingjbh;
    }

    public void setLingjbh(String lingjbh) {
        this.lingjbh = lingjbh == null ? null : lingjbh.trim();
    }

    public String getLujbh() {
        return lujbh;
    }

    public void setLujbh(String lujbh) {
        this.lujbh = lujbh == null ? null : lujbh.trim();
    }

    public String getLujmc() {
        return lujmc;
    }

    public void setLujmc(String lujmc) {
        this.lujmc = lujmc == null ? null : lujmc.trim();
    }

    public String getZhidgys() {
        return zhidgys;
    }

    public void setZhidgys(String zhidgys) {
        this.zhidgys = zhidgys == null ? null : zhidgys.trim();
    }

    public String getJiaofm() {
        return jiaofm;
    }

    public void setJiaofm(String jiaofm) {
        this.jiaofm = jiaofm == null ? null : jiaofm.trim();
    }

    public BigDecimal getBeihzq() {
        return beihzq;
    }

    public void setBeihzq(BigDecimal beihzq) {
        this.beihzq = beihzq;
    }

    public BigDecimal getYunszq() {
        return yunszq;
    }

    public void setYunszq(BigDecimal yunszq) {
        this.yunszq = yunszq;
    }

    public String getGcbh() {
        return gcbh;
    }

    public void setGcbh(String gcbh) {
        this.gcbh = gcbh == null ? null : gcbh.trim();
    }

    public String getXiehztbh() {
        return xiehztbh;
    }

    public void setXiehztbh(String xiehztbh) {
        this.xiehztbh = xiehztbh == null ? null : xiehztbh.trim();
    }

    public BigDecimal getSonghpc() {
        return songhpc;
    }

    public void setSonghpc(BigDecimal songhpc) {
        this.songhpc = songhpc;
    }

    public String getMudd() {
        return mudd;
    }

    public void setMudd(String mudd) {
        this.mudd = mudd == null ? null : mudd.trim();
    }

    public String getDinghck() {
        return dinghck;
    }

    public void setDinghck(String dinghck) {
        this.dinghck = dinghck == null ? null : dinghck.trim();
    }

    public String getMos2() {
        return mos2;
    }

    public void setMos2(String mos2) {
        this.mos2 = mos2 == null ? null : mos2.trim();
    }

    public BigDecimal getCangkshpc2() {
        return cangkshpc2;
    }

    public void setCangkshpc2(BigDecimal cangkshpc2) {
        this.cangkshpc2 = cangkshpc2;
    }

    public BigDecimal getCangkshsj2() {
        return cangkshsj2;
    }

    public void setCangkshsj2(BigDecimal cangkshsj2) {
        this.cangkshsj2 = cangkshsj2;
    }

    public BigDecimal getCangkfhsj2() {
        return cangkfhsj2;
    }

    public void setCangkfhsj2(BigDecimal cangkfhsj2) {
        this.cangkfhsj2 = cangkfhsj2;
    }

    public BigDecimal getBeihsj2() {
        return beihsj2;
    }

    public void setBeihsj2(BigDecimal beihsj2) {
        this.beihsj2 = beihsj2;
    }

    public String getXianbck() {
        return xianbck;
    }

    public void setXianbck(String xianbck) {
        this.xianbck = xianbck == null ? null : xianbck.trim();
    }

    public String getMos() {
        return mos;
    }

    public void setMos(String mos) {
        this.mos = mos == null ? null : mos.trim();
    }

    public BigDecimal getBeihsj() {
        return beihsj;
    }

    public void setBeihsj(BigDecimal beihsj) {
        this.beihsj = beihsj;
    }

    public BigDecimal getBeihsjc() {
        return beihsjc;
    }

    public void setBeihsjc(BigDecimal beihsjc) {
        this.beihsjc = beihsjc;
    }

    public BigDecimal getGongysfe() {
        return gongysfe;
    }

    public void setGongysfe(BigDecimal gongysfe) {
        this.gongysfe = gongysfe;
    }

    public String getDinghcklx() {
        return dinghcklx;
    }

    public void setDinghcklx(String dinghcklx) {
        this.dinghcklx = dinghcklx == null ? null : dinghcklx.trim();
    }

    public String getGongyslx() {
        return gongyslx;
    }

    public void setGongyslx(String gongyslx) {
        this.gongyslx = gongyslx == null ? null : gongyslx.trim();
    }

    public String getShengcxbh() {
        return shengcxbh;
    }

    public void setShengcxbh(String shengcxbh) {
        this.shengcxbh = shengcxbh == null ? null : shengcxbh.trim();
    }

    public String getGongysbh() {
        return gongysbh;
    }

    public void setGongysbh(String gongysbh) {
        this.gongysbh = gongysbh == null ? null : gongysbh.trim();
    }

    public String getCkusbzlx() {
        return ckusbzlx;
    }

    public void setCkusbzlx(String ckusbzlx) {
        this.ckusbzlx = ckusbzlx == null ? null : ckusbzlx.trim();
    }

    public BigDecimal getCkusbzrl() {
        return ckusbzrl;
    }

    public void setCkusbzrl(BigDecimal ckusbzrl) {
        this.ckusbzrl = ckusbzrl;
    }

    public String getCkuclx() {
        return ckuclx;
    }

    public void setCkuclx(String ckuclx) {
        this.ckuclx = ckuclx == null ? null : ckuclx.trim();
    }

    public BigDecimal getCkucrl() {
        return ckucrl;
    }

    public void setCkucrl(BigDecimal ckucrl) {
        this.ckucrl = ckucrl;
    }

    public String getZickbh() {
        return zickbh;
    }

    public void setZickbh(String zickbh) {
        this.zickbh = zickbh == null ? null : zickbh.trim();
    }

    public BigDecimal getAnqkcsl() {
        return anqkcsl;
    }

    public void setAnqkcsl(BigDecimal anqkcsl) {
        this.anqkcsl = anqkcsl;
    }

    public BigDecimal getCkxittzz() {
        return ckxittzz;
    }

    public void setCkxittzz(BigDecimal ckxittzz) {
        this.ckxittzz = ckxittzz;
    }

    public String getJianglms() {
        return jianglms;
    }

    public void setJianglms(String jianglms) {
        this.jianglms = jianglms == null ? null : jianglms.trim();
    }

    public String getJianglms2() {
        return jianglms2;
    }

    public void setJianglms2(String jianglms2) {
        this.jianglms2 = jianglms2 == null ? null : jianglms2.trim();
    }

    public BigDecimal getCkyifyhlzl() {
        return ckyifyhlzl;
    }

    public void setCkyifyhlzl(BigDecimal ckyifyhlzl) {
        this.ckyifyhlzl = ckyifyhlzl;
    }

    public BigDecimal getCkzhongzzl() {
        return ckzhongzzl;
    }

    public void setCkzhongzzl(BigDecimal ckzhongzzl) {
        this.ckzhongzzl = ckzhongzzl;
    }

    public BigDecimal getCkjiaofzl() {
        return ckjiaofzl;
    }

    public void setCkjiaofzl(BigDecimal ckjiaofzl) {
        this.ckjiaofzl = ckjiaofzl;
    }

    public String getZhongwmc() {
        return zhongwmc;
    }

    public void setZhongwmc(String zhongwmc) {
        this.zhongwmc = zhongwmc == null ? null : zhongwmc.trim();
    }

    public String getJihy() {
        return jihy;
    }

    public void setJihy(String jihy) {
        this.jihy = jihy == null ? null : jihy.trim();
    }

    public String getLingjsx() {
        return lingjsx;
    }

    public void setLingjsx(String lingjsx) {
        this.lingjsx = lingjsx == null ? null : lingjsx.trim();
    }

    public String getDanw() {
        return danw;
    }

    public void setDanw(String danw) {
        this.danw = danw == null ? null : danw.trim();
    }

    public String getShengxsj() {
        return shengxsj;
    }

    public void setShengxsj(String shengxsj) {
        this.shengxsj = shengxsj == null ? null : shengxsj.trim();
    }

    public String getShengxsj2() {
        return shengxsj2;
    }

    public void setShengxsj2(String shengxsj2) {
        this.shengxsj2 = shengxsj2 == null ? null : shengxsj2.trim();
    }

    public String getFahd() {
        return fahd;
    }

    public void setFahd(String fahd) {
        this.fahd = fahd == null ? null : fahd.trim();
    }
}