package com.athena.ckx.module.baob.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.util.CollectionUtils;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.baob.CollectionUtilTool;
import com.athena.ckx.entity.baob.JihuChengys;
import com.athena.ckx.entity.baob.Shisywyhl;
import com.athena.ckx.entity.baob.Yanwyhlconfig;
import com.athena.ckx.entity.xuqjs.Peislb;
import com.athena.ckx.module.baob.service.ShisywyhlConfigService;
import com.athena.ckx.module.baob.service.ShisywyhlService;
import com.athena.excore.template.DownLoadServices;
import com.athena.excore.template.export.ExportConstants;
import com.athena.util.CommonUtil;
import com.athena.util.exception.ActionException;
import com.toft.core2.ToftConst;
import com.toft.core2.UserInfo;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;
import com.toft.mvc.dispacher.ActionContext;

@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class ShisywyhlConfigAction extends ActionSupport {
	@Inject
	private ShisywyhlConfigService yhlService;
	@Inject
	private UserOperLog userOperLog;
	@Inject
	private DownLoadServices  downloadServices;		//数据导出
	/**
	 * 获取当前用户信息
	 * @return LoginUser
	 */
	public LoginUser getLoginUser() {
		return AuthorityUtils.getSecurityUser();
	}
	//11458
	/**
	 * 跳转到初始页面
	 * @return String
	 */
	public String execute(){
		String usercenter = getLoginUser().getUsercenter();
		
		List<HashMap<String, String>> factoryList = yhlService.queryFactoryByUsercebter(usercenter);
		setResult("fac",factoryList);
		userOperLog.addCorrect(CommonUtil.MODULE_CKX, "用户中心", "查询工厂");	
		
		setResult("USERCENTER", usercenter);	//登录人所在的用户中心
		return "select";
		/*
		try {
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "报表","根据用户中心查询工厂信息");
			String p = CollectionUtilTool.listToJson(yhlService.queryFactoryByUsercebter(usercenter), "KEY", "VALUE");
			setResult("factory", p);
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "报表","根据用户中心查询工厂信息", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		*/
	}
	
	/**
	 * 查询
	 * @param bean
	 * @author denggq
	 * @date 2012-1-16
	 * @return String
	 */
	public String query(@Param Yanwyhlconfig bean){
		try {
			String usercenter = getLoginUser().getUsercenter();
			bean.setUsercenter(getLoginUser().getUsercenter());
			//List<HashMap<String, String>> factoryList = yhlService.queryFactoryByUsercebter(usercenter);
			//String strFactory = CollectionUtilTool.listToJson(factoryList, "KEY", "VALUE");
			//String strFactory = new String();
			//for(int i=0;i<factoryList.size();i++)
			//{
			//	strFactory = strFactory.concat(factoryList.get(i).get("VALUE")).concat(",");
			//}
			//setResult("fac",strFactory.substring(0, strFactory.length()-1));
			//setResult("fac",factoryList);
			//userOperLog.addCorrect(CommonUtil.MODULE_CKX, "用户中心", "查询工厂");		
			setResult("result", yhlService.query(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "报表","实时延误要货令大屏配置查询");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "报表","实时延误要货令大屏配置查询", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		
		return AJAX;
	}

	/**
	 * 根据用户中心获取仓库
	 * @param bean
	 * @author denggq
	 * @date 2012-7-14
	 * @return String
	 */
	public String queryCangkByUsercenter(@Param("usercenter") String usercenter){
		try{
			if(usercenter==null||"".equals(usercenter)){
				usercenter=getLoginUser().getUsercenter();
			}
			setResult("result", yhlService.queryCangkByUsercenter(usercenter));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "零件", "查询仓库");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result", map);
			userOperLog.addError(CommonUtil.MODULE_CKX, "零件", "查询仓库", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	
	/**
	 * update
	 * @param bean
	 * @author huxy
	 * @date 2015-7-16
	 * @return String
	 */
	public String save(@Param Yanwyhlconfig bean){
		try {
			bean.setUsercenter(getLoginUser().getUsercenter());
			setResult("result", yhlService.save(bean));
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "报表","实时延误要货令大屏配置保存");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "报表","实时延误要货令大屏配置保存", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			setResult("result",map);
		}
		
		return AJAX;
	}
	
	
	/**
	 * 出错跳转到初始页面
	 * @return String
	 */
	public String showerror(){
		return "select";
	}
	
	/**
	 * 显示大屏
	 * @return String
	 */
	public String showscreen(){
		//setResult("result", this.getParam("putvalue"));
		String s = this.getParam("inputvalue");
		setResult("result",s);
		return "select";
	}
	
	public String showBaobiao()
	{
		/*
		int factoryNum;
		String factoryList[];
		Map<String,String> params = this.getParams(); 
		Yanwyhlconfig bean = new Yanwyhlconfig();
		bean.setUsercenter(params.get("usercenter"));
		bean.setCangkbh(params.get("cangkbh"));
		bean.setChanx(params.get("chanx"));
		bean.setChengysdm(params.get("chengysdm"));
		bean.setGongysdm(params.get("gongysdm"));
		bean.setJihy(params.get("jihy"));
		bean.setLingjbh(params.get("lingjbh"));
		String yaohllx = strToSql(params.get("yaohllx"),"yaohllx");
		String yaohlzt = strToSql(params.get("yaohlzt"),"yaohlzt");
		bean.setYaohllx(yaohllx);
		bean.setYaohlzt(yaohlzt);
		String x = params.get("_jihy");
		//按计划员归集
		if(params.get("_jihy").equals("1"))
		{
			ArrayList<String>  factoryName = new ArrayList<String>();
			factoryList = params.get("factory").split(" ");
			factoryNum  = factoryList.length;
			if(factoryNum == 1)
			{
				bean.setFactory(" and b.gongc = "+"'"+factoryList[0]+"' ");
				factoryName.add(yhlService.queryFactoryNameByBs(factoryList[0]).get(0).get("ZIDMC"));
				setResult("factoryList",factoryName.get(0));
			}else{
				bean.setFactory(" and b.gongc in ("+"'"+factoryList[0]+"' ,"+"'"+factoryList[1]+"')");
				factoryName.add(yhlService.queryFactoryNameByBs(factoryList[0]).get(0).get("ZIDMC"));
				factoryName.add(yhlService.queryFactoryNameByBs(factoryList[1]).get(0).get("ZIDMC"));
				//工厂名称 如"三厂，新能源"
				setResult("factoryList",factoryName.get(0)+","+factoryName.get(1));
			}
			List<JihuChengys> list = yhlService.getBaobiaoByJihy(bean);
			List<JihuChengys> list1 = new ArrayList<JihuChengys>();
			JihuChengys data = new JihuChengys();

			if(list.size() == 0)
			{
				setResult("result","");
			}else{
				data = list.get(0);
				//不相同 换个位置
				if(!data.getFactory_1().equals(factoryName.get(0)))
				{
					data.setFactory_2(data.getFactory_1());
					data.setNum_2(data.getNum_1());
					data.setFactory_1(factoryName.get(0));
					data.setNum_1(0);
				}
			}
			for(int j=1;j<list.size();j++)
			{
				//如果本条数据的计划员和上条的相同
				if(list.get(j).getJihy().equals(data.getJihy()))
				{
					if(list.get(j).getFactory_1().equals(factoryName.get(0)))
					{
						data.setFactory_1(list.get(j).getFactory_1());
						data.setNum_1(list.get(j).getNum_1());
					}else{
						data.setFactory_2(list.get(j).getFactory_1());
						data.setNum_2(list.get(j).getNum_1());
					}
				}else{
					list1.add(data);
					data = list.get(j);
					if(!data.getFactory_1().equals(factoryName.get(0)))
					{
						data.setFactory_2(data.getFactory_1());
						data.setNum_2(data.getNum_1());
						data.setFactory_1(factoryName.get(0));
						data.setNum_1(0);
					}
				}
			}
			list1.add(data);
			setResult("result", list1.get(0));
		}
		*/
		setResult("result",this.getParam("putvalue"));
		return "select";
	}
	
	@SuppressWarnings("unchecked")
	public String showBaobiaoTotal()
	{
	  try{	
		int factoryNum;
		String factoryList[];
		Map<String,String> params = this.getParams(); 
		Yanwyhlconfig bean = new Yanwyhlconfig();
		bean.setUsercenter(params.get("usercenter"));
		bean.setCangkbh(params.get("cangkbh"));
		bean.setChanx(params.get("chanx"));
		bean.setChengysdm(params.get("chengysdm"));
		bean.setGongysdm(params.get("gongysdm"));
		bean.setJihy(params.get("jihy"));
		bean.setLingjbh(params.get("lingjbh"));
		String yaohllx = strToSql(params.get("yaohllx"),"yaohllx");
		String yaohlzt = strToSql(params.get("yaohlzt"),"yaohlzt");
		bean.setYaohllx(yaohllx);
		bean.setYaohlzt(yaohlzt);
		String x = params.get("_jihy");
		//按计划员归集
		if(params.get("_jihy").equals("1"))
		{
			ArrayList<String>  factoryName = new ArrayList<String>();
			factoryList = params.get("factory").split(" ");
			factoryNum  = factoryList.length;
			if(factoryNum == 1)
			{
				bean.setFactory(" and b.gongc = "+"'"+factoryList[0]+"' ");
				factoryName.add(yhlService.queryFactoryNameByBs(factoryList[0]).get(0).get("ZIDMC"));
				setResult("factoryList",factoryName.get(0));
			}else{
				bean.setFactory(" and b.gongc in ("+"'"+factoryList[0]+"' ,"+"'"+factoryList[1]+"')");
				factoryName.add(yhlService.queryFactoryNameByBs(factoryList[0]).get(0).get("ZIDMC"));
				factoryName.add(yhlService.queryFactoryNameByBs(factoryList[1]).get(0).get("ZIDMC"));
				//工厂名称 如"三厂，新能源"
				setResult("factoryList",factoryName.get(0)+","+factoryName.get(1));
			}
			bean.setCurrentPage(Integer.parseInt(params.get("currentPage")));
			bean.setPageSize((Integer.parseInt(params.get("pageSize"))));
			Map<String, Object> map = yhlService.getBaobiaoByJihy(bean);
			List<JihuChengys> list = (List<JihuChengys>) map.get("rows");
			
			
			//List<JihuChengys> list = yhlService.getBaobiaoByJihy(bean);
			List<JihuChengys> list1 = new ArrayList<JihuChengys>();
			JihuChengys data = new JihuChengys();

			if(list.size() == 0)
			{
				setResult("result","");
			}else{
				data = list.get(0);
				//不相同 换个位置
				if(!data.getFactory_1().equals(factoryName.get(0)))
				{
					data.setFactory_2(data.getFactory_1());
					data.setNum_2(data.getNum_1());
					data.setFactory_1(factoryName.get(0));
					data.setNum_1(0);
				}
			}
			for(int j=1;j<list.size();j++)
			{
				//如果本条数据的计划员和上条的相同
				if(list.get(j).getJihy() == null)
				{
					continue;
				}
				if(list.get(j).getJihy().equals(data.getJihy()))
				{
					if(list.get(j).getFactory_1().equals(factoryName.get(0)))
					{
						data.setFactory_1(list.get(j).getFactory_1());
						data.setNum_1(list.get(j).getNum_1());
					}else{
						data.setFactory_2(list.get(j).getFactory_1());
						data.setNum_2(list.get(j).getNum_1());
					}
				}else{
					list1.add(data);
					data = list.get(j);
					if(!data.getFactory_1().equals(factoryName.get(0)))
					{
						data.setFactory_2(data.getFactory_1());
						data.setNum_2(data.getNum_1());
						data.setFactory_1(factoryName.get(0));
						data.setNum_1(0);
					}
				}
			}
			list1.add(data);
			setResult("total",list1.size());
			setResult("result", list1);
			setResult("group","计划员");
		}else if(params.get("_chengys").equals("1"))
		{
			ArrayList<String>  factoryName = new ArrayList<String>();
			factoryList = params.get("factory").split(" ");
			factoryNum  = factoryList.length;
			if(factoryNum == 1)
			{
				bean.setFactory(" and b.gongc = "+"'"+factoryList[0]+"' ");
				factoryName.add(yhlService.queryFactoryNameByBs(factoryList[0]).get(0).get("ZIDMC"));
				setResult("factoryList",factoryName.get(0));
			}else{
				bean.setFactory(" and b.gongc in ("+"'"+factoryList[0]+"' ,"+"'"+factoryList[1]+"')");
				factoryName.add(yhlService.queryFactoryNameByBs(factoryList[0]).get(0).get("ZIDMC"));
				factoryName.add(yhlService.queryFactoryNameByBs(factoryList[1]).get(0).get("ZIDMC"));
				//工厂名称 如"三厂，新能源"
				setResult("factoryList",factoryName.get(0)+","+factoryName.get(1));
			}
			bean.setCurrentPage(Integer.parseInt(params.get("currentPage")));
			bean.setPageSize((Integer.parseInt(params.get("pageSize"))));
			List<JihuChengys> list = yhlService.getBaobiaoByChengys(bean);
			List<JihuChengys> list1 = new ArrayList<JihuChengys>();
			JihuChengys data = new JihuChengys();

			if(list.size() == 0)
			{
				setResult("result","");
			}else{
				data = list.get(0);
				//不相同 换个位置
				if(!data.getFactory_1().equals(factoryName.get(0)))
				{
					data.setFactory_2(data.getFactory_1());
					data.setNum_2(data.getNum_1());
					data.setFactory_1(factoryName.get(0));
					data.setNum_1(0);
				}
			}
			for(int j=1;j<list.size();j++)
			{
				//如果本条数据的计划员和上条的相同
				if(list.get(j).getChengys() == null)
				{
					continue;
				}
				if(list.get(j).getChengys().equals(data.getChengys()))
				{
					if(list.get(j).getFactory_1().equals(factoryName.get(0)))
					{
						data.setFactory_1(list.get(j).getFactory_1());
						data.setNum_1(list.get(j).getNum_1());
					}else{
						data.setFactory_2(list.get(j).getFactory_1());
						data.setNum_2(list.get(j).getNum_1());
					}
				}else{
					list1.add(data);
					data = list.get(j);
					if(!data.getFactory_1().equals(factoryName.get(0)))
					{
						data.setFactory_2(data.getFactory_1());
						data.setNum_2(data.getNum_1());
						data.setFactory_1(factoryName.get(0));
						data.setNum_1(0);
					}
				}
			}
			
			list1.add(data);
			setResult("total",list1.size());
			//setResult("total",map.get("total"));
			int fromIndex = Math.max((bean.getCurrentPage() - 1) * bean.getPageSize(), 0);
			int toIndex = Math.min(bean.getCurrentPage() * bean.getPageSize(), list1.size());
			setResult("result", list1.subList(fromIndex, toIndex));
			setResult("group","承运商");
		}
		userOperLog.addCorrect(CommonUtil.MODULE_CKX, "根据计划员或者承运商", "统计延误报表");
	  }catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result", map);
			userOperLog.addError(CommonUtil.MODULE_CKX, "根据计划员或者承运商", "统计延误报表", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	/**查询延误要货令
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public String queryYhl()
	{
		try{		
			Map<String,String> params = this.getParams();
			String usercenter = params.get("usercenter");
			Yanwyhlconfig bean = new Yanwyhlconfig();
			bean.setCangkbh(params.get("cangkbh"));
			bean.setChanx(params.get("chanx"));
			bean.setChengysdm(params.get("chengysdm"));
			bean.setGongysdm(params.get("gongysdm"));
			bean.setJihy(params.get("jihy"));
			bean.setLingjbh(params.get("lingjbh"));
			bean.setUsercenter(usercenter);
			bean.setCurrentPage(Integer.parseInt(params.get("currentPage")));
			bean.setPageSize((Integer.parseInt(params.get("pageSize"))));
			
			String yaohllx = strToSql(params.get("yaohllx"),"yaohllx");
			String yaohlzt = strToSql(params.get("yaohlzt"),"yaohlzt");
			bean.setYaohllx(yaohllx);
			bean.setYaohlzt(yaohlzt);
			String factory = params.get("factory");
			String[] factoryList = factory.split(" ");
			List list = new ArrayList<String>(); 
			List<HashMap<String,String>> list1  = new  ArrayList<HashMap<String,String>>();
			int i = 0;
			for(i=0;i<factoryList.length;i++)
			{
				bean.setFactory(" and trim(b.gongc) = "+"'"+factoryList[i]+"'");
				String s = "result" + i;
				setResult(s, yhlService.queryYwyhlByParam(bean));
				list1 = yhlService.queryFactoryNameByBs(factoryList[i]);
				list.add(list1.get(0).get("ZIDMC"));			
				list1.clear();
			}
			//工厂名称，若是2个，左边在前，右边在后
			setResult("factoryList",list);
			/*
			if(usercenter.equals("UL"))
			{
				bean.setFactory(" and trim(b.gongc) = '3'");
				setResult("result", yhlService.queryYwyhlByParam(bean));
				bean.setFactory(" and trim(b.gongc) = '0'");
				setResult("result1", yhlService.queryYwyhlByParam(bean));
			}else if(usercenter.equals("UW"))
			{
				bean.setFactory(" and trim(b.gongc) = '1'");
				setResult("result", yhlService.queryYwyhlByParam(bean));
				bean.setFactory(" and trim(b.gongc) = '2'");
				setResult("result1", yhlService.queryYwyhlByParam(bean));
			}*/
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "参数", "查询延误要货令");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result", map);
			userOperLog.addError(CommonUtil.MODULE_CKX, "参数", "查询延误要货令", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	/*方法功能：将输入的字符串转换成sql in的方式
	 * 目标 and yaohllx in ('CD','C1','R2','R1') 现在：CD C1 R2 R1
	 * 
	 */
	public String strToSql(String inStr,String reg)
	{
		String str = new String();
		String str1 = new String();
		String[] s = inStr.split(" ");
		int i;
		for (i = 0;i<s.length;i++)
		{
			str = str + "'"+s[i]+"',";
		}
		str = str.substring(0, str.length()-1);
		str1 = " and "+reg+ " in ( "+str+" )";
		
		return str1;
	}
	

	public String Daoc(){
		Map<String,String> message = new HashMap<String,String>();
		try{
			//数据源
			Map<String, Object> dataSurce = new HashMap<String, Object>();
			//构造数据
			buildDataSurce(dataSurce);
			sumTotal(dataSurce);
			dataSurce.put("rows", dataSurce);
			//拿到response对象
			HttpServletResponse response = ActionContext.getActionContext().getResponse();
			if(dataSurce.get("type").equals("jihy")){
				downloadServices.downloadFile("shisywyhl_jihy.ftl", dataSurce, response, "实时延误要货令大屏报表", ExportConstants.FILE_XLS, false);
			}else{
				downloadServices.downloadFile("shisywyhl_chenys.ftl", dataSurce, response, "实时延误要货令大屏报表", ExportConstants.FILE_XLS, false);
			}
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "实时延误要货令大屏报表", "数据导出");
		
		} catch (Exception e) {
			message.put("message", e.getMessage());
			userOperLog.addError(CommonUtil.MODULE_CKX, "实时延误要货令大屏报表", "数据导出", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		setResult("result", message);
		return "downLoad";
	}
	
	
	public String queryFactoryByUsercebter()
	{
		try{
			String usercenter = getLoginUser().getUsercenter();
			List<HashMap<String, String>> factoryList = yhlService.queryFactoryByUsercebter(usercenter);
			String strFactory = CollectionUtilTool.listToJson(factoryList, "KEY", "VALUE");
			setResult("factory",strFactory);
			userOperLog.addCorrect(CommonUtil.MODULE_CKX, "用户中心", "查询工厂");
		}catch (Exception e) {
			Map<String,String> map = new HashMap<String,String>();
			map.put("message", e.getMessage());
			setResult("result", map);
			userOperLog.addError(CommonUtil.MODULE_CKX, "用户中心", "查询工厂", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
		}
		return AJAX;
	}
	
	private void buildDataSurce(Map<String, Object> dataSurce){
		int factoryNum;
		String factoryList[];
		Map<String,String> params = this.getParams(); 
		Yanwyhlconfig bean = new Yanwyhlconfig();
		bean.setUsercenter(params.get("usercenter"));
		bean.setCangkbh(params.get("cangkbh"));
		bean.setChanx(params.get("chanx"));
		bean.setChengysdm(params.get("chengysdm"));
		bean.setGongysdm(params.get("gongysdm"));
		bean.setJihy(params.get("jihy"));
		bean.setLingjbh(params.get("lingjbh"));
		String yaohllx = strToSql(params.get("yaohllx"),"yaohllx");
		String yaohlzt = strToSql(params.get("yaohlzt"),"yaohlzt");
		bean.setYaohllx(yaohllx);
		bean.setYaohlzt(yaohlzt);
		String x = params.get("_jihy");
		List<String> nameList = new ArrayList<String>();
		//按计划员归集
		if(params.get("_jihy").equals("1"))
		{
			ArrayList<String>  factoryName = new ArrayList<String>();
			factoryList = params.get("factory").split(" ");
			factoryNum  = factoryList.length;
			if(factoryNum == 1)
			{
				bean.setFactory(" and b.gongc = "+"'"+factoryList[0]+"' ");
				factoryName.add(yhlService.queryFactoryNameByBs(factoryList[0]).get(0).get("ZIDMC"));
				nameList.add(factoryName.get(0));
				dataSurce.put("factoryList", nameList);
			}else{
				bean.setFactory(" and b.gongc in ("+"'"+factoryList[0]+"' ,"+"'"+factoryList[1]+"')");
				factoryName.add(yhlService.queryFactoryNameByBs(factoryList[0]).get(0).get("ZIDMC"));
				factoryName.add(yhlService.queryFactoryNameByBs(factoryList[1]).get(0).get("ZIDMC"));
				//工厂名称 如"三厂，新能源"
				nameList.add(factoryName.get(0));
				nameList.add(factoryName.get(1));
				dataSurce.put("factoryList", nameList);
			}
			Map<String, Object> map = yhlService.getBaobiaoByJihy_export(bean);
			List<JihuChengys> list = (List<JihuChengys>) map.get("rows");
			
			
			//List<JihuChengys> list = yhlService.getBaobiaoByJihy(bean);
			List<JihuChengys> list1 = new ArrayList<JihuChengys>();
			JihuChengys data = new JihuChengys();

			if(list.size() == 0)
			{
				dataSurce.put("result", "");
			}else{
				data = list.get(0);
				//不相同 换个位置
				if(!data.getFactory_1().equals(factoryName.get(0)))
				{
					data.setFactory_2(data.getFactory_1());
					data.setNum_2(data.getNum_1());
					data.setFactory_1(factoryName.get(0));
					data.setNum_1(0);
				}
			}
			for(int j=1;j<list.size();j++)
			{
				//如果本条数据的计划员和上条的相同
				if(list.get(j).getJihy() == null)
				{
					continue;
				}
				if(list.get(j).getJihy().equals(data.getJihy()))
				{
					if(list.get(j).getFactory_1().equals(factoryName.get(0)))
					{
						data.setFactory_1(list.get(j).getFactory_1());
						data.setNum_1(list.get(j).getNum_1());
					}else{
						data.setFactory_2(list.get(j).getFactory_1());
						data.setNum_2(list.get(j).getNum_1());
					}
				}else{
					list1.add(data);
					data = list.get(j);
					if(!data.getFactory_1().equals(factoryName.get(0)))
					{
						data.setFactory_2(data.getFactory_1());
						data.setNum_2(data.getNum_1());
						data.setFactory_1(factoryName.get(0));
						data.setNum_1(0);
					}
				}
			}
			list1.add(data);
			dataSurce.put("list", list1);
			dataSurce.put("type", "jihy");
		}else if(params.get("_chengys").equals("1"))
		{
			ArrayList<String>  factoryName = new ArrayList<String>();
			factoryList = params.get("factory").split(" ");
			factoryNum  = factoryList.length;
			if(factoryNum == 1)
			{
				bean.setFactory(" and b.gongc = "+"'"+factoryList[0]+"' ");
				factoryName.add(yhlService.queryFactoryNameByBs(factoryList[0]).get(0).get("ZIDMC"));
				nameList.add(factoryName.get(0));
				dataSurce.put("factoryList", nameList);
			}else{
				bean.setFactory(" and b.gongc in ("+"'"+factoryList[0]+"' ,"+"'"+factoryList[1]+"')");
				factoryName.add(yhlService.queryFactoryNameByBs(factoryList[0]).get(0).get("ZIDMC"));
				factoryName.add(yhlService.queryFactoryNameByBs(factoryList[1]).get(0).get("ZIDMC"));
				//工厂名称 如"三厂，新能源"
				nameList.add(factoryName.get(0));
				nameList.add(factoryName.get(1));
				dataSurce.put("factoryList", nameList);
			}
			Map<String, Object> map = yhlService.getBaobiaoByChengys_export(bean);
			
			List<JihuChengys> list = (List<JihuChengys>) map.get("rows");
			List<JihuChengys> list1 = new ArrayList<JihuChengys>();
			JihuChengys data = new JihuChengys();

			if(list.size() == 0)
			{
				setResult("result","");
			}else{
				data = list.get(0);
				//不相同 换个位置
				if(!data.getFactory_1().equals(factoryName.get(0)))
				{
					data.setFactory_2(data.getFactory_1());
					data.setNum_2(data.getNum_1());
					data.setFactory_1(factoryName.get(0));
					data.setNum_1(0);
				}
			}
			for(int j=1;j<list.size();j++)
			{
				//如果本条数据的计划员和上条的相同
				if(list.get(j).getChengys() == null)
				{
					continue;
				}
				if(list.get(j).getChengys().equals(data.getChengys()))
				{
					if(list.get(j).getFactory_1().equals(factoryName.get(0)))
					{
						data.setFactory_1(list.get(j).getFactory_1());
						data.setNum_1(list.get(j).getNum_1());
					}else{
						data.setFactory_2(list.get(j).getFactory_1());
						data.setNum_2(list.get(j).getNum_1());
					}
				}else{
					list1.add(data);
					data = list.get(j);
					if(!data.getFactory_1().equals(factoryName.get(0)))
					{
						data.setFactory_2(data.getFactory_1());
						data.setNum_2(data.getNum_1());
						data.setFactory_1(factoryName.get(0));
						data.setNum_1(0);
					}
				}
			}
			list1.add(data);
			dataSurce.put("list", list1);
			dataSurce.put("type", "chenys");
		}
	}
	
	private void sumTotal(Map<String, Object> dataSurce){
		long total1 = 0;
		long total2 = 0;
		for (JihuChengys jihu : (List<JihuChengys>)dataSurce.get("list")) {
			total1 += jihu.getNum_1();
			total2 += jihu.getNum_2();
		}
		dataSurce.put("total1", total1);
		dataSurce.put("total3", total1 + total2);
		if(((List<String>)dataSurce.get("factoryList")).size() == 2){
			dataSurce.put("total2", total2);
		}
	}
	
}
