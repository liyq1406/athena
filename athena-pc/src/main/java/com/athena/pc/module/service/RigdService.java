package com.athena.pc.module.service;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.pc.common.CollectionUtil;
import com.athena.pc.entity.Rigdpcmx;
import com.athena.util.exception.ServiceException;
import com.toft.core3.Pageable;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;
/**
 * <p>
 * Title:日滚动排产业务逻辑类
 * </p>
 * <p>
 * Description:定义日滚动排产逻辑方法
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
 * @date 2012-3-7
 */
@Component
public class RigdService extends BaseService<Rigdpcmx> {

	/**
	 * 日滚动排产计划明细查询
	 * @author hzg
	 * @date 2012-3-8
	 * @param page 分页显示
	 * @param param 查询参数
	 * @return Map<String,Object>
	 */
	public Map<String,Object> select(Pageable page,Map<String,String> param) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).selectPages("rigd.queryRigd",param,page);
	}
	
	/**
	 * 查询产线为Y的状态记录数
	 * @author 贺志国
	 * @date 2012-4-13
	 * @param param
	 * @return 当天生产线排产状态为Y的个数 pcCount
	 */
	public String selectShiFPC(Map<String,String> param){
		return (String)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).selectObject("rigd.queryShiFPC", param);
	}
	
	/**
	 * 查询产线为N的状态记录数
	 * @author 贺志国
	 * @date 2012-4-13
	 * @param param
	 * @return 当天生产线排产状态为N的个数 sxCount
	 */
	public String selectShiFSX(Map<String,String> param){
		return (String)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).selectObject("rigd.queryShiFSX", param);
	}
	
	/**
	 * 更新日滚动模拟计划表中的是否确认状态为已确认'Y'
	 * @param param 日滚动计划号
	 * @return int 更新成功个数
	 */
	@Transactional
	public int updateRigdjh(Map<String,String> param)throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("rigd.updateShengx", param);
	}
	
	/**
	 * 产线查询
	 * @date 2012-2-13
	 * @param param 参数
	 * @return List<Map<String,String>>
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectChanx(Map<String,String> param) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("rigd.queryShengcx",param);
	}
	
	/**
	 * 产线查询
	 * @date 2012-2-13
	 * @param param 参数
	 * @return List<Map<String,String>>
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectBanc(Map<String,String> param) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("rigd.queryBanc",param);
	}
	
	/**
	 * 批量保存
	 * @author gswang
	 * @date 2012-4-12
	 * @param editList 备储明细批量修改集合
	 * @param banList  班次列表
	 * @param bancList  班次所对应的数据库列明
	 * @param param		界面得到的参数
	 * @return String null,success数据没有改变返回null，有数据增加或修改则返回success
	 */
	@Transactional
	public String  saveRigd(ArrayList<Rigdpcmx> editList,String banList,String bancList,Map<String,String> param)throws ServiceException{
		String []banString = banList.split(",");
		String []bancString = bancList.split(",");
		if(editList.size()==0 || banString.length ==0){
			return "null";
		}
		String nullBan = checkBan(editList,banString,bancString,param);
		if(nullBan.length()>0){
			return nullBan;
		}
		saveRigdmx(editList,banString,bancString,param);
		return "success";
	}
	
	/**
	 * 检测当给班安排了零件生产后，判断分配的班当天是否工作
	 * @author gswang
	 * @date 2012-4-12
	 * @param editList 备储明细批量修改集合
	 * @param banList  班次列表
	 * @param bancList  班次所对应的数据库列明
	 * @param param		界面得到的参数
	 * @return String null,success数据没有改变返回null，有数据增加或修改则返回success
	 */
	@Transactional
	public String checkBan(ArrayList<Rigdpcmx> editList,String[] banList,String[] bancList,Map<String,String> param)throws ServiceException{
//		List result = new ArrayList();
		StringBuffer result = new StringBuffer();
		Map<String,String> params = new HashMap<String,String>();
		for(Rigdpcmx  RigdpcmxBean : editList){
			String errBan = "";
			String flag = "";
			for(int i = 0;i<banList.length;i++){
				if(getBanSl(RigdpcmxBean,bancList[i])>0){
					params.put("USERCENTER", param.get("USERCENTER"));
					params.put("SHIJ", RigdpcmxBean.getShij());
					params.put("CHANXH", RigdpcmxBean.getChanxh());
					params.put("BAN", banList[i]);
					List<Map<String, String>> banc = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("rigd.queryDayBanc",params);
					if(banc.size()==0){
						errBan =  errBan + flag + banList[i] + "班";
						flag = ",";
					}
				}
			}
			if(errBan.length()>0){
				result.append(RigdpcmxBean.getChanxh()+ " "+RigdpcmxBean.getShij()+"日,"+errBan+" 不工作，无法分配零件生产，如需分配零件生产，请调整工作日历\r\n");
			}
		}
		return result.toString();
	}
	
	/**
	 * 将更改了的零件数量更新到日滚动明细表和日滚动班次明细表
	 * @author gswang
	 * @date 2012-4-12
	 * @param editList 备储明细批量修改集合
	 * @param banList  班次列表
	 * @param bancList  班次所对应的数据库列明
	 * @param param		界面得到的参数
	 * @return String null,success数据没有改变返回null，有数据增加或修改则返回success
	 */
	@Transactional
	public String saveRigdmx(ArrayList<Rigdpcmx> editList,String[] banList,String[] bancList,Map<String,String> param)throws ServiceException{
		StringBuffer result = new StringBuffer();
		Map<String,String> params = new HashMap<String,String>();
		String timeNow = getTimeNow("yyyy-MM-dd HH:mm:ss");
		for(Rigdpcmx  RigdpcmxBean : editList){
			params.put("USERCENTER", param.get("USERCENTER"));
			params.put("SHIJ", RigdpcmxBean.getShij());
			params.put("CHANXH", RigdpcmxBean.getChanxh());
			params.put("LINGJBH", RigdpcmxBean.getLingjbh());
			params.put("LINGJSL", RigdpcmxBean.getLingjsl().toString());
			params.putAll((Map<String,String>)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).selectObject("rigd.queryChanxzbh", params));
			params.put("EDITOR", param.get("jihybh"));
			params.put("EDIT_TIME", timeNow);
			params.put("CREATOR", param.get("jihybh"));
			params.put("CREATE_TIME", timeNow);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("rigd.updateRIGDPCJHMX",params);
			for(int i = 0;i<banList.length;i++){
				double lingjsl = getBanSl(RigdpcmxBean,bancList[i]);
				params.put("BAN", banList[i]);
				params.put("LINGJSLBan", String.valueOf(lingjsl));
				int num = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("rigd.updateBANCMX",params);
				if(lingjsl>0 && num==0){
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).execute("rigd.insertBANCMX",params);
				}
			}
		}
		return result.toString();
	}
	
	/**
	 * 得到对应班的零件数量
	 * @author gswang
	 * @date 2012-4-12
	 * @param RigdpcmxBean 日滚动班次明细bean
	 * @param num  班次顺序
	 * @return String null,success数据没有改变返回null，有数据增加或修改则返回success
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public double getBanSl(Rigdpcmx RigdpcmxBean,String methodName)throws ServiceException{
		
		BigDecimal num = BigDecimal.ZERO;
		try{
			String name = "get"+methodName.substring(0, 1)+(methodName.substring(1).toLowerCase());
		   Object obj=Class.forName("com.athena.pc.entity.Rigdpcmx").newInstance();
		   Method met=obj.getClass().getMethod(name);
		   num =  (BigDecimal)met.invoke(RigdpcmxBean, new   Object[]   {});
		}catch(Exception e){

		}
		return num.doubleValue();
	}
	
	/**
	 * @description 得到今天的时间
	 * @author 王国首
	 * @date 2012-1-14
	 * @param format 	日期的格式
	 * @param String	 一定格式的日期
	 */
	public String getTimeNow(String format){
		Date date = new Date(); 
		SimpleDateFormat fmat = new SimpleDateFormat(format); 
		return fmat.format(date);
	}
	
	/**
	 * 查询日滚动错误消息
	 * @param param 参数
	 * @return String 错误消息
	 */
	public String selectErrorMessage(Map<String,String> param)throws ServiceException{
		return (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).selectObject("rigd.queryErrorMessage", param);
	}
	
	/**
	 * 查询某条产线所有的月模拟错误消息
	 * @param param 参数
	 * @return String 错误消息
	 */
	public Map<String,Object> selectMessage(Rigdpcmx bean,Map<String,String> param)throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).selectPages("rigd.queryMessage",param,bean);
	}
	
	/**
	 * 月模拟导出报表数据
	 * @param param 参数
	 * @return dataSource 数据源集合
	 */
	public void getDownload(Map<String,String> param, Map<String, Object> dataSource){
		param.put("kaissj", param.get("kaissj"));
		param.put("jiessj", param.get("jiessj"));
		dataSource.put("kaissjChina", CollectionUtil.getYearMonthDayChina(param.get("kaissj")));
		dataSource.put("jiessjChina", CollectionUtil.getYearMonthDayChina(param.get("jiessj")));
		dataSource.put("downloadDay", CollectionUtil.getTimeNow(CollectionUtil.YMDFORMAT));
		param.put("banFlag", "Y");
		List<Map<String, String>> chanxzrl = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("yuemn.queryChanxzrl",param);
		List<Map<String, String>> banc = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("rigd.queryOutBanc",param);
		List<String> riq = new ArrayList<String>();
		List<String> xingq = new ArrayList<String>();
		List<String> ban = new ArrayList<String>();
//		Map<String, String> daysql = new HashMap<String, String>();
		String [] xingqihao = {"周一","周二","周三","周四","周五","周六","周日"};
		StringBuffer sqlName = new StringBuffer();
		StringBuffer sqlName_nban = new StringBuffer();
//		int i = 0;
		int j = 0;
		int k = 0;
		for(Map<String, String> chanxzrlMap : chanxzrl){
			riq.add(String.valueOf(chanxzrlMap.get("DAYNUM")));
			xingq.add(xingqihao[Integer.parseInt(String.valueOf(chanxzrlMap.get("XINGQ")))-1]);
			for( Map<String, String> bancMap: banc){
				ban.add(bancMap.get("BAN")+"班");
				sqlName.append(",").append("sum(decode(ymn.shij,'").append(chanxzrlMap.get("RIQ")+bancMap.get("BAN")).append("',ymn.lingjsl,0)) as A").append(j);
				j++;
			}
			sqlName_nban.append(",").append("sum(decode(ymn.shij,'").append(chanxzrlMap.get("RIQ")).append("',ymn.lingjsl,0)) as A").append(k);
			k++;
		}
		param.put("sqlName", sqlName.toString());
		param.put("sqlName_nban", sqlName_nban.toString());
		dataSource.put("tiannum", riq.size());
		dataSource.put("titlecol", String.valueOf(4+riq.size()*banc.size()));
		dataSource.put("titlecol_nban", String.valueOf(4+riq.size()));
		dataSource.put("bannum", banc.size());
		dataSource.put("Mergebannum", banc.size()>0?banc.size()-1:0);
		dataSource.put("riq", riq);
		dataSource.put("xingq", xingq);
		dataSource.put("ban", ban);
		dataSource.put("banallnum", ban.size());
		List<Map<String, String>> chanxLingj = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("rigd.queryChanxLingjOut",param);
//		if(param.get("chanxFlag") != null &&  "Y".equals(param.get("chanxFlag"))){
//		}
		paraDownloadData( param,  dataSource,chanxzrl,chanxLingj,banc);
		param.put("chanxFlag", "N");
		List<Map<String, String>> Lingj = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("rigd.queryLingjOut",param);
		paraDownloadData( param,  dataSource,chanxzrl,Lingj,banc);
		
		param.put("banFlag", "N");
		List<Map<String, String>> Lingj_nban = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_PC).select("rigd.queryLingjNbanOut",param);
		paraDownloadData( param,  dataSource,chanxzrl,Lingj_nban,banc);
	}
	
	public void paraDownloadData(Map<String,String> param, Map<String, Object> dataSource,List<Map<String, String>> chanxzrl,List<Map<String, String>> chanxLingj,List<Map<String, String>> banc){
		int Lie = "Y".equals(param.get("banFlag"))? chanxzrl.size() * banc.size() : chanxzrl.size();
		Map<String, Object> chanxOne = new HashMap<String, Object>();
		for( Map<String, String> chanxLingjMap: chanxLingj){
			Map<String, Object> lingjOne = new HashMap<String, Object>();
			List<Map<String, Object>> LingJList = null;
			if(chanxOne.containsKey(chanxLingjMap.get("CHANXH"))){
				LingJList = (List<Map<String, Object>>)chanxOne.get(chanxLingjMap.get("CHANXH"));
			}else{
				LingJList = new ArrayList<Map<String, Object>>();
			}
			chanxLingjMap.put("lingjKS", "1");
			chanxLingjMap.put("lingjJS", String.valueOf(Lie));
			lingjOne.put("nameMap", chanxLingjMap);
			List value = new ArrayList();
			for(int i = 0;i<Lie;i++){
				value.add(chanxLingjMap.get("A"+i));
			}
			lingjOne.put("valueList", value);
			LingJList.add(lingjOne);
			chanxOne.put(chanxLingjMap.get("CHANXH"), LingJList);
		}
		Iterator<Entry<String, Object>> it = chanxOne.entrySet().iterator();
		List<Map<String, Object>> chanx =new ArrayList<Map<String, Object>>();
		while(it.hasNext()){
			Entry<String, Object> entry = (Entry<String, Object>)it.next();
			Map<String, Object> chanxOut = new HashMap<String, Object>();
			Map<String, String> chanxMap = new HashMap<String, String>();
			List<Map<String, Object>> LingJList = (List<Map<String, Object>>)entry.getValue();
			chanxMap.put("chanxh", entry.getKey());
			chanxMap.put("chanxKS", String.valueOf(0-LingJList.size()));
			chanxMap.put("chanxJS", "-1");
			chanxMap.put("chanxMergeDown", String.valueOf(LingJList.size()-1));
			chanxOut.put("cxMap", chanxMap);
			List value = new ArrayList();
			for(int i = 0;i<Lie;i++){
				value.add(chanxMap.get("chanxKS"));
			}
			chanxOut.put("cxList", value);
			chanxOut.put("ljList", LingJList);
			chanx.add(chanxOut);
		}
		if("Y".equals(param.get("banFlag"))){
			if("Y".equals(param.get("chanxFlag"))){
				dataSource.put("lingjcx", chanx);
			}else{
				dataSource.put("lingjnotcx", chanx);
			}
		}else{
			dataSource.put("lingjban", chanx);
		}

		
	}
}
