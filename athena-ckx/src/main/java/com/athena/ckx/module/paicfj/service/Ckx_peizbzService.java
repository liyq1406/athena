package com.athena.ckx.module.paicfj.service;


import java.util.Date;
import java.util.List;

import com.athena.ckx.entity.paicfj.Ckx_peizbz;
import com.athena.ckx.entity.paicfj.Ckx_peizbzbh;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;
/**
 * 配载包装定义Service
 * @author hj
 * @Date 2012-02-21
 */

@Component
public class Ckx_peizbzService extends BaseService<Ckx_peizbz> {

	@Inject
	private Ckx_peizbzbhService peizbzbhService;
	protected String getNamespace() {
		return "ts_ckx";
	}
	/**
	 * 数据操作
	 * @author hj
	 * @Date 2012-02-21
	 * @param bean
	 * @param operate
	 * @param insert
	 * @param delete
	 * @param userID
	 * @return null|success，如果为null，则提示数据未变更，如果为success，则提示数据操作成功
	 * @throws ServiceException
	 */
	@Transactional
	public String save(Ckx_peizbz bean,
			Integer operate,
			List<Ckx_peizbzbh> insert,
			List<Ckx_peizbzbh> delete,
			String userID) throws ServiceException{
		if (null == bean.getBaozzbh() && 0 == insert.size()
				&& 0 == delete.size()) {
			return "";
		}
		// 如果operate为1，则产线组进行录入操作，若为2，则产线组进行编辑操作
		if (1 == operate) {
			inserts(bean, userID);
		} else {
			edits(bean, userID);
		}
		//子表的数据操作
		peizbzbhService.inserts(insert, bean, userID);
		peizbzbhService.removes(delete, userID);
		return "success";
	}
	/**
	 * 数据录入
	 * @author hj
	 * @Date 2012-02-21
	 * @param bean
	 * @param userID
	 * @return ""
	 * @throws ServiceException
	 */
	private String inserts(Ckx_peizbz bean,String userID) throws ServiceException{
		bean.setCreator(userID);
		bean.setCreate_time(new Date());
		bean.setEditor(userID);
		bean.setEdit_time(bean.getCreate_time());
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertCkx_peizbz", bean);
		return "";
	}
	/**
	 * 数据编辑
	 * @author hj
	 * @Date 2012-02-21
	 * @param bean
	 * @param userID
	 * @return ""
	 * @throws ServiceException
	 */
	private String edits(Ckx_peizbz bean,String userID) throws ServiceException{
		bean.setEditor(userID);
		bean.setEdit_time(new Date());
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateCkx_peizbz", bean);
		return "";
	}
	/**
	 * 数据删除（逻辑删除）
	 * @author hj
	 * @Date 2012-02-21
	 * @param bean
	 * @param userID
	 * @return ""
	 * @throws ServiceException
	 */
	@Transactional
	public String remove(Ckx_peizbz bean,String userID) throws ServiceException{
		bean.setEditor(userID);
		bean.setEdit_time(new Date());
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteCkx_peizbz", bean);
		return "success";
	}
	
}
