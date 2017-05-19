package com.athena.ckx.module.paicfj.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.ckx.entity.paicfj.Ckx_chanxz;
import com.athena.ckx.entity.paicfj.Ckx_chanxz_paiccs;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;


/**
 * 排产参数设置Service-产线组子表
 * @author hj
 * @Date 12/02/16
 * 
 */
@Component
public class Ckx_chanxz_paiccsService extends BaseService<Ckx_chanxz_paiccs> {

	@Override
	public String getNamespace(){
		return "ts_ckx";
	}
	/**
	 * 批量录入排产参数设置数据
     * @author hj
     * @Date 12/02/16
	 * @param insert
	 * @param userID
	 * @param chanxz
	 * @return ""
	 * @throws ServiceException
	 */
	public String inserts(List<Ckx_chanxz_paiccs> insert,String userID,Ckx_chanxz chanxz){
		Date date = new Date();
		Date f=new Date();
		for(Ckx_chanxz_paiccs bean:insert){				
			SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
			try {
				f=sf.parse(bean.getBanb());
			} catch (ParseException e) {
				//日期格式转换异常
				throw new ServiceException(GetMessageByKey.getMessage("rqyc"));
			}
			//如果当前版本小于当前日期，抛出异常（版本必须大于当前日期）提示，并跳出此方法
			if(!f.after(new Date())){
				//"版本必须大于当前日期"
				throw new ServiceException(GetMessageByKey.getMessage("bbbxdydq"));
			}  
			//将产线组的主键赋值给子表
			bean.setChanxzbh(chanxz.getChanxzbh());
			bean.setUsercenter(chanxz.getUsercenter());
			bean.setCreator(userID);
			bean.setCreate_time(date);
			bean.setEditor(userID);
			bean.setEdit_time(date);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertCkx_chanxz_paiccs",bean);
		}
		return "";
	}
	/**
	 * 批量修改排产参数设置数据
	 * @author hj
     * @Date 12/02/16
	 * @param edit
	 * @param userID
	 * @param chanxz
	 * @return ""
	 * @throws ServiceException
	 */
	public String edits(List<Ckx_chanxz_paiccs> edit,String userID,Ckx_chanxz chanxz){
		Date date = new Date();
		for(Ckx_chanxz_paiccs bean:edit){
			// 将产线组的主键赋值给子表
			bean.setChanxzbh(chanxz.getChanxzbh());
			bean.setUsercenter(chanxz.getUsercenter());
			bean.setEditor(userID);
			bean.setEdit_time(date);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateCkx_chanxz_paiccs", bean);
		}
		return "";
	}
	/**
	 * 批量删除排产参数设置数据
	 * @author hj
     * @Date 12/02/16
	 * @param delete
	 * @param chanxz
	 * @return ""
	 * @throws ServiceException
	 */
	public String deletes(List<Ckx_chanxz_paiccs> delete,Ckx_chanxz chanxz){
		Date f = new Date();
		Date d = new Date();
		for(Ckx_chanxz_paiccs  bean : delete){	
			SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
			String ds = "";
			try {
				f = sf.parse(bean.getBanb());
				ds = sf.format(d);
			} catch (ParseException e) {
				//"日期格式转换异常"
				throw new ServiceException(GetMessageByKey.getMessage("rqyc"));
			}
			Map<String,String> m = new HashMap<String, String>();
			m.put("currentDate", ds);
			m.put("usercenter", chanxz.getUsercenter());
			m.put("chanxzbh", chanxz.getChanxzbh());
			Integer count = (Integer) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getCountpaiccs",m);
			
			if(2 > count){
				//"必须保留一条没有过期的版本"
				throw new ServiceException(GetMessageByKey.getMessage("bbbxdydq"));
			}
			//如果当前版本小于当前日期，抛出异常（版本必须大于当前日期）提示，并跳出此方法
			if(!f.after(new Date())){
				//版本已过期,不能删除
				throw new ServiceException(GetMessageByKey.getMessage("bnsc"));
			}  
			bean.setUsercenter(chanxz.getUsercenter());
			bean.setChanxzbh(chanxz.getChanxzbh());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteCkx_chanxz_paiccs",bean);
		}
		return "";
	}
	
}
