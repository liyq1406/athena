package com.athena.ckx.module.xuqjs.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.athena.ckx.entity.xuqjs.CkxLingjxhd;
import com.athena.ckx.entity.xuqjs.Shengcx;
import com.athena.ckx.entity.xuqjs.Xiaohc;
import com.athena.ckx.entity.xuqjs.Xiaohccx;
import com.athena.ckx.util.DBUtil;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 仓库
 * @author denggq
 * @date 2012-4-10
 */
@Component
public class XiaohcService extends BaseService<Xiaohc> {
	
	@Inject
	private XiaohccxService xiaohccxService;
	
	/**
	 * 获得命名空间
	 * @author denggq
	 * @date 2012-4-10
	 */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}
	/**
	 * 执行层查询页面
	 * @param bean
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, Object> selectZxc(Xiaohc bean) throws ServiceException {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ts_ckx_ck.queryXiaohc",bean,bean);
	}
	
	/**
	 * 执行层保存小火车
	 * @param bean
	 * @param userName
	 * @return
	 */
	@Transactional
	public String saveZxc(Xiaohc bean,String userName){
		bean.setCreator(userName);
		bean.setEditor(userName);
		//修改小火车
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx_ck.updateXiaohc", bean);//修改数据库
		//修改小火车模板控制表
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx_ck.mergeXiaohcmbKz", bean);//修改数据库
	    return "修改成功";
	}
	
	/**
	 * 保存小火车-车厢
	 * @param bean
	 * @param operant 1-新增  2-修改
	 * @author denggq
	 * @date 2012-4-10
	 * @return bean
	 */
	@Transactional
	public String save(Xiaohc bean , Integer operant , ArrayList<Xiaohccx> insert , ArrayList<Xiaohccx> edit , ArrayList<Xiaohccx> delete ,String userId) throws ServiceException{
		
		//生产线编号是否存在
//		String sql = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_shengcx where usercenter = '"+bean.getUsercenter()+"' and shengcxbh = '"+bean.getShengcxbh()+"' and biaos = '1' and flag = '1'";
//		DBUtilRemove.checkBH(sql, GetMessageByKey.getMessage("shengchanxbh")+bean.getShengcxbh()+GetMessageByKey.getMessage("notexist"));
		Shengcx shengcx = new Shengcx();
		shengcx.setUsercenter(bean.getUsercenter());
		shengcx.setShengcxbh(bean.getShengcxbh());
		shengcx.setBiaos("1");
		DBUtil.checkCount(baseDao, "ts_ckx.getCountShengcx", shengcx, GetMessageByKey.getMessage("shengchanxbh")+bean.getShengcxbh()+GetMessageByKey.getMessage("notexist"));

		bean.setEditor(userId);
		bean.setEdit_time(DateTimeUtil.getAllCurrTime());
		
		if (1 == operant){//增加
			bean.setCreator(userId);
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertXiaohc", bean);//增加数据库
		}else if(2 == operant){//修改
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateXiaohc", bean);//修改数据库
		}
		
		xiaohccxService.save(insert, edit, delete, userId, bean);//小火车车厢增删改
		
		return "success";
	}
	
	/**
	 * 小火车失效(准备层)
	 * @author denggq
	 * @param bean
	 * @date 2012-4-10
	 * @return String
	 */
	@SuppressWarnings("rawtypes")
	@Transactional
	public String doDelete(Xiaohc bean) throws ServiceException{
		CkxLingjxhd ckxLingjxhd = new CkxLingjxhd();
		ckxLingjxhd.setUsercenter(bean.getUsercenter());
		//0007779 失效必须加上生产线为过滤条件
		ckxLingjxhd.setShengcxbh(bean.getShengcxbh());
		ckxLingjxhd.setXiaohcbh(bean.getXiaohcbh());
		ckxLingjxhd.setBiaos("1");
		List list1=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryCkxLingjxhd", ckxLingjxhd);//获得小火车下得所有车厢
		if(0 != list1.size()){//小火车是否存在零件消耗点中，若不存在则可以失效
			 return "bunengshixiao";
		}
		
		Xiaohccx z=new Xiaohccx();//小火车车厢
		z.setUsercenter(bean.getUsercenter());//用户中心
		z.setShengcxbh(bean.getShengcxbh());//生产线编号
		z.setXiaohcbh(bean.getXiaohcbh());//小火车编号
		z.setBiaos("1");
		List list2=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryXiaohccx", z);//获得小火车下得所有车厢
		if(0 == list2.size()){//小火车是否存在车厢，若不存在则可以失效
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteXiaohc", bean);//删除小火车
			return "shixiaocg";
		}
		return "bnshixiao";
	}
	/**
	 * 小火车失效(执行层)
	 * @author denggq
	 * @param bean
	 * @date 2012-4-10
	 * @return String
	 */
	@SuppressWarnings("rawtypes")
	@Transactional
	public String doDeleteZXC(Xiaohc bean) throws ServiceException{
		CkxLingjxhd ckxLingjxhd = new CkxLingjxhd();
		ckxLingjxhd.setUsercenter(bean.getUsercenter());
		//0007779 失效必须加上生产线为过滤条件
		ckxLingjxhd.setShengcxbh(bean.getShengcxbh());
		ckxLingjxhd.setXiaohcbh(bean.getXiaohcbh());
		ckxLingjxhd.setBiaos("1");
		List list1=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryCkxLingjxhd", ckxLingjxhd);//获得小火车下得所有车厢
		if(0 != list1.size()){//小火车是否存在零件消耗点中，若不存在则可以失效
			 return "bunengshixiao";
		}
		
		//Xiaohccx z=new Xiaohccx();//小火车车厢
		//z.setUsercenter(bean.getUsercenter());//用户中心
		//z.setShengcxbh(bean.getShengcxbh());//生产线编号
		//z.setXiaohcbh(bean.getXiaohcbh());//小火车编号
		//z.setBiaos("1");
		//List list2=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryXiaohccx", z);//获得小火车下得所有车厢
		//if(0 == list2.size()){//小火车是否存在车厢，若不存在则可以失效
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteXiaohc", bean);//删除小火车
		return "shixiaocg";
		//}
		//return "bnshixiao";
	}

}
