package com.athena.ckx.entity.cangk;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 库位
 * @author denggq
 * @date 2012-2-8
 */
public class Kuw extends PageableSupport implements Domain{

	private static final long serialVersionUID = 8160483530215429946L;

	private String usercenter;	//用户中心
	
	private String cangkbh;		//仓库编号
	
	private String zickbh;		//子仓库编号
	
	private String kuwbh;		//库位编号
	
	private String kuwdjbh;		//库位等级编号
	
	private String kuwzt;	//库位转头
	
	private String yifgs;	//已放个数
	
	private String baozlx;	//包装类型
	
	private String baozgs;	//包装个数
	
	private String creator;		//创建人
	
	private String create_time;	//创建时间
	
	private String editor;		//修改人
	
	private String edit_time;	//修改时间
	
	private String mian;		//临时变量：面
	
	private String ceng;		//临时变量：层
	
	private String qislh;		//临时变量：起始列号
	
	private String jieslh;		//临时变量：结束列号
	
	private String lieh;		//临时变量：列号
	
	private String jiangl;		//间隔列
	
	private String shifky;		//是否可用
	
	private String dingybzgs;   //定义包装个数
	
	private String dingykwgs;    //定义库位个数
	
	private int kuwdjbhgs;       //库位等级编号个数
	
	private String ykuwdjbh;       //原库位等级编号
	
	private String lingjbh;           //定置零件
	
	private String dingzkw;
	
	private String kuwdjbhtd;		//库位等级编号替代
	
	private String kuwzttd;	//库位转头替代
	 
	
	
	public String getKuwdjbhtd() {
		return kuwdjbhtd;
	}

	public void setKuwdjbhtd(String kuwdjbhtd) {
		this.kuwdjbhtd = kuwdjbhtd;
	}

	public String getKuwzttd() {
		return kuwzttd;
	}

	public void setKuwzttd(String kuwzttd) {
		this.kuwzttd = kuwzttd;
	}

	public String getDingzkw() {
		return dingzkw;
	}

	public void setDingzkw(String dingzkw) {
		this.dingzkw = dingzkw;
	}

	public String getLingjbh() {
		return lingjbh;
	}

	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}

	public String getYkuwdjbh() {
		return ykuwdjbh;
	}

	public void setYkuwdjbh(String ykuwdjbh) {
		this.ykuwdjbh = ykuwdjbh;
	}

	public int getKuwdjbhgs() {
		return kuwdjbhgs;
	}

	public void setKuwdjbhgs(int kuwdjbhgs) {
		this.kuwdjbhgs = kuwdjbhgs;
	}

	public String getDingybzgs() {
		return dingybzgs;
	}

	public void setDingybzgs(String dingybzgs) {
		this.dingybzgs = dingybzgs;
	}

	public String getDingykwgs() {
		return dingykwgs;
	}

	public void setDingykwgs(String dingykwgs) {
		this.dingykwgs = dingykwgs;
	}

	public String getShifky() {
		return shifky;
	}

	public void setShifky(String shifky) {
		this.shifky = shifky;
	}

	public String getZickbh() {
		return zickbh;
	}

	public void setZickbh(String zickbh) {
		this.zickbh = zickbh;
	}

	public String getKuwbh() {
		return kuwbh;
	}

	public void setKuwbh(String kuwbh) {
		this.kuwbh = kuwbh;
	}

	public String getKuwdjbh() {
		return kuwdjbh;
	}

	public void setKuwdjbh(String kuwdjbh) {
		this.kuwdjbh = kuwdjbh;
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

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getCangkbh() {
		return cangkbh;
	}

	public void setCangkbh(String cangkbh) {
		this.cangkbh = cangkbh;
	}

	public String getMian() {
		return mian;
	}

	public void setMian(String mian) {
		this.mian = mian;
	}

	public String getCeng() {
		return ceng;
	}

	public void setCeng(String ceng) {
		this.ceng = ceng;
	}

	public String getQislh() {
		return qislh;
	}

	public void setQislh(String qislh) {
		this.qislh = qislh;
	}

	public String getJieslh() {
		return jieslh;
	}

	public String getKuwzt() {
		return kuwzt;
	}

	public String getYifgs() {
		return yifgs;
	}

	public String getBaozlx() {
		return baozlx;
	}

	public String getBaozgs() {
		return baozgs;
	}

	public void setKuwzt(String kuwzt) {
		this.kuwzt = kuwzt;
	}

	public void setYifgs(String yifgs) {
		this.yifgs = yifgs;
	}

	public void setBaozlx(String baozlx) {
		this.baozlx = baozlx;
	}

	public void setBaozgs(String baozgs) {
		this.baozgs = baozgs;
	}

	public void setJieslh(String jieslh) {
		this.jieslh = jieslh;
	}

	public void setLieh(String lieh) {
		this.lieh = lieh;
	}

	public String getLieh() {
		return lieh;
	}

	public void setJiangl(String jiangl) {
		this.jiangl = jiangl;
	}

	public String getJiangl() {
		return jiangl;
	}

	public void setId(String id) {
		
	}

	public String getId() {
		return null;
	}


}
