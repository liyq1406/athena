package com.athena.fj.module.service;

import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.fj.entity.Chelsb;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.date.DateUtil;
import com.athena.util.exception.ServiceException;
import com.toft.core3.Pageable;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;
/**
 * <p>
 * Title:车辆资源申报Service类
 * </p>
 * <p>
 * Description:定义车辆资源申报业务逻辑方法
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company: 软通动力
 * </p>
 * 
 * @author 贺志国
 * @E-mail zghe@isoftstone.com
 * @version v1.0
 * @date 2011-12-19
 */
@Component
public class ChelsbService extends BaseService<Chelsb> {
	@Inject
	private UserOperLog userOperLog;
	/**
	 * 车辆申报资源查询
	 * @author 贺志国
	 * @date 2011-12-19
	 * @param page 分页显示
	 * @param param 查询参数
	 * @return Map<String,Object>
	 */
	@Transactional
	public Map<String,Object> select(Pageable page,Map<String,String> param) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).selectPages("chelsb.queryChelsb",param,page);
	}
	
	
	/**
	 * 参考系车型查询
	 * @return 车型集合map
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectChex() throws ServiceException{
		return  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("chelsb.queryChexMap");
	}
	
	/**
	 * 参考系运输商查询
	 * @return 车型集合map
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectYunss(String usercenter) throws ServiceException{
		return  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).select("chelsb.queryYunssMap",usercenter);
	}
	
	/**
	 * 批量保存行编辑数据（增加、删除操作）
	 * @param insertList 批量增加的行数据集合
	 * @param deleteList 批量删除的行数据集合
	 * @param userID 用户ID
	 * @return String null,save_success,数据没有改变返回null，有数据增加或删除则返回save_success
	 */
	@Transactional
	public String save(List<Chelsb> insertList,List<Chelsb> deleteList,String username,String usercenter) throws ServiceException{
		if(insertList.size()==0&&deleteList.size()==0){
			userOperLog.addError(CommonUtil.MODULE_FJ, "车辆申报service", "车辆申报数据未变更", CommonUtil.getClassMethod(), "");
			return "null";
		}
		userOperLog.addError(CommonUtil.MODULE_FJ, "车辆申报service", "车辆申报批量增加开始", CommonUtil.getClassMethod(), "");
		batchInsert(insertList,username,usercenter);
		userOperLog.addError(CommonUtil.MODULE_FJ, "车辆申报service", "车辆申报批量删除开始", CommonUtil.getClassMethod(), "");
		batchDelete(deleteList,username);
		userOperLog.addError(CommonUtil.MODULE_FJ, "车辆申报service", "车辆申报批量操作结束", CommonUtil.getClassMethod(), "");
		return "save_success";
	}
	
	
	/**
	 * 车辆申报资源批量插入
	 * @param list 车辆申报资源集合
	 */
	@Transactional
	public void batchInsert(List<Chelsb> list,String username,String usercenter) throws ServiceException{
		for(Chelsb aChelsb : list) {
			aChelsb.setUsercenter(usercenter);
			aChelsb.setCreator(username);
			aChelsb.setCreateTime(DateUtil.curDateTime());
			aChelsb.setEditor(username);
			aChelsb.setEditTime(DateUtil.curDateTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("chelsb.insertChelsb",aChelsb); 
		}
		
	}
	
	/**
	 * 批量删除
	 * @author 贺志国
	 * @date 2011-12-19
	 * @update 2011-12-22
	 * @param bean 删除数据信息
	 */
	@Transactional
	public void batchDelete(List<Chelsb> list,String username) throws ServiceException{
		for(Chelsb aChelsb : list) {
			aChelsb.setCreator(username);
			aChelsb.setCreateTime(DateUtil.curDateTime());
			aChelsb.setEditor(username);
			aChelsb.setEditTime(DateUtil.curDateTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("chelsb.deleteChelsb", aChelsb);
		}
		
	}

	/**
	 * 返回sqlmap-xxx.xml配置文件中的namespace属性名
	 */
	@Override
	protected String getNamespace(){
		return "chelsb";
	}
	

}
