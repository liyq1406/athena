package com.athena.ckx.module.cangk.service;

import java.util.List;

import com.athena.ckx.entity.cangk.Yansbllj;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.ibatis.support.AbstractIBatisDao;
import com.toft.core3.transaction.annotation.Transactional;


/**
 * 维护零件供应商接口
 * @author denggq
 * @date 2012-2-9
 * @modify 2012-02-22
 */
@Component
public class YansblljInterface {

	@Inject
	private AbstractIBatisDao baseDao;
	
	/**
	 * 零件供应商接口
	 * @author denggq
	 * @date 2012-5-8
	 * @param bean 
	 * @return flag
	 */
	@SuppressWarnings({"unchecked"})
	@Transactional
	public void  commit(String usercenter, String lingjbh, String gongysbh , String userId) throws ServiceException {
		
		Yansbllj bean=new Yansbllj();	  //java bean
		bean.setUsercenter(usercenter);   //用户中心
		bean.setLingjbh(lingjbh);         //零件编号
		bean.setGongysbh(gongysbh);       //供应商编号
		bean.setCreator(userId);
		bean.setCreate_time(DateTimeUtil.getAllCurrTime());
		bean.setEditor(userId);
		bean.setEdit_time(DateTimeUtil.getAllCurrTime());
		List<Yansbllj> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.getYansbllj",bean);
		if(0 == list.size()){
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertByLingjgys", bean);//生成【零件验收比例设置】数据
		}

	}
}