package com.athena.ckx.entity.xuqjs;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;


/**
 * 零件供应商
 * @author qizhongtao
 * @date 2012-3-29
 */
public class Lingjgys extends PageableSupport implements Domain{
	
	private static final long serialVersionUID = 1960559787804675428L;
	
	private String usercenter;    //用户中心
	private String gongysbh;      //供应商编号
	private String lingjbh;       //零件编号
	private String gongyhth;      //供应商合同号
	private Double gongyfe;       //供应份额
	private Integer youxq;        //有效期
	private String fayd;          //发运地
	private String shengxsj;      //生效时间
	private String shixsj;        //失效时间
	private Double zuixqdl;       //最小起定量
	private Double cankfz;        //参考阀值
	private String zhijcjbl;      //质检抽检比例
	private String shifyzpch;     //是否验证批次号
	private String ucbzlx;        //供应商UC的包装类型
	private Double ucrl;          //供应商UC的容量
	private String uabzlx;        //供应商UA的包装类型
	private Integer uaucgs;       //供应商UA里UC的个数
	private Double uarl;		  //供应商UA容量
	private String gaib;          //盖板
	private String neic;          //内衬
	private String biaos;		  //标识
	private String shifczlsbz;    //是否存在临时包装
	private String creator;       //创建人
	private String create_time;   //创建时间
	private String editor;        //修改人
	private String edit_time;     //修改时间
	
	private String shifsxgl;	//是否失效管理  2012-08-10
	
	private String dinghlx;     //订货路线
	
	public String getDinghlx() {
		return dinghlx;
	}
	public void setDinghlx(String dinghlx) {
		this.dinghlx = dinghlx;
	}
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
	public String getLingjbh() {
		return lingjbh;
	}
	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}
	public String getGongyhth() {
		return gongyhth;
	}
	public void setGongyhth(String gongyhth) {
		this.gongyhth = gongyhth;
	}
	public Double getGongyfe() {
		return gongyfe;
	}
	public void setGongyfe(Double gongyfe) {
		this.gongyfe = gongyfe;
	}
	public Integer getYouxq() {
		return youxq;
	}
	public void setYouxq(Integer youxq) {
		this.youxq = youxq;
	}
	public String getFayd() {
		return fayd;
	}
	public void setFayd(String fayd) {
		this.fayd = fayd;
	}
	public String getShengxsj() {
		return shengxsj;
	}
	public void setShengxsj(String shengxsj) {
		this.shengxsj = shengxsj;
	}
	public String getShixsj() {
		return shixsj;
	}
	public void setShixsj(String shixsj) {
		this.shixsj = shixsj;
	}
	public Double getZuixqdl() {
		return zuixqdl;
	}
	public void setZuixqdl(Double zuixqdl) {
		this.zuixqdl = zuixqdl;
	}
	public Double getCankfz() {
		return cankfz;
	}
	public void setCankfz(Double cankfz) {
		this.cankfz = cankfz;
	}
	public String getZhijcjbl() {
		return zhijcjbl;
	}
	public void setZhijcjbl(String zhijcjbl) {
		this.zhijcjbl = zhijcjbl;
	}
	public String getShifyzpch() {
		return shifyzpch;
	}
	public void setShifyzpch(String shifyzpch) {
		this.shifyzpch = shifyzpch;
	}
	public String getUcbzlx() {
		return ucbzlx;
	}
	public void setUcbzlx(String ucbzlx) {
		this.ucbzlx = ucbzlx;
	}
	public Double getUcrl() {
		return ucrl;
	}
	public void setUcrl(Double ucrl) {
		this.ucrl = ucrl;
	}
	public String getUabzlx() {
		return uabzlx;
	}
	public void setUabzlx(String uabzlx) {
		this.uabzlx = uabzlx;
	}
	public Integer getUaucgs() {
		return uaucgs;
	}
	public void setUaucgs(Integer uaucgs) {
		this.uaucgs = uaucgs;
	}
	public String getGaib() {
		return gaib;
	}
	public void setGaib(String gaib) {
		this.gaib = gaib;
	}
	public String getNeic() {
		return neic;
	}
	public void setNeic(String neic) {
		this.neic = neic;
	}
	public String getShifczlsbz() {
		return shifczlsbz;
	}
	public void setShifczlsbz(String shifczlsbz) {
		this.shifczlsbz = shifczlsbz;
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

	
	public Double getUarl() {
		return uarl;
	}
	public void setUarl(Double uarl) {
		this.uarl = uarl;
	}
	public void setBiaos(String biaos) {
		this.biaos = biaos;
	}
	public String getBiaos() {
		return biaos;
	}
	public void setShifsxgl(String shifsxgl) {
		this.shifsxgl = shifsxgl;
	}
	public String getShifsxgl() {
		return shifsxgl;
	}
	@Override
	public void setId(String id) {
		
	}
	@Override
	public String getId() {
		return null;
	}
	
}
