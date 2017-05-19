package com.athena.truck.module.kcckx.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.truck.entity.Chac;
import com.athena.truck.entity.Chew;
import com.athena.truck.entity.Dengdqy;
import com.athena.util.date.DateUtil;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 叉车-车位
 * @author liushuang
 * @date 2015-1-20
 */
@Component
public class ChacService extends BaseService<Chac>{
	
	@Inject
	private ChaccwService chaccwService;
	/**
	 * 获得命名空间
	 * @author liushuang
	 * @date 2015-1-20
	 */
	@Override
	protected String getNamespace() {
		return "ts_kac";
	}
	
	/**
	 * 查询
	 * @param bean
     * @author liushaung
	 * @date 2015-1-20
	 * @return String
	 */
	@SuppressWarnings("rawtypes")
	public List list(Chac bean) throws ServiceException {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_kac.queryChac",bean);
		
	}
	
	/**
	 * 查询叉车组
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	public List queryChacz(Map<String,String> map) {
		return  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_kac.getChaczAthority",map);
	}
	
	/**
	 * 增加查询叉车组
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	public List queryYXChacz(Map<String,String> map) {
		return  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_kac.getYXChacz",map);
	}
	
	/**
	 * 保存叉车与车位
	 * @param bean
	 * @param operant 新增 
	 * @author liushuang
	 * @date 2015-1-7
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional
	public String save( Chac bean,Integer operant,ArrayList<Chew> insert,String userId) throws ServiceException{
		
		bean.setEditor(userId);							//修改人
		bean.setEdit_time(DateUtil.curDateTime());		//修改时间
		bean.setCreator(userId);//增加人
		bean.setCreate_time(DateUtil.curDateTime());	//增加时间

		List<Chac> list   =  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_kac.getChacxx", bean);
		List<String> strList = new ArrayList<String>();
		for (Chac cc : list) 
		{
			strList.add(cc.getChacz());
		}
		
		if(!strList.contains(bean.getChacz())){
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_kac.insertChac", bean);		//增加数据库
			
			chaccwService.save(insert,userId, bean);    //增加叉车车位关系
			
		}else{
			throw new ServiceException("叉车组已被选择，请重新选择叉车组！");
		}
		return "保存成功";
	} 

	
	/**
	 * 单条记录删除
	 * @param bean
     * @author liushaung
	 * @date 2015-1-20
	 * @return String
	 */
	@Transactional
	public String Delete(Chac bean) throws ServiceException{
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_kac.deleteChaccw", bean);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_kac.deleteChac", bean);
		return "删除成功";
	}
	
	/**
	 * 单条记录删除
	 * @param bean
     * @author liushaung
	 * @date 2015-1-20
	 * @return String
	 */
	@Transactional
	public String Deletecc(Chac bean) throws ServiceException{
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_kac.deleteChaccw", bean);
		return "删除成功";
	}
	
	
}
