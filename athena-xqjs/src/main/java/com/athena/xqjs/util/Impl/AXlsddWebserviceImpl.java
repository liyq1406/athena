package com.athena.xqjs.util.Impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jws.WebService;

import org.apache.commons.logging.Log;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.common.Gongys;
import com.athena.xqjs.entity.common.Lingj;
import com.athena.xqjs.entity.common.LingjGongys;
import com.athena.xqjs.entity.common.Lingjxhd;
import com.athena.xqjs.entity.common.Wullj;
import com.athena.xqjs.entity.common.Xiehzt;
import com.athena.xqjs.entity.ilorder.Dingdmx;
import com.athena.xqjs.entity.kanbyhl.Lingjck;
import com.athena.xqjs.util.AXlsddWebservice;
import com.toft.core3.container.annotation.Component;



/**
 * 用于按需临时订单在执行层的校验
 * @author admin
 *
 */
@SuppressWarnings("rawtypes")
@WebService(endpointInterface="com.athena.xqjs.util.AXlsddWebservice", serviceName="/axLSDDWebservice")
@Component
public class AXlsddWebserviceImpl extends BaseService implements AXlsddWebservice
{
	// 日志
	private final Log log = org.apache.commons.logging.LogFactory.getLog(AXlsddWebserviceImpl.class);

	private String result_suss = "success";	//成功代码
	private String result_fail = "fail";	//失败代码
	
	private Map<String, Lingj> lingjs = new HashMap<String, Lingj>();				//零件
	private Map<String, Gongys> gongyss = new HashMap<String, Gongys>();			//供应商
	private Map<String, LingjGongys> lingjgyss = new HashMap<String, LingjGongys>();//零件供应商
	private Map<String, Map<String, Wullj>> wulljs = new HashMap<String, Map<String, Wullj>>();				//物流路径
	private Map<String, Lingjck> lingjcks = new HashMap<String, Lingjck>();			//零件仓库
	private Map<String, Lingjxhd> lingjxhds = new HashMap<String, Lingjxhd>();		//零件消耗点
	private Map<String, Xiehzt> xiehzts = new HashMap<String, Xiehzt>();			//卸货站台
	
