package com.athena.ckx.module.cangk.service;


import com.athena.ckx.entity.cangk.Xingzysts;
import com.athena.ckx.entity.cangk.Yansxlj;
import com.athena.ckx.util.DBUtil;
import com.athena.ckx.util.DBUtilRemove;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.dao.DataAccessException;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 零件类型验收比例设置
 * @author wangyu
 * @date 2012-2-6
 */
@Component
public class YansxljService extends BaseService<Yansxlj>{

	/**
	 * 获得命名空间
	 * @author wangyu
	 * @date 2012-2-6
	 */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}
	
	
	/**
	 * 批量保存方法
	 * @author wangyu
	 * @date 2012-2-6
	 * @param insert,edit,delete,userID
	 * @return ""
	 */
	@Transactional
	public String save(Yansxlj bean,Integer operant,String userID) throws ServiceException{
		if (1 == operant){
			inserts(bean,userID);
		}
		return "success";
	}
	
	/**
	 * 私有批量insert方法
	 * @author wangyu
	 * @date 2012-2-6
	 * @param insert,userID
	 * @return  ""
	 */
	@Transactional
	public String inserts(Yansxlj bean,String userID)throws ServiceException{
			//验收项编号是否存在
			Xingzysts xingzysts = new Xingzysts();
			xingzysts.setYansxbh(bean.getYansxbh());
			xingzysts.setBiaos("1");
			String mse = GetMessageByKey.getMessage("yanshouxbh")+bean.getYansxbh()+GetMessageByKey.getMessage("notexist");
			if(!DBUtil.checkCount(baseDao,"ts_ckx.getCountXingzysts", xingzysts)){
				throw new ServiceException(mse);
			}
//			String sql = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_xingzysts where yansxbh = '"+bean.getYansxbh()+"' and biaos = '1'";
//			DBUtilRemove.checkBH(sql,GetMessageByKey.getMessage("yanshouxbh")+bean.getYansxbh()+GetMessageByKey.getMessage("notexist"));
			//仓库编号是否存在
//			String sql = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_cangk where cangkbh = '"+bean.getCangkbh()+"' and biaos = '1'";
//			DBUtilRemove.checkBH(sql,GetMessageByKey.getMessage("仓库编号")+bean.getCangkbh()+GetMessageByKey.getMessage("notexist"));
			bean.setCreator(userID);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(userID);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			//先新增  零件类型验收项  在删除这个新增的零件类型 和 验收项  在新增 插入到 yansbllj 表中
			try {
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertYansxlj",bean);
			} catch (DataAccessException e) {
				throw new ServiceException("零件类型验收项中已存在该数据");
			}
			//baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteByMob",bean);//删除模板数据
			try {
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertByMob", bean);//根据模板生成数据
			} catch (DataAccessException e) {
				throw new ServiceException("零件验收比例设置中已存在该数据");
			}
		return "";
	}
	
	
	/**
	 * 私有批量删除方法
	 * @author wangyu
	 * @date 2012-2-6
	 * @param delete,userID
	 * @return ""
	 */
	@Transactional
	public String deletes(Yansxlj bean,String userID)throws ServiceException{
			bean.setEditor(userID);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteYansxlj",bean);
		return "";
	}
	
	
	/**
	 * 失效
	 * @author wangyu
	 * @date 2012-2-22
	 * @return 主键
	 */
	@Transactional
	public String doDelete(Yansxlj bean) throws ServiceException{
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteByMob",bean);//删除模板数据
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteYansxlj", bean);
		return bean.getYansxbh();
	}
}