package com.athena.xqjs.module.common.service;

import java.util.HashMap;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.common.Jdygzq;
import com.athena.xqjs.module.common.CommonFun;
import com.toft.core3.container.annotation.Component;

@Component
public class JidygzqService extends BaseService{
	public String queryDingdnr(String zhizlx,String suoszq){
		Map<String,String> map = new HashMap<String,String>();
		map.put("dinghlx", zhizlx);
		map.put("suozgyzq", suoszq);
		CommonFun.mapPrint(map, "查询既定-预告周期表queryDingdnr方法参数map");
		String dingdnr= null;
		CommonFun.logger.debug("查询既定-预告周期表queryDingdnr方法的sql语句为：common.queryDingdnr");
		Jdygzq jdygzq =(Jdygzq)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("common.queryDingdnr", map);
		if(null == jdygzq){
			return dingdnr;
		}else{
			dingdnr= jdygzq.getDingdnr();
			return dingdnr;
		}
		
		
		
			
		
			
		
		
	}

}
