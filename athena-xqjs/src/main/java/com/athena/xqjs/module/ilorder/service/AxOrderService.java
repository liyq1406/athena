package com.athena.xqjs.module.ilorder.service;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.athena.authority.entity.LoginUser;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.athena.xqjs.entity.common.Gongys;
import com.athena.xqjs.entity.common.Lingj;
import com.athena.xqjs.entity.common.LingjGongys;
import com.athena.xqjs.entity.common.Lingjxhd;
import com.athena.xqjs.entity.common.Nianxb;
import com.athena.xqjs.entity.common.Wullj;
import com.athena.xqjs.entity.common.Xiehzt;
import com.athena.xqjs.entity.ilorder.Dingd;
import com.athena.xqjs.entity.ilorder.Dingdmx;
import com.athena.xqjs.entity.kanbyhl.Lingjck;
import com.athena.xqjs.entity.userCenter.UserCenter;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.ycbj.service.YicbjService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;


@Component
public class AxOrderService extends BaseService {

	
	public  final Logger log = Logger.getLogger(AxOrderService.class);

	// 获取订单号，key为用户中心+供应商代码，value为订单号
	private Map<String, String> orderNumberMap = new HashMap<String, String>();

	public Map<String, String> getOrderNumberMap() {
		return orderNumberMap;
	}

	public void setOrderNumberMap(Map<String, String> orderNumberMap) {
		this.orderNumberMap = orderNumberMap;
	}



	@Inject
	private NianxbService nianxbService;// 注入年包service
	@Inject
	private DingdService dingdservice;
	@Inject
	private YicbjService yicbjservice;
	/**
	 * 获取订单号
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-1
	 * @参数说明：String pattern，需求类型,String usercenter 用户中心,String gongysdm 供应商代码
	 */
	public String getOrderNumber(String pattern, String usercenter, String gongysdm,Map<String,String> orderNumberMap) throws ServiceException {// 根据不同的需求类型分别来获取订单号
		String mos = pattern.substring(0, 1);
		CommonFun.logger.debug("获取订单号getOrderNumber方法中mos="+mos);
		CommonFun.logger.debug("获取订单号getOrderNumber方法中usercenter="+usercenter);
		CommonFun.logger.debug("获取订单号getOrderNumber方法中gongysdm="+gongysdm);
		// 判断在map中是否存在订单号，若存在则直接返回订单号，
		if (null != orderNumberMap.get(usercenter + gongysdm + mos)) {
			// 直接返回订单号，
			CommonFun.mapPrint(orderNumberMap, "获取订单号getOrderNumber方法中orderNumberMap");
			return orderNumberMap.get(usercenter + gongysdm + mos);
		}
		// 若不存在对应的订单号，则先生成部分订单号
		//流水号
		String liushStr = "";
		// 获取到年
		String year = CommonFun.getDate(Const.YEARY);
		CommonFun.logger.debug("获取订单号getOrderNumber方法中year="+year);
		// 获取到月份
		String month0 = CommonFun.getDate(Const.MONTH);
		CommonFun.logger.debug("获取订单号getOrderNumber方法中month0="+month0);
		// 获取到日
		String day = CommonFun.getDate(Const.DAY);
		CommonFun.logger.debug("获取订单号getOrderNumber方法中day="+day);
		// 转换得到16进制的月份
		String month = Integer.toHexString(Integer.valueOf(month0)).toUpperCase();
		CommonFun.logger.debug("获取订单号getOrderNumber方法中month="+month);
		// 实例化年型表
		Nianxb nianx = new Nianxb();
		// 将截取到的年份set到年型的实体中
		nianx.setNianf(year);
		nianx.setUsercenter(usercenter);
		// 调用获取年型的方法，返回一个实体
		nianx = this.nianxbService.getNian(nianx);
		if (nianx == null) {
			String paramStr[] = new String[] { pattern, usercenter, gongysdm };
			yicbjservice.insertError(Const.YICHANG_LX2,
					Const.YICHANG_LX2_str66, "", paramStr,
					usercenter, "", new LoginUser(),
					Const.JISMK_IL_CD);
			throw new ServiceException("获取到年为空值,请检查年型码设置");
		}
		// 通过获取到的年型的实体，得到需要的年型
		year = nianx.getNianx();
		CommonFun.logger.debug("获取订单号getOrderNumber方法中year="+year);
		// 将各种形式放到map中
		Map<String, String> map = CommonFun.getPartten(year, month, day);
		// 得到部分订单号
		String orderNumber = map.get(pattern.toUpperCase());
		CommonFun.logger.debug("获取订单号getOrderNumber方法中orderNumber="+orderNumber);
		// 由部分订单号去订单明细总去匹配查询
		List<Dingd> list = this.dingdservice.queryOrderNumbers(orderNumber);
	//	CommonFun.objListPrint(list, "订单查找返回list");
		// 若查寻的结果集为空，则直接加上流水号0001，存入到map中
		if (list.isEmpty()) {
			// 生成最终的订单号
			orderNumber += Const.LSH_1;
			liushStr = Const.LSH_1;
			CommonFun.logger.debug("获取订单号getOrderNumber方法中订单号list为空时orderNumber="+orderNumber);
			// 若得到的结果不为空，则表示已经存在，则在流水号上加1
		} else {
			// 定义订单号的模板
			DecimalFormat data = new DecimalFormat(Const.LSH_0);
			//数据库中流水号值
			int dataLiush = Integer.valueOf(list.get(0).getDingdh()
					.substring(5, list.get(0).getDingdh().length())) + 1;
			//MAP缓存中流水号值
			int mapLiush = Integer.valueOf(StringUtils.defaultIfEmpty(orderNumberMap.get("liush"), "0")) + 1;
			if(mapLiush >= dataLiush){
				liushStr = data.format(mapLiush);
			}else{
				liushStr = data.format(dataLiush);
			}
			// 若得到的结果不为空，则表示已经存在，则在流水号上加1
			orderNumber = orderNumber + liushStr;
			CommonFun.logger.debug("获取订单号getOrderNumber方法中订单号list不为空时orderNumber="+orderNumber);
		}
		//保存流水号字符串
		orderNumberMap.put("liush",liushStr);
		// 将最终生成的订单号存放到map中
		orderNumberMap.put(usercenter + gongysdm + mos, orderNumber);
		return orderNumber.trim();// 返回订单号
	}

