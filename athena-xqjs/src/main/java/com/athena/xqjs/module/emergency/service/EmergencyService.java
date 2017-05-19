package com.athena.xqjs.module.emergency.service;


import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.common.Gongys;
import com.athena.xqjs.entity.common.Lingj;
import com.athena.xqjs.entity.common.LingjGongys;
import com.athena.xqjs.entity.common.Lingjxhd;
import com.athena.xqjs.entity.common.Nianxb;
import com.athena.xqjs.entity.common.Wullj;
import com.athena.xqjs.entity.common.Xiaohcyssk;
import com.athena.xqjs.entity.common.Xiehzt;
import com.athena.xqjs.entity.common.Yunssk;
import com.athena.xqjs.entity.emergency.EmergencyModel;
import com.athena.xqjs.entity.ilorder.Dingd;
import com.athena.xqjs.entity.ilorder.Dingdmx;
import com.athena.xqjs.module.anxorder.service.AnxMaoxqService;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.common.service.LingjxhdService;
import com.athena.xqjs.module.common.service.WulljService;
import com.athena.xqjs.module.common.service.XiaohcysskService;
import com.athena.xqjs.module.ilorder.service.DingdService;
import com.athena.xqjs.module.ilorder.service.NianxbService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.support.PageableSupport;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * @see    紧急件报警服务类
 * @author wuyichao
 *
 */
@SuppressWarnings("rawtypes")
@Component
public class EmergencyService extends BaseService 
{
	
	@Inject
	private NianxbService nianxbService;
	
	@Inject
	private DingdService dingdService;
	
	@Inject
	private LingjxhdService lingjxhdService;

	@Inject
	private XiaohcysskService xService;

	@Inject
	private AnxMaoxqService anxMaoxqService;
	
	@Inject
	private WulljService wulljService;// 柔性比例service
	
	public  Object queryEmergency(PageableSupport pageBean  ,Map<String, String> params)
	{
		System.out.println(params.get("gongysbh"));
		if(!"exportXls".equals(params.get("exportXls")))
		{
			return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("anx.queryEmergency", params , pageBean);
		}
		else
		{
			List list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anx.queryEmergency",params);
			if( null != list )
			{
				if(list.size() > 5000)
					return "<script>alert(\"导出数量不能大于5000!请做调整\");window.close();</script>";
				else
					return CommonFun.listToMap(list);
			}
			return list;
		}
	}

	@Transactional
	public void unoperating(String ids,String username) 
	{
		if(StringUtils.isNotBlank(ids))
		{
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("ids", ids);
			paramMap.put("username", username);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("anx.unoperatingEmergency",paramMap);
		}
	}

	public EmergencyModel findById(Map<String, String> paramMap) 
	{
		EmergencyModel emergencyModel = null;
		if(null != paramMap && StringUtils.isNotBlank(paramMap.get("id")))
		{
			emergencyModel = (EmergencyModel) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anx.queryEmergencyById", paramMap);
		}
		return emergencyModel;
	}

	public List<EmergencyModel> queryWulljxx(Map<String, String> paramMap)
	{
		List<EmergencyModel> wulljList = null;
		if(null != paramMap && StringUtils.isNotBlank(paramMap.get("lingjbh")) && StringUtils.isNotBlank(paramMap.get("usercenter"))  && StringUtils.isNotBlank(paramMap.get("gongysbh")))
		{
			wulljList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anx.queryWulljxxByEmergency", paramMap);
		}
		return wulljList;
	}

	public List<EmergencyModel> queryGcbhxx(Map<String, String> paramMap)
	{
		List<EmergencyModel> gcbhList = null;
		if(null != paramMap && StringUtils.isNotBlank(paramMap.get("gcbh")) )
		{
			gcbhList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anx.queryGongysChengysxxByEmergency", paramMap);
		}
		return gcbhList;
	}

	public List<EmergencyModel> findByIds(String ids) 
	{
		List<EmergencyModel> emergencyModels = null;
		if(StringUtils.isNotBlank(ids))
		{
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("ids", ids);
			emergencyModels = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anx.queryEmergencyById", paramMap);
		}
		return emergencyModels;
	}

