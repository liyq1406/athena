package com.athena.xqjs.module.anxorder.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.athena.authority.entity.LoginUser;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.athena.xqjs.entity.anxorder.AnxMaoxq;
import com.athena.xqjs.entity.anxorder.Anxjscszjb;
import com.athena.xqjs.entity.anxorder.Kucjscsb;
import com.athena.xqjs.entity.anxorder.Ticxxsj;
import com.athena.xqjs.entity.common.CalendarGroup;
import com.athena.xqjs.entity.common.CalendarTeam;
import com.athena.xqjs.entity.common.CalendarVersion;
import com.athena.xqjs.entity.common.Gongys;
import com.athena.xqjs.entity.common.Lingj;
import com.athena.xqjs.entity.common.Lingjxhd;
import com.athena.xqjs.entity.common.Tidj;
import com.athena.xqjs.entity.common.Xiaohcyssk;
import com.athena.xqjs.entity.common.Xiehzt;
import com.athena.xqjs.entity.common.Xitcsdy;
import com.athena.xqjs.entity.common.Yunssk;
import com.athena.xqjs.entity.ilorder.Dingd;
import com.athena.xqjs.entity.ilorder.Dingdlj;
import com.athena.xqjs.entity.ilorder.Dingdmx;
import com.athena.xqjs.entity.ilorder.Ziykzb;
import com.athena.xqjs.entity.ycbj.Yicbj;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.common.service.GongysService;
import com.athena.xqjs.module.common.service.LingjService;
import com.athena.xqjs.module.common.service.LingjxhdService;
import com.athena.xqjs.module.common.service.XiaohcysskService;
import com.athena.xqjs.module.ilorder.service.DingdService;
import com.athena.xqjs.module.ilorder.service.DingdljService;
import com.athena.xqjs.module.ilorder.service.DingdmxService;
import com.athena.xqjs.module.ilorder.service.IlOrderService;
import com.athena.xqjs.module.ycbj.service.YicbjService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

/**
 * Title:按需计算
 * 
 * @author 李明
 * @version v1.0
 * @date 2012-03-19
 */
@SuppressWarnings({ "rawtypes" })
@Component
public class AnxOrderService extends BaseService {
	
	public  Logger logger = Logger.getLogger(AnxOrderService.class);
	// 定义锁定状态
	//public static boolean LOCKEDFLAG = true;
	@Inject
	// Il订单service
	private IlOrderService ilOrderService;
	@Inject
	// 异常报警service
	private YicbjService yicbjService;
	@Inject
	// 库存计算service
	private KucjscsbService kucjscsbService;
	@Inject
	// 按需毛需求service
	private AnxMaoxqService anxMaoxqService;
	@Inject
	// 零件消耗点service
	private LingjxhdService lingjxhdService;
	// 供应商service
	@Inject
	private GongysService gongysService;
	@Inject
	// 小火车运输时刻service
	private XiaohcysskService xiaohcysskService;
	@Inject
	// 按需中间表service
	private AnxjscszjbService anxjscszjbService;
	@Inject
	// 订单service
	private DingdService dingdService;
	@Inject
	// 订单明细service
	private DingdmxService dingdmxService;
	@Inject
	// 订单零件service
	private DingdljService dingdljService;
	@Inject
	// 零件service
	private LingjService lingjService;
	
	@Inject
	private AnxParam anxParam;
	
	/**
	 * 按需计算主方法
	 **/
	public void anxOrderMethod(String user) throws Exception {
		/**
		 * 查看计算正在进行，若正在继续，则插入到异常报警表中；反之，则正常进行计算，同时将锁定标志更新为false；
		 **/ 
			long start = System.currentTimeMillis();
			/**
			 * 数据准备
			 **/
			anxDataPreparation(user);
			long end = System.currentTimeMillis();
			CommonFun.logger.info("----------------------------按需计算数据,计算时间"+ (end - start)/1000);
			/**
			 * 按需计算分两种模式CD,MD和C1，M1
			 **/
			anxFilterPartten(user);
			CommonFun.logger.info("----------------------------按需计算主方法,计算时间"+ (System.currentTimeMillis() - end)/1000);
	}

	/**
	 * 拆分成时间段
	 * **/
	public void splitTime() throws Exception {
		CommonFun.logger.info("按需计算拆分按需毛需求为时间段开始");
		// 清除毛需求中间表
		this.anxMaoxqService.removeAnxMaoxqzjb();
		// 参数map
		Map<String, String> map = new HashMap<String, String>();
		// 格式化时间
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		// 时间间隔
		BigDecimal jiange = BigDecimal.ZERO;
		/**
		 * 按照用户中心，消耗点，零件编号分组，得到组内最大时间和最小时间
		 * **/
		List<AnxMaoxq> allMaoxq = this.anxMaoxqService.sumAllMaoxq(map);
		//拆分时间,昨天12点到今天12点
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		//取昨天日期
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		map.put("yesterday", CommonFun.sdf.format(calendar.getTime())+Const.TIME_12);
		map.put("today", CommonFun.getJavaTime("yyyy-MM-dd")+Const.TIME_12);
		// 获取时间间隔
		map.put("zidmc", Const.ZIDMC);
		List<Xitcsdy> listXitcsdy = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anx.queryShijjg",map);
		// 判断不为空
		if (!allMaoxq.isEmpty()) {
			// 循环开始
			for (AnxMaoxq obj : allMaoxq) {	
				for (int i = 0; i < listXitcsdy.size(); i++) {
					Xitcsdy xitcsdy = listXitcsdy.get(i);
					if(CommonFun.strNull(xitcsdy.getUsercenter()).equals(obj.getUsercenter())){
						// 查询出时间段间隔
						jiange = xitcsdy.getQujzxz();
						break;
					}
				}
				/*
				map.put("usercenter", obj.getUsercenter());
				map.put("zidmc", Const.ZIDMC);
				Xitcsdy xitcsdy = anxMaoxqService.queryAnxShijjg(map);
				jiange = xitcsdy.getQujzxz();
				*/
				//如果间隔为空,默认30分钟
				if(jiange == null || jiange.compareTo(BigDecimal.ZERO) == 0){
					jiange = new BigDecimal(30);
				}
				// 获取到开始时间
				String startTime = obj.getStartTime();
				// 下一个时间
				Date nextTimes = dateFormat.parse(startTime);
				// 最大时间
				Date maxTimes = new Date();
				maxTimes = dateFormat.parse(obj.getXhsj());
				
				map.put("usercenter", obj.getUsercenter());
				map.put("lingjbh", obj.getLingjbh());
				map.put("xiaohd", obj.getXiaohd());
				List<AnxMaoxq> listAnxMaoxq = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anx.queryAnxmaoxq",map);
				TreeMap<String,BigDecimal> mapAnxMaoxq = new TreeMap<String,BigDecimal>();
				for (int i = 0; i < listAnxMaoxq.size(); i++) {
					AnxMaoxq anxMaoxq = listAnxMaoxq.get(i);
					//nextTimes.setTime(dateFormat.parse(startTime).getTime() + jiange.intValue() * 60 * 1000 * i);
					Date xhsj = dateFormat.parse(anxMaoxq.getXhsj());
					int num = 1;
					boolean flag = true;
					do {
						// 得到下一时间段的时间点
						nextTimes.setTime(dateFormat.parse(startTime).getTime() + jiange.intValue() * 60 * 1000 * num);
						// 如果到了最大时间点，并且跳出循环
						if (nextTimes.after(maxTimes)) {
							// 下一时间段就是最大时间点
							//nextTimes = maxTimes;
							// 跳出循环的条件
							flag = false;
						} 
						// 如果开始时间和下一时间不在同一天,则重置开始时间,重置间隔为0
						if (CommonFun.sdf.parse(startTime).compareTo(CommonFun.sdf.parse(dateFormat.format(xhsj))) != 0) {
							startTime = dateFormat.format(xhsj);
							num = 1;
							continue;
						}
						if(xhsj.compareTo(nextTimes) < 0){
							String key = dateFormat.format(new Date(nextTimes.getTime() - jiange.intValue() * 60 * 1000));
							mapAnxMaoxq.put(key,CommonFun.getBigDecimal(mapAnxMaoxq.get(key)).add(anxMaoxq.getXiaohxs()));
							flag = false;
						}
						num ++;
					} while (flag);
				}
				for (String str : mapAnxMaoxq.keySet()) {
					obj.setXhsj(str);
					obj.setXiaohxs(mapAnxMaoxq.get(str));
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("anx.insertAnxmaoxqzjb2", obj);
				}
				/*
				int i = 1;
				// 将连续的时间拆分成时间段
				do {
					start = System.currentTimeMillis();
					maxTimes = dateFormat.parse(obj.getXhsj());
					nextTimes = dateFormat.parse(startTime);
					// 得到下一时间段的时间点
					nextTimes.setTime(nextTimes.getTime() + jiange.intValue() * 60 * 1000 * i);
					// 如果到了最大时间点，并且跳出循环
					if (nextTimes.after(maxTimes)) {
						// 下一时间段就是最大时间点
						nextTimes = maxTimes;
						// 跳出循环的条件
						flag = false;
					}
					// 加入起止时间的条件
					map.put("xiaohsj", startTime);
					map.put("xiaohsj2", dateFormat.format(nextTimes));
					// 如果开始时间和下一时间不在同一天,则重置开始时间,重置间隔为0
					if (CommonFun.sdf.parse(startTime).compareTo(CommonFun.sdf.parse(dateFormat.format(nextTimes))) != 0) {
						startTime = dateFormat.format(nextTimes);
						i = 1;
						continue;
					}
					long end2 = System.currentTimeMillis();
					System.out.println(end2 - end3);
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("anx.insertAnxmaoxqzjb", map);
					System.out.println(System.currentTimeMillis() - end2);
					i++;
					// startTime = dateFormat.format(nextTimes);
				} while (flag);
				*/
			}
		}
		CommonFun.logger.info("按需拆分毛需求为时间段结束");
	}

