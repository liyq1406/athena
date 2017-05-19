package com.athena.fj.module.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.ws.addressing.WSAddressingFeature;
import org.apache.log4j.Logger;

import com.athena.component.service.BaseService;
import com.athena.component.wtc.WtcResponse;
import com.athena.db.ConstantDbCode;
import com.athena.fj.entity.Peizdmx;
import com.athena.fj.entity.Peizjh;
import com.athena.fj.entity.Wulgz;
import com.athena.fj.entity.Yaocjh;
import com.athena.fj.module.common.LoaderPrintProperties;
import com.athena.print.Constants;
import com.athena.print.controller.GetPrintService;
import com.athena.util.date.DateUtil;
import com.athena.util.exception.ServiceException;
import com.athena.util.uid.CreateUid;
import com.toft.core3.Pageable;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * <p>
 * Title:配载计划Service类
 * </p>
 * <p>
 * Description:定义配载计划业务逻辑方法
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company: 软通动力
 * </p>
 * 
 * @author 贺志国
 * @E-mail zghe@isoftstone.com
 * @version v1.0
 * @date 2011-12-26
 */
@Component
public class PeizjhService extends BaseService<Peizjh>{
	private final int SHEET_AREA_BODY_HEAD = 1;//发货通知单表单上部区域
	private final int SHEET_AREA_BODY_BOTTOM = 2;//发货通知单表单下部区域
	private static Logger log = Logger.getLogger(PeizjhService.class.getName());
	//配置文件路径
	private final String fileName="com/athena/fj/config/print.properties";  
	
