/**
 * 
 */
package com.athena.component.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.component.entity.Domain;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.ibatis.support.AbstractIBatisDao;
import com.toft.core3.jdbc.support.MultiDataSource;
import com.toft.core3.util.Assert;
import com.toft.utils.UUIDHexGenerator;

/**
 * @author Administrator
 *
 */
public class BaseService<T extends Domain> {
	@Inject 
	protected AbstractIBatisDao baseDao;
	
	/**
	 * 生成UUID
	 * @return
	 */
	protected  String getUUID(){
		return UUIDHexGenerator.getInstance().generate();
	}
	/**
	 * 
	 * @return
	 */
	protected long getSequence(String seqName){
		Map<String,String> parameterObject = 
			new HashMap<String,String>();
		parameterObject.put("seqName", seqName);
		Object seqObject = 
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CORE).selectObject("component.getSequence", parameterObject);
		Assert.notNull(seqObject,"未获得到序列值！");
		return new Long(seqObject.toString());
	}
	
	/**
	 * 分页查询
	 */
	public Map<String, Object> select(T bean) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CORE).selectPages(getSqlId(bean,"query"),bean,bean);
	}
	
	/**
	 * 多记录查询
	 */
	@SuppressWarnings("rawtypes")
	public List list(T bean) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CORE).select(getSqlId(bean,"query"),bean);
	}
	
	/**
	 * 单记录查询
	 */
	@SuppressWarnings("unchecked")
	public T get(T bean) throws ServiceException{
		Object result = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CORE).selectObject(getSqlId(bean,"get"),bean);
		return result==null?null:(T)result;
	}
	/**
	 * 保存数据
	 * @param user
	 * @return
	 */
	public Object save(T bean) throws ServiceException{
		boolean isUpdate = hasKey(bean);
		String sqlId;
		if(isUpdate){//更新
			sqlId = getSqlId(bean,"update");
		}else{//插入
			bean.setId(generateKey(bean));
			sqlId = getSqlId(bean,"insert");
		}
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CORE).execute(sqlId, bean);
		return bean.getId();
	}
	/**
	 * 删除
	 */
	public String doDelete(T bean) throws ServiceException{
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CORE).execute(getSqlId(bean,"delete"), bean);
		return bean.getId().toString();
	}
	/**
	 * 可覆盖
	 * @param user
	 * @return
	 */
	protected boolean hasKey(T bean){
		String id = bean.getId();
		return id!=null&&!"".equals(id);
	}
	
	protected String generateKey(T user){
		return getUUID();
	}
	
	protected String getNamespace(){
		return null;
	}
	
	private String getSqlId(T user,String type){
		String namespace = getNamespace();
		if(namespace==null){
			namespace = "";
		}else{
			namespace = namespace+".";
		}
		return namespace+type+user.getClass().getSimpleName();
	}
	
	/**
	 * 数据源切换操作
	 * @author 贺志国
	 * @date 2017-3-3
	 */
	public void changeSourceId(String sourceId){
		MultiDataSource.useDataSource(sourceId);
	}
}
