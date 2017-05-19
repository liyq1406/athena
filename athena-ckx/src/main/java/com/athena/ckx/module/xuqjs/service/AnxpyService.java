package com.athena.ckx.module.xuqjs.service;

import com.athena.ckx.entity.xuqjs.Anxpy;

import java.util.ArrayList;
import java.util.List;
import com.athena.authority.entity.LoginUser;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.component.service.BaseService;
import com.athena.component.service.Message;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.dao.DataAccessException;
import com.toft.core3.transaction.annotation.Transactional;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * 按需需求平移
 * @author kong
 */
@Component
public class AnxpyService extends BaseService<Anxpy>{
	@Override
	public String getNamespace() {
		return "ts_ckx";
	}
	
	@Transactional
	public String save(List<Anxpy> addList,List<Anxpy> editList,List<Anxpy> delList,LoginUser user) {
		add(addList,user);//添加
		edit(editList,user);//修改
		del(delList,user);//删除
		return new Message("saveSuccess","i18n.ckx.workCalendar.i18n_workCalendar").getMessage();
	}
	
	/**
	 * 检查产线数据  0011651
	 * @param addList
	 * @return
	 * @throws Exception 
	 */
	public String validateChax(List<Anxpy> addList,List<Anxpy> editList){	
		String i="";
		if(addList.size()!=0){
	 		for (Anxpy anxpy : addList) {
		 			anxpy.getUsercenter();//用户中心
		 			anxpy.getChanx();//产线
		 			
		 			 i = (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.checkChanx", anxpy);
		 			 //0011730 hanwu 20150909 原来的写法只会校验最后一条数据
		 			 if("0".equals(i))
		 				 return i;
	 		}
		}		
		
		if(editList.size()!=0){
	 		for (Anxpy anxpye : editList) {
	 			anxpye.getUsercenter();//用户中心
	 			anxpye.getChanx();//产线
	 			
	 			 i = (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.checkChanx", anxpye);
	 			//0011730 hanwu 20150909 原来的写法只会校验最后一条数据
	 			 if("0".equals(i))
	 				 return i;
	 		}
		}
		
		return i;	
	}
	
	/**
	 * 验证修改时的标识，只能对未生效的按需平移进行修改
	 * @param editList
	 * @return
	 */
	public boolean validateFlag(List<Anxpy> editList, List<Anxpy> delList){
		if(editList.size()!=0){
	 		for (Anxpy anxpy : editList) {
	 			//标识
	 			String flag = anxpy.getFlag();
	 			if(!"0".equals(flag))
	 				return false;
	 		}
		}
		if(delList.size()!=0){
	 		for (Anxpy anxpy : delList) {
	 			//标识
	 			String flag = anxpy.getFlag();
	 			if(!"0".equals(flag))
	 				return false;
	 		}
		}
		return true;
	}
	
	/**
	 * 根据用户中心+产线+未生效验证按需平移的唯一性
	 * @param addList
	 * @return
	 */
	public boolean validateUniqueness(List<Anxpy> addList){
		List<String> strList = new ArrayList<String>(); 
		if(addList.size()!=0){
	 		for (Anxpy anxpy : addList) {
	 			Anxpy param = new Anxpy();
	 			param.setUsercenter(anxpy.getUsercenter());
	 			param.setChanx(anxpy.getChanx());
	 			param.setFlag("0");
	 			//验证addList中的按需平移是否重复
	 			if(strList.contains(anxpy.getUsercenter()+anxpy.getChanx()+"0")){
		 			return false;
		 		}else {
		 			strList.add(anxpy.getUsercenter()+anxpy.getChanx()+"0");
		 		}
	 			//获取同一用户中心+同一产线+未生效的按需平移
		 		String count = (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.checkAnxpyUniqueness", param);
		 		//验证数据库中的按需平移是否重复
		 		if(!"0".equals(count))
		 			return false;
	 		}
		}	
		return true;
	}
	
	
	/**
	 * 增加数据
	 * @param addList
	 * @return
	 * @throws Exception 
	 */
	private void add(List<Anxpy> addList,LoginUser user){
 		for (Anxpy anxpy : addList) {
//			try {
	 			anxpy.setCreateTime(DateTimeUtil.getAllCurrTime());//创建时间
	 			anxpy.setCreator(user.getUsername());//创建人
	 			anxpy.setEditor(user.getUsername());//修改人
	 			anxpy.setEditTime(DateTimeUtil.getAllCurrTime());//修改时间
				super.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertAnxpy", anxpy);
//			} catch (DataAccessException e) {
//				//日期{0}已存在，保存失败
//				throw new ServiceException(new Message("dateObjsaveError","i18n.ckx.workCalendar.i18n_workCalendar").getMessage(new String[]{ckxXiuxr.getRiq()}));
//			}
		}
	}
	/**
	 * 修改数据
	 * @param editList
	 * @return
	 * @throws Exception 
	 */
	private void edit(List<Anxpy> editList,LoginUser user) {
		for (Anxpy anxpy : editList) {
			anxpy.setEditor(user.getUsername());//修改人
			anxpy.setEditTime(DateTimeUtil.getAllCurrTime());//修改时间
			super.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateAnxpy", anxpy);
		}
	}
	/**
	 * 删除数据
	 * @param delList
	 * @return
	 * @throws Exception 
	 */
	private void del(List<Anxpy> delList,LoginUser user){
		for (Anxpy anxpy : delList) {
			try {
				anxpy.setEditor(user.getUsername());//修改人
				anxpy.setEditTime(DateTimeUtil.getAllCurrTime());//修改时间
				super.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteAnxpy", anxpy);
			} catch (DataAccessException e) {
				//日期{}删除失败
				throw new ServiceException("删除错误", e);
			}
		}
	}
	
	/**0011786
	 * 停线时间必须大于接口上次计算时间
	 * @param editList
	 * @return
	 */
	public boolean validateTxsj(List<Anxpy> editList, List<Anxpy> addList){
		//查询1890接口上次运行时间 
		String ULinbh="1890";
		String UWinbh="1891";
		
		String ULlastTime = (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getLasttime", ULinbh);// 上次运行时间
		String UWlastTime = (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getLasttime", UWinbh);// 上次运行时间
		
		
		if(ULlastTime!=null && !ULlastTime.equals("") && UWlastTime!=null && !UWlastTime.equals("") ){
			if(editList.size()!=0){
		 		for (Anxpy anxpy : editList) {
		 			//停线时间 
		 			String txsj = anxpy.getTxsj(); 
		 			String usercenter =  anxpy.getUsercenter();
		 			
		 			if(usercenter.equals("UL")||usercenter.equals("UX")){			 			
			 			if(!dateToStringYMDHms(ULlastTime,txsj)){ 
			 				return false;
			 			}
		 			}
		 			
		 			if(usercenter.equals("UW")){			 			
			 			if(!dateToStringYMDHms(UWlastTime,txsj)){ 
			 				return false;
			 			}
		 			}
		 		}
			}
			if(addList.size()!=0){
		 		for (Anxpy anxpy : addList) {
		 			//停线时间 
		 			String txsj = anxpy.getTxsj();
		 			String usercenter =  anxpy.getUsercenter();
		 			
		 			if(usercenter.equals("UL")||usercenter.equals("UX")){			 			
			 			if(!dateToStringYMDHms(ULlastTime,txsj)){ 
			 				return false;
			 			}
		 			}
		 			
		 			if(usercenter.equals("UW")){			 			
			 			if(!dateToStringYMDHms(UWlastTime,txsj)){ 
			 				return false;
			 			}
		 			}
		 			
		 		}
			}  
		} 
	
		return true;
	}
	
	public  boolean dateToStringYMDHms(String beforeDate,String afterDate){
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA); 
    	boolean flag=false;
    	try {
    		flag=formatter.parse(beforeDate).before(formatter.parse(afterDate));
		} catch (ParseException e) {
			flag=false;
		}
		return flag;
		
	}
}