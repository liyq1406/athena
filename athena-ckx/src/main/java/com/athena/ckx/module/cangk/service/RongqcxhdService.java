package com.athena.ckx.module.cangk.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.ckx.entity.cangk.Rongqc;
import com.athena.ckx.entity.cangk.Rongqcxhd;
import com.athena.ckx.util.DBUtil;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;


/**
 * 容器场-消耗点
 * @author denggq
 * 2012-12-10
 */
@Component
public class RongqcxhdService extends BaseService<Rongqcxhd>{

	@Override
	public String getNamespace() {
		return "ts_ckx";
	}

	/**
	 * 查询未配置的容器场
	 * @author wangyu
	 * @date 2012-4-17
	 * @param insert,edit,delete,userID
	 * @return ""
	 */
	@Transactional
	public Map<String,Object> selectwpz(Rongqcxhd bean) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ts_ckx.queryRongqcxhdwpz", bean, bean);
	}
	
	
	/**
	 * 批量保存方法
	 * @author wangyu
	 * @date 2012-4-17
	 * @param insert,edit,delete,userID
	 * @return ""
	 */
	@Transactional
	public String save( ArrayList<Rongqcxhd> edit,LoginUser user) throws ServiceException{
		edits(edit,user);
		return "success";
	}
	
	
	/**
	 * 私有批量insert方法  
	 * @author denggq
	 * @date 2012-4-17
	 * @param insert,userID
	 * @return  ""
	 */
	@Transactional
	public String inserts(Rongqcxhd bean,LoginUser user)throws ServiceException{
			//新增容器场时，提交后需要验证容器场编号是否存在
			//容器场编号是否存在
			Rongqc rongqc = new Rongqc();
			rongqc.setUsercenter(user.getUsercenter());
			rongqc.setRongqcbh(bean.getRongqcbh());
			rongqc.setShiffk("R");
			rongqc.setBiaos("1");
			String mes1 = GetMessageByKey.getMessage("rongqcbh")+bean.getRongqcbh()+GetMessageByKey.getMessage("notexist");
			if(!DBUtil.checkCount(baseDao,"ts_ckx.getCountRongqc", rongqc)){
				throw new ServiceException(mes1);
			}
			//并且工艺消耗点不管填写多少位，都会将类似的数据全部对应的插入容器场
			List<Rongqcxhd> rongqcxhdlist = new ArrayList<Rongqcxhd>();
			List<Rongqcxhd>	rongqcxhd= new ArrayList<Rongqcxhd>();
			if(null==bean.getGongyxhd()){
				rongqcxhd =  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryRongqcxhd");
			}else{
				bean.setGongyxhd(bean.getGongyxhd().trim());
				rongqcxhd =  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryXhd",bean);
			}
			if(rongqcxhd.size()>0){
				for (Rongqcxhd rongqcxhd2 : rongqcxhd) {
					rongqcxhd2.setRongqcbh(bean.getRongqcbh());
					rongqcxhd2.setEditor(user.getUsername());
					rongqcxhd2.setEdit_time(DateTimeUtil.getAllCurrTime());
					rongqcxhdlist.add(rongqcxhd2);
				}
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch("ts_ckx.updateRongqcxhd", rongqcxhdlist);
			}else{
				throw new ServiceException("工艺消耗点不存在");
			}
			return "success";
	}		
	
	/**
	 * 私有批量update方法
	 * @author denggq
	 * @date 2012-4-17
	 * @param user
	 * @return ""
	 */
	@Transactional
	private void edits(List<Rongqcxhd> edit,LoginUser user) throws ServiceException{
		for(Rongqcxhd bean:edit){
			bean.setEditor(user.getUsername());
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			//容器场编号是否存在
			if(null != bean.getRongqcbh()&&!"".equals(bean.getRongqcbh())){
				Rongqc rongqc = new Rongqc();
				rongqc.setUsercenter(user.getUsercenter());
				rongqc.setRongqcbh(bean.getRongqcbh());
				rongqc.setShiffk("R");
				rongqc.setBiaos("1");
				String mes1 = GetMessageByKey.getMessage("rongqcbh")+bean.getRongqcbh()+GetMessageByKey.getMessage("notexist");
				if(!DBUtil.checkCount(baseDao,"ts_ckx.getCountRongqc", rongqc)){
					throw new ServiceException(mes1);
				}
			}
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateRongqcxhd",bean);
		}
	}
}
