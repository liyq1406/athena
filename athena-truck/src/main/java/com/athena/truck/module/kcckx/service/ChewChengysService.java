package com.athena.truck.module.kcckx.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.truck.entity.Chew;
import com.athena.truck.entity.ChewChengys;
import com.athena.util.date.DateUtil;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;
/**
 * 车位承运商
 * @author wangliang
 * @date 2015-01-24
 */
@Component
public class ChewChengysService extends BaseService<ChewChengys> {
	/**
	 * 获得命名空间
	 * @author wangliang
	 * @date 0215-01-24
	 */
	@Override
	protected String getNamespace() {
		return "ts_kac";
	}
	
	/**
	 * 获取多个chengys
	 * @param bean
	 * @return List
	 * @author wangliang
	 * @date 0215-01-24
	 */

	@SuppressWarnings("rawtypes")
	public List list(ChewChengys bean) throws ServiceException {
		
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_kac.queryChewChengys",bean);
	}
	
	
	@SuppressWarnings("unchecked")

	public List<ChewChengys> getx(ChewChengys bean) throws ServiceException {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_kac.queryChengys",bean);
	}
	
	
	public Map<String, Object> querychewcc(ChewChengys bean) throws ServiceException {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ts_kac.querychewcc",bean,bean);
	}
	
	/**
	 * 批量保存方法
	 * @author wangliang
	 * @date 0215-01-24
	 * @param insert,edit,delete,userID
	 * @return ""
	 */
	@Transactional
	public String save(ArrayList<ChewChengys> insert,
	           ArrayList<ChewChengys> edit,
	   		   ArrayList<ChewChengys> delete,String userID,Chew chew) throws ServiceException{
		if(0 == insert.size()&& 0 == edit.size()&& 0 == delete.size()){//无操作
			return "null";
		}
		inserts(insert,userID,chew);//增加
		edits(edit,userID,chew);//修改
		deletes(delete,userID,chew);//删除
		return "success";
	}
	
	/**
	 * 私有批量insert方法
	 * @author wangliang
	 * @date 0215-01-24
	 * @param insert,userID
	 * @return  ""
	 */
	@Transactional
	@SuppressWarnings("unchecked")
	public String inserts(List<ChewChengys> insert,String userID,Chew chew)throws ServiceException{
		for(ChewChengys bean:insert){
			bean.setUsercenter(chew.getUsercenter());//用户中心
			bean.setChewbh(chew.getChewbh());          //车位编号
			bean.setDaztbh(chew.getDaztbh());			//大站台编号
			bean.setCreator(userID);
			bean.setCreat_time(DateUtil.curDateTime());
			bean.setEditor(userID);
			bean.setEdit_time(DateUtil.curDateTime());
			List<ChewChengys> list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_kac.queryChengys",bean);//增加数据库
			if(list.size()==1){
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_kac.insertChewChengys",bean);//增加数据库
			}else{
				throw new ServiceException("承运商不存在，不允许添加！");
			}
		}
		return "";
	}
	/**
	 * 私有批量update方法
	 * @author wangliang
	 * @date 0215-01-24
	 * @param edit,userID
	 * @return ""
	 */
	@Transactional
	@SuppressWarnings("unchecked")
	public String edits(List<ChewChengys> edit,String userID,Chew chew) throws ServiceException{
		for(ChewChengys bean:edit){
			bean.setUsercenter(chew.getUsercenter());//用户中心	
			bean.setChewbh(chew.getChewbh());         //车位编号
			bean.setDaztbh(chew.getDaztbh());			//大站台编号
			bean.setEditor(userID);
			bean.setEdit_time(DateUtil.curDateTime());
			List<ChewChengys> list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_kac.queryChengys",bean);//增加数据库
			if(list.size()==1){
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_kac.updateChewChengys",bean);//更新数据库
			}else{
				throw new ServiceException("承运商不存在，不允许添加！");
			}
		}
		return "";
	}
	/**
	 * 私有批量删除方法
	 * @author wangliang
	 * @date 0215-01-24
	 * @param delete,userID
	 * @return ""
	 */
	@Transactional
	public String deletes(List<ChewChengys> delete,String userID,Chew chew)throws ServiceException{
		for(ChewChengys bean:delete){
			bean.setUsercenter(chew.getUsercenter());//用户中心
			bean.setChewbh(chew.getChewbh());   
			bean.setDaztbh(chew.getDaztbh());			//大站台编号
			bean.setEditor(userID);
			bean.setEdit_time(DateUtil.curDateTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_kac.deleteChewcys",bean);//失效数据库
		}
		return "";
	}
	
}