	/**
	 * 校验订单明细
	 */
	public String checkAXlldd(List<Dingdmx> list) {
		String result = "";
		StringBuffer lingjParam = new StringBuffer("");
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getLingjbh() != null && !list.get(i).getLingjbh().equals("")) {
				if (i != 0 && i < list.size()) {
					lingjParam.append(",");
				}
				lingjParam.append("'").append(list.get(i).getLingjbh()).append("'");
			}
		}
		
		lingjs = this.getLingj(lingjParam.toString());
		gongyss = this.getGongys();
		lingjgyss = this.getLingjgys(lingjParam.toString());
		wulljs = this.getWullj(lingjParam.toString());
		lingjcks = this.getLingjck(lingjParam.toString());
		lingjxhds = this.getLingjxhd(lingjParam.toString());
		xiehzts = this.getXiehzt();
		for (Dingdmx ddmx:list) {
			if (null != ddmx.getGonghlx() && !ddmx.getGonghlx().equals("")) {
				
				//执行层参考系-零件
				if (!lingjs.containsKey(ddmx.getUsercenter() + ddmx.getLingjbh())) {
					//result = this.result_fail;
					log.info(ddmx.getLingjbh()+"找不到零件");
					result = ddmx.getLingjbh();
					return result;
				}
				
				if (ddmx.getGonghlx().equals("C1") || ddmx.getGonghlx().equals("CV") || ddmx.getGonghlx().equals("CD")) {
					//执行层参考系-供应商
					if (!gongyss.containsKey(ddmx.getUsercenter() + ddmx.getGongysdm())) {
						//result = this.result_fail;
						log.info(ddmx.getLingjbh()+"找不到供应商");
						result = ddmx.getLingjbh();
						return result;
					}
					
					//执行层参考系-零件供应商
					if (!lingjgyss.containsKey(ddmx.getUsercenter() + ddmx.getGongysdm() + ddmx.getLingjbh())) {
						//result = this.result_fail;
						log.info(ddmx.getLingjbh()+"找不到零件供应商");
						result = ddmx.getLingjbh();
						return result;
					}
				}
				
				if (ddmx.getGongysdm().equals("M1") || ddmx.getGonghlx().equals("MV") || ddmx.getGonghlx().equals("MD")) {
					if (!lingjcks.containsKey(ddmx.getUsercenter() + ddmx.getLingjbh() + ddmx.getGongysdm())) {
						//result = this.result_fail;
						result = ddmx.getLingjbh();
						log.info(ddmx.getLingjbh()+"找不到订货仓库");
						return result;
					}
					
					if (!lingjcks.containsKey(ddmx.getUsercenter() + ddmx.getLingjbh() + ddmx.getCangkdm())) {
						//result = this.result_fail;
						result = ddmx.getLingjbh();
						log.info(ddmx.getLingjbh()+"找不到线边仓库");
						return result;
					}
					
				}
				
				String key = "";
				String mos = "";
				if (ddmx.getGonghlx().equals("CD")) {
					//key=用户中心+零件编号+供应商代码+生产线编号+分配区号+外部模式
					key = ddmx.getUsercenter() + ddmx.getLingjbh() + ddmx.getGongysdm() + ddmx.getChanx() + ddmx.getKeh().substring(0, 5) + ddmx.getGonghlx();
					mos = "CD";
				}else if (ddmx.getGonghlx().equals("C1") || ddmx.getGonghlx().equals("CV")) {
					//key=用户中心+零件编号+目的地+外部模式+供应商代码
					key = ddmx.getUsercenter() + ddmx.getLingjbh() + ddmx.getKeh() + ddmx.getGonghlx() + ddmx.getGongysdm();
					mos = "CV";
				}else if (ddmx.getGonghlx().equals("MD")) {
					//key=用户中心+零件编号+订货仓库+分配区+模式
					key = ddmx.getUsercenter() + ddmx.getLingjbh() + ddmx.getGongysdm() + ddmx.getKeh().substring(0, 5) + ddmx.getGonghlx();
					mos = "MD";
				}else if (ddmx.getGonghlx().equals("M1") || ddmx.getGonghlx().equals("MV")) {
					//key=用户中心+零件编号+订货仓库+线边仓库+模式2
					key = ddmx.getUsercenter() + ddmx.getLingjbh() + ddmx.getGongysdm() + ddmx.getKeh() + ddmx.getGonghlx();
					mos = "MV";
				}
				//执行层参考系-物流路径
				if (!wulljs.containsKey(mos)) {
					//result = this.result_fail;
					result = ddmx.getLingjbh();
					log.info(ddmx.getLingjbh()+"找不到当前模式对应的物流路径");
					return result;
				}else {
					if (!wulljs.get(mos).containsKey(key)) {
						//result = this.result_fail;
						result = ddmx.getLingjbh();
						log.info(ddmx.getLingjbh()+"找不到当前键对应的物流路径");
						return result;
					}else {
						Wullj wullj = wulljs.get(mos).get(key);
						if (wullj.getXiehztbh().equals("") || wullj.getXiehztbh() == null) {
							result = ddmx.getLingjbh();
							log.info(ddmx.getLingjbh()+"当前物流路径没有卸货站台");
							return result;
						}
						if (wullj.getGcbh().equals("") || wullj.getGcbh() == null) {
							result = ddmx.getLingjbh();
							log.info(ddmx.getLingjbh()+"当前物流路径没有承运商");
							return result;					
												}
						if (wullj.getLujbh().equals("") || wullj.getLujbh() == null) {
							result = ddmx.getLingjbh();
							log.info(ddmx.getLingjbh()+"当前物流路径没有路径编码");
							return result;
						}
					}
				}
				
				//执行层参考系-零件仓库
				if (!lingjcks.containsKey(ddmx.getUsercenter() + ddmx.getLingjbh() + ddmx.getCangkdm())) {
					//result = this.result_fail;
					result = ddmx.getLingjbh();
					log.info(ddmx.getLingjbh()+"找不到仓库");
					return result;
				}
				
				if (ddmx.getGonghlx().equals("CD") || ddmx.getGonghlx().equals("MD")) {
					//执行层参考系-零件消耗点
					if (!lingjxhds.containsKey(ddmx.getUsercenter() + ddmx.getLingjbh() + ddmx.getXiaohd())) {
						//result = this.result_fail;
						result = ddmx.getLingjbh();
						return result;
					}
					
				}
				
				if (ddmx.getGonghlx().equals("C1") || ddmx.getGonghlx().equals("CV") || ddmx.getGonghlx().equals("CD")) {
					//执行层参考系-卸货站台
					if (!xiehzts.containsKey(ddmx.getUsercenter() + ddmx.getXiehzt())) {
						//result = this.result_fail;
						result = ddmx.getLingjbh();
						return result;
					}
					
				}
				
			}else {
				result = this.result_fail;
				return result;
			}
		}
		result = this.result_suss;
		return result;
	}
	
	/**
	 * 查询ckx_lingj(usercenter, lingjbh)
	 */
	public Map<String, Lingj> getLingj(String str){
		Map<String, Lingj> mapLingj = new HashMap<String, Lingj>();
		List<Lingj> listLingj = new ArrayList<Lingj>();
		listLingj = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("axlsOrder.getAllLingj", str);
		for (Lingj lingj : listLingj) {
			mapLingj.put(lingj.getUsercenter() + lingj.getLingjbh(), lingj);
		}
		return mapLingj;
	}
	
	/**
	 * 如果不是M1/MV/MD，查询ckx_gongys(usercenter, gongysdm, gonghms)
	 */
	public Map<String, Gongys> getGongys(){
		Map<String, Gongys> mapGongys = new HashMap<String, Gongys>();
		List<Gongys> listGongys = new ArrayList<Gongys>();
		listGongys = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("axlsOrder.getAllGongys");
		for (Gongys gongys : listGongys) {
			mapGongys.put(gongys.getUsercenter() + gongys.getGcbh(), gongys);
		}
		return mapGongys;
	}
	
	/**
	 * 如果不是M1/MV/MD，查询ckx_lingjgys(usercenter, gongysdm, lingjbh)
	 */
	public Map<String, LingjGongys> getLingjgys(String str){
		Map<String, LingjGongys> mapLingjgys = new HashMap<String, LingjGongys>();
		List<LingjGongys> listLingjgys = new ArrayList<LingjGongys>();
		listLingjgys = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("axlsOrder.getAllLingjgys", str);
		for (LingjGongys lingjgys : listLingjgys) {
			mapLingjgys.put(lingjgys.getUsercenter() + lingjgys.getGongysbh() + lingjgys.getLingjbh(), lingjgys);
		}
		return mapLingjgys;
	}
	
	/**
	 * 查询ckx_wullj(usercenter, gongysdm, lingjbh, gonghms, cangkbh, xiaohd, zickbh)
	 */
	public Map<String, Map<String, Wullj>> getWullj(String str){
		Map<String, Wullj> mapWulljCV = new HashMap<String, Wullj>();
		Map<String, Wullj> mapWulljCD = new HashMap<String, Wullj>();
		Map<String, Wullj> mapWulljMV = new HashMap<String, Wullj>();
		Map<String, Wullj> mapWulljMD = new HashMap<String, Wullj>();
		//根据供货模式的物流路径
		Map<String, Map<String, Wullj>> mapWuljMOS = new HashMap<String, Map<String,Wullj>>();
		List<Wullj> listWullj = new ArrayList<Wullj>();
		listWullj = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("axlsOrder.getAllWullj", str);
		for (Wullj wullj : listWullj) {
			if (wullj.getWaibms() != null && wullj.getWaibms().equals("CD")) {
				mapWulljCD.put(wullj.getUsercenter() + wullj.getLingjbh() + wullj.getGongysbh() + wullj.getShengcxbh() + wullj.getFenpqh() + wullj.getWaibms(), wullj);
			}
			if ((wullj.getWaibms() != null && wullj.getWaibms().equals("C1")) || (wullj.getWaibms() != null && wullj.getWaibms().equals("CV"))) {
				mapWulljCV.put(wullj.getUsercenter() + wullj.getLingjbh() + wullj.getMudd() + wullj.getWaibms() + wullj.getGongysbh(), wullj);
			}
			if (wullj.getMos2() != null && wullj.getMos2().equals("MD")) {
				mapWulljMD.put(wullj.getUsercenter() + wullj.getLingjbh() + wullj.getDinghck() + wullj.getFenpqh() +wullj.getMos(), wullj);
			}
			if ((wullj.getMos2() != null && wullj.getMos2().equals("M1")) || (wullj.getMos2() != null && wullj.getMos2().equals("MV"))) {
				mapWulljMV.put(wullj.getUsercenter() + wullj.getLingjbh() + wullj.getDinghck() + wullj.getXianbck() + wullj.getMos2(), wullj);
			}
			mapWuljMOS.put("CD", mapWulljCD);	//CD
			mapWuljMOS.put("CV", mapWulljCV);	//C1/CD
			mapWuljMOS.put("MD", mapWulljMD);	//MD
			mapWuljMOS.put("MV", mapWulljMV);	//M1/MD
		}
		return mapWuljMOS;
	}
	
	/**
	 * 查询ckx_lingjck(usercenter, lingjbh, gonghms, cangkbh, zickbh)
	 */
	public Map<String, Lingjck> getLingjck(String str){
		Map<String, Lingjck> mapLingjck = new HashMap<String, Lingjck>();
		List<Lingjck> listLingjck = new ArrayList<Lingjck>();
		listLingjck = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("axlsOrder.getAllLingjck", str);
		for (Lingjck lingjck : listLingjck) {
			mapLingjck.put(lingjck.getUsercenter() + lingjck.getLingjbh() + lingjck.getCangkbh(), lingjck);
		}
		return mapLingjck;
	}
	
	/**
	 * 如果是CD/MD，查询ckx_lingjxhd(usercenter, lingjbh, xiaohd)
	 */
	public Map<String, Lingjxhd> getLingjxhd(String str){
		Map<String, Lingjxhd> mapLingjxhd = new HashMap<String, Lingjxhd>();
		List<Lingjxhd> listLingjxhd = new ArrayList<Lingjxhd>();
		listLingjxhd = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("axlsOrder.getAllLingjxhd", str);
		for (Lingjxhd lingjxhd : listLingjxhd) {
			mapLingjxhd.put(lingjxhd.getUsercenter() + lingjxhd.getLingjbh() + lingjxhd.getXiaohdbh(), lingjxhd);
		}
		return mapLingjxhd;
	}
	
	/**
	 * 如果是C1/CV/CD，查询ckx_xiehzt(usercenter, xiehzt)
	 */
	public Map<String, Xiehzt> getXiehzt(){
		Map<String, Xiehzt> mapXiehzt = new HashMap<String, Xiehzt>();
		List<Xiehzt> listXiehzt = new ArrayList<Xiehzt>();
		listXiehzt = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("axlsOrder.getAllXiehzt");
		for (Xiehzt xiehzt : listXiehzt) {
			mapXiehzt.put(xiehzt.getUsercenter() + xiehzt.getXiehztbh(), xiehzt);
		}
		return mapXiehzt;
	}
}
