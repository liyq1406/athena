package com.athena.xqjs.module.kdorder.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.ilorder.Aixddppjg;
import com.athena.xqjs.entity.ilorder.Aixddzxgz;
import com.athena.xqjs.entity.ilorder.Dingdlj;
import com.athena.xqjs.entity.ilorder.Dingdmx;
import com.athena.xqjs.module.common.CommonFun;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.support.PageableSupport;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * <p>
 * 项目名称：athena-xqjs
 * </p>
 * 类名称：DingdxgjsxgService
 * <p>
 * 类描述：订单修改及生效service
 * </p>
 * 创建人：WL
 * <p>
 * 创建时间：2012-03-05
 * </p>
 * 
 * @version 1.0
 * 
 */
@Component
@SuppressWarnings({ "rawtypes", "unchecked" })
public class AixddService extends BaseService {

	/**
	 * 保存爱信订单匹配结果
	 * 
	 * @param aixddppjg
	 *            保存参数
	 */
	public Integer saveAixddppgzjg(Aixddppjg aixddppjg) {
		aixddppjg.setId(getUUID());// 生成ID
		Map map = new HashMap<String, String>();
		map.put("lingjbh", aixddppjg.getLingjbh());
		map.put("dingdh", aixddppjg.getDingdh());
		Dingdlj dingdlj = (Dingdlj) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("aixdd.queryDingdlj", map);
		aixddppjg.setDanw(dingdlj.getDanw());
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("aixdd.insertPipjg", aixddppjg);
	}

