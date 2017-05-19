package com.athena.xqjs.module.kdorder.service;

import java.util.HashMap;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.ilorder.Dingdmx;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;
@Component
public class KdrddmxService extends BaseService {
	/**
	 * 插如操作
	 * 
	 * @author 陈骏
	 * @version v1.0
	 * @date 2011-12-09 参数说明：Dingdmx bean 订单明细实体
	 */
	public boolean doInsert(Dingdmx bean) {
		int count = 0;
		if(bean.getDingdh()!=null){
			count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("kdorder.insertKdrddmx", bean);
		}
		return count > 0;
	}
	/**
	 * 更新订单明细状态
	 * 
	 * @author 陈骏
	 * @version v1.0
	 * @date 2011-12-09 参数说明：订单号,订单状态
	 */
	@Transactional
	public boolean doUpdateMxZt(String dingdh,String dingdzt) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("dingdh", dingdh);
		map.put("zhuangt", dingdzt);
		int count = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("kdorder.updateKdrddmxZt", map);
		return count > 0;
	}
	/**
	 * 清理mx中间表
	 * 
	 * @author 陈骏
	 * @version v1.0
	 * @date 2011-12-09 
	 */
	@Transactional
	public void deleteAllKdrddmx() {		
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("kdorder.deleteAllKdrddmx");
	}
}
