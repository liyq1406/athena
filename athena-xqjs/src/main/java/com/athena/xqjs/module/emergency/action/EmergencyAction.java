package com.athena.xqjs.module.emergency.action;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.athena.authority.entity.LoginUser;
import com.athena.component.wtc.WtcResponse;
import com.athena.component.wtc.action.BaseWtcAction;
import com.athena.xqjs.entity.common.LingjGongys;
import com.athena.xqjs.entity.common.Wullj;
import com.athena.xqjs.entity.emergency.EmergencyModel;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.common.service.LingjGongysService;
import com.athena.xqjs.module.common.service.WulljService;
import com.athena.xqjs.module.emergency.service.EmergencyService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.mvc.annotaions.Param;
import com.toft.mvc.dispacher.ActionContext;

/**
 * @see    紧急件报警功能
 * @author wuyichao
 *
 */
@Component
public class EmergencyAction extends BaseWtcAction 
{

	private Logger logger = Logger.getLogger(EmergencyAction.class);
	
	
	@Inject
	private EmergencyService emergencyService;
	
	@Inject
	private WulljService wulljService;
	
	@Inject
	private LingjGongysService lingjGongysService;
	//获取用户中心
	public LoginUser getUserInfo()
	{
		return com.athena.authority.util.AuthorityUtils.getSecurityUser() ;
	}
	
	/**
	 * @see init page
	 * @return
	 */
	public String initEmergency()
	{
		setResult("usercenter", this.getUserInfo().getUsercenter());
		return "success";
	}
	
	
	
	
	/**
	 * @see   查询列表
	 * @param kucjscsb
	 * @return
	 */
	public String queryEmergency(@Param EmergencyModel emergencyModel)
	{
		Object obj = emergencyService.queryEmergency(emergencyModel , this.getParams());
		if(null != obj)
		{
			if(obj instanceof String)
			{
				HttpServletResponse reqResponse = ActionContext.getActionContext().getResponse();
				PrintWriter out = null;
				try
				{
					reqResponse.setContentType("text/html");
					reqResponse.setCharacterEncoding("UTF-8");
					out = reqResponse.getWriter();
				}
				catch (Exception e) 
				{
					logger.error(e);
				}
				finally
				{
					if (out != null) 
					{
						out.print(obj);
						out.flush();
						out.close();
					}
				}
			}
			else if(obj instanceof Map)
			{
				List<EmergencyModel> emergencyModels = (List<EmergencyModel>) ((Map) obj).get("rows");
				if(null != emergencyModels && emergencyModels.size() > 0)
				{
					for (EmergencyModel model : emergencyModels)
					{
						if(null != model.getZiycy() && model.getZiycy().doubleValue() > 0)
						{
							model.setZiyqk("资源充足");
						}
						else if(null != model.getZiycy() && model.getZiycy().doubleValue() < 0)
						{
							model.setZiyqk("资源不足");
						}
						else if(null != model.getZiycy() && model.getZiycy().doubleValue() == 0)
						{
							model.setZiyqk("没有资源");
						}
						if(StringUtils.isBlank(model.getXiaoh_time()))
						{
							model.setXiaoh_time(getJavaTime());
						}
						if(null == model.getBaozgs())
						{
							model.setBaozgs(BigDecimal.ONE);
							model.setYaohsl(model.getUarl());
						}
						if("exportXls".equals(this.getParams().get("exportXls")))
						{
							String pandzt = model.getPandzt();
							String zhuangt = model.getZhuangt();
							if(StringUtils.isNotBlank(pandzt) && pandzt.equalsIgnoreCase("0"))
							{
								model.setPandzt("盘亏");
							}
							else if(StringUtils.isNotBlank(pandzt) && pandzt.equalsIgnoreCase("1"))
							{
								model.setPandzt("盘盈");
							}
							else if(StringUtils.isNotBlank(pandzt) && pandzt.equalsIgnoreCase("2"))
							{
								model.setPandzt("不盈不亏");
							}
							if(StringUtils.isNotBlank(zhuangt) && pandzt.equalsIgnoreCase("0"))
							{
								model.setZhuangt("待处理");
							}
							else if(StringUtils.isNotBlank(zhuangt) && pandzt.equalsIgnoreCase("1"))
							{
								model.setZhuangt("已处理");
							}
							else if(StringUtils.isNotBlank(zhuangt) && pandzt.equalsIgnoreCase("2"))
							{
								model.setZhuangt("不处理");
							}
							else if(StringUtils.isNotBlank(zhuangt) && pandzt.equalsIgnoreCase("3"))
							{
								model.setZhuangt("处理时出错");
							}
						}
					}
				}
				setResult("result",obj );
			}
		}
		return AJAX;
	}
	
	
	/**
	 * @see   批量不处理
	 * @param kucjscsb
	 * @return
	 */
	public String unoperating(@Param("rows") ArrayList<EmergencyModel> emergencyModels)
	{
		if(null != emergencyModels && emergencyModels.size() > 0)
		{
			try
			{
				StringBuffer idsSb = new StringBuffer(); 
				int index = 0 , max = emergencyModels.size();
				for (EmergencyModel emergencyModel : emergencyModels)
				{
					idsSb.append("'").append(emergencyModel.getId()).append("'");
					if(index < max - 1)
					{
						idsSb.append(",");
					}
					index ++;
				}
				if(idsSb.length() > 0)
				{
					emergencyService.unoperating(idsSb.toString(),getUserInfo().getUsername());
					setResult("result","处理完成!" );
				}
			}
			catch (Exception e) 
			{
				setResult("result","处理紧急件状态为不处理时出错!" );
				logger.error(e);
			}
		}
		else
		{
			setResult("result","提交参数有误!" );
		}
		return AJAX;
	}
	
	
	
