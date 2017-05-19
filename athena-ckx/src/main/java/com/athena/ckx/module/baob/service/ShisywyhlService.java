package com.athena.ckx.module.baob.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.ckx.entity.baob.Shisywyhl;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.toft.core3.container.annotation.Component;


@Component
public class ShisywyhlService extends BaseService<Shisywyhl> {
	
	
	
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}
	
	@SuppressWarnings("unchecked")
	public Map<String,Object> query(Shisywyhl bean,String exportXls){
		bean.setCurrentDate(DateTimeUtil.getCurrDateTime());
		if("exportXls".equals(exportXls)){
			List<Shisywyhl> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryShisywyhl",bean);
			Map<String,Object> m = new HashMap<String, Object>();
			m.put("total", list.size());
			m.put("rows", list);
			return m;
		}
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ts_ckx.queryShisywyhl",bean,bean);
	}
	
	
	public List<?> queryYhllx() {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryYhllx");
	}
	
}
