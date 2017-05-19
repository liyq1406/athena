package com.athena.ckx.module.paicfj.service;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.ckx.entity.paicfj.Ckx_lingjkh;
import com.athena.ckx.util.DBUtilRemove;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;
/**
 * 零件客户Service
 * @author hj
 * @Date 2012-02-21
 */
@Component
public class Ckx_lingjkhService extends BaseService<Ckx_lingjkh> {

	protected String getNamespace() {
		return "ts_ckx";
	}
	/**
	 * 数据操作
	 * @param insert
	 * @param edit
	 * @param delete
	 * @param userID
	 * @return null|success，如果为null，则提示数据未变更，如果为success，则提示数据操作成功
	 * @throws ServiceException
	 */
	@Transactional
	public String save(Ckx_lingjkh bean, Integer operate
			,LoginUser user)throws ServiceException{
		// 如果operate为1，则产线组进行录入操作，若为2，则产线组进行编辑操作
		if (1 == operate) {
			inserts(bean, user);
		} else {
			edits(bean, user);
		}
		return "success";
		
	}
	/**
	 * 数据录入
	 * @author hj
	 * @Date 12/02/21
	 * @param insert
	 * @param userID
	 * @return ""
	 * @throws ServiceException
	 */
	private String inserts(Ckx_lingjkh bean,LoginUser user)throws ServiceException{
		if (DateTimeUtil.compare(bean.getShengxrq(), bean.getShixrq())) {
			Date date = new Date();
			bean.setCreator(user.getUsername());
			bean.setCreate_time(date);
			bean.setEditor(user.getUsername());
			bean.setEdit_time(date);
			Map<String,String> map1 = new HashMap<String,String>();
			Map<String,String> map2 = new HashMap<String,String>();
			Map<String,String> map3 = new HashMap<String,String>();
			map1.put("tableName", "ckx_lingj");
			map1.put("lingjbh", bean.getLingjbh());
			map1.put("usercenter", user.getUsercenter());
			map1.put("biaos", "1");
			map2.put("tableName", "ckx_kehb");
			map2.put("kehbh", bean.getKehbh());
			map3.put("tableName", "ckx_fahzt");
			map3.put("fahztbh", bean.getFahzt());
			map3.put("usercenter", bean.getUsercenter());
			map3.put("biaos", "1");
			//"零件表中不存在当前用户中心下零件编号为"+bean.getLingjbh()+"的数据或该数据已失效";
			String mes1 = GetMessageByKey.getMessage("ljbbczljbh")+bean.getLingjbh()+GetMessageByKey.getMessage("ysxlbcz");
			//"客户表中不存在当前用户中心下客户编号为"+bean.getKehbh()+"的数据或该数据已失效";
			String mes2 = GetMessageByKey.getMessage("khbckhbh")+bean.getKehbh()+GetMessageByKey.getMessage("ysxlbcz");
			//"发货站台表中不存在当前用户中心下发货站台编号为"+bean.getFahzt()+"的数据或该数据已失效";
			String mes3 = GetMessageByKey.getMessage("fhztbczfhbh")+bean.getFahzt()+GetMessageByKey.getMessage("ysxlbcz");
			DBUtilRemove.checkYN(map1, mes1);
			map1.clear();
			DBUtilRemove.checkYN(map2, mes2);
			map2.clear();
			if(null != bean.getFahzt()){
				DBUtilRemove.checkYN(map3, mes3);
				map3.clear();
			}	
			if(null != bean.getUabaoz()){
				// "包装表中不存在包装编号为"+bean.getUabaoz()+"的数据或该数据已失效";
				String m =  GetMessageByKey.getMessage("bczbaozhuang")+bean.getUabaoz()+GetMessageByKey.getMessage("ysxlbcz");
				String sql = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_baoz where biaos='1' and baozlx='"+bean.getUabaoz()+"' ";
				DBUtilRemove.checkBH(sql, m);
			}
			if(null != bean.getUcbaoz()){
				//"包装表中不存在包装编号为"+bean.getUcbaoz()+"的数据或该数据已失效";
				String m = GetMessageByKey.getMessage("bczbaozhuang")+bean.getUcbaoz()+GetMessageByKey.getMessage("ysxlbcz");
				String sql = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_baoz where biaos='1' and baozlx='"+bean.getUabaoz()+"'";
				DBUtilRemove.checkBH(sql, m);
			}
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertCkx_lingjkh", bean);
		} else {
			//"失效日期必须大于生效日期"
			throw new ServiceException(GetMessageByKey.getMessage("sxrqdysx"));
		}

		return "";
	}
	/**
	 * 数据编辑
	 * @author hj
	 * @Date 12/02/21
	 * @param edit
	 * @param userID
	 * @return ""
	 * @throws ServiceException
	 */
	private String edits(Ckx_lingjkh bean, LoginUser user)throws ServiceException {
		if (DateTimeUtil.compare(bean.getShengxrq(), bean.getShixrq())) {
			Map<String,String> map3 = new HashMap<String,String>();
			map3.put("tableName", "ckx_fahzt");
			map3.put("fahztbh", bean.getFahzt());
			map3.put("usercenter", bean.getUsercenter());
			map3.put("biaos", "1");
			//"发货站台表中不存在当前用户中心下发货站台编号为"+bean.getFahzt()+"的数据或该数据已失效";
			String mes3 = GetMessageByKey.getMessage("fhztbczfhbh")+bean.getFahzt()+GetMessageByKey.getMessage("ysxlbcz");
			if(null != bean.getFahzt()){
				DBUtilRemove.checkYN(map3, mes3);
				map3.clear();
			}
			if(null != bean.getUabaoz()){
				//"包装表中不存在包装编号为"+bean.getUabaoz()+"的数据或该数据已失效";
				String m = GetMessageByKey.getMessage("bczbaozhuang")+bean.getUabaoz()+GetMessageByKey.getMessage("ysxlbcz");
				String sql = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_baoz where biaos='1' and baozlx='"+bean.getUabaoz()+"' ";
				DBUtilRemove.checkBH(sql, m);
			}
			if(null != bean.getUcbaoz()){
				//"包装表中不存在包装编号为"+bean.getUcbaoz()+"的数据或该数据已失效";
				String m = GetMessageByKey.getMessage("bczbaozhuang")+bean.getUcbaoz()+GetMessageByKey.getMessage("ysxlbcz");
				String sql = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_baoz where biaos='1' and baozlx='"+bean.getUabaoz()+"'";
				DBUtilRemove.checkBH(sql, m);
			}
			bean.setEditor(user.getUsername());
			bean.setEdit_time(new Date());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateCkx_lingjkh", bean);
		} else {
			//"失效日期必须大于生效日期"
			throw new ServiceException(GetMessageByKey.getMessage("sxrqdysx"));
		}
		return "";
	}
	/**
	 * 数据删除（物理删除）
	 * @author hj
	 * @Date 12/02/21
	 * @param bean
	 * @param userID
	 * @return ""
	 * @throws ServiceException
	 */
	@Transactional
	public String remove(Ckx_lingjkh bean)throws ServiceException{
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteCkx_lingjkh", bean);
		return "success";
	}
	/**
	 * 多记录查询
	 */
	
	@SuppressWarnings("unchecked")
	public List<Ckx_lingjkh> listAll(Ckx_lingjkh bean) throws ServiceException{
		return (List<Ckx_lingjkh>)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.ListALLLingjkh",bean);
	}
}
