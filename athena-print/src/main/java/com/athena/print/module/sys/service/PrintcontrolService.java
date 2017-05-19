package com.athena.print.module.sys.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.athena.ckx.entity.xuqjs.Usercenter;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.print.entity.sys.Cangk;
import com.athena.print.entity.sys.Ckbatctrl;
import com.athena.print.entity.sys.Plttrscodej;
import com.athena.print.entity.sys.PrintControl;
import com.athena.print.entity.sys.PrintStrogdict;
import com.athena.util.cache.CacheManager;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;


/**
 * 用户中心
 * @author denggq
 * 2012-3-19
 */
@Component
public class PrintcontrolService extends BaseService<PrintControl>{
	
	static Logger log = Logger.getLogger(PrintcontrolService.class);
	
	@Inject
	private CacheManager cacheManager;//缓存

	@Override
	public String getNamespace() {
		return "sys";
	}

	
	/**
	 * 保存包装
	 * @param list
	 * @param username
	 * @return String
	 * @author denggq
	 * @time 2012-3-6
	 */
	@Transactional
	public String save(ArrayList<PrintControl> insert,ArrayList<PrintControl> edit, ArrayList<PrintControl> delete,String username) throws ServiceException{
		if(0 == insert.size()&&0 == edit.size()&&0 == delete.size()){
			return "null";
		}
		log.info("参考系-用户中心-失效数据");
		deletes(delete,username);
		log.info("参考系-打印单据控制-增加数据");
		inserts(insert,username);
		//log.info("参考系-打印单据控制-修改数据");
		//edits(edit,username);
		log.info("参考系-刷新用户中心缓存");
		cacheManager.refreshCache("queryUserCenterMap");//刷新缓存
		return "success";
	}
	
	
	/**
	 * 私有批量增加方法
	 * @author denggq
	 * @date 2012-1-16
	 * @param list,username
	 * @return  ""
	 */
	@Transactional
	public String inserts(List<PrintControl> insert,String username)throws ServiceException{
		for(PrintControl bean:insert){
			bean.setCreator(username);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(username);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			//校验仓库编号
			String cangkbh = bean.getCangkbh();
			String usercenter = bean.getUsercenter();
			Map mapk = new HashMap();
			mapk.put("usercenter", usercenter);
			mapk.put("cangkbh", cangkbh);
			mapk.put("biaos", "1");
			Cangk cangk = (Cangk)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).selectObject("sys.queryCangks",mapk);
			 if(null==cangk){
					throw new ServiceException("仓库编号不存在");
			 }
			//校验单据编号
			String danjbh = bean.getDanjbh();
			String usercenter1 = bean.getUsercenter();
			Map maps = new HashMap();
			maps.put("usercenter", usercenter1);
			maps.put("cangkbh", cangkbh);
			maps.put("biaos", "1");
			maps.put("zidbm", danjbh);
			PrintStrogdict printStrogdict = (PrintStrogdict)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).selectObject("sys.queryPrintStrogdictos",maps);
			if(null==printStrogdict){
				throw new ServiceException("单据编号不存在");
			}
			//校验交易码
			String jiaoym = bean.getJiaoym();
			String usercenter2 = bean.getUsercenter();
			if(!"".equals(jiaoym)&&null!=jiaoym){
				if(jiaoym.substring(0, 1).equals("0")||jiaoym.substring(0, 1).equals("C")||jiaoym.substring(0, 1).equals("c")){
					Ckbatctrl beans = new Ckbatctrl();
					beans.setBatcode(jiaoym);
					Ckbatctrl ckbatctrl =(Ckbatctrl)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).selectObject("sys.queryCk_batctrl",beans);
					if(ckbatctrl==null){
						throw new ServiceException("交易码或批量号不存在");
					}
				}else{
					Map map = new HashMap();
					map.put("usercenter", usercenter2);
					map.put("trscode", jiaoym);
					Plttrscodej plttrscodej = (Plttrscodej) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PRINT).selectObject("sys.queryPlt_trscode_j",map);
					if(plttrscodej==null){
						throw new ServiceException("交易码或批量号不存在");
					}
				}
			}
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("sys.insertPrintcontrol",bean);
		}
		return "";
	}
	
	/**
	 * 私有批量修改方法
	 * @author denggq
	 * @date 2012-1-16
	 * @param list,username
	 * @return ""
	 */
	@Transactional
	public String edits(List<PrintControl> edit,String username) throws ServiceException{
		for(PrintControl bean:edit){
			bean.setEditor(username);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("sys.updatePrintcontrol",bean);
		}
		return "";
	}
	
	/**
	 * 私有批量删除方法
	 * @author denggq
	 * @date 2012-3-19
	 * @param list,username
	 * @return ""
	 */
	@Transactional
	public String deletes(List<PrintControl> delete,String username)throws ServiceException{
		for(PrintControl bean:delete){
			bean.setEditor(username);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("sys.deletePrintcontrols",bean);
		}
		return "";
	}
	
	
	/**
	 * 失效
	 * @param bean
	 * @return 主键
	 * @author denggq
	 * @time 2012-3-19
	 */
	public String doDelete(PrintControl bean) throws ServiceException{
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("sys.deletePrintcontrol", bean);
		return "success";
	}
	
}
