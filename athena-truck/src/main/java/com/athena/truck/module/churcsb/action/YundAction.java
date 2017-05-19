package com.athena.truck.module.churcsb.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.truck.module.churcsb.util.CommonEntity;
import com.athena.truck.entity.Yund;
import com.athena.truck.module.churcsb.service.YundService;
import com.athena.util.athenalog.impl.UserOperLog;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.container.core.ComponentDefinition;
import com.toft.mvc.ActionSupport;
import com.toft.mvc.annotaions.Param;

/**
 * 运单-出入厂申报
 * @author huilit
 *
 */
@Component (scope = ComponentDefinition.SCOPE_PROTOTYPE)
public class YundAction extends ActionSupport {
	
	@Inject
	private UserOperLog userOperLog;
	
	@Inject
	private YundService yundService;
	
	public String execute(){
		userOperLog.addCorrect("truck", "运单申报", "入厂申报页面");
		LoginUser currentUser = AuthorityUtils.getSecurityUser();
		
		String usercenter = currentUser.getUsercenter();
		//区域组id，通过区域组id和用户中心在SYS_QY_GROUP表中查到所有区域
		Map<String,String> postMap = currentUser.getPostAndRoleMap();
		String postId = postMap.get("QUYGLY");
		Map<String,Object> quyParam = new HashMap<String,Object>();
		quyParam.put("usercenter", usercenter);
		quyParam.put("postId", postId);
		List<Map<String,Object>> quyResults = yundService.queryCurUserQuy(quyParam);
		
		if(quyResults.size()>0){
			setRequestAttribute("quybh",quyResults.get(0).get("QUYBH"));
		}
		
//		String username = currentUser.getUsername();
//		usercenter = currentUser.getUsercenter();
		setRequestAttribute("usercenter",usercenter);
		return SUCCESS;
	}
	
	/**
	 * 175001运单查看页面
	 * @return
	 * String
	 */
	public String yundym(){
		return "select";
	}
	
	
	public String yund(@Param Yund bean){
		LoginUser currentUser = AuthorityUtils.getSecurityUser();
		String usercenter = currentUser.getUsercenter();
		String username = currentUser.getUsername();
		Map<String, String> map = this.getParams();
		//Yund yundCk = new Yund();
		bean.setUsercenter(usercenter);
		bean.setCreator(username);
		bean.setPageSize(Integer.valueOf(map.get("pagesize")));
		bean.setPageNo(Integer.valueOf(map.get("currentPage")));
		map.put("usercenter", bean.getUsercenter());
		map.put("creator", bean.getCreator());
		Map<String, Object> yundList = yundService.queryYundCk(map, bean);
		/*Map map = new HashMap();
		map.put("list", yundList);
		map.put("total", yundList.size());*/
		List<Yund> ls = (List<Yund>) yundList.get("rows");
		ArrayList aList = new ArrayList();
		for (int i = 0; i < ls.size(); i++) {
			Yund yund = ls.get(i);
			aList.add("{YUNDH:\"" + yund.getYundh() + "\",DAZTBH:\""
					+yund.getDaztbh() + "\",KACH:\""+yund.getKach()+"\",SHENGBSJ:\""+yund.getShengbsj()+"\",ZHUANGTMC:\""+yund.getZhuangtmc()+"\"}");
		}
		
		yundList.put("rows", ls);
//		Map<String, Object> tt = new HashMap<String, Object>(); 
//		tt.put("list", aList);
//		tt.put("currentPage", Integer.valueOf(map.get("currentPage")));
		JSONObject a=new JSONObject();
		 a.put("currentPage", Integer.valueOf(map.get("currentPage")));
		 a.put("list", aList);
		 
		CommonEntity commonEntity = new CommonEntity();
		
		commonEntity.setResponse("0000");
		commonEntity.setParameter(a.toString());
		int total_page =(Integer) yundList.get("total");
		
		JSONObject obj = JSONObject.fromObject(commonEntity);
		
		//setResult("result", total_page);
		obj.put("total_page", total_page);
		//obj.put("pagesize", map.get("currentPage"));
//		obj.put("currentPage", Integer.valueOf(map.get("currentPage")));
//		setResult("currentPage", Integer.valueOf(map.get("currentPage")));
		setResult("result", obj);
		
		//setRequestAttribute("result", obj);
		return AJAX;
		
		
		
//		yundList.put("rows", ls);
//		CommonEntity commonEntity = new CommonEntity();
//		String sJson = JSONArray.fromObject(aList).toString();
//		commonEntity.setParameter("{list:" + sJson + "}");
//		commonEntity.setResponse("0000");
//		//commonEntity.setParameter("")
//		JSONObject obj = JSONObject.fromObject(commonEntity);
//		//setResult("result", map);
//		/*ArrayList<String> list = new ArrayList<String>();
//		for(Map<String,Object> quyResult:yundList){
//			String yundh = (String)quyResult.get("YUNDH");
//			list.add("{name:\""+yundh+"\",value:\""+yundh+"\"}");
//		}
//		String sJson = JSONArray.fromObject(yundList).toString();
//		CommonEntity commonEntity = new CommonEntity();
//		commonEntity.setParameter("{list:" + sJson + "}");
//		JSONObject obj = JSONObject.fromObject(commonEntity);*/
//		setRequestAttribute("result", obj);
//		//setResult("result", obj);
//		return "select";
		
		
	}
	
//	public String chaKanYD(@Param Yund bean){
//		LoginUser currentUser = AuthorityUtils.getSecurityUser();
//		String usercenter = currentUser.getUsercenter();
//		String username = currentUser.getUsername();
//		//Yund yundCk = new Yund();
//		bean.setUsercenter(usercenter);
//		bean.setCreator(username);
//		Map map = this.getParams();
//		map.put("usercenter", bean.getUsercenter());
//		map.put("creator", bean.getCreator());
//		Map<String, Object> yundList = yundService.queryYundCk(map, bean);
//		/*Map map = new HashMap();
//		map.put("list", yundList);
//		map.put("total", yundList.size());*/
//		List<Yund> ls = (List<Yund>) yundList.get("rows");
//		ArrayList aList = new ArrayList();
//		for (int i = 0; i < ls.size(); i++) {
//			Yund yund = ls.get(i);
//			aList.add("{YUNDH:\"" + yund.getYundh() + "\",DAZTBH:\""
//					+yund.getDaztbh() + "\",KACH:\""+yund.getKach()+"\",SHENGBSJ:\""+yund.getShengbsj()+"\",ZHUANGT:\""+yund.getZhuangt()+"\"}");
//		}
//			yundList.put("rows", ls);
//		CommonEntity commonEntity = new CommonEntity();
//		String sJson = JSONArray.fromObject(aList).toString();
//		commonEntity.setParameter("{list:" + sJson + "}");
//		commonEntity.setResponse("0000");
//		//commonEntity.setParameter("")
//		JSONObject obj = JSONObject.fromObject(commonEntity);
//		//setResult("result", map);
//		/*ArrayList<String> list = new ArrayList<String>();
//		for(Map<String,Object> quyResult:yundList){
//			String yundh = (String)quyResult.get("YUNDH");
//			list.add("{name:\""+yundh+"\",value:\""+yundh+"\"}");
//		}
//		String sJson = JSONArray.fromObject(yundList).toString();
//		CommonEntity commonEntity = new CommonEntity();
//		commonEntity.setParameter("{list:" + sJson + "}");
//		JSONObject obj = JSONObject.fromObject(commonEntity);*/
//		setResult("result", obj);
//		return AJAX;
//	}
	
