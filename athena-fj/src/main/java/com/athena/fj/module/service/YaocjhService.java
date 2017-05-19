package com.athena.fj.module.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.jws.WebService;

import org.apache.log4j.Logger;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.fj.entity.ClMx;
import com.athena.fj.entity.JhYaoHl;
import com.athena.fj.entity.WarpCelZ;
import com.athena.fj.entity.WarpLXZ;
import com.athena.fj.entity.WrapCacheData;
import com.athena.fj.entity.WrapCelL;
import com.athena.fj.entity.YAOCMx;
import com.athena.fj.entity.YaoCJhMx;
import com.athena.fj.entity.YaoCJhZb;
import com.athena.fj.entity.Yaocjh;
import com.athena.fj.module.interfaces.IYaoHLJhService;
import com.athena.util.CommonUtil;
import com.athena.util.athenalog.impl.UserOperLog;
import com.athena.util.date.DateUtil;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * @author 王冲
 * @email jonw@sina.com
 * @date 2012-2-16
 * @time 上午11:16:07
 * @description 要车计划Service类,并实现要贷令计划接口
 */
@WebService(
				endpointInterface="com.athena.fj.module.interfaces.IYaoHLJhService",
				serviceName = "/yaocjhService")
@Component
public class YaocjhService extends BaseService<Yaocjh> implements IYaoHLJhService {

	@Inject
	private FJScheduleService fJScheduleService;
	
	 /* description:   分隔符*/
	public static final String  SEPARATOR= ":";
	/* description:   车辆申报*/
	public static final int  CLZY_YXJ_1=1;
	/* description:   用户中心车辆资源*/
	public static final int  CLZY_YXJ_2=2;
	/* description:   最优方案(不考滤车辆资源)*/
	public static final int  CLZY_YXJ_3=3;
	/* description:   不考滤车辆运输商*/
	public static final int  CLZY_YXJ_4=4;
	
	/* description:   满载*/
	public static final int  MZY_YXJ_1=1;
	/* description:   未满载*/
	public static final int  MZY_YXJ_2=2;
	
	public static final String TIME1= "23:59" ;
	public static final String TIME2= "23:59:59" ;
	
	/* description:   写入数数据库  */
	public static final int WRITEDB_YES = 1;
	/* description:   不写入数数据库  (手工配载的推荐配载)*/
	public static final int WRITEDB_NO = 0;
	
	/**优先基娄装满**/
	 public static final int YXJS=1;
	/**不考滤基数**/
	 public static final int QCJS=2;
	 
	 public String editor = "";

	 public String inter = "4160";
	 
	@Inject
	private UserOperLog userOperLog;
	
