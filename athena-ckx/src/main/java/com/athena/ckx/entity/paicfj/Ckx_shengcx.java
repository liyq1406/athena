package com.athena.ckx.entity.paicfj;

import java.util.Date;

import com.athena.component.entity.Domain;

import com.toft.core3.support.PageableSupport;
/**
 * 生产线
 * @author hj
 * @date 2011-12-30
 */
public class Ckx_shengcx extends PageableSupport implements Domain{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public void setId(String id) {
	}


	public String getId() {
		return null;
	}
	
	private String usercenter   ;//  用户中心 
	private String shengcxbh    ;//  生产线编号
	private String chanxzbh     ;//  产线组编号
	private String qiehsj       ;//  切换时间
	private String shengcjp     ;//  整车生产节拍
	private String weilscjp     ;//  整车未来生产节拍
	private String cpshengcjp   ;//  成品生产节拍
//	private String tessjxq1     ;//  特殊时间1星期
//	private Double tessjxs1     ;//  特殊时间1小时
//	private String tessjxq2     ;//  特殊时间2星期
//	private Double tessjxs2     ;//  特殊时间2小时
//	private String tessjxq3     ;//  特殊时间3星期
//	private Double tessjxs3     ;//  特殊时间3小时
	private String biaos        ;//  标识
	

	private String creator;       //创建人
	private Date   create_time;   //创建时间
	private String editor;        //修改人
	private Date  edit_time;      //修改时间

	
	
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


	public String getChanxzbh() {
		return chanxzbh;
	}


	public void setChanxzbh(String chanxzbh) {
		this.chanxzbh = chanxzbh;
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


//	public String getTessjxq1() {
//		return tessjxq1;
//	}
//
//
//	public void setTessjxq1(String tessjxq1) {
//		this.tessjxq1 = tessjxq1;
//	}
//
//
//	public Double getTessjxs1() {
//		return tessjxs1;
//	}
//
//
//	public void setTessjxs1(Double tessjxs1) {
//		this.tessjxs1 = tessjxs1;
//	}
//
//
//	public String getTessjxq2() {
//		return tessjxq2;
//	}
//
//
//	public void setTessjxq2(String tessjxq2) {
//		this.tessjxq2 = tessjxq2;
//	}
//
//
//	public Double getTessjxs2() {
//		return tessjxs2;
//	}
//
//
//	public void setTessjxs2(Double tessjxs2) {
//		this.tessjxs2 = tessjxs2;
//	}
//
//
//	public String getTessjxq3() {
//		return tessjxq3;
//	}
//
//
//	public void setTessjxq3(String tessjxq3) {
//		this.tessjxq3 = tessjxq3;
//	}
//
//
//	public Double getTessjxs3() {
//		return tessjxs3;
//	}
//
//
//	public void setTessjxs3(Double tessjxs3) {
//		this.tessjxs3 = tessjxs3;
//	}


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
	

	
	
	
}
