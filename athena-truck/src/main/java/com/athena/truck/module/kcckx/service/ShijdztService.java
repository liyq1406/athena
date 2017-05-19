package com.athena.truck.module.kcckx.service;

import java.util.ArrayList;
import java.util.List;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.truck.entity.Chew;
import com.athena.truck.entity.Dengdqy;
import com.athena.truck.entity.Liucdy;
import com.athena.truck.entity.Shijdzt;
import com.athena.util.date.DateUtil;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 实际大站台
 * @author chenpeng
 * @date 2015-1-7
 */
@Component
public class ShijdztService extends BaseService<Shijdzt>{

	/**
	 * 获得命名空间
	 * @author chenpeng
	 * @date 2015-1-7
	 */
	@Override
	protected String getNamespace() {
		return "ts_kac";
	}
	
	/**
	 * 获取多个实际大站台
	 * @param bean
	 * @return List
	 * @author chenpeng
	 * @date 2015-1-7
	 */
	
	@SuppressWarnings("rawtypes")
	public List list(Shijdzt bean) throws ServiceException {
		
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_kac.queryShijdzt",bean);
	}
	
	
	
	
	/**
	 * 获取单个实际大站台
	 * @param bean
	 * @return List
	 * @author liushuang
	 * @date 2015-1-31
	 */
	@SuppressWarnings("rawtypes")
	public List listShijdztqc(Shijdzt bean) throws ServiceException {
		
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_kac.queryShijdztqc",bean);
	}
	
	/**
	 * 获取大站台编号
	 * @param bean
	 * @return List
	 * @author chenpeng
	 * @date 2015-1-31
	 */
	
	@SuppressWarnings("rawtypes")
	public List listDazt(Shijdzt bean) throws ServiceException {
		
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_kac.queryShijdztbhh",bean);
	}
	
	/**
	 * 批量保存方法
	 * @author chenpeng
	 * @date 2015-1-7
	 * @param insert,edit,delete,userID
	 * @return ""
	 */
	@Transactional
	public String save(ArrayList<Shijdzt> insert,
	           ArrayList<Shijdzt> edit,
	   		   ArrayList<Shijdzt> delete,String userID,Dengdqy dengdqy) throws ServiceException{
		if(0 == insert.size()&&0 == edit.size()&&0 == delete.size()){//无操作
			return "null";
		}
		inserts(insert,userID,dengdqy);//增加
		edits(edit,userID,dengdqy);//修改
		deleteDzt(delete, userID, dengdqy);//删除
		return "保存成功";
	}
	
	/**
	 * 私有批量insert方法
	 * @author chenpeng
	 * @date 2015-1-7
	 * @param insert,userID
	 * @return  ""
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public String inserts(List<Shijdzt> insert,String userID,Dengdqy dengdqy)throws ServiceException{
		//查询默认流程返回list
		List<Liucdy> liuclist =baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_kac.morenLiuc", dengdqy) ;
		for(Shijdzt bean:insert){
			bean.setUsercenter(dengdqy.getUsercenter());//用户中心
			bean.setQuybh(dengdqy.getQuybh());          //区域编号
			bean.setCreator(userID);
			bean.setCreate_time(DateUtil.curDateTime());
			bean.setEditor(userID);
			bean.setEdit_time(DateUtil.curDateTime());
		
			if(bean.getPaidtqqsx()>=bean.getPaidtqqxx()){
				throw new ServiceException("排队提前期上限必须小于提前期下限！");
			}else{
			
			if(null!= bean.getDuiycmqy()){
				
				List<Dengdqy> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_kac.queryQuybh", dengdqy);	//查询同一个用户中心下的区域编号
				List<String> strList = new ArrayList<String>();
				for (Dengdqy d : list) 
				{
					strList.add(d.getQuybh());
				}
				String[] arr = bean.getDuiycmqy().split(",");
				boolean flag = true;
				for (int i = 0; i < arr.length; i++) {
					if(!strList.contains(arr[i]))
					{
						flag = false;
						break;
					}
				}
				if(flag){
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_kac.insertShijdzt",bean);//增加数据库
					List<Liucdy> daztlist = getLiucdyOfMor(liuclist, bean);
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch("ts_kac.insertLiucdys", daztlist);  //将默认流程插入流程定义表中
				}else{
					throw new ServiceException("出厂编号"+bean.getDuiycmqy()+"在区域编号中不存在！添加多个出厂编号时只能以逗号分隔！");
				}
			}else{
				   baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_kac.insertShijdzt",bean);//增加数据库
					List<Liucdy> daztlist = getLiucdyOfMor(liuclist, bean);
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch("ts_kac.insertLiucdys", daztlist);  //将默认流程插入流程定义表中
			}
		  }	
		}
		return "";
	}
	
	
	/**
	 * 获得默认流程放到一个list集合
	 */
	public List<Liucdy> getLiucdyOfMor(List<Liucdy> list,Shijdzt bean){
		List<Liucdy>  lcList  = new ArrayList<Liucdy>();
		for(Liucdy liucdyBean : list){
			Liucdy lcBean = new Liucdy();
			lcBean.setLiucbh(liucdyBean.getLiucbh());
			lcBean.setLiucmc(liucdyBean.getLiucmc());
			lcBean.setUsercenter(bean.getUsercenter());
			lcBean.setQuybh(bean.getQuybh());
			lcBean.setDaztbh(bean.getDaztbh());
			lcBean.setBiaos(bean.getBiaos());
			
			lcBean.setLeix(liucdyBean.getLeix());
			lcBean.setBiaozsj(liucdyBean.getBiaozsj());
			lcBean.setLiucbs(liucdyBean.getLiucbs());
			
			lcBean.setCreator(bean.getCreator());
			lcBean.setEditor(bean.getEditor());
			lcList.add(lcBean);
		}
		return lcList;
	}
	
	
	
