package com.athena.truck.module.yundgz.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.truck.entity.Yund;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.date.DateUtil;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;

@Component
public class YundgzService extends BaseService<Yund> {

	@Inject
	private UserOperLog userOperLog;
	
	@SuppressWarnings("rawtypes")
	public List queryYundgzDengdqy(Map<String,String> params) throws ServiceException {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("kac_yundgz.queryYundgzDengdqy",params);
	}

	@SuppressWarnings("rawtypes")
	public List queryYundgzDazt(Map<String,String> params) throws ServiceException {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("kac_yundgz.queryYundgzDazt",params);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Object> queryYundgzZhuangt(Map<String,String> params) throws ServiceException {
		return (List<Object>)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("kac_yundgz.queryYundgzZhuangt",params);
	}
	
	public Map<String, Object> queryYundgz(Yund bean,Map<String,String> params) throws ServiceException {
		return  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("kac_yundgz.queryYundgz",params,bean);
	}
	
	public Map<String, Object> queryYundgzmx(Yund bean,Map<String,String> params) throws ServiceException {
		return  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("kac_yundgz.queryYundgzmx",params,bean);
	}
	
	@Transactional
	public String yunDtiqpd(ArrayList<Yund> list,Map<String,String> params) throws ServiceException {
		String re = "";
		if(params.get("type") != null && "T".equals(params.get("type"))){
			for( int i = list.size()-1;i>=0;i--){
				Yund bean = list.get(i);
				Yund yd = (Yund)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("kac_yundgz.queryYundCheck",bean);
				if(yd==null ){
					list.remove(i);
					continue;
				}else{
					if(!"1".equals(yd.getZhuangt())){
						throw new ServiceException("运单"+bean.getYundh()+"状态为"+yd.getZhuangt()+" "+yd.getZhuangtmc()+"，不可以设置为提前排队");
					}else if("1".equals(yd.getJijbs())){
						throw new ServiceException("运单"+bean.getYundh()+"为急件，不可以设置为提前排队");
					}else if("1".equals(yd.getTiqpdbs()) || "2".equals(yd.getTiqpdbs())){
						list.remove(i);
						continue;
					}
				}
				bean.setEditTime(DateUtil.curDateTime());
				bean.setEditor(params.get("username"));
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("kac_yundgz.updateYundTiqpd",bean);
				
				try{
					yd.setBeiz1(params.get("username"));
					yd.setBeiz2(DateUtil.curDateTime());
					yd.setCreateTime(DateUtil.curDateTime());
					yd.setCreator(params.get("username"));
					yd.setBeiz3("1");
					yd.setTiqpdbs("1");
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("kac_yundgz.insertChurcls",yd);
				}catch(RuntimeException e){
					userOperLog.addError(CommonUtil.MODULE_CKX, "运单急件", "运单急件", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
					throw new ServiceException("运单"+bean.getYundh()+"操作流水记录失败！");
				}
			}

		}else if(params.get("type") != null && "J".equals(params.get("type"))){
			for( int i = list.size()-1;i>=0;i--){
				Yund bean = list.get(i);
				Yund yd = (Yund)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("kac_yundgz.queryYundCheck",bean);
				if(yd==null ){
					list.remove(i);
					continue;
				}else{
					if(!("1".equals(yd.getZhuangt()) || "2".equals(yd.getZhuangt()))){
						throw new ServiceException("运单"+bean.getYundh()+"状态为"+yd.getZhuangt()+" "+yd.getZhuangtmc()+"，不可以设置为急件");
					}else if("1".equals(yd.getTiqpdbs())|| "2".equals(yd.getTiqpdbs())){
					//	throw new ServiceException("运单"+bean.getYundh()+"为提前排队，不可以设置为急件");
					}else if("1".equals(yd.getJijbs())){
						list.remove(i);
						continue;
					}
				}
				bean.setEditTime(DateUtil.curDateTime());
				bean.setEditor(params.get("username"));
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("kac_yundgz.updateYundJijqr",bean);
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("kac_yundgz.updateChelpdBs",bean);
				try{
					yd.setBeiz1(params.get("username"));
					yd.setBeiz2(DateUtil.curDateTime());
					yd.setBeiz3("1");
					yd.setCreateTime(DateUtil.curDateTime());
					yd.setCreator(params.get("username"));
					yd.setJijbs("1");
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("kac_yundgz.insertChurcls",yd);
				}catch(RuntimeException e){
					userOperLog.addError(CommonUtil.MODULE_CKX, "运单排队", "运单排队", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
					throw new ServiceException("运单"+bean.getYundh()+"操作流水记录失败！");
				}
			}
		}
		return "success";
	}
	
	
	@Transactional
	public String yundDelete(ArrayList<Yund> list,Map<String,String> params) throws ServiceException {
		for( int i = list.size()-1;i>=0;i--){
			Yund bean = list.get(i);
			bean.setEditTime(DateUtil.curDateTime());
			bean.setEditor(params.get("username"));
			Yund yd = (Yund)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("kac_yundgz.queryYundCheck",bean);

			if(yd==null ){
				list.remove(i);
			}else{
				if("1".equals(yd.getZhuangt())||"2".equals(yd.getZhuangt())){
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("kac_yundgz.deleteChelpd",bean);
					//baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("kac_yundgz.deleteYundmx",bean);
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("kac_yundgz.deleteYund",bean);
				}else{
					throw new ServiceException("运单"+bean.getYundh()+"状态为"+yd.getZhuangt()+" "+yd.getZhuangtmc()+"，不可以删除!");
				}
				
				try{
					yd.setBeiz1(params.get("username"));
					yd.setBeiz2(DateUtil.curDateTime());
					yd.setBeiz3("D");
					yd.setZhuangt("80");
					yd.setCreateTime(DateUtil.curDateTime());
					yd.setCreator(params.get("username"));
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("kac_yundgz.insertChurcls",yd);
				}catch(RuntimeException e){
					userOperLog.addError(CommonUtil.MODULE_CKX, "运单删除", "运单删除", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
					throw new ServiceException("运单"+bean.getYundh()+"操作流水记录失败！");
				}
			}
		}
		return "success";
	}
	
	@Transactional
	public String yundRuc(ArrayList<Yund> list,Map<String,String> params) throws ServiceException {
		for( int i = list.size()-1;i>=0;i--){
			Yund bean = list.get(i);
			bean.setEditor(params.get("username"));
			Yund yd = (Yund)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("kac_yundgz.queryYundCheck",bean);

			if(yd==null ){
				list.remove(i);
			}else{
				if("3".equals(yd.getZhuangt()) && (yd.getZhunrsj()).equals(yd.getRucsj())){
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("kac_yundgz.updateYundrc",bean);
				}else{
					throw new ServiceException("运单"+bean.getYundh()+"不可以进行入厂操作!");
				}
				try{
					yd.setBeiz1(params.get("username"));
					yd.setCreator(params.get("username"));
					yd.setBeiz3("R");
					yd.setZhuangt("98");
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("kac_yundgz.insertChurcls",yd);
				}catch(RuntimeException e){
					userOperLog.addError(CommonUtil.MODULE_CKX, "卡车入厂", "卡车入厂", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
					throw new ServiceException("运单"+bean.getYundh()+"操作流水记录失败！");
				}
			}
		}
		return "success";
	}

}
