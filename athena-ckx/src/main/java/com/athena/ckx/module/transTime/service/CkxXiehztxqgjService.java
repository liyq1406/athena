package com.athena.ckx.module.transTime.service;


import java.util.HashMap;

import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.ckx.entity.transTime.CkxXiehztxqgj;

import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.dao.DataAccessException;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 卸货站台需求归集
 * @author kong
 */
@Component
public class CkxXiehztxqgjService extends BaseService<CkxXiehztxqgj>{
	//获取命名空间
	@Override
	protected String getNamespace() {
		return "transTime";
	}
	
	/**
	 * 添加卸货站台需求归集
	 * 从临时表2取数据
	 * @param user
	 * @return
	 */
	
	@Transactional
	public String addCkxXiehztxqgj(LoginUser user){
		Map<String,String> map=new HashMap<String,String>();
		map.put("creator", user.getUsername());//创建人
		map.put("createTime", DateTimeUtil.getAllCurrTime());//系统当前时间
		//判断卸货站台需求归集数据是否正确
//		List<Map<String,Object>> listMap = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("transTime.queryCkxXiehztxqgjByMxqcf",map);
//		List<Yicbj> listYicbc = new ArrayList<Yicbj>();
//		for (Map<String, Object> listM : listMap) {			
//			
//		}
//		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("ycbj.insertYcbjCKX", listYicbc);
		try {
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("transTime.deleteCkxXiehztxqgj");//清空需求归集表
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("transTime.insertCkxXiehztxqgj",map);//添加卸货站台需求归集
		} catch (DataAccessException e) {
			throw new ServiceException(e.getMessage());
		}
		return GetMessageByKey.getMessage("addSuccess");
	}
}