	//////////////////CSY 2016-11-10 按需临时订单导入/////////////////////////
	@Transactional
	public boolean impTempDingd(List<Dingdmx> dingdmxs) 
	{
		boolean falg = true;
		Map<String,Dingd> dingdMap = new HashMap<String, Dingd>();
		Map<String,List<Dingdmx>> dingdmxMap = new HashMap<String, List<Dingdmx>>();
		Dingd dingd = null;
		List<Dingdmx> dingdmxList = null;
		String key = null;
		for (Dingdmx dingdmx : dingdmxs) {
			key = dingdmx.getUsercenter() + dingdmx.getGongysdm() + dingdmx.getGonghlx() + dingdmx.getFasgysStr();
			dingd = dingdMap.get(key);
			if(null == dingd){
				dingd = new Dingd();
				dingd.setUsercenter( dingdmx.getUsercenter());
				dingd.setGongysdm(dingdmx.getGongysdm());
				dingd.setBeiz("导入数据");
				dingd.setActive(Const.ACTIVE_1);
				dingd.setChullx(dingdmx.getGonghlx());
				dingd.setDingdzt(Const.DINGD_STATUS_ZZZ);
				dingd.setShiffsgys(dingdmx.getFasgysStr());
				dingd.setFahzq(dingdmx.getFahzq());
				dingd.setDingdlx(Const.DINGDLX_LINS);
				dingd.setDingdjssj(dingdmx.getCreate_time());
				dingd.setCreate_time(dingdmx.getCreate_time());
				dingd.setCreator(dingdmx.getCreator());
				dingd.setEdit_time(dingdmx.getEdit_time());
				dingd.setEditor(dingdmx.getEditor());
				dingd.setDingdh(getOrderNumber("T", dingd.getDingdh(), dingd.getGongysdm(), new HashMap<String, String>()));
				if(!dingdservice.doInsert(dingd)){
					throw new RuntimeException("导入按需临时订单时插入订单有误!");
				}
				dingdMap.put(key, dingd);
			}
			dingdmxList = dingdmxMap.get(key);
			if(null == dingdmxList){
				dingdmxList = new ArrayList<Dingdmx>();
			}
			dingdmx.setDingdh(dingd.getDingdh());
			dingdmxList.add(dingdmx);
			dingdmxMap.put(key, dingdmxList);
		}
		for (List<Dingdmx> insert : dingdmxMap.values()) {
			List<Dingdmx> ddmxlist=new ArrayList();
			for(Dingdmx bean:insert){
				ddmxlist.add(bean);
			}
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch("ilorder.insertDingdmx", ddmxlist);
		}
		return falg;
	}
	
	/**
	 * 插入操作
	 * 
	 * @author CSY
	 * @version v4.0
	 * @date 2016-11-14
	 * 
	 */
	@Transactional
	public boolean doInsert(Dingd bean) {
		int count = 0;
		Map<String, String> map = new HashMap<String, String>();
		map.put("dingdh", bean.getDingdh());
		CommonFun.mapPrint(map, "订单插入doInsert方法中查询已有订单参数map");
		CommonFun.logger.debug("订单插入doInsert方法中查询已有订单的sql语句为：ilorder.queryDingdByDingdh");
		Dingd object = (Dingd) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ilorder.queryDingdByDingdh", map);
		map.clear();
		//LoginUser loginUser = AuthorityUtils.getSecurityUser();
		if (object == null) {
			CommonFun.logger.debug("订单插入doInsert方法中插入订单的sql语句为：ilorder.insertDingd");
			count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ilorder.insertDingd", bean);
		}
		return count > 0;
	}
	
	/**
	 * 查询ckx_usercenter(usercenter,usercenter)
	 * @return
	 */
	public Map<String, UserCenter> getUsercenters(){
		Map<String, UserCenter> ucs = new HashMap<String, UserCenter>();
		List<UserCenter> ucList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("axlsOrder.getAllUsercenter");
		if (null != ucList && ucList.size() > 0) {
			for (int i = 0; i < ucList.size(); i++) {
				ucs.put(ucList.get(i).getUsercenter(), ucList.get(i));
			}
		}
		return ucs;
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


