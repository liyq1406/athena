package com.athena.ckx.entity.xuqjs;

import com.athena.component.entity.Domain;
import com.toft.core3.support.PageableSupport;


/**
 * 零件供应商临时包装
 * @author denggq
 * @date 2012-4-17
 */
public class Lingjgyslsbz extends PageableSupport implements Domain{
	
	private static final long serialVersionUID = -7371707413342124001L;
	
	private String usercenter;    //用户中心
	
	private String gongysbh;      //供应商编号
	
	private String lingjbh;       //零件编号
	
	private Integer xuh;			  //序号
	
	private String shengxsj;	  //生效时间
	
	private String shixsj;		  //失效时间

	private String ucbzlx;        //供应商UC的包装类型
	
	private Double ucrl;          //供应商UC的容量
	
	private String uabzlx;        //供应商UA的包装类型
	
	private Integer uaucgs;        //供应商UA里UC的个数
	
	private String gaib;          //盖板
	
	private String neic;          //内衬

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
	public String getLingjbh() {
		return lingjbh;
	}
	public void setLingjbh(String lingjbh) {
		this.lingjbh = lingjbh;
	}
	public void setXuh(Integer xuh) {
		this.xuh = xuh;
	}
	public Integer getXuh() {
		return xuh;
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

	@Override
	public void setId(String id) {
		
	}
	@Override
	public String getId() {
		return null;
	}
	
}
