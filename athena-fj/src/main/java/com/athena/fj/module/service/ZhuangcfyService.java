package com.athena.fj.module.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.log4j.Logger;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.fj.entity.Yaocjh;
import com.athena.fj.entity.Zhuangcfy;
import com.athena.fj.entity.Zhuangcmy;
import com.athena.fj.module.common.LoaderPrintProperties;
import com.athena.print.Constants;
import com.athena.print.controller.GetPrintService;
import com.athena.util.date.DateUtil;
import com.athena.util.exception.ServiceException;
import com.athena.util.uid.CreateUid;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.ibatis.support.AbstractIBatisDao;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * <p>
 * Title:装车发运Service类
 * </p>
 * <p>
 * Description:定义装车发运业务逻辑方法
 * </p>
 * <p>
 * Copyright: Copyright (c) 2012
 * </p>
 * <p>
 * Company: 软通动力
 * </p>
 * 
 * @author 贺志国
 * @E-mail zghe@isoftstone.com
 * @version v1.0
 * @date 2012-1-17
 */
@Component
public class ZhuangcfyService extends BaseService<Zhuangcfy>{

	private static String ZT_SHIFZC = "1";//是否装车，0未装车，1已装车
	private static String ZT_YAOHLZT = "66"; //要货令状态，06发运 --仓库有变更，由06改为66 hzg 2012-11-16
	private static String ZT_BEIHDMX = "4"; //BIAOS，3已打印 仓库3标识入库，有冲突，由3改为4  gswang 2012-05-07
	private final int SHEET_AREA_BODY_HEAD = 1;//发货通知单表单上部区域
	private final int SHEET_AREA_BODY_BOTTOM = 2;//发货通知单表单下部区域
	
	private static Logger log = Logger.getLogger(ZhuangcfyService.class.getName());
	//配置文件路径
	private final String fileName="com/athena/fj/config/print.properties";  
	
	public void setBaseDao(AbstractIBatisDao baseDao){
		this.baseDao = baseDao;
	}
	
