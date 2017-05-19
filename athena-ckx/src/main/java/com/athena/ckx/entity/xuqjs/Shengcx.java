package com.athena.ckx.entity.xuqjs;

import com.athena.component.entity.Domain;

import com.toft.core3.support.PageableSupport;
/**
 * 生产线
 * @author denggq
 * @String 2012-3-21
 */
public class Shengcx extends PageableSupport implements Domain{

	private static final long serialVersionUID = 462812396261385771L;

	private String usercenter   ;//  用户中心 
	
	private String shengcxbh    ;//  生产线编号
	
	private String qiehsj       ;//  切换时间
	
	private String shengcjp     ;//  整车生产节拍
	
	private String weilscjp     ;//  整车未来生产节拍
	
	private String chults		;//	  排产计划处理天数
	
	private String flag;		//成品非成品生产线
	
	private Double anqkctsmrz	;//安全库存天数默认值
	
	private String biaos        ;//  标识

	private String creator;       //创建人
	
	private String create_time;   //创建时间
	
	private String editor;        //修改人
	
	private String edit_time;      //修改时间

	private String cpshengcjp   ;//  成品生产节拍
	
	
	private String chews;        //车位数
	
	private String kongcss;     //空车身数
	
	private String chanxlx;		//产线类型
	
	private String duiydx;		//对应大线
	
	
	public String getChews() {
		return chews;
	}


	public void setChews(String chews) {
		this.chews = chews;
	}


	public String getKongcss() {
		return kongcss;
	}


	public void setKongcss(String kongcss) {
		this.kongcss = kongcss;
	}


	public String getCpshengcjp() {
		return cpshengcjp;
	}


	public void setCpshengcjp(String cpshengcjp) {
		this.cpshengcjp = cpshengcjp;
	}


	public String getUsercenter() {
		return usercenter;
	}


	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}


	public String getShengcxbh() {
		return shengcxbh;
	}


	public void setShengcxbh(String shengcxbh) {
		this.shengcxbh = shengcxbh;
	}


	public String getQiehsj() {
		return qiehsj;
	}


	public void setQiehsj(String qiehsj) {
		this.qiehsj = qiehsj;
	}


	public String getShengcjp() {
		return shengcjp;
	}


	public void setShengcjp(String shengcjp) {
		this.shengcjp = shengcjp;
	}


	public String getWeilscjp() {
		return weilscjp;
	}


	public void setWeilscjp(String weilscjp) {
		this.weilscjp = weilscjp;
	}


	public String getBiaos() {
		return biaos;
	}


	public void setBiaos(String biaos) {
		this.biaos = biaos;
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


	public void setChults(String chults) {
		this.chults = chults;
	}


	public String getChults() {
		return chults;
	}


	public void setAnqkctsmrz(Double anqkctsmrz) {
		this.anqkctsmrz = anqkctsmrz;
	}


	public Double getAnqkctsmrz() {
		return anqkctsmrz;
	}


	public void setFlag(String flag) {
		this.flag = flag;
	}


	public String getFlag() {
		return flag;
	}


	public String getChanxlx() {
		return chanxlx;
	}


	public void setChanxlx(String chanxlx) {
		this.chanxlx = chanxlx;
	}


	public String getDuiydx() {
		return duiydx;
	}


	public void setDuiydx(String duiydx) {
		this.duiydx = duiydx;
	}


	@Override
	public void setId(String id) {
		
	}


	@Override
	public String getId() {
		return null;
	}
	

	
	
	
}
