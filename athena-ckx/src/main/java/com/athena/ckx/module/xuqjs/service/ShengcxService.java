package com.athena.ckx.module.xuqjs.service;

import java.util.ArrayList;
import java.util.List;

import com.athena.ckx.entity.xuqjs.Fenpq;
import com.athena.ckx.entity.xuqjs.Shengcx;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 生产线
 * @author denggq
 * @date 2012-3-21
 */
@Component
public class ShengcxService  extends BaseService<Shengcx> {
	
	@Inject
	private FenpqService fenpqService;
	
	/**
	 * 获得命名空间
	 * @author denggq
	 * @date 2012-3-21
	 */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}
	
	
	/**
	 * 保存生产线
	 * @param bean
	 * @param operant 1-新增  2-修改
	 * @author denggq
	 * @date 2012-2-3
	 */
	@Transactional
	public String save(Shengcx bean,Integer operant,ArrayList<Fenpq> insert,ArrayList<Fenpq> edit,ArrayList<Fenpq> delete,String userId) throws ServiceException{
		if( null != bean.getShengcjp() && null != bean.getWeilscjp() && bean.getWeilscjp().equals(bean.getShengcjp())){
			throw new ServiceException(GetMessageByKey.getMessage("zcscjpbutwlscjp"));
		}
		
		if(null != bean.getQiehsj() ){
			if(DateTimeUtil.compare(bean.getQiehsj(), DateTimeUtil.getCurrDate())){
				throw new ServiceException(GetMessageByKey.getMessage("bixushiweilai"));
			}
			if(null == bean.getWeilscjp()){
				throw new ServiceException(GetMessageByKey.getMessage("zcwlnotnull"));
			}
		}
		
		if(null == bean.getQiehsj() && null != bean.getWeilscjp()){
			throw new ServiceException(GetMessageByKey.getMessage("zcqhsjnotnull"));
		}
		
		bean.setEditor(userId);									//修改人
		bean.setEdit_time(DateTimeUtil.getAllCurrTime());		//修改时间
		bean.setCpshengcjp(bean.getShengcjp());
		if (1 == operant){			//增加
			bean.setCreator(userId);//增加人
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());	//增加时间
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertShengcx", bean);		//增加数据库
		}else if(2 == operant){		//修改
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateShengcx", bean);		//修改数据库
		}
		
		fenpqService.save(insert, edit, delete, userId, bean);//分配区增删改
		
		return "success";
	} 
	
	/**
	 * 失效生产线
	 * @param bean
	 * @author denggq
	 * @date 2012-3-21
	 */
	@SuppressWarnings("rawtypes")
	@Transactional
	public String doDelete(Shengcx bean) throws ServiceException{
		
		Fenpq d=new Fenpq();								//分配区bean
		d.setUsercenter(bean.getUsercenter());				//用户中心
		d.setShengcxbh(bean.getShengcxbh());				//生产线编号
		d.setBiaos("1");
		
		List list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryFenpq", d);	//查询此生产线下得分配区信息
		
		if(0 == list.size()){								//此生产线不存在分配区信息
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteShengcx", bean);	//失效生产线
			return bean.getShengcxbh();
		}
		
		return "";
	}
	
	/**
	 * 获得多个生产线
	 * @param bean
	 * @return List
	 * @author denggq
	 * @date 2012-8-3
	 */
	@Transactional
	@SuppressWarnings("rawtypes")
	public List list(Shengcx bean) throws ServiceException {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryShengcx",bean);
	}
	
	/**
	 * 获得多个分配区
	 * @param bean
	 * @return List
	 * @author denggq
	 * @date 2012-8-3
	 */
	@Transactional
	@SuppressWarnings("rawtypes")
	public List listFenpq(Shengcx bean) throws ServiceException {
		Fenpq  f = new Fenpq();
		f.setUsercenter(bean.getUsercenter());
		f.setShengcxbh(bean.getShengcxbh());
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryFenpq",f);
	}
}
