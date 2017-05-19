package com.athena.ckx.module.xuqjs.service;


import java.util.ArrayList;
import java.util.List;

import com.athena.ckx.entity.xuqjs.CkxLingjxhd;
import com.athena.ckx.entity.xuqjs.Xiaohc;
import com.athena.ckx.entity.xuqjs.Xiaohccx;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 小火车车厢
 * @author denggq
 * @date 2012-4-10
 */
@Component
public class XiaohccxService extends BaseService<Xiaohccx> {
	
	/**
	 * 获得命名空间
	 * @author denggq
	 * @date 2012-4-10
	 */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}
	
	
	/**
	 * 批量保存方法
	 * @author denggq
	 * @date 2012-4-10
	 * @param insert,edit,delete,userID
	 * @return ""
	 */
	@Transactional
	public String save(ArrayList<Xiaohccx> insert,
	           ArrayList<Xiaohccx> edit,
	   		   ArrayList<Xiaohccx> delete,String userID,Xiaohc xiaohc) throws ServiceException{
		if(0 == insert.size()&&0 == edit.size()&&0 == delete.size()){//无操作
			return "null";
		}
		inserts(insert,userID,xiaohc);//增加
		edits(edit,userID,xiaohc);//修改
//		deletes(delete,userID,xiaohc);//失效
		return "success";
	}
	
	/**
	 * 私有批量insert方法
	 * @author denggq
	 * @date 2012-4-10
	 * @param insert,userID
	 * @return  ""
	 */
	@Transactional
	public String inserts(List<Xiaohccx> insert,String userID,Xiaohc xiaohc)throws ServiceException{
		for(Xiaohccx bean:insert){
			bean.setUsercenter(xiaohc.getUsercenter());//用户中心
			bean.setShengcxbh(xiaohc.getShengcxbh());//生产线编号
			bean.setXiaohcbh(xiaohc.getXiaohcbh());//小火车编号
			bean.setCreator(userID);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(userID);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertXiaohccx",bean);//增加小火车车厢
		}
		return "";
	}
	/**
	 * 私有批量update方法
	 * @author denggq
	 * @date 2012-4-10
	 * @param edit,userID
	 * @return ""
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public String edits(List<Xiaohccx> edit,String userID,Xiaohc xiaohc) throws ServiceException{
		for(Xiaohccx bean:edit){
			if("0".equals(bean.getBiaos())){
				CkxLingjxhd ckxLingjxhd = new CkxLingjxhd();
				ckxLingjxhd.setUsercenter(bean.getUsercenter());
				ckxLingjxhd.setShengcxbh(xiaohc.getShengcxbh());
				ckxLingjxhd.setXiaohcbh(xiaohc.getXiaohcbh());
				ckxLingjxhd.setXiaohccxbh(bean.getChexh());
				ckxLingjxhd.setBiaos("1");
				List<CkxLingjxhd> list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryCkxLingjxhd", ckxLingjxhd);//获得小火车下得所有车厢
				if(0 != list.size()){//小火车车厢是否存在零件消耗点中，若不存在则可以失效
					throw new ServiceException(GetMessageByKey.getMessage("ljxhdyongdao"));
				}
			}
			bean.setUsercenter(xiaohc.getUsercenter());//用户中心
			bean.setShengcxbh(xiaohc.getShengcxbh());//生产线编号
			bean.setXiaohcbh(xiaohc.getXiaohcbh());//小火车编号
			bean.setEditor(userID);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateXiaohccx",bean);//更新小火车车厢
		}
		return "";
	}
	/**
	 * 私有批量删除方法
	 * @author denggq
	 * @date 2012-4-10
	 * @param delete,userID
	 * @return ""
	 */
	@Transactional
	public String deletes(List<Xiaohccx> delete,String userID,Xiaohc xiaohc)throws ServiceException{
		for(Xiaohccx bean:delete){
			bean.setUsercenter(xiaohc.getUsercenter());//用户中心
			bean.setShengcxbh(xiaohc.getShengcxbh());//生产线编号
			bean.setXiaohcbh(xiaohc.getXiaohcbh());//小火车编号
			bean.setEditor(userID);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteXiaohccx",bean);//失效小火车车厢
		}
		return "";
	}
	
	
	public String remove(Xiaohccx bean) throws ServiceException{
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.removeXiaohccx",bean);//删除小火车车厢
		return "deletesuccess";
	}
}
