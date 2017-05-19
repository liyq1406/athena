package com.athena.truck.module.kcckx.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.truck.entity.Dengdqy;
import com.athena.truck.entity.Shijdzt;
import com.athena.util.date.DateUtil;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 等待区域
 * @author chenpeng
 * @date 2015-1-7
 */
@Component
public class DengdqyService  extends BaseService<Dengdqy> {
	
	@Inject
	private ShijdztService shijdztService;
	
	/**
	 * 获得命名空间
	 * @author chenpeng
	 * @date 2015-1-7
	 */
	@Override
	protected String getNamespace() {
		return "ts_kac";
	}
	
	
	/**
	 * 获得多个等待区域
	 * @param bean
	 * @return List
	 * @author chenpeng
	 * @date 2015-1-7
	 */
	
	@SuppressWarnings("rawtypes")
	public List list(Dengdqy bean) throws ServiceException {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_kac.queryDengdqy",bean);
	}
	
	
	@SuppressWarnings("rawtypes")
	public List listquybhqc(Dengdqy bean) throws ServiceException {
		
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_kac.queryQuybhqc",bean);
	}

	
	/**
	 * 查询单个等待区域
	 * @return 
	 */
	public Map<String, Object> selectS(Dengdqy bean) {
		return  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ts_kac.queryDengdqy",bean,bean);
	}
	
	/**
	 * 保存区域定义
	 * @param bean
	 * @param operant 1-新增  2-修改
	 * @author chenpeng
	 * @date 2015-1-7
	 */
	@SuppressWarnings({ "unchecked"})
	@Transactional
	public String save(Dengdqy bean,Integer operant,ArrayList<Shijdzt> insert,ArrayList<Shijdzt> edit,ArrayList<Shijdzt> delete,String userId,Map<String,String> params) throws ServiceException{
		
		bean.setEditor(userId);									//修改人
		bean.setEdit_time(DateUtil.curDateTime());		//修改时间
		if (1 == operant){			//增加
			bean.setCreator(userId);//增加人
			bean.setCreate_time(DateUtil.curDateTime());	//增加时间
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_kac.insertDengdqy", bean);		//增加数据库
			shijdztService.save(insert, edit, delete, userId, bean);//实际大站台增删改
		}else if(2 == operant){		//修改
			List<Dengdqy> qy=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_kac.querySQQy", params);	//查询此区域权限是否已分配信息
			List<String> strList = new ArrayList<String>();
			for (Dengdqy d : qy) 
			{
				strList.add(d.getQuybh());
			}
			if(strList.contains(bean.getQuybh())){
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_kac.updateDengdqy", bean);		//修改数据库
			shijdztService.save(insert, edit, delete, userId, bean);//实际大站台增删改
		}else{
				throw new ServiceException("您没有修改此区域大站台的权限！");
			}
		}
		return "保存成功";
	} 
	
	/**
	 * 失效区域定义
	 * @param bean
	 * @author chenpeng
	 * @date 2015-1-7
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional
	public String doDelete(Dengdqy bean,Map<String,String> params) throws ServiceException{
		
		
		Shijdzt sj=new Shijdzt();								//实际大站台bean
		sj.setUsercenter(bean.getUsercenter());				    //用户中心
		sj.setQuybh(bean.getQuybh());							//区域编号
		sj.setBiaos("1");
		
		List<Dengdqy> qy=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_kac.querySQQy", params);	//查询此区域权限是否已分配信息
		List<String> strList = new ArrayList<String>();
		for (Dengdqy d : qy) 
		{
			strList.add(d.getQuybh());
		}
		if(strList.contains(bean.getQuybh())){
			List list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_kac.queryShijdzt", sj);	//查询此区域下的实际大站台信息
			
			if(0 == list.size()){								//此区域下不存在实际大站台信息
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_kac.deleteDengdqy", bean);	//失效等待区域
				return bean.getQuybh();
			}
	   }else{
		   throw new ServiceException("您没有失效此区域大站台的权限！"); 
	   }
		
		return "";
	}
	
	
	
}
