package com.athena.util.cache;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
/**
 * 字典转换类
 * @author WL
 * @date 2011-12-05
 *
 */
public class ConvertTagUtils{
	private static Logger logger = Logger.getLogger(ConvertTagUtils.class.getName());
	/**
	 * 转换数据
	 * @param pageContext 上下文根
	 * @param convert 转换数据标识
	 */
	@SuppressWarnings("unchecked")
	public static void addPageConvert(PageContext pageContext,String convert){
		try {
			logger.debug("查找" + convert + "对应缓存");
			Object obj = pageContext.getAttribute("convertList");//转换集合
			List<String> list;
			if(obj != null){//转换集合不为空,取出集合添加该转换
				list = (List<String>) obj;
			}
			else{
				list = new ArrayList<String>();	//当前不存在转换集合,新建
			}
			if(!list.contains(convert)){//不包含当前转换,添加
				LoginUser user = AuthorityUtils.getSecurityUser();//获取用户信息
				String json = "";
				//如果是用户中心缓存
				if(convert.equals("queryUserCenterMap")){
					list.add(convert);
					json = fromString(user.getUcList());
				}else{
					List<CacheValue> value = new ArrayList<CacheValue>();
					CacheManager cm = CacheManager.getInstance();//获取缓存管理类实例
					Cache cache = cm.getCacheInfo(convert);//取缓存值进行转换
					if(cache != null){
						value = (List<CacheValue>) cache.getCacheValue();
						json = fromObject(value,user.getUsercenter());//将缓存值转换为json字符串
					}
				}
				if(!json.equals("")){
					list.add(convert);
					pageContext.setAttribute(convert, json);//保存转换后的json	
					pageContext.setAttribute("convertList", list);
					logger.debug("生成" + convert + "对应缓存");
				} else {
					logger.debug(convert + "没有找到对应缓存");
				}
			}
		}catch (Exception e) {
			logger.error("读取缓存异常！");
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * MAP转换成json字符串
	 * @param map map数据集合
	 * @param usercenter用户中心
	 * @return json字符串
	 */
	private static String fromObject(List<CacheValue> list,String usercenter){
		try {
			StringBuilder sb = new StringBuilder("{");
			String flag = "";
			for (int i = 0; i < list.size(); i++) {//循环map,拼接json字符串
				CacheValue value = list.get(i);
				if(value.getUsercenter() == null || value.getUsercenter().equals("0")){//如果获取的用户中心为0,则代表不需要进行过滤
					sb.append(flag).append("'").append(value.getKey());
					sb.append("':'");
					sb.append(value.getValue()).append("'"); 
					flag = ",";
				}else if(value.getUsercenter().equals(usercenter)){//过滤用户中心
					sb.append(flag).append("'").append(value.getKey());
					sb.append("':'");
					sb.append(value.getValue()).append("'"); 
					flag = ",";
				} 
			} 
			sb.append("}");//循环后续处理
			return sb.toString();
		} catch (Exception e) {
			logger.error("JSON转换异常！");
			logger.error(e.getMessage());
			return "{}";//异常返回空串
		}
	}
	
	/**
	 * MAP转换成json字符串
	 * @param map map数据集合
	 * @param usercenter用户中心
	 * @return json字符串
	 */
	private static String fromString(List<String> list){
		try {
			StringBuilder sb = new StringBuilder("{");
			String flag = "";
			for (int i = 0; i < list.size(); i++) {//循环map,拼接json字符串
				String str = list.get(i);
				sb.append(flag).append("'").append(str);
				sb.append("':'");
				sb.append(str).append("'"); 
				flag = ",";
			} 
			sb.append("}");//循环后续处理
			return sb.toString();
		} catch (Exception e) {
			logger.error("JSON转换异常！");
			logger.error(e.getMessage());
			return "{}";//异常返回空串
		}
	}
	
	public static String getConvertValue(PageContext pageContext,String convert){
		return (String) pageContext.getAttribute(convert);
	}
}
