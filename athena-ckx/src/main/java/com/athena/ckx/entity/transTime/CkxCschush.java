package com.athena.ckx.entity.transTime;

import java.util.Date;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * CS初始化
 * @author xss
 * @Date 2014-09-16
 */
public class CkxCschush extends PageableSupport  implements Domain{
	

	private static final long serialVersionUID = -6790607714902341609L;
	private String usercenter;//用户中心
	private String chanx;//产线	
	private String xiaohd;//消耗点
	private String pandsj;//盘点时间
	private String lingjbh;     //零件编号
	private String pandsl;   //盘点数量
	private String pandr;    //创建人
	private Date create_time;    //创建时间
	private String zhengclsh;//修改人
	private String zhuangt;//状态
	
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}
	public String getchanx() {
		return chanx;
	}
	public void setchanx(String chanx) {
		this.chanx = chanx;
	}
	public String getxiaohd() {
		return xiaohd;
	}
	public void setxiaohd(String xiaohd) {
		this.xiaohd = xiaohd;
	}
	public String getpandsj() {
		return pandsj;
	}
	public void setpandsj(String pandsj) {
		this.pandsj = pandsj;
	}
	public String getlingjbh() {
		return lingjbh;
	}
	public void setlingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}
	
	public String getpandsl() {
		return pandsl;
	}
	public void setpandsl(String pandsl) {
		this.pandsl = pandsl;
	}
	public String getpandr() {
		return pandr;
	}
	public void setpandr(String pandr) {
		this.pandr = pandr;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public String getzhengclsh() {
		return zhengclsh;
	}
	public void setzhengclsh(String zhengclsh) {
		this.zhengclsh = zhengclsh;
	}

	public void setId(String id) {
		// TODO Auto-generated method stub
		
	}

	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
