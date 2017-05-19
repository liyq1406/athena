package com.athena.xqjs.module.ilorder.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.athena.component.service.BaseService;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.exception.ServiceException;
import com.athena.xqjs.entity.ilorder.Dingdlj;
import com.athena.xqjs.module.common.CommonFun;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

/**
 * <p>
 * Title:份额计算
 * </p>
 * <p>
 * Description:份额计算
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company: 软通动力
 * </p>
 * 
 * @author 李明
 * @version v1.0
 * @date 2011-12-09
 */
@Component
public class FeneService extends BaseService {

	@Inject
	private IlOrderService ilOrderService;

	// 初始化零件flag
	private String flagLingj = "";

	public String getFlagLingj() {
		return flagLingj;
	}


	public void setFlagLingj(String flagLingj) {
		this.flagLingj = flagLingj;
	}


	// 初始化仓库flag
	private String flagCangku = "";

	public String getFlagCangku() {
		return flagCangku;
	}


	public void setFlagCangku(String flagCangku) {
		this.flagCangku = flagCangku;
	}


	// 初始化零件map
	private Map<String, BigDecimal> zongMap = new TreeMap<String, BigDecimal>();

	public Map<String, BigDecimal> getZongMap() {
		return zongMap;
	}


	public void setZongMap(Map<String, BigDecimal> zongMap) {
		this.zongMap = zongMap;
	}


	// 初始化仓库map
	private Map<String, BigDecimal> cangkMap = new TreeMap<String, BigDecimal>();
	
	public Map<String, BigDecimal> getCangkMap() {
		return cangkMap;
	}


	public void setCangkMap(Map<String, BigDecimal> cangkMap) {
		this.cangkMap = cangkMap;
	}


	private int zhouqixuhao = 0;

	public int getZhouqixuhao() {
		return zhouqixuhao;
	}


	public void setZhouqixuhao(int zhouqixuhao) {
		this.zhouqixuhao = zhouqixuhao;
	}


	/**
	 * 份额计算
	 * 
	 * @author 李明
	 * @version v1.0
	 * @date 2011-12-09
	 * @参数说明：Dingdlj bean 订单零件实体，BigDecimal zongyhl 总要货量
	 */
	@Inject
	private UserOperLog userOperLog;
	