	//运单删除
	public String yundSC(@Param("list") ArrayList<Yund> yundList) {
		LoginUser currentUser = AuthorityUtils.getSecurityUser();
		Map<String,String> params = this.getParams();
		params.put("username", currentUser.getUsername());
		params.put("usercenter", currentUser.getUsercenter());
		//params.put("list", yundList);
//		System.out.println(params.get("username")+"*********");
//		System.out.println(params.get("usercenter")+"*********");
		String strResponse = "";
		try{ 
			strResponse = yundService.yundSC(yundList,params);	
		}catch (Exception e) {
			//e.printStackTrace();
			setResult("messageColor", "red");
			setResult("messageContent",e.getMessage());
			return AJAX;
		 }
		 
		String usercenter = currentUser.getUsercenter();
		String username = currentUser.getUsername();
		Yund yundCk = new Yund();
		yundCk.setUsercenter(usercenter);
		yundCk.setCreator(username);
		yundCk.setPageSize(Integer.valueOf(params.get("pagesize")));
		yundCk.setPageNo(Integer.valueOf(params.get("currentPage")));
//		Map<String, String> map = this.getParams();
//		params.put("usercenter", yundCk.getUsercenter());
		params.put("creator", yundCk.getCreator());
		Map<String, Object> yunds = yundService.queryYundCk(params, yundCk);
		/*Map map = new HashMap();
		map.put("list", yundList);
		map.put("total", yundList.size());*/
		List<Yund> ls = (List<Yund>) yunds.get("rows");
		ArrayList aList = new ArrayList();
		for (int i = 0; i < ls.size(); i++) {
			Yund yund = ls.get(i);
			aList.add("{YUNDH:\"" + yund.getYundh() + "\",DAZTBH:\""
					+yund.getDaztbh() + "\",KACH:\""+yund.getKach()+"\",SHENGBSJ:\""+yund.getShengbsj()+"\",ZHUANGTMC:\""+yund.getZhuangtmc()+"\"}");
		}
		
		yunds.put("rows", ls);
		JSONObject a=new JSONObject();
		 a.put("currentPage", Integer.valueOf(params.get("currentPage")));
		 a.put("list", aList);
		String sJson = JSONArray.fromObject(aList).toString();
		CommonEntity commonEntity = new CommonEntity();
		
		commonEntity.setResponse(strResponse);
		commonEntity.setParameter(a.toString());
		
		int total_page =(Integer) yunds.get("total");
		
		JSONObject obj = JSONObject.fromObject(commonEntity);
		
		//setResult("result", total_page);
		obj.put("total_page", total_page);
		setResult("result", obj);
		return AJAX;
		
	}
	
	
	public String getQuybhSelect(){
		LoginUser currentUser = AuthorityUtils.getSecurityUser();
		String usercenter = currentUser.getUsercenter();
		Map<String,String> postMap = currentUser.getPostAndRoleMap();
		String postId = postMap.get("QUYGLY");
		Map<String,Object> quyParam = new HashMap<String,Object>();
		quyParam.put("usercenter", usercenter);
		quyParam.put("postId", postId);
		List<Map<String,Object>> quyResults = yundService.queryCurUserQuy(quyParam);
		ArrayList<String> list = new ArrayList<String>();
		for(Map<String,Object> quyResult:quyResults){
			String quy = (String)quyResult.get("QUYBH");
			list.add("{name:\""+quy+"\",value:\""+quy+"\"}");
		}
		String sJson = JSONArray.fromObject(list).toString();
		CommonEntity commonEntity = new CommonEntity();
		commonEntity.setParameter("{list:" + sJson + "}");
		JSONObject obj = JSONObject.fromObject(commonEntity);
		setResult("result", obj);
		
		return AJAX;
	}
	