	public String operating(List<EmergencyModel> emergencyModels, String username) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, ParseException 
	{
		String result = "处理完成!";
		StringBuffer errorSb = new StringBuffer(); 
		if(null != emergencyModels && emergencyModels.size() > 0 && StringUtils.isNotBlank(username))
		{
			Map<String, List<EmergencyModel>> gongysMap = new HashMap<String, List<EmergencyModel>>();
			//处理紧急件信息，并生成CD模式的临时订单,订单不需要审核直接生效
			for (EmergencyModel emergencyModel : emergencyModels) 
			{
				/*if(checkEmergencyModel(emergencyModel))
				{*/
					//将同供应商的紧急件信息归集在一起
					//emergencyModel.setGongysbh(emergencyModel.getGongysbh().replaceAll("\\W", " "));
					//emergencyModel.setChengysbh(emergencyModel.getChengysbh().replaceAll("\\W", " "));
					List<EmergencyModel> flagEmergencyModels = null;
					String flagStr = emergencyModel.getUsercenter() + "_" + emergencyModel.getGongysbh();
					flagEmergencyModels = gongysMap.get(flagStr);
					if(null == flagEmergencyModels)
					{
						flagEmergencyModels = new ArrayList<EmergencyModel>();
					}
					flagEmergencyModels.add(emergencyModel);
					gongysMap.put(flagStr, flagEmergencyModels);
				/*}
				else
				{
					emergencyModel.setBeiz("参数有缺失!");
					emergencyModel.setZhuangt("3");
					//修改状态
					this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("anx.editEmergencyById", emergencyModel);
				}*/
			}
			//创建订单
			errorSb.append(createOrder(gongysMap,username));
			result = errorSb.toString();
		}
		else
		{
			result = "用户名为空,或没有紧急件数据进行处理!";
		}
		return result;
	}

	//创建订单信息
	private String createOrder(Map<String, List<EmergencyModel>> gongysMap, String username) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, ParseException 
	{
		StringBuffer resule = new StringBuffer("处理完成.");
		Set<String> kyes = gongysMap.keySet();
		List<EmergencyModel> oks = new ArrayList<EmergencyModel>();
		List<EmergencyModel> errors = new ArrayList<EmergencyModel>();
		//得到物流路径信息
		List<LingjGongys> lingjGongys = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anx.queryLingjGongysByEmergencyModel");
		List<Lingj> lingjs = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anx.queryLingjsByEmergencyModel");
		List<Gongys> ckxGongys = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anx.queryGongysByEmergencyModel");
		Map<String, LingjGongys> lingjGysMap = translateListToMap(lingjGongys, new String[]{"usercenter","lingjbh","gongysbh"});
		Map<String, Lingj> lingjMap = translateListToMap2(lingjs, new String[]{"usercenter","lingjbh"});
		Map<String, Gongys> ckxGysMap = translateListToMap3(ckxGongys,new String[]{"usercenter","gcbh"});
		for (String key : kyes) 
		{
			List<EmergencyModel> values = gongysMap.get(key);
			List<Dingdmx> dingdmxs = new ArrayList<Dingdmx>();
			for (EmergencyModel emergencyModel : values)
			{
				//将消耗时间translation
				emergencyModel.setEditor(username);
				emergencyModel.setXiaoh_time(emergencyModel.getXiaoh_time().replaceAll("_", " "));
				//消耗时间是否大于当前时间
				if(compare(getAllCurrTime(),emergencyModel.getXiaoh_time()))
				{
					//生成订单明细
					Object obj = translateDingdmxByEmergencyModel(emergencyModel,ckxGysMap,lingjGysMap,lingjMap,username);
					//如果订单明细创建失败，备注失败信息
					if(obj instanceof String)
					{
						emergencyModel.setBeiz((String)obj);
						emergencyModel.setZhuangt("3");
						errors.add(emergencyModel);
					}
					//成功归集订单明细
					else if(obj instanceof Dingdmx)
					{
						dingdmxs.add((Dingdmx)obj);
						oks.add(emergencyModel);
					}
				}
				else
				{
					emergencyModel.setBeiz("消耗时间小于当前时间!");
					emergencyModel.setZhuangt("3");
					errors.add(emergencyModel);
				}
			}
			if(dingdmxs.size() > 0)
			{
				//创建订单
				Dingd dingd = createDingd(key,username);
				//创建订单明细
				//修改紧急件订单信息
				if(null != dingd)
				{
					for (Dingdmx dingdmx : dingdmxs)
					{
						dingdmx.setDingdh(dingd.getDingdh());
					}
					//批量插入订单明细
					this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch("anx.insertDingdmxByEmergencyModel", dingdmxs);
					for (EmergencyModel emergencyModel : oks)
					{
						emergencyModel.setDingdh(dingd.getDingdh());
						emergencyModel.setZhuangt("1");
						emergencyModel.setBeiz("报警件处理完成!");
					}
				}
			}
			//修改状态
			this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch("anx.editEmergencyById", values);
		}
		if(errors.size() > 0)
		{
			resule.append("但是有部分数据因某些原因无法生成临时订单.");
		}
		return resule.toString();		
	}

	

	
	//创建订单
	private Dingd createDingd(String key, String username) 
	{
		Dingd dingd = null;
		String time = CommonFun.getJavaTime();
		String[] flags = key.split("_");
		String usercenter = flags[0];
		String gongysbh = flags[1];
		String dingdh = null;
		dingdh = createDingdh(usercenter,gongysbh);
		if(StringUtils.isNotBlank(dingdh))
		{
			dingd = new Dingd();
			dingd.setDingdh(dingdh);
			dingd.setUsercenter(usercenter);
			dingd.setCreator(username);
			dingd.setActive(Const.ACTIVE_1);
			dingd.setBeiz("报警件生成的CD模式临时订单!");
			dingd.setChullx(Const.ANX_MS_CD);
			dingd.setCreate_time(time);
			dingd.setDingdjssj(time.substring(0, 19));
			dingd.setDingdlx(Const.DINGDLX_LINS);
			dingd.setGongysdm(gongysbh);
			dingd.setDingdzt(Const.DINGD_STATUS_YSX);
			dingd.setShiffsgys(Const.SHIFFSGYS_YES);
			dingd.setEdit_time(time);
			dingd.setEditor(username);
			if(!dingdService.doInsert(dingd))
			{
				dingd = null;
			}
		}
		return dingd;
	}

