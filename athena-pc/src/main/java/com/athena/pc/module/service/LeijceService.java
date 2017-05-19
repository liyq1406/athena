package com.athena.pc.module.service;

import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.pc.entity.Leijce;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.date.DateUtil;
import com.toft.core3.Pageable;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;
import com.athena.db.ConstantDbCode;

/**
 * <p>
 * Title:累计交付差额业务类
 * </p>
 * <p>
 * Description:定义累计交付差额业务方法
 * </p>
 * <p>
 * Copyright: Copyright (c) 2012
 * </p>
 * <p>
 * Company: 软通动力
 * </p>
 * 
 * @author 贺志国
 * @E-mail zghe@isoftstone.com
 * @version v1.0
 * @date 2012-6-14
 */
@Component
public class LeijceService extends BaseService<Leijce> {
	@Inject
	private UserOperLog userOperLog;
	/**
	 * 查询累计交付差额
	 * @author 贺志国
	 * @date 2012-6-14
	 * @param page 分页查询
	 * @param param 零件编号
	 * @return map 结果集
	 */
	public Map<String,Object> selectLeijce(Pageable page,Map<String,String> param){
		//20170220-xss
	   return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).selectPages("leijce.queryLeijjf", param, page);
	   
	}
	
	/**
	 * 
	 * @author 贺志国
	 * @date 2012-6-14
	 * @param updateList 更新数据集合
	 * @param username 用户名称
	 * @return String 消息名称
	 */
	@Transactional
	public String updateLeijce(List<Leijce> updateList,String username){
		if(updateList.size()==0){
			userOperLog.addError(CommonUtil.MODULE_PC, "累计交付差额service", "累计交付差额数据未变更", CommonUtil.getClassMethod(), "");
			return "null";
		}
		batchUpdate(updateList,username);
		return "save_success";
	}
	
	/**
	 * 删除累计差额
	 * @author 贺志国
	 * @date 2012-6-14
	 * @param bean 实体集
	 */
	@Transactional
	public void deleteLeijce(Leijce bean){
		//20170220-xss
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("leijce.deleteLeijce", bean);
	}
	
	/**
	 * 批量更新累计差额
	 * @author 贺志国
	 * @date 2012-6-14
	 * @param updateList 页面更新结果集
	 * @param username 用户名
	 */
	@Transactional
	public void batchUpdate(List<Leijce> updateList,String username){
		for(Leijce leijce : updateList){
			leijce.setEditor(username);
			leijce.setCreate_time(DateUtil.curDateTime());
			//20170220-xss
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("leijce.updateLeijce", leijce);
		}
	}
	
}