	/**
	 * 配载单下的UC标签记录查询
	 * @param map （参数为配载单号、用户中心、UC卡号）
	 * @return list 单条UC记录
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> queryUCInfo(Map<String, String> param) throws ServiceException {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).select("zhuangcfy.queryUC", param);
	}

	/**
	 * 获取UC列表
	 * @param param UC卡号（参数以“,”分隔，用于in打件查询）
	 * @return List UC记录集合
	 */
	@SuppressWarnings("unchecked")
	public List<Zhuangcmy> queryUCList(Map<String, String> param) throws ServiceException {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).select("zhuangcfy.queryUCList", param);
	}
	
	/**
	 * 查询配载单车牌
	 * @param param 配载单号
	 * @return 车牌号
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> queryPeizdChep(Map<String, String> param) throws ServiceException {
		return  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).select("zhuangcfy.queryPeizdChep", param);
	}
	
	/**
	 * 装车和装车明细表中插入数据
	 * @param ucList uc集合
	 */
	@Transactional
	public void insertZhuangc(Map<String,String> ucMap,Zhuangcfy zhuangcfyBean,List<String> xiehdList,String userName) throws ServiceException{
		Map<String,String> param = new HashMap<String,String>();
		param.putAll(ucMap);
		param.put("cangkbh", zhuangcfyBean.getCangkbh());
		Map<String,String> rongqcMap = (Map)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).selectObject("zhuangcfy.queryCangkRongqc", param);
		param.putAll(rongqcMap);
		List<Map<String,String>> rongqzz = new ArrayList<Map<String,String>>();
		//根据卸货点来生成发货通知单，有几个卸货点生成几个发货通知单
		for(String xiehd : xiehdList){
			//生成装车发运序列号
			String zhuangcfydh = this.getZhungcdh(zhuangcfyBean.getUsercenter(),String.valueOf( this.getSequence("seq_zhuangcdh")),8);
			zhuangcfyBean.setZhuangcdh(zhuangcfydh);
			//zhuangcfyBean.setZhuangcdh(billUtil.createDJNum(zhuangcfyBean.getUsercenter(), 8, 3, null));
			//根据UC号和卸货点号查询UC集合
			ucMap.put("xiehd", xiehd);
			List<Zhuangcmy> ucList = this.queryUCList(ucMap);//根据卸货点打印不同的bill单
			//向装车表中插入数据
			//mantis bug:0006307  ，UC标签对应的要货令中的供应商代码取其一
			zhuangcfyBean.setGongysbm(ucList.get(0).getGongysdm());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("zhuangcfy.insertZhuangcfy", zhuangcfyBean);
			//向装车明细表中插入数据
			for(Zhuangcmy zhuangcmyBean : ucList){
				zhuangcmyBean.setZhuangcdh(zhuangcfydh);
				zhuangcmyBean.setCreator(userName);
				zhuangcmyBean.setCreateTime(DateUtil.curDateTime());
				zhuangcmyBean.setEditor(userName);
				zhuangcmyBean.setEditTime(DateUtil.curDateTime());
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("zhuangcfy.insertZhuangcmy", zhuangcmyBean);
			}
			zhuangcfyBean.setDaodTime((String)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).selectObject("zhuangcfy.queryDaodsj", zhuangcfyBean));
			//打印发运通知单
			this.batchInsertPrintBusinessTable(ucList, xiehd, zhuangcfyBean, userName);   //gswang test  修改完成需要释放开
			//更新容器信息
			this.insertRongq(param, ucMap, zhuangcfyBean, xiehdList, userName,rongqzz);
		}
		
		//更新备货单明细表biaos状态为3
		ucMap.put("biaos", ZT_BEIHDMX);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("zhuangcfy.updateBeihdmxZT", ucMap);
		
		//更新要货令状态为已装车06
		Map<String,String> yhlParam = new HashMap<String,String>();
		yhlParam.put("yaohlzt", ZT_YAOHLZT);
		yhlParam.put("yaohlhs", zhuangcfyBean.getYaohlhs());
		yhlParam.put("usercenter",zhuangcfyBean.getUsercenter());
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("zhuangcfy.updateYaohlZT", yhlParam);
		
		//更新配载单车牌和装车状态
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("chep", zhuangcfyBean.getChep());
		//配载单下的UC号是否全部装车
		String numb = (String)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).selectObject("zhuangcfy.getCountOfUCH", zhuangcfyBean.getPeizdh());
		if(Integer.parseInt(numb)==0){//配载单下的UCH全部装车
			paramMap.put("shifzc", ZT_SHIFZC);
		}
		paramMap.put("peizdh", zhuangcfyBean.getPeizdh());
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("zhuangcfy.updatePeizdChep", paramMap);
		//修改要车明细表中的要车计划状态  hzg 2012-08-17 add
		Map<String,String> ycmxMap = new HashMap<String,String>();
		ycmxMap.put("usercenter",zhuangcfyBean.getUsercenter());
		ycmxMap.put("peizdh", zhuangcfyBean.getPeizdh());
		String yaocmxh = (String)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).selectObject("zhuangcfy.getYaocmxhOfpeizd", ycmxMap);
		Map<String,String> map = new HashMap<String,String>();
		map.put("yaocmxh", yaocmxh);
		map.put("yaoczt", Yaocjh.STATUE_ZC);//要车状态更改为已装车
		map.put("editor",userName);
		map.put("editTime",DateUtil.curDateTime());
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("peizjh.updateYaocjhzt", map);
	}

	/**
	 * 返回指定格式的装车单号
	 * @author 贺志国
	 * @date 2012-7-26
	 * @param uc 用户中心
	 * @param xlh 序列号
	 * @param len 长度
	 * @return String X00000001
	 */
	public String getZhungcdh(String uc,String xlh,int len){
		// 用户中心为空
		if (uc == null || uc.trim().length() <= 1){
			throw new ServiceException("用户中心为空,或者长度不正确!");
		}
		// 得到用户中心第二位字符
		String secondChar = uc.substring(1, 2);
		String zhcdh = "";
		// 序列号不足8位补0
		if (xlh.length() != 0) {
			while(xlh.length()<len)
			{
				xlh = "0"+xlh;
			}
			zhcdh = secondChar+xlh;
		}
		
		return zhcdh;
	}
	/**
	 * 查询UC表供应商编码
	 * @param uch UC卡号
	 * @return Gongysdm 供应商编码
	 */
	public String queryUCGongysdm(String uch) throws ServiceException{
		return (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).selectObject("zhuangcfy.queryUCGongysdm", uch);
	}
	
	/**
	 * 查询fj_peizjh表仓库编码
	 * @author 贺志国
	 * @date 2012-7-12
	 * @param peizdh
	 * @return cangkbh 仓库编码
	 */
	public String queryCangkbhOfPeizd(Map<String,String> params) throws ServiceException{
		return (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).selectObject("zhuangcfy.queryCangkbhOfPeizd", params);
	}

	/**
	 * 运输路线客户查询
	 * @author 贺志国
	 * @date 2012-7-9
	 * @param params 用户中心
	 * @return 运输路线客户集合
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectKehLxz(Map<String,String> params) throws ServiceException{
		return  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).select("zhuangcfy.queryKehLxz",params);
	}
	
	
	/**
	 * 封装客户路线组
	 * @author 贺志国
	 * @date 2012-7-9
	 * @param list
	 * @return Map<String,List<String>> 运输路线客户KEY/VALUE集合
	 */
	public Map<String,List<String>> wrapKehLxz(List<Map<String,String>> list) throws ServiceException{
		Map<String,List<String>> lxz =  new HashMap<String,List<String>>();
		for(Map<String,String> khLxzmap : list){
			if(lxz.containsKey(khLxzmap.get("YUNSLXBH"))){
				lxz.get(khLxzmap.get("YUNSLXBH")).add(khLxzmap.get("KEHBH"));
			}else{
				List<String> khList = new ArrayList<String>();
				khList.add(khLxzmap.get("KEHBH"));
				lxz.put(khLxzmap.get("YUNSLXBH"), khList);
			}
		}
		return lxz;
	}
	
	/**
	 * 根据路线组查询路线组下的客户集合
	 * @author 贺志国
	 * @date 2012-7-10
	 * @param params 用户中心、路线组编号
	 * @return List<String>
	 */
	@SuppressWarnings("unchecked")
	public List<String> getKehOfLxz(Map<String,String> params) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).select("zhuangcfy.queryKehOfLxz",params);
	}
	
	
	/**
	 * 客户分组查询
	 * @author 贺志国
	 * @date 2012-7-12
	 * @param params UC号、用户中心
	 * @return 客户分组
	 */
	@SuppressWarnings("unchecked")
	public List<String> getKehOfUCH(Map<String,String> params){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).select("zhuangcfy.queryKehOfUCH",params);
	}
	
	/**
	 * 查询UC卸货点分组
	 * @author 贺志国
	 * @date 2012-7-12
	 * @param params UC号、用户中心
	 * @return 卸货点分组集合
	 */
	@SuppressWarnings("unchecked")
	public List<String> getXiehdOfUCH(Map<String,String> params){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).select("zhuangcfy.queryXiehdOfUCH",params);
	}
	
	/**
	 * 查询要货令号
	 * @author 贺志国
	 * @date 2012-7-13
	 * @param params UC号、用户中心
	 * @return List<String> 要货令号集合
	 */
	@SuppressWarnings("unchecked")
	public List<String> getYaohlhOfUCH(Map<String,String> params){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).select("zhuangcfy.queryYaohlhOfUCH", params);
	}
	
	/**
	 * 用于easyMock测试的临时方法
	 * @author 贺志国
	 * @date 2012-9-22
	 * @return
	 */
	public int getCalculateResult(Map<String,String> params){
		List<String> list1 = getXiehdOfUCH(params);
		List<String> list2 = getYaohlhOfUCH(params);
		return 1+Integer.parseInt(list1.get(0))+Integer.parseInt(list2.get(1));
	}
	
	
	/**
	 * 批量插入打印业务表
	 * @author 贺志国
	 * @param bean 配载计划实体bean
	 * @param pzdmx 配载单明细集合
	 * @date 2012-6-5
	 */
	@Transactional
	public void batchInsertPrintBusinessTable(List<Zhuangcmy> ucList,String xiehd,Zhuangcfy bean,String userName){
		log.info("打印开始，插入打印信息到相关打印表");
		//获得工厂编码
		Map<String,String> xhdMap = new HashMap<String,String>();
		xhdMap.put("xiehdbh", xiehd);
		xhdMap.put("usercenter", bean.getUsercenter());
		String gongcbm = (String)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).selectObject("zhuangcfy.queryGongcbm",xhdMap);
		
		//生成打印作业编号
		String qid = CreateUid.getUID(20);
		//设置打印队列主表参数并将值插入ckx_print_qtask_main表
		this.setPrintMainParas(bean,qid,userName);
		//设置单据打印队列表参数并将值插入ckx_print_qtask_info表
		this.setPrintInfoParas(bean, qid);
		//表单的两个区域，1为表单上部区域和2为表单下部区域，中间为列表
		//设置1区域并将值插入到ckx_print_qtask_sheet表
		this.setPrintSheetParas(bean,SHEET_AREA_BODY_HEAD,qid,gongcbm,xiehd,userName);
		//设置2区域并将值插入到ckx_print_qtask_sheet表
		this.setPrintSheetParas(bean,SHEET_AREA_BODY_BOTTOM,qid,gongcbm,xiehd,userName);
		
		//循环清单列表
		for(int i=0;i<ucList.size();i++){
			//设置打印列表参数并将值播入ckx_print_qtask_list表
			this.setPrintListParas(ucList,qid,bean,i);
		}
		log.info("开始调用打印接口，等待打印");
		//开始调用打印接口 
