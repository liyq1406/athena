package com.athena.truck.module.kcckx.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.truck.entity.ChacChew;
import com.athena.truck.entity.Chew;
import com.athena.truck.entity.ChewChengys;
import com.athena.truck.entity.Shijdzt;
import com.athena.util.date.DateUtil;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;
/**
 * 车位
 * @author wangliang
 * @date 2015-01-24
 */
@Component
public class ChewService  extends BaseService<Chew>{
	
	@Inject
	private ChewChengysService chewChengysService;
	
	/**
	 * 获得命名空间
	 * @author wangliang
	 * @date 2015-01-24
	 */
	@Override
	protected String getNamespace() {
		return "ts_kac";
	}
	
	
	/**
	 * 获得多个chew
	 * @param bean
	 * @return List
	 * @author wangliang
	 * @date 0215-01-24
	 */

	@SuppressWarnings("rawtypes")
	public List list(Chew bean) throws ServiceException {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_kac.queryChewxx",bean);
	}
	
	
	/**
	 * 保存区域定义
	 * @param bean
	 * @param operant 1-新增  2-修改
	 * @author wangliang
	 * @date 0215-01-24
	 */
	@Transactional
	@SuppressWarnings("unchecked")
	public String save(Chew bean,Integer operant,ArrayList<ChewChengys> insert,ArrayList<ChewChengys> edit,ArrayList<ChewChengys> delete,String userId) throws ServiceException{
		
		bean.setEditor(userId);									//修改人
		bean.setEdit_time(DateUtil.curDateTime());		//修改时间
		if (1 == operant){			//增加
			bean.setCreator(userId);//增加人
			bean.setCreate_time(DateUtil.curDateTime());	//增加时间
			//20170214 BY CSY 取消车位序列号验证
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_kac.insertChew", bean);		//增加数据库
			
		}else if(2 == operant){		//修改
			Shijdzt dazt= new Shijdzt();
			dazt.setUsercenter(bean.getUsercenter());
			dazt.setDaztbh(bean.getDaztbh());
				
			//提交时校验标识是否有效
			List<Shijdzt> blist = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_kac.queryChewDaztbhxx", dazt);	//大站台是否存在
			if(blist.size()!=0){
				//20170214 BY CSY 取消车位序列号验证
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_kac.updateChew", bean);		//修改数据库
			}else{
				if("0".equals(bean.getBiaos())){
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_kac.updateChew", bean);
				}else{
					throw new ServiceException("大站台已失效");
				}
			}	
		}
			
		chewChengysService.save(insert, edit, delete, userId, bean);//chewchengys增删改
		
		return "保存成功";
	} 
	
	
	/**
	 *查询单个车位
	 */

	public Map<String, Object> selectx(Chew bean) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ts_kac.queryChewxx", bean,bean);
	}
	
	/**
	 * 失效chew定义
	 * @param bean
	 * @author wangliang
	 * @date 0215-01-30
	 */
	@SuppressWarnings("rawtypes")
	@Transactional
	public String doDeletebs(Chew bean) throws ServiceException{
		
		ChewChengys cc=new ChewChengys();								//chewchengysbean
		cc.setUsercenter(bean.getUsercenter());				    //用户中心
		cc.setChewbh(bean.getChewbh());							//chew编号
		cc.setDaztbh(bean.getDaztbh());							//大站台编号
		cc.setBiaos("1");
		List list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_kac.queryChewChengys", cc);	//查询chewchengys信息
		if(0 == list.size()){								//此区域下不存在chewchengs信息
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_kac.deleteChew", bean);	//失效chew
			return bean.getChewbh();
		}
		return "";
	
	}
	
	/**
	 * 失效chew定义删除叉车车位关系
	 * @param bean
	 * @author wangliang
	 * @date 0215-01-30
	 */
	@Transactional
	public String doDeletechacw(Chew bean) throws ServiceException{
		
		ChacChew chacc=new ChacChew();								//chewchengysbean
		chacc.setUsercenter(bean.getUsercenter());				    //用户中心
		chacc.setChewbh(bean.getChewbh());							//chew编号
		chacc.setDaztbh(bean.getDaztbh());							//大站台编号
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_kac.deleteChacChew", chacc);	//失效chew
		return "";
	}
	/**
	 * 失效chew定义删除车位承运商关系
	 * @param bean
	 * @author wangliang
	 * @date 0215-01-30
	 */
	@Transactional
	public String doDeletebChewcys(Chew bean) throws ServiceException{
		ChewChengys cc=new ChewChengys();								//chewchengysbean
		cc.setUsercenter(bean.getUsercenter());				    //用户中心
		cc.setChewbh(bean.getChewbh());							//chew编号
		cc.setDaztbh(bean.getDaztbh());							//大站台编号
		cc.setBiaos("1");
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_kac.deleteChewcys", cc);	//失效chew
		return "";
	}


	/**
	 *查询叉车车位
	 */
	
	public Map<String, Object> selectchache(ChacChew bean) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ts_kac.queryChachew", bean,bean);
	}
	
	/**
	 * 判断车位序列是否重复
	 * @param bean
	 * @return
	 */
	public String validatexl(Chew bean) {
		int cw=  (Integer)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_kac.getChewxl",bean);
		if(cw==1){
			throw new ServiceException("序列号已存在！");
		}else{
			return "1";
		}
	}

	@SuppressWarnings("rawtypes")
	public List queryChewDaztbh(Map<String,String> params) throws ServiceException {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_kac.queryChewDaztbh",params);
	}

	
}