	public Map<String,BigDecimal> gongysFeneJs(Dingdlj bean, BigDecimal zongyhl,String zhizlx,int zhouqxh) throws ServiceException {
		//this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "KD订单计算", "份额计算开始") ;
		CommonFun.logger.debug("KD订单计算,份额计算开始");
		// 存放结果map
		Map<String, BigDecimal> valueMap = new HashMap<String, BigDecimal>();
		if (null != bean) {
			// 获取到对象零件flag
			String ljFlag = bean.getUsercenter() + bean.getLingjbh();
			CommonFun.logger.debug("份额计算零件flage：ljFlag="+ljFlag);
			// 获取到对象仓库flag
			String ckFlag = bean.getCangkdm();
			CommonFun.logger.debug("份额计算仓库flage：ckFlag="+ckFlag);
			// 份额
			BigDecimal fene = bean.getGongysfe();
			CommonFun.logger.debug("份额计算份额：fene="+fene);
			// 供应商已要货数量
			BigDecimal gongysyyhl = BigDecimal.ZERO;
			// 定义总要货量
			BigDecimal zyhl = BigDecimal.ZERO;
			// 要货量
			BigDecimal p0 = BigDecimal.ZERO;
			// 周期要货量
			BigDecimal zhouqyhl = zongyhl;
			CommonFun.logger.debug("份额计算周期要货量：zhouqyhl="+zhouqyhl);
			// 最后要货量
			BigDecimal yaohl = BigDecimal.ZERO;
			// 
			BigDecimal zero = BigDecimal.ZERO;
			// 判断是否是第一次计算，当flag都为空的时候，为第一次计算
			if (flagCangku == null || flagCangku.equals("") || flagLingj == null || flagLingj.equals("")) {
				CommonFun.logger.debug("份额计算当flag都为空的时候");
				
				cangkMap = this.getValue(bean, fene, zyhl, zhouqyhl, gongysyyhl) ;
				//CommonFun.mapPrint(cangkMap, "份额计算仓库map：cangkMap");
				yaohl = cangkMap.get(bean.getGongysdm());
				CommonFun.logger.debug("份额计算要货量：yaohl="+yaohl);
				// 将得到的结果存放到map中(分配后的要货量和盈余量)
				valueMap = this.ilOrderService.quzheng(yaohl, zhizlx,
						bean.getUabzucrl().multiply(bean.getUabzucsl()), bean.getZuixqdl());
				//CommonFun.mapPrint(valueMap, "将得到的结果存放到map中(分配后的要货量和盈余量)：valueMap");
				// 赋给零件标志新值
				flagLingj = bean.getUsercenter() + bean.getLingjbh();
				CommonFun.logger.debug("份额计算零件标志新值：flagLingj="+flagLingj);
				// 赋给仓库标志新值
				flagCangku = bean.getCangkdm();
				CommonFun.logger.debug("份额计算仓库标志新值：flagCangku="+flagCangku);
			} else if (flagCangku != null && flagLingj != null) {// 当flag不为空的时候
				CommonFun.logger.debug("份额计算当flag不为空的时候");
				// 当标记没有发生变化的时候
				if (flagLingj.equals(ljFlag) && flagCangku.equals(ckFlag)) {// 标志没有发生变化
					CommonFun.logger.debug("份额计算当标记没有发生变化的时候");
					// 没有指定供应商
					if (null == bean.getZhidgys()) {
						// 从公式得到要货量
						CommonFun.logger.debug("份额计算总要货：zyhl="+zyhl);
						CommonFun.logger.debug("份额计算周期要货量：zhouqyhl="+zhouqyhl);
						CommonFun.logger.debug("份额计算份额：fene="+fene);
						CommonFun.logger.debug("份额计算供应商要货量：gongysyyhl="+gongysyyhl);
						p0 = (zyhl.add(zhouqyhl)).multiply(fene).subtract(gongysyyhl);
						CommonFun.logger.debug("份额计算p0计算结果（没有指定供应商）：p0="+p0);
						// 指定供应商的情况
					} else {
						// 指定供应商一致的情况，将份额是为100%
						if (bean.getGongysdm().equalsIgnoreCase(bean.getZhidgys())) {
							CommonFun.logger.debug("份额计算总要货：zyhl="+zyhl);
							CommonFun.logger.debug("份额计算周期要货量：zhouqyhl="+zhouqyhl);
							CommonFun.logger.debug("份额计算供应商要货量：gongysyyhl="+gongysyyhl);
							p0 = (zyhl.add(zhouqyhl)).subtract(gongysyyhl);
							CommonFun.logger.debug("份额计算p0计算结果（指定供应商的情况）：p0="+p0);
							// 指定供应商不一致的情况
						} else {
							p0 = zero;
							CommonFun.logger.debug("份额计算p0计算结果（指定供应商不一致的情况）：p0="+p0);
						}
					}
					// 得带的结果为负数的时间，将要货量置为0
					if (p0.intValue() < 0) {
						p0 = zero;
						CommonFun.logger.debug("份额计算p0计算结果（p0.intValue() < 0时）：p0="+p0);
						// 总map中不能大于周期要货量，当大于周期要货量的时候，需要对其减去多余的部分；
					} else if (p0.subtract(zhouqyhl).intValue() > 0) {
						p0 = zhouqyhl;
						CommonFun.logger.debug("份额计算p0计算结果（p0.subtract(zhouqyhl).intValue() > 0时）：p0="+p0);
					}
					if(null == cangkMap.get(bean.getGongysdm())){
						cangkMap = this.getValue(bean, fene, zyhl, zhouqyhl, gongysyyhl);
						//CommonFun.mapPrint(cangkMap, "null == cangkMap.get(bean.getGongysdm())时的仓库map");
					}else{
						// 将要货量存放到仓库map
						cangkMap.put(bean.getGongysdm(), p0);
					}
					CommonFun.logger.debug("份额计算全局变量周期序号：zhouqixuhao="+zhouqixuhao);
					CommonFun.logger.debug("份额计算当前周期序号：zhouqxh="+zhouqxh);
					if(zhouqixuhao != zhouqxh){
						cangkMap = this.getValue(bean, fene, zyhl, zhouqyhl, gongysyyhl);
						//CommonFun.mapPrint(cangkMap, "zhouqixuhao != zhouqxh时的仓库map");
						zhouqixuhao=zhouqxh;
					}
					yaohl = cangkMap.get(bean.getGongysdm());
					CommonFun.logger.debug("份额计算从cangkMap中取出的供应商yaohl：yaohl="+yaohl);
					// 将得到的结果存放到map中(分配后的要货量和盈余量)
					valueMap = this.ilOrderService.quzheng(yaohl, zhizlx,
							bean.getUabzucrl().multiply(bean.getUabzucsl()), bean.getZuixqdl());
					CommonFun.mapPrint(valueMap, "将得到的结果存放到map中(分配后的要货量和盈余量)：valueMap");
					// 赋给零件标志新值
					flagLingj = bean.getUsercenter() + bean.getLingjbh();
					CommonFun.logger.debug("份额计算赋给零件标志新值：flagLingj="+flagLingj);
					// 赋给仓库标志新值
					flagCangku = bean.getCangkdm();
					CommonFun.logger.debug("份额计算赋给仓库标志新值：flagCangku="+flagCangku);
				} else if (!flagLingj.equals(ljFlag)) {// 零件标志发生变化的时候，需要清空两个map
					CommonFun.logger.debug("份额计算零件标志发生变化的时候");
					// 清空仓库map
					cangkMap.clear();
					// 清空总map
					zongMap.clear();
					// 从公式得到要货量
					CommonFun.logger.debug("份额计算总要货：zyhl="+zyhl);
					CommonFun.logger.debug("份额计算周期要货量：zhouqyhl="+zhouqyhl);
					CommonFun.logger.debug("份额计算份额：fene="+fene);
					CommonFun.logger.debug("份额计算供应商要货量：gongysyyhl="+gongysyyhl);
					if (null == bean.getZhidgys())
					{
						p0 = (zyhl.add(zhouqyhl)).multiply(fene).subtract(gongysyyhl);
					}
					else {
						// 指定供应商一致的情况，将份额是为100%
						if (bean.getGongysdm().equalsIgnoreCase(bean.getZhidgys())) {
							p0 = (zyhl.add(zhouqyhl)).subtract(gongysyyhl);
							// 指定供应商不一致的情况
						} else {
							p0 = zero;
						}
					}
					CommonFun.logger.debug("份额计算p0计算结果：p0="+p0);
					// 不为负数
					if (p0.intValue() < 0) {
						p0 = zero;
						CommonFun.logger.debug("份额计算p0计算结果（p0.intValue() < 0的情况）：p0="+p0);
						// 要货数量不大于周期要货数量
					} else if (p0.subtract(zhouqyhl).intValue() > 0) {
						p0 = zhouqyhl;
						CommonFun.logger.debug("份额计算p0计算结果（zhouqyhl).intValue() > 0的情况）：p0="+p0);
					}
					// 将要货量存放到仓库map
					cangkMap.put(bean.getGongysdm(), p0);
					// 将要货量设置到实体p0数量中去
					yaohl = cangkMap.get(bean.getGongysdm());
					CommonFun.logger.debug("份额计算从cangkMap中取出的供应商yaohl：yaohl="+yaohl);
					// 将得到的结果存放到map中(分配后的要货量和盈余量)
					valueMap = this.ilOrderService.quzheng(yaohl, zhizlx,
							bean.getUabzucrl().multiply(bean.getUabzucsl()), bean.getZuixqdl());
					//CommonFun.mapPrint(valueMap, "将得到的结果存放到map中(分配后的要货量和盈余量)：valueMap");
					// 赋给零件标志新值
					flagLingj = bean.getUsercenter() + bean.getLingjbh();
					CommonFun.logger.debug("份额计算赋给零件标志新值：flagLingj="+flagLingj);
					// 赋给仓库标志新值
					flagCangku = bean.getCangkdm();
					CommonFun.logger.debug("份额计算赋给仓库标志新值：flagCangku="+flagCangku);
				} else if (flagLingj.equals(ljFlag) && !flagCangku.equals(ckFlag)) {// 仓库发生变化的时候
					CommonFun.logger.debug("份额计算仓库发生变化的时候");
					// 将仓库map中的值汇总到总map中
					//CommonFun.mapPrint(cangkMap, "份额计算仓库map");
					zongMap = CommonFun.sumMapValue(cangkMap, zongMap);
					//CommonFun.mapPrint(zongMap, "份额计算总map");
					// 清空仓库map
					cangkMap.clear();
					// 判断map不为空
					if (zongMap.size() > 0.1) {
						// 遍历map得到总要货量
						zyhl = CommonFun.sumValue(zongMap);
						CommonFun.logger.debug("份额计算总要货量：zyhl="+zyhl);
						// 从总map中获取到供应商已要货量
						gongysyyhl = zongMap.get(bean.getGongysdm());
						CommonFun.logger.debug("份额计算供应商货量：gongysyyhl="+gongysyyhl);
						if(null==gongysyyhl){
							gongysyyhl=BigDecimal.ZERO;
						}
					}
					// 从公式得到要货量p0
					CommonFun.logger.debug("份额计算总要货：zyhl="+zyhl);
					CommonFun.logger.debug("份额计算周期要货量：zhouqyhl="+zhouqyhl);
					CommonFun.logger.debug("份额计算份额：fene="+fene);
					CommonFun.logger.debug("份额计算供应商要货量：gongysyyhl="+gongysyyhl);
					p0 = (zyhl.add(zhouqyhl)).multiply(fene).subtract(gongysyyhl);
					CommonFun.logger.debug("份额计算p0计算结果：p0="+p0);
					// 不为负数
					if (p0.intValue() < 0) {
						p0 = zero;
						CommonFun.logger.debug("份额计算p0计算结果（p0.intValue() < 0的情况）：p0="+p0);
						// 要货数量不大于周期要货数量
					} else if (p0.subtract(zhouqyhl).intValue() > 0) {
						p0 = zhouqyhl;
						CommonFun.logger.debug("份额计算p0计算结果（p0.subtract(zhouqyhl).intValue() > 0的情况）：p0="+p0);
					}
					if(null == cangkMap.get(bean.getGongysdm())){
						cangkMap = this.getValue(bean, fene, zyhl, zhouqyhl, gongysyyhl);
						//CommonFun.mapPrint(cangkMap, "份额计算取不到供应商时的仓库map");
					}
					// 将要货量设置到实体p0数量中去
					yaohl = cangkMap.get(bean.getGongysdm());
					CommonFun.logger.debug("份额计算从cangkMap中取出的供应商yaohl：yaohl="+yaohl);
					// 将得到的结果存放到map中(分配后的要货量和盈余量)
					valueMap = this.ilOrderService.quzheng(yaohl, zhizlx,
							bean.getUabzucrl().multiply(bean.getUabzucsl()), bean.getZuixqdl());
					//CommonFun.mapPrint(valueMap, "将得到的结果存放到map中(分配后的要货量和盈余量)：valueMap");
					// 赋给零件标志新值
					flagLingj = bean.getUsercenter() + bean.getLingjbh();
					CommonFun.logger.debug("份额计算赋给零件标志新值：flagLingj="+flagLingj);
					// 赋给仓库标志新值
					flagCangku = bean.getCangkdm();
					CommonFun.logger.debug("份额计算赋给仓库标志新值：flagCangku="+flagCangku);
				}
			}
		}
		CommonFun.logger.debug("KD订单计算,份额计算结束");
		//this.userOperLog.addCorrect(CommonUtil.MODULE_XQJS, "KD订单计算", "份额计算结束") ;
		// 返回实体
		return valueMap;
	}
	
	
	/**
	 * 初始化要货数量
	 * **/
	public Map getValue(Dingdlj bean,BigDecimal fene,BigDecimal zyhl,BigDecimal zhouqyhl,BigDecimal gongysyyhl){
		// 要货量
		BigDecimal psl = BigDecimal.ZERO;
		// 
		BigDecimal zero = BigDecimal.ZERO;
		// 没有指定供应商
		if (null == bean.getZhidgys()) {
			// 从公式得到要货量
			CommonFun.logger.debug("初始化要货数量总要货：zyhl="+zyhl);
			CommonFun.logger.debug("初始化要货数量周期要货量：zhouqyhl="+zhouqyhl);
			CommonFun.logger.debug("初始化要货数量份额：fene="+fene);
			CommonFun.logger.debug("初始化要货数量供应商要货量：gongysyyhl="+gongysyyhl);
			psl = (zyhl.add(zhouqyhl)).multiply(fene).subtract(gongysyyhl);
			CommonFun.logger.debug("初始化要货数量psl计算结果（没有指定供应商）：psl="+psl);
			// 指定供应商的情况
		} else {
			// 指定供应商一致的情况，将份额是为100%
			if (bean.getGongysdm().equalsIgnoreCase(bean.getZhidgys())) {
				CommonFun.logger.debug("初始化要货数量总要货：zyhl="+zyhl);
				CommonFun.logger.debug("初始化要货数量周期要货量：zhouqyhl="+zhouqyhl);
				CommonFun.logger.debug("初始化要货数量供应商要货量：gongysyyhl="+gongysyyhl);
				psl = (zyhl.add(zhouqyhl)).subtract(gongysyyhl);
				CommonFun.logger.debug("初始化要货数量psl计算结果（指定供应商一致的情况）：psl="+psl);
				// 指定供应商不一致的情况
			} else {
				psl = zero;
				CommonFun.logger.debug("初始化要货数量psl计算结果（指定供应商不一致的情况）：psl="+psl);
			}
		}
		// 得带的结果为负数的时间，将要货量置为0
		if (psl.intValue() < 0) {
			psl = zero;
			CommonFun.logger.debug("初始化要货数量psl计算结果（psl.intValue() < 0情况）：psl="+psl);
			// 总map中不能大于周期要货量，当大于周期要货量的时候，需要对其减去多余的部分；
		} else if (psl.subtract(zhouqyhl).intValue() > 0) {
			psl = zhouqyhl;
			CommonFun.logger.debug("初始化要货数量psl计算结果（psl.subtract(zhouqyhl).intValue() > 0）：psl="+psl);
		}
		// 将得到的要货量存放到仓库map中
		cangkMap.put(bean.getGongysdm(), psl);
		return cangkMap ;
	}
	
}
