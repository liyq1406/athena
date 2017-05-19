package com.athena.ckx.module.baob.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;


import com.athena.ckx.entity.baob.Anxbb;
import com.athena.ckx.entity.xuqjs.CkxLingj;
import com.athena.ckx.module.baob.action.BbAction;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

//V4_041
@Component
public class anxbbService extends BaseService<Anxbb>{
	private static Logger logger =Logger.getLogger(anxbbService.class);
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}
	
	public Map<String,Object> query(Anxbb bean) {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ts_ckx.queryAnxyhlbb",bean,bean);
	}

	public List<String> queryYaohllx(Anxbb bean) {
		// TODO Auto-generated method stub
		List<String> list = new ArrayList<String>();
		try{
			 list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.querylx",bean);
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		logger.info("查询出"+list.size()+"个要货令类型");
		return list;
	}
	
	public List<String> querytongjrqyear(Anxbb bean) {
		// TODO Auto-generated method stub
		List<String> list = new ArrayList<String>();
		try{
			 list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryrqyear",bean);
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		logger.info("查询出"+list.size()+"个统计日期(带年)");
		return list;
	}

	public List<String> querytongjrq(Anxbb bean) {
		// TODO Auto-generated method stub
		List<String> list = new ArrayList<String>();
		try{
			 list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryrq",bean);
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		logger.info("查询出"+list.size()+"个统计日期");
		return list;
	}

	public List<Anxbb> queryNum(Anxbb bean) {
		// TODO Auto-generated method stub
		List<Anxbb> list = new ArrayList<Anxbb>();
		try{
			 list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.querynum",bean);
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		if(list.isEmpty())
		{
			bean.setShul(0);
			list.add(bean);
		}
		logger.info("根据工厂"+bean.getFactory()+"flag"+bean.getFlag()+"统计日期"+bean.getTongjrq()+"要货令类型"+bean.getYaohllx()+"查询数量");
		return list;
	}

	public List<Anxbb> queryJihsl(Anxbb bean) {
		// TODO Auto-generated method stub
		List<Anxbb> sl = new ArrayList<Anxbb>();
		try{
			sl = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryjihsl",bean);
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		if(sl.isEmpty())
		{
			bean.setShul(0);
			sl.add(bean);
		}
		logger.info("根据工厂"+bean.getFactory()+"flag"+bean.getFlag()+"统计日期"+bean.getTongjrq()+"要货令类型"+bean.getYaohllx()+"lx"+bean.getBiaos()+"查询数量");
		return sl;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<Map<String,String>> queryGongc(String usercenter) throws ServiceException  {
		List list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryFactory",usercenter);;
		if(0 == list.size()){
			Map<String,String> map = new HashMap<String,String>();
			map.put("KEY", "");
			map.put("VALUE", GetMessageByKey.getMessage("weipz"));
			list.add(map);
		}
		return list;
	}
}