	/**
	 * 复选框选择事件，通过查询大站台确定是否是混装状态  +查询卡车号确定是否存在不同卡车号的BL单
	 * @param list
	 * @return
	 */
	public String queryHunzFlagQjp(@Param("list") ArrayList<Yund> list){
		String kach = this.getParam("kach");
		String blhs = "";
		for(Yund unit:list){
			if(blhs.length() != 0){
				blhs = ","+blhs;
			}
			blhs = "'"+unit.getBlh()+"'"+blhs;
		}
		//查询是否混装，混装则改状态，同时根据大站台数量生成多个运单
		Map<String,Object> daztParam = new HashMap<String,Object>();
		daztParam.put("blhs", blhs);
		List<Map<String,Object>> daztResults = yundService.queryHunzFlag(daztParam);
		
		String messageColor = "blue";
		String meeeageContent = "";
		
		//有选中
		if(daztResults.size() > 0){
			//有混装
			if(daztResults.size() > 1){
//				setResult("messageColor","blue");
//				setResult("messageContent","注意：存在混装情况，如仍需通过申报，请继续进行“确认申报”操作");
//				setResult("messageColor","blue");
//				setResult("messageContent","存在混装情况，且有BL单不属于该卡车，如仍需申报，请继续进行“确认申报”操作");
				meeeageContent = "存在混装情况，";
			}
			//无混装
			else{
//				setResult("messageColor","blue");
//				setResult("messageContent","如已选中所有相关BL单，请继续进行“确认申报”操作");
//				setResult("messageColor","blue");
//				setResult("messageContent","有BL单不属于该卡车，如仍需通过申报，请继续进行“确认申报”操作");
			}
		}
		//无选中
		else{
			setResult("messageColor","red");
			setResult("messageContent","请先选择要申报的BL单");
			return AJAX;
		}
		
		List<Map<String,Object>> kachResults = yundService.queryKachFlag(daztParam);
		if(kachResults.size() != 1){
			if("".equals(meeeageContent)){
				meeeageContent = "有BL单不属于该卡车，";
			}else{
				meeeageContent = meeeageContent + "且有BL单不属于该卡车，";
			}
		}else{
			if(!kach.equals(kachResults.get(0).get("KACH"))){
				if("".equals(meeeageContent)){
					meeeageContent = "有BL单不属于该卡车，";
				}else{
					meeeageContent = meeeageContent + "且有BL单不属于该卡车，";
				}
			}
		}
		
		if(!"".equals(meeeageContent)){
			messageColor = "blue";
			meeeageContent = meeeageContent + "如仍需申报，请继续进行“确认申报”操作";
		}else{
			//meeeageContent = "如已选中所有相关BL单，请继续进行“确认申报”操作";
			meeeageContent = "请继续操作";
		}
		
		setResult("messageColor",messageColor);
		setResult("messageContent",meeeageContent);
		
		return AJAX;
	}