//		this.callPrintInterface(getPropertiesValue(fileName,"print_address"), qid);  
		log.info("打印完成");
	}
	
	
	/**
	 * 获取配置信息值
	 * @author 贺志国
	 * @date 2012-9-25
	 * @return 配置信息
	 */
	public String getPropertiesValue(String fname,String key){
		return LoaderPrintProperties.getPropertiesMap(fname).get(key);
	}
	
	/**
	 * 调用打印接口打印发货通知单
	 * @author 贺志国
	 * @date 2012-9-24
	 * @param address 打印接口服务地址
	 * @param qid 主任务ID
	 */
	public void callPrintInterface(String address,String qid){
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean ();
		factory.setServiceClass(GetPrintService.class); 
		factory.setAddress(address);
		factory.create();    
		GetPrintService hs=  (GetPrintService) factory.create();
		hs.getQtaskController(qid); 
	}
	
	/**
	 * 设置单据打印队列表参数并将值插入ckx_print_qtask_main表
	 * @author 贺志国
	 * @date 2012-9-22
	 * @param printMainMap
	 * @param bean
	 * @param qid
	 * @param userName
	 * @return map
	 */
	public void setPrintMainParas(Zhuangcfy bean,String qid,String userName){
		//获取打印机组编号
		Map<String,String> djMap = this.getDayjzbh(bean, userName,new HashMap<String,String>());
		//定义参数map
		Map<String,String> printMainMap = new HashMap<String, String>();
		printMainMap.put("usercenter", bean.getUsercenter());
		printMainMap.put("qid", qid);
		printMainMap.put("saccount", userName);
		printMainMap.put("pgid", djMap.get("dayjzbh"));    //打印机组编号
		printMainMap.put("scode", djMap.get("djzbh"));   //单据组编号
		printMainMap.put("storagecode", bean.getCangkbh());
		printMainMap.put("sdevicecode", Constants.DEVICECODE);
		printMainMap.put("createtime", DateUtil.curDateTime());
		printMainMap.put("status", String.valueOf(Constants.TASK_STATUS_PRE));
		printMainMap.put("biaos", "0");
		//插入打印队列主表
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("zhuangcfy.insertPrintQtaskMain", printMainMap);
	}
	
	/**
	 * 获取打印机组编号
	 * @author 贺志国
	 * @date 2012-9-24
	 * @param bean 实体类
	 * @param userName 用户编号
	 * @return 打印机组编号
	 */
	public Map<String,String> getDayjzbh(Zhuangcfy bean,String userName,Map<String,String> param){
		//保存返回值Map
		Map<String,String> result = new HashMap<String,String>();
		//参数Map
		//Map<String,String> param = new HashMap<String,String>();
		param.put("usercenter", bean.getUsercenter());
		param.put("username", userName);
		//查询打印用户信息表获得用户组编号
		String yhzbh = getYhzbh(param);
		//查询打印字典信息表获得单据组编号
		param.put("zidbm", Constants.PRINT_DICT_FHTZD);
		param.put("cangkbh", bean.getCangkbh());
		String djzbh = getDjzbh(param);
		param.put("yhzbh", yhzbh);
		param.put("djzbh", djzbh);
		//查询打印权限表获得打印机组编号
		String dayjzbh = getDayjzbh(param);
		result.put("djzbh", djzbh);
		result.put("dayjzbh", dayjzbh == null ?"":dayjzbh);
		return result;
	}
	/**
	 * 获得打组用户组编号
	 * @author 贺志国
	 * @date 2012-9-25
	 * @param param
	 * @return
	 */
	public String getYhzbh(Map<String,String> param){
		String yhzbh = (String)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).selectObject("zhuangcfy.queryYhzbhOfPrintUserInfo", param);
		return yhzbh;
	}
	
	/**
	 * 获得单据组编号
	 * @author 贺志国
	 * @date 2012-9-25
	 * @param param
	 * @return
	 */
	public String getDjzbh(Map<String,String> param){
		String djzbh = (String)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).selectObject("zhuangcfy.queryDjzbhOfPrintDictInfo", param);
		return djzbh;
	}
	
	/**
	 * 获得打印机组编号
	 * @author 贺志国
	 * @date 2012-9-25
	 * @param param
	 * @return
	 */
	public String getDayjzbh(Map<String,String> param){
		String dayjzbh = (String)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).selectObject("zhuangcfy.queryDyjzbhOfPrintRight", param);
		return dayjzbh;
	}
	
	/**
	 * 设置单据打印队列表参数并将值插入ckx_print_qtask_info表
	 * @author 贺志国
	 * @date 2012-9-23
	 * @param printInfoMap 
	 * @param bean
	 * @param qid
	 * @return map
	 */
	public void setPrintInfoParas(Zhuangcfy bean,String qid){
		//定义参数map
		Map<String,String> printInfoMap = new HashMap<String, String>();
		printInfoMap.put("usercenter", bean.getUsercenter());
		printInfoMap.put("qid", qid);
		printInfoMap.put("seq",bean.getZhuangcdh()); 
		printInfoMap.put("modelnumber",Constants.PRINT_DICT_FHTZD);
		printInfoMap.put("printnumber",String.valueOf(Constants.PRINT_PRINTNUMBER));
		printInfoMap.put("printunitcount",String.valueOf(Constants.PRINT_PRINTUNITCOUNT));
		printInfoMap.put("printtype",Constants.PRINT_PRINTTYPE_A);
		printInfoMap.put("status",String.valueOf(Constants.TASK_STATUS_NOSEND)); 
		printInfoMap.put("quyzs",Constants.PRINT_QUY_TWO); 
		//插入单据打印队列表
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("zhuangcfy.insertPrintQtaskInfo", printInfoMap);
	}
	
	/**
	 * 设置表单明细参数并将值插入到ckx_print_qtask_sheet表
	 * @author 贺志国
	 * @date 2012-9-23
	 * @param bean
	 * @param qid
	 * @param gongcbm
	 * @param xiehd
	 * @param userName
	 * @return map
	 */
	public void setPrintSheetParas(Zhuangcfy bean,int area ,String qid,String gongcbm,String xiehd,String userName){
		//定义参数map
		Map<String,String> printSheetMap = new HashMap<String, String>();
		//获得系统当前时间
		String currDate = DateUtil.curDateTime();
		//创建StringBuffer
		StringBuffer sheetSpars = new StringBuffer();
		//设置表单明细参数
		printSheetMap.put("qid", qid);
		printSheetMap.put("seq",bean.getZhuangcdh());
		
		if(area==SHEET_AREA_BODY_HEAD){ //表单第1区域
			sheetSpars.append("[{\"1-2\":\""+bean.getZhuangcdh()+"\",\"1-4\":\"");
			sheetSpars.append(bean.getZhuangcdh()+"\",\"1-5\":\""+gongcbm+"\",\"1-7\":\"");
			sheetSpars.append(bean.getUsercenter()+"\",\"1-9\":\""+bean.getYunssbm()+"\",\"1-11\":\"");
			sheetSpars.append(bean.getGongsmc()+"\",\"1-13\":\""+bean.getChep()+"\",\"1-21\":\""+xiehd+"\",\"1-15\":\""+bean.getDaodTime()+"\"}]");
			printSheetMap.put("area", String.valueOf(Constants.PRINT_SHEET_GRID));
			printSheetMap.put("spars", sheetSpars.toString());
		}else if(area==SHEET_AREA_BODY_BOTTOM){//表单第2区域
			sheetSpars.append("[{\"3-2\":\""+userName+"\",\"3-7\":\""+currDate+"\"}]");
			printSheetMap.put("area", String.valueOf(Constants.PRINT_SHEET_GRIDS));
			printSheetMap.put("spars", sheetSpars.toString());
		}
		//将值插入表单明细表
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("zhuangcfy.insertPrintQtaskSheet", printSheetMap);
	}
	
	/**
	 * 设置打印列表参数并将值播入ckx_print_qtask_list表
	 * @author 贺志国
	 * @date 2012-9-23
	 * @param ucList
	 * @param qid
	 * @param bean
	 * @param i
	 * @param listSpars
	 * @return map
	 */
	public void setPrintListParas(List<Zhuangcmy> ucList,String qid,Zhuangcfy bean,int i){
		StringBuffer listSpars = new StringBuffer();
		//定义参数map
		Map<String,String> printListMap = new HashMap<String, String>();
		listSpars.append("[{\"2-1\":\""+(i+1)+"\",\"2-2\":\""+ucList.get(i).getYaohlbh()+"\",\"2-3\":\"");
		listSpars.append(ucList.get(i).getLingjbh()+"\",\"2-4\":\"");
		listSpars.append(ucList.get(i).getLingjmc()+"\",\"2-5\":\"");
		listSpars.append(ucList.get(i).getLingjsl()+"\",\"2-6\":\"");
		listSpars.append(ucList.get(i).getDanw()+"\",\"2-7\":\"");
		listSpars.append(ucList.get(i).getPich()+"\",\"2-8\":\"");
		listSpars.append(ucList.get(i).getUaxh()+"\",\"2-9\":\"");
		listSpars.append(ucList.get(i).getUcxh()+"\",\"2-10\":\"");
		listSpars.append(ucList.get(i).getUcgs()+"\",\"2-11\":\"");
		listSpars.append(ucList.get(i).getUcrl()+"\",\"2-12\":\"");
		listSpars.append(ucList.get(i).getGongysdm()+"\",\"2-13\":\"");
		listSpars.append(ucList.get(i).getGongysmc()+"\",\"2-14\":\"").append(ucList.get(i).getUch()+"\"}]");
		//设置清单明细参数
		printListMap.put("qid", qid);
		printListMap.put("seq",bean.getZhuangcdh());
		printListMap.put("area", "2");
		printListMap.put("xuh", String.valueOf(i+1)); 
		printListMap.put("spars", listSpars.toString());
		//插入清单明细
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("zhuangcfy.insertPrintQtaskList", printListMap);
	}

	/**
	 * 701装车发运计算容器流水帐和总帐
	 * @author 王国首
	 * @date 2012-9-23
	 * @param printInfoMap 
	 * @param bean
	 * @param qid
	 * @return map
	 */
	public void insertRongq(Map<String,String> param,Map<String,String> ucMap,Zhuangcfy zhuangcfyBean,List<String> xiehdList,String userName,List<Map<String,String>> rongqzz){
		List<Map<String,String>> ucbqList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).select("zhuangcfy.queryUcbq", ucMap);
		List<Map<String,String>> ucbqOutList = new ArrayList<Map<String,String>>();
		for(int i = ucbqList.size()-1;i>=0;i--){
			Map<String,String> ucbqMap = ucbqList.get(i);
			if(param.get("JIZQBH").equals(ucbqMap.get("JIZQBH"))){
				ucbqMap.put("CHENGYS", "000000");
				ucbqMap.put("GONGYSDM", "000000");
				ucbqMap.put("JZLX", "1");
			}else{
				ucbqMap.put("CHENGYS", zhuangcfyBean.getYunssbm());
				//  2013-08-30  供应商，承运商全部修改为记录承运商。
				ucbqMap.put("GONGYSDM", zhuangcfyBean.getYunssbm());
				ucbqMap.put("JZLX", "2");
			}
			ucbqMap.put("USERCENTER", param.get("usercenter"));
			ucbqMap.put("WULD", param.get("RONGQCBH"));
			ucbqMap.put("RONGQCBH", param.get("RONGQCBH"));
			ucbqMap.put("SJWULD", param.get("cangkbh"));
			ucbqMap.put("JIZQBH", param.get("JIZQBH"));
		}
		this.insertRongqzz(param, ucMap, zhuangcfyBean, xiehdList, userName, rongqzz, ucbqList);
	}
	
	
	public void insertRongqzz(Map<String,String> param,Map<String,String> ucMap,Zhuangcfy zhuangcfyBean,List<String> xiehdList,String userName,List<Map<String,String>> rongqzz,List<Map<String,String>> ucbqList){
		Map<String,Map<String,String>> rqls = new HashMap<String,Map<String,String>>();
		for(Map<String,String> ucbqMap: ucbqList){
			String shangxlx = ucbqMap.get("SHANGXLX");
			if(shangxlx != null && shangxlx.length()>0){
				String idh = param.get("usercenter")+zhuangcfyBean.getZhuangcdh()+shangxlx+ucbqMap.get("GONGYSDM")+ucbqMap.get("CHENGYS");
				if(rqls.get(idh) != null){
					Map<String,String> rqlsMap = rqls.get(idh);
					rqlsMap.put("RONGQSL", String.valueOf(Integer.parseInt(String.valueOf(rqlsMap.get("RONGQSL")))+1));
				}else{
					Map<String,String> uaMap = new HashMap<String,String>();
					uaMap.putAll(ucbqMap);
					uaMap.put("RONGQSL", "1");
					uaMap.put("RONGQXH", shangxlx);
					uaMap.put("JSFHDH", zhuangcfyBean.getZhuangcdh());
					uaMap.put("JIESRQ", DateUtil.curDateTime());
					uaMap.put("CAOZY", userName);
					uaMap.put("ZAIY", zhuangcfyBean.getPeizdh());
					uaMap.put("CAOZM","R36");
					rqls.put(idh, uaMap); 
				}
				String ucxh = ucbqMap.get("UCXH");
				int ucgs = Integer.parseInt(String.valueOf(ucbqMap.get("UCGS")));
				if(ucxh != null && ucxh.length()>0 && !shangxlx.equals(ucxh) && ucgs>0){
					String idhuc = param.get("usercenter")+zhuangcfyBean.getZhuangcdh()+ucxh+ucbqMap.get("GONGYSDM")+ucbqMap.get("CHENGYS");
					if(rqls.get(idhuc) != null){
						Map<String,String> rqlsMap = rqls.get(idhuc);
						rqlsMap.put("RONGQSL", String.valueOf(Integer.parseInt(String.valueOf(rqlsMap.get("RONGQSL")))+ucgs));
					}else{	
						Map<String,String> ucxhMap = new HashMap<String,String>();
						ucxhMap.putAll(ucbqMap);
						ucxhMap.put("RONGQSL", String.valueOf(ucgs));
						ucxhMap.put("RONGQXH", ucxh);
						ucxhMap.put("JSFHDH", zhuangcfyBean.getZhuangcdh());
						ucxhMap.put("JIESRQ", DateUtil.curDateTime());
						ucxhMap.put("CAOZY", userName);
						ucxhMap.put("ZAIY", zhuangcfyBean.getPeizdh());
						ucxhMap.put("CAOZM","R36");
						rqls.put(idhuc, ucxhMap);
					}
				}
			}
		}
		final Iterator<String> it = rqls.keySet().iterator();
		while(it.hasNext()){
			Map<String,String> rqlsMap = rqls.get(it.next());
			rqlsMap.put("JIESRQ", DateUtil.curDateTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("zhuangcfy.insertRONGQZWLS",rqlsMap);
			rqlsMap.put("RONGQSL", String.valueOf(Integer.parseInt(String.valueOf(rqlsMap.get("RONGQSL")))*(-1)));
			if("2".equals(rqlsMap.get("JZLX"))){
				rqlsMap.put("WULD", rqlsMap.get("JIZQBH"));
				insertRqzz(rqlsMap);
				rqlsMap.put("WULD", rqlsMap.get("RONGQCBH"));
			}
			rqlsMap.put("JZLX", "1");
			rqlsMap.put("CHENGYS", "000000");
			rqlsMap.put("GONGYSDM", "000000");
			insertRqzz(rqlsMap);
		}
	}
	
	public void insertRqzz(Map<String,String> rqlsMap){
		rqlsMap.put("LIUSH", String.valueOf(this.getSequence("seq_zhuangcdh")));
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("zhuangcfy.insertRONGQZZ",rqlsMap);
	}
	
	/**
	 * 返回sqlmap-xxx.xml配置文件中的namespace属性名
	 */
	@Override
	protected String getNamespace(){
		return "zhuangcfy";
	}
	
	
}
