package com.athena.fj.module.action;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.excore.template.DownLoadServices;
import com.athena.excore.template.export.ExportConstants;
import com.athena.fj.entity.CelInfo;
import com.athena.fj.entity.KehGysInfo;
import com.athena.fj.entity.YAOCMx;
import com.athena.fj.entity.YaoCJhZb;
import com.athena.fj.entity.YssInfo;
import com.athena.fj.module.common.BillNumUtil;
import com.athena.fj.module.common.CollectionUtil;
import com.athena.fj.module.service.FJScheduleService;
import com.athena.fj.module.service.FenPeiYaoCeJhService;
import com.athena.fj.module.service.YaocjhService;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.date.DateUtil;
import com.athena.util.exception.ActionException;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
import com.toft.mvc.dispacher.ActionContext;
import com.toft.mvc.interceptor.supports.log.Log;

/**
 * @author 王冲
 * @email jonw@sina.com
 * @date 2012-2-20
 * @time 下午01:39:30
 * @description 分配要车计划
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class FenPeiYaoCeJHAction extends ActionSupport 
{
	/* description:   分配要车计划service*/
	@Inject
	private FenPeiYaoCeJhService fenPeiYaoCeJhService = null;
	@Inject
	private FJScheduleService fJScheduleService;
	/* description:   公共类*/
	@Inject
	private BillNumUtil billNumUtil = null;
	@Inject
	private YaocjhService yaocjhService = null;
	LoginUser user = AuthorityUtils.getSecurityUser();
	@Inject
	private DownLoadServices downLoadServices = null;
	
	@Inject
	private UserOperLog userOperLog;
	LoginUser loginUser = AuthorityUtils.getSecurityUser(); 
	/**
	 * 目标页面
	 * @author 贺志国
	 * @date 2011-12-21
	 * @param bean 要车计划bean
	 * @return String 返回ajax
	 */
	@Log(description="queryYaocjhZb",content="{Toft_SessionKey_UserData.userName}执行了：查询操作 ",
			whenException="{Toft_SessionKey_UserData.userName}执行了：查询操作出现异常")
	public String queryYaocjhZbForJsp(){ 
		return "selectZb";
	}
	
	/**
	 * 调用webServiece执行要车计算
	 * @author 贺志国
	 * @date 2012-4-10
	 * @return
	 * @throws UnknownHostException 
	 */
	/*public String caculateYaocjh() throws UnknownHostException{
		InetAddress inet = InetAddress.getLocalHost();
		//String localAddress = "http://"+inet.getHostAddress()+":8085/athena/services/yaocjhService";

		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean ();
		factory.setServiceClass(IYaoHLJhService.class); 
		//factory.setAddress(localAddress);
		factory.setAddress("/yaocjhService");
		try{               
				IYaoHLJhService hs=  (IYaoHLJhService) factory.create();
		        hs.createYaoHLJhMx(); 
		   } catch (Exception e) {
				throw new ActionException(e.getMessage()); 
			}   
		   //消息
			Map<String, String> message = new HashMap<String, String>();
			message.put("message", "webService自动调度要车计算完成");
			setResult("result",message);
		   return AJAX;
	}   */
	
	
	/**
	 * 调用yaocjhService的方法执行要车计算
	 * @author 贺志国
	 * @date 2012-4-11
	 * @return AJAX
	 */
	public String  transferCaculateYaocjh(){
		try{  
			yaocjhService.setEditor(loginUser.getUsername());
			yaocjhService.createYaoHLJhMx();
			userOperLog.addCorrect(CommonUtil.MODULE_FJ,"分配要车计划","执行要车计算,用户点击计算要车计划按钮,调用要车计划计算结束");
			Map<String, String> message = new HashMap<String, String>();
			message.put("message", "程序调用要车计算完成");
			setResult("result",message);
		 } catch (Exception e) {
			 userOperLog.addError(CommonUtil.MODULE_FJ, "分配要车计划", "分配要车计划出错，执行要车计算", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			 throw new ActionException(e.getMessage()); 
		}   
		   //消息
		return AJAX;
	}
	
	
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-20
	 * @time 下午02:22:18
	 * @param yaoCJhZb
	 * @return String 
	 * @description  要车计划总表 BY 用户中心
	 */
	@Log(description="queryYaocjhZb",content="{Toft_SessionKey_UserData.userName}执行了：查询操作 ",
			whenException="{Toft_SessionKey_UserData.userName}执行了：查询操作出现异常")
		public String queryYaocjhZbForAjax(@Param YaoCJhZb yao ){ 
		try {
			LoginUser loginUser = AuthorityUtils.getSecurityUser(); 
			Map<String, String> params = this.getParams();
			params.put("UC", loginUser.getUsercenter());
			setResult("result", fenPeiYaoCeJhService.selectYaoCeJhZb(yao,params));
		} catch (Exception e) {
			throw new ActionException(e.getMessage()); 
		}
		return  AJAX;
	}
	
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-20
	 * @time 下午02:22:18
	 * @param yaoCJhZb
	 * @description  查询车辆明细,  by 要车计划序号 用户中心
	 */
	@Log(description="queryYaocjhCelMx",content="{Toft_SessionKey_UserData.userName}执行了：查询操作 ",
					whenException="{Toft_SessionKey_UserData.userName}执行了：查询操作出现异常")
	public String queryYaocjhCelMx(@Param YAOCMx yao)
	{
		try {
			//参数
			Map<String, String> params = new HashMap<String, String>();
			//用户中心
			params.put("UC", this.getParam("USERCENTER"));
			//要车计划序号
			params.put("YAOCJHXH",this.getParam("YAOCJHXH"));
			
			//车辆明细
			Map<String,Object> map = fenPeiYaoCeJhService.selectYaocjhCelMx(yao,params);
			setResult("result", map);
			
		} catch (Exception e) {
			throw new ActionException(e.getMessage()); 
		}
		return AJAX;
	}
	
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-20
	 * @time 下午02:22:18
	 * @param yaoCJhZb
	 * @description  查询 所有要车明细 by 要车计划序号 用户中心
	 */
	@Log(description="selectYaoCelMxAll",content="{Toft_SessionKey_UserData.userName}执行了：查询操作 ",
					whenException="{Toft_SessionKey_UserData.userName}执行了：查询操作出现异常")
	public String selectYaoCelMxAll(@Param YAOCMx yao)
	{
		try {
			//参数
			Map<String, String> params = new HashMap<String, String>();
			//用户中心
			params.put("UC", this.getParam("USERCENTER"));
			//要车计划序号
			params.put("YAOCJHXH",this.getParam("YAOCJHXH"));
			//要车明细
			Map<String,Object> map = 	fenPeiYaoCeJhService.selectYaoCelMxAll(yao, params);
			setResult("result", map);
			
		} catch (Exception e) {
			throw new ActionException(e.getMessage()); 
		}
		return AJAX;
	}
	
	
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-20
	 * @time 下午02:25:05
	 * @return String
	 * @description  转页要车计划详细页面
	 */
	@Log(description="toPageYaocjhMx",content="{Toft_SessionKey_UserData.userName}执行了：查询操作 ",
			whenException="{Toft_SessionKey_UserData.userName}执行了：查询操作出现异常")
	public String toPageYaocjhMx(){ 
		
		
		setResult("YAOCJHXH",this.getParam("YAOCJHXH"));
		setResult("USERCENTER",this.getParam("USERCENTER"));
		
		return "selectMx";
	}
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-20
	 * @time 下午02:25:05
	 * @return String
	 * @description 查询尚未分配的要车明细
	 */
	@Log(description = "selectYaocjhMx", content = "{Toft_SessionKey_UserData.userName}执行了：查询操作 ", whenException = "{Toft_SessionKey_UserData.userName}执行了：查询操作出现异常")
	public String selectYaocjhMx(@Param YAOCMx yao) {
		try {
			// 参数
			Map<String, String> params = new HashMap<String, String>();
			// 用户中心
			params.put("UC", user.getUsercenter());
			// 要车计划序号
			params.put("YAOCJHXH", this.getParam("YAOCJHXH"));
			// 要车明细
			List<Map<String, String>> list = fenPeiYaoCeJhService.selectYaoCelMxAllNotAssign(yao, params);
			Map<String, Object> map =  new HashMap<String, Object>();
			map.put("total", list.size());
			map.put("rows", list);
			setResult("result", map);
		}
		catch (Exception e) {
			throw new ActionException(e.getMessage());
		}
		return AJAX;
	}
	
	
	/**
	 * 参考系运输商集合
	 * @date 2012-4-1
	 * @author hzg
	 * @return AJAX
	 */
	public String selectYunss(){
		try {
			String  yaocjhxh = this.getParam("YAOCJHXH");
			Map<String,Object> mapObj = new HashMap<String, Object>();
			Map<String,String> mapParam = new HashMap<String,String>();
			mapParam.put("usercenter", user.getUsercenter());
			mapParam.put("yaocjhxh", yaocjhxh);
			String yunslxbhs = CollectionUtil.listToString(fenPeiYaoCeJhService.getYunslxOfYaocmx(mapParam));
			mapParam.put("yunslxbhs", yunslxbhs);
			
			List<Map<String,String>> listYunss = fenPeiYaoCeJhService.selectYunss(mapParam);
			mapObj.put("total",listYunss.size());
			mapObj.put("rows", listYunss);
			setResult("result",mapObj);
		}catch (Exception e) {
				throw new ActionException(e.getMessage());
			}
			return AJAX;
	}
	
	
	/**
	 * 运输商对应的车辆明细查询
	 * @author 贺志国
	 * @date 2012-4-1
	 * @return
	 */
	public String selectYunssMx(){
		try {
		Map<String,Object> mapObj = new HashMap<String, Object>();
		Map<String,String> mapParam = this.getParams();
		mapParam.put("UC", user.getUsercenter());
		List<Map<String,String>> listYunssmx = fenPeiYaoCeJhService.selectYunssMx(mapParam);
		mapObj.put("total",listYunssmx.size());
		mapObj.put("rows", listYunssmx);
		setResult("result",mapObj);
	}catch (Exception e) {
			throw new ActionException(e.getMessage());
		}
		return AJAX;
	}
	

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-20
	 * @time 下午02:25:05
	 * @return String
	 * @description  查询车辆申报资源
	 */
	@Log(description="selectYaocjhYyS",content="{Toft_SessionKey_UserData.userName}执行了：查询操作 ",
			whenException="{Toft_SessionKey_UserData.userName}执行了：查询操作出现异常")
	public String selectYaocjhYyS(){ 
		try {
			//参数
			Map<String, String> params = new HashMap<String, String>();
			// 当前登录用户
			LoginUser loginUser = AuthorityUtils.getSecurityUser(); 
			// 用户中心
			params.put("UC", loginUser.getUsercenter());
//			params.put("UC", "UW");
			// 要车计划序号
			params.put("YAOCJHXH", this.getParam("YAOCJHXH"));
//			params.put("YAOCJHXH", "2012-01-14");
			
			
//			List<Map<String,String>> yys = fenPeiYaoCeJhService.selectYaocjhYySByClSb(params);
//			//封装车辆资源
//			Map<String, YssInfo> map1 = this.warpYssInfo(yys) ;
//			setResult("result",map1);
//			
//			
//			//查询客户运输商关系
//			List<Map<String,String>>  keh_gys =  fenPeiYaoCeJhService.queryYaocjhKehGys(params) ;
//			//封装客户运输商
//			Map<String, KehGysInfo> map2 = warpKehGys(keh_gys) ;
//			setResult("kehGys",map2);
			List<Map<String,String>> cysList  = this.fenPeiYaoCeJhService.selectLxzByCys(params); 
			Map<String,Set<String>> cyslxz = this.fenPeiYaoCeJhService.wrapLxzByCys(cysList) ;
			setResult("cysLxz",cyslxz);
			
			
		} catch (Exception e) {
			throw new ActionException(e.getMessage()); 
		}
		return AJAX;
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-2
	 * @time 上午11:57:23
	 * @param keh_gys
	 * @return Map<String, KehGysInfo>
	 * @description 封装客户运输商
	 */
	@SuppressWarnings("unused")
	private Map<String, KehGysInfo> warpKehGys(List<Map<String, String>> keh_gys) {

		Map<String, KehGysInfo> tmp = new HashMap<String, KehGysInfo>();
		for (Map<String, String> map : keh_gys) {
			if (tmp.containsKey(map.get("GCBH"))) {
				tmp.get(map.get("GCBH")).getKeh().add(map.get("KEHBH"));
			}
			else
			{
				KehGysInfo info = new KehGysInfo();
				info.setGys(map.get("GCBH"));
				info.getKeh().add(map.get("KEHBH")) ;
				tmp.put(map.get("GCBH"), info);
			}
		}
		
		return tmp;
		
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-2
	 * @time 上午11:32:58
	 * @param yys
	 * @return List<YssInfo>
	 * @description  封装车辆资源
	 */
	@SuppressWarnings("unused")
	private Map<String, YssInfo> warpYssInfo(List<Map<String, String>> yys) {
		Map<String, YssInfo> tmp = new HashMap<String, YssInfo>();
		for (Map<String, String> map : yys) {
			if (tmp.containsKey(map.get("YUNSSBM"))) {
				YssInfo info =  tmp.get(map.get("YUNSSBM"));
				info.getCex().add(map.get("CHEX")) ;
				info.setCxmc(setMap(info.getCxmc(), map.get("CHEX"),
								map.get("CHEXMC"),
								Integer.parseInt(map.get("SHUL"))));
				
			}

			else {
				YssInfo info = new YssInfo();
				info.setGysMc(map.get("GONGSMC"));
				info.setGysbh(map.get("YUNSSBM"));
				info.getCex().add(map.get("CHEX")) ;
				info.setCxmc(setMap(info.getCxmc(), map.get("CHEX"),
								map.get("CHEXMC"),
								Integer.parseInt(map.get("SHUL"))));
				tmp.put(map.get("YUNSSBM"), info);
			}
			
		}
		return tmp;

	}
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-2
	 * @time 上午11:48:16 
	 * @param map 车辆
	 * @param cx 车辆型号
	 * @param mc 车辆名称
	 * @param sl 车辆数量
	 * @description   封装车辆资源
	 */
	private Map<String ,CelInfo>  setMap(Map<String ,CelInfo> map,String cx,String mc,int sl)
	{
		
		if(map.containsKey(cx))
		{
			CelInfo info = map.get(cx) ;
			info.setCxsl(info.getCxsl()+sl) ;
		}
		else
		{
			CelInfo info = new CelInfo();
			info.setCxbh(cx) ;
			info.setCxmc(mc);
			info.setCxsl(sl);
			map.put(cx, info);
		}
		return map;
	}
	
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-20
	 * @time 下午02:31:23
	 * @return
	 * @description  更新要车明细
	 */
	@SuppressWarnings("finally")
	public String updateYaoCMx( @Param("list") ArrayList<YAOCMx> list,@Param("YAOCJHXH") String yaocjhxh )
	{
		//消息
		Map<String, String> message = new HashMap<String, String>();
		
		try {
			userOperLog.addCorrect(CommonUtil.MODULE_FJ,"分配要车计划","更新要车明细开始");
			// 当前登录用户
			LoginUser loginUser = AuthorityUtils.getSecurityUser(); 
			
			//参数
			Map<String, String> params = new HashMap<String, String>();

			//用户中心
			params.put("UC",loginUser.getUsercenter());
			//申报时间
			params.put("YAOCJHXH", yaocjhxh);
			
			//得到所有的要车计划明细
			 Map<String ,Integer> yaocjh = new HashMap<String, Integer>();
			//要车计划号
			 Map<String ,String> jhh = new HashMap<String, String>();
			 
			 if(list.size()==0){
				 throw new Exception("您所选的要车明细已分配，数据未改变，请刷新");
			 }
			 for(YAOCMx p:list)
				{
				  this.warpYAOCJH(yaocjh, p.getGCBH()) ;
				}
			 
			 //写入要车计划
			 for(Map.Entry<String, Integer> jh:yaocjh.entrySet()){
					//生成要车计划号
					String YAOCJHH = billNumUtil.createDJNum(loginUser.getUsercenter(), 6, BillNumUtil.BILL_YCJHH,null) ;
					//新增要车计划
					Map<String,String> yaocjhh = new HashMap<String, String>() ;
					yaocjhh.put("YAOCJHH",YAOCJHH) ;
					yaocjhh.put("USERCENTER",loginUser.getUsercenter()) ;
					yaocjhh.put("SHIFQR", "1") ;
					yaocjhh.put("ZONGCC", String.valueOf(jh.getValue().intValue())) ;
					yaocjhh.put("YUNSSBM", jh.getKey()) ;
					yaocjhh.put("KAISSJ",yaocjhxh ) ;
					yaocjhh.put("JIESSJ",yaocjhxh ) ;
					yaocjhh.put("CREATOR", loginUser.getUsername()) ;
					yaocjhh.put("CREATE_TIME", DateUtil.curDateTime()) ;
					fenPeiYaoCeJhService.insertYAOCJH(yaocjhh) ;
					//记录此要车计划的要车计划号
					jhh.put(jh.getKey(), YAOCJHH);
			 }
		
			
	
				//写入配载计划明细与要贷令明细
				for(YAOCMx p:list)
				{
					String yaocjhh = checkYaocmxShiffp(p.getID());
					if(yaocjhh!=null){
						throw new Exception("您所选的要车明细存在已分配的，请返回或刷新后重新选择");
					}
					//2.新增配载计划
					Map<String,String> mapParams = new HashMap<String, String>() ;
					mapParams.put("ID",p.getID()) ;
					mapParams.put("YUNSSBM", p.getGCBH()) ;
					mapParams.put("JIHZT", "0") ;
					mapParams.put("UC", loginUser.getUsercenter()) ;
					mapParams.put("YAOCJHH", jhh.get(p.getGCBH())) ;
					mapParams.put("CREATOR", loginUser.getUsername()) ;
					fenPeiYaoCeJhService.insertPeiZJH(mapParams) ;
					
					//3.生成要贷令明细
					this.fenPeiYaoCeJhService.insertYaoHLMX(mapParams) ;
					
					//更新要车明细中的要车计划号
					 fenPeiYaoCeJhService.updateYaoCMx(mapParams);
					
				}
			
			
			//判断要车计划总表的车辆是否全部用完
			List<Map<String,String>> zb = this.fenPeiYaoCeJhService.checkYaoCelJhZbIsComplete(params);
			//如果要车计划总表完成
			if(zb.get(0).get("ZCC").equalsIgnoreCase("0"))
			{
				this.fenPeiYaoCeJhService.updateYaoCZB(params);
			}
			userOperLog.addCorrect(CommonUtil.MODULE_FJ,"分配要车计划","分配要车明细结束");
			 message.put("message", "分配成功");
			 message.put("success", "true");
			 
		} catch (Exception e) {
			userOperLog.addError(CommonUtil.MODULE_FJ, "分配要车计划", "更新要车明细出错", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			message.put("message", e.getMessage());
			message.put("success", "false");
			throw new ActionException(e.getMessage()); 
			
		} finally{
			this.setResult("result", message) ;
			return AJAX;
			
		}
		
	}
	
	/**
	 * 
	 * @param yaocjh 要车计划
	 * @param gcbh 运输商编号
	 * @description 归集要车计划
	 */
	public void warpYAOCJH(Map<String ,Integer> yaocjh,String gcbh){
		if(yaocjh.containsKey(gcbh)){
			yaocjh.put(gcbh, yaocjh.get(gcbh)+1) ;
		}
		else{
			yaocjh.put(gcbh, 1) ;
		}
	}
	
	
	/**
	 * 检查要车明细表是否分配
	 * @author 贺志国
	 * @date 2012-5-3
	 * @param yaocmxID 要车明细号
	 * @return 要车计划号
	 */
	public String checkYaocmxShiffp(String yaocmxID){
		Map<String,String> param = new HashMap<String, String>();
		param.put("usercenter", user.getUsercenter());
		param.put("id", yaocmxID);
		return fenPeiYaoCeJhService.checkYaocmxShiffp(param);
	}
	
	
	
	/**
	 * 删除要车明细
	 * @author 贺志国
	 * @date 2012-4-5
	 * @param list 要车明细ID，要车计划序号
	 * @return AJAX
	 */
	public String deleteYaoCMx(@Param("list") ArrayList<YAOCMx> list,@Param("YAOCJHXH") String yaocjhxh){
		try{
			//消息
			Map<String, String> message = new HashMap<String, String>();
			Map<String,String> params = new HashMap<String, String>() ;
			StringBuffer buffer = new StringBuffer();
			String flag = "";
			for(YAOCMx ycmx : list){
				buffer.append(flag).append("'").append(ycmx.getID()).append("'");
				flag=",";
			}
			String IDs = buffer.toString();
			params.put("UC", user.getUsercenter());
			params.put("IDs",IDs);
			params.put("YAOCJHXH", yaocjhxh);
			this.fenPeiYaoCeJhService.deleteYaoCMxBatch(params,list);

			//判断要车计划总表的车辆是否全部用完
			List<Map<String,String>> zb = this.fenPeiYaoCeJhService.checkYaoCelJhZbIsComplete(params);
			//如果要车计划总表完成
			if(zb.get(0).get("ZCC").equalsIgnoreCase("0"))
			{
				this.fenPeiYaoCeJhService.updateYaoCZB(params);
			}
			userOperLog.addCorrect(CommonUtil.MODULE_FJ,"删除要车明细","删除要车明细结束");
			message.put("message", "删除成功");
			this.setResult("result", message) ;
		}catch (Exception e) {
			userOperLog.addError(CommonUtil.MODULE_FJ, "删除要车明细", "删除要车明细出错", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			throw new ActionException(e.getMessage()); 

		} 
		return AJAX;
	}
	
	
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-22
	 * @time 下午01:30:24
	 * @return  String
	 * @description  重新分配
	 */
	@Log(description="caculateForFenPei",content="{Toft_SessionKey_UserData.userName}执行了：查询操作 ",
					whenException="{Toft_SessionKey_UserData.userName}执行了：重新分配操作出现异常")
	public String caculateForFenPei()
	{
		try { 
			// 参数
			Map<String, String> params = new HashMap<String, String>();
			// 用户中心
			params.put("UC", this.getParam("USERCENTER"));
			//要车计划号
			params.put("YAOCJHXH", this.getParam("YAOCJHXH"));
			
			if(!fenPeiYaoCeJhService.checkPeizjhzt(params)){
				setResult("msgs","要车计划下配载单已开始执行，不允许重新分配");
				return "selectZb";
			}
			//删除0204要货令明细
			this.fenPeiYaoCeJhService.deleteYaohlmx(params) ;
			//删除0203配载计划 
			this.fenPeiYaoCeJhService.deletePeiZaiJh(params) ;
			//删除0201要车计划
			this.fenPeiYaoCeJhService.deleteYaoCeJh(params) ;
			//置空0202要车明细表的要车序号
			this.fenPeiYaoCeJhService.updateYaoCeMxOfYaoCJHHToNull(params) ;
			userOperLog.addCorrect(CommonUtil.MODULE_FJ,"重新分配要车计划","重新分配要车计划结束");
			//带入要车计划序号
			String tmp = this.getParam("YAOCJHXH") ;
			setResult("YAOCJHXH",tmp);
		}catch (Exception e) {
			userOperLog.addError(CommonUtil.MODULE_FJ, "重新分配要车计划", "重新分配要车计划出错", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			throw new ActionException(e.getMessage()); 
		}
		return "selectMx";
	}
	
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-22
	 * @time 下午03:21:00
	 * @return String
	 * @description  重新计算
	 */
	@Log(description="caculateFotJiShuan",content="{Toft_SessionKey_UserData.userName}执行了：查询操作 ",
					whenException="{Toft_SessionKey_UserData.userName}执行了：重新计算操作出现异常")
	public String caculateFotJiShuan()
	{
		try { 
			//消息
			Map<String, String> message = new HashMap<String, String>();
			// 参数
			Map<String, String> params = new HashMap<String, String>();
			// 用户中心
			params.put("UC", user.getUsercenter());
			//要车计划号
			params.put("YAOCJHXH", this.getParam("YAOCJHXH"));
			
			if(!fenPeiYaoCeJhService.checkPeizjhzt(params)){
				message.put("message", "要车计划下配载单已开始执行，不允许重新计算");
				setResult("result", message);
				return AJAX;
			}
			
			fJScheduleService.scheduleRun("");
			//解除锁定配载
			this.fenPeiYaoCeJhService.updateYaoHL(params) ;
			//清空计划要贷令
			this.fenPeiYaoCeJhService.deleteJhYaoL(params) ;
			//清空要车明细
			this.fenPeiYaoCeJhService.deleteYaoCmx(params) ;
			//清空车辆明细
			this.fenPeiYaoCeJhService.deleteCelMx(params) ;
			//清空要车计划总表
			this.fenPeiYaoCeJhService.deleteJHZb(params) ;
			
			//重新计算
			yaocjhService.shengCYaoCJHmx(user.getUsercenter(), this.getParam("YAOCJHXH")+" 23:59:59") ;
			
			
			//清空车辆明细
			this.fenPeiYaoCeJhService.deleteCelMx(params) ;
			//清空要车计划总表
			this.fenPeiYaoCeJhService.deleteJHZb(params) ;
			
			//统计车名称
			List<Map<String, String>> selectCelXx = this.fenPeiYaoCeJhService.selectCelXx(params) ;
			//新增要车计划
			int sl = 0;
			Set<String> set = new HashSet<String>();
			for(Map<String, String> map:selectCelXx)
			{
				set.add(map.get("CHEXMC")) ;
				 sl+=Integer.parseInt(map.get("SL")) ;
			}
			this.yaocjhService.addYaoCJHZb(params.get("YAOCJHXH"), sl, set, user.getUsercenter()) ;
			
			//新增车辆明细
			Map<String, Integer> cl = new HashMap<String, Integer>();
			for(Map<String, String> map:selectCelXx)
			{
				cl.put(map.get("JIHCX"), Integer.parseInt(map.get("SL")) );
			}
			this.yaocjhService.addCLMx(params.get("YAOCJHXH"), cl, user.getUsercenter()) ;
			userOperLog.addCorrect(CommonUtil.MODULE_FJ,"重新计算要车计划","重新计算要车计划结束");
			//带入要车计划序号
			String tmp = params.get("YAOCJHXH");
			message.put("message", "重计算成功");
			setResult("YAOCJHXH",tmp);
			setResult("result", message) ;
			
		}catch (Exception e) {
			userOperLog.addError(CommonUtil.MODULE_FJ, "重新计算要车计划", "重新计算要车计划出错", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			throw new ActionException(e.getMessage()); 
		}
		return AJAX;
	}
	
	
	public String downLoadFile(){
		// 参数
		Map<String, String> params = new HashMap<String, String>();
		// 用户中心
		params.put("UC", this.getParam("USERCENTER"));
		//要车计划号
		params.put("YAOCJHXH", this.getParam("YAOCJHXH"));
		
		List<Map<String, String>> list = this.fenPeiYaoCeJhService.selectYaoCMXForDownLoad( params) ;
		
		Map<String, Object> dataSource;
		dataSource = new HashMap<String, Object>();
		dataSource.put("YAOCJHXH",this.getParam("YAOCJHXH"));
		dataSource.put("ycjh", list);
		HttpServletResponse response = ActionContext.getActionContext().getResponse() ;
		try{
		downLoadServices.downloadFile("ycjh.ftl", dataSource, response, "要车计划", ExportConstants.FILE_XLS, false);
		}
		catch(ServiceException e){
			HttpServletRequest request = ActionContext.getActionContext().getRequest();
			request.setAttribute("message",e);
			return "error";
		}  
		return "downLoad";
		
	}
}