	/**
	 * 入厂申报
	 * @param list
	 * @return
	 */
	public String insertShenbaoQjp(@Param("list") ArrayList<Yund> list){
		Map<String,String> map = this.getParams();
		String usercenter = (String)map.get("usercenter");

		LoginUser currentUser = AuthorityUtils.getSecurityUser();
		String username = currentUser.getUsername();
		Map<String,String> postMap = currentUser.getPostAndRoleMap();
		//区域组id，通过区域组id和用户中心在SYS_QY_GROUP表中查到所有区域。
		//如用户非任何区域组成员，且进入该方法，则会报错
		String postId = postMap.get("QUYGLY");
		Map<String,Object> quyParam = new HashMap<String,Object>();
		quyParam.put("usercenter", usercenter);
		quyParam.put("postId", postId);
		List<Map<String,Object>> quyResults = yundService.queryCurUserQuy(quyParam);
		String quyResultStr = null;
		try {
			Map<String,Object> quyResult = quyResults.get(0);
			quyResultStr = quyResult.get("QUYBH").toString();
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			setResult("message","1");
			setResult("messageContent","您没有操作该区域的权限");
			return AJAX;
		}
		
		String shenbsj = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(java.util.Calendar.getInstance().getTime());
		String shenbsjStr = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(java.util.Calendar.getInstance().getTime());
		
		Yund yundParam = new Yund();
		List<Yund> yundmxParams = new ArrayList<Yund>();
		
		yundParam.setUsercenter(usercenter);
		yundParam.setQuybh(quyResultStr);

		yundParam.setKach((String)map.get("kach"));
		yundParam.setShengbsj(shenbsj);
		yundParam.setRucsj("");
		
		//出厂标记
		Map<String,Object> chucbjPar = new HashMap<String,Object>();
		chucbjPar.put("kach", yundParam.getKach());
		chucbjPar.put("usercenter", usercenter);
		List<Map<String,Object>> chucbjResults = yundService.queryLastChucsj(chucbjPar);
		if(chucbjResults.size()>0){
			for(int i=0;i<chucbjResults.size();i++){
				Map<String,Object> chucbjRes = chucbjResults.get(i);
				String zhuangt = (String)chucbjRes.get("ZHUANGT");
				if(!"1".equals(zhuangt) 
						&& !"2".equals(zhuangt)
						&& !"8".equals(zhuangt)&&!"80".equals(zhuangt)&&!"90".equals(zhuangt)){
					setResult("message","1");
					setResult("messageContent","该卡车已入厂卸货，不能再次进行申报，如与实际情况不符，请联系管理员");
					return AJAX;
				}
			}
			for(int i=0;i<chucbjResults.size();i++){
				Map<String,Object> chucbjRes = chucbjResults.get(i);
//				String chucsj = (String)chucbjRes.get("CHUCSJ");
//				if(chucsj == null || "".equals(chucsj)){
//				}
				Map<String,Object> liucParam = new HashMap<String,Object>();
				liucParam.put("usercenter", usercenter);
				liucParam.put("quybh", quyResultStr);
				liucParam.put("daztbh", (String)chucbjRes.get("DAZTBH"));
				liucParam.put("liucbh", "9");
				List<Map<String,Object>> liucResults = yundService.queryLiucdy(liucParam);
				Map<String,Object> liuc = null;
				try {
					liuc = liucResults.get(0);
				} catch (IndexOutOfBoundsException e) {
					e.printStackTrace();
					setResult("message","1");
					setResult("messageContent","进行出厂标记操作时，缺少相关的卡车流程定义，请联系管理员");
					return AJAX;
				}
				Yund tempPar = new Yund();
				tempPar.setYundh((String)chucbjRes.get("YUNDH"));
				tempPar.setZhuangt((String)liuc.get("LIUCBH"));
				tempPar.setZhuangtmc((String)liuc.get("LIUCMC"));
				tempPar.setKach(yundParam.getKach());
				tempPar.setQuybh(yundParam.getQuybh());
				tempPar.setDaztbh((String)chucbjRes.get("DAZTBH"));
				tempPar.setUsercenter(usercenter);
				tempPar.setEditor(username);
				
				BigDecimal bzsj = (BigDecimal)liuc.get("BIAOZSJ");
				if(bzsj==null ){
					tempPar.setBiaozsj("0");
				}
				tempPar.setBiaozsj(bzsj+"");
				yundService.updateChucbj(tempPar,(String)chucbjRes.get("ZHUANGT"));
				
				List<Map<String,Object>> rongqdjs = yundService.queryRongqdj(chucbjPar);
				if(rongqdjs.size()>0){
					for(int j=0;j<rongqdjs.size();j++){
						Map<String,Object> dangj = rongqdjs.get(j);
						String zhuangt = (String)dangj.get("ZHUANGT");
						if(!"5".equals(zhuangt)){
							Yund tempYd = new Yund();
							tempYd.setUsercenter((String)dangj.get("USERCENTER"));
							tempYd.setDanjbh((String)dangj.get("DANJBH"));
							tempYd.setKach((String)dangj.get("TCH"));
							tempYd.setEditor(username);
							yundService.updateRongqzt(tempYd);
						}
					}
				}
			}
		}
		//出厂标记结束
		
		yundParam.setChucsj("");
		
		yundParam.setBeiz2("0");
		yundParam.setBeiz3("");
		
		yundParam.setCreator(username);
		yundParam.setCreateTime(shenbsj);
		yundParam.setEditor(username);
		yundParam.setEditTime(shenbsj);
		
		String blhs = "";
		for(Yund unit:list){
			if(blhs.length() != 0){
				blhs = ","+blhs;
			}
			blhs = "'"+unit.getBlh()+"'"+blhs;
		}
		//查询是否混装，混装则改状态，同时根据大站台数量生成多个运单
		Map<String,Object> daztParam = new HashMap<String,Object>();
		daztParam.put("blhs", blhs);
		List<Map<String,Object>> daztResults = yundService.queryHunzFlag(daztParam);
		
		//运单号生成规则，混装会在后面加递增数字
		String yundh = (String)map.get("kach") 
				+ shenbsjStr
				;

		//分批存入运单表的执行对
		Map<String,Object> exes = new HashMap<String,Object>();
		if(daztResults.size() > 0){
			if(daztResults.size() > 1){
				yundParam.setFlag("2");
				for(int i=0;i<daztResults.size();i++){
					Yund yundParamTemp = new Yund(); 
					
					yundParamTemp.setUsercenter(yundParam.getUsercenter());
					yundParamTemp.setQuybh(yundParam.getQuybh());
	
					yundParamTemp.setKach(yundParam.getKach());
					yundParamTemp.setShengbsj(yundParam.getShengbsj());
					yundParamTemp.setRucsj(yundParam.getRucsj());
					yundParamTemp.setChucsj(yundParam.getChucsj());
					
					yundParamTemp.setBeiz2(yundParam.getBeiz2());
					yundParamTemp.setBeiz3(yundParam.getBeiz3());
					
					yundParamTemp.setCreator(yundParam.getCreator());
					yundParamTemp.setCreateTime(yundParam.getCreateTime());
					yundParamTemp.setEditor(yundParam.getEditor());
					yundParamTemp.setEditTime(yundParam.getEditTime());
					
					yundParamTemp.setFlag(yundParam.getFlag());
					yundParamTemp.setYundh(yundh+(i+1));
					
					//取大站台编号，为空则加入到字符串"null"开头并递增数字的key中去
					String daztbhStr = (String)daztResults.get(i).get("DAZTBH");
					if(daztbhStr == null || "".equals(daztbhStr)){
						setResult("message","1");
						setResult("messageContent","相关BL单中的实际大站台数据缺失，请联系管理员");
						return AJAX;
					}
					yundParamTemp.setDaztbh(daztbhStr);
					String quybhStr = (String)daztResults.get(i).get("QUYBH");
					if(quybhStr == null || "".equals(quybhStr)){
						setResult("message","1");
						setResult("messageContent","相关BL单中的实际大站台与区域的数据关联缺失，请联系管理员");
						return AJAX;
					}
					yundParamTemp.setQuybh(quybhStr);

					//从卡车流程表中取流程，必须有流程1，否则系统报错，业务错误
					Map<String,Object> liucParam = new HashMap<String,Object>();
					liucParam.put("usercenter", usercenter);
					liucParam.put("quybh", quyResultStr);
					liucParam.put("daztbh", daztbhStr);
					liucParam.put("liucbh", "1");
					List<Map<String,Object>> liucResults = yundService.queryLiucdy(liucParam);
					Map<String,Object> liuc = null;
					try {
						liuc = liucResults.get(0);
					} catch (IndexOutOfBoundsException e) {
						e.printStackTrace();
						setResult("message","1");
						setResult("messageContent","缺少相关的卡车流程定义，请联系管理员");
						return AJAX;
					}
					yundParamTemp.setZhuangt((String)liuc.get("LIUCBH"));
					yundParamTemp.setZhuangtmc((String)liuc.get("LIUCMC"));
					
					BigDecimal bzsj = (BigDecimal)liuc.get("BIAOZSJ");
					if(bzsj==null ){
						yundParamTemp.setBiaozsj("0");
					}
					
					yundParamTemp.setBiaozsj(bzsj+"");
					
					if(daztbhStr == null || "".equals(daztbhStr)){
						daztbhStr = "null"+i;
					}
					exes.put(daztbhStr, yundParamTemp);
				}
			}else{
				yundParam.setFlag("1");
				yundParam.setYundh(yundh+0);

				//取大站台编号，为空则加入到字符串"null"开头并递增数字的key中去
				String daztbhStr = (String)daztResults.get(0).get("DAZTBH");
				if(daztbhStr == null || "".equals(daztbhStr)){
					setResult("message","1");
					setResult("messageContent","相关BL单中的实际大站台数据缺失，请联系管理员");
					return AJAX;
				}
				yundParam.setDaztbh(daztbhStr);
				String quybhStr = (String)daztResults.get(0).get("QUYBH");
				if(quybhStr == null || "".equals(quybhStr)){
					setResult("message","1");
					setResult("messageContent","相关BL单中的实际大站台与区域的数据关联缺失，请联系管理员");
					return AJAX;
				}
				yundParam.setQuybh(quybhStr);

				//从卡车流程表中取流程，必须有流程1，否则系统报错，业务错误
				Map<String,Object> liucParam = new HashMap<String,Object>();
				liucParam.put("usercenter", usercenter);
				liucParam.put("quybh", quyResultStr);
				liucParam.put("daztbh", daztbhStr);
				liucParam.put("liucbh", "1");
				List<Map<String,Object>> liucResults = yundService.queryLiucdy(liucParam);
				Map<String,Object> liuc = null;
				try {
					liuc = liucResults.get(0);
				} catch (IndexOutOfBoundsException e) {
					e.printStackTrace();
					setResult("message","1");
					setResult("messageContent","缺少相关的卡车流程定义，请联系管理员");
					return AJAX;
				}
				yundParam.setZhuangt((String)liuc.get("LIUCBH"));
				yundParam.setZhuangtmc((String)liuc.get("LIUCMC"));

				BigDecimal bzsj = (BigDecimal)liuc.get("BIAOZSJ");
				if(bzsj==null ){
					yundParam.setBiaozsj("0");
				}
				
				yundParam.setBiaozsj(bzsj+"");

				if(daztbhStr == null || "".equals(daztbhStr)){
					daztbhStr = "null0";
				}
				exes.put(daztbhStr, yundParam);
			}
		}else{
			setResult("message","1");
			setResult("messageContent","BL单数据中找不到对应的大站台，请联系管理员");
			return AJAX;
		}
		
//		List<Yund> otherYundList = new ArrayList<Yund>();
//		boolean isHunz = false;
		
		for(Yund unit:list){
			String dztbhTemp = unit.getDaztbh();
			if(dztbhTemp == null || "".equals(dztbhTemp)){
				dztbhTemp = "null0";
				Yund temp = (Yund)exes.get(dztbhTemp);
				if(temp == null){
					dztbhTemp = "null1";
					temp = (Yund)exes.get(dztbhTemp);
				}
				unit.setYundh(temp.getYundh());
			}else{
				unit.setYundh(((Yund)exes.get(dztbhTemp)).getYundh());
			}
			unit.setBeiz2("0");
			unit.setBeiz3("");
			if(unit.getBeiz1().equals("1")){
			((Yund)exes.get(dztbhTemp)).setTiqpdbs("2");	
			}
			unit.setCreator(username);
			unit.setCreateTime(shenbsj);
			unit.setEditor(username);
			unit.setEditTime(shenbsj);
			boolean flag=yundService.queryBLH(unit);
		    if(flag){
		    	setResult("message","1");
				setResult("messageContent","BL单已申报，不能重复申报！");
				return AJAX;	
		    }
			yundmxParams.add(unit);
		}
		
		List<Yund> ydParams = new ArrayList<Yund>();
		java.util.Iterator<String> it = exes.keySet().iterator();
		while(it.hasNext()){
			String dztbh = it.next();
			Yund tem = (Yund)exes.get(dztbh);
			tem.setChengysdm(yundmxParams.get(0).getChengysdm());
			if(tem.getChengysdm() == null 
					|| "".equals(tem.getChengysdm())){
				tem.setChengysdm("null");
			}
			ydParams.add(tem);
		}
		try {
			yundService.insertShenbao(ydParams, yundmxParams);
		} catch (Exception e) {
			e.printStackTrace();setResult("message","1");
			setResult("messageContent","后台数据处理错误，请联系管理员");
			return AJAX;
		}
		setResult("message","0");
		
		return AJAX;
	}
	
	
	/**
	 * 通过BL号找BL单，将key转换为小写，目标为全键盘界面
	 * @param params
	 * @return
	 */
	public String findBldsByParamsQjp(@Param Yund params){
		Map<String,String> formParam = this.getParams();
		StringBuffer msg = new StringBuffer();
		msg.append("BL单"+formParam.get("blh"));
		Boolean bool = true;
		
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("kach", params.getKach());
		param.put("blh", params.getBlh());
		//param.put("usercenter", params.getUsercenter());
		
		List<Map<String,Object>> results = yundService.findBldsByParams(param);
		List<Map<String,Object>> newResults = new ArrayList<Map<String,Object>>();
		for(int i=0;i<results.size();i++){
			Map<String,Object> oldResult = results.get(i);
			Map<String,Object> newResult = new HashMap<String,Object>();
			java.util.Iterator<String> it = oldResult.keySet().iterator();
			while(it.hasNext()){
				String oldKey = it.next();
				String newKey = oldKey.toLowerCase();
				newResult.put(newKey, oldResult.get(oldKey));
			}
			if(!formParam.get("usercenter").equals(newResult.get("usercenter"))){
				msg.append("不属于您所在的用户中心");
				bool = false;
			}
			if(!formParam.get("quybh").equals(newResult.get("quybh"))){
				if(!bool){
					msg.append("，且");
				}
				msg.append("不属于您当前处理的区域");
				bool = false;
			}
			List<Map<String,Object>> biaos = yundService.queryBiaosFlag(newResult);
			for(Map<String,Object> tempBiaos : biaos){
				if(!"1".equals(tempBiaos.get("BIAOS"))){
					if(!bool){
						msg.append("，且");
					}
					msg.append("所属的大站台已被停用");
					bool = false;
				}
			}
			if(bool){
				newResults.add(newResult);
			}else{
				msg.append("，请重新输入");
				setResult("msg",msg.toString());
			}
		}
		setResult("bls",newResults);
		
		
		return AJAX;
	}
	/**
	 * 通过卡车号和用户中心找BL单，将key转换为小写，目标为全键盘界面
	 * @param params
	 * @return
	 */
	public String findBldsByKachQjp(@Param Yund params){
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("kach", params.getKach());
		param.put("usercenter", params.getUsercenter());
		param.put("quybh", params.getQuybh());
		
		List<Map<String,Object>> results = yundService.findBldsByKach(param);
		List<Map<String,Object>> newResults = new ArrayList<Map<String,Object>>();
		for(int i=0;i<results.size();i++){
			Map<String,Object> oldResult = results.get(i);
			Map<String,Object> newResult = new HashMap<String,Object>();
			java.util.Iterator<String> it = oldResult.keySet().iterator();
			while(it.hasNext()){
				String oldKey = it.next();
				String newKey = oldKey.toLowerCase();
				newResult.put(newKey, oldResult.get(oldKey));
			}
			newResults.add(newResult);
		}
		setResult("bls",newResults);
		
		
		return AJAX;
	}

	

