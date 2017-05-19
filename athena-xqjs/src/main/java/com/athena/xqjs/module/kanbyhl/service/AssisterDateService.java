package com.athena.xqjs.module.kanbyhl.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.common.Gongyzq;
import com.athena.xqjs.entity.kanbyhl.Kanbxhgm;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.ilorder.service.GongyzqService;
import com.athena.xqjs.module.ycbj.service.YicbjService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;
/**
 * <p>
 * 看板规模计算service
 * </p>
 * @author Niesy
 * 2015-04-27		gswang			0011283: 看板在做失效和消耗点替换时，准备层发给变更记录表2次
 *
 * 
 */
@SuppressWarnings("rawtypes")
@Component
public class AssisterDateService extends BaseService {
	@Inject
	private GongyzqService gongyzqService;

	@Inject
	private YicbjService yicbjService;

	/**
	 * log4j日志打印
	 */
	private final Logger log = Logger.getLogger(KanbjsService.class);
	/**
	 * 需求类型为周期, 计算周期开始时间和结束时间
	 * 计算日后推2天，做为滚动月开始日期，
	 * 开始日期的月份加1后的日期前推1天为滚动月结束日
	 */
	public Map<String, String> cycleJudge() {
		Map<String, String> dateMap = new HashMap<String, String>();
		// 周期起始时间与结束时间
		// 实力化Calendar类对象
		Calendar calendar = new GregorianCalendar();
		// 设置日期格式
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		// 让日期加2
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 2);
		// 计算日期即开始日期
		String jsDate = sf.format(calendar.getTime());
		// 月份加1后日期往前推1天即为计算结束日期
		// 月份加1
		//calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 20);
		// 让日期加-1
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 1);
		// 计算结束日期
		String jsEnd = sf.format(calendar.getTime());
		// 判断计算日期是否跨月
		Gongyzq gyzq1 = gongyzqService.queryGongyzqbyRq(jsDate);
		Gongyzq gyzq2 = gongyzqService.queryGongyzqbyRq(jsEnd);
		// 异常报警信息
		String lingjbh = "";
		String jismk = Const.JISMK_kANB_CD;
		String cuowlx = Const.CUOWLX_200;
		String cuowxxxx = "工业周期表未找到日期" + jsDate + "的工业周期";
		if (gyzq1 == null) {
			yicbjService.saveYicInfo(jismk, lingjbh, cuowlx, cuowxxxx);
			dateMap.put("message", "工业周期表数据缺失.");
			return dateMap;
		}
		if (gyzq2 == null) {
			cuowxxxx = "工业周期表未找到日期" + jsEnd + "的工业周期";
			yicbjService.saveYicInfo(jismk, lingjbh, cuowlx, cuowxxxx);
			dateMap.put("message", "工业周期表数据缺失.");
			return dateMap;
		}
		String startMonth = gyzq1.getGongyzq();
		String endMonth = gyzq2.getGongyzq();

		if (startMonth.compareTo(endMonth) != 0) {
			if (gyzq1.getKaissj() == null || gyzq1.getJiessj() == null) {
				cuowxxxx = "工业周期表工业周期" + startMonth + "数据有误";
				yicbjService.saveYicInfo(jismk, lingjbh, cuowlx, cuowxxxx);
			}
			if (gyzq2.getKaissj() == null || gyzq2.getJiessj() == null) {
				cuowxxxx = "工业周期表工业周期" + endMonth + "数据有误";
				yicbjService.saveYicInfo(jismk, lingjbh, cuowlx, cuowxxxx);
			}
			// 跨月
			// 需求所属周期2
			dateMap.put("xuqsszq2", endMonth);
			// 结束月开始日期
			String retStart = gyzq2.getKaissj();
			// 看板计算开始日期所在月的结束日期
			String retEnd = gyzq1.getJiessj();
			// 将计算开始日期所在月的开始日期存进Map
			dateMap.put("monthStartofDate", gyzq1.getKaissj());
			// 将计算开始日期所在月的结束日期存进Map
			dateMap.put("retEnd", retEnd);
			// 将计算结束日期所在月的开始日期存进Map
			dateMap.put("retStart", retStart);
			// 将计算结束日期所在月的结束日期存进Map
			dateMap.put("monthEndofDate", gyzq2.getJiessj());
		}
		// 需求所属周期1
		dateMap.put("xuqsszq", startMonth);
		// 将计算开始日期存进Map
		dateMap.put(Const.JSDATE, jsDate);
		// 将计算结束日期存进Map
		dateMap.put(Const.JSEND, jsEnd);

		return dateMap;
	}

	// TODO 失效失效消耗对应的看板循环、零件仓库切换的原仓库对应的看板循环
	/**
	 * 根据失效的零件消耗点和变更记录中的原编号失效看板循环
	 */
	@Transactional
	@SuppressWarnings("unchecked")
	public void disabledKbxh(String usercenter,String creator,List<Map<String, String>> bjshix) {
//		Map<String, String> kbmap = queryAllKbxh();
//		Map<String, String> kbmap = new HashMap<String, String>();
		//用于存相同客户点对应的多模式的数据
		Map<String , List<String>> listKbMap = new HashMap<String, List<String>>();
		//查询看板（带模式）
		List<Map<String, String>> mapls = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select(
				"kanbyhl.queryAllKbgmMs");
		for (int i = mapls.size() - 1; i >= 0; i--) {
//			kbmap.put(mapls.get(i).get("USERCENTER") + mapls.get(i).get("LINGJBH") + mapls.get(i).get("GONGHMS")
//					+ mapls.get(i).get("KEHD"), mapls.get(i).get("XUNHBM"));
//			mapls.remove(i);
			String key = mapls.get(i).get("USERCENTER") + mapls.get(i).get("LINGJBH")+ mapls.get(i).get("KEHD");
			String value = mapls.get(i).get("XUNHBM");
			List<String> list ;
			if(listKbMap.containsKey(key)){
				list = listKbMap.get(key);
			}else{
				list = new ArrayList<String>();
			}
			list.add(value);
			listKbMap.put(key, list);
			mapls.remove(i);
		}
		//查询所有失效的零件消耗点，返回结果格式：用户中心+零件+消耗点
		List<String> sxls = new ArrayList<String>();
		//查询所有变更记录表：返回结果格式：用户中心+零件+原编号（仓库，消耗点）
//		sxls.addAll(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("common.queryBgjlForck"));
		//查询所有变更记录表（未使用过得）：返回结果格式：用户中心+零件+原编号（仓库，消耗点）
		sxls.addAll(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("common.queryBgjlSyForckNo",usercenter));
		List<Map<String, String>> upList = new ArrayList<Map<String, String>>();
		for (int i = 0; i < sxls.size(); i++) {
			if (listKbMap.containsKey(sxls.get(i))) {
				List<String> listKb = listKbMap.get(sxls.get(i));
				for (String kbbm : listKb) {
					Map<String, String> param = new HashMap<String, String>();
					param.put("xunhbm", kbbm);
					param.put("shengxzt", Const.STRING_TWO);
					param.put("weihr", creator);
					param.put("editor", creator);
					List<Kanbxhgm> kbxh =  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("kanbyhl.queryKbgmbyxhBh",param);
					if(kbxh !=null && kbxh.size()>0){
						Kanbxhgm kbxhBean = kbxh.get(0);
						if(!"2".equals(kbxhBean.getShengxzt())){
							bjshix.add(param);
						}
					}
					upList.add(param);
				}
			}
		}
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("kanbyhl.updateXiafgmSx", upList);
		//gswang 2015-05-11 查询失效零件消耗点时，需要按照用户中心查询失效，所有用户中心失效运行uw后会影响ul的数据。
		sxls = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("kanbyhl.queryAllxhdUsercenter",
				usercenter);
		List<Map<String, String>> bgupList = new ArrayList<Map<String, String>>();
		for (int i = 0; i < sxls.size(); i++) {
			if (listKbMap.containsKey(sxls.get(i))) {
				List<String> listKb = listKbMap.get(sxls.get(i));
				for (String kbbm : listKb) {
					Map<String, String> param = new HashMap<String, String>();
					param.put("xunhbm", kbbm);
					param.put("shengxzt", Const.STRING_TWO);
					param.put("weihr", creator);
					param.put("editor", creator);
					bgupList.add(param);
				}
			}
		}		
		sxls = null;
		listKbMap = null;
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("kanbyhl.updateXiafgmSx", bgupList);
		
	}

	// TODO 生效 切换新消耗点，和新仓库对应的对应的看板循环
	
	/**
	 * 将变更记录表中的原编号的下发循环规模赋值给先编号的循环并生效
	 */
	@Transactional
	@SuppressWarnings("unchecked")
	public void enabledKbxh(String usercenter,String creator,List<Map<String, String>> bjshix) {
//		Map<String, String> kbmap = queryAllKbxh();
		Map<String, String> kbmap = new HashMap<String, String>();
		List<Map<String, String>> mapls = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select(
				"kanbyhl.queryAllKbgmMs");
		for (int i = mapls.size() - 1; i >= 0; i--) {
			kbmap.put(mapls.get(i).get("USERCENTER") + mapls.get(i).get("LINGJBH") 
					+ mapls.get(i).get("KEHD")+ mapls.get(i).get("GONGHMS"), mapls.get(i).get("XUNHBM"));
			mapls.remove(i);
		}

		//获取变更记录表的原编号对应的下发循环规模赋值给现循环规模
		List<Map<String,Object>> bgjlXfxhgmList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("common.queryBgjlSyForckSX",usercenter);
		//记录需要替换的看板循环
		List<Map<String, String>> upList = new ArrayList<Map<String, String>>();
		//记录原编号应该赋值给现编号的下发循环规模和冻结状态，（状态继承）
		Map<String,String> MapBean = null;
		//记录编号的状态（0 ：失效，1：生效）
		Map<String,String> mapBiaos = new HashMap<String, String>(); 
		
		//记录现编号，原编号
		Map<String,String> xianYuan = new HashMap<String, String>(); 
		
		//记录MapBean 对象
		Map<String,Map<String,String>> mapKey = new HashMap<String, Map<String,String>>(); 
		for (int j = bgjlXfxhgmList.size()-1;j>=0;j-- ) {
			Map<String, Object> map = bgjlXfxhgmList.get(j);
			if("2".equals(map.get("XSHENGXZT"))){
				String yuan = StringUtils.trimToEmpty(map.get("YUANBH")+"");
				if (kbmap.containsKey(yuan)) {
					Map<String, String> lost = new HashMap<String, String>();
					lost.put("xunhbm", kbmap.get(yuan));
					lost.put("shengxzt", Const.STRING_ZERO);
					lost.put("weihr", creator);
					lost.put("editor", creator);
					bgjlXfxhgmList.remove(j);
					continue;
				}
			}
			MapBean = new HashMap<String, String>();
			map.put("CREATOR", creator);
			//现编号    用户中心+零件+现编号
			String key = StringUtils.trimToEmpty(map.get("ULX")+"");
			//原编号对应的下发循环规模
			String xiafxhgm = StringUtils.trimToEmpty(map.get("XIAFXHGM")+"");
			//原编号   用户中心+零件+原编号
			String yuanKey = StringUtils.trimToEmpty(map.get("YUANBH")+"");
			//冻结状态的继承
			String dongjjdzt = StringUtils.trimToEmpty(map.get("DONGJJDZT")+"");
			//设置编号对应的状态   最后只取状态为生效的编号去生效
			mapBiaos.put(key, "1");
			mapBiaos.put(yuanKey, "0");
			xianYuan.put(key, yuanKey);
			//记录原编码对应的下发循环规模和冻结状态
			MapBean.put("XIAFXHGM",xiafxhgm);
			MapBean.put("DONGJJDZT", dongjjdzt);
			//如果存在 原编号对应的现编号下的下发循环规模，则取该下发循环规模为现
			if(mapKey.containsKey(yuanKey)){
				mapKey.put(key, mapKey.get(yuanKey));				
			}else{
				//如果不存在，则取原编号对应的下发循环规模
				mapKey.put(key, MapBean);
			}
		}
		for (String key : mapBiaos.keySet()) {
			if("0".equals(mapBiaos.get(key))){
				//如果状态为失效 ，则删除该数据
				mapKey.remove(key);
			}else{
				//如果状态为生效，则给下发循环规模赋值，并生效
				if (kbmap.containsKey(key)) {
					Map<String, String> param = new HashMap<String, String>();
					String xunhbm = kbmap.get(key);
					param.put("xunhbm", xunhbm);
					param.put("shengxzt", Const.STRING_ONE);
					param.put("xiafxhgm", StringUtils.defaultIfEmpty(mapKey.get(key).get("XIAFXHGM"), "0"));
					param.put("dongjjdzt", StringUtils.defaultIfEmpty(mapKey.get(key).get("DONGJJDZT"), "0"));
					param.put("weihr", creator);
					param.put("editor", creator);
					upList.add(param);
					String yuanbh = kbmap.get(xianYuan.get(key));
					if(yuanbh!=null){
						for(int t = bjshix.size() -1;t >=0;t--){
							Map<String, String> sxxh = bjshix.get(t);
							if(yuanbh.equals(sxxh.get("xunhbm"))){
								bjshix.remove(t);
							}
						}
					}
				}else{
					String yuanbh = xianYuan.get(key);
					for(int t = bgjlXfxhgmList.size() -1;t >=0;t--){
						Map<String, Object> bgjl = bgjlXfxhgmList.get(t);
						String yuanKey = StringUtils.trimToEmpty(bgjl.get("YUANBH")+"");
						if(yuanbh.equals(yuanKey)){
							bgjlXfxhgmList.remove(t);
						}
					}
					log.error("根据零件消耗点和零件仓库的替换记录，未找到"+key+"的循环编码");
				}
			}
		}
		mapKey = null;
		mapBiaos = null;
		if(upList.size()>0){
			log.info("根据零件消耗点和零件仓库的替换记录，共"+bgjlXfxhgmList.size()+"条记录，"+upList.size()+"条数据执行了看板循环的继承");
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS)
			.executeBatch("kanbyhl.updateXiafgmSx", upList);
		}else{
			log.info("零件消耗点和零件仓库没有替换记录，没有执行看板循环的继承");
		}
		//更新变更记录表的状态为已使用
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS)
		.executeBatch("common.updateShifsy", bgjlXfxhgmList);
		
		for( Map<String, String> bjshixMap:bjshix){
			bjshixMap.put("shengxzt", Const.STRING_ZERO);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("kanbyhl.updateXiafgmSx", bjshixMap);
		}

	}
	

	/**
	 * 查询所有状态的循环
	 * @return map:key=USERCENTER+LINGJBH+KEHD,value=XUNHBM
	 */
	@SuppressWarnings("unchecked")
	private Map<String, String> queryAllKbxh() {
		Map<String, String> kbmap = new HashMap<String, String>();
		//查询所有状态的循环
		List<Map<String, String>> mapls = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select(
				"kanbyhl.queryAllKbgmR");
		for (int i = mapls.size() - 1; i >= 0; i--) {
			kbmap.put(mapls.get(i).get("USERCENTER") + mapls.get(i).get("LINGJBH") + mapls.get(i).get("KEHD"), mapls
					.get(i).get("XUNHBM"));
			mapls.remove(i);
		}
		//map:key=USERCENTER+LINGJBH+KEHD,value=XUNHBM
		return kbmap;
	}
	
	// TODO 模式切换，更新模式切换中间表   9642599580
	@Transactional
	@SuppressWarnings("unchecked")
	public void changeMode(String usercenter,String creator) {
		Map<String, String> kbmap = new HashMap<String, String>();
		List<Map<String, String>> mapls = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select(
				"kanbyhl.queryAllKbgmMs");
		for (int i = mapls.size() - 1; i >= 0; i--) {
			kbmap.put(mapls.get(i).get("USERCENTER") + mapls.get(i).get("LINGJBH") + mapls.get(i).get("GONGHMS")
					+ mapls.get(i).get("KEHD"), mapls.get(i).get("XUNHBM"));
			mapls.remove(i);
		}
		Map<String, String> param = new HashMap<String, String>();
		List<Map<String, String>> updatelsKbxh = new ArrayList<Map<String, String>>();
		List<Map<String, String>> updatelsKbxh3 = new ArrayList<Map<String, String>>();
		param.put("shifkbqh", Const.STRING_ZERO);
		param.put("usercenter", usercenter);
		// param.put("zhuangt", Const.STRING_ONE);
		// 查询模式切换中间表：其他模式切看板0
		mapls = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("kanbyhl.queryMsqhb", param);
		// USERCENTER+LINGJBH+MOS+KEH(CANGKBH/XIAOHD)+MUDDLX
		String editor = creator;
		String edit_time = CommonFun.getJavaTime();
		for (int i = 0; i < mapls.size(); i++) {
			String kehd = "";
			Map<String, String> paramKb = new HashMap<String, String>();
			if(mapls.get(i).get("MOS").contains("R")){
				if (mapls.get(i).get("MOS").equals(Const.KANB_JS_WAIBMOS_R1)
						|| mapls.get(i).get("MOS").equals(Const.KANB_JS_NEIBMOS_RD)) {
					kehd = mapls.get(i).get("XIAOHD");
				} else if(mapls.get(i).get("MOS").equals(Const.KANB_JS_NEIBMOS_RM)
						|| mapls.get(i).get("MOS").equals(Const.KANB_JS_WAIBMOS_R2)){
					kehd = mapls.get(i).get("CANGKBH");
				}
				paramKb.put("xunhbm",
						kbmap.get(mapls.get(i).get("USERCENTER") + mapls.get(i).get("LINGJBH") + mapls.get(i).get("MOS")
								+ kehd));
				
			}
			mapls.get(i).put("xunhbm",
					kbmap.get(mapls.get(i).get("USERCENTER") + mapls.get(i).get("LINGJBH") + mapls.get(i).get("MOS")
							+ kehd));		
			mapls.get(i).put("editor", editor);
			mapls.get(i).put("edit_time", edit_time);
			
			paramKb.put("leix", Const.STRING_THREE);
			// paramKb.put("shengxzt", Const.STRING_ZERO);
			paramKb.put("jianglms", Const.STRING_ZERO);
			paramKb.put("editor", editor);
			paramKb.put("edit_time", edit_time);
			paramKb.put("weihr", editor);
			paramKb.put("weihsj", edit_time);
			updatelsKbxh.add(paramKb);
		}
		// 更新中间表的XUNHBM，更新看板表的类型为3
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("kanbyhl.updateMsbh", mapls);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("kanbyhl.updateXiafgmSx", updatelsKbxh);
		// 清空
		mapls.clear();
		updatelsKbxh.clear();
		
		// 查询模式切换中间表：看板模式切其他：2   0008902   2013-11-21
		param.put("shifkbqh",Const.STRING_TWO);
		mapls = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("kanbyhl.queryMsqhb", param);
		// USERCENTER+LINGJBH+XUNHBM+CANGKBH/XIAOHD+YUANMS+MOS+MUDDLX
		for (int i = 0; i < mapls.size(); i++) {
			String kehd = "";
			Map<String, String> paramKb = new HashMap<String, String>();
			if (mapls.get(i).get("YUANMS").equals(Const.KANB_JS_WAIBMOS_R1)
					|| mapls.get(i).get("YUANMS").equals(Const.KANB_JS_NEIBMOS_RD)) {
				kehd = mapls.get(i).get("XIAOHD");
			} else if (mapls.get(i).get("YUANMS").equals(Const.KANB_JS_NEIBMOS_RM)
					|| mapls.get(i).get("YUANMS").equals(Const.KANB_JS_WAIBMOS_R2)) {
				kehd = mapls.get(i).get("CANGKBH");
			}
			mapls.get(i).put("yuanxhbm",
					kbmap.get(mapls.get(i).get("USERCENTER")+ mapls.get(i).get("LINGJBH")+ mapls.get(i).get("YUANMS") + kehd));
			paramKb.put("xunhbm",
					kbmap.get(mapls.get(i).get("USERCENTER")+ mapls.get(i).get("LINGJBH")+ mapls.get(i).get("YUANMS") + kehd));
			paramKb.put("shengxzt", Const.STRING_TWO);

//			mapls.get(i).put("xunhbm",
//					kbmap.get(mapls.get(i).get("USERCENTER") + mapls.get(i).get("LINGJBH") + mapls.get(i).get("MOS")
//							+ kehd));		
			mapls.get(i).put("editor", editor);
			mapls.get(i).put("edit_time", edit_time);
			
			paramKb.put("leix", Const.STRING_ONE);
			// paramKb.put("shengxzt", Const.STRING_ZERO);
//			paramKb.put("jianglms", Const.STRING_ZERO);
			paramKb.put("editor", editor);
			paramKb.put("edit_time", edit_time);
			paramKb.put("weihr", editor);
			paramKb.put("weihsj", edit_time);
			updatelsKbxh.add(paramKb);
		}
		// 更新中间表的XUNHBM、YUANMS，看板原模式生效状态为2
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("kanbyhl.updateMsbh", mapls);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("kanbyhl.updateXiafgmSx", updatelsKbxh);
		// 清空
		mapls.clear();
		updatelsKbxh.clear();
		// 查询模式切换中间表：看板模式切看板1
		param.put("shifkbqh", Const.STRING_ONE);
		mapls = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("kanbyhl.queryMsqhb", param);
		// USERCENTER+LINGJBH+XUNHBM+CANGKBH/XIAOHD+YUANMS+MOS+MUDDLX
		for (int i = 0; i < mapls.size(); i++) {
			String kehdOld = "";
			String kehdNew = "";
			if (mapls.get(i).get("MOS").equals(Const.KANB_JS_WAIBMOS_R1)
					&& mapls.get(i).get("YUANMS").equals(Const.KANB_JS_WAIBMOS_R2)) {
				kehdNew = mapls.get(i).get("XIAOHD");
				kehdOld = mapls.get(i).get("CANGKBH");
			} else if (mapls.get(i).get("MOS").equals(Const.KANB_JS_WAIBMOS_R1)
					&& mapls.get(i).get("YUANMS").equals(Const.KANB_JS_NEIBMOS_RD)) {
				kehdNew = mapls.get(i).get("XIAOHD");
				kehdOld = mapls.get(i).get("XIAOHD");
			} else if (mapls.get(i).get("MOS").equals(Const.KANB_JS_WAIBMOS_R2)
					&& mapls.get(i).get("YUANMS").equals(Const.KANB_JS_WAIBMOS_R1)) {
				kehdNew = mapls.get(i).get("CANGKBH");
				kehdOld = mapls.get(i).get("XIAOHD");
			} else if (mapls.get(i).get("MOS").equals(Const.KANB_JS_NEIBMOS_RD)
					&& mapls.get(i).get("YUANMS").equals(Const.KANB_JS_WAIBMOS_R1)) {
				kehdNew = mapls.get(i).get("XIAOHD");
				kehdOld = mapls.get(i).get("XIAOHD");
			}
			mapls.get(i).put("xunhbm",
					kbmap.get(mapls.get(i).get("USERCENTER") + mapls.get(i).get("LINGJBH") + mapls.get(i).get("MOS")
							+ kehdNew));
			mapls.get(i).put("yuanxhbm",
					kbmap.get(mapls.get(i).get("USERCENTER") + mapls.get(i).get("LINGJBH") + mapls.get(i).get("YUANMS")
							+ kehdOld));
			mapls.get(i).put("editor", editor);
			mapls.get(i).put("edit_time", edit_time);
			Map<String, String> paramsx = new HashMap<String, String>();
			Map<String, String> paramupleix = new HashMap<String, String>();
			// 更新新模式类型3
			paramupleix.put("xunhbm",
					kbmap.get(mapls.get(i).get("USERCENTER") + mapls.get(i).get("LINGJBH") + mapls.get(i).get("MOS")
							+ kehdNew));
			paramupleix.put("leix", Const.STRING_THREE);
			// paramupleix.put("shengxzt", Const.STRING_ZERO);
			paramupleix.put("jianglms", Const.STRING_ZERO);
			paramupleix.put("editor", editor);
			paramupleix.put("edit_time", edit_time);
			paramupleix.put("weihr", editor);
			paramupleix.put("weihsj", edit_time);
			// 失效原模式2
			paramsx.put("xunhbm",
					kbmap.get(mapls.get(i).get("USERCENTER") + mapls.get(i).get("LINGJBH") + mapls.get(i).get("YUANMS")
							+ kehdOld));
			paramsx.put("leix", Const.STRING_ONE);
			paramsx.put("shengxzt", Const.STRING_TWO);
			paramsx.put("editor", editor);
			paramsx.put("edit_time", edit_time);
			paramsx.put("weihr", editor);
			paramsx.put("weihsj", edit_time);
			updatelsKbxh.add(paramupleix);
			updatelsKbxh3.add(paramsx);
		}
		// 更新中间表的XUNHBM、YUANMS，更新看板表的类型为3，看板原模式生效状态为2
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("kanbyhl.updateMsbh", mapls);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("kanbyhl.updateXiafgmSx", updatelsKbxh);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("kanbyhl.updateXiafgmSx", updatelsKbxh3);
	}

	/**
	 * <p>
	 * 将来模式计算，RM/RD仓库循环时间去仓库循环时间表
	 * <p/>
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Map<String, Object>> getCangkxhsj(String usercenter, String mos) {
		Map<String, Map<String, Object>> cangkxhsjmap = new HashMap<String, Map<String, Object>>();
		Map<String, String> param = new HashMap<String, String>();
		param.put("usercenter", usercenter);
		param.put("mos", mos);
		param.put("shengxbs", Const.STRING_ONE);
		// 查询某用户中心下所有有效的仓库循环时间
		List<Map<String, Object>> mapls = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select(
				"kanbyhl.queryCangkxhsj", param);
		for (int i = 0; i < mapls.size(); i++) {
			cangkxhsjmap.put(mapls.get(i).get("USERCENTER").toString() + mapls.get(i).get("CANGKBH")
					+ mapls.get(i).get("FENPQHCK") + mapls.get(i).get("MOS"), mapls.get(i));
		}
		return cangkxhsjmap;
	}
}