	/**
	 * @see   批量处理
	 * @param kucjscsb
	 * @return
	 */
	public String operating()
	{
		String jsonStr = this.getParam("data");
		if(StringUtils.isNotBlank(jsonStr))
		{
			List<EmergencyModel> emergencyModels = null;
			emergencyModels = this.getList(jsonStr, EmergencyModel.class);
			if(null != emergencyModels && emergencyModels.size() > 0)
			{
				try
				{
					List<EmergencyModel> operatings = new ArrayList<EmergencyModel>();
					////wuyichao  紧急件报警调用WTC服务 查看是否满足拆分要货令信息
					for (EmergencyModel emergencyModel : emergencyModels) 
					{
						if(checkEmergencyModel(emergencyModel))
						{
							emergencyModel.setGongysbh(emergencyModel.getGongysbh().replaceAll("\\W", " "));
							emergencyModel.setChengysbh(emergencyModel.getChengysbh().replaceAll("\\W", " "));
							Map<String,String> map = new  HashMap<String, String>();
							map.put("usercneter", emergencyModel.getUsercenter());
							map.put("lingjbh", emergencyModel.getLingjbh());
							map.put("gongysbh", emergencyModel.getGongysbh());
							map.put("fenpqh", emergencyModel.getXiaohd().substring(0, 5));
							map.put("waibms", Const.ANX_MS_CD);
							Wullj wullj = wulljService.queryWulljObject(map);
							LingjGongys ljgys = lingjGongysService.select(emergencyModel.getUsercenter(), emergencyModel.getGongysbh(), emergencyModel.getLingjbh());
							if(StringUtils.isBlank(ljgys.getUabzlx())
									|| StringUtils.isBlank(ljgys.getUcbzlx())
									|| null == ljgys.getUaucgs()
									|| null == ljgys.getUcrl()
							)
							{
								setResult("errorMsg", "用户中心" + emergencyModel.getUsercenter() + "零件号" + emergencyModel.getLingjbh() + "零件包装信息不完整(UA包装类型,UC包装类型,UC容量,UAUC个数)!");
								return AJAX;
							}
							Map<String, Object> wtcMap = new HashMap<String, Object>();
							wtcMap.put("usercneter", emergencyModel.getUsercenter());
							wtcMap.put("lingjbh", emergencyModel.getLingjbh());
							wtcMap.put("gongysdm", emergencyModel.getGongysbh());
							wtcMap.put("gonghms", Const.ANX_MS_CD);
							wtcMap.put("xiaohd", emergencyModel.getXiaohd());
							wtcMap.put("cangkbh", wullj == null ? null : wullj.getMudd());
							wtcMap.put("zickbh", wullj == null ? null : wullj.getMudd());
							wtcMap.put("xiehzt", wullj == null ? null : wullj.getXiehztbh());
							try
							{
								WtcResponse wtcResponse = callWtc(emergencyModel.getUsercenter(), "Y0201", wtcMap);
								if (!wtcResponse.get("response").equals(Const.WTC_SUSSCESS)) {
									emergencyModel.setBeiz("未能满足执行层拆分要货令条件,该零件部分信息可能于当天维护,请于明天操作!");	
									emergencyModel.setZhuangt("3");
									//修改状态
									emergencyService.updateEmergency(emergencyModel);
								}
								else
								{
									operatings.add(emergencyModel);
								}
							}
							catch (Exception e) {
								setResult("errorMsg", "调用WTC服务失败,请联系管理人员!");
								e.printStackTrace();
								logger.error("调用WTC服务失败", e);
								return AJAX;
							}
						}
						else
						{
							emergencyModel.setBeiz("参数有缺失!");
							emergencyModel.setZhuangt("3");
							emergencyService.updateEmergency(emergencyModel);
						}
					}
					////wuyichao  紧急件报警调用WTC服务 查看是否满足拆分要货令信息
					String resultStr = "所提交的数据因某些原因无法生成临时订单!";
					if(operatings.size() > 0)
					{
						resultStr = emergencyService.operating(operatings,getUserInfo().getUsername());
					}
					setResult("result",resultStr );
				}
				catch (Exception e) 
				{
					setResult("result","处理紧急件状态为处理时出错!" );
					logger.error(e);
				}
			}
			else
			{
				setResult("result","提交参数有误!" );
			}
		}
		return AJAX;
	}
	
	
	