	//生成订单号
	private String createDingdh(String usercenter,String gongysbh) 
	{
		String liushStr = "";
		// 获取到年
		String year = CommonFun.getDate(Const.YEARY);
		// 获取到月份
		String month0 = CommonFun.getDate(Const.MONTH);
		// 获取到日
		String day = CommonFun.getDate(Const.DAY);
		// 转换得到16进制的月份
		String month = Integer.toHexString(Integer.valueOf(month0)).toUpperCase();
		// 实例化年型表
		Nianxb nianx = new Nianxb();
		// 将截取到的年份set到年型的实体中
		nianx.setNianf(year);
		nianx.setUsercenter(usercenter);
		// 调用获取年型的方法，返回一个实体
		nianx = this.nianxbService.getNian(nianx);
		// 通过获取到的年型的实体，得到需要的年型
		year = nianx.getNianx();
		CommonFun.logger.debug("获取订单号getOrderNumber方法中year="+year);
		// 将各种形式放到map中
		Map<String, String> map = CommonFun.getPartten(year, month, day);
		// 得到部分订单号
		String orderNumber = map.get(Const.BJ);
		// 由部分订单号去订单明细总去匹配查询
		List<Dingd> list = this.dingdService.queryOrderNumbers(orderNumber);
		if (list.isEmpty())
		{
			// 生成最终的订单号
			orderNumber += Const.LSH_1;
		}
		else
		{
			// 定义订单号的模板
			DecimalFormat data = new DecimalFormat(Const.LSH_0);
			//数据库中流水号值
			int dataLiush = Integer.valueOf(list.get(0).getDingdh()
					.substring(5, list.get(0).getDingdh().length())) + 1;
			liushStr = data.format(dataLiush);
			// 若得到的结果不为空，则表示已经存在，则在流水号上加1
			orderNumber = orderNumber + liushStr;
		}
		return orderNumber;
	}

