/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company: 软通动力
 * </p>
 * 
 * @author 穆伟
 * @version v1.0
 * @date 
 */
package com.athena.authority.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.athena.authority.entity.Dic;
import com.athena.component.service.BaseService;
import com.athena.component.service.Message;
import com.athena.db.ConstantDbCode;
import com.toft.core3.container.annotation.Component;
import com.toft.utils.JSONUtils;
import com.toft.utils.json.JSONException;

@Component
public class DicService extends BaseService<Dic>{
	// log4j日志初始化
	private final Log log = LogFactory.getLog(DicService.class);
	/**
	 * 获取模块空间
	 */
	@Override
	protected String getNamespace() {
		return "authority";
	}
	/**
	 * 更新业务
	 * @param bean
	 */
	public void update(Dic bean) {
		log.info("dic_code"+bean.getDicCode());
		String dicCode=bean.getDicCode();
		if(dicCode!=null&&!"".equals(dicCode)){
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).execute(getNamespace()+".updateDic", bean);
		}else{
			log.error(" dicCode is null");
		}
		
	}
	/**
	 * 删除业务信息
	 * 删除时，作逻辑判断，已被用户组使用了的业务，则不能被删除
	 * @param bean
	 * @return
	 */
	public Message delete(Dic bean) {
		log.info("dic_code"+bean.getDicCode());
		String dicCode=bean.getDicCode();
		Message msg = null;
		if(dicCode!=null&&!"".equals(dicCode)){
			Integer dicCounts = (Integer) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).selectObject(getNamespace()+".countPostByDic", bean);
			if(dicCounts==0){
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).execute(getNamespace()+".deleteDic", bean);
				msg = new Message("删除成功");
			}else{
				msg = new Message("请确认该业务角色未被用户组使用!");
			}
		}else{
			log.error(" dicCode is null");
		}
		return msg;
	}
	/**
	 * 验证维一性
	 * @param map
	 * @return
	 */
	public String validateOnlyDic(Map<String, Object> map) {
		Integer count = (Integer)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).selectObject(getNamespace()+".validateOnlyDic",map);
		String msg = "";
		if(count>0){
			msg = "1";
		}else if(count==0){
			msg = "0";
		}
		return msg;
	}
	/**
	 * 刷新缓存queryDicCache
	 * @return
	 */
	public Object refreshAllData() {
		return  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).select("authority.queryDicCache");
	}
	/**
	 * 条件：dicCode
	 * 将返回的HashMap值进行处理生成业务BEAN:DIC，重新生成JSON数据
	 * @param map
	 * @return
	 */
	public List<String> getDicByDicCode(Map<String, Object> map) {
		List<Map<String,String>> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_AUTHORITY).select(getNamespace()+".getDicByDicCode",map);
		List<String> resultList = new ArrayList<String>();
		for(Map<String,String> resultmap:list){
			String postGroupId = resultmap.get("POST_GROUP_ID");
			//POST_GROUP_NAME以POST_CODE(POST_GROUP_NAME)为数据
			String postGroupName = resultmap.get("POST_GROUP_NAME");
			String dicCode = resultmap.get("DIC_CODE");
			String dicName = resultmap.get("DIC_NAME");
			Dic dic = new Dic();
			dic.setPostGroupId(postGroupId);
			if(postGroupName.equals("()")){
				postGroupName = "";
			}
			dic.setPostGroupName(postGroupName);
			dic.setDicCode(dicCode);
			dic.setDicName(dicName);
			try {
				String result = JSONUtils.toJSON(dic);
				resultList.add(result);
			} catch (JSONException e) {
				// log.info(e.toString());
			}
		}
		return resultList;
	}
	
	
}
