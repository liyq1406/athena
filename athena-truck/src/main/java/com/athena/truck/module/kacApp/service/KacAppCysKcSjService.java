package com.athena.truck.module.kacApp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.truck.entity.Chengyskc;
import com.athena.truck.entity.Kacsj;
import com.athena.truck.entity.Ucip;
import com.athena.util.cache.CacheManager;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;


/**
 * 卡车司机信息
 * @author CSY
 * 2016-9-9
 */
@Component
public class KacAppCysKcSjService extends BaseService<Ucip>{
	
	static Logger log = Logger.getLogger(KacAppCysKcSjService.class);
	
	@Inject
	private CacheManager cacheManager;//缓存

	@Override
	public String getNamespace() {
		return "kc_app";
	}

	/**
	 * 分页查询承运商-卡车
	 * @param bean
	 * @author CSY
	 * @date 2016-09-09	 
	 */
	public Map query(Chengyskc bean){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("kc_app.getChengyskc",bean,bean);
	}
	
	/**
	 * 分页查询卡车-司机
	 * @param bean
	 * @author CSY
	 * @date 2016-09-09	 
	 */
	public Map queryKacsj(Kacsj bean){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("kc_app.getKacsj",bean,bean);
	}
	
	/**
	 * 失效承运商卡车
	 * @param bean
	 * @author CSY
	 * @date 2016-9-9
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional
	public String doDelete(Chengyskc bean,Map<String,String> params) throws ServiceException{
		
		Kacsj sj=new Kacsj();	//承运商卡车bean
		sj.setKach(bean.getKach());
		
		List list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("kc_app.getKacsjYX", sj); // 查询此卡车号下有效司机信息

		if (0 == list.size()) { // 此卡车号下无司机信息
			int r = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("kc_app.deleteChengyskc", bean); // 失效等待区域
			if (r == 0) {
				return "更新条数为0，请检查实际失效结果";
			}
			return "失效成功！";
		}
		
		return "";
	}
	
	/**
	 * 保存卡车司机信息
	 * @param bean
	 * @param operant 1-新增  2-修改
	 * @author CSY
	 * @throws java.lang.Exception 
	 * @date 2016-9-12
	 */
	@SuppressWarnings({ "unchecked"})
	@Transactional
	public String save(Chengyskc bean,Integer operant,ArrayList<Kacsj> insert,ArrayList<Kacsj> edit,ArrayList<Kacsj> delete,String userId,Map<String,String> params) throws java.lang.Exception{
		
		bean.setEditor(userId);									//修改人
		if (1 == operant){			//增加
			bean.setCreator(userId);//增加人
			//先查询承运商代码是否正确
			int count = (Integer)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("kc_app.getCountOfCYS", bean);
			if (count<=0) {
				throw new java.lang.Exception("承运商不存在或无效，请重试！");
			}
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("kc_app.insertChengyskc", bean);		//增加数据库
			this.save(insert, edit, delete, userId, bean);//新增修改
		}else if(2 == operant){		//修改
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("kc_app.updateChengyskc", bean);		//修改数据库
			this.save(insert, edit, delete, userId, bean);//实际大站台增删改
		}
		return "保存成功";
	} 
	
	/**
	 * 批量保存
	 * @param insert
	 * @param edit
	 * @param delete
	 * @param userID
	 * @param chengyskc
	 * @return
	 * @throws ServiceException
	 */
	@Transactional
	public String save(ArrayList<Kacsj> insert,
	           ArrayList<Kacsj> edit,
	   		   ArrayList<Kacsj> delete,String userID,Chengyskc chengyskc) throws ServiceException{
		if(0 == insert.size()&&0 == edit.size()&&0 == delete.size()){//无操作
			return "null";
		}
		inserts(insert,userID,chengyskc);//增加
		edits(edit,userID,chengyskc);//修改
		deletes(delete,userID);//删除
		return "保存成功";
	}
	
	/**
	 * 批量插入卡车司机表
	 * @param insert
	 * @param userID
	 * @param chengyskc
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public String inserts(List<Kacsj> insert,String userID,Chengyskc chengyskc)throws ServiceException{
		for(Kacsj bean:insert){
			bean.setChengys(chengyskc.getChengys());//承运商
			bean.setKach(chengyskc.getKach());//卡车号
			bean.setCreator(userID);
			bean.setEditor(userID);
		
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("kc_app.insertKacsj",bean);//增加数据库
		}	
		return "";
	}
	
	/**
	 * 更新卡车司机
	 * @param edit
	 * @param userID
	 * @param chengyskc
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional
	public String edits(List<Kacsj> edit,String userID,Chengyskc chengyskc) throws ServiceException{
		for(Kacsj bean:edit){
			bean.setChengys(chengyskc.getChengys());//承运商
			bean.setKach(chengyskc.getKach());//卡车号
			bean.setCreator(userID);
			bean.setEditor(userID);
			
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("kc_app.updateKacsj",bean);//更新数据库
			
		}
		return "";
	}
	
	/**
	 * 私有批量删除方法
	 * @author CSY
	 * @date 2016-9-9
	 * @param list,username
	 * @return ""
	 */
	@Transactional
	public String deletes(List<Kacsj> delete,String userID)throws ServiceException{
		for(Kacsj bean:delete){
			bean.setEditor(userID);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("kc_app.deleteKacsj",bean);
		}
		return "";
	}
	
}
