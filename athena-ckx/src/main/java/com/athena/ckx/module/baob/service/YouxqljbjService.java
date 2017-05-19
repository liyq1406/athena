package com.athena.ckx.module.baob.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.ckx.entity.baob.Beihlgz;
import com.athena.ckx.entity.baob.Fahtzcx;
import com.athena.ckx.entity.baob.Lingjjhygys;
import com.athena.ckx.entity.baob.Youxqljbj;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.toft.core3.container.annotation.Component;

@Component
public class YouxqljbjService extends BaseService<Youxqljbj> {
	
	
	
	/**
	 * 获得命名空间
	 * @author denggq
	 * @date 2012-1-12
	 */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}
	
	@SuppressWarnings("unchecked")
	public Map<String,Object> query(Youxqljbj bean,String exportXls){
		if("exportXls".equals(exportXls)){
			List<Lingjjhygys> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryYxqljbj",bean);
			Map<String,Object> m = new HashMap<String, Object>();
			m.put("total", list.size());
			m.put("rows", list);
			return m;
		}
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ts_ckx.queryYxqljbj",bean,bean);
	}
	
	
}
