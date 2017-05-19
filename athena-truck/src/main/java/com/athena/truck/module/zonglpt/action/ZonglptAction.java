package com.athena.truck.module.zonglpt.action;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.component.service.Message;
import com.athena.truck.entity.Chac;
import com.athena.truck.entity.Chew;
import com.athena.truck.entity.Dengdqy;
import com.athena.truck.entity.Yund;
import com.athena.truck.entity.Zonglpt;
import com.athena.truck.entity.Zonglptmx;
import com.athena.truck.module.kcckx.service.ChacService;
import com.athena.truck.module.yundgz.service.YundgzService;
import com.athena.truck.module.zonglpt.service.ZonglptService;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;


/**
 * 叉车-车位
 * @author liushaung
 * @date 2015-1-20
 */

@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class ZonglptAction extends ActionSupport{

	/**
	 * 用户级操作日志
	 * @author liushaung
	 * @date 2015-1-20
	 * @return bean
	 */
	@Inject
	private UserOperLog userOperLog;

	/**
	 * 注入chacService
	 * @author liushaung
	 * @date 2015-1-20
	 * @return bean
	 */
	@Inject
	private YundgzService yundgzService;
	
	@Inject
	private ZonglptService zonglptService;
	
	/**
	 * 获取当前用户信息
	 * @author liushaung
	 * @date 2015-1-20
	 * @return LoginUser
	 */
	public LoginUser getLoginUser() {
		return AuthorityUtils.getSecurityUser();
	}
	
	
	/**
	 * 跳转到初始页面
	 * @author liushaung
	 * @date 2015-1-20
	 * @return String
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 */
	public String execute() throws InstantiationException, IllegalAccessException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException{
		//先查询出循环区域    查询下面的grid中的一些需要显示 大站台编号 和 车辆数
		Map<String,String> mapPara = new HashMap<String, String>();
		Map<String,String> postMap = getLoginUser().getPostAndRoleMap();
		String quygly = postMap.get("QUYGLY")==null ? "":postMap.get("QUYGLY");
		mapPara.put("post_code", quygly);
		mapPara.put("usercenter", getLoginUser().getUsercenter());
		//Dengdqy bean = new Dengdqy();
		//bean.setUsercenter();
		List<Dengdqy> listquymc = zonglptService.listMc(mapPara);
		List<Zonglpt> zonglpt = new ArrayList<Zonglpt>();
		//循环 等待区域表  跟据区域编号 到运单表中找有多少个大站台
		for (Dengdqy dengdqy : listquymc) {
			Map map = new HashMap();
			map.put("usercenter", getLoginUser().getUsercenter());
			map.put("quybh", dengdqy.getQuybh());
			List<Yund> listdazt =zonglptService.listMcinner(map);
			//查询对于没有大站台的不在下面的列表中显示出来
			if(listdazt.size()>0){
			//根据查询出来的大站台去看有多少申报的车辆
				Zonglpt pt = new Zonglpt();
				for (Yund Yund : listdazt) {
					Zonglptmx ptmx = new Zonglptmx();
					Zonglptmx obj = null;
						ptmx.setDazt(Yund.getDaztbh());
						pt.setQuymc(Yund.getQuymc());
						pt.setQuybh(Yund.getQuybh());
						//在到车辆排队表中根据  用户中心\区域编号\大站台编号\排队状态去查询 排队车辆 和  分配车辆 和 车位数
						ptmx.setShenbkcs(Yund.getSb());
						ptmx.setPaidkcs(Yund.getPd());
						ptmx.setFangkkcs(Yund.getFk());
						ptmx.setJingjkcs(Yund.getJj());
						if(null!=Yund.getCw()){

							//根据SQL中查询出来的CW 来切割 车位数 和 车位的状态
							Class ptm;
							try {
								ptm = Class.forName("com.athena.truck.entity.Zonglptmx");
								obj = (Zonglptmx)ptm.newInstance();
								obj.setDazt(Yund.getDaztbh());
								obj.setShenbkcs(Yund.getSb());
								obj.setPaidkcs(Yund.getPd());
								obj.setFangkkcs(Yund.getFk());
								obj.setJingjkcs(Yund.getJj());
								String[] str  =  Yund.getCw().split(",");
								for (int i = 0; i < str.length; i++) {
									if(i>=15){
										break;
									}
									String[] str2  =  str[i].split("_");
									for (int k = 0; k < str2.length; k++) {
										if(k==0){
											Method method1 = obj.getClass().getMethod("setChew"+i, String.class);
											method1.invoke(obj, str2[k]); 
										}else{
											Method method2 = obj.getClass().getMethod("setChewzt"+i, String.class);
											method2.invoke(obj, str2[k]);
										}
									} 
								}
							} catch (ClassNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							pt.getZonglptmx().add(obj);
						}else{
							pt.getZonglptmx().add(ptmx);
						}
				 }
				zonglpt.add(pt);
		  }
		}
		setResult("zonglpt",zonglpt);
		setResult("usercenter", getLoginUser().getUsercenter());
		return "select";
	}
	
	/**
	 * 分页查询叉车
	 * @param bean
     * @author liushaung
	 * @date 2015-1-20
	 * @return String
	 */
	public String queryZonglpt(){
		try {
			Dengdqy bean = new Dengdqy();
//			bean.setUsercenter(getLoginUser().getUsercenter());
//			List<Dengdqy> listquymc = zonglptService.listMc(bean);
//			List<Zonglptmx> zonglptlist = new ArrayList<Zonglptmx>();
//			//循环 等待区域表  跟据区域编号 到运单表中找有多少个大站台
//			for (Dengdqy dengdqy : listquymc) {
//				Zonglptmx pt = new Zonglptmx();
//				pt.setQuymcs(dengdqy.getQuymc());
//				Map map = new HashMap();
//				map.put("usercenter", getLoginUser().getUsercenter());
//				map.put("quybh", dengdqy.getQuybh());
//			
//			}
//			Map<String,Object> maprows = new HashMap<String, Object>();
//			maprows.put("total", zonglptlist.size());
//			maprows.put("rows", zonglptlist);
//			setResult("result",maprows);
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "总览平台", "数据查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "总览平台", "数据查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		return AJAX;
	}
	
	
	
	public String selectquybh(){
		Map map = getParams();
		List<Yund> dazt =zonglptService.listyunda(map);
		Map<String,Object> maprows = new HashMap<String, Object>();
		maprows.put("total", dazt.size());
		maprows.put("rows", dazt);
		setResult("result",maprows);
		//setResult("result",dazt);
		return AJAX;
	}
	
	
}
