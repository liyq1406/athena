package com.athena.xqjs.module.zygzbj.service;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.athena.authority.entity.LoginUser;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.date.DateUtil;
import com.athena.xqjs.entity.anxorder.AnxMaoxq;
import com.athena.xqjs.entity.common.Lingj;
import com.athena.xqjs.entity.common.Lingjxhd;
import com.athena.xqjs.entity.ilorder.CdSdMaoxuq;
import com.athena.xqjs.entity.ilorder.Maoxq;
import com.athena.xqjs.entity.ilorder.Maoxqmx;
import com.athena.xqjs.entity.yaohl.Yaohl;
import com.athena.xqjs.entity.ycbj.Yicbj;
import com.athena.xqjs.entity.zygzbj.ZiygzbjHz;
import com.athena.xqjs.entity.zygzbj.Ziygzbjmx;
import com.athena.xqjs.entity.zygzbj.Ziygzbjmxq;
import com.athena.xqjs.entity.zygzbj.Zygzlx;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.ycbj.service.YicbjService;
import com.toft.core3.Pageable;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

/**
 * <p>
 * 项目名称：athena-xqjs
 * </p>
 * 类名称：ZygzbjjsService
 * <p>
 * 类描述：资源跟踪报警计算service
 * </p>
 * 创建人：WL
 * <p>
 * 创建时间：2012-01-18
 * </p>
 * 
 * @version 1.0
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
@Component
public class ZygzbjjsService extends BaseService {
	
	@Inject
	private YicbjService yicbjService;
	
	@Inject
	private ZygzbjjsParam zigzbjjsParam;
	
	/**
	 * 查询毛需求版次信息
	 * 
	 * @param page
	 *            分页显示对象
	 * @param param
	 *            查询参数
	 * @return 查询结果
	 */
	public Map queryMxq(Pageable page, Map<String, String> param) {
		// 多来源
		List<Zygzlx> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("zygzbj.queryZygzlx", param);
		String jislx = String.valueOf(list.get(0).getJslxmc().charAt(0));
		//如果为按需,则不需要查询毛需求,只有一版
		if("T".equals(jislx)){
			Maoxq maoxq = new Maoxq();
			maoxq.setXuqly("按需");
			maoxq.setXuqbc("anx");
			List maoxqList = new ArrayList<Maoxq>();
			maoxqList.add(maoxq);
			return CommonFun.listToMap(maoxqList);
		}else{
		// 需求来源
		StringBuilder xqly = new StringBuilder("");
		for (int i = 0; i < list.size(); i++) {
			Zygzlx zygzlx = list.get(i);
			// 拼接需求来源
			xqly.append("'" + zygzlx.getXuqly() + "',");
		}
		if (!list.isEmpty()) {
			// 去掉最后一个,
			xqly.deleteCharAt(xqly.lastIndexOf(","));
			// //根据需求类型查询需求版次
			param.put("xqly", xqly.toString());
		}
		return CommonFun.listToMap(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("zygzbj.queryMxq", param));
		}
	}

	/**
	 * 资源跟踪报警计算
	 * 
	 * @param param
	 *            计算参数
	 * @param edit
	 *            参数
	 * @return 计算结果
	 */
	public void jiS(Map<String, String> map, List<Maoxq> edit) throws Exception {
		long start = System.currentTimeMillis();
		// 用户名
		String username = map.get("username");
		// 查询参数
		Map<String, String> param = new HashMap<String, String>();
		param.put("username", username);
		// 计算日期
		String jisrq = CommonFun.getJavaTime(Const.TIME_FORMAT_YY_MM_DD);
		param.put("jisrq", jisrq);
		// 资源获取日期
		String ziyhqrq = map.get("zyhqrq");
		param.put("ziyhqrq", ziyhqrq);
		// 用户中心
		param.put("usercenter", map.get("usercenter"));
		/**
		 * 根据计算类型查询计算方法
		 */
		param.put("jslx", map.get("jslx"));
		
		// 删除之前的该计算类型的计算记录,同一计算类型只计算一次
		delZygzbjhz(param);
		
		/**
		 * 加载资源跟踪报警计算参数
		 */
		zigzbjjsParam.initParam(map);
		
		//查询资源跟踪报警计算类型
		List<Zygzlx> listZygzlx = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("zygzbj.queryZygzlx", param);
		//制造路线
		StringBuilder zhizlxs = new StringBuilder("");//制造路线
		//制造路线标识,如果只包含KD的进行KD的汇总,直接找订货库,如果包含KD以外的通过分配去找订货库或者线边库
		boolean blZhizlx = true;
		//汇总制造路线
		for (int i = 0; i < listZygzlx.size(); i++) {
			String zhizlx = CommonFun.strNull(listZygzlx.get(i).getZhizlx());
			//如果制造路线包含非KD的,则用非KD算法
			if(!zhizlx.equals(Const.ZHIZAOLUXIAN_KD_AIXIN) && !zhizlx.equals(Const.ZHIZAOLUXIAN_KD_PSA)){
				blZhizlx = false;
			}
			//制造路线集合,不包含添加,进行制造路线过滤
			if(zhizlxs.indexOf(zhizlx) == -1){
				zhizlxs.append("'"+zhizlx+"',");
			}
		}
		zhizlxs.deleteCharAt(zhizlxs.lastIndexOf(","));
		param.put("zhizlxs", zhizlxs.toString());
		// 查询计算方法
		Zygzlx zygzlx =  listZygzlx.get(0);
		//资源跟踪类型的首位为计算类型
		String jislx = String.valueOf(zygzlx.getJslxmc().charAt(0));
		String waibms = CommonFun.splitString(zygzlx.getGonghms());
		//拼接供货模式
		param.put("waibms", waibms);
		
		/**
		 * 多版次合并
		 */
		StringBuilder xuqbc = new StringBuilder("");
		// 拼接需求版次
		for (int i = 0; i < edit.size(); i++) {
			xuqbc.append("'" + edit.get(i).getXuqbc() + "',");
		}
		// 去掉最后一个,
		xuqbc.deleteCharAt(xuqbc.lastIndexOf(","));
		// 需求版次
		param.put("xuqbc", xuqbc.toString());
		CommonFun.logger.info("资源跟踪报警计算开始"+param);
		Map countUcMap= countLingjUc();
		Map rilbcMap = selRilbc();
		
		// 查询零件
		List<Lingj> lingjList = (List) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("zygzbj.selLjAll");
		Map lingjMap = new HashMap();
		for(Lingj lingj:lingjList){
			String usercenter = lingj.getUsercenter();
			String lingjbh = lingj.getLingjbh();
			String key = usercenter+":"+lingjbh;
			lingjMap.put(key, lingj);
		}
		
		// 查询零件仓库
		List lingjckList = (List) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("zygzbj.selAllBzlx");
		Map lingjckAllMap = new HashMap();
		for(int i=0;i<lingjckList.size();i++){
			Map lingjckMap = (Map)lingjckList.get(i);
			String usercenter = (String)lingjckMap.get("USERCENTER");
			String lingjbh = (String)lingjckMap.get("LINGJBH");
			String cangkdm = (String)lingjckMap.get("CANGKBH");
			String key = usercenter+":"+lingjbh+":"+cangkdm;
			lingjckAllMap.put(key, lingjckMap);
		}
		
		//取零件供应商信息
		List<Map> lingjgysList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("zygzbj.selLingjgys");
		Map ljgysMap = new HashMap();
		for(Map lgmap:lingjgysList){
			String lingjbh = (String)lgmap.get("LINGJBH");
			String usercenter = (String)lgmap.get("USERCENTER");
			String key = usercenter+":"+lingjbh;
			BigDecimal bjrl = (BigDecimal)lgmap.get("BZRL");
			ljgysMap.put(key, bjrl);
		}
		
		
		//如果为时段类型
		if ("T".equals(jislx)) {
			//计算类型为CD,则为时段类型
			if ("CD".equals(zygzlx.getJslxmc().substring(1,3))) {
				jisCDSdbj(param, zygzlx,username,countUcMap,rilbcMap,lingjMap,lingjckAllMap,ljgysMap);
				return;
			}else{
				jisSdbj(param, zygzlx,username,blZhizlx,rilbcMap,lingjMap,lingjckAllMap,ljgysMap,countUcMap);
				return;
			}
		} 
		/**
		 * 查询毛需求明细,进行计算
		 */
		List<Maoxqmx> listMxqmx = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("zygzbj.queryAllMxqmx", param);
		Map dwMap = new HashMap();
		if(blZhizlx){//KD毛需求汇总处理
			 dwMap = kdHzmxq(listMxqmx, param, username);
		}else{//非KD毛需求汇总处理
			 dwMap = mxqHz(listMxqmx, param, waibms, username);
		}
		CommonFun.logger.info("资源跟踪报警汇总毛需求到仓库");
		
		// 查询资源跟踪报警毛需求
		List<Ziygzbjmxq> listZiygzbjmxq = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("zygzbj.queryZiygzbjmxq");
		
		// 查询未发运数量
		List<Map> weifyList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("zygzbj.selAllWeify");
		Map weifySlmap = new HashMap();
		for(Map weifyMap:weifyList){
			String usercenter = (String)weifyMap.get("USERCENTER");
			String lingjbh = (String)weifyMap.get("LINGJBH");
			String cangkbh = (String)weifyMap.get("CANGKBH");
			BigDecimal yaohlsl = CommonFun.getBigDecimal(weifyMap.get("YAOHLSL"));
			String key = usercenter+":"+lingjbh+":"+cangkbh;
			weifySlmap.put(key, yaohlsl);
		}
		
		List<Map> calendarList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("zygzbj.selAllCalendarVersion");
		List riqList = new ArrayList();
		Map nianzqMap = new HashMap();
		Map nianzxMap = new HashMap();
		for(Map calendarMap:calendarList){
			String banc = (String)calendarMap.get("BANC");
			String usercenter = (String)calendarMap.get("USERCENTER");
			String riq = (String)calendarMap.get("RIQ");
			String nianzq = (String)calendarMap.get("NIANZQ");
			String zhoux = (String)calendarMap.get("ZHOUX");
			String nianzx = (String)calendarMap.get("NIANZX");
			String shifgzr = (String)calendarMap.get("SHIFGZR");
			String key = usercenter+":"+banc+":"+riq;
			riqList.add(key);
			nianzqMap.put(key, nianzq);
			nianzxMap.put(key, nianzx);
		}
		
		for (int i = 0; i < listZiygzbjmxq.size(); i++) {
			Ziygzbjmxq Ziygzbjmxq = listZiygzbjmxq.get(i);
			/**
			 * 资源跟踪报警汇总结果
			 */
			ZiygzbjHz ziygzbjhz = new ZiygzbjHz();
			ziygzbjhz.setZygy("1");
			ziygzbjhz.setId(getUUID());// ID
			ziygzbjhz.setCreator(username);// 创建人
			ziygzbjhz.setWeihr(username);// 维护人
			ziygzbjhz.setJislx(map.get("jslx"));// 计算类型
			ziygzbjhz.setMaoxqbc(xuqbc.toString());// 毛需求版次
			ziygzbjhz.setUsercenter(Ziygzbjmxq.getUsercenter());// 用户中心
			ziygzbjhz.setLingjbh(Ziygzbjmxq.getLingjbh());// 零件编号
			ziygzbjhz.setZhizlx(Ziygzbjmxq.getZhizlx());// 制造路线
			ziygzbjhz.setCangkdm(Ziygzbjmxq.getCangkdm());// 仓库
			// 创建时间
			String time = CommonFun.getJavaTime("yyyy-MM-dd HH:mm:ss");
			ziygzbjhz.setCreate_time(time);
			ziygzbjhz.setWeihsj(time);
			ziygzbjhz.setJissj(time);

			// 查询参数
			Map<String, String> mapZiygzbjhz = new HashMap<String, String>();
			mapZiygzbjhz.put("xuqbc", ziygzbjhz.getMaoxqbc());
			mapZiygzbjhz.put("usercenter", ziygzbjhz.getUsercenter());
			mapZiygzbjhz.put("lingjbh", ziygzbjhz.getLingjbh());
			mapZiygzbjhz.put("cangkdm", ziygzbjhz.getCangkdm());
			mapZiygzbjhz.put("cangk", Ziygzbjmxq.getXianbk());
			mapZiygzbjhz.put("zhizlx", ziygzbjhz.getZhizlx());
			mapZiygzbjhz.put("jisrq", jisrq);		
			mapZiygzbjhz.put("ziyhqrq", ziyhqrq);
			mapZiygzbjhz.put("username", username);
			String key = ziygzbjhz.getUsercenter()+":"+ziygzbjhz.getLingjbh();
			Lingj lingj = (Lingj)lingjMap.get(key);
			if (lingj == null) {
				//saveYicbj(param, "用户中心"+ziygzbjhz.getUsercenter()+"下零件有以下参数错误:零件查询为空,零件编号"+ziygzbjhz.getLingjbh(), username);
				List msgList = new ArrayList();
				msgList.add(ziygzbjhz.getUsercenter());
				msgList.add(ziygzbjhz.getLingjbh());
				LoginUser loginuser = new LoginUser();
				loginuser.setUsername(username);
				yicbjService.insertError(Const.YICHANG_LX2,
						Const.YICHANG_LX2_str54, "", msgList, ziygzbjhz.getUsercenter(),
						ziygzbjhz.getLingjbh(), loginuser, Const.JISMK_GZBJ_CD);
				continue;
			}
			ziygzbjhz.setLingjmc(CommonFun.strNull(lingj.getZhongwmc()));// 中文名称
			ziygzbjhz.setLingjdw(CommonFun.strNull(dwMap.get(ziygzbjhz.getLingjbh())));// 单位
			ziygzbjhz.setJihydm(CommonFun.strNull(lingj.getJihy()));// 计划员代码
			// 设置多用户中心使用
			ziygzbjhz.setShifdyhzxsy((Integer)countUcMap.get(ziygzbjhz.getLingjbh()));// 是否多用户中心使用
			String keyCangdkdm = ziygzbjhz.getUsercenter()+":"+ziygzbjhz.getLingjbh()+":"+ziygzbjhz.getCangkdm();
			Map lingjck = (Map)lingjckAllMap.get(keyCangdkdm);
			if (lingjck == null) {
				//saveYicbj(param, "用户中心"+ziygzbjhz.getUsercenter()+"下零件有以下参数错误:零件仓库查询为空,零件编号"+ziygzbjhz.getLingjbh(), username);
				List msgList = new ArrayList();
				msgList.add(ziygzbjhz.getUsercenter());
				msgList.add(ziygzbjhz.getLingjbh());
				LoginUser loginuser = new LoginUser();
				loginuser.setUsername(username);
				yicbjService.insertError(Const.YICHANG_LX2,
						Const.YICHANG_LX2_str55, "", msgList, ziygzbjhz.getUsercenter(),
						ziygzbjhz.getLingjbh(), loginuser, Const.JISMK_GZBJ_CD);
				continue;
			}
			ziygzbjhz.setBaozlx(CommonFun.strNull(lingjck.get("USBZLX")));// 包装类型
			ziygzbjhz.setBaozrl(CommonFun.getBigDecimal(ljgysMap.get(ziygzbjhz.getUsercenter() + ":" + ziygzbjhz.getLingjbh())));// 包装容量
			ziygzbjhz.setAnqkc(CommonFun.getBigDecimal(lingjck.get("ANQKCSL")));// 安全库存
			ziygzbjhz.setZuidkcsl(CommonFun.getBigDecimal(lingjck.get("ZUIDKCSL")));// 最大库存数量
			BigDecimal mafkc = BigDecimal.ZERO;
			if(zigzbjjsParam.inMafMap.get(key)!=null){
				mafkc = (BigDecimal)zigzbjjsParam.inMafMap.get(key);
			}
			ziygzbjhz.setMafkc(mafkc);
			ziygzbjhz.setWeifysl((BigDecimal)weifySlmap.get(keyCangdkdm));// 未发运数量
			// 查询库存快照
			BigDecimal kuc = CommonFun.getBigDecimal(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selKckz", mapZiygzbjhz));
			ziygzbjhz.setQickc(kuc);// 期初库存
			BigDecimal kaisrzy = kuc;//开始日资源
			
			// 判断需求类型,如果为周期,则按周期汇总毛需求
			if ("P".equals(jislx)) {
				ziygzbjhz.setBaojlx("1");// 报警类型,1,周期报警
				mapZiygzbjhz.put("xuqlx", "nianzq");
				//周期报警计算
				if(!zqBaojjs(zygzlx, mapZiygzbjhz, param, ziygzbjhz, lingj,rilbcMap,riqList,nianzqMap,nianzxMap)){
					continue;
				}
				// 如果需求类型为周,则按周汇总毛需求
			} else if ("S".equals(jislx)) {
				ziygzbjhz.setBaojlx("2");// 报警类型,2,周报警
				mapZiygzbjhz.put("xuqlx", "nianzx");
			
				//周报警计算
				if(!zBaojjs(zygzlx, mapZiygzbjhz, param, ziygzbjhz, lingj, rilbcMap,riqList,nianzxMap)){
					continue;
				}
				
				//日计算类型
			} else if("J".equals(jislx)){
				String jisr = jisrq;
				ziygzbjhz.setBaojlx("3");// 报警类型,4,日报警
				mapZiygzbjhz.put("xuqlx", "riq");
				// 计算30天
				// 查询仓库日历版次
				String rilbc = (String)rilbcMap.get(ziygzbjhz.getUsercenter()+":"+mapZiygzbjhz.get("cangkdm"));
				if(rilbc==null||"".equals(rilbc)){
					//saveYicbj(mapZiygzbjhz, "用户中心"+ziygzbjhz.getUsercenter()+"下零件有以下参数错误:日历版次为空,计算日期为"+jisrq+"仓库代码为"+mapZiygzbjhz.get("cangkdm"), username);
					List msgList = new ArrayList();
					msgList.add(param.get("jisrq"));
					msgList.add(mapZiygzbjhz.get("cangkdm"));
					LoginUser loginuser = new LoginUser();
					loginuser.setUsername( param.get("username"));
					yicbjService.insertError(Const.YICHANG_LX2,
							Const.YICHANG_LX2_str56, "", msgList, mapZiygzbjhz.get("usercenter"),
							lingj.getLingjbh(), loginuser, Const.JISMK_GZBJ_CD);
					continue;
				}
				mapZiygzbjhz.put("rilbc", rilbc);
				mapZiygzbjhz.put("riq", jisr);
				Map riqThirtyMap = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("zygzbj.nextRiqThirty", mapZiygzbjhz, 1, 30);
				List riqThiryList = (List)riqThirtyMap.get("rows");
				int m = 1;
				for (int j = 0; j < riqThiryList.size(); j++) {
						Map tempMap = (Map)riqThiryList.get(j);
						jisr = (String)tempMap.get("RIQ");
						String shifgzr = (String)tempMap.get("SHIFGZR");
						int days = CommonFun.daysOfDate(DateUtil.stringToDateYMD(jisr),DateUtil.stringToDateYMD(jisrq));
						Class cl = ZiygzbjHz.class;
						PropertyDescriptor pd = new PropertyDescriptor("xuq" + days, cl);
						PropertyDescriptor pd2 = new PropertyDescriptor("yijf" + days, cl);
						if(shifgzr.equals("1")&&m<10){
							// 需求数量
							BigDecimal xuqsl = BigDecimal.ZERO;
							// 到货数量
							mapZiygzbjhz.put("riq", jisr);
							String lastJisr = (String)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.lastRiq",mapZiygzbjhz);
							mapZiygzbjhz.put("lastJisr", lastJisr);
							BigDecimal daoh = selRiDaohL(mapZiygzbjhz);
							List<Ziygzbjmxq> listZiygzbjmxqmx = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("zygzbj.queryZiygzbjmxqmx", mapZiygzbjhz);
							for (int k = 0; k < listZiygzbjmxqmx.size(); k++) {
								Ziygzbjmxq ziygzbjmxq = listZiygzbjmxqmx.get(k);
								mapZiygzbjhz.put("chanx", ziygzbjmxq.getChanx());// 产线
								mapZiygzbjhz.put("lxValue", jisr);
								// 需求数量
								xuqsl = xuqsl.add(CommonFun.getBigDecimal(
										baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.queryMxqsl", mapZiygzbjhz)).multiply(
										CommonFun.getBigDecimal(ziygzbjmxq.getXiaohbl())));
							}
							BigDecimal ysxuqsl = xuqsl;
							xuqsl = compareXq(CommonFun.strNull(lingj.getLingjsx()), param, jisr, xuqsl);
							pd.getWriteMethod().invoke(ziygzbjhz, ysxuqsl.setScale(0,BigDecimal.ROUND_HALF_UP));
							// 计算日期向后推
							//jisr = CommonFun.strNull(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.nextRiq", mapZiygzbjhz));
							// 已交付
							kaisrzy = kaisrzy.add(daoh).subtract(xuqsl);
							pd2.getWriteMethod().invoke(ziygzbjhz, kaisrzy.setScale(0,BigDecimal.ROUND_HALF_UP));
							saveDuand(kaisrzy, ziygzbjhz, jisr, String.valueOf(j));
							m++;
						}else if(shifgzr.equals("0")){
							pd.getWriteMethod().invoke(ziygzbjhz, BigDecimal.ZERO);
							pd2.getWriteMethod().invoke(ziygzbjhz, BigDecimal.ZERO);
						}
					
				}
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("zygzbj.saveZygzbjhz", ziygzbjhz);
			}
			//ziygzbjhz.setDaixhl(daixh);// 待消耗
			// 保存资源跟踪报警汇总结果
		}
		
		/**
		 * 销毁资源跟踪报警参数
		 */
		zigzbjjsParam.destory();
		
		CommonFun.logger.info("资源跟踪报警计算结束,耗时:"+(System.currentTimeMillis() - start)+"毫秒");
	}

	/**
	 * 计算时段报警
	 * @param param 计算参数
	 * @param zygzlx 资源跟踪报警计算类型
	 * @param username 操作用户
	 * @throws Exception 
	 */
	public void jisCDSdbj(Map param,Zygzlx zygzlx,String username,Map countUcMap,Map rilbcMap,Map lingjMap,Map lingjckAllMap,Map ljgysMap) throws Exception{
		String ziyhqrq = CommonFun.strNull(param.get("ziyhqrq"));//资源获取日期
		List<Ziygzbjmxq> listMxq = new ArrayList<Ziygzbjmxq>();
		
		//汇总时段毛需求(取按需毛需求表),按消耗点,产线,零件编号,用户中心汇总
		List<CdSdMaoxuq> listMxqmx = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("zygzbj.queryCDSdmxq",param);
		for (int i = 0; i < listMxqmx.size(); i++) {
			CdSdMaoxuq  cdSdMaoxq = listMxqmx.get(i);
			Ziygzbjmxq ziygzbjmxq = new Ziygzbjmxq();
			ziygzbjmxq.setLingjbh(cdSdMaoxq.getLingjbh());
			ziygzbjmxq.setUsercenter(cdSdMaoxq.getUsercenter());
			ziygzbjmxq.setZhizlx(cdSdMaoxq.getZhizlx());
			ziygzbjmxq.setXiaohd(cdSdMaoxq.getXiaohd());
			ziygzbjmxq.setChanx(cdSdMaoxq.getChanx());
			param.put("lingjbh", cdSdMaoxq.getLingjbh());
			
			if((cdSdMaoxq.getFenpqbh() != null) && (!"".endsWith(cdSdMaoxq.getFenpqbh())) ){
				//取订货库
				String ck = CommonFun.strNull(cdSdMaoxq.getDinghck());
				//如果订货库为空,取线边库
				if(ck.equals("")){
					ck = CommonFun.strNull(cdSdMaoxq.getXianbck());
				}
				//订货库,线边库都为空,不计算该条
				if(ck.equals("")){
					saveYicbj(param, "用户中心"+cdSdMaoxq.getUsercenter()+"以下参数错误:查询零件消耗点分配区仓库为空,零件编号"+cdSdMaoxq.getLingjbh()+"产线"+cdSdMaoxq.getChanx(), username);
				}else{
					param.put("id", getUUID());
					param.put("appobj", ck);
					param.put("xiaohd", CommonFun.strNull(cdSdMaoxq.getXiaohd()));
					// 仓库,产线汇总
					ziygzbjmxq.setId(getUUID());
					ziygzbjmxq.setCangkdm(ck);
				}
			}else{
				saveYicbj(param, "用户中心"+cdSdMaoxq.getUsercenter()+"零件编号"+cdSdMaoxq.getLingjbh()+"以下参数错误:分配区,物流路径仓库信息查询为空,消耗点"+cdSdMaoxq.getXiaohd()+"生产线"+cdSdMaoxq.getChanx(), username);
			}
			listMxq.add(ziygzbjmxq);
		}
		
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("zygzbj.inCDZiygzbjmxq", listMxq);
		
		//资源跟踪汇总
		List<ZiygzbjHz> listZiygzbjhz = new ArrayList<ZiygzbjHz>();
		List<Ziygzbjmx> listZiygzbjmx = new ArrayList<Ziygzbjmx>();
		List<Map> listDuand = new ArrayList<Map>();
		
		// 查询资源跟踪报警毛需求
		List<Ziygzbjmxq> listZiygzbjmxq = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("zygzbj.queryCDZiygzbjmxq");
		for (int i = 0; i < listZiygzbjmxq.size(); i++) {
			Ziygzbjmxq Ziygzbjmxq = listZiygzbjmxq.get(i);
			// 线边理论库存查询参数
			Map<String, String> mapxianbllkc = new HashMap<String, String>();
			mapxianbllkc.put("usercenter", Ziygzbjmxq.getUsercenter());
			mapxianbllkc.put("lingjbh", Ziygzbjmxq.getLingjbh());
			mapxianbllkc.put("xiaohdbh", Ziygzbjmxq.getXiaohd());
			
			/**
			 * 资源跟踪报警汇总结果
			 */
			ZiygzbjHz ziygzbjhz = new ZiygzbjHz();
			ziygzbjhz.setId(getUUID());// ID
			ziygzbjhz.setCreator(username);// 创建人
			ziygzbjhz.setWeihr(username);// 维护人
			ziygzbjhz.setJislx(CommonFun.strNull(param.get("jslx")));// 计算类型
			ziygzbjhz.setMaoxqbc(CommonFun.strNull(param.get("xuqbc")));// 毛需求版次
			ziygzbjhz.setUsercenter(Ziygzbjmxq.getUsercenter());// 用户中心
			ziygzbjhz.setLingjbh(Ziygzbjmxq.getLingjbh());// 零件编号
			ziygzbjhz.setZhizlx(Ziygzbjmxq.getZhizlx());// 制造路线
			ziygzbjhz.setCangkdm(Ziygzbjmxq.getXiaohd());// 仓库
			// 创建时间
			String time = CommonFun.getJavaTime("yyyy-MM-dd HH:mm:ss");
			ziygzbjhz.setCreate_time(time);
			ziygzbjhz.setWeihsj(time); 
			ziygzbjhz.setJissj(time);

			// 查询参数
			Map<String, String> mapZiygzbjhz = new HashMap<String, String>();
			mapZiygzbjhz.put("usercenter", ziygzbjhz.getUsercenter());
			mapZiygzbjhz.put("lingjbh", ziygzbjhz.getLingjbh());
			mapZiygzbjhz.put("cangkdm", Ziygzbjmxq.getCangkdm());
			mapZiygzbjhz.put("zhizlx", ziygzbjhz.getZhizlx());
			String jisrq = CommonFun.strNull(param.get("jisrq"));//计算日期
			String jisrq1 = CommonFun.strNull(param.get("jisrq"));//计算日期
			mapZiygzbjhz.put("jisrq", jisrq);
			mapZiygzbjhz.put("ziyhqrq",ziyhqrq);
			
			mapZiygzbjhz.put("chanx",Ziygzbjmxq.getChanx());
			mapZiygzbjhz.put("xiaohd",Ziygzbjmxq.getXiaohd());
			mapZiygzbjhz.put("mudd",Ziygzbjmxq.getXiaohd());
			mapZiygzbjhz.put("shixrq", jisrq);

			// 查询零件
			Lingj lingj = (Lingj) lingjMap.get(mapZiygzbjhz.get("usercenter")+":"+mapZiygzbjhz.get("lingjbh"));
			if (lingj == null) {
				saveYicbj(param, "用户中心"+ziygzbjhz.getUsercenter()+"下零件有以下参数错误:零件查询为空,零件编号"+ziygzbjhz.getLingjbh(), username);
				continue;
			}
			ziygzbjhz.setLingjmc(CommonFun.strNull(lingj.getZhongwmc()));// 中文名称
			ziygzbjhz.setLingjdw(CommonFun.strNull(lingj.getDanw()));// 单位
			ziygzbjhz.setJihydm(CommonFun.strNull(lingj.getJihy()));// 计划员代码
			ziygzbjhz.setBaojlx("4");//报警类型为时段报警

			// 统计是否多用户中心使用
			ziygzbjhz.setShifdyhzxsy((Integer)countUcMap.get(ziygzbjhz.getLingjbh()));// 是否多用户中心使用
			// 查询零件仓库
			Map lingjck = (Map) lingjckAllMap.get(mapZiygzbjhz.get("usercenter")+":"+mapZiygzbjhz.get("lingjbh")+":"+mapZiygzbjhz.get("cangkdm"));
			if(lingjck == null){
				saveYicbj(param, "用户中心"+ziygzbjhz.getUsercenter()+"下零件有以下参数错误:零件仓库查询为空,零件编号"+ziygzbjhz.getLingjbh()+
						"仓库代码"+ziygzbjhz.getCangkdm(), username);
				continue;
			}
			Lingjxhd lingjxhd = zigzbjjsParam.lingjxhdMap.get(ziygzbjhz.getUsercenter() + ziygzbjhz.getLingjbh()
					+ Ziygzbjmxq.getXiaohd());
			if(lingjxhd == null){
				saveYicbj(param, "用户中心"+ziygzbjhz.getUsercenter()+"下零件有以下参数错误:零件消耗点查询为空,零件编号"+ziygzbjhz.getLingjbh()+
						"消耗点"+Ziygzbjmxq.getXiaohd(), username);
				continue;
			}
			ziygzbjhz.setBaozlx(CommonFun.strNull(lingjck.get("USBZLX")));// 包装类型
			ziygzbjhz.setBaozrl(CommonFun.getBigDecimal(ljgysMap.get(mapZiygzbjhz.get("usercenter")+":"+mapZiygzbjhz.get("lingjbh"))));// 包装容量
			ziygzbjhz.setAnqkc(CommonFun.getBigDecimal(lingjxhd.getAnqkcs()));// 安全库存
			//ziygzbjhz.setZuidkcsl(CommonFun.getBigDecimal(lingjck.get("ZUIDKCSL")));// 最大库存数量
			// 查询MAF库存
			ziygzbjhz.setMafkc(CommonFun.getBigDecimal(zigzbjjsParam.inMafMap.get(ziygzbjhz.getUsercenter() + ziygzbjhz.getLingjbh())));
			
			// 查询未发运数量
			BigDecimal weify = CommonFun.getBigDecimal(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selCDWeify", mapZiygzbjhz));
			ziygzbjhz.setWeifysl(weify);// 未发运数量
			
			// 查询线边理论库存 期初库存
			BigDecimal kuc = CommonFun.getBigDecimal(lingjxhd.getXianbllkc());
			ziygzbjhz.setQickc(kuc);

			// 查询产线日历版次
			String rilbc = (String)rilbcMap.get(ziygzbjhz.getUsercenter()+":"+mapZiygzbjhz.get("chanx"));
			mapZiygzbjhz.put("rilbc", rilbc);
			// 待消耗
			BigDecimal daixh = BigDecimal.ZERO;
			// 查询资源跟踪报警毛需求明细
			//List<Ziygzbjmxq> listZiygzbjmxqmx = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("zygzbj.queryCDZiygzbjmxqmx", mapZiygzbjhz);
			ziygzbjhz.setDaixhl(daixh);// 待消耗
			//开始日资源 
			BigDecimal kaisrzy = kuc;
			
			/**
			 * 按时间汇总按需毛需求
			 */
			//时段
			Calendar calendar = Calendar.getInstance();
			//从7点开始
			calendar.setTime(CommonFun.yyyyMMddHHmmss.parse(jisrq + " 07:00:00"));
			Date startDate = calendar.getTime();//开始时间
			//向后推一个小时
			calendar.add(calendar.HOUR, 1);
			Date endDate = calendar.getTime();//结束时间
			mapZiygzbjhz.put("startTime", CommonFun.yyyyMMddHHmmss.format(startDate));
			List<AnxMaoxq> listAnxmxq = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("zygzbj.selAanxmxq",mapZiygzbjhz);
			BigDecimal hourXqsl = BigDecimal.ZERO;//一小时内毛需求数量
			Map<String,BigDecimal> mapAnxmxq = new HashMap<String, BigDecimal>();
			for (int j = 0; j < listAnxmxq.size(); j++) {
				AnxMaoxq anxMaoxq = listAnxmxq.get(j);
				Date xhsj = CommonFun.yyyyMMddHHmmss.parse(anxMaoxq.getXhsj());//毛需求消耗时间
				//如果毛需求消耗时间大于等于开始时间
				if(xhsj.compareTo(startDate) >=0 ){
					//如果毛需求消耗时间小于结束时间
					if(xhsj.before(endDate)){
						hourXqsl = hourXqsl.add(anxMaoxq.getXiaohxs());//毛需求数量相加
						if(j == listAnxmxq.size() - 1){
							mapAnxmxq.put(CommonFun.yyyyMMddHHmmss.format(startDate), hourXqsl);
						}
					}else{
						//倒退,下次循环继续汇总本条毛需求信息
						j--;
						//小时内毛需求汇总完毕
						mapAnxmxq.put(CommonFun.yyyyMMddHHmmss.format(startDate), hourXqsl);
						hourXqsl = BigDecimal.ZERO;
						startDate = calendar.getTime();//开始时间
						//向后推一个小时
						calendar.add(calendar.HOUR, 1);
						endDate = calendar.getTime();//结束时间
					}
				}
			}
			
			//从7点开始
			calendar.setTime(CommonFun.yyyyMMddHHmmss.parse(jisrq + " 07:00:00"));
			startDate = calendar.getTime();//开始时间
			//向后推一个小时
			calendar.add(calendar.HOUR, 1);
			endDate = calendar.getTime();//结束时间
			BigDecimal hourDaoh = BigDecimal.ZERO;//一小时内毛需求数量
			Map<String,BigDecimal> mapNbyhl = new HashMap<String, BigDecimal>();
			mapZiygzbjhz.put("startTime", CommonFun.yyyyMMddHHmmss.format(startDate));
			//查询到货
			List<Map> listNbyhl = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("zygzbj.queryCDSdDhList", mapZiygzbjhz);
			for (int j = 0; j < listNbyhl.size(); j++) {
				Map nbyhl = listNbyhl.get(j);
				Date beihsj = CommonFun.yyyyMMddHHmmss.parse(CommonFun.strNull(nbyhl.get("BEIHSJ")));//毛需求消耗时间
				//如果毛需求消耗时间大于等于开始时间
				if(beihsj.compareTo(startDate) >=0 ){
					//如果毛需求消耗时间小于结束时间
					if(beihsj.before(endDate)){
						hourDaoh = hourDaoh.add(CommonFun.getBigDecimal(nbyhl.get("YAOHSL")));//毛需求数量相加
						if(j == listNbyhl.size() - 1){
							mapNbyhl.put(CommonFun.yyyyMMddHHmmss.format(startDate), hourDaoh);
						}
					}else{
						//倒退,下次循环继续汇总本条毛需求信息
						j--;
						//小时内毛需求汇总完毕
						mapNbyhl.put(CommonFun.yyyyMMddHHmmss.format(startDate), hourDaoh);
						hourDaoh = BigDecimal.ZERO;
						startDate = calendar.getTime();//开始时间
						//向后推一个小时
						calendar.add(calendar.HOUR, 1);
						endDate = calendar.getTime();//结束时间
					}
				}
			}
			
			//时段算2天,每天按24小时,每隔1小时汇总,计算需求和已交付
			for (int j = 0; j < 2; j++) {
				Class cl = ZiygzbjHz.class;
				PropertyDescriptor pd = new PropertyDescriptor("xuq" + j, cl);
				PropertyDescriptor pd2 = new PropertyDescriptor("yijf" + j, cl);
				// 需求数量
				BigDecimal xuqsl = BigDecimal.ZERO;
				//从7点开始
				calendar.setTime(CommonFun.yyyyMMddHHmmss.parse(jisrq + " 07:00:00"));
				String rq= jisrq;
				//每天24小时,每隔1小时汇总
				for (int k = 0; k < 24; k++) {
					
					//开始时间
					String startTime = CommonFun.yyyyMMddHHmmss.format(calendar.getTime());
					BigDecimal hourXusl = CommonFun.getBigDecimal(mapAnxmxq.get(startTime));
					//向后推一个小时
					calendar.add(calendar.HOUR, 1);
					//结束时间
					String endTime = CommonFun.yyyyMMddHHmmss.format(calendar.getTime());
					mapZiygzbjhz.put("endTime",endTime);
					
					BigDecimal daoh = BigDecimal.ZERO;
					//当计算第二个工作日的开始时,需考虑第一个工作日到第二个工作之间非工作日的到货情况
					if(k ==0 ){
						Calendar calendar1 = Calendar.getInstance();
						calendar1.setTime(CommonFun.yyyyMMddHHmmss.parse(jisrq1 + " 07:00:00"));
						//向前推一个自然日
						calendar1.add(calendar1.DATE, -1);
						mapZiygzbjhz.put("riq1", CommonFun.sdf.format(calendar1.getTime()));
						//判断该自然日是否工作日
						boolean shifgzr = CommonFun.getBigDecimal(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.shifGZR", mapZiygzbjhz)).intValue()>0?true:false;
						//非工作日取前一个工作日
						if(!shifgzr){
							//前一个工作日
							rq = CommonFun.strNull(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.previousRiq", mapZiygzbjhz));
							Calendar calendar2 = Calendar.getInstance();
							//前一个工作日向后推一天
							calendar2.setTime(CommonFun.sdf.parse(rq));
							calendar2.add(calendar2.DAY_OF_YEAR, 1);
							rq = CommonFun.sdf.format(calendar2.getTime());
						}
						//上一个工作日后的非工作日
						startTime = rq + startTime.substring(10);
						mapZiygzbjhz.put("startTime",startTime);
						//查询到货
						daoh = CommonFun.getBigDecimal(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.queryCDSdDh", mapZiygzbjhz));
					}else{
						daoh = CommonFun.getBigDecimal(mapNbyhl.get(startTime));
					}
					
					// 已交付
					kaisrzy = kaisrzy.add(daoh).subtract(hourXusl);
					//保存资源跟踪报警明细
					Ziygzbjmx ziygzbjmx = getZygzbjmx(ziygzbjhz, j, k, hourXusl, kaisrzy, startTime, endTime);
					listZiygzbjmx.add(ziygzbjmx);
					//保存断点信息
					Map duandMap = getDuand(kaisrzy, ziygzbjhz, endTime, String.valueOf(k));
					if(duandMap != null){
						listDuand.add(duandMap);
					}
					//汇总需求数量
					xuqsl = xuqsl.add(hourXusl);
				}
				pd.getWriteMethod().invoke(ziygzbjhz, xuqsl.setScale(0,BigDecimal.ROUND_HALF_UP));
				pd2.getWriteMethod().invoke(ziygzbjhz, kaisrzy.setScale(0,BigDecimal.ROUND_HALF_UP));
				mapZiygzbjhz.put("riq", jisrq);
				// 计算日期向后推
				jisrq = CommonFun.strNull(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.nextRiq", mapZiygzbjhz));
				jisrq1=jisrq;
			}  
			
			listZiygzbjhz.add(ziygzbjhz);
		}
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("zygzbj.saveSdZygzbjdd", listDuand);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("zygzbj.saveZygzbjmx", listZiygzbjmx);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("zygzbj.saveZygzbjhz",listZiygzbjhz);
		// 清理资源跟踪报警毛需求
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("zygzbj.delZiygzbjmxq");
	}
	
	/**
	 * 计算时段报警
	 * @param param 计算参数
	 * @param zygzlx 资源跟踪报警计算类型
	 * @param username 操作用户
	 * @param blZhizlx 是否KD
	 * @throws Exception 
	 */
	public void jisSdbj(Map param,Zygzlx zygzlx,String username,boolean blZhizlx,Map rilbcMap,Map lingjMap,Map lingjckAllMap,Map ljgysMap,
			Map countUcMap) throws Exception{
		String ziyhqrq = CommonFun.strNull(param.get("ziyhqrq"));//资源获取日期
		
		List<Ziygzbjmxq> listMxq = new ArrayList<Ziygzbjmxq>();
		//汇总时段毛需求(取按需毛需求表),按消耗点,产线,零件编号,用户中心汇总
		List<CdSdMaoxuq> listMxqmx = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("zygzbj.selectSdmxq",param);
		for (int i = 0; i < listMxqmx.size(); i++) {
			CdSdMaoxuq  cdSdMaoxq = listMxqmx.get(i);
			Ziygzbjmxq ziygzbjmxq = new Ziygzbjmxq();
			ziygzbjmxq.setLingjbh(cdSdMaoxq.getLingjbh());
			ziygzbjmxq.setUsercenter(cdSdMaoxq.getUsercenter());
			ziygzbjmxq.setZhizlx(cdSdMaoxq.getZhizlx());
			ziygzbjmxq.setXiaohd(cdSdMaoxq.getXiaohd());
			ziygzbjmxq.setChanx(cdSdMaoxq.getChanx());
			param.put("lingjbh", cdSdMaoxq.getLingjbh());
			
			if((cdSdMaoxq.getFenpqbh() != null) && (!"".endsWith(cdSdMaoxq.getFenpqbh())) ){
				//取订货库
				String ck = CommonFun.strNull(cdSdMaoxq.getDinghck());
				//如果订货库为空,取线边库
				if(ck.equals("")){
					ck = CommonFun.strNull(cdSdMaoxq.getXianbck());
				}
				//订货库,线边库都为空,不计算该条
				if(ck.equals("")){
					saveYicbj(param, "用户中心"+cdSdMaoxq.getUsercenter()+"以下参数错误:查询零件消耗点分配区仓库为空,零件编号"+cdSdMaoxq.getLingjbh()+"产线"+cdSdMaoxq.getChanx(), username);
				}else{
					param.put("id", getUUID());
					param.put("appobj", ck);
					param.put("xiaohd", CommonFun.strNull(cdSdMaoxq.getXiaohd()));
					// 仓库,产线汇总
					ziygzbjmxq.setId(getUUID());
					ziygzbjmxq.setCangkdm(ck);
				}
			}else{
				saveYicbj(param, "用户中心"+cdSdMaoxq.getUsercenter()+"零件编号"+cdSdMaoxq.getLingjbh()+"以下参数错误:分配区,物流路径仓库信息查询为空,消耗点"+cdSdMaoxq.getXiaohd()+"生产线"+cdSdMaoxq.getChanx(), username);
			}
			listMxq.add(ziygzbjmxq);
		}
		
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("zygzbj.inCDZiygzbjmxq", listMxq);
		
		//资源跟踪汇总
		List<ZiygzbjHz> listZiygzbjhz = new ArrayList<ZiygzbjHz>();
		List<Ziygzbjmx> listZiygzbjmx = new ArrayList<Ziygzbjmx>();
		List<Map> listDuand = new ArrayList<Map>();
		// 查询资源跟踪报警毛需求
		List<Ziygzbjmxq> listZiygzbjmxq = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("zygzbj.queryZiygzbjmxq");
		for (int i = 0; i < listZiygzbjmxq.size(); i++) {
			
			Ziygzbjmxq Ziygzbjmxq = listZiygzbjmxq.get(i);
			/**
			 * 资源跟踪报警汇总结果
			 */
			ZiygzbjHz ziygzbjhz = new ZiygzbjHz();
			ziygzbjhz.setZygy("1");
			ziygzbjhz.setId(getUUID());// ID
			ziygzbjhz.setCreator(username);// 创建人
			ziygzbjhz.setWeihr(username);// 维护人
			ziygzbjhz.setJislx(CommonFun.strNull(param.get("jslx")));// 计算类型
			ziygzbjhz.setMaoxqbc(CommonFun.strNull(param.get("xuqbc")));// 毛需求版次
			ziygzbjhz.setUsercenter(Ziygzbjmxq.getUsercenter());// 用户中心
			ziygzbjhz.setLingjbh(Ziygzbjmxq.getLingjbh());// 零件编号
			//ziygzbjhz.setZhizlx(Ziygzbjmxq.getZhizlx());// 制造路线
			ziygzbjhz.setCangkdm(Ziygzbjmxq.getCangkdm());// 仓库
			// 创建时间
			String time = CommonFun.getJavaTime("yyyy-MM-dd HH:mm:ss");
			ziygzbjhz.setCreate_time(time);
			ziygzbjhz.setWeihsj(time); 
			ziygzbjhz.setJissj(time);

			// 查询参数
			Map<String, String> mapZiygzbjhz = new HashMap<String, String>();
			mapZiygzbjhz.put("usercenter", ziygzbjhz.getUsercenter());
			mapZiygzbjhz.put("lingjbh", ziygzbjhz.getLingjbh());
			mapZiygzbjhz.put("cangkdm", ziygzbjhz.getCangkdm());
			mapZiygzbjhz.put("zhizlx", ziygzbjhz.getZhizlx());
			String jisrq = CommonFun.strNull(param.get("jisrq"));//计算日期
			String jisrq1 = CommonFun.strNull(param.get("jisrq"));//计算日期
			mapZiygzbjhz.put("jisrq", jisrq);
			mapZiygzbjhz.put("ziyhqrq",ziyhqrq);
			mapZiygzbjhz.put("shixrq", jisrq);
			// 查询零件
			Lingj lingj = (Lingj) lingjMap.get(mapZiygzbjhz.get("usercenter")+":"+mapZiygzbjhz.get("lingjbh"));
			if (lingj == null) {
				saveYicbj(param, "用户中心"+ziygzbjhz.getUsercenter()+"下零件有以下参数错误:零件查询为空,零件编号"+ziygzbjhz.getLingjbh(), username);
				continue;
			}
			ziygzbjhz.setLingjmc(CommonFun.strNull(lingj.getZhongwmc()));// 中文名称
			ziygzbjhz.setLingjdw(CommonFun.strNull(lingj.getDanw()));// 单位
			ziygzbjhz.setJihydm(CommonFun.strNull(lingj.getJihy()));// 计划员代码
			ziygzbjhz.setBaojlx("4");//报警类型为时段报警

			// 统计是否多用户中心使用
			ziygzbjhz.setShifdyhzxsy((Integer)countUcMap.get(ziygzbjhz.getLingjbh()));// 是否多用户中心使用

			// 查询零件仓库
			Map lingjck = (Map) lingjckAllMap.get(mapZiygzbjhz.get("usercenter")+":"+mapZiygzbjhz.get("lingjbh")+":"+mapZiygzbjhz.get("cangkdm"));
			if(lingjck == null){
				saveYicbj(param, "用户中心"+ziygzbjhz.getUsercenter()+"下零件有以下参数错误:零件仓库查询为空,零件编号"+ziygzbjhz.getLingjbh()+
						"仓库代码"+ziygzbjhz.getCangkdm(), username);
				continue;
			}
			ziygzbjhz.setBaozlx(CommonFun.strNull(lingjck.get("USBZLX")));// 包装类型
			ziygzbjhz.setBaozrl(CommonFun.getBigDecimal(ljgysMap.get(mapZiygzbjhz.get("usercenter")+":"+mapZiygzbjhz.get("lingjbh"))));// 包装容量
			ziygzbjhz.setAnqkc(CommonFun.getBigDecimal(lingjck.get("ANQKCSL")));// 安全库存
			ziygzbjhz.setZuidkcsl(CommonFun.getBigDecimal(lingjck.get("ZUIDKCSL")));// 最大库存数量
			
			// 查询MAF库存
			ziygzbjhz.setMafkc(CommonFun.getBigDecimal(zigzbjjsParam.inMafMap.get(ziygzbjhz.getUsercenter() + ziygzbjhz.getLingjbh())));

			// 查询未发运数量
			BigDecimal weify = CommonFun.getBigDecimal(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selWeify", mapZiygzbjhz));
			ziygzbjhz.setWeifysl(weify);// 未发运数量
			// 查询库存快照
			BigDecimal kuc = CommonFun.getBigDecimal(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selKckz", mapZiygzbjhz));
			ziygzbjhz.setQickc(kuc);// 期初库存
			// 查询仓库日历版次
			String rilbc = (String)rilbcMap.get(ziygzbjhz.getUsercenter()+":"+mapZiygzbjhz.get("cangkdm"));
			mapZiygzbjhz.put("rilbc", rilbc);
			// 待消耗
			BigDecimal daixh = BigDecimal.ZERO;
			// 查询资源跟踪报警毛需求明细
			//List<Ziygzbjmxq> listZiygzbjmxqmx = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("zygzbj.queryZiygzbjmxqmx", mapZiygzbjhz);
			ziygzbjhz.setDaixhl(daixh);// 待消耗
			//开始日资源 = 库存-待消耗
			BigDecimal kaisrzy = kuc;
			
			/**
			 * 按时间汇总按需毛需求
			 */
			//时段
			Calendar calendar = Calendar.getInstance();
			//从7点开始
			calendar.setTime(CommonFun.yyyyMMddHHmmss.parse(jisrq + " 07:00:00"));
			Date startDate = calendar.getTime();//开始时间
			//向后推一个小时
			calendar.add(calendar.HOUR, 1);
			Date endDate = calendar.getTime();//结束时间
			mapZiygzbjhz.put("startTime", CommonFun.yyyyMMddHHmmss.format(startDate));
			List<AnxMaoxq> listAnxmxq = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("zygzbj.selSdAanxmxq",mapZiygzbjhz);
			BigDecimal hourXqsl = BigDecimal.ZERO;//一小时内毛需求数量
			Map<String,BigDecimal> mapAnxmxq = new HashMap<String, BigDecimal>();
			for (int j = 0; j < listAnxmxq.size(); j++) {
				AnxMaoxq anxMaoxq = listAnxmxq.get(j);
				Date xhsj = CommonFun.yyyyMMddHHmmss.parse(anxMaoxq.getXhsj());//毛需求消耗时间
				//如果毛需求消耗时间大于等于开始时间
				if(xhsj.compareTo(startDate) >=0 ){
					//如果毛需求消耗时间小于结束时间
					if(xhsj.before(endDate)){
						hourXqsl = hourXqsl.add(anxMaoxq.getXiaohxs());//毛需求数量相加
						if(j == listAnxmxq.size() - 1){
							mapAnxmxq.put(CommonFun.yyyyMMddHHmmss.format(startDate), hourXqsl);
						}
					}else{
						//倒退,下次循环继续汇总本条毛需求信息
						j--;
						//小时内毛需求汇总完毕
						mapAnxmxq.put(CommonFun.yyyyMMddHHmmss.format(startDate), hourXqsl);
						hourXqsl = BigDecimal.ZERO;
						startDate = calendar.getTime();//开始时间
						//向后推一个小时
						calendar.add(calendar.HOUR, 1);
						endDate = calendar.getTime();//结束时间
					}
				}
			}
			
			//从7点开始
			calendar.setTime(CommonFun.yyyyMMddHHmmss.parse(jisrq + " 07:00:00"));
			startDate = calendar.getTime();//开始时间
			//向后推一个小时
			calendar.add(calendar.HOUR, 1);
			endDate = calendar.getTime();//结束时间
			Map<String,List<Yaohl>> mapYhl = new HashMap<String, List<Yaohl>>();
			mapZiygzbjhz.put("startTime", CommonFun.yyyyMMddHHmmss.format(startDate));
			//查询到货
			List<Yaohl> listYhl = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("zygzbj.querySdYhl", mapZiygzbjhz);
			for (int j = 0; j < listYhl.size(); j++) {
				Yaohl yhl = listYhl.get(j);
				Date yhlsj = CommonFun.yyyyMMddHHmmss.parse(getYhlSj(yhl));//毛需求消耗时间
				//如果毛需求消耗时间大于等于开始时间
				if(yhlsj.compareTo(startDate) >=0 ){
					//如果毛需求消耗时间小于结束时间
					if(yhlsj.before(endDate)){
						List<Yaohl> hourDaoh = mapYhl.get(CommonFun.yyyyMMddHHmmss.format(startDate));
						if(hourDaoh == null){
							hourDaoh = new ArrayList();//一小时内毛需求数量
						}
						hourDaoh.add(yhl);
						mapYhl.put(CommonFun.yyyyMMddHHmmss.format(startDate), hourDaoh);
					}else{
						//倒退,下次循环继续汇总本条毛需求信息
						j--;
						startDate = calendar.getTime();//开始时间
						//向后推一个小时
						calendar.add(calendar.HOUR, 1);
						endDate = calendar.getTime();//结束时间
					}
				}
			}
			
			//时段算2天,每天按24小时,每隔1小时汇总,计算需求和已交付
			for (int j = 0; j < 2; j++) {
				Class cl = ZiygzbjHz.class;
				PropertyDescriptor pd = new PropertyDescriptor("xuq" + j, cl);
				PropertyDescriptor pd2 = new PropertyDescriptor("yijf" + j, cl);
				// 需求数量
				BigDecimal xuqsl = BigDecimal.ZERO;
				//时段
				//Calendar calendar = Calendar.getInstance();
				//从00点开始
				calendar.setTime(CommonFun.yyyyMMddHHmmss.parse(jisrq + " 07:00:00"));
				//每天24小时,每隔1小时汇总
				String rq= jisrq;
				for (int k = 0; k < 24; k++) {
					//开始时间
					String startTime = CommonFun.yyyyMMddHHmmss.format(calendar.getTime());
					BigDecimal hourXusl = CommonFun.getBigDecimal(mapAnxmxq.get(startTime));
					mapZiygzbjhz.put("startTime",startTime);
					//向后推一个小时
					calendar.add(calendar.HOUR, 1);
					//结束时间
					String endTime = CommonFun.yyyyMMddHHmmss.format(calendar.getTime());
					mapZiygzbjhz.put("endTime",endTime);
					
					BigDecimal daohsl = BigDecimal.ZERO;//到货数量
					//当计算第二个工作日的开始时,需考虑第一个工作日到第二个工作之间非工作日的到货情况
					if(k ==0 ){
						Calendar calendar1 = Calendar.getInstance();
						calendar1.setTime(CommonFun.yyyyMMddHHmmss.parse(jisrq1 + " 07:00:00"));
						calendar1.add(calendar1.DATE, -1);
						mapZiygzbjhz.put("riq1", CommonFun.sdf.format(calendar1.getTime()));
						boolean shifgzr = CommonFun.getBigDecimal(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.shifGZR", mapZiygzbjhz)).intValue()>0?true:false;
						if(!shifgzr){
							rq=CommonFun.strNull(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.previousRiq", mapZiygzbjhz));
							Calendar calendar2 = Calendar.getInstance();
							calendar2.setTime(CommonFun.sdf.parse(rq));
							calendar2.add(calendar2.DAY_OF_YEAR, 1);
							rq = CommonFun.sdf.format(calendar2.getTime());
						}
						startTime = rq + startTime.substring(10);//保留上一个日期
						mapZiygzbjhz.put("startTime",startTime);
						daohsl = selSdDaoh(mapZiygzbjhz);
					}else{
						List<Yaohl> daoh = mapYhl.get(startTime);
						if(daoh != null){
							daohsl = getSdDaoh(mapZiygzbjhz, daoh);
						}
					}
					// 已交付
					kaisrzy = kaisrzy.add(daohsl).subtract(hourXusl);
					//保存资源跟踪报警明细
					Ziygzbjmx ziygzbjmx = getZygzbjmx(ziygzbjhz, j, k, hourXusl, kaisrzy, startTime, endTime);
					listZiygzbjmx.add(ziygzbjmx);
					//保存断点信息
					Map duandMap = getDuand(kaisrzy, ziygzbjhz, endTime, String.valueOf(k));
					if(duandMap != null){
						listDuand.add(duandMap);
					}
					//汇总需求数量
					xuqsl = xuqsl.add(hourXusl);
				}
				pd.getWriteMethod().invoke(ziygzbjhz, xuqsl.setScale(0,BigDecimal.ROUND_HALF_UP));
				pd2.getWriteMethod().invoke(ziygzbjhz, kaisrzy.setScale(0,BigDecimal.ROUND_HALF_UP));
				mapZiygzbjhz.put("riq", jisrq);
				// 计算日期向后推
				jisrq = CommonFun.strNull(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.nextRiq", mapZiygzbjhz));
				jisrq1=jisrq;
			}  
			listZiygzbjhz.add(ziygzbjhz);
		}
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("zygzbj.saveSdZygzbjdd", listDuand);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("zygzbj.saveZygzbjmx", listZiygzbjmx);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("zygzbj.saveZygzbjhz",listZiygzbjhz);
		// 清理资源跟踪报警毛需求
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("zygzbj.delZiygzbjmxq");
	}
	
	

	/**
	 * 计算周期资源交付
	 * 
	 * @param param
	 *            计算参数
	 * @param jisrq
	 *            计算日期
	 * @param jisff
	 *            计算方法
	 * @param ziygzbjhz
	 *            资源跟踪报警汇总
	 * @param kaisrzy
	 *            开始日资源
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws IntrospectionException
	 * @throws ParseException 
	 */
	public ZiygzbjHz jisZqJf(Map param, String jisrq, String jisff, ZiygzbjHz ziygzbjhz, BigDecimal kaisrzy,
			String lingjsx,Map nianzqMap,Map nianzxMap) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException,
			IntrospectionException, ParseException {
		Class cl = ZiygzbjHz.class;
		/*if(param.get("lingjbh").equals("7903053025")){
			System.out.println("hello");
		}*/
		/**
		 * 计算第一个周期报警
		 */
		// 查询计算日期年周期
		String usercenter = (String)param.get("usercenter");
		String banc = (String)param.get("rilbc");
		String key = usercenter+":"+banc+":"+jisrq;
		String jisNzq = (String)nianzqMap.get(key);
		param.put("nianzq", jisNzq);
		// 查询计算日期年周序
		String jisNzx = (String)nianzxMap.get(key);;
		param.put("nianzx", jisNzx);
		/*if(ziygzbjhz.getLingjbh().equals("7552160880")){
			System.out.println("hello");
		}*/
		/**
		 * 计算第一周报警
		 */
		// 计算CMJ
		BigDecimal CMJ = jisCmj(param, jisNzq, jisff);
		param.put("xuqlx", "nianzx");
		// 第一周最大日期
		Map zxMaxMin = (Map) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selMaxMin", param);
		String maxRq = CommonFun.strNull(zxMaxMin.get("MAXRIQ"));
		BigDecimal days = daysOfDate(jisrq, maxRq, param);
		BigDecimal xuqsl = CMJ.multiply(days);
		BigDecimal yxuqsl = xuqsl;
		// 比较需求和消耗
		xuqsl = compareZongXq(lingjsx, param, jisrq, maxRq, xuqsl);
		// 第一周需求
		ziygzbjhz.setXuq0(yxuqsl.setScale(0,BigDecimal.ROUND_HALF_UP));
		param.put("riq1", jisrq);
		param.put("riq2", maxRq);
		// 第一周已交付 = 开始日资源 + 到货 - 需求
		ziygzbjhz.setYijf0(kaisrzy.add(selZRiqDaoh(param))
				.subtract(xuqsl).setScale(0,BigDecimal.ROUND_HALF_UP));
		saveDuand(ziygzbjhz.getYijf0(), ziygzbjhz, jisrq, "0");
		// 查询计算周期内最大年周序
		param.put("xuqlx", "nianzq");
		// 查询周期具体信息
		Map zqMaxMin = (Map) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selMaxMin", param);
		String maxNzx = CommonFun.strNull(zqMaxMin.get("MAXNIANZX"));
		// 周序差
		int num = CommonFun.getBigDecimal(maxNzx).subtract(CommonFun.getBigDecimal(jisNzx)).intValue();
		ziygzbjhz.setNum(num);
		// 期初库存
		BigDecimal qickc = ziygzbjhz.getYijf0();
		for (int k = 1; k <= num; k++) {
			PropertyDescriptor pd = new PropertyDescriptor("xuq" + k, cl);
			PropertyDescriptor pd2 = new PropertyDescriptor("yijf" + k, cl);
			// 下一周序
			jisNzx = CommonFun.strNull(CommonFun.getBigDecimal(jisNzx).add(BigDecimal.ONE));
			param.put("lxValue", jisNzx);
			param.put("xuqlx", "nianzx");
			param.put("nianzx", jisNzx);
			Map maxMin = (Map) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selMaxMin", param);
			String maxriq = CommonFun.strNull(maxMin.get("MAXRIQ"));
			String minriq = CommonFun.strNull(maxMin.get("MINRIQ"));
			param.put("riq1", minriq);
			param.put("riq2", maxriq);
			// 查询工作天数
			BigDecimal workDays = CommonFun.getBigDecimal(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selWorkDays", param));
			// 计算需求
			xuqsl = CMJ.multiply(workDays);
			pd.getWriteMethod().invoke(ziygzbjhz, xuqsl.setScale(0,BigDecimal.ROUND_HALF_UP));
			// 比较需求和消耗
			xuqsl = compareZongXq(lingjsx, param, minriq,
					maxriq, xuqsl);
			// 计算已交付
			qickc = qickc.add(selZRiqDaoh(param)).subtract(xuqsl);
			pd2.getWriteMethod().invoke(ziygzbjhz, qickc.setScale(0,BigDecimal.ROUND_HALF_UP));
			saveDuand(qickc, ziygzbjhz,minriq, String.valueOf(k));
		}
		/**
		 * 计算后三个周期报警
		 */
		for (int k = 0; k < 3; k++) {
			// 总需求
			PropertyDescriptor pd = new PropertyDescriptor("zongxq" + k, cl);
			// 总已交付
			PropertyDescriptor pd2 = new PropertyDescriptor("zongyjf" + k, cl);
			// 下一年周期
			jisNzq = CommonFun.strNull(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.nextZq", param));
			param.put("nianzq", jisNzq);
			param.put("xuqlx", "nianzq");
			// 计算CMJ
			CMJ = jisCmj(param, jisNzq, jisff);
			// 查询该周期内最小周序
			// 查询周期具体信息
			zqMaxMin = (Map) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selMaxMin", param);
			String minNzx = CommonFun.strNull(zqMaxMin.get("MINNIANZX"));
			// 查询该周期内最大周序
			maxNzx = CommonFun.strNull(zqMaxMin.get("MAXNIANZX"));
			// 周序差
			int zxNum = CommonFun.getBigDecimal(maxNzx).subtract(CommonFun.getBigDecimal(minNzx)).intValue();
			// 总需求数量
			BigDecimal zongXq = BigDecimal.ZERO;
			// 计算周期内周报警
			for (int l = 0; l <= zxNum; l++) {
				// 下一周序
				jisNzx = CommonFun.strNull(CommonFun.getBigDecimal(minNzx).add(BigDecimal.valueOf(l)));
				param.put("xuqlx", "nianzx");
				param.put("lxValue", jisNzx);
				param.put("nianzx", jisNzx);
				// 查询周序工作天数
				BigDecimal workDays = CommonFun.getBigDecimal(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selWorkDays", param));
				// 需求数量 = CMJ * 工作天数
				xuqsl = CMJ.multiply(workDays);
				BigDecimal yxuqsls = xuqsl;
				zxMaxMin = (Map) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selMaxMin", param);
				String maxriq = CommonFun.strNull(zxMaxMin.get("MAXRIQ"));
				String minriq = CommonFun.strNull(zxMaxMin.get("MINRIQ"));
				param.put("riq1", minriq);
				param.put("riq2", maxriq);
				zongXq = zongXq.add(xuqsl);// 总需求叠加
				// 比较需求和消耗
				xuqsl = compareZongXq(lingjsx, param, minriq,
						maxriq, xuqsl);
				// 已交付 = 期初库存 + 到货 - 需求
				qickc = qickc.add(selZRiqDaoh(param)).subtract(
						xuqsl);
				// 查询周期具体信息
				saveDuand(qickc, ziygzbjhz, minriq,
						Integer.toString(k) + Integer.toString(l));
				// 保存资源跟踪报警明细
				saveZygzbjmx(ziygzbjhz, k, l, yxuqsls, qickc, minriq,
						maxriq);
			}
			pd.getWriteMethod().invoke(ziygzbjhz, zongXq.setScale(0,BigDecimal.ROUND_HALF_UP));
			pd2.getWriteMethod().invoke(ziygzbjhz, qickc.setScale(0,BigDecimal.ROUND_HALF_UP));
		}
		return ziygzbjhz;
	}

	/**
	 * 计算周期资源交付
	 * 
	 * @param param
	 *            计算参数
	 * @param jisrq
	 *            计算日期
	 * @param jisff
	 *            计算方法
	 * @param ziygzbjhz
	 *            资源跟踪报警汇总
	 * @param kaisrzy
	 *            开始日资源
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws IntrospectionException
	 */
	public ZiygzbjHz jisZqCxJf(Map param, String jisrq, String jisff, ZiygzbjHz ziygzbjhz, String lingjsx,Map nianzqMap,Map nianzxMap)
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IntrospectionException {
		Class cl = ZiygzbjHz.class;
		/**
		 * 计算第一个周期报警
		 */
		// 查询计算日期年周期
		String usercenter = (String)param.get("usercenter");
		String banc = (String)param.get("rilbc");
		String key = usercenter+":"+banc+":"+jisrq;
		String jisNzq = (String)nianzqMap.get(key);
		param.put("nianzq", jisNzq);
		// 查询计算日期年周序
		String jisNzx = (String)nianzxMap.get(key);;
		param.put("nianzx", jisNzx);
		param.put("xuqlx", "nianzq");
		/**
		 * 计算第一周报警
		 */
		// 计算CMJ
		BigDecimal CMJ = jisCmj(param, jisNzq, jisff);
		param.put("xuqlx", "nianzx");
		// 第一周最大日期
		Map zxMaxMin = (Map) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selMaxMin", param);
		String maxRq = CommonFun.strNull(zxMaxMin.get("MAXRIQ"));
		BigDecimal days = daysOfDate(jisrq, maxRq, param);
		BigDecimal xuqsl = CMJ.multiply(days);
		ziygzbjhz.setNianzx0(maxRq);
		// 第一周需求
		ziygzbjhz.setXuq0(xuqsl);
		// 查询计算周期内最大年周序
		param.put("xuqlx", "nianzq");
		// 查询周期具体信息
		Map zqMaxMin = (Map) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selMaxMin", param);
		String maxNzx = CommonFun.strNull(zqMaxMin.get("MAXNIANZX"));
		// 周序差
		int num = CommonFun.getBigDecimal(maxNzx).subtract(CommonFun.getBigDecimal(jisNzx)).intValue();
		ziygzbjhz.setNum(num);
		for (int k = 1; k <= num; k++) {
			PropertyDescriptor pd = new PropertyDescriptor("xuq" + k, cl);
			PropertyDescriptor pd2 = new PropertyDescriptor("nianzx" + k, cl);
			// 下一周序
			jisNzx = CommonFun.strNull(CommonFun.getBigDecimal(jisNzx).add(BigDecimal.ONE));
			param.put("lxValue", jisNzx);
			param.put("xuqlx", "nianzx");
			// 查询工作天数
			BigDecimal workDays = CommonFun.getBigDecimal(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selWorkDays", param));
			// 计算需求
			xuqsl = CMJ.multiply(workDays);

			pd.getWriteMethod().invoke(ziygzbjhz, xuqsl);
			pd2.getWriteMethod().invoke(ziygzbjhz, jisNzx);
		}

		/**
		 * 计算后三个周期报警
		 */
		for (int k = 0; k < 3; k++) {
			PropertyDescriptor pd = new PropertyDescriptor("mx" + k, cl);
			// 下一年周期
			jisNzq = CommonFun.strNull(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.nextZq", param));
			if(jisNzq.isEmpty()){
				saveYicbj(param, "用户中心"+ziygzbjhz.getUsercenter()+"下日历版次"+param.get("rilbc")+"有以下参数错误:"+param.get("nianzq")+"为空,计算日期为"+jisrq, "");
				break;
			}
			param.put("nianzq", jisNzq);
			param.put("xuqlx", "nianzq");
			// 计算CMJ
			CMJ = jisCmj(param, jisNzq, jisff);
			// 查询该周期内最小周序
			// 查询周期具体信息
			zqMaxMin = (Map) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selMaxMin", param);
			String minNzx = CommonFun.strNull(zqMaxMin.get("MINNIANZX"));
			// 查询该周期内最大周序
			maxNzx = CommonFun.strNull(zqMaxMin.get("MAXNIANZX"));
			// 周序差
			int zxNum = CommonFun.getBigDecimal(maxNzx).subtract(CommonFun.getBigDecimal(minNzx)).intValue();
			List<Ziygzbjmx> list = new ArrayList<Ziygzbjmx>();
			// 计算周期内周报警
			for (int l = 0; l <= zxNum; l++) {
				param.put("xuqlx", "nianzx");
				param.put("lxValue", minNzx);

				// 查询周序工作天数
				BigDecimal workDays = CommonFun.getBigDecimal(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selWorkDays", param));
				// 需求数量 = CMJ * 工作天数
				xuqsl = CMJ.multiply(workDays);
				Ziygzbjmx ziygzbjmx = new Ziygzbjmx();
				ziygzbjmx.setNianzx(minNzx);
				ziygzbjmx.setXuql(xuqsl);
				ziygzbjmx.setHuizxh(k);
				ziygzbjmx.setMingxxh(l);
				ziygzbjmx.setStarttime(CommonFun.strNull(zqMaxMin.get("MINRIQ")));
				ziygzbjmx.setEndtime(CommonFun.strNull(zqMaxMin.get("MAXRIQ")));
				list.add(ziygzbjmx);
				// 下一周序
				minNzx = CommonFun.strNull(CommonFun.getBigDecimal(minNzx).add(BigDecimal.ONE));
			}
			pd.getWriteMethod().invoke(ziygzbjhz, list);
		}
		return ziygzbjhz;
	}

	/**
	 * 比较需求和消耗
	 * 
	 * @param lingjsx
	 *            零件属性
	 * @param param
	 *            参数
	 * @param jisrq
	 *            消耗日期
	 * @param xuqsl
	 *            需求数量
	 * @return 需求数量
	 */
	public BigDecimal compareXq(String lingjsx, Map param, String jisrq, BigDecimal xuqsl) {
		// 如果是辅料
		if (lingjsx.equals("M")) {
			param.put("shixrq", jisrq);
			String nextRiq = (String)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.nextRiq", param);
			param.put("nextRiq", nextRiq);
			BigDecimal xiaoh = CommonFun.getBigDecimal(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selXh", param));
			// 如果待消耗小于消耗,则取消耗
			if (xuqsl.compareTo(xiaoh) < 0) {
				return xiaoh;
			}
		}
		return xuqsl;
	}

	/**
	 * 比较需求和消耗
	 * 
	 * @param lingjsx
	 *            零件属性
	 * @param param
	 *            参数
	 * @param riq1
	 *            消耗日期1
	 * @param riq2
	 *            消耗日期2
	 * @param xuqsl
	 *            需求数量
	 * @return 需求数量
	 */
	public BigDecimal compareZongXq(String lingjsx, Map param, String riq1, String riq2, BigDecimal xuqsl) {
		// 如果为辅料
		if (lingjsx.equals("M")) {
			param.put("riq1", riq1);
			param.put("riq2", riq2);
			BigDecimal xiaoh = CommonFun.getBigDecimal(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selZongXh", param));
			// 如果待消耗小于消耗,则取消耗
			if (xuqsl.compareTo(xiaoh) < 0) {
				return xiaoh;
			}
		}
		return xuqsl;
	}

	/**
	 * 计算周序交付
	 * 
	 * @param param
	 *            计算参数
	 * @param jisrq
	 *            计算日期
	 * @param jisff
	 *            计算方法
	 * @param ziygzbjhz
	 *            资源跟踪报警汇总
	 * @param kaisrzy
	 *            开始日资源
	 * @return 资源跟踪报警汇总
	 * @throws IntrospectionException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws ParseException 
	 */
	public ZiygzbjHz jisCxZxJf(Map param, String jisrq, String jisff, ZiygzbjHz ziygzbjhz, String lingjsx,Map nianzxMap)
			throws IntrospectionException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, ParseException {
		Class cl = ZiygzbjHz.class;
		// 查询计算日期年周序
		String usercenter = (String)param.get("usercenter");
		String banc = (String)param.get("rilbc");
		String key = usercenter+":"+banc+":"+jisrq;
		// 查询计算日期年周序
		String jisNzx = (String)nianzxMap.get(key);;
		param.put("nianzx", jisNzx);
		/**
		 * 计算第一周报警
		 */
		// 计算CMJ
		BigDecimal CMJ = jisCmj(param, jisNzx, jisff);
		// 查询周序最大,最小日期
		Map zxMaxMin = (Map) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selMaxMin", param);
		// 周序内最大日期
		String maxRiq = CommonFun.strNull(zxMaxMin.get("MAXRIQ"));
		// 日期差
		int num = daysOfDate(jisrq, maxRiq, param).intValue();
		ziygzbjhz.setNum(num);
		String riq = jisrq;
		// 计算第一周
		for (int i = 0; i < num; i++) {
			int days = CommonFun.daysOfDate(DateUtil.stringToDateYMD(riq),DateUtil.stringToDateYMD(jisrq));
			PropertyDescriptor pd = new PropertyDescriptor("xuq" + days, cl);
			PropertyDescriptor pd2 = new PropertyDescriptor("nianzx" + days, cl);
			param.put("riq", riq);
			// 计算日期
			pd2.getWriteMethod().invoke(ziygzbjhz, riq);
			pd.getWriteMethod().invoke(ziygzbjhz, CMJ);
			// 计算日期向后推
			riq = CommonFun.strNull(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.nextRiq", param));
		}

		// 计算后三周
		for (int i = 0; i < 3; i++) {
			PropertyDescriptor pd = new PropertyDescriptor("mx" + i, cl);
			// 计算周序向后推
			jisNzx = CommonFun.strNull(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.nextZ", param));
			CMJ = jisCmj(param, jisNzx, jisff);
			param.put("nianzx", jisNzx);
			//zxMaxMin = (Map) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selMaxMin", param);
			// 周序内最大日期
			//maxRiq = CommonFun.strNull(zxMaxMin.get("MAXRIQ"));
			// 周序内最小日期
			//String minRiq = CommonFun.strNull(zxMaxMin.get("MINRIQ"));
			// 天数差
			//num = daysOfDate(minRiq, maxRiq, param).intValue();
			List<Map> cvMap =(List<Map>)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("zygzbj.selCVByzqorzx", param);
			int size = cvMap.size();
			Map minMap =cvMap.get(0);
			String minRiq = (String)minMap.get("RIQ");
			Map maxMap =cvMap.get(size-1);
			String tempMaxRiq = (String)maxMap.get("RIQ");
			BigDecimal zongxq = BigDecimal.ZERO;
			String mxRiq = minRiq;
			List<Ziygzbjmx> list = new ArrayList<Ziygzbjmx>();
			for (int j = 0; j < size; j++) {
				Map map = cvMap.get(j);
				String tempriq = (String)map.get("RIQ");
				param.put("riq", tempriq);
				Ziygzbjmx ziygzbjmx = new Ziygzbjmx();
				ziygzbjmx.setXuql(CMJ);
				ziygzbjmx.setHuizxh(i);
				ziygzbjmx.setMingxxh(j);
				ziygzbjmx.setStarttime(minRiq);
				ziygzbjmx.setEndtime(tempMaxRiq);
				ziygzbjmx.setNianzx(mxRiq);
				list.add(ziygzbjmx);
				
				BigDecimal xuqsl = CMJ;
				zongxq = zongxq.add(xuqsl);
				// 计算日期向后推
				//mxRiq = CommonFun.strNull(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.nextRiq", param));
			}
			pd.getWriteMethod().invoke(ziygzbjhz, list);
		}
		return ziygzbjhz;
	}

	/**
	 * 计算周序交付
	 * 
	 * @param param
	 *            计算参数
	 * @param jisrq
	 *            计算日期
	 * @param jisff
	 *            计算方法
	 * @param ziygzbjhz
	 *            资源跟踪报警汇总
	 * @param kaisrzy
	 *            开始日资源
	 * @return 资源跟踪报警汇总
	 * @throws IntrospectionException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws ParseException 
	 */
	public ZiygzbjHz jisZxJf(Map param, String jisrq, String jisff, ZiygzbjHz ziygzbjhz, BigDecimal kaisrzy,
			String lingjsx,Map nianzxMap) throws IntrospectionException, IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, ParseException {
		Class cl = ZiygzbjHz.class;
		// 查询计算日期年周序
		// 查询计算日期年周序
		String usercenter = (String)param.get("usercenter");
		String banc = (String)param.get("rilbc");
		String key = usercenter+":"+banc+":"+jisrq;
		// 查询计算日期年周序
		String jisNzx = (String)nianzxMap.get(key);;
		param.put("nianzx", jisNzx);
		/**
		 * 计算第一周报警
		 */
		// 计算CMJ
		BigDecimal CMJ = jisCmj(param, jisNzx, jisff);
		// 查询周序最大,最小日期
		Map zxMaxMin = (Map) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selMaxMin", param);
		// 周序内最大日期
		String maxRiq = CommonFun.strNull(zxMaxMin.get("MAXRIQ"));
		// 日期差
		int num = daysOfDate(jisrq, maxRiq, param).intValue();
		ziygzbjhz.setNum(num);
		String riq = jisrq;
		// 计算第一周
		
		for (int i = 0; i < num; i++) {
			int days = CommonFun.daysOfDate(DateUtil.stringToDateYMD(riq),DateUtil.stringToDateYMD(jisrq));
		
			PropertyDescriptor pd = new PropertyDescriptor("xuq" + days, cl);
			PropertyDescriptor pd2 = new PropertyDescriptor("yijf" + days, cl);
			param.put("riq", riq);
			BigDecimal xuqsl = CMJ;
			xuqsl = compareXq(lingjsx, param, riq, CMJ);
			pd.getWriteMethod().invoke(ziygzbjhz, CMJ.setScale(0,BigDecimal.ROUND_HALF_UP));
			// 计算已交付
			String lastJisr = (String)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.lastRiq",param);
			param.put("lastJisr", lastJisr);
			kaisrzy = kaisrzy.add(selRiDaohL(param)).subtract(xuqsl);
			pd2.getWriteMethod().invoke(ziygzbjhz, kaisrzy.setScale(0,BigDecimal.ROUND_HALF_UP));
			saveDuand(kaisrzy, ziygzbjhz, riq, String.valueOf(i));
			// 计算日期向后推
			riq = CommonFun.strNull(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.nextRiq", param));
		}
		
		
		
		// 计算后三周
		for (int i = 0; i < 3; i++) {
			
			PropertyDescriptor pd = new PropertyDescriptor("zongxq" + i, cl);
			PropertyDescriptor pd2 = new PropertyDescriptor("zongyjf" + i, cl);
			// 计算周序向后推
			jisNzx = CommonFun.strNull(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.nextZ", param));
			CMJ = jisCmj(param, jisNzx, jisff);
			param.put("nianzx", jisNzx);
			//zxMaxMin = (Map) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selMaxMin", param);
			// 周序内最大日期
			//maxRiq = CommonFun.strNull(zxMaxMin.get("MAXRIQ"));
			// 周序内最小日期
			//String minRiq = CommonFun.strNull(zxMaxMin.get("MINRIQ"));
			//num = daysOfDate(minRiq, maxRiq, param).intValue();
			List<Map> cvMap =(List<Map>)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("zygzbj.selCVByzqorzx", param);
			if(cvMap!=null && cvMap.size()>0){
				int size = cvMap.size();
				Map minMap =cvMap.get(0);
				String minRiq = (String)minMap.get("RIQ");
				Map maxMap =cvMap.get(size-1);
				String tempMaxRiq = (String)maxMap.get("RIQ");
				BigDecimal zongxq = BigDecimal.ZERO;
				String mxRiq = minRiq;
				for (int j = 0; j < size; j++) {
					Map map = cvMap.get(j);
					String tempriq = (String)map.get("RIQ");
					param.put("riq", tempriq);
					BigDecimal xuqsl = CMJ;
					BigDecimal yxuqsl = xuqsl;
					xuqsl = compareXq(lingjsx, param, tempriq, CMJ);
					zongxq = zongxq.add(CMJ);
					String lastJisr = (String)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.lastRiq",param);
					param.put("lastJisr", lastJisr);
					// 计算已交付
					kaisrzy = kaisrzy.add(selRiDaohL(param)).subtract(xuqsl);
					// 计算日期向后推
					//mxRiq = CommonFun.strNull(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.nextRiq", param));
					// 保存资源跟踪报警明细
					saveZygzbjmx(ziygzbjhz, i, j, yxuqsl, kaisrzy, tempriq, tempriq);
					saveDuand(kaisrzy, ziygzbjhz, tempriq, Integer.toString(i) + Integer.toString(j));
				}
				// 设置需求
				pd.getWriteMethod().invoke(ziygzbjhz, zongxq.setScale(0,BigDecimal.ROUND_HALF_UP));
				// 设置已交付
				pd2.getWriteMethod().invoke(ziygzbjhz, kaisrzy.setScale(0,BigDecimal.ROUND_HALF_UP));
			} else {
				String username =  param.get("username").toString();
				saveYicbj(param, "年周序为:"+CommonFun.strNull(param.get("nianzx"))+"无工作日",username);
			}
		}
		return ziygzbjhz;
	}

	/**
	 * 计算CMJ
	 * 
	 * @param param
	 *            计算参数
	 * @param lxValue
	 *            周期或者周序值
	 * @param jisff
	 *            计算方法
	 * @return CMJ
	 */
	public BigDecimal jisCmj(Map param, String lxValue, String jisff) {
		BigDecimal CMJ = BigDecimal.ZERO;
		// 周期或周序值
		param.put("lxValue", lxValue);
		BigDecimal xuqsl = BigDecimal.ZERO;// 需求数量
		// 计算方法为1,计算仓库CMJ
		if ("1".equals(jisff)) {
			// 汇总仓库需求
			List<Ziygzbjmxq> listZiygzbjmxq = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("zygzbj.queryZiygzbjmxqmxzq", param);
			for (int i = 0; i < listZiygzbjmxq.size(); i++) {
				Ziygzbjmxq ziygzbjmxq = listZiygzbjmxq.get(i);
				// 根据产线,消耗比例,汇总仓库需求数量
				param.put("chanx", ziygzbjmxq.getChanx());
				xuqsl = xuqsl.add(CommonFun.getBigDecimal(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.queryMxqsl", param)).multiply(
						CommonFun.getBigDecimal(ziygzbjmxq.getXiaohbl())));
			}
			// 计算方法为2,计算产线CMJ
		} else if ("2".equals(jisff)) {
			// 查询毛需求数量
			xuqsl = CommonFun.getBigDecimal(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.queryMxqsl", param)).multiply(
					CommonFun.getBigDecimal(param.get("xiaohbl")));
		}
		// 查询工作天数
		BigDecimal workDays = CommonFun.getBigDecimal(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selWorkDays", param));
		//如果工作天数不为0
		if(workDays.compareTo(BigDecimal.ZERO) > 0){
			CMJ = xuqsl.divide(workDays, 5, BigDecimal.ROUND_CEILING);
		}
		// 计算CMJ
		return CMJ;
	}

	/**
	 * 根据计算类型删除资源跟踪报警明细
	 * 
	 * @param map
	 *            参数
	 * @return
	 */
	public int delZygzbjhz(Map map) {
		// 清楚资源跟踪报警毛需求
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("zygzbj.delZiygzbjmxq");
		// 删除汇总
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("zygzbj.delZygzbjhz", map);
		// 删除断点
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("zygzbj.delZiygzbjdd", map);
		// 删除明细
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("zygzbj.delZygzbjmx", map);
	}

	/**
	 * 根据零件编号,生产线编号查询订货库
	 * 
	 * @param lingjbh
	 *            零件编号
	 * @param shengcxbh
	 *            生产线编号
	 * @param gonghms
	 *            供货模式
	 * @return 订货库
	 */
	public Map selCk(Map map) {
		return (Map) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selCk", map);// 查询物流路径仓库
	}

	/**
	 * 获取年周期
	 * 
	 * @param map
	 *            查询参数
	 * @param riq
	 *            日期
	 * @return 年周期
	 */
	public String selNzq(Map map, String riq) {
		map.put("riq", riq);
		return CommonFun.strNull(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selNzq", map));
	}

	/**
	 * 获取年周期
	 * 
	 * @param map
	 *            查询参数
	 * @param riq
	 *            日期
	 * @return 年周期
	 */
	public String selXuqlx(Map map, String riq) {
		map.put("riq", riq);
		return CommonFun.strNull(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selXuqlx", map));
	}

	/**
	 * 查询年周序
	 * 
	 * @param map
	 *            查询参数
	 * @param riq
	 *            日期
	 * @return 年周序
	 */
	public String selNzx(Map map, String riq) {
		map.put("riq", riq);
		return CommonFun.strNull(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selNzx", map));
	}

	/**
	 * 统计零件是否多用户中心使用
	 * 
	 * @param lingjbh
	 *            零件编号
	 * @return 统计结果 1:多用户中心使用,0:单用户中心使用
	 */
	public Map countLingjUc() {
		List<Map> countList = (List) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("zygzbj.countAllLingjUc");
		// 如果数量大于1,则为多用户中心使用,返回1
		Map countUclingjMap = new HashMap();
		for(int i=0;i<countList.size();i++){
			Map map = countList.get(i);
			String _lingjbh = (String)map.get("LINGJBH");
			BigDecimal countLingjbh = (BigDecimal)map.get("COUNTLINGJBH");
			if (countLingjbh.intValue() > 1) {
				countUclingjMap.put(_lingjbh, 1);
				// 否则,为单用户中心使用,返回0
			} else {
				countUclingjMap.put(_lingjbh, 0);
			}
		}
		return countUclingjMap;
	}

	/**
	 * 保存资源跟踪报警明细
	 * 
	 * @param hz
	 *            资源跟踪报警汇总信息
	 * @param xuh
	 *            汇总序号
	 * @param mxxuh
	 *            明细序号
	 * @param xuqsl
	 *            需求数量
	 * @param yijf
	 *            已交付
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 */
	public Ziygzbjmx getZygzbjmx(ZiygzbjHz hz, Integer xuh, Integer mxxuh, BigDecimal xuqsl, BigDecimal yijf,
			String startTime, String endTime) {
		Ziygzbjmx mx = new Ziygzbjmx();
		mx.setMingxxh(mxxuh);// 明细序号
		mx.setId(getUUID());// ID
		mx.setZiyhzid(hz.getId());// 资源汇总ID
		mx.setHuizxh(xuh);// 序号
		mx.setBaojlx(hz.getBaojlx());// 报警类型
		mx.setUsercenter(hz.getUsercenter());// 用户中心
		mx.setCangkdm(hz.getCangkdm());// 仓库
		mx.setZhizlx(hz.getZhizlx());// 制造路线
		mx.setLingjbh(hz.getLingjbh());// 零件编号
		mx.setLingjmc(hz.getLingjmc());// 零件名称
		mx.setStarttime(startTime);// 开始日期
		mx.setEndtime(endTime);// 结束日期
		mx.setXuql(xuqsl.setScale(0,BigDecimal.ROUND_HALF_UP));// 需求数量.
		mx.setYijfl(yijf.setScale(0,BigDecimal.ROUND_HALF_UP));// 已交付数量
		BigDecimal tempWeifysl = BigDecimal.ZERO;
		if(hz.getWeifysl()!=null){
			tempWeifysl= hz.getWeifysl().setScale(0,BigDecimal.ROUND_HALF_UP);
		}
		mx.setWeifyl(tempWeifysl);// 未发运
		mx.setJislx(hz.getJislx());// 计算类型
		return mx;
	}
	
	/**
	 * 保存资源跟踪报警明细
	 * 
	 * @param hz
	 *            资源跟踪报警汇总信息
	 * @param xuh
	 *            汇总序号
	 * @param mxxuh
	 *            明细序号
	 * @param xuqsl
	 *            需求数量
	 * @param yijf
	 *            已交付
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 */
	public int saveZygzbjmx(ZiygzbjHz hz, Integer xuh, Integer mxxuh, BigDecimal xuqsl, BigDecimal yijf,
			String startTime, String endTime) {
		Ziygzbjmx mx = new Ziygzbjmx();
		mx.setMingxxh(mxxuh);// 明细序号
		mx.setId(getUUID());// ID
		mx.setZiyhzid(hz.getId());// 资源汇总ID
		mx.setHuizxh(xuh);// 序号
		mx.setBaojlx(hz.getBaojlx());// 报警类型
		mx.setUsercenter(hz.getUsercenter());// 用户中心
		mx.setCangkdm(hz.getCangkdm());// 仓库
		mx.setZhizlx(hz.getZhizlx());// 制造路线
		mx.setLingjbh(hz.getLingjbh());// 零件编号
		mx.setLingjmc(hz.getLingjmc());// 零件名称
		mx.setStarttime(startTime);// 开始日期
		mx.setEndtime(endTime);// 结束日期
		mx.setXuql(xuqsl.setScale(0,BigDecimal.ROUND_HALF_UP));// 需求数量.
		mx.setYijfl(yijf.setScale(0,BigDecimal.ROUND_HALF_UP));// 已交付数量
		BigDecimal tempWeifysl = BigDecimal.ZERO;
		if(hz.getWeifysl()!=null){
			tempWeifysl= hz.getWeifysl().setScale(0,BigDecimal.ROUND_HALF_UP);
		}
		mx.setWeifyl(tempWeifysl);// 未发运
		mx.setJislx(hz.getJislx());// 计算类型
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("zygzbj.saveZygzbjmx", mx);
	}

	/**
	 * 查询两个日期内天数差
	 * 
	 * @param date1
	 *            日期1 (较小的)
	 * @param date2
	 *            日期2 (较大的)
	 * @param map
	 *            参数
	 * @return 天数差
	 */
	public BigDecimal daysOfDate(String date1, String date2, Map map) {
		map.put("date1", date1);
		map.put("date2", date2);
		return CommonFun.getBigDecimal(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selDaysOfDate", map));
	}

	/**
	 * 计算待消耗时查询日期天数差
	 * 
	 * @param date1
	 *            日期1 (较小的)
	 * @param date2
	 *            日期2 (较大的)
	 * @param map
	 *            参数
	 * @return 天数差
	 */
	public BigDecimal daysOfDxh(String date1, String date2, Map map) {
		map.put("date1", date1);
		map.put("date2", date2);
		return CommonFun.getBigDecimal(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selDaysOfDxh", map));
	}

	/**
	 * 查询周期内最大最小信息
	 * 
	 * @param zq
	 *            周期
	 * @param map
	 *            查询参数
	 * @return 周期内最大最小信息
	 */
	public Map selZqMaxMin(String zq, Map map) {
		map.put("nianzq", zq);
		map.put("nianzx", zq);
		return (Map) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selMaxMin", map);
	}

	/**
	 * 查询日历版次
	 * 
	 * @param appobj
	 *            仓库或者产线
	 * @param map
	 *            查询参数
	 * @return 日历版次
	 */
	public Map selRilbc() {
		List<Map> allRilbcList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("zygzbj.selAllRilbc");
		Map rilbcMap = new HashMap();
		for(Map _map:allRilbcList){
			String usercenter = (String)_map.get("USERCENTER");
			String appobj = (String)_map.get("APPOBJ");
			String rilbc = (String)_map.get("RILBC");
			String key = usercenter+":"+appobj;
			rilbcMap.put(key, rilbc);
		}
		return rilbcMap;
	}

	/**
	 * 查询毛需求明细
	 * 
	 * @param xuqbc
	 *            需求版次
	 * @return 毛需求明细信息
	 */
	public Map selMxqmx(Pageable page, Map<String, String> param) {
		if("anx".equals(param.get("xuqbc"))){
			return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("zygzbj.selAxMxqmx", param, page);
		}else{
			return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("zygzbj.selMxqmx", param, page);
		}
	}

	/**
	 * 保存断点
	 * 
	 * @param yijf
	 *            已交付
	 * @param huiz
	 *            汇总信息
	 * @param duandsj
	 *            断点时间
	 * @param xuh
	 *            断点序号
	 */
	public Map getDuand(BigDecimal yijf, ZiygzbjHz huiz, String duandsj, String xuh) {
		Map<String, String> map = new HashMap<String, String>();
		// 如果已交付小于安全库存,保存断点信息
		if (yijf.compareTo(huiz.getAnqkc()) < 0 && !duandsj.equals("")) {
			
			// 期末库存
			map.put("qimkc", CommonFun.strNull(yijf.setScale(0,BigDecimal.ROUND_HALF_UP)));
			// 汇总ID
			map.put("huizid", huiz.getId());
			// 断点时间
			map.put("duandsj", duandsj);
			// 断点序号
			map.put("duandxh", xuh);
			// 用户中心
			map.put("usercenter", huiz.getUsercenter());
			//报警类型为4,时段报警,断点时间到时分秒
			if("4".equals(huiz.getBaojlx())){
				map.put("baojlx", "4");
			}
			// 计算类型
			map.put("jislx", huiz.getJislx());
			return map;
		}
		//判断资源供应为正常，短缺，过量
		if(!huiz.getZygy().equals("1")){
			if(yijf.compareTo(huiz.getAnqkc()) < 0){
				if(!huiz.getZygy().equals("2")){
					huiz.setZygy("4");
				}
			}else if(yijf.compareTo(huiz.getZuidkcsl())>0){
				if(!huiz.getZygy().equals("3")){
					huiz.setZygy("4");
				}
			}else{
				huiz.setZygy("1");
			}
		}else if(huiz.getZygy().equals("1")){
			if(yijf.compareTo(huiz.getAnqkc()) < 0){
				huiz.setZygy("2");
			}else if(yijf.compareTo(huiz.getZuidkcsl())>0){
				huiz.setZygy("3");
			}else{
				huiz.setZygy("1");
			}
		}
		return null;
	}
	
	/**
	 * 保存断点
	 * 
	 * @param yijf
	 *            已交付
	 * @param huiz
	 *            汇总信息
	 * @param duandsj
	 *            断点时间
	 * @param xuh
	 *            断点序号
	 */
	public void saveDuand(BigDecimal yijf, ZiygzbjHz huiz, String duandsj, String xuh) {
		Map<String, String> map = new HashMap<String, String>();
		// 如果已交付小于安全库存,保存断点信息
		if (yijf.compareTo(huiz.getAnqkc()) < 0 && !duandsj.equals("")) {
			
			// 期末库存
			map.put("qimkc", CommonFun.strNull(yijf.setScale(0,BigDecimal.ROUND_HALF_UP)));
			// 汇总ID
			map.put("huizid", huiz.getId());
			// 断点时间
			map.put("duandsj", duandsj);
			// 断点序号
			map.put("duandxh", xuh);
			// 用户中心
			map.put("usercenter", huiz.getUsercenter());
			//报警类型为4,时段报警,断点时间到时分秒
			if("4".equals(huiz.getBaojlx())){
				map.put("baojlx", "4");
			}
			// 计算类型
			map.put("jislx", huiz.getJislx());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("zygzbj.saveZygzbjdd", map);
		}
		//判断资源供应为正常，短缺，过量
		if(!huiz.getZygy().equals("1")){
			if(yijf.compareTo(huiz.getAnqkc()) < 0){
				if(!huiz.getZygy().equals("2")){
					huiz.setZygy("4");
				}
			}else if(yijf.compareTo(huiz.getZuidkcsl())>0){
				if(!huiz.getZygy().equals("3")){
					huiz.setZygy("4");
				}
			}else{
				huiz.setZygy("1");
			}
		}else if(huiz.getZygy().equals("1")){
			if(yijf.compareTo(huiz.getAnqkc()) < 0){
				huiz.setZygy("2");
			}else if(yijf.compareTo(huiz.getZuidkcsl())>0){
				huiz.setZygy("3");
			}else{
				huiz.setZygy("1");
			}
		}
	}
	
	
	/**
	 * 保存断点
	 * 
	 * @param yijf
	 *            已交付
	 * @param huiz
	 *            汇总信息
	 * @param duandsj
	 *            断点时间
	 * @param xuh
	 *            断点序号
	 */
	public void saveCDDuand(BigDecimal yijf, ZiygzbjHz huiz, String duandsj, String xuh) {
		// 如果已交付小于安全库存,保存断点信息
		if (yijf.compareTo(huiz.getAnqkc()) < 0 && !duandsj.equals("")) {
			Map<String, String> map = new HashMap<String, String>();
			// 期末库存
			map.put("qimkc", CommonFun.strNull(yijf.setScale(0,BigDecimal.ROUND_HALF_UP)));
			// 汇总ID
			map.put("huizid", huiz.getId());
			// 断点时间
			map.put("duandsj", duandsj);
			// 断点序号
			map.put("duandxh", xuh);
			// 用户中心
			map.put("usercenter", huiz.getUsercenter());
			//报警类型为4,时段报警,断点时间到时分秒
			if("5".equals(huiz.getBaojlx())){
				map.put("baojlx", "5");
			}
			// 计算类型
			map.put("jislx", huiz.getJislx());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("zygzbj.saveCDZygzbjdd", map);
		}
		//判断资源供应为正常，短缺，过量
		if(!huiz.getZygy().equals("1")){
			if(yijf.compareTo(huiz.getAnqkc()) < 0){
				if(!huiz.getZygy().equals("2")){
					huiz.setZygy("4");
				}
			}else if(yijf.compareTo(huiz.getZuidkcsl())>0){
				if(!huiz.getZygy().equals("3")){
					huiz.setZygy("4");
				}
			}else{
				huiz.setZygy("1");
			}
		}else if(huiz.getZygy().equals("1")){
			if(yijf.compareTo(huiz.getAnqkc()) < 0){
				huiz.setZygy("2");
			}else if(yijf.compareTo(huiz.getZuidkcsl())>0){
				huiz.setZygy("3");
			}else{
				huiz.setZygy("1");
			}
		}
		
	}

	/**
	 * 保存异常报警
	 * 
	 * @param map
	 *            参数
	 * @param cuowxx
	 *            错误详细信息
	 * @param jihydm
	 *            计划员代码
	 * @param jihyz
	 *            计划员组
	 */
	public void saveYicbj(Map map, String cuowxx, String jihydm) {
		Yicbj yicbj = new Yicbj();
		// 用户中心
		yicbj.setUsercenter(CommonFun.strNull(map.get("usercenter")));
		// 零件编号
		yicbj.setLingjbh(CommonFun.strNull(map.get("lingjbh")));
		// 错误类型
		yicbj.setCuowlx("200");
		// 错误详细信息
		yicbj.setCuowxxxx(cuowxx);
		// 计划员代码
		yicbj.setJihydm(jihydm);
		// 查询零件
		Lingj lingj = (Lingj) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selLj", map);
		if(lingj!=null){
			// 计划员组
			yicbj.setJihyz(lingj.getJihy());
		}
		// 计算模块
		yicbj.setJismk("50");
		//创建人
		yicbj.setCreator(jihydm);
		String time = CommonFun.getJavaTime();
		//创建时间
		yicbj.setCreate_time(time);
		//编辑人
		yicbj.setEditor(jihydm);
		//编辑时间
		yicbj.setEdit_time(time);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ycbj.insertYcbj", yicbj);
	}

	/**
	 * 查询毛需求明细
	 * 
	 * @param xuqbc
	 *            需求版次
	 * @return 毛需求明细信息
	 */
	public List queryZiyhqrl() {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("zygzbj.queryZiyhqrl");
	}
	
	/**
	 * 查询资源计算类型
	 * 
	 * @param xuqbc
	 *            需求版次
	 * @return 毛需求明细信息
	 */
	public List queryZyJslx() {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("zygzbj.queryJslx");
	}
	
	/**
	 * KD毛需求汇总
	 * @param listMxqmx 毛需求集合
	 * @param listZhizlx 制造路线集合
	 * @param param 查询参数
	 * @param username 用户名
	 */
	public Map kdHzmxq(List<Maoxqmx> listMxqmx,Map param,String username){
		Map ucMap = new HashMap();
		ucMap.put("usercenter", param.get("usercenter"));
		List<Map> ckAll = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("zygzbj.selCkAll", ucMap);// 查询物流路径仓库
		Map ckMap = new HashMap();
		for(Map map:ckAll){
			String lingjbh = (String)map.get("LINGJBH");
			if(ckMap.get(lingjbh)==null){
				List list = new ArrayList();
				list.add(map);
				ckMap.put(lingjbh, list);
			}else{
				List list = (List)ckMap.get(lingjbh);
				list.add(map);
				ckMap.put(lingjbh, list);
			}
		}
		Map dwMap = new HashMap();
		for (int i = 0; i < listMxqmx.size(); i++) {
			Maoxqmx mxqmx = listMxqmx.get(i);
			//用户中心
			param.put("usercenter", mxqmx.getUsercenter());
				// 零件编号
				param.put("lingjbh", mxqmx.getLingjbh());
				// 产线
				param.put("shengcxbh", CommonFun.strNull(mxqmx.getChanx()));
				String lingjdw = mxqmx.getDanw();
				dwMap.put(mxqmx.getLingjbh(), lingjdw);
				//制造路线
				param.put("zhizlx", mxqmx.getZhizlx());
				List<Map> cks = (List)ckMap.get(mxqmx.getLingjbh());// 查询物流路径仓库// 查询物流路径仓库
				String dinghck = "";//订货库
				StringBuilder xianbk = new StringBuilder("");//线边库、
				if(cks!=null){
					for (int j = 0; j < cks.size(); j++) {
						Map ck = cks.get(j);
						if(dinghck.isEmpty()){
							dinghck = CommonFun.strNull(ck.get("DINGHCK"));
						}
						String xianbck = CommonFun.strNull(ck.get("XIANBCK"));
						if(!xianbck.isEmpty()){
							xianbk.append("'"+xianbck+"',");
						}
					}
				}
				// 如果仓库为空,跳过该条
				if (dinghck == null || dinghck.isEmpty()) {
					saveYicbj(param, "查询零件仓库为空,产线:"+CommonFun.strNull(mxqmx.getChanx()), username);
					continue;
				}
				xianbk.append("'"+dinghck+"'");
				param.put("id", getUUID());
				param.put("xiaohbl", CommonFun.strNull(BigDecimal.ONE));
				param.put("appobj", dinghck);
				param.put("xianbk", xianbk.toString());
				param.put("xiaohd", "");
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("zygzbj.inZiygzbjmxq", param);
		}
		return dwMap;
	}
	
	/**
	 * 毛需求汇总
	 * @param listMxqmx 毛需求集合
	 * @param listZhizlx 制造路线集合
	 * @param param 查询参数
	 * @param waibms 外部模式
	 * @param username 用户名
	 */
	public Map mxqHz(List<Maoxqmx> listMxqmx,Map param,String waibms,String username){
		// 根据零件编号,生产线,用户中心查询分配循环
		Map ucMap = new HashMap();
		ucMap.put("usercenter", param.get("usercenter"));
		List<Map> listFenpqbh = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("zygzbj.selFenpqbh", param);
		Map fenpxh = new HashMap();
		Map xiaohblMap = new HashMap();
		for(Map map:listFenpqbh){
			String lingjbh = (String)map.get("LINGJBH");
			String shengcxbh = (String)map.get("SHENGCXBH");
			String fenpqbh = (String)map.get("FENPQBH"); 
			String key = lingjbh+":"+shengcxbh+":"+fenpqbh;
			BigDecimal _xiaohbl = (BigDecimal)map.get("XIAOHBL");
			if(xiaohblMap.get(key)==null){
				xiaohblMap.put(key, _xiaohbl);
			}else if(xiaohblMap.get(key)!=null){
				BigDecimal tempxiaohbl = (BigDecimal)xiaohblMap.get(key);
				tempxiaohbl = tempxiaohbl.add(_xiaohbl);
				xiaohblMap.put(key, tempxiaohbl);
			}
			String cxkey = lingjbh+":"+shengcxbh;
			if(fenpxh.get(cxkey)==null){
				List list = new ArrayList();
				list.add(map);
				fenpxh.put(cxkey, list);
			}else{
				List list = (List)fenpxh.get(cxkey);
				list.add(map);
				fenpxh.put(cxkey, list);
			}
			//fenpxh.put(key, map);
		}
		List<Map> ckAll = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("zygzbj.selCkAll", ucMap);// 查询物流路径仓库
		Map ckMap = new HashMap();
		for(Map map:ckAll){
			String lingjbh = (String)map.get("LINGJBH");
			String fenpqbh = (String)map.get("FENPQH");
			String key = lingjbh+":"+fenpqbh;
			if(ckMap.get(key)==null){
				List list = new ArrayList();
				list.add(map);
				ckMap.put(key, list);
			}else{
				List list = (List)ckMap.get(key);
				list.add(map);
				ckMap.put(key, list);
			}
		}
		List mxqList = new ArrayList();
		Map dwMap = new HashMap();
		for (int i = 0; i < listMxqmx.size(); i++) {
			Maoxqmx mxqmx = listMxqmx.get(i);
			String lingjbh = mxqmx.getLingjbh();
			String shengcxbh = CommonFun.strNull(mxqmx.getChanx());
			List fenpqbhList = new ArrayList();
			String key = lingjbh+":"+shengcxbh;
			if(fenpxh.get(key)!=null){
				fenpqbhList = (List)fenpxh.get(key);
				for(int j=0;j<fenpqbhList.size();j++){
					Map mapFenpqbh = (Map)fenpqbhList.get(j);
					String lingjdw = mxqmx.getDanw();
					dwMap.put(lingjbh, lingjdw);
					String fenpqh = (String)mapFenpqbh.get("FENPQBH");
					Map ziygzbjmxq = new HashMap();
					ziygzbjmxq.put("id", getUUID());
					String usercenter = (String)param.get("usercenter");
					ziygzbjmxq.put("usercenter", usercenter);
					// 零件编号
					ziygzbjmxq.put("lingjbh", mxqmx.getLingjbh());
					// 产线
					ziygzbjmxq.put("shengcxbh", CommonFun.strNull(mxqmx.getChanx()));
					//制造路线
					ziygzbjmxq.put("zhizlx", mxqmx.getZhizlx());
					String fenpqkey = key+":"+fenpqh;
					// 根据分配区去找仓库
					ziygzbjmxq.put("xiaohbl", CommonFun.strNull(xiaohblMap.get(fenpqkey)));
					ziygzbjmxq.put("xiaohd", CommonFun.strNull(mapFenpqbh.get("XIAOHDBH")));
						List<Map> cks = (List)ckMap.get(lingjbh+":"+fenpqh);// 查询物流路径仓库
						
						String dinghck = "";//订货库
						StringBuilder xianbk = new StringBuilder("");//线边库
						
						//取订货库下所有线边库
						if(cks!=null){
							for (int k = 0; k < cks.size(); k++) {
								Map ck = cks.get(k);
								//模式判断,如果不包含,过滤
								if(waibms.indexOf(CommonFun.strNull(ck.get("WAIBMS"))) != -1){
									String xianbck = CommonFun.strNull(ck.get("XIANBCK"));
									if(!xianbck.isEmpty()){
										xianbk.append("'"+xianbck+"',");
									}
									if (dinghck.isEmpty()) {
										dinghck = CommonFun.strNull(ck.get("DINGHCK"));// 取订货库
										if (dinghck.isEmpty()){// 如果订货库为空,返回线边库
											dinghck = xianbck;
										}
									}
								}
							}
						}
						// 如果仓库为空,跳过该条
						if (dinghck.isEmpty()) {
							//saveYicbj(param, "零件消耗点分配区仓库为空,外部模式:"+waibms+",产线:"+CommonFun.strNull(mxqmx.getChanx())+",分配循环:"+mapFenpqbh.get("FENPQBH"), username);
							List msgList = new ArrayList();
							msgList.add(lingjbh);
							msgList.add(waibms.replaceAll("'", ""));
							msgList.add(CommonFun.strNull(mxqmx.getChanx()));
							msgList.add(mapFenpqbh.get("FENPQBH"));
							LoginUser loginuser = new LoginUser();
							loginuser.setUsername(username);
							yicbjService.insertError(Const.YICHANG_LX2,
									Const.YICHANG_LX2_str53, "", msgList, usercenter,
									lingjbh, loginuser, Const.JISMK_GZBJ_CD);
							continue;
						}
						xianbk.append("'"+dinghck+"'");
						ziygzbjmxq.put("appobj", dinghck);
						ziygzbjmxq.put("xianbk", xianbk.toString());
						// 仓库,产线汇总
						mxqList.add(ziygzbjmxq);
				}
			}
		}
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("zygzbj.inZiygzbjmxq", mxqList);
		return dwMap;
	}
	
	/**
	 * 周期报警计算
	 * @param zygzlx 资源跟踪类型
	 * @param mapZiygzbjhz 汇总参数
	 * @param param 查询参数
	 * @param ziygzbjhz 资源跟踪报警汇总信息
	 * @param lingj 零件信息
	 * @return true-正常,false-异常需跳过
	 * @throws Exception
	 */
	public boolean zqBaojjs(Zygzlx zygzlx,Map<String,String> mapZiygzbjhz,Map<String,String> param,ZiygzbjHz ziygzbjhz,Lingj lingj,Map rilbcMap,List riqList,Map nianzqMap,Map nianzxMap) throws Exception{
		BigDecimal kaisrzy = ziygzbjhz.getQickc();//开始日资源
		// 计算方法1,计算仓库
		if (zygzlx.getJisff().equals("1")) {
			// 查询仓库日历版次
			String rilbc = (String)rilbcMap.get(mapZiygzbjhz.get("usercenter")+":"+mapZiygzbjhz.get("cangkdm"));
			if(rilbc==null||"".equals(rilbc)){
				List msgList = new ArrayList();
				msgList.add(param.get("jisrq"));
				msgList.add(mapZiygzbjhz.get("cangkdm"));
				LoginUser loginuser = new LoginUser();
				loginuser.setUsername( param.get("username"));
				yicbjService.insertError(Const.YICHANG_LX2,
						Const.YICHANG_LX2_str56, "", msgList, mapZiygzbjhz.get("usercenter"),
						lingj.getLingjbh(), loginuser, Const.JISMK_GZBJ_CD);
				return false;
			}
			mapZiygzbjhz.put("rilbc", rilbc);
			String riqKey = mapZiygzbjhz.get("usercenter")+":"+rilbc+":"+mapZiygzbjhz.get("jisrq");
			if(!riqList.contains(riqKey)){
				List msgList = new ArrayList();
				msgList.add(param.get("jisrq"));
				msgList.add(mapZiygzbjhz.get("cangkdm"));
				LoginUser loginuser = new LoginUser();
				loginuser.setUsername( param.get("username"));
				yicbjService.insertError(Const.YICHANG_LX2,
						Const.YICHANG_LX2_str56, "", msgList, mapZiygzbjhz.get("usercenter"),
						lingj.getLingjbh(), loginuser, Const.JISMK_GZBJ_CD);
				return false;
			}
			ziygzbjhz = jisZqJf(mapZiygzbjhz, param.get("jisrq"), zygzlx.getJisff(), ziygzbjhz, ziygzbjhz.getQickc(), CommonFun.strNull(lingj.getLingjsx()),nianzqMap,nianzxMap);
			// 计算方法2,计算产线
		} else if (zygzlx.getJisff().equals("2")) {
			Class cl = ZiygzbjHz.class;
			List<Ziygzbjmxq> listZiygzbjmxqmx = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("zygzbj.queryZiygzbjmxqmx", mapZiygzbjhz);
			// 计算产线需求,汇总产线需求为仓库需求
			for (int j = 0; j < listZiygzbjmxqmx.size(); j++) {
				Ziygzbjmxq ziygzbjmxq = listZiygzbjmxqmx.get(j);
				mapZiygzbjhz.put("chanx", ziygzbjmxq.getChanx());// 产线
				mapZiygzbjhz.put("xiaohbl", CommonFun.strNull(ziygzbjmxq.getXiaohbl()));// 消耗比例
				// 查询产线日历版次
				String rilbc = (String)rilbcMap.get(mapZiygzbjhz.get("usercenter")+":"+ziygzbjmxq.getChanx());
				if(rilbc==null||"".equals(rilbc)){
					//saveYicbj(mapZiygzbjhz, "日历版次为空,计算日期为"+param.get("jisrq")+"产线为"+ziygzbjmxq.getChanx(), param.get("username"));
					List msgList = new ArrayList();
					msgList.add(param.get("jisrq"));
					msgList.add(ziygzbjmxq.getChanx());
					LoginUser loginuser = new LoginUser();
					loginuser.setUsername( param.get("username"));
					yicbjService.insertError(Const.YICHANG_LX2,
							Const.YICHANG_LX2_str57, "", msgList, mapZiygzbjhz.get("usercenter"),
							lingj.getLingjbh(), loginuser, Const.JISMK_GZBJ_CD);
					continue;
				}
				mapZiygzbjhz.put("rilbc", rilbc);
				String riqKey = mapZiygzbjhz.get("usercenter")+":"+rilbc+":"+mapZiygzbjhz.get("jisrq");
				if(!riqList.contains(riqKey)){
					//saveYicbj(mapZiygzbjhz, "计算日期为空,计算日期为"+param.get("jisrq")+"日历版次为"+rilbc, param.get("username"));
					List msgList = new ArrayList();
					msgList.add(param.get("jisrq"));
					msgList.add(rilbc);
					LoginUser loginuser = new LoginUser();
					loginuser.setUsername( param.get("username"));
					yicbjService.insertError(Const.YICHANG_LX2,
							Const.YICHANG_LX2_str58, "", msgList, mapZiygzbjhz.get("usercenter"),
							lingj.getLingjbh(), loginuser, Const.JISMK_GZBJ_CD);
					return false;
				}

				ZiygzbjHz newZiygzbjHz = new ZiygzbjHz();
				newZiygzbjHz = jisZqCxJf(mapZiygzbjhz, param.get("jisrq"), zygzlx.getJisff(), newZiygzbjHz,
						CommonFun.strNull(lingj.getLingjsx()),nianzqMap,nianzxMap);
				for (int k = 0; k < 3; k++) {
					PropertyDescriptor pdMx = new PropertyDescriptor("mx" + k, cl);
					// 获取明细
					List<Ziygzbjmx> listNew = (List<Ziygzbjmx>) pdMx.getReadMethod().invoke(newZiygzbjHz);
					List<List> list = (List<List>) pdMx.getReadMethod().invoke(ziygzbjhz);
					// 明细数据处理
					if (list == null) {
						list = new ArrayList<List>();
						for (int a = 0; a < listNew.size(); a++) {
							List<Ziygzbjmx> listl = new ArrayList<Ziygzbjmx>();
							listl.add(listNew.get(a));
							list.add(listl);
						}
					} else {
						for (int a = 0; a < listNew.size(); a++) {
							if (a >= list.size()) {
								List<Ziygzbjmx> listnewL = new ArrayList<Ziygzbjmx>();
								listnewL.add(listNew.get(a));
								list.add(listnewL);
							} else {
								List<Ziygzbjmx> listl = list.get(a);
								if (listl != null) {
									listl.add(listNew.get(a));
									list.set(a, listl);
								}
							}
						}
					}
					pdMx.getWriteMethod().invoke(ziygzbjhz, list);
				}
				// 设置周序数
				if (CommonFun.getBigDecimal(newZiygzbjHz.getNum()).compareTo(
						CommonFun.getBigDecimal(ziygzbjhz.getNum())) > 0) {
					ziygzbjhz.setNum(newZiygzbjHz.getNum());
				}
				for (int k = 0; k <= newZiygzbjHz.getNum(); k++) {
					PropertyDescriptor pd = new PropertyDescriptor("xuq" + k, cl);
					PropertyDescriptor pd2 = new PropertyDescriptor("nianzx" + k, cl);
					// 设置需求
					pd.getWriteMethod().invoke(
							ziygzbjhz,
							CommonFun.getBigDecimal(pd.getReadMethod().invoke(ziygzbjhz)).add(
									CommonFun.getBigDecimal(pd.getReadMethod().invoke(newZiygzbjHz))));
					// 设置需求
					pd2.getWriteMethod().invoke(ziygzbjhz, pd2.getReadMethod().invoke(newZiygzbjHz));
				}
			}
			// 查询仓库日历版次
			String rilbc = (String)rilbcMap.get(mapZiygzbjhz.get("usercenter")+":"+mapZiygzbjhz.get("cangkdm"));
			if(rilbc==null||"".equals(rilbc)){
				//saveYicbj(mapZiygzbjhz, "日历版次为空,计算日期为"+param.get("jisrq")+"仓库代码为"+mapZiygzbjhz.get("cangkdm"), param.get("username"));
				List msgList = new ArrayList();
				msgList.add(param.get("jisrq"));
				msgList.add(mapZiygzbjhz.get("cangkdm"));
				LoginUser loginuser = new LoginUser();
				loginuser.setUsername( param.get("username"));
				yicbjService.insertError(Const.YICHANG_LX2,
						Const.YICHANG_LX2_str56, "", msgList, mapZiygzbjhz.get("usercenter"),
						lingj.getLingjbh(), loginuser, Const.JISMK_GZBJ_CD);
				return false;
			}
			mapZiygzbjhz.put("rilbc", rilbc);
			// 计算仓库已交付
 			for (int j = 0; j <= ziygzbjhz.getNum(); j++) {
				PropertyDescriptor pd = new PropertyDescriptor("xuq" + j, cl);
				PropertyDescriptor pd2 = new PropertyDescriptor("nianzx" + j, cl);
				PropertyDescriptor pd3 = new PropertyDescriptor("yijf" + j, cl);
				BigDecimal xuqsl = CommonFun.getBigDecimal(pd.getReadMethod().invoke(ziygzbjhz));
				BigDecimal yxuqsl = xuqsl;
				String nianzx = CommonFun.strNull(pd2.getReadMethod().invoke(ziygzbjhz));
				mapZiygzbjhz.put("xuqlx", "nianzx");
				mapZiygzbjhz.put("nianzx", nianzx);
				Map zxMaxMin = (Map) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selMaxMin", mapZiygzbjhz);
				xuqsl = compareZongXq(lingj.getLingjsx(), param, CommonFun.strNull(zxMaxMin.get("MINRIQ")),
						CommonFun.strNull(zxMaxMin.get("MAXRIQ")), xuqsl);
				BigDecimal daoh = BigDecimal.ZERO;
				// 断点时间
				String duandsj = "";
				if (j == 0) {
					mapZiygzbjhz.put("riq1", param.get("jisrq"));
					mapZiygzbjhz.put("riq2", nianzx);
					duandsj = param.get("jisrq");
					// 计算已交付
					daoh = selZRiqDaoh(param);
				} else {
					daoh = CommonFun.getBigDecimal(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selZxDaoh", mapZiygzbjhz));
					duandsj = CommonFun.strNull(zxMaxMin.get("MINRIQ"));
				}
				kaisrzy = kaisrzy.add(daoh).subtract(xuqsl);
				pd.getWriteMethod().invoke(ziygzbjhz, yxuqsl.setScale(0,BigDecimal.ROUND_HALF_UP));
				pd3.getWriteMethod().invoke(ziygzbjhz, kaisrzy.setScale(0,BigDecimal.ROUND_HALF_UP));
				saveDuand(kaisrzy, ziygzbjhz, duandsj, String.valueOf(j));
			}	
 			//合并
			for (int j = 0; j < 3; j++) {
				PropertyDescriptor pd = new PropertyDescriptor("zongxq" + j, cl);
				PropertyDescriptor pd2 = new PropertyDescriptor("zongyjf" + j, cl);
				PropertyDescriptor pdMx = new PropertyDescriptor("mx" + j, cl);
				List<List<Ziygzbjmx>> list = (List<List<Ziygzbjmx>>) pdMx.getReadMethod().invoke(ziygzbjhz);
				if (list == null) {// 如果明细为空
					continue;
				}
				BigDecimal zongxq = BigDecimal.ZERO;// 总需求
				BigDecimal yzongxq = BigDecimal.ZERO;
				for (int k = 0; k < list.size(); k++) {
					List<Ziygzbjmx> listk = list.get(k);
					Ziygzbjmx mx = listk.get(0);
					mapZiygzbjhz.put("xuqlx", "nianzx");
					mapZiygzbjhz.put("nianzx", mx.getNianzx());
					Map zxMaxMin = (Map) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selMaxMin", mapZiygzbjhz);
					for (int l = 0; l < listk.size(); l++) {
						mx = listk.get(l);
						// 比较需求数量
						yzongxq = yzongxq.add(mx.getXuql());
						BigDecimal xuqsl = compareZongXq(CommonFun.strNull(lingj.getLingjsx()), param,
								CommonFun.strNull(zxMaxMin.get("MINRIQ")),
								CommonFun.strNull(zxMaxMin.get("MAXRIQ")), mx.getXuql());
						zongxq = zongxq.add(xuqsl);
						// 计算已交付
						kaisrzy = kaisrzy
								.add(CommonFun.getBigDecimal(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selZxDaoh",
										mapZiygzbjhz))).subtract(xuqsl);
					}
					// 保存明细
					saveZygzbjmx(ziygzbjhz, mx.getHuizxh(), mx.getMingxxh(), yzongxq, kaisrzy,
							mx.getStarttime(), mx.getEndtime());
					saveDuand(kaisrzy, ziygzbjhz, CommonFun.strNull(zxMaxMin.get("MINRIQ")),
							Integer.toString(j) + Integer.toString(k));
				}
				// 设置总需求
				pd.getWriteMethod().invoke(ziygzbjhz, yzongxq.setScale(0,BigDecimal.ROUND_HALF_UP));
				// 设置总已交付
				pd2.getWriteMethod().invoke(ziygzbjhz, kaisrzy.setScale(0,BigDecimal.ROUND_HALF_UP));
			}
		}
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("zygzbj.saveZygzbjhz", ziygzbjhz);
		return true;
	}
	
	/**
	 * 周资源跟踪报警查询
	 * @param zygzlx 资源跟踪类型
	 * @param mapZiygzbjhz 汇总参数
	 * @param param 查询参数
	 * @param ziygzbjhz 资源跟踪报警汇总
	 * @param lingj 零件信息
	 * @return true-正常,false-异常需跳过
	 * @throws Exception
	 */
	public boolean zBaojjs(Zygzlx zygzlx,Map<String,String> mapZiygzbjhz,Map<String,String> param,ZiygzbjHz ziygzbjhz,Lingj lingj,Map rilbcMap,List riqList,Map nianzxMap) throws Exception{
		BigDecimal kaisrzy = ziygzbjhz.getQickc();
		// 计算方法1,计算仓库
		if (zygzlx.getJisff().equals("1")) {
			// 查询仓库日历版次
			String rilbc = (String)rilbcMap.get(mapZiygzbjhz.get("usercenter")+":"+mapZiygzbjhz.get("cangkdm"));
			if(rilbc==null||"".equals(rilbc)){
				//saveYicbj(mapZiygzbjhz, "日历版次为空,计算日期为"+param.get("jisrq")+"仓库代码为"+mapZiygzbjhz.get("cangkdm"), param.get("username"));
				List msgList = new ArrayList();
				msgList.add(param.get("jisrq"));
				msgList.add(mapZiygzbjhz.get("cangkdm"));
				LoginUser loginuser = new LoginUser();
				loginuser.setUsername( param.get("username"));
				yicbjService.insertError(Const.YICHANG_LX2,
						Const.YICHANG_LX2_str56, "", msgList, mapZiygzbjhz.get("usercenter"),
						lingj.getLingjbh(), loginuser, Const.JISMK_GZBJ_CD);
				return false;
			}
			mapZiygzbjhz.put("rilbc", rilbc);
			String str =  mapZiygzbjhz.get("usercenter")+":"+rilbc+":"+mapZiygzbjhz.get("jisrq");
			if(!riqList.contains(str)){
				//saveYicbj(mapZiygzbjhz, "计算日期为空,计算日期为"+param.get("jisrq")+"日历版次为"+rilbc, param.get("username"));
				List msgList = new ArrayList();
				msgList.add(param.get("jisrq"));
				msgList.add(rilbc);
				LoginUser loginuser = new LoginUser();
				loginuser.setUsername( param.get("username"));
				yicbjService.insertError(Const.YICHANG_LX2,
						Const.YICHANG_LX2_str58, "", msgList, mapZiygzbjhz.get("usercenter"),
						lingj.getLingjbh(), loginuser, Const.JISMK_GZBJ_CD);
				return false;
			}
			ziygzbjhz = jisZxJf(mapZiygzbjhz, param.get("jisrq"), zygzlx.getJisff(), ziygzbjhz, kaisrzy, CommonFun.strNull(lingj.getLingjsx()),nianzxMap);
			// 计算方法2,计算产线
		} else if (zygzlx.getJisff().equals("2")) {
			Class cl = ZiygzbjHz.class;
			// 计算产线待消耗,汇总产线待消耗为仓库待消耗
			List<Ziygzbjmxq> listZiygzbjmxqmx = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("zygzbj.queryZiygzbjmxqmx", mapZiygzbjhz);
			for (int j = 0; j < listZiygzbjmxqmx.size(); j++) {
				Ziygzbjmxq ziygzbjmxq = listZiygzbjmxqmx.get(j);
				mapZiygzbjhz.put("chanx", ziygzbjmxq.getChanx());// 产线
				mapZiygzbjhz.put("xiaohbl", CommonFun.strNull(ziygzbjmxq.getXiaohbl()));// 消耗比例
				// 查询产线日历版次
				String rilbc = (String)rilbcMap.get(ziygzbjmxq.getChanx()+":"+mapZiygzbjhz.get("usercenter"));
				if(rilbc==null||"".equals(rilbc)){
					//saveYicbj(mapZiygzbjhz, "日历版次为空,计算日期为"+param.get("jisrq")+"产线为"+ziygzbjmxq.getChanx(), param.get("username"));
					List msgList = new ArrayList();
					msgList.add(param.get("jisrq"));
					msgList.add(mapZiygzbjhz.get("cangkdm"));
					LoginUser loginuser = new LoginUser();
					loginuser.setUsername( param.get("username"));
					yicbjService.insertError(Const.YICHANG_LX2,
							Const.YICHANG_LX2_str57, "", msgList, mapZiygzbjhz.get("usercenter"),
							lingj.getLingjbh(), loginuser, Const.JISMK_GZBJ_CD);
					continue;
				}
				mapZiygzbjhz.put("rilbc", rilbc);
				String str = mapZiygzbjhz.get("usercenter")+":"+rilbc+":"+mapZiygzbjhz.get("jisrq");;
				if(!riqList.contains(str)){
					//saveYicbj(mapZiygzbjhz, "计算日期为空,计算日期为"+param.get("jisrq")+"日历版次为"+rilbc, param.get("username"));
					List msgList = new ArrayList();
					msgList.add(param.get("jisrq"));
					msgList.add(mapZiygzbjhz.get("cangkdm"));
					LoginUser loginuser = new LoginUser();
					loginuser.setUsername( param.get("username"));
					yicbjService.insertError(Const.YICHANG_LX2,
							Const.YICHANG_LX2_str58, "", msgList, mapZiygzbjhz.get("usercenter"),
							lingj.getLingjbh(), loginuser, Const.JISMK_GZBJ_CD);
					continue;
				}
				ZiygzbjHz newZiygzbjHz = new ZiygzbjHz();
				newZiygzbjHz = jisCxZxJf(mapZiygzbjhz, param.get("jisrq"), zygzlx.getJisff(), newZiygzbjHz,
						CommonFun.strNull(lingj.getLingjsx()),nianzxMap);
				for (int k = 0; k < 3; k++) {
					PropertyDescriptor pdMx = new PropertyDescriptor("mx" + k, cl);
					// 获取明细
					List<Ziygzbjmx> listNew = (List<Ziygzbjmx>) pdMx.getReadMethod().invoke(newZiygzbjHz);
					List<List> list = (List<List>) pdMx.getReadMethod().invoke(ziygzbjhz);
					// 明细数据处理
					if (list == null) {
						list = new ArrayList<List>();
						for (int a = 0; a < listNew.size(); a++) {
							List<Ziygzbjmx> listl = new ArrayList<Ziygzbjmx>();
							listl.add(listNew.get(a));
							list.add(listl);
						}
					} else {
						for (int a = 0; a < listNew.size(); a++) {
							if (a >= list.size()) {
								List<Ziygzbjmx> listnewL = new ArrayList<Ziygzbjmx>();
								listnewL.add(listNew.get(a));
								list.add(listnewL);
							} else {
								List<Ziygzbjmx> listl = list.get(a);
								if (listl != null) {
									listl.add(listNew.get(a));
									list.set(a, listl);
								}
							}
						}
					}
					pdMx.getWriteMethod().invoke(ziygzbjhz, list);
				}
				// 设置周序数
				if (CommonFun.getBigDecimal(newZiygzbjHz.getNum()).compareTo(
						CommonFun.getBigDecimal(ziygzbjhz.getNum())) > 0) {
					ziygzbjhz.setNum(newZiygzbjHz.getNum());
				}
				for (int k = 0; k <= newZiygzbjHz.getNum(); k++) {
					PropertyDescriptor pd = new PropertyDescriptor("xuq" + k, cl);
					PropertyDescriptor pd2 = new PropertyDescriptor("nianzx" + k, cl);
					// 设置需求
					pd.getWriteMethod().invoke(
							ziygzbjhz,
							CommonFun.getBigDecimal(pd.getReadMethod().invoke(ziygzbjhz)).add(
									CommonFun.getBigDecimal(pd.getReadMethod().invoke(newZiygzbjHz))));
					// 设置需求
					pd2.getWriteMethod().invoke(ziygzbjhz, pd2.getReadMethod().invoke(newZiygzbjHz));
				}

			}
			// 查询仓库日历版次
			String rilbc = (String)rilbcMap.get(mapZiygzbjhz.get("cangkdm")+":"+mapZiygzbjhz.get("usercenter"));
			if(rilbc==null||"".equals(rilbc)){
				//saveYicbj(mapZiygzbjhz, "日历版次为空,计算日期为"+param.get("jisrq")+"仓库代码为"+mapZiygzbjhz.get("cangkdm"), param.get("username"));
				List msgList = new ArrayList();
				msgList.add(param.get("jisrq"));
				msgList.add(mapZiygzbjhz.get("cangkdm"));
				LoginUser loginuser = new LoginUser();
				loginuser.setUsername( param.get("username"));
				yicbjService.insertError(Const.YICHANG_LX2,
						Const.YICHANG_LX2_str56, "", msgList, mapZiygzbjhz.get("usercenter"),
						lingj.getLingjbh(), loginuser, Const.JISMK_GZBJ_CD);
				return false;
			}
			mapZiygzbjhz.put("rilbc", rilbc);
			// 计算仓库已交付
			for (int j = 0; j <= ziygzbjhz.getNum(); j++) {
				PropertyDescriptor pd = new PropertyDescriptor("xuq" + j, cl);
				PropertyDescriptor pd2 = new PropertyDescriptor("nianzx" + j, cl);
				PropertyDescriptor pd3 = new PropertyDescriptor("yijf" + j, cl);
				// 需求数量
				BigDecimal xuqsl = CommonFun.getBigDecimal(pd.getReadMethod().invoke(ziygzbjhz));
				// 计算日期
				String riq = CommonFun.strNull(pd2.getReadMethod().invoke(ziygzbjhz));
				mapZiygzbjhz.put("xuqlx", "riq");
				mapZiygzbjhz.put("riq", riq);
				BigDecimal ysxuqsl = xuqsl;
				// 比较需求数量
				xuqsl = compareXq(CommonFun.strNull(lingj.getLingjsx()), mapZiygzbjhz, riq, xuqsl);
				// 计算已交付
				kaisrzy = kaisrzy.add(
						selRiDaoh(mapZiygzbjhz))
						.subtract(xuqsl);
				//该周期过期的零件量比需求大时，需求栏还是显示原始的需求，只是在资源里面减掉
				pd.getWriteMethod().invoke(ziygzbjhz, ysxuqsl.setScale(0,BigDecimal.ROUND_HALF_UP));
				pd3.getWriteMethod().invoke(ziygzbjhz, kaisrzy.setScale(0,BigDecimal.ROUND_HALF_UP));
				saveDuand(kaisrzy, ziygzbjhz, riq, String.valueOf(j));
			}
			for (int j = 0; j < 3; j++) {
				PropertyDescriptor pd = new PropertyDescriptor("zongxq" + j, cl);
				PropertyDescriptor pd2 = new PropertyDescriptor("zongyjf" + j, cl);
				PropertyDescriptor pdMx = new PropertyDescriptor("mx" + j, cl);
				// 获取明细
				List<List> list = (List<List>) pdMx.getReadMethod().invoke(ziygzbjhz);
				if (list == null) {// 如果明细为空
					continue;
				}
				BigDecimal zongxq = BigDecimal.ZERO;// 总需求
				for (int k = 0; k < list.size(); k++) {
					List<Ziygzbjmx> listk = list.get(k);
					Ziygzbjmx mx = listk.get(0);
					mapZiygzbjhz.put("xuqlx", "nianzx");
					mapZiygzbjhz.put("nianzx", mx.getNianzx());
					for (int l = 0; l < listk.size(); l++) {
						mx = listk.get(l);
						// 比较需求数量
						BigDecimal xuqsl = compareXq(CommonFun.strNull(lingj.getLingjsx()), mapZiygzbjhz, mx.getNianzx(),
								mx.getXuql());
						zongxq = zongxq.add(mx.getXuql());
						// 计算已交付
						kaisrzy = kaisrzy
								.add(selRiDaoh(mapZiygzbjhz)).subtract(xuqsl);
					}
					// 保存明细
					saveZygzbjmx(ziygzbjhz, mx.getHuizxh(), mx.getMingxxh(), zongxq, kaisrzy,
							mx.getStarttime(), mx.getEndtime());
					saveDuand(kaisrzy, ziygzbjhz, mx.getNianzx(), Integer.toString(j) + Integer.toString(k));
				}
				// 设置总需求
				pd.getWriteMethod().invoke(ziygzbjhz, zongxq.setScale(0,BigDecimal.ROUND_HALF_UP));
				// 设置总已交付
				pd2.getWriteMethod().invoke(ziygzbjhz, kaisrzy.setScale(0,BigDecimal.ROUND_HALF_UP));
			}
		}
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("zygzbj.saveZygzbjhz", ziygzbjhz);
		return true;
	}
	
	/**
	 * 查询周日期范围内到货
	 * @param param 查询参数
	 * @return 到货数量
	 * @throws ParseException
	 */
	public BigDecimal selZRiqDaoh(Map param) throws ParseException{
		//查询满足条件的要货令集合
		List<Yaohl> listYaohl = new ArrayList();
		//日期范围内到货数量
		BigDecimal zRiqDaoh = BigDecimal.ZERO;
		if(param.get("riq1")!=null&&param.get("riq2")!=null){
			listYaohl = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("zygzbj.selZRiqDaoh", param);
			//开始日期
			Date riq1 = CommonFun.sdf.parse(CommonFun.strNull(param.get("riq1")));
			//结束日期
			Date riq2 = CommonFun.sdf.parse(CommonFun.strNull(param.get("riq2")));
			
			//遍历要货令集合
			for (int i = 0; i < listYaohl.size(); i++) {
				Yaohl yaohl = listYaohl.get(i);
				//如果拉箱指定到达时间不为空
				if(!StringUtils.isEmpty(yaohl.getLaxzdddsj())){
					Date laxzdddsj = CommonFun.sdf.parse(yaohl.getLaxzdddsj());
					//如果拉箱指定到达时间大于等于开始日期,小于等于结束日期,则加上要货数量
					if(laxzdddsj.compareTo(riq1) >= 0 && laxzdddsj.compareTo(riq2) <= 0){
						zRiqDaoh = zRiqDaoh.add(yaohl.getYaohsl());
					}
				}else if(!StringUtils.isEmpty(yaohl.getXiughyjjfsj())){
					//如果修改后预计交付时间不为空
					Date xiughyjjfsj = CommonFun.sdf.parse(yaohl.getXiughyjjfsj());
					//如果修改后预计交付时间大于等于开始日期,小于等于结束日期,则加上要货数量
					if(xiughyjjfsj.compareTo(riq1) >= 0 && xiughyjjfsj.compareTo(riq2) <= 0){
						zRiqDaoh = zRiqDaoh.add(yaohl.getYaohsl());
					}
				}else if(!StringUtils.isEmpty(yaohl.getZuiwsj())){
					//如果最晚到货时间不为空
					Date zuiwsj = CommonFun.sdf.parse(yaohl.getZuiwsj());
					//如果最晚到达时间大于等于开始日期,小于等于结束日期,则加上要货数量
					if(zuiwsj.compareTo(riq1) >= 0 && zuiwsj.compareTo(riq2) <= 0){
						zRiqDaoh = zRiqDaoh.add(yaohl.getYaohsl());
					}	
				}
			}
		}
		
		//返回汇总数量
		return zRiqDaoh;
	}
	
	
	
	/**
	 * 汇总日到货数量
	 * @param param 查询参数
	 * @return 日到货数量
	 * @throws ParseException
	 */
	public BigDecimal selRiDaohL(Map param) throws ParseException{
		//查询满足条件的要货令集合
		List<Yaohl> listYaohl = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("zygzbj.selRiDaohByRi", param);
		//日期
/*		if(param.get("lingjbh").equals("7703008172")){
			System.out.println("hello");
		}*/
		Date riq = CommonFun.sdf.parse(CommonFun.strNull(param.get("riq")));
		Date lastJisr =  CommonFun.sdf.parse(CommonFun.strNull(param.get("lastJisr")));
		//日期内到货数量
		BigDecimal zRiqDaoh = BigDecimal.ZERO;
		//遍历要货令集合
		for (int i = 0; i < listYaohl.size(); i++) {
			Yaohl yaohl = listYaohl.get(i);
			//如果拉箱指定到达时间不为空
			if(!StringUtils.isEmpty(yaohl.getLaxzdddsj())){
				Date laxzdddsj = CommonFun.sdf.parse(yaohl.getLaxzdddsj());
				//如果拉箱指定到达时间大于等于指定日期
				if(laxzdddsj.compareTo(lastJisr) > 0&&laxzdddsj.compareTo(riq) <= 0){
					zRiqDaoh = zRiqDaoh.add(yaohl.getYaohsl());
				}
			}else if(!StringUtils.isEmpty(yaohl.getXiughyjjfsj())){
				//如果修改后预计交付时间不为空
				Date xiughyjjfsj = CommonFun.sdf.parse(yaohl.getXiughyjjfsj());
				//如果修改后预计交付时间大于等于指定日期
				if(xiughyjjfsj.compareTo(lastJisr) > 0&&xiughyjjfsj.compareTo(riq) <= 0){
					zRiqDaoh = zRiqDaoh.add(yaohl.getYaohsl());
				}
			}else if(!StringUtils.isEmpty(yaohl.getZuiwsj())){
				//如果最晚到货时间不为空
				Date zuiwsj = CommonFun.sdf.parse(yaohl.getZuiwsj());
				//如果最晚到达时间等于指定日期
				if(zuiwsj.compareTo(lastJisr) > 0&&zuiwsj.compareTo(riq) <= 0){
					zRiqDaoh = zRiqDaoh.add(yaohl.getYaohsl());
				}	
			}
		}
		//返回汇总数量
		return zRiqDaoh;
	}
	
	
	/**
	 * 汇总日到货数量
	 * @param param 查询参数
	 * @return 日到货数量
	 * @throws ParseException
	 */
	public BigDecimal selRiDaoh(Map param) throws ParseException{
		//查询满足条件的要货令集合
		List<Yaohl> listYaohl = new ArrayList();
		//日期内到货数量
		BigDecimal zRiqDaoh = BigDecimal.ZERO;
		if(param.get("riq")!=null){
			listYaohl = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("zygzbj.selRiDaoh", param);

			//日期
			Date riq = CommonFun.sdf.parse(CommonFun.strNull(param.get("riq")));
			//遍历要货令集合
			for (int i = 0; i < listYaohl.size(); i++) {
				Yaohl yaohl = listYaohl.get(i);
				//如果拉箱指定到达时间不为空
				if(!StringUtils.isEmpty(yaohl.getLaxzdddsj())){
					Date laxzdddsj = CommonFun.sdf.parse(yaohl.getLaxzdddsj());
					//如果拉箱指定到达时间等于指定日期
					if(laxzdddsj.compareTo(riq) == 0 ){
						zRiqDaoh = zRiqDaoh.add(yaohl.getYaohsl());
					}
				}else if(!StringUtils.isEmpty(yaohl.getXiughyjjfsj())){
					//如果修改后预计交付时间不为空
					Date xiughyjjfsj = CommonFun.sdf.parse(yaohl.getXiughyjjfsj());
					//如果修改后预计交付时间等于指定日期
					if(xiughyjjfsj.compareTo(riq) == 0){
						zRiqDaoh = zRiqDaoh.add(yaohl.getYaohsl());
					}
				}else if(!StringUtils.isEmpty(yaohl.getZuiwsj())){
					//如果最晚到货时间不为空
					Date zuiwsj = CommonFun.sdf.parse(yaohl.getZuiwsj());
					//如果最晚到达时间等于指定日期
					if(zuiwsj.compareTo(riq) == 0){
						zRiqDaoh = zRiqDaoh.add(yaohl.getYaohsl());
					}	
				}
			}
		}
		//返回汇总数量
		return zRiqDaoh;
	}
	
	/**
	 * 汇总日到货数量
	 * @param param 查询参数
	 * @return 日到货数量
	 * @throws ParseException
	 */
	public BigDecimal selSdDaoh(Map param) throws ParseException{
		//查询满足条件的要货令集合
		List<Yaohl> listYaohl = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("zygzbj.querySdDh", param);
		//日期
		Date riq = CommonFun.yyyyMMddHHmmss.parse(CommonFun.strNull(param.get("startTime")));
		//日期
		Date riq2 = CommonFun.yyyyMMddHHmmss.parse(CommonFun.strNull(param.get("endTime")));
		//日期内到货数量
		BigDecimal zRiqDaoh = BigDecimal.ZERO;
		//遍历要货令集合
		for (int i = 0; i < listYaohl.size(); i++) {
			Yaohl yaohl = listYaohl.get(i);
			//如果拉箱指定到达时间不为空
			if(!StringUtils.isEmpty(yaohl.getLaxzdddsj())){
				Date laxzdddsj = CommonFun.yyyyMMddHHmmss.parse(yaohl.getLaxzdddsj());
				//如果拉箱指定到达时间等于指定日期
				if(laxzdddsj.compareTo(riq) >= 0 && laxzdddsj.compareTo(riq2) < 0 ){
					zRiqDaoh = zRiqDaoh.add(yaohl.getYaohsl());
				}
			}else if(!StringUtils.isEmpty(yaohl.getXiughyjjfsj())){
				//如果修改后预计交付时间不为空
				Date xiughyjjfsj = CommonFun.yyyyMMddHHmmss.parse(yaohl.getXiughyjjfsj());
				//如果修改后预计交付时间等于指定日期
				if(xiughyjjfsj.compareTo(riq) >= 0 && xiughyjjfsj.compareTo(riq2) < 0){
					zRiqDaoh = zRiqDaoh.add(yaohl.getYaohsl());
				}
			}else if(!StringUtils.isEmpty(yaohl.getZuiwsj())){
				//如果最晚到货时间不为空
				Date zuiwsj = CommonFun.yyyyMMddHHmmss.parse(yaohl.getZuiwsj());
				//如果最晚到达时间等于指定日期
				if(zuiwsj.compareTo(riq) >= 0 && zuiwsj.compareTo(riq2) < 0){
					zRiqDaoh = zRiqDaoh.add(yaohl.getYaohsl());
				}	
			}
		}
		//返回汇总数量
		return zRiqDaoh;
	}
	
	/**
	 * 汇总日到货数量
	 * @param param 查询参数
	 * @return 日到货数量
	 * @throws ParseException
	 */
	public BigDecimal getSdDaoh(Map param,List<Yaohl> listYaohl) throws ParseException{
		//日期
		Date riq = CommonFun.yyyyMMddHHmmss.parse(CommonFun.strNull(param.get("startTime")));
		//日期
		Date riq2 = CommonFun.yyyyMMddHHmmss.parse(CommonFun.strNull(param.get("endTime")));
		//日期内到货数量
		BigDecimal zRiqDaoh = BigDecimal.ZERO;
		//遍历要货令集合
		for (int i = 0; i < listYaohl.size(); i++) {
			Yaohl yaohl = listYaohl.get(i);
			//如果拉箱指定到达时间不为空
			if(!StringUtils.isEmpty(yaohl.getLaxzdddsj())){
				Date laxzdddsj = CommonFun.yyyyMMddHHmmss.parse(yaohl.getLaxzdddsj());
				//如果拉箱指定到达时间等于指定日期
				if(laxzdddsj.compareTo(riq) >= 0 && laxzdddsj.compareTo(riq2) < 0 ){
					zRiqDaoh = zRiqDaoh.add(yaohl.getYaohsl());
				}
			}else if(!StringUtils.isEmpty(yaohl.getXiughyjjfsj())){
				//如果修改后预计交付时间不为空
				Date xiughyjjfsj = CommonFun.yyyyMMddHHmmss.parse(yaohl.getXiughyjjfsj());
				//如果修改后预计交付时间等于指定日期
				if(xiughyjjfsj.compareTo(riq) >= 0 && xiughyjjfsj.compareTo(riq2) < 0){
					zRiqDaoh = zRiqDaoh.add(yaohl.getYaohsl());
				}
			}else if(!StringUtils.isEmpty(yaohl.getZuiwsj())){
				//如果最晚到货时间不为空
				Date zuiwsj = CommonFun.yyyyMMddHHmmss.parse(yaohl.getZuiwsj());
				//如果最晚到达时间等于指定日期
				if(zuiwsj.compareTo(riq) >= 0 && zuiwsj.compareTo(riq2) < 0){
					zRiqDaoh = zRiqDaoh.add(yaohl.getYaohsl());
				}	
			}
		}
		//返回汇总数量
		return zRiqDaoh;
	}

	/**
	 * 获取要货令时间
	 * @param yaohl 要货令
	 * @return 要货令时间
	 */
	public String getYhlSj(Yaohl yaohl){
		//判断拉箱指定时间是否为空,不为空取拉箱指定时间
		if(!StringUtils.isEmpty(yaohl.getLaxzdddsj())){
			return yaohl.getLaxzdddsj();
		//修改后预计交付时间不为空,则取修改后预计交付时间
		}else if(!StringUtils.isEmpty(yaohl.getXiughyjjfsj())){
			return yaohl.getXiughyjjfsj();
		//都为空则取最晚时间
		}else{
			return yaohl.getZuiwsj();
		}
	}
	
}
