package com.athena.xqjs.util;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.athena.authority.util.AuthorityUtils;

import com.athena.util.exception.ServiceException;



/**
 * @description 获得唯一组代码优先级如下
 * @description 准备层POA，计划员，物流工艺员，执行层POA，质检员，物流员，排产计划员，要车计划员，配载计划员
 * @date 2012-08-10
 * @author denggq
 *
 */
public final class GetPostOnly {
	@SuppressWarnings("rawtypes")
	public static String getPostOnly(Map map){
		
//		if("root".equals(AuthorityUtils.getSecurityUser().getUsername())){
//			return "root";
//		}else
			
			if(null != map.get("ZBCPOA") && !"".equals((String)map.get("ZBCPOA"))){//准备层POA
			return "ZBCPOA";
		}else if(null != map.get("JIHUAY") && !"".equals((String)map.get("JIHUAY"))){//计划员
			return "JIHUAY";
		}else if(null != map.get("WULGYY") && !"".equals((String)map.get("WULGYY"))){//物流工艺员
			return "WULGYY";
		}else if(null != map.get("ZXCPOA") && !"".equals((String)map.get("ZXCPOA"))){//执行层POA
			return "ZXCPOA";
		}else if(null != map.get("ZHIJIA") && !"".equals((String)map.get("ZHIJIA"))){//质检员
			return "ZHIJIA";
		}else if(null != map.get("WULIUY") && !"".equals((String)map.get("WULIUY"))){//物流员
			return "WULIUY";
		}else if(null != map.get("PCJIHY") && !"".equals((String)map.get("PCJIHY"))){//排产计划员
			return "PCJIHY";
		}else if(null != map.get("YCJIHY") && !"".equals((String)map.get("YCJIHY"))){//要车计划员
			return "YCJIHY";
		}else if(null != map.get("PZJIHY") && !"".equals((String)map.get("PZJIHY"))){//配载计划员
			return "PZJIHY";
		}else if(null != map.get("CK_001") && !"".equals((String)map.get("CK_001"))){//仓库计划员
			return "CK_001";
		}else if(null != map.get("CK_002") && !"".equals((String)map.get("CK_002"))){//客户计划员
			return "CK_002";
		}else if(null != map.get("CK_003") && !"".equals((String)map.get("CK_003"))){//容器场计划员
			return "CK_003";
		}else if(null != map.get("QUYGLY") && !"".equals((String)map.get("QUYGLY"))){//配载计划员
			return "QUYGLY";
		}else if(null != map.get("CHACGLY") && !"".equals((String)map.get("CHACGLY"))){//配载计划员
			return "CHACGLY";
		}else if(null != map.get("QUHYFJHY") && !"".equals((String)map.get("QUHYFJHY"))){//配载计划员
			return "QUHYFJHY";
		}else {
			return "root";
		}
	}
	/**
	 * 获取当前用户的业务编号的集合
	 * @param map
	 * @return
	 */
	public static String getPostAll(Map<String,String> map){
		
		
		//优化方法
//		StringBuffer keys=new StringBuffer();
//		Set<String>  set=map.keySet();
//		for (String s : set) {
//			if(keys.length()!=0){
//				keys.append(",");
//			}
//			keys.append(s);
//		}
//		System.out.println(keys);
		
		
		String post = "";
		if(null != map.get("ZBCPOA") && !"".equals((String)map.get("ZBCPOA"))){//准备层POA
			post+= "ZBCPOA,";
		}else if(null != map.get("ZXCPOA") && !"".equals((String)map.get("ZXCPOA"))){//执行层POA
			post+= "ZXCPOA,";
		}	
		if(null != map.get("WULGYY") && !"".equals((String)map.get("WULGYY"))){//物流工艺员
			post+= "WULGYY,";
		}
		if(null != map.get("JIHUAY") && !"".equals((String)map.get("JIHUAY"))){//计划员
			post+= "JIHUAY,";
		}
		if(null != map.get("ZHIJIA") && !"".equals((String)map.get("ZHIJIA"))){//质检员
			post+= "ZHIJIA,";
		}
		if(null != map.get("WULIUY") && !"".equals((String)map.get("WULIUY"))){//物流员
			post+= "WULIUY,";
		}
		if(null != map.get("PCJIHY") && !"".equals((String)map.get("PCJIHY"))){//排产计划员
			post+= "PCJIHY,";
		}
		if(null != map.get("YCJIHY") && !"".equals((String)map.get("YCJIHY"))){//要车计划员
			post+= "YCJIHY,";
		}
		if(null != map.get("PZJIHY") && !"".equals((String)map.get("PZJIHY"))){//配载计划员
			post+= "PZJIHY,";
		}
		
		if(null != map.get("CK_001") && !"".equals((String)map.get("CK_001"))){//仓库计划员
			post+= "CK_001,";
		}
		if(null != map.get("CK_002") && !"".equals((String)map.get("CK_002"))){//客户计划员
			post+= "CK_002,";
		}
		if(null != map.get("CK_003") && !"".equals((String)map.get("CK_003"))){//容器场计划员
			post+= "CK_003,";
		}
		
		if(null != map.get("QUYGLY") && !"".equals((String)map.get("QUYGLY"))){//配载计划员
			post+= "QUYGLY,";
		}
		if(null != map.get("CHACGLY") && !"".equals((String)map.get("CHACGLY"))){//配载计划员
			post+= "CHACGLY,";
		}
		if("".equals(post)){
			post = "root";
		}
		return post;
	}
	
	public static void checkqhqx(String usercenter) {
		Map<String,String> map = AuthorityUtils.getSecurityUser().getPostAndRoleMap();//一个人可对应多个业务角色
		String key = GetPostOnly.getPostOnly(map);
		if(!"root".equals(key)){	
			if(0 > key.indexOf("QUHYFJHY") ){
				if(map.get("QUHYFJHY")=="" || null == map.get("QUHYFJHY")){
				 throw new ServiceException("您没有取货运费计划员权限，不能操作数据");	
				}else if(StringUtils.isNotBlank(usercenter)){
					List<String> uclist=(List) AuthorityUtils.getSecurityUser().getDicCodeUcMap().get("QUHYFJHY");
					int flag=0;
					for (String object : uclist) {
						if(object.contains(usercenter)){
							flag=1;
						}			
					}
					if(flag==0){
						 throw new ServiceException("取货运费计划员没有用户中心为"+usercenter+"的权限");	
					}
				}
			}
		}
	}
}
