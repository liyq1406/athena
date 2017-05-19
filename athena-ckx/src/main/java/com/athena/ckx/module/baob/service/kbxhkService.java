package com.athena.ckx.module.baob.service;


import java.util.List;
import java.util.Map;

import com.athena.ckx.entity.baob.Kanbxhgm;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.toft.core3.container.annotation.Component;


/**
 * 看板循环卡
 * @author wy
 * 2013-3-13
 */
@Component
public class kbxhkService extends BaseService<Kanbxhgm>{

	@Override
	public String getNamespace() {
		return "ts_ckx";
	}

	/**
	 * 查询生产线
	 * 
	 * @param param
	 * @return list
	 */
	public List<?> selectChanx(Map<String, String> param) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.selectChanx", param);
	}
	
	public List list(Map map){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.selectKbxhk",map);
	}
	
	
	public Map<String,Object> query(Kanbxhgm bean){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ts_ckx.queryKbxhk",bean,bean);
	}
}