	//转换成订单mx
	private Object translateDingdmxByEmergencyModel(EmergencyModel emergencyModel,Map<String, Gongys> ckxGysMap,Map<String, LingjGongys> lingjGysMap,Map<String, Lingj> lingjMap,String username) throws ParseException 
	{
		
		String time = CommonFun.getJavaTime();
		Lingj lingj = lingjMap.get(emergencyModel.getUsercenter() + emergencyModel.getLingjbh());
		if(null == lingj)
		{
			return "未找到零件信息!";
		}
		Gongys gongys = ckxGysMap.get(emergencyModel.getUsercenter() + emergencyModel.getGongysbh());
		if(null == gongys)
		{
			return "未找到供应商信息!";
		}
		LingjGongys lingjGongys = lingjGysMap.get(emergencyModel.getUsercenter() + emergencyModel.getLingjbh() + emergencyModel.getGongysbh());
		if(null == lingjGongys)
		{
			return "零件与供应商不能对应,未找到零件供应商信息!";
		}
		Dingdmx dingdmx = new Dingdmx();
		dingdmx.setActive(Const.ACTIVE_1);
		dingdmx.setUsercenter(emergencyModel.getUsercenter());
		dingdmx.setGongysdm(gongys.getGcbh());
		emergencyModel.setGongysbh(gongys.getGcbh());
		dingdmx.setLingjbh(emergencyModel.getLingjbh());
		dingdmx.setLingjmc(lingj.getZhongwmc());
		dingdmx.setDanw(lingj.getDanw());
		dingdmx.setJihyz(lingj.getJihy());
		dingdmx.setFahd(lingjGongys.getFayd());
		dingdmx.setUabzlx(lingjGongys.getUabzlx());
		dingdmx.setUabzrl(emergencyModel.getUarl());
		dingdmx.setUabzucrl(lingjGongys.getUcrl());
		dingdmx.setUabzuclx(lingjGongys.getUcbzlx());
		dingdmx.setUabzucsl(lingjGongys.getUaucgs());
		dingdmx.setGongyfe(lingjGongys.getGongyfe());
		dingdmx.setDinghcj(lingj.getDinghcj());
		dingdmx.setShul(emergencyModel.getYaohsl());
		dingdmx.setJissl(emergencyModel.getYaohsl());
		dingdmx.setZhuangt(Const.DINGD_STATUS_YSX);
		dingdmx.setCreator(username);
		dingdmx.setEditor(username);
		dingdmx.setCreate_time(time);
		dingdmx.setEdit_time(time);
		dingdmx.setGongyslx(gongys.getLeix());
		dingdmx.setGongsmc(gongys.getGongsmc());
		dingdmx.setNeibyhzx(gongys.getUsercenter());
		dingdmx.setXiaohd(emergencyModel.getXiaohd());
		dingdmx.setXiaohsj(emergencyModel.getXiaoh_time());
		dingdmx.setGonghlx(Const.ANX_MS_CD);
		return createDingdmx(dingdmx,emergencyModel);
	}

