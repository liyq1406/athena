package com.athena.xqjs.module.hlorder.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.beanutils.BeanUtils;

import com.athena.authority.entity.LoginUser;
import com.athena.component.service.BaseService;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.xqjs.entity.common.CalendarGroup;
import com.athena.xqjs.entity.common.CalendarVersion;
import com.athena.xqjs.entity.common.Gongyzq;
import com.athena.xqjs.entity.common.Jiaofrl;
import com.athena.xqjs.entity.ilorder.Dingdlj;
import com.athena.xqjs.entity.ilorder.Dingdmx;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.common.service.CalendarCenterService;
import com.athena.xqjs.module.common.service.CalendarGroupService;
import com.athena.xqjs.module.common.service.CalendarVersionService;
import com.athena.xqjs.module.common.service.JiaofrlService;
import com.athena.xqjs.module.ilorder.service.GongyzqService;
import com.athena.xqjs.module.ycbj.service.YicbjService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

/**
 * @Title:计算交付日交付数量类
 * @Description:计算交付日交付数量类
 * @Copyright: Copyright (c) 2011
 * @Company: 软通动力
 * @author 李明
 * @version v1.0
 * @date 2011-12-20
 */
@Component
public class VjDayOrderCountService extends BaseService {

	@Inject
	private JiaofrlService jiaofrlService;// 注入交付日历service

	@Inject
	private CalendarGroupService calendarGroupService; // -日历版次-工作时间编组service

	@Inject
	private CalendarVersionService calendarVersionService;// 注入日历版次service

	@Inject
	private GongyzqService gongyzqService;// 注入工业周期service

	@Inject
	private VjDingdljService vjDingdljService;// 注入订单零件service
	@Inject
	private UserOperLog userOperLog;
	@Inject
	private YicbjService yicbjService;// 异常报警service
	@Inject
	private CalendarCenterService calendarcenterservice;

