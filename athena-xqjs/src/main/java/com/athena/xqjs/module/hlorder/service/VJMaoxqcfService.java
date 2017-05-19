/**
 * VJ毛需求拆分服务
 */
package com.athena.xqjs.module.hlorder.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.UUID;

import javax.jws.WebService;

import org.apache.log4j.Logger;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.date.DateUtil;
import com.athena.xqjs.entity.hlorder.Clddxx;
import com.athena.xqjs.entity.hlorder.Clddxx2;
import com.athena.xqjs.entity.hlorder.Clddxx2Tmp;
import com.athena.xqjs.entity.hlorder.Daxpcsl;
import com.athena.xqjs.entity.hlorder.DdbhMaoxqsxxh;
import com.athena.xqjs.entity.hlorder.DdbhMaoxqsxxhr;
import com.athena.xqjs.module.common.CommonFun;
import com.toft.core3.container.annotation.Component;

@SuppressWarnings("rawtypes")
@WebService(endpointInterface = "com.athena.xqjs.module.hlorder.service.VJMaoxqcf", serviceName = "/vjMaoxqcf")
@Component
public class VJMaoxqcfService extends BaseService implements VJMaoxqcf {
	public final Logger log = Logger.getLogger(VJMaoxqcfService.class);
	private long tmpxh = 0; // 临时序号

	/**
	 * vj毛需求拆分入口
	 */
	public void vjMaoxqCaifen() {
		log.info(" vj毛需求拆分计算开始!");
		// 初始化临时序号
		tmpxh = 0;
		// 初始化参数
		List<Clddxx> cxs = initParam();
		// 上次推上线计划最后的顺序号, 按用户中心+产线号 UL5L1 做为健来存储 DDBH_TEST.SXSXH
		Map<String, DdbhMaoxqsxxh> maoxqcfxhmap = initMaoxqcfxhmap();//后面会更新为新的流水，并保存
		Map<String, DdbhMaoxqsxxh> maoxqcfxhmapold = initMaoxqcfxhmap(); // 保留在插入中需要
		// 根据JT计划向大线排产数量插入数据
		fillDaxpcsl(maoxqcfxhmap);
		if (cxs != null && cxs.size() > 0) {
			// 清空中间表
			clearMidtable();
			Map<String, List<Daxpcsl>> hzjhMap = new HashMap<String, List<Daxpcsl>>();
			/**
			 * 产线循环 统计获取总装的下线集合(zzxxlist)和涂装下线集合(tzsxlist) 清空中间表 in_clddxx2_tmp
			 * in_clddxx_tmp
			 * 
			 */
			for (Clddxx cx : cxs) {
				Map<String, Object> ksrqmap = computeCxZzKaisrq(cx,
						maoxqcfxhmap, cx.getUsercenter() + "5" + cx.getScxh());// 产线开始时间
				Date kaisrq = (Date) ksrqmap.get("ksrq");
				Integer kaisrqtype = (Integer) ksrqmap.get("type");
				if (kaisrq == null) {
					continue;
				}
				log.info("产线开始时间:" + kaisrq);
				long jsts = computeJsts(cx);// 计算天数
				log.info("产线计算天数:" + jsts);
				if(jsts <= 0){
					log.error("产线" + cx.getUsercenter() + "5" + cx.getScxh()
							+ "计算天数为0，计算跳过该产线!");
					continue;
				}
				/**
				 * 计算总装上线 获取计算开始日期到计算结束日期之间总装的上线日期及计划量集合（zzjhlist） 产线号不同 5
				 */
				List<Daxpcsl> zzjhlist = findSppvZzjhlist(cx, kaisrq,
						kaisrqtype);
				if (zzjhlist == null) {
					zzjhlist = new ArrayList<Daxpcsl>();
				}
				log.info("开始日期到计算结束日期之间总装的上线日期及计划量集合:" + zzjhlist.size() + "个");

				// 获取总装的车位数(zzchews)
				Long zzchews = null;
				BigDecimal zzccs = findZzchews(cx);
				if (zzccs != null) {
					zzchews = zzccs.longValue();
				} else {
					log.error("产线" + cx.getUsercenter() + "5" + cx.getScxh()
							+ "车位数数据为空，计算跳过该产线!");
					continue;
				}

				log.info("获取总装的车位数:" + zzchews);

				// 根据产线，计算开始时间 获取排产计划集合（按产线、上线序号 正序排列 zcxxlist）
				List<Clddxx> zzxxlist = findZzxxlist(cx, kaisrq, maoxqcfxhmap);

				if (zzxxlist != null) {
					// 循环总装上线日期及计划量,插入对应空数据
					zzxxlist = insert2Zzxxlist(zzjhlist, zzxxlist, cx, kaisrq,
							maoxqcfxhmap); // 序列为题
					
					ksrqmap = computeTzCxKaisrq(cx, maoxqcfxhmap,
							cx.getUsercenter() + "3" + cx.getScxh()); // 产线开始时间
																		// 3
					
					// 循环总装上线日期及计划量,计算总装上线记录并处理
					List<Clddxx2> zzsxlist = computeZzsxlist(zzjhlist,
							zzxxlist, zzchews, jsts, maoxqcfxhmap,cx,ksrqmap);
					log.info("插入总装上线记录:" + zzsxlist.size() + "个");
					
					
					
					
					
					
					
					// 总装循环完成
					// 计算总装上线集合为涂装下线集合
					// 计算涂装
				
					kaisrq = (Date) ksrqmap.get("ksrq");
					kaisrqtype = (Integer) ksrqmap.get("type");
					if (kaisrq == null) {
						continue;
					}
					log.info("涂装开始时间:" + kaisrq);
					// 获取计算开始日期到计算结束日期之间涂装的上线日期及计划量集合（zzjhlist） //产线号不同 3
					List<Daxpcsl> tzjhlist = findSppvTzjhlist(cx, kaisrq,
							kaisrqtype);
					if (tzjhlist == null) {
						tzjhlist = new ArrayList<Daxpcsl>();
					}
					log.info("开始日期到计算结束日期之间涂装的上线日期及计划量集合:" + tzjhlist.size());
					if (tzjhlist != null) {
						// 获取涂装的车位数(zzchews)

						Long tzchews = null;
						BigDecimal tzccs = findTzchews(cx);
						if (tzccs != null) {
							tzchews = tzccs.longValue();
						} else {
							log.error("产线" + cx.getUsercenter() + "3"
									+ cx.getScxh() + "车位数数据为空，计算跳过该产线!");
							continue;
						}

						log.info("涂装的车位数:" + tzchews);
						// 根据产线，计算开始时间 获取排产计划集合（按产线、上线序号 正序排列 zcxxlist）
						// 总装上线集合就是涂装的下线集合,不需要查询数据库(剔除空车身)
						List<Clddxx2> tzxxlist = new ArrayList<Clddxx2>();
						//for(Clddxx2 zzsx : zzsxlist)
						for (int i = 0;i< zzsxlist.size();i++)
						{
							Clddxx2 zzsx = zzsxlist.get(i);
							if ( !"whof".equals(zzsx.getWhof()))
							{
								tzxxlist.add(zzsx);
							}
						}
						if (tzxxlist != null) {
							// 循环涂装上线日期及计划量,插入对应空数据
							tzxxlist = insert2Tzxxlist(tzjhlist, tzxxlist, cx,
									kaisrq, maoxqcfxhmap);
							// 循环涂装上线日期及计划量,计算总装上线记录并处理
							// 涂装上线集合
							List<Clddxx2> tzsxlist = computeTzsxlist(tzjhlist,
									tzxxlist, tzchews, jsts, maoxqcfxhmap,cx);
							log.info("涂装的的上线结果做为涂装的下线集合插入：" + tzsxlist.size()
									+ "条");
							// //批量插入in_clddxx_zyhtmp 转运后表,利用中间来做
							if (tzsxlist != null && tzsxlist.size() > 0) {
								// 将涂装变成焊装，需要将产线等变过来
								for (Clddxx2 cl2 : tzsxlist) {
									String scxh = cl2.getScxh();
									cl2.setScxh(scxh.substring(0, 2) + "2"
											+ scxh.substring(3));
								}
									//L1线和L2线的上线计划都会插入转运后临时表，L2线转入到L1线的车辆就可以从这张表查出来
								baseDao.getSdcDataSource("2").executeBatch(
										"hlorder.insertclddxxzyhtmp", tzsxlist);
							}
							tzsxlist = null;
						}
						tzxxlist = null;

					} else {
						log.info("获取计算开始日期到计算结束日期之间涂装的上线日期及计划量集合，没有获取到对应的记录!");
					}
					tzjhlist = null;
					zzsxlist = null;
				} else {
					log.info("根据产线，计算开始时间 获取排产计划集合，没有获取到对应的记录!");
				}
				zzxxlist = null;

				zzjhlist = null;
			}

			if (!maoxqcfxhmapold.isEmpty()) {
				// 按用户中心插入焊装中需要，涂装没有的记录补充，因此循环每条产线
				long zqtmpxh = 0; // 之前的临时流水号
				List<Clddxx2> addouts = new ArrayList<Clddxx2>(); // 全部转运未处理的
				for (Clddxx cx : cxs) {
					// 由于产线的上线序号不一定同一，
					DdbhMaoxqsxxh maoxqsxh3 = maoxqcfxhmapold.get(cx
							.getUsercenter() + "3" + cx.getScxh()); // 获取上次推的涂装顺序号
					DdbhMaoxqsxxh maoxqsxh2 = maoxqcfxhmapold.get(cx
							.getUsercenter() + "2" + cx.getScxh()); // 获取上次推的焊装装顺序号
					Map<String, Object> ksrqmap = computeHzCxKaisrq(cx,
							maoxqcfxhmap, cx.getUsercenter() + "2" + cx.getScxh());// 焊装产线开始时间
					if (maoxqsxh3 != null
							&& maoxqsxh2 != null
							&& maoxqsxh2.getSxsxh().longValue() <= maoxqsxh3
									.getSxsxh().longValue()) {// 获取在in_clddxx2中获取
																// 焊装流水号<=流水号 <=
																// 涂装流水号的记录，并合并到涂装上线列表中
						HashMap<String, Object> param = new HashMap<String, Object>();
						param.put("tzxxh", maoxqsxh2.getSxsxh());// 焊装
						param.put("zzxxh", maoxqsxh3.getSxsxh());// 涂装
						param.put("scxh",
								cx.getUsercenter() + "3" + cx.getScxh()); // 涂装线号
						param.put("usercenter", cx.getUsercenter());
						List<Clddxx2> saoclddxxexist = baseDao
								.getSdcDataSource("2")
								.select("hlorder.queryComputeZzsxlistBYtzxxhhzzxxh",
										param);// 缺少的
						if (saoclddxxexist != null && saoclddxxexist.size() > 0) {
								for (int i = saoclddxxexist.size() - 1; i >= 0; i--) {
									String scxh = saoclddxxexist.get(i)
											.getScxh();
									saoclddxxexist.get(i).setScxh(
											scxh.substring(0, 2) + "2"
													+ scxh.substring(3));
									saoclddxxexist.get(i).setLiush(
											BigDecimal.valueOf((--zqtmpxh)));// 流水号头退
									saoclddxxexist.get(i).setId(
											UUID.randomUUID().toString()
													.replaceAll("-", ""));
								}
								baseDao.getSdcDataSource("2").executeBatch(
										"hlorder.insertclddxxzyhtmp",
										saoclddxxexist);
						}

					}else if(maoxqsxh2==null && ksrqmap.get("ksrq")!=null  && maoxqsxh3!=null){
						HashMap<String, Object> param = new HashMap<String, Object>();
						param.put("ksrq", (Date) ksrqmap.get("ksrq"));  //焊装的开始排产日期
						param.put("zzxxh", maoxqsxh3.getSxsxh());//涂装的最后上线顺序号
						param.put("scxh", cx.getUsercenter() + "3" + cx.getScxh()); // 涂装线号
						param.put("tzscxh",	cx.getUsercenter() + "2" + cx.getScxh()); // 焊装线号
						param.put("usercenter", cx.getUsercenter());
						List<Clddxx2> saoclddxxexist = baseDao.getSdcDataSource("2")
								.select("hlorder.queryComputeZzsxlistBYtzKsrq",
										param);// 缺少的
						if (saoclddxxexist != null && saoclddxxexist.size() > 0) {
							if (saoclddxxexist.size() > 0) {
								for (int i = saoclddxxexist.size() - 1; i >= 0; i--) {
									String scxh = saoclddxxexist.get(i)
											.getScxh();
									saoclddxxexist.get(i).setScxh(
											scxh.substring(0, 2) + "2"
													+ scxh.substring(3));
									saoclddxxexist.get(i).setLiush(
											BigDecimal.valueOf((--zqtmpxh)));// 流水号头退
									saoclddxxexist.get(i).setId(
											UUID.randomUUID().toString()
													.replaceAll("-", ""));
								}
								baseDao.getSdcDataSource("2").executeBatch(
										"hlorder.insertclddxxzyhtmp",
										saoclddxxexist);
							}
						}
					}
					// 增加转运没有处理的
					// 获取上次没有处理的转运记录
					if (maoxqsxh2 != null) {// 已运行过
						ArrayList<Clddxx2> outzycqlsts = null; // 转移丢失的
						if (maoxqsxh2.getZhanysxsxh() != null) {
							if (maoxqsxh2.getSxsxh().compareTo(
									maoxqsxh2.getZhanysxsxh()) > 0) {// 需要补充
								HashMap<String, Object> outparam = new HashMap<String, Object>();
								outparam.put("usercenter", cx.getUsercenter());
								outparam.put("scxh", cx.getScxh());
								outparam.put("minoutxxh",
										maoxqsxh2.getZhanysxsxh());
								outparam.put("maxoutxxh", maoxqsxh2.getSxsxh());
								outzycqlsts = (ArrayList<Clddxx2>) baseDao
										.getSdcDataSource("2")
										.select("hlorder.queryClddxx2ByZyhcxzycqdius",
												outparam);
							}
						}
	
						if (outzycqlsts != null && outzycqlsts.size() > 0) {
							for (int i = outzycqlsts.size() - 1; i >= 0; i--) {
								Clddxx2 otem = outzycqlsts.get(i);
								otem.setScxh(otem.getScxh().substring(0, 2)
										+ "2" + otem.getScxh().substring(3));
								otem.setLiush(BigDecimal.valueOf((--zqtmpxh)));// 流水号头退
								otem.setId(UUID.randomUUID().toString()
										.replaceAll("-", ""));
								addouts.add(otem);
							}
						}
					}
				}
				if (addouts.size() > 0) {
					baseDao.getSdcDataSource("2").executeBatch(
							"hlorder.insertclddxxzyhtmp", addouts);
				}
			}

			/**
			 * 计算焊装
			 */
			Map<String, BigDecimal> outsxh = new HashMap<String, BigDecimal>();
			for (Clddxx cx : cxs) {
				Map<String, Object> ksrqmap = computeHzCxKaisrq(cx,
						maoxqcfxhmap, cx.getUsercenter() + "2" + cx.getScxh());// 产线开始时间
				Date kaisrq = (Date) ksrqmap.get("ksrq");
				Integer kaisrqtype = (Integer) ksrqmap.get("type");
				if (kaisrq == null) {
					continue;
				}
				long jsts = computeJsts(cx);// 计算天数
				log.info("产线计算天数:" + jsts);

				if(jsts <= 0){
					log.error("产线" + cx.getUsercenter() + "5" + cx.getScxh()
							+ "计算天数为0，计算跳过该产线!");
					continue;
				}
				// 获取焊装的车位数(zzchews)
				Long hzchews = null;
				BigDecimal hzccs = findHzchews(cx);
				if (hzccs != null) {
					hzchews = hzccs.longValue();
				} else {
					log.error("产线" + cx.getUsercenter() + "2" + cx.getScxh()
							+ "车位数数据为空，计算跳过该产线!");
					continue;
				}
				
				// 获取计算开始日期之后的焊装的上线日期及计划量集合
				List<Daxpcsl> hzjhlist = findSppvHzjhlist(cx, kaisrq,
						kaisrqtype, maoxqcfxhmap, hzchews);
				
				if (hzjhlist.size() == 0 || hzjhlist == null) {
					hzjhlist = new ArrayList<Daxpcsl>();
				}

				// 通过转运后的中间表数据得到转运前的焊装上线排产计划集合
				List<Clddxx2> hzxxlist = findZyqHzjhlistBytable(cx,
						maoxqcfxhmapold, hzjhlist, hzchews); // 转运前的
				if (hzxxlist != null && !hzxxlist.isEmpty()) {
					// 保存转运前
					baseDao.getSdcDataSource("2").executeBatch(
							"hlorder.insertClddxxZyqtmpByclddxx2", hzxxlist);
					// 循环焊装上线日期及计划量,插入对应空数据
					hzxxlist = insert2Hzxxlist(hzjhlist, hzxxlist, cx, kaisrq,
							maoxqcfxhmap);
					// 循环焊装上线日期及计划量,计算总装上线记录并处理
					// 焊装上线集合

					computeHzsxlist(hzjhlist, hzxxlist, hzchews, jsts,
							maoxqcfxhmap, outsxh,cx);
				}
				hzjhMap.put(cx.getScxh(), hzjhlist);
				//hzjhlist = null;
				hzxxlist = null;

			}
			
			// 汇总上线计划
			// 根据 usercenter,lcdv24,scxh,yjsyhsj,lcdvbzk,yjsxsj
			// 汇总数量(in_clddxx_tmp)，
			sumSxjh();

			// 记录之前的
			saveHisMaoxqCfxhMap(maoxqcfxhmapold);
			// 保存本次推的最后顺序
			saveMaoxqCfxhMap(maoxqcfxhmap, outsxh);
			
			try {
				checkSxsl(hzjhMap, maoxqcfxhmapold);
			} catch (Exception e) {
				log.error("上线计划数量于实际上线数不匹配!");
			}
			
			log.info(" vj毛需求拆分计算结束!");
		} else {
			log.info("毛需求拆分总装产线初始化，没有获取到对应的记录!");
		}

	}

