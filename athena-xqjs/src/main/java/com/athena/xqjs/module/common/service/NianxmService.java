package com.athena.xqjs.module.common.service;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.common.Nianxb;
import com.toft.core3.container.annotation.Component;


@SuppressWarnings("rawtypes")
@Component
public class NianxmService extends BaseService{
	
	public String  getYear(String userCenter){
		Calendar  calendar=new GregorianCalendar();
		//取的年份转为String
		String year=String.valueOf(calendar.get(Calendar.YEAR));
		Nianxb   nb=new  Nianxb();
		nb.setNianf(year);
		nb.setUsercenter(userCenter);
		//查询年型表,返回bean
		nb=(Nianxb)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("Nianxb.queryNianx",nb);
		//获得年型码
		return nb.getNianx();
		
	}

}