	/**
	 * 以周期类型为key，value为对应的值
	 * **/
	private String getGhlx(String partten) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Const.PP, Const.PM);
		map.put(Const.PJ, Const.JM);
		map.put(Const.PS, Const.SM);
		map.put(Const.VJ, Const.VJ);
		map.put(Const.MJ, Const.MJ);
		return map.get(partten.toUpperCase());
	}

	/**
	 * @方法名： 计算交付日交付数量
	 * @author 李明
	 * @version v1.0
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @date 2011-12-20
	 * @参数说明：订单零件实体
	 */
	public void countDayOrder(Dingdlj bean, String partten, String lingjmc,
			String gongsmc, String neibyhzx, String caozz, String jihyz,
			LoginUser loginUser, String gcbh,
			Map<String, List<Dingdmx>> saveMingxMap) {
		userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "il订单计算", "计算交付日交付数量开始");
		// 异常报警计算模块
		String jismk = Const.JISMK_IL_CD;
		// 异常报警错误类型
		String cuowlx = Const.CUOWLX_200;
		// 异常报警信息
		String info = "";
		// 实例化订单明细
		Dingdmx dingdmx = new Dingdmx();
		// 定义订单行要货量
		BigDecimal dingdLineCount = BigDecimal.ZERO;
		if (null != bean && null != bean.getDingdh()) {
			// 将订单零件中的相关属性设置到订单明细中
			dingdmx.setUabzlx(bean.getUabzlx());
			dingdmx.setUabzuclx(bean.getUabzuclx());
			dingdmx.setUabzucsl(bean.getUabzucsl());
			dingdmx.setUabzucrl(bean.getUabzucrl());
			dingdmx.setDingdh(bean.getDingdh());
			dingdmx.setUsercenter(bean.getUsercenter());
			dingdmx.setLingjbh(bean.getLingjbh());
			dingdmx.setGongysdm(bean.getGongysdm());
			dingdmx.setGongyslx(bean.getGongyslx());
			dingdmx.setCangkdm(bean.getCangkdm());
			dingdmx.setFahd(bean.getFahd());
			dingdmx.setDinghcj(bean.getDinghcj());
			dingdmx.setDanw(bean.getDanw());
			dingdmx.setLujdm(bean.getLujdm());
			dingdmx.setJihyz(bean.getJihyz());
			dingdmx.setYijfl(bean.getYijfl());
			dingdmx.setLeix(Const.SHIFOUSHIJIDING);
			dingdmx.setGonghlx(this.getGhlx(partten));
			dingdmx.setZhuangt(Const.DINGD_STATUS_DSX);
			dingdmx.setActive(Const.ACTIVE_1);
			dingdmx.setLingjmc(lingjmc);
			dingdmx.setGongsmc(gongsmc);
			dingdmx.setNeibyhzx(neibyhzx);
			dingdmx.setCreator(caozz);
			dingdmx.setCreate_time(CommonFun.getJavaTime());
			dingdmx.setEditor(caozz);
			dingdmx.setEdit_time(CommonFun.getJavaTime());
			dingdmx.setXiehzt(bean.getXiehzt());
			dingdmx.setGcbh(gcbh);
			// 获得既定的个数
			int count = this.vjDingdljService.getDingdnr(bean.getDingdnr());
			CommonFun.logger.debug(" 计算交付日交付数量中既定个数：count=" + count);

			BigDecimal faysj = new BigDecimal(bean.getFayzq()
					.divide(BigDecimal.ONE, 0, BigDecimal.ROUND_UP).intValue());
			// 定义年周期
			String zhouqi = "";
			CommonFun.logger.debug(" 计算交付日交付数量中查询日历版次参数：用户中心="
					+ bean.getUsercenter() + "仓库代码=" + bean.getCangkdm());
			// 查询出日历版次
			CalendarGroup group = this.calendarGroupService
					.queryCalendarGroupObject(bean.getUsercenter(),
							bean.getCangkdm());
			CommonFun.objPrint(group, "计算交付日交付数量中日历版次group");
			// 定义交付日历中的结果集
			List<Jiaofrl> list = new ArrayList<Jiaofrl>();
			// 循环既定周期
			for (int j = 0; j < count; j++) {
				// 通过P0周期推算出其他的周期，解决跨年问题
				zhouqi = CommonFun
						.addNianzq(bean.getP0fyzqxh(), Const.MAXZQ, j);
				CommonFun.logger.debug(" 计算交付日交付数量中第" + j + "次循环时年周期：zhouqi="
						+ zhouqi);
				// 得到对应周期内的交付日期集合
				list = this.jiaofrlService.queryAllJiaofm(bean.getUsercenter(),
						bean.getJiaofm(), zhouqi);
				CommonFun.objListPrint(list, "得到对应周期内的交付日期集合list");
				// 得到集合长度
				int size = list.size();
				// 获取到了工业周期第一天的日期
				String fristDay = size > 0 ? list.get(0).getRi()
						.substring(0, 10) : null;
				CommonFun.logger.debug(" 计算交付日交付数量中第" + j
						+ "次循环时起始日期：fristDay=" + fristDay);
				// 获取到了工业周期最后一天的日期
				String endDay = size > 0 ? list.get(size - 1).getRi()
						.substring(0, 10) : null;
				CommonFun.logger.debug(" 计算交付日交付数量中第" + j + "次循环时结束日期：endDay="
						+ endDay);
				if (null != group) {
					// 循环交付日期集合，得到真正的交付日
					Map<Integer, String> mapJiaofr = jisWorkDay(list, group,
							size, fristDay, endDay);
					CommonFun.mapPrint(mapJiaofr, "循环交付日期集合，得到真正的交付日mapJiaofr");
					// ==============订单行计算---start
					// 定义存放订单行数量的Map
					Map<Integer, BigDecimal> dingdLineMap = new TreeMap<Integer, BigDecimal>();
					// 获取到去掉重复值之后的map集合
					Map<Integer, String> jiaofrSingleMap = CommonFun
							.clearRepeatValue(mapJiaofr);
					CommonFun.mapPrint(jiaofrSingleMap,
							"获取到去掉重复值之后的jiaofrSingleMap集合");
					// 获取订单量（即UA量）
					BigDecimal uaCount = BigDecimal.ZERO;
					// 定义P的数量
					BigDecimal pisl = BigDecimal.ZERO;
					// 反射获取到类
					Class cls = null;
					try {
						cls = Class.forName("com.athena.xqjs.entity.ilorder.Dingdlj");
					} catch (ClassNotFoundException e) {
						CommonFun.logger.error(e);
						throw new RuntimeException("DayOrderCountService.countDayOrder,ClassNotFoundException");
					}
					// 定义动态的方法值
					String sl = null;
					// 当循环的既定的时候，清空订单行数量的map
					dingdLineMap.clear();
					// 动态获取
					sl = Const.GETP + j + "sl";
					CommonFun.logger.debug(" 计算交付日交付数量中第" + j+ "次循环时dingdlj动态获取方法：sl=" + sl);
					// 获取到方法
					Method meth = null;
					try {
						meth = cls.getMethod(sl, new Class[] {});
					} catch (SecurityException e) {
						CommonFun.logger.error(e);
						throw new RuntimeException(
								"DayOrderCountService.countDayOrder,SecurityException");
					} catch (NoSuchMethodException e) {
						CommonFun.logger.error(e);
						throw new RuntimeException(
								"DayOrderCountService.countDayOrder,NoSuchMethodException");
					}
					// 调用meth方法,将其转换成对应的数据类型
					try {
						pisl = new BigDecimal(meth
								.invoke(bean, new Object[] {}).toString());
					} catch (IllegalArgumentException e) {
						CommonFun.logger.error(e);
						throw new RuntimeException(
								"DayOrderCountService.countDayOrder,IllegalArgumentException");
					} catch (IllegalAccessException e) {
						CommonFun.logger.error(e);
						throw new RuntimeException(
								"DayOrderCountService.countDayOrder,IllegalAccessException");
					} catch (InvocationTargetException e) {
						CommonFun.logger.error(e);
						throw new RuntimeException(
								"DayOrderCountService.countDayOrder,InvocationTargetException");
					}
					CommonFun.logger.debug(" 计算交付日交付数量中第" + j
							+ "次循环时pisl：pisl=" + pisl);
					// 获取订单量（即UA量）
					CommonFun.logger
							.debug(" 计算交付日交付数量中第"
									+ j
									+ "次循环时包装容量：bean.getUabzucsl().multiply(bean.getUabzucrl()="
									+ bean.getUabzucsl().multiply(
											bean.getUabzucrl()));

					uaCount = pisl.divide(
							bean.getUabzucsl().multiply(bean.getUabzucrl()),
							BigDecimal.ROUND_UP);
					CommonFun.logger.debug(" 计算交付日交付数量中第" + j
							+ "次循环时获取订单量（即UA量）：uaCount=" + uaCount);
					// 设置临时数量
					BigDecimal tempCount = BigDecimal.ZERO;
					// 获取到既定周期生产日天数
					BigDecimal dayCount = new BigDecimal(calendarVersionService
							.queryCountGzr(bean.getUsercenter(),
									group.getRilbc(), zhouqi, Const.GZR_Y)
							.size());
					CommonFun.logger.debug(" 计算交付日交付数量中第" + j
							+ "次循环时周期生产日天数：dayCount=" + dayCount);
					// 判断处理后map不为null
					if (!jiaofrSingleMap.isEmpty() && dayCount.intValue() != 0) {
						// 取得交付次数，判断交付次数的值，若值为1，计算单行的数量=订单量
						if (jiaofrSingleMap.size() == 1) {
							// 当交付次数为1的时候，订单行的数量就是ua的量
							dingdLineCount = uaCount;
							CommonFun.logger.debug(" 计算交付日交付数量中第" + j
									+ "次循环时订单行的数量：dingdLineCount="
									+ dingdLineCount);
							// 将得到的数量设置到订单明细实体中去
							dingdmx.setShul(dingdLineCount.multiply(bean
									.getUabzucsl().multiply(bean.getUabzucrl())));
							// 将得到的订单行数量设置到实体中去；
							// dingdmx.setShul(dingdLineCount);
							dingdmx.setJissl(dingdLineCount.multiply(bean
									.getUabzucsl().multiply(bean.getUabzucrl())));
							// 根据年周期查找开始，结束时间
							Gongyzq gongyzq = this.gongyzqService
									.queryGongyzq(zhouqi);
							if (null != gongyzq) {
								// 设置要货开始时间
								dingdmx.setYaohqsrq(gongyzq.getKaissj());
								CommonFun.logger.debug(" 计算交付日交付数量中第" + j
										+ "次循环时要货开始时间：=" + gongyzq.getKaissj());
								// 设置要货结束时间
								dingdmx.setYaohjsrq(gongyzq.getJiessj());
								CommonFun.logger.debug(" 计算交付日交付数量中第" + j
										+ "次循环时要货结束时间：=" + gongyzq.getJiessj());
								dingdmx.setJiaofrq(jiaofrSingleMap.get(0));
								CommonFun.logger
										.debug(" 计算交付日交付数量中第" + j
												+ "次循环时交付日期：="
												+ jiaofrSingleMap.get(0));

								List riqList = this.calendarcenterservice
										.getSubRq(jiaofrSingleMap.get(0),
												bean.getUsercenter(),
												faysj.toString());
								if (riqList.size() > 0) {
									dingdmx.setFayrq(riqList.get(
											riqList.size() - 1).toString());
								} else {
									dingdmx.setFayrq(null);
								}
								// ////////////////////////////wuyichao//////////////////////////////////
								String key = dingdmx.getDingdh();
								List<Dingdmx> flagmx = saveMingxMap.get(key);
								if (flagmx == null) {
									flagmx = new ArrayList<Dingdmx>();
								}
								try {
									flagmx.add((Dingdmx) BeanUtils
											.cloneBean(dingdmx));
								} catch (IllegalAccessException e) {
									e.printStackTrace();
								} catch (InstantiationException e) {
									e.printStackTrace();
								} catch (InvocationTargetException e) {
									e.printStackTrace();
								} catch (NoSuchMethodException e) {
									e.printStackTrace();
								}
								saveMingxMap.put(key, flagmx);
								// ////////////////////////////wuyichao//////////////////////////////////

								CommonFun.logger.debug(" 计算交付日交付数量中第" + j
										+ "次循环时发运日期：=" + dingdmx.getFayrq());
							}
							// 执行插入操作,插入到订单明细表中去
						} else if (jiaofrSingleMap.size() > 1) {// 判断交付次数的值，若值不为1的时候，计算订单行数量
							for (int k = 0; k < jiaofrSingleMap.size(); k++) {
								// 若是第一个交付日
								if (k == 0) {

									// 获取到第2个交付日之间所有的工作日期
									List<CalendarVersion> gongzrList = this.calendarVersionService
											.queryCalendarVersionGzr(bean
													.getUsercenter(), group
													.getRilbc(), zhouqi,
													jiaofrSingleMap.get(k + 1)
															.toString(),
													Const.GZR_Y);
									CommonFun.objListPrint(gongzrList,
											" 计算交付日交付数量中第" + j
													+ "次循环且K=0时所有的工作日期list");
									// 当得到的天数为空的时候，订单行数量为0
									if (null == gongzrList
											|| gongzrList.size() == 0) {

										dingdLineCount = BigDecimal.ZERO;
										CommonFun.logger
												.debug(" 计算交付日交付数量中第"
														+ j
														+ "次循环且K=0&&null == gongzrList时dingdLineCount="
														+ dingdLineCount);
									} else {
										// 调用公式得到订单行数量
										dingdLineCount = uaCount.multiply(
												new BigDecimal(gongzrList
														.size() - 1)).divide(
												dayCount, BigDecimal.ROUND_UP);
									}
									CommonFun.logger
											.debug(" 计算交付日交付数量中第"
													+ j
													+ "次循环且K=0&&null != gongzrList时dingdLineCount="
													+ dingdLineCount);
									// 检验订单行数量是否大于订单数量
									tempCount = uaCount
											.subtract(dingdLineCount);
									CommonFun.logger.debug(" 计算交付日交付数量中第" + j
											+ "次循环且K=0时uaCount=" + uaCount);
									CommonFun.logger.debug(" 计算交付日交付数量中第" + j
											+ "次循环且K=0时tempCount=" + tempCount);
									// 根据年周期查找开始，结束时间
									Gongyzq gongyzq = this.gongyzqService
											.queryGongyzq(zhouqi);
									if (null != gongyzq) {
										// 设置要货开始时间
										dingdmx.setYaohqsrq(gongyzq.getKaissj());
										CommonFun.logger.debug(" 计算交付日交付数量中第"
												+ j + "次循环时要货开始时间：="
												+ gongyzq.getKaissj());
										// 设置要货结束时间
										dingdmx.setYaohjsrq(jiaofrSingleMap
												.get(k + 1));
										CommonFun.logger.debug(" 计算交付日交付数量中第"
												+ j + "次循环时要货结束时间：="
												+ jiaofrSingleMap.get(k + 1));
										dingdmx.setJiaofrq(jiaofrSingleMap
												.get(k));
										CommonFun.logger.debug(" 计算交付日交付数量中第"
												+ j + "次循环时交付日期：="
												+ jiaofrSingleMap.get(k));
										List riqList = this.calendarcenterservice
												.getSubRq(
														jiaofrSingleMap.get(k),
														bean.getUsercenter(),
														faysj.toString());
										if (riqList.size() > 0) {
											dingdmx.setFayrq(riqList.get(
													riqList.size() - 1)
													.toString());
										} else {
											dingdmx.setFayrq(null);
										}
										CommonFun.logger.debug(" 计算交付日交付数量中第"
												+ j + "次循环时发运日期：="
												+ dingdmx.getFayrq());
									}

								} else if (k > 0
										&& k < jiaofrSingleMap.size() - 1) {// 判断不是第一个交付日的情况，判断交付日是否大于1且不是最后一次交付
									// 获取到N+1之间的所有的工作日天数(listGzr.size())
									List<CalendarVersion> listGzr = this.calendarVersionService
											.queryCalendarVersionListGzr(
													jiaofrSingleMap.get(k)
															.toString(),
													jiaofrSingleMap.get(k + 1)
															.toString(), bean
															.getUsercenter(),
													group.getRilbc(),
													Const.GZR_Y);
									CommonFun.objListPrint(listGzr,
											"N+1之间的所有的工作日天数listGzr");
									// 计算出订单行的数量,向上取整
									if (null == listGzr || listGzr.size() == 0) {
										dingdLineCount = BigDecimal.ZERO;
									} else {
										dingdLineCount = uaCount.multiply(
												new BigDecimal(
														listGzr.size() - 1))
												.divide(dayCount,
														BigDecimal.ROUND_UP);
									}
									CommonFun.logger
											.debug(" 计算交付日交付数量中第"
													+ j
													+ "次循环且k > 0 && k < jiaofrSingleMap.size() - 1时dingdLineCount="
													+ dingdLineCount);
									// 得到map中订单行的所有的数量
									BigDecimal sumValue = CommonFun
											.sumValue(dingdLineMap);
									CommonFun.logger
											.debug(" 计算交付日交付数量中第"
													+ j
													+ "次循环且k > 0 && k < jiaofrSingleMap.size() - 1时所有的数量sumValue="
													+ sumValue);
									// 检验订单行数量是否大于订单数量
									tempCount = uaCount.subtract(sumValue
											.add(dingdLineCount));
									CommonFun.logger
											.debug(" 计算交付日交付数量中第"
													+ j
													+ "次循环且k > 0 && k < jiaofrSingleMap.size() - 1时tempCount="
													+ tempCount);
									// 设置要货开始时间
									dingdmx.setYaohqsrq(jiaofrSingleMap.get(k));
									CommonFun.logger.debug(" 计算交付日交付数量中第" + j
											+ "次循环时要货开始时间：="
											+ jiaofrSingleMap.get(k));
									// 设置要货结束时间
									dingdmx.setYaohjsrq(jiaofrSingleMap
											.get(k + 1));
									CommonFun.logger.debug(" 计算交付日交付数量中第" + j
											+ "次循环时要货结束时间：="
											+ jiaofrSingleMap.get(k + 1));
									dingdmx.setJiaofrq(jiaofrSingleMap.get(k));
									CommonFun.logger.debug(" 计算交付日交付数量中第" + j
											+ "次循环时交付日期：="
											+ jiaofrSingleMap.get(k));
									List riqList = this.calendarcenterservice
											.getSubRq(jiaofrSingleMap.get(k),
													bean.getUsercenter(),
													faysj.toString());
									if (riqList.size() > 0) {
										dingdmx.setFayrq(riqList.get(
												riqList.size() - 1).toString());
									} else {
										dingdmx.setFayrq(null);
									}
									CommonFun.logger
											.debug(" 计算交付日交付数量中第" + j
													+ "次循环时发运日期：="
													+ dingdmx.getFayrq());
								} else {// 不满足条件的情况,订单行数量=订单量/UA容量-之前所有订单行的和的量

									// 根据年周期查找开始，结束时间
									Gongyzq gongyzq = this.gongyzqService
											.queryGongyzq(zhouqi);
									// 得到之前map中所有的订单行数量
									BigDecimal sumValue = CommonFun
											.sumValue(dingdLineMap);
									CommonFun.logger.debug(" 计算交付日交付数量中第" + j
											+ "次循环在最后else中所有的订单行数量sumValue="
											+ sumValue);
									// 获取最后一个交付日的交付天数（dayMax.size()）
									List<CalendarVersion> dayMax = this.calendarVersionService
											.queryCalendarVersionGzrByMax(bean
													.getUsercenter(), group
													.getRilbc(), zhouqi,
													jiaofrSingleMap.get(k)
															.toString(),
													Const.GZR_Y);
									CommonFun
											.objListPrint(
													dayMax,
													"计算交付日交付数量中第"
															+ j
															+ "次循环在最后else中最后一个交付日的交付天数dayMax");
									// 当得到的最后一个交付日的天数为空的时候
									if (null == dayMax || dayMax.size() == 0) {

										// 订单行数为0
										dingdLineCount = BigDecimal.ZERO;
										CommonFun.logger
												.debug(" 计算交付日交付数量中第"
														+ j
														+ "次循环在最后else中null == dayMax时dingdLineCount="
														+ dingdLineCount);
									} else {
										// 计算得到订单行数量
										dingdLineCount = uaCount.multiply(
												new BigDecimal(dayMax.size()))
												.divide(dayCount,
														BigDecimal.ROUND_UP);
									}
									CommonFun.logger.debug(" 计算交付日交付数量中第" + j
											+ "次循环在最后else中dingdLineCount="
											+ dingdLineCount);
									// 检验订单行数量是否大于订单数量
									tempCount = uaCount.subtract(sumValue
											.add(dingdLineCount));
									CommonFun.logger.debug(" 计算交付日交付数量中第" + j
											+ "次循环在最后else中tempCount="
											+ tempCount);

									if (null != gongyzq) {
										// 设置要货开始时间
										dingdmx.setYaohqsrq(jiaofrSingleMap
												.get(k));
										CommonFun.logger.debug(" 计算交付日交付数量中第"
												+ j + "次循环时要货开始时间：="
												+ jiaofrSingleMap.get(k));
										// 设置要货结束时间
										dingdmx.setYaohjsrq(gongyzq.getJiessj());
										CommonFun.logger.debug(" 计算交付日交付数量中第"
												+ j + "次循环时要货结束时间：="
												+ gongyzq.getJiessj());
										dingdmx.setJiaofrq(jiaofrSingleMap
												.get(k));
										CommonFun.logger.debug(" 计算交付日交付数量中第"
												+ j + "次循环时交付日期：="
												+ gongyzq.getJiessj());
										List riqList = this.calendarcenterservice
												.getSubRq(
														jiaofrSingleMap.get(k),
														bean.getUsercenter(),
														faysj.toString());
										if (riqList.size() > 0) {
											dingdmx.setFayrq(riqList.get(
													riqList.size() - 1)
													.toString());
										} else {
											dingdmx.setFayrq(null);
										}
										CommonFun.logger.debug(" 计算交付日交付数量中第"
												+ j + "次循环时发运日期：="
												+ dingdmx.getFayrq());
									}

								}
								// 当订单行数量是否大于订单数量，将大于部分减去，存放到map中去
								if (tempCount.intValue() < 0) {
									// 将大于部分减去，存放到map中去
									dingdLineMap.put(k,
											dingdLineCount.add(tempCount));
									// 得到减去多余部分的订单行数量
									dingdLineCount = dingdLineCount
											.add(tempCount);
								}
								// 将计算得到的订单行数量存放到map中；
								dingdLineMap.put(k, dingdLineCount);
								dingdmx.setJissl(dingdLineCount.multiply(bean
										.getUabzucsl().multiply(
												bean.getUabzucrl())));
								// 将得到的订单行数量设置到实体中去；
								dingdmx.setShul(dingdLineCount.multiply(bean
										.getUabzucsl().multiply(
												bean.getUabzucrl())));
								// 执行插入操作,插入到订单明细表中去

								// ////////////////////////////wuyichao//////////////////////////////////
								String key = dingdmx.getDingdh();
								List<Dingdmx> flagmx = saveMingxMap.get(key);
								if (flagmx == null) {
									flagmx = new ArrayList<Dingdmx>();
								}
								try {
									flagmx.add((Dingdmx) BeanUtils
											.cloneBean(dingdmx));
								} catch (IllegalAccessException e) {
									e.printStackTrace();
								} catch (InstantiationException e) {
									e.printStackTrace();
								} catch (InvocationTargetException e) {
									e.printStackTrace();
								} catch (NoSuchMethodException e) {
									e.printStackTrace();
								}
								saveMingxMap.put(key, flagmx);
								// this.dingdmxService.doInsert(dingdmx);
								// ////////////////////////////wuyichao//////////////////////////////////
							}
						}
					} else if (jiaofrSingleMap.isEmpty()) {
						String paramStr[] = new String[] {
								bean.getUsercenter(), bean.getLingjbh(), "交付日期" };
						this.yicbjService.insertError(Const.YICHANG_LX2,
								Const.YICHANG_LX2_str12, jihyz, paramStr,
								bean.getUsercenter(), bean.getLingjbh(),
								loginUser, jismk);

						
						CommonFun.logger.info(jismk + "\\" + bean.getLingjbh()
								+ "\\" + cuowlx + "\\" + info);
					} else if (dayCount.intValue() == 0) {
						String paramStr[] = new String[] {
								bean.getUsercenter(), bean.getLingjbh(), zhouqi };
						this.yicbjService.insertError(Const.YICHANG_LX3,
								Const.YICHANG_LX3_str3, jihyz, paramStr,
								bean.getUsercenter(), bean.getLingjbh(),
								loginUser, jismk);

					
					}
				}
			}
		}
		this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "il订单计算",
				"计算交付日交付数量结束");
	}

	/**
	 * 计算交付日
	 * */
	private Map<Integer, String> jisWorkDay(List<Jiaofrl> list,
			CalendarGroup group, int size, String fristDay, String endDay) {
		/*
		 * 定义交付次数存放的map: key 序号/value 交付日
		 */
		Map<Integer, String> mapJiaofr = new TreeMap<Integer, String>();
		// 定义treeMap中的key
		Integer keyJfr = 0;
		for (int k = 0; k < size; k++) {
			// 验证交付日是否为工作日
			CalendarVersion calendarbean = this.calendarVersionService
					.queryCalendarVersionNianzq(list.get(k).getRi(),
							group.getUsercenter(), group.getRilbc());
			CommonFun.objPrint(calendarbean, "计算交付日calendarbean");
			// 是工作日
			if (null != calendarbean) {
				// 将得到的交付日放到map中去，
				mapJiaofr.put(keyJfr, list.get(k).getRi().substring(0, 10));
				CommonFun.logger.debug("计算交付日在是工作日的情况下keyJfr=" + keyJfr);
				CommonFun.logger.debug("计算交付日在是工作日的情况下value="
						+ list.get(k).getRi().substring(0, 10));
				keyJfr++;
				// 不是工作日
			} else {
				// 不是工作日的情况，要根据获取交付日的规则来计算交付日
				List<CalendarVersion> versionBack = null;
				if (k != size - 1) {
					// 通过查询向后查找到对应的集合
					versionBack = this.calendarVersionService
							.queryCalendarVersionList(list.get(k).getRi(), list
									.get(k + 1).getRi(), group.getUsercenter(),
									group.getRilbc(), Const.GZR_Y);
					CommonFun.objListPrint(versionBack,
							"计算交付日在不是工作日的情况下且k != size - 1时的versionBack");
				} else {
					versionBack = this.calendarVersionService
							.queryCalendarVersionList(list.get(k).getRi(),
									endDay, group.getUsercenter(),
									group.getRilbc(), Const.GZR_Y);
					CommonFun.objListPrint(versionBack,
							"计算交付日在不是工作日的情况下且不是k != size - 1时的versionBack");
				}
				List<CalendarVersion> versionFront = null;
				if (k == 0) {
					// 通过查询向前查找到对应的集合(当j为1的时候)
					versionFront = this.calendarVersionService
							.queryCalendarVersionList(fristDay, list.get(k)
									.getRi(), group.getUsercenter(), group
									.getRilbc(), Const.GZR_Y);
					CommonFun.objListPrint(versionBack,
							"计算交付日在不是工作日的情况下且是k == 0时的versionBack");
				} else {
					// 通过查询向前查找到对应的集合(当j不为1的时候)
					versionFront = this.calendarVersionService
							.queryCalendarVersionList(list.get(k - 1).getRi(),
									list.get(k).getRi(), group.getUsercenter(),
									group.getRilbc(), Const.GZR_Y);
					CommonFun.objListPrint(versionBack,
							"计算交付日在不是工作日的情况下且不是k == 0时的versionBack");
				}
				// 先向后查找，再向前查找，两者都找不到的时候，就取周期第一天为交付日
				if (!versionBack.isEmpty()) {
					// 取出最近的一个日期,顺序取得
					calendarbean = versionBack.get(0);
					// 将得到的交付日放到map中去，
					mapJiaofr.put(keyJfr, calendarbean.getRiq()
							.substring(0, 10));
					CommonFun.logger.debug("计算交付日在是向后查找的情况下keyJfr=" + keyJfr);
					CommonFun.logger.debug("计算交付日在是向后查找的情况下value="
							+ calendarbean.getRiq().substring(0, 10));
					keyJfr++;
				} else if (!versionFront.isEmpty() && versionBack.isEmpty()) {
					// 取出最近的一个日期,倒序取得
					calendarbean = versionFront.get(versionFront.size() - 1);
					// 将得到的交付日放到map中去，
					mapJiaofr.put(keyJfr, calendarbean.getRiq()
							.substring(0, 10));
					CommonFun.logger.debug("计算交付日在是向前查找的情况下keyJfr=" + keyJfr);
					CommonFun.logger.debug("计算交付日在是向前查找的情况下value="
							+ calendarbean.getRiq().substring(0, 10));
					keyJfr++;
				} else if (versionFront.isEmpty() && versionBack.isEmpty()) {
					// 将得到的交付日放到map中去，
					mapJiaofr.put(keyJfr, fristDay);
					CommonFun.logger.debug("计算交付日在是取周期第一天的情况下keyJfr=" + keyJfr);
					CommonFun.logger
							.debug("计算交付日在是取周期第一天的情况下value=" + fristDay);
					keyJfr++;
				}

			}
		}
		return mapJiaofr;
	}

}