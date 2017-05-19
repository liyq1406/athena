package com.athena.truck.module.bizApp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.truck.entity.Liucdy;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;

/**
 * 
 * @author 贺志国
 * @E-mail zghe@isoftstone.com
 * @version v1.0
 * @date 2015-1-28
 */

@Component
public class KacPdService  extends BaseService<Liucdy>{

	protected static Logger logger = Logger.getLogger(KacPdService.class);	//定义日志方法
	//private Connection conn = DbUtils.getConnection("1");
	
	/**
	 * @author 贺志国
	 * @date 2015-1-28
	 */
	@Override
	protected String getNamespace() {
		return "ts_kac";
	}

	
	/**
	 * 登录验证，查询是否存在卡车号
	 * @author 贺志国
	 * @date 2015-2-5
	 * @param kach
	 * @return
	 * @throws ServiceException
	 */
	public String selectKacNum(String kach) throws ServiceException{
		Map<String,String> params  = new HashMap<String,String>();
		params.put("kach", kach);
		return (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_kac.queryKacNum",params);
	}
	
	/**
	 * 卡车当前状态查询
	 * @author 贺志国
	 * @date 2015-2-10
	 * @param kach
	 * @return
	 * 查询返回数据
	  maps = [
	 		{
				"userID" : "001",
				"userName" : "董卓",
				"userAge" : "56"
			}, 
			{
				"userID" : "002",
				"userName" : "马超",
				"userAge" : "36"
			}
		];
	 */
	@SuppressWarnings("unchecked")
	public String selectKacStatusCurr(String kach){
		Map<String,String> params  = new HashMap<String,String>();
		params.put("kach", kach);
		List<Map<String,String>> list= baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_kac.queryKacCurrStatus",params);
		JSONArray jsonarray = JSONArray.fromObject(list); 
		logger.info("kacStatusArray-->"+jsonarray.toString());
		return jsonarray.toString();
		
	}
	
	
	/**
	 * 卡车排队查询
	 * @author 贺志国
	 * @date 2015-2-10
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String selectChelpd(String kach,String paidbs){
		Map<String,String> params  = new HashMap<String,String>();
		params.put("kach", kach);
		params.put("paidbs", paidbs);
		List<Map<String,String>> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_kac.queryKacOfPaid",params);
		JSONArray chelpdJson = JSONArray.fromObject(list); 
		logger.info("chelpdJson-->"+chelpdJson.toString());
		return chelpdJson.toString();
	}


	/**
	 * 卡车所在区域下正在排队的运单查询
	 * @author 贺志国
	 * @date 2015-2-12
	 * @param kach
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String selectYundOfKac(String kach){
		Map<String,String> params  = new HashMap<String,String>();
		params.put("kach", kach);
		List<Map<String,String>> list =  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_kac.queryYundOfpaid",params);
		JSONArray yundPaidJson = JSONArray.fromObject(list); 
		logger.info("yundPaidJson-->"+yundPaidJson.toString());
		return yundPaidJson.toString();
		
	}
	
	/**
	 * 单独查询运单数据  停用
	 * @author 贺志国
	 * @date 2015-2-10
	 * @return 返回JSON字符串 OK
	 */
	@SuppressWarnings("unchecked")
	public String selectYund(String kach){
		Map<String,String> params  = new HashMap<String,String>();
		params.put("kach", kach);
		List<Map<String,String>> list =  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_kac.queryYundOfpaidInfo",params);
		JSONArray yundJson = JSONArray.fromObject(list); 
		logger.info("YundJson-->"+yundJson.toString());
		return yundJson.toString();
		
	}

	/**
	 * wmsys.wm_concat(a.chengysbh||'') as chengysbh
	 * onwerInfo 我的信息查询
	 * @author 贺志国
	 * @date 2015-2-11
	 * @param kach
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String kacInfo(String kach) {
		Map<String,String> params  = new HashMap<String,String>();
		params.put("kach", kach);
		List<Map<String,String>> list =  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_kac.queryYundOfOwnerInfo",params);
		JSONArray OwnerJson = JSONArray.fromObject(list); 
		logger.info("OwnerJson-->"+OwnerJson.toString());
		return OwnerJson.toString();
	}

	
	/**
	 * 运单明细
	 * @author 贺志国
	 * @date 2015-3-21
	 * @param yundh
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String selectYundmx(String yundh) {
		Map<String,String> params  = new HashMap<String,String>();
		params.put("yundh", yundh);
		List<Map<String,String>> list =  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_kac.queryYundMx",params);
		JSONArray yundmxJson = JSONArray.fromObject(list); 
		logger.info("yundmxJson-->"+yundmxJson.toString());
		return yundmxJson.toString();
	}
	
}
