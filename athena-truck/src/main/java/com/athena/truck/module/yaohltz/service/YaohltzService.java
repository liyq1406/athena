package com.athena.truck.module.yaohltz.service;

import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.truck.entity.Yaohltzcx;
import com.toft.core3.Pageable;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

@Component
public class YaohltzService extends BaseService {

	/**
	 * 要货令查询
	 */
	public Map<String,Object> queryYaohlxx(Pageable page,Yaohltzcx yaohlzt){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("yaohltz.queryYaohlxx",yaohlzt,page );
	}
	
	/**
	 * 要货令调整表中添加数据
	 */
	@Transactional
	public boolean insertYaohltz(Yaohltzcx yaohlzt){
		boolean flag=false;
		int tiaozgs=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("yaohltz.updateYaohl", yaohlzt);
		yaohlzt.setTiaozgs(tiaozgs);
		int tiaoz=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("yaohltz.insertYaohlzt", yaohlzt);
		if(tiaoz == 1){
			flag = true;
		}
		return  flag; 
	}
	/**
	 * 卸货站台信息
	 */
	public List queryXiehzt(Map param){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("yaohltz.queryXiehzt", param);
	}
	/**
	 * 卸货站台编组信息
	 */
	public List queryXiehztbz(Map param){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("yaohltz.queryXiehztbz", param);
	}
}
