package com.athena.ckx.module.cangk.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.ckx.entity.cangk.Rongqc;
import com.athena.ckx.entity.cangk.Wuldrqgx;
import com.athena.ckx.entity.xuqjs.Shengcx;
import com.athena.ckx.util.DBUtil;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;


/**
 *物理点容器关系
 * @author wangyu
 * 2012-12-10
 */
@Component
public class WuldrqgxService extends BaseService<Wuldrqgx>{

	@Override
	public String getNamespace() {
		return "ts_ckx";
	}

	/**
	 * 批量保存方法
	 * @author wangyu
	 * @date 2012-4-17
	 * @param insert,edit,delete,userID
	 * @return ""
	 */
	@Transactional
	public String save( Wuldrqgx bean,LoginUser user) throws ServiceException{
		edits(bean,user);
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
	public String inserts(Wuldrqgx bean,LoginUser user)throws ServiceException{
			bean.setCreator(user.getUsername());
			bean.setCreate_time(DateTimeUtil.getAllCurrTime());
			//如果类型为 产线,则要验证 生产线编号的存在性
			if("1".equals(bean.getWuldlx())){
				Shengcx scx = new Shengcx();
				scx.setUsercenter(bean.getUsercenter());
				scx.setShengcxbh(bean.getWuld());
				scx.setBiaos("1");
				String mes1 = "物理点"+bean.getWuld()+"不存在";
				if(!DBUtil.checkCount(baseDao,"ts_ckx.getCountShengcx", scx)){
					throw new ServiceException(mes1);
				}
				//验证 填写的生产线编号 是否不是  工艺消耗点中的生产线编号 
				Map map = new HashMap();
				map.put("usercenter", bean.getUsercenter());
				map.put("shengcxbh", bean.getWuld());
				List<Shengcx> shengcx = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.yzsxc", map);
				if(0==shengcx.size()){
					throw new ServiceException("生产线"+bean.getWuld()+"已在 消耗点-容器场 存在");
				}
			}
			//验证容器场编号是否存在
			Rongqc rongqc = new Rongqc();
			rongqc.setUsercenter(bean.getUsercenter());
			rongqc.setRongqcbh(bean.getWuld2());
			rongqc.setShiffk("R");
			rongqc.setBiaos("1");
			String mes = GetMessageByKey.getMessage("rongqcbh")+bean.getWuld2()+GetMessageByKey.getMessage("notexist");
			if(!DBUtil.checkCount(baseDao,"ts_ckx.getCountRongqc", rongqc)){
				throw new ServiceException(mes);
			}
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertWuldrqgx",bean);
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
	private void edits(Wuldrqgx bean,LoginUser user) throws ServiceException{
			bean.setEditor(user.getUsername());
			bean.setEdit_time(DateTimeUtil.getAllCurrTime());
			//验证容器场编号是否存在
			Rongqc rongqc = new Rongqc();
			rongqc.setUsercenter(bean.getUsercenter());
			rongqc.setRongqcbh(bean.getWuld2());
			rongqc.setShiffk("R");
			rongqc.setBiaos("1");
			String mes = GetMessageByKey.getMessage("rongqcbh")+bean.getWuld2()+GetMessageByKey.getMessage("notexist");
			if(!DBUtil.checkCount(baseDao,"ts_ckx.getCountRongqc", rongqc)){
				throw new ServiceException(mes);
			}
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateWuldrqgx",bean);
	}
}