	static Logger logger = Logger.getLogger(YaocjhService.class.getName());
//	private static final String String = null;
	/**
	 * 计划跟踪查询
	 * @author 贺志国
	 * @date 2012-4-9
	 * @param page 分页显示
	 * @param param 查询参数
	 * @return Map<String,Object>
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Map<String,Object>> selectJihgz(Map<String,String> param) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).select("yaocjh.queryJihgz",param);
	}
	/*****************BY 王冲   date :2012-02-09*****************************/
	
	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}
	
	/**
	 * 更新内部要货令表中的发运时间(只有在内部要货令表中的发运时间为空时间才执行此操作)
	 * 
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-20
	 * @time 下午04:52:06
	 * @param param  {UC:用户中心}
	 * @return int
	 */
	@Transactional
	public int updateFaYSJ(Map<String,String> param) throws ServiceException
	{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("yaocjh.updateFaYSJ",param);
	}
	
	/**
	 * 归集客户-供应商
	 * 
	 * @author 王冲
	 * @date 2011-12-8
	 * @param  params   {DATE:日期时间}  
	 * @return List 包含客户编号-供应商编码集合
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectKehGYS(final Map<String,String> params) throws ServiceException {  
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).select("yaocjh.queryKehGYS",params);
	}
	/**
	 * 路线组
	 * 
	 * @author 王冲
	 * @date 2011-12-8
	 * @param  params   {DATE:日期时间,UC:用户中心}  
	 * @return List 
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectLuXZ(final Map<String,String> params) throws ServiceException {  
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).select("yaocjh.queryLuXZ",params);
	}
	

	public List<HashMap<String,String>> selectYhlLuXZ(final Map<String,String> params) throws ServiceException {  
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).select("yaocjh.queryLuXZ",params);
	}
	/**
	 * 路线组交付时刻
	 * 
	 * @author 王冲
	 * @date 2011-12-8
	 * @param  params   {DATE:日期时间,UC:用户中心}  
	 * @return List 
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectJiaoFSK(final Map<String,String> params) throws ServiceException {  
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).select("yaocjh.queryJiaoFSK",params);
	}
	/**
	 * 路线组仓库
	 * 
	 * @author 王冲
	 * @date 2011-12-8
	 * @param  params   {DATE:日期时间,UC:用户中心}  
	 * @return List 
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectLXZCK(final Map<String,String> params) throws ServiceException {  
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).select("yaocjh.queryLXZCK",params);
	}
	
	/**
	 * 车辆申报资源
	 * 
	 * @author 王冲
	 * @date 2012-02-09
	 * @param param   {DATE:日期时间,UC:用户中心}  
	 * @return Map<String,Object>
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Map<String,String>> selectCLBySB(final Map<String,String> param) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).select("yaocjh.queryCLBySB",param);
	}
	
	
	
	/**
	 * 用户中心车辆资源
	 * 
	 * @author 王冲
	 * @date 2012-02-09
	 * @param param   {DATE:日期时间,UC:用户中心}  
	 * @return Map<String,Object>
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Map<String,String>> selectCLByUC(final Map<String,String> param) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).select("yaocjh.queryCLByUC",param);
	}
	
	/**
	 * 根据客户和要贷令归集策略
	 * 
	 * @author 王冲
	 * @date 2012-02-09
	 * @param param   KEY:{KEH:客户编码   DATE:发运日期}
	 * @return Map<String,Object>
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Map<String,String>> selectCelZ(final Map<String,String> param) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).select("yaocjh.queryCelZ",param);
	}
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-14
	 * @time 下午04:21:10
	 * @param param {KEY:日期  key:客户}
	 * @return List<Map<String,String>> 
	 * @throws ServiceException
	 * @description   归集要贷令号
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public List<HashMap<String,String>> selectYaoHL(final Map<String,String> param ) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).select("yaocjh.queryYaoHL",param);
	} 

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-14
	 * @time 下午04:21:10
	 * @param param {KEY:日期  key:客户}
	 * @return List<Map<String,String>> 
	 * @throws ServiceException
	 * @description   归集要贷令号
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public List<HashMap<String,String>> selectYaoHLGJ(final Map<String,String> param ) throws ServiceException{
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).select("yaocjh.queryYaoHLGJ",param);
	} 	
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-17
	 * @time 上午11:23:37
	 * @return   List<Map<String,String>>
	 * @description  得到所有的用户中心
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectUc() throws ServiceException
	{
		return  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).select("yaocjh.queryUC");
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-22
	 * @time 下午01:06:53
	 * @return List<Map<String,String>>
	 * @description  每个路线组在下一天的第一个交付时刻
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> selectFirstJiaoFSK(final Map<String,String> param) throws ServiceException
	{
		return  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).select("yaocjh.queryFirstJiaoFSK",param);
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-22
	 * @time 下午01:10:03
	 * @param list 每个路线组在下一天的第一个交付时刻
	 * @return Map<String,String >
	 * @description 封装交付时刻  
	 */
	public Map<String,String >  wrapFirstJiaoFSK(List<Map<String,String>> list) throws ServiceException
	{
		Map<String,String > map = new HashMap<String, String>();
		//迭代交付时刻
		for(Map<String,String> tmp:list)
		{
			//封装数据集
			map.put(tmp.get("YUNSLXBH"), tmp.get("JIAOFSK"));
		}
		return map;
		
	}
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-22
	 * @time 下午01:10:03
	 * @param list 路线组-仓库
	 * @return  Map<String,HashSet<String>>
	 * @description 封装路线组-仓库
	 */
	public Map<String,HashSet<String>>  wrapLXZCK(List<Map<String,String>> list) throws ServiceException
	{
		Map<String,HashSet<String>> mapCK = new HashMap<String, HashSet<String>>();
		//迭代路线组-仓库
		for(Map<String,String> tmp:list)
		{
			/**如果存在此路组组**/
			if(mapCK.containsKey(tmp.get("YUNSLXBH"))){
				//将仓库编号加入的set的集合中
				mapCK.get(tmp.get("YUNSLXBH")).add(tmp.get("CANGKBH"));
			}
			else{
				HashSet<String> set = new HashSet<String>();
				//得到仓库编号 
				set.add(tmp.get("CANGKBH"));
				//写入缓存存
				mapCK.put(tmp.get("YUNSLXBH"), set);
			}
		}
		return mapCK;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-21
	 * @time 上午10:43:03
	 * @param kehGys  客户供应商
	 * @param lxzList 路线组集合
	 * @return Map<String,List<WarpLXZ>>
	 * @description  封装路线组
	 */
	public Map<String,ArrayList<WarpLXZ>> getWarpLXZ(final List<Map<String,String>> kehGys,
			final List<Map<String,String>> lxzList) throws ServiceException
	{
		//封装集
		//key:CYS:CX
		Map<String,ArrayList<WarpLXZ>> map = new HashMap<String,ArrayList<WarpLXZ>>();
		//迭代
		for(Map<String,String> tmp:kehGys)
		{
			
			//封装
			String key = tmp.get("KEH") ;
			//路线组{key:GYS:KEH}
			ArrayList<WarpLXZ> lxzs = new ArrayList<WarpLXZ>();
			for(Map<String,String> lxz:lxzList)
			{
				//如果要货令中的供应商与客户与路线组的供应商客户一致，则进行归集
				if(tmp.get("KEH").equalsIgnoreCase(lxz.get("KEHBH"))){
					
					/*1.key:CYS:CX*/
					//路线entity
					WarpLXZ lx = new WarpLXZ();
					//仓库编号
					lx.setCkbh(lxz.get("CANGKBH")) ;
					//运输商
					lx.setCys(lxz.get("CHENGYSBH"));
					//客户
					lx.setKeh(lxz.get("KEHBH"));
					//路线组最大提前期
					lx.setLxzdztqq(Integer.parseInt(lxz.get("ZUIDTQFYSJ"))) ;
					//路线组编号
					lx.setLzxbh(lxz.get("YUNSLXBH")) ;
					//要车提前期
					lx.setYaoctqq(Integer.parseInt(lxz.get("YAOCTQQ"))) ;
					//归集
					lxzs.add(lx) ;
					
				}
				
			}
			
			//写入数据中心
			map.put(key,lxzs );
		}
		return map;
	}
	
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-21
	 * @time 上午10:43:34
	 * @param jiaoFSK 交付时刻集合
	 * @param date yyyy-mm-dd
	 * @return Map<String,LinkedList<String>>
	 * @description  封装交付时刻
	 */
	public Map<String,ArrayList<LinkedList<String>>> warpJiaoFSK(final List<Map<String,String>> jiaoFSK,String date) throws ServiceException
	{
		//1.第一层封装
		Map<String, ArrayList<String>>  sjMap = new HashMap<String,ArrayList<String>>();
		//迭代交付时刻
		for(Map<String,String> tmp :jiaoFSK)
		{
			//如果已经封装了此路线
			if(sjMap.containsKey(tmp.get("YUNSLXBH")))
			{
				//将其交付时刻写入
				sjMap.get(tmp.get("YUNSLXBH")).add(tmp.get("JIAOFSK"));
			}
			else
			{ 
				//归集并封装交付时刻
				ArrayList<String> lk =  new ArrayList<String>();
				//写入交付时刻
				lk.add(tmp.get("JIAOFSK"));
				sjMap.put(tmp.get("YUNSLXBH"), lk ) ;
			}
		}
		
		//2.第二层封装装
		Map<String,ArrayList<LinkedList<String>>> warpSjMap = new HashMap<String, ArrayList<LinkedList<String>>>();
		//迭代封装好的路线
		for(Map.Entry<String, ArrayList<String>> entry:sjMap.entrySet()){
			//时间区
			LinkedList<String> linked = new LinkedList<String>();
			//时间区列表
			ArrayList<LinkedList<String>> ll = new ArrayList<LinkedList<String>>();
			//交付时间列表
			ArrayList<String> al = entry.getValue();
			//迭代交付时间表
			for(String s:al){
				//如果是偶数,则将此时间放入linked的first位置
				if(al.indexOf(s)==0){
					//放入first位置
					linked.addFirst(date+" "+s) ;
				}else{
					//如果是奇数,则将此时间放入linked的first位置
					//放入last位置
					linked.addLast(date+" "+s);
					//将此linked添加的时间区列表中
					ll.add(linked);
					//创建新的时间区
					linked = new LinkedList<String>();
					linked.addFirst(date+" "+s);
				}
					
			}
			//最后一个时间点
			ll.add(linked);
			//第二层封装
			warpSjMap.put(entry.getKey(), ll);
		}
		
		return warpSjMap;
	}
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-9
	 * @time 下午03:06:59
	 * @param param 车辆组 (依据客户生成的)
	 * @return Map<String,WarpCelZ>
	 * @description  重新封装申报的车辆资源
	 */
	public Map<String,WrapCelL>  warpCelL(final List<Map<String,String>> param ) throws ServiceException
	{
		//封装集
		Map<String,WrapCelL> celLMap = new HashMap<String, WrapCelL>();

		for(Map<String,String> o:param)
		{
				WrapCelL cl =  new WrapCelL();
				/* 车辆型号 */
				cl.setClLx(o.get("CHEX"));
				/*承运商*/
				cl.setCys(o.get("YUNSSBM"));
				/*车辆名称*/
				cl.setClName(o.get("CHEXMC")) ;
				/*申报数量*/
				cl.setSbSl(Integer.parseInt(o.get("SHUL"))) ;
				/*使用数量*/
				cl.setSysl(Integer.parseInt(o.get("SYSL"))) ;
				/*最大数量*/
				cl.setZdsl(Integer.parseInt(o.get("ZUIDSL"))) ;
				/*封装*/
				String key=cl.getCys()+SEPARATOR+cl.getClLx();
				celLMap.put(key,cl);
		}
		return celLMap;
	}
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-9
	 * @time 下午03:06:59
	 * @param param 策略组 (依据客户生成的)
	 * @return Map<String,WarpCelZ>
	 * @description  重新封装归集后的策略组
	 */
	public Map<String,WarpCelZ>  getWarpCelZ(final List<Map<String,String>> param ) throws ServiceException
	{
		Map<String,WarpCelZ> celZMap = new LinkedHashMap<String, WarpCelZ>();
		//迭代
		for(Map<String,String> o:param)
		{
			//如果策略组存在此策略编号
			if (celZMap.containsKey(o.get("CELBH"))) {
				//如果策略包装组中存在此包装编号
				if (!celZMap.get(o.get("CELBH")).getBzzSl()
								.containsKey(o.get("BAOZZBH")))
				{
					/* 对应的包装组 */
					celZMap.get(o.get("CELBH")).getBzzSl().put(o.get("BAOZZBH"),
									Integer.parseInt(o.get("BAOZSL")));
					/*对应的包装组基数*/
					celZMap.get(o.get("CELBH")).getBzzJs().put(o.get("BAOZZBH"),
									Integer.parseInt(o.get("BAOZBSJS")));
				}
			}
			else {
				WarpCelZ _celZ = new WarpCelZ();
				/* 策略类型 */
				_celZ.setCelH(o.get("CELBH"));
				/* 对应车辆类型 */
				_celZ.setCx(o.get("CHEXBH"));
				/*车辆名称*/
				_celZ.setCxName(o.get("CHEXMC")) ;
				/* 对应的包装组 */
				_celZ.getBzzSl().put(o.get("BAOZZBH"),
								Integer.parseInt(o.get("BAOZSL")));
				/*对应的包装组基数*/
				_celZ.getBzzJs().put(o.get("BAOZZBH"),
								Integer.parseInt(o.get("BAOZBSJS")));
				
				/* 封装 */
				celZMap.put(o.get("CELBH"), _celZ);
			}
		}
		return celZMap;
	}
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 上午09:44:24
	 * @description  更新要货令的配载状态
	 */
	@Transactional
	public int  updateYHLPZ() throws ServiceException
	{
		
		return  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("yaocjh.updateYHLPZ");
	}
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 上午09:44:24
	 * @description  更新要货令的锁定状态
	 */
	@Transactional
	public int  updateYHLSUODPZ() throws ServiceException
	{
		
		return  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("yaocjh.updateYHLSUODPZ");
	}
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 上午09:44:24
	 * @description  锁定要贷令
	 */
	@Transactional
	public int  updateYaoHL(final Map<String,String> param) throws ServiceException
	{
		
		return  baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("yaocjh.lockYaoHL", param);
	}
	
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 上午10:42:59
	 * @return Map
	 * @description   得到要车明细主键
	 */
	@SuppressWarnings("unchecked")
	public String selectYaoCMxPriKey() throws ServiceException
	{
		Map<String, String> map =  (Map<String, String>) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).selectObject("yaocjh.queryYaoCMXPriKey") ;
		return map.get("PRIKEY") ;
		
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 上午10:42:59
	 * @return Map
	 * @description   得到车辆明细主键
	 */
	@SuppressWarnings("unchecked")
	public String selectCLMxPriKey() throws ServiceException
	{
		Map<String, String> map =  (Map<String, String>) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).selectObject("yaocjh.queryCLMxPriKey") ;
		return map.get("PRIKEY") ;
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 上午11:02:08
	 * @param param 
	 * @description  新增要车明细
	 */
	@Transactional
	public void  insertYAOCMx(YAOCMx param) throws ServiceException
	{
		 baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("yaocjh.insertYAOCMx", param);
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 上午11:02:08
	 * @param param 
	 * @description  新增计划要贷令明细
	 */
	@Transactional
	public void  insertJhYaoHl(JhYaoHl param) throws ServiceException
	{
		 baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("yaocjh.insertJhYaoHl", param);
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 上午11:02:08
	 * @param param 
	 * @description  新增要车计划总表
	 */
	@Transactional
	public void  insertYaoCJhZb(YaoCJhZb param) throws ServiceException
	{
		 baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("yaocjh.insertYaoCJhZb", param);
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 上午11:02:08
	 * @param param 
	 * @description  新增车辆明细表
	 */
	@Transactional
	public void  insertClMx(ClMx param) throws ServiceException
	{
		 baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ).execute("yaocjh.insertClMx", param);
	}
	
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-21
	 * @time 下午01:34:43
	 * @param celH 车辆型号
	 * @param cache 数据缓存区
	 * @param levl 是否优先满载率
	 * @return Map<String,String>
	 * @description  匹配路线
	 */
	public boolean getLxBySB(WarpLXZ lx,WarpCelZ ceZ ,WrapCacheData cache) throws ServiceException
	{
		    //1.优先车辆资源
		
			// 运输商:车辆型号
			String cysKey = lx.getCys() + SEPARATOR + ceZ.getCx();

			// 判断申报车辆资源是否有此车型
			if (cache.getMapCLBySB().containsKey(cysKey)) {
				// 得到此车辆的信息
				WrapCelL cl = cache.getMapCLBySB().get(cysKey);
				// 判断此车辆型号的可使用的数量是否大于0
				if (cl.getSbSl() - cl.getSysl() > 0) {
					lx.setClly("车辆申报") ;
					return true;
					
				}
			}
			return false;
		
	}
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-21
	 * @time 下午01:34:43
	 * @param celH 车辆型号
	 * @param cache 数据缓存区
	 * @param levl 是否优先满载率
	 * @return Map<String,String>
	 * @description  匹配路线
	 */
	public boolean getLxByUC(WarpLXZ lx,WarpCelZ ceZ, WrapCacheData cache  ) throws ServiceException
	{
		// 运输商:车辆型号
		String cysKey = lx.getCys() + SEPARATOR + ceZ.getCx();

		
		
		// 判断申报车辆资源是否有此车型
		if (cache.getMapCLByUC().containsKey(cysKey)) {
			// 得到此车辆的信息
			WrapCelL cl = cache.getMapCLByUC().get(cysKey);
			// 判断此车辆型号的可使用的数量是否大于0
			if (cl.getZdsl() - cl.getSysl() > 0) {
				lx.setClly("用户中心") ;
				return true;
				
			}
		}
		
		
		//迭代此用户中心车辆资源
		for(Map.Entry<String, WrapCelL> zy:cache.getMapCLByUC().entrySet()){
			//车辆型号
			if(zy.getKey().indexOf(ceZ.getCx())>=0){
				// 得到此车辆的信息
				WrapCelL cl = zy.getValue();
				// 判断此车辆型号的可使用的数量是否大于0
				if (cl.getZdsl() - cl.getSysl() > 0) {
					lx.setClly("用户中心") ;
					lx.setCys(cl.getCys()) ;
					return true;
					
				}
			}
		}
		
		
		

		return false;
	}
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-21
	 * @time 下午01:34:43
	 * @param celH 车辆型号
	 * @param cache 数据缓存区
	 * @param levl 是否优先满载率
	 * @return Map<String,String>
	 * @description  匹配路线
	 */
	public boolean getLxByHigh(WarpLXZ lx,WarpCelZ ceZ, WrapCacheData cache ) throws ServiceException
	{
			// 运输商:车辆型号
			String cysKey = lx.getCys() + SEPARATOR + ceZ.getCx();
			
			// 判断用户中心资源是否有此车辆型号
			if (cache.getMapCLByUC().containsKey(cysKey)) {
				lx.setClly("最优方案") ;
				return true;
			}
			
			
			//迭代此用户中心车辆资源
			for(Map.Entry<String, WrapCelL> zy:cache.getMapCLByUC().entrySet()){
				//车辆型号
				if(zy.getKey().indexOf(ceZ.getCx())>=0){
					lx.setClly("最优方案") ;
					lx.setCys(zy.getValue().getCys()) ;
					return true;
			 }
			}
			
//			// 判断用户中心资源是否有此车辆型号
//			if (cache.getMapCLByUC().containsKey(cysKey)) {
//				lx.setClly("最优方案") ;
//				return true;
//			}
			return false;
	}
	
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-21
	 * @time 下午02:46:12
	 * @param yao
	 *            要车明细
	 * @param param
	 *            {UC:用户中心}
	 * @description 锁定要货令
	 */
	public void lockYaoHL(YaoCJhMx yao, Map<String, String> param)  throws ServiceException{
		
		
		// 要货令编号
		String t = "";
		// 迭代要货令
		for (Iterator<String> s = yao.getYhlbh().iterator(); s.hasNext();) {
			// 拼接要货令
			t = "'" + s.next() + "'" + "," + t;
		}
		// 去掉前后的'
		//t = t.substring(1);
		t = t.substring(0, t.lastIndexOf(','));
		// 将参数封装成map对象
		Map<String, String> tmp = new HashMap<String, String>();
		// 用户中心
		tmp.put("UC", param.get("UC"));
		// 拼接后的要货令编号
		tmp.put("YAOHLHS", t);
		// 锁定要货令
		 this.updateYaoHL(tmp) ;
	}
	
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-21
	 * @time 下午02:49:33
	 * @param param {CX:车辆型号,CYS:运运商}
	 * @param cache 数据缓存中心
	 * @description  当分配车型后，同步更新车辆申报资源和用户中心下的车辆资源
	 */
	public void syncCelZY( Map<String, String> param,WrapCacheData cache) throws ServiceException
	{
		//GYSDM:CX
		String  key = param.get("CX");
		
		//1.同步更新车辆申报资源
		if(cache.getMapCLBySB().containsKey(key)){
			
			//得到此车辆型号的资源
			WrapCelL zy = cache.getMapCLBySB().get(key);
			//如果车辆资源没有消耗完
			if(zy.getSbSl()!=zy.getSysl()){
				//将其使用数量+1
				zy.setSysl(zy.getSysl()+1);
			}
			
		}
		
		//2.同步更新用户车辆资源
		if(cache.getMapCLByUC().containsKey(key)){
			
			//得到此车辆型号的资源
			WrapCelL zy = cache.getMapCLByUC().get(key);
			//如果此运输商下的此车型的最大数量大于使用数量,则使用数量加1
			if(zy.getZdsl()-zy.getSysl()>0){
				//将其使用数量+1
				zy.setSysl(zy.getSysl()+1);
			}
			//否则用户中心下的车辆资源已用完
		}
	}
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-22
	 * @time 上午11:23:07
	 * @param cx 车辆型号
	 * @param cache 数据缓存区
	 * @description  缓存车辆明细
	 */
	public void cacheCl(YaoCJhMx yao,WrapCacheData cache) throws ServiceException{
		
		//1.缓存车辆明细
		Map<String,Integer> clmx  = cache.getClmx();
		//车辆明细
		if(clmx.containsKey(yao.getCllx())){
			//该车型+1
			clmx.put(yao.getCllx(), clmx.get(yao.getCllx())+1);
		}
		else{
			//新的车辆型号
			clmx.put(yao.getCllx(), 1);
		}
		//2.缓存车辆名称
		cache.getClmc().add(yao.getClName());
		//3.缓存总的车辆数量
		cache.setCls(cache.getCls()+1) ;
	}
	
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-21
	 * @time 下午03:45:17
	 * @param yao 要车明细
	 * @param yaoHL 要货令
	 * @return YaoCJhMx
	 * @description  将上一交付时刻点尚未装满的装满
	 */
	public void casedYaoCMX(List<HashMap<String,String>> yaoHL,WrapCacheData cache) throws ServiceException
	{
		// 上一交付时刻未装满的车
		List<YaoCJhMx> yaoSK = cache.getYaoSKD();
		// 迭代每个要车明细
		for (YaoCJhMx yao : yaoSK) {

			// 包装状态
			Map<String, HashMap<String, Integer>> yaoHLstate = yao.getYaoHLstate();

			// 迭代要车状态，得到包装组
			for (Map.Entry<String, HashMap<String, Integer>> entry : yaoHLstate	.entrySet()) {

				// 初始化包装组的状态
				this.casedBzZJS(yao, entry.getKey(), cache);

				// 1.优先匹配相同包装型号的要货令 迭代包装组，得到具体的包装信息
				for (Map.Entry<String, Integer> tmp : entry.getValue().entrySet()) {
					// 包装组编号
					String bzzBH = entry.getKey();
					// 包装组型号
					String bzzXH = tmp.getKey();

					// 判断此包装型号的数量是否与包装组的基数是否一致处,一致则进入下个包装型号匹配
					if (tmp.getValue().intValue() == yao.getJhjs()) {
						continue;
					}

					// 实际基数
					yao.setSjjs(tmp.getValue().intValue());
					/* 匹配此包装型号的要货令，将基装入车中 */
					this.caculateJs(yaoHL, yao, bzzXH, bzzBH);
				}

				// 2.其次匹配相同包装组的要货令
				// 判断此包装组的实际数量是否与包装组的计划数量是否一致处,一致则进入下个包装组匹配
				if (yao.getTmpBaoZJhsl() == yao.getTmpBaoZsjsl()) {
					continue;
				}
				this.caculateStore(yao, entry.getKey(), yaoHL,QCJS);
			}

			// 3.最后匹配尚未装入车的包装组

			// 得到此车的配载策略
			/* 根据策略号得到策略组 */
			WarpCelZ celz = cache.getMapCELZ().get(yao.getCelbh());
			/* 得到该组下的包装组 */
			Map<String, Integer> bzzSl = celz.getBzzSl();
			
			//迭代此配载策略下的包装组
			for (Iterator<String> it = bzzSl.keySet().iterator(); it.hasNext();) {
				//包装组编号
				String bzz = it.next();
				//如果此车不存在此包装组
				if (!yaoHLstate.containsKey(bzz)) {
					// 初始化包装组的状态
					this.casedBzZJS(yao, bzz, cache);
					//包装组计算
					this.caculateStore(yao, bzz, yaoHL,QCJS);
				}

			}
			
			//如果此车没有装满，则写入缓存冲
			if(yao.getJhsl()==yao.getSjsl()){
				 //缓存车辆型号
				 this.cacheCl(yao, cache);
				 cache.getYaoMZ().add(yao) ;
					//写入要车明细
				writeYAOCMx(yao, cache) ;
			}else{
				//将此车写入缓存区，等待混装计算
				this.markYAO(cache, yao) ;
			}
			//锁定要货令
			this.lockYaoHL(yao, cache.getParam());
			//将装车后的要贷令移除
			this.removeYaoHL(yao.getYhlbh(), yaoHL) ;
		}
		
	}
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-26
	 * @time 上午09:40:23
	 * @param cache 数据缓存中心
	 * @param yao 要车明细
	 * @description  记录此车的时刻点，及所属的路线组,策略
	 */
	public void markYAO(WrapCacheData cache,YaoCJhMx yao) throws ServiceException{
		
		/**1.记录此车的路线组**/
		this.markLXZ(cache, yao) ;
		/**2.记录此车的配载策略**/
		this.markPZCL(cache, yao) ;
		/**3.记录此车的要货令**/
		this.markYHL(cache, yao);
		/**4.记录此车*/
		this.markMX(cache, yao) ;
		
	}
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-26
	 * @time 上午10:06:38
	 * @param cache 数据缓存中心
	 * @param yao 要车明细
	 * @description   记录此车的路线组
	 */
	public void markLXZ(WrapCacheData cache, YaoCJhMx yao)  throws ServiceException{
		//得到记录的缓存区
		Map<String,HashSet<WarpLXZ>> userLXZ = cache.getUserSKD();
		//判断是否存在此发运时刻点
		if(userLXZ.containsKey(yao.getFysj())){
			//记录此车的路线编号
			userLXZ.get(yao.getFysj()).add(yao.getLxz()) ;
		}else{
			//不存在此发运时刻点
			LinkedHashSet<WarpLXZ> hash = new LinkedHashSet<WarpLXZ>();
			//记录此路线编号
			hash.add(yao.getLxz()) ;
			//写入缓存中心
			userLXZ.put(yao.getFysj(), hash);
		}
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-26
	 * @time 上午10:06:38
	 * @param cache 数据缓存中心
	 * @param yao 要车明细
	 * @description   记录此车的配载策略
	 */
	public void markPZCL(WrapCacheData cache, YaoCJhMx yao)  throws ServiceException{
		//得到记录的缓存区
		Map<String,HashSet<String>> userPZCL = cache.getUseCelZ();
		//判断是否存在此发运时刻点
		if(userPZCL.containsKey(yao.getFysj())){
			//记录此车的配载策略
			userPZCL.get(yao.getFysj()).add(yao.getCelbh()) ;
		}else{
			//不存在此发运时刻点
			HashSet<String> hash = new HashSet<String>();
			//记录此车的配载策略
			hash.add(yao.getCelbh()) ;
			//写入缓存中心
			userPZCL.put(yao.getFysj(), hash);
		}
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-26
	 * @time 上午10:06:38
	 * @param cache 数据缓存中心
	 * @param yao 要车明细
	 * @description   记录此车
	 */
	public void markMX(WrapCacheData cache, YaoCJhMx yao)  throws ServiceException{
		//得到记录的缓存区
		Map<String,HashMap<String,List<YaoCJhMx>>>   userSKD = cache.getYaoHz();
		//判断是否存在此发运时刻点
		if(userSKD.containsKey(yao.getFysj())){
			//如存在此仓库编号
			if(userSKD.get(yao.getFysj()).containsKey(yao.getLxz().getCkbh())){
				//记录此车
				userSKD.get(yao.getFysj()).get(yao.getLxz().getCkbh()).add(yao);
			}
			else{
			//不存在此仓库编号
				//得到此时刻点的缓存
				Map<String,List<YaoCJhMx>> ck = userSKD.get(yao.getFysj());
				//新键仓库
				List<YaoCJhMx> yaos = new ArrayList<YaoCJhMx>();
				//缓存数据
				yaos.add(yao) ;
				ck.put(yao.getLxz().getCkbh(), yaos);
			}
		
		}else{
			//新键仓库
			HashMap<String,List<YaoCJhMx>> ck = new HashMap<String, List<YaoCJhMx>>();
			List<YaoCJhMx> yaos = new ArrayList<YaoCJhMx>();
			//缓存数据
			yaos.add(yao) ;
			ck.put(yao.getLxz().getCkbh(), yaos);
			//写入缓存中心
			userSKD.put(yao.getFysj(), ck);
		}

	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-26
	 * @time 上午10:06:38
	 * @param cache 数据缓存中心
	 * @param yao 要车明细
	 * @description   记录此车的要货令
	 */
	public void markYHL(WrapCacheData cache, YaoCJhMx yao)  throws ServiceException{
		//得到记录的缓存区
		Map<String,ArrayList<HashMap<String, String>> > userYHL  =  cache.getUserYHL() ;
		//判断是否存在此发运时刻点
		if(userYHL.containsKey(yao.getFysj())){
			//要货令
			ArrayList<HashMap<String, String>>  yhl = cache.getUserYHL().get(yao.getFysj()) ;
			//记录此车的明细
			for(HashMap<String, String> mx :yao.getYhl()){
				yhl.add(mx) ;
			}
		}else{
			//要货令
			ArrayList<HashMap<String, String>>  yhl = new ArrayList<HashMap<String,String>>() ;
			//记录此车的明细
			for(HashMap<String, String> mx :yao.getYhl()){
				yhl.add(mx) ;
			}
			//写入缓存中心
			userYHL.put(yao.getFysj(), yhl);
		}
	}

	
	
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-22
	 * @time 上午10:32:12
	 * @param yao 要车明细
	 * @param bzzbh 包装编号
	 * @param cache 数据缓存区
	 * @description   计算包装组的计划数量，实际数量，包装倍数基数
	 */
	@SuppressWarnings("unused")
	public void casedBzZJS(YaoCJhMx yao,String bzzbh,WrapCacheData cache) throws ServiceException{
		
		/*根据策略号得到策略组*/
		WarpCelZ celz = cache.getMapCELZ().get(yao.getCelbh());
		/*得到该组下的包装组*/
		Map<String, Integer> bzzSl = celz.getBzzSl();
		/*得到该组下的包装组基数*/
		Map<String, Integer> bzzJs = celz.getBzzJs();
		//包装组计划数量
		yao.setTmpBaoZJhsl(bzzSl.get(bzzbh));
		//包装组计划基数
		yao.setJhjs(bzzJs.get(bzzbh)) ;
		
		//包装组实际数量
		int i=0;
		//包装型号的实际数量
		int j = 0;
		//此包装组的缓存信息
		Map<String,Integer> yaoHLstate = yao.getYaoHLstate().get(bzzbh);
		//如果存在此包装组的状态
		if(yaoHLstate!=null)
		{
			//计算得到此包装组的实际数量
			for(Map.Entry<String, Integer> tmp:yaoHLstate.entrySet()){
				i = i+	tmp.getValue().intValue();
			}
		}
		//包装组实际数量归0
		yao.setTmpBaoZsjsl(i) ;
	}
	
	
	

	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-29
	 * @time 下午03:46:31
	 * @param celH 策略号
	 * @param lx 路线组
	 * @param yaoHL 要货令
	 * @param cache 数据缓存
	 * @param levl 车辆资源级别
	 * @param writedb 是否写入数据库（作推荐配载用）
	 * @description   生成要车明细（满载  的）
	 */
	
	public void getYaoCjhMxOf_Full(String celH,WarpLXZ lx,List<HashMap<String,String>> yaoHL,
			WrapCacheData cache,int levl,int writedb) throws ServiceException
	{
		
		/*根据策略号得到策略组*/
		WarpCelZ celz = cache.getMapCELZ().get(celH);
		
		// 如果无车辆资源，则进入下一策略匹配
		if (!hasCLZY(lx, celz, cache, levl)) {
				return;
		}
		/*得到该组下的包装组*/
		Map<String, Integer> bzzSl = celz.getBzzSl();
		/*得到该组下的包装组基数*/
		Map<String, Integer> bzzJs = celz.getBzzJs();
		/*包装组编码集合*/
		Iterator<String> bzKs= bzzSl.keySet().iterator();
		/*要车明细*/
		YaoCJhMx  yao = new YaoCJhMx();
		/*设置路线组*/
		yao.setLxz(lx) ;
		//计算计划要贷令数量
		this.caculateClJhsl(yao, bzzSl) ;
		bzKs = bzzSl.keySet().iterator();
		while(bzKs.hasNext())
		{
			/*包装组编码*/
			String bzK= bzKs.next();
			/*此包装组需要数量*/
			//包装组计划数量
			yao.setTmpBaoZJhsl(bzzSl.get(bzK));
			//包装组计划基数
			yao.setJhjs(bzzJs.get(bzK)) ;
			//包装组实际数量归0
			yao.setTmpBaoZsjsl(0) ;
			/*是否能装满此包装*/
			yao.setFull(false) ;
			/*装车计划   */
			yao = this.caculateStore(yao, bzK, yaoHL,YXJS) ;
			//如果装满 则进入下一包装组
			if(yao.isFull())
			{
				continue;
			}
			else
			{
				//是否写入数据库,推荐配载用
				if(levl==CLZY_YXJ_4){
					this.tjpzYhl(yaoHL, cache) ;
				}
				return  ;
			}
		}
		
		//将装车后的要贷令移除
		this.removeYaoHL(yao.getYhlbh(), yaoHL) ;
		//缓存已装满的要车明细
		cache.getYaoMZ().add(yao) ;
		//是否写入数据库
		if(writedb==WRITEDB_YES){
			//生车要车明细
			yao = this.createMX(cache,lx, yao, celz) ;
			//缓存车辆
			this.cacheCl(yao, cache);
			//锁定要货令
			this.lockYaoHL(yao, cache.getParam());
			 //同步车辆资源
			Map<String,String> map  = new HashMap<String, String>();
			 map.put("GYS", yao.getCys()) ;
			 map.put("CX", yao.getCllx()) ;
			this.syncCelZY(map, cache) ;
			//写入要车明细
			writeYAOCMx(yao, cache) ;
		}
		else{
			//记录策略编号
			yao.setCelbh(celH);
			//记录车辆类型
			yao.setCllx(celz.getCx()) ;
			//返回要货令
			return ;
			
		}
		//递归装车	
		this.getYaoCjhMxOf_Full(celH,lx, yaoHL, cache,levl,writedb);
	}
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-29
	 * @time 上午11:44:22
	 * @param yaoHL 未装车的要货令
	 * @param cache 数据中心
	 * @description   推荐配载时，会记录未装车的要货令
	 */
	public void tjpzYhl(List<HashMap<String,String>> yaoHL,WrapCacheData cache) throws ServiceException{
		
		//迭代未装车的要货令
		for(Map<String,String> map :yaoHL)
		{
			//如果此要货令，未装车,且未分配
			if(!cache.getYhlbh().contains(map.get("YAOHLH"))){
				//记录此要要货令
				cache.getYhl().add((HashMap<String,String>)map) ;
				//记录此要货令的编号
				cache.getYhlbh().add(map.get("YAOHLH")) ;
			}
		}
	}
	
	
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-13
	 * @time 上午11:10:32
	 * @param yao 要车计划明细
	 * @param bzZ 包装组
	 * @param baoLX 包装类型
	 * @return YaoCJhMx
	 * @description  判断能否将此包装组装满
	 */
	public YaoCJhMx caculateStore( YaoCJhMx yao,String bzK,List<HashMap<String,String>> yaoHL,int jsLevl ) throws ServiceException
	{
		
		
		/*迭代要贷令集合,判断是否有此包装组*/
		for(Map<String,String> map: yaoHL)
		{
			//判断要货令的仓库是否与此车的仓库不一致辞
			if(!map.get("NEIBGYS_CANGKBH").equalsIgnoreCase(yao.getLxz().getCkbh())){
				//如果一致
				continue;
			}

			if(map.get("LXZ") != null && map.get("LXZ").indexOf("-"+yao.getLxz().getLzxbh()+"-")==-1){
				//如果一致
				continue;
			}
			
			//如果存在此包装组,该要贷令尚未装车
			if(map.get("BAOZZBH").equalsIgnoreCase(bzK)&&!yao.getYhlbh().contains(map.get("YAOHLH")))
			{
				//实际基数归0
				yao.setSjjs(0) ;
				//基数计算
				this.caculateJs(yaoHL, yao, map.get("BAOZXH"), bzK) ;
				
				//如果此基数没有装满
				if(!yao.isFull()){
					this.deductionJs(yao, map.get("BAOZXH"), bzK) ;
					continue;
//					break;	
					}				
//				}
				
				/*设置此包装组是否装满*/
				yao.setFull(yao.getTmpBaoZJhsl()==yao.getTmpBaoZsjsl()) ;
				/*如果包装装满,则终止循环*/
				if(yao.isFull()){
					break;					
				}
			}
		}
		
		
		//如果此包装组没有装满
		if(jsLevl==QCJS&&!yao.isFull()){
			//则考滤基数
			this.caculateStoreNoMz(yao, bzK, yaoHL) ;
		}
		
		return yao;
	}
	
	/*****2012-04-16*******/
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-13
	 * @time 上午11:10:32
	 * @param yao 要车计划明细
	 * @param bzZ 包装组
	 * @param baoLX 包装类型
	 * @return YaoCJhMx
	 * @description  判断能否将此包装组装满
	 */
	public YaoCJhMx caculateStoreNoMz( YaoCJhMx yao,String bzK,List<HashMap<String,String>> yaoHL ) throws ServiceException
	{
		
			/*迭代要贷令集合,判断是否有此包装组*/
			for(Map<String,String> map: yaoHL)
			{
				//判断要货令的仓库是否与此车的仓库不一致辞
				if(!map.get("NEIBGYS_CANGKBH").equalsIgnoreCase(yao.getLxz().getCkbh())){
					//如果一致
					continue;
				}
				if(map.get("LXZ") != null && map.get("LXZ").indexOf("-"+yao.getLxz().getLzxbh()+"-")==-1){
					//如果一致
					continue;
				}
				//如果存在此包装组,该要贷令尚未装车
				if(map.get("BAOZZBH").equalsIgnoreCase(bzK)&&!yao.getYhlbh().contains(map.get("YAOHLH")))
				{
					//实际基数归0
					yao.setSjjs(0) ;
					//基数计算
					this.caculateJs(yaoHL, yao, map.get("BAOZXH"), bzK) ;
					/*设置此包装组是否装满*/
					yao.setFull(yao.getTmpBaoZJhsl()==yao.getTmpBaoZsjsl()) ;
					/*如果包装装满,则终止循环*/
					if(yao.isFull()){
						break;					
					}
				}
			}
		return yao;
	}
	
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-24
	 * @time 下午01:37:45
	 * @param yaoHL 要贷令集合
	 * @param yao 车辆
	 * @param BAOZXH 包装型号
	 * @param BAOZZBH 包装组代码
	 * @description  计算基数
	 */
	public void deductionJs(YaoCJhMx yao,
			String BAOZXH,String BAOZZBH) throws ServiceException
	{ 
		int sjjs = yao.getSjjs();
		//回滚数据
		for(int i=0;i<sjjs;i++)
		{
			/*迭代要贷令集合,依据包装组基数装满此车*/
			for(Map<String,String> map: yao.getYhl())
			{
				//如果包装型号想同且包装组相同
				if(map.get("BAOZXH").equalsIgnoreCase(BAOZXH)&&map.get("BAOZZBH").equalsIgnoreCase(BAOZZBH))
				{
					
						//回滚此车的要货令状态
						this.removeYaoHLState(BAOZZBH, BAOZXH, yao) ;
						/*如存在,则包装组实际数量+1*/
						yao.setTmpBaoZsjsl(yao.getTmpBaoZsjsl()-1);
						/*实际要贷令数加1*/
						yao.setSjsl(yao.getSjsl()-1);
						/*包装基数加1*/
						yao.setSjjs(yao.getSjjs()-1) ;
						/*设置此基数是否达标*/
						yao.setFull(yao.getJhjs()==yao.getSjjs()) ;
						/*设置要贷令编号*/
						yao.getYhlbh().remove(map.get("YAOHLH")) ;
						/*记录此要货令*/
						yao.getYhl().remove((HashMap<String,String>)map) ;
						break;
					}
				}
		}
			
	}
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-22
	 * @time 上午09:58:47
	 * @param bzzBH 包装组编号
	 * @param bzzXH 包装型号
	 * @param yao 要车明细
	 * @description   保存车辆状态
	 */
	public void  removeYaoHLState(String bzzBH,String bzzXH,YaoCJhMx yao) throws ServiceException
	{
		
		//回滚车辆状态
		if(yao.getYaoHLstate().containsKey(bzzBH)){
			
			//得到包装型号的状态
			 HashMap<String, Integer> bzzXHState = yao.getYaoHLstate().get(bzzBH);
			 //如果此型号的数量与基数相等
			 if(bzzXHState.get(bzzXH).intValue()==yao.getSjjs()){
				 yao.getYaoHLstate().get(bzzBH).remove(bzzXH);
				 if(bzzXHState.keySet().size()==0){
					 yao.getYaoHLstate().remove(bzzBH);
				 }
			 }else{
				 yao.getYaoHLstate().get(bzzBH).put(bzzXH, bzzXHState.get(bzzXH).intValue()-yao.getSjjs());
			 }
		}
		
	}
	/********end********/
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-24
	 * @time 下午01:37:45
	 * @param yaoHL 要贷令集合
	 * @param yao 车辆
	 * @param BAOZXH 包装型号
	 * @param BAOZZBH 包装组代码
	 * @description  计算基数
	 */
	public void caculateJs(List<HashMap<String,String>> yaoHL ,YaoCJhMx yao,
			String BAOZXH,String BAOZZBH) throws ServiceException
	{ 
		/*迭代要贷令集合,依据包装组基数装满此车*/
		for(Map<String,String> map: yaoHL)
		{
			//判断要货令的仓库是否与此车的仓库不一致辞
			if(!map.get("NEIBGYS_CANGKBH").equalsIgnoreCase(yao.getLxz().getCkbh())){
				//如果一致
				continue;
			}

			if(map.get("LXZ") != null && map.get("LXZ").indexOf("-"+yao.getLxz().getLzxbh()+"-")==-1){
				//如果一致
				continue;
			}
			
			//如果包装型号想同且包装组相同
			if(map.get("BAOZXH").equalsIgnoreCase(BAOZXH)&&map.get("BAOZZBH").equalsIgnoreCase(BAOZZBH))
			{
				//如果此车车辆没有装载此要贷令
				if(!yao.getYhlbh().contains(map.get("YAOHLH")))
				{
					/*如存在,则包装组实际数量+1*/
					yao.setTmpBaoZsjsl(yao.getTmpBaoZsjsl()+1);
					/*实际要贷令数加1*/
					yao.setSjsl(yao.getSjsl()+1);
					/*包装基数加1*/
					yao.setSjjs(yao.getSjjs()+1) ;
					/*设置此基数是否达标*/
					yao.setFull(yao.getJhjs()==yao.getSjjs()) ;
					/*设置要贷令编号*/
					yao.getYhlbh().add(map.get("YAOHLH")) ;
					/*设置包装编号*/
					this.setBaoZs(map.get("BAOZXH"), 1, yao);
					/*设置零件数量*/
					this.setLinJ(map.get("LINGJBH"),Integer.parseInt(map.get("YAOHSL")),yao);
					/*设置此车的包装状态*/
					this.setYaoHLState(BAOZZBH, BAOZXH, yao);
					/*记录此要货令*/
					yao.getYhl().add((HashMap<String,String>)map) ;
					
					//判断此包装组是否装满
					if(yao.getTmpBaoZJhsl()==yao.getTmpBaoZsjsl()){
						break;
					}
					/*如果包装组的基数已达到,则终止循环*/
					if(yao.isFull()){
						break;						
					}
				}
			}
			
		}
	}
	
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-22
	 * @time 上午09:58:47
	 * @param bzzBH 包装组编号
	 * @param bzzXH 包装型号
	 * @param yao 要车明细
	 * @description   保存车辆状态
	 */
	public void  setYaoHLState(String bzzBH,String bzzXH,YaoCJhMx yao) throws ServiceException
	{
		HashMap<String,Integer> bzzxh = null;
		//判断此车没有此包装组
		if(!yao.getYaoHLstate().containsKey(bzzBH)){
			//创建一个数据缓冲区保存此包装编号的状态
			bzzxh = new HashMap<String, Integer>();
			bzzxh.put(bzzXH, 1);
		}
		else{
			//如果此车存在此包装编号
			if(yao.getYaoHLstate().get(bzzBH).containsKey(bzzXH)){
				//得到此包装组的缓存
				 bzzxh = yao.getYaoHLstate().get(bzzBH);
				//将其数据加1
				bzzxh.put(bzzXH, bzzxh.get(bzzXH).intValue()+1);
			}
			else{
				//如果不存在此包装型号
				//创建一个数据缓冲区保存此包装编号的状态
			    bzzxh = new HashMap<String, Integer>();
				bzzxh.put(bzzXH, 1);
			}
			
		}
		//缓存数据
		yao.getYaoHLstate().put(bzzBH, bzzxh);
	}
	
	
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 下午05:09:28
	 * @param yaoHLBh 需要移除的要贷令集合
	 * @param yaoHL  要贷令集合
	 * @description   移除要贷令
	 */
	public void removeYaoHL(Set<String> yaoHLBh,List<HashMap<String,String>> yaoHL) throws ServiceException
	{
		Iterator<String> yhs =  yaoHLBh.iterator();
		while(yhs.hasNext())
		{
			String s = yhs.next();
//			for(Map<String,String> map :yaoHL)
//			{
//				//如果要移除的要贷令编号存在于要要贷令集合中,如存在则有移除，并终止循环
//				if(s.equalsIgnoreCase(map.get("YAOHLH"))){
//					yaoHL.remove(map) ;
//					break;
//				}
//			}
			
			for(int i=0;i<yaoHL.size() ;i++)
			{
				//如果要移除的要贷令编号存在于要要贷令集合中,如存在则有移除，并终止循环
				if(s.equalsIgnoreCase(yaoHL.get(i).get("YAOHLH"))){
					yaoHL.remove(yaoHL.get(i)) ;
					i--;
				}
			}
			
		}
	}
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 下午05:10:39
	 * @description  计算此车 计划要贷令数量
	 */
	public void caculateClJhsl(YaoCJhMx  yao,Map<String, Integer> bzzSl) throws ServiceException
	{
		/*包装组编码集合*/
		Iterator<String> bzKs= bzzSl.keySet().iterator();
		//计算计划要贷令
		while(bzKs.hasNext())
		{
			/*包装组编码*/
			String bzK= bzKs.next();
			//计划数量
			yao.setJhsl(yao.getJhsl()+bzzSl.get(bzK));
		}
	}
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-10
	 * @time 下午04:17:03
	 * @param celH 策略号
	 * @param param 参数  KEY:{KEH:客户编码   DATE:发运日期}
	 * @param celHZ  策略组
	 * @param yaoHL 要贷令号
	 * @param yaoCJhMx 要车明细
	 * @return List<YaoCJhMx> 要车明细
	 * @description   得到要车计划明细（未装满的）
	 */
	public List<YaoCJhMx>  getYaoCjhMxOf_NotFull(String celH,WarpLXZ lx,List<HashMap<String,String>> yaoHL,
			WrapCacheData cache,List<YaoCJhMx> yaoCJhMx,Set<String>  zy ,int levl) throws ServiceException
	{
		/*根据策略号得到策略组*/
		WarpCelZ celz = cache.getMapCELZ().get(celH);
		// 如果无车辆资源，则进入下一策略匹配
		if (!hasCLZY(lx, celz, cache, levl)) {
			    //如果此策略无资源，则记录下来
			     zy.add(celH) ;
				return yaoCJhMx;
		}
		/*得到该组下的包装组*/
		Map<String, Integer> bzzSl = celz.getBzzSl();
		/*得到该组下的包装组基数*/
		Map<String, Integer> bzzJs = celz.getBzzJs();
		/*包装组编码集合*/
		Iterator<String> bzKs= bzzSl.keySet().iterator();
		/*要车明细*/
		YaoCJhMx  yao = new YaoCJhMx();
		/*设置路线组*/
		yao.setLxz(lx) ;
		//计算计划要贷令数量
		this.caculateClJhsl(yao, bzzSl) ;
		while(bzKs.hasNext())
		{
			/*包装组编码*/
			String bzK= bzKs.next();
			/*此包装组计划数量*/
			yao.setTmpBaoZJhsl(bzzSl.get(bzK));
			/*设置包装实际*/
			yao.setTmpBaoZsjsl(0) ;
			//包装组计划基数
			yao.setJhjs(bzzJs.get(bzK)) ;
			/*是否能装满此包装*/
			yao.setFull(false) ;
			//装车计划 
			this.caculateStore(yao, bzK, yaoHL,QCJS) ;
		}
		
//		//生车要车明细
		yao = this.createMX(cache,lx, yao , celz) ;
		
//		// (如是不是空车)
		if(yao.getSjsl()!=0){
			
			//新增要车明细
			yaoCJhMx.add(yao);	
		}
		
		return yaoCJhMx;
	}
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 上午10:17:12
	 * @param param
	 * @param yao
	 * @param cache
	 * @param celz 
	 * @return
	 * @description  要车明细
	 */
	public YaoCJhMx createMX(WrapCacheData cache,final WarpLXZ lx,YaoCJhMx  yao,WarpCelZ celz) throws ServiceException
	{
		/* 设备包装明细 */
		//车型
		yao.setCllx(celz.getCx());
		//车辆名称
		yao.setClName(celz.getCxName()) ;
		//发运时间
		yao.setFysj(cache.getParam().get("KSSJ"));
		//要车时间
		yao.setYcsj(yao.getFysj()==null?null:DateUtil.DateSubtractMinutes(yao.getFysj(), lx.getYaoctqq()));
		//客户编码
		yao.setKhbm(cache.getParam().get("KEH"));
		//策略编号
		yao.setCelbh(celz.getCelH()) ;
		//设置运输商编号
		yao.setCys(lx.getCys());
		//车辆来源
		yao.setNote(lx.getClly()) ;
		//满载率
		yao.setZmy((float)(yao.getSjsl()) /(float)(yao.getJhsl()));
		return yao;
	}
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-11
	 * @time 下午02:45:24
	 * @param list 未装满车的明细
	 * @param yaoHL 要贷令号
	 * @param celLZy 车辆申报资源
	 * @return YaoCJhMx
	 * @description  返回一条装满率最高的要车明细
	 */
	public YaoCJhMx getBestCelByZMY(List<YaoCJhMx> list,List<HashMap<String,String>> yaoHL,
			WrapCacheData cache,int levl) throws ServiceException
	{
		YaoCJhMx yao = null;
		/*得到装满率最高的那条要车明细*/
		for(YaoCJhMx tmp:list)
		{
			if(yao==null){
				yao = tmp;				
			}
			else
			{
				// 排序得到要贷令装载率最高的车型
				if (yao.getYhlbh().size() <= tmp.getYhlbh().size()){
					yao = tmp;					
				}
			}
		}
		if(yao == null){
			return yao;			
		}
	
		//将装车后的要贷令移除掉
		this.removeYaoHL(yao.getYhlbh(), yaoHL) ;
		//更新车辆资源
		Map<String, String> map = new HashMap<String, String>();
		map.put("GYS", yao.getCys());
		map.put("CX", yao.getCllx());
		// 如果是推荐配载,则直接返回
		if (levl == CLZY_YXJ_4) {
			return yao;
		}
		// 锁定要货令
		this.lockYaoHL(yao, cache.getParam());
		// 同步车辆资源
		this.syncCelZY(map, cache);

		return yao;
	}

	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-11
	 * @time 下午02:48:42
	 * @param param 参数
	 * @param list 要车明细
	 * @param celHZ 策略号组
	 * @param yaoHL 要贷令号
	 * @param celLZy 车辆资源
	 * @return List<YaoCJhMx> 
	 * @description   返回没有装满的车辆
	 */
	public List<YaoCJhMx> getYaoCMxOf_NotFull(final WarpLXZ lx,final Set<String> celHZ ,
			List<HashMap<String,String>> yaoHL,WrapCacheData cache,int levl) throws ServiceException
	{
		//策略资源
		Set<String> zy = new HashSet<String>();
		//直到要贷令全部发车
		while (true)
		{
			//如果要货令为空
			if(yaoHL.size()==0){
				break;				
			}
			//如果级别为1,2
			if(levl==CLZY_YXJ_1||levl==CLZY_YXJ_2){
				//如果所有的策略的车辆资源为0
				if(zy.size()==celHZ.size()){
					break;		
				}
			}
			
			//策略号组
			Iterator<String>  celHs = 	celHZ.iterator();
			//要车明细 (待选择的)
			List<YaoCJhMx> _tmp =  new ArrayList<YaoCJhMx>();
			
			//策略号
			String celh = null;
			while(celHs.hasNext())
			{
				 celh = celHs.next();
				 this.getYaoCjhMxOf_NotFull(celh, lx,yaoHL , cache,_tmp,zy, levl) ;
			}
			//选出装满率最高的配载策略
			YaoCJhMx mx = this.getBestCelByZMY(_tmp,yaoHL , cache,levl) ;
			//如果有要车明细
			if(mx!=null)
			{
				cache.getYaoSKD().add(mx) ;
				// 如果是推荐配载,则直接返回
				if (levl == CLZY_YXJ_4) {
					break;
				}
				
			}//此路线组下无任何车辆
			else{
				break;
			}
		}
		return cache.getYaoSKD();
	}
	
	
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-22
	 * @time 下午02:25:14
	 * @param params {UC:用户中心,DATE:日期时间}
	 * @description 初始化数据缓存中心 
	 */
	public void initCacheDataUC(Map<String, String>  params,List<Map<String, String>> kehGys,
			WrapCacheData cache) throws ServiceException{
		
		/*0.计算并更新要货令表中的发运时间*/
//		this.updateFaYSJ(params) ;
		/*1.基本参数*/
        cache.setParam(params);
        /*2.设置计划序号 格式化成YYYY-MM-DD*/
        cache.setJhxh(DateUtil.stringSFMToStringYMD(params.get("DATE")));
        /*3.设置路线组*/
        //归集路线组
        List<Map<String, String>> lxzList = this.selectLuXZ(params) ;
        //如果路组为空,则终止执行
        if(lxzList.size()==0){
        	 cache.setMapLXZ(null) ;
        	 return ;
        }
        cache.setLxzList(this.selectYhlLuXZ(params));
        //设置路线组
        cache.setMapLXZ(getWarpLXZ(kehGys, lxzList)) ;
        /*4.设置路线组的交付时刻*/
        //归集交付时刻 
        List<Map<String, String>> jfskList = this.selectJiaoFSK(params) ;
      //如果路组为空,则终止执行
        if(jfskList.size()==0){
        	 cache.setMapJiaoFSK(null) ;
        	 return ;
        }
        //设置交付时刻
        cache.setMapJiaoFSK(warpJiaoFSK(jfskList, cache.getJhxh())) ;
        /*5.车辆申报资源*/
        //归集车辆资源
        List<Map<String, String>> clsbList = this.selectCLBySB(params);
        //设置车辆资源
        cache.setMapCLBySB(warpCelL(clsbList));
        /*6.用户中心车辆资源*/
     	 //归集车辆资源
        List<Map<String, String>> yhzxClist = this.selectCLByUC(params) ;
        //设置车辆资源
        cache.setMapCLByUC(warpCelL(yhzxClist));
        /*7.下一天的第一个交付时刻*/
        //归集下一天的交付时刻
		List<Map<String, String>> nextJiaoFsk = this.selectFirstJiaoFSK(params) ;
        //设置交会付时刻
        cache.setNextDayJiaoFSK(wrapFirstJiaoFSK(nextJiaoFsk)) ;
        /*8.归集路线组的仓库*/
        List<Map<String, String>> lxzCk = this.selectLXZCK(params) ;
        //设置仓库
        cache.setMapCK(wrapLXZCK(lxzCk)) ;
        
	}
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-22
	 * @time 下午04:04:22
	 * @param key 供应商-客户
	 * @param cache 数据缓存区
	 * @param cllevl 车辆资源优先级
	 * @param zmyLevel 满载优先级
	 * @description  优先装满率
	 */
	public void priorityMZY(String key,WrapCacheData cache,int cllevl,int zmyLevel) throws ServiceException{
		// 路线组
		List<WarpLXZ> lxz = cache.getMapLXZ().get(key);
		// 参数
		Map<String, String> params = cache.getParam();
		for (WarpLXZ lx : lxz) {
			// 时间区
			ArrayList<LinkedList<String>> sj = cache.getMapJiaoFSK().get(lx.getLzxbh());
			//迭代时间
//			for (LinkedList<String> s : sj) {
			for (int i = 0;sj != null && i<sj.size();i++ ) {	
				LinkedList<String> s = sj.get(i);
				// 开始时间
				if(i == 0){
					params.put("KSSJTEMP", "2000-01-01 00:00");
				}else{
					params.put("KSSJTEMP", s.getFirst());
				}
				params.put("KSSJ", s.getFirst());
				//计算结束时间
				caculateSJ(params, s, sj, cache, lx) ;
				/* 归集配载策略 */
				List<Map<String, String>> pzzxList = this.selectCelZ(params);
				/* 如果此时间内没有匹配的配载策略,则进入下一配载策略 */
				if (pzzxList == null || pzzxList.size() == 0) {
					continue;
				}
				// 策略组
				Map<String, WarpCelZ> mapCELZ = this.getWarpCelZ(pzzxList);
				//将此策略集合设置此缓存中心
				cache.setMapCELZ(mapCELZ) ;
				// 迭代策略组
				for (Map.Entry<String, WarpCelZ> map : mapCELZ.entrySet()) {

					// 如果无车辆资源，则进入下一策略匹配
					if (!hasCLZY(lx, map.getValue(), cache, cllevl)) {
						continue;
					}

					// 归集要货令
					List<HashMap<String, String>> yaoHL = this.selectYaoHLGJ(cache.getParam());
					// 如果无要货令，则进入下一策略
					if (yaoHL.size() == 0) {
						continue;
					}
					setYhlLxz(yaoHL,cache.getLxzList());
					//根据满载率优先级,车辆资源优先级计算要车明细
					this.caculateYaoCMXByMzy(params, map, mapCELZ, yaoHL, lx, cache, cllevl, zmyLevel) ;
				}
			}
		}
	}
	
	public void setYhlLxz(List<HashMap<String, String>> yaoHL,List<HashMap<String, String>> lxz){
		for(HashMap<String, String> yhlMap :yaoHL){
			String yhlucangkKeh = yhlMap.get("USERCENTER")+yhlMap.get("NEIBGYS_CANGKBH")+yhlMap.get("KEH");
			for( HashMap<String, String> lxzMap:lxz){
				if(yhlucangkKeh.equals(lxzMap.get("USERCENTER")+lxzMap.get("CANGKBH")+lxzMap.get("KEHBH"))){
					yhlMap.put("LXZ", yhlMap.get("LXZ")+lxzMap.get("YUNSLXBH")+"-");
				}
			}
		}
	}
	
	/**
	 * 计算时间
	 * @param params 参数
	 * @param s 时间点
	 * @param sj 时间集
	 * @param cache 数据中心
	 * @param lx 路线组
	 */
	public void caculateSJ(Map<String, String> params,LinkedList<String> s,
			List<LinkedList<String>> sj,WrapCacheData cache,WarpLXZ lx) throws ServiceException{
		// 如果是最后时间点
		if (sj.indexOf(s) == sj.size() - 1)	{
			//如果路线组存在此第二天的第一个交付时刻
			if(cache.getNextDayJiaoFSK().containsKey(lx.getLzxbh())){
			// 结束时间
			params.put("JSSJ", cache.getNextDayJiaoFSK().get(lx.getLzxbh()));
			}else{
				String time = cache.getJhxh()+" "+TIME1;
				params.put("JSSJ", time);
			}
		}
		else{
		// 结束时间
		params.put("JSSJ", s.getLast());
		}
	}
	
	/**
	 *  根据满载率计算要车明细
	 * @param params 参数
	 * @param map 配载策略
	 * @param mapCELZ 配载策略集合
	 * @param yaoHL 要货令集合
	 * @param lx 路线组
	 * @param cache 数据中心
	 * @param cllevl 车辆资源优先级
	 * @param zmyLevel 满载率优先级
	 */
	public void caculateYaoCMXByMzy(Map<String, String> params,Map.Entry<String, WarpCelZ> map,
			Map<String, WarpCelZ> mapCELZ,List<HashMap<String, String>> yaoHL,WarpLXZ lx,
			WrapCacheData cache,int cllevl,int zmyLevel) throws ServiceException{
		// 满载率优先级
		switch (zmyLevel)
		{
			case MZY_YXJ_1:
				// 生成要车明细,装满的
				this.getYaoCjhMxOf_Full(map.getKey(),lx,yaoHL,cache,cllevl,WRITEDB_YES);
				break;
			case MZY_YXJ_2:
				// 未装满的
				this.getYaoCMxOf_NotFull(lx, mapCELZ.keySet(), yaoHL, cache, cllevl);
				//交付时刻+路线组最大提前期
				String date = DateUtil.dateAddMinutes(cache.getParam().get("JSSJ"), lx.getLxzdztqq()) ;   
				cache.getParam().put("KSSJ",params.get("JSSJ"));
				cache.getParam().put("JSSJ",date); 

				// 归集要货令
				List<HashMap<String, String>> tmps = this.selectYaoHL(cache.getParam());
				if (tmps.size() == 0) {
					
					for(YaoCJhMx o :cache.getYaoSKD()){
						/*记录此车，等待混装计算*/
						this.markYAO(cache, o) ;
					}
					cache.getYaoSKD().clear();

					return;
				}
				setYhlLxz(tmps,cache.getLxzList());
				//计算未装满的车
				this.casedYaoCMX(tmps, cache);
				//清空缓存
				cache.getYaoSKD().clear();

				break;
		}
	}
	
	
		
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-23
	 * @time 上午11:28:54
	 * @param lx 路线组
	 * @param celz  策略组
	 * @param cache 缓存中心
	 * @param cllevl 车辆资源级别
	 * @return  boolean
	 * @description  判断是否有无车辆资源
	 */
	public boolean hasCLZY(WarpLXZ lx,WarpCelZ celz,WrapCacheData cache,int cllevl) throws ServiceException{
		// 是否有无车辆资源
		boolean hasZY = false;
		// 车辆资源优先级
		switch (cllevl)
		{
			// 车辆申报资源
			case CLZY_YXJ_1:
				hasZY = this.getLxBySB(lx,celz, cache);
				break;
			// 用户中心资源
			case CLZY_YXJ_2:
				hasZY = this.getLxByUC(lx, celz, cache);
				break;
			// 最优方案
			case CLZY_YXJ_3:
				hasZY = this.getLxByHigh(lx, celz	,cache );
				break;
			//不考滤车辆型号和运输商
			case CLZY_YXJ_4:
				hasZY = true;
				break;
		}
		
		return hasZY;
	}
	
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-11
	 * @time 下午03:29:39
	 * @param UC 用户中心
	 * @param TOMORROW 第二天的时间
	 * @return List<YaoCJhMx>
	 * @description   返回要车明细
	 */
	public WrapCacheData shengCYaoCJHmx(String UC,String TOMORROW) throws ServiceException

	{
		 Map<String, String>  params = new HashMap<String, String>();
		 params.put("DATE", TOMORROW);
         params.put("UC", UC);
 		/*0.计算并更新要货令表中的发运时间*/
 		int ynum = this.updateFaYSJ(params) ;
 		logger.info("更新要货令发运时间要货令条数为："+ynum);
         /* 归集客户 ,供应商*/
 		 List<Map<String, String>> kehGys = this.selectKehGYS(params);
 		/* 如果没有客户要贷令,则终止退出 */
 		 if (kehGys == null || kehGys.size() == 0){
 		 	return null;			
 		 }
         /*创建数据缓存区*/
         WrapCacheData cache = new WrapCacheData();
         /*初始化数据缓存*/
         this.initCacheDataUC(params, kehGys, cache) ;
		
         /*如果路线组为空*/
         if(cache.getMapLXZ()==null){
        	 return null;
         }
         /*如果交付时刻为空*/
         if(cache.getMapJiaoFSK()==null){
        	 return null;
         }
         
		/* 要车明细 */
//		List<YaoCJhMx> yaoCMx = new ArrayList<YaoCJhMx>();
		
		/*归集路线组*/
		
         /*依客户与供应商*/
		 for(Map<String,String> keHmap:kehGys)
		 {
			 params.put("KEH", keHmap.get("KEH"));
			 
			 String key =  keHmap.get("KEH");
			 /*1.优先车辆资源*/
			 //1).优先车辆申报资源,并优先满载率1
			 this.priorityMZY(key, cache, CLZY_YXJ_1,MZY_YXJ_1) ;
			 //2).优先车辆申报资源,不考滤满载率2
			 this.priorityMZY(key, cache, CLZY_YXJ_1,MZY_YXJ_2) ;
			 /*用户中心资源*/
			 //1).其次用户中心资源,并优先满载率1
			 this.priorityMZY(key, cache, CLZY_YXJ_2,MZY_YXJ_1) ;
			 //2).其次用户中心资源,并优先满载率1
			 this.priorityMZY(key, cache, CLZY_YXJ_2,MZY_YXJ_2) ;
			 /*最优方案*/
			 //1).不考滤车辆资源,并优先满载率1
			 this.priorityMZY(key, cache, CLZY_YXJ_3,MZY_YXJ_1) ;
			 //2).不考滤车辆资源,并优先满载率1
			 this.priorityMZY(key, cache, CLZY_YXJ_3,MZY_YXJ_2) ;
		 }
		 
//		 //混装
//		 System.out.println("混装前:") ;
//		 CopyOfYaocjhTest.print(cache) ;
		 this.caculateHZ(cache) ;
		
		 return cache;
	}
	
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-26
	 * @time 上午10:51:02
	 * @param cache 数据中心
	 * @description  混装
	 */
	public void caculateHZ(WrapCacheData cache) throws ServiceException{
		
		// 迭代发运时刻点
		for (Entry<String, HashSet<WarpLXZ>> fysj : cache.getUserSKD().entrySet()) {
			WrapCacheData tmpAll = (WrapCacheData) WrapCacheData.deepCopy(cache);

			// 发运时刻
			String fysk = fysj.getKey();
			HashMap<String, List<YaoCJhMx>> ckclsAll = tmpAll.getYaoHz().get(fysk);
			ckclsAll.clear();
			// 混装车辆
			HashMap<String, List<YaoCJhMx>> ckcls = cache.getYaoHz().get(fysk);
			// 迭代路线组
			for (Iterator<WarpLXZ> lxs = fysj.getValue().iterator(); lxs
					.hasNext();) {
				// 策略组
				Set<String> cel = cache.getUseCelZ().get(fysk);
				// 复制数据中心
				WrapCacheData tmp = (WrapCacheData) WrapCacheData.deepCopy(cache);
				// 回滚车辆资源
				syncRoolBackHZCelZY(fysk, cache);
				tmp.setMapCLBySB(cache.getMapCLBySB());
				tmp.setMapCLByUC(cache.getMapCLByUC());
				tmp.getParam().put("KSSJ",fysk);
				// 清空车辆明细
				tmp.getYaoSKD().clear();
				// 路线组
				WarpLXZ lxz = lxs.next();
				// 要货令
				List<HashMap<String, String>> yhl = cache.getUserYHL().get(fysk);
             
				// 得到此路线组的仓库
				HashSet<String> cks = cache.getMapCK().get(lxz.getLzxbh());
				for (Iterator<String> ck = cks.iterator(); ck.hasNext();) {
					// 仓库编号
					String ckbh = ck.next();
					List<YaoCJhMx> cls = ckcls.get(ckbh);
					//如果此仓库下在这个时间点没有要混装的车辆
					if(cls==null){
						//进入下一个循环
						continue;
					}
					/* 如果此时间点没有要混装的车辆 */
					if (cls.size() < 1) {
						// 进入下一仓库计算
						continue;
					}
					// 设置仓库编号
					lxz.setCkbh(ckbh);
					/* 优先车辆资源 */
					this.getYaoCMxOf_NotFull(lxz, cel, yhl, tmp, CLZY_YXJ_1);
					/* 其次用户中心 */
					this.getYaoCMxOf_NotFull(lxz, cel, yhl, tmp, CLZY_YXJ_2);
					/* 最优方案 */
					this.getYaoCMxOf_NotFull(lxz, cel, yhl, tmp, CLZY_YXJ_3);

//					if (tmp.getYaoSKD().size() < cls.size()) {
//						// 使用混装的数量数
//						ckcls.put(ckbh, tmp.getYaoSKD());
//						ckclsAll.get(ckbh).addAll(tmp.getYaoSKD());
//					}
					if(ckclsAll.get(ckbh)== null || ckclsAll.get(ckbh).size() ==0){
						ckclsAll.put(ckbh, tmp.getYaoSKD());
					}else{
						ckclsAll.get(ckbh).addAll(tmp.getYaoSKD());
					}
				}
			}
			
			/**混装后的要车明细写入数据库**/
			cache.getYaoHz().put(fysk, ckclsAll);
			/**进入下一个时间点的混装**/
		}
		
//		 System.out.println("混装后:") ;
//		 CopyOfYaocjhTest.print(cache) ;
		//将生成的要车明细写入数据库
		//迭代混装后的车辆,得到仓库-车辆明细
		for(Map.Entry<String, HashMap<String,List<YaoCJhMx>> > fysj :cache.getYaoHz().entrySet()){
			//迭代仓库-车辆明细,得到车辆明细
			for(Map.Entry<String, List<YaoCJhMx> > o :fysj.getValue().entrySet()){
			 //写入数据库
			 this.writeDB(o.getValue(), cache ) ;
			}
		 }
		 /*写入数据库*/
		//新增要车计划总表
		this.addYaoCJHZb(cache.getJhxh(), cache.getCls(), cache.getClmc(),cache.getParam().get("UC")) ;
		//新增车辆明细
		this.addCLMx(cache.getJhxh(),cache.getClmx() , cache.getParam().get("UC")) ;
		
		
	}
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-3-21
	 * @time 下午02:49:33
	 * @param param {CX:车辆型号,CYS:运运商}
	 * @param cache 数据缓存中心
	 * @description  混装时，回滚车辆资源
	 */
	public void syncRoolBackHZCelZY( String fysj,WrapCacheData cache) throws ServiceException
	{
		
		for (Map.Entry<String, HashMap<String, List<YaoCJhMx>>> hz : cache
				.getYaoHz().entrySet()) {
			for (Map.Entry<String, List<YaoCJhMx>> o : hz.getValue().entrySet()) {
				// 得到此时间点的所有车辆型号
				List<YaoCJhMx> yaos = o.getValue();
				for (YaoCJhMx mx : yaos) {
					// GYSDM:CX
					String key = mx.getCllx();

					// 1.同步回滚车辆申报资源
					if (cache.getMapCLBySB().containsKey(key)) {

						// 得到此车辆型号的资源
						WrapCelL zy = cache.getMapCLBySB().get(key);
						// 如果车辆资源没有消耗完
						if (zy.getSysl() != 0) {
							// 将其使用数量+1
							zy.setSysl(zy.getSysl() - 1);
						}
					}

					// 2.同步回滚用户车辆资源
					if (cache.getMapCLByUC().containsKey(key)) {
						// 得到此车辆型号的资源
						WrapCelL zy = cache.getMapCLByUC().get(key);
						// 如果此运输商下的此车型的最大数量大于使用数量,则使用数量加1
						if (zy.getSysl() != 0) {
							// 将其使用数量+1
							zy.setSysl(zy.getSysl() - 1);
						}
						// 否则用户中心下的车辆资源已用完
					}

				}
			}
		}
	}
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 上午11:52:57
	 * @param yaoCMx 要车明细
	 * @param params key:TOMORROW
	 * @description  将要车明细写入数据库
	 */
	public void writeDB(final List<YaoCJhMx> yaoCMx,WrapCacheData cache) throws ServiceException
	{
		//车辆明细
		Map<String,Integer> tmp = cache.getClmx();
		//车辆名称
		Set<String> clmc = cache.getClmc();
		for(YaoCJhMx yao:yaoCMx)
		{
			//写入要车明细
			writeYAOCMx(yao, cache) ;
			
			//车辆名称
			clmc.add(yao.getClName());
			
			//车辆明细
			if(tmp.containsKey(yao.getCllx()))
			{
				//该车型+1
				tmp.put(yao.getCllx(), tmp.get(yao.getCllx())+1);
			}
			else
			{
				tmp.put(yao.getCllx(), 1);
			}
			
			//车辆数+1
			cache.setCls(cache.getCls()+1) ;
		}
	}
	
	
	public void writeYAOCMx(YaoCJhMx yao,WrapCacheData cache) throws ServiceException{
		//获取主键号
		String key = this.selectYaoCMxPriKey();
		YAOCMx _yao = new YAOCMx();
		//主键
		_yao.setID(key);
		//要车时间
		_yao.setDAOCSJ(yao.getYcsj());
		//发贷时间
		_yao.setFAYSJ(yao.getFysj());
		//车辆型号
		_yao.setJIHCX(yao.getCllx()) ;
		//客户编码
		_yao.setKEHBM(yao.getKhbm()) ;
		//要车计划序号
		_yao.setYAOCJHXH(cache.getJhxh()) ;
		//策略编号
		_yao.setCELBH(yao.getCelbh()) ;
		
		//是否装满
		_yao.setSHIFMZ(yao.getSfzm()) ;
		//设置用户中心
		_yao.setUSERCENTER(cache.getParam().get("UC"));
		//运输路线组
		_yao.setYUNSLX(yao.getLxz().getLzxbh());
		//仓库编号
		_yao.setCANGKBH(yao.getLxz().getCkbh()) ;
		//创建人
		_yao.setCREATOR(this.getEditor());
		//新增要车明细
		this.insertYAOCMx(_yao);
		//新增计划要贷令明细
		this.addJhYaoHL(key, yao) ;
	}
	
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 下午01:36:23
	 * @description  新增计划要贷令
	 */
	public void addJhYaoHL(String key,YaoCJhMx yao) throws ServiceException
	{
		Iterator<String> yhlhs = yao.getYhlbh().iterator() ;
		//迭代新增计划要贷令
		while(yhlhs.hasNext())
		{
			//要贷令编号
			String yhbh = yhlhs.next();
			JhYaoHl jhyao = new JhYaoHl();
			//设置主键
			jhyao.setID(key) ;
			//设置要贷令编号
			jhyao.setYAOHLBH(yhbh);
			//写入数据库
			this.insertJhYaoHl(jhyao);
		}
		
	}
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 下午01:51:09
	 * @param tormorrow
	 * @param clsl
	 * @param clmc
	 * @param uc
	 * @description  新增要车计划总表
	 */
	public void addYaoCJHZb(String tormorrow,int clsl,Set<String> clmc,String uc) throws ServiceException
	{
		//如果没有车辆
		if(clsl==0){
			return;
		}
			
		//车辆名称
		String mc = "";
		Iterator<String> clmcs = clmc.iterator() ;
		/*迭代车辆名称*/
		while(clmcs.hasNext())
		{
			//名称
			String _mc = clmcs.next();
			mc = mc+","+_mc;
		}
		//截掉第一个,
		if(mc.indexOf(',')==0){
			mc = mc.substring(mc.indexOf(',')+1) ;			
		}
		
		YaoCJhZb zb = new YaoCJhZb();
		//要车计划序号
		zb.setYAOCJHXH(tormorrow);
		//设置用户中心
		zb.setUSERCENTER(uc) ;
		//总车次
		zb.setZONGCC(clsl);
		//车辆名称
		zb.setCHEXMC(mc) ;
		zb.setCREATOR(this.getEditor());
		//写入数据库
		this.insertYaoCJhZb(zb) ;
	}
	
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-15
	 * @time 下午01:58:45
	 * @param tormorrow
	 * @param tmp
	 * @param uc 用户中心
	 * @description  新增要车明细
	 */
	public void addCLMx(String tormorrow,Map<String,Integer> tmp,String uc) throws ServiceException
	{
		Iterator<String> cllxs = tmp.keySet().iterator();
		while(cllxs.hasNext())
		{
			//车辆类型
			String lx = cllxs.next();
			int sl = tmp.get(lx) ;
			//生成主键号
			String key = this.selectCLMxPriKey();
			ClMx cl = new ClMx();
			//设置车型
			cl.setCHEX(lx) ;
			//主键
			cl.setID(key) ;
			//总车次
			cl.setZONGCC(sl) ;
			//设置用户中心
			cl.setUSERCENTER(uc);
			//要车计划序号
			cl.setYAOCJHXH(tormorrow);
			//写入数据库
			this.insertClMx(cl) ;
			
		}
	}
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-24
	 * @time 下午04:46:10
	 * @param linJBh 零件编号
	 * @param linJSl 零件数量
	 * @param mx 要车计划明细
	 * @description 新增零件  
	 */
	public void setLinJ(String linJBh,Integer linJSl,YaoCJhMx mx) throws ServiceException
	{
		//此车的零件集合
		Map<String, Integer> linj = mx.getLinj();
		//判断此车是否存在此零件
		if(linj.containsKey(linJBh))
		{
			//如是存在,则相加
			linj.put(linJBh, linj.get(linJBh)+linJSl.intValue());
		}
		else
		{
			//不存在,则创建
			linj.put(linJBh, linJSl.intValue());
		}
		
	}
	
	/**
	 * @author 王冲
	 * @email jonw@sina.com
	 * @date 2012-2-24
	 * @time 下午04:46:10
	 * @param baoZBh 包装编号 
	 * @param baoSl 包装数量
	 * @param mx 要车计划明细
	 * @description 新增零件  
	 */
	public void setBaoZs(String baoZBh,Integer baoSl,YaoCJhMx mx) throws ServiceException
	{
		//此车的零件集合
		Map<String, Integer> linj = mx.getBzbh();
		//判断此车是否存在此零件
		if(linj.containsKey(baoZBh))
		{
			//如是存在,则相加
			linj.put(baoZBh, linj.get(baoZBh)+baoSl.intValue());
		}
		else
		{
			//不存在,则创建
			linj.put(baoZBh, baoSl.intValue());
		}
		
	}
	
	/* (non-Javadoc)
	 * @see com.athena.fj.module.interfaces.IYaoHLJhService#createYaoHLJhMx()
	 */
	@Override
	@Transactional
	public void createYaoHLJhMx()  throws ServiceException{
		
		try{
			logger.info("分配要车计划，执行要车计算开始." );
			userOperLog.addCorrect(CommonUtil.MODULE_FJ, "分配要车计划", "分配要车计划，执行要车计算开始.");
			if(!(this.getEditor()!=null && this.getEditor().length()>0)){
				this.setEditor(inter);
			}
			//更新仓库，子仓库编号
			//此方法修改为由接口2030，2560接口实现，此处取消调用
//			fJScheduleService.scheduleRun("");
			//更新要货令锁定状态
			this.updateYHLSUODPZ();
			/*当前日期*/
			String curDate = DateUtil.getCurrentDate();
	/*		curDate = curDate+" 23:59:59";
			//计算24小时有多少分钟
			int min = 24*60;*/
			/*第二天的时间*/
			String torromow = DateUtil.dateAddDays(curDate, 1)+" "+TIME2 ; 
			/*得到所有用户中心*/
			List<Map<String,String>> ucList = this.selectUc();
			//迭代用户中心
			for(Map<String,String> uc:ucList){
				/*生成要车明细*/
				this.shengCYaoCJHmx(uc.get("USERCENTER"), torromow) ;
			}
			logger.info("分配要车计划，执行要车计算完成." );
			userOperLog.addCorrect(CommonUtil.MODULE_FJ, "分配要车计划", "分配要车计划，执行要车计算完成.");
		}catch (Exception e){
			 userOperLog.addError(CommonUtil.MODULE_FJ, "分配要车计划", "分配要车计划出错，执行要车计算", CommonUtil.getClassMethod(), CommonUtil.replaceBlank(e.getMessage()));
			 logger.error("分配要车计划，执行要车计算出错:" +e.getMessage());
			 throw new ServiceException(e.getMessage()); 
		}
	}

}
