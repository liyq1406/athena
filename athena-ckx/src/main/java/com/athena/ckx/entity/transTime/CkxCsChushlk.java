package com.athena.ckx.entity.transTime;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 
 * @author xss
 * @date 2012-4-10
 */
public class CkxCsChushlk extends PageableSupport implements Domain{

	private static final long serialVersionUID = 2892190665367165024L;

	private String usercenter;	//用户中心
	
	private String shengcxbh;	//生产线编号
	
	private String xiaohcbh;	//小火车编号
	
	private String xiaohcmc;	//小火车名称
	
	private String biaos;		//标识
	
	private String creator;		//增加人
	
	private String create_time;	//增加时间
	
	private String editor;		//修改人
	
	private String edit_time;	//修改时间
	
	private String chanx;	//产线
	
	private String pianycws;    //偏移车位数
	
	private String xunhcss;     //循环车身数
	 
	private String beihtqcss;    //备货提前车身数
	
	private String shangxtqcss;   //上线提前车身数
	
	private String jianglcssxr;   //将来参数生效日期
	
	private String jianglpycws;   //将来偏移车位数
	
	private String jianglxhcss;   //将来循环车身数
	
	private String jianglbhtqcss;  //将来备货提前车身数
	
	private String jianglsxtqcss;   //将来上线提前车身数
	
	
	private String dangqtc;        //当前趟次
	
	private String jieslsh;        //结束流水号
	
	
	
	public String getChanx() {
		return chanx;
	}

	public void setChanx(String chanx) {
		this.chanx = chanx;
	}

	
	public String getDangqtc() {
		return dangqtc;
	}

	public void setDangqtc(String dangqtc) {
		this.dangqtc = dangqtc;
	}

	public String getJieslsh() {
		return jieslsh;
	}

	public void setJieslsh(String jieslsh) {
		this.jieslsh = jieslsh;
	}

	public String getJianglcssxr() {
		return jianglcssxr;
	}

	public void setJianglcssxr(String jianglcssxr) {
		this.jianglcssxr = jianglcssxr;
	}

	public String getJianglpycws() {
		return jianglpycws;
	}

	public void setJianglpycws(String jianglpycws) {
		this.jianglpycws = jianglpycws;
	}

	public String getJianglxhcss() {
		return jianglxhcss;
	}

	public void setJianglxhcss(String jianglxhcss) {
		this.jianglxhcss = jianglxhcss;
	}

	public String getJianglbhtqcss() {
		return jianglbhtqcss;
	}

	public void setJianglbhtqcss(String jianglbhtqcss) {
		this.jianglbhtqcss = jianglbhtqcss;
	}

	public String getJianglsxtqcss() {
		return jianglsxtqcss;
	}

	public void setJianglsxtqcss(String jianglsxtqcss) {
		this.jianglsxtqcss = jianglsxtqcss;
	}

	public String getPianycws() {
		return pianycws;
	}

	public void setPianycws(String pianycws) {
		this.pianycws = pianycws;
	}

	public String getXunhcss() {
		return xunhcss;
	}

	public void setXunhcss(String xunhcss) {
		this.xunhcss = xunhcss;
	}

	public String getBeihtqcss() {
		return beihtqcss;
	}

	public void setBeihtqcss(String beihtqcss) {
		this.beihtqcss = beihtqcss;
	}

	public String getShangxtqcss() {
		return shangxtqcss;
	}

	public void setShangxtqcss(String shangxtqcss) {
		this.shangxtqcss = shangxtqcss;
	}

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getBiaos() {
		return biaos;
	}

	public String getShengcxbh() {
		return shengcxbh;
	}

	public String getXiaohcbh() {
		return xiaohcbh;
	}

	public String getXiaohcmc() {
		return xiaohcmc;
	}

	public void setShengcxbh(String shengcxbh) {
		this.shengcxbh = shengcxbh;
	}

	public void setXiaohcbh(String xiaohcbh) {
		this.xiaohcbh = xiaohcbh;
	}

	public void setXiaohcmc(String xiaohcmc) {
		this.xiaohcmc = xiaohcmc;
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

	public void setId(String id) {
		
	}

	public String getId() {
		return null;
	}

}
