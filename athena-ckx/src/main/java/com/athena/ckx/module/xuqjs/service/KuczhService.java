package com.athena.ckx.module.xuqjs.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.athena.ckx.entity.cangk.Cangk;
import com.athena.ckx.entity.xuqjs.Gongysxhc;
import com.athena.ckx.entity.xuqjs.Kuczh;
import com.athena.ckx.entity.xuqjs.Usercenter;
import com.athena.ckx.util.DBUtil;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

@Component
public class KuczhService extends BaseService<Kuczh> {
	static Logger log = Logger.getLogger(KuczhService.class);
	/**
	 * 获得命名空间
	 * @author zbb
	 * @date 2015-12-12
	 */
	@Override
	protected String getNamespace() {
		return "Kuczh";
	}
	
	/**
	 * 分页查询供应商小火车页面
	 * @param bean
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, Object> selectKuczh(Kuczh bean) throws ServiceException {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ckxKuczh.queryKuczhByParam",bean,bean);
	}
	
	/**
	 * 保存库存置换
	 * @param insert
	 * @param edit
	 * @param delete
	 * @author zbb
	 * @date 2015-12-12
	 * @return String
	 */
	@Transactional
	public String save(ArrayList<Kuczh> insert , ArrayList<Kuczh> edit , ArrayList<Kuczh> delete ,String userId) throws ServiceException{
		if(0 == insert.size()&&0 == edit.size()&&0 == delete.size()){
			return "null";
		}
		String result = checkData(insert,edit);
		if(result!=null){
			return result;
		}
		log.info("参考系-库存置换-失效数据");
		deletes(delete,userId);
		log.info("参考系-库存置换-增加数据");
		inserts(insert,userId);
		log.info("参考系-库存置换-修改数据");
		edits(edit,userId);
		log.info("参考系-刷新用户中心缓存");
				
		return "success";
	}
	/**
	 * 私有批量增加方法
	 * @author denggq
	 * @date 2012-1-16
	 * @param list,username
	 * @return  ""
	 */
	@Transactional
	public String inserts(List<Kuczh> insert,String username)throws ServiceException{
		for(Kuczh bean:insert){
			bean.setCreator(username);	
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(username);		
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ckxKuczh.insertKuczh",bean);
		}
		return "";
	}
	
	/**
	 * 私有批量修改方法
	 * @author denggq
	 * @date 2012-1-16
	 * @param list,username
	 * @return ""
	 */
	@Transactional
	public String edits(List<Kuczh> edit,String username) throws ServiceException{
		for(Kuczh bean:edit){
			bean.setEditor(username);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ckxKuczh.updateKuczhByParam",bean);
		}
		return "";
	}
	
	/**
	 * 私有批量删除方法
	 * @author denggq
	 * @date 2012-3-19
	 * @param list,username
	 * @return ""
	 */
	@Transactional
	public String deletes(List<Kuczh> delete,String username)throws ServiceException{
		for(Kuczh bean:delete){
			bean.setEditor(username);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ckxKuczh.deleteKuczh",bean);
		}
		return "";
	}
	
	
	private String checkData(ArrayList<Kuczh> insert , ArrayList<Kuczh> edit){
		String result = null;
		Kuczh kuczh=null;
		Cangk cangk = null;
		Map<String,String> param = new HashMap<String,String>();	
		//用户中心仓库是否存在，主键是否重复
		for(Kuczh bean:insert){
			param.put("cangkbh", bean.getCangk());
			param.put("usercenter", bean.getUsercenter());
			cangk = (Cangk) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getCangk",param);
			if(cangk==null){
				result= "yonghzxckbcz";//用户中心仓库不存在
				return result;
			}
			kuczh = (Kuczh) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ckxKuczh.queryKuczhByParam",bean);
			if(kuczh!=null){
				result = "yonghzxckysz";//用户中心仓库已设置
				return result;
			}
			param.clear();
		}
		//用户中心仓库是否存在
		for(Kuczh bean:edit){
			param.put("cangkbh", bean.getCangk());
			param.put("usercenter", bean.getUsercenter());
			cangk = (Cangk) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getCangk",param);
			if(cangk==null){
				result= "yonghzxckbcz";//用户中心仓库不存在
				return result;
			}			
			param.clear();
		}
		return result;
	}
}