	/**
	 * 出厂申报 
	 * @param list
	 * @return
	 */
	public String executeChuc(){
		userOperLog.addCorrect("truck", "运单申报", "出厂申报页面");
		LoginUser currentUser = AuthorityUtils.getSecurityUser();
		
		String usercenter = currentUser.getUsercenter();
		String username = currentUser.getUsername();

		Map<String,String> postMap = currentUser.getPostAndRoleMap();
		//区域组id，通过区域组id和用户中心在SYS_QY_GROUP表中查到所有区域。
		//如用户非任何区域组成员，且进入该方法，则会报错
		String postId = postMap.get("QUYGLY");
		Map<String,Object> quyParam = new HashMap<String,Object>();
		quyParam.put("usercenter", usercenter);
		quyParam.put("postId", postId);
		List<Map<String,Object>> quyResults = yundService.queryCurUserQuy(quyParam);
		if(quyResults.size()>0){
			setRequestAttribute("quybh",quyResults.get(0).get("QUYBH").toString());
		}
		setRequestAttribute("usercenter",usercenter);
		setRequestAttribute("username",username);
		return SUCCESS;
	}
	
	/**
	 * 第一步，扫描输入卡车号，得到运单和容器返空单信息
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getYundRongqfkdByKach(){
		Map map = this.getParams();
		List<Yund> l = this.yundService.queryAllYundForChuc(map);
		
		String msgColor = "blue";
		String message = "";
		
		if(l.size()==0){
			msgColor = "red";
			message = "找不到该车的运单数据";
			setResult("msgColor",msgColor);
			setResult("message",message);
			return AJAX;
		}
		int noMatchedCount = 0;
		for(Yund temp:l){
			if(!"8".equals(temp.getZhuangt())){
				msgColor = "red";
				message = "该车还存在未卸货完成的运单，不能执行出厂申报";
				setResult("msgColor",msgColor);
				setResult("message",message);
				return AJAX;
			}
			String duiycmqy = temp.getBeiz();
			Boolean m1 = false;
			Boolean m2 = false;
			if(duiycmqy != null 
					&& !"".equals(duiycmqy)
					&& !map.get("quybh").equals(duiycmqy)){
				m1 = true;
			}
			if(m1){
				String[] cmqys = duiycmqy.split(",");
				for(String cmqy:cmqys){
					if(map.get("quybh").equals(cmqy)){
						m2 = false;
						break;
					}else{
						m2 = true;
					}
				}
			}
			
			if(m1 && m2){
				noMatchedCount++;
			}
		}
		if(noMatchedCount == l.size()){
			msgColor = "red";
			message = "该车没有任何运单指定您所在的区域为出门区域，不能为该车进行出厂申报";
			setResult("msgColor",msgColor);
			setResult("message",message);
			return AJAX;
		}
		
		int rqfkdj = 0;	//容器返空单数量
		int kcccdj = 0;	//空车出厂单数量
		//int wfkrq = 0;	//未返空容器数量
		
		List<Map<String,Object>> results = this.yundService.queryRongqfkqk(map);
		for(Map<String,Object> res:results){
			String danjlx = (String)res.get("DANJLX");
			
			if("1".equals(danjlx))	//单据类型是空车出厂单或容器返空单
			{
				kcccdj++;
				res.put("DANJLX", "空车出厂单");
			}else{
				rqfkdj++;
				res.put("DANJLX", "容器返空单");
			}
			
			/*String chae = (String)res.get("CHAE");
			Integer chaeInt = 0;
			try {
				chaeInt = Integer.parseInt(chae);
			} catch (NumberFormatException e) {
				
			}
			wfkrq = wfkrq + chaeInt;*/
		}
		
		/*if(wfkrq>0){
			msgColor = "red";
			message = "该车有"+wfkrq+"个数量的容器未返空，不能进行出厂申报操作，请等待容器返空完成后重新扫描或输入卡车号";
		}else{*/
			if(rqfkdj==0 && kcccdj==0){
				msgColor = "red";
				message = "该车找不到任何相关的容器返空单或空车出厂单，不能进行出厂申报操作";
			}else{
				message = "该车有"+rqfkdj+"个容器返空单，"+kcccdj+"个空车出厂单，请继续操作";
			}
		//}
		
		setResult("msgColor",msgColor);
		setResult("message",message);
		
		//旧方法，带有运单号的混合结果
