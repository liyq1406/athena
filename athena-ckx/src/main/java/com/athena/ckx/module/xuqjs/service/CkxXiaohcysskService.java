
package com.athena.ckx.module.xuqjs.service;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.ckx.entity.xuqjs.CkxXiaohcyssk;
import com.athena.ckx.entity.xuqjs.Shengcx;
import com.athena.ckx.entity.xuqjs.Ticxxsj;
import com.athena.ckx.entity.xuqjs.Xiaohc;
import com.athena.ckx.entity.xuqjs.Xiaohcmb;
import com.athena.ckx.entity.xuqjs.Yicbj;
import com.athena.ckx.util.DBUtil;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.ckx.util.GetYicbj;
import com.athena.component.runner.Runner;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * @description小火车运输时刻模板
 * @author denggq
 * @date 2012-4-12
 */
@Component
public class CkxXiaohcysskService extends BaseService<CkxXiaohcyssk> {
	
	/**
	 * @description获得命名空间
	 * @author denggq
	 * @date 2012-4-12
	 */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}

	
	/**
	 * @description 保存小火车-车厢
	 * @param bean
	 * @author denggq
	 * @date 2012-4-12
	 * @return bean
	 */
	@Transactional
	public String save(ArrayList<CkxXiaohcyssk> insert , ArrayList<CkxXiaohcyssk> edit , ArrayList<CkxXiaohcyssk> delete ,String userId) throws ServiceException{
		
		inserts(insert,userId);//增加
		edits(edit,userId);//修改
		deletes(delete,userId);//删除
		
		return "success";
		
	}
	
	/**
	 * @description 私有批量insert方法
	 * @author denggq
	 * @date 2012-4-12
	 * @param insert,userId
	 * @return  ""
	 */
	@Transactional
	public String inserts(List<CkxXiaohcyssk> insert,String userId)throws ServiceException{
		for(CkxXiaohcyssk bean:insert){
			
			//生产线编号是否存在
//			String sql1= "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_shengcx where usercenter = '"+bean.getUsercenter()+"' and shengcxbh = '"+bean.getShengcxbh()+"' and biaos = '1' and flag = '1'";
//			DBUtilRemove.checkBH(sql1,GetMessageByKey.getMessage("shengchanxbh")+bean.getShengcxbh()+GetMessageByKey.getMessage("notexist"));
			Shengcx shengcx = new Shengcx();
			shengcx.setUsercenter(bean.getUsercenter());
			shengcx.setShengcxbh(bean.getShengcxbh());
			shengcx.setBiaos("1");
			DBUtil.checkCount(baseDao, "ts_ckx.getCountShengcx", shengcx, GetMessageByKey.getMessage("shengchanxbh")+bean.getShengcxbh()+GetMessageByKey.getMessage("notexist"));
			//小火车编号是否存在
//			String sql2 = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_xiaohc where usercenter = '"+bean.getUsercenter()+"' and xiaohcbh = '"+bean.getXiaohcbh()+"' and shengcxbh = '"+bean.getShengcxbh()+"' and biaos = '1'";
//			DBUtilRemove.checkBH(sql2, GetMessageByKey.getMessage("xiaohcbh")+bean.getXiaohcbh()+GetMessageByKey.getMessage("notexist"));
			Xiaohc xiaohc = new Xiaohc();
			xiaohc.setUsercenter(bean.getUsercenter());
			xiaohc.setXiaohcbh(bean.getXiaohcbh());
			xiaohc.setShengcxbh(bean.getShengcxbh());
			xiaohc.setBiaos("1");
			DBUtil.checkCount(baseDao, "ts_ckx.getCountXiaohc", xiaohc, GetMessageByKey.getMessage("xiaohcbh")+bean.getXiaohcbh()+GetMessageByKey.getMessage("notexist"));
			bean.setCreator(userId);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(userId);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertCkxXiaohcyssk",bean);//增加数据库
		}
		return "";
	}
	
	
	/**
	 * @description 私有批量update方法
	 * @author denggq
	 * @date 2012-4-12
	 * @param edit,userId
	 * @return ""
	 */
	@Transactional
	public String edits(List<CkxXiaohcyssk> edit,String userId) throws ServiceException{
		for(CkxXiaohcyssk bean:edit){
			bean.setEditor(userId);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateCkxXiaohcyssk",bean);//修改数据库
		}
		return "";
	}
	
	
	/**
	 * @description 私有批量删除方法
	 * @author denggq
	 * @date 2012-4-12
	 * @param delete,userId
	 * @return ""
	 */
	@Transactional
	public String deletes(List<CkxXiaohcyssk> delete,String userId) throws ServiceException{
		for(CkxXiaohcyssk bean:delete){
			bean.setEditor(userId);
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteCkxXiaohcyssk",bean);//删除数据库
		}
		return "";
	}
	
	
	/**
	 * @description 计算小火车运输时刻
	 * @author denggq
	 * @param bean
	 * @date 2012-4-20
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public String calculateXiaohcYssk(String userId) throws ServiceException{
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.truncateCkxXiaohcyssk");//清空小火车运输表数据
		List<Xiaohcmb> distinctXiaohc = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.DistinctXiaohc");//获得模板数据,按用户中心,生产线,小火车归集模板
		if(0 == distinctXiaohc.size()){
			throw new ServiceException(GetMessageByKey.getMessage("notdata"));
		}
//		Runner runner=new Runner(distinctXiaohc.size());//线程类
		Map<String,String> map = new HashMap<String, String>();
		for (Xiaohcmb xiaohcmb : distinctXiaohc) {
			
			List<Xiaohcmb> mbList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryXiaohcmb",xiaohcmb);//获得模板数据,按用户中心,生产线,小火车归集
			
			Ticxxsj t1 = new Ticxxsj();//剔除休息时间实体类
			t1.setUsercenter(xiaohcmb.getUsercenter());//用户中心
			t1.setChanxck(xiaohcmb.getShengcxbh());//生产线编号
			t1.setGongzr(DateTimeUtil.getCurrDate());//当期日期
			
			List<Ticxxsj> gongzrList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryGongzr", t1);//按照用户中心+生产线+工作日获得七天的工作日
			if(0 == gongzrList.size()){
				map.put(xiaohcmb.getUsercenter()+","+xiaohcmb.getShengcxbh(), xiaohcmb.getUsercenter());
				continue;
				//throw new ServiceException(GetMessageByKey.getMessage("time")+xiaohcmb.getShengcxbh()+GetMessageByKey.getMessage("data"));
			}
			
//			runner.addCommand(new XiaohcYunsskTread(super.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX),t1,mbList,gongzrList,userId));//启动多线程
			new XiaohcYunsskTread(super.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX),t1,mbList,gongzrList,userId).execute();//启动单线程
		}
		//记录异常报警
		List<Yicbj> listYicbc = new ArrayList<Yicbj>();
		for(String usercenterChanx:map.keySet()){
			String key[] = usercenterChanx.split(",");
			String exceptionMes = "小火车运输时刻：用户中心：" +key[0]+",产线：" +key[1] +"没有对应的工作日历";
			listYicbc.add(new GetYicbj().getYicbj(map.get(usercenterChanx), "200", "CKX_20",null, exceptionMes,"POA"));
		}
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("ycbj.insertYcbjCKX", listYicbc);
		return "success";
	}
}
