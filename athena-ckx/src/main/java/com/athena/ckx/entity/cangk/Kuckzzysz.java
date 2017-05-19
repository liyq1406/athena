package com.athena.ckx.entity.cangk;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 库存快照资源设置
 * @author 王宇
 * @date 2013-7-30
 */
public class Kuckzzysz extends PageableSupport implements Domain{

	private static final long serialVersionUID = 2892190665367165024L;

	private String usercenter;	//用户中心
	
	private String baozzt;		//包装状态
	
	private String zhuangtsx;		//状态属性
	
	private String shengxbs;	//生效标识
	
	private String beiz1;		//备注1
	
	private String beiz2;		//备注2
	
	private int beiz3;		//备注3
	
	private int beiz4;     //备注4
	
	private String baozz1;

	private String zhuangts1;	
	
	private String shengxb1;
	

	public String getBaozz1() {
		return baozz1;
	}

	public void setBaozz1(String baozz1) {
		this.baozz1 = baozz1;
	}

	public String getZhuangts1() {
		return zhuangts1;
	}

	public void setZhuangts1(String zhuangts1) {
		this.zhuangts1 = zhuangts1;
	}

	public String getShengxb1() {
		return shengxb1;
	}

	public void setShengxb1(String shengxb1) {
		this.shengxb1 = shengxb1;
	}

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getBaozzt() {
		return baozzt;
	}

	public void setBaozzt(String baozzt) {
		this.baozzt = baozzt;
	}

	public String getZhuangtsx() {
		return zhuangtsx;
	}

	public void setZhuangtsx(String zhuangtsx) {
		this.zhuangtsx = zhuangtsx;
	}

	public String getShengxbs() {
		return shengxbs;
	}

	public void setShengxbs(String shengxbs) {
		this.shengxbs = shengxbs;
	}

	public String getBeiz1() {
		return beiz1;
	}

	public void setBeiz1(String beiz1) {
		this.beiz1 = beiz1;
	}

	public String getBeiz2() {
		return beiz2;
	}

	public void setBeiz2(String beiz2) {
		this.beiz2 = beiz2;
	}

	public int getBeiz3() {
		return beiz3;
	}

	public void setBeiz3(int beiz3) {
		this.beiz3 = beiz3;
	}

	public int getBeiz4() {
		return beiz4;
	}

	public void setBeiz4(int beiz4) {
		this.beiz4 = beiz4;
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
