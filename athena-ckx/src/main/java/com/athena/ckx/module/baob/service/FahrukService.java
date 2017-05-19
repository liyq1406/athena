package com.athena.ckx.module.baob.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.ckx.entity.baob.Fahruk;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.toft.core3.container.annotation.Component;


@Component
public class FahrukService extends BaseService<Fahruk> {
	
	
	
	/**
	 * 获得命名空间
	 * @author xryuan
	 * @date 2013-3-27
	 */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}
	
	@SuppressWarnings("unchecked")
	public Map<String,Object> query(Fahruk bean,String exportXls,Map<String,String> map){
		if("exportXls".equals(exportXls)){
			List<Fahruk> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryfahruk",map);
			Map<String,Object> m = new HashMap<String, Object>();
			m.put("total", list.size());
			m.put("rows", list);
			return m;
		}
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ts_ckx.queryfahruk",map,bean);
	}
	
	
}
