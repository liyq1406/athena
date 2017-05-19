package com.athena.xqjs.module.anxorder.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.xqjs.entity.anxorder.GongysFeneJis;
import com.athena.xqjs.entity.anxorder.Ticxxsj;
import com.athena.xqjs.entity.common.LingjGongys;
import com.athena.xqjs.entity.common.Tidj;
import com.athena.xqjs.entity.common.Wullj;
import com.athena.xqjs.entity.common.Xiehzt;
import com.athena.xqjs.entity.ilorder.Ziykzb;
import com.toft.core3.container.annotation.Component;

/**
 * 按需计算参数内存类
 * @author WL
 *
 */
@Component
public class AnxParam extends BaseService{
	
	/**
	 * 日志打印
	 */
	public static final Logger logger = Logger.getLogger(AnxParam.class);
	
	/**
	* 替代件用户中心,零件编号下的替代件信息集合
	*/
	public static Map<String, List<Tidj>> tidjMap;
	 
	/**
	 * 未来几日剔除休息时间
	 */
	public static Map<String, List<Ticxxsj>> ticxxsjMap;
	
	/**
	 * 卸货站台
	 */
	public static Map<String,Xiehzt> xiehztMap;
	
	/**
	 * 用户中心零件,用于份额计算,记录零件累积
	 */
	public static Map<String,BigDecimal> lingjSlMap;
	
	/**
	 * 用户中心零件供应商,用于份额计算,记录零件供应商累积情况
	 */
	public static Map<String,GongysFeneJis> gongysFeneMap;
	
	/**
	 * 零件供应商MAP
	 */
	public static Map<String,List<LingjGongys>> lingjgysMap;
	
	/**
	 * 物流路径MAP
	 */
	public static Map<String,Wullj> wulljMap;
	
	/**
	 * 资源快照MAP
	 */
	public static Map<String,Ziykzb> ziykzMap;
	
	/**
	 * 销毁
	 */
	public void destory(){
		tidjMap = null;
		ticxxsjMap = null;
		xiehztMap = null;
		lingjSlMap = null;
		gongysFeneMap = null;
		lingjgysMap = null;
		wulljMap = null;
		ziykzMap = null;
		logger.info("按需参数销毁");
	}
	
	 /**
	  * 初始化按需参数
	  */
	public void initParam(Map<String,String> map){
		logger.info("初始化按需参数开始");
		long start = System.currentTimeMillis();
		tidjMap = new HashMap<String, List<Tidj>>();
		ticxxsjMap = new HashMap<String, List<Ticxxsj>>();
		xiehztMap = new HashMap<String, Xiehzt>();
		lingjSlMap = new HashMap<String, BigDecimal>();
		gongysFeneMap = new HashMap<String, GongysFeneJis>();
		lingjgysMap = new HashMap<String, List<LingjGongys>>();
		wulljMap = new HashMap<String, Wullj>();
		ziykzMap = new HashMap<String, Ziykzb>();
		
		//查询替代件信息
		List<Tidj> tidjList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("common.queryTidj",map);
		//组装替代件信息数据map
		for (int i = 0; i < tidjList.size(); i++) {
			Tidj tidj = tidjList.get(i);
			//key=用户中心+零件号
			String key = tidj.getUsercenter()+tidj.getLingjbh();
			//如果已经包含该用户中心,零件号的替代件信息,添加
			if(tidjMap.containsKey(key)){
				tidjMap.get(key).add(tidj);
			}else{//如果没有,新建该用户中心,零件号替代件信息集合
				List<Tidj> tidjs = new ArrayList<Tidj>();
				tidjs.add(tidj);
				tidjMap.put(key,tidjs);
			}
		}
		//释放内存
		tidjList = null;
		
		//查询未来几日剔除休息时间
		List<Ticxxsj> ticxxsjList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("common.queryTicxxsj",map);
		//组装数据
		for (int i = 0; i < ticxxsjList.size(); i++) {
			Ticxxsj ticxxsj = ticxxsjList.get(i);
			//key=用户中心+产线仓库+工作日
			String key = ticxxsj.getUsercenter()+ticxxsj.getChanxck()+ticxxsj.getGongzr();
			//已经存在,添加
			if(ticxxsjMap.containsKey(key)){
				ticxxsjMap.get(key).add(ticxxsj);
			//不存在,新建
			}else{
				List<Ticxxsj> ticxxsjs = new ArrayList<Ticxxsj>();
				ticxxsjs.add(ticxxsj);
				ticxxsjMap.put(key, ticxxsjs);
			}
		}
		//释放内存
		ticxxsjList = null;
		
		//查询卸货站台
		List<Xiehzt> xiehztList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anx.queryXiehztObject",map);
		//组装数据
		for (int i = 0; i < xiehztList.size(); i++) {
			Xiehzt xiehzt = xiehztList.get(i);
			xiehztMap.put(xiehzt.getUsercenter()+xiehzt.getXiehztbh(), xiehzt);
		}
		//释放内存
		xiehztList = null;
		
		/**
		 * 零件供应商集合
		 */
		List<LingjGongys> lingjgysList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anxjis.queryLingjGysList",map);
		for (int i = 0; i < lingjgysList.size(); i++) {
			LingjGongys lingjgys = lingjgysList.get(i);
			String key = lingjgys.getUsercenter() + lingjgys.getLingjbh();
			//已经存在,添加
			if(lingjgysMap.containsKey(key)){
				lingjgysMap.get(key).add(lingjgys);
			//不存在,新建
			}else{
				List<LingjGongys> lingjgyss = new ArrayList<LingjGongys>();
				lingjgyss.add(lingjgys);
				lingjgysMap.put(key, lingjgyss);
			}
		}
		lingjgysList = null;
		
		/**
		 * 物流路径集合
		 */
		List<Wullj> wulljList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("anxjis.queryWulljOnly",map);
		for (int i = 0; i < wulljList.size(); i++) {
			Wullj wullj = wulljList.get(i);
			String key = wullj.getUsercenter() + wullj.getLingjbh() + wullj.getGongysbh() + wullj.getFenpqh();
			wulljMap.put(key, wullj);
		}
		wulljList = null;
		
		logger.info("加载按需计算参数结束,耗时---------------------"+(System.currentTimeMillis() - start));
	}
}
