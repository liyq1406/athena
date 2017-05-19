package com.athena.ckx.module.baob.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.ckx.entity.baob.Dfpvfhtzd;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.toft.core3.container.annotation.Component;
//<!--rencong 0011937 -->
@Component
public class DfpvfhtzdService extends BaseService<Dfpvfhtzd> {
	
	

	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}
	
	@SuppressWarnings("unchecked")
	public Map<String,Object> query(Dfpvfhtzd bean,String exportXls){
		if("exportXls".equals(exportXls)){
			List<Dfpvfhtzd> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.querydfDvfhtzd",bean);
			Map<String,Object> m = new HashMap<String, Object>();
			m.put("total", list.size());
			m.put("rows", list);
			return m;
		}
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ts_ckx.queryDfpvfhtzd",bean,bean);
	}
	
	
}
