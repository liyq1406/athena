package com.athena.truck.module.bizApp;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.truck.entity.Liucdy;
import com.toft.core2.dao.database.DbUtils;
import com.toft.core3.container.annotation.Component;

/**
 * 
 * @author 贺志国
 * @E-mail zghe@isoftstone.com
 * @version v1.0
 * @date 2015-1-28
 */

@Component
public class ChacsjService  extends BaseService<Liucdy>{

	protected static Logger logger = Logger.getLogger(ChacsjService.class);	//定义日志方法
	private Connection conn = DbUtils.getConnection("1");

	
	/**
	 * @author 贺志国
	 * @date 2015-1-28
	 */
	@Override
	protected String getNamespace() {
		return "ts_kac";
	}
	
	
	/**
	 * 登录验证，查询是用户是否存在
	 * @author 贺志国
	 * @date 2015-3-11
	 * @param username
	 * @param password
	 * @return
	 */
	public String onLoginValid(String username,	String password) {
		//验证一次
		String str = "";
		try{
			Map<String,String> param = new HashMap<String, String>();
			param.put("username", username);
			param.put("password", string2MD5(password));
			str =  (String)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_kac.onLoginValid",param);
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		return str;
	}
	
	
	/**
	 * MD5加码 生成32位md5码  
	 * @author 贺志国
	 * @date 2015-3-20
	 * @param inStr
	 * @return
	 */
	public String string2MD5(String inStr){  
		MessageDigest md5 = null;  
		try{  
			md5 = MessageDigest.getInstance("MD5");  
		}catch (Exception e){  
			System.out.println(e.toString());  
			e.printStackTrace();  
			return "";  
		}  
		char[] charArray = inStr.toCharArray();  
		byte[] byteArray = new byte[charArray.length];  

		for (int i = 0; i < charArray.length; i++)  
			byteArray[i] = (byte) charArray[i];  
		byte[] md5Bytes = md5.digest(byteArray);  
		StringBuffer hexValue = new StringBuffer();  
		for (int i = 0; i < md5Bytes.length; i++){  
			int val = ((int) md5Bytes[i]) & 0xff;  
			if (val < 16)  
				hexValue.append("0");  
			hexValue.append(Integer.toHexString(val));  
		}  
		return hexValue.toString();  
	}
	
	/**
	 * //根据组找叉车所负责的大站台车位列表（所有准入的卡车列表）
	 * @author 贺志国
	 * @date 2015-3-13
	 * @param username
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String waitXiehKacList(Map<String,String> postMap,String username,String usercenter){
		//1、判断叉车下是否配置 了车位	
		Map<String,String> map = new HashMap<String,String>();
		map.put("usercenter", usercenter);//用户中心
		String chacz = postMap.get("POST_CODE")==null ? "":postMap.get("POST_CODE");
		map.put("chacz",chacz);
		Integer cc =  (Integer)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_kac.queryChacCount",map);
		List<Map<String,String>> list = null;
		if(cc.intValue()>0){//叉车下定义了车位关系，取大站台下分配了车位的卡车
			list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_kac.queryChacCwOfDazt",map);
		}else{//没有定义车位关系，取大站台下所有车位的卡车
			list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_kac.queryAllChewOfDazt",map);
		}
		JSONArray chewJson = JSONArray.fromObject(list); 
		logger.info("chewJson-->"+chewJson.toString());
		return chewJson.toString();
	}
	
	
	/**
	 * 登录用户所在的叉车组
	 * @author 贺志国
	 * @date 2015-3-13
	 * @param username
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> getLoginUserPost(String dicCode ,String username,String usercenter){
		List<Map<String,String>>  map  = null;
		try{
			Map<String,String>  params  = new HashMap<String,String>();
			params.put("username", username);
			params.put("dicCode", dicCode);
			params.put("usercenter", usercenter);
			map = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_kac.getLoginUserPost", params);
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		return map;
	}
	
	
	/**
	 * 登录用户所管理的区域
	 * 如果有多个区域，则取一个，一个用户对应一个区域
	 * @author 贺志国
	 * @date 2015-3-20
	 * @param username
	 * @param usercenter
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String> getLoginUserQuybh(Map<String,String> postMap,String username,String usercenter){
		Map<String,String>  map  = null;
		try{
			Map<String,String>  params  = new HashMap<String,String>();
			params.put("username", username);
			params.put("dicCode", "CHACGLY");
			params.put("usercenter", usercenter);
			map = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectMap("ts_kac.getLoginUserQuybh", params);
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		return map;
	}
	
	
	/**
	 * 叉车编号查询
	 * @author 贺志国
	 * @date 2015-3-20
	 * @param username
	 * @param usercenter
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> getChacbh(Map<String,String> postMap,String username,String usercenter){
		List<Map<String,String>> map  = null;
		try{
			Map<String,String>  params  = new HashMap<String,String>();
			params.put("chacz", postMap.get("POST_CODE")==null ? "":postMap.get("POST_CODE"));
			params.put("usercenter", usercenter);
			map = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_kac.getChacbh", params);
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		return map;
	}
	
	/**
	 * 叉车员操作按钮功能 ，更新运单状态，写流水表
	 * @author 贺志国
	 * @date 2015-3-19
	 * @param params yundh,usercenter,liucbh,liucmc
	 * @return i >0 success 
	 */
	public int startXiehOperate(String username,Map<String, String> params) {
		//修改状态 参数yundh,usercenter,liucbh,liucmc
		int i = 0;
		params.put("username", username);
		try{
			i = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_kac.updateYundZt", params);
			if(i>0){ //写流水表
				 baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_kac.insertLius", params);
			}
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		return i;
	}
	
	
}
