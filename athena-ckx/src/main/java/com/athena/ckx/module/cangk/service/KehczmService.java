package com.athena.ckx.module.cangk.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.athena.ckx.entity.cangk.Cangk;
import com.athena.ckx.entity.cangk.Danjdy;
import com.athena.ckx.entity.cangk.Kehczm;
import com.athena.ckx.entity.xuqjs.Fenpq;
import com.athena.ckx.util.DBUtil;
import com.athena.ckx.util.DBUtilRemove;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 客户操作码
 * @author denggq
 * @date 2012-1-30
 * 2015-04-16		gswang		0011297: 客户操作码不能维护*号，导致授权困难。
 */
@Component
public class KehczmService  extends BaseService<Kehczm> {
	
	@Inject
	private DanjdyService danjdyService;
	
	/**
	 * 获得命名空间
	 * @author denggq
	 * @date 2012-1-30
	 */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}
	
	/**
	 * 分页查询方法
	 * @author denggq
	 * @date 2012-2-18
	 * @param bean
	 * @return Map 分页的结果
	 */
	@Transactional
	public Map<String, Object> select(Kehczm bean) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ts_ckx.queryKehczm",bean,bean);
	}
	
	/**
	 * 保存客户操作码
	 * @param bean
	 * @param operant 1-新增  2-修改
	 * @author denggq
	 * @date 2012-2-3
	 */
	@Transactional
	public String save(Kehczm bean,Integer operant,ArrayList<Danjdy> insert,ArrayList<Danjdy> edit,ArrayList<Danjdy> delete,String userId) throws ServiceException{
							   
		bean.setEditor(userId);
		bean.setEdit_time(DateTimeUtil.getAllCurrTime());
		bean.setZhangh(bean.getZhangh().replace(" ", " ").trim());
		if(null != bean.getKehbh()){//bug 0002829   
			//////2015-04-16 gswang 0011297: 客户操作码不能维护*号，导致授权困难。
			String kehbh = bean.getKehbh().trim();
			if(kehbh.indexOf("*")>-1 && !"3".equals( bean.getKehlx())){
				throw new ServiceException(kehbh+"中有*号，客户类型只能是其他客户"); 
			}
			if( "2".equals( bean.getKehlx())){//仓库
				Cangk ck = new Cangk();
				ck.setUsercenter(bean.getUsercenter());
				ck.setCangkbh(bean.getKehbh());
				ck.setBiaos("1");
				String mes = GetMessageByKey.getMessage("kehubianhao")+bean.getKehbh()+GetMessageByKey.getMessage("notexist");
				if(!DBUtil.checkCount(baseDao, "ts_ckx.getCountCangk", ck)){
					throw new ServiceException(mes); 
				}
//				String sql = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_cangk where usercenter = '"+bean.getUsercenter()+"' and cangkbh = '"+bean.getKehbh()+"' and biaos = '1'";
//				DBUtilRemove.checkBH(sql, GetMessageByKey.getMessage("kehubianhao")+bean.getKehbh()+GetMessageByKey.getMessage("notexist"));
			}else if( "1".equals( bean.getKehlx())){//分配区
				Fenpq fq = new Fenpq();
				fq.setUsercenter(bean.getUsercenter());
				fq.setFenpqh(bean.getKehbh());
				fq.setBiaos("1");
				String mes = GetMessageByKey.getMessage("kehubianhao")+bean.getKehbh()+GetMessageByKey.getMessage("notexist");
				if(!DBUtil.checkCount(baseDao, "ts_ckx.getCountFenpq", fq)){
					throw new ServiceException(mes); 
				}
//				String sql = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_fenpq where usercenter = '"+bean.getUsercenter()+"' and fenpqh = '"+bean.getKehbh()+"' and biaos = '1'";
//				DBUtilRemove.checkBH(sql, GetMessageByKey.getMessage("kehubianhao")+bean.getKehbh()+GetMessageByKey.getMessage("notexist"));
			}if( "3".equals( bean.getKehlx())){//其他客户
				if(kehbh.indexOf("*")>-1){
					if(kehbh.length() != 4){
						throw new ServiceException(kehbh+"中有*号，客户编码长度只能是4位长度"); 
					}
					if(kehbh.indexOf("*") != 3){
						throw new ServiceException(kehbh+"中有*号，*号只能出现在客户编码中的第4位"); 
					}
				}
			}
		}
		
		if (1 == operant){
			//客户编号是否存在
			bean.setCreator(userId);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			
			Kehczm kehczm = (Kehczm)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.queryKehczms", bean);
			if(null!=kehczm){
				if(bean.getKehbh().equals(kehczm.getKehbh())){
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteKehczms", bean);
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertKehczm", bean);
				}
			}else{
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertKehczm", bean);
			}
		}else if(2 == operant){
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateKehczm", bean);
		}
		
		danjdyService.save(insert, edit, delete, userId, bean);
		
		return "success";
	} 
	
	/**
	 * 删除客户操作码
	 * @param bean
	 * @author denggq
	 * @date 2012-1-30
	 */
	@SuppressWarnings("rawtypes")
	@Transactional
	public String doDelete(Kehczm bean) throws ServiceException{
		
		Danjdy d=new Danjdy();					//单据打印bean
		d.setUsercenter(bean.getUsercenter());	//用户中心
		d.setKehbh(bean.getKehbh());			//客户编号
		
		List list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryDanjdy", d);
		
		if(0 == list.size()){//此客户是否存在单据打印信息,若不存在则可失效
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteKehczm", bean);
			return bean.getKehbh();
		}
		
		return "";
	}
	
}
