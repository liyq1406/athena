package com.athena.xqjs.entity.quhyuns;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

public class Rukmx extends PageableSupport implements Domain{
	
	private static final long serialVersionUID = 1861676571164967553L;
	
	private String usercenter;		//用户名称
	
	private String lingjbh;		//零件编号
	
	private String lingjmc;		//零件名称
	
	private Double lingjsl;		//零件数量
	
	private String gongysdm;	//承运商代码
	
	private String gongysmc;	//承运商名称
	
	private String chengysdm;	//承运商代码
	
	private String chengysmc;  //承运商名称

	private String baozxh;		//包装型号
	
	private Double baozrl;		//包装容量
	
	private String ruksj;		//入库时间
	

	
	private String lingjlx;		//零件类型
	
	private String danjlx;		//单据类型
	
	private Double xinljdj;		//新零件单价
	
	private Double lingjdj;		//零件单价
	
	private Double xinlfmdj;	//新立方米单价
	
	private Double lifmdj;		//立方米单价
	
	private Double xinysfy;		//新运输费用
	
	private Double yunsfy;		//运输费用
		
	private Double xinfkfy;		//新返空费用	
	
	private Double fankfy;		//返空费用
	
	private Double xintpfy;     //新托盘费用
	
	private Double tuopfy;      //托盘费用
	
	private Double xintpfkfy;	//新托盘返空费用
	
	private Double tuopfkfy;	//托盘返空费用
	
	private Double xinjjfy;	    //新紧急费用
	
	private Double jinjfy;	   //紧急费用
	
	private Double xinlxfy;	   //新零星费用
	
	private Double lingxfy;	   //零星费用
		
	private Integer baozgs;		//包装个数
	
	private String creator;		//创建人
	
	private String create_time;	//创建时间
		
	private String editor;		//修改人
	
	private String edit_time;	//修改时间
	
	private String biaos;	    //标识

	private String shenhzt;	    //审核状态
	
	private String danjmc;		//单据名称
	
	private String danjh;		//单据名称
	
	private String jiszt;	//计算状态
	
	private String qisruksj;	//起始入库日期
	
	private String jisruksj;	//结束入库日期
	
	
	private Double zhengctj; //正常件体积
	
	private Double tuoptj; //托盘体积
	
	private Double lingxjtj; //零星件体积
	
	private Double zhengcjfktj; //正常件返空体积
	
	private Double tuopfktj; //托盘返空体积
	

	
    private Double yzhengctj; //原正常件体积
	
	private Double ytuoptj; //原托盘体积
	
	private Double ylingxjtj; //原零星件体积
	
	private Double yzhengcjfktj; //原正常件返空体积
	
	private Double ytuopfktj; //原托盘返空体积
	

	private String jinjjdjh;  //紧急件单据号
	
	private String ybaozxh;  //原包装型号
	
	private String  ybaozrl;//原包装容量
	
	private Double  ytangc;  //原趟次
	
	
	
	private String lxjlb;		//零星件类别
	
	
	private String uclist; //用户组对应的有权限的用户中心
	
	
	
	public String getUclist() {
		return uclist;
	}

	public void setUclist(String uclist) {
		this.uclist = uclist;
	}


	public String getLxjlb() {
		return lxjlb;
	}

	public void setLxjlb(String lxjlb) {
		this.lxjlb = lxjlb;
	}

	public Double getYtangc() {
		return ytangc;
	}

	public void setYtangc(Double ytangc) {
		this.ytangc = ytangc;
	}

	public String getYbaozrl() {
		return ybaozrl;
	}

	public void setYbaozrl(String ybaozrl) {
		this.ybaozrl = ybaozrl;
	}

	public String getYbaozxh() {
		return ybaozxh;
	}

	public void setYbaozxh(String ybaozxh) {
		this.ybaozxh = ybaozxh;
	}

	public Double getYzhengctj() {
		return yzhengctj;
	}

	public void setYzhengctj(Double yzhengctj) {
		this.yzhengctj = yzhengctj;
	}

	public Double getYtuoptj() {
		return ytuoptj;
	}

	public void setYtuoptj(Double ytuoptj) {
		this.ytuoptj = ytuoptj;
	}

	public Double getYlingxjtj() {
		return ylingxjtj;
	}

	public void setYlingxjtj(Double ylingxjtj) {
		this.ylingxjtj = ylingxjtj;
	}

	public Double getYzhengcjfktj() {
		return yzhengcjfktj;
	}

	public void setYzhengcjfktj(Double yzhengcjfktj) {
		this.yzhengcjfktj = yzhengcjfktj;
	}

	public Double getYtuopfktj() {
		return ytuopfktj;
	}

	public void setYtuopfktj(Double ytuopfktj) {
		this.ytuopfktj = ytuopfktj;
	}

	



	
	
	public String getJinjjdjh() {
		return jinjjdjh;
	}

	public void setJinjjdjh(String jinjjdjh) {
		this.jinjjdjh = jinjjdjh;
	}

	public Double getZhengctj() {
		return zhengctj;
	}

	public void setZhengctj(Double zhengctj) {
		this.zhengctj = zhengctj;
	}

	public Double getTuoptj() {
		return tuoptj;
	}

	public void setTuoptj(Double tuoptj) {
		this.tuoptj = tuoptj;
	}

	public Double getLingxjtj() {
		return lingxjtj;
	}

	public void setLingxjtj(Double lingxjtj) {
		this.lingxjtj = lingxjtj;
	}

