package com.athena.ckx.entity.xuqjs;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;


/**
 * 配送类别
 * @author qizhongtao
 * 2012-4-10
 */
public class Peislb extends PageableSupport implements Domain {

	private static final long serialVersionUID = -687115448563417886L;
	
	private String usercenter;    //用户中心

	private String peislx;        //配送类型
	
	private String peislxmc;      //配送类型名称
	
	private String baozlx;        //包装类型
	
	private Integer baozsl;        //包装零件数量/流水号数量
	
	private Integer zuicddcws;     //最长等待车位数
	
	private String tongbjpbs;     //配送标识
	
	private String shangxd;       //上线点
	
	private String peitsxbs;      //配套上线标识
	
	private Double beihtqq;       //备货提前期（天）
	
	private String xiaohccxc;     //小货车车厢层
	
	private String shifgj;        //是否归集(按零件)
	
	private String beiz;          //备注(左/右/车型等)
	
	private String shifbhd;       //是否生成备货单
	
	private String cangkbh;		  //仓库编号
	
	private String zickbh;		  //子仓库编号
	
	private String wlgyy;		  //物流工艺员
	
	private String biaos;         //标识
	
	private String creator;       //创建人
	
	private String create_time;   //创建时间
	
	private String editor;        //修改人
	
	private String edit_time;     //修改时间
	
	private String role;	//角色
	
	public String getPeislx() {
		return peislx;
	}
	
	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public void setPeislx(String peislx) {
		this.peislx = peislx;
	}

	public String getPeislxmc() {
		return peislxmc;
	}

	public void setPeislxmc(String peislxmc) {
		this.peislxmc = peislxmc;
	}

	public String getBaozlx() {
		return baozlx;
	}

	public void setBaozlx(String baozlx) {
		this.baozlx = baozlx;
	}

	public Integer getBaozsl() {
		return baozsl;
	}

	public void setBaozsl(Integer baozsl) {
		this.baozsl = baozsl;
	}

	public Integer getZuicddcws() {
		return zuicddcws;
	}

	public void setZuicddcws(Integer zuicddcws) {
		this.zuicddcws = zuicddcws;
	}

	public String getTongbjpbs() {
		return tongbjpbs;
	}

	public void setTongbjpbs(String tongbjpbs) {
		this.tongbjpbs = tongbjpbs;
	}

	public String getShangxd() {
		return shangxd;
	}

	public void setShangxd(String shangxd) {
		this.shangxd = shangxd;
	}

	public String getPeitsxbs() {
		return peitsxbs;
	}

	public void setPeitsxbs(String peitsxbs) {
		this.peitsxbs = peitsxbs;
	}

	public Double getBeihtqq() {
		return beihtqq;
	}

	public void setBeihtqq(Double beihtqq) {
		this.beihtqq = beihtqq;
	}

	public String getXiaohccxc() {
		return xiaohccxc;
	}

	public void setXiaohccxc(String xiaohccxc) {
		this.xiaohccxc = xiaohccxc;
	}

	public String getShifgj() {
		return shifgj;
	}

	public void setShifgj(String shifgj) {
		this.shifgj = shifgj;
	}

	public String getBeiz() {
		return beiz;
	}

	public void setBeiz(String beiz) {
		this.beiz = beiz;
	}

	public String getCangkbh() {
		return cangkbh;
	}

	public void setCangkbh(String cangkbh) {
		this.cangkbh = cangkbh;
	}

	public void setZickbh(String zickbh) {
		this.zickbh = zickbh;
	}

	public String getZickbh() {
		return zickbh;
	}

	public String getShifbhd() {
		return shifbhd;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void setShifbhd(String shifbhd) {
		this.shifbhd = shifbhd;
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

	public void setWlgyy(String wlgyy) {
		this.wlgyy = wlgyy;
	}

	public String getWlgyy() {
		return wlgyy;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String getBiaos() {
		return biaos;
	}

	public void setBiaos(String biaos) {
		this.biaos = biaos;
	}

	@Override
	public void setId(String id) {
		
	}

	@Override
	public String getId() {
		return null;
	}

}