	/**
	 * 配载计划查询
	 * @author 贺志国
	 * @createDate 2011-12-26
	 * @param page 分页显示
	 * @param param 查询参数
	 * @return Map<String,Object>
	 */
	public Map<String,Object> select(Pageable page,Map<String,String> param) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).selectPages("peizjh.queryPeizjh",param,page);
	}
	
	/**
	 * 根据传入的sqlmapId查询需要的集合
	 * @return 集合map
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectMap(String sqlmapId,String usercenter) throws ServiceException{
		return  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).select("peizjh."+sqlmapId,usercenter);
	}
	
	
	/**
	 * 根据配载计划号查询配载单下的要货令
	 * @param param 
	 * @return 要货令集合
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> select(Map<String,String> param) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).select("peizjh.queryPeizjh_yaohl", param);
	}

	/**
	 * 自动配载时查询没有备货的要货令
	 * @param param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> queryNotBeiHOfYaohl(Peizjh bean)  throws ServiceException{
		StringBuffer buf = new StringBuffer();
		String flag = "";
		for(String yaohl : bean.getYaohlList()){
			buf.append(flag).append("'").append(yaohl).append("'");
			flag = ",";
		}
		bean.setYaohls(buf.toString());
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).select("peizjh.queryNoMatchYaohl", bean);
	}
	
	/**
	 * 手工配载时查询没有备货的要货令
	 * @param param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> queryNotBeiHOfYaohlSG(Map<String,String> param)  throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).select("peizjh.queryNoMatchYaohlSG", param);
	}
	
	
	/**
	 * 查询备货单
	 * @date 2012-1-18
	 * @param bean
	 * @return Peizdmx 配载单明细记录
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public List<Peizdmx> getBeihdList(String beihls) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).select("peizjh.queryBeihd",beihls);
	}
	
	/**
	 * 将配载信息插入配载单表和配载单明细表
	 * @param bean 配载计划
	 * @param listPeizdmx 配载单明细集合
	 */
	@Transactional
	public void batchInsert(Peizjh bean,List<Peizdmx> listPeizdmx,String username)throws ServiceException{
		//将确认的配载信息插入到配载单表
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("peizjh.insertPeizd",bean);
		//将备货令信息插入到配载单明细表
		for(Peizdmx pzmxBean :listPeizdmx){
			pzmxBean.setPeizdh(bean.getPeizdh());
			pzmxBean.setCreator(username);
			pzmxBean.setCreateTime(DateUtil.curDateTime());
			pzmxBean.setEditor(username);
			pzmxBean.setEditTime(DateUtil.curDateTime());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("peizjh.insertPeizdmx",pzmxBean);
		}
	}
	
	/**
	 * 自动配载，仓库返回成功消息时，修改配载计划、修改要车明细表状态，插入配载单和配载明细
	 * @param bean 配载计划
	 * @param listPeizdmx 配载单明细集合
	 */
	@Transactional
	public void batchUpdate(Peizjh bean,List<Peizdmx> listPeizdmx,String username)throws ServiceException{
		//更新配载计划表
		bean.setCreator(username);
		bean.setCreateTime(DateUtil.curDateTime());
		bean.setEditor(username);
		bean.setEditTime(DateUtil.curDateTime());
		bean.setJihzt(Peizjh.STATUE_PRINT);
		bean.setDaysj(DateUtil.curDateTime());
		//bean.setFaysj(queryFaysjOfYaohlmx(bean.getPeizdh()));
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("peizjh.updatePeizjh", bean);
		batchInsert(bean,listPeizdmx,username);
		//修改要车明细表中的要车计划状态
		Map<String,String> map = new HashMap<String,String>();
		map.put("yaocmxh", bean.getYaocmxh());
		map.put("yaoczt", Yaocjh.STATUE_PEIZ);//要车状态更改为已配载
		map.put("editor",username);
		map.put("editTime",DateUtil.curDateTime());
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("peizjh.updateYaocjhzt", map);
	}
	
	/**
	 * 自动配载后，要货令部分满足情况下用户选择继续配载，
	 * 记物流故障，删除没有配载的要货令明细，改更要货令锁定状态，将确认配载信息插入配载单
	 * @param peizjhBean
	 * @param wulgzBean
	 */
	@Transactional
	public void surePeiz(Peizjh peizjhBean,Wulgz wulgzBean,List<Peizdmx> listPeizdmx,String username)throws ServiceException{
		StringBuffer buf = new StringBuffer();
		String flag = "";
		for(String yaohl : peizjhBean.getYaohlList()){
			buf.append(flag).append("'").append(yaohl).append("'");
			flag = ",";
		}
		peizjhBean.setYaohls(buf.toString());
		//记物流故障
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("peizjh.insertWulgz",wulgzBean);
		
		peizjhBean.setCreator(username);
		peizjhBean.setCreateTime(DateUtil.curDateTime());
		peizjhBean.setEditor(username);
		peizjhBean.setEditTime(DateUtil.curDateTime());
		//将没有匹配上的要货令更新为未锁定状态0（未锁定）
		peizjhBean.setSuodpz(Peizjh.SUODPZ_STATE_0);
		//删除没有配载上的要货令明细
		deleteYaohlmx(peizjhBean);
		peizjhBean.setJihzt(Peizjh.STATUE_PRINT);
		peizjhBean.setDaysj(DateUtil.curDateTime());
		//peizjhBean.setFaysj(queryFaysjOfYaohlmx(peizjhBean.getPeizdh()));
		//更新配载计划表
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("peizjh.updatePeizjh", peizjhBean);
		//修改要车明细表中的要车计划状态
		Map<String,String> map = new HashMap<String,String>();
		map.put("yaocmxh", peizjhBean.getYaocmxh());
		map.put("yaoczt", Yaocjh.STATUE_PEIZ);//要车状态更改为已配载
		map.put("editor",username);
		map.put("editTime",DateUtil.curDateTime());
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("peizjh.updateYaocjhzt", map);
		
		batchInsert(peizjhBean,listPeizdmx,username);
		
	}
	
	/**
	 * 手工配载流程（全部配载），将配载信息插入配载单和配载单明细表
	 * @param peizjhBean 存放手工配载车型、车牌运输商信息
	 * @param listPeizdmx 配载单明细集合
	 */
	@Transactional
	public void batchPeizSG(Peizjh peizjhBean,List<Peizdmx> listPeizdmx,String username)throws ServiceException{
		StringBuffer buf = new StringBuffer();
		
		//给创建人、时间赋值 hzg 2012-8-7 更新代码
		peizjhBean.setCreator(username);
		peizjhBean.setCreateTime(DateUtil.curDateTime());
		peizjhBean.setEditor(username);
		peizjhBean.setEditTime(DateUtil.curDateTime());
		peizjhBean.setJihzt(Peizjh.STATUE_PRINT);
		peizjhBean.setDaysj(DateUtil.curDateTime());
		
		String flag = "";
		for(String yaohl : peizjhBean.getYaohlList()){
			buf.append(flag).append("'").append(yaohl).append("'");
			flag = ",";
		}
		peizjhBean.setYaohls(buf.toString());
		//将匹配上的要货令状态更改为锁定1（锁定）
		peizjhBean.setSuodpz(Peizjh.SUODPZ_STATE_1);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("peizjh.updateYaohlSdzt", peizjhBean);

		//更新配载计划状态为已打印
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("peizjh.updatePeizjh", peizjhBean);

		//获得发运时间
		//String faysj = getFaysj(peizjhBean);
		//peizjhBean.setFaysj(faysj);
		//根据发运时间和客户提前期得到车时间
		//peizjhBean.setDaocsj(getDaocsj(peizjhBean));
		batchInsert(peizjhBean,listPeizdmx,username);
	}
	
	/**
	 * 手工配载流程（继续配载），更新要货令表中的锁定状态为未锁定，将配载信息插入配载单和配载单明细表
	 * @param peizjhBean 存放手工配载车型、车牌运输商信息
	 * @param listPeizdmx 配载单明细集合
	 */
	@Transactional
	public void surePeizSG(Peizjh peizjhBean,List<Peizdmx> listPeizdmx,String username)throws ServiceException{
		StringBuffer buf = new StringBuffer();
		String flag = "";
		for(String yaohl : peizjhBean.getYaohlList()){
			buf.append(flag).append("'").append(yaohl).append("'");
			flag = ",";
		}
		log.info("要货令buf:"+buf.toString());
		peizjhBean.setYaohls(buf.toString());
		//获得发运时间
		//String faysj = getFaysj(peizjhBean);
		//peizjhBean.setFaysj(faysj);
		//根据发运时间和客户提前期得到车时间
		//peizjhBean.setDaocsj(getDaocsj(peizjhBean));
		peizjhBean.setDaysj(DateUtil.curDateTime());
		//将匹配上的要货令更新为锁定状态1（锁定）
		peizjhBean.setCreator(username);
		peizjhBean.setCreateTime(DateUtil.curDateTime());
		peizjhBean.setEditor(username);
		peizjhBean.setEditTime(DateUtil.curDateTime());
		peizjhBean.setSuodpz(Peizjh.SUODPZ_STATE_0);
		log.info("配载车牌chep："+peizjhBean.getChep());
		log.info("yaohl："+peizjhBean.getYaohls());
		peizjhBean.setJihzt(Peizjh.STATUE_PRINT);
		peizjhBean.setDaysj(DateUtil.curDateTime());
		//更新配载计划表
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("peizjh.updatePeizjh", peizjhBean);
		deleteYaohlmx(peizjhBean);
//		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("peizjh.updateYaohlSdzt", peizjhBean);
		batchInsert(peizjhBean,listPeizdmx,username);
	}
	
	/**
	 * 获得发运时间  ||2012-4-19 页面已经将faysj放到bean了，这里不需要再查询设值
	 * @param peizjhBean 封装有用户中心和要货令号的bean
	 * @return String 发运时间 
	 */
	/*public String getFaysj(Peizjh peizjhBean)throws ServiceException{
		//获得发运时间
		Map<String,String> param = new HashMap<String,String>();
		param.put("usercenter", peizjhBean.getUsercenter());
		param.put("yaohls", peizjhBean.getYaohls());
		List<Map<String,String>> listMap = queryFaysjOfYaohl(param);
		 return listMap.get(0).get("FAYSJ");
	}*/
	
	
	/**
	 * 根据发送时间计划到车时间
	 * @param faysj 发运时间
	 * @return String 到车时间
	 */
	/*public String getDaocsj(Peizjh peizjhBean)throws ServiceException{
		String faysj = getFaysj(peizjhBean);
		Map<String,String> param = new HashMap<String,String>();
		param.put("usercenter", peizjhBean.getUsercenter());
		param.put("yaohls", peizjhBean.getYaohls());
		param.put("yunssbm", peizjhBean.getYunssbm());
		List<Map<String,String>> listMap = queryFaysjOfYaohl(param);
		param.put("keh", listMap.get(0).get("KEH"));
		param.put("cangkbh", listMap.get(0).get("CANGKBH"));
		//查询参考系客户成品库关系表，获得客户提前期
		String kehtqq = queryHehChengpkOfTiqq(param);
		return DateUtil.DateSubtractMinutes(faysj, Integer.parseInt(kehtqq));
	}*/
	
	/**
	 * 自动匹载时，查询要货令明细表发运时间  ||2012-4-19 不需根据配载
	 * @param peizdh 配载单号
	 * @return String 发运时间
	 */
