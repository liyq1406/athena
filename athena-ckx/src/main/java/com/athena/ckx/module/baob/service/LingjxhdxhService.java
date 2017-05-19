package com.athena.ckx.module.baob.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.ckx.entity.baob.Lingjxhdxh;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;


@Component
public class LingjxhdxhService extends BaseService<Lingjxhdxh> {
	
	
	
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
	public Map<String,Object> query(Lingjxhdxh bean,String exportXls){
		if("exportXls".equals(exportXls)){
			List<Lingjxhdxh> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.querylingjxhdxh",bean);
			Map<String,Object> m = new HashMap<String, Object>();
			m.put("total", list.size());
			m.put("rows", list);
			return m;
		}
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ts_ckx.querylingjxhdxh",bean,bean);
	}
	@Transactional
	public String timingAddLingjxhdxh(){
		//1.清空表（rep_lingjxhdxh）
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.truncateLingjxhdxh");
		//2.数据写入表（rep_lingjxhdxh）
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.addlingjxhdxh");
		return "success";
	}
}
