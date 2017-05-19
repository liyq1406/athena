package com.athena.xqjs.entity.common;

import com.toft.core3.support.PageableSupport;
/**
 * <p>
 * Title:产线/仓库 -日历版次-工作时间编组实体类
 * </p>
 * <p>
 * Description:产线/仓库 -日历版次-工作时间编组实体类
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company: 软通动力
 * </p>
 * 
 * @author 李明
 * @version v1.0
 * @date 2011-12-13
 */
public class CalendarGroup extends PageableSupport{
	
	private static final long serialVersionUID = -6416990806688863683L;
	private String  usercenter	;		
	private String appobj ;
	private String bianzh ;
	private String rilbc ;
	private String beiz ;	
	private String biaos	;	
	private String weilbzh ;			
	private String shengxsj ;
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	public String getAppobj() {
		return appobj;
	}
	public void setAppobj(String appobj) {
		this.appobj = appobj;
	}
	public String getBianzh() {
		return bianzh;
	}
	public void setBianzh(String bianzh) {
		this.bianzh = bianzh;
	}
	public String getRilbc() {
		return rilbc;
	}
	
	public void setRilbc(String rilbc) {
		this.rilbc = rilbc;
	}
	public String getBeiz() {
		return beiz;
	}
	
	public void setBeiz(String beiz) {
		this.beiz = beiz;
	}
	
	public String getBiaos() {
		return biaos;
	}
	
	public void setBiaos(String biaos) {
		this.biaos = biaos;
	}
	
	public String getWeilbzh() {
		return weilbzh;
	}
	
	public void setWeilbzh(String weilbzh) {
		this.weilbzh = weilbzh;
	}
	
	public String getShengxsj() {
		return shengxsj;
	}
	public void setShengxsj(String shengxsj) {
		this.shengxsj = shengxsj;
	}	
	
	

}
