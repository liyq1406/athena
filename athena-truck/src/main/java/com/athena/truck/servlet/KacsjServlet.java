package com.athena.truck.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;

import com.athena.truck.module.bizApp.KacPdService;
import com.athena.util.cache.CacheManager;
import com.athena.util.cache.CacheValue;
import com.toft.core3.web.context.support.WebSdcContextUtils;

/**
 * 卡车司机登录
 * @author 贺志国
 * @E-mail zghe@isoftstone.com
 * @version v1.0
 * @date 2015-3-10
 */
public class KacsjServlet extends HttpServlet {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	protected static Logger logger = Logger.getLogger(KacsjServlet.class);	//定义日志方法
	
	KacPdService kpdService = null;
	
	/**
	 * 构造函数
	 */
	public KacsjServlet(){
		super();
	}
	
	/**
	 * servlet初始化
	 */
	public void init(){
		ServletContext context = this.getServletContext();
		kpdService = WebSdcContextUtils.getWebSdcContext(context).getComponent(KacPdService.class);
	}
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String collback =request.getParameter("collback");
		String kach =request.getParameter("kach");   //卡车号
		String yundh =request.getParameter("yundh"); //运单号
		String code =request.getParameter("code"); //CODE码
		String paidbs =request.getParameter("pdbs"); //排队标识
		String ak =request.getParameter("ak");       //返回码
		logger.info("code="+code+"  paidbs="+paidbs+"  kach="+kach+"  yundh="+yundh+"  ak="+ak);
		response.setContentType("text/html;charset=utf-8");
		String jsonStr = "";
		switch (Integer.parseInt((code))) {
		case 1: //验证卡车号是否存在
			jsonStr = loginValidateKac(kach,collback);
			break;
		case 2: //状态
			jsonStr = kacStatusSelf(kach,collback);
			break;
		case 3://排队情况
			jsonStr = paidList(kach,paidbs,collback);
			break;
		case 4://运单列表
			jsonStr = yundPaidList(kach,collback);
			break;
		case 5://我的信息
			jsonStr = OwnerInfo(kach,collback);
			break;
		case 6://运单明细
			jsonStr = yundmxList(yundh,collback);
			break;
		case 7://取用户中心下拉列表
			jsonStr = getOptionsUsercenter(collback);
			break;
		default:
			logger.info("无此状态码"+code);
			break;
		}
		logger.info(jsonStr);
        response.getWriter().write(jsonStr);
	}



	/**
	 * 登录卡车验证
	 * @author 贺志国
	 * @date 2015-2-5
	 * @param kach 卡车号
	 * @return JSON 字符串
	 */
	public String loginValidateKac(String kach,String collback){
		String numC = kpdService.selectKacNum(kach);
		String str =  "{\"numC\":"+numC+"}";
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
	 * 卡车状态查询
	 * @author 贺志国
	 * @date 2015-2-10
	 * @param kach
	 * @param collback
	 * @return
	 */
	public String kacStatusSelf(String kach,String collback){
		String str = kpdService.selectKacStatusCurr(kach);
		logger.info("kacStatusSelf-->"+getJsonStr(collback,str));
		return getJsonStr(collback,str);
	}
	
	
	/**
	 * 排队列表查询
	 * @author 贺志国
	 * @date 2015-2-10
	 * @param kach
	 * @param collback
	 * @return
	 */
	public String paidList(String kach,String paidbs,String collback){
		String str = kpdService.selectChelpd(kach,paidbs);
		logger.info("paidList-->"+getJsonStr(collback,str));
		return  getJsonStr(collback,str);
	}
	
	
	/**
	 * 排队的运单列表
	 * @author 贺志国
	 * @date 2015-2-12
	 * @param kach
	 * @param collback
	 * @return
	 */
	private String yundPaidList(String kach, String collback) {
		String str =kpdService.selectYundOfKac(kach);
		logger.info("yundPaidList->"+getJsonStr(collback,str));
		return getJsonStr(collback,str);
	}
	
	/**
	 * 运单查询
	 * @author 贺志国
	 * @date 2015-2-10
	 * @param kach
	 * @param collback
	 * @return OK
	 */
	public String yundList(String kach,String collback){
		String str = kpdService.selectYund(kach);
		logger.info("yundList-->"+getJsonStr(collback,str));
		return  getJsonStr(collback,str);
	}
	
	/**
	 * 运单明细查询
	 * @author 贺志国
	 * @date 2015-3-21
	 * @param yundh
	 * @param collback
	 * @return
	 */
	private String yundmxList(String yundh, String collback) {
		String str = kpdService.selectYundmx(yundh);
		logger.info("yundmxList-->"+getJsonStr(collback,str));
		return  getJsonStr(collback,str);
	}
	
	
	/**
	 * 承运商信息INFO
	 * @author 贺志国
	 * @date 2015-2-12
	 * @param kach
	 * @param collback
	 * @return
	 */
	private String OwnerInfo(String kach, String collback) {
		String str = kpdService.kacInfo(kach);
		logger.info("kacInfo-->"+getJsonStr(collback,str));
		return getJsonStr(collback,str);
	}
	
	/**
	 * JSON转换
	 * @author 贺志国
	 * @date 2015-2-10
	 * @param collback
	 * @param str
	 * @return
	 */
	private String getJsonStr(String collback,String str){
		StringBuilder strBuf = new StringBuilder();
		strBuf.append(collback+"(");
		strBuf.append(str);
		strBuf.append(")");
		return strBuf.toString();
	}

}
