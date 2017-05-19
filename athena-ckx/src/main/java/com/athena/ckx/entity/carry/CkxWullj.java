package com.athena.ckx.entity.carry;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 物流路径总图
 * @author kong
 *
 */
public class CkxWullj extends PageableSupport  implements Domain{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3480031697141054019L;
	private String usercenter;//用户中心
	private String fenpqh;//分配区
	private String lingjbh;//零件编号
	private String gongysbh;//供应商编号
	private String shengcxbh;//生产线编号
	private String zhizlx;//制造路线（供货类型）
	private String lujbh;
	private Double gongyfe;//供应份额
	private String zhidgys;
	private String lujmc;
	private String fahd;
	private String waibms;
	private String wjianglms;
	private String wshengxsj;
	private String jiaofm;
	private Double beihzq;
	private String gonghlx;//新增
	private Double panysj;//新增
	private String chengysbh;//新增
	private String cangkbh;//新增
	private Double yunszq;
	private String gcbh;
	private String xiehztbh;
	private Integer songhpc;
	private Double beihsjc;//备货时间C仓库-卸货站台的备货时间
	private String mudd;
	private String dinghck;
	private String mos2;
	private String jianglms2;//将来模式
	private String shengxsj2;//将来模式生效时间
	private Integer cangkshpc2;
	private Double cangkshsj2;
	private Double cangkfhsj2;
	private Double beihsj2;
	private Double ibeihsj2;
	private Double pbeihsj2;
	private String xianbck;
	private String mos;
	private String jianglms;//将来模式
	private String shengxsj;//将来模式生效时间
	private Integer cangkshpc;
	private Double cangkshsj;
	private Double cangkfhsj;
	private Double beihsj;
	private Double ibeihsj;
	private Double pbeihsj;
	private String creator;//创建人
	private String createTime;
	private String editor;
	private String editTime;
	
	
	
	
	public String getGonghlx() {
		return gonghlx;
	}
	public void setGonghlx(String gonghlx) {
		this.gonghlx = gonghlx;
	}
	public Double getPanysj() {
		return panysj;
	}
	public void setPanysj(Double panysj) {
		this.panysj = panysj;
	}
	public String getChengysbh() {
		return chengysbh;
	}
	public void setChengysbh(String chengysbh) {
		this.chengysbh = chengysbh;
	}
	public String getCangkbh() {
		return cangkbh;
	}
	public void setCangkbh(String cangkbh) {
		this.cangkbh = cangkbh;
	}
	public String getWjianglms() {
		return wjianglms;
	}
	public void setWjianglms(String wjianglms) {
		this.wjianglms = wjianglms;
	}

	public String getWshengxsj() {
		return wshengxsj;
	}
	public void setWshengxsj(String wshengxsj) {
		this.wshengxsj = wshengxsj;
	}
	public String getJianglms2() {
		return jianglms2;
	}
	public void setJianglms2(String jianglms2) {
		this.jianglms2 = jianglms2;
	}
	public String getShengxsj2() {
		return shengxsj2;
	}
	public void setShengxsj2(String shengxsj2) {
		this.shengxsj2 = shengxsj2;
	}
	public String getJianglms() {
		return jianglms;
	}
	public void setJianglms(String jianglms) {
		this.jianglms = jianglms;
	}
	public String getShengxsj() {
		return shengxsj;
	}
	public void setShengxsj(String shengxsj) {
		this.shengxsj = shengxsj;
	}
	public String getZhizlx() {
		return zhizlx;
	}
	public void setZhizlx(String zhizlx) {
		this.zhizlx = zhizlx;
	}
	
	public String getDinghck() {
		return dinghck;
	}
	public void setDinghck(String dinghck) {
		this.dinghck = dinghck;
	}
	public String getXianbck() {
		return xianbck;
	}
	public void setXianbck(String xianbck) {
		this.xianbck = xianbck;
	}
	public String getZhidgys() {
		return zhidgys;
	}
	public void setZhidgys(String zhidgys) {
		this.zhidgys = zhidgys;
	}
	public Double getBeihsjc() {
		return beihsjc;
	}
	public void setBeihsjc(Double beihsjc) {
		this.beihsjc = beihsjc;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getEditor() {
		return editor;
	}
	public void setEditor(String editor) {
		this.editor = editor;
	}
	public String getEditTime() {
		return editTime;
	}
	public void setEditTime(String editTime) {
		this.editTime = editTime;
	}
	public String getUsercenter() {
		return usercenter;
	}
	public void setUsercenter(String usercenter) {
		this.usercenter = usercenter;
	}

	public String getFenpqh() {
		return fenpqh;
	}
	public void setFenpqh(String fenpqh) {
		this.fenpqh = fenpqh;
	}
	public String getLingjbh() {
		return lingjbh;
	}
	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}
	public String getGongysbh() {
		return gongysbh;
	}
	public void setGongysbh(String gongysbh) {
		this.gongysbh = gongysbh;
	}
	public String getShengcxbh() {
		return shengcxbh;
	}
	public void setShengcxbh(String shengcxbh) {
		this.shengcxbh = shengcxbh;
	}
	public String getLujbh() {
		return lujbh;
	}
	public void setLujbh(String lujbh) {
		this.lujbh = lujbh;
	}
	public String getLujmc() {
		return lujmc;
	}
	public void setLujmc(String lujmc) {
		this.lujmc = lujmc;
	}
	public String getFahd() {
		return fahd;
	}
	public void setFahd(String fahd) {
		this.fahd = fahd;
	}
	public String getWaibms() {
		return waibms;
	}
	public void setWaibms(String waibms) {
		this.waibms = waibms;
	}
	