	/**
	 * 数据准备
	 **/
	public void anxDataPreparation(String user) throws Exception {
		// 第一步：将连续时间的毛需求汇总成时间段的毛需求
		//splitTime();
		// 第二步清空中间表(按需计算参数中间表)数据
		anxjscszjbService.doRemove();
		// 第三步：关联物流路径，零件消耗点，按需毛需求明细，把得到的结果汇总到按需计算参数中间表中
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("anx.insertAnxMaoxqhz");
		/*
		List<Anxjscszjb> anxjscszjbList = anxMaoxqService.queryForAnxjscszjb();
		if (!anxjscszjbList.isEmpty()) {
			// 最后：将结果汇总到按需计算参数中间表
			for (Anxjscszjb obj : anxjscszjbList) {
					//校验数据,如果通过,插入中间表
					if(checkData(obj,user)){
						obj.setXiaohsj(obj.getXiaohsj().substring(0, 19));
						obj.setChaifsj(obj.getChaifsj().substring(0, 19));
						// 插入到中间表
						anxjscszjbService.doInsert(obj);
					}
			}
		}
		*/
	}
	
	/**
	 * 获取计算日期
	 * @param obj 按需计算初始化中间对象
	 * @return 计算日期
	 * @throws ParseException 
	 */
	public int getJisDate(Anxjscszjb obj) throws ParseException{
		//如果是CD,C1,取备货周期,运输周期向上取整
		int day = 0;
		if(Const.ANX_MS_CD.equalsIgnoreCase(obj.getMos()) || Const.ANX_MS_C1.equalsIgnoreCase(obj.getMos())){
			day = obj.getBeihzq().add(obj.getYunszq()).setScale(0,BigDecimal.ROUND_UP).intValue();
		}else{//如果是MD,M1模式,取备货时间2+仓库送货时间2+仓库返回时间2向上取整
			day = obj.getBeihsj2().add(obj.getCangkshsj2()).add(obj.getCangkfhsj2()).setScale(0,BigDecimal.ROUND_UP).intValue();
		}
		return day;
	}
	
	/**
	 * 校验数据
	 * @param obj 数据对象
	 * @return true:校验通过;false:校验不通过
	 */
	public boolean checkData(Anxjscszjb obj,String user){
		//错误详细信息
		StringBuilder cuowxxxx = new StringBuilder("");
		//已发要货总量为空
		if(obj.getYifyhlzl() == null){
			cuowxxxx.append("已发要货总量为空");
		//交付总量为空
		}else if(obj.getJiaofzl() == null){
			cuowxxxx.append("交付总量为空");
		//终止总量为空
		}else if(obj.getZhongzzl() == null){ 
			cuowxxxx.append("终止总量为空");
		}else if(obj.getXiaohcbh() == null || obj.getXiaohcbh().equals("")){
			cuowxxxx.append("小火车编号为空");
		}else if(obj.getBeihzq() == null){
			cuowxxxx.append("备货周期为空");
		}else if(obj.getYunszq() == null){
			cuowxxxx.append("发运周期为空");
		}
		//C1/CD模式校验UA包装信息,M1/MD模式校验US包装信息
		if(CommonFun.strNull(obj.getMos()).equalsIgnoreCase(Const.ANX_MS_CD) || CommonFun.strNull(obj.getMos()).equalsIgnoreCase(Const.ANX_MS_C1)){
			if(obj.getGysucrl() == null || obj.getGysuaucgs() == null || CommonFun.strNull(obj.getGysuabzlx()).equals("") || CommonFun.strNull(obj.getGysucbzlx()).equals("")){
				cuowxxxx.append("UA包装信息为空");
			}
		}else if(CommonFun.strNull(obj.getMos2()).equalsIgnoreCase(Const.ANX_MS_M1)){
			if(obj.getCkusbzrl() == null || CommonFun.strNull(obj.getCkusbzlx()).equals("")){
				cuowxxxx.append("M1模式US包装信息为空");
			}
		}else{
			if(obj.getCkucrl() == null || CommonFun.strNull(obj.getCkuclx()).equals("")){
				cuowxxxx.append("MD模式UC包装信息为空");
			}
		}
		//如果错误详细信息不为空,则保存异常报警表,跳过本条数据.
		if(cuowxxxx.length() != 0){
			cuowxxxx.insert(0, "在供货模式%1下，零件%2-客户%3：");
			Map lingjMap = new HashMap();
			lingjMap.put("lingjbh", obj.getLingjbh());
			lingjMap.put("usercenter", obj.getUsercenter());
			// 查询零件
			Lingj lingj = (Lingj) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selLj", lingjMap);
			List<String> paramStr = new ArrayList<String>();
			paramStr.add(obj.getMos());
			paramStr.add(obj.getLingjbh());
			paramStr.add(obj.getXiaohd());
			yicbjService.insertError(Const.YICHANG_LX2,cuowxxxx.toString(), lingj.getJihy(), 
					paramStr, obj.getUsercenter(), obj.getLingjbh(), user, Const.JISMK_ANX_CD);
			return false;
		}
		return true;
	}