	private Object createDingdmx(Dingdmx bean,EmergencyModel emergencyModel) throws ParseException
	{
		String beihsj = null;
		String gcbh = null;
		Wullj wullj=null;
		Map<String, String> map = new HashMap<String, String>();
		map.put("usercenter", bean.getUsercenter());
		map.put("lingjbh", bean.getLingjbh());
		map.put("xiaohdbh", bean.getXiaohd());
		map.put("gongysbh", bean.getGongysdm());
		Lingjxhd lingjxhd = lingjxhdService.queryLingjxhdObject(map);
		// 如果小火车时刻表没有数据,跳过该条记录,不予计算
		if (lingjxhd == null) {
			return "未找到该消耗点";
		}
		if(StringUtils.isEmpty(lingjxhd.getXiaohcbh())){
			return "小火车编号为空";
		}
		bean.setFenpxh(bean.getXiaohd().substring(0, 5));
		bean.setXianbyhlx(lingjxhd.getXianbyhlx());
		map.put("xiaohcbh", lingjxhd.getXiaohcbh());
		map.put("shengcxbh", lingjxhd.getShengcxbh());
		map.put("chufsxsj", bean.getXiaohsj().substring(0, 16));
		bean.setXiaohch(lingjxhd.getXiaohcbh());
		bean.setChanx(lingjxhd.getShengcxbh());
		// 取小火车运输时刻
		List<Xiaohcyssk> Listxhcyssk = xService.queryXiaohcysskObjectByShangx(map);
		// 如果小火车时刻表没有数据,跳过该条记录,不予计算
		if (Listxhcyssk == null || Listxhcyssk.isEmpty()) {
			return "未查到相关小火车信息";
		}
		Xiaohcyssk xiaohcyssk = Listxhcyssk.get(1);
		// 备货时间
		beihsj = xiaohcyssk.getKaisbhsj();
		bean.setXiaohcbhsj(beihsj);
		// 上线时间
		String shangxsj = xiaohcyssk.getChufsxsj();
		bean.setXiaohcsxsj(shangxsj.substring(0, 16));
		bean.setTangc(xiaohcyssk.getTangc());//小火车运输时刻趟次
		bean.setXiaohch(lingjxhd.getXiaohcbh());//小火车编号
		bean.setXiaohccxh(lingjxhd.getXiaohccxbh());//小火车车厢编号
		if (beihsj == null) {
			return "小火车运输时刻,小火车编号" + lingjxhd.getXiaohcbh() + "生产线编号" + lingjxhd.getShengcxbh() + "备货时间为空";
		}
		map.put("fenpqh", bean.getXiaohd().substring(0, 5));
		map.put("waibms", bean.getGonghlx());
		wullj= wulljService.queryWulljObject(map);
		gcbh = wullj == null ? null : wullj.getGcbh();
		bean.setCangkdm(wullj == null ? null : wullj.getMudd());
		bean.setBeihsj2(wullj == null ? null : wullj.getBeihsj());//备货时间
		if (wullj == null) {
			return "未在物流路径外部模式为CD中找到相关记录.";
		}
		bean.setGcbh(gcbh);
		emergencyModel.setChengysbh(gcbh);
		bean.setXiehzt(wullj.getXiehztbh());
		bean.setLujdm(wullj.getLujbh());
		bean.setLeix("9");
		BigDecimal neibwlsj = wullj.getBeihsjc();
		// 如果备货时间为空
		if (neibwlsj == null) {
			return "内部物流时间C为空";
		}
		/**
		 * 获取到最晚到货时间
		 * */
		map.put("juedsk", beihsj);
		map.put("shijNum", CommonFun.strNull(neibwlsj.intValue()));//内部物流时间
		//预计到货时间=时间(CDMD为小火车备货时间,C1M1为消耗时间)-内部物流时间
		String zuiwdhsj = CommonFun.strNull(anxMaoxqService.queryGongzsjmbQ(map));
		map.put("usercenter", bean.getUsercenter());
		map.put("xiehztbh", wullj.getXiehztbh());
		
		map.put("gcbh", gcbh);
		map.put("daohsj", zuiwdhsj);
		/**
		 * 从卸货站台得到提前到货时间
		 */
		Xiehzt xiehzt = anxMaoxqService.queryXiehztObject(map);
		BigDecimal yunxtqdhsj = xiehzt.getYunxtqdhsj();
		if (yunxtqdhsj == null) {
			return "卸货站台允许提前到货时间";
		}
		map.put("xiehztbh", wullj.getXiehztbh().substring(0,4));
		List<Yunssk> listYunssk = (List<Yunssk>) anxMaoxqService.queryYunssk(map);
		if (listYunssk != null && !listYunssk.isEmpty()) {
			zuiwdhsj = listYunssk.get(0).getDaohsj().substring(0, 16);
		}
		//预计到货时间=时间(CDMD为小火车备货时间,C1M1为消耗时间)-内部物流时间
		map.put("juedsk", zuiwdhsj);
		map.put("shijNum", CommonFun.strNull(yunxtqdhsj.intValue()));//允许提前到货时间

		// 最早到货时间
		String zuizdhsj = CommonFun.strNull(anxMaoxqService.queryGongzsjmbQ(map));
		bean.setZuizdhsj(zuizdhsj);
		bean.setZuiwdhsj(zuiwdhsj);	
		Date date = CommonFun.yyyyMMddHHmm.parse(bean.getZuiwdhsj());//设置为最晚到货时间
		//发运时间 = 最晚到货时间 - 运输周期
		date.setTime(date.getTime() - CommonFun.getBigDecimal(wullj.getYunszq()).multiply(new BigDecimal(24))
				.multiply(new BigDecimal(60)).multiply(new BigDecimal(60)).multiply(new BigDecimal(1000)).intValue());
		bean.setFaysj(CommonFun.yyyyMMddHHmm.format(date));
		bean.setJiaofrq(bean.getZuiwdhsj());
		return bean;
	}

