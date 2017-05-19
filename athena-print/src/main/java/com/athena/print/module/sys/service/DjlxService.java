package com.athena.print.module.sys.service;

import java.util.List;
import org.apache.log4j.Logger;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.print.entity.sys.Danjlx;
import com.athena.util.date.DateUtil;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;


/**
 *单据管理
 * @author wy
 * 2012-3-19
 */
@Component
public class DjlxService extends BaseService<Danjlx>{
	
	private static Logger log = Logger.getLogger(DjlxService.class);
	

	@Override
	public String getNamespace() {
		return "sys";
	}

	
	/**
	 * 保存
	 * @param list
	 * @param username
	 * @return String
	 * @author wy
	 * @time 2012-3-6
	 */
	@Transactional
	public String save(List<Danjlx> insert,List<Danjlx> edit, List<Danjlx> delete,String username) throws ServiceException{
		if(0 == insert.size()&&0 == edit.size()&&0 == delete.size()){
			return "null";
		}
		inserts(insert,username);
		
		edits(edit,username);
		
		deletes(delete,username);
		
		return "success";
	}
	
	
	/**
	 * 私有批量增加方法
	 * @author wy
	 * @date 2012-1-16
	 * @param list,username
	 * @return  ""
	 */
	@Transactional
	public String inserts(List<Danjlx> insert,String username)throws ServiceException{
		for(Danjlx bean:insert){
			bean.setCreator(username);
			bean.setCreate_time(DateUtil.curDateTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("sys.insertDanjlx",bean);
		}
		return "";
	}
	
	/**
	 * 私有批量修改方法
	 * @author wy
	 * @date 2012-1-16
	 * @param list,username
	 * @return ""
	 */
	@Transactional
	public String edits(List<Danjlx> edit,String username) throws ServiceException{
		for(Danjlx bean:edit){
			bean.setEditor(username);
			bean.setEdit_time(DateUtil.curDateTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("sys.updateDanjlx",bean);
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
	public String deletes(List<Danjlx> delete,String username)throws ServiceException{
		for(Danjlx bean:delete){
			bean.setEditor(username);
			bean.setEdit_time(DateUtil.curDateTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("sys.deleteDanjlx",bean);
		}
		return "";
	}
	
	
}
