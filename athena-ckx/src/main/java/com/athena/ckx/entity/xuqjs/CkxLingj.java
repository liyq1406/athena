package com.athena.ckx.entity.xuqjs;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

public class CkxLingj extends PageableSupport implements Domain{
	
	private static final long serialVersionUID = 4322051830747332856L;

	private String usercenter;	//用户中心
	
	private String lingjbh;		//零件编号
	
	private String lingjlx;		//零件类型
	
	private String zhongwmc;	//中文名称
	
	private String fawmc;   	//法文名称
	
	private String lingjsx;		//零件属性
	
	private String danw;		//单位
	
	private String zhizlx;		//制造路线
	
	private String kaisrq;		//开始日期
	
	private String jiesrq;		//结束日期
	
	private Double zhuangcxs;	//装车系数
	
	private String anqm;		//安全码
	
	private Double lingjzl;		//零件重量
	
	private String biaos;		//生效标识
	
	private String guanjljjb;	//关键零件级别
	
	private String dinghcj;		//订货车间
	
	private String gongybm;		//工艺编码
	
	private String zongcldm;	//总成类型代码
	
	private String diycqysj;	//第一次启运时间
	
	private String creator;		//创建人
	 
	private String create_time;	//创建时间
	 
	private String editor;		//修改人
	 
	private String edit_time;	//修改时间
	
	private String jihy;		//计划员
	
	private String wuly;		//物流员
	
	private String wlgyy;		//物流工艺员
	
	private String zhijy;		//质检员
	
	private String pcjhy;		//排产计划员组（删掉）
	
	private String role;		//角色
	
	private String anjmlxhd; //按件目录卸货点
	
	private String chejmc;     //车间名称(根据左连接查询得到)
	
	


	public String getChejmc() {
		return chejmc;
	}

	public void setChejmc(String chejmc) {
		this.chejmc = chejmc;
	}

	public String getWuly() {
		return wuly;
	}

	public void setWuly(String wuly) {
		this.wuly = wuly;
	}

	public String getWlgyy() {
		return wlgyy;
	}

	public void setWlgyy(String wlgyy) {
		this.wlgyy = wlgyy;
	}

	public String getZhijy() {
		return zhijy;
	}

	public void setZhijy(String zhijy) {
		this.zhijy = zhijy;
	}

	public String getPcjhy() {
		return pcjhy;
	}

	public void setPcjhy(String pcjhy) {
		this.pcjhy = pcjhy;
	}

	public String getBiaos() {
		return biaos;
	}

	public void setBiaos(String biaos) {
		this.biaos = biaos;
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

	public String getLingjsx() {
		return lingjsx;
	}

	public void setLingjsx(String lingjsx) {
		this.lingjsx = lingjsx;
	}

	public String getJihy() {
		return jihy;
	}

	public void setJihy(String jihy) {
		this.jihy = jihy;
	}

	public String getDanw() {
		return danw;
	}

	public void setDanw(String danw) {
		this.danw = danw;
	}

	public String getZhizlx() {
		return zhizlx;
	}

	public void setZhizlx(String zhizlx) {
		this.zhizlx = zhizlx;
	}

	public String getKaisrq() {
		return kaisrq;
	}

	public void setKaisrq(String kaisrq) {
		this.kaisrq = kaisrq;
	}

	public String getJiesrq() {
		return jiesrq;
	}

	public void setJiesrq(String jiesrq) {
		this.jiesrq = jiesrq;
	}

	public Double getZhuangcxs() {
		return zhuangcxs;
	}

	public void setZhuangcxs(Double zhuangcxs) {
		this.zhuangcxs = zhuangcxs;
	}

	public String getAnqm() {
		return anqm;
	}

	public void setAnqm(String anqm) {
		this.anqm = anqm;
	}

	public Double getLingjzl() {
		return lingjzl;
	}

	public void setLingjzl(Double lingjzl) {
		this.lingjzl = lingjzl;
	}

	public String getGongybm() {
		return gongybm;
	}

	public void setGongybm(String gongybm) {
		this.gongybm = gongybm;
	}

	public String getZongcldm() {
		return zongcldm;
	}

	public void setZongcldm(String zongcldm) {
		this.zongcldm = zongcldm;
	}

	public String getDiycqysj() {
		return diycqysj;
	}

	public void setDiycqysj(String diycqysj) {
		this.diycqysj = diycqysj;
	}

	public String getUsercenter() {
		return usercenter;
	}

	public String getCreator() {
		return creator;
	}

	public String getCreate_time() {
		return create_time;
	}

	public String getEditor() {
		return editor;
	}

	public String getEdit_time() {
		return edit_time;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public void setEdit_time(String edit_time) {
		this.edit_time = edit_time;
	}

	public String getLingjbh() {
		return lingjbh;
	}

	public String getLingjlx() {
		return lingjlx;
	}

	public String getGuanjljjb() {
		return guanjljjb;
	}

	public String getDinghcj() {
		return dinghcj;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}

	public void setLingjlx(String lingjlx) {
		this.lingjlx = lingjlx;
	}

	public void setGuanjljjb(String guanjljjb) {
		this.guanjljjb = guanjljjb;
	}

	public void setDinghcj(String dinghcj) {
		this.dinghcj = dinghcj;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getRole() {
		return role;
	}

	@Override
	public void setId(String id) {
		
	}

	@Override
	public String getId() {
		return null;
	}

	public String getAnjmlxhd() {
		return anjmlxhd;
	}

	public void setAnjmlxhd(String anjmlxhd) {
		this.anjmlxhd = anjmlxhd;
	}

}