	/**
	 * @see   重置
	 * @param emergencyModels
	 * @return
	 */
	public String reset(@Param("rows") ArrayList<EmergencyModel> emergencyModels)
	{
		if(null != emergencyModels && emergencyModels.size() > 0)
		{
			try
			{
				StringBuffer idsSb = new StringBuffer(); 
				int index = 0 , max = emergencyModels.size();
				for (EmergencyModel emergencyModel : emergencyModels)
				{
					idsSb.append("'").append(emergencyModel.getId()).append("'");
					if(index < max - 1)
					{
						idsSb.append(",");
					}
					index ++;
				}
				if(idsSb.length() > 0)
				{
					emergencyModels = (ArrayList<EmergencyModel>) emergencyService.findByIds(idsSb.toString());
					setResult("result",emergencyModels );
				}
			}
			catch (Exception e) 
			{
				setResult("message","处理紧急件状态为不处理时出错!" );
				logger.error(e);
			}
		}
		else
		{
			setResult("message","提交参数有误!" );
		}
		return AJAX;
	}
	
	
	
	/**
	 * @see 查询供应商所对应的所有信息 
	 * @return
	 */
	public String validateEmergencyGongys()
	{
		Map<String,String> paramMap = this.getParams();
		Map<String, Object> resultMap =  new HashMap<String, Object>();
		String gongysbh = paramMap.get("gongysbh");
		String id = paramMap.get("id");
		String lingjbh = paramMap.get("lingjbh");
		String usercenter = paramMap.get("usercenter");
		if(StringUtils.isBlank(id) || StringUtils.isBlank(gongysbh) ||StringUtils.isBlank(lingjbh) ||StringUtils.isBlank(usercenter) )
		{
			//参数有误 id不存在
			resultMap.put("message", "参数传递出错!");
		}
		else
		{
			gongysbh = gongysbh.replaceAll(",", " ");
			paramMap.put("gongysbh", gongysbh);
			EmergencyModel emergencyModel = emergencyService.findById(paramMap);
			if(null != emergencyModel)
			{
				String zhuangt = emergencyModel.getZhuangt();
				if(StringUtils.isNotBlank(zhuangt))
				{
					//待处理，处理时出错
					if(zhuangt.equalsIgnoreCase("0") || zhuangt.equalsIgnoreCase("3"))
					{
						List<EmergencyModel> wulljList = emergencyService.queryWulljxx(paramMap);
						if(null != wulljList && wulljList.size() > 0)
						{
							EmergencyModel wulljXX = wulljList.get(0);
							emergencyModel.setGongysbh(wulljXX.getGongysbh());
							emergencyModel.setChengysbh(wulljXX.getChengysbh());
							emergencyModel.setGongysfe(wulljXX.getGongysfe());
							StringBuffer gcbhSb = new StringBuffer();
							gcbhSb.append("'").append(wulljXX.getGongysbh()).append("','").append(wulljXX.getChengysbh()).append("'");
							paramMap.put("gcbh", gcbhSb.toString());
							List<EmergencyModel> gcbhList = emergencyService.queryGcbhxx(paramMap);
							if(null != gcbhList)
							{
								for (EmergencyModel gcbhXX : gcbhList)
								{
									if(StringUtils.isNotBlank(gcbhXX.getGongysbh()) && gcbhXX.getGongysbh().equalsIgnoreCase(wulljXX.getGongysbh()))
									{
										emergencyModel.setGongysmc(gcbhXX.getGongysmc());
									}
									if(StringUtils.isNotBlank(gcbhXX.getChengysbh()) && gcbhXX.getChengysbh().equalsIgnoreCase(wulljXX.getChengysbh()))
									{
										emergencyModel.setChengysmc(gcbhXX.getChengysmc());
									}
								}
							}
							//返回新的报警信息
							resultMap.put("result", emergencyModel);
						}
						else
						{
							//供应商信息不正确，不能在物流路径内找到
							resultMap.put("message", "未找到所对应的物流路径!");
						}
					}
					else
					{
						//参数有误  状态
						resultMap.put("message", "只能操作状态为待处理或处理时出错的紧急件信息!");
					}
				}
				else
				{
					//参数有误  状态
					resultMap.put("message", "只能操作状态为待处理或处理时出错的紧急件信息!");
				}
			}
			else
			{
				//参数有误 id不存在
				resultMap.put("message", "参数传递出错!");
			}
		}
		setResult("result", resultMap);
		return AJAX;
	}
	
	
	public static String getJavaTime() 
	{
		Date date = new Date(System.currentTimeMillis());
		date.setHours(date.getHours() + 3);
		Format df = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		return df.format(date);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private  List getList(String jsonString, Class beanClass)
	{
		JSONArray jsonArray = null;
		List resultList = null;
		try
		{
			jsonArray = JSONArray.fromObject(jsonString);
			resultList = new ArrayList();
			for(Iterator iter = jsonArray.iterator(); iter.hasNext();)
			{      
				JSONObject jsonObject = (JSONObject)iter.next(); 
				resultList.add(JSONObject.toBean(jsonObject, beanClass));   
			}
		}
		catch(Exception e)
		{
			logger.error(e);
		}
		return resultList;
	}
	
	private boolean checkEmergencyModel(EmergencyModel emergencyModel)
	{
		
		if(null == emergencyModel || StringUtils.isBlank(emergencyModel.getId()) || StringUtils.isBlank(emergencyModel.getLingjbh())
		    || StringUtils.isBlank(emergencyModel.getLingjbh()) || StringUtils.isBlank(emergencyModel.getUsercenter()) ||
		    StringUtils.isBlank(emergencyModel.getChengysbh()) || null == emergencyModel.getYaohsl() || StringUtils.isBlank(emergencyModel.getXiaoh_time())
		    || StringUtils.isBlank(emergencyModel.getXiaohd())
		)
			return false;
		return true;
	}
}
