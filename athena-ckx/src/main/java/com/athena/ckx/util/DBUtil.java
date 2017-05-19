package com.athena.ckx.util;

import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.ibatis.support.AbstractIBatisDao;


/**
 * 数据工具类
 * @author 
 *
 */
public final class DBUtil {
	
	/**
	 * 检查数据是否存在
	 * @param baseDao
	 * @param sqlId 
	 * @param bean
	 * @return 存在true  不存在  false
	 */
	public static boolean checkCount(AbstractIBatisDao baseDao,String sqlId,Object bean){
		Object obj= baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject(sqlId,bean);
		try {
			return Integer.valueOf(obj.toString())==0?false:true;
		} catch (NumberFormatException e) {
			throw new ServiceException(e.getMessage());
		}
	}
	/**
	 * 检查数据是否存在
	 * @param baseDao
	 * @param sqlId 
	 * @param bean
	 * @return 存在为空，  不存在  返回mes
	 */
	public static void checkCount(AbstractIBatisDao baseDao,String sqlId,Object bean,String mes){
		try {
			if(!checkCount(baseDao, sqlId, bean)){
				throw new ServiceException(mes);
			}
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
	}

}
