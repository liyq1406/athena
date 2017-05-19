/**
 * 代码声明
 */
package com.athena.print.module.sys.service;

import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;
import com.athena.print.entity.sys.PrintDictinfos;
import com.athena.print.entity.sys.PrintStrogdict;
import com.athena.util.cache.CacheManager;
import com.athena.util.date.DateUtil;
import com.athena.util.exception.ServiceException;

@Component
public class PrintDictinfosService extends BaseService<PrintDictinfos>{
	
	@Inject
	private CacheManager cacheManager;//缓存
	
	@Override
	//返回sqlMap的命名空间
	protected String getNamespace() {
		return "sys";
	}
	
	//单表行编辑的 增 删改 操作
	@Transactional
	public String saves(List<PrintDictinfos> insert,List<PrintDictinfos> edit, List<PrintDictinfos> delete,String userId) throws ServiceException{
		if(0 == insert.size()&&0 == edit.size()&&0 == delete.size()){
			return "null";
		}
		//批量新增操作
		inserts(insert,userId);
		//批量的修改操作
		edits(edit,userId);
		//批量删除操作
		deletes(delete,userId);	
		//新增后刷新缓存数据
		cacheManager.refreshCache("queryDjlx");//刷新缓存
		
		return "success";
	}
	
	/**
	 * 私有批量update方法
	 * @author wy
	 * @date 2011-2-9
	 * @param update
	 * @return ""
	 */
	private String edits(List<PrintDictinfos> update,String userId) throws ServiceException{
		//判断是否为空 null!=update
		if(0!=update.size()){
			for(PrintDictinfos printDictinfos:update){
				//设置修改时间
				printDictinfos.setEditor(userId);
				printDictinfos.setEdit_time(DateUtil.curDateTime());
				//执行批量的更新
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).execute("sys.updatePrintDictinfos", printDictinfos);
			}
		}
		return "Data change success";
	}
	/**
	 * 私有批量增加方法
	 * @author wy
	 * @date 2012-1-16
	 * @param list,username
	 * @return  ""
	 */
	@Transactional
	public String inserts(List<PrintDictinfos> insert,String username)throws ServiceException{
		for(PrintDictinfos bean:insert){
			bean.setCreator(username);
			bean.setCreate_time(DateUtil.curDateTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("sys.insertPrintDictinfo",bean);
		}
		return "";
	}
	
	/**
	 * 私有批量删除方法
	 * @author wy
	 * @date 2012-3-19
	 * @param list,username
	 * @return ""
	 */
	@Transactional
	public String deletes(List<PrintDictinfos> delete,String username)throws ServiceException{
		for(PrintDictinfos bean:delete){
//			PrintStrogdict printDictinfos = (PrintStrogdict)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("sys.queryPrintStrogdicto",bean);
//			if(null!=printDictinfos.getDanjzbh()){
//				throw new ServiceException("单据已设定单据组,请先在仓库取消单据组后在进行删除操作");
//			}
			bean.setEditor(username);
			bean.setEdit_time(DateUtil.curDateTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("sys.deleteDictinfos",bean);
			PrintStrogdict printStrogdict = new PrintStrogdict();
			printStrogdict.setUsercenter(bean.getUsercenter());
			printStrogdict.setZidbm(bean.getZidbm());
			printStrogdict.setZidmc(bean.getZidmc());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("sys.deletePrintStrogdicts",printStrogdict);
		}
		return "";
	}
	
	
	/**
	 * 带条件查询
	 * @return 
	 */
	public Map<String, Object> selectU(PrintDictinfos bean) {
		return  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).selectPages("sys.queryPrintDictinfos1",bean,bean);
	}
	
}