	/**
	 * 私有批量update方法
	 * @author chenpeng
	 * @date 2015-1-7
	 * @param edit,userID
	 * @return ""
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional
	public String edits(List<Shijdzt> edit,String userID,Dengdqy dengdqy) throws ServiceException{
		for(Shijdzt bean:edit){
			bean.setUsercenter(dengdqy.getUsercenter());//用户中心	
			bean.setQuybh(dengdqy.getQuybh());          //区域编号
			bean.setEditor(userID);
			bean.setEdit_time(DateUtil.curDateTime());
			
			if(bean.getPaidtqqsx()>=bean.getPaidtqqxx()){
				throw new ServiceException("排队提前期上限必须小于提前期下限！");
			}else{
			
			if(("0".equals(bean.getBiaos())&& ("1".equals(bean.getYuanbiaos())))){
				Chew cw=new Chew();								//车位bean
				cw.setUsercenter(bean.getUsercenter());				    //用户中心
				cw.setDaztbh(bean.getDaztbh());							//大站台编号
				cw.setBiaos("1");							//标识
				List list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_kac.queryChewccccx", cw);	//查询此大站台下的车位信息
				
				if(0 != list.size()){	
					throw new ServiceException("大站台下面有车位，不能失效！");
				}else{
					//此大站台下不存在车位信息
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_kac.deleteShijdzt", bean);	//失效大站台
				}
			}
			
			if(null!= bean.getDuiycmqy()){
				
				List<Dengdqy> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_kac.queryQuybh", dengdqy);	//查询同一个用户中心下的区域编号
				List<String> strList = new ArrayList<String>();
				for (Dengdqy d : list) 
				{
					strList.add(d.getQuybh());
				}
				String[] arr = bean.getDuiycmqy().split(",");
				boolean flag = true;
				for (int i = 0; i < arr.length; i++) {
					if(!strList.contains(arr[i]))
					{
						flag = false;
						break;
					}
				}
				if(flag){
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_kac.updateShijdzt",bean);//更新数据库
				}else{
					throw new ServiceException("出厂编号"+bean.getDuiycmqy()+"在区域编号中不存在！添加多个出厂编号时只能以逗号分隔！");
				}
			}else{
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_kac.updateShijdzt",bean);//更新数据库
			  }
		   }
		}
		return "success";
	}
	/**
	 * 私有批量删除方法
	 * @author chenpeng
	 * @date 2015-1-7
	 * @param delete,userID
	 * @return ""
	 */
	@Transactional
	public String deletes(List<Shijdzt> delete,String userID,Dengdqy dengdqy)throws ServiceException{
		for(Shijdzt bean:delete){
			bean.setUsercenter(dengdqy.getUsercenter());//用户中心
			bean.setQuybh(dengdqy.getQuybh());          //区域编号   
			bean.setEditor(userID);
			bean.setEdit_time(DateUtil.curDateTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_kac.deleteShijdzt",bean);//失效数据库
		}
		return "";
	}
	
	/**
	 * 删除大站台 by CSY 20170117
	 * @param delete
	 * @param userID
	 * @param dengdqy
	 * @return
	 * @throws ServiceException
	 */
	@Transactional
	public String deleteDzt(List<Shijdzt> delete,String userID,Dengdqy dengdqy)throws ServiceException{
		for(Shijdzt bean:delete){
			bean.setUsercenter(dengdqy.getUsercenter());//用户中心
			bean.setQuybh(dengdqy.getQuybh());          //区域编号   
			bean.setEditor(userID);
			bean.setEdit_time(DateUtil.curDateTime());
			String err = this.delCheck(bean);
			if (err.equals("") || err == null) {
				//删除大站台
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_kac.deleteDazt",bean);
				//删除流程定义
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_kac.deleteDztLiucdy", bean);
			}else {
				throw new ServiceException(err);
			}
		}
		return "";
	}
	
	/**
	 * 删除验证by CSY 20170117
	 * @param bean
	 * @return
	 */
	public String delCheck(Shijdzt bean){
		StringBuffer res = new StringBuffer("");
		String err = "";
		//验证车位
		if ((Integer)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_kac.countDztChew", bean) != 0) {
			res.append("存在有效车位，");
		}
		//验证卸货站台
		if ((Integer)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_kac.countDztXiehzt", bean) != 0) {
			res.append("存在有效卸货站台，");
		}
		//验证运单
		if ((Integer)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_kac.countDztYund", bean) != 0) {
			res.append("存在未处理运单，");
		}
		if (!res.toString().equals("")) {
			err = "用户中心" + bean.getUsercenter() + "大站台" + bean.getDaztbh() + res.toString() + "不可删除！";
		}
		return err;
	}

}
