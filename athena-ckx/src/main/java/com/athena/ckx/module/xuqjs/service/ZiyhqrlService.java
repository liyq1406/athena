package com.athena.ckx.module.xuqjs.service;

import java.util.ArrayList;

import com.athena.ckx.entity.xuqjs.Ziyhqrl;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 资源获取日历Service
 * @author qizhongtao
 * @date 2012-4-16
 */
@Component
public class ZiyhqrlService extends BaseService<Ziyhqrl>{
	/**
	 * 获取命名空间
	 * */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}
	/**
	 * 批量数据保存
	 * @author qizhongtao
	 * @date 2012-4-16
	 * @param insert,delete,userName
	 * @return String
	 * */
	@Transactional
	public String save(ArrayList<Ziyhqrl> insert,ArrayList<Ziyhqrl> delete,String userName)throws ServiceException{
		if(0 == insert.size()&&0 == delete.size()){//无操作
			return "null";
		}else{
			inserts(insert,userName);//增加
			deletes(delete,userName);//删除
		}
		return "success";
	}
	/**
	 * 批量insert
	 * @author qizhongtao
	 * @date 2012-4-16
	 * @param insert,userName
	 * @return ""
	 * */
	@Transactional
	public String inserts(ArrayList<Ziyhqrl> insert,String userName)throws ServiceException{
		for (Ziyhqrl bean : insert) {
			bean.setCreator(userName);//增加人
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());//增加时间
			bean.setEditor(userName);//修改人
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());//修改时间
			if(DateTimeUtil.compare(bean.getZiyhqrq(), DateTimeUtil.getCurrDate())){//所输日期大于当前日期
				throw new ServiceException(GetMessageByKey.getMessage("bxwlrq"));
			}
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertZiyhqrl", bean);//增加数据库
		}
		return "";
	}
	/**
	 * 批量delete
	 * @author qizhongtao
	 * @date 2012-4-16
	 * @param delete,userName
	 * @return ""
	 * */
	@Transactional
	public String deletes(ArrayList<Ziyhqrl> delete,String userName)throws ServiceException{
		for (Ziyhqrl bean : delete) {
			bean.setEditor(userName);//修改人
			bean.setEdit_time(DateTimeUtil.getSimpleCurrDate());//修改时间
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteZiyhqrl", bean);//删除数据库
		}
		return "";
	}
}
