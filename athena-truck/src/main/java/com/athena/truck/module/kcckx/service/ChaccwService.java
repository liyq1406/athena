package com.athena.truck.module.kcckx.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.truck.entity.Chac;
import com.athena.truck.entity.Chew;
import com.athena.util.date.DateUtil;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 叉车-车位
 * @author liushuang
 * @date 2015-1-20
 */
@Component
public class ChaccwService extends BaseService<Chew>{
	/**
	 * 获得命名空间
	 * @author liushuang
	 * @date 2015-1-20
	 */
	@Override
	protected String getNamespace() {
		return "ts_kac";
	}
	
	//查询车位
	@SuppressWarnings("rawtypes")
	public List list(Chew bean) throws ServiceException {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_kac.queryChew",bean);
		
	}
	//查询车位
	@SuppressWarnings("rawtypes")
	public Map<String, Object> listchaccw(Chew bean) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ts_kac.queryChaccw",bean,bean);
	}
	

	/**
	 * 批量保存方法
	 * @author liushuang
	 * @date 2015-1-7
	 * @param insert
	 * @return ""
	 */
	@Transactional
	public String save(ArrayList<Chew> insert,String userID,Chac chac) throws ServiceException{
		if(0 == insert.size()){//无操作
			return "";
		}
		inserts(insert,userID,chac);//增加
		return "success";
	}
	
	/**
	 * 私有批量insert方法
	 * @author liushuang
	 * @date 2015-1-7
	 * @param insert,userID
	 * @return  ""
	 */
	@Transactional
	public String inserts(List<Chew> insert,String userID,Chac chac)throws ServiceException{
		for(Chew bean:insert){
			bean.setUsercenter(chac.getUsercenter());//用户中心
			bean.setQuybh(chac.getQuybh());          //区域编号
			bean.setDaztbh(chac.getDaztbh()); 
			bean.setChacbh(chac.getChacbh()); 
			bean.setCreator(userID);
			bean.setCreate_time(DateUtil.curDateTime());
			bean.setEditor(userID);
			bean.setEdit_time(DateUtil.curDateTime());
			
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_kac.insertChaccw",bean);//增加数据库
			
		}
		return "";
	}
	

}