	public String getJiaofm() {
		return jiaofm;
	}
	public void setJiaofm(String jiaofm) {
		this.jiaofm = jiaofm;
	}
	public Double getBeihzq() {
		return beihzq;
	}
	public void setBeihzq(Double beihzq) {
		this.beihzq = beihzq;
	}
	public Double getYunszq() {
		return yunszq;
	}
	public void setYunszq(Double yunszq) {
		this.yunszq = yunszq;
	}
	public String getGcbh() {
		return gcbh;
	}
	public void setGcbh(String gcbh) {
		this.gcbh = gcbh;
	}
	public String getXiehztbh() {
		return xiehztbh;
	}
	public void setXiehztbh(String xiehztbh) {
		this.xiehztbh = xiehztbh;
	}
	public Integer getSonghpc() {
		return songhpc;
	}
	public void setSonghpc(Integer songhpc) {
		this.songhpc = songhpc;
	}
	public String getMudd() {
		return mudd;
	}
	public void setMudd(String mudd) {
		this.mudd = mudd;
	}

	public String getMos2() {
		return mos2;
	}
	public void setMos2(String mos2) {
		this.mos2 = mos2;
	}
	public Integer getCangkshpc2() {
		return cangkshpc2;
	}
	public void setCangkshpc2(Integer cangkshpc2) {
		this.cangkshpc2 = cangkshpc2;
	}
	public Double getCangkshsj2() {
		return cangkshsj2;
	}
	public void setCangkshsj2(Double cangkshsj2) {
		this.cangkshsj2 = cangkshsj2;
	}
	public Double getCangkfhsj2() {
		return cangkfhsj2;
	}
	public void setCangkfhsj2(Double cangkfhsj2) {
		this.cangkfhsj2 = cangkfhsj2;
	}
	public Double getBeihsj2() {
		return beihsj2;
	}
	public void setBeihsj2(Double beihsj2) {
		this.beihsj2 = beihsj2;
	}
	public Double getIbeihsj2() {
		return ibeihsj2;
	}
	public void setIbeihsj2(Double ibeihsj2) {
		this.ibeihsj2 = ibeihsj2;
	}
	public Double getPbeihsj2() {
		return pbeihsj2;
	}
	public void setPbeihsj2(Double pbeihsj2) {
		this.pbeihsj2 = pbeihsj2;
	}

	
	public String getMos() {
		return mos;
	}
	public void setMos(String mos) {
		this.mos = mos;
	}
	public Integer getCangkshpc() {
		return cangkshpc;
	}
	public void setCangkshpc(Integer cangkshpc) {
		this.cangkshpc = cangkshpc;
	}
	public Double getCangkshsj() {
		return cangkshsj;
	}
	public void setCangkshsj(Double cangkshsj) {
		this.cangkshsj = cangkshsj;
	}
	public Double getCangkfhsj() {
		return cangkfhsj;
	}
	public void setCangkfhsj(Double cangkfhsj) {
		this.cangkfhsj = cangkfhsj;
	}
	public Double getBeihsj() {
		return beihsj;
	}
	public void setBeihsj(Double beihsj) {
		this.beihsj = beihsj;
	}
	public Double getIbeihsj() {
		return ibeihsj;
	}
	public void setIbeihsj(Double ibeihsj) {
		this.ibeihsj = ibeihsj;
	}
	public Double getPbeihsj() {
		return pbeihsj;
	}
	public void setPbeihsj(Double pbeihsj) {
		this.pbeihsj = pbeihsj;
	}
	public void setId(String id) {
		// TODO Auto-generated method stub
		
	}
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}
	public Double getGongyfe() {
		return gongyfe;
	}
	public void setGongyfe(Double gongyfe) {
		this.gongyfe = gongyfe;
	}

	
}
