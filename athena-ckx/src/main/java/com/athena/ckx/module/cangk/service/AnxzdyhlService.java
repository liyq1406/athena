package com.athena.ckx.module.cangk.service;
//11976
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.cangk.Anxyhlbbcssz;
import com.athena.ckx.entity.cangk.Anxzdyhl;
import com.athena.ckx.entity.xuqjs.Fanhjzs;
import com.athena.ckx.module.xuqjs.service.FanhjzsService;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.Pageable;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

@Component
public class AnxzdyhlService extends BaseService<Anxzdyhl> {

	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}
	
	static Logger log = Logger.getLogger(FanhjzsService.class);
	
	public LoginUser getLoginUser() {
		return AuthorityUtils.getSecurityUser();
	}
	
	@SuppressWarnings("unchecked")
	public  Map<String, Object> queryZdyhl(Anxzdyhl bean) {
		// TODO Auto-generated method stub
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ts_ckx.queryZuidyhl", bean,bean.getCurrentPage(),bean.getPageSize());
	}
	
	@Transactional
	public String save(ArrayList<Anxzdyhl> insert,
	           ArrayList<Anxzdyhl> edit,
	   		   ArrayList<Anxzdyhl> delete) throws ServiceException{
		if(0 == insert.size()&&0 == edit.size()&&0 == delete.size()){
			return "null";
		}
		inserts(insert);
		edits(edit);
		deletes(delete);
		return "success";
	}
	
	@Transactional
	public String inserts(List<Anxzdyhl> insert)throws ServiceException{
		for(Anxzdyhl bean:insert){		
			bean.setCreator(getLoginUser().getUsername());
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(getLoginUser().getUsername());
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());	
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insert_anxzdyhl",bean);
		}
		return "success";
	}
	
	@Transactional
	public String edits(List<Anxzdyhl> edit) throws ServiceException{
		for(Anxzdyhl bean:edit){			
			bean.setCreator(getLoginUser().getUsername());
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			bean.setEditor(getLoginUser().getUsername());
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());	
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.update_anxzdyhl",bean);
		}
		return "success";
	}
	
	@Transactional
	public String deletes(List<Anxzdyhl> delete)throws ServiceException{
		for(Anxzdyhl bean:delete){
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.delete_anxzdyhl",bean);
		}
		return "success";
	}
	//huxy_V4_041
	public Map<String, Object>  queryParam(Pageable page,Map<String,String> map) {
		// TODO Auto-generated method stub
		
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ts_ckx.query_yhlsz",map,page);
	}
	

	@SuppressWarnings("unchecked")
	@Transactional
	public String deleteSz(Anxyhlbbcssz bean) {
		// TODO Auto-generated method stub
		String errorMessage = "success";
		String s = new String();
		if(null == bean.getUsercenter() || null ==bean.getGongc() || null == bean.getChanx()){
				s = "用户中心:"+bean.getUsercenter()+"工厂:"+bean.getGongc()+"产线:"+bean.getChanx()+"至少有一缺失，删除失败";
				return s;
		}
		
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.delete_anxyhlbbcssz",bean);
		return errorMessage;
	}
	
	@Transactional
	public String editSz(Anxyhlbbcssz bean) {
		// TODO Auto-generated method stub
		Map<String,String> map = new HashMap<String,String>();
		String errorMessage = "success";
		String s = new String();
		if(null == bean.getUsercenter() || null ==bean.getGongc() || null == bean.getChanx()){
				s = "用户中心:"+bean.getUsercenter()+"工厂:"+bean.getGongc()+"产线:"+bean.getChanx()+"至少有一缺失，编辑失败";
				return s;
		}
		bean.setEditor(getLoginUser().getUsername());
		bean.setEdit_time(DateTimeUtil.getAllCurrTime());
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.update_anxyhlbbcssz",bean);
		return errorMessage;
	}
	
	@Transactional
	public String insertSz(Anxyhlbbcssz bean) {
		// TODO Auto-generated method stub
	
		bean.setCreator(getLoginUser().getUsername());
		bean.setCreate_time(DateTimeUtil.getAllCurrTime());
		bean.setEditor(getLoginUser().getUsername());
		bean.setEdit_time(DateTimeUtil.getAllCurrTime());
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insert_anxyhlbbcssz",bean);
		return "success";
	}

	public List<?> queryAnxyhllx() {
		// TODO Auto-generated method stub
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryAnxyhllx");
	}
	
	public List<HashMap<String, String>> queryFactoryByUsercenter(String usercenter) {
		// TODO Auto-generated method stub
		@SuppressWarnings("unchecked")
		List<HashMap<String, String>> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.query_Gongc",usercenter);;
		if(0 == list.size()){
			Map<String,String> map1 = new HashMap<String,String>();
			map1.put("KEY", "");	
			map1.put("VALUE", GetMessageByKey.getMessage("weipz"));
			list.add((HashMap<String, String>) map1);
			return list;
		}
		return list;
	}
}