	private boolean checkEmergencyModel(EmergencyModel emergencyModel)
	{
		
		if(null == emergencyModel || StringUtils.isBlank(emergencyModel.getId()) || StringUtils.isBlank(emergencyModel.getLingjbh())
		    || StringUtils.isBlank(emergencyModel.getLingjbh()) || StringUtils.isBlank(emergencyModel.getUsercenter()) ||
		    StringUtils.isBlank(emergencyModel.getChengysbh()) || null == emergencyModel.getYaohsl() || StringUtils.isBlank(emergencyModel.getXiaoh_time())
		    || StringUtils.isBlank(emergencyModel.getXiaohd())
		)
			return false;
		return true;
	}
	
	
	/**
	 * @see 根据参数列得到的值作为Key 将List 转换为Map
	 * @param kucfxs
	 * @param propertys
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	private Map<String,LingjGongys> translateListToMap(List<LingjGongys> lingjGongys,String... propertys) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
	{
		Map<String, LingjGongys> result = new HashMap<String, LingjGongys>();
		LingjGongys gongys = null;
		if(null != lingjGongys && lingjGongys.size() > 0 && null != propertys && propertys.length > 0)
		{
			for (int i=0,j=lingjGongys.size();i<j;i++) 
			{
				gongys = lingjGongys.get(i);
				StringBuffer flagKey = new StringBuffer();
				for (int k = 0,p = propertys.length; k < p; k++)
				{
					flagKey.append(BeanUtils.getProperty(gongys, propertys[k]));
				}
				result.put(flagKey.toString(), gongys);
			}
		}
		return result;
	}
	
	private Map<String, Lingj> translateListToMap2(List<Lingj> lingjs,String... propertys) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException 
	{
		Map<String, Lingj> result = new HashMap<String, Lingj>();
		Lingj lingj = null;
		if(null != lingjs && lingjs.size() > 0 && null != propertys && propertys.length > 0)
		{
			for (int i=0,j=lingjs.size();i<j;i++) 
			{
				lingj = lingjs.get(i);
				StringBuffer flagKey = new StringBuffer();
				for (int k = 0,p = propertys.length; k < p; k++)
				{
					flagKey.append(BeanUtils.getProperty(lingj, propertys[k]));
				}
				result.put(flagKey.toString(), lingj);
			}
		}
		return result;
	}
	
	private Map<String, Gongys> translateListToMap3(List<Gongys> gongys,String... propertys)throws IllegalAccessException, InvocationTargetException, NoSuchMethodException
	{
		Map<String, Gongys> result = new HashMap<String, Gongys>();
		Gongys gongy = null;
		if(null != gongys && gongys.size() > 0 && null != propertys && propertys.length > 0)
		{
			for (int i=0,j=gongys.size();i<j;i++) 
			{
				gongy = gongys.get(i);
				StringBuffer flagKey = new StringBuffer();
				for (int k = 0,p = propertys.length; k < p; k++)
				{
					flagKey.append(BeanUtils.getProperty(gongy, propertys[k]));
				}
				result.put(flagKey.toString(), gongy);
			}
		}
		return result;
	}
	
	
  public  boolean compare(String beforeDate,String afterDate){
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	boolean flag=false;
	try {
		flag=formatter.parse(beforeDate).before(formatter.parse(afterDate));
	} catch (ParseException e) {
		flag=false;
	}
	return flag;
  }
	  
  public static String getAllCurrTime()
  {
	  Date date = new Date();
      Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      return formatter.format(date);
  }

  public void updateEmergency(EmergencyModel emergencyModel)
  {
	  if(null != emergencyModel && null != emergencyModel.getId())
      {
		this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("anx.editEmergencyById", emergencyModel);
	  }
  }
}