/*	public String queryFaysjOfYaohlmx(String peizdh)throws ServiceException{
		return (String)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("peizjh.queryFaysjOfYaohlmx", peizdh);
	}*/
	/**
	 * 客户提前期   ||2012-4-19  经余飞确定手工配载不需计算客户提交期
	 * @author 贺志国
	 * @date 2012-3-23
	 * @param param 用户中心，仓库编号，客户编号，承运商编号
	 * @return String 客户提前期
	 */
	/*public String queryHehChengpkOfTiqq(Map<String,String> param) throws ServiceException{
		return (String)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("peizjh.queryHehChengpkOfTiqq",param);
	}
	*/
	/**
	 * 手工配载时，查询要货令表发运时间 ||2012-4-19手工配载不需要发运时间，该方法只用于获取仓库编号
	 * @param peizdh 配载单号
	 * @return String 发运时间
	 * @throws ServiceException service异常
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> queryFaysjOfYaohl(Map<String,String> param)throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).select("peizjh.queryFaysjOfYaohl", param);
	}
	
	
	/**
	 * 删除配载计划，增加物流故障，同时修改要车计划状态为物流故障
	 * @param bean 物流故障实体集合
	 * @return void 空
	 * @throws ServiceException service异常
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public void delPeizjh(Wulgz bean,String username)throws ServiceException{
		bean.setCreator(username);
		bean.setCreateTime(DateUtil.curDateTime());
		bean.setEditor(username);
		bean.setEditTime(DateUtil.curDateTime());
		//增加物流故障
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("peizjh.insertWulgz",bean);
		//删除配载计划
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("peizjh.deletePeizjh", bean);
		//修改改要车计划状态为物流故障
		Map<String,String> map = new HashMap<String,String>();
		map.put("yaocmxh", bean.getYaocmxh());
		map.put("yaoczt", Yaocjh.STATUE_WULGZ);
		map.put("editor",username);
		map.put("editTime",DateUtil.curDateTime());
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("peizjh.updateYaocjhzt", map);
		Peizjh peizjhBean = new Peizjh();
		//根据配载单查找要货令明细表中的要货令号
		List<Map<String,String>> yhlLists = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).select("peizjh.queryYaohlOfPeizd", bean.getPeizdh());
		StringBuffer buf = new StringBuffer();
		String flag = "";
		
		if(yhlLists.size()>0){
			for(Map<String,String> yaohlMap : yhlLists){
				buf.append(flag).append("'").append(yaohlMap.get("YAOHLBH")).append("'");
				flag = ",";
			}
			peizjhBean.setPeizdh(bean.getPeizdh());
			peizjhBean.setYaohls(buf.toString());
			peizjhBean.setEditor(username);
			peizjhBean.setEditTime(DateUtil.curDateTime());
			peizjhBean.setSuodpz(Peizjh.SUODPZ_STATE_0);
			//删除配载计划下的要货令
			deleteYaohlmx(peizjhBean);
		}

	}
	
	/**
	 * 删除配载计划下的要货令，并将要货令表中的锁定状态为0（未锁定）
	 * @author 贺志国
	 * @date 2012-3-27
	 * @param peizjhBean
	 * @throws ServiceException
	 */
	@Transactional
	public void deleteYaohlmx(Peizjh peizjhBean)throws ServiceException{
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("peizjh.deleteYaohlmx", peizjhBean);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("peizjh.deleteJihyhlmx", peizjhBean);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("peizjh.updateYaohlSdzt",peizjhBean);
	}
	
	/**
	 * 校验仓库零件资源是否满足配载单中要货令
	 * @param map 配载单集合
	 * @return true=校验成功，false=校验失败
	 */ 
	public boolean checkStoreResponse(Map<String,String> parameter)throws ServiceException{ 
		return Boolean.parseBoolean(parameter.get("success"));
	}
	
	
	/**
	 * 仓库资源不足，确认备货
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> sureStoreResponse(WtcResponse wtc){
		String response = wtc.get("response").toString();
		if(!response.equals(Peizjh.SUCCESS_TRANSFER)){
			throw new ServiceException(wtc.get("respdesc").toString());
		}
		return (Map<String,String>)wtc.get("parameter"); 
	}
	
	/**
	 * 删除备货单
	 */
	public boolean delStoreResponse(WtcResponse wtc){
		String response = wtc.get("response").toString();
		if(!response.equals(Peizjh.SUCCESS_TRANSFER)){
			throw new ServiceException(wtc.get("respdesc").toString());
		}
		return true;
	}
	
	/**
	 * 解析仓库返回的消息
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> parseCheckWtcResponse(WtcResponse wtc) {
		String response = wtc.get("response").toString();
		if(!response.equals(Peizjh.SUCCESS_TRANSFER)){
			throw new ServiceException(wtc.get("respdesc").toString());
		}
		return (Map<String,String>)wtc.get("parameter"); 
		
	}
	

	
	/**
	 * 根据查询区域条件查询未配载要货令集合，分页查询
	 * @author 贺志国
	 * @date 2012-3-22
	 * @param page 分页参数
	 * @param param 页面查询参数
	 * @return Map<String,Object> 分页数据集合
	 * @throws ServiceException service异常
	 */
	public Map<String,Object> selectNoPeizYaohl(Pageable page,Map<String,String> param) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).selectPages("peizjh.queryNoPeizYaohl",param,page);
	}
	
	/**
	 * 新增配载计划
	 * @author 贺志国
	 * @date 2012-3-23
	 * @param bean 配载计划实体类
	 * @param param map参数
	 */
	@Transactional
	public void savePeizjh(Peizjh bean,Map<String,String> param) throws ServiceException{
		//根据序列号生成配载计划号
		String peizdh = this.getPeizdh(param.get("usercenter"),String.valueOf( this.getSequence("seq_peizdh")),6,"P");
		bean.setPeizdh(peizdh);
		param.put("peizdh", peizdh);
		//新增配载计划
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("peizjh.insertPeizjh", bean);
		//新增要货令明细
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("peizjh.insertYaohlmx", param);
		//将已配载要货令的锁定状态改为1
		bean.setYaohls(param.get("yaohls"));
		bean.setSuodpz(Peizjh.SUODPZ_STATE_1);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("peizjh.updateYaohlSdzt", bean);
	}
	
	/**
	 * 返回指定格式的配载单号
	 * @author 贺志国
	 * @date 2012-7-26
	 * @param uc 用户中心
	 * @param xlh 序列号
	 * @param len 长度
	 * @return String PW0001
	 */
	public String getPeizdh(String uc,String xlh,int len,String pre){
		// 用户中心为空
		if (uc == null || uc.trim().length() <= 1){
			throw new ServiceException("用户中心为空,或者长度不正确!");
		}
		// 得到用户中心第二位字符
		String secondChar = uc.substring(1, 2);
		String pzdh = "";
		String xlhTemp = xlh;
		// 序列号不足4位补0
		if (xlhTemp.length() != 0) {
			while(xlhTemp.length()<len)
			{
				xlhTemp = "0"+xlhTemp;
			}
			pzdh = pre+secondChar+xlhTemp;
		}
		
		return pzdh;
	}
	
	/**
	 * 新增要货令明细，将要货令表中的锁定状态改为1
	 * @author 贺志国
	 * @date 2012-3-28
	 * @param param 参数，用户中心和配载单号
	 */
	@Transactional
	public void addYaohlmx(Peizjh bean,Map<String,String> param) throws ServiceException{
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("peizjh.insertYaohlmx", param);
		//将已配载要货令的锁定状态改为1
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("peizjh.updateYaohlSdzt", bean);
	}
	
	/**
	 * 返回sqlmap-xxx.xml配置文件中的namespace属性名
	 */
	@Override
	protected String getNamespace(){
		return "peizjh";
	}
	/**
	 * 更新配载计划表中的运输商编码
	 * @author 贺志国
	 * @date 2012-3-28
	 * @param param
	 */
	@Transactional
	public void updatePeizjhYunss(Map<String, String> param) throws ServiceException {
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("peizjh.updatePeizjhYunss", param);
	}

	/**
	 * 配载计划下的要货令零件汇总
	 * @author 贺志国
	 * @date 2012-3-29
	 * @param param peizdh,usercenter 配载单号和用户中心
	 * @return List<Map<String,String>> 零件汇总集合
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectPeizjhYaohlOfLingj(Map<String, String> param) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).select("peizjh.queryPeizjh_yaohlOfLingj", param);
	}

	/**
	 * 配载计划下的要货令包装汇总
	 * @author 贺志国
	 * @date 2012-3-29
	 * @param param peizdh,usercenter 配载单号和用户中心
	 * @return List<Map<String,String>> 包装汇总集合
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectPeizjhYaohlOfBaoz(Map<String, String> param) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).select("peizjh.queryPeizjh_yaohlOfBaoz", param);
	}
	
	/**
	 * 根据运输路线查询未配载要货令集合
	 * @author 贺志国
	 * @date 2012-3-21
	 * @param page 分页参数
	 * @param param 运输路线编号
	 * @return Map<String,Object> 分页数据集合
	 * @throws ServiceException service异常
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectYaohlOfTuij(Map<String,String> param) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).select("peizjh.queryYunslxOfYaohl",param);
	}
	
	
	//************************ 有关打印数据查询与插入操作 2012-6-5 hzg *********************************//
	
	/**
	 * 查询要货令明细表客户编码
	 * @date 2012-6-5
	 * @param peizdh 配载单号
	 * @return String 客户编码
	 */
	public String queryKehbmOfYaohlmx(String peizdh)throws ServiceException{
		return (String)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).selectObject("peizjh.queryKehbmOfYaohlmx", peizdh);
	}
	
	/**
	 * 根据配载单号查询配载单明细表相关记录
	 * @author 贺志国
	 * @date 2012-6-5
	 * @param peizdh 配载单号
	 * @return Peizdmx 配载单明细记录集合
	 */
	@SuppressWarnings("unchecked")
	public List<Peizdmx> selectQueryPeizdmx(String peizdh){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).select("peizjh.queryPeizdmx", peizdh);
	}
	
	/**
	 * 批量插入打印业务表
	 * @author 贺志国
	 * @param bean 配载计划实体bean
	 * @param pzdmx 配载单明细集合
	 * @date 2012-6-5
	 */
	@Transactional
	public void batchInsertPrintBusinessTable(Peizjh bean,List<Peizdmx> pzdmx,String userName){
		log.info("打印开始，插入打印信息到相关打印表");
		//生成打印作业编号
		String qid = CreateUid.getUID(20);
		
		//设置打印队列主表参数并将值插入ckx_print_qtask_main表
		this.setPrintMainParas(bean,qid,userName);
		//设置单据打印队列表参数并将值插入ckx_print_qtask_info表
		this.setPrintInfoParas(bean, qid);
		//表单的两个区域，1为表单上部区域和2为表单下部区域，中间为列表
		//设置1区域并将值插入到ckx_print_qtask_sheet表
		this.setPrintSheetParas(bean,SHEET_AREA_BODY_HEAD,qid,userName);
		//设置2区域并将值插入到ckx_print_qtask_sheet表
		this.setPrintSheetParas(bean,SHEET_AREA_BODY_BOTTOM,qid,userName);
		//循环清单列表
		for(int i=0;i<pzdmx.size();i++){
			//设置打印列表参数并将值播入ckx_print_qtask_list表
			this.setPrintListParas(pzdmx,qid,bean,i);
		}
		
		log.info("开始调用打印接口，等待打印");
		//开始调用打印接口 
//		this.callPrintInterface(getPropertiesValue(fileName,"print_address"), qid);  
		log.info("打印完成");
	}
	
	/**
	 * 获取配置信息值
	 * @author 贺志国
	 * @date 2012-12-18
	 * @return 配置信息
	 */
	public String getPropertiesValue(String fname,String key){
		return LoaderPrintProperties.getPropertiesMap(fname).get(key);
	}
	
	/**
	 * 调用打印接口打印发货通知单
	 * @author 贺志国
	 * @date 2012-12-18
	 * @param address 打印接口服务地址
	 * @param qid 主任务ID
	 */
	public void callPrintInterface(String address,String qid){
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean ();
		factory.setServiceClass(GetPrintService.class); 
		factory.setAddress(address);
//		factory.create();    //此行多余，可以去掉  hzg 2013-8-26
		factory.getFeatures().add(new WSAddressingFeature());  //增加一行 hzg 2013-8-26
		GetPrintService hs=  (GetPrintService) factory.create();
		hs.getQtaskController(qid); 
	}
	
	/**
	 * 设置单据打印队列表参数并将值插入ckx_print_qtask_main表
	 * @author 贺志国
	 * @date 2012-12-18
	 * @param printMainMap
	 * @param bean
	 * @param qid
	 * @param userName
	 * @return map
	 */
	public void setPrintMainParas(Peizjh bean,String qid,String userName){
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
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("peizjh.insertPrintQtaskMain", printMainMap);
	}
	
	/**
	 * 设置单据打印队列表参数并将值插入ckx_print_qtask_info表
	 * @author 贺志国
	 * @date 2012-12-18
	 * @param printInfoMap 
	 * @param bean
	 * @param qid
	 * @return map
	 */
	public void setPrintInfoParas(Peizjh bean,String qid){
		//定义参数map
		Map<String,String> printInfoMap = new HashMap<String, String>();
		printInfoMap.put("usercenter", bean.getUsercenter());
		printInfoMap.put("qid", qid);
		printInfoMap.put("seq",bean.getPeizdh()); 
		printInfoMap.put("modelnumber",Constants.PRINT_DICT_PZD);
		printInfoMap.put("printnumber",String.valueOf(Constants.PRINT_PRINTNUMBER));
		printInfoMap.put("printunitcount",String.valueOf(Constants.PRINT_PRINTUNITCOUNT));
		printInfoMap.put("printtype",Constants.PRINT_PRINTTYPE_A);
		printInfoMap.put("status",String.valueOf(Constants.TASK_STATUS_NOSEND)); 
		printInfoMap.put("quyzs",Constants.PRINT_QUY_TWO); 
		//插入单据打印队列表
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("peizjh.insertPrintQtaskInfo", printInfoMap);
	}
	
	/**
	 * 设置表单明细参数并将值插入到ckx_print_qtask_sheet表
	 * @author 贺志国
	 * @date 2012-12-18
	 * @param bean
	 * @param qid
	 * @param gongcbm
	 * @param xiehd
	 * @param userName
	 * @return map
	 */
	public void setPrintSheetParas(Peizjh bean,int area ,String qid,String userName){
		//定义参数map
		Map<String,String> printSheetMap = new HashMap<String, String>();
		//获得系统当前时间
		String currDate = DateUtil.curDateTime();
		//创建StringBuffer
		StringBuffer sheetSpars = new StringBuffer();
		//设置表单明细参数
		printSheetMap.put("qid", qid);
		printSheetMap.put("seq",bean.getPeizdh());
		
		if(area==SHEET_AREA_BODY_HEAD){ //表单第1区域
			sheetSpars.append("[{\"1-3\":\""+bean.getPeizdh()+"\",\"1-5\":\"");
			sheetSpars.append(bean.getYunssbm()+"\",\"1-7\":\""+bean.getJihcx()+"\",\"1-9\":\"");
			sheetSpars.append(bean.getChep()+"\",\"1-11\":\""+bean.getFaysj()+"\",\"1-13\":\"");
			sheetSpars.append(bean.getZuiwsj()+"\",\"1-15\":\""+bean.getKehbm()+"\"}]");
			printSheetMap.put("area", String.valueOf(Constants.PRINT_SHEET_GRID));
			printSheetMap.put("spars", sheetSpars.toString());
		}else if(area==SHEET_AREA_BODY_BOTTOM){//表单第2区域
			sheetSpars.append("[{\"3-2\":\""+userName+"\",\"3-4\":\""+currDate+"\"}]");
			printSheetMap.put("area", "3");
			printSheetMap.put("spars", sheetSpars.toString());
		}
		//将值插入表单明细表
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("peizjh.insertPrintQtaskSheet", printSheetMap);
	}
	
	/**
	 * 设置打印列表参数并将值播入ckx_print_qtask_list表
	 * @author 贺志国
	 * @date 2012-12-18
	 * @param ucList
	 * @param qid
	 * @param bean
	 * @param i
	 * @param listSpars
	 * @return map
	 */
	public void setPrintListParas(List<Peizdmx> pzdmx,String qid,Peizjh bean,int i){
		StringBuffer listSpars = new StringBuffer();
		//定义参数map
		Map<String,String> printListMap = new HashMap<String, String>();
		listSpars.append("[{\"2-1\":\""+pzdmx.get(i).getCangkbh()+"\",\"2-2\":\"");
		listSpars.append(pzdmx.get(i).getZickbh()+"\",\"2-3\":\""+pzdmx.get(i).getBeihdh()+"\",\"2-4\":\"");
		listSpars.append(pzdmx.get(i).getBaozsl()+"\"}]");
		//设置清单明细参数
		printListMap.put("qid", qid);
		printListMap.put("seq",bean.getPeizdh());
		printListMap.put("area", "2");
		printListMap.put("xuh", i+1+""); 
		printListMap.put("spars", listSpars.toString());
		//插入清单明细
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("peizjh.insertPrintQtaskList", printListMap);
	}
	
	
	/**
	 * 获取打印机组编号
	 * @author 贺志国
	 * @date 2012-12-18
	 * @param bean 实体类
	 * @param userName 用户编号
	 * @return 打印机组编号
	 */
	public Map<String,String> getDayjzbh(Peizjh bean,String userName,Map<String,String> param){
		//保存返回值Map
		Map<String,String> result = new HashMap<String,String>();
		param.put("usercenter", bean.getUsercenter());
		param.put("username", userName);
		//查询打印用户信息表获得用户组编号
		String yhzbh = getYhzbh(param);
		//查询打印字典信息表获得单据组编号
		param.put("zidbm", Constants.PRINT_DICT_PZD);
		param.put("cangkbh", bean.getCangkbh());
		String djzbh = getDjzbh(param);
		param.put("yhzbh", yhzbh);
		param.put("djzbh", djzbh);
		//查询打印权限表获得打印机组编号
		String dayjzbh = getDayjzbh(param);
		result.put("djzbh", djzbh);
		result.put("dayjzbh", dayjzbh);
		return result;
	}
	
	/**
	 * 获得打组用户组编号
	 * @author 贺志国
	 * @date 2012-12-18
	 * @param param
	 * @return
	 */
	public String getYhzbh(Map<String,String> param){
		String yhzbh = (String)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).selectObject("peizjh.queryYhzbhOfPrintUserInfo", param);
		return yhzbh;
	}
	
	/**
	 * 获得单据组编号
	 * @author 贺志国
	 * @date 2012-12-18
	 * @param param
	 * @return
	 */
	public String getDjzbh(Map<String,String> param){
		String djzbh = (String)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).selectObject("peizjh.queryDjzbhOfPrintDictInfo", param);
		return djzbh;
	}
	
	/**
	 * 获得打印机组编号
	 * @author 贺志国
	 * @date 2012-12-18
	 * @param param
	 * @return
	 */
	public String getDayjzbh(Map<String,String> param){
		String dayjzbh = (String)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).selectObject("peizjh.queryDyjzbhOfPrintRight", param);
		return dayjzbh;
	}
	
	
	/**
	 * 运输路线下的运输商查询
	 * @author 贺志国
	 * @date 2012-8-3
	 * @param param 运输路线、用户中心
	 * @return List<Map<String,String>>
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectYunssOfLXZ(Map<String,String> param) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).select("peizjh.queryYunssOfLXZ",param);
	}
	
	/**
	 * 新增要货令明细，将要货令表中的锁定状态改为1
	 * @author 贺志国
	 * @date 2012-3-28
	 * @param param 参数，用户中心和配载单号
	 */
	@Transactional
	public void updatejihzt(Map<String,String> param) throws ServiceException{
		//更新配载计划表计划状态
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("peizjh.updatePeizjhzt", param);
	}
	
	/**
	 * 查询要货令明细表最小的最晚时间
	 * @date 2014-4-18
	 * @param peizdh 配载单号
	 * @return String 最晚时间
	 */
	public String queryZuiwsj(String peizdh)throws ServiceException{
		return (String)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).selectObject("peizjh.queryZuiwsj", peizdh);
	}
	
	/**
	 * 查询要货令明细表客户编码
	 * @date 2014-5-9
	 * @param peizdh 配载单号
	 * @return String 客户编码
	 */
	public String queryPrintStatus(String peizdh)throws ServiceException{
		return (String)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).selectObject("peizjh.queryPrintStatus", peizdh);
	}
	
	/**
	 * 查询配载计划明细
	 * @date 2014-5-9
	 * @param peizdh 配载单号
	 * @return String 客户编码
	 */
	public List<Map>  queryPeizjhmx(String peizdh)throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).select("peizjh.queryPeizjhmx", peizdh);
	}
}
