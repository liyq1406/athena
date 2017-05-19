/**
 * 参考IlOrderService的Vj计算需要服务
 */
package com.athena.xqjs.module.hlorder.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.date.DateUtil;
import com.athena.util.exception.ServiceException;
import com.athena.xqjs.entity.common.CalendarCenter;
import com.athena.xqjs.entity.common.LingjGongys;
import com.athena.xqjs.entity.common.Lingjxhd;
import com.athena.xqjs.entity.common.Nianxb;
import com.athena.xqjs.entity.common.Tidj;
import com.athena.xqjs.entity.common.Wullj;
import com.athena.xqjs.entity.common.Xiaohblzjb;
import com.athena.xqjs.entity.common.Xiehzt;
import com.athena.xqjs.entity.hlorder.MaoxqhzjcTmp;
import com.athena.xqjs.entity.hlorder.VjKuczh;
import com.athena.xqjs.entity.hlorder.VjXianbkc;
import com.athena.xqjs.entity.ilorder.Dingd;
import com.athena.xqjs.entity.ilorder.Dingdlj;
import com.athena.xqjs.entity.ilorder.Dingdmx;
import com.athena.xqjs.entity.ilorder.Ziykzb;
import com.athena.xqjs.entity.ycbj.Yicbj;
import com.athena.xqjs.module.anxorder.service.AnxMaoxqService;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.common.service.CalendarCenterService;
import com.athena.xqjs.module.common.service.JidygzqService;
import com.athena.xqjs.module.common.service.LingjxhdService;
import com.athena.xqjs.module.ilorder.service.DingdService;
import com.athena.xqjs.module.ilorder.service.NianxbService;
import com.athena.xqjs.module.ycbj.service.YicbjService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

@Component
public class VjOrderService extends BaseService {

	public final Logger log = Logger.getLogger(VjOrderService.class);

	@Inject
	private CalendarCenterService calendarCenterService;// 用户中心日历service

	@Inject
	private MaoxqhzjTmpService maoxqhzjTmpService;// 注入毛需求汇总sJervice

	@Inject
	private MaoxqhzjcTmpService maoxqhzjcTmpService;// 注入毛需求汇总JCservice

	@Inject
	private NianxbService nianxbService;// 注入年包service

	@Inject
	private VjMaoxqmxService vjMaoxqmxService;// 毛需求明细service

	@Inject
	private VjFeneService vjFeneService; // VJ份额service

	@Inject
	private YugzjbTmpService yugzjbTmpService; // 预告中间表service

	@Inject
	private DingdService dingdservice;// 订单service

	@Inject
	private YicbjService yicbjservice; // 异常报警service

	@Inject
	private JidygzqService jdygzqservice;// 既定预告周期service

	@Inject
	private UserOperLog userOperLog; // 用户日志service

	@Inject
	private AnxMaoxqService anxMaoxqService;// 按需毛需求service

	@Inject
	private LingjxhdService lingjxhdservice;// 零件消耗点service

	@Inject
	private VJWulljService vjWulljService;// VJ物流路径service