	/**
	 * 按需计算CD、MD
	 **/
	public Map anxCountCDMD(Anxjscszjb bean, String user,String dingdjssj,String shangcjssj) throws Exception {
		Map result = new HashMap();
		// 存放 参数map
		Map<String, String> map = new HashMap<String, String>();
		String gongzr = CommonFun.sdf.format(new Date());
		map.put("gongzr", gongzr);
		map.put("usercenter", bean.getUsercenter());
		map.put("lingjbh", bean.getLingjbh());
		map.put("shengcxbh", bean.getShengcxbh());
		map.put("xiaohd", bean.getXiaohd());
		map.put("appobj", bean.getXianh());// 产线
		int day = getJisDate(bean);
		map.put("num", String.valueOf(day + 1));
		List<Ticxxsj> listJsrq = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("common.queryTicxxsjGzrNum",map);
		//工作日历中没有相关的维护，系统给出报 警，不进行计算
		if(listJsrq == null || listJsrq.isEmpty()){
			Map lingjMap = new HashMap();
			lingjMap.put("lingjbh", bean.getLingjbh());
			lingjMap.put("usercenter", bean.getUsercenter());
			// 查询零件
			Lingj lingj = (Lingj) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selLj", lingjMap);
			List<String> paramStr = new ArrayList<String>();
			paramStr.add(bean.getMos());
			paramStr.add(bean.getLingjbh());
			paramStr.add(bean.getXiaohd());
			paramStr.add(String.valueOf(day + 1));
			yicbjService.insertError(Const.YICHANG_LX2, "在供货模式%1下，零件%2-客户%3：%4工作日历为空", lingj.getJihy(), 
					paramStr, bean.getUsercenter(), bean.getLingjbh(), user, Const.JISMK_ANX_CD);
			result.put("flag", 2);
			return result;
		}
		//计算日期,推最晚到货时间在计算日期工作时间内的才生成订单明细
		String jisrq = listJsrq.get(day - 1).getGongzr();
		//毛需求消耗时间
		Date xiaohsj = CommonFun.sdf.parse(bean.getXiaohsj());
		if(xiaohsj.before(CommonFun.sdf.parse(jisrq))){
			result.put("flag", 2);
			return result;
		}
		//订单类型
		String dingdlx = "";
		//模式
		String mos = "";
		//供应商代码字段,如果为C1/CD模式的,则存物流路径供应商代码,如果为M1/MD模式的则存订货库
		String gongysdm = "";
		//供应商类型,如果为C1/CD模式,则存物流路径供应商类型,如果为M1/MD模式则存物流路径订货库类型
		String gongyslx = "";
		// 得到资源要货量
		Map<String, BigDecimal> mapResource = anxResourceCount(bean,mos,user,dingdjssj,shangcjssj,day);
		//如果资源要货量为空,跳过当前
		if(mapResource == null){
			result.put("flag", 1);
			return result;
		}
		//将来模式生效日期
		Date shengxrq = null;
		BigDecimal anqkc = mapResource.get("anqkc") == null ? BigDecimal.ZERO : mapResource.get("anqkc");
		BigDecimal ziyyhl = mapResource.get("ziyyhl");
		// 取得第一时段的毛需求bean.getShul();
		if (CommonFun.strNull(bean.getMos()).equalsIgnoreCase(Const.ANX_MS_CD)) {
			mos = bean.getMos();
			gongysdm = bean.getGongysbh();
			gongyslx = bean.getGongyslx();
			//订单类型为 按需正常C
			dingdlx = Const.DINGD_LX_ANX_ZC_C;
			//将来模式生效日期不为空
			if(!CommonFun.strNull(bean.getShengxsj()).equals("")){
				shengxrq = CommonFun.sdf.parse(bean.getShengxsj());
			}
		} else {
			mos = bean.getMos2();
			gongysdm = bean.getDinghck();
			gongyslx = bean.getDinghcklx();
			//订单类型为 按需正常C
			dingdlx = Const.DINGD_LX_ANX_ZC_M;
			//将来模式生效日期不为空
			if(!CommonFun.strNull(bean.getShengxsj2()).equals("")){
				shengxrq = CommonFun.sdf.parse(bean.getShengxsj2());
			}
		}
		map.put("gongysdm",gongysdm);
		// 得到相关计算时间(最早，最晚及上线时间计算)
		Map<String, String> mapTime = anxTimeCount(bean,mos,user);
		//如果资源要货量为空,跳过当前
		if(mapTime == null){
			result.put("flag", 1);
			return result;
		}
		BigDecimal yhl = BigDecimal.ZERO;
		
		map.put("riq", jisrq);
		List<Ticxxsj> listTicxxsj = anxParam.ticxxsjMap.get(bean.getUsercenter()+bean.getShengcxbh()+jisrq);
		//queryCalendarTeam(map);
		//工作日历中没有相关的维护，系统给出报 警，不进行计算
		if(listTicxxsj == null || listTicxxsj.isEmpty()){
			Map lingjMap = new HashMap();
			lingjMap.put("lingjbh", bean.getLingjbh());
			lingjMap.put("usercenter", bean.getUsercenter());
			// 查询零件
			Lingj lingj = (Lingj) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selLj", lingjMap);
			List<String> paramStr = new ArrayList<String>();
			paramStr.add(bean.getMos());
			paramStr.add(bean.getLingjbh());
			paramStr.add(bean.getXiaohd());
			paramStr.add(jisrq);
			yicbjService.insertError(Const.YICHANG_LX2, "在供货模式%1下，零件%2-客户%3：%4工作日历为空", lingj.getJihy(), 
					paramStr, bean.getUsercenter(), bean.getLingjbh(), user, Const.JISMK_ANX_CD);
			result.put("flag", 2);
			return result;
		}
		//最晚到货时间
		Date zuiwdhsj = CommonFun.yyyyMMddHHmm.parse(mapTime.get("zuiwdhsj"));
		int flag = 0;
		//如果最晚到货时间早于到货日期开始工作时间,跳过
		if(zuiwdhsj.before(CommonFun.yyyyMMddHHmmss.parse(listTicxxsj.get(0).getRiq() + " " +listTicxxsj.get(0).getShijdkssj()))){
			flag = 1;
		//如果最晚到货时间早于到货日期开始工作时间,终止
		}else if(zuiwdhsj.after(CommonFun.yyyyMMddHHmmss.parse(listTicxxsj.get(listTicxxsj.size() - 1).getRiq() + " " + listTicxxsj.get(listTicxxsj.size() - 1).getShijdjssj()))){
			flag = -1;
		}
		//如果将来模式生效日期不为空,判断到货时间是否晚于生效日期,如果晚于表示模式已经切换,不要货
		if(shengxrq != null){
			Date dhsj = CommonFun.sdf.parse(mapTime.get("zuiwdhsj"));
			if(dhsj.after(shengxrq)){
				flag = 2;
			}
		}
		if (flag == 0 || flag == 1) {
			// 生成订单号
			Map dingdhMap = new HashMap();
			String dingdh = this.ilOrderService.getOrderNumber(mos, bean.getUsercenter(), gongysdm,dingdhMap);
			map.put("dingdh", dingdh);
			// 得到到货数量
			Dingdmx dingdmx = this.anxMaoxqService.queryCountAnxDingdmx(map);
			yhl = bean.getShul().subtract(ziyyhl).add(anqkc);
			// 当是第一次计算的时候，到货数量为0
			if (null != dingdmx) {
				yhl = bean.getShul().subtract(dingdmx.getShul()).add(dingdmx.getJissl());
			}
			if(yhl.compareTo(BigDecimal.ZERO) != 1){
				result.put("flag", flag);
				return result;
			}
			//yyyy-mm-dd日期
			String rq = CommonFun.sdf.format(CommonFun.sdf.parse(bean.getXiaohsj()));
			String shid = bean.getXiaohsj().substring(0,16);
				if(flag == 1){
					//如果早于,但是没有要货,则跳过
					rq = listTicxxsj.get(0).getRiq();
					shid = rq + " " +listTicxxsj.get(0).getShijdkssj();
					shid = shid.substring(0,16);
				}
			Dingd object = this.dingdService.queryDingdByDingdh(map);
			boolean dingdFlag = true;
			// 订单不存在的时候新增
			if (null == object) {
				Dingd dingd = new Dingd();
				// 合同号
				dingd.setHeth(bean.getGongyhth());
				// 订单号
				dingd.setDingdh(dingdh);
				// 创建时间
				dingd.setCreate_time(CommonFun.getJavaTime());
				// 订单状态
				dingd.setDingdzt(Const.DINGD_STATUS_YSX);
				// 供应商代码
				dingd.setGongysdm(gongysdm);
				dingd.setGongyslx(gongyslx);// 供应商类型
				// 用户中心
				dingd.setUsercenter(bean.getUsercenter());
				// 处理类型
				dingd.setChullx(mos);
				// 订单类型
				dingd.setDingdlx(dingdlx);// 类型
				// 删除标识
				dingd.setActive(Const.ACTIVE_1);
				dingd.setFahzq(rq);
				dingd.setShiffsgys(Const.SHIFFSGYS_NO);// 不是临时订单
				dingd.setDingdjssj(dingdjssj);// 订单计算时间
				dingd.setDingdnr(Const.SHIFOUSHIJIDING);// 既定
				dingd.setJislx(user);// 系统计算(admin)、用户提交(提交者用户名)
				dingd.setShifzfsyhl(Const.SHIFZFSYHL_YES);// 发送要货令
				// 是否要插入到订单
				dingdFlag = this.dingdService.doInsert(dingd);
			}
			// 是否插入成功
			if (dingdFlag) {
				
				/**
				 * 插入到订单明细
				 * **/
				Dingdmx mx = new Dingdmx();
				// 查询零件信息
				Lingj lingj = this.lingjService.queryObject(bean.getLingjbh(), bean.getUsercenter());
				// 得到供应商信息
				Gongys gongys = gongysService.queryObject(
						bean.getGongysbh(),
						bean.getUsercenter());
				// 订单号
				mx.setDingdh(dingdh);
				// 删除标识
				mx.setActive(Const.ACTIVE_1);
				//趟次
				String tangc = mapTime.get("tangc");
				if(tangc != null && !tangc.equals("")){
					//小火车趟次
					mx.setTangc(CommonFun.getBigDecimal(tangc));
				}
				// 用户中心
				mx.setUsercenter(bean.getUsercenter());
				//零件名称
				mx.setLingjmc(lingj.getZhongwmc());
				// 零件编号
				mx.setLingjbh(bean.getLingjbh());
				// 供应商代码
				mx.setGongysdm(gongysdm);
				//供应商名称
				mx.setGongsmc(gongys.getGongsmc());
				//供应商类型
				mx.setGongyslx(gongyslx);
				// 小火车编号
				mx.setXiaohch(bean.getXiaohcbh());
				mx.setGongyfe(bean.getGongysfe());
				// 消耗点
				mx.setXiaohd(bean.getXiaohd());
				mx.setFenpxh(bean.getFenpbh());
				mx.setXiaohccxh(bean.getXiaohccxbh());
				//承运商编号
				mx.setGcbh(bean.getGcbh());
				//仓库备货时间
				mx.setBeihsj2(bean.getBeihsj2());
				// 创建时间
				mx.setCreate_time(CommonFun.getJavaTime());
				// 创建人
				mx.setCreator(user);
				mx.setEditor(user);
				mx.setEdit_time(CommonFun.getJavaTime());
				// 要货日期
				mx.setYaohjsrq(rq);
				mx.setYaohqsrq(rq);
				// 计划员组
				mx.setJihyz(lingj.getJihy());
				mx.setLingjsx(lingj.getLingjsx());
				// 车间
				mx.setDinghcj(bean.getChej());
				
				// 交付日期
				mx.setJiaofrq(rq);
				// 状态
				mx.setZhuangt(Const.DINGD_STATUS_YSX);
				mx.setLeix(Const.SHIFOUSHIJIDING);// 既定
				mx.setZuihwhr(user);// 订单维护人
				mx.setZuihwhsj(CommonFun.getJavaTime(Const.TIME_FORMAT_HH_MM_SS));// 维护时间
				// 消耗时间
				mx.setShid(shid);
				// 产线
				mx.setChanx(bean.getXianh());
				mx.setXianbyhlx(bean.getXianbyhlx());
				// 仓库代码
				mx.setCangkdm(bean.getXianbck());
				// 指定供应商
				mx.setZhidgys(bean.getZhidgys());
				// 单位
				mx.setDanw(bean.getDanw());
				// 发货点
				mx.setFahd(bean.getFahd());
				// 卸货站台
				mx.setXiehzt(bean.getXiehztbh());
				mx.setYijfl(bean.getJiaofzl());
				mx.setZuixqdl(bean.getZuixqdl());
				// 消耗时间
				mx.setXiaohsj(shid);
				mx.setLujdm(bean.getLujbh());
				// 供货类型
				mx.setGonghlx(mos);
				if(flag == 0){
					mx.setZuizdhsj(mapTime.get("zuizdhsj"));// 最早到货时间
					mx.setZuiwdhsj(mapTime.get("zuiwdhsj"));// 最晚到货时间
					mx.setXiaohcbhsj(mapTime.get("beihsj"));//小火车备货时间
					mx.setXiaohcsxsj(mapTime.get("shangxsj"));//小火车上线时间
				}else{
					mx.setZuizdhsj(shid);// 最早到货时间
					mx.setZuiwdhsj(shid);// 最晚到货时间
					mx.setXiaohcbhsj(shid);//小火车备货时间
					mx.setXiaohcsxsj(shid);//小火车上线时间
				}
			
				mx.setGongyfe(bean.getGongysfe());
				// 判断模式来选择计算方式
				if (mos.equalsIgnoreCase(Const.ANX_MS_CD)) {
					
					// 份额计算
					BigDecimal fenejs = fenejs(mx, yhl);
					mx.setJissl(yhl);
					mx.setShul(CommonFun.roundingByPack(fenejs, bean.getGysucrl().multiply(bean.getGysuaucgs())));
					// 当为MD时候的计算方式
				} else {
					mx.setJissl(yhl);
					mx.setShul(CommonFun.roundingByPack(yhl, bean.getCkucrl()));
				}
				// UA类型
				mx.setUabzlx(bean.getGysuabzlx());
				mx.setUabzuclx(bean.getGysucbzlx());
				mx.setUabzucrl(bean.getGysucrl());
				mx.setUabzucsl(bean.getGysuaucgs());
				mx.setUsbaozlx(bean.getCkusbzlx());
				mx.setUsbaozrl(bean.getCkusbzrl());
				if (mx.getShul() != BigDecimal.ZERO) {
					// 插入到订单明细
					this.dingdmxService.doInsert(mx);
					Dingdlj dingdlj = dingdljService.addDingdlj(map, bean, user,mos,rq,gongysdm);
					result.put("dingdlj", dingdlj);
				}
			}
		}
		result.put("flag", flag);
		return result;
	}