	public Double getZhengcjfktj() {
		return zhengcjfktj;
	}

	public void setZhengcjfktj(Double zhengcjfktj) {
		this.zhengcjfktj = zhengcjfktj;
	}

	public Double getTuopfktj() {
		return tuopfktj;
	}

	public void setTuopfktj(Double tuopfktj) {
		this.tuopfktj = tuopfktj;
	}

	public String getQisruksj() {
		return qisruksj;
	}

	public void setQisruksj(String qisruksj) {
		this.qisruksj = qisruksj;
	}

	public String getJisruksj() {
		return jisruksj;
	}

	public void setJisruksj(String jisruksj) {
		this.jisruksj = jisruksj;
	}

	public Double getXinlfmdj() {
		return xinlfmdj;
	}

	public void setXinlfmdj(Double xinlfmdj) {
		this.xinlfmdj = xinlfmdj;
	}

	public Double getLifmdj() {
		return lifmdj;
	}

	public void setLifmdj(Double lifmdj) {
		this.lifmdj = lifmdj;
	}

	public Double getXinysfy() {
		return xinysfy;
	}

	public void setXinysfy(Double xinysfy) {
		this.xinysfy = xinysfy;
	}

	public Double getYunsfy() {
		return yunsfy;
	}

	public void setYunsfy(Double yunsfy) {
		this.yunsfy = yunsfy;
	}

	public Double getXinfkfy() {
		return xinfkfy;
	}

	public void setXinfkfy(Double xinfkfy) {
		this.xinfkfy = xinfkfy;
	}

	public Double getFankfy() {
		return fankfy;
	}

	public void setFankfy(Double fankfy) {
		this.fankfy = fankfy;
	}

	public Double getXintpfy() {
		return xintpfy;
	}

	public void setXintpfy(Double xintpfy) {
		this.xintpfy = xintpfy;
	}

	public Double getTuopfy() {
		return tuopfy;
	}

	public void setTuopfy(Double tuopfy) {
		this.tuopfy = tuopfy;
	}

	public Double getXintpfkfy() {
		return xintpfkfy;
	}

	public void setXintpfkfy(Double xintpfkfy) {
		this.xintpfkfy = xintpfkfy;
	}

	public Double getTuopfkfy() {
		return tuopfkfy;
	}

	public void setTuopfkfy(Double tuopfkfy) {
		this.tuopfkfy = tuopfkfy;
	}

	public Double getXinjjfy() {
		return xinjjfy;
	}

	public void setXinjjfy(Double xinjjfy) {
		this.xinjjfy = xinjjfy;
	}

	public Double getJinjfy() {
		return jinjfy;
	}

	public void setJinjfy(Double jinjfy) {
		this.jinjfy = jinjfy;
	}

	public Double getXinlxfy() {
		return xinlxfy;
	}

	public void setXinlxfy(Double xinlxfy) {
		this.xinlxfy = xinlxfy;
	}

	public Double getLingxfy() {
		return lingxfy;
	}

	public void setLingxfy(Double lingxfy) {
		this.lingxfy = lingxfy;
	}

	public String getJiszt() {
		return jiszt;
	}

	public void setJiszt(String jiszt) {
		this.jiszt = jiszt;
	}

	public String getDanjh() {
		return danjh;
	}

	public void setDanjh(String danjh) {
		this.danjh = danjh;
	}

	

	public String getDanjmc() {
		return danjmc;
	}

	public void setDanjmc(String danjmc) {
		this.danjmc = danjmc;
	}

	public String getShenhzt() {
		return shenhzt;
	}

	public void setShenhzt(String shenhzt) {
		this.shenhzt = shenhzt;
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

	public String getLingjmc() {
		return lingjmc;
	}

	public void setLingjmc(String lingjmc) {
		this.lingjmc = lingjmc;
	}





	public Double getLingjsl() {
		return lingjsl;
	}

	public void setLingjsl(Double lingjsl) {
		this.lingjsl = lingjsl;
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

	public String getBaozxh() {
		return baozxh;
	}

	public void setBaozxh(String baozxh) {
		this.baozxh = baozxh;
	}



	public Double getBaozrl() {
		return baozrl;
	}

	public void setBaozrl(Double baozrl) {
		this.baozrl = baozrl;
	}

	public String getRuksj() {
		return ruksj;
	}

	public void setRuksj(String ruksj) {
		this.ruksj = ruksj;
	}



	public String getLingjlx() {
		return lingjlx;
	}

	public void setLingjlx(String lingjlx) {
		this.lingjlx = lingjlx;
	}

	public String getDanjlx() {
		return danjlx;
	}

	public void setDanjlx(String danjlx) {
		this.danjlx = danjlx;
	}


	
	public Double getXinljdj() {
		return xinljdj;
	}

	public void setXinljdj(Double xinljdj) {
		this.xinljdj = xinljdj;
	}

	public Double getLingjdj() {
		return lingjdj;
	}

	public void setLingjdj(Double lingjdj) {
		this.lingjdj = lingjdj;
	}



	public Integer getBaozgs() {
		return baozgs;
	}

	public void setBaozgs(Integer baozgs) {
		this.baozgs = baozgs;
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

	public String getBiaos() {
		return biaos;
	}

	public void setBiaos(String biaos) {
		this.biaos = biaos;
	}

	public void setId(String id) {
		
	}

	public String getId() {
		return null;
	}
	
	
	
}
