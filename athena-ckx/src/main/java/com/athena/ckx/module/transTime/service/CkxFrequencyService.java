package com.athena.ckx.module.transTime.service;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.authority.util.AuthorityUtils;
import com.athena.ckx.entity.cangk.Xiehztbz;
import com.athena.ckx.entity.transTime.CkxGongysChengysXiehzt;
import com.athena.ckx.entity.workCalendar.CkxCalendarGroup;
import com.athena.ckx.entity.xuqjs.Gongcy;
import com.athena.ckx.entity.xuqjs.Yicbj;
import com.athena.ckx.util.DBUtil;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.ckx.util.GetPostOnly;
import com.athena.ckx.util.GetYicbj;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.dao.DataAccessException;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 承运商，卸货站台，送货频次
 * @author kong
 */
@Component
public class CkxFrequencyService  extends BaseService<CkxGongysChengysXiehzt>{
	//获取命名空间
	@Override
	protected String getNamespace() {
		return "transTime";
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, Object> select(CkxGongysChengysXiehzt bean,String exportXls){
		Map<String,String> map = AuthorityUtils.getSecurityUser().getPostAndRoleMap();//一个人可对应多个业务角色
		String key = GetPostOnly.getPostOnly(map);		
		if(1 > key.indexOf("POA")&&!"root".equals(key)){			
			if(0 > key.indexOf("WULGYY")){
				bean.setXiehztbzs("('')");
			}else{		
				Xiehztbz xiehztbz = new Xiehztbz();				
				String value = (String) map.get("WULGYY");
				xiehztbz.setWulgyyz(value);
				List<Xiehztbz> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryXiehztbz", xiehztbz);
				String xiehztbzs ="('',";
				for (Xiehztbz xiehztbz2 : list) {
					xiehztbzs +="'"+ xiehztbz2.getXiehztbzh()+"',";
				}
				if(xiehztbzs.endsWith(",")){
					xiehztbzs = xiehztbzs.substring(0,xiehztbzs.length()-1)+")";
				}
				bean.setXiehztbzs(xiehztbzs);
			}
		}
		//0008693 加导出
		Map<String,Object> mapValue = new HashMap<String, Object>();
		if("exportXls" .equals(exportXls)){			
			List list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("transTime.queryCkxGongysChengysXiehzt", bean);
			mapValue.put("totas", list.size());
			mapValue.put("rows", list);
		}else{
			mapValue = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("transTime.queryCkxGongysChengysXiehzt", bean, bean);
		}
		return mapValue;
	}
	/**
	 * 计算送货频次
	 * @param user
	 * @return
	 * @author kong
	 */
	@Transactional
	public String compute(LoginUser user,String usercenter){
		Map<String,String> map=new HashMap<String,String>();
		map.put("creator", user.getUsername());
		map.put("usercenter", usercenter);
		map.put("create_time", DateTimeUtil.getAllCurrTime());
		Map<String,String> mapAuth = AuthorityUtils.getSecurityUser().getPostAndRoleMap();//一个人可对应多个业务角色
		String key = GetPostOnly.getPostOnly(mapAuth);		
		if(1 > key.indexOf("POA")&&!"root".equals(key)){	
			map.put("wulgyyz", mapAuth.get(key));
		}
		 
		addTemp1(map);//将需求计算的毛需求按产线写入临时表1
		addTemp2(map);//将产线毛需求（临时表1）拆分为分配区毛需求（临时表2）
		//循环记录送货频次

		List<CkxGongysChengysXiehzt> list = getFrequency(map);// 获取最新计算结果
		for (CkxGongysChengysXiehzt x : list) {
			x.setCreator(user.getUsername());// 用户中心
			x.setCreate_time(DateTimeUtil.getAllCurrTime());// 创建时间
			x.setEditor(user.getUsername());// 用户名
			x.setEdit_time(DateTimeUtil.getAllCurrTime());// 修改时间
			x.setJisrq(DateTimeUtil.getAllCurrTime());// 计算日期
			if (null == baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX)
					.selectObject("transTime.getCkxGongysChengysXiehzt", x)) {// 判断是增加还是修改
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX)
						.execute("transTime.insertCkxGongysChengysXiehzt", x);
			} else {
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX)
						.execute("transTime.updateFrequency", x);
			}
		}		
		return "计算成功";
	}
	/**
	 * 检测承运商的额定装载率、波动放大系数、卡车标准体积是否存在 0006140
	 * @param m
	 */
	@SuppressWarnings( "unchecked" )
	private void checkChengysNull(Map<String,String> m){
		List<Map<String,String>> list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("transTime.checkChengysNull",m);
		List<Yicbj> listYicbc = new ArrayList<Yicbj>();
		for (Map<String, String> map : list) {
			Xiehztbz  xiehztbz = (Xiehztbz) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getXiehztbzByBh",(String)map.get("XIEHZT"));
			//"用户中心："+map.get("USERCENTER")+" ,卸货站台编组："+map.get("XIEHZT")+" , 承运商"+map.get("GCBH").toString()+",";
			String exceptionMes = GetMessageByKey.getMessage("yhzxxhztbzcys", 
					new String[]{map.get("USERCENTER") == null?null:map.get("USERCENTER").toString(),
							map.get("XIEHZT") == null?null:map.get("XIEHZT").toString(),
							map.get("GCBH") == null?null:map.get("GCBH").toString()})+",";
			if(map.get("BODFDXS")==null || "0".equals(map.get("BODFDXS"))){
				//波动放大系数为空或为零
				exceptionMes += GetMessageByKey.getMessage("bodfdxs")+",";
			}
			if(map.get("KACBZTJ")==null || "0".equals(map.get("KACBZTJ"))){
				//卡车标准体积为空或为零
				exceptionMes += GetMessageByKey.getMessage("kacbztj")+",";
			}
			if(map.get("EDZZL")==null || "0".equals(map.get("EDZZL"))){
				//额定装载率为空或为零
				exceptionMes += GetMessageByKey.getMessage("edzzl")+",";
			}
			//去除结尾的逗号
			if(exceptionMes.endsWith(",")){
				exceptionMes = exceptionMes.substring(0,exceptionMes.length()-1);
			} 
//			String exceptionMes = GetMessageByKey.getMessage("yhzxxhztbzwfzdgzsj", new String[]{map.get("USERCENTER") == null?null:map.get("USERCENTER").toString(),map.get("XIEHZT") == null?null:map.get("XIEHZT").toString()});
			listYicbc.add(new GetYicbj().getYicbj(map.get("USERCENTER").toString(), "200", "CKX_10",null, exceptionMes,xiehztbz.getWulgyyz()));
		}
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("ycbj.insertYcbjCKX", listYicbc);
	}
	/**
	 * 从临时表2中获取数据，并组成对象
	 * @param m
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<CkxGongysChengysXiehzt> getFrequency(Map<String,String> m){
		checkChengysNull(m);
		//从临时表2中获取数据并计算每个卸货站台承运商的送货频次		
		List<Map<String,Object>> list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("transTime.getFrequency",m);

		/*检查临时表2中商是否有数据，如果没有，则说明系统没有成功拆分产线需求，
		    需要去检查 零件消耗点，零件供应商，物流路径中的供应商关系是否存在*/
		if(list==null||list.size()==0){
			//系统未能成功拆分产线需求!请检查对应的 零件消耗点，零件供应商，物流路径中的供应商关系是否存在  
			throw new ServiceException(GetMessageByKey.getMessage("xtwncgcfcxxq"));
		}
		List<CkxGongysChengysXiehzt> xlist=new ArrayList<CkxGongysChengysXiehzt>();
		List<Yicbj> listYicbc = new ArrayList<Yicbj>();
		for (Map<String, Object> map : list) {
			Xiehztbz  xiehztbz = (Xiehztbz) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("ts_ckx.getXiehztbzByBh",(String)map.get("XIEHZT"));
			//检查卸货站台的总工作时间是否存在 
			if(map.get("DAYS")==null){
				//"用户中心："+map.get("USERCENTER")+" ,卸货站台编组："+map.get("XIEHZT")+" ,无法找到对应的卸货站台编组的工作时间"
				String exceptionMes = GetMessageByKey.getMessage("yhzxxhztbzwfzdgzsj", new String[]{map.get("USERCENTER") == null?null:map.get("USERCENTER").toString(),map.get("XIEHZT") == null?null:map.get("XIEHZT").toString()});
				listYicbc.add(new GetYicbj().getYicbj(map.get("USERCENTER").toString(), "200", "CKX_10",null, exceptionMes,xiehztbz.getWulgyyz()));
				continue;
//				throw new ServiceException("系统未能获取卸货站台编组"+map.get("XIEHZT").toString()+"的工作时间!");
			}
			
			if(map.get("CHES")==null||map.get("TIJ")==null){
				//"用户中心："+map.get("USERCENTER")+" ,卸货站台编组："+map.get("XIEHZT")+", 承运商"+map.get("GCBH").toString()+" ： 无法获取UA包装类型!";
				String exceptionMes =  GetMessageByKey.getMessage("yhzxxhztbzcyswfhqualx", 
						new String[]{map.get("USERCENTER") == null?null:map.get("USERCENTER").toString(),
								map.get("XIEHZT") == null?null:map.get("XIEHZT").toString(),
								map.get("GCBH") == null?null:map.get("GCBH").toString()});
				listYicbc.add(new GetYicbj().getYicbj(map.get("USERCENTER").toString(), "200", "CKX_10",null, exceptionMes,xiehztbz.getWulgyyz()));
				continue;
//				throw new ServiceException("无法获取承运商"+map.get("GCBH").toString()+"UC包装类型!");
			}
			if(map.get("CHES").toString().trim().length()>3){
				//"用户中心："+map.get("USERCENTER")+" ,卸货站台编组："+map.get("XIEHZT")+" , 承运商"+map.get("GCBH").toString()+"车数过大!
				String exceptionMes = GetMessageByKey.getMessage("yhztxhztbzcyscsgd", 
						new String[]{map.get("USERCENTER") == null?null:map.get("USERCENTER").toString(),
								map.get("XIEHZT") == null?null:map.get("XIEHZT").toString(),
								map.get("GCBH") == null?null:map.get("GCBH").toString()});
				listYicbc.add(new GetYicbj().getYicbj(map.get("USERCENTER").toString(), "200", "CKX_10",null, exceptionMes,xiehztbz.getWulgyyz()));
				continue;
//				throw new ServiceException("承运商"+map.get("GCBH").toString()+"车数过大!");
			}
			
			
//			int i;
//			try {
//				//将计算出来的频次,车数截取，并四舍五入
//				//频次
//				String s=map.get("PC").toString();
//				int index=s.indexOf(".");
//				if(index!=-1){//如果带小数
//					i = (int) Math.round(Double.valueOf(s.substring(0, index+2)));//截取后两位小数并四舍五入
//				}else{
//					i=Integer.valueOf(s);
//				}
//			
//				
//			} catch (NumberFormatException e) {
//				throw new ServiceException("数据转换错误");
//			}
			
			CkxGongysChengysXiehzt x=new CkxGongysChengysXiehzt();
			x.setUsercenter(map.get("USERCENTER").toString());//用户中心
			x.setXiehztbh(map.get("XIEHZT").toString());//卸货站台
			x.setGcbh(map.get("GCBH").toString());//承运商
			x.setGongzsj(Integer.valueOf(map.get("DAYS").toString()));
			x.setShijpc(null);//标准送货频次
			x.setShengxpc(null);
			x.setJispc(Integer.valueOf(map.get("PC").toString()));//计算送货频次
//			x.setShengxpc(Integer.valueOf(map.get("PC").toString()));//生效送货频次
			x.setChes(Integer.valueOf(map.get("CHES").toString().trim()));//车数
			
			xlist.add(x);
		}
		if(list.size() == listYicbc.size()){
			//"拆分产线毛需求失败，请检查是否存在对应拆分数据。"
			throw new ServiceException(GetMessageByKey.getMessage("cfcxmxqsbqjcsfczdycfsj"));
		}
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("ycbj.insertYcbjCKX", listYicbc);
		return xlist;
	}
	/**
	 * 修改送货频次
	 * @param editList  需要修改的对象集合
	 * @return
	 * @author kong
	 */
	@Transactional
	public String edit(List<CkxGongysChengysXiehzt> editList,LoginUser user){
		for (CkxGongysChengysXiehzt xie : editList) {
			//验证最大送货频次
			if(xie.getShengxpc()>xie.getChes()){
				//"承运商["+xie.getGcbh()+"]生效送货频次不能大于其车数（"+xie.getChes()+"次）"
				throw new ServiceException(GetMessageByKey.getMessage("cyssxshpcbndyqcs",new String [] {xie.getGcbh(),xie.getChes().toString()}));
			}
			//验证最小送货频次
//			if(xie.getShengxshpc()<xie.getSonghzxpc()){
//				throw new ServiceException("承运商["+xie.getGcbh()+"]生效送货频次不能小于其最小送货频次（"+xie.getSonghzxpc()+"次）");
//			}
			//设置标准送货频次为生效送货频次
			xie.setShijpc(xie.getShengxpc());//把生效送货频次覆盖标准送货频次
			xie.setEditor(user.getUsername());//修改人
			xie.setEdit_time(DateTimeUtil.getAllCurrTime());//修改时间
			super.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("transTime.updateCkxGongysChengysXiehzt", xie);
		}
		//保存成功
		return GetMessageByKey.getMessage("savesuccess");
	}
	/**
	 * 将需求计算的毛需求按产线写入临时表1
	 * @param map
	 * @author kong
	 */
	
	@SuppressWarnings("unchecked")
	private void addTemp1(Map<String,String> map){
		//判断获取需求计算产线毛需求数据
		List<Map<String,Object>> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("transTime.queryTempMaoxqByXqjs",map);
		if(0 == list.size()){
			//未能获取指定需求计算的产线毛需求，或数据不在计算周期内
			throw new ServiceException(GetMessageByKey.getMessage("wnhqzdxqjsdcxmxqhsjbzzqn"));
		}
		//从需求计算获取产线毛需求插入临时表1
		try {			
			//删除本用户中心下的数据
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("transTime.deleteTempMaoxq",map);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("transTime.insertTempMaoxq",map);
		} catch (DataAccessException e) {
			//毛需求表：
			throw new ServiceException(GetMessageByKey.getMessage("maoxqb")+e.getMessage());
		}
	}
	/**
	 * 将产线毛需求（临时表1）拆分为分配区毛需求（临时表2）
	 * 所关联的表有：零件消耗点，零件供应商，物流路径总图
	 * @param map
	 * @author kong
	 */
	@SuppressWarnings("unchecked")
	private void addTemp2(Map<String,String> map){
//		1.判断是否归集到对应的零件消耗点
		List<Map<String,Object>> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("transTime.queryTempMaoxqLjxhdCheck",map);
		List<Yicbj> listYicbc = new ArrayList<Yicbj>();
		for (Map<String, Object> map2 : list) {
			if(null == map2.get("XIAOHDBH")){
				// "用户中心："+map2.get("USERCENTER")+",零件："+map2.get("LINGJBH")+" ,产线："+map2.get("CHANX")+"无法找到对应的零件消耗点";
				String exceptionMes = GetMessageByKey.getMessage("yhzxljcxwfzdljxhd"
						,new  String[]{map2.get("USERCENTER")==null?null:map2.get("USERCENTER").toString(),
								map2.get("LINGJBH")==null?null:map2.get("LINGJBH").toString(),
								map2.get("CHANX")==null?null:map2.get("CHANX").toString()});
				listYicbc.add(new GetYicbj().getYicbj(map2.get("USERCENTER").toString(), "200", "CKX_10",map2.get("LINGJBH").toString(), exceptionMes,null));
				continue;
			}
		}
		if(list.size() == listYicbc.size()){
			//"无法计算，所有零件都无法找到对应的零件消耗点"
			throw new ServiceException(GetMessageByKey.getMessage("wfjssyljdwfzdljxhd"));
		}
		if(0<listYicbc.size()){
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("ycbj.insertYcbjCKX", listYicbc);
			listYicbc.clear();
		}
		List<Map<String,Object>> listWullj = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("transTime.queryTempMaoxqcfCheckWullj",map);
		
		for (Map<String, Object> map3 : listWullj) {
			if(null == map3.get("GCBH")|| null == map3.get("XIEHZT")){
				//"用户中心："+map3.get("USERCENTER")+",零件："+map3.get("LINGJBH")+" ,分配区："+map3.get("FENPQBH")+" , 无法找到对应的承运商或卸货站台编组，请检查物流路径";
				String exceptionMes = GetMessageByKey.getMessage("yhzxljfpqwfzdwllj",
						new String[]{map3.get("USERCENTER")==null?null:map3.get("USERCENTER").toString(),
								map3.get("LINGJBH")==null?null:map3.get("LINGJBH").toString(),
										map3.get("FENPQBH")==null?null:map3.get("FENPQBH").toString()});
				listYicbc.add(new GetYicbj().getYicbj(map3.get("USERCENTER").toString(), "200", "CKX_10",map3.get("LINGJBH").toString(), exceptionMes,null));
				continue;
			}
			if(null == map3.get("TIJ")){
				//"用户中心："+map3.get("USERCENTER")+",零件："+map3.get("LINGJBH")+" ,供应商："+map3.get("GONGYSBH")+" , 无法找到对应零件供应商或UA类型为空";
				String exceptionMes = GetMessageByKey.getMessage("yhzxljgyswfzdljgyshualxwk",
						new String[]{map3.get("USERCENTER")==null?null:map3.get("USERCENTER").toString(),
								map3.get("LINGJBH")==null?null:map3.get("LINGJBH").toString(),
								map3.get("GONGYSBH")==null?null:map3.get("GONGYSBH").toString()});
				listYicbc.add(new GetYicbj().getYicbj(map3.get("USERCENTER").toString(), "200", "CKX_10",map3.get("LINGJBH").toString(), exceptionMes,null));
				continue;
			}
		}
		if(listWullj.size() == listYicbc.size()){
			//"无法计算，所有零件都无法找到对应的物流路径或没有找到对应的UA包装类型"
			throw new ServiceException(GetMessageByKey.getMessage("wfjssydwfzdwlljhmyuabzlx"));
		}
		if(0<listYicbc.size()){
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).executeBatch("ycbj.insertYcbjCKX", listYicbc);
			listYicbc.clear();
		}
		//将产线毛需求（临时表1）拆分为分配区毛需求（临时表2）
		try {
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("transTime.deleteTempMaoxqcf",map);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("transTime.insertTempMaoxqcf",map);
		} catch (DataAccessException e) {
			//"毛需求拆分："
			throw new ServiceException(GetMessageByKey.getMessage("maoxqcf")+e.getMessage());
		}
	}
	/**
	 * 得到数据权限内的卸货站台编组
	 * ('','L01X','L02X')
	 */
	@SuppressWarnings("unchecked")
	public String getXiehztbzhs(String usercenter){
		Map<String,String> map = AuthorityUtils.getSecurityUser().getPostAndRoleMap();//一个人可对应多个业务角色
		String key = GetPostOnly.getPostOnly(map);
		String xiehztbzs = null;
		if(1 > key.indexOf("POA")&&!"root".equals(key)){			
			
			if(0 > key.indexOf("WULGYY")){
				xiehztbzs = "('')";
			}else{		
				Xiehztbz xiehztbz = new Xiehztbz();				
				String value = (String) map.get("WULGYY");
				xiehztbz.setWulgyyz(value);
				xiehztbz.setUsercenter(usercenter);
				xiehztbz.setBiaos("1");
				List<Xiehztbz> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryXiehztbz", xiehztbz);
				xiehztbzs ="('',";
				for (Xiehztbz xiehztbz2 : list) {
					xiehztbzs += "'"+xiehztbz2.getXiehztbzh()+"',";
				}
				if(xiehztbzs.endsWith(",")){
					xiehztbzs = xiehztbzs.substring(0,xiehztbzs.length()-1)+")";
				}				
			}
			
		}
		return xiehztbzs;
	}
	
	public String insert(CkxGongysChengysXiehzt bean,LoginUser user){
		if(1440 <= bean.getGongzsj()){
			throw new ServiceException(GetMessageByKey.getMessage("gongzsjccyxfwz"));
		}
		String xiehztbzs = getXiehztbzhs(bean.getUsercenter()); 
		if(xiehztbzs != null && xiehztbzs.indexOf(bean.getXiehztbh()) < 0){
			throw new ServiceException("对不起，您没有卸货站台编组："+bean.getXiehztbh()+"的数据权限");
		}
		//验证供应商编号是否存在
		Gongcy gongcy = new Gongcy();
		gongcy.setUsercenter(bean.getUsercenter());
		gongcy.setGcbh(bean.getGcbh());
		gongcy.setBiaos("1");
		//供应商中不存在供应商编号："+gongcy.getGcbh()+"的数据"
		DBUtil.checkCount(baseDao, "ts_ckx.getCountGongys", gongcy,GetMessageByKey.getMessage("gongyszbczsj",new String[]{gongcy.getUsercenter(),gongcy.getGcbh()}));
		//验证卸货站台编组是否存在工作时间关联中
		CkxCalendarGroup group = new CkxCalendarGroup();
		group.setUsercenter(bean.getUsercenter());
		group.setAppobj(bean.getXiehztbh());
		group.setBiaos("1");
		//工作时间关联中不存在卸货站台编组："+group.getAppobj()+"的数据
		DBUtil.checkCount(baseDao, "workCalendar.getCountByAppobj", group,GetMessageByKey.getMessage("gongzsjglzbczsj",new String[]{group.getUsercenter(),group.getAppobj()}));
		//0007426检测同一个卸货站台编组工作时间的一致性
		checkGongzsj(bean.getUsercenter(),bean.getXiehztbh(),bean.getGongzsj());
		bean.setCreator(user.getUsername());
		bean.setCreate_time(DateTimeUtil.getAllCurrTime());//设置创建时间
		bean.setEditor(user.getUsercenter());
		bean.setEdit_time(DateTimeUtil.getAllCurrTime());//设置修改时间
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("transTime.insertCkxGongysChengysXiehzt",bean);
		return GetMessageByKey.getMessage("addSuccess");
	}
	/**
	 * 检查卸货站台编组的工作时间的一致性
	 * @param usercenter
	 * @param xiehztbz
	 */
	@SuppressWarnings("unchecked")
	private void checkGongzsj(String usercenter,String xiehztbz,Integer gzsj){
		CkxGongysChengysXiehzt bean = new CkxGongysChengysXiehzt();
		bean.setUsercenter(usercenter);
		bean.setXiehztbh(xiehztbz);
		//0007426 有可能为新增的卸货站台
		List<CkxGongysChengysXiehzt>  list = this.list(bean);
		if(list.size() >0 ){
			bean = list.get(0);
			if(bean.getGongzsj().intValue() != gzsj.intValue()){
				throw new ServiceException("同一个卸货站台编组"+xiehztbz+"工作时间必须一致");
			}
		}
	}
	/**
	 * 物理删除
	 * @param bean
	 * @return
	 */
	public String delete(CkxGongysChengysXiehzt bean){
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("transTime.deleteCkxGongysChengysXiehztByxiehztbz",bean);
		return GetMessageByKey.getMessage("deleteSuccess");
	}
	/**
	 * 查询卸货站台或
	 * @param bean
	 * @return
	 */
	
	@SuppressWarnings("unchecked")
	public List<CkxGongysChengysXiehzt> listGcbhORXiehzt(CkxGongysChengysXiehzt bean){ 
		if(null == bean.getCreator()){
			return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("transTime.listXiehzt",bean);
		}
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("transTime.listGcbh",bean);
	}
}