	/**
	 * 按需计算C1、M1
	 */
	public Map anxCountC1M1(Anxjscszjb bean, String user,String dingdjssj,String shangcjssj) throws Exception {
		Map result = new HashMap();
		// 存放 参数map
		Map<String, String> map = new HashMap<String, String>();
		String gongzr = CommonFun.sdf.format(new Date());
		map.put("gongzr", gongzr);
		map.put("usercenter", bean.getUsercenter());
		map.put("lingjbh", bean.getLingjbh());
		map.put("gongysdm", bean.getGongysbh());
		map.put("shengcxbh", bean.getShengcxbh());
		map.put("xiaohd", bean.getXiaohd());
		map.put("appobj", bean.getXianh());// 产线
		int day = getJisDate(bean);
		map.put("num", String.valueOf(day + 1));
		List<Ticxxsj> listJsrq = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("common.queryTicxxsjGzrNum",map);
		//工作日历中没有相关的维护，系统给出报 警，不进行计算
		if(listJsrq == null || listJsrq.isEmpty()){
			Map lingjMap = new HashMap();
			lingjMap.put("lingjbh", bean.getLingjbh());
			lingjMap.put("usercenter", bean.getUsercenter());
			// 查询零件
			Lingj lingj = (Lingj) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selLj", lingjMap);
			List<String> paramStr = new ArrayList<String>();
			paramStr.add(bean.getMos());
			paramStr.add(bean.getLingjbh());
			paramStr.add(bean.getXiaohd());
			paramStr.add(String.valueOf(day + 1));
			yicbjService.insertError(Const.YICHANG_LX2, "在供货模式%1下，零件%2-客户%3：%4工作日历为空", lingj.getJihy(), 
					paramStr, bean.getUsercenter(), bean.getLingjbh(), user, Const.JISMK_ANX_CD);
			result.put("flag", 2);
			return result;
		}
		//计算日期,推最晚到货时间在计算日期工作时间内的才生成订单明细
		String jisrq = listJsrq.get(day - 1).getGongzr();
		//毛需求消耗时间
		Date xiaohsj = CommonFun.sdf.parse(bean.getXiaohsj());
		if(xiaohsj.before(CommonFun.sdf.parse(jisrq))){
			result.put("flag", 2);
			return result;
		}
		//订单类型
		String dingdlx = "";
		//模式
		String mos = "";
		//供应商代码字段,如果为C1/CD模式的,则存物流路径供应商代码,如果为M1/MD模式的则存订货库
		String gongysdm = "";
		//供应商类型,如果为C1/CD模式的,则存物流路径供应商类型,如果为M1/MD模式的则存订货库类型
		String gongyslx = "";
		/*
		 * 资源总量
		 */
		BigDecimal ziyyhl = BigDecimal.ZERO;
		/*
		 * 安全库存
		 */
		BigDecimal anqkc = BigDecimal.ZERO;
		Map<String, BigDecimal> mapResource = anxResourceCount(bean,mos,user,dingdjssj,shangcjssj,day);
		//如果资源要货量为空,跳过当前
		if(mapResource == null){
			result.put("flag", 1);
			return result;
		}
		if (!mapResource.isEmpty()) {
			anqkc = mapResource.get("anqkc") == null ? BigDecimal.ZERO : mapResource.get("anqkc");
			ziyyhl = mapResource.get("ziyyhl");
		}
		//将来模式生效日期
		Date shengxrq = null;
		// 第一时段的需求：第一时段的毛需求+安全库存
		// bean.getShul().add(anqkc);
		// 第一时段要货量：
		if (CommonFun.strNull(bean.getMos()).equalsIgnoreCase(Const.ANX_MS_C1)) {
			mos = bean.getMos();
			gongysdm = bean.getGongysbh();
			gongyslx = bean.getGongyslx();
			//订单类型为按需正常C
			dingdlx = Const.DINGD_LX_ANX_ZC_C;
			//将来模式生效日期不为空
			if(!CommonFun.strNull(bean.getShengxsj()).equals("")){
				shengxrq = CommonFun.sdf.parse(bean.getShengxsj());
			}
		} else {
			mos = bean.getMos2();
			gongysdm = bean.getDinghck();
			map.put("gongysdm", gongysdm);
			gongyslx = bean.getDinghcklx();
			//订单类型为按需正常M
			dingdlx = Const.DINGD_LX_ANX_ZC_M;
			//将来模式生效日期不为空
			if(!CommonFun.strNull(bean.getShengxsj2()).equals("")){
				shengxrq = CommonFun.sdf.parse(bean.getShengxsj2());
			}
		}
		// 得到相关计算时间(最早，最晚及上线时间计算)
		Map<String, String> timeMap = anxTimeCount(bean,mos,user);
		//如果资源要货量为空,跳过当前
		if(timeMap == null){
			result.put("flag", 1);
			return result;
		}
		BigDecimal yhl = BigDecimal.ZERO;

		map.put("riq", jisrq);
		List<Ticxxsj> listTicxxsj = anxParam.ticxxsjMap.get(bean.getUsercenter()+bean.getShengcxbh()+jisrq);
		//queryCalendarTeam(map);
		//工作日历中没有相关的维护，系统给出报 警，不进行计算
		if(listTicxxsj == null || listTicxxsj.isEmpty()){
			Map lingjMap = new HashMap();
			lingjMap.put("lingjbh", bean.getLingjbh());
			lingjMap.put("usercenter", bean.getUsercenter());
			// 查询零件
			Lingj lingj = (Lingj) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selLj", lingjMap);
			List<String> paramStr = new ArrayList<String>();
			paramStr.add(bean.getMos());
			paramStr.add(bean.getLingjbh());
			paramStr.add(bean.getXiaohd());
			paramStr.add(jisrq);
			yicbjService.insertError(Const.YICHANG_LX2, "在供货模式%1下，零件%2-客户%3：%4工作日历为空", lingj.getJihy(), 
					paramStr, bean.getUsercenter(), bean.getLingjbh(), user, Const.JISMK_ANX_CD);
			result.put("flag", 1);
			return result;
		}
		//最晚到货时间
		Date zuiwdhsj = CommonFun.yyyyMMddHHmm.parse(timeMap.get("zuiwdhsj"));
		int flag = 0;
		//如果最晚到货时间早于到货日期开始工作时间,跳过
		if(zuiwdhsj.before(CommonFun.yyyyMMddHHmmss.parse(listTicxxsj.get(0).getRiq() + " " +listTicxxsj.get(0).getShijdkssj()))){
			flag = 1;
		//如果最晚到货时间早于到货日期开始工作时间,终止
		}else if(zuiwdhsj.after(CommonFun.yyyyMMddHHmmss.parse(listTicxxsj.get(listTicxxsj.size() - 1).getRiq() + " " + listTicxxsj.get(listTicxxsj.size() - 1).getShijdjssj()))){
			flag = -1;
		}
		//如果将来模式生效日期不为空,判断到货时间是否晚于生效日期,如果晚于表示模式已经切换,不要货
		if(shengxrq != null){
			Date dhsj = CommonFun.sdf.parse(timeMap.get("zuiwdhsj"));
			if(dhsj.after(shengxrq)){
				flag = 2;
			}
		}
		// 当前时间在等于最晚到货时间
		if (flag != -1) {
			// 生成订单号
			Map dingdhMap = new HashMap();
			String dingdh = ilOrderService
					.getOrderNumber(mos, bean.getUsercenter(), gongysdm,dingdhMap);
			// 根据订单号判断订单是否存在
			map.put("dingdh", dingdh);
			// 得到到货数量
			Dingdmx dingdmx = this.anxMaoxqService.queryCountAnxDingdmx(map);
			yhl = bean.getShul().subtract(ziyyhl).add(anqkc);
			// 当是第一次计算的时候，到货数量为0
			if (null != dingdmx) {
				yhl = bean.getShul().subtract(CommonFun.getBigDecimal(dingdmx.getShul())).add(CommonFun.getBigDecimal(dingdmx.getJissl()));
			}
			if(yhl.compareTo(BigDecimal.ZERO) != 1){
				result.put("flag", flag);
				return result;
			}
			//yyyy-mm-dd日期
			String rq = CommonFun.sdf.format(CommonFun.sdf.parse(bean.getXiaohsj()));
			String shid = bean.getXiaohsj().substring(0,16);
				if(flag == 1){
					//如果早于,但是没有要货,则跳过
					rq = listTicxxsj.get(0).getRiq();
					shid = rq + " " +listTicxxsj.get(0).getShijdkssj();
					shid = shid.substring(0,16);
				}
			
			Dingd object = dingdService.queryDingdByDingdh(map);
			map.clear();
			boolean dingdFlag = true;
			if (null == object) {
				Dingd dingd = new Dingd();
				// 合同号
				dingd.setHeth(bean.getGongyhth());
				// 订单号
				dingd.setDingdh(dingdh);
				// 删除标识
				dingd.setActive(Const.ACTIVE_1);
				dingd.setDingdlx(dingdlx);// 类型
				// 创建时间
				dingd.setCreate_time(CommonFun.getJavaTime());
				// 订单状态
				dingd.setDingdzt(Const.DINGD_STATUS_YSX);
				// 供应商代码
				dingd.setGongysdm(gongysdm);
				dingd.setGongyslx(gongyslx);// 供应商类型
				// 用户中心
				dingd.setUsercenter(bean.getUsercenter());
				// 处理类型
				dingd.setChullx(mos);
				dingd.setFahzq(rq);//发货周期
				dingd.setShiffsgys(Const.SHIFFSGYS_NO);// 不是临时订单
				dingd.setDingdjssj(dingdjssj);// 订单计算时间
				dingd.setDingdnr(Const.SHIFOUSHIJIDING);// 既定
				dingd.setJislx(new LoginUser().getUsername());// 系统计算(admin)、用户提交(提交者用户名)
				dingd.setShifzfsyhl(Const.SHIFZFSYHL_YES);// 发送要货令
				// 插入到订单
				dingdFlag = dingdService.doInsert(dingd);
			}
			if (dingdFlag) {
				map.put("usercenter", bean.getUsercenter());
				map.put("lingjbh", bean.getLingjbh());
				map.put("xiaohd", bean.getXiaohd());
				map.put("dingdh", dingdh);
				
				/**
				 * 插入到订单明细
				 * **/
				Dingdmx mx = new Dingdmx();
				Lingj lingj = this.lingjService.queryObject(bean.getLingjbh(), bean.getUsercenter());
				// 得到供应商信息
				Gongys gongys = gongysService.queryObject(
						bean.getGongysbh(),
						bean.getUsercenter());
				// 订单号
				mx.setDingdh(dingdh);
				// 删除标识
				mx.setActive(Const.ACTIVE_1);
				//承运商编号
				mx.setGcbh(bean.getGcbh());
				//零件名称
				mx.setLingjmc(lingj.getZhongwmc());
				//仓库备货时间
				mx.setBeihsj2(bean.getBeihsj2());
				// 用户中心
				mx.setUsercenter(bean.getUsercenter());
				// 零件编号
				mx.setLingjbh(bean.getLingjbh());
				//供应商名称
				mx.setGongsmc(gongys.getGongsmc());
				// 供应商代码
				mx.setGongysdm(gongysdm);
				//供应商类型
				mx.setGongyslx(gongyslx);
				mx.setGongyfe(bean.getGongysfe());// 供应份额
				// 小火车编号
				mx.setXiaohch(bean.getXiaohcbh());
				// 消耗点
				mx.setXiaohd(bean.getXiaohd());
				mx.setFenpxh(bean.getFenpbh());
				mx.setXiaohccxh(bean.getXiaohccxbh());
				// 创建时间
				mx.setCreate_time(CommonFun.getJavaTime());
				// 创建人
				mx.setCreator(user);
				mx.setYaohjsrq(rq);
				mx.setYaohqsrq(rq);
				mx.setJihyz(lingj.getJihy());
				mx.setLingjsx(lingj.getLingjsx());
				mx.setDinghcj(bean.getChej());
				mx.setUabzlx(bean.getGysuabzlx());
				mx.setUabzuclx(bean.getGysucbzlx());
				mx.setUabzucrl(bean.getGysucrl());
				mx.setUabzucsl(bean.getGysuaucgs());
				mx.setUsbaozlx(bean.getCkusbzlx());
				mx.setUsbaozrl(bean.getCkusbzrl());
				
				// 交付日期
				mx.setJiaofrq(rq);
				// 状态
				mx.setZhuangt(Const.DINGD_STATUS_YSX);
				mx.setLeix(Const.SHIFOUSHIJIDING);// 既定
				mx.setZuihwhr(user);// 订单维护人
				mx.setZuihwhsj(CommonFun.getJavaTime(Const.TIME_FORMAT_HH_MM_SS));// 维护时间
				// 消耗时间
				mx.setShid(shid);
				// 产线
				mx.setChanx(bean.getXianh());
				//
				mx.setXianbyhlx(bean.getXianbyhlx());
				// 仓库代码
				mx.setCangkdm(bean.getXianbck());
				// 指定供应商
				mx.setZhidgys(bean.getZhidgys());
				// 单位
				mx.setDanw(bean.getDanw());
				// 发货地
				mx.setFahd(bean.getFahd());
				// 卸货站台
				mx.setXiehzt(bean.getXiehztbh());
				mx.setYijfl(bean.getJiaofzl());
				mx.setZuixqdl(bean.getZuixqdl());
				// 消耗时间
				mx.setXiaohsj(shid);
				// 路径代码
				mx.setLujdm(bean.getLujbh());
				// 供货类型
				mx.setGonghlx(mos);
				if(flag == 0){
					mx.setZuizdhsj(timeMap.get("zuizdhsj"));// 最早到货时间
					mx.setZuiwdhsj(timeMap.get("zuiwdhsj"));// 最晚到货时间
				}else{
					mx.setZuizdhsj(shid);// 最早到货时间
					mx.setZuiwdhsj(shid);// 最晚到货时间
				}
				mx.setGongyfe(bean.getGongysfe());
				// 判断模式来选择计算方式
				if (mos.equalsIgnoreCase(Const.ANX_MS_C1)) {
					// 份额计算
					BigDecimal fenejs = this.fenejs(mx, yhl);
					mx.setJissl(yhl);
					mx.setShul(CommonFun.roundingByPack(fenejs, bean.getGysucrl().multiply(bean.getGysuaucgs())));
				} else {
					mx.setJissl(yhl);
					mx.setShul(CommonFun.roundingByPack(yhl, bean.getCkusbzrl()));
				}
				if (mx.getShul() != BigDecimal.ZERO) {
					// 插入到订单明细
					dingdmxService.doInsert(mx);
					Dingdlj dingdlj = dingdljService.addDingdlj(map, bean, user,mos,rq,gongysdm);
					result.put("dingdlj", dingdlj);
				}
			}
		}
		result.put("flag", flag);
		return result;
	}
	
