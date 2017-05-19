package com.athena.ckx.entity.carry;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;


/**
 * 内部物流路径与外部模式组成的实体类
 * @author kong 
 *
 */
public class CkxInnerPathAndModle extends PageableSupport  implements Domain{

	
	private static final long serialVersionUID = 2322668848959328448L;
	
	private String usercenter;//用户中心
	private String lingjbh;//零件编号
	private String shengcxbh;//生产线编号
	private String fenpqh;//分配区
	private String mos;//模式
	private String jianglms;//将来模式
	private String jianglmssxsj;//将来模式生效时间
	private String qidlx;//起点类型
	private String xianbk;//线边库
	private String xianbk1;//线边库临时变量
	private String mos2;//模式
	private String jianglms2;//将来模式
	private String jianglmssxsj2;//将来模式生效时间
	private String qidlx2;//起点类型
	private String dinghk;//订货库
	private String wms;//外部模式
	private String mudd;//外部模式
	private String zhidgys;
	private String wjlms;//外部将来模式
	private String shengxsj;
	private String wulgyyz;//物流工艺员组，查询数据权限（目的地按仓库查询权限）
	private String creator;//创建人  add by lc 2016.10.26
	private String createTime;//创建时间  add by lc 2016.10.26
	private String editor;//修改人  add by lc 2016.10.26
	private String editTime;//修改人  add by lc 2016.10.26
	private String uclist;//用户组对应的有权限的用户中心   add by lc 2017.2.17
	
	public String getMudd() {
		return mudd;
	}

	public void setMudd(String mudd) {
		this.mudd = mudd;
	}

	public String getXianbk1() {
		return xianbk1;
	}

	public void setXianbk1(String xianbk1) {
		this.xianbk1 = xianbk1;
	}

	public String getZhidgys() {
		return zhidgys;
	}

	public void setZhidgys(String zhidgys) {
		this.zhidgys = zhidgys;
	}

	public String getWjlms() {
		return wjlms;
	}

	public void setWjlms(String wjlms) {
		this.wjlms = wjlms;
	}

	public String getShengxsj() {
		return shengxsj;
	}

	public void setShengxsj(String shengxsj) {
		this.shengxsj = shengxsj;
	}

	public String getQidlx() {
		return qidlx;
	}

	public void setQidlx(String qidlx) {
		this.qidlx = qidlx;
	}

	public String getQidlx2() {
		return qidlx2;
	}

	public void setQidlx2(String qidlx2) {
		this.qidlx2 = qidlx2;
	}

	public String getShengcxbh() {
		return shengcxbh;
	}

	public void setShengcxbh(String shengcxbh) {
		this.shengcxbh = shengcxbh;
	}

	public String getUsercenter() {
		return usercenter;
	}

	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getLingjbh() {
		return lingjbh;
	}

	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}

	public String getFenpqh() {
		return fenpqh;
	}

	public void setFenpqh(String fenpqh) {
		this.fenpqh = fenpqh;
	}

	public String getMos() {
		return mos;
	}

	public void setMos(String mos) {
		this.mos = mos;
	}

	public String getJianglms() {
		return jianglms;
	}

	public void setJianglms(String jianglms) {
		this.jianglms = jianglms;
	}

	public String getJianglmssxsj() {
		return jianglmssxsj;
	}

	public void setJianglmssxsj(String jianglmssxsj) {
		this.jianglmssxsj = jianglmssxsj;
	}

	public String getXianbk() {
		return xianbk;
	}

	public void setXianbk(String xianbk) {
		this.xianbk = xianbk;
	}

	public String getMos2() {
		return mos2;
	}

	public void setMos2(String mos2) {
		this.mos2 = mos2;
	}

	public String getJianglms2() {
		return jianglms2;
	}

	public void setJianglms2(String jianglms2) {
		this.jianglms2 = jianglms2;
	}

	public String getJianglmssxsj2() {
		return jianglmssxsj2;
	}

	public void setJianglmssxsj2(String jianglmssxsj2) {
		this.jianglmssxsj2 = jianglmssxsj2;
	}

	public String getDinghk() {
		return dinghk;
	}

	public void setDinghk(String dinghk) {
		this.dinghk = dinghk;
	}

	public String getWms() {
		return wms;
	}

	public void setWms(String wms) {
		this.wms = wms;
	}

	
	
	
	
	public void setWulgyyz(String wulgyyz) {
		this.wulgyyz = wulgyyz;
	}

	public String getWulgyyz() {
		return wulgyyz;
	}

	public void setId(String id) {
		// TODO Auto-generated method stub
		
	}

	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	public String getCreator() {
		return creator;
	}
	
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	public String getCreateTime() {
		return createTime;
	}
	
	public void setEditor(String editor) {
		this.editor = editor;
	}
	
	public String getEditor() {
		return editor;
	}
	
	public void setEditTime(String editTime) {
		this.editTime = editTime;
	}
	
	public String getEditTime() {
		return editTime;
	}
	
	public String getUclist() {
		return uclist;
	}

	public void setUclist(String uclist) {
		this.uclist = uclist;
	}

}