	/**
	 * 更新爱信订单匹配结果
	 * 
	 * @param aixddppjg
	 *            更新参数
	 */
	public Integer updateAixddppgzjg(Aixddppjg aixddppjg) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("aixdd.updateAixddppgzjg", aixddppjg);
	}

	/**
	 * 查询订单零件
	 * 
	 * @param page
	 *            返回对象
	 * @param param
	 *            查询参数
	 * @return 查询结果
	 */
	public Map queryDingdlj(PageableSupport page, Map<String, String> param) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("aixdd.queryDingdlj", param, page);
	}

	/**
	 * 查询爱信订单匹配规则
	 * 
	 * @param page
	 *            返回对象
	 * @param param
	 *            查询参数
	 * @return 查询结果
	 */
	public Map queryAixddppgzjg(PageableSupport page, Map<String, String> param) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("aixdd.queryAixddppgz", param, page);
	}

	/**
	 * 匹配装箱规则
	 * 
	 * @param dingdmx
	 *            参数
	 */
	@Transactional
	public void piPzxgz(Dingdmx dingdmx) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("aixdd.deletePipjg", dingdmx);
		// 查询发运日期,按发运日期分组
		List<Dingdmx> listDdmxFyrq = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("aixdd.queryDingdmxFayrq", dingdmx);
		//零件总数量
		BigDecimal sumNum = BigDecimal.ZERO;
		// 多堆数总数
		BigDecimal sumDds = BigDecimal.ZERO;
		// 少堆数总数
		BigDecimal sumSds = BigDecimal.ZERO;
		for (int i = 0; i < listDdmxFyrq.size(); i++) {
			Dingdmx mxFyrq = listDdmxFyrq.get(i);
			dingdmx.setFayrq(mxFyrq.getFayrq());// 设置发运日期
			// 根据订单号,零件编号,供应商代码,用户中心查询订单明细列表,按数量排序,升序
			String[] usercenter = dingdmx.getUsercenter().split(";");
			for(int x=0;x<usercenter.length;x++){
				dingdmx.setUsercenter(usercenter[x]);
				List<Dingdmx> listDdmx = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("aixdd.queryDingdmx", dingdmx);
				// 查询爱信订单装箱规则,降序排序
				List<Map> listZxgz = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("aixdd.queryAixddzxgz");
				if (listZxgz != null && !listZxgz.isEmpty()) {
					// UA个数1
					BigDecimal uags1 = CommonFun.getBigDecimal(listZxgz.get(0).get("UAGS"));
					// UA个数2
					BigDecimal uags2 = CommonFun.getBigDecimal(listZxgz.get(1).get("UAGS"));
					// 遍历明细列表进行规则匹配
					int size = listDdmx.size();// size
					for (int j = 0; j < size; j++) {
						Dingdmx mx = listDdmx.get(j);
						// 爱信订单匹配结果
						Aixddppjg aixddppjg = new Aixddppjg();
						aixddppjg.setId(getUUID());// ID
						aixddppjg.setUsercenter(mx.getUsercenter());// 用户中心
						aixddppjg.setDingdh(mx.getDingdh());// 订单号
						aixddppjg.setGongysdm(mx.getGongysdm());// 供应商代码
						aixddppjg.setLingjbh(mx.getLingjbh());// 零件编号
						aixddppjg.setFayrq(mx.getFayrq());// 发运日期
						aixddppjg.setDanw(mx.getDanw());// 单位
						BigDecimal shul = mx.getShul();// 数量
						sumNum = sumNum.add(shul);//总数量累加
						aixddppjg.setShul(shul);// 数量
						BigDecimal bzrl = mx.getUabzucrl();// UA包装容量
						// 计算多的堆零件数量
						BigDecimal duoDsl = bzrl.multiply(uags1);
						// 计算少的堆零件数量
						BigDecimal shaoDsl = bzrl.multiply(uags2);
						// 多堆数
						BigDecimal duoDs = BigDecimal.ZERO;
						// 少堆数
						BigDecimal shaods = BigDecimal.ZERO;
						// 如果到了最后一条,先从小的推
						if (j < size - 1  || i < listDdmxFyrq.size() - 1) {
							// 多UA堆数,除了取整
							duoDs = shul.divideToIntegralValue(duoDsl);
							// 余量
							BigDecimal yul = shul.remainder(duoDsl);
							// 如果余量大于少堆数量,则多堆数+1
							if (yul.compareTo(shaoDsl) > 0) {
								duoDs = duoDs.add(BigDecimal.ONE);
							} else if(yul.compareTo(BigDecimal.ZERO) != 0){//如果余数少于少堆数量,并且余数不等于0,则少堆数+1
								shaods = shaods.add(BigDecimal.ONE);
							}
							// 统计多堆数,累加
							sumDds = sumDds.add(duoDs);
							// 统计少堆数,累加
							sumSds = sumSds.add(shaods);
						} else {
							// UA堆数1
							BigDecimal uads1 = CommonFun.getBigDecimal(listZxgz.get(0).get("UADS"));
							// UA堆数2
							BigDecimal uads2 = CommonFun.getBigDecimal(listZxgz.get(1).get("UADS"));
							//整箱容量
							BigDecimal rongl = duoDsl.multiply(uads1).add(shaoDsl.multiply(uads2));
							//最后一批少堆数为零件总需求按箱子容量向上取整减去之前的少堆数
							shaods = sumNum.divide(rongl,0,BigDecimal.ROUND_CEILING).subtract(sumSds);
							// 统计少堆数,累加
							sumSds = sumSds.add(shaods);
							//最后多堆数等于少堆数乘以堆数比例减去之前多堆数之和
							duoDs = sumSds.multiply(uads1.divide(uads2,5,BigDecimal.ROUND_CEILING)).subtract(sumDds);
							// 统计多堆数,累加
							sumDds = sumDds.add(duoDs);
						}
						aixddppjg.setDuouads(duoDs.intValue());// 多堆数
						aixddppjg.setShaouads(shaods.intValue());// 少堆数
						// 实际订货数量=(多堆零件数量*多堆堆数)+(少堆零件数量*少堆堆数)
						aixddppjg.setDinghsl(duoDsl.multiply(duoDs).add(shaoDsl.multiply(shaods)));
						// 差异量
						aixddppjg.setChayl(aixddppjg.getDinghsl().subtract(shul));
						// 保存匹配结果
						baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("aixdd.insertPipjg", aixddppjg);
					}
				}
			}
			
		}
	}

	/**
	 * 保存爱信订单装箱规则
	 * 
	 * @param aixddzxgz
	 */
	public String insertAixddzxgz(Aixddzxgz aixddzxgz) {
		String result = "";
		List list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("aixdd.queryZxgz");
		if (list != null && list.size() >= 2) {
			result = "匹配规则已存在两条数据,请删除后再新增";
		} else {
			aixddzxgz.setId(getUUID());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("aixdd.insertAixddzxgz", aixddzxgz);
		}
		return result;
	}

	/**
	 * 更新爱信订单装箱规则
	 * 
	 * @param aixddzxgz
	 */
	public void updateAixddzxgz(Aixddzxgz aixddzxgz) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("aixdd.updateAixddzxgz", aixddzxgz);
	}

	/**
	 * 查询装箱规则
	 * 
	 * @param page
	 * @param param
	 * @return
	 */
	public Map queryZxgz(PageableSupport page, Map<String, String> param) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("aixdd.queryZxgz", param, page);
	}

	/**
	 * 查询装箱规则
	 * 
	 * @param page
	 * @param param
	 * @return
	 */
	public Aixddzxgz queryZxgzMx(Map map) {
		return (Aixddzxgz) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("aixdd.queryZxgz", map);
	}

	/**
	 * 删除爱信订单装箱规则
	 * 
	 * @param aixddzxgz
	 */
	public void removeAixddzxgz(Aixddzxgz aixddzxgz) {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("aixdd.removeAixddzxgz", aixddzxgz);
	}
}
