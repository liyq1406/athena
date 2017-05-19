package com.athena.ckx.entity.carry;

import java.util.Date;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 仓库循环时间
 * @author kong
 *
 */
public class CkxMsqhb extends PageableSupport  implements Domain{

	private static final long serialVersionUID = -7806263175923774742L;
	private String liush;                     //流水号（id）
	private String usercenter;               //用户中心
	private String xunhbm;                  //循环编码
	private String yuanxhbm;               //原循环编码
	private String lingjbh;               //零件编号
	private String cangkbh;              //仓库编号
	private String xiaohd;              //消耗点
	private String muddlx;             //目的地类型
	private String mos;               //模式
	private String yuanms;           //原模式
	private String shifkbqh;        //是否看板切换(0|其他切看板，1|看板切看板，2|看板切其他)
	private String zhuangt;        //切换状态
	private String creator;       //创建人
	private Date create_time;     //创建时间
	private String editor;		//修改人
	private Date edit_time;    //修改时间
	
	
	
	
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	public String getXunhbm() {
		return xunhbm;
	}
	public void setXunhbm(String xunhbm) {
		this.xunhbm = xunhbm;
	}
	public String getYuanxhbm() {
		return yuanxhbm;
	}
	public void setYuanxhbm(String yuanxhbm) {
		this.yuanxhbm = yuanxhbm;
	}
	public String getLingjbh() {
		return lingjbh;
	}
	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}
	public String getCangkbh() {
		return cangkbh;
	}
	public void setCangkbh(String cangkbh) {
		this.cangkbh = cangkbh;
	}
	public String getXiaohd() {
		return xiaohd;
	}
	public void setXiaohd(String xiaohd) {
		this.xiaohd = xiaohd;
	}
	public String getMuddlx() {
		return muddlx;
	}
	public void setMuddlx(String muddlx) {
		this.muddlx = muddlx;
	}
	public String getMos() {
		return mos;
	}
	public void setMos(String mos) {
		this.mos = mos;
		this.zhuangt = "0";
		this.shifkbqh = "0";
		if(this.mos.contains("R")&& this.yuanms.contains("R")){
			this.shifkbqh = "1";
		}else if(this.yuanms.startsWith("R")&&!this.mos.contains("R")){
			this.shifkbqh = "2";
		}
	}
	public String getYuanms() {
		return yuanms;
	}
	public void setYuanms(String yuanms) {
		this.yuanms = yuanms;
	}
	public String getShifkbqh() {
		return shifkbqh;
	}
	public void setShifkbqh(String shifkbqh) {
		this.shifkbqh = shifkbqh;
	}
	public String getZhuangt() {
		return zhuangt;
	}
	public void setZhuangt(String zhuangt) {
		this.zhuangt = zhuangt;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	public String getEditor() {
		return editor;
	}
	public void setEditor(String editor) {
		this.editor = editor;
	}
	public Date getEdit_time() {
		return edit_time;
	}
	public void setEdit_time(Date edit_time) {
		this.edit_time = edit_time;
	}
	public void setId(String id) {
		
		
	}
	public String getId() {
		
		return "";
	}
	public String getLiush() {
		return liush;
	}
	public void setLiush(String liush) {
		this.liush = liush;
	}
	
}
