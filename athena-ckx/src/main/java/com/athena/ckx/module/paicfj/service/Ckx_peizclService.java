package com.athena.ckx.module.paicfj.service;



import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.ckx.entity.paicfj.Ckx_peizcl;
import com.athena.ckx.entity.paicfj.Ckx_peizclzb;
import com.athena.ckx.util.DBUtilRemove;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 配载策略
 * @author hj
 * @Date 2012-02-21
 */
@Component
public class Ckx_peizclService extends BaseService<Ckx_peizcl> {


	@Inject 
	private Ckx_peizclzbService peizclzbService;
	protected String getNamespace() {
		return "ts_ckx";
	}
	/**
	 * 数据操作
	 * @author hj
	 * @Date 2012-02-21
	 * @param bean
	 * @param operate
	 * @param insert
	 * @param edit
	 * @param delete
	 * @param userID
	 * @return null|success，如果为null，则提示数据未变更，如果为success，则提示数据操作成功
	 * @throws ServiceException
	 */
	@Transactional
	public String save(Ckx_peizcl bean,	Integer operate,List<Ckx_peizclzb> insert,
			List<Ckx_peizclzb> edit,List<Ckx_peizclzb> delete,String userID) throws ServiceException{
		if (null == bean.getCelbh() && 0 == insert.size() && 0 == edit.size()&& 0 == delete.size()) {
			return "null";
		}
		// 如果operate为1，则产线组进行录入操作，若为2，则产线组进行编辑操作
		if (1 == operate) {
			inserts(bean, userID);
		} else {
			edits(bean, userID);
		}
		//子表的数据操作
		peizclzbService.inserts(insert, bean, userID);
		peizclzbService.edits(edit, bean, userID);
		peizclzbService.removes(delete, bean);
		return "success";
	}
	/**
	 * 数据录入
	 * @author hj
	 * @Date 2012-02-21
	 * @param bean
	 * @param userID
	 * @return ""
	 * @throws ServiceException
	 */
	private String inserts(Ckx_peizcl bean,String userID) throws ServiceException{
		bean.setCreator(userID);
		bean.setCreate_time(new Date());
		bean.setEditor(userID);
		bean.setEdit_time(bean.getCreate_time());
		
		Map<String,String> map2 = new HashMap<String,String>();
		map2.put("tableName", "ckx_chex");
		map2.put("chexbh", bean.getChexbh());
		map2.put("biaos", "1");
		// "车型表里不存在 车型编号为"+bean.getChexbh()+"的数据或数据已失效"
		String mes2 = GetMessageByKey.getMessage("cxbczcxbh")+bean.getChexbh()+GetMessageByKey.getMessage("sjsx");
		DBUtilRemove.checkYN(map2, mes2);
		
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertCkx_peizcl", bean);
		return "";
	}
	/**
	 * 数据编辑
	 * @author hj
	 * @Date 2012-02-21
	 * @param bean
	 * @param userID
	 * @return ""
	 * @throws ServiceException
	 */
	private String edits(Ckx_peizcl bean,String userID) throws ServiceException{
		bean.setEditor(userID);
		bean.setEdit_time(new Date());
		
		Map<String,String> map2 = new HashMap<String,String>();
		map2.put("tableName", "ckx_chex");
		map2.put("chexbh", bean.getChexbh());
		map2.put("biaos", "1");
		//"车型表里不存在 车型编号为"+bean.getChexbh()+"的数据或数据已失效" 
		String mes2 = GetMessageByKey.getMessage("cxbczcxbh")+bean.getChexbh()+GetMessageByKey.getMessage("sjsx");;
		DBUtilRemove.checkYN(map2, mes2);
		
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateCkx_peizcl", bean);
		return "";
	}
	/**
	 * 数据删除（物理删除）
	 * @author hj
	 * @Date 2012-02-21
	 * @param bean
	 * @param userID
	 * @return ""
	 * @throws ServiceException
	 */
	@Transactional
	public String remove(Ckx_peizcl bean) throws ServiceException{
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteCkx_peizcl",bean);
		List<Ckx_peizclzb> delete = new ArrayList<Ckx_peizclzb>();
		Ckx_peizclzb ckx_peizclzb = new Ckx_peizclzb();
		delete.add(ckx_peizclzb);
		peizclzbService.removes(delete, bean);
		return "success";
	}
	
}