	/**
	 * 上线计划数量校验
	 * @throws Exception
	 */
	private void checkSxsl(Map<String, List<Daxpcsl>> hzjhMap, Map<String, DdbhMaoxqsxxh> maoxqcfxhmap) throws Exception
	{
		// 查询参数map
		Map<String, Object> paramMap = new HashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");   
		Date getDate = new Date(); // 日期
		String tmpDate = sdf.format(getDate);
		getDate = sdf.parse(tmpDate);
		paramMap.put("getDate", getDate);
		// 汇总当天生成的上线计划数量
		List<Map<String, Object>> clddxx2List = baseDao.getSdcDataSource(
				ConstantDbCode.DATASOURCE_EXTENDS2).select(
				"hlorder.queryClddxx2ShulNow", paramMap);
		for (Map<String, Object> clddxx2 : clddxx2List)
		{
			// 只验证拆分日期之后的上线计划数量
			if(maoxqcfxhmap != null && maoxqcfxhmap.containsKey(clddxx2.get("SCXH"))){
				DdbhMaoxqsxxh ddbhMaoxqsxxh = maoxqcfxhmap.get(clddxx2.get("SCXH"));
				if(ddbhMaoxqsxxh.getCaifrq().compareTo((Date) clddxx2.get("YJSXSJ")) >= 0){
					continue;
				}
			}
			paramMap = new HashMap<String, Object>();
			paramMap.put("riq", (Date) clddxx2.get("YJSXSJ"));
			paramMap.put("daxxh", clddxx2.get("SCXH"));
			// In_daxpcsl
			BigDecimal jihsxl = CommonFun.getBigDecimal(baseDao
					.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2)
					.selectObject("hlorder.queryDaxpcShul", paramMap));
			if ( null != jihsxl && 0 != jihsxl.compareTo(BigDecimal.ZERO))
			{
				if ( 0 != jihsxl.compareTo((BigDecimal)clddxx2.get("SXSL")))
				{
					throw new Exception(" 上线计划数量于实际上线数不匹配!");
				}
			}
			else
			{
				paramMap = new HashMap<String, Object>();
				paramMap.put("usercenter", clddxx2.get("SCXH").toString().substring(0, 2));
				paramMap.put("zzx", clddxx2.get("SCXH").toString().substring(3, 5));
				paramMap.put("getDate", (Date)clddxx2.get("YJSXSJ"));
				// 查询大线排产数量in_jtpcjh
				BigDecimal jtSl = CommonFun.getBigDecimal(baseDao.getSdcDataSource(
						ConstantDbCode.DATASOURCE_EXTENDS2).selectObject(
						"hlorder.queryShulByDateR", paramMap));
				// 焊装产线，由于可能有转运车辆，故不能直接用jt的数量
				if(clddxx2.get("SCXH").toString().substring(2, 3).equals("2")){
					if(hzjhMap.containsKey(clddxx2.get("SCXH").toString().substring(3, 5))){
						List<Daxpcsl> list = hzjhMap.get(clddxx2.get("SCXH").toString().substring(3, 5));
						for (Daxpcsl daxpcsl : list) {
							if(daxpcsl.getRiq().equals((Date)clddxx2.get("YJSXSJ"))){
								jtSl = daxpcsl.getJihsxl();
							}
						}
					}
				}
				if ( 0 != jtSl.compareTo((BigDecimal)clddxx2.get("SXSL")))
				{
					throw new Exception(" 上线计划数量于实际上线数不匹配!");
				}
			}
		}
	}
	
	/**
	 * 保存本次计算的序号和时间
	 * @param maoxqcfxhmap
	 * @param outsxh
	 */
	private void saveMaoxqCfxhMap(Map<String, DdbhMaoxqsxxh> maoxqcfxhmap,
			Map<String, BigDecimal> outsxh) {
		if (maoxqcfxhmap != null && !maoxqcfxhmap.isEmpty()) {
			Collection<DdbhMaoxqsxxh> savelst = maoxqcfxhmap.values();
			HashMap<String, String> param = new HashMap<String, String>();
			for (DdbhMaoxqsxxh save : savelst) {
				BigDecimal outsavaxx = outsxh.get(save.getDaxxh());// 转移出去处理的序号
				if (outsavaxx != null) {
					save.setZhanysxsxh(outsavaxx); // 保存移出去处理的序号，如果没有，则和处理的一样
				} else {
					if (save.getZhanysxsxh() == null) {
						save.setZhanysxsxh(BigDecimal.ZERO); // 保存移出去处理的序号，如果没有，则和处理的一样
					}
				}
				param.clear();
				param.put("daxxh", save.getDaxxh());
				// 查询
				DdbhMaoxqsxxh exist = (DdbhMaoxqsxxh) baseDao.getSdcDataSource(
						"2").selectObject("hlorder.queryDdbhMaoxqsxxhBykey",
						param);
				if (exist != null) {// 更新
					baseDao.getSdcDataSource("2").execute(
							"hlorder.updateDdbhMaoxqsxxh", save);
				} else {// 插入
					baseDao.getSdcDataSource("2").execute(
							"hlorder.insertDdbhMaoxqsxxh", save);
				}
			}
		}
	}
	
	/**
	 * 保存历史序号信息
	 * @param maoxqcfxhmap
	 */
	private void saveHisMaoxqCfxhMap(Map<String, DdbhMaoxqsxxh> maoxqcfxhmap) {
		if (maoxqcfxhmap != null && !maoxqcfxhmap.isEmpty()) {
			Collection<DdbhMaoxqsxxh> savelst = maoxqcfxhmap.values();
			List<DdbhMaoxqsxxhr> sxxhRList = new ArrayList<DdbhMaoxqsxxhr>();
			for (DdbhMaoxqsxxh save : savelst) {
				DdbhMaoxqsxxhr ddbhMaoxqsxxhr = new DdbhMaoxqsxxhr();
				ddbhMaoxqsxxhr.setCaifrq(save.getCaifrq());
				ddbhMaoxqsxxhr.setCj_date(new Date());
				ddbhMaoxqsxxhr.setCkpyy(save.getCkpyy());
				ddbhMaoxqsxxhr.setDaxxh(save.getDaxxh());
				ddbhMaoxqsxxhr.setSxsxh(save.getSxsxh());
				ddbhMaoxqsxxhr.setWhof(save.getWhof());
				ddbhMaoxqsxxhr.setZhanysxsxh(save.getZhanysxsxh());
				sxxhRList.add(ddbhMaoxqsxxhr);
			}
			// 批量插入
			baseDao.getSdcDataSource("2").executeBatch(
					"hlorder.insertDdbhMaoxqsxxhr", sxxhRList);
		}
	}

	/*
	 * 通过转运后的中间表数据得到转运前的排产计划集合根据产线，计算开始时间 获取排产计划集合（按产线、上线序号 正序排列 zcxxlist）
	 * 涂装上线集合就是焊装的下线集合,不需要查询数据库转运处理转运出去 select b.* from ddbh_test.ckx_chexpt c
	 * ,ddbh_test.in_clddxx_zyhtmp b where c.usercenter=b.usercenter and
	 * c.shengcxbhzz !='L1' and c.shengcxbhhz='L1' and
	 * c.lcdv=substr(b.lcdv24,0,6) 没有转运 select b.* from ddbh_test.ckx_chexpt c
	 * ,ddbh_test.in_clddxx_zyhtmp b where c.usercenter=b.usercenter and
	 * c.shengcxbhzz ='L1' and c.shengcxbhhz=L1 and c.lcdv=substr(b.lcdv24,0,6)
	 * 
	 * 只有增加没有删除的。运行时到的。注意保持顺序sql 得到转运出去 转运进来的获取产线规则:参考系车型平台 ckx_chexpt 和
	 * in_clddxx2_tmp 在用户中心,LCPV前6位相 和1.在用户中心,LCPV前6位相当的记录中,如果如果总装线号与焊装线号不一致就是转移
	 * 得到转运记录后,转运出去的,在对应时间、产线 集合中删除，删除条件：转入的，在对应时间、原焊装产线 集合中均等插入，插入时要考虑新插入记录的后移
	 * 例如原焊装产线 集合为 l0 = h1,l1 = h2,l2 = h3,l3 = h4,l4 = h5, 插入3条结果为：l0-h6,l1 =
	 * h1,l2=h7,l3 = h2,l4=h8,l5 = h3,l6 = h4,l7 = h5,焊装上线集合考虑转运校正焊装上线集合
	 */
	private List<Clddxx2> findZyqHzjhlistBytable(Clddxx cx,
			Map<String, DdbhMaoxqsxxh> maoxqcfxhmapold, List<Daxpcsl> hzjhlist, Long hzchews) {

		// 获取上次没有处理的转运记录
		DdbhMaoxqsxxh maoxqcfsxxh = maoxqcfxhmapold.get(cx.getUsercenter()
				+ "2" + cx.getScxh());
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("usercenter", cx.getUsercenter());
		param.put("scxh", cx.getScxh());
		// 转运出去
		ArrayList<Clddxx2> zycqlists = (ArrayList<Clddxx2>) baseDao
				.getSdcDataSource("2").select(
						"hlorder.queryClddxx2ByZyhcxzycq", param);
		// 删除重复记录由sql：queryClddxx2ByZyhcxzycq直接完成 避免拼装错误 20160712
		//deleteRepeatTranspost(zycqlists, cx);
		// 没有转运
		ArrayList<Clddxx2> myzylists = (ArrayList<Clddxx2>) baseDao
				.getSdcDataSource("2").select(
						"hlorder.queryClddxx2ByZyhcxmyzycq", param);
		if (zycqlists != null && zycqlists.size() > 0) {
			if (myzylists == null || myzylists.size() == 0) {// 全部转运出去了
				myzylists = new ArrayList<Clddxx2>(zycqlists.size());
				for (Clddxx2 t : zycqlists) {
					t.setOutscxh(t.getScxh());
					t.setScxh(t.getScxh().substring(0, 3) + cx.getScxh());
					myzylists.add(t);
				}
				return myzylists;
			} else {// 按日期 均等插入转运出去的
				// 按日期收集
				Map<String, List<Clddxx2>> cldd2meiyoumap = new HashMap<String, List<Clddxx2>>();// 按日期回收没有转运的
				for (Clddxx2 cl : myzylists) {
					String key = DateUtil.dateToStringYMD(cl.getYjsxsj());// 上线日期
					List<Clddxx2> lst = cldd2meiyoumap.get(key); // 日期
					if (lst == null) {
						lst = new ArrayList<Clddxx2>();
					}
					lst.add(cl);
					cldd2meiyoumap.put(key, lst);
				}
				Map<String, List<Clddxx2>> cldd2map = new HashMap<String, List<Clddxx2>>();// 按日期回收转运出去的
				for (Clddxx2 cl : zycqlists) {
					String key = DateUtil.dateToStringYMD(cl.getYjsxsj());// 上线日期
					List<Clddxx2> lst = cldd2map.get(key); // 日期
					if (lst == null) {
						lst = new ArrayList<Clddxx2>();
					}
					lst.add(cl);
					cldd2map.put(key, lst);
				}
				
				List<Clddxx2> beforeList = new ArrayList<Clddxx2>();
				
				// 按日期等比插入，当存在上次保存的下标时，要插在下标之后，如果第一次时，要使日期〉=计算的开始日期，需要修改日期

				if (maoxqcfsxxh == null) {// 第一次
					List<String> meiyzykeylst = new ArrayList<String>(
							cldd2meiyoumap.keySet());// 没有转移的
					Collections.sort(meiyzykeylst, new Comparator<String>() {

						@Override
						public int compare(String o1, String o2) {
							return o1.compareTo(o2);
						}
					});
					
					//偏移计划，得到焊装上线计划
					beforeList = offsetPlans(meiyzykeylst, cldd2meiyoumap, hzchews.intValue(), hzjhlist, null);	
					
					sumUndoTransport(cldd2map, hzjhlist); // 汇总未完成的转运计划
					
					List<String> zykeylst = new ArrayList<String>(
							cldd2map.keySet());// 转移的
					Collections.sort(zykeylst);
					for (String key : zykeylst) {// 循环转运的

						String inkey = key; // 插入的关键字
						// 等比插入
						List<Clddxx2> meizylst;// 没有转运的
						meizylst = cldd2meiyoumap.get(inkey);
						if (meizylst == null) {
							meizylst = new ArrayList<Clddxx2>();
						}
						
						List<Clddxx2> zylst = cldd2map.get(key);// 转运的
						Map<String, Integer> map = calcTransportInsert(meizylst.size(), zylst.size());
						int interval = map.get("interval");	//插入间隔
						int count = map.get("count");	//插入数量
						for (int idx = 0; idx < zylst.size(); idx++) {
							Clddxx2 t = zylst.get(idx);
							t.setOutscxh(t.getScxh());
							t.setScxh(t.getScxh().substring(0, 3)
									+ cx.getScxh());
							try {
								t.setYjsxsj(DateUtil.stringToDateYMD(inkey));
							} catch (ParseException e) {
								log.error(e.getMessage(), e);
							}
							if(count == 1){
								meizylst.add(idx + idx * interval, t);
							}else if(interval == 1){
								meizylst.add(idx + idx / count, t);
							}
						}
						//cldd2meiyoumap.put(inkey, meizylst);
					}
				} else {// 不是第一次
					BigDecimal flag = maoxqcfsxxh.getSxsxh();// 上次保存的标记
					List<String> meiykeylst = new ArrayList<String>(
							cldd2meiyoumap.keySet());// 没有转移的
					Collections.sort(meiykeylst, new Comparator<String>() {

						@Override
						public int compare(String o1, String o2) {
							return o1.compareTo(o2);
						}

					});
					String minrq = DateUtil.dateToStringYMD(maoxqcfsxxh
							.getCaifrq());// 最小日期
					int beginidx = -1;
					// 找第一个合适的记录
					for (String key : meiykeylst) {
						List<Clddxx2> lst = cldd2meiyoumap.get(key);
						for (int fi = 0; fi < lst.size(); fi++) { // 找上次保存的序号下标
							if (flag.compareTo(lst.get(fi).getSxsxh()) <= 0) {// 相等
								minrq = key;
								beginidx = fi;
								break;
							}
						}
						if (beginidx > -1) {// 已找到
							break;
						}
					}
					
					beforeList = offsetPlans(meiykeylst, cldd2meiyoumap, beginidx + 1, hzjhlist, minrq);	//偏移计划

					sumUndoTransport(cldd2map, hzjhlist); // 汇总未完成的转运计划
					
					List<String> zykeylst = new ArrayList<String>(
							cldd2map.keySet());// 转移的
					Collections.sort(zykeylst);
					for (String key : zykeylst) {// 循环转运的
						String inkey = key; // 插入的关键字
						// 等比插入
						List<Clddxx2> meizylst;// 没有转运的
						meizylst = cldd2meiyoumap.get(inkey);
						// 当日本身计划为空，则直接跳过，不插入转运计划
						if (meizylst == null || meizylst.isEmpty()) {
							continue;
						}
						
						List<Clddxx2> zylst = cldd2map.get(key);// 转运的
						Map<String, Integer> map = calcTransportInsert(meizylst.size(), zylst.size());
						int interval = map.get("interval");	//插入间隔
						int count = map.get("count");	//插入数量
						for (int idx = 0; idx < zylst.size(); idx++) {
							Clddxx2 t = zylst.get(idx);
							t.setOutscxh(t.getScxh());
							t.setScxh(t.getScxh().substring(0, 3)
									+ cx.getScxh());
							try {
								t.setYjsxsj(DateUtil.stringToDateYMD(inkey));
							} catch (ParseException e) {
								log.error(e.getMessage(), e);
							}
							if(count == 1){
								meizylst.add(idx + idx * interval, t);
							}else if(interval == 1){
								meizylst.add(idx + idx / count, t);
							}
						}
						//cldd2meiyoumap.put(inkey, meizylst);
					}
				}
				List<String> meiykeys = new ArrayList<String>(
						cldd2meiyoumap.keySet());// 没有转移的
				Collections.sort(meiykeys, new Comparator<String>() {

					@Override
					public int compare(String o1, String o2) {
						return o1.compareTo(o2);
					}
				});
				ArrayList<Clddxx2> result = new ArrayList<Clddxx2>();
				// 偏移量之前的计划，保证计划完整，避免对后面的计算造成影响
				result.addAll(beforeList);	
				for (String key : meiykeys) {
					result.addAll(cldd2meiyoumap.get(key));
				}
				return result;

			}
		}

		return myzylists;
	}
	
	/**
	 * 删除重复的转运数据
	 * @param zycqlists
	 * @param cx
	 */
	private void deleteRepeatTranspost(ArrayList<Clddxx2> zycqlists, Clddxx cx){
		if (zycqlists != null && zycqlists.size() > 0) {
			StringBuffer st = new StringBuffer();
			for (Clddxx2 t : zycqlists) {
				if (!"whof".equals(t.getWhof())) {
					st.append("'");
					st.append(t.getWhof());
					st.append("'");
					st.append(",");
				}
			}
			if (st.length() > 0) {
				HashMap<String, String> wofparam = new HashMap<String, String>();
				wofparam.put("whofs", st.substring(0, st.length() - 1));
				wofparam.put("scxh", cx.getUsercenter() + "2" + cx.getScxh());
				wofparam.put("usercenter", cx.getUsercenter());
				List<String> clddxxexist = baseDao.getSdcDataSource("2")
						.select("hlorder.queryComputeZzsxlistBYInwokfAndScxh",
								wofparam);
				if (clddxxexist != null && clddxxexist.size() > 0) {// 变换后处理
					for (String s : clddxxexist) {
						for (Clddxx2 t : zycqlists) {
							if (s.equals(t.getWhof())) {
								zycqlists.remove(t);
								break;
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * 计划转移插入间隔 和数量
	 * @param onLineCount 上线计划数量
	 * @param transportCount 转移计划数量
	 * @return
	 */
	private Map<String, Integer> calcTransportInsert(int onLineCount, int transportCount){
		
		int interval = 0;	// 插入间隔
		int count = 1;	// 插入数量
		if(onLineCount != 0 && transportCount != 0){
			if(onLineCount >= transportCount){
				interval = onLineCount / transportCount;	// 向下取整
				count = 1;
			}else{
				interval = 1;
				count = (int) Math.ceil((Double.valueOf(transportCount)) / (Double.valueOf(onLineCount)));	// 向上取整
			}
		}
		
		Map<String, Integer> resultMap = new HashMap<String, Integer>();
		resultMap.put("interval", interval);
		resultMap.put("count", count);
		return resultMap;
	}
	
	/**
	 * 偏移计划
	 * @param sortedKetList
	 * @param plans
	 * @param offset
	 */
	private List<Clddxx2> offsetPlans(List<String> sortedKetList, 
			Map<String, List<Clddxx2>> plans, int offset,List<Daxpcsl> hzjhlist, String minrq){
		// 每日的计划数量
		Map<String, Integer> countMap = new TreeMap<String, Integer>();
		// 所有的计划list
		List<Clddxx2> allPlanList = new ArrayList<Clddxx2>();
		Collections.sort(hzjhlist, new Comparator<Daxpcsl>() {

			@Override
			public int compare(Daxpcsl o1, Daxpcsl o2) {
				return o1.getRiq().compareTo(o1.getRiq());
			}

		});
		
		// 汇总计划
		for (String date : sortedKetList) {
			List<Clddxx2> dayPlans = plans.get(date);
			if(dayPlans != null){
				allPlanList.addAll(dayPlans);
				// 偏移量只是当日的偏移量，如果跨天需要加入之前的计划数量
				if(minrq != null && date.compareTo(minrq) < 0){
					offset += dayPlans.size();
				}
				dayPlans.clear();
			}
		}
		
		
		// 按照日期统计数量
		for (Daxpcsl daxpcsl : hzjhlist) {
			String date = DateUtil.dateToStringYMD(daxpcsl.getRiq());
			countMap.put(date, daxpcsl.getJihsxlWithOutTransport() == null ?
					daxpcsl.getJihsxl().intValue() : daxpcsl.getJihsxlWithOutTransport().intValue());
		}
		int off = offset;
		// 按照偏移量进行偏移
		for (String date : countMap.keySet()) {
			Integer count = countMap.get(date);
			List<Clddxx2> dayPlans = plans.get(date);
			if(dayPlans == null){
				dayPlans = new ArrayList<Clddxx2>();
				plans.put(date, dayPlans);
			}
			if(off <= allPlanList.size()){
				dayPlans.addAll(allPlanList.subList(off, Math.min(off + count, allPlanList.size())));
				removeList(allPlanList, off, Math.min(off + count, allPlanList.size()));
			}
		}
		return allPlanList;
	}
	
	/**
	 * 从集合中移除指定角标范围的集合
	 * @param list
	 * @param startIndex
	 * @param endIndex
	 */
	private void removeList(List list, int startIndex, int endIndex){
		startIndex = startIndex < 0 ? 0 : startIndex;
		endIndex = endIndex > list.size() ? list.size() : endIndex;
		for (int i = startIndex; i < endIndex; i++) {
			list.remove(i);
			i--;
			endIndex--;
		}
	}
	
	/**
	 * 获取焊装上线计划数量
	 * @param dateRelationMap
	 * @param inkey
	 * @param hzjhlist
	 * @param meizylst
	 * @return
	 */
	private int getOnLineCount(Map<String, Date> dateRelationMap, String inkey, 
			List<Daxpcsl> hzjhlist, List<Clddxx2> meizylst){
		
		int onLineCount = 0; //焊装上线量
//		if(dateRelationMap.containsKey(inkey)){ // 获取对应焊装上线日期的计划数量
//			Date onLineDate = dateRelationMap.get(inkey);
//			for (Daxpcsl daxpcsl : hzjhlist) {
//				if(onLineDate.compareTo(daxpcsl.getRiq()) == 0){
//					onLineCount = daxpcsl.getJihsxl().intValue();
//					break;
//				}
//			}
//		}
//		if(onLineCount == 0){ // 没有则按照原逻辑用焊装下线日期的数量
//			onLineCount = meizylst.size();
//		}
		// 现在 已经是上线
		onLineCount = meizylst.size();
		
		return onLineCount;
	}
	
	
	/**
	 * 汇总未完成的转运计划（上线日期小于此次计算第一日的计划）
	 * @param cldd2map	需要转运的计划
	 * @param minrq	此次计算的第一日
	 */
	private void sumUndoTransport(Map<String, List<Clddxx2>> cldd2map , List<Daxpcsl> hzjhlist){
		if(cldd2map == null || cldd2map.isEmpty() || hzjhlist.isEmpty()){
			return;
		}
		String minrq = DateUtil.dateToStringYMD(hzjhlist.get(0).getRiq());
		// 第一天转运的计划
		List<Clddxx2> firstDayList = cldd2map.get(minrq);
		if(firstDayList == null){
			firstDayList = new ArrayList<Clddxx2>();
			cldd2map.put(minrq, firstDayList);
		}
		// 循环每日的需要的转运计划
		Iterator<Entry<String, List<Clddxx2>>> iter = cldd2map.entrySet().iterator();
		while(iter.hasNext()){
			Entry<String, List<Clddxx2>> entry = iter.next();
			// 需要转运计划的日期小于此次计算的第一日
			if(entry.getKey().compareTo(minrq) < 0){
				// 将转运计划放在计算的第一日
				firstDayList.addAll(entry.getValue());
				Collections.sort(firstDayList, new Comparator<Clddxx2>() {

					@Override
					public int compare(Clddxx2 o1, Clddxx2 o2) {
						if(o1.getSxsxh() != null && o2.getSxsxh() != null){
							return o1.getSxsxh().compareTo(o2.getSxsxh());
						}
						return 0;
					}
				});
				// 移除当日的转运计划
				iter.remove();
			}
		}
	}

	/*
	 * @param hzchews 焊装总装的车位数
	 * 
	 * @param hzjhlist 焊装上线集合及计算量
	 * 
	 * @param hzxxlist 焊装的下线集合
	 * 
	 * 
	 * 获取涂装的下线集合(tzxxlist)中第（tzchews+m）条记录为当前涂装上线的车身 上线时间为循环的计算时间（riq）
	 * 产线为（用户中心+3+线号）将涂装的的上线结果做为涂装的下线集合（tzsxlist）同时插入in_clddxx2中
	 * 
	 * 车身 插第一天时要做一个判断，根据whof 和产线
	 * 如果存在，不做任何记录，如果不存在，插入时，与其它的处理不一样，需要变预计上线时间变成循环日期下标
	 * +1记录的日期.如果当前只有一条记录,不做动作. 例如: 10.2 10.5 天 当10.2存在时,取日期10.5的时间 如果
	 */
	private List<Clddxx2> computeHzsxlist(List<Daxpcsl> hzjhlist,
			List<Clddxx2> hzxxlist, Long hzchews, long jsts,
			Map<String, DdbhMaoxqsxxh> maoxqcfxhmap,
			Map<String, BigDecimal> outsxh,Clddxx cx) {
		// 推算开始下表
		List<Clddxx2> hzsxlist = new ArrayList<Clddxx2>();
		DdbhMaoxqsxxh maoxqcfsxxh = null;
		if (hzjhlist.size() > 0) {
			maoxqcfsxxh = maoxqcfxhmap.get(hzjhlist.get(0).getDaxxh()); // 获取上次推的顺序号

			// 列表开始序号 。当maoxqcfsxxh不为空时，在zzxxlist中查找，找到订单号一样记录。该记录下标+1就是开始序号
			// 当maoxqcfsxxh为空或没有查找到，按以前时间的第一个+车身数
			// 序号只在大线运营的第一次查找，后期直接+1
			Long shangccfksxh = null; // 上次拆分开始序号
			Long rqinsertind = null; // 按日期查找的插入下标
			Integer insertind = null; // 插入下标
			// 结束日期
			Date bcjsri = null;
			// 焊装上线集合

			boolean isaddinset = false; // 不存在，开始插入标记
			List<String> cldri = new ArrayList<String>(); // cldd的顺序日期
			for (int i = 0; i < hzjhlist.size(); i++) {
				Daxpcsl daxpcsl = hzjhlist.get(i);
				String ri = DateUtil.dateToStringYMD(daxpcsl.getRiq());// 得到年月日
				for (int indx = 0; indx < hzxxlist.size(); indx++) {
					// 查找上次拆分的开始序号
					// 查找上次拆分的开始序号
					// 根据上线顺序号+ 空偏移量
					// 如果能找到cldd记录的顺序号=存储的上线顺序的记录，则更加空偏移量偏移对应的数量。
					// 如果在偏移过程中< 空偏移量的空值，则在最先得到的正常记录的前一个空记录处停止
					// 如果找不到，则找到第一个>存储的上线顺序的记录 -1
					if (maoxqcfsxxh != null) {
						if (shangccfksxh == null) {
							if (hzxxlist.get(indx).getSxsxh() != null) {
								if (maoxqcfsxxh.getSxsxh().compareTo(
										hzxxlist.get(indx).getSxsxh()) <= 0
										&& "1".equals(hzxxlist.get(indx)
												.getNooutflag())) {// 没有转运出去的相同记录数
									if (maoxqcfsxxh.getSxsxh().equals(
											hzxxlist.get(indx).getSxsxh())) {// 找到对应记录
										if (BigDecimal.ZERO
												.compareTo(maoxqcfsxxh
														.getCkpyy()) < 0) {// 存在偏移值
											for (int off = indx + 1; off <= indx
													+ maoxqcfsxxh.getCkpyy()
															.intValue(); off++) {
												if (off < hzxxlist.size()) {// 有效下标
													if ("whof"
															.equals(hzxxlist
																	.get(off)
																	.getWhof())) {// 存在空值
																					// 或转运出去的
														shangccfksxh = (long) (off + 1); // 下一位
													} else if (!"1"
															.equals(hzxxlist
																	.get(off)
																	.getNooutflag())) {
														shangccfksxh = (long) off;// 第一个转运出去的，在程序前已经将已转移出去的删除了。因此必须增加到现在的队列中
														break;
													} else {
														shangccfksxh = (long) off;// 第一个不为空值
														break;
													}
												}
											}
										} else {// 不存在偏移值
											shangccfksxh = (long) (indx + 1);
										}
									} else {// 没有
										shangccfksxh = (long) indx; // 最先大于的记录
									}
								}
							}

						}
					}
					if (!cldri.contains(DateUtil.dateToStringYMD(hzxxlist.get(//没有用到
							indx).getYjsxsj()))) {
						cldri.add(DateUtil.dateToStringYMD(hzxxlist.get(indx)
								.getYjsxsj()));
					}
					if (ri.equals(DateUtil.dateToStringYMD(hzxxlist.get(indx)
							.getYjsxsj()))) {// 日期相等
						if (rqinsertind == null) {
							rqinsertind = (long) indx; // 插入下标
						}
					}
				}
			}
			// 计算结束日期 顺序查找
			bcjsri = createSaveLastDate(jsts, maoxqcfsxxh,"2",cx);

			// 计算开始下标
			if (shangccfksxh != null) {
				insertind = shangccfksxh.intValue(); // 下一个下标
			} else {
				if (rqinsertind != null) {// 日期开始下标+车身数
					insertind = (int) (rqinsertind + hzchews.longValue());
				}

			}

			if (insertind != null) {// 有开始记录下标
				// 循环计划量
				for (int i = 0; i < hzjhlist.size(); i++) {
					Daxpcsl daxpcsl = hzjhlist.get(i);
					Date riq = daxpcsl.getRiq(); // 上线日期
					long jihsxl = daxpcsl.getJihsxl().longValue(); // 计划上线量
					if (maoxqcfsxxh != null) {// 以前计算过，顺序号后的记录不需要后移一个日期
						isaddinset = true;
					}
					// 循环计划量
					for (int m = 0; m < jihsxl; m++) {
						if (insertind < hzxxlist.size()) { // 在下标之内
							Clddxx2 clddxx = hzxxlist.get(insertind); // 获取总装的下线集合(zzxxlist)中第（zzchews+m）条记录为当前总装上线的车身
							// 如果当日为上线计划日期的起始日期
							// 
							// 判断当前的车辆信息是否存在于车辆上线计划表中如果存在则不插入，如果不存在则插入，插入时上线日期修改为上线日期集合中上线起始日期的下一个日期。

							if (!isaddinset) {// 还需要查询
								// 根据whof 和产线判断在In_clddxx2 是否已存在
								HashMap<String, String> param = new HashMap<String, String>();
								param.put("whof", clddxx.getWhof());
								param.put("scxh", clddxx.getScxh());
								param.put("usercenter", clddxx.getUsercenter());
								List<Clddxx2> clddxxexist = baseDao
										.getSdcDataSource("2")
										.select("hlorder.queryComputeZzsxlistBYwokfAndScxh",
												param);
								if (clddxxexist == null
										|| clddxxexist.size() == 0) {// 变换后处理
									Clddxx2 first = clddxx22Clddxx2(clddxx);
									first.setId(UUID.randomUUID().toString()
											.replaceAll("-", ""));
									first.setScxh(clddxx.getScxh());
									first.setYjsxsj(riq);// 预计上线时间变成循环日期下标
															// +1记录的日期
									first.setCjDate(new Date()); // 创建时间
									first.setLiush(BigDecimal.valueOf(++tmpxh));// 临时序号只在临时表中使用，在clddxx2表中使用的是数据库序号
									first.setLeix("2");
									hzsxlist.add(first);
									isaddinset = true; // 可以开始插入
									if ("whof".equals(first.getWhof()))
									{
										jihsxl++;//空的计划量不算入实际计划量
									}

								} else {
									if (i == 0 && i < hzjhlist.size() - 1) {// 有下一个下标且是第一天
										riq = hzjhlist.get(i + 1).getRiq();// 预计上线时间变成循环日期下标
																			// +1记录的日期
									}
								}
							} else {
								// 根据whof 和产线判断在In_clddxx2 是否已存在
								HashMap<String, String> param = new HashMap<String, String>();
								param.put("whof", clddxx.getWhof());
								param.put("scxh", clddxx.getScxh());
								param.put("usercenter", clddxx.getUsercenter());
								param.put("sxsxh", clddxx.getSxsxh().toString());
								List<Clddxx2> clddxxexist = baseDao
										.getSdcDataSource("2")
										.select("hlorder.queryComputeZzsxlistBYwokfAndScxh",
												param);
								if (clddxxexist == null
										|| clddxxexist.size() == 0) {// 变换后处理
								Clddxx2 temp = clddxx22Clddxx2(clddxx);
								temp.setId(UUID.randomUUID().toString()
										.replaceAll("-", ""));
								temp.setLiush(BigDecimal.valueOf(++tmpxh));// 临时序号只在临时表中使用，在clddxx2表中使用的是数据库序号
								temp.setLeix("2");
								temp.setScxh(clddxx.getScxh());
								temp.setYjsxsj(riq);// 预计上线时间变成循环日期下标 +1记录的日期
								temp.setCjDate(new Date()); // 创建时间
								hzsxlist.add(temp);
								if ("whof".equals(temp.getWhof()))
								{
									jihsxl++;
								}
							}
							}
						}
						insertind++;
					}
				}

				// 插入记录到 in_clddxx2表中,序号已使用数据库序列更改了
				// 根据结束日期，日期<=结束日期时，才插入
				if (hzsxlist.size() > 0 && bcjsri != null) {
					// 修改最后拆分推顺序对应的订单号
					// 处理符合日期和记录条数的正常记录，以及最后保存的记录
					List<Clddxx2> zzsxlistxydyjsri = handComputerResult(
							maoxqcfsxxh, hzjhlist, hzsxlist, bcjsri);
					if (zzsxlistxydyjsri.size() > 0) {
						baseDao.getSdcDataSource("2").executeBatch(
								"hlorder.insertcomputeZzsxlist",
								zzsxlistxydyjsri);
					}
					maoxqcfsxxh = handMaoxqxlHz(maoxqcfsxxh, zzsxlistxydyjsri,//记录最后上线车辆流水
							hzjhlist, outsxh, bcjsri);
					if (maoxqcfsxxh != null) {
						maoxqcfxhmap.put(hzjhlist.get(0).getDaxxh(),
								maoxqcfsxxh);
					}
				}
			}
		}
		return hzsxlist;

	}

	/*
	 * 插入焊装hzxxlist集合中插入N条空数据插入集合的起始位置
	 */
	private List<Clddxx2> insert2Hzxxlist(List<Daxpcsl> hzjhlist,
			List<Clddxx2> hzxxlist, Clddxx cx, Date kaisrq,
			Map<String, DdbhMaoxqsxxh> maoxqcfxhmap) {
		
		for (Daxpcsl daxpcsl : hzjhlist) {
			// (涂装的上线计划量（jihsxl）-涂装的下线计划量（jihxxl）)>0
			int nullleng = daxpcsl.getJihsxl().intValue()
					- daxpcsl.getJihxxl().intValue();
			if (nullleng > 0) {
				// 查询对应日期，计算插入下标
				int insertind = hzxxlist.size() == 0 ? 0 : hzxxlist.size(); // 插入下标
				BigDecimal sxsxh = BigDecimal.ZERO;
				sxsxh = hzxxlist.get(insertind-1).getSxsxh();
				String ri = DateUtil.dateToStringYMD(daxpcsl.getRiq());// 得到年月日
				for (int indx = 0; indx < hzxxlist.size(); indx++) {
					if (ri.compareTo(DateUtil.dateToStringYMD(hzxxlist
							.get(indx).getYjsxsj())) <= 0) {// 日期相等
						insertind = indx; // 插入下标
						sxsxh = hzxxlist.get(insertind).getSxsxh();
						break;
					}
				}
				// 插入

				for (int i = 0; i < nullleng; i++) {
					// 空数据
					Clddxx2 clddxx = new Clddxx2();
					clddxx.setWhof("whof");
					clddxx.setUsercenter(cx.getUsercenter());
					clddxx.setScxh(cx.getUsercenter() + "2" + cx.getScxh());
					clddxx.setYjsxsj(daxpcsl.getRiq()); // 插入下标中记录的上线时间
					clddxx.setLcdv24("lcdv24");
					clddxx.setLcdvbzk("lcdvbzk");
					clddxx.setYjsyhsj("1900-01-01");
					clddxx.setSxsxh(sxsxh);
					hzxxlist.add(insertind, clddxx);
				}

			}
		}
		return hzxxlist;
	}

	/**
	 * 计算焊装车辆数
	 * 
	 * @param cx
	 * @return
	 */
	private BigDecimal findHzchews(Clddxx cx) {
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("usercenter", cx.getUsercenter());
		param.put("scxh", cx.getScxh());
		return (BigDecimal) baseDao.getSdcDataSource("2").selectObject(
				"hlorder.queryfindHzchews", param);
	}

	/*
	 * 获取计算开始日期到计算结束日期之间焊装的上线日期及计划量集合 产线号2
	 */
	@SuppressWarnings("unchecked")
	private List<Daxpcsl> findSppvHzjhlist(Clddxx cx, Date kaisrq,
			int kaisrqtype, Map<String, DdbhMaoxqsxxh> maoxqcfxhmap , Long hzchews) {
		List<Daxpcsl> daxList= new ArrayList<Daxpcsl>();
		List<Daxpcsl> updateList= new ArrayList<Daxpcsl>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("usercenter", cx.getUsercenter());
		param.put("scxh", cx.getScxh());
		param.put("kaisrq", kaisrq);
		if (2 == kaisrqtype) {// min（clddxx） 查询时需要等于
			
			daxList = (ArrayList<Daxpcsl>) baseDao.getSdcDataSource("2").select(
					"hlorder.queryFindSppvZzjhlistHzbyminclddxx", param);
		} else if (1 == kaisrqtype) {// min（clddxx） 查询时不需要等于
			daxList = (ArrayList<Daxpcsl>) baseDao.getSdcDataSource("2").select(
					"hlorder.queryFindSppvZzjhlistHz", param);
		}
		
		// 合并大线排产数量
		mergeDaxpcsl(daxList, kaisrq, cx, maoxqcfxhmap, cx.getUsercenter() + "2" + cx.getScxh());
		
		// 查询转运 车辆数，并对焊装计划量进行修改
		Date riq = new Date();
		param = new HashMap<String, Object>();
		for(int i = 0;i< daxList.size();i++)
		{
			Daxpcsl daxpcsl = daxList.get(i);
			// 根据JT计划输出来的daxpcsl的editor为null
			if (daxpcsl.getEditor() != null && !"4300".equals(daxpcsl.getEditor()))
			{
				continue;
			}
			riq = daxpcsl.getRiq();
			param.put("usercenter", cx.getUsercenter());
			param.put("scxh", cx.getScxh());
			param.put("index", i);
			param.put("yjsxsj", riq);
			ArrayList<Clddxx2> zycqclddList = (ArrayList<Clddxx2>) baseDao
					.getSdcDataSource("2").select(
							"hlorder.queryClddxx2ByZyhcxzycqSum", param);//转运出去
			ArrayList<Clddxx2> zyjlclddList = (ArrayList<Clddxx2>) baseDao
					.getSdcDataSource("2").select(
							"hlorder.queryClddxx2ByZyhcxzyjlSum", param);//转运进来
			// // 已修改  删除重复记录由sql：queryClddxx2ByZyhcxzyjlSum直接完成 避免拼装错误 20160712
			//deleteRepeatTranspost(zycqclddList, cx);
			int zycqCount = 0, zyjlCount = 0;
			if (null != zycqclddList) {
				zycqCount = zycqclddList.size();
			}
			if (null != zyjlclddList) {
				zyjlCount = zyjlclddList.size();
			}
			// 把转运出去的数量加到大线排产数量
			if ( zycqCount >0)
			{
				daxpcsl.setJihsxlWithOutTransport(daxpcsl.getJihxxl());
				daxpcsl.setJihxxl(daxpcsl.getJihxxl().add(new BigDecimal(zycqCount)));
				daxpcsl.setJihsxl(daxpcsl.getJihsxl().add(new BigDecimal(zycqCount)));
				daxpcsl.setChej(daxpcsl.getDaxxh().substring(0, 3));
				if(daxpcsl.getEditor() != null){
					daxpcsl.setEditTime(new Date());
					daxpcsl.setEditor("4300");
					updateList.add(daxpcsl);
					// 更新大线排产数量
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2)
					.executeBatch("hlorder.updatetDaxpcsl", updateList);
				}
			}
			// 把转运进来的数量在大线排产数量减掉
			if ( zyjlCount >0)
			{
				daxpcsl.setJihsxlWithOutTransport(daxpcsl.getJihxxl());
				daxpcsl.setJihxxl(daxpcsl.getJihxxl().subtract(new BigDecimal(zyjlCount)));
				daxpcsl.setJihsxl(daxpcsl.getJihsxl().subtract(new BigDecimal(zyjlCount)));
				daxpcsl.setChej(daxpcsl.getDaxxh().substring(0, 3));
				if(daxpcsl.getEditor() != null){
					daxpcsl.setEditTime(new Date());
					daxpcsl.setEditor("4300");
					updateList.add(daxpcsl);
					// 更新大线排产数量
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2)
					.executeBatch("hlorder.updatetDaxpcsl", updateList);
				}
			}
		}
		
		return daxList;
	}
	
	/**
	 * 合并大线排产数量(根据in_daxpcsl与in_jtpcjh统计每日的上线量进行合并)
	 * @param hzjhlist
	 * @param kaisrq
	 * @param hzxxlist
	 * @param maoxqsxh
	 */
	private void mergeDaxpcsl(List<Daxpcsl> hzjhlist, Date kaisrq, 
			Clddxx cx, Map<String, DdbhMaoxqsxxh> maoxqcfxhmap , String scxh){
		DdbhMaoxqsxxh maoxqsxh = maoxqcfxhmap.get(scxh);
		String strksrq = DateUtil.dateToStringYMD(kaisrq); // 开始日期字符
		
		Map<String, Daxpcsl> mgspcslmap = new HashMap<String, Daxpcsl>();
		
		List<Clddxx2> hzxxlist = new ArrayList<Clddxx2>();
		Clddxx2 temp = new Clddxx2();
		temp.setUsercenter(cx.getUsercenter());
		temp.setScxh(scxh);
		try {
			temp.setYjsxsj(DateUtil.stringToDateYMD("2099-1-1"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		hzxxlist.add(temp);
		// 根据JT计划获取排产数量
		getPaicslByJtpcjh(hzxxlist, mgspcslmap, "2",maoxqsxh);
		if (!mgspcslmap.isEmpty()) {
			int num = 0;
			for (Daxpcsl csl : mgspcslmap.values()) {
				boolean isadd = true;
				for (Daxpcsl zzj : hzjhlist) {
					// 大线线号+日期一致
					if (csl.getDaxxh().equals(zzj.getDaxxh())
							&& DateUtil.dateToStringYMD(csl.getRiq()).equals(
									DateUtil.dateToStringYMD(zzj.getRiq()))) {
						isadd = false;// 已存在
						break;
					}
				}
				if (isadd) {
					if (maoxqsxh != null) {// 已运行过
						if (strksrq.compareTo(DateUtil.dateToStringYMD(csl
								.getRiq())) < 0) {// 开始时间<计划进总装量时间
							hzjhlist.add(csl);
							num++;
						}
					} else {
						hzjhlist.add(csl);
						num++;
					}

				}
			}
			if (num > 0) {// 存在新增加的.按日期正序排序
				Collections.sort(hzjhlist, new Comparator<Daxpcsl>() {
					@Override
					public int compare(Daxpcsl o1, Daxpcsl o2) {
						return DateUtil.dateToStringYMD(o1.getRiq()).compareTo(
								DateUtil.dateToStringYMD(o2.getRiq()));
					}
				});
			}
		}
		mgspcslmap = null;
	}

	/*
	 * @param zzchews 涂装总装的车位数
	 * 
	 * @param tzjhlist 涂装上线集合及计算量
	 * 
	 * @param tzxxlist 涂装的下线集合
	 * 
	 * 
	 * 获取涂装的下线集合(tzxxlist)中第（tzchews+m）条记录为当前涂装上线的车身 上线时间为循环的计算时间（riq）
	 * 产线为（用户中心+3+线号）将涂装的的上线结果做为涂装的下线集合（tzsxlist）同时插入in_clddxx2中
	 * 
	 * 车身 插第一天时要做一个判断，根据whof 和产线
	 * 如果存在，不做任何记录，如果不存在，插入时，与其它的处理不一样，需要变预计上线时间变成循环日期下标
	 * +1记录的日期.如果当前只有一条记录,不做动作. 例如: 10.2 10.5 天 当10.2存在时,取日期10.5的时间 如果
	 */
	private List<Clddxx2> computeTzsxlist(List<Daxpcsl> tzjhlist,
			List<Clddxx2> tzxxlist, Long tzchews, long jsts,
			Map<String, DdbhMaoxqsxxh> maoxqcfxhmap,Clddxx cx) {
		List<Clddxx2> tzsxlist = new ArrayList<Clddxx2>();

		// 推算开始下表
		DdbhMaoxqsxxh maoxqcfsxxh = null;
		if (tzjhlist.size() > 0) {
			maoxqcfsxxh = maoxqcfxhmap.get(tzjhlist.get(0).getDaxxh()); // 获取上次推的顺序号

			// 列表开始序号 。当maoxqcfsxxh不为空时，在zzxxlist中查找，找到订单号一样记录。该记录下标+1就是开始序号
			// 当maoxqcfsxxh为空或没有查找到，按以前时间的第一个+车身数
			// 序号只在大线运营的第一次查找，后期直接+1
			Long shangccfksxh = null; // 上次拆分开始序号
			Long rqinsertind = null; // 按日期查找的插入下标
			Integer insertind = null; // 插入下标
			// 结束日期
			Date bcjsri = null;
			// 涂装上线集合

			boolean isaddinset = false; // 不存在，开始插入标记
			List<String> cldri = new ArrayList<String>(); // cldd的顺序日期
			for (int i = 0; i < tzjhlist.size(); i++) {
				Daxpcsl daxpcsl = tzjhlist.get(i);
				String ri = DateUtil.dateToStringYMD(daxpcsl.getRiq());// 得到年月日
				for (int indx = 0; indx < tzxxlist.size(); indx++) {
					// 查找上次拆分的开始序号
					// 查找上次拆分的开始序号
					// 根据上线顺序号+ 空偏移量
					// 如果能找到cldd记录的顺序号=存储的上线顺序的记录，则更加空偏移量偏移对应的数量。
					// 如果在偏移过程中< 空偏移量的空值，则在最先得到的正常记录的前一个空记录处停止
					// 如果找不到，则找到第一个>存储的上线顺序的记录 -1
					if (maoxqcfsxxh != null
							&& shangccfksxh == null
							&& maoxqcfsxxh.getSxsxh().compareTo(
									tzxxlist.get(indx).getSxsxh()) <= 0) {
						if (maoxqcfsxxh.getSxsxh().equals(
								tzxxlist.get(indx).getSxsxh())) {// 找到对应记录
							if (BigDecimal.ZERO.compareTo(maoxqcfsxxh
									.getCkpyy()) < 0) {// 存在偏移值
								for (int off = indx + 1; off <= indx
										+ maoxqcfsxxh.getCkpyy().intValue(); off++) {
									if (off < tzxxlist.size()) {// 有效下标
										if ("whof".equals(tzxxlist.get(off)
												.getWhof())) {// 存在空值
											shangccfksxh = (long) (off + 1); // 下一位
										} else {
											shangccfksxh = (long) off;// 第一个不为空值
											break;
										}
									}
								}
							} else {// 不存在偏移值
								shangccfksxh = (long) (indx + 1);
							}
						} else {// 没有
							shangccfksxh = (long) indx; // 最先大于的记录
						}
					}
					if (!cldri.contains(DateUtil.dateToStringYMD(tzxxlist.get(
							indx).getYjsxsj()))) {
						cldri.add(DateUtil.dateToStringYMD(tzxxlist.get(indx)
								.getYjsxsj()));
					}
					if (ri.equals(DateUtil.dateToStringYMD(tzxxlist.get(indx)
							.getYjsxsj()))) {// 日期相等
						if (rqinsertind == null) {
							rqinsertind = (long) indx; // 插入下标
						}
					}

				}
			}

			// 计算结束日期 顺序查找
			bcjsri = createSaveLastDate(jsts, maoxqcfsxxh,"3",cx);

			// 计算开始下标
			if (shangccfksxh != null) {
				insertind = shangccfksxh.intValue(); // 下一个下标
			} else {
				if (rqinsertind != null) {// 日期开始下标+车身数
					insertind = (int) (rqinsertind + tzchews.longValue());
				}

			}

			if (insertind != null) {// 有开始记录下标
				// 循环计划量
				if (maoxqcfsxxh != null) {// 以前计算过，顺序号后的记录不需要后移一个日期
					isaddinset = true;
				}
				for (int i = 0; i < tzjhlist.size(); i++) {
					Daxpcsl daxpcsl = tzjhlist.get(i);
					Date riq = daxpcsl.getRiq(); // 上线日期
					long jihsxl = daxpcsl.getJihsxl().longValue(); // 计划上线量
					for (int m = 0; m < jihsxl; m++) {
						if (insertind < tzxxlist.size()) { // 在下标之内
							Clddxx2 clddxx = tzxxlist.get(insertind); // 获取总装的下线集合(zzxxlist)中第（zzchews+m）条记录为当前总装上线的车身
							// 如果当日为上线计划日期的起始日期
							// 
							// 判断当前的车辆信息是否存在于车辆上线计划表中如果存在则不插入，如果不存在则插入，插入时上线日期修改为上线日期集合中上线起始日期的下一个日期。
							if (!isaddinset) {// 还需要查询
								// 根据whof 和产线判断在In_clddxx2 是否已存在
								HashMap<String, String> param = new HashMap<String, String>();
								param.put("whof", clddxx.getWhof());
								param.put("scxh", clddxx.getScxh());
								param.put("usercenter", clddxx.getUsercenter());
								List<Clddxx2> clddxxexist = baseDao
										.getSdcDataSource("2")
										.select("hlorder.queryComputeZzsxlistBYwokfAndScxh",
												param);
								if (clddxxexist == null
										|| clddxxexist.size() == 0) {// 变换后处理
									Clddxx2 first = clddxx22Clddxx2(clddxx);
									first.setId(UUID.randomUUID().toString()
											.replaceAll("-", ""));
									first.setScxh(clddxx.getScxh());
									first.setYjsxsj(riq);// 预计上线时间变成循环日期下标
															// +1记录的日期
									first.setCjDate(new Date()); // 创建时间
									first.setLiush(BigDecimal.valueOf(++tmpxh));// 临时序号只在临时表中使用，在clddxx2表中使用的是数据库序号
									first.setLeix("3");
									tzsxlist.add(first);
									isaddinset = true; // 可以开始插入
									if ("whof".equals(first.getWhof()))
									{
										jihsxl++;
									}
								} else {
									if (i == 0 && i < tzjhlist.size() - 1) {// 有下一个下标且是第一天
										riq = tzjhlist.get(i + 1).getRiq();// 预计上线时间变成循环日期下标
																			// +1记录的日期
									}
								}
							} else {
								HashMap<String, String> param = new HashMap<String, String>();
								param.put("whof", clddxx.getWhof());
								param.put("scxh", clddxx.getScxh());
								param.put("usercenter", clddxx.getUsercenter());
								param.put("sxsxh", clddxx.getSxsxh().toString());
								List<Clddxx2> clddxxexist = baseDao
										.getSdcDataSource("2")
										.select("hlorder.queryComputeZzsxlistBYwokfAndScxh",
												param);
								if (clddxxexist == null
										|| clddxxexist.size() == 0) {// 变换后处理
								Clddxx2 temp = clddxx22Clddxx2(clddxx);
								temp.setId(UUID.randomUUID().toString()
										.replaceAll("-", ""));
								temp.setScxh(clddxx.getScxh());
								temp.setYjsxsj(riq);// 预计上线时间变成循环日期下标 +1记录的日期
								temp.setCjDate(new Date()); // 创建时间
								temp.setLeix("3");
								temp.setLiush(BigDecimal.valueOf(++tmpxh));// 临时序号只在临时表中使用，在clddxx2表中使用的是数据库序号
								tzsxlist.add(temp);
								// 剔除插空的车辆信息
								if ("whof".equals(temp.getWhof()))
								{
									jihsxl++;
								}
							}
							}
						}
						insertind++;
					}
				}
			}

			// 根据结束日期，日期<=结束日期时，才插入
			if (tzsxlist.size() > 0 && bcjsri != null) {
				// 修改最后拆分推顺序对应的订单号
				// 处理符合日期和记录条数的正常记录，以及最后保存的记录
				List<Clddxx2> zzsxlistxydyjsri = handComputerResult(
						maoxqcfsxxh, tzjhlist, tzsxlist, bcjsri);
				// 插入记录到 in_clddxx2表中
				if (zzsxlistxydyjsri.size() > 0) {
					baseDao.getSdcDataSource("2").executeBatch(
							"hlorder.insertcomputeZzsxlist", zzsxlistxydyjsri);
				}
				maoxqcfsxxh = handMaoxqxl(maoxqcfsxxh, zzsxlistxydyjsri,
						tzjhlist, bcjsri);
				if (maoxqcfsxxh != null) {
					maoxqcfxhmap.put(tzjhlist.get(0).getDaxxh(), maoxqcfsxxh);
				}
			}
		}

		return tzsxlist;
	}

	/*
	 * 根据计算天数，计算最后保存日期
	 */
	private Date createSaveLastDate(long jsts, DdbhMaoxqsxxh maoxqcfsxxh, String scxh, Clddxx cx) {
		Map<String, Daxpcsl> mgspcslmap = new HashMap<String, Daxpcsl>();
		// 根据计算天数计算的本次计算结束日期
		Date calculationEndDate = new Date();
		// 查找JT从当前日期开始的排产计划
		getPcslByJtpcjhFromNow(cx,mgspcslmap,scxh);
		// 查找In_daxpcsl从当前日期开始的排产计划
		List<Daxpcsl> daxpcslList = findSppvjhlist(cx,scxh);
		if (!mgspcslmap.isEmpty()) {
			int num = 0;
			for (Daxpcsl csl : mgspcslmap.values()) {
				boolean isadd = true;
				for (Daxpcsl zzj : daxpcslList) {
					// 大线线号+日期一致
					if (csl.getDaxxh().equals(zzj.getDaxxh())
							&& csl.getRiq().getTime() == zzj.getRiq().getTime()) {
						isadd = false;// 已存在
						break;
					}
				}
				if (isadd) {//不存在则添加in_jtpcjh日期
						daxpcslList.add(csl);
						num++;
					}

			}
			if (num > 0) {// 存在新增加的.按日期正序排序
				Collections.sort(daxpcslList, new Comparator<Daxpcsl>() {
					@Override
					public int compare(Daxpcsl o1, Daxpcsl o2) {
						return DateUtil.dateToStringYMD(o1.getRiq()).compareTo(
								DateUtil.dateToStringYMD(o2.getRiq()));
					}
				});
			}
		}
		
		if (daxpcslList != null && daxpcslList.size() > 0)
		{
			if (daxpcslList.size() >= jsts)
			{
				calculationEndDate = daxpcslList.get((int) jsts - 1).getRiq();//根据计算天数取最后计算日期
			} 
			else 
			{
				calculationEndDate = daxpcslList.get(daxpcslList.size() - 1).getRiq();
			}
		} 
		else 
		{
			return null;
		}
		// 判断当前结束日期是否在上次计算日期之前，如果在之前说明该日期无效，无需计算。
		if ( null == maoxqcfsxxh )
		{
			return calculationEndDate;
		}
		else
		{
			String beflasttime = DateUtil.dateToStringYMD(maoxqcfsxxh
					.getCaifrq());// 上次推导后的最后日期
			
			// 比较上次计算结束 日期和本次结束日期，
			if (beflasttime.compareTo(DateUtil
					.dateToStringYMD(calculationEndDate)) < 0) {
				return calculationEndDate;
			}
			else{
				return null;
			}
		}
	}

	/*
	 * 初始化毛需求拆分序号
	 */
	private Map<String, DdbhMaoxqsxxh> initMaoxqcfxhmap() {
		HashMap<String, String> param = new HashMap<String, String>();
		List<DdbhMaoxqsxxh> maoxqsxxhlst = baseDao.getSdcDataSource("2")
				.select("hlorder.queryDdbhMaoxqsxxhAll", param);
		Map<String, DdbhMaoxqsxxh> maoxqcfxhmap = new HashMap<String, DdbhMaoxqsxxh>();
		if (maoxqsxxhlst != null && maoxqsxxhlst.size() > 0) {
			for (DdbhMaoxqsxxh t : maoxqsxxhlst) {
				maoxqcfxhmap.put(t.getDaxxh(), t);
			}
		}
		return maoxqcfxhmap;
	}

	/*
	 * 插入涂装tzxxlist集合中插入N条空数据插入集合的起始位置
	 */
	private List<Clddxx2> insert2Tzxxlist(List<Daxpcsl> tzjhlist,
			List<Clddxx2> tzxxlist, Clddxx cx, Date kaisrq,
			Map<String, DdbhMaoxqsxxh> maoxqcfxhmap) {
		String strksrq = DateUtil.dateToStringYMD(kaisrq); // 开始日期字符
		DdbhMaoxqsxxh maoxqsxh = maoxqcfxhmap.get(cx.getUsercenter() + "3"
				+ cx.getScxh());
		// 合并zzxxlist记录时间，zzxxlist中存在，但在zzjhlist中没有的，增加到zzjhlist中，规则为：上线计划量=下线计划量=记录条数
		// 收集clddxx中对应的线时间+大线线号
		Map<String, Daxpcsl> mgspcslmap = new HashMap<String, Daxpcsl>();
		// for (Clddxx2 cl : tzxxlist) {
		// String key = DateUtil.dateToStringYMD(cl.getYjsxsj()) + "_"
		// + cl.getScxh();// 总装下线时间+大线线号
		// Daxpcsl pcsl = mgspcslmap.get(key);
		// if (pcsl == null) {
		// pcsl = new Daxpcsl();
		// pcsl.setUsercenter(cl.getUsercenter());
		// pcsl.setDaxxh(cl.getScxh());
		// pcsl.setRiq(cl.getYjsxsj());
		// pcsl.setJihsxl(BigDecimal.ZERO);
		// pcsl.setJihxxl(BigDecimal.ZERO);
		// pcsl.setChej(cl.getUsercenter() + "3");
		// }
		// pcsl.setJihsxl(BigDecimal.ONE.add(pcsl.getJihsxl()));// 上线量+1
		// pcsl.setJihxxl(BigDecimal.ONE.add(pcsl.getJihxxl()));// 下线量+1
		// mgspcslmap.put(key, pcsl);
		// }
		// 根据JT计划获取排产数量
		getPaicslByJtpcjh(tzxxlist, mgspcslmap, "3",maoxqsxh);
		if (!mgspcslmap.isEmpty()) {
			int num = 0;
			for (Daxpcsl csl : mgspcslmap.values()) {
				boolean isadd = true;
				for (Daxpcsl zzj : tzjhlist) {
					// 大线线号+日期一致
					if (csl.getDaxxh().equals(zzj.getDaxxh())
							&& DateUtil.dateToStringYMD(csl.getRiq()).equals(
									DateUtil.dateToStringYMD(zzj.getRiq()))) {
						isadd = false;// 已存在
						break;
					}
				}
				if (isadd) {
					if (maoxqsxh != null) {// 已运行过
						if (strksrq.compareTo(DateUtil.dateToStringYMD(csl
								.getRiq())) < 0) {// 开始时间<计划进总装量时间
							tzjhlist.add(csl);
							num++;
						}
					} else {
						tzjhlist.add(csl);
						num++;
					}

				}
			}
			if (num > 0) {// 存在新增加的.按日期正序排序
				Collections.sort(tzjhlist, new Comparator<Daxpcsl>() {
					@Override
					public int compare(Daxpcsl o1, Daxpcsl o2) {
						return DateUtil.dateToStringYMD(o1.getRiq()).compareTo(
								DateUtil.dateToStringYMD(o2.getRiq()));
					}
				});
			}
		}
		mgspcslmap = null;
		for (Daxpcsl daxpcsl : tzjhlist) {
			// (焊装的上线计划量（jihsxl）-焊装的下线计划量（jihxxl）)>0
			int nullleng = daxpcsl.getJihsxl().intValue()
					- daxpcsl.getJihxxl().intValue();
			if (nullleng > 0) {
				// 查询对应日期，计算插入下标
				int insertind = tzxxlist.size() == 0 ? 0 : tzxxlist.size(); // 插入下标
				BigDecimal sxsxh = BigDecimal.ZERO;
				sxsxh = tzxxlist.get(insertind-1).getSxsxh();
				String ri = DateUtil.dateToStringYMD(daxpcsl.getRiq());// 得到年月日
				for (int indx = 0; indx < tzxxlist.size(); indx++) {
					if (ri.compareTo(DateUtil.dateToStringYMD(tzxxlist
							.get(indx).getYjsxsj())) <= 0) {// 日期相等
						insertind = indx; // 插入下标
						sxsxh = tzxxlist.get(insertind).getSxsxh();
						break;
					}
				}
				// 插入

				for (int i = 0; i < nullleng; i++) {
					// 空数据
					Clddxx2 clddxx = new Clddxx2();
					clddxx.setWhof("whof");
					clddxx.setUsercenter(cx.getUsercenter());
					clddxx.setScxh(cx.getUsercenter() + "3" + cx.getScxh());
					// ------------------- hanwu 20160218
					// --------------------------
					// clddxx.setYjsxsj(tzxxlist.get(insertind).getYjsxsj()); //
					// 插入下标中记录的上线时间
					clddxx.setYjsxsj(daxpcsl.getRiq());
					clddxx.setSxsxh(sxsxh);
					// ------------------- hanwu 20160218
					// --------------------------
					clddxx.setLcdv24("lcdv24");
					clddxx.setLcdvbzk("lcdvbzk");
					clddxx.setYjsyhsj("1900-01-01");
					tzxxlist.add(insertind, clddxx);
				}

			}
		}
		return tzxxlist;
	}

	/*
	 * 获取涂装车身数
	 */
	private BigDecimal findTzchews(Clddxx cx) {
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("usercenter", cx.getUsercenter());
		param.put("scxh", cx.getScxh());
		return (BigDecimal) baseDao.getSdcDataSource("2").selectObject(
				"hlorder.queryfindTzchews", param);
	}

	/*
	 * 获取计算开始日期到计算结束日期之间涂装的上线日期及计划量集合
	 */
	@SuppressWarnings("unchecked")
	private List<Daxpcsl> findSppvTzjhlist(Clddxx cx, Date kaisrq,
			int kaisrqtype) {
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("usercenter", cx.getUsercenter());
		param.put("scxh", cx.getScxh());
		param.put("kaisrq", kaisrq);

		if (2 == kaisrqtype) {// min（clddxx） 查询时需要等于
			return (ArrayList<Daxpcsl>) baseDao.getSdcDataSource("2").select(
					"hlorder.queryFindSppvZzjhlistTzbyminclddxx", param);
		} else if (1 == kaisrqtype) {// min（clddxx） 查询时不需要等于
			return (ArrayList<Daxpcsl>) baseDao.getSdcDataSource("2").select(
					"hlorder.queryFindSppvZzjhlistTz", param);
		} else {
			return null;
		}

	}

	/*
	 * 计算涂装开始时间
	 */
	private Map<String, Object> computeTzCxKaisrq(Clddxx cx,
			Map<String, DdbhMaoxqsxxh> maoxqcfxhmap, String daxxh) {
		Map<String, Object> result = new HashMap<String, Object>();
		Date data = null;
		DdbhMaoxqsxxh maoxqsxh = maoxqcfxhmap.get(cx.getUsercenter() + "3"
				+ cx.getScxh());
		if (maoxqsxh != null) {// 不是第一次运行
			try {
				data = DateUtil.stringToDateYMD(DateUtil
						.dateToStringYMD(maoxqsxh.getCaifrq()));
			} catch (ParseException e) {
				log.error(e.getMessage(), e);
			}
		}
		// 这个地方需要修改成JL的最小下线日期（YJJZLSJ）这个字段的意思是 预计出总装时间。
		// 计算开始时间先由上线计划表提供，获取上线计划表相应用户中心和产线的最大yjsxsj，如果记录为空
		// 取in_daxpcsl 表 中的最小日期最为计算开始 
		if (data == null) {
			HashMap<String, String> param = new HashMap<String, String>();
			param.put("usercenter", cx.getUsercenter());
			param.put("scxh", daxxh);
			param.put("startdate", DateUtil
					.dateToStringYMD(new Date()));
			data = (Date) baseDao.getSdcDataSource("2").selectObject(
					"hlorder.queryMaxClddxxSxsj", param);
			if ( null == data || "".equals(data))
			{
				data = new Date();
			}	
			result.put("ksrq", data);
			result.put("type", new Integer(2));
		
		} else {
			result.put("ksrq", data);
			result.put("type", new Integer(1));
		}
		return result;
	}

	/*
	 * 计算焊装开始时间
	 */
	private Map<String, Object> computeHzCxKaisrq(Clddxx cx,
			Map<String, DdbhMaoxqsxxh> maoxqcfxhmap, String daxxh) {
		Map<String, Object> result = new HashMap<String, Object>();
		Date data = null;
		DdbhMaoxqsxxh maoxqsxh = maoxqcfxhmap.get(cx.getUsercenter() + "2"
				+ cx.getScxh());
		if (maoxqsxh != null) {// 不是第一次运行
			try {
				data = DateUtil.stringToDateYMD(DateUtil
						.dateToStringYMD(maoxqsxh.getCaifrq()));
			} catch (ParseException e) {
				log.error(e.getMessage(), e);
			}
		}

		// 这个地方需要修改成JL的最小下线日期（YJJZLSJ）这个字段的意思是 预计出总装时间。
		// 计算开始时间先由上线计划表提供，获取上线计划表相应用户中心和产线的最大yjsxsj，如果记录为空
		// 取in_daxpcsl 表 中的最小日期最为计算开始 
		if (data == null) {
			HashMap<String, String> param = new HashMap<String, String>();
			param.put("usercenter", cx.getUsercenter());
			param.put("scxh", daxxh);
			param.put("startdate", DateUtil
					.dateToStringYMD(new Date()));
			data = (Date) baseDao.getSdcDataSource("2").selectObject(
					"hlorder.queryMaxClddxxSxsj", param);
			if ( null == data || "".equals(data))
			{
				data = new Date();
			}	
			result.put("ksrq", data);
			result.put("type", new Integer(2));
		
		} else {
			result.put("ksrq", data);
			result.put("type", new Integer(1));
		}
		return result;
	}

	/*
	 * 产线集合 select distinct usercenter,scxh from in_clddxx
	 */
	private List<Clddxx> initParam() {
		List<Clddxx> clddxxs = baseDao.getSdcDataSource("2").select(
				"hlorder.queryusercenterAndscxh");
		return clddxxs;
	}

	/*
	 * 查询计算天数
	 */
	private long computeJsts(Clddxx cx) {
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("usercenter", cx.getUsercenter());
		param.put("scxh", cx.getScxh());
		BigDecimal date = (BigDecimal) baseDao
				.getSdcDataSource("2")
				.selectObject("hlorder.queryCxJiesrqBYusercenterAndscxh", param);
		if (date == null) {
			return 0;
		}
		return date.longValue();

	}

	/**
	 * 计算产线总装开始时间
	 * 取上次计算最大的上线日期，如果当前线号下上次计算最大上线日期为空；则取当前线号下JL下线日期（YJJZLSJ）最小上线日期做为起始日期。
	 * 上次计算最大的总装上线日期 select * from select max(yjsxsj) from ddbh_TESt.in_clddxx2
	 * where usercenter=usercenter and scxh=usercenter||5||scxh;Clddxx2 where
	 * usercenter= and SCXH=
	 * 
	 * sppv的最小的上线时间select min(riq) from in_daxpcsl where usercenter=usercenter
	 * and daxxh=usercenter||5||scxh;
	 * 
	 * JL的最小下线日期 select min(yjjzlsj) from in_clddxx where usercenter =
	 * usercenter and scxh = scxh 返回开始时间，当为max(yjsxsj) from ddbh_TESt.in_clddxx2
	 * 时，类型为 1; 后期查询Daxpcsl 时，不带等于
	 * 
	 * 当为min(yjjzlsj) from in_clddxx 时，类型为 2; 后期查询Daxpcsl 时，带等于
	 * 
	 */
	private Map<String, Object> computeCxZzKaisrq(Clddxx cx,
			Map<String, DdbhMaoxqsxxh> maoxqcfxhmap, String daxxh) {
		Map<String, Object> result = new HashMap<String, Object>();
		Date data = null;
		DdbhMaoxqsxxh maoxqsxh = maoxqcfxhmap.get(cx.getUsercenter() + "5"
				+ cx.getScxh());
		if (maoxqsxh != null) {// 不是第一次运行
			try {
				data = DateUtil.stringToDateYMD(DateUtil
						.dateToStringYMD(maoxqsxh.getCaifrq()));
			} catch (ParseException e) {
				log.error(e.getMessage(), e);
			}
		}

		// 这个地方需要修改成JL的最小下线日期（YJJZLSJ）这个字段的意思是 预计出总装时间。
		// 计算开始时间先由上线计划表提供，获取上线计划表相应用户中心和产线的最大yjsxsj，如果记录为空
		// 取in_daxpcsl 表 中的最小日期最为计算开始 
		if (data == null) {
			HashMap<String, String> param = new HashMap<String, String>();
			param.put("usercenter", cx.getUsercenter());
			param.put("scxh", daxxh);
			param.put("startdate", DateUtil
					.dateToStringYMD(new Date()));
			data = (Date) baseDao.getSdcDataSource("2").selectObject(
					"hlorder.queryMaxClddxxSxsj", param);
			if ( null == data || "".equals(data))
			{
				data = new Date();
			}	
			result.put("ksrq", data);
			result.put("type", new Integer(2));
		
		} else {
			result.put("ksrq", data);
			result.put("type", new Integer(1));
		}
		return result;
	}

	/*
	 * 获取计算开始日期到计算结束日期之间总装的上线日期及计划量集合（zzjhlist） select riq,jihsxl,jihxxl from
	 * in_daxpcsl where usercenter=usercenter and daxxh=usercenter||5||scxh and
	 * riq>=kaisrq and riq<jiesrq order by riq 经讨论，查询时，不用< 结束日期
	 */
	private List<Daxpcsl> findSppvZzjhlist(Clddxx cx, Date kaisrq,
			int kaisrqtype) {
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("usercenter", cx.getUsercenter());
		param.put("scxh", cx.getScxh());
		param.put("kaisrq", kaisrq);
		if (2 == kaisrqtype) {// min（clddxx） 查询时需要等于
			return (ArrayList<Daxpcsl>) baseDao.getSdcDataSource("2").select(
					"hlorder.queryFindSppvZzjhlistzzbyminclddxx", param);
		} else if (1 == kaisrqtype) {// min（clddxx） 查询时不需要等于
			return (ArrayList<Daxpcsl>) baseDao.getSdcDataSource("2").select(
					"hlorder.queryFindSppvZzjhlistzz", param);
		} else {
			return null;
		}
	}

	/*
	 * 获取总装的车位数(zzchews) Select chews from ckx_shengcx where
	 * usercenter=usercenter and shengcxbh=usercenter||5||scxh
	 * 
	 * @param cx queryfindZzchews
	 * 
	 * @return
	 */
	private BigDecimal findZzchews(Clddxx cx) {
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("usercenter", cx.getUsercenter());
		param.put("scxh", cx.getScxh());
		return (BigDecimal) baseDao.getSdcDataSource("2").selectObject(
				"hlorder.queryfindZzchews", param);
	}

	/*
	 * 根据产线，获取大于等于计算开始时间 获取排产计划集合（按产线、上线序号 正序排列） 如果是非第一次计算 根据上次的顺序号来查询 >=顺序号
	 * Select usercenter,whof,lcdv24,yjjzlsj,yjsyhsj,sxsxh,lcdvbzk from
	 * in_clddxx c where c.usercenter=usercenter and scxh=scxh and
	 * yjjzlsj>=kaisrqorder by scxh,yjjzlsj
	 * 
	 * Select usercenter,whof,lcdv24,yjjzlsj,yjsyhsj,sxsxh,lcdvbzk from
	 * in_clddxx c where c.usercenter=usercenter and scxh=scxh and
	 * yjjzlsj>=kaisrq order by scxh,yjjzlsj
	 */
	private List<Clddxx> findZzxxlist(Clddxx cx, Date kaisrq,
			Map<String, DdbhMaoxqsxxh> maoxqcfxhmap) {
		DdbhMaoxqsxxh maoxqsxh = maoxqcfxhmap.get(cx.getUsercenter() + "5"
				+ cx.getScxh());
		List<Clddxx> result = null;
		if (maoxqsxh != null) {// 不是第一次运行,按顺序号查
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("usercenter", cx.getUsercenter());
			param.put("scxh", cx.getScxh());
			param.put("sxsxh", maoxqsxh.getSxsxh());
			result = (List<Clddxx>) baseDao.getSdcDataSource("2").select(
					"hlorder.queryfindZzxxlistbylastsxxh", param);
		}
		if (result == null || result.size() == 0) {
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("usercenter", cx.getUsercenter());
			param.put("scxh", cx.getScxh());
			param.put("kaisrq", DateUtil.dateToStringYMD(kaisrq));
			result = (List<Clddxx>) baseDao.getSdcDataSource("2").select(
					"hlorder.queryfindZzxxlist", param);

		}
		return result;

	}

	/*
	 * 循环总装上线日期及计划量,插入对应空数据 zzjhlist 总装上线日期及计划量 zzxxlist 总装下线集合例如
	 * 总装的上线计划量（jihsxl）-总装的下线计划量（jihxxl） 为 2 zzxxlist 已存在 zzxxlist[0]= Clddxx0
	 * ,zzxxlist[1]= Clddxx1 插入后的结果 zxxlist zxxlist[0]= Clddxxnull0,zzxxlist[1]=
	 * Clddxxnull1 zzxxlist[2]= Clddxx0 ,zzxxlist[3]= Clddxx1,z 空数据规则：
	 * 1、whof="whof" 2、序号为统一数据库序列 3、用户中心 循环记录的用户中心 4、预计上线时间 循环记录的预计上线时间 5、生产线
	 * 循环记录的生产线 注：插入时，考虑对应的日期，
	 */
	private List<Clddxx> insert2Zzxxlist(List<Daxpcsl> zzjhlist,
			List<Clddxx> zzxxlist, Clddxx cx, Date kaisrq,
			Map<String, DdbhMaoxqsxxh> maoxqcfxhmap) {
		// 合并zzxxlist记录时间，zzxxlist中存在，但在zzjhlist中没有的，增加到zzjhlist中，规则为：上线计划量=下线计划量=记录条数
		// 收集clddxx中对应的线时间+大线线号
		String strksrq = DateUtil.dateToStringYMD(kaisrq); // 开始日期字符
		DdbhMaoxqsxxh maoxqsxh = maoxqcfxhmap.get(cx.getUsercenter() + "5"
				+ cx.getScxh());
		Map<String, Daxpcsl> mgspcslmap = new HashMap<String, Daxpcsl>();
		// for (Clddxx cl : zzxxlist) {
		//
		// String key = DateUtil.dateToStringYMD(cl.getYjjzlsj()) + "_"
		// + cl.getUsercenter() + "5" + cl.getScxh();// 总装下线时间+大线线号
		// Daxpcsl pcsl = mgspcslmap.get(key);
		// if (pcsl == null) {
		// pcsl = new Daxpcsl();
		// pcsl.setUsercenter(cl.getUsercenter());
		// pcsl.setDaxxh(cl.getUsercenter() + "5" + cl.getScxh());
		// pcsl.setRiq(cl.getYjjzlsj());
		// pcsl.setJihsxl(BigDecimal.ZERO);
		// pcsl.setJihxxl(BigDecimal.ZERO);
		// pcsl.setChej(cl.getUsercenter() + "5");
		// }
		// pcsl.setJihsxl(BigDecimal.ONE.add(pcsl.getJihsxl()));// 上线量+1
		// pcsl.setJihxxl(BigDecimal.ONE.add(pcsl.getJihxxl()));// 下线量+1
		// mgspcslmap.put(key, pcsl);
		//
		// }
		// 根据JT计划获取排产数量
		getPaicslByJtpcjh(zzxxlist, mgspcslmap, "5",maoxqsxh);
		if (!mgspcslmap.isEmpty()) {
			int num = 0;
			for (Daxpcsl csl : mgspcslmap.values()) {
				boolean isadd = true;
				for (Daxpcsl zzj : zzjhlist) {
					// 大线线号+日期一致
					if (csl.getDaxxh().equals(zzj.getDaxxh())
							&& DateUtil.dateToStringYMD(csl.getRiq()).equals(
									DateUtil.dateToStringYMD(zzj.getRiq()))) {
						isadd = false;// 已存在
						break;
					}
				}
				if (isadd) {
					if (maoxqsxh != null) {// 已运行过
						if (strksrq.compareTo(DateUtil.dateToStringYMD(csl
								.getRiq())) < 0) {// 开始时间<计划进总装量时间
							zzjhlist.add(csl);
							num++;
						}
					} else {
						zzjhlist.add(csl);
						num++;
					}
				}
			}
			if (num > 0) {// 存在新增加的.按日期正序排序
				Collections.sort(zzjhlist, new Comparator<Daxpcsl>() {
					@Override
					public int compare(Daxpcsl o1, Daxpcsl o2) {
						return DateUtil.dateToStringYMD(o1.getRiq()).compareTo(
								DateUtil.dateToStringYMD(o2.getRiq()));
					}
				});
			}
		}
		mgspcslmap = null;

		for (Daxpcsl daxpcsl : zzjhlist) {
			// (总装的上线计划量（jihsxl）-总装的下线计划量（jihxxl）)>0
			int nullleng = daxpcsl.getJihsxl().intValue()
					- daxpcsl.getJihxxl().intValue();
			if (nullleng > 0) {
				// 查询对应日期，计算插入下标
				int insertind = zzxxlist.size() == 0 ? 0 : zzxxlist.size(); // 插入下标
				BigDecimal sxsxh = BigDecimal.ZERO;
				sxsxh = zzxxlist.get(insertind-1).getSxsxh();
				String ri = DateUtil.dateToStringYMD(daxpcsl.getRiq());// 得到年月日
				for (int indx = 0; indx < zzxxlist.size(); indx++) {
					if (ri.compareTo(DateUtil.dateToStringYMD(zzxxlist
							.get(indx).getYjjzlsj())) <= 0) {// 日期相等
						insertind = indx; // 插入下标
						sxsxh = zzxxlist.get(insertind).getSxsxh();
						break;
					}
				}
				// 插入
				for (int i = 0; i < nullleng; i++) {
					// 空数据
					Clddxx clddxx = new Clddxx();
					clddxx.setWhof("whof");
					clddxx.setUsercenter(cx.getUsercenter());
					clddxx.setScxh(cx.getScxh());
					clddxx.setShangxsj(daxpcsl.getRiq()); // 插入下标中记录的上线时间
					clddxx.setYjjzlsj(daxpcsl.getRiq()); // 循环的日期
					clddxx.setLcdv24("lcdv24");
					clddxx.setLcdvbzk("lcdvbzk");
					clddxx.setSxsxh(sxsxh);
					try {
						clddxx.setYjsyhsj(DateUtil
								.stringToDateYMD("1900-01-01"));
					} catch (ParseException e) {
						log.error(e.getMessage(), e);
					}
					zzxxlist.add(insertind, clddxx);

				}
			}
		}
		return zzxxlist;
	}

	/**
	 * @param zzchews
	 *            总装的车位数
	 * 
	 * @param zzjhlist
	 *            总装上线集合
	 * 
	 * @param zzxxlist
	 *            总装的下线集合
	 * 
	 * 
	 *            获取总装的下线集合(zzxxlist)中第（zzchews+m）条记录为当前总装上线的车 上线时间为循环的计算时间（riq）
	 *            产线为（用户中心+5+线号）将总装的的上线结果做为涂装的下线集合（zzsxlist）同时插入in_clddxx2中
	 * 
	 *            车身 插第一天时要做一个判断，根据whof 和产线
	 *            如果存在，不做任何记录，如果不存在，插入时，与其它的处理不一样，需要变预计上线时间变成循环日期下标
	 *            如果
	 *            +1记录的日期.如果当前只有一条记录,不做动作. 例如: 10.2 10.5 天 当10.2存在时,取日期10.5的时间
	 * 
	 *            通过计算天数来决定结束日期，结束日期 为在clddxx列表中第计算天数顺位的日期
	 */
	private List<Clddxx2> computeZzsxlist(List<Daxpcsl> zzjhlist,
			List<Clddxx> zzxxlist, Long zzchews, long jsts,
			Map<String, DdbhMaoxqsxxh> maoxqcfxhmap,Clddxx cx,Map<String, Object> ksrqmap) {
		List<Clddxx2> zzsxlist = new ArrayList<Clddxx2>();
		// 推算开始下表
		Long zzscsxsxh = null;// 总装上次推导的顺序号
		DdbhMaoxqsxxh maoxqcfsxxh = null;
		if (zzjhlist.size() > 0) {
			maoxqcfsxxh = maoxqcfxhmap.get(zzjhlist.get(0).getDaxxh()); // 获取上次推的顺序号
			if (maoxqcfsxxh != null) {
				zzscsxsxh = maoxqcfsxxh.getSxsxh().longValue(); // 得到原始的
			}
		} else {
			if (zzxxlist != null && zzxxlist.size() > 0) {
				maoxqcfsxxh = maoxqcfxhmap.get(zzxxlist.get(0).getUsercenter()
						+ "5" + zzxxlist.get(0).getScxh()); // 获取上次推的顺序号
				if (maoxqcfsxxh != null) {
					zzscsxsxh = maoxqcfsxxh.getSxsxh().longValue(); // 得到原始的
				}
			}
		}
		if (zzjhlist.size() > 0) {
			// 列表开始序号 。当maoxqcfsxxh不为空时，在zzxxlist中查找，找到订单号一样记录。该记录下标+1就是开始序号
			// 当maoxqcfsxxh为空或没有查找到，按以前时间的第一个+车身数
			// 序号只在大线运营的第一次查找，后期直接+1
			Long shangccfksxh = null; // 上次拆分找到的开始序号
			Long rqinsertind = null; // 按日期查找的插入下标
			Integer insertind = null; // 插入下标
			// 结束日期
			Date bcjsri = null;
			// 总装上线集合

			boolean isaddinset = false; // 不存在，开始插入标记
			List<String> cldri = new ArrayList<String>(); // cldd的顺序日期
			for (int i = 0; i < zzjhlist.size(); i++) {
				Daxpcsl daxpcsl = zzjhlist.get(i);
				String ri = DateUtil.dateToStringYMD(daxpcsl.getRiq());// 得到年月日
				for (int indx = 0; indx < zzxxlist.size(); indx++) {
					// 查找上次拆分的开始序号
					// 根据上线顺序号+ 空偏移量
					// 如果能找到cldd记录的顺序号=存储的上线顺序的记录，则更加空偏移量偏移对应的数量。
					// 如果在偏移过程中< 空偏移量的空值，则在最先得到的正常记录的前一个空记录处停止
					// 如果找不到，则找到第一个>存储的上线顺序的记录 -1
					if (maoxqcfsxxh != null
							&& shangccfksxh == null
							&& zzxxlist.get(indx).getSxsxh() != null
							&& maoxqcfsxxh.getSxsxh().compareTo(
									zzxxlist.get(indx).getSxsxh()) <= 0) {
						if (maoxqcfsxxh.getSxsxh().equals(
								zzxxlist.get(indx).getSxsxh())) {// 找到对应记录
							if (BigDecimal.ZERO.compareTo(maoxqcfsxxh
									.getCkpyy()) < 0) {// 存在偏移值
								for (int off = indx + 1; off <= indx
										+ maoxqcfsxxh.getCkpyy().intValue(); off++) {
									if (off < zzxxlist.size()) {// 有效下标
										if ("whof".equals(zzxxlist.get(off)
												.getWhof())) {// 存在空值
											shangccfksxh = (long) (off + 1); // 下一位
										} else {
											shangccfksxh = (long) off;// 第一个不为空值
											break;
										}
									}
								}
							} else {// 不存在偏移值
								shangccfksxh = (long) (indx + 1);
							}
						} else {// 没有
							shangccfksxh = (long) indx; // 最先大于的记录
						}
					}
					if (!cldri.contains(DateUtil.dateToStringYMD(zzxxlist.get(//没有用到
							indx).getYjjzlsj()))) {
						cldri.add(DateUtil.dateToStringYMD(zzxxlist.get(indx)
								.getYjjzlsj()));
					}
					if (ri.equals(DateUtil.dateToStringYMD(zzxxlist.get(indx)
							.getYjjzlsj()))) {// 日期相等
						if (rqinsertind == null) {
							rqinsertind = (long) indx; // 插入下标
						}
					}
				}
			}

			// 计算结束日期 顺序查找
			bcjsri = createSaveLastDate(jsts, maoxqcfsxxh,"5",cx);

			// 计算开始下标
			if (shangccfksxh != null) {
				insertind = shangccfksxh.intValue(); // 下一个下标
			} else {
				if (rqinsertind != null) {// 日期开始下标+车身数
					insertind = (int) (rqinsertind + zzchews.longValue());
				}

			}
			if (insertind != null) {// 有开始记录下标
				// 循环计划量
				if (maoxqcfsxxh != null) {// 以前计算过，顺序号后的记录不需要后移一个日期
					isaddinset = true;
				}
				for (int i = 0; i < zzjhlist.size(); i++) {
					Daxpcsl daxpcsl = zzjhlist.get(i);
					Date riq = daxpcsl.getRiq(); // 上线日期3
					long jihsxl = daxpcsl.getJihsxl().longValue(); // 计划上线量
					for (int m = 0; m < jihsxl; m++) {
						if (insertind < zzxxlist.size()) { // 在下标之内
							Clddxx clddxx = zzxxlist.get(insertind); // 获取总装的下线集合(zzxxlist)中第insertind条记录为当前总装上线
							// 如果当日为上线计划日期的起始日期
							// 
							// 判断当前的车辆信息是否存在于车辆上线计划表中如果存在则不插入，如果不存在则插入，插入时上线日期修改为上线日期集合中上线起始日期的下一个日期。

							if (!isaddinset) {// 还需要查询
								// 根据whof 和产线判断在In_clddxx2 是否已存在

								HashMap<String, String> param = new HashMap<String, String>();
								param.put("whof", clddxx.getWhof());
								param.put("scxh", clddxx.getUsercenter() + "5"
										+ clddxx.getScxh());
								param.put("usercenter", clddxx.getUsercenter());
								List<Clddxx2> clddxxexist = baseDao
										.getSdcDataSource("2")
										.select("hlorder.queryComputeZzsxlistBYwokfAndScxh",
												param);
								if (clddxxexist == null
										|| clddxxexist.size() == 0) {// 变换后处理
									Clddxx2 first = clddxx2Clddxx2(clddxx);
									first.setId(UUID.randomUUID().toString()
											.replaceAll("-", ""));
									first.setScxh(clddxx.getUsercenter() + "5"
											+ clddxx.getScxh());
									first.setYjsxsj(riq);// 预计上线时间变成循环日期下标
									first.setLiush(BigDecimal.valueOf(++tmpxh));// 临时序号只在临时表中使用，在clddxx2表中使用的是数据库序号
									first.setCjDate(new Date()); // 创建时间
									first.setLeix("5");
									if ("whof".equals(first.getWhof()))
									{
										jihsxl++;
									}
									zzsxlist.add(first);
									isaddinset = true; // 可以开始插入

								} else {
									if (i == 0 && i < zzjhlist.size() - 1) {// 有下一个下标且是第一天
										riq = zzjhlist.get(i + 1).getRiq();// 预计上线时间变成循环日期下标
																			// +1记录的日期
									}
								}
							} else {
								HashMap<String, String> param = new HashMap<String, String>();
								param.put("whof", clddxx.getWhof());
								param.put("scxh", clddxx.getUsercenter() + "5"
										+ clddxx.getScxh());
								param.put("usercenter", clddxx.getUsercenter());
								param.put("sxsxh", clddxx.getSxsxh().toString());
								List<Clddxx2> clddxxexist = baseDao
										.getSdcDataSource("2")
										.select("hlorder.queryComputeZzsxlistBYwokfAndScxh",
												param);
								if (clddxxexist == null
										|| clddxxexist.size() == 0) {// 变换后处理
								Clddxx2 temp = clddxx2Clddxx2(clddxx);
								temp.setId(UUID.randomUUID().toString()
										.replaceAll("-", ""));
								temp.setScxh(clddxx.getUsercenter() + "5"
										+ clddxx.getScxh());
								temp.setYjsxsj(riq);// 预计上线时间变成循环日期下标 +1记录的日期
								temp.setCjDate(new Date()); // 创建时间
								temp.setLiush(BigDecimal.valueOf(++tmpxh));// 临时序号只在临时表中使用，在clddxx2表中使用的是数据库序号
								temp.setLeix("5");
								if ("whof".equals(temp.getWhof()))
								{
									jihsxl++;
								}
								zzsxlist.add(temp);
								} 
							}
						}
						insertind++;
					}
				}
			}

			// 插入记录到 in_clddxx2表中,序号已使用数据库序列更改了
			// 根据结束日期，日期<=结束日期时，才插入
			if (zzsxlist.size() > 0 && bcjsri != null) {

				// 修改最后拆分推顺序对应的订单号
				// 处理符合日期和记录条数的正常记录，以及最后保存的记录
				List<Clddxx2> zzsxlistxydyjsri = handComputerResult(
						maoxqcfsxxh, zzjhlist, zzsxlist, bcjsri);
				if (zzsxlistxydyjsri.size() > 0) {
					baseDao.getSdcDataSource("2").executeBatch(
							"hlorder.insertcomputeZzsxlist", zzsxlistxydyjsri);
				}
				maoxqcfsxxh = handMaoxqxl(maoxqcfsxxh, zzsxlistxydyjsri,
						zzjhlist, bcjsri);
				if (maoxqcfsxxh != null) {
					maoxqcfxhmap.put(zzjhlist.get(0).getDaxxh(), maoxqcfsxxh);
				}

			}
		}

		// 在in_clddxx2中获取 涂装流水号<=流水号 <= 总装流水号的记录，并合并到涂装上线列表中
		if (zzscsxsxh != null) {
			DdbhMaoxqsxxh maoxqsxh3 = null;
			if (zzjhlist.size() > 0) {
				maoxqsxh3 = maoxqcfxhmap.get(zzjhlist.get(0).getDaxxh()
						.substring(0, 2)
						+ "3" + zzjhlist.get(0).getDaxxh().substring(3)); // 获取上次推的顺序号
			} else {
				if (zzxxlist != null && zzxxlist.size() > 0) {
					maoxqsxh3 = maoxqcfxhmap.get(zzxxlist.get(0)
							.getUsercenter() + "3" + zzxxlist.get(0).getScxh()); // 获取上次推的顺序号
				}
			}
			if (maoxqsxh3 != null
					&& (maoxqsxh3.getSxsxh().longValue() <= zzscsxsxh)) {// 获取在in_clddxx2中获取
																			// 涂装流水号<=流水号
																			// <=
																			// 总装流水号的记录，并合并到涂装上线列表中
				HashMap<String, Object> param = new HashMap<String, Object>();
				param.put("tzxxh", maoxqsxh3.getSxsxh().longValue());
				param.put("zzxxh", zzscsxsxh);
				param.put("scxh", maoxqcfsxxh.getDaxxh()); // 总装线号
				param.put("usercenter", maoxqcfsxxh.getDaxxh().substring(0, 2));
				List<Clddxx2> saoclddxxexist = baseDao.getSdcDataSource("2")
						.select("hlorder.queryComputeZzsxlistBYtzxxhhzzxxh",
								param);// 缺少的
				if (saoclddxxexist != null && saoclddxxexist.size() > 0) {
					for (int i = saoclddxxexist.size() - 1; i >= 0; i--) {
						zzsxlist.add(0, saoclddxxexist.get(i));
					}
				}
			}else if(maoxqsxh3==null && ksrqmap.get("ksrq")!=null){
				HashMap<String, Object> param = new HashMap<String, Object>();
				param.put("ksrq", (Date) ksrqmap.get("ksrq"));
				param.put("zzxxh", zzscsxsxh);
				param.put("tzscxh",	zzxxlist.get(0)
				.getUsercenter() + "3" + zzxxlist.get(0).getScxh()); //涂装线号
				param.put("scxh", maoxqcfsxxh.getDaxxh()); // 总装线号
				param.put("usercenter", maoxqcfsxxh.getDaxxh().substring(0, 2));
				List<Clddxx2> saoclddxxexist = baseDao.getSdcDataSource("2")
						.select("hlorder.queryComputeZzsxlistBYtzKsrq",
								param);// 缺少的
				if (saoclddxxexist != null && saoclddxxexist.size() > 0) {
					for (int i = saoclddxxexist.size() - 1; i >= 0; i--) {
						zzsxlist.add(0, saoclddxxexist.get(i));
					}
				}
			}
		}
		// 将总装变成涂装，需要将产线等变过来
		if (zzsxlist.size() > 0) {
			for (Clddxx2 cl2 : zzsxlist) {
				String scxh = cl2.getScxh();
				cl2.setScxh(scxh.substring(0, 2) + "3" + scxh.substring(3));
			}
		}

		return zzsxlist;

	}

	/*
	 * 将推导出来的上线计划，按保存日期、以及对应日期的上线计划量=具体记录数 来截取相应的正常记录
	 */
	private List<Clddxx2> handComputerResult(DdbhMaoxqsxxh maoxqcfsxxh,
			List<Daxpcsl> zzjhlist, List<Clddxx2> zzsxlist, Date bcjsri) {
		Map<String, List<Clddxx2>> riqsl = new HashMap<String, List<Clddxx2>>(); // 每天对应的记录数，
		// 如果记录数不等于同日期的上线计划量，则不保存，该日期后的数据也不保存
		for (Clddxx2 t : zzsxlist) {
			String rqkey = DateUtil.dateToStringYMD(t.getYjsxsj());
			if (rqkey.compareTo(DateUtil.dateToStringYMD(bcjsri)) <= 0) {// <=结束时间
				List<Clddxx2> num = riqsl.get(rqkey); // 对应日期的记录
				if (num == null) {
					num = new ArrayList<Clddxx2>();
				}
				num.add(t);
				riqsl.put(rqkey, num); // 对应日期的记录数
			}
		}
		List<Clddxx2> zzsxlistxydyjsri = new ArrayList<Clddxx2>();// 从
																	// 小于等于结束日期的记录临时记录队列中，找出记录数等于上线量的正常推算记录
		if (!riqsl.isEmpty()) {// 不为空
			for (Daxpcsl dax : zzjhlist) {
				String rqkey = DateUtil.dateToStringYMD(dax.getRiq());
				List<Clddxx2> num = riqsl.get(rqkey); // 对应日期的记录
				if (num != null) {// 记录数等于上线量的正常推算保存
					List<Clddxx2> tmp = new ArrayList<Clddxx2>();
					for(Clddxx2 clddxx2 :num)
					{
						if (!"whof".equals(clddxx2.getWhof()))
						{
							tmp.add(clddxx2);
						}
					}
					if ( tmp.size() == dax.getJihsxl().intValue())
					{
						zzsxlistxydyjsri.addAll(num);
					}
				} else if ((num == null || num.size() == 0)
						&& dax.getJihsxl().intValue() == 0) {
					// 越过
				} else {
					break;// 对应日期没有记录或不等于上线数，则退出，不再保存当前日期和以后的日期的记录
				}
			}
		}
		return zzsxlistxydyjsri;
	}

	/*
	 * 处理本次最后推导的标记记录 。按最后一个实际记录+空记录偏移量来保存线序号和订单号
	 */
	private DdbhMaoxqsxxh handMaoxqxl(DdbhMaoxqsxxh maoxqcfsxxh,
			List<Clddxx2> zzsxlistxydyjsri, List<Daxpcsl> zzjhlist, Date bcjsri) {
		if (zzsxlistxydyjsri != null && zzsxlistxydyjsri.size() > 0) {
			int koff = 0;
			int insertidex = zzsxlistxydyjsri.size() - 1;
			for (int lastindx = zzsxlistxydyjsri.size() - 1; lastindx >= 0; lastindx--) {
				if ("whof".equals(zzsxlistxydyjsri.get(lastindx).getWhof())) {// 是空插入记录
					koff++;
					insertidex = lastindx - 1;
				} else {// 第一个正常数据
					insertidex = lastindx;
					break;
				}
			}
			if (insertidex >= 0) {// 倒叙排列的最先第一个正常记录
				if (maoxqcfsxxh == null) {
					maoxqcfsxxh = new DdbhMaoxqsxxh();
				}
				maoxqcfsxxh.setCaifrq(zzsxlistxydyjsri.get(insertidex)
						.getYjsxsj());
				maoxqcfsxxh.setDaxxh(zzjhlist.get(0).getDaxxh());
				maoxqcfsxxh.setSxsxh(zzsxlistxydyjsri.get(insertidex)
						.getSxsxh());
				maoxqcfsxxh.setWhof(zzsxlistxydyjsri.get(insertidex).getWhof());
				maoxqcfsxxh.setCkpyy(BigDecimal.valueOf(koff));
			} else {// 全部为空记录
				if (maoxqcfsxxh != null) {
					maoxqcfsxxh.setCaifrq(zzsxlistxydyjsri.get(
							zzsxlistxydyjsri.size() - 1).getYjsxsj());
					maoxqcfsxxh.setCkpyy(maoxqcfsxxh.getCkpyy().add(
							BigDecimal.valueOf(koff)));
				}
			}
		} else {// 可能计算天数为空
			if (maoxqcfsxxh != null) {
				if (zzjhlist != null && zzjhlist.size() > 0) {
					for (Daxpcsl dax : zzjhlist) {
						String rqkey = DateUtil.dateToStringYMD(dax.getRiq());
						if (rqkey.compareTo(DateUtil.dateToStringYMD(bcjsri)) <= 0) {// <=结束时间
							if (0 == dax.getJihsxl().intValue()) {
								maoxqcfsxxh.setCaifrq(dax.getRiq());
							} else { // 不为0，说明数据不满足
								break;
							}
						}
					}

				}
			}
		}

		return maoxqcfsxxh;
	}

	/*
	 * 焊装处理本次最后推导的标记记录 。按最后一个没有转运出去的实际记录+空记录偏移量来保存线序号和订单号
	 */
	private DdbhMaoxqsxxh handMaoxqxlHz(DdbhMaoxqsxxh maoxqcfsxxh,
			List<Clddxx2> zzsxlistxydyjsri, List<Daxpcsl> zzjhlist,
			Map<String, BigDecimal> outsxh, Date bcjsri) {

		if (zzsxlistxydyjsri != null && zzsxlistxydyjsri.size() > 0) {
			int koff = 0;
			int insertidex = zzsxlistxydyjsri.size() - 1;
			for (int lastindx = zzsxlistxydyjsri.size() - 1; lastindx >= 0; lastindx--) {
				if ("whof".equals(zzsxlistxydyjsri.get(lastindx).getWhof())) {// 是空插入记录
					koff++;
					insertidex = lastindx - 1;
				} else {// 第一个 没有转运出去正常数据
					if ("1".equals(zzsxlistxydyjsri.get(lastindx)
							.getNooutflag())) {// 没有转运出去的标记
						insertidex = lastindx;
						break;
					} else {// 转运出去的标记
						koff++;
						insertidex = lastindx - 1;
					}

				}
			}
			if (insertidex >= 0) {// 倒叙排列的最先第一个没有转运出去正常记录
				if (maoxqcfsxxh == null) {
					maoxqcfsxxh = new DdbhMaoxqsxxh();
				}
				maoxqcfsxxh.setCaifrq(zzsxlistxydyjsri.get(insertidex)
						.getYjsxsj());
				maoxqcfsxxh.setDaxxh(zzjhlist.get(0).getDaxxh());
				maoxqcfsxxh.setSxsxh(zzsxlistxydyjsri.get(insertidex)
						.getSxsxh());
				maoxqcfsxxh.setWhof(zzsxlistxydyjsri.get(insertidex).getWhof());
				maoxqcfsxxh.setCkpyy(BigDecimal.valueOf(koff));
			} else {// 全部为空记录
				if (maoxqcfsxxh != null) {
					maoxqcfsxxh.setCaifrq(zzsxlistxydyjsri.get(
							zzsxlistxydyjsri.size() - 1).getYjsxsj());
					maoxqcfsxxh.setCkpyy(maoxqcfsxxh.getCkpyy().add(
							BigDecimal.valueOf(koff)));
				}
			}
			// 处理全部转运记录，记录产线最大的转运已处理的记录

			insertidex = zzsxlistxydyjsri.size() - 1;
			Map<String, BigDecimal> maxmap = new HashMap<String, BigDecimal>();
			for (int lastindx = zzsxlistxydyjsri.size() - 1; lastindx >= 0; lastindx--) {
				Clddxx2 tmp = zzsxlistxydyjsri.get(lastindx);
				if (!"whof".equals(tmp.getWhof())
						&& "0".equals(zzsxlistxydyjsri.get(lastindx)
								.getNooutflag())) {// 转运的记录
					BigDecimal max = maxmap.get(tmp.getOutscxh());// 转移产线

					if (max == null) {
						max = tmp.getSxsxh();
					} else {
						if (max.compareTo(tmp.getSxsxh()) < 0) {
							max = tmp.getSxsxh();
						}
					}
					maxmap.put(tmp.getOutscxh(), max);
				}
			}
			if (!maxmap.isEmpty()) {
				for (String key : maxmap.keySet()) {
					outsxh.put(key, maxmap.get(key));
				}
			}
		} else {// 可能计算天数为空
			if (maoxqcfsxxh != null) {
				if (zzjhlist != null && zzjhlist.size() > 0) {
					for (Daxpcsl dax : zzjhlist) {
						String rqkey = DateUtil.dateToStringYMD(dax.getRiq());
						if (rqkey.compareTo(DateUtil.dateToStringYMD(bcjsri)) <= 0) {// <=结束时间
							if (0 == dax.getJihsxl().intValue()) {
								maoxqcfsxxh.setCaifrq(dax.getRiq());
							} else { // 不为0，说明数据不满足
								break;
							}
						}
					}

				}
			}
		}

		return maoxqcfsxxh;
	}

	/*
	 * 将Clddxx 变成Clddxx2类型
	 */
	private Clddxx2 clddxx2Clddxx2(Clddxx clddxx) {
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		if (clddxx != null) {
			Clddxx2 cl2 = new Clddxx2();
			cl2.setUsercenter(clddxx.getUsercenter());
			cl2.setWhof(clddxx.getWhof());
			cl2.setLcdv24(clddxx.getLcdv24());
			cl2.setYplbj(clddxx.getYplbj());
			cl2.setScxh(clddxx.getScxh());
			if (clddxx.getYjsyhsj() != null) {
				cl2.setYjsyhsj(sd.format(clddxx.getYjsyhsj()));
			}
			cl2.setSxsxh(clddxx.getSxsxh());
			cl2.setLcdv(clddxx.getLcdv());
			cl2.setCjDate(clddxx.getCjDate());
			cl2.setClzt(clddxx.getClzt());
			cl2.setLcdvbzk(clddxx.getLcdvbzk());
			return cl2;
		}
		return null;
	}

	/*
	 * 将Clddxx2 变成Clddxx2类型
	 */
	private Clddxx2 clddxx22Clddxx2(Clddxx2 clddxx) {
		if (clddxx != null) {
			Clddxx2 cl2 = new Clddxx2();
			cl2.setUsercenter(clddxx.getUsercenter());
			cl2.setWhof(clddxx.getWhof());
			cl2.setLcdv24(clddxx.getLcdv24());
			cl2.setYplbj(clddxx.getYplbj());
			cl2.setYjsxsj(clddxx.getYjsxsj());
			cl2.setScxh(clddxx.getScxh());
			cl2.setYjsyhsj(clddxx.getYjsyhsj());
			cl2.setSxsxh(clddxx.getSxsxh());
			cl2.setLcdv(clddxx.getLcdv());
			cl2.setLeix(clddxx.getLeix());
			cl2.setCjDate(clddxx.getCjDate());
			cl2.setClzt(clddxx.getClzt());
			cl2.setLcdvbzk(clddxx.getLcdvbzk());
			cl2.setShul(clddxx.getShul());
			cl2.setNooutflag(clddxx.getNooutflag());
			cl2.setOutscxh(clddxx.getOutscxh());
			return cl2;
		}
		return null;
	}

	/*
	 * 汇总上线计划根据 usercenter,lcdv24,scxh,yjsyhsj,lcdvbzk,yjsxsj
	 * 汇总数量(in_clddxx_tmp)select
	 * c.usercenter,c.lcdv24,c.lcdvbzk,c.yjjzlsj,c.yjsyhsj,count(*) shul from
	 * in_clddxx2 c group by c.usercenter,c.lcdv24,c.lcdvbzk,c.yjjzlsj,c.yjsyhsj
	 * 然后变化后插入in_clddxx_tmp表
	 * 
	 * @return
	 */
	private void sumSxjh() {
		HashMap<String, Object> param = new HashMap<String, Object>();
		// 汇总上线计划
		List<Clddxx2> sumclddxx2lists = (List<Clddxx2>) baseDao
				.getSdcDataSource("2")
				.select("hlorder.querymaoxqcfsxhz", param);
		if (sumclddxx2lists != null && sumclddxx2lists.size() > 0) {
			List<Clddxx2Tmp> tmplists = new ArrayList<Clddxx2Tmp>(
					sumclddxx2lists.size());
			for (Clddxx2 clddxx2 : sumclddxx2lists) {
				Clddxx2Tmp cld2 = clddxx22Clddxx2Tmp(clddxx2);
				tmplists.add(cld2);
			}
			// 批量插入
			baseDao.getSdcDataSource("2").executeBatch(
					"hlorder.insertClddxx2Tmp", tmplists);
		}
	}

	/*
	 * 转换 Clddxx2对象为Clddxx2Tmp
	 * 
	 * @param clddxx2
	 * 
	 * @return
	 */
	private Clddxx2Tmp clddxx22Clddxx2Tmp(Clddxx2 clddxx2) {
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		Clddxx2Tmp cl2tmp = null;
		if (clddxx2 != null) {
			cl2tmp = new Clddxx2Tmp();
			cl2tmp.setId(clddxx2.getId());
			cl2tmp.setLcdv24(clddxx2.getLcdv24());
			cl2tmp.setLcdvbzk(clddxx2.getLcdvbzk());
			cl2tmp.setScxh(clddxx2.getScxh());
			cl2tmp.setUsercenter(clddxx2.getUsercenter());
			cl2tmp.setYjsyhsj(clddxx2.getYjsyhsj());
			if (clddxx2.getShul() != null) {
				cl2tmp.setShul(new BigDecimal(clddxx2.getShul()));
			}
			if (clddxx2.getYjsxsj() != null) {
				cl2tmp.setYjsxsj(sd.format(clddxx2.getYjsxsj()));
			}

			return cl2tmp;
		}
		return cl2tmp;
	}

	/*
	 * 清空中间表
	 */
	private void clearMidtable() {
		CommonFun.logger.info("清空毛需求拆分中间表清理开始");
		// 删除汇总上线计划中间表IN_CLDDXX2_TMP全部记录
		baseDao.getSdcDataSource("2")
				.execute("hlorder.deleteClddxx2TmpAlldata");
		// 删除转运后上线计划中间表IN_CLDDXX_ZYHTMP全部记录
		baseDao.getSdcDataSource("2").execute(
				"hlorder.deleteClddxxZyhTmpAlldata");
		// 删除转运前上线计划中间表IN_CLDDXX_ZYHTMP全部记录
		baseDao.getSdcDataSource("2").execute(
				"hlorder.deleteClddxxZyqTmpAlldata");
		CommonFun.logger.info("清空毛需求拆分中间表清理结束");
	}

	/**
	 * 根据JT计划向大线排产数量插入数据
	 */
	private void fillDaxpcsl(Map<String, DdbhMaoxqsxxh> maoxqcfxhmap) {
		// 查询JL计划的最近九天的每日排产数量
		List<Daxpcsl> jlpcslList = baseDao.getSdcDataSource(
				ConstantDbCode.DATASOURCE_EXTENDS2).select(
				"hlorder.queryShulFromJtpcjh2");
		// 查询大线排产数量
		List<Daxpcsl> daxpcslList = baseDao.getSdcDataSource(
				ConstantDbCode.DATASOURCE_EXTENDS2).select(
				"hlorder.queryAllDaxpcsl");
		// 封装为map
		Map<String, Daxpcsl> jlpcslMap = wrapAsMap(jlpcslList);
		// 封装为map
		Map<String, Daxpcsl> daxpcslMap = wrapAsMap(daxpcslList);
		Iterator<Entry<String, Daxpcsl>> iter = jlpcslMap.entrySet().iterator();
		List<Daxpcsl> insertList = new ArrayList<Daxpcsl>();
		List<Daxpcsl> updateList = new ArrayList<Daxpcsl>();
		while (iter.hasNext()) {
			Entry<String, Daxpcsl> entry = iter.next();
			Daxpcsl daxpcsl = entry.getValue();
			daxpcsl.setChej(daxpcsl.getDaxxh().substring(0, 3));
			daxpcsl.setEditTime(new Date());
			daxpcsl.setEditor("4300");
			if (daxpcslMap.containsKey(entry.getKey())) { // 大线排产数量表已存在则更新
				if(maoxqcfxhmap.containsKey(daxpcsl.getDaxxh())){	// 不是第一次排产
					Date caifrq = maoxqcfxhmap.get(daxpcsl.getDaxxh()).getCaifrq();
					if(daxpcsl.getRiq().compareTo(caifrq) > 0){	// 只更新拆分日期之后的大线排产数量
						updateList.add(daxpcsl);
					}
				}
			} else { // 不存在则插入
				daxpcsl.setCreateTime(new Date());
				daxpcsl.setCreator("4300");
				insertList.add(daxpcsl);
			}
		}
		// 插入大线排产数量
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2)
				.executeBatch("hlorder.insertDaxpcsl", insertList);
		// 更新大线排产数量
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2)
				.executeBatch("hlorder.updatetDaxpcsl", updateList);
	}

	/**
	 * 将list封装为Map
	 * 
	 * @param list
	 * @return
	 */
	private Map<String, Daxpcsl> wrapAsMap(List<Daxpcsl> list) {
		Map<String, Daxpcsl> resultMap = new HashMap<String, Daxpcsl>();
		if (list != null) {
			for (Daxpcsl daxpcsl : list) {
				// key = 用户中心 + 大线线号 + 日期
				String key = daxpcsl.getUsercenter() + daxpcsl.getDaxxh()
						+ DateUtil.dateToStringYMD(daxpcsl.getRiq());
				if (!resultMap.containsKey(key)) {
					resultMap.put(key, daxpcsl);
				}
			}
		}
		return resultMap;
	}

	/**
	 * 获取JT排产数量
	 * 
	 * @param list
	 * @param mgspcslmap
	 * @param line
	 */
	private void getPaicslByJtpcjh(List<?> list,
			Map<String, Daxpcsl> mgspcslmap, String line,DdbhMaoxqsxxh maoxqsxh) {
		if (list != null && list.size() > 0) {
			// 查询参数map
			Map<String, Object> paramMap = new HashMap<String, Object>();
			String usercenter = null; // 用户中心
			String zzx = null; // 线号
			Date beginDate = null; // 计划开始日期
			Date endDate = null; // 计划结束日期
			if ( null != maoxqsxh)
			{
				beginDate = maoxqsxh.getCaifrq();
			}
			else
			{
				try {
					beginDate = DateUtil.stringToDateYMD(DateUtil.dateToStringYMD(new Date()));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			if (list.get(0) instanceof Clddxx) {
				usercenter = ((Clddxx) list.get(0)).getUsercenter();
				zzx = ((Clddxx) list.get(0)).getScxh();
				endDate = ((Clddxx) list.get(list.size() - 1)).getYjjzlsj();
			} else if (list.get(0) instanceof Clddxx2) {
				usercenter = ((Clddxx2) list.get(0)).getUsercenter();
				zzx = ((Clddxx2) list.get(0)).getScxh().substring(3);
				endDate = ((Clddxx2) list.get(list.size() - 1)).getYjsxsj();
			}
			paramMap.put("usercenter", usercenter);
			paramMap.put("zzx", zzx);
			paramMap.put("beginDate", beginDate);
			paramMap.put("endDate", endDate);
			// 查询大线排产数量
			List<Map<String, Object>> daxpcslList = baseDao.getSdcDataSource(
					ConstantDbCode.DATASOURCE_EXTENDS2).select(
					"hlorder.queryShulByDate", paramMap);
			for (Map<String, Object> bean : daxpcslList) {
				// key = 总装下线时间+大线线号
				String key = DateUtil.dateToStringYMD((Date) bean.get("JTRQ"))
						+ "_" + usercenter + line + zzx;
				Daxpcsl pcsl = new Daxpcsl();
				pcsl.setUsercenter(usercenter);
				pcsl.setDaxxh(usercenter + line + zzx);
				try {
					pcsl.setRiq(DateUtil.stringToDateYMD(String.valueOf(bean.get("JTRQ"))));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				pcsl.setJihsxl((BigDecimal) bean.get("SHUL"));
				pcsl.setJihxxl((BigDecimal) bean.get("SHUL"));
				pcsl.setChej(usercenter + line);
				mgspcslmap.put(key, pcsl);
			}
		}
	}

	
	/*
	 * 获取计算开始日期到计算结束日期之间总装的上线日期及计划量集合（zzjhlist） select riq,jihsxl,jihxxl from
	 * in_daxpcsl where usercenter=usercenter and daxxh=usercenter||5||scxh and
	 * riq>=kaisrq and riq<jiesrq order by riq 经讨论，查询时，不用< 结束日期
	 */
	private List<Daxpcsl> findSppvjhlist(Clddxx cx,String scxh) {
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("usercenter", cx.getUsercenter());
		param.put("daxxh", cx.getUsercenter()+scxh+cx.getScxh());
		param.put("kaisrq", new Date());
		// 含开始日期
		return (ArrayList<Daxpcsl>) baseDao.getSdcDataSource("2").select(
				"hlorder.queryFindSppvZzjhlistContainsKaisrq", param);
	}
	
	private void getPcslByJtpcjhFromNow(Clddxx cx,Map<String, Daxpcsl> mgspcslmap, String line) {
		// 查询参数map
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String usercenter = cx.getUsercenter(); // 用户中心
		String zzx = cx.getScxh(); // 线号
		paramMap.put("usercenter", usercenter);
		paramMap.put("beginDate", new Date());
		paramMap.put("zzx", cx.getScxh());
		// 大线排产数量
		List<Map<String, Object>> daxpcslList;
		// 包含开始日期
		daxpcslList = baseDao.getSdcDataSource(
				ConstantDbCode.DATASOURCE_EXTENDS2).select(
				"hlorder.queryJtjhByDateContainsKaisrq", paramMap);
		for (Map<String, Object> bean : daxpcslList) {
			// key = 总装下线时间+大线线号
			String key = DateUtil.dateToStringYMD((Date) bean.get("JTRQ"))
					+ "_" + usercenter + line + zzx;
			Daxpcsl pcsl = new Daxpcsl();
			pcsl.setUsercenter(usercenter);
			pcsl.setDaxxh(usercenter + line + zzx);
			try {
				pcsl.setRiq(DateUtil.stringToDateYMD(String.valueOf(bean.get("JTRQ"))));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			pcsl.setJihsxl((BigDecimal) bean.get("SHUL"));
			pcsl.setJihxxl((BigDecimal) bean.get("SHUL"));
			pcsl.setChej(usercenter + line);
			mgspcslmap.put(key, pcsl);
		}
	}
	
}
