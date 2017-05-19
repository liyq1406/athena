package com.athena.xqjs.module.common.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.common.Rouxbl;
import com.athena.xqjs.entity.ilorder.Dingdlj;
import com.athena.xqjs.module.common.CommonFun;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.ycbj.service.YicbjService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

@Component
public class RouxblService extends BaseService {
@Inject
private YicbjService yicbjService ;
	/**
	 * @方法： 查询实体
	 * @author 李明
	 * @version v1.0
	 * @date 2012-3-6
	 * @参数说明：订货周期（p0-p4），供应商编号，用户中心，是否关键零件
	 */
	public Rouxbl queryRouxblObject(String dinghzq, String usercenter, String guanjljjb, String gongysbh) {
		Rouxbl bean = new Rouxbl();
		Map<String, String> map = new HashMap<String, String>();
		map.put("dinghzq", dinghzq);
		map.put("usercenter", usercenter);
		map.put("guanjljjb", guanjljjb);
		map.put("gongysbh", gongysbh);
		bean = (Rouxbl) this.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("common.queryRouxblObject", map);
		map.clear();
		return bean;
	}
	/**
	 * 处理柔性比例的边界问题
	 */
	public Map<String, Double> checkRxbl(BigDecimal rxbl, Double index,Dingdlj bean,String jihyz,LoginUser loginUser) {
		Map<String, Double> map = new HashMap<String, Double>();
		if(rxbl!= null){
		Double start = rxbl.doubleValue()<0.0?0.0:rxbl.doubleValue();
		CommonFun.logger.debug("处理柔性比例的边界问题checkRxbl方法rxbl="+rxbl);
		CommonFun.logger.debug("处理柔性比例的边界问题checkRxbl方法start="+start);
		Double end = 0.0;
		if(rxbl.doubleValue()<0.0){
			String paramStr[] = new String []{bean.getUsercenter(), bean.getLingjbh(),"柔性比例"};
			this.yicbjService.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str12, jihyz, paramStr,bean.getUsercenter(), bean.getLingjbh(), loginUser, Const.JISMK_KD_CD);
			return map;
//			this.yicbjService.saveYicInfo("32", bean.getLingjbh(), "300", "柔性比例为负数，请检查供应商柔性比例表") ;
		}else{
			if (rxbl.doubleValue() >= index) {
				end = -index;
				CommonFun.logger.debug("处理柔性比例的边界问题checkRxbl方法在（rxbl.doubleValue() >= index）时rxbl="+rxbl);
			} else if (rxbl.doubleValue() < index && rxbl.doubleValue() > 0.0) {
				end = -start;
				CommonFun.logger.debug("处理柔性比例的边界问题checkRxbl方法在（rxbl.doubleValue() < index && rxbl.doubleValue() > 0.0）时rxbl="+rxbl);
			} 
		}
		map.put("max", start);
		map.put("min", end);
		}else{
			String paramStr[] = new String []{bean.getUsercenter(), bean.getLingjbh(),"柔性比例"};
			this.yicbjService.insertError(Const.YICHANG_LX2, Const.YICHANG_LX2_str12, jihyz, paramStr,bean.getUsercenter(), bean.getLingjbh(), loginUser, Const.JISMK_KD_CD);
			return map;
		}
		return map;
	}

	/**
	 * 获得对应零件在具体周期的柔性比例
	 * **/
	public BigDecimal getRxbl(String usercenter, String guanjljjb, String dinghzq, String gongysdm) {
		BigDecimal rxbl = new BigDecimal("-1");
		Rouxbl rouxbl = this.queryRouxblObject(dinghzq, usercenter, guanjljjb, gongysdm);
		if (null != rouxbl) {
			rxbl = rouxbl.getRouxbl();
		}
		return rxbl;
	}
}
