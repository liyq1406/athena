package com.athena.xqjs.module.common.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.toft.core3.container.annotation.Component;

@Component
public class WzklszService  extends BaseService{
	public BigDecimal sumChuk(String caozm,String lingjbh,String kais,String jies,String ckbm,String usercenter){
		Map<String,String> map = new HashMap<String,String>();
		map.put("czm", caozm);
		map.put("lingjbh", lingjbh);
		map.put("kais", kais);
		map.put("jies", jies);
		map.put("ckbm", ckbm);
		map.put("usercenter", usercenter);
		BigDecimal sum = BigDecimal.ZERO;
		Object obj =  this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectObject("common.querySumChuk",map);
		if(null != obj){
			 sum = (BigDecimal)obj;
		}
		return sum;
	}
}
