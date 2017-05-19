package com.athena.ckx.entity.xuqjs;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;

/**
 * 消耗点-零件
 * @author qizhongtao
 * 2012-4-12
 */
public class CkxLingjxhd extends PageableSupport implements Domain{

	private static final long serialVersionUID = 5473820186894872128L;
	
	public static final String WTC_CHECK_PSLX = "PSLB01"; //WTC服务交易码：验证配送类型
	
	public static final String WTC_SUCCESS_PSLX = "0000"; //WTC服务交易码：验证配送类型
	
	private String usercenter;     //用户中心
	
	private String lingjbh;        //零件编号
	
	private String xiaohdbh;       //消耗点编号
	
	private String wulbh;          //物流描述编号
	
	private String fenpqbh;        //分配循环编号(根据消耗点编号自动推出)
	
	private String shengcxbh;      //生产线编号
	
	private String gongybs;		   //工艺标识
	
	private String peislxbh;       //配送类型
	
	private String qianhcbs;       //前后车标识
	
	private String jipbzwz;        //集配包装位置
	
	private String shengxr;        //消耗点起始日
	
	private String jiesr;          //结束日期
	
	private String guanlybh;       //管理员编号
	
	private Double tiqjsxhbl;      //提前计算消耗比例
	
	private Double xiaohbl;        //消耗比例
	
	private String gongysbh;       //指定供应商
	
	private String shunxglbz;      //顺序管理标志
	
	private String zidfhbz;        //自动发货标志
	
	private String xianbyhlx;      //线边要货类型:K/R/P/S/G
	
	private String yancsxbz;       //是否延迟上线:I/P
	
	private String beihdbz;        //是否产生备货单
	
	private String xiaohcbh;       //小火车编号
	
	private String xiaohccxbh;     //小火车车厢编号
	
	private String yuanxhdbh;      //原消耗点
	
	private Double anqkcts;        //安全库存天数
	
	private Double anqkcs;         //安全库存数
	
	private Double xianbllkc;      //线边理论库存
	
	private Double yifyhlzl;       //已发要货令总量
	
	private Double jiaofzl;        //交付总量
	
	private Double xittzz;         //系统调整值(已发要货令总数量 - 交付总量)
	
	private String pdsbz;          //PDS拆分标记(同步/集配/按需)
	
	private String biaos;          //生效标志
	
	private Double zhongzzl;       //终止总量
	
	private String creator;        //创建人
	
	private String create_time;    //创建时间
	
	private String editor;         //修改人
	
	private String edit_time;      //修改时间
	
	private String zuh;				//组号
	
	private String zhizlx;    		//制造路线（物流路径专用，由零件表带出）
	
	private String wulgyyz;			//物流工艺员组
	
	private String ceng;			//层（仓库用）
	//0007775当零件-消耗点 从失效恢复成有效时   记录原标识
	private String yuanbiaos;        //原标识
	
	
	private Integer version;  //控制并发的 版本号
	
    private String pdsshengxsj; //PDS生效时间
    
    private String pdsshixsj;   //PDS失效时间	
    
    private String gongyxl;		//工艺系列
    
    private String fenzxh;		//分装线号
    
