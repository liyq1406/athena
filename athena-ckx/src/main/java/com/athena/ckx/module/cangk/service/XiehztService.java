package com.athena.ckx.module.cangk.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.cangk.Xiehzt;
import com.athena.ckx.entity.cangk.Xiehztbz;
import com.athena.ckx.entity.xuqjs.Baoz;
import com.athena.ckx.util.DBUtil;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.ckx.util.GetPostOnly;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 卸货站台
 * @author denggq
 * @date 2012-1-16
 */
@Component
public class XiehztService extends BaseService<Xiehzt>{
	
	/**
	 * 获得命名空间
	 * @author denggq
	 * @date 2012-1-16
	 */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}
	
	/**
	 * 分页查询方法
	 * @author denggq
	 * @date 2012-2-17
	 * @param bean
	 * @return Map 分页的结果
	 */
	@Transactional
	public Map<String, Object> select(Xiehzt bean) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ts_ckx.queryXiehzt",bean,bean);
	}
	/**
	 * 分页查询方法
	 * @author hj
	 * @date 2012-8-21
	 * @param bean
	 * @return Map 分页的结果
	 */
	@Transactional
	public Map<String, Object> selectBZ(Xiehztbz bean) throws ServiceException{
		Map<String,String> map = AuthorityUtils.getSecurityUser().getPostAndRoleMap();//一个人可对应多个业务角色
		String key = GetPostOnly.getPostAll(map);
		if(1 > key.indexOf("POA")&&!"root".equals(key)){
			if(0 > key.indexOf("WULGYY")){
				bean.setWulgyyz("null ");
			}else{				
				String value = (String) map.get("WULGYY");
				bean.setWulgyyz(value);
			}
		}
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ts_ckx.queryXiehztbz",bean,bean);
	}
	
	/**
	 * 批量保存方法
	 * @author denggq
	 * @date 2012-1-16
	 * @param insert,edit,delete,userID
	 * @return ""
	 */
	@Transactional
	public String save(
			Xiehztbz bean,Integer operate,
			ArrayList<Xiehzt> insert,
	           ArrayList<Xiehzt> edit,
	   		   ArrayList<Xiehzt> delete,String userID) throws ServiceException{
		if (null == bean.getXiehztbzh() && 0 == insert.size() && 0 == edit.size()&& 0 == delete.size()) {
			return "null";
		}
		// 如果operate为1，则进行录入操作，若为2，则进行编辑操作
		if (1 == operate) {
			insertXiehztBZ(bean, userID);
		} else {
			editXiehztBZ(bean, userID);
		}
		deletes(delete,userID,bean);
		inserts(insert,userID,bean);
		edits(edit,userID,bean);
		return "success";
	}
	/**
	 * 数据录入 卸货站台编组
	 * @param bean
	 * @param userID
	 * @return
	 */
	private String insertXiehztBZ(Xiehztbz bean,String userID){
		bean.setCreator(userID);
		bean.setCreate_time(DateTimeUtil.getAllCurrTime());
		bean.setEditor(userID);
		bean.setEdit_time(DateTimeUtil.getAllCurrTime());
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertXiehztbz",bean);
		return "";
	}
	/**
	 * 数据编辑 卸货站台编组
	 * @param bean
	 * @param userID
	 * @return
	 */
	private String editXiehztBZ(Xiehztbz bean,String userID){
		bean.setEditor(userID);
		bean.setEdit_time(DateTimeUtil.getAllCurrTime());
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateXiehztbz",bean);
		return "";
	}
	/**
	 * 私有批量insert方法
	 * @author denggq
	 * @date 2012-1-16
	 * @param insert,userID
	 * @return  ""
	 */	
	public String inserts(List<Xiehzt> insert,String userID,Xiehztbz xiehztbz)throws ServiceException{
		for(Xiehzt bean:insert){
			//大站台是否存在
			if(null != bean.getDaztbh()){
				DBUtil.checkCount(baseDao, "ts_ckx.getDaztbhCount", bean, GetMessageByKey.getMessage("daZhanTaiBianHao")+bean.getDaztbh()+GetMessageByKey.getMessage("notexist"));
			}
			bean.setUsercenter(xiehztbz.getUsercenter());
			bean.setXiehztbzh(xiehztbz.getXiehztbzh());
			bean.setXiehztbh(xiehztbz.getXiehztbzh()+bean.getXiehztbh());
			bean.setCreator(userID);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(userID);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertXiehzt",bean);
		}
		return "";
	}
	/**
	 * 私有批量update方法
	 * @author denggq
	 * @date 2012-1-16
	 * @param edit,userID
	 * @return ""
	 */
	
	public String edits(List<Xiehzt> edit,String userID,Xiehztbz xiehztbz) throws ServiceException{
		for(Xiehzt bean:edit){
			//大站台是否存在
			if(null != bean.getDaztbh()){
				DBUtil.checkCount(baseDao, "ts_ckx.getDaztbhCount", bean, GetMessageByKey.getMessage("daZhanTaiBianHao")+bean.getDaztbh()+GetMessageByKey.getMessage("notexist"));
			}
			
			bean.setUsercenter(xiehztbz.getUsercenter());
			bean.setXiehztbzh(xiehztbz.getXiehztbzh());
			bean.setXiehztbh(xiehztbz.getXiehztbzh()+bean.getXiehztbh());
			bean.setEditor(userID);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateXiehzt",bean);
		}
		return "";
	}
	/**
	 * 私有批量删除方法
	 * @author denggq
	 * @date 2012-1-16
	 * @param delete,userID
	 * @return ""
	 */	
	public String deletes(List<Xiehzt> delete,String userID,Xiehztbz xiehztbz)throws ServiceException{
		for(Xiehzt bean:delete){			
			bean.setUsercenter(xiehztbz.getUsercenter());
			bean.setEditor(userID);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteXiehzt",bean);
		}
		return "";
	}

	
	/**
	 * 失效
	 * @author denggq
	 * @date 2012-2-22
	 * @return 主键
	 */
	@Transactional
	public String doDelete(Xiehztbz bean) throws ServiceException{
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteXiehztbz", bean);
		return "shujusxcg";
	}
	
	/**
	 * 根据卸货站台获取工作时间
	 * hj
	 * 2012-03-26
	 * @param sqlId
	 * @return
	 */
	
	@SuppressWarnings("unchecked")
	public ArrayList<Xiehzt> getListCkxBiaozgzsj(String sqlId){
		return (ArrayList<Xiehzt>)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.getListCkxBiaozgzsj",sqlId);
	}
	
	/**
	 * 大站台编号
	 * @author hzg
	 * @param 
	 */
	@Transactional
	public String getDaztbhCount(Xiehzt bean) throws ServiceException  {
		String Dcount = (String)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getDaztbhCount",bean);;
		return Dcount;
	}
	
}
