package com.athena.ckx.entity.transTime;

import java.util.Date;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 运输时间模板生效
 * @author gswang
 * @Date 2012-03-19
 */
public class CkxYunssjMBSx extends PageableSupport  implements Domain{
	

	private static final long serialVersionUID = -6791607714902341609L;
	private String usercenter;//用户中心
	private String xiehztbzh;//卸货站台编组号
	private String shengxsj;//生效时间
	private String creator;    //创建人
	private Date create_time;    //创建时间
	private String editor;//修改人
	private Date edit_time;//修改时间
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getXiehztbzh() {
		return xiehztbzh;
	}
	public void setXiehztbzh(String xiehztbzh) {
		this.xiehztbzh = xiehztbzh;
	}
	public String getShengxsj() {
		return shengxsj;
	}
	public void setShengxsj(String shengxsj) {
		this.shengxsj = shengxsj;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
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
		// TODO Auto-generated method stub
		
	}

	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
