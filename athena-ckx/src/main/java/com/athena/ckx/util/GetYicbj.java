package com.athena.ckx.util;



import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.xuqjs.Yicbj;


public class GetYicbj {
	
	public GetYicbj(){
		
	}
	/**
	 * 获取异常报警对象
	 * @param usercenter
	 * @param cuowlx
	 * @param jismk
	 * @param cuowxxxx
	 */
	public  Yicbj getYicbj(String usercenter,String cuowlx,String jismk,String lingjbh,String cuowxxxx,String jihyz){
		Yicbj bean = new Yicbj();
		LoginUser loginUser = AuthorityUtils.getSecurityUser() ;
		String userName = loginUser == null?usercenter+"root":loginUser.getUsername();
		bean.setUsercenter(usercenter) ;
		bean.setCreator(userName) ;
		bean.setCreate_time(DateTimeUtil.getAllCurrTime()) ;
		bean.setEditor(userName) ;
		bean.setEdit_time(bean.getCreate_time());
		bean.setCuowlx(cuowlx);
		bean.setJismk(jismk);
		bean.setJihyz(jihyz);
		bean.setJihydm(bean.getJihyz()) ;
		bean.setCuowxxxx(cuowxxxx);
		bean.setLingjbh(lingjbh);	
		return bean;
	}
}
