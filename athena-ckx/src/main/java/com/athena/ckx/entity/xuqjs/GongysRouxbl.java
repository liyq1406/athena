package com.athena.ckx.entity.xuqjs;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 供应商-柔性比例
 * @author qizhogntao
 * 2012-4-9
 */
public class GongysRouxbl extends PageableSupport implements Domain{

	private static final long serialVersionUID = 6760859243199263715L;

	private String usercenter;    //用户中心
	
	private String gongysbh;      //供应商编号
	
	private String guanjljjb;     //关键零件级别
	
	private String dinghzq;       //订货周期
	
	private Double rouxbl;        //柔性比例
	
	private String creator;       //创建人
	
	private String create_time;   //创建时间
	
	private String editor;        //修改人
	
	private String edit_time;     //修改时间
	
	
	
	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getGongysbh() {
		return gongysbh;
	}

	public void setGongysbh(String gongysbh) {
		this.gongysbh = gongysbh;
	}

	public String getGuanjljjb() {
		return guanjljjb;
	}

	public void setGuanjljjb(String guanjljjb) {
		this.guanjljjb = guanjljjb;
	}

	public String getDinghzq() {
		return dinghzq;
	}

	public void setDinghzq(String dinghzq) {
		this.dinghzq = dinghzq;
	}

	public Double getRouxbl() {
		return rouxbl;
	}

	public void setRouxbl(Double rouxbl) {
		this.rouxbl = rouxbl;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public void setId(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

}