	public String getPdsshengxsj() {
		return pdsshengxsj;
	}
	public void setPdsshengxsj(String pdsshengxsj) {
		this.pdsshengxsj = pdsshengxsj;
	}
	public String getPdsshixsj() {
		return pdsshixsj;
	}
	public void setPdsshixsj(String pdsshixsj) {
		this.pdsshixsj = pdsshixsj;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public String getYuanbiaos() {
		return yuanbiaos;
	}
	public void setYuanbiaos(String yuanbiaos) {
		this.yuanbiaos = yuanbiaos;
	}
	public String getWulgyyz() {
		return wulgyyz;
	}
	public void setWulgyyz(String wulgyyz) {
		this.wulgyyz = wulgyyz;
	}
	public String getZhizlx() {
		return zhizlx;
	}
	public void setZhizlx(String zhizlx) {
		this.zhizlx = zhizlx;
	}
	public void setZuh(String zuh) {
		this.zuh = zuh;
	}
	public String getZuh() {
		return zuh;
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
	public String getXiaohdbh() {
		return xiaohdbh;
	}
	public void setXiaohdbh(String xiaohdbh) {
		this.xiaohdbh = xiaohdbh;
	}
	public String getWulbh() {
		return wulbh;
	}
	public void setWulbh(String wulbh) {
		this.wulbh = wulbh;
	}
	public String getFenpqbh() {
		return fenpqbh;
	}
	public void setFenpqbh(String fenpqbh) {
		this.fenpqbh = fenpqbh;
	}
	public String getShengcxbh() {
		return shengcxbh;
	}
	public void setShengcxbh(String shengcxbh) {
		this.shengcxbh = shengcxbh;
	}
	public void setGongybs(String gongybs) {
		this.gongybs = gongybs;
	}
	public String getGongybs() {
		return gongybs;
	}
	public String getPeislxbh() {
		return peislxbh;
	}
	public void setPeislxbh(String peislxbh) {
		this.peislxbh = peislxbh;
	}
	public String getQianhcbs() {
		return qianhcbs;
	}
	public void setQianhcbs(String qianhcbs) {
		this.qianhcbs = qianhcbs;
	}
	public String getJipbzwz() {
		return jipbzwz;
	}
	public void setJipbzwz(String jipbzwz) {
		this.jipbzwz = jipbzwz;
	}
	public String getShengxr() {
		return shengxr;
	}
	public void setShengxr(String shengxr) {
		this.shengxr = shengxr;
	}
	public String getJiesr() {
		return jiesr;
	}
	public void setJiesr(String jiesr) {
		this.jiesr = jiesr;
	}
	public String getGuanlybh() {
		return guanlybh;
	}
	public void setGuanlybh(String guanlybh) {
		this.guanlybh = guanlybh;
	}
	public Double getTiqjsxhbl() {
		return tiqjsxhbl;
	}
	public void setTiqjsxhbl(Double tiqjsxhbl) {
		this.tiqjsxhbl = tiqjsxhbl;
	}
	public Double getXiaohbl() {
		return xiaohbl;
	}
	public void setXiaohbl(Double xiaohbl) {
		this.xiaohbl = xiaohbl;
	}
	public String getGongysbh() {
		return gongysbh;
	}
	public void setGongysbh(String gongysbh) {
		this.gongysbh = gongysbh;
	}
	public String getShunxglbz() {
		return shunxglbz;
	}
	public void setShunxglbz(String shunxglbz) {
		this.shunxglbz = shunxglbz;
	}
	public String getZidfhbz() {
		return zidfhbz;
	}
	public void setZidfhbz(String zidfhbz) {
		this.zidfhbz = zidfhbz;
	}
	public String getXianbyhlx() {
		return xianbyhlx;
	}
	public void setXianbyhlx(String xianbyhlx) {
		this.xianbyhlx = xianbyhlx;
	}
	public String getYancsxbz() {
		return yancsxbz;
	}
	public void setYancsxbz(String yancsxbz) {
		this.yancsxbz = yancsxbz;
	}
	public String getBeihdbz() {
		return beihdbz;
	}
	public void setBeihdbz(String beihdbz) {
		this.beihdbz = beihdbz;
	}
	public String getXiaohcbh() {
		return xiaohcbh;
	}
	public void setXiaohcbh(String xiaohcbh) {
		this.xiaohcbh = xiaohcbh;
	}
	public String getXiaohccxbh() {
		return xiaohccxbh;
	}
	public void setXiaohccxbh(String xiaohccxbh) {
		this.xiaohccxbh = xiaohccxbh;
	}
	public String getYuanxhdbh() {
		return yuanxhdbh;
	}
	public void setYuanxhdbh(String yuanxhdbh) {
		this.yuanxhdbh = yuanxhdbh;
	}
	public Double getAnqkcts() {
		return anqkcts;
	}
	public void setAnqkcts(Double anqkcts) {
		this.anqkcts = anqkcts;
	}
	public Double getAnqkcs() {
		return anqkcs;
	}
	public void setAnqkcs(Double anqkcs) {
		this.anqkcs = anqkcs;
	}
	public Double getXianbllkc() {
		return xianbllkc;
	}
	public void setXianbllkc(Double xianbllkc) {
		this.xianbllkc = xianbllkc;
	}
	public Double getYifyhlzl() {
		return yifyhlzl;
	}
	public void setYifyhlzl(Double yifyhlzl) {
		this.yifyhlzl = yifyhlzl;
	}
	public Double getJiaofzl() {
		return jiaofzl;
	}
	public void setJiaofzl(Double jiaofzl) {
		this.jiaofzl = jiaofzl;
	}
	public Double getXittzz() {
		return xittzz;
	}
	public void setXittzz(Double xittzz) {
		this.xittzz = xittzz;
	}
	public String getPdsbz() {
		return pdsbz;
	}
	public void setPdsbz(String pdsbz) {
		this.pdsbz = pdsbz;
	}
	public String getBiaos() {
		return biaos;
	}
	public void setBiaos(String biaos) {
		this.biaos = biaos;
	}
	public Double getZhongzzl() {
		return zhongzzl;
	}
	public void setZhongzzl(Double zhongzzl) {
		this.zhongzzl = zhongzzl;
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
	public void setCeng(String ceng) {
		this.ceng = ceng;
	}
	public String getCeng() {
		return ceng;
	}
	public String getGongyxl() {
		return gongyxl;
	}
	public void setGongyxl(String gongyxl) {
		this.gongyxl = gongyxl;
	}
	public String getFenzxh() {
		return fenzxh;
	}
	public void setFenzxh(String fenzxh) {
		this.fenzxh = fenzxh;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public void setId(String id) {
		
	}
	public String getId() {
		return null;
	}
	
	
}
