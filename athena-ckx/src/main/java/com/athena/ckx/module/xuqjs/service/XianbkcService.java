package com.athena.ckx.module.xuqjs.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.athena.ckx.entity.cangk.Cangk;
import com.athena.ckx.entity.xuqjs.Gongysxhc;
import com.athena.ckx.entity.xuqjs.Shengcx;
import com.athena.ckx.entity.xuqjs.Xianbkc;
import com.athena.ckx.entity.xuqjs.Usercenter;
import com.athena.ckx.entity.xuqjs.Xianbkc;
import com.athena.ckx.entity.xuqjs.Yingyxgjl;
import com.athena.ckx.util.DBUtil;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

@Component
public class XianbkcService extends BaseService<Xianbkc> {
	static Logger log = Logger.getLogger(XianbkcService.class);
	/**
	 * 获得命名空间
	 * @author zbb
	 * @date 2015-12-12
	 */
	@Override
	protected String getNamespace() {
		return "Xianbkc";
	}
	
	/**
	 * 分页查询线边库存页面
	 * @param bean
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, Object> selectXianbkc(Xianbkc bean) throws ServiceException {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ckxXianbkc.queryXianbkcByParam",bean,bean);
	}
	
	/**
	 * 保存线边库存
	 * @param insert
	 * @param edit
	 * @param delete
	 * @author zbb
	 * @date 2015-12-12
	 * @return String
	 */
	@Transactional
	public String save(ArrayList<Xianbkc> insert , ArrayList<Xianbkc> edit , ArrayList<Xianbkc> delete ,String userId) throws ServiceException{
		if(0 == insert.size()&&0 == edit.size()&&0 == delete.size()){
			return "null";
		}
		String result = checkData(insert,edit);
		if(result!=null&&!"".equals(result)){
			return result;
		}
		log.info("参考系-线边库存-失效数据");
		//deletes(delete,userId);
		log.info("参考系-线边库存-增加数据");
		//inserts(insert,userId);
		log.info("参考系-线边库存-修改数据");
		edits(edit,userId);
		log.info("参考系-刷新用户中心缓存");
				
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
	public String inserts(List<Xianbkc> insert,String username)throws ServiceException{
		for(Xianbkc bean:insert){
			bean.setCreator(username);	
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(username);		
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ckxXianbkc.insertXianbkc",bean);
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
	public String edits(List<Xianbkc> edit,String username) throws ServiceException{
		//盈余修改记录
		Yingyxgjl yingyxgjl = new Yingyxgjl();
		Xianbkc oxianbkc = null;//原来的线边库存		
		for(Xianbkc bean:edit){
			oxianbkc = (Xianbkc) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ckxXianbkc.queryXianbkcByParam",bean);
			yingyxgjl.setUsercenter(bean.getUsercenter());
			yingyxgjl.setLingjbh(bean.getLingjbh());
			yingyxgjl.setMudd(bean.getMudd());
			yingyxgjl.setOyingy(oxianbkc.getYingy());
			yingyxgjl.setNyingy(bean.getYingy());
			yingyxgjl.setEditor(username);
			yingyxgjl.setEdit_time(DateTimeUtil.getAllCurrTime());
			//插入盈余修改记录
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ckxXianbkc.insertYingyxgjl",yingyxgjl);
			
			bean.setEditor(username);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ckxXianbkc.updateXianbkcByParam",bean);
			
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
	public String deletes(List<Xianbkc> delete,String username)throws ServiceException{
		for(Xianbkc bean:delete){
			bean.setEditor(username);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ckxXianbkc.deleteXianbkc",bean);
		}
		return "";
	}
	
	
	private String checkData(ArrayList<Xianbkc> insert , ArrayList<Xianbkc> edit){
		String result = null;			
		List<Cangk> cangklist=new ArrayList<Cangk>();
		List<Shengcx> shengcxlist=new ArrayList<Shengcx>();		
		Map<String,String> param = new HashMap<String,String>();	
		//用户中心零件是否存在
		for(Xianbkc bean:insert){	
			param.put("cangkbh", bean.getMudd());
			param.put("usercenter", bean.getUsercenter());
			param.put("lingjbh", bean.getLingjbh());
			param.put("shengcxbh", bean.getChanx());
			
			//用户中心产线
			shengcxlist = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryShengcxsByParam",param);
			if(shengcxlist.size()<1){
				result= "yonghzxcxbcz";//用户中心产线不存在
				return result;
			}	
			
			//用户中心仓库	
			if(bean.getLeix().equals("1")){				
				cangklist = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.getCangk",param);
				if(cangklist.size()<1){
					result= "yonghzxckbcz";//用户中心仓库不存在
					return result;
				}		
			}
			param.clear();
		}
		
		for(Xianbkc bean:edit){	
			param.put("cangkbh", bean.getMudd());
			param.put("usercenter", bean.getUsercenter());
			param.put("lingjbh", bean.getLingjbh());
			param.put("shengcxbh", bean.getChanx());
			//用户中心产线
			shengcxlist = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryShengcxsByParam",param);
			if(shengcxlist.size()<1){
				result= "yonghzxcxbcz";//用户中心产线不存在
				return result;
			}	
			
			//用户中心仓库
			if(bean.getLeix().equals("1")){				
				cangklist = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.getCangk",param);
				if(cangklist.size()<1){
					result= "yonghzxckbcz";//用户中心仓库不存在
					return result;
				}		
			}
			param.clear();
		}
		return result;
	}
}