	/**
	 * 获取订单号
	 * 
	 * @参数说明：String pattern，需求类型,String usercenter 用户中心,String gongysdm 供应商代码
	 */
	public String getOrderNumber(String pattern, String usercenter,
			String gongysdm, Map<String, String> orderNumberMap,
			MaoxqhzjcTmp maoxqhzjc, LoginUser loginUse) throws ServiceException {// 根据不同的需求类型分别来获取订单号
		String mos = pattern.substring(0, 1);
		CommonFun.logger.debug("获取订单号getOrderNumber方法中mos=" + mos);
		CommonFun.logger
				.debug("获取订单号getOrderNumber方法中usercenter=" + usercenter);
		CommonFun.logger.debug("获取订单号getOrderNumber方法中gongysdm=" + gongysdm);
		// 判断在map中是否存在订单号，若存在则直接返回订单号，
		if (null != orderNumberMap.get(usercenter + gongysdm + mos)) {
			// 直接返回订单号
			CommonFun.mapPrint(orderNumberMap,
					"获取订单号getOrderNumber方法中orderNumberMap");
			return orderNumberMap.get(usercenter + gongysdm + mos);
		}
		// 若不存在对应的订单号，则先生成部分订单号
		// 流水号
		String liushStr = "";
		// 获取到年
		String year = CommonFun.getDate(Const.YEARY);
		CommonFun.logger.debug("获取订单号getOrderNumber方法中year=" + year);
		// 获取到月份
		String month0 = CommonFun.getDate(Const.MONTH);
		CommonFun.logger.debug("获取订单号getOrderNumber方法中month0=" + month0);
		// 获取到日
		String day = CommonFun.getDate(Const.DAY);
		CommonFun.logger.debug("获取订单号getOrderNumber方法中day=" + day);
		// 转换得到16进制的月份
		String month = Integer.toHexString(Integer.valueOf(month0))
				.toUpperCase();
		CommonFun.logger.debug("获取订单号getOrderNumber方法中month=" + month);
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
					Const.YICHANG_LX2_str66, maoxqhzjc.getJihyz(), paramStr,
					usercenter, maoxqhzjc.getLingjbh(), loginUse,
					Const.JISMK_IL_CD);
			throw new ServiceException("获取到年为空值,请检查年型码设置");
		}

		// 通过获取到的年型的实体，得到需要的年型
		year = nianx.getNianx();
		CommonFun.logger.debug("获取订单号getOrderNumber方法中year=" + year);
		// 将各种形式放到map中
		Map<String, String> map = CommonFun.getPartten(year, month, day);
		// 得到部分订单号
		String orderNumber = map.get(pattern.toUpperCase());
		CommonFun.logger.debug("获取订单号getOrderNumber方法中orderNumber="
				+ orderNumber);
		// 由部分订单号去订单明细总去匹配查询
		List<Dingd> list = this.dingdservice.queryOrderNumbers(orderNumber);
		// CommonFun.objListPrint(list, "订单查找返回list");
		// 若查寻的结果集为空，则直接加上流水号0001，存入到map中
		if (list.isEmpty()) {
			// 生成最终的订单号
			orderNumber += Const.LSH_1;
			liushStr = Const.LSH_1;
			CommonFun.logger
					.debug("获取订单号getOrderNumber方法中订单号list为空时orderNumber="
							+ orderNumber);
			// 若得到的结果不为空，则表示已经存在，则在流水号上加1
		} else {
			// 定义订单号的模板
			DecimalFormat data = new DecimalFormat(Const.LSH_0);
			// 数据库中流水号值
			int dataLiush = Integer.valueOf(list.get(0).getDingdh()
					.substring(5, list.get(0).getDingdh().length())) + 1;
			// MAP缓存中流水号值
			int mapLiush = Integer.valueOf(StringUtils.defaultIfEmpty(
					orderNumberMap.get("liush"), "0")) + 1;
			if (mapLiush >= dataLiush) {
				liushStr = data.format(mapLiush);
			} else {
				liushStr = data.format(dataLiush);
			}
			// 若得到的结果不为空，则表示已经存在，则在流水号上加1
			orderNumber = orderNumber + liushStr;
			CommonFun.logger
					.debug("获取订单号getOrderNumber方法中订单号list不为空时orderNumber="
							+ orderNumber);
		}
		// 保存流水号字符串
		orderNumberMap.put("liush", liushStr);
		// 将最终生成的订单号存放到map中
		orderNumberMap.put(usercenter + gongysdm + mos, orderNumber);
		return orderNumber.trim();// 返回订单号
	}

	/**
	 * 清除中间表的数据
	 * 
	 * @author wyg
	 * @version v1.0
	 * @date 2011-12-1 参数说明：String pattern，需求类型
	 */
	public void clearDataByUsercenter(String pattern, String usercenter) {
		CommonFun.logger.info("Il订单计算模式为" + pattern + "的中间表清理开始");
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单计算",
				"清除中间表的数据开始");
		// 清除预告中间表
		this.yugzjbTmpService.doAllDelete(usercenter);
		this.maoxqhzjTmpService.doAllDelete(usercenter);// 调用毛需求汇总vJ的删除方法
		this.maoxqhzjcTmpService.doAllDelete(usercenter);// 调用毛需求汇总关联参考系vJ的删除方法
		CommonFun.logger.info("Il订单计算模式为" + pattern + "的中间表清理结束");
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单计算",
				"清除中间表的数据结束");
	}

	/**
	 * @see 删除计算中的订单
	 * @param user
	 * @param dingdlx
	 * @param active
	 */
	private void deleteDingd(String user, String dingdlx, String active,
			String usercenter) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("creator", user);
		param.put("dingdlx", dingdlx);
		param.put("usercenter", usercenter);
		if (StringUtils.isNotBlank(active)) {
			param.put("active", active);
		}
		// 清除订单内计算中的数据
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
				"hlorder.deleteDingdLjJsz", param);
		// 清除订单明细内计算中的数据
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
				"hlorder.deleteDingdMxJsz", param);
		// 清除订单内计算中的数据
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
				"hlorder.deleteDingdJsz", param);
		// 使用DingdljTmp.xml删除没有实际存在订单号的订单零件
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
				"hlorder.deleteDingdLjByNoDingdh");
		// 使用DingdmxTmp.xml删除没有实际存在订单号的订单明细
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
				"hlorder.deleteDingdMxByNoDingdh");
	}

	/**
	 * Title:根据vj计算修改成的vj计算 国产件订单计算 Description:按照选取的毛需求计算既定和预告
	 * 参数为订单类型,资源获取日期,用户中心组 计算是否顺利完成
	 * 
	 * @param zhizlx
	 *            制造路线 "97W"
	 * @param dingdlx
	 *            订单类型 "VJ"
	 * @param ziyxqrq
	 *            资源获取日期
	 * @param jiihyz
	 *            ""
	 * @param banc
	 *            需求版次
	 * @param userString
	 *            用户字符 "UL"
	 * @param loginUser
	 *            登录用户
	 * 
	 * @author wuyg
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws ClassNotFoundException
	 * @throws ParseException
	 * @date 2011-12-1
	 */

	public String doCalculate(String zhizlx, String dingdlx, String ziyxqrq,
			String jiihyz, String banc, String userString, LoginUser loginUser,
			List<Dingd> dingdList, String jkCode) {
		Map<String, String> orderNumberMap = new HashMap<String, String>();

		CommonFun.logger.debug("国产件订单类型为" + dingdlx + "的计算操作开始");
		String message = "";
		String jssj = CommonFun.getJavaTime();
		Integer count = 0;
		// 用于存储maoxqhzpc表的数据
		List<MaoxqhzjcTmp> ls = new ArrayList<MaoxqhzjcTmp>();
		// 用于存储dingdlj表的数据
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单计算",
				"取得用户中心开始");
		String caozz = loginUser.getUsername();
		
		//用户中心
		loginUser.setUsercenter(userString);
		 
		CommonFun.logger.debug("国产件订单计算操作者" + caozz);
		// String banch = "";
		CommonFun.logger.debug("国产件订单计算用户中心" + userString);
		Map<String, String> dingdhMap = new HashMap<String, String>();
		CommonFun.logger.debug("国产件订单计算订货路线为" + zhizlx);
		// 清空计算中的订单信息
		this.deleteDingd(null, "'" + Const.DINGDLX_ILORDER + "'", "",
				userString);
		// }

		Map<String, BigDecimal> resMap = new HashMap<String, BigDecimal>();
		boolean flag = false;// 用于判断是否有多个既定

		this.clearDataByUsercenter(dingdlx, userString);// 清理中间表
		this.conversionColRow(banc, dingdlx, ziyxqrq, userString, zhizlx);// 取出需要计算的毛需求并进行行列转换

		// 按中心，零件，产线，使用车间汇总毛需求
		// 在vj模式中，不需要效验的零件消耗点是否满足百分百，删除效验方法checkXiaohbl(dingdlx);
		this.checkXiaohbl(dingdlx, userString);

		long startx = System.currentTimeMillis();
		// 汇总并插入中间表
		// 将毛需求汇总到仓库，并关联参考系参数然后将数据插入到毛需求汇总_参考系表
		message = this
				.maoxqhzglckx(ziyxqrq, dingdlx, jiihyz, zhizlx, loginUser);
		if (null != message) {
			return message;
		}
		long endx = System.currentTimeMillis();
		CommonFun.logger.info("----------------------------IL计算,单次计算时间"
				+ (endx - startx) / 1000);
		CommonFun.logger.debug("国产件订单类型为" + dingdlx + "的参数校验");

		// 效验中间表数据
		count = this.check(loginUser, dingdlx);
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单计算", "数据准备结束");

		// ///////////////////wuyichao 优化订单明细 订单零件为批量插入//////////////////
		Map<String, List<Dingdlj>> saveLingjMap = new HashMap<String, List<Dingdlj>>();
		Map<String, List<Dingdmx>> saveMingxMap = new HashMap<String, List<Dingdmx>>();
		// ///////////////////wuyichao 优化订单明细 订单零件为批量插入//////////////////
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单周期订单计算",
				"计算开始");

		Map<String, LingjCangkYuxx> leijljqzl = new HashMap<String, LingjCangkYuxx>();
		
		//用户中心 - xss-20160422
		ls = this.maoxqhzjcTmpService.selectDingd(dingdlx ,loginUser);// 得到maoxqhzjcTmp表数据
		
		// 供应商最大偏移量
		int maxPx = 0;
		// 零件列表
		List<String> lingjlst = new ArrayList<String>();
		if (!ls.isEmpty()) {

			// 将list 按库存、零件主键排列
			Map<String, List<MaoxqhzjcTmp>> tempmap = new HashMap<String, List<MaoxqhzjcTmp>>();
			for (MaoxqhzjcTmp maotemp : ls) {
				String key = createCanklingjkey(maotemp);
				List<MaoxqhzjcTmp> maxmaplst = tempmap.get(key);
				if (maxmaplst == null) {
					maxmaplst = new ArrayList<MaoxqhzjcTmp>();
				}
				maxmaplst.add(maotemp);
				tempmap.put(key, maxmaplst);
				if (!lingjlst.contains(maotemp.getLingjbh())) {// 零件排重
					lingjlst.add(maotemp.getLingjbh());
				}
			}
			ls.clear();

			for (List<MaoxqhzjcTmp> vs : tempmap.values()) {
				ls.addAll(vs);
			}
			tempmap = null;
			// 重新按零件计算供应商分配比例
			Map<String, List<LingjGysfexz>> congxjsgyfr = chongxjsgysfe(
					userString, lingjlst);

			// 如果得到的数据不为空
			// 查询全部库存转换记录 ,为减少查询次数，一个用户中心查询一次

			// 根据用户中心汇总全部库存置换集合
			Map<String, Map<String, VjKuczh>> kuczhUsercenterMap = new HashMap<String, Map<String, VjKuczh>>();
		
		//xss-20161103-0012933 	
		int x = 0 ;	
		
		BigDecimal kuc_new = BigDecimal.ZERO;//xss-0012989
		for(int m = 0 ; m<= 15 ; m++){  
			
			for (int i = 0; i < ls.size(); i++) {
				MaoxqhzjcTmp maoxqhzjc = (MaoxqhzjcTmp) ls.get(i);// 取得毛需求汇总_参考系中一条数据

				int pianysj = 0; //封闭期
				int jidgs = 0;   //用于判断既定个数
				
				// 初始化订单零件
				Dingdlj dingdlj = new Dingdlj();
				BigDecimal ddljzl = BigDecimal.ZERO;
				
				  if( m >0 ){
					  jidgs  = maoxqhzjc.getJidgs();
					  pianysj = maoxqhzjc.getPianysj();
					  x  = maoxqhzjc.getX();
					  
					  //xss_0012989
					  if( maoxqhzjc.getKuc_new()!=null ){
						  kuc_new = maoxqhzjc.getKuc_new(); 
					  } 					   
				  }else{
					  x = 0;
					  
					  	//用户中心 - xss-20160422 
						maxPx = this.getGongysMaxPx( maoxqhzjc.getLingjbh() , maoxqhzjc.getUsercenter() );
						
						CommonFun.logger.info("最大封闭期=" + maxPx);
						// 按用户中心得到该用户中心的全部 库存置换集合
						kuczhUsercenterMap = findKuczh(maoxqhzjc.getUsercenter(),
								kuczhUsercenterMap);
						
						String dingdnr = maoxqhzjc.getDingdnr();
						CommonFun.logger.debug("国产件VJ模式订单计算dingdnr=" + dingdnr);
						for (int j = 0; j < dingdnr.length(); j++) {
							String neir = maoxqhzjc.getDingdnr().substring(j, j + 1);
							if (neir.equalsIgnoreCase("9")) {
								jidgs = jidgs + 1;
							}
						}
						CommonFun.logger.debug("国产件VJ模式订单计算jidgs=" + jidgs);

						// 封闭期计算						
						pianysj = this.getPianysj(maoxqhzjc, maxPx);
						CommonFun.logger.info("实际封闭期：" + pianysj);
						if (pianysj == -1) {
							continue;
						}
						// end 封闭期的计算
						// 当maxPx>pianysj时，需要补算既定天数到封闭期日期
						// 偏移差异
						int pianycy = 0;
						if (maxPx > pianysj) {
							pianycy = maxPx - pianysj;
						}
						jidgs = jidgs + pianycy;
						CommonFun.logger.info("偏移差异：" + pianycy);
						CommonFun.logger.info("既定个数：" + jidgs);
						
						maoxqhzjc.setPianysj(pianysj);
						maoxqhzjc.setJidgs(jidgs);
				  }
								
				CommonFun.logger.debug("国产件VJ模式订单计算pianysj=" + pianysj);
				if (pianysj <= Const.ZJCHANGDU) {
				  //xss20161103 - 0012933
				  if( (pianysj + x) == m && x < jidgs ) {
					//for (int x = 0; x < jidgs; x++) {
						String method = Const.GETJ + (pianysj + x);// 拼接getPi方法
						CommonFun.logger.debug("国产件VJ模式订单计算既定计算method="
								+ method);
						String methodJd = Const.GETP + x + "sl";
						CommonFun.logger.debug("国产件VJ模式订单计算既定计算methodJd="
								+ methodJd);
						Class clsdd = dingdlj.getClass();
						Method methdjd = null;
						String pxriq = "";
						BigDecimal jiding = BigDecimal.ZERO;
						if (pianysj + x > Const.ZJCHANGDU) {
							jiding = BigDecimal.ZERO;
							CommonFun.logger
									.debug("国产件VJ模式订单计算既定计算在（pianysj + x > Const.ZJCHANGDU）时jiding="
											+ jiding);
							resMap = this.jiDingCalculate(jiding, maoxqhzjc,
									flag, x, pianysj, dingdlj, zhizlx, i,
									caozz, ddljzl,
									sfayujs(kuczhUsercenterMap, maoxqhzjc),
									orderNumberMap, loginUser, leijljqzl,
									congxjsgyfr, jkCode, kuc_new ,x);// 调用既定数量计算方法
							ddljzl = resMap.get("ddljzl");
							kuc_new = resMap.get("yingyu"); //最新库存 xss-20160827
							maoxqhzjc.setKuc_new(kuc_new);//xss_20161125
							
							CommonFun.logger
									.debug("国产件VJ模式订单计算既定计算在（pianysj + x > Const.ZJCHANGDU）时yingyu="
											+ resMap.get("yingyu"));
						} else {
							try {
								methdjd = clsdd.getMethod(methodJd,
										new Class[] {});
							} catch (SecurityException e) {
								CommonFun.logger.error(e);
								throw new RuntimeException(
										"IlOrderService.doCalculate,SecurityException");
							} catch (NoSuchMethodException e) {
								CommonFun.logger.error(e);
								throw new RuntimeException(
										"IlOrderService.doCalculate,NoSuchMethodException");
							}
							jiding = this.backValue(method, maoxqhzjc, pianysj
									+ x, dingdlx);
							CommonFun.logger
									.debug("国产件VJ模式订单计算既定计算不在（pianysj + x > Const.ZJCHANGDU）时jiding="
											+ jiding);
							resMap = this.jiDingCalculate(jiding, maoxqhzjc,
									flag, x, pianysj, dingdlj, zhizlx, i,
									caozz, ddljzl,
									sfayujs(kuczhUsercenterMap, maoxqhzjc),
									orderNumberMap, loginUser, leijljqzl,
									congxjsgyfr, jkCode, kuc_new, x);// 调用既定数量计算方法
							ddljzl = resMap.get("ddljzl");
							kuc_new = resMap.get("yingyu"); //最新库存 xss-20160827
							maoxqhzjc.setKuc_new(kuc_new);//xss_20161125
							
							CommonFun.logger
									.debug("国产件VJ模式订单计算既定计算不在（pianysj + x > Const.ZJCHANGDU）时yingyu="
											+ resMap.get("yingyu"));
							clsdd = dingdlj.getClass();
							if (x == 0) {
								methodJd = "getP" + x + "fyzqxh";
								CommonFun.logger
										.debug("国产件VJ模式订单计算既定计算在（x == 0）时methodJd="
												+ methodJd);
							} else {
								methodJd = "getP" + x + "rq";
								CommonFun.logger
										.debug("国产件VJ模式订单计算既定计算不在（x == 0）时methodJd="
												+ methodJd);
							}
							try {
								methdjd = clsdd.getMethod(methodJd,
										new Class[] {});
							} catch (SecurityException e1) {
								CommonFun.logger.error(e1);
								throw new RuntimeException(
										"IlOrderService.doCalculate,SecurityException");
							} catch (NoSuchMethodException e1) {
								CommonFun.logger.error(e1);
								throw new RuntimeException(
										"IlOrderService.doCalculate,NoSuchMethodException");
							}
							Object rqObj;
							try {
								rqObj = methdjd.invoke(dingdlj, null);
							} catch (IllegalArgumentException e) {
								CommonFun.logger.error(e);
								throw new RuntimeException(
										"IlOrderService.doCalculate,IllegalArgumentException");
							} catch (IllegalAccessException e) {
								CommonFun.logger.error(e);
								throw new RuntimeException(
										"IlOrderService.doCalculate,IllegalAccessException");
							} catch (InvocationTargetException e) {
								CommonFun.logger.error(e);
								throw new RuntimeException(
										"IlOrderService.doCalculate,InvocationTargetException");
							}
							if (null == rqObj) {
								pxriq = null;
								CommonFun.logger.debug("国产件VJ模式订单计算既定计算pxriq="
										+ pxriq);
							} else {
								try {
									pxriq = methdjd.invoke(dingdlj, null)
											.toString();
								} catch (IllegalArgumentException e) {
									CommonFun.logger.error(e);
									throw new RuntimeException(
											"IlOrderService.doCalculate,IllegalArgumentException");
								} catch (IllegalAccessException e) {
									CommonFun.logger.error(e);
									throw new RuntimeException(
											"IlOrderService.doCalculate,IllegalAccessException");
								} catch (InvocationTargetException e) {
									CommonFun.logger.error(e);
									throw new RuntimeException(
											"IlOrderService.doCalculate,InvocationTargetException");
								}
								CommonFun.logger.debug("国产件VJ模式订单计算既定计算pxriq="
										+ pxriq);
							}
						}
						methodJd = Const.GETP + x + "sl";
						CommonFun.logger.debug("国产件VJ模式订单计算既定计算methodJd="
								+ methodJd);
						BigDecimal jissl = this.backValue(methodJd, dingdlj, x,
								dingdlx);
						CommonFun.logger.debug("国产件VJ模式订单计算既定计算jissl=" + jissl);
						this.insertDingdMX(dingdlj, maoxqhzjc, zhizlx, x,
								dingdlx, dingdhMap, jissl, pxriq, caozz,
								Const.SHIFOUSHIJIDING, saveMingxMap);
						x++;
						maoxqhzjc.setX(x);
					}
//					BigDecimal yingyu = resMap.get("yingyu");
//					for (int y = 0; y < Const.DINGDNRCHANGDU - jidgs; y++) {
//						String method = Const.GETJ + (pianysj + jidgs + y);// 拼接getPi方法
//						CommonFun.logger.debug("国产件VJ模式订单计算预告计算method="
//								+ method);
//						BigDecimal yugao = BigDecimal.ZERO;
//						Class clsdd = dingdlj.getClass();
//						String methodJd = Const.GETP + (jidgs + y) + "sl";
//						CommonFun.logger.debug("国产件VJ模式订单计算预告计算methodJd="
//								+ methodJd);
//						Method methdjd;
//						try {
//							methdjd = clsdd.getMethod(methodJd, new Class[] {});
//						} catch (SecurityException e) {
//							CommonFun.logger.error(e);
//							throw new RuntimeException(
//									"IlOrderService.doCalculate,SecurityException");
//						} catch (NoSuchMethodException e) {
//							CommonFun.logger.error(e);
//							throw new RuntimeException(
//									"IlOrderService.doCalculate,NoSuchMethodException");
//						}
//
//						BigDecimal jissl = BigDecimal.ZERO;
//						String pxriq = "";
//						if (pianysj + jidgs + y > Const.ZJCHANGDU) {
//							yugao = BigDecimal.ZERO;
//							CommonFun.logger.debug("国产件VJ模式订单计算预告计算yugao="
//									+ yugao);
//						} else {
//							yugao = this.backValue(method, maoxqhzjc, pianysj
//									+ jidgs + y, dingdlx);// 执行得到的方法，取得既定周期的数量
//							CommonFun.logger.debug("国产件VJ模式订单计算预告计算yugao="
//									+ yugao);
//						}
//						if (maoxqhzjc.getYugsfqz().equalsIgnoreCase(
//								Const.BUYiLAI)) {
//							CommonFun.logger.debug("国产件VJ模式订单计算预告计算yingyu="
//									+ BigDecimal.ZERO);
//						}
//						/*
//						 * BigDecimal yingyu = this.yuGaoCalculate(yugao,
//						 * maoxqhzjc, findyinyBycanklingjh(maoxqhzjc,
//						 * leijljqzl), jidgs + y, dingdlj);// 调用预告数量计算方法
//						 */
//						yingyu = this.yuGaoCalculate(yugao, maoxqhzjc, yingyu,
//								jidgs + y, dingdlj);
//
//						CommonFun.logger.debug("国产件VJ模式订单计算预告计算yingyu="
//								+ yingyu);
//						if (jidgs + y == 0) {
//							methodJd = "getP" + (jidgs + y) + "fyzqxh";
//							CommonFun.logger
//									.debug("国产件VJ模式订单计算预告计算在（jidgs + y == 0）时methodJd="
//											+ methodJd);
//						} else {
//							methodJd = "getP" + (jidgs + y) + "rq";
//							CommonFun.logger
//									.debug("国产件VJ模式订单计算预告计算不在（jidgs + y == 0）时methodJd="
//											+ methodJd);
//						}
//						try {
//							methdjd = clsdd.getMethod(methodJd, new Class[] {});
//						} catch (SecurityException e) {
//							CommonFun.logger.error(e);
//							throw new RuntimeException(
//									"IlOrderService.doCalculate,SecurityException");
//						} catch (NoSuchMethodException e) {
//							CommonFun.logger.error(e);
//							throw new RuntimeException(
//									"IlOrderService.doCalculate,NoSuchMethodException");
//						}
//						Object riqObj;
//						try {
//							riqObj = methdjd.invoke(dingdlj, null);
//						} catch (IllegalArgumentException e) {
//							CommonFun.logger.error(e);
//							throw new RuntimeException(
//									"IlOrderService.doCalculate,IllegalArgumentException");
//						} catch (IllegalAccessException e) {
//							CommonFun.logger.error(e);
//							throw new RuntimeException(
//									"IlOrderService.doCalculate,IllegalAccessException");
//						} catch (InvocationTargetException e) {
//							CommonFun.logger.error(e);
//							throw new RuntimeException(
//									"IlOrderService.doCalculate,InvocationTargetException");
//						}
//						if (null == riqObj) {
//							pxriq = null;
//							CommonFun.logger
//									.debug("国产件VJ模式订单计算预告计算在（null == riqObj）时pxriq="
//											+ pxriq);
//						} else {
//							try {
//								pxriq = methdjd.invoke(dingdlj, null)
//										.toString();
//							} catch (IllegalArgumentException e) {
//								CommonFun.logger.error(e);
//								throw new RuntimeException(
//										"IlOrderService.doCalculate,IllegalArgumentException");
//							} catch (IllegalAccessException e) {
//								CommonFun.logger.error(e);
//								throw new RuntimeException(
//										"IlOrderService.doCalculate,IllegalAccessException");
//							} catch (InvocationTargetException e) {
//								CommonFun.logger.error(e);
//								throw new RuntimeException(
//										"IlOrderService.doCalculate,InvocationTargetException");
//							}
//							CommonFun.logger
//									.debug("国产件VJ模式订单计算预告计算不在（null == riqObj）时pxriq="
//											+ pxriq);
//						}
//
//						methodJd = Const.GETP + (jidgs + y) + "sl";
//						CommonFun.logger.debug("国产件VJ模式订单计算预告计算methodJd="
//								+ methodJd);
//						jissl = this.backValue(methodJd, dingdlj, jidgs + y,
//								dingdlx);
//						CommonFun.logger.debug("国产件VJ模式订单计算预告计算jissl=" + jissl);
//						if (pianysj + jidgs + y <= Const.ZJCHANGDU) {
//							this.insertDingdMX(dingdlj, maoxqhzjc, zhizlx,
//									jidgs + y, dingdlx, dingdhMap, jissl,
//									pxriq, caozz, Const.SHIFOUSHIYUGAO,
//									saveMingxMap);
//						}
//					}
					// vjDingdljService.doInsert(dingdlj);
					List<Dingdlj> flagLingj = saveLingjMap.get(dingdlj
							.getDingdh());
					if (null == flagLingj) {
						flagLingj = new ArrayList<Dingdlj>();
					}
					flagLingj.add(dingdlj);
					saveLingjMap.put(dingdlj.getDingdh(), flagLingj);
					this.insertdingd(dingdlj, maoxqhzjc, dingdlx, jssj, banc,
							jiihyz, caozz);
				} else {
					String[] paramStr = new String[] {
							maoxqhzjc.getUsercenter(), maoxqhzjc.getGongysdm(),
							maoxqhzjc.getLingjbh() };
					this.yicbjservice.insertError(Const.YICHANG_LX3,
							Const.YICHANG_LX3_str9, maoxqhzjc.getJihyz(),
							paramStr, maoxqhzjc.getUsercenter(),
							maoxqhzjc.getLingjbh(), loginUser,
							Const.JISMK_IL_CD);
				}
			}
			
		  }
		}

		Map<String, String> map = new HashMap<String, String>();
		map.put("xuqbc", banc);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
				"hlorder.updateMaoxq", map);
		Set<String> flagDingdhSet = saveLingjMap.keySet();
		for (String flagDingdh : flagDingdhSet) {
			executeBatch(saveLingjMap.get(flagDingdh), "hlorder.insertDingdlj");
			executeBatch(saveMingxMap.get(flagDingdh), "hlorder.insertDingdmx");
			// 零件流三期 VJ日订单均分 0011486
			// 判断当前的的订单类型 ，如果当前订单时VJ时则将订单明细数据同时插入到VJ均分订单明细表中
			if (dingdlx.equalsIgnoreCase(Const.VJ)) {
				vJdingdjf(flagDingdh, userString);
			}
		}
		
	

		try {
			// 如果是日订单直接生效
			//xss_0013089
			if (dingdlx.equalsIgnoreCase(Const.VJ)) {
				this.updateDingd(dingdlx,null, Const.DINGD_STATUS_YSX, caozz, "'"
						+ Const.DINGDLX_ILORDER + "','" + Const.DINGDLX_JL
						+ "'");
			}
			// 其他订单需审核
			else {
				this.updateDingd(dingdlx,null, Const.DINGD_STATUS_ZZZ, caozz, "'"
						+ Const.DINGDLX_ILORDER + "','" + Const.DINGDLX_JL
						+ "'");
			}
		} catch (Exception e) {
			CommonFun.logger.error(e.toString());
			log.error("Il订单计算出错");
			log.error(e.toString());
		}
		if (count != 0) {
			message = "计算完成,但是校验时某些零件参数有误,请查看异常报警信息!";
		} else {
			message = "计算完成";
		}
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单周期订单计算",
				"计算结束");
		CommonFun.logger.info("Il订单计算正常结束");
		log.info("Il订单计算正常结束");
		return message;
	}

	/**
	 * 更新订单状态
	 * 
	 * @param dingdjssj
	 *            订单计算时间
	 * @param state
	 *            订单状态
	 * @param user
	 *            操作人
	 */
	private void updateDingd(String chullx,String dingdjssj, String state, String user,
			String dingdlx) {
		//xss_0013089
		Map<String, String> param = new HashMap<String, String>();
		param.put("state", state);
		param.put("dingdjssj", dingdjssj);
		param.put("creator", user);
		param.put("dingdlx", dingdlx);
		
		param.put("chullx", chullx);
		// 更新订单状态
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
				"hlorder.updateDingdJsz", param);
	}

	/*
	 * 按用户中心、零件号、仓库获取盈余
	 */
	private BigDecimal findyinyBycanklingjh(MaoxqhzjcTmp maoxqhzjc,
			Map<String, LingjCangkYuxx> leijljqzl, int x) {		
		//String key = createCanklingjkey(maoxqhzjc);
		//xss-20161102
		String key = createCanklingjCurrkey( maoxqhzjc , x ); 
		
		LingjCangkYuxx ljckyxx = leijljqzl.get(key);// 是否已计算过
		if (ljckyxx == null) { // 减去盈余
			VjXianbkc xianbkc = queryXianbkc(maoxqhzjc.getUsercenter(),
					maoxqhzjc.getLingjbh(), null, maoxqhzjc.getCangkdm(), "1",
					maoxqhzjc.getDanw());
			if (xianbkc.getYingy() == null) {
				xianbkc.setYingy(BigDecimal.ZERO);// 置零
			}
			return xianbkc.getYingy();
		} else {
			return ljckyxx.computerYingy();
		}
	}

	/*
	 * 创建仓库、零件盈余保持主键
	 */
	private String createCanklingjkey(MaoxqhzjcTmp maoxqhzjc) {
		StringBuffer str = new StringBuffer();
		str.append(maoxqhzjc.getUsercenter());
		str.append("_");
		str.append(maoxqhzjc.getCangkdm());
		str.append("_");
		str.append(maoxqhzjc.getLingjbh());
		str.append("_");
		str.append(maoxqhzjc.getJ0rq());
		str.append("_");
		/*
		 * str.append(maoxqhzjc.getJ1riq()); str.append("_");
		 * str.append(maoxqhzjc.getJ2riq()); str.append("_");
		 * str.append(maoxqhzjc.getJ3riq()); str.append("_");
		 * str.append(maoxqhzjc.getJ4riq()); str.append("_");
		 * str.append(maoxqhzjc.getJ5riq()); str.append("_");
		 * str.append(maoxqhzjc.getJ6riq()); str.append("_");
		 * str.append(maoxqhzjc.getJ7riq()); str.append("_");
		 * str.append(maoxqhzjc.getJ8riq()); str.append("_");
		 * str.append(maoxqhzjc.getJ9riq()); str.append("_");
		 * str.append(maoxqhzjc.getJ10riq()); str.append("_");
		 * str.append(maoxqhzjc.getJ11riq()); str.append("_");
		 * str.append(maoxqhzjc.getJ12riq()); str.append("_");
		 * str.append(maoxqhzjc.getJ13riq()); str.append("_");
		 * str.append(maoxqhzjc.getJ14riq()); str.append("_");
		 */
		str.append(maoxqhzjc.getJ15riq());

		return str.toString();
	}
	
	/*
	 * xss-20161102
	 * 创建仓库、零件盈余保持 当前 J + x 主键
	 */
	private String createCanklingjCurrkey(MaoxqhzjcTmp maoxqhzjc,int x) {
		StringBuffer str = new StringBuffer();
		
		int px = x;

		String method = "";// 拼接getPi方法

		if(px > 0){
			method = "getJ" + px + "riq";
			CommonFun.logger
					.debug("国产件订单计算VJ模式既定计算jiDingCalculate方法不在（）时method="
							+ method);
		}else{
			method = "getJ" + px + "rq";
			CommonFun.logger
					.debug("国产件订单计算VJ模式既定计算jiDingCalculate方法不在（）时method="
							+ method);
		}
		

		String pxriq = null;
		Class cls = maoxqhzjc.getClass();// 得到Maoxqhzpc类
		Method meth;
		try {
			meth = cls.getMethod(method, new Class[] {});
		} catch (SecurityException e) {
			CommonFun.logger.error(e);
			throw new RuntimeException(
					"IlOrderService.jiDingCalculate,SecurityException");
		} catch (NoSuchMethodException e) {
			CommonFun.logger.error(e);
			throw new RuntimeException(
					"IlOrderService.jiDingCalculate,NoSuchMethodException");
		}// 得到Maoxqhzpc类的method拼接的方法
		Object obj;
		try {
			obj = meth.invoke(maoxqhzjc, null);
		} catch (IllegalArgumentException e) {
			CommonFun.logger.error(e);
			throw new RuntimeException(
					"IlOrderService.jiDingCalculate,IllegalArgumentException");
		} catch (IllegalAccessException e) {
			CommonFun.logger.error(e);
			throw new RuntimeException(
					"IlOrderService.jiDingCalculate,IllegalAccessException");
		} catch (InvocationTargetException e) {
			CommonFun.logger.error(e);
			throw new RuntimeException(
					"IlOrderService.jiDingCalculate,InvocationTargetException");
		}
			try {
				pxriq = meth.invoke(maoxqhzjc, null).toString();
			} catch (IllegalArgumentException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException(
						"IlOrderService.jiDingCalculate,IllegalArgumentException");
			} catch (IllegalAccessException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException(
						"IlOrderService.jiDingCalculate,IllegalAccessException");
			} catch (InvocationTargetException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException(
						"IlOrderService.jiDingCalculate,InvocationTargetException");
			} 
			CommonFun.logger.debug("国产件订单计算VJ模式既定计算jiDingCalculate方法pxriq="
					+ pxriq);
	 
		str.append(maoxqhzjc.getUsercenter());
		str.append("_");
		str.append(maoxqhzjc.getCangkdm());
		str.append("_");
		str.append(maoxqhzjc.getLingjbh());
		str.append("_");
		str.append(pxriq);
		
		return str.toString();
	}

	/**
	 * <p>
	 * Title:Vj国产件既定要货量计算
	 * </p>
	 * <p>
	 * Description:按照选取的毛需求计算既定
	 * </p>
	 * 参数为既定数量，以及毛需求类，以及多既定FLAG
	 * <p>
	 * Copyright: Copyright (c) 2011
	 * </p>
	 * <p>
	 * Company: 软通动力
	 * </p>
	 * 
	 * @param jiding
	 * @param maoxqhzjc
	 *            毛需求汇总参考系记录
	 * @param flag
	 *            用于判断是否有多个既定
	 * @param yingyu
	 *            盈余
	 * @param jiszq
	 *            既定个数循环
	 * @param pianysj
	 *            偏移时间
	 * @param dingdlj
	 *            订单零件
	 * @param zhizlx
	 *            制造路线 "97W"
	 * @param zhouqxh
	 *            周期序号
	 * @param caozz
	 *            操作者
	 * @param ddljzl
	 * @param sfayyjs
	 *            是否按盈余计算
	 * @author 陈骏
	 * @version v1.0
	 * @throws NoSuchMethodException
	 * 
	 * @throws SecurityException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws Exception
	 * @date 2011-12-1
	 **/
	public Map<String, BigDecimal> jiDingCalculate(BigDecimal jiding,
			MaoxqhzjcTmp maoxqhzjc, boolean flag, int jiszq, int pianysj,
			Dingdlj dingdlj, String zhizlx, int zhouqxh, String caozz,
			BigDecimal ddljzl, boolean sfayyjs,
			Map<String, String> orderNumberMap, LoginUser loginUse,
			Map<String, LingjCangkYuxx> leijljqzl,
			Map<String, List<LingjGysfexz>> congxjsgyfr, String jkCode, BigDecimal kuc_new , int x) {
		Map<String, BigDecimal> resMap = new HashMap<String, BigDecimal>();
		BigDecimal kuc = BigDecimal.ZERO;// 库存
		BigDecimal anqkc = BigDecimal.ZERO;// 安全库存
		BigDecimal daijf = BigDecimal.ZERO;// 待交付
		BigDecimal daixh = BigDecimal.ZERO;// 待消耗
		BigDecimal yaohl = BigDecimal.ZERO;// 要货量
		BigDecimal quzhyhl = BigDecimal.ZERO;// 要货量取整后的值
		BigDecimal yingyu = BigDecimal.ZERO;// xss-206160914
		
		Map<String, Object> map = new HashMap<String, Object>();
		if (!flag) {
			if (sfayyjs) {// 按盈余
				// 查询盈余 需要时实查询，应为在计算中会修改盈余。
				// 获取线边库存
				//xss-20161102-0012933
				//String key = createCanklingjkey(maoxqhzjc);
				String key = createCanklingjCurrkey(maoxqhzjc,x);
				LingjCangkYuxx ljckyxx = leijljqzl.get(key);// 是否已计算过
				 if (ljckyxx == null) { // 减去盈余
						VjXianbkc xianbkc = queryXianbkc(maoxqhzjc.getUsercenter(),
								maoxqhzjc.getLingjbh(), null, maoxqhzjc.getCangkdm(),
								"1", maoxqhzjc.getDanw());
						if (xianbkc.getYingy() == null) {
							xianbkc.setYingy(BigDecimal.ZERO);// 置零
						}
						
				
				// 异常消耗
				BigDecimal yicxh = BigDecimal.ZERO;
				if (jiszq == 0)
				{
					// 盈余计算时要减去安全库存减去异常消耗，保存时要增加安全库存 。目的是为保障供应时有一定的安全库存
					Date date = new Date();
					DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String time = format.format(date);
					Map<String, String> param = new HashMap<String, String>();
					param.put("lingjbh", maoxqhzjc.getLingjbh());
					param.put("usercenter", maoxqhzjc.getUsercenter());
					param.put("gonghlx", maoxqhzjc.getWaibghms());
					param.put("mudd", maoxqhzjc.getCangkdm());
					param.put("jilrq", time);
					yicxh = this.getYcxh(param);
					if (null == yicxh) {
						yicxh = BigDecimal.ZERO;
					}
				}
				BigDecimal ziyl = xianbkc.getYingy()
						.subtract(maoxqhzjc.getAnqkc()).subtract(yicxh);
				yaohl = jiding.subtract(ziyl);
				// 判断要货量是否大于盈余
				if (yaohl.compareTo(BigDecimal.ZERO) < 0) {// 盈余大于要货量
					yaohl = BigDecimal.ZERO;// 盈余能满足需求
				}
				ljckyxx = new LingjCangkYuxx();
				ljckyxx.setCangkdm(maoxqhzjc.getCangkdm());
				ljckyxx.setLingjbh(maoxqhzjc.getLingjbh());
				ljckyxx.setUsercenter(maoxqhzjc.getUsercenter());
				ljckyxx.setJingd(jiding);
				ljckyxx.setYiny(xianbkc.getYingy());
				ljckyxx.setAnqkc(maoxqhzjc.getAnqkc());
				ljckyxx.setYaohl(yaohl);
				leijljqzl.put(key, ljckyxx);
				} else {
				yaohl = ljckyxx.getYaohl();
				}
			} else {// 按库存
				CommonFun.logger.debug("国产件订单计算VJ模式既定计算jiDingCalculate方法是否依赖库存"
						+ maoxqhzjc.getShifylkc());
					//xss-20160827				
				if (maoxqhzjc.getShifylkc().equalsIgnoreCase("1")) {// 查看订单计算是否依赖库存
						if (x == 0){
							kuc = maoxqhzjc.getKuc();
						}else{
							kuc = kuc_new;
						}
				}			
				

				CommonFun.logger
						.debug("国产件订单计算VJ模式既定计算jiDingCalculate方法是否依赖安全库存"
								+ maoxqhzjc.getShifylaqkc());
				if (maoxqhzjc.getShifylaqkc().equalsIgnoreCase("1")) {// 查看订单计算是否依赖安全库存
					anqkc = maoxqhzjc.getAnqkc();
				}
				CommonFun.logger
						.debug("国产件订单计算VJ模式既定计算jiDingCalculate方法是否依赖待交付"
								+ maoxqhzjc.getShifyldjf());
				if (maoxqhzjc.getShifyldjf().equalsIgnoreCase("1")) {// 查看订单计算是否依赖待交付
					daijf = maoxqhzjc.getDingdlj()
							.subtract(maoxqhzjc.getJiaoflj())
							.subtract(maoxqhzjc.getDingdzzlj());
					CommonFun.logger.info("待交付：" + daijf);
				}
				CommonFun.logger
						.debug("国产件订单计算VJ模式既定计算jiDingCalculate方法是否依赖待消耗"
								+ maoxqhzjc.getShifyldxh());
				if (maoxqhzjc.getShifyldxh().equalsIgnoreCase("1")) {// 查看订单计算是否依赖待消耗
					//动态获取待消耗 add by zbb
					//daixh = this.getDaixh(maoxqhzjc, pianysj+jiszq);
					//CommonFun.logger.debug("动态待消耗="	+ daixh);
					daixh = maoxqhzjc.getDaixh();
				}
				BigDecimal tidjKuc = this.tidjKuc(maoxqhzjc.getUsercenter(),
						maoxqhzjc.getLingjbh(), maoxqhzjc.getZiyhqrq(),
						maoxqhzjc.getCangkdm());// 替代件数量
				CommonFun.logger
						.debug("国产件订单计算VJ模式既定计算jiDingCalculate方法tidjKuc="
								+ tidjKuc);
				// DecimalFormat data = new DecimalFormat("00");
				
				BigDecimal xuqiu  = BigDecimal.ZERO;
				if (x == 0){
					 xuqiu = jiding.add(anqkc);// 计算需求
				}else{
					 xuqiu = jiding;// 计算需求
				}
				
				CommonFun.logger
						.debug("国产件订单计算VJ模式既定计算jiDingCalculate方法jiding="
								+ jiding);
				CommonFun.logger.debug("国产件订单计算VJ模式既定计算jiDingCalculate方法anqkc="
						+ anqkc);
				CommonFun.logger.debug("国产件订单计算VJ模式既定计算jiDingCalculate方法xuqiu="
						+ xuqiu);
				
				BigDecimal ziyuan = BigDecimal.ZERO;
				if (x == 0){
					 ziyuan = kuc.add(daijf).subtract(daixh)// 计算资源
							.add(maoxqhzjc.getXittzz()).add(tidjKuc);	
				}else{
					 ziyuan = kuc;
				}
				
				CommonFun.logger.debug("国产件订单计算VJ模式既定计算jiDingCalculate方法kuc="
						+ kuc);
				CommonFun.logger.debug("国产件订单计算VJ模式既定计算jiDingCalculate方法daijf="
						+ daijf);
				CommonFun.logger.debug("国产件订单计算VJ模式既定计算jiDingCalculate方法daixh="
						+ daixh);
				CommonFun.logger
						.debug("国产件订单计算VJ模式既定计算jiDingCalculate方法Xittzz="
								+ maoxqhzjc.getXittzz());
				CommonFun.logger
						.debug("国产件订单计算VJ模式既定计算jiDingCalculate方法tidjKuc="
								+ tidjKuc);
				CommonFun.logger
						.debug("国产件订单计算VJ模式既定计算jiDingCalculate方法ziyuan="
								+ ziyuan);
				
				if (x == 0){ 
					 //0012914-如果计算调整值为负数，则计算调整值变为需求来计算
					 if(maoxqhzjc.getTiaozjsz().compareTo(BigDecimal.ZERO) < 0  ){
						yaohl = xuqiu.subtract(ziyuan).subtract( maoxqhzjc.getTiaozjsz() );
					 }else{ 
						 yaohl = xuqiu.subtract(ziyuan).add( // 计算要货量
								maoxqhzjc.getTiaozjsz());	
					 }  
				}else{
					yaohl = xuqiu.subtract(ziyuan); 
				}
					
				yingyu = BigDecimal.ZERO.subtract(yaohl);//xss-20160914
			}
			// 按库存和盈余计算结束
			if (yaohl.compareTo(BigDecimal.ZERO) == -1) {
				yaohl = BigDecimal.ZERO;
			}
			

			CommonFun.logger
					.debug("国产件订单计算VJ模式既定计算jiDingCalculate方法Tiaozjsz()="
							+ maoxqhzjc.getTiaozjsz());
			dingdlj.setDingdh(this.getOrderNumber(Const.VJ,
					maoxqhzjc.getUsercenter(), maoxqhzjc.getGongysdm(),
					orderNumberMap, maoxqhzjc, loginUse));// 订单号

			dingdlj.setLingjbh(maoxqhzjc.getLingjbh());// 零件号
			dingdlj.setGongysdm(maoxqhzjc.getGongysdm());// 供应商代码
			dingdlj.setGongyslx(maoxqhzjc.getGongyslx());// 供应商类型
			dingdlj.setUsercenter(maoxqhzjc.getUsercenter());// 用户中心
			dingdlj.setDinghcj(maoxqhzjc.getDinghcj());// 订货车间
			dingdlj.setCangkdm(maoxqhzjc.getCangkdm());// 仓库代码

			int px = pianysj;

			// int px = maoxqhzjc.getBeihzq().add(maoxqhzjc.getFayzq())
			// .divide(BigDecimal.ONE, 0, BigDecimal.ROUND_UP).intValue();
			CommonFun.logger.debug("国产件订单计算VJ模式既定计算jiDingCalculate方法px="
					+ jiszq);
			String method = "";// 拼接getPi方法
			if (px == 0) {
				method = "getJ" + px + "rq";
				CommonFun.logger
						.debug("国产件订单计算VJ模式既定计算jiDingCalculate方法在（）时method="
								+ method);
			} else {
				method = "getJ" + px + "riq";
				CommonFun.logger
						.debug("国产件订单计算VJ模式既定计算jiDingCalculate方法不在（）时method="
								+ method);
			}

			String pxriq = null;
			Class cls = maoxqhzjc.getClass();// 得到Maoxqhzpc类
			Method meth;
			try {
				meth = cls.getMethod(method, new Class[] {});
			} catch (SecurityException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException(
						"IlOrderService.jiDingCalculate,SecurityException");
			} catch (NoSuchMethodException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException(
						"IlOrderService.jiDingCalculate,NoSuchMethodException");
			}// 得到Maoxqhzpc类的method拼接的方法
			Object obj;
			try {
				obj = meth.invoke(maoxqhzjc, null);
			} catch (IllegalArgumentException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException(
						"IlOrderService.jiDingCalculate,IllegalArgumentException");
			} catch (IllegalAccessException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException(
						"IlOrderService.jiDingCalculate,IllegalAccessException");
			} catch (InvocationTargetException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException(
						"IlOrderService.jiDingCalculate,InvocationTargetException");
			}

			if (null == obj) {
				dingdlj.setP0fyzqxh(null);
				CommonFun.logger
						.debug("国产件订单计算VJ模式既定计算jiDingCalculate方法P0fyzqxh="
								+ null);
			} else {
				try {
					pxriq = meth.invoke(maoxqhzjc, null).toString();
				} catch (IllegalArgumentException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException(
							"IlOrderService.jiDingCalculate,IllegalArgumentException");
				} catch (IllegalAccessException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException(
							"IlOrderService.jiDingCalculate,IllegalAccessException");
				} catch (InvocationTargetException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException(
							"IlOrderService.jiDingCalculate,InvocationTargetException");
				}// 执行得到的方法，取得预告周期的数量
				CommonFun.logger.debug("国产件订单计算VJ模式既定计算jiDingCalculate方法pxriq="
						+ pxriq);
				dingdlj.setP0fyzqxh(pxriq.substring(0, 10));// p0周期序号
				CommonFun.logger
						.debug("国产件订单计算VJ模式既定计算jiDingCalculate方法P0fyzqxh="
								+ pxriq.substring(0, 10));
			}
			dingdlj.setUabzlx(maoxqhzjc.getUabzlx());// ua包装类型
			dingdlj.setUabzuclx(maoxqhzjc.getUabzuclx());// ua中uc包装类型
			dingdlj.setUabzucrl(maoxqhzjc.getUabzucrl());// ua中uc包装容量
			dingdlj.setUabzucsl(maoxqhzjc.getUabzucsl());// ua中uc包装数量
			dingdlj.setDanw(maoxqhzjc.getDanw());// 单位
			dingdlj.setGongysfe(maoxqhzjc.getGongysfe());// 供应商份额
			dingdlj.setBeihzq(maoxqhzjc.getBeihzq());// 备货周期
			dingdlj.setFayzq(maoxqhzjc.getFayzq());// 发运周期
			dingdlj.setKuc(maoxqhzjc.getKuc());// 库存
			dingdlj.setAnqkc(maoxqhzjc.getAnqkc());// 安全库存
			dingdlj.setTiaozjsz(maoxqhzjc.getTiaozjsz());// 调整计算值
			dingdlj.setXittzz(maoxqhzjc.getXittzz());// 系统调整值
			dingdlj.setDingdlj(maoxqhzjc.getDingdlj());// 订单累计
			dingdlj.setJiaoflj(maoxqhzjc.getJiaoflj());// 交付累计
			dingdlj.setDaixh(maoxqhzjc.getDaixh());// 待消耗
			dingdlj.setZiyhqrq(maoxqhzjc.getZiyhqrq());// 资源获取日期
			dingdlj.setJihyz(maoxqhzjc.getJihyz());// 计划员组
			dingdlj.setLujdm(maoxqhzjc.getLujdm());// 路径代码
			dingdlj.setYugsfqz(maoxqhzjc.getYugsfqz());// 预告是否取整
			dingdlj.setShifylaqkc(maoxqhzjc.getShifylaqkc());// 是否依赖安全库存
			dingdlj.setShifyldjf(maoxqhzjc.getShifyldjf());// 是否依赖待交付
			dingdlj.setShifyldxh(maoxqhzjc.getShifyldxh());// 是否依赖待消耗
			dingdlj.setShifylkc(maoxqhzjc.getShifylkc());// 是否依赖库存
			dingdlj.setDingdnr(maoxqhzjc.getDingdnr());// 订单内容
			dingdlj.setZhidgys(maoxqhzjc.getZhidgys());// 指定供应商
			dingdlj.setDingdzzsj(CommonFun.getJavaTime().toString());// 订单制作时间
			dingdlj.setLingjsx(maoxqhzjc.getLingjsx());
			dingdlj.setZuixqdl(maoxqhzjc.getZuixqdl());
			dingdlj.setGonghms(maoxqhzjc.getWaibghms());
			dingdlj.setCreator(caozz);
			dingdlj.setCreate_time(CommonFun.getJavaTime());
			dingdlj.setEditor(caozz);
			dingdlj.setXiehzt(maoxqhzjc.getXiehztbh());
			dingdlj.setEdit_time(CommonFun.getJavaTime());
			dingdlj.setActive(Const.ACTIVE_1);
			//xss_0013097
			for (int z = 1; z <= 15; z++) {
				//xss-20160422
				if( (px + z) > 15 ){
					break;
				}
				
				// 拼接getPi方法
				method = "getJ" + (px + z) + "riq";
				CommonFun.logger
						.debug("国产件订单计算VJ模式既定计算jiDingCalculate方法method="
								+ method);
				cls = maoxqhzjc.getClass();// 得到Maoxqhzpc类
				try {
					meth = cls.getMethod(method, new Class[] {});
				} catch (SecurityException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException(
							"IlOrderService.jiDingCalculate,SecurityException");
				} catch (NoSuchMethodException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException(
							"IlOrderService.jiDingCalculate,NoSuchMethodException");
				}// 得到Maoxqhzpc类的method拼接的方法

				Object riqObj;
				try {
					riqObj = meth.invoke(maoxqhzjc, null);
					if (null == riqObj) {
						pxriq = null;
						CommonFun.logger
								.debug("国产件订单计算VJ模式既定计算jiDingCalculate方法pxriq="
										+ pxriq);
					} else {
						pxriq = meth.invoke(maoxqhzjc, null).toString();
						// 执行得到的方法，取得预告周期的数量
						CommonFun.logger
								.debug("国产件订单计算VJ模式既定计算jiDingCalculate方法pxriq="
										+ pxriq);
					}
				} catch (IllegalArgumentException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException(
							"IlOrderService.jiDingCalculate,IllegalArgumentException");
				} catch (IllegalAccessException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException(
							"IlOrderService.jiDingCalculate,IllegalAccessException");
				} catch (InvocationTargetException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException(
							"IlOrderService.jiDingCalculate,InvocationTargetException");
				}
				method = "setP" + (z) + "rq";// 拼接getPi方法
				cls = dingdlj.getClass();// 得到Maoxqhzpc类
				try {
					meth = cls.getMethod(method, String.class);
				} catch (SecurityException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException(
							"IlOrderService.jiDingCalculate,SecurityException");
				} catch (NoSuchMethodException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException(
							"IlOrderService.jiDingCalculate,NoSuchMethodException");
				}// 得到Maoxqhzpc类的method拼接的方法
				try {
					meth.invoke(dingdlj, pxriq);
				} catch (IllegalArgumentException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException(
							"IlOrderService.jiDingCalculate,IllegalArgumentException");
				} catch (IllegalAccessException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException(
							"IlOrderService.jiDingCalculate,IllegalAccessException");
				} catch (InvocationTargetException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException(
							"IlOrderService.jiDingCalculate,InvocationTargetException");
				}// 执行得到的方法，setPx
			}

			// 调用份额分配方法
			// 要货量
			if (yaohl.compareTo(BigDecimal.ZERO) < 0) {
				yaohl = BigDecimal.ZERO;
				map = vjFeneService.gongysFeneJs(dingdlj, yaohl, zhizlx,
						zhouqxh, congxjsgyfr);

				quzhyhl = BigDecimal.ZERO;
			} else {
				map = vjFeneService.gongysFeneJs(dingdlj, yaohl, zhizlx,
						zhouqxh, congxjsgyfr);
				quzhyhl = (BigDecimal) map.get("yaohl");
				CommonFun.logger
						.debug("国产件订单计算VJ模式既定计算jiDingCalculate方法quzhyhl="
								+ quzhyhl);
			}
			// 更新盈余
			if (sfayyjs) {// 按 盈余计算，更新盈余
				//xss-20161102
				addGongysqzslhyy(maoxqhzjc, quzhyhl, leijljqzl, jiding, jkCode ,x);
			}
			method = "setP" + jiszq + "sl";// 拼接getPi方法
			CommonFun.logger.debug("国产件订单计算VJ模式既定计算jiDingCalculate方法method="
					+ method);
			cls = dingdlj.getClass();// 得到Dingdlj类
			try {
				meth = cls.getMethod(method, BigDecimal.class);
			} catch (SecurityException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException(
						"IlOrderService.jiDingCalculate,SecurityException");
			} catch (NoSuchMethodException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException(
						"IlOrderService.jiDingCalculate,NoSuchMethodException");
			}// 得到Dingdlj类的method拼接的方法
			try {
				meth.invoke(dingdlj, quzhyhl);
			} catch (IllegalArgumentException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException(
						"IlOrderService.jiDingCalculate,IllegalArgumentException");
			} catch (IllegalAccessException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException(
						"IlOrderService.jiDingCalculate,IllegalAccessException");
			} catch (InvocationTargetException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException(
						"IlOrderService.jiDingCalculate,InvocationTargetException");
			}// 执行得到的方法，setPx
		}
//		if (flag) {
//			// 获取线边库存
//			String key = createCanklingjkey(maoxqhzjc);
//			LingjCangkYuxx ljckyxx = leijljqzl.get(key);// 是否已计算过
//			if (ljckyxx == null) { // 减去盈余
//				VjXianbkc xianbkc = queryXianbkc(maoxqhzjc.getUsercenter(),
//						maoxqhzjc.getLingjbh(), null, maoxqhzjc.getCangkdm(),
//						"1", maoxqhzjc.getDanw());
//				if (xianbkc.getYingy() == null) {
//					xianbkc.setYingy(BigDecimal.ZERO);// 置零
//				}
//
//				// 盈余计算时要减去安全库存，保存时要增加安全库存 。目的是为保障供应时有一定的安全库存
//				BigDecimal ziyl = xianbkc.getYingy().subtract(
//						maoxqhzjc.getAnqkc());
//				yaohl = jiding.subtract(ziyl);
//				// 判断要货量是否大于盈余
//				if (yaohl.compareTo(BigDecimal.ZERO) < 0) {// 盈余大于要货量
//					yaohl = BigDecimal.ZERO;// 盈余能满足需求
//				}
//				ljckyxx = new LingjCangkYuxx();
//				ljckyxx.setCangkdm(maoxqhzjc.getCangkdm());
//				ljckyxx.setLingjbh(maoxqhzjc.getLingjbh());
//				ljckyxx.setUsercenter(maoxqhzjc.getUsercenter());
//				ljckyxx.setJingd(jiding);
//				ljckyxx.setYiny(xianbkc.getYingy());
//				ljckyxx.setAnqkc(maoxqhzjc.getAnqkc());
//				ljckyxx.setYaohl(yaohl);
//				leijljqzl.put(key, ljckyxx);
//			} else {
//				yaohl = ljckyxx.getYaohl();
//			}
//
//			map = vjFeneService.gongysFeneJs(dingdlj, yaohl, zhizlx, zhouqxh,
//					congxjsgyfr);
//			quzhyhl = (BigDecimal) map.get("yaohl");
//			CommonFun.logger.debug("国产件订单计算VJ模式既定计算jiDingCalculate方法quzhyhl="
//					+ quzhyhl);
//
//			// 更新盈余
//			addGongysqzslhyy(maoxqhzjc, quzhyhl, leijljqzl, jiding, jkCode);
//
//			CommonFun.logger.debug("国产件订单计算VJ模式既定计算jiDingCalculate方法yingyu="
//					+ findyinyBycanklingjh(maoxqhzjc, leijljqzl));
//			String method = "setP" + jiszq + "sl";// 拼接getPi方法
//			CommonFun.logger.debug("国产件订单计算VJ模式既定计算jiDingCalculate方法method="
//					+ method);
//			Class cls = dingdlj.getClass();// 得到Dingdlj类
//			Method meth;
//			try {
//				meth = cls.getMethod(method, BigDecimal.class);
//			} catch (SecurityException e) {
//				CommonFun.logger.error(e);
//				throw new RuntimeException(
//						"IlOrderService.jiDingCalculate,SecurityException");
//			} catch (NoSuchMethodException e) {
//				CommonFun.logger.error(e);
//				throw new RuntimeException(
//						"IlOrderService.jiDingCalculate,NoSuchMethodException");
//			}// 得到Dingdlj类的method拼接的方法
//			try {
//				meth.invoke(dingdlj, quzhyhl);
//			} catch (IllegalArgumentException e) {
//				CommonFun.logger.error(e);
//				throw new RuntimeException(
//						"IlOrderService.jiDingCalculate,IllegalArgumentException");
//			} catch (IllegalAccessException e) {
//				CommonFun.logger.error(e);
//				throw new RuntimeException(
//						"IlOrderService.jiDingCalculate,IllegalAccessException");
//			} catch (InvocationTargetException e) {
//				CommonFun.logger.error(e);
//				throw new RuntimeException(
//						"IlOrderService.jiDingCalculate,InvocationTargetException");
//			}// 执行得到的方法，setPx
//		}
		ddljzl = ddljzl.add(quzhyhl);
//		flag = true;
		// 根据不同的算法取不同的盈余量
		if (!sfayyjs) {
			//xss20160913
			if( yaohl.compareTo(BigDecimal.ZERO) > 0){
				resMap.put("yingyu", new BigDecimal(map.get("yingyu").toString()));
			}else{
				resMap.put("yingyu", yingyu);
			}
		} else {
			//xss-20161102-0012933
			resMap.put("yingyu", findyinyBycanklingjh(maoxqhzjc, leijljqzl ,x) );
		}
		resMap.put("ddljzl", ddljzl);
		
		return resMap;
	}

	/*
	 * 增加供应商累计
	 * 
	 * @param maoxqhzjc
	 * 
	 * @param quzhyhl
	 * 
	 * @param leijljqzl
	 * 
	 * @param jingd
	 */
	private void addGongysqzslhyy(MaoxqhzjcTmp maoxqhzjc, BigDecimal quzhyhl,
			Map<String, LingjCangkYuxx> leijljqzl, BigDecimal jingd,
			String jkCode , int x) {
		//xss - 20161102
		//String key = createCanklingjkey(maoxqhzjc);
		
		String key = createCanklingjCurrkey(maoxqhzjc,x);
		LingjCangkYuxx ljckyxx = leijljqzl.get(key);// 是否已计算过
		LingjYuxx lingjyuxx = new LingjYuxx();
		lingjyuxx.setGongysdm(maoxqhzjc.getGongysdm());
		lingjyuxx.setQuzyhl(quzhyhl); // 取整要货量
		ljckyxx.addGongys(lingjyuxx);
		leijljqzl.put(key, ljckyxx);
		updateYingy(ljckyxx.getUsercenter(), ljckyxx.getLingjbh(),
				ljckyxx.getCangkdm(), ljckyxx.computerYingy(), jkCode);
	}

	/**
	 * <p>
	 * Title:pp国产件VJ模式预告要货量计算
	 * </p>
	 * <p>
	 * Description:按照选取的毛需求计算预告
	 * </p>
	 * 参数为预告数量，毛需求类汇总参考系类，盈余量，计算周期，订单零件类
	 * <p>
	 * Copyright: Copyright (c) 2011
	 * </p>
	 * <p>
	 * Company: 软通动力
	 * </p>
	 * 
	 * @author 陈骏
	 * @version v1.0
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws Exception
	 * @date 2011-12-1
	 **/
	public BigDecimal yuGaoCalculate(BigDecimal yugao, MaoxqhzjcTmp maoxqhzjc,
			BigDecimal yingyu, int jiszq, Dingdlj dingdlj) {

		BigDecimal yaohl = BigDecimal.ZERO;// 要货量

		BigDecimal yingyl = BigDecimal.ZERO;// 盈余量

		if (maoxqhzjc.getYugsfqz().equalsIgnoreCase(Const.YiLAI)) {
			CommonFun.logger.debug("国产件VJ模式订单预告计算预告取整情况下：yugao=" + yugao);
			CommonFun.logger.debug("国产件VJ模式订单预告计算预告取整情况下：yingyu=" + yingyu);
			CommonFun.logger.debug("国产件VJ模式订单预告计算预告取整情况下：Gongysfe="
					+ maoxqhzjc.getGongysfe());
			CommonFun.logger.debug("国产件VJ模式订单预告计算预告取整情况下：getBaozrl="
					+ maoxqhzjc.getBaozrl());

			yaohl = yugao.multiply(maoxqhzjc.getGongysfe()).subtract(yingyu);

			CommonFun.logger.debug("国产件VJ模式订单预告计算预告取整情况下：yaohl=" + yaohl);
			if (yaohl.compareTo(BigDecimal.ZERO) == -1) {
				yingyu = yaohl.negate();
				yaohl = BigDecimal.ZERO;
				CommonFun.logger.debug("国产件PP模式订单预告计算计算得到的要货量小于0的情况下：yaohl="
						+ yaohl);
			} else {
				yaohl = yaohl.divide(maoxqhzjc.getBaozrl(), 0,
						BigDecimal.ROUND_UP).multiply(maoxqhzjc.getBaozrl());// 毛需求乘以供应商份额然后按包装容量取整
				yingyu = yaohl.subtract(yugao.subtract(yingyu).multiply(
						maoxqhzjc.getGongysfe()));
			}
			CommonFun.logger.debug("国产件VJ模式订单预告计算预告取整情况下：yingyl=" + yingyl);

		} else {
			CommonFun.logger.debug("国产件VJ模式订单预告计算预告不取整情况下：yugao=" + yugao);
			CommonFun.logger.debug("国产件VJ模式订单预告计算预告不取整情况下：Gongysfe="
					+ maoxqhzjc.getGongysfe());
			yaohl = yugao.multiply(maoxqhzjc.getGongysfe());
			CommonFun.logger.debug("国产件VJ模式订单预告计算预告不取整情况下：yaohl=" + yaohl);
			yingyu = BigDecimal.ZERO;
			CommonFun.logger.debug("国产件VJ模式订单预告计算预告不取整情况下：yingyl=" + yingyl);
		}
		if (yaohl.compareTo(BigDecimal.ZERO) == -1) {
			yaohl = BigDecimal.ZERO;
			CommonFun.logger.debug("国产件VJ模式订单预告计算计算得到的要货量小于0的情况下：yaohl="
					+ yaohl);
		}
		String method = "setP" + jiszq + "sl";// 拼接getPi方法
		Class cls = dingdlj.getClass();// 得到Dingdlj类
		Method meth;
		try {
			meth = cls.getMethod(method, BigDecimal.class);
		} catch (SecurityException e) {
			CommonFun.logger.error(e);
			throw new RuntimeException(
					"IlOrderService.yuGaoCalculate,SecurityException");
		} catch (NoSuchMethodException e) {
			CommonFun.logger.error(e);
			throw new RuntimeException(
					"IlOrderService.yuGaoCalculate,NoSuchMethodException");
		}// 得到Dingdlj类的method拼接的方法

		try {
			meth.invoke(dingdlj, yaohl);
		} catch (IllegalArgumentException e) {
			CommonFun.logger.error(e);
			throw new RuntimeException(
					"IlOrderService.yuGaoCalculate,IllegalArgumentException");
		} catch (IllegalAccessException e) {
			CommonFun.logger.error(e);
			throw new RuntimeException(
					"IlOrderService.yuGaoCalculate,IllegalAccessException");
		} catch (InvocationTargetException e) {
			CommonFun.logger.error(e);
			throw new RuntimeException(
					"IlOrderService.yuGaoCalculate,InvocationTargetException");
		}// 执行得到的方法，setPx
		CommonFun.logger.debug("国产件VJ模式订单预告计算：yingyu=" + yingyu);
		return yingyu;
	}

	/**
	 * 行列转换
	 * 
	 * @param bancs
	 *            需求版次数组
	 * @param parrten
	 *            订单类型 "VJ"
	 * @param riq
	 *            资源获取日期
	 * @param usercenter
	 *            用户中心 "UL","UW"
	 * @param zhizlx
	 *            制造路线 "97W"
	 * 
	 * @参数说明：String pattern，需求类型；String riq，资源获取日期;String[] usercenter
	 *              用户中心数组,String[] banc 需求版次，String zhizlx 制造路线
	 */
	public void conversionColRow(String banc, String parrten, String riq,
			String usercenter, String zhizlx) {
		CommonFun.logger.info("Il订单计算模式为" + parrten + "的行列转换开始");
		long start = System.currentTimeMillis();
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单计算",
				"conversionColRow----行列转换开始");
		// 存放查询参数的map
		Map<String, String> map = new HashMap<String, String>();
		map.put("usercenter", usercenter);
		map.put("riq", riq);
		CommonFun.logger.debug("Il订单计算模式为" + parrten + "的行列循环中，版次号为：" + banc
				+ "用户中心为：" + usercenter + "资源获取日期为：" + riq);
		CalendarCenter calendarcenter = this.calendarCenterService
				.queryCalendarCenterObject(map);
		CommonFun.objPrint(calendarcenter, "返回的中心日历实体calendarcenter");
		map.clear();
		if (null != calendarcenter) {
			// 获取到年周期
			String nianzq = "";
			CommonFun.logger.debug("Il订单计算模式为" + parrten + "的行列循环中订货路线为"
					+ zhizlx);
			nianzq = calendarcenter.getNianzq();
			CommonFun.logger.debug("Il订单计算模式为" + parrten + "的行列循环中国产件年周期为："
					+ nianzq);

			// 获取到年周序
			String nianzx = calendarcenter.getNianzx();
			CommonFun.logger.debug("Il订单计算模式为" + parrten + "的行列循环中年周序为："
					+ nianzx);
			List allList = new ArrayList();
			
			//XSS 20161011 V4_015
			List allList2 = new ArrayList(); 
			
			//循环 ckx_wullj中不存在需求版次的VJ零件编号  
			List<Wullj> wulljList = this.vjMaoxqmxService.getWulljLingj(usercenter,banc, zhizlx);
			
			for(int m=0; m<wulljList.size();m++ ){
					Map<String, String> inMap2 = this.vjMaoxqmxService.getriq(wulljList.get(m).getShengcxbh() , usercenter, riq);
					if (null != inMap2 && !inMap2.isEmpty()) {
						//xss_20161122_非工作日时，向后推算J0日期
						//inMap2.put("j0riq", riq);
						
						inMap2.put("zhizlx", zhizlx);
						inMap2.put("usercenter", usercenter);
						//inMap.put("xuqbc", banc);
						inMap2.put("chanx", wulljList.get(m).getShengcxbh() );
						inMap2.put("lingjbh", wulljList.get(m).getLingjbh());
						CommonFun.mapPrint(inMap2, "Il订单计算模式为" + parrten
								+ "的行列循环中周模式汇总的参数map：" + inMap2); 
						allList2 = this.maoxqhzjTmpService.queryMaoxqhzj2(inMap2);
						if (!allList2.isEmpty()) {
							this.vjMaoxqmxService.listInsertForIL(allList2,
									parrten.toUpperCase());
						}
					}
			}

			
				//存在毛需求版次
				// 根据用户中心 需求版次 制造路线 在毛需求明细中查询产线
				List<String> chanxList = this.vjMaoxqmxService.getChanx(usercenter,
						banc, zhizlx);
				for (int x = 0; x < chanxList.size(); x++) {
					Map<String, String> inMap = this.vjMaoxqmxService.getriq(
							chanxList.get(x), usercenter, riq);
					if (null != inMap && !inMap.isEmpty()) {
						//xss_20161122_非工作日时，向后推算J0日期
						//inMap2.put("j0riq", riq);
						
						inMap.put("zhizlx", zhizlx);
						inMap.put("usercenter", usercenter);
						inMap.put("xuqbc", banc);
						inMap.put("chanx", chanxList.get(x));
						CommonFun.mapPrint(inMap, "Il订单计算模式为" + parrten
								+ "的行列循环中周模式汇总的参数map：" + inMap); 
						
						allList = this.maoxqhzjTmpService.queryMaoxqhzj(inMap);
						if (!allList.isEmpty()) {
							this.vjMaoxqmxService.listInsertForIL(allList,
									parrten.toUpperCase());
						}
					}
				} 
		}

		long end = System.currentTimeMillis();
		CommonFun.logger.info("----------------------------IL计算,行列转换时间"
				+ (end - start) / 1000);
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单计算",
				"conversionColRow----行列转换结束");
	}

	/**
	 * @param ziyxqrq
	 *            资源获取日期
	 * @param dingdlx
	 *            订单类型 "VJ"
	 * @param jiihyz
	 *            ""
	 * @param zhizlx
	 *            制造路线 "97W"
	 * @param loginuser
	 *            登录用户
	 * 
	 *            将毛需求分配循环表中的毛需求汇总到仓库并关联相关参考系然后插入到毛需求汇总参考系表中
	 * 
	 * @author 陈骏 参数为资源获取日期
	 * 
	 * @date 2011-12-14
	 */
	public String maoxqhzglckx(String ziyhqrq, String dingdlx, String jihyz,
			String zhizlx, LoginUser loginuser) {
		long start = System.currentTimeMillis();
		String message = null;
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il周期订单计算",
				"毛需求关联参考系开始");
		this.pjCheckWullj(loginuser);
		// 毛需求汇总 ,并插入中间表
		message = this.vjMaoxqHz(message, ziyhqrq, zhizlx, jihyz, loginuser);
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il日订单计算",
				"毛需求关联参考系结束");
		long end = System.currentTimeMillis();
		CommonFun.logger.info("----------------------------IL计算,计算时间"
				+ (end - start) / 1000);
		return message;
	}

	private void pjCheckWullj(LoginUser loginuser) {
		try {
			List<Yicbj> yicbjList = new ArrayList();
			List<MaoxqhzjcTmp> list = baseDao
					.select("hlorder.pjCheckMaoxqhzjcWullj");
			if (null != list && list.size() > 0) {
				for (MaoxqhzjcTmp maoxqhzjc : list) {
					String[] paramStr = new String[] {
							maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(),
							maoxqhzjc.getZhizlx() };
					CommonFun.insertError(Const.YICHANG_LX2,
							Const.YICHANG_LX2_str65, yicbjList,
							maoxqhzjc.getJihyz(), paramStr,
							maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(),
							loginuser, Const.JISMK_IL_CD);
					maoxqhzjTmpService.deleteMaoxqhzjById(maoxqhzjc.getId());
				}
			}
			if (yicbjList.size() > 0) {
				this.yicbjservice.insertAll(yicbjList);
			}
		} catch (Exception e) {
			CommonFun.logger.error(e);
		}
	}

	// 份额计算：判断传入的参数是否大于最大值和是否小于0
	private BigDecimal checkZero(BigDecimal value, BigDecimal maxValue) {
		BigDecimal lastValue = BigDecimal.ZERO;
		if (value.intValue() < 0) {
			lastValue = BigDecimal.ZERO;
		} else if (value.subtract(maxValue).intValue() > 0) {
			lastValue = maxValue;
		}
		return lastValue;
	}

	/**
	 * <p>
	 * Title:要货量取整方法
	 * </p>
	 * <p>
	 * Description:根据已有的要货量进行向上取整
	 * </p>
	 * 参数为
	 * <p>
	 * Copyright: Copyright (c) 2011
	 * </p>
	 * <p>
	 * Company: 软通动力
	 * </p>
	 * 
	 * @author 陈骏
	 * @version v1.0
	 * @date 2011-12-1
	 **/
	public Map<String, Object> quzheng(BigDecimal yaohls, String zhizlx,
			BigDecimal baozrl, BigDecimal zuixqdl) {
		Map<String, Object> map = new HashMap<String, Object>();
		BigDecimal quzhyhl = BigDecimal.ZERO;
		BigDecimal yingyus = BigDecimal.ZERO;
		// 按照零件供应商表中的最小起订量取整
		CommonFun.logger.debug("普通零件包装取整包装容量：" + baozrl);
		CommonFun.logger.debug("普通零件包装取整要货数量：" + yaohls);
		if (yaohls.compareTo(BigDecimal.ZERO) == -1) {
			yingyus = yaohls.negate();
			yaohls = BigDecimal.ZERO;
			quzhyhl = yaohls.divide(baozrl, 0, BigDecimal.ROUND_UP).multiply(
					baozrl);
		} else {
			quzhyhl = yaohls.divide(baozrl, 0, BigDecimal.ROUND_UP).multiply(
					baozrl);
			CommonFun.logger.debug("普通零件包装取整取整后要货量：" + quzhyhl);
			yingyus = quzhyhl.subtract(yaohls);
			CommonFun.logger.debug("普通零件包装取整盈余数量：" + yingyus);
		}
		map.put("yaohl", quzhyhl);
		map.put("yingyu", yingyus);
		return map;
	}

	/*
	 * 替代库存
	 */
	public BigDecimal tidjKuc(String usercenter, String lingjbh,
			String ziyhqrq, String cangkbh) {
		Map<String, Object> tidjMap = new HashMap<String, Object>();
		tidjMap.put("usercenter", usercenter);
		tidjMap.put("lingjbh", lingjbh);
		CommonFun.mapPrint(tidjMap, "替代件库存查询tidjKuc方法查找替代件仓库参数map");
		CommonFun.logger.debug("替代件库存查询tidjKuc方法查找替代件仓库的sql语句为：");
		List<Tidj> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("common.queryTidj", tidjMap);
		BigDecimal tidjKcSum = BigDecimal.ZERO;
		BigDecimal tidjdjf = BigDecimal.ZERO;

		if (!list.isEmpty()) {
			for (Tidj tidj : list) {

				Map<String, Object> tidjKcMap = new HashMap();
				tidjKcMap.put("usercenter", tidj.getUsercenter());
				tidjKcMap.put("lingjbh", tidj.getTidljh());
				tidjKcMap.put("ziyhqrq", ziyhqrq);
				tidjKcMap.put("cangkdm", cangkbh);
				CommonFun.mapPrint(tidjMap, "替代件库存查询tidjKuc方法查找替代件仓库库存和参数map");
				Object obj = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("common.queryTidjkc",
						tidjKcMap);

				Object djf = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("xinaxjis.queryTidjdjfC1",
						tidjKcMap);
				tidjdjf = new BigDecimal(djf != null ? djf.toString() : "0");
				if (obj == null) {
					tidjKcSum = tidjKcSum.add(BigDecimal.ZERO);
				} else {
					tidjKcSum = tidjKcSum.add(new BigDecimal(obj.toString()))
							.add(tidjdjf);
				}

			}
		}
		return tidjKcSum;
	}

	/**
	 * @行列转换，得到订单零件类集合（日模式）,然后插入订单零件表
	 * @author 陈骏
	 * @version v1.0
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @date 2012-01-03
	 */
	public void yugInDingdljPJ(Map<String, String> map, BigDecimal beihzq,
			BigDecimal fayzq, BigDecimal gongysfe, String ziyhqrq,
			String jihydz, String dingdnr, String gongyshth, String jssj,
			String jihyz, String banc, String caozz, String gcbh,
			Map<String, List<Dingdlj>> saveLingjMap,
			Map<String, List<Dingdmx>> saveMingxMap,
			Map<String, String> orderNumberMap, MaoxqhzjcTmp maoxqhzjc,
			LoginUser loginUse) {
		List<Dingdlj> ls = this.yugzjbTmpService.colRowYugzjbJ(map);
		if (null != ls) {
			for (Dingdlj dingdlj : ls) {
				dingdlj.setDingdh(this.getOrderNumber(Const.VJ,
						map.get("usercenter"), map.get("gongysdm"),
						orderNumberMap, maoxqhzjc, loginUse));// 订单号
				dingdlj.setGonghms(Const.FEIZHOUQIPJ);
				dingdlj.setBeihzq(beihzq);
				dingdlj.setFayzq(fayzq);
				dingdlj.setGongysfe(gongysfe);
				dingdlj.setZiyhqrq(ziyhqrq);
				dingdlj.setJihyz(jihydz);
				dingdlj.setDingdnr(dingdnr);
				dingdlj.setActive(Const.ACTIVE_1);
				dingdlj.setCreator(caozz);
				dingdlj.setP0fyzqxh(map.get("j0rq"));
				dingdlj.setCreate_time(CommonFun.getJavaTime());
				dingdlj.setEditor(caozz);
				dingdlj.setEdit_time(CommonFun.getJavaTime());
				CommonFun.objPrint(dingdlj,
						"行列转换，得到订单零件类集合yugInDingdljPJ方法中ps模式dingdlj");
				// this.vjDingdljService.doInsert(dingdlj);
				for (int x = 0; x < Const.DINGDNRCHANGDU; x++) {
					Dingdmx dingdmx = new Dingdmx();
					String methodJd = Const.GETP + x + "sl";
					CommonFun.logger
							.debug("行列转换，得到订单零件类集合yugInDingdljPJ方法中ps模式methodJd="
									+ methodJd);
					Class clsdd = dingdlj.getClass();
					Method methdjd;
					try {
						methdjd = clsdd.getMethod(methodJd, new Class[] {});
					} catch (SecurityException e1) {
						CommonFun.logger.error(e1);
						throw new RuntimeException(
								"IlOrderService.yugInDingdljPJ,SecurityException");
					} catch (NoSuchMethodException e1) {
						CommonFun.logger.error(e1);
						throw new RuntimeException(
								"IlOrderService.yugInDingdljPJ,NoSuchMethodException");
					}
					String pxriq = "";
					Method meth = null;

					BigDecimal jissl = BigDecimal.ZERO;
					if (x > Const.ZJCHANGDU) {
						jissl = BigDecimal.ZERO;
						CommonFun.logger
								.debug("行列转换，得到订单零件类集合yugInDingdljPJ方法中ps模式在（x > Const.ZJCHANGDU）时jissl="
										+ jissl);
					} else {

						clsdd = dingdlj.getClass();
						if (x == 0) {
							methodJd = "getP" + x + "fyzqxh";
							CommonFun.logger
									.debug("行列转换，得到订单零件类集合yugInDingdljPJ方法中ps模式不在（x > Const.ZJCHANGDU）并且在（x == 0）时methodJd="
											+ methodJd);
						} else {
							methodJd = "getP" + x + "rq";
							CommonFun.logger
									.debug("行列转换，得到订单零件类集合yugInDingdljPJ方法中ps模式不在（x > Const.ZJCHANGDU）并且不在在（x == 0）时methodJd="
											+ methodJd);
						}

						try {
							methdjd = clsdd.getMethod(methodJd, new Class[] {});
						} catch (SecurityException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException(
									"IlOrderService.yugInDingdljPJ,SecurityException");
						} catch (NoSuchMethodException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException(
									"IlOrderService.yugInDingdljPJ,NoSuchMethodException");
						}
						try {
							pxriq = methdjd.invoke(dingdlj, null).toString();
						} catch (IllegalArgumentException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException(
									"IlOrderService.yugInDingdljPJ,IllegalArgumentException");
						} catch (IllegalAccessException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException(
									"IlOrderService.yugInDingdljPJ,IllegalAccessException");
						} catch (InvocationTargetException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException(
									"IlOrderService.yugInDingdljPJ,InvocationTargetException");
						}
						CommonFun.logger
								.debug("行列转换，得到订单零件类集合yugInDingdljPJ方法中ps模式pxriq="
										+ pxriq);
						methodJd = Const.GETP + x + "sl";
						CommonFun.logger
								.debug("行列转换，得到订单零件类集合yugInDingdljPJ方法中ps模式methodJd="
										+ methodJd);
						try {
							methdjd = clsdd.getMethod(methodJd, new Class[] {});
						} catch (SecurityException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException(
									"IlOrderService.yugInDingdljPJ,SecurityException");
						} catch (NoSuchMethodException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException(
									"IlOrderService.yugInDingdljPJ,NoSuchMethodException");
						}
						try {
							jissl = new BigDecimal(methdjd
									.invoke(dingdlj, null).toString());
						} catch (IllegalArgumentException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException(
									"IlOrderService.yugInDingdljPJ,IllegalArgumentException");
						} catch (IllegalAccessException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException(
									"IlOrderService.yugInDingdljPJ,IllegalAccessException");
						} catch (InvocationTargetException e) {
							CommonFun.logger.error(e);
							throw new RuntimeException(
									"IlOrderService.yugInDingdljPJ,InvocationTargetException");
						}
						CommonFun.logger
								.debug("行列转换，得到订单零件类集合yugInDingdljPJ方法中ps模式jissl="
										+ jissl);
					}
					dingdmx.setUabzlx(dingdlj.getUabzlx());
					dingdmx.setUabzuclx(dingdlj.getUabzuclx());
					dingdmx.setUabzucsl(dingdlj.getUabzucsl());
					dingdmx.setUabzucrl(dingdlj.getUabzucrl());
					dingdmx.setDingdh(dingdlj.getDingdh());
					dingdmx.setUsercenter(dingdlj.getUsercenter());
					dingdmx.setLingjbh(dingdlj.getLingjbh());
					dingdmx.setGongysdm(dingdlj.getGongysdm());
					dingdmx.setGongyslx(dingdlj.getGongyslx());
					dingdmx.setCangkdm(dingdlj.getCangkdm());
					dingdmx.setFahd(dingdlj.getFahd());
					dingdmx.setDinghcj(dingdlj.getDinghcj());
					dingdmx.setDanw(dingdlj.getDanw());
					dingdmx.setLujdm(dingdlj.getLujdm());
					dingdmx.setJihyz(dingdlj.getJihyz());
					dingdmx.setYijfl(dingdlj.getYijfl());
					// ///dingdmx.setZhuangt(Const.DINGD_STATUS_DSX);
					// //订单明细修改为已生效
					dingdmx.setZhuangt(Const.DINGD_STATUS_YSX);
					dingdmx.setLeix(Const.SHIFOUSHIYUGAO);
					dingdmx.setGonghlx(Const.FEIZHOUQIPJ);
					dingdmx.setActive(Const.ACTIVE_1);
					// 设置要货开始时间
					dingdmx.setYaohqsrq(pxriq);
					// 设置要货结束时间
					dingdmx.setYaohjsrq(pxriq);
					dingdmx.setJiaofrq(pxriq);

					dingdmx.setFayrq(pxriq);
					dingdmx.setShul(jissl);
					dingdmx.setJissl(jissl);
					dingdmx.setCreator(caozz);
					dingdmx.setGcbh(gcbh);
					dingdmx.setCreate_time(CommonFun.getJavaTime());
					CommonFun.objPrint(dingdmx,
							"行列转换，得到订单零件类集合yugInDingdljPJ方法中dingdmx");
					// ///////////////////////////////wuyichao
					List<Dingdmx> flagMx = saveMingxMap
							.get(dingdmx.getDingdh());
					if (null == flagMx) {
						flagMx = new ArrayList<Dingdmx>();
					}
					flagMx.add(dingdmx);
					saveMingxMap.put(dingdmx.getDingdh(), flagMx);
					// this.dingdmxService.doInsert(dingdmx);
					// //////////////////////////wuyichao
				}
				// //////////////////////////wuyichao
				List<Dingdlj> flagLj = saveLingjMap.get(dingdlj.getDingdh());
				if (null == flagLj) {
					flagLj = new ArrayList<Dingdlj>();
				}
				flagLj.add(dingdlj);
				saveLingjMap.put(dingdlj.getDingdh(), flagLj);
				// this.vjDingdljService.doInsert(dingdlj);
				// //////////////////////////wuyichao
				Dingd existDd = new Dingd();
				Map<String, String> ddhMap = new HashMap<String, String>();
				ddhMap.put("dingdh", dingdlj.getDingdh());
				existDd = this.dingdservice.queryDingdByDingdh(ddhMap);
				CommonFun.objPrint(existDd,
						"行列转换，得到订单零件类集合yugInDingdljPJ方法中existDd");
				if (null == existDd) {

					Map<String, String> riqMap = new HashMap<String, String>();
					riqMap.put("usercenter", dingdlj.getUsercenter());
					riqMap.put("riq", dingdlj.getZiyhqrq().substring(0, 10));
					CalendarCenter centerObject = this.calendarCenterService
							.queryCalendarCenterObject(riqMap);
					Dingd dd = new Dingd();
					dd.setDingdh(dingdlj.getDingdh());// 订单号
					dd.setHeth(gongyshth);// 合同号
					dd.setDingdlx(Const.DINGDLX_ILORDER);// 国产订单类型
					dd.setGongysdm(dingdlj.getGongysdm());// 供应商编号
					dd.setChullx(dingdlj.getGonghms());
					dd.setDingdzt(Const.DINGD_STATUS_JSZ);
					dd.setDingdjssj(jssj);
					dd.setDingdnr(dingdlj.getDingdnr());
					dd.setZiyhqrq(dingdlj.getZiyhqrq());
					dd.setFahzq(dingdlj.getP0fyzqxh());
					dd.setMaoxqbc(banc);
					dd.setJislx(jihyz);
					dd.setShifyjsyhl(Const.SHIFYJSYHL_Y);
					dd.setShifzfsyhl(Const.SHIFZFSYHL_YES);
					dd.setCreator(caozz);
					dd.setCreate_time(CommonFun.getJavaTime());
					dd.setActive(Const.ACTIVE_0);
					dd.setUsercenter(dingdlj.getUsercenter());
					dd.setJiszq(centerObject.getNianzq());
					CommonFun.objPrint(dd, "行列转换，得到订单零件类集合yugInDingdljPJ方法中dd");
					this.dingdservice.doInsert(dd);
				}
			}
		}
	}

	public BigDecimal backValue(String method, Object obj, int pianysj,
			String dingdlx) {
		CommonFun.logger.debug("反射取值backValue方法参数method=" + method);
		CommonFun.logger.debug("反射取值backValue方法参数pianysj=" + pianysj);
		CommonFun.logger.debug("反射取值backValue方法参数dingdlx=" + dingdlx);
		CommonFun.objPrint(obj, "反射取值backValue方法参数obj");
		BigDecimal value = BigDecimal.ZERO;
		if (null != obj) {
			Class cls = obj.getClass();// 得到Maoxqhzpc类
			Method meth = null;
			int changdu = 0;
			changdu = Const.ZJCHANGDU;
			if (pianysj > changdu) {
				value = BigDecimal.ZERO;
			} else {

				try {
					meth = cls.getMethod(method, new Class[] {});
				} catch (NoSuchMethodException e1) {
					CommonFun.logger.error(e1);
					throw new RuntimeException(
							"IlOrderService.backValue,NoSuchMethodException");

				} catch (SecurityException e1) {
					CommonFun.logger.error(e1);
					throw new RuntimeException(
							"IlOrderService.backValue,SecurityException");
				}
				try {
					value = new BigDecimal(meth.invoke(obj, null).toString());
				} catch (IllegalArgumentException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException(
							"IlOrderService.backValue,IllegalArgumentException");

				} catch (IllegalAccessException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException(
							"IlOrderService.backValue,IllegalAccessException");

				} catch (InvocationTargetException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException(
							"IlOrderService.backValue,InvocationTargetException");

				}
			}
		}
		CommonFun.logger.debug("反射取值backValue方法结果value=" + value);
		return value;
	}

	public void insertDingdMX(Dingdlj dingdlj, Object obj, String zhizlx,
			int pianysj, String dingdlx, Map<String, String> dingdhMap,
			BigDecimal jissl, String pxriq, String caozz, String zhouqilx,
			Map<String, List<Dingdmx>> saveMingxMap) {
		if (null == dingdlj.getDingdh()) {
			return;
		}

		Dingdmx dingdmx = new Dingdmx();
		dingdmx.setUabzlx(dingdlj.getUabzlx());
		dingdmx.setUabzuclx(dingdlj.getUabzuclx());
		dingdmx.setUabzucsl(dingdlj.getUabzucsl());
		dingdmx.setUabzucrl(dingdlj.getUabzucrl());
		dingdmx.setDingdh(dingdlj.getDingdh());
		dingdmx.setUsercenter(dingdlj.getUsercenter());
		dingdmx.setLingjbh(dingdlj.getLingjbh());
		dingdmx.setGongysdm(dingdlj.getGongysdm());
		dingdmx.setGongyslx(dingdlj.getGongyslx());
		dingdmx.setCangkdm(dingdlj.getCangkdm());
		dingdmx.setFahd(dingdlj.getFahd());
		dingdmx.setDinghcj(dingdlj.getDinghcj());
		dingdmx.setDanw(dingdlj.getDanw());
		dingdmx.setLujdm(dingdlj.getLujdm());
		dingdmx.setJihyz(dingdlj.getJihyz());
		dingdmx.setYijfl(dingdlj.getYijfl());
		dingdmx.setCreator(caozz);
		dingdmx.setCreate_time(CommonFun.getJavaTime());
		dingdmx.setEditor(caozz);
		dingdmx.setEdit_time(CommonFun.getJavaTime());
		dingdmx.setXiehzt(dingdlj.getXiehzt());
		dingdmx.setZhuangt(Const.DINGD_STATUS_DSX);

		if (zhouqilx.equals(Const.SHIFOUSHIJIDING)) {
			dingdmx.setLeix(Const.SHIFOUSHIJIDING);
		} else if (zhouqilx.equals(Const.SHIFOUSHIYUGAO)) {
			dingdmx.setLeix(Const.SHIFOUSHIYUGAO);
		}
		dingdmx.setActive(Const.ACTIVE_1);
		dingdmx.setShul(jissl);
		dingdmx.setJissl(jissl);
		dingdmx.setCreator(caozz);
		dingdmx.setCreate_time(CommonFun.getJavaTime());
		MaoxqhzjcTmp maoxqhzjc = (MaoxqhzjcTmp) obj;
		dingdmx.setGcbh(maoxqhzjc.getGcbh());
		dingdmx.setGonghlx(Const.VJ);
		dingdmx.setLingjmc(maoxqhzjc.getLingjmc());
		dingdmx.setGongsmc(maoxqhzjc.getGongsmc());
		dingdmx.setNeibyhzx(maoxqhzjc.getNeibyhzx());
		// 设置要货开始时间
		dingdmx.setYaohqsrq(pxriq);
		// 设置要货结束时间
		dingdmx.setYaohjsrq(pxriq);
		dingdmx.setJiaofrq(pxriq);
		dingdmx.setFayrq(pxriq);
		// 订单明细状态变成已发送
		dingdmx.setZhuangt(Const.DINGD_STATUS_YSX);

		// /////////////////////wuyichao///////////////
		String dingdh = dingdmx.getDingdh();
		List<Dingdmx> flagDingdmx = saveMingxMap.get(dingdh);
		if (null == flagDingdmx) {
			flagDingdmx = new ArrayList<Dingdmx>();
		}
		flagDingdmx.add(dingdmx);
		saveMingxMap.put(dingdh, flagDingdmx);
		// /////////////////////wuyichao///////////////
	}

	public void insertdingd(Dingdlj dingdlj, Object obj, String dingdlx,
			String jssj, String banch, String jihyz, String caozz) {
		if (null == dingdlj.getDingdh()) {
			return;
		}
		Dingd dd = new Dingd();
		dd.setDingdh(dingdlj.getDingdh());// 订单号
		dd.setDingdlx(Const.DINGDLX_ILORDER);// 国产订单类型
		dd.setGongysdm(dingdlj.getGongysdm());// 供应商编号
		dd.setDingdzt(Const.DINGD_STATUS_JSZ);
		dd.setDingdjssj(jssj);
		dd.setDingdnr(dingdlj.getDingdnr());
		dd.setZiyhqrq(dingdlj.getZiyhqrq());
		dd.setFahzq(dingdlj.getP0fyzqxh());
		dd.setMaoxqbc(banch);
		dd.setJislx(jihyz);
		dd.setShifyjsyhl(Const.SHIFYJSYHL_N);
		dd.setShifzfsyhl(Const.SHIFZFSYHL_YES);
		dd.setCreator(caozz);
		dd.setCreate_time(CommonFun.getJavaTime());
		dd.setActive(Const.ACTIVE_0);
		MaoxqhzjcTmp maoxqhzjc = (MaoxqhzjcTmp) obj;
		dd.setHeth(maoxqhzjc.getGongyhth());// 合同号
		dd.setChullx(Const.VJ);
		dd.setUsercenter(maoxqhzjc.getUsercenter());
		dd.setJiszq(dingdlj.getZiyhqrq().substring(0, 10));

		Map<String, String> ddhMap = new HashMap<String, String>();
		ddhMap.put("dingdh", dingdlj.getDingdh());
		Dingd existDd = this.dingdservice.queryDingdByDingdh(ddhMap);
		CommonFun.objPrint(dd, "国产订单插入时的dd");
		if (null == existDd) {
			this.dingdservice.doInsert(dd);
		}
	}

	/**
	 * 
	 * @param message
	 * @param ziyxqrq
	 *            资源获取日期
	 * @param zhizlx
	 *            制造路线 "97W"
	 * @param jihyz
	 *            ""
	 * @param loginuser
	 *            登录用户
	 * @return
	 */
	public String vjMaoxqHz(String message, String ziyhqrq, String zhizlx,
			String jihyz, LoginUser loginuser) {
		String dingdnr = "";
		
		//用户中心 - xss-20160422 
		List list = this.maoxqhzjcTmpService.proMaoxqhzjc( ziyhqrq ,loginuser);// 把毛需求汇总日表的需求汇总到仓库并关联参考系表，并将数据插入到数据库中
		// CommonFun.objListPrint(list, "VJ模式毛需求-参考系汇总结果LIST");
		if (list.size() > 0) {
			Map<String, String> map = new HashMap<String, String>();
			MaoxqhzjcTmp mzjc = (MaoxqhzjcTmp) list.get(0);
			map.put("usercenter", mzjc.getUsercenter());
			map.put("riq", ziyhqrq);
			CalendarCenter calendarCenterObject = this.calendarCenterService
					.queryCalendarCenterObject(map);
			String suoszq = calendarCenterObject.getNianzq().substring(4);
			CommonFun.logger.debug("VJ模式毛需求-参考系汇总所属周期为：" + suoszq);
			dingdnr = this.jdygzqservice.queryDingdnr(zhizlx, suoszq);
			CommonFun.logger.debug("VJ模式毛需求-参考系汇总订单内容为：" + dingdnr);
			if (null == dingdnr) {
				message = zhizlx + "订货路线下" + suoszq + "既定-预告-周期表订单内容字段为空。";
				return message;
			}
		} else {
			message = "对应的毛需求汇总_参考系表表数据为空.";
			return message;
		}
		// ////////////wuyichao 2014/02/20 将资源快照表内的数据存入内存
		// //////////////////////////////
		Map<String, BigDecimal> xianbKcMap = new HashMap<String, BigDecimal>();
		Map<String, Integer> xianbCountMap = new HashMap<String, Integer>();
		Map<String, BigDecimal> dinghKcMap = new HashMap<String, BigDecimal>();
		Map<String, Integer> dinghCountMap = new HashMap<String, Integer>();
		HashMap<String, Integer> fengbqMap = new HashMap<String, Integer>();
		// 初始化map
		initKucMapAndCountMap(queryAllKucByXianbck(ziyhqrq), xianbKcMap,
				xianbCountMap);
		initKucMapAndCountMap(queryAllKucByDinghck(ziyhqrq), dinghKcMap,
				dinghCountMap);
		// ////////////wuyichao 2014/02/20 将资源快照表内的数据存入内存
		// //////////////////////////////

		List<MaoxqhzjcTmp> rsList = new ArrayList<MaoxqhzjcTmp>();
		int maxPx = 0;
		int pianysj = 0;
		int fengbqMax = 0;
		// 遍历最大封闭期
		for (int i = 0; i < list.size(); i++) {
			MaoxqhzjcTmp maoxqhzjc = (MaoxqhzjcTmp) list.get(i);// 把汇总的数据插入到毛需求汇总参考系表中
			pianysj = maoxqhzjc.getBeihzq().add(maoxqhzjc.getFayzq())
				.divide(BigDecimal.ONE, 0, BigDecimal.ROUND_UP).intValue();
			Integer fengbq = fengbqMap.get(maoxqhzjc.getLingjbh());
			if (null == fengbq || fengbq <= 1)
			{
				fengbqMap.put(maoxqhzjc.getLingjbh(),pianysj);
			}
			else
			{
				if ( pianysj > fengbq)
				{
					maxPx = pianysj;
					fengbqMap.put(maoxqhzjc.getLingjbh(),maxPx);
				}
			}
		}
		
		for (int i = 0; i < list.size(); i++) {
			MaoxqhzjcTmp maoxqhzjc = (MaoxqhzjcTmp) list.get(i);// 把汇总的数据插入到毛需求汇总参考系表中
			// begin 封闭期的计算
			fengbqMax = fengbqMap.get(maoxqhzjc.getLingjbh());
			pianysj = this.getPianysj(maoxqhzjc, fengbqMax);
			CommonFun.logger.info("实际封闭期：" + pianysj);
			if (pianysj == -1) {
				continue;
			}
			// end 封闭期的计算
			// int pianysj = maoxqhzjc.getBeihzq().add(maoxqhzjc.getFayzq())
			// .divide(BigDecimal.ONE, 0, BigDecimal.ROUND_UP).intValue();
			CommonFun.logger.debug("VJ模式毛需求-参考系汇总偏移时间为：" + pianysj);
			BigDecimal daixh = BigDecimal.ZERO;
			for (int x = 0; x < pianysj; x++) {
				Method meth = null;
				try {
					if (x <= 15) {
						String method = Const.GETJ + x;// 拼接getPi方法
						Class cls = maoxqhzjc.getClass();// 得到Maoxqhzpc类

						meth = cls.getMethod(method, new Class[] {});
					}
				} catch (SecurityException e1) {
					CommonFun.logger.error(e1);
					throw new RuntimeException(
							"IlOrderService.pjMaoxqHz,SecurityException");
				} catch (NoSuchMethodException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException(
							"IlOrderService.pjMaoxqHz,NoSuchMethodException");
				}// 得到Maoxqhzpc类的method拼接的方法
				try {
					if (x > 15) {
						daixh = daixh.add(BigDecimal.ZERO);
					} else {
						daixh = daixh.add(new BigDecimal(meth.invoke(maoxqhzjc,
								null).toString()));
						CommonFun.logger.info("待消耗量：" + daixh);
					}

				} catch (IllegalArgumentException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException(
							"IlOrderService.pjMaoxqHz,IllegalArgumentException");
				} catch (IllegalAccessException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException(
							"IlOrderService.pjMaoxqHz,IllegalAccessException");
				} catch (InvocationTargetException e) {
					CommonFun.logger.error(e);
					throw new RuntimeException(
							"IlOrderService.pjMaoxqHz,InvocationTargetException");
				}// 执行得到的方法，取得既定周期的数量
			}
			CommonFun.logger.debug("VJ模式毛需求-参考系汇总待消耗为：" + daixh);
			maoxqhzjc.setDaixh(daixh);
			String key = maoxqhzjc.getUsercenter() + maoxqhzjc.getLingjbh()
					+ maoxqhzjc.getCangkdm();
			if (maoxqhzjc.getCangklx().equals("0")) {
				Integer count = xianbCountMap.get(key);
				if (null == count || count <= 1) {
					maoxqhzjc
							.setKuc(xianbKcMap.get(key) == null ? BigDecimal.ZERO
									: xianbKcMap.get(key));
				} else {
					String paramStr[] = new String[] {
							maoxqhzjc.getUsercenter(), ziyhqrq,
							maoxqhzjc.getLingjbh(), maoxqhzjc.getCangkdm() };
					yicbjservice.insertError(Const.YICHANG_LX3,
							Const.YICHANG_LX3_str8, jihyz, paramStr,
							maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(),
							loginuser, Const.JISMK_IL_CD);
					continue;
				}
			} else if (maoxqhzjc.getCangklx().equals("1")) {
				Integer count = dinghCountMap.get(key);
				if (null == count || count <= 1) {
					maoxqhzjc
							.setKuc(dinghKcMap.get(key) == null ? BigDecimal.ZERO
									: dinghKcMap.get(key));
				} else {
					String paramStr[] = new String[] {
							maoxqhzjc.getUsercenter(), ziyhqrq,
							maoxqhzjc.getLingjbh(), maoxqhzjc.getCangkdm() };
					yicbjservice.insertError(Const.YICHANG_LX3,
							Const.YICHANG_LX3_str8, jihyz, paramStr,
							maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(),
							loginuser, Const.JISMK_IL_CD);
					continue;
				}

			}
			if (null == maoxqhzjc.getAnqkc()) {
				maoxqhzjc.setAnqkc(BigDecimal.ZERO);
			}
			if (null == maoxqhzjc.getKuc()) {
				maoxqhzjc.setKuc(BigDecimal.ZERO);
			}
			if (null == maoxqhzjc.getTiaozjsz()) {
				maoxqhzjc.setTiaozjsz(BigDecimal.ZERO);
			}
			if (null == maoxqhzjc.getDingdlj()) {
				maoxqhzjc.setDingdlj(BigDecimal.ZERO);
			}
			if (null == maoxqhzjc.getJiaoflj()) {
				maoxqhzjc.setJiaoflj(BigDecimal.ZERO);
			}
			if (null == maoxqhzjc.getXittzz()) {
				maoxqhzjc.setXittzz(BigDecimal.ZERO);
			}
			if (null == maoxqhzjc.getDingdzzlj()) {
				maoxqhzjc.setDingdzzlj(BigDecimal.ZERO);
			}
			maoxqhzjc.setDingdnr(dingdnr);
			CommonFun.objPrint(maoxqhzjc, "VJ模式毛需求-参考系汇总maoxqhzjc类");
			rsList.add(maoxqhzjc);
		}

		this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch("hlorder.insertMaoxqhzjcTmp", rsList);
		return message;
	}

	public Integer check(LoginUser loginuser, String dingdlx) {
		return this.vjCheck(loginuser);
	}

	/**
	 * 效验中间表
	 * 
	 * @param loginuser
	 * @return
	 */
	public Integer vjCheck(LoginUser loginuser) {
		Integer count = 0;
		List<MaoxqhzjcTmp> erroList = new ArrayList();
		List<Yicbj> yicbjList = new ArrayList();

		// 份额校验，份额之和必须为1
		erroList = this.maoxqhzjcTmpService.checkFene();
		if (erroList.size() > 0) {
			count = erroList.size();
			for (MaoxqhzjcTmp maoxqhzjc : erroList) {
				String[] paramStr = new String[] { maoxqhzjc.getUsercenter(),
						maoxqhzjc.getLingjbh() };
				CommonFun.insertError(Const.YICHANG_LX4,
						Const.YICHANG_LX4_str1, yicbjList,
						maoxqhzjc.getJihyz(), paramStr,
						maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(),
						loginuser, Const.JISMK_IL_CD);
			}

		}

		erroList.clear();
		erroList = this.maoxqhzjcTmpService.checkZhizlx();
		if (erroList.size() > 0) {
			count = erroList.size();
			for (MaoxqhzjcTmp maoxqhzjc : erroList) {
				String[] paramStr = new String[] { maoxqhzjc.getUsercenter(),
						maoxqhzjc.getLingjbh(), "制造路线" };
				CommonFun.insertError(Const.YICHANG_LX2,
						Const.YICHANG_LX2_str15, yicbjList,
						maoxqhzjc.getJihyz(), paramStr,
						maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(),
						loginuser, Const.JISMK_IL_CD);
			}
		}

		erroList.clear();
		erroList = this.maoxqhzjcTmpService.checkGongysdm();
		if (erroList.size() > 0) {
			count = erroList.size();
			for (MaoxqhzjcTmp maoxqhzjc : erroList) {
				String[] paramStr = new String[] { maoxqhzjc.getUsercenter(),
						maoxqhzjc.getLingjbh(), "供应商代码" };
				CommonFun.insertError(Const.YICHANG_LX2,
						Const.YICHANG_LX2_str5, yicbjList,
						maoxqhzjc.getJihyz(), paramStr,
						maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(),
						loginuser, Const.JISMK_IL_CD);
			}
		}

		erroList.clear();
		erroList = this.maoxqhzjcTmpService.checkDinghcj();
		if (erroList.size() > 0) {
			count = erroList.size();
			for (MaoxqhzjcTmp maoxqhzjc : erroList) {
				String[] paramStr = new String[] { maoxqhzjc.getUsercenter(),
						maoxqhzjc.getLingjbh(), "订货车间" };
				CommonFun.insertError(Const.YICHANG_LX2,
						Const.YICHANG_LX2_str50, yicbjList,
						maoxqhzjc.getJihyz(), paramStr,
						maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(),
						loginuser, Const.JISMK_IL_CD);
			}
		}

		erroList.clear();
		erroList = this.maoxqhzjcTmpService.checkBeihzq();
		if (erroList.size() > 0) {
			count = erroList.size();
			for (MaoxqhzjcTmp maoxqhzjc : erroList) {
				String[] paramStr = new String[] { maoxqhzjc.getUsercenter(),
						maoxqhzjc.getLingjbh(), "备货周期" };
				CommonFun.insertError(Const.YICHANG_LX2,
						Const.YICHANG_LX2_str4, yicbjList,
						maoxqhzjc.getJihyz(), paramStr,
						maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(),
						loginuser, Const.JISMK_IL_CD);
			}
		}

		erroList.clear();
		erroList = this.maoxqhzjcTmpService.checkFayzq();
		if (erroList.size() > 0) {
			count = erroList.size();
			for (MaoxqhzjcTmp maoxqhzjc : erroList) {
				String[] paramStr = new String[] { maoxqhzjc.getUsercenter(),
						maoxqhzjc.getLingjbh(), "发运周期" };
				CommonFun.insertError(Const.YICHANG_LX2,
						Const.YICHANG_LX2_str12, yicbjList,
						maoxqhzjc.getJihyz(), paramStr,
						maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(),
						loginuser, Const.JISMK_IL_CD);
			}
		}

		erroList.clear();
		erroList = this.maoxqhzjcTmpService.checkZiyhqrq();
		if (erroList.size() > 0) {
			count = erroList.size();
			for (MaoxqhzjcTmp maoxqhzjc : erroList) {
				String[] paramStr = new String[] { maoxqhzjc.getUsercenter(),
						maoxqhzjc.getLingjbh(), "资源获取日期" };
				CommonFun.insertError(Const.YICHANG_LX2,
						Const.YICHANG_LX2_str49, yicbjList,
						maoxqhzjc.getJihyz(), paramStr,
						maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(),
						loginuser, Const.JISMK_IL_CD);
			}
		}

		erroList.clear();
		erroList = this.maoxqhzjcTmpService.checkLujdm();
		if (erroList.size() > 0) {
			count = erroList.size();
			for (MaoxqhzjcTmp maoxqhzjc : erroList) {
				if (maoxqhzjc.getCangkdm() == null
						|| maoxqhzjc.getGongysdm() == null) {
					String[] paramStr = new String[] {
							maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(),
							"路径代码" };
					CommonFun.insertError(Const.YICHANG_LX2,
							Const.YICHANG_LX2_str49, yicbjList,
							maoxqhzjc.getJihyz(), paramStr,
							maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(),
							loginuser, Const.JISMK_IL_CD);
				} else {
					String[] paramStr = new String[] {
							maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(),
							maoxqhzjc.getGongysdm(), maoxqhzjc.getCangkdm(),
							"路径代码" };
					CommonFun.insertError(Const.YICHANG_LX2,
							Const.YICHANG_LX2_str13, yicbjList,
							maoxqhzjc.getJihyz(), paramStr,
							maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(),
							loginuser, Const.JISMK_IL_CD);
				}
			}
		}

		erroList.clear();
		erroList = this.maoxqhzjcTmpService.checkCangkdm();
		if (erroList.size() > 0) {
			count = erroList.size();
			for (MaoxqhzjcTmp maoxqhzjc : erroList) {
				String[] paramStr = new String[] { maoxqhzjc.getUsercenter(),
						maoxqhzjc.getLingjbh(), "仓库编号" };
				CommonFun.insertError(Const.YICHANG_LX2,
						Const.YICHANG_LX2_str49, yicbjList, Const.POAcode,
						paramStr, maoxqhzjc.getUsercenter(),
						maoxqhzjc.getLingjbh(), loginuser, Const.JISMK_IL_CD);
			}
		}

		erroList.clear();
		erroList = this.maoxqhzjcTmpService.checkUabzlx();
		if (erroList.size() > 0) {
			count = erroList.size();
			for (MaoxqhzjcTmp maoxqhzjc : erroList) {
				if (maoxqhzjc.getGongysdm() != null) {
					String wlgyy = this.vjWulljService.queryWlgyy(
							maoxqhzjc.getUsercenter(), maoxqhzjc.getCangkdm());
					String[] paramStr = new String[] {
							maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(),
							maoxqhzjc.getGongysdm(), "UA包装类型" };
					CommonFun.insertError(Const.YICHANG_LX2,
							Const.YICHANG_LX2_str59, yicbjList, wlgyy,
							paramStr, maoxqhzjc.getUsercenter(),
							maoxqhzjc.getLingjbh(), loginuser,
							Const.JISMK_IL_CD);
				} else {
					String[] paramStr = new String[] {
							maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(),
							"UA包装类型" };
					CommonFun.insertError(Const.YICHANG_LX2,
							Const.YICHANG_LX2_str49, yicbjList, Const.POAcode,
							paramStr, maoxqhzjc.getUsercenter(),
							maoxqhzjc.getLingjbh(), loginuser,
							Const.JISMK_IL_CD);
				}
			}
		}

		erroList.clear();
		erroList = this.maoxqhzjcTmpService.checkUabzuclx();
		if (erroList.size() > 0) {
			count = erroList.size();
			for (MaoxqhzjcTmp maoxqhzjc : erroList) {
				if (maoxqhzjc.getGongysdm() != null) {
					String wlgyy = this.vjWulljService.queryWlgyy(
							maoxqhzjc.getUsercenter(), maoxqhzjc.getCangkdm());
					String[] paramStr = new String[] {
							maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(),
							maoxqhzjc.getGongysdm(), "UA中UC包装类型" };
					CommonFun.insertError(Const.YICHANG_LX2,
							Const.YICHANG_LX2_str59, yicbjList, wlgyy,
							paramStr, maoxqhzjc.getUsercenter(),
							maoxqhzjc.getLingjbh(), loginuser,
							Const.JISMK_IL_CD);
				} else {
					String[] paramStr = new String[] {
							maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(),
							"UA中UC包装类型" };
					CommonFun.insertError(Const.YICHANG_LX2,
							Const.YICHANG_LX2_str49, yicbjList, Const.POAcode,
							paramStr, maoxqhzjc.getUsercenter(),
							maoxqhzjc.getLingjbh(), loginuser,
							Const.JISMK_IL_CD);
				}

			}
		}

		erroList.clear();
		erroList = this.maoxqhzjcTmpService.checkUabzucsl();
		if (erroList.size() > 0) {
			count = erroList.size();
			for (MaoxqhzjcTmp maoxqhzjc : erroList) {
				if (maoxqhzjc.getGongysdm() != null) {
					String wlgyy = this.vjWulljService.queryWlgyy(
							maoxqhzjc.getUsercenter(), maoxqhzjc.getCangkdm());
					String[] paramStr = new String[] {
							maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(),
							maoxqhzjc.getGongysdm(), "UA中UC个数" };
					CommonFun.insertError(Const.YICHANG_LX2,
							Const.YICHANG_LX2_str59, yicbjList, wlgyy,
							paramStr, maoxqhzjc.getUsercenter(),
							maoxqhzjc.getLingjbh(), loginuser,
							Const.JISMK_IL_CD);
				} else {
					String[] paramStr = new String[] {
							maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(),
							"UA中UC个数" };
					CommonFun.insertError(Const.YICHANG_LX2,
							Const.YICHANG_LX2_str49, yicbjList, Const.POAcode,
							paramStr, maoxqhzjc.getUsercenter(),
							maoxqhzjc.getLingjbh(), loginuser,
							Const.JISMK_IL_CD);
				}
			}
		}

		erroList.clear();
		erroList = this.maoxqhzjcTmpService.checkUabzucrl();
		if (erroList.size() > 0) {
			count = erroList.size();
			for (MaoxqhzjcTmp maoxqhzjc : erroList) {
				if (maoxqhzjc.getGongysdm() != null) {
					String wlgyy = this.vjWulljService.queryWlgyy(
							maoxqhzjc.getUsercenter(), maoxqhzjc.getCangkdm());
					String[] paramStr = new String[] {
							maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(),
							maoxqhzjc.getGongysdm(), "UA中UC容量" };
					CommonFun.insertError(Const.YICHANG_LX2,
							Const.YICHANG_LX2_str59, yicbjList, wlgyy,
							paramStr, maoxqhzjc.getUsercenter(),
							maoxqhzjc.getLingjbh(), loginuser,
							Const.JISMK_IL_CD);
				} else {
					String[] paramStr = new String[] {
							maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(),
							"UA中UC容量" };
					CommonFun.insertError(Const.YICHANG_LX2,
							Const.YICHANG_LX2_str49, yicbjList, Const.POAcode,
							paramStr, maoxqhzjc.getUsercenter(),
							maoxqhzjc.getLingjbh(), loginuser,
							Const.JISMK_IL_CD);
				}
			}
		}

		erroList.clear();
		erroList = this.maoxqhzjcTmpService.checkWaibghms();
		if (erroList.size() > 0) {
			count = erroList.size();
			for (MaoxqhzjcTmp maoxqhzjc : erroList) {
				if (maoxqhzjc.getGongysdm() == null
						|| maoxqhzjc.getCangkdm() == null) {
					String[] paramStr = new String[] {
							maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(),
							"外部供货模式" };
					CommonFun.insertError(Const.YICHANG_LX2,
							Const.YICHANG_LX2_str49, yicbjList,
							maoxqhzjc.getJihyz(), paramStr,
							maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(),
							loginuser, Const.JISMK_IL_CD);
				} else {
					String[] paramStr = new String[] {
							maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(),
							maoxqhzjc.getGongysdm(), maoxqhzjc.getCangkdm(),
							"外部供货模式" };
					CommonFun.insertError(Const.YICHANG_LX2,
							Const.YICHANG_LX2_str13, yicbjList,
							maoxqhzjc.getJihyz(), paramStr,
							maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(),
							loginuser, Const.JISMK_IL_CD);
				}
			}
		}

		erroList.clear();
		erroList = this.maoxqhzjcTmpService.checkJihyz();
		if (erroList.size() > 0) {
			count = erroList.size();
			for (MaoxqhzjcTmp maoxqhzjc : erroList) {
				String[] paramStr = new String[] { maoxqhzjc.getUsercenter(),
						maoxqhzjc.getLingjbh(), "计划员代码" };
				CommonFun.insertError(Const.YICHANG_LX2,
						Const.YICHANG_LX2_str49, yicbjList,
						maoxqhzjc.getJihyz(), paramStr,
						maoxqhzjc.getUsercenter(), maoxqhzjc.getLingjbh(),
						loginuser, Const.JISMK_IL_CD);
			}
		}
		try {
			if (yicbjList.size() > 0) {
				this.yicbjservice.insertAll(yicbjList);
			}
			this.maoxqhzjcTmpService.clearErro();
		} catch (Exception e) {
			CommonFun.logger.error(e);
		}
		return count;
	}

	// ////////////////////////////////wuyichao 添加方法 2014/02/20/////////////
	/**
	 * @see 查询所有库存以线边仓库为基准
	 * @return
	 */
	public List<Ziykzb> queryAllKucByXianbck(String ziyhqrq) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ziyhqrq", ziyhqrq);
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryAllKucByXianbck", map);
	}

	/**
	 * @see 查询所有库存以订货仓库为基准
	 * @return
	 */
	public List<Ziykzb> queryAllKucByDinghck(String ziyhqrq) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ziyhqrq", ziyhqrq);
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ilorder.queryAllKucByDinghck", map);
	}

	/**
	 * @see 初始化库存map 和 计数map Key 以 usercenter,lingjbh,cangkdm value 为kucsl 和计数值
	 *      两个参数MAP必须为实际的map 不能为null
	 * @param kucMap
	 * @param countMap
	 * @param ziykzbs
	 */
	public void initKucMapAndCountMap(List<Ziykzb> ziykzbs,
			Map<String, BigDecimal> kucMap, Map<String, Integer> countMap) {
		if (null != ziykzbs && ziykzbs.size() > 0) {
			for (Ziykzb ziykzb : ziykzbs) {
				String key = ziykzb.getUsercenter() + ziykzb.getLingjbh()
						+ ziykzb.getCangkdm();
				if (kucMap.containsKey(key)) {
					countMap.put(key, countMap.get(key) + 1);
				} else {
					kucMap.put(key, ziykzb.getKucsl());
					countMap.put(key, 1);
				}
			}
		}
	}

	private void executeBatch(List list, String sqlId) {
		if (null != list && StringUtils.isNotBlank(sqlId)) {
			if (list.size() / 10000 > 1) {
				int i = 1;
				List flag = new ArrayList();
				for (Object object : list) {
					if (i > 10000) {
						baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch(sqlId, flag);
						flag = new ArrayList();
						i = 0;
					}
					flag.add(object);
					i++;
				}
				if (flag.size() > 0) {
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch(sqlId, flag);
				}
			} else {
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch(sqlId, list);
			}
		}
	}

	private void getMaxniqnzq(String nianzq, String usercenter,
			Map<String, String> map) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("usercenter", usercenter);
		param.put("nianzq", nianzq.substring(0, 4));
		CalendarCenter calendarCenter = calendarCenterService.maxTime(param);
		if (null != calendarCenter
				&& StringUtils.isNotBlank(calendarCenter.getNianzq()))
			map.put(usercenter, calendarCenter.getNianzq().substring(4));
	}

	private void getMaxniqnzx(String zhouqxh, String usercenter,
			Map<String, String> map) {
		Map<String, String> param = new HashMap<String, String>();
		map.put("usercenter", usercenter);
		map.put("nianzx", zhouqxh.substring(0, 4));
		String maxNianzx = calendarCenterService.getMaxNianzx(param);
		if (StringUtils.isNotBlank(maxNianzx))
			map.put(usercenter, maxNianzx);
	}

	// VJ订单拆分
	public void vJdingdjf(String dingdh, String usercenter) {
		Map<String, String> mapvjjf = new HashMap<String, String>();
		mapvjjf.put("usercenter", usercenter);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
				"hlorder.deleteVJJF", mapvjjf);

		//xss 20161011 v4_015
		Map map2 = new HashMap(); 
		map2.put("dingdh", dingdh);
		map2.put("usercenter", usercenter);

		//根据订单号 查询 订单明细的数据
		List<Dingdmx> list2 = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("hlorder.queryDindmx",map2);
		
		for (Dingdmx bean : list2) { 
			Map map_tqrq = new HashMap();  
			map_tqrq.put("usercenter", bean.getUsercenter()); 
			map_tqrq.put("dingdh",  bean.getDingdh()); 
			map_tqrq.put("gcbh", bean.getGcbh());
			map_tqrq.put("xiehzt", bean.getXiehzt());
			map_tqrq.put("shengcxbh", bean.getXiehzt().substring(0, 4)); // 取卸货站台前4位  
			map_tqrq.put("cangkdm", bean.getCangkdm());
			map_tqrq.put("lingjbh", bean.getLingjbh());
			map_tqrq.put("id", bean.getId());
			
			
			//查询提前天数			
			BigDecimal tiqiants  = (BigDecimal)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectObject("hlorder.queryTiqiants",bean);			
			
			if( tiqiants !=null  && tiqiants.compareTo(BigDecimal.ZERO) > 0 ){ //如果有外部物流的运输周期， 否则就不提前
				BigDecimal b_tqts = tiqiants.setScale(0, BigDecimal.ROUND_UP);
				
				int tqts = Integer.parseInt(String.valueOf(b_tqts));
				
				map_tqrq.put("tqts", tqts);
				CommonFun.logger.debug("需要提前："+tqts+"天！");
				
				String tqrq_jf = bean.getJiaofrq();//交付日期 
				map_tqrq.put("tqrq", tqrq_jf);
				
				//根据工作时间模板  和 提前天数  推算出  新的交付日期
				List<String> list_jiaofq = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("hlorder.queryTiqianrq",map_tqrq);
				if (!list_jiaofq.isEmpty()) {
					String n_jiaofrq =  list_jiaofq.get(tqts); //提前后的交付日期 
					map_tqrq.put("n_jiaofrq", n_jiaofrq);
					bean.setJiaofrq(n_jiaofrq);
					CommonFun.logger.debug("提前后的交付日期为："+n_jiaofrq);
				}

				String tqrq_fy = bean.getFayrq();//发运日期
				map_tqrq.put("tqrq", tqrq_fy);
				
				//根据工作时间模板  和 提前天数  推算出  新的发运日期
				List<String> list_fayrq = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).select("hlorder.queryTiqianrq",map_tqrq);
				if (!list_fayrq.isEmpty()) {
					String n_fayrq =  list_fayrq.get(tqts); //提前后的发运日期 
					map_tqrq.put("n_fayrq", n_fayrq);
					bean.setFayrq(n_fayrq); 
					CommonFun.logger.debug("提前后的发运日期为："+n_fayrq);
				}  
				
				if(map_tqrq.get("n_fayrq") !=null  && map_tqrq.get("n_jiaofrq") !=null){
					//更新订单明细中的发运日期和交付日期
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("hlorder.updateTiqianrq", map_tqrq);									
				}   								
			}	
		}
		
		
		//0012924
		//查找运输时刻 ，如果没有的话 最早到货时间：交付日期 + “00：00：01” 最晚到货时间：交付日期 + “23：59：59”  	
		for (Dingdmx bean_yssk : list2) { 
			String start  = " 00:00:01";
			String end =  " 23:59:59";
			
			List<Dingdmx> list_yssk = baseDao.getSdcDataSource( ConstantDbCode.DATASOURCE_XQJS).select("hlorder.queryVJ0Yussk", bean_yssk); 
			
			if(list_yssk.size() == 0){ 
				String zuizdhsj_yssk = bean_yssk.getJiaofrq().concat(start);
				String zuiwdhsj_yssk = bean_yssk.getJiaofrq().concat(end);
				 
				bean_yssk.setZuizdhsj(zuizdhsj_yssk);
				bean_yssk.setZuiwdhsj(zuiwdhsj_yssk);
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("hlorder.updateVjdhsj", bean_yssk); 
			 }
		}
		
		
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
				"hlorder.insertDingdmxvjjf", dingdh);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
				"hlorder.deleteVJjfmx", dingdh);
		List<Dingdmx> list = baseDao.getSdcDataSource(
				ConstantDbCode.DATASOURCE_XQJS).select("hlorder.queryVJjf",
				dingdh);
		for (Dingdmx bean : list) {
			Map map = new HashMap();
			map.put("usercenter", bean.getUsercenter());
			map.put("xiehztbh", bean.getXiehzt());
			map.put("shengcxbh", bean.getXiehzt().substring(0, 4)); // 取卸货站台前4位
			// 查询运输时刻的发运周期
			map.put("lingjbh", bean.getLingjbh());
			map.put("gongysbh", bean.getGongysdm());
			
			Xiehzt xiehzt = anxMaoxqService.queryXiehztObject(map);
			if (xiehzt != null) {
				BigDecimal yunxtqdhsj = xiehzt.getYunxtqdhsj();
				map.put("juedsk", bean.getJiaofrq());
				map.put("shijNum", CommonFun.strNull(yunxtqdhsj.intValue()));// 允许提前到货时间
				String zuizdhsj = CommonFun.strNull(anxMaoxqService
						.queryGongzsjmbQ(map));
				if (StringUtils.isBlank(zuizdhsj)) {
					zuizdhsj = bean.getJiaofrq();
				}
				
				bean.setZuizdhsj(zuizdhsj);
				bean.setZuiwdhsj(bean.getJiaofrq());
			}
			map.remove("shengcxbh");
			map.put("waibms", "VJ");
			map.put("mudd", bean.getCangkdm());
			map.put("xiehzt", bean.getXiehzt());
			map.put("gongysbh", bean.getGongysdm());
			List<Wullj> wulljxx = baseDao.getSdcDataSource(
					ConstantDbCode.DATASOURCE_XQJS).select("common.queryWullj",
					map);
			if (wulljxx.size() > 0) {
				Wullj wullj = wulljxx.get(0);
				BigDecimal yunszq = (wullj.getYunszq()).multiply(BigDecimal
						.valueOf(24 * 60));
				String faysj = DateUtil.DateSubtractMinutes(bean.getJiaofrq(),
						yunszq.intValue());
				bean.setFayrq(faysj);
			}
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
					"hlorder.updateVjjf", bean);
		}
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
				"hlorder.insertDingdmxfromvjjf", dingdh);
		
		
	}

	/*
	 * 是否按盈余计算 true 是 false 否
	 */
	private boolean sfayujs(
			Map<String, Map<String, VjKuczh>> kuczhUsercenterMap,
			MaoxqhzjcTmp maoxqhzjc) {
		Map<String, VjKuczh> kuczhmap = kuczhUsercenterMap.get(maoxqhzjc
				.getUsercenter());// 用户中心的全部库存置换参考记录
		String key = kuczhCreateKey(maoxqhzjc.getUsercenter(),
				maoxqhzjc.getCangkdm());
		VjKuczh vjkuczh = kuczhmap.get(key);
		if (vjkuczh != null) {
			if ("1".equals(vjkuczh.getIskaolckjs())) {
				return false;
			} else {
				return true;
			}
		}
		return true;
	}

	/**
	 * 更新盈余
	 * 
	 */
	private void updateYingy(String usercenter, String lingjbh, String mudd,
			BigDecimal yingy, String jkCode) {
		Map<String, Object> map = new HashMap<String, Object>();// 参数map
		map.put("usercenter", usercenter);
		map.put("lingjbh", lingjbh);
		map.put("mudd", mudd);
		map.put("leix", "1");// 仓库
		map.put("yingy", yingy);
		map.put("editor", jkCode);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
				"hlorder.updateXianbkcByParam", map);
	}

	/*
	 * 获取线边库存
	 * 
	 * @param userCenter 用户中心
	 * 
	 * @param lingjbh 零件编号
	 * 
	 * @param shengcxbh 生产线编号
	 * 
	 * @param mudd 仓库编号
	 * 
	 * @param leix 类型1仓库 2消耗点 固定为1
	 * 
	 * @return danw 零件的单位
	 */
	private VjXianbkc queryXianbkc(String userCenter, String lingjbh,
			String shengcxbh, String mudd, String leix, String danw) {
		Map<String, String> map = new HashMap<String, String>();// 参数map
		map.put("usercenter", userCenter);
		map.put("lingjbh", lingjbh);
		map.put("mudd", mudd);
		map.put("leix", leix);
		// logger.info("线边仓库："+userCenter+"-"+lingjbh+"-"+shengcxbh+"-"+mudd);
		VjXianbkc xianbkc = (VjXianbkc) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject(
				"hlorder.queryXianbkcByParam", map);
		if (xianbkc == null) {
			xianbkc = new VjXianbkc();
			xianbkc.setUsercenter(userCenter);
			xianbkc.setChanx(shengcxbh);
			xianbkc.setLingjbh(lingjbh);
			xianbkc.setMudd(mudd);// 仓库编码
			xianbkc.setLeix(leix); // 1仓库 2消耗点
			xianbkc.setDanw(danw); // 零件的单位
			xianbkc.setYingy(BigDecimal.ZERO);
			xianbkc.setPandcy(BigDecimal.ZERO);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute(
					"hlorder.insertXianbkc", xianbkc);
		}

		return xianbkc;
	}

	/*
	 * kuczhUsercenterMap 格式为： key = 用户中心 值kuczhmap kuczhmap 格式为key 用户中心+库存号
	 * 值库存置换记录 针对用户中心，每一个用户中心都需要查询，不能查询结果是否有，都要存入一个map，以免多次查询
	 */
	private Map<String, Map<String, VjKuczh>> findKuczh(String usercenter,
			Map<String, Map<String, VjKuczh>> kuczhUsercenterMap) {
		Map<String, VjKuczh> kuczhmap = kuczhUsercenterMap.get(usercenter);
		if (kuczhmap == null) {// 没有查询过为null 如果查询过且没有值为空map
			// 库存置换集合
			Map<String, String> map = new HashMap<String, String>();
			map.put("usercenter", usercenter);// 用户中心
			List<VjKuczh> kuczhList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select(
					"hlorder.queryKuczhByParam", map);
			if (kuczhList != null) {
				kuczhmap = new HashMap<String, VjKuczh>();
				for (int i = 0; i < kuczhList.size(); i++) {
					VjKuczh kuczh = kuczhList.get(i);
					kuczhmap.put(
							kuczhCreateKey(kuczh.getUsercenter(),
									kuczh.getCangk()), kuczh);
				}
			} else {
				kuczhmap = new HashMap<String, VjKuczh>(0);
			}
			kuczhUsercenterMap.put(usercenter, kuczhmap);

		}
		return kuczhUsercenterMap;
	}

	/*
	 * 按用户中心+库存号产生mapkey
	 */
	private String kuczhCreateKey(String usercenter, String cangk) {
		return usercenter + cangk;
	}

	/**
	 * @方法： 检查消耗比例：根据订单类型获取执行的sql语句
	 * @参数：订单类型和计划员组
	 * */
	public int checkXiaohbl(String dingdlx, String usercneter) {
		long start = System.currentTimeMillis();
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单周期订单计算",
				"产线消耗比例校验开始");
		this.vjWulljService.checkVJXhbl(dingdlx, usercneter);
		CommonFun.logger.debug("VJ模式产线消耗比例校验语句为：hlorder.checkXiaohblVJ");
		Map<String, String> map = new HashMap<String, String>();
		map.put("usercenter", usercneter);
		ArrayList<Xiaohblzjb> errorList = (ArrayList<Xiaohblzjb>) baseDao
				.select("hlorder.checkXiaohblVJ", map);
		if (errorList.size() > 0) {
			for (Xiaohblzjb xiaohblzjb : errorList) {
				LoginUser loginUser = AuthorityUtils.getSecurityUser();
				Map<String, String> param = new HashMap<String, String>();
				param.put("usercenter", xiaohblzjb.getUsercenter());
				param.put("lingjbh", xiaohblzjb.getLingjbh());
				param.put("shengcxbh", xiaohblzjb.getShengcxbh());
				List<Lingjxhd> xhdList = this.lingjxhdservice
						.queryShengcxByParam(param);
				try {
					if (null != xhdList && xhdList.size() > 0) {
						for (Lingjxhd lingjxhd : xhdList) {
							param.clear();
							param.put("usercenter", lingjxhd.getUsercenter());
							param.put("lingjbh", lingjxhd.getLingjbh());
							param.put("shengcxbh", lingjxhd.getShengcxbh());
							param.put("fenpqh", lingjxhd.getFenpqbh());
							param.put("gongysbh", lingjxhd.getGongysbh());
							Wullj wj = this.vjWulljService
									.queryWulljObject(param);
							String paramStr[] = new String[] {
									lingjxhd.getUsercenter(),
									lingjxhd.getLingjbh(),
									lingjxhd.getShengcxbh(),
									lingjxhd.getFenpqbh() };

							this.yicbjservice.insertError(Const.YICHANG_LX6,
									Const.YICHANG_LX6_str2, wj.getWulgyyz(),
									paramStr, lingjxhd.getUsercenter(),
									lingjxhd.getLingjbh(), loginUser,
									Const.JISMK_IL_CD);
						}
					} else {
						this.yicbjservice.saveYicbj(xiaohblzjb.getLingjbh(),
								xiaohblzjb.getUsercenter(), "用户中心:"
										+ xiaohblzjb.getUsercenter()
										+ "下零件编号为:" + xiaohblzjb.getLingjbh()
										+ "在产线:" + xiaohblzjb.getShengcxbh()
										+ "上没有消耗点.", loginUser.getUsername(),
								Const.JISMK_IL_CD, Const.YICHANG_LX6,
								loginUser.getUsername());
					}
				} catch (Exception e) {
					CommonFun.logger.error(e);
				}
				Map<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("usercenter", xiaohblzjb.getUsercenter());
				paramMap.put("lingjbh", xiaohblzjb.getLingjbh());
				paramMap.put("chanx", xiaohblzjb.getShengcxbh());
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS)
						.execute("hlorder.deleteOneOfMaoxqhzjTmp", paramMap);
			}
		}

		long end = System.currentTimeMillis();
		CommonFun.logger.info("----------------------------IL计算,消耗比例校验时间"
				+ (end - start) / 1000);
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "Il订单周期订单计算",
				"产线消耗比例校验结束");
		return errorList.size();
	}

	// 根据偏移量获取封闭期日期
	private String getFengbq(int px, MaoxqhzjcTmp maoxqhzjc) {
		String method = "";// 拼接getPi方法
		if (px == 0) {
			method = "getJ" + px + "rq";
			CommonFun.logger
					.debug("国产件订单计算VJ模式既定计算jiDingCalculate方法在（）时method="
							+ method);
		} else {
			method = "getJ" + px + "riq";
			CommonFun.logger
					.debug("国产件订单计算VJ模式既定计算jiDingCalculate方法不在（）时method="
							+ method);
		}

		String pxriq = null;
		Class cls = maoxqhzjc.getClass();// 得到Maoxqhzpc类
		Method meth;
		try {
			meth = cls.getMethod(method, new Class[] {});
		} catch (SecurityException e) {
			CommonFun.logger.error(e);
			throw new RuntimeException(
					"IlOrderService.jiDingCalculate,SecurityException");
		} catch (NoSuchMethodException e) {
			CommonFun.logger.error(e);
			throw new RuntimeException(
					"IlOrderService.jiDingCalculate,NoSuchMethodException");
		}// 得到Maoxqhzpc类的method拼接的方法
		Object obj;
		try {
			obj = meth.invoke(maoxqhzjc, null);
		} catch (IllegalArgumentException e) {
			CommonFun.logger.error(e);
			throw new RuntimeException(
					"IlOrderService.jiDingCalculate,IllegalArgumentException");
		} catch (IllegalAccessException e) {
			CommonFun.logger.error(e);
			throw new RuntimeException(
					"IlOrderService.jiDingCalculate,IllegalAccessException");
		} catch (InvocationTargetException e) {
			CommonFun.logger.error(e);
			throw new RuntimeException(
					"IlOrderService.jiDingCalculate,InvocationTargetException");
		}

		if (null == obj) {
			CommonFun.logger.debug("国产件订单计算VJ模式既定计算jiDingCalculate方法P0fyzqxh="
					+ null);
		} else {
			try {
				pxriq = meth.invoke(maoxqhzjc, null).toString();
			} catch (IllegalArgumentException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException(
						"IlOrderService.jiDingCalculate,IllegalArgumentException");
			} catch (IllegalAccessException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException(
						"IlOrderService.jiDingCalculate,IllegalAccessException");
			} catch (InvocationTargetException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException(
						"IlOrderService.jiDingCalculate,InvocationTargetException");
			}
		}
		return pxriq;
	}

	// 获取供应商最大偏移量
	private int getGongysMaxPx(String lingjbh , String usercenter) {
		int maxPx = 0;
		Map<String, String> param = new HashMap<String, String>();
		// 从xqjs_dingd表里取出对应零件的最大fayrq
		param.put("lingjbh",lingjbh);
		
		//用户中心 - xss-20160422
		param.put("usercenter",usercenter);
		
		Object obj = baseDao.getSdcDataSource(
				ConstantDbCode.DATASOURCE_XQJS).selectObject(
						"hlorder.getMaxPx", param);
		 if ( null!=obj && !"".equals(obj))
		 {
			 maxPx = ((BigDecimal)obj).divide(BigDecimal.ONE, 0, BigDecimal.ROUND_UP).intValue();
		 }
		return maxPx;
	}

	// 获取封闭期的偏移时间
	public int getPianysj(MaoxqhzjcTmp maoxqhzjc, int maxPx) {
		int pianysj = 0;
		// 查询map
		Map<String, String> param = new HashMap<String, String>();
		// 从xqjs_dingd表里取出对应零件的最大fayrq
		param.put("usercenter", maoxqhzjc.getUsercenter());
		param.put("lingjbh", maoxqhzjc.getLingjbh());
		param.put("gonghlx", maoxqhzjc.getWaibghms());
		String yaohqsrq = (String) baseDao.getSdcDataSource(
				ConstantDbCode.DATASOURCE_XQJS).selectObject(
				"hlorder.getMaxYaohqsrq", param);
		// 要货起始日期为空
		if (yaohqsrq == null || "".equals(yaohqsrq)) {
			return maxPx;
		}

		Date yaohqsrqDate = null;
		try {
			yaohqsrqDate = CommonFun.sdfAxparse(yaohqsrq);
		} catch (ParseException e2) {
			e2.printStackTrace();
		}
		Date J0Date = null;
		try {
			J0Date = CommonFun.sdfAxparse(maoxqhzjc.getJ0rq());
		} catch (ParseException e2) {
			e2.printStackTrace();
		}
		
		//xss-20161103 如果最大要货起始日期小于资源获取日期(J0日期)，则更新要货起始日期
		if(yaohqsrqDate.before(J0Date)){
			yaohqsrqDate = J0Date;
		}
	

		// 按正常流程
		if (yaohqsrqDate.before(J0Date)) {
			pianysj = maxPx;
		} else {
			String fengbq = this.getFengbq(maxPx, maoxqhzjc);
			Date fengbqDate = null;
			try {
				fengbqDate = CommonFun.sdfAxparse(fengbq);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			// 封闭期大于fayrqDate,fayrqDate下一天作为封闭期。
			if (yaohqsrqDate.before(fengbqDate)) {
				for (int k = 0; k < Const.ZJCHANGDU; k++) {
					try {
						// 依次查找，找出fayrq对应的pianysj
						fengbqDate = CommonFun.sdfAxparse(this.getFengbq(k,
								maoxqhzjc));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					// 找到对应的日期
					if (fengbqDate.getTime() == yaohqsrqDate.getTime()) {
						pianysj = k + 1;
						break;
					}
					if (fengbqDate.getTime() > yaohqsrqDate.getTime()) {
						pianysj = k;
						break;
					}
				}
			} else {// 退出不做计算
				return -1;
			}
		}
		return pianysj;
	}

	/**
	 * 得到零件供应商汇总
	 */
	private List<LingjGongys> queryGongyfe(String lingjbh, String usercenter) {
		// 按用户中心查询全部的供应商
		Map<String, String> map = new HashMap<String, String>();
		Map<String, List<LingjGongys>> lingjgysMap = new HashMap<String, List<LingjGongys>>();
		map.put("lingjbh", lingjbh);
		map.put("usercenter", usercenter); //xss-20160811
		List<LingjGongys> lingjgysList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select(
				"hlorder.queryGongyshz", map);
		return lingjgysList;
	}

	/*
	 * 汇总供应商累计份额
	 */
	private Map<String, List<LingjGysfexz>> chongxjsgysfe(String usercenter,
			List<String> lingjlst) {
		Map<String, List<LingjGysfexz>> result = new HashMap<String, List<LingjGysfexz>>(); // 零件、供应商份额
		if (lingjlst != null && lingjlst.size() > 0) {
			Map<String, String> paraMap = new HashMap<String, String>();
			for (String lingjbh : lingjlst) {// 计算全部零件对应的供应商份额
				paraMap.clear();
				paraMap.put("usercenter", usercenter);
				paraMap.put("lingjbh", lingjbh);
				// 零件供应商汇总
				List<LingjGongys> lingjgyshz = queryGongyfe(lingjbh,usercenter);//xss-20160811

				String key = usercenter + lingjbh;
				// 仓库零件累计量
				BigDecimal ckLingjljsl = this.getBigDecimalAx(baseDao
						.selectObject("xinaxjis.queryLingjljOfCKLingjgys",
								paraMap));
				if (ckLingjljsl != null
						&& ckLingjljsl.compareTo(BigDecimal.ZERO) != 0) {// 总量不为0
					List<LingjGysfexz> gongysfexz = new ArrayList<LingjGysfexz>();// 供应商份额校正
					for (LingjGongys lingjGongys : lingjgyshz) {// 循环零件供应商
						paraMap.put("gongysbh", lingjGongys.getGongysbh());
						// 零件供应商累计
						BigDecimal biaodsl = this.getBigDecimalAx(baseDao
								.selectObject(
										"xinaxjis.queryBiaodslOfCKLingjgys",
										paraMap));
						// 计算该供应商本次实际供应 份额
						LingjGysfexz temfe = new LingjGysfexz();
						temfe.setGongyfe(lingjGongys.getGongyfe());
						temfe.setGongyljfe(biaodsl.divide(ckLingjljsl, 2,
								BigDecimal.ROUND_CEILING));
						temfe.setGongysbh(lingjGongys.getGongysbh());
						gongysfexz.add(temfe);
					}
					// 校准份额
					gongysfexz = xiaozfe(gongysfexz);
					result.put(lingjbh, gongysfexz);
				}

			}
		}
		return result;
	}

	/*
	 * 将多于合同供货的比例，分配到实际缺少的供应商上
	 */
	private List<LingjGysfexz> xiaozfe(List<LingjGysfexz> gongysfexz) {

		if (gongysfexz != null && gongysfexz.size() > 0) {
			// 累计实际供货比合同供货多出来的份额
			BigDecimal ljfe = BigDecimal.ZERO; // 初始零
			for (LingjGysfexz fe : gongysfexz) {
				BigDecimal off = fe.getGongyljfe().subtract(fe.getGongyfe());
				if (off.compareTo(BigDecimal.ZERO) > 0) {// 说明实际供货量〉合同比例
					ljfe = ljfe.add(off);
					fe.setBencgongyfe(fe.getGongyfe().subtract(off));
				} else {
					fe.setBencgongyfe(fe.getGongyfe());// 初始化
				}

			}
			// 校准
			for (LingjGysfexz fe : gongysfexz) {
				BigDecimal off = fe.getGongyljfe().subtract(fe.getGongyfe());
				if (off.compareTo(BigDecimal.ZERO) <= 0) {// 说明实际供货量〉合同比例
					// 小于 或等于
					if (ljfe.compareTo(BigDecimal.ZERO) > 0) {// 有可以校准的比例
						BigDecimal addoff = off.negate();
						if (addoff.compareTo(ljfe) <= 0) {
							fe.setBencgongyfe(fe.getGongyfe().add(addoff));// 增加缺少的比例
							ljfe = ljfe.subtract(addoff);
						} else {
							fe.setBencgongyfe(fe.getGongyfe().add(ljfe));// 增加缺少的比例
							ljfe = BigDecimal.ZERO; // 初始零
							break;
						}

					} else {// 没有校准的比例了
						break;
					}
				}
			}
			// 最后校准
			if (ljfe.compareTo(BigDecimal.ZERO) > 0) {// 还有可以校准的比例
				for (LingjGysfexz fe : gongysfexz) {
					BigDecimal off = fe.getBencgongyfe().subtract(
							fe.getGongyfe());
					if (off.compareTo(BigDecimal.ZERO) <= 0) {// 说明本次供货量<合同比例
						// 小于 或等于
						if (ljfe.compareTo(BigDecimal.ZERO) > 0) {// 有可以校准的比例
							BigDecimal addoff = off.negate();
							if (addoff.compareTo(ljfe) <= 0) {
								/*
								 * fe.setBencgongyfe(fe.getBencgongyfe().add(
								 * addoff));// 增加缺少的比例
								 */ljfe = ljfe.subtract(addoff);
								fe.setBencgongyfe(ljfe);
							} else {
								fe.setBencgongyfe(fe.getBencgongyfe().add(ljfe));// 增加缺少的比例
								ljfe = BigDecimal.ZERO; // 初始零
								break;
							}
						} else {// 没有校准的比例了
							break;
						}
					}
				}
			}
		}
		return gongysfexz;

	}

	private BigDecimal getBigDecimalAx(Object obj) {
		if (obj != null) {
			return new BigDecimal(this.strNullAx(obj));
		} else {
			return BigDecimal.ZERO;
		}
	}

	private String strNullAx(Object obj) {
		if (obj != null) {
			return obj.toString();
		} else {
			return "";
		}
	}

	/**
	 * 获取零件 的异常消耗
	 * 
	 * @param param
	 * @return
	 */
	public BigDecimal getYcxh(Map<String, String> param) {
		BigDecimal yicxh = (BigDecimal) baseDao.getSdcDataSource(
				ConstantDbCode.DATASOURCE_XQJS).selectObject(
				"hlorder.queryYicxhl", param);
		return yicxh;
	}

	/**
	 * 获取待消耗
	 * 
	 * @param param
	 * @return
	 */
	public BigDecimal getDaixh(MaoxqhzjcTmp maoxqhzjc, int pianysj) {
		BigDecimal daixh = BigDecimal.ZERO;
		for (int x = 0; x < pianysj; x++) {
			Method meth = null;
			try {
				if (x <= 15) {
					String method = Const.GETJ + x;// 拼接getPi方法
					Class cls = maoxqhzjc.getClass();// 得到Maoxqhzpc类

					meth = cls.getMethod(method, new Class[] {});
				}
			} catch (SecurityException e1) {
				CommonFun.logger.error(e1);
				throw new RuntimeException(
						"IlOrderService.pjMaoxqHz,SecurityException");
			} catch (NoSuchMethodException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException(
						"IlOrderService.pjMaoxqHz,NoSuchMethodException");
			}// 得到Maoxqhzpc类的method拼接的方法
			try {
				if (x > 15) {
					daixh = daixh.add(BigDecimal.ZERO);
				} else {
					daixh = daixh.add(new BigDecimal(meth.invoke(maoxqhzjc,
							null).toString()));
					CommonFun.logger.info("待消耗量：" + daixh);
				}

			} catch (IllegalArgumentException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException(
						"IlOrderService.pjMaoxqHz,IllegalArgumentException");
			} catch (IllegalAccessException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException(
						"IlOrderService.pjMaoxqHz,IllegalAccessException");
			} catch (InvocationTargetException e) {
				CommonFun.logger.error(e);
				throw new RuntimeException(
						"IlOrderService.pjMaoxqHz,InvocationTargetException");
			}// 执行得到的方法，取得既定周期的数量
		}
		CommonFun.logger.debug("VJ模式毛需求-参考系汇总待消耗为：" + daixh);
		return daixh;
	}
}
