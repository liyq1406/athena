package com.athena.pc.entity;

import com.athena.component.entity.Domain;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.support.PageableSupport;

@Component
public class Yuemnjh extends PageableSupport implements Domain {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1741642641375032158L;
	
	private String yuemnjhh;      //月模拟计划号
	private String usercenter;    //用户中心
	private String chanxh;        //产线号
	private String kaissj;        //开始时间
	private String jiessj;        //结束时间
	private String shifqr;        //是否确认
	private String creator;       //P_修改人
	private String create_time;   //P_修改时间
	private String editor;        //P_创建人
	private String edit_time;     //P_创建时间

	private String shij;     //时间
	private String lingjbh;  //零件编号
	private String lingjsl;  //零件数量
	private String shijscsl; //实际生产数量
	private String dantce;   //当天差额
	private String ywhour;   //延误工时
	private String leijce;   //累计差额

	public String getYuemnjhh() {
		return yuemnjhh;
	}

	public void setYuemnjhh(String yuemnjhh) {
		this.yuemnjhh = yuemnjhh;
	}

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getChanxh() {
		return chanxh;
	}

	public void setChanxh(String chanxh) {
		this.chanxh = chanxh;
	}

	public String getKaissj() {
		return kaissj;
	}

	public void setKaissj(String kaissj) {
		this.kaissj = kaissj;
	}

	public String getJiessj() {
		return jiessj;
	}

	public void setJiessj(String jiessj) {
		this.jiessj = jiessj;
	}

	public String getShifqr() {
		return shifqr;
	}

	public void setShifqr(String shifqr) {
		this.shifqr = shifqr;
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

	public String getShij() {
		return shij;
	}

	public void setShij(String shij) {
		this.shij = shij;
	}

	public String getLingjbh() {
		return lingjbh;
	}

	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}

	public String getLingjsl() {
		return lingjsl;
	}

	public void setLingjsl(String lingjsl) {
		this.lingjsl = lingjsl;
	}

	public String getShijscsl() {
		return shijscsl;
	}

	public void setShijscsl(String shijscsl) {
		this.shijscsl = shijscsl;
	}

	public String getDantce() {
		return dantce;
	}

	public void setDantce(String dantce) {
		this.dantce = dantce;
	}

	public String getYwhour() {
		return ywhour;
	}

	public void setYwhour(String ywhour) {
		this.ywhour = ywhour;
	}

	public String getLeijce() {
		return leijce;
	}

	public void setLeijce(String leijce) {
		this.leijce = leijce;
	}

	public void setId(String id) {
		
	}

	public String getId() {
		return null;
	}

}
