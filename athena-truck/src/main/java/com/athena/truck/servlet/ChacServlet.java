package com.athena.truck.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;

import com.athena.authority.util.AuthorityUtils;
import com.athena.truck.module.bizApp.ChacsjService;
import com.athena.util.cache.CacheManager;
import com.athena.util.cache.CacheValue;
import com.toft.core3.web.context.support.WebSdcContextUtils;

/**
 * 叉车司机登录
 * @author 贺志国
 * @E-mail zghe@isoftstone.com
 * @version v1.0
 * @date 2015-3-10
 */

public class ChacServlet extends HttpServlet {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	protected static Logger logger = Logger.getLogger(ChacServlet.class);	//定义日志方法
		
	 Map<String,String> postMap = new HashMap<String, String>();
	 ChacsjService chacsjService = null;
		//引入service Dao
	 public void init() throws ServletException {
		 ServletContext context = this.getServletContext();
		 chacsjService = WebSdcContextUtils.getWebSdcContext(context).getComponent(ChacsjService.class);
	 }
	
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String collback =request.getParameter("collback");
		String kach =request.getParameter("kach");
		String usercenter =request.getParameter("usercenter");
		String username =request.getParameter("username");
		String password =request.getParameter("password");
		String code =request.getParameter("code");
		String ak =request.getParameter("ak");
		String chewbh =request.getParameter("chewbh");
		String yundh =request.getParameter("yundh");
		String zhuangt =request.getParameter("zhuangt");
		String liucbh =request.getParameter("liucbh");
		String liucmc =request.getParameter("liucmc");
		String quybh =request.getParameter("quybh");
		String daztbh =request.getParameter("daztbh");
		String biaozsj =request.getParameter("biaozsj");
		
		Map<String,String> params = new HashMap<String,String>();
		params.put("usercenter", usercenter);
		params.put("kach", kach);
		params.put("chewbh", chewbh);
		params.put("yundh", yundh);
		params.put("zhuangt", zhuangt);
		params.put("liucbh", liucbh);
		params.put("liucmc", liucmc);
		params.put("quybh", quybh);
		params.put("daztbh", daztbh);
		params.put("biaozsj", biaozsj);
		
		logger.info("collback-->"+collback+" code-->"+code+" ak--"+ak+" usercenter-->"+usercenter+" username-->"+username);
		logger.info("quybh-->"+quybh+" daztbh-->"+daztbh+" liucbh-->"+liucbh+" liucmc-->"+liucmc);
		response.setContentType("text/html;charset=utf-8");
		String jsonStr = "";
		switch (Integer.parseInt((code))) {
		case 1: //取用户中心下拉列表
			jsonStr = getOptionsUsercenter(collback);
			break;
		case 2: //登录验证
			jsonStr = loginValidate(username,usercenter,password,collback);
			break;
		case 3://卸货卡车列表
			jsonStr = xiehChacList(username,usercenter,collback);
			break;
		case 4://叉车按钮操作
			jsonStr = btnXiehOperate(username,params,collback);
			break;
		default:
			logger.info("无此状态码"+code);
			break;
		}
		logger.info(jsonStr);
        response.getWriter().write(jsonStr);
	}
	
	/**
	 * 卸货叉车列表
	 * @author 贺志国
	 * @date 2015-3-13
	 * @param username
	 * @param collback
	 * @return
	 */
	private String xiehChacList(String username, String usercenter,String collback) {
		String str =chacsjService.waitXiehKacList(postMap,username,usercenter);
		logger.info("waitXiehKacList->"+getJsonStr(collback,str));
		return getJsonStr(collback,str);
	}


	/**
	 * 叉车司机登录验证
	 * @author 贺志国
	 * @date 2015-3-11
	 * @param usercenter 用户中心
	 * @param username 用户名
	 * @param password 密码
	 * @param collback
	 * @return
	 */
	private String loginValidate(String username,String usercenter,
			String password, String collback) {
		if(postMap!=null && postMap.size()>0){
			postMap.clear();
		}
		String strNum = chacsjService.onLoginValid(username,password);
		if(Integer.parseInt(strNum)>0){
			List<Map<String,String>> listMap = chacsjService.getLoginUserPost("CHACGLY", username, usercenter);
			if(listMap.size()>0){
				postMap.put("POST_CODE", listMap.get(0).get("POST_CODE"));
				postMap.put("USERCENTER", listMap.get(0).get("USERCENTER"));
			}
		}
		List<Map<String,String>> chacbh = chacsjService.getChacbh(postMap,username, usercenter);
		String str =  "{\"numC\":"+strNum+",\"quybh\":\""+chacbh.get(0).get("QUYBH")+"\",\"chacbh\":\""+chacbh.get(0).get("CHACBH")+"\"}";
		return getJsonStr(collback,str);
	}
	

	/**
	 * 用户中心缓存列表
	 * @author 贺志国
	 * @date 2015-3-11
	 * @param collback
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String getOptionsUsercenter(String collback) {
		CacheManager cm = CacheManager.getInstance();
		List<CacheValue> list = (List<CacheValue>) cm.getCacheInfo("queryUserCenterMap").getCacheValue();
		JSONArray ucList = JSONArray.fromObject(list); 
		logger.info("bufJson-->"+ucList.toString());
		logger.info("usercenterList->"+getJsonStr(collback,ucList.toString()));
		return getJsonStr(collback,ucList.toString());
	}

	/**
	 *叉车员操作按钮功能
	 * @author 贺志国
	 * @date 2015-3-19
	 * @param params
	 * @param collback
	 * @return
	 */
	private String btnXiehOperate(String username,Map<String,String> params, String collback) {
		int i = chacsjService.startXiehOperate(username,params);
		String str =  "{\"success\":"+i+"}";
		logger.info("xiehOperate-->"+getJsonStr(collback,str));
		return getJsonStr(collback,str);
	}

	
	
	/**
	 * jsonp调用格式
	 * @author 贺志国
	 * @date 2015-2-10
	 * @param collback
	 * @param str
	 * @return
	 */
	private String getJsonStr(String collback,String str){
		StringBuilder strBuf = new StringBuilder();
		strBuf.append(collback+"(");
		strBuf.append(str+")");
		return strBuf.toString();
	}

}
