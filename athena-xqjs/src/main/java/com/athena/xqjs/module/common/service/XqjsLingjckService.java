/**
 * 代码声明
 */
package com.athena.xqjs.module.common.service;

import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.toft.core3.container.annotation.Component;

@SuppressWarnings("rawtypes")
@Component
public class XqjsLingjckService extends BaseService {

	/**
	 * 查询
	 * 
	 * @author NIESY
	 * @see参数：零件编号，用户中心
	 */
	@SuppressWarnings("unchecked")
	public List<com.athena.xqjs.entity.kanbyhl.Lingjck> queryObject(Map<String, String> map) {
		return (List<com.athena.xqjs.entity.kanbyhl.Lingjck>) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("common.queryLingjck", map);
	}

	public com.athena.xqjs.entity.kanbyhl.Lingjck querySingle(Map<String, String> map) {
		return (com.athena.xqjs.entity.kanbyhl.Lingjck) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("common.queryLingjck", map);
	}
}