//		List<Map<String,Object>> rqdjList = new ArrayList<Map<String,Object>>();
//		Map<String,Object> rqdj = null;
//		java.util.Iterator<String> keyIter = null;
//		for(Yund temp:l){
//			Map<String,Object> param = new HashMap<String,Object>();
//			param.put("kach", temp.getKach());
//			param.put("usercenter", temp.getUsercenter());
//			param.put("yundh", temp.getYundh());
//			List<Map<String,Object>> ress = this.yundService.queryRongqfkqk(param);
//			for(Map<String,Object> res:ress){
//				
//				rqdj = new HashMap<String,Object>();
//				keyIter = res.keySet().iterator();
//				while(keyIter.hasNext()){
//					String key = keyIter.next();
//					String value = (String)res.get(key);
//					key = key.toLowerCase();
//					rqdj.put(key, value);
//				}
//				rqdjList.add(rqdj);
//			}
//		}
//		setResult("rqfkds",rqdjList);
		
		return AJAX;
	}

	/**
	 * 第二步，扫描输入单据号，得到对应单据信息
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getRongqfkdByDanjbh(){
		Map map = this.getParams();
		String msgColor = "blue";
		String message = "请继续操作";
		List<Map<String,Object>> l = this.yundService.queryRongqfkqk(map);
		if(l.size()<=0){
			msgColor = "red";
			message = "该卡车下找不到单据号为"+(String)(map.get("danjbh"))+"的单据";
			setResult("msgColor",msgColor);
			setResult("message",message);
			return AJAX;
		}
		List<Map<String,Object>> results = new ArrayList<Map<String,Object>>();
		Map<String,Object> temp = null;
		java.util.Iterator<String> keyIter = null;
		for(Map<String,Object> res:l){
			String danjlx = (String)res.get("DANJLX");
			
			if("1".equals(danjlx))	//单据类型是空车出厂单或容器返空单
			{
				res.put("DANJLX", "空车出厂单");
			}else{
				res.put("DANJLX", "容器返空单");
			}
			temp = new HashMap<String,Object>();
			keyIter = res.keySet().iterator();
			while(keyIter.hasNext()){
				String key = keyIter.next();
				String value = (String)res.get(key);
				key = key.toLowerCase();
				temp.put(key, value);
			}
			results.add(temp);
		}
		setResult("msgColor",msgColor);
		setResult("message",message);
		setResult("rqfkds",results);
		return AJAX;
	}
	
	public String updateChucShenbaoQjp(@Param("list") ArrayList<Yund> djlist){
		Map<String,String> map = this.getParams();
		List<Yund> list = this.yundService.queryAllYundForChuc(map);
		LoginUser user = AuthorityUtils.getSecurityUser();
		
		String msgColor = "blue";
		String message = "操作成功";
		try {
			List<Yund> params = new ArrayList<Yund>();
			for(Yund temp:list){
				//从卡车流程表中取流程，必须有流程9，否则系统报错，业务错误
				Map<String,Object> liucParam = new HashMap<String,Object>();
				liucParam.put("usercenter", temp.getUsercenter());
				liucParam.put("quybh", temp.getQuybh());
				liucParam.put("daztbh", temp.getDaztbh());
				liucParam.put("liucbh", "9");
				List<Map<String,Object>> liucResults = yundService.queryLiucdy(liucParam);
				Map<String,Object> liuc = null;
				try {
					liuc = liucResults.get(0);
				} catch (IndexOutOfBoundsException e) {
					e.printStackTrace();
					msgColor = "red";
					message = "缺少相关的卡车流程定义，请联系管理员";
					setResult("msgColor",msgColor);
					setResult("message",message);
					return AJAX;
				}
				
				temp.setZhuangt((String)liuc.get("LIUCBH"));
				temp.setZhuangtmc((String)liuc.get("LIUCMC"));
				temp.setEditor(user.getUsername());
				
				BigDecimal bzsj = (BigDecimal)liuc.get("BIAOZSJ");
				if(bzsj==null ){
					temp.setBiaozsj("0");
				}
				
				temp.setBiaozsj(bzsj+"");
//				this.yundService.updateChuc(temp,djlist);
				params.add(temp);
			}
			List<Yund> djs = new ArrayList<Yund>();
			for(Yund temp:djlist){
				temp.setEditor(user.getUsername());
				djs.add(temp);
			}
			this.yundService.updateChucBatch(params, djs);
		} catch (Exception e) {
			e.printStackTrace();
			msgColor = "red";
			message = "操作失败，请联系管理员";
		}
		setResult("msgColor",msgColor);
		setResult("message",message);
		return AJAX;
	}
}