	/**
	 * 计算替代件的数量
	 * **/
	public BigDecimal queryTidjsl(String cangk, String usercenter, String lingjbh, String riq) {
		// 替代件总量
		BigDecimal tidjsl = BigDecimal.ZERO;
		BigDecimal xbsl = BigDecimal.ZERO;
		// 参数map
		Map<String, String> map = new HashMap<String, String>();
		// 查询替代件信息
		List<Tidj> tidjAll = this.anxMaoxqService.queryTidjAll(usercenter, lingjbh);
		//CommonFun.objListPrint(tidjAll, "计算替代件的数量queryTidjsl方法tidjAll");
		// 判断集合不为空
		if (!tidjAll.isEmpty()) {
			// 线边仓库的数量
			for (Tidj bean : tidjAll) {
				// 参数map
				map.put("usercenter", usercenter);
				map.put("lingjbh", bean.getTidljh());
				map.put("cangkdm", cangk);
				map.put("ziyhqrq", riq.substring(0, 10));
				// 查找资源快照表
				Ziykzb ziykzb = this.anxMaoxqService.queryZykzObject(map);
				//CommonFun.objPrint(ziykzb, "计算替代件的数量queryTidjsl方法ziykzb");
				map.clear();
				// 得到替代件的数量
				if (ziykzb != null && null != ziykzb.getKucsl()) {
					xbsl = ziykzb.getKucsl().add(xbsl);
					CommonFun.logger.debug("计算替代件的数量queryTidjsl方法(ziykzb != null)时xbsl="+xbsl);
				}
			}
		}
		return tidjsl;
	}
	
	
	//////////////////wuyichao 2014-05-16 ////////////////////
	/**
	 * 计算替代件的数量
	 * **/
	public BigDecimal queryTidjsls(String usercenter, String lingjbh, String riq) {
		// 替代件总量
		BigDecimal xbsl = BigDecimal.ZERO;
		// 参数map
		Map<String, String> map = new HashMap<String, String>();
		// 查询替代件信息
		List<Tidj> tidjAll = this.anxMaoxqService.queryTidjAll(usercenter, lingjbh);
		//CommonFun.objListPrint(tidjAll, "计算替代件的数量queryTidjsl方法tidjAll");
		// 判断集合不为空
		if (!tidjAll.isEmpty()) {
			// 线边仓库的数量
			for (Tidj bean : tidjAll) {
				// 参数map
				map.put("usercenter", usercenter);
				map.put("lingjbh", bean.getTidljh());
				map.put("ziyhqrq", riq.substring(0, 10));
				// 查找资源快照表
				Ziykzb ziykzb = this.anxMaoxqService.queryZykzObjects(map);
				//CommonFun.objPrint(ziykzb, "计算替代件的数量queryTidjsl方法ziykzb");
				map.clear();
				// 得到替代件的数量
				if (ziykzb != null && null != ziykzb.getKucsl()) {
					xbsl = ziykzb.getKucsl().add(xbsl);
				}
			}
		}
		return xbsl;
	}

	//////////////////wuyichao 2014-05-16 ////////////////////
	/**
	 * 计算替代件的数量
	 * **/
	public BigDecimal anxQueryTidjsl(String cangk, String usercenter, String lingjbh, String riq) throws Exception {
		// 替代件总量
		BigDecimal tidjsl = BigDecimal.ZERO;
		BigDecimal xbsl = BigDecimal.ZERO;
		// 参数map
		Map<String, String> map = new HashMap<String, String>();
		// 查询替代件信息
		//List<Tidj> tidjAll = this.anxMaoxqService.queryTidjAll(usercenter, lingjbh);
		List<Tidj> tidjAll = anxParam.tidjMap.get(usercenter+lingjbh);
		// 判断集合不为空
		if (tidjAll != null && !tidjAll.isEmpty()) {
			// 线边仓库的数量
			for (Tidj bean : tidjAll) {
				// 参数map
				map.put("usercenter", usercenter);
				map.put("lingjbh", bean.getTidljh());
				map.put("cangkdm", cangk);
				map.put("ziyhqrq", riq.substring(0, 10));
				// 查找资源快照表
				Ziykzb ziykzb = this.anxMaoxqService.queryZykzObject(map);
				map.clear();
				// 得到替代件的数量
				if (ziykzb != null) {
					xbsl = ziykzb.getKucsl().add(xbsl);
				}
			}
		}
		return tidjsl;
	}
	
