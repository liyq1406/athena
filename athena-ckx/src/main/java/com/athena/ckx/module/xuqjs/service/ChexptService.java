package com.athena.ckx.module.xuqjs.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.ckx.entity.xuqjs.Chexpt;
import com.athena.ckx.util.DBUtilRemove;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;

/**
 * 车型平台关系
 * @author denggq
 * @date 2012-4-18
 */
@Component
public class ChexptService extends BaseService<Chexpt>{

	/**
	 * 获得命名空间
	 * @author denggq
	 * @date 2012-4-18
	 */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}
	
	
	/**
	 * 查询方法（外层）
	 * CSY 修改：执行修改前后添加切换数据源
	 * 2017-03-03
	 * @author wangyu
	 * @date 2012-9-20
	 * @param 
	 * @return ""
	 */
	public Map<String,Object> selectChexpt(Chexpt bean) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			this.changeSourceId(ConstantDbCode.DATASOURCE_EXTENDS2);
			map = this.getSelectChexpt(bean);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		} finally{
			this.changeSourceId(ConstantDbCode.DATASOURCE_CKX);
		}
		return map;
	}
	
	/**
	 * 查询方法（内层）
	 * CSY 修改：执行修改前后添加切换数据源
	 * 2017-03-03
	 * @author wangyu
	 * @date 2012-9-20
	 * @param 
	 * @return ""
	 */
	private Map<String,Object> getSelectChexpt(Chexpt bean) throws Exception{
		Map<String,Object> map = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).selectPages("ts_ckx.queryChexpt", bean, bean);
		return map;
	}
	
	///////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 批量保存方法（外层）
	 * CSY 修改：执行修改前后添加切换数据源
	 * 2017-03-03
	 * @author denggq
	 * @date 2012-4-18
	 * @param insert,edit,delete,userId
	 * @return ""
	 */
	public String save(ArrayList<Chexpt> insert,
	           ArrayList<Chexpt> edit,
	   		   ArrayList<Chexpt> delete,String userId) throws ServiceException{
		String resuslt = "";
		try {
			this.changeSourceId(ConstantDbCode.DATASOURCE_EXTENDS2);
			resuslt = this.getSave(insert, edit, delete, userId);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		} finally{
			this.changeSourceId(ConstantDbCode.DATASOURCE_CKX);
		}
		return resuslt;
	}
	
	/**
	 * 批量保存方法（内层）
	 * CSY 修改：执行修改前后添加切换数据源
	 * 2017-03-03
	 * @author denggq
	 * @date 2012-4-18
	 * @param insert,edit,delete,userId
	 * @return ""
	 */
	public String getSave(ArrayList<Chexpt> insert,
	           ArrayList<Chexpt> edit,
	   		   ArrayList<Chexpt> delete,String userId) throws ServiceException{
		if(0 == insert.size()&&0 == edit.size()&&0 == delete.size()){
			return "null";
		}
		inserts(insert,userId);
		edits(edit,userId);
		deletes(delete,userId);
		return "success";
	}
	
	
	
	/**
	 * 私有批量insert方法
	 * @author denggq
	 * @date 2012-4-18
	 * @param insert,userId
	 * @return  ""
	 */
	public String inserts(List<Chexpt> insert,String userId)throws ServiceException{
		//MultiDataSource.useDataSource(ConstantDbCode.DATASOURCE_EXTENDS2);
		for(Chexpt bean:insert){
			String shengcxbhzz = bean.getChejbhzz()+bean.getShengcxbhzz();//总装生产线编号
			String shengcxbhhz = bean.getChejbhhz()+bean.getShengcxbhhz();//焊装生产线编号
			
			//总装生产线编号是否存在
			String sql1 = "select count(*) from "+DBUtilRemove.getdbSchemal("dbSchemal5","ckx")+"ckx_shengcx where usercenter = '"+bean.getUsercenter()+"' and shengcxbh = '"+shengcxbhzz+"' and biaos = '1' and flag = '1'";
			DBUtilRemove.checkBHDDBH(sql1, GetMessageByKey.getMessage("zongzhuangsxc")+shengcxbhzz+GetMessageByKey.getMessage("notexist"));
			
			//焊装生产线编号是否存在
			String sql2 = "select count(*) from "+DBUtilRemove.getdbSchemal("dbSchemal5","ckx")+"ckx_shengcx where usercenter = '"+bean.getUsercenter()+"' and shengcxbh = '"+shengcxbhhz+"' and biaos = '1' and flag = '1'";
			DBUtilRemove.checkBHDDBH(sql2, GetMessageByKey.getMessage("hangzhuangsxc")+shengcxbhhz+GetMessageByKey.getMessage("notexist"));
			
			//生产平台编号是否存在
			String sql3 = "select count(*) from "+DBUtilRemove.getdbSchemal("dbSchemal5","ckx")+"ckx_shengcpt where usercenter = '"+bean.getUsercenter()+"' and shengcxbh = '"+shengcxbhhz+"'and shengcptbh = '"+bean.getShengcptbh()+"' and biaos = '1'";
			DBUtilRemove.checkBHDDBH(sql3, GetMessageByKey.getMessage("shengchangpt")+bean.getShengcptbh()+GetMessageByKey.getMessage("notexist"));
			
			bean.setCreator(userId);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(userId);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).execute("ts_ckx.insertChexpt",bean);
		}
		return "";
	}
	/**
	 * 私有批量update方法
	 * @author denggq
	 * @date 2012-4-18
	 * @param edit,userId
	 * @return ""
	 */
	public String edits(List<Chexpt> edit,String userId) throws ServiceException{
		for(Chexpt bean:edit){
			bean.setEditor(userId);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			String shengcxbhhz = bean.getChejbhhz()+bean.getShengcxbhhz();//焊装生产线编号
			//焊装生产线编号是否存在
			String sql2 = "select count(*) from "+DBUtilRemove.getdbSchemal("dbSchemal5","ckx")+"ckx_shengcx where usercenter = '"+bean.getUsercenter()+"' and shengcxbh = '"+shengcxbhhz+"' and biaos = '1' and flag = '1'";
			DBUtilRemove.checkBHDDBH(sql2, GetMessageByKey.getMessage("hangzhuangsxc")+shengcxbhhz+GetMessageByKey.getMessage("notexist"));
			
			//生产平台编号是否存在
			String sql3 = "select count(*) from "+DBUtilRemove.getdbSchemal("dbSchemal5","ckx")+"ckx_shengcpt where usercenter = '"+bean.getUsercenter()+"' and shengcxbh = '"+shengcxbhhz+"'and shengcptbh = '"+bean.getShengcptbh()+"' and biaos = '1'";
			DBUtilRemove.checkBHDDBH(sql3, GetMessageByKey.getMessage("shengchangpt")+bean.getShengcptbh()+GetMessageByKey.getMessage("notexist"));
			
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).execute("ts_ckx.updateChexpt",bean);
			
		}
		return "";
	}
	/**
	 * 私有批量删除方法
	 * @author denggq
	 * @date 2012-4-18
	 * @param delete,userId
	 * @return ""
	 */
	public String deletes(List<Chexpt> delete,String userId)throws ServiceException{
		for(Chexpt bean:delete){
			bean.setEditor(userId);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_EXTENDS2).execute("ts_ckx.deleteChexpt",bean);
			
		}
		return "";
	}
	
}
