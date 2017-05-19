package com.athena.xqjs.module.anxorder.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.jws.WebService;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.anxorder.Anxjscszjb;
import com.athena.xqjs.entity.anxorder.Xiaohcxhdmb;
import com.toft.core3.container.annotation.Component;
/**
 * 新按需计算webservice
 * 
 * @author zbb
 *
 */
@SuppressWarnings("rawtypes")
@WebService(endpointInterface="com.athena.xqjs.module.anxorder.service.XinaxCshWebservice", serviceName="/xinaxCshWebservice")
@Component
public class XinaxCshWebserviceImpl extends BaseService implements XinaxCshWebservice {

	@Override
	public String queryZhengcxh(Anxjscszjb anxjscszjb,String liush) {
		// TODO Auto-generated method stub
		Map<String, String> param = new HashMap<String, String>();
		param.put("lingjbh", anxjscszjb.getLingjbh());//零件编号
		param.put("xiaohd", anxjscszjb.getXiaohd());//消耗点
		param.put("usercenter", anxjscszjb.getUsercenter());//用户中心	
		param.put("xiaohcbh", anxjscszjb.getXiaohcbh());//小火车编号
		param.put("shengcxbh", anxjscszjb.getShengcxbh());//生产线
		param.put("zongzlsh", liush);
		return this.strNullAx(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectObject("xinaxChushJis.selectzhengcLiush", param));
	}

	@Override
	public Xiaohcxhdmb queryXiaohcxhdmb(Anxjscszjb anxjscszjb,String zhengcxh) {
		// TODO Auto-generated method stub	
		Map<String, String> param = new HashMap<String, String>();		
		param.put("xiaohd", anxjscszjb.getXiaohd());//消耗点
		param.put("usercenter", anxjscszjb.getUsercenter());//用户中心	
		param.put("xiaohcbh", anxjscszjb.getXiaohcbh());//小火车编号
		param.put("shengcxbh", anxjscszjb.getShengcxbh());//生产线
		param.put("liush", zhengcxh);
		Xiaohcxhdmb xiaohcxhdmb =  (Xiaohcxhdmb) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectObject("xinaxChushJis.queryXiaohcxhdmb", param);
		return xiaohcxhdmb;
	}	

	@Override
	public BigDecimal querySppvxhl(Anxjscszjb anxjscszjb,String kaislsh,String zhengcxh,String tangc) {
		// TODO Auto-generated method stub
		Map<String, String> param = new HashMap<String, String>();
		param.put("lingjbh", anxjscszjb.getLingjbh());//零件编号
		param.put("xiaohd", anxjscszjb.getXiaohd());//消耗点
		param.put("usercenter", anxjscszjb.getUsercenter());//用户中心			
		param.put("shengcxbh", anxjscszjb.getShengcxbh());//生产线		
		param.put("liush", zhengcxh);
		param.put("kaislsh", kaislsh);
		return this.getBigDecimalAx(baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectObject("xinaxChushJis.querySppvxhl", param));
	}
	public String strNullAx(Object obj) {
		if (obj != null) {
			return obj.toString();
		} else {
			return "";
		}
	}
	public BigDecimal getBigDecimalAx(Object obj) {
		if (obj != null) {
			return new BigDecimal(this.strNullAx(obj));
		} else {
			return BigDecimal.ZERO;
		}
	}

}
