package com.athena.xqjs.module.hlorder.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
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
public class VjFeneService extends BaseService {

	@Inject
	private VjOrderService vjOrderService;

	
		
	public Map<String,Object> gongysFeneJs(Dingdlj bean, BigDecimal zongyhl,String zhizlx,int zhouqxh,Map<String,List<LingjGysfexz>> congxjsgyfr) throws ServiceException {
		CommonFun.logger.debug("KD订单计算,份额计算开始");
		// 存放结果map
		Map<String, Object> valueMap = new HashMap<String, Object>();
		if (null != bean) {
			// 份额
			BigDecimal fene = bean.getGongysfe(); //默认为合同份额
			if(congxjsgyfr != null){
				List<LingjGysfexz> sijfelst = congxjsgyfr.get(bean.getLingjbh()); //零件编号
				if(sijfelst != null){
					for(LingjGysfexz sjfe : sijfelst){
						if(bean.getGongysdm().equalsIgnoreCase(sjfe.getGongysbh())){//相当的供应商
							if(sjfe.getBencgongyfe() != null){ //修改为本次供应份额
								fene = sjfe.getBencgongyfe(); 
								break;
							}
						}
					}
				}
			}
			CommonFun.logger.debug("份额计算份额：fene="+fene);
			// 最后要货量
			// 没有指定供应商
			if (null == bean.getZhidgys()) {
				// 从公式得到要货量
				valueMap = this.vjOrderService.quzheng(zongyhl.multiply(fene), zhizlx,
						bean.getUabzucrl().multiply(bean.getUabzucsl()), bean.getZuixqdl());
				// 指定供应商的情况
			} else {
				// 指定供应商一致的情况，将份额是为100%
				if (bean.getGongysdm().equalsIgnoreCase(bean.getZhidgys())) {
					valueMap = this.vjOrderService.quzheng(zongyhl, zhizlx,
							bean.getUabzucrl().multiply(bean.getUabzucsl()), bean.getZuixqdl());
					// 指定供应商不一致的情况
				} else {
					valueMap = this.vjOrderService.quzheng(BigDecimal.ZERO, zhizlx,
							bean.getUabzucrl().multiply(bean.getUabzucsl()), bean.getZuixqdl());
				}
			}
		}
		return valueMap ;
		
	}
	
	
	
}
