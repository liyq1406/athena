package com.athena.ckx.entity.cangk;

/**
 * 客户操作码
 * @author denggq
 * @date 2012-1-30
 */
import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

public class Kehczm extends PageableSupport implements Domain{

	private static final long serialVersionUID = -3734097726110257393L;

	private String usercenter;	//用户中心
	
	private String kehbh;		//客户编号
	
	private String kehlx;       //客户类型
	
	private String zhangh;		//账户
	
	private String caozm;		//操作码
	
	private String biaos;		//标识
	
	private String creator;		//增加人
	
	private String create_time;	//增加时间
	
	private String editor;		//修改人
	
	private String edit_time;	//修改时间

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getKehlx() {
		return kehlx;
	}

	public void setKehlx(String kehlx) {
		this.kehlx = kehlx;
	}

	public String getKehbh() {
		return kehbh;
	}

	public void setKehbh(String kehbh) {
		this.kehbh = kehbh;
	}

	public String getZhangh() {
		return zhangh;
	}

	public void setZhangh(String zhangh) {
		this.zhangh = zhangh;
	}

	public String getCaozm() {
		return caozm;
	}

	public void setCaozm(String caozm) {
		this.caozm = caozm;
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

	public void setId(String id) {
		
	}

	public String getId() {
		return null;
	}
	
}