	/**
	 * 计算线边库存
	 * @param bean 计算参数
	 * @return 线边库存数量
	 */
	public BigDecimal getXianbkc(Anxjscszjb bean,String dingdjssj,String user,String shangcjssj) {
		BigDecimal xianbkc = BigDecimal.ZERO;
		// 参数map
		Map<String, String> map = new HashMap<String, String>();
		map.put("usercenter", bean.getUsercenter());
		map.put("lingjbh", bean.getLingjbh());
		map.put("xiaohdbh", bean.getXiaohd());
		List<Ticxxsj> listTicxxsj = anxParam.ticxxsjMap.get(bean.getUsercenter()+bean.getShengcxbh()+dingdjssj.substring(0,10));
		BigDecimal kucslDxh = BigDecimal.ZERO;
		if(listTicxxsj != null && !listTicxxsj.isEmpty()){
			Ticxxsj ticxxsj = listTicxxsj.get(0);
			String jn =	ticxxsj.getGongzr() + " " +ticxxsj.getShijdkssj();	
			// 上次计算时间
			if(!shangcjssj.equals("")){
				map.put("xiaohsj2", jn.substring(0, 16));
				map.put("xiaohsj", shangcjssj.substring(0, 16));
				kucslDxh = CommonFun.getBigDecimal(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anx.queryXiaohl", map));
			}
		} 
		// 在库存计算参数表中获取到统计后的待消耗总量，异常总量,到库量，理论库存量
		// 获取到最新的线边理论库存数量，更新到零件消耗点中
		Kucjscsb kucsl = kucjscsbService.queryAllKuc(map);
		Lingjxhd lingjxhd = lingjxhdService.queryLingjxhdObject(map);
		//计算线边库存
		xianbkc = CommonFun.getBigDecimal(lingjxhd.getXianbllkc()).add(CommonFun.getBigDecimal(kucsl.getDaohl()))
			.subtract(CommonFun.getBigDecimal(kucsl.getYicxhl())).subtract(kucslDxh);
		return xianbkc;
	}
	
	/**
	 * 计算未交付量
	 **/
	public BigDecimal getWeijf(Anxjscszjb bean,BigDecimal yifyhlzl,BigDecimal jiaofzl,BigDecimal zhongzzl,String user) throws Exception {
		// 未交付总量
		BigDecimal weijfzl = BigDecimal.ZERO;
			/*
			 * 未交付 = 已发要货令总量-交付总量-终止总量
			 */
			weijfzl = yifyhlzl.subtract(jiaofzl).subtract(zhongzzl);
			// 校验得到的未交付数量是否为负数，若为负数插入到异常报警表中
			if (weijfzl.intValue() < 0) {
				Map lingjMap = new HashMap();
				lingjMap.put("lingjbh", bean.getLingjbh());
				lingjMap.put("usercenter", bean.getUsercenter());
				// 查询零件
				Lingj lingj = (Lingj) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selLj", lingjMap);
				List<String> paramStr = new ArrayList<String>();
				paramStr.add(bean.getMos());
				paramStr.add(bean.getLingjbh());
				paramStr.add(bean.getXiaohd());
				paramStr.add(weijfzl.toString());
				yicbjService.insertError(Const.YICHANG_LX2, "在供货模式%1下，零件%2-客户%3：%4未交付数量为负数", lingj.getJihy(), 
						paramStr, bean.getUsercenter(), bean.getLingjbh(), user, Const.JISMK_ANX_CD);
			}
		// 返回数
		return weijfzl;
	}

	/**
	 * 更新线边理论库存
	 * 
	 * @param bean
	 */
	public void updateXbllkc(Anxjscszjb bean,String user,String dingdjssj,String shangcjssj) {
		Map map = new HashMap();
		map.put("usercenter", bean.getUsercenter());
		map.put("lingjbh", bean.getLingjbh());
		map.put("xiaohdbh", bean.getXiaohd());
		map.put("xiaohd", bean.getXiaohd());
		Lingjxhd lingjxhd = lingjxhdService.queryLingjxhdObject(map);
		List<Ticxxsj> listTicxxsj = anxParam.ticxxsjMap.get(bean.getUsercenter()+bean.getShengcxbh()+dingdjssj.substring(0,10));
		BigDecimal kucslDxh = BigDecimal.ZERO;
		if(listTicxxsj != null && !listTicxxsj.isEmpty()){
			Ticxxsj ticxxsj = listTicxxsj.get(0);
			String jn =	ticxxsj.getGongzr() + " " +ticxxsj.getShijdkssj();	
			// 上次计算时间
			if(!shangcjssj.equals("")){
				map.put("xiaohsj2", jn.substring(0, 16));
				map.put("xiaohsj", shangcjssj.substring(0, 16));
				kucslDxh = CommonFun.getBigDecimal(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anx.queryXiaohl", map));
			}
		} 
		// 在库存计算参数表中获取到统计后的待消耗总量，异常总量,到库量，理论库存量
		Kucjscsb kucsl = kucjscsbService.queryAllKuc(map);
		// 设置现编理论库存
		lingjxhd.setXianbllkc(CommonFun.getBigDecimal(lingjxhd.getXianbllkc()).add(CommonFun.getBigDecimal(kucsl.getDaohl()))
				.subtract(CommonFun.getBigDecimal(kucsl.getYicxhl())).subtract(kucslDxh));
		lingjxhd.setEditor(user);// 后期需要获取当前登录信息，设置到其中
		lingjxhd.setEdit_time(CommonFun.getJavaTime());
		// 将得到的库存更新到零件消耗点中
		lingjxhdService.doUpdate(lingjxhd);
	}

	/*
	 * 计算最终库存
	 */
	public Ziykzb lastKucsl(Anxjscszjb bean, String time) {
		// 参数map
		Map<String, String> map = new HashMap<String, String>();
		// 最终得到库存
		map.put("usercenter", bean.getUsercenter());
		map.put("lingjbh", bean.getLingjbh());
		map.put("cangkdm", bean.getXianbck().substring(0, 3));
		map.put("ziyhqrq", time.substring(0, 10));
		// 查询资源快照表
		Ziykzb ziykzb = this.anxMaoxqService.queryZykzObject(map);
		//查不到库存,把库存当做0计算
		if(ziykzb == null){
			ziykzb = new Ziykzb();
			ziykzb.setAnqkc(BigDecimal.ZERO);
			ziykzb.setKucsl(BigDecimal.ZERO);
			ziykzb.setDingdlj(BigDecimal.ZERO);
			ziykzb.setJiaoflj(BigDecimal.ZERO);
			ziykzb.setDingdzzlj(BigDecimal.ZERO);
		}
		return ziykzb;
	}

	/**
	 * 资源计算
	 **/
	public Map<String, BigDecimal> anxResourceCount(Anxjscszjb bean,String mos,String user,String dingdjssj,String shangcjssj,int day) throws Exception {
		// 参数map
		Map<String, String> map = new HashMap<String, String>();
		// 存放结果map
		Map<String, BigDecimal> mapResource = new HashMap<String, BigDecimal>();
		// 资源总量
		BigDecimal ziyyhl = BigDecimal.ZERO;
		// 未交付总量
		BigDecimal weijfzl = BigDecimal.ZERO;
		// 替代件总量
		BigDecimal tidjzl = BigDecimal.ZERO;
		// 从初始化资源表中取得数据，首先取得标志，判断是否初始化布线
		map.put("usercenter", bean.getUsercenter());
		map.put("lingjbh", bean.getLingjbh());
		map.put("shengcxbh", bean.getShengcxbh());
		map.put("xiaohdbh", bean.getXiaohd());
		map.put("zhongzlxh", bean.getLiush());
		// 最终得到库存
		Ziykzb ziykzb = lastKucsl(bean, CommonFun.getJavaTime());
		if(Const.ANX_MS_C1.equals(mos) || Const.ANX_MS_M1.equals(mos)){
			// 得到未交付数量
			weijfzl = getWeijf(bean,ziykzb.getDingdlj(),ziykzb.getJiaoflj(),bean.getZhongzzl(),user);
		}else{
			// 得到未交付数量
			weijfzl = getWeijf(bean,bean.getYifyhlzl(),bean.getJiaofzl(),bean.getZhongzzl(),user);
		}
		// 替代件数量
		tidjzl = anxQueryTidjsl(bean.getXianbck(), bean.getUsercenter(), bean.getLingjbh(), CommonFun.getJavaTime());
		// 计算待消耗
		map.put("appobj", bean.getXianh());// 产线
		//待消耗取计算日期向后推1日
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		String jisrq = CommonFun.sdf.format(calendar.getTime()).substring(0,10);
		map.put("gongzr", jisrq);
		map.put("num", String.valueOf(day+1));
		// 查询工作时间
		List<Ticxxsj> listTicxxsj1 = anxParam.ticxxsjMap.get(bean.getUsercenter()+bean.getShengcxbh()+jisrq);
		// 查询后一个工作日开班时间
		Ticxxsj ticxxsj = (Ticxxsj) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("common.queryTicxxsjNum",map);
		/*
		try {
			//listCalendarTeam = queryCalendarTeam(map);
		} catch (Exception e) {
			
		}
		*/
		//工作日历中没有相关的维护，系统给出报 警，不进行计算
		if(listTicxxsj1 == null || listTicxxsj1.isEmpty() || ticxxsj == null){
			Map lingjMap = new HashMap();
			lingjMap.put("lingjbh", bean.getLingjbh());
			lingjMap.put("usercenter", bean.getUsercenter());
			// 查询零件
			Lingj lingj = (Lingj) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selLj", lingjMap);
			List<String> paramStr = new ArrayList<String>();
			paramStr.add(bean.getMos());
			paramStr.add(bean.getLingjbh());
			paramStr.add(bean.getXiaohd());
			paramStr.add(jisrq);
			yicbjService.insertError(Const.YICHANG_LX2, "在供货模式%1下，零件%2-客户%3：%4工作日历为空", lingj.getJihy(), 
					paramStr, bean.getUsercenter(), bean.getLingjbh(), user, Const.JISMK_ANX_CD);
			return null;
		}
		map.put("kaissj", jisrq + " " + listTicxxsj1.get(0).getShijdkssj());// 开始时间
		map.put("jiessj", ticxxsj.getRiq() + " " + ticxxsj.getShijdkssj());// 结束时间
		// 计算待消耗
		BigDecimal daixh = CommonFun.getBigDecimal(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anx.queryDxh", map));
		// CD,MD模式
		if (CommonFun.strNull(bean.getMos()).equalsIgnoreCase(Const.ANX_MS_CD) || 
				CommonFun.strNull(bean.getMos2()).equalsIgnoreCase(Const.ANX_MS_MD)) {
			BigDecimal xianbkc = getXianbkc(bean,dingdjssj,user,shangcjssj);
			// 资源要货量 = （未交付+库存）,需要考虑替代件的数量
			ziyyhl = xianbkc.add(weijfzl.add(ziykzb.getKucsl()).add(tidjzl).subtract(daixh));
			// C1,M1模式
		} else if (CommonFun.strNull(bean.getMos()).equalsIgnoreCase(Const.ANX_MS_C1)
				|| CommonFun.strNull(bean.getMos2()).equalsIgnoreCase(Const.ANX_MS_M1)) {
			ziyyhl = ziykzb.getKucsl().add(weijfzl).subtract(daixh);
						// 替代件信息
			tidjzl = queryTidjsl(bean.getXianbck(), bean.getUsercenter(), bean.getLingjbh(),
					jisrq);
			// 资源总量+替代件数量
			ziyyhl = ziyyhl.add(tidjzl);
			// 设置安全库存
			mapResource.put("anqkc", ziykzb.getAnqkc());
		}
		// 设置资源要货量
		mapResource.put("ziyyhl", ziyyhl);
		return mapResource;
	}

	/**
	 * 查询工作时间
	 * 
	 * @param map
	 *            查询参数
	 * @return 工作时间表
	 */
	public List<CalendarTeam> queryCalendarTeam(Map map) {
		// 根据用户中心+产线编号查询工作日历,获得日历版次,工作时间编号
		
		CalendarGroup calendarGroup = (CalendarGroup) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("common.queryCalendarGroupObject", map);
		map.put("banc", calendarGroup.getRilbc());
		// 根据用户中心+日历版次+日期查询周序
		CalendarVersion calendarVersion = (CalendarVersion) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject(
				"calendarVersion.queryCalendarVersionNianzq", map);
		map.put("xingqxh", calendarVersion.getXingq());
		map.put("bianzh", calendarGroup.getBianzh());
		// 根据工作时间编号,星期序号查询工作时间
		List<CalendarTeam> listCalendarTeam = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("calendarVersion.queryCalendarTeam", map);
		return listCalendarTeam;
	}

	/**
	 * 检查拆分时间 判断拆分时间是是否在：昨天12点到今天12点之前
	 * **/
	public boolean checkChaifsj(Anxjscszjb bean,LoginUser user) throws Exception {
		boolean flag = false;
		// 定义时间格式
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = Const.TIME_12;
		// 得到当前时间拼接到12点
		String nowTime = CommonFun.getJavaTime().substring(0, 10) + time;
		Date lastDay = new Date();
		// 当前时间减去24个小时候，得到昨天的中午时间
		lastDay.setTime(dateFormat.parse(nowTime).getTime() - 24 * 60 * 60 * 1000);
		if (null != bean) {
			// 判断拆分时间是否大于昨天的时间同时小于今天时间
			if (dateFormat.parse(nowTime).getTime() >= dateFormat.parse(bean.getChaifsj()).getTime()
					&& dateFormat.parse(bean.getChaifsj()).getTime() >= lastDay.getTime()) {
				flag = true;
				// 当拆分时间错误的时候，插入到异常报警表中
			} else {
				Yicbj yc = new Yicbj();
				yc.setCuowlx("200");
				yc.setCuowxxxx("按需毛需求中的拆分时间错误");
				yc.setUsercenter(bean.getUsercenter());
				yc.setLingjbh(bean.getLingjbh());
				yc.setJihydm(user.getUsername());
				Map map = new HashMap();
				map.put("lingjbh", bean.getLingjbh());
				map.put("usercenter", bean.getUsercenter());
				// 查询零件
				Lingj lingj = (Lingj) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selLj", map);
				yc.setJihyz(lingj.getJihy());
				yc.setCreator(user.getUsername());
				String yicTime = CommonFun.getJavaTime();
				yc.setCreate_time(yicTime);
				yc.setEditor(user.getUsername());
				yc.setEdit_time(yicTime);
				yc.setJismk("33");
				// 插入到异常
				this.yicbjService.insert(yc);
			}
		}
		return flag;
	}

	/**
	 * 过滤模式(CD,MD,C1,M1)
	 * **/
	public void anxFilterPartten(String user) throws Exception {
		
		// 参数map
		Map<String, String> map = new HashMap<String, String>();
		anxParam.initParam(map);
		List<Dingdlj> listDingdlj = new ArrayList<Dingdlj>();
		//订单计算时间
		String dingdjssj = CommonFun.getJavaTime(Const.TIME_FORMAT_HH_MM_SS);
		CommonFun.logger.info("按需计算数据计算开始,计算时间--------------"+dingdjssj);
		String shangcjssj = dingdmxService.getShangcjssj();
		long start = System.currentTimeMillis();
		// 取得中间表中用户中心+零件编号+消耗点全部信息
		map.put("mos", "CD");
		map.put("mos2", "MD");
		List<Anxjscszjb> allAnxjscszjb = this.anxjscszjbService.queryAllAnxjscszjb(map);
		if (!allAnxjscszjb.isEmpty()) {
			for (Anxjscszjb bean : allAnxjscszjb) {
				map.put("usercenter", bean.getUsercenter());
				map.put("lingjbh", bean.getLingjbh()); 
				map.put("xiaohd", bean.getXiaohd());
				BigDecimal yingy = BigDecimal.ZERO;//盈余
				BigDecimal xuqsl = BigDecimal.ZERO;//需求
				// 查询全部信息
				List<Anxjscszjb> anxjscszjbList = this.anxjscszjbService.queryAllAnxjscszjbList(map);
				// 判断不为空
				if (!anxjscszjbList.isEmpty()) {
					// 循环计算
					for (Anxjscszjb anxjscszjb : anxjscszjbList) {
						try {
							if(!checkData(anxjscszjb, user)){
								continue;
							}
									Map result = anxCountCDMD(anxjscszjb, user,dingdjssj,shangcjssj);
									int flagCDMD = (Integer) result.get("flag");
									Object dingdlj = result.get("dingdlj");
									if (dingdlj != null) {
										listDingdlj.add((Dingdlj) dingdlj);
									}
									if (flagCDMD < 0) {
										break;
									}
									
							} catch (Exception e) {
								Map lingjMap = new HashMap();
								lingjMap.put("lingjbh", bean.getLingjbh());
								lingjMap.put("usercenter", bean.getUsercenter());
								// 查询零件
								Lingj lingj = (Lingj) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selLj", lingjMap);
								List<String> paramStr = new ArrayList<String>();
								paramStr.add(bean.getMos()+bean.getMos2());
								paramStr.add(bean.getLingjbh());
								paramStr.add(bean.getXiaohd());
								paramStr.add(e.toString());
								yicbjService.insertError(Const.YICHANG_LX2, "在供货模式%1下，零件%2-客户%3：%4", lingj.getJihy(), 
										paramStr, bean.getUsercenter(), bean.getLingjbh(), user, Const.JISMK_ANX_CD);
								CommonFun.logger.error("按需计算异常--------"+e);
							}
						}
					//}
				}
			}
			map.put("mos", "C1");
			map.put("mos2", "M1");
			allAnxjscszjb = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anx.queryAnxjscszjbForAnxC1M1",map);
			if (!allAnxjscszjb.isEmpty()) {
				for (Anxjscszjb bean : allAnxjscszjb) {
					map.put("usercenter", bean.getUsercenter());
					map.put("lingjbh", bean.getLingjbh()); 
					map.put("xianbck", bean.getXianbck());
				// 查询全部信息
				List<Anxjscszjb> anxjscszjbList2 = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anx.queryListAnxjscszjbC1M1", map);
				// 判断不为空
				if (!anxjscszjbList2.isEmpty()) {
					// 循环计算
					for (Anxjscszjb anxjscszjb : anxjscszjbList2) {
						try {
							if(!checkData(anxjscszjb, user)){
								continue;
							}
							Map result = anxCountC1M1(anxjscszjb, user,dingdjssj,shangcjssj);
							int flagC1M1 = (Integer) result.get("flag");
							Object dingdlj = result.get("dingdlj");
							if (dingdlj != null) {
								listDingdlj.add((Dingdlj) dingdlj);
							}
							if (flagC1M1 < 0) {
								break;
							}
							} catch (Exception e) {
								Map lingjMap = new HashMap();
								lingjMap.put("lingjbh", bean.getLingjbh());
								lingjMap.put("usercenter", bean.getUsercenter());
								// 查询零件
								Lingj lingj = (Lingj) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selLj", lingjMap);
								List<String> paramStr = new ArrayList<String>();
								paramStr.add(bean.getMos()+bean.getMos2());
								paramStr.add(bean.getLingjbh());
								paramStr.add(bean.getXiaohd());
								paramStr.add(e.toString());
								yicbjService.insertError(Const.YICHANG_LX2, "在供货模式%1下，零件%2-客户%3：%4", lingj.getJihy(), 
										paramStr, bean.getUsercenter(), bean.getLingjbh(), user, Const.JISMK_ANX_CD);
								CommonFun.logger.error("按需计算异常--------"+e);
							}
						}
					//}
				}
				}
			}
			long end = System.currentTimeMillis();
			CommonFun.logger.info("按需计算数据计算结束,耗时----------------"+(end - start));
			
			// 更新的CD,MD的线边理论库存
			List<Anxjscszjb> listAnxjscszjb = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anx.queryXbkcXhd");
			for (int i = 0; i < listAnxjscszjb.size(); i++) {
				updateXbllkc(listAnxjscszjb.get(i),user,dingdjssj,shangcjssj);
			}
			long end2 = System.currentTimeMillis();
			CommonFun.logger.info("按需计算更新CD,MD线边理论库存结束,耗时-----------"+(end2 - end));
			// 更新订单零件数量
			for (int i = 0; i < listDingdlj.size(); i++) {
				Dingdlj dingdlj = listDingdlj.get(i);
				dingdlj.setP0sl(CommonFun.getBigDecimal(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anx.queryDingdmxSl", dingdlj)));
				dingdljService.doUpdateForKd(dingdlj);
			}
			anxParam.destory();
			
		}
	}

	/**
	 * 份额计算
	 * 
	 * @param bean 按需订单明细
	 * @param zongyhl 时段总要货量
	 * 
	 * @return 份额计算后的要货量
	 *
	 * @throws ServiceException
	 */
	public BigDecimal fenejs(Dingdmx bean, BigDecimal zongyhl) throws ServiceException {
	 
	logger.info(this.getClass().getName() +"：份额计算开始：fenejs():");
	// 订单明细为空，终止计算
	if (null == bean) {
	logger.info(this.getClass().getName() +"：份额计算异常结束：fenejs():Dingdmx bean == null");
	logger.debug(this.getClass().getName() +"：份额计算正常结束：yaohl=BigDecimal.ZERO");
	return BigDecimal.ZERO;
	}
	// 要货量
	BigDecimal yaohl = BigDecimal.ZERO;
	// 指定供应商时
	if (!StringUtils.isBlank(bean.getZhidgys())) {
	 
	// 指定的供应商代码  = 订单明细关联的供应商时
	if (bean.getGongysdm().equalsIgnoreCase(bean.getZhidgys())) {
	// 要货份额为100%
	yaohl = zongyhl;
	// 指定的供应商代码  ≠ 订单明细关联的供应商时
	} else {
	// 要货量为０
	yaohl = BigDecimal.ZERO;
	}
	// 未指定供应商时
	} else {
	// 要货量 = 总要货量   X 要货份额%
	yaohl = zongyhl.multiply(bean.getGongyfe());
	}
	logger.info(this.getClass().getName() +"：份额计算正常结束：fenejs():");
	logger.debug(this.getClass().getName() +"：份额计算正常结束：yaohl=" + yaohl);
	 
	// 返回计算份额后的要货量
	return yaohl;
	}


	/*
	 * 时间计算(最早，最晚及上线时间计算) 返回结果位map暂定为存放的最早，最晚，预计到货时间
	 */
	public Map<String, String> anxTimeCount(Anxjscszjb bean,String mos,String user) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		// 定义最早到货时间
		String zuizdhsj = "";
		// 最晚到货时间
		String zuiwdhsj = "";
		// 定义预计到货时间
		String yujdhsj = "";
		// 定义上线时间
		String shangxsj = "";
		// 定义备货时间
		String beihsj = "";
		/**
		 * 从卸货站台得到提前到货时间
		 */
		//Xiehzt xiehzt = this.anxMaoxqService.queryXiehztObject(map);
		Xiehzt xiehzt = anxParam.xiehztMap.get(bean.getUsercenter()+bean.getXiehztbh());
		map.clear();
		// 允许提前到货时间
		BigDecimal yunxtqdhsj = xiehzt.getYunxtqdhsj();
		//如果备货时间为空
		if(yunxtqdhsj == null){
			Map lingjMap = new HashMap();
			lingjMap.put("lingjbh", bean.getLingjbh());
			lingjMap.put("usercenter", bean.getUsercenter());
			// 查询零件
			Lingj lingj = (Lingj) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selLj", lingjMap);
			List<String> paramStr = new ArrayList<String>();
			paramStr.add(bean.getMos());
			paramStr.add(bean.getLingjbh());
			paramStr.add(bean.getXiaohd());
			paramStr.add(bean.getXiehztbh());
			yicbjService.insertError(Const.YICHANG_LX2, "在供货模式%1下，零件%2-客户%3：%4允许提前到货时间为空", lingj.getJihy(), 
					paramStr, bean.getUsercenter(), bean.getLingjbh(), user, Const.JISMK_ANX_CD);
			return null;
		}
		/**
		 * 从卸货站台循环时间得到内部物流时间
		 */
		//map.put("usercenter", bean.getUsercenter());
		//map.put("xiehztbh", bean.getXiehztbh());
		//map.put("cangkbh", bean.getDinghck());
		//map.put("mos", mos);
		//Xiehztxhsj xiehztxhsj = this.anxMaoxqService.queryXiehztxhsjObject(map);.
		//Xiehztxhsj xiehztxhsj = anxParam.xiehztxhsjMap.get(bean.getUsercenter()+bean.getDinghck()+bean.getXiehztbh()+mos);
		//map.clear();
		//如果备货时间为空
		/*
		if(xiehztxhsj == null){
			Yicbj yicbj = new Yicbj();
			yicbj.setCuowlx("200");
			yicbj.setCuowxxxx("卸货站台备货时间为空");
			yicbj.setUsercenter(bean.getUsercenter());
			yicbj.setLingjbh(bean.getLingjbh());
			yicbj.setJihydm(user);
			yicbj.setJihyz(user);
			yicbj.setJismk("33");
			yicbj.setCreator(user);
			String yicTime = CommonFun.getJavaTime();
			yicbj.setCreate_time(yicTime);
			yicbj.setEditor(user);
			yicbj.setEdit_time(yicTime);
			// 插入到异常
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ycbj.insertYcbj", yicbj);
			return null;
		}
		*/
		// 内部物流时间
		BigDecimal neibwlsj = bean.getBeihsjc();
		//如果备货时间为空
		if(neibwlsj == null){
			Map lingjMap = new HashMap();
			lingjMap.put("lingjbh", bean.getLingjbh());
			lingjMap.put("usercenter", bean.getUsercenter());
			// 查询零件
			Lingj lingj = (Lingj) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selLj", lingjMap);
			List<String> paramStr = new ArrayList<String>();
			paramStr.add(bean.getMos());
			paramStr.add(bean.getLingjbh());
			paramStr.add(bean.getXiaohd());
			yicbjService.insertError(Const.YICHANG_LX2, "在供货模式%1下，零件%2-客户%3：内部物流时间为空", lingj.getJihy(), 
					paramStr, bean.getUsercenter(), bean.getLingjbh(), user, Const.JISMK_ANX_CD);
			return null;
		}
		Date date = new Date();
		Date dateBeihsj = new Date();
		if (mos.equalsIgnoreCase(Const.ANX_MS_CD) || 
				mos.equalsIgnoreCase(Const.ANX_MS_MD)) {
			// ==============备货时间--strat==============
			map.put("usercenter", bean.getUsercenter());
			// map.put("riq", CommonFun.getJavaTime().substring(0, 10));
			map.put("xiaohcbh", bean.getXiaohcbh());
			map.put("shengcxbh", bean.getShengcxbh());
			map.put("kaisbhsj", bean.getXiaohsj().substring(0, 16));
			// 取小火车运输时刻
			List<Xiaohcyssk> Listxhcyssk = xiaohcysskService.queryXiaohcysskObject(map);
			//如果小火车时刻表没有数据,跳过该条记录,不予计算
			if(Listxhcyssk == null || Listxhcyssk.isEmpty()){
				Map lingjMap = new HashMap();
				lingjMap.put("lingjbh", bean.getLingjbh());
				lingjMap.put("usercenter", bean.getUsercenter());
				// 查询零件
				Lingj lingj = (Lingj) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selLj", lingjMap);
				List<String> paramStr = new ArrayList<String>();
				paramStr.add(bean.getMos());
				paramStr.add(bean.getLingjbh());
				paramStr.add(bean.getXiaohd());
				paramStr.add(bean.getXiaohcbh());
				yicbjService.insertError(Const.YICHANG_LX2, "在供货模式%1下，零件%2-客户%3：%4小火车编号为空", lingj.getJihy(), 
						paramStr, bean.getUsercenter(), bean.getLingjbh(), user, Const.JISMK_ANX_CD);
				return null;
			}
			map.clear();
			// 第一条运输时间如果和消耗时间相等,则取前面一条小火车备货时间为备货时间
			// 反之,取第二条
			Xiaohcyssk xiaohcyssk = Listxhcyssk.get(1);
			// 备货时间
			beihsj = xiaohcyssk.getKaisbhsj();
			// 上线时间
			shangxsj = xiaohcyssk.getChufsxsj();
			map.put("tangc", CommonFun.strNull(xiaohcyssk.getTangc()));//趟次
			/**
			 * ==============上线时间--end==============
			 * **/
			// 预计到货时间(yujdhsj)=小火车备货时间-内部物流时间
			Date dates = CommonFun.yyyyMMddHHmm.parse(beihsj);
			dates.setTime(dates.getTime() - neibwlsj.intValue() * 60 * 1000);
			// 最后得到:预计到货时间
			yujdhsj = CommonFun.yyyyMMddHHmm.format(dates);
			dateBeihsj = CommonFun.yyyyMMddHHmm.parse(beihsj);
		} else {
			// C1,M1模式的时间
			// 预计到货时间(yujdhsj)=消耗时间-内部物流时间
			dateBeihsj = CommonFun.yyyyMMddHHmm.parse(bean.getXiaohsj());
		}
		
		date.setTime(dateBeihsj.getTime() - neibwlsj.intValue() * 60 * 1000);

		/**
		 * 获取到最晚到货时间
		 * */
		map.put("usercenter", bean.getUsercenter());
		if(bean.getXiehztbh() == null || bean.getXiehztbh().equals("")){
			Map lingjMap = new HashMap();
			lingjMap.put("lingjbh", bean.getLingjbh());
			lingjMap.put("usercenter", bean.getUsercenter());
			// 查询零件
			Lingj lingj = (Lingj) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zygzbj.selLj", lingjMap);
			List<String> paramStr = new ArrayList<String>();
			paramStr.add(bean.getMos());
			paramStr.add(bean.getLingjbh());
			paramStr.add(bean.getXiaohd());
			paramStr.add(bean.getXiaohcbh());
			yicbjService.insertError(Const.YICHANG_LX2, "在供货模式%1下，零件%2-客户%3：%4卸货站台编号为空", lingj.getJihy(), 
					paramStr, bean.getUsercenter(), bean.getLingjbh(), user, Const.JISMK_ANX_CD);
			return null;
		}
		map.put("xiehztbh", bean.getXiehztbh().substring(0,4));//取编号前4位为卸货站台编组
		map.put("gcbh", bean.getGcbh());//承运商编号
		zuiwdhsj = CommonFun.yyyyMMddHHmm.format(date);
		map.put("daohsj", zuiwdhsj);
		List<Yunssk> listYunssk = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anx.queryYunsskObject", map);
		if (listYunssk != null && !listYunssk.isEmpty()) {
			zuiwdhsj = listYunssk.get(0).getDaohsj().substring(0, 16);
		}
		Date zuizDate = new Date();
		zuizDate.setTime(CommonFun.yyyyMMddHHmm.parse(zuiwdhsj).getTime() - yunxtqdhsj.intValue() * 60 * 1000);

		// 最早到货时间
		zuizdhsj = CommonFun.yyyyMMddHHmm.format(zuizDate);
		// 存放到Map中
		map.put("zuiwdhsj", zuiwdhsj);
		map.put("zuizdhsj", zuizdhsj);
		map.put("yujdhsj", yujdhsj);
		map.put("shangxsj", shangxsj);
		map.put("beihsj", beihsj);

		return map;
	}
}