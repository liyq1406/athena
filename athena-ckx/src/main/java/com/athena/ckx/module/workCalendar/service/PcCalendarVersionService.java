/**
 * 代码声明
 */
package com.athena.ckx.module.workCalendar.service;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.athena.authority.entity.LoginUser;
import com.athena.ckx.entity.workCalendar.CkxCalendarCenter;
import com.athena.ckx.entity.workCalendar.CkxCalendarGroup;
import com.athena.ckx.entity.workCalendar.CkxCalendarVersion;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.BaseService;
import com.athena.component.service.Message;
import com.athena.db.ConstantDbCode;
import com.athena.util.cache.CacheManager;
import com.athena.util.cache.CacheValue;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.dao.DataAccessException;
import com.toft.core3.transaction.annotation.Transactional;
/**
 * 日历版次
 * @author kong
 */
@Component
public class PcCalendarVersionService extends BaseService<CkxCalendarVersion>{
	@Inject
	private  CacheManager cacheManager;//缓存
	
	private  Logger logger =Logger.getLogger(PcCalendarVersionService.class);
	
	// 获取命名空间
	@Override
	protected String getNamespace() {
		return "workCalendarPC";
	}
	/**
	 * 修改日历版次
	 * @param editList
	 * @return
	 */
	@Transactional
	public String edit(ArrayList<CkxCalendarVersion> editList,LoginUser user){
		for (CkxCalendarVersion ckxCalendarVersion : editList) {//循环获取日历版次
			ckxCalendarVersion.setEditor(user.getUsername());//设置修改人
			ckxCalendarVersion.setEditTime(DateTimeUtil.getAllCurrTime());//设置修改时间
			super.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("workCalendarPC.updateCkxCalendarVersion", ckxCalendarVersion);//更新版次
		}
		return new Message("updateSuccess","i18n.ckx.workCalendar.i18n_workCalendar").getMessage();
	}
	/**
	 * 添加版次
	 * @param year 年份,多年份用逗号分隔
	 * @param center 用户中心
	 * @param userCode 用户指定码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public String addVersion(String years,String center,String userCode,LoginUser user){
		//年份是以逗号组成的字符串，所以取值时需要分割
		String[] strs= years.split(",");
		
		//根据年份查询出中心日历
		for (String year : strs) {
			CkxCalendarCenter cond=  new CkxCalendarCenter();
			cond.setUsercenter(center);//设置用户中心
			cond.setRiq(year);//日期
			//根据年份，用户中心获取中心日历
			List<CkxCalendarCenter> listtemp= baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("workCalendar.queryCkxCalendarCenterForVersion",cond);
			
			if(listtemp==null||listtemp.size()==0){
				//未找到年份为"+year+"的中心日历
				return new Message("notFindYearObj","i18n.ckx.workCalendar.i18n_workCalendar").getMessage(new String[]{year});
			}
		}
		//获取最大版次序号和当前月份
		Object o=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("workCalendarPC.getVersionMaxNo");
		String no="01";
		if(o!=null&&!"".equals(o.toString())){
			no=o.toString();			
			no=String.valueOf(Integer.valueOf(no)+1);
			no=no.length()==1?"0"+no:no;			
		}
		//get month
		String string=DateTimeUtil.getCurrYearMonth();
		String s=string.substring(string.indexOf('.')+1);
		String month=Integer.toHexString(Integer.parseInt(s));
		//生成版次号 :1年型+1月+2序号+4用户指定
		String currentYear = DateTimeUtil.getCurrDate().substring(0,4);
		String banc=getNianxm(currentYear)+month.toUpperCase()+no+userCode.toUpperCase();
		//添加版次
		for (String year : strs) {
			Map<String,String> map=new HashMap<String,String>();
			map.put("creator", user.getUsername());
			map.put("createTime", DateTimeUtil.getAllCurrTime());
			map.put("year",year);
			map.put("banc", banc);
			map.put("usercenter", center);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("workCalendarPC.addCkxCalendarVersion", map);
		}
		
		return new Message("saveSuccess","i18n.ckx.workCalendar.i18n_workCalendar").getMessage();
	}
	/**
	 * 复制版次
	 * @param versionCode 原版次号
	 * @param userCode 新用户指定码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public String copyVersion(String versionCode,String userCode,LoginUser user){
		CkxCalendarVersion varsion=new CkxCalendarVersion();
		varsion.setBanc(versionCode.toUpperCase());
		List<CkxCalendarVersion> list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("workCalendarPC.queryCkxCalendarVersion",varsion);
		if(list==null||list.size()==0){
			//未找到被复制的版次versionCode
			return new Message("notFindCopyObj","i18n.ckx.workCalendar.i18n_workCalendar").getMessage(new String[]{versionCode});
		}
		Object o=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("workCalendarPC.getVersionMaxNo");
		String no="01";
		if(o!=null){
			no=o.toString();
			no=String.valueOf(Integer.valueOf(no)+1);
			no=no.length()==1?"0"+no:no;
		}
		//get month
		String string=DateTimeUtil.getCurrYearMonth();
		String s=string.substring(string.indexOf('.')+1);//分割月份
		String month=Integer.toHexString(Integer.parseInt(s));
		String currentYear = DateTimeUtil.getCurrDate().substring(0,4);
		String banc=getNianxm(currentYear)+month.toUpperCase()+no+userCode.toUpperCase();
		//复制版次
		Map<String,String> map=new HashMap<String,String>();
		map.put("creator", user.getUsername());
		map.put("createTime", DateTimeUtil.getAllCurrTime());
		map.put("versionCode",versionCode);
		map.put("banc", banc);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("workCalendarPC.copyCkxCalendarVersion", map);

		return new Message("copySuccess","i18n.ckx.workCalendar.i18n_workCalendar").getMessage();
	}
	/**
	 * 添加日历 ,根据版次带出用户中心
	 * @param versionCode 版次
	 * @param year 添加的年份
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public String addDay(String versionCode,String years,LoginUser user){
		//根据版次查询用户中心
		CkxCalendarVersion varsion=new CkxCalendarVersion();
		varsion.setBanc(versionCode.toUpperCase());
		List<CkxCalendarVersion> list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("workCalendarPC.queryCkxCalendarVersion",varsion);
		if(list==null||list.size()==0){//如果版次号不存在
			return new Message("notFindVersionObj","i18n.ckx.workCalendar.i18n_workCalendar").getMessage(new String[]{versionCode});
		}
		String[] strs= years.split(",");
		for (String year : strs) {
			CkxCalendarCenter cond=  new CkxCalendarCenter();
			cond.setUsercenter(list.get(0).getUsercenter());//从版次中带出用户中心
			cond.setRiq(year);
			//根据年份，用户中心获取中心日历
			List<CkxCalendarCenter> listtemp= baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("workCalendar.queryCkxCalendarCenterForVersion",cond);
			if(listtemp==null||listtemp.size()==0){
				//未找到年份为"+year+"的中心日历
				return new Message("notFindYearObj","i18n.ckx.workCalendar.i18n_workCalendar").getMessage(new String[]{year});
			}
		}
		try {
			for (String year : strs) {
				Map<String,String> map=new HashMap<String,String>();
				map.put("creator", user.getUsername());
				map.put("createTime", DateTimeUtil.getAllCurrTime());
				map.put("year",year);
				map.put("banc", versionCode.toUpperCase());//版次号不变，使用原来的版次
				map.put("usercenter", list.get(0).getUsercenter());//从版次中带出用户中心
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("workCalendarPC.addCkxCalendarVersion", map);
			}
		} catch (DataAccessException e) {
			// 年份重复
			return new Message("yearRepeating","i18n.ckx.workCalendar.i18n_workCalendar").getMessage();
		}
		
		//添加成功
		return new Message("addSuccess","i18n.ckx.workCalendar.i18n_workCalendar").getMessage();
	}
	/**
	 * 逻辑删除
	 * @param versionCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String delVersion(String versionCode,LoginUser user){
		CkxCalendarVersion varsion=new CkxCalendarVersion();
		varsion.setBanc(versionCode.toUpperCase());//版次大写
		List<CkxCalendarVersion> list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("workCalendarPC.queryCkxCalendarVersion",varsion);
		if(list==null||list.size()==0){
			return new Message("notFindVersionObj","i18n.ckx.workCalendar.i18n_workCalendar").getMessage(new String[]{versionCode});
		}
		//检查此版次号是否与仓库产线有关联
		CkxCalendarGroup group=new CkxCalendarGroup();
		group.setRilbc(versionCode.toUpperCase());
//		List<CkxCalendarGroup> glist=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("workCalendar.queryCkxCalendarGroup",group);
//		if(glist!=null&&glist.size()!=0){
			
		int count = (Integer) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("workCalendarPC.selectCountByBanc", group);
		if(count!=0){
			//{0}版次的日历正在被使用，无法删除
			return new Message("objVersionDelError","i18n.ckx.workCalendar.i18n_workCalendar").getMessage(new String[]{versionCode});
		}
		CkxCalendarVersion version=new CkxCalendarVersion();
		version.setBanc(versionCode.toUpperCase());//版次
		version.setEditor(user.getUsername());//修改人
		version.setEditTime(DateTimeUtil.getAllCurrTime());
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("workCalendarPC.deleteCkxCalendarVersionLogic", version);
		//timingTask();
		return new Message("deleteSuccess","i18n.ckx.workCalendar.i18n_workCalendar").getMessage();
	}
	/**
	 * 获取版次号集合
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CkxCalendarVersion> getVersionNo(CkxCalendarVersion bean){
		List<CkxCalendarVersion> list=new ArrayList<CkxCalendarVersion>();
		List<String> verlist = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("workCalendarPC.getSelectVersionCode",bean);
		for (String string : verlist) {
			CkxCalendarVersion v=new CkxCalendarVersion();
			v.setBanc(string);
			list.add(v);
		}
		return list;
	}

	/**
	 * 定时任务 定时检查版次号信息，如果没有与别的表关联则删除 定时检测有无两年前的版本，如果有，则删除
	 */
	public void clearVersion() {
		// /*-- 将所有版次号与仓库产线的版次进行对比，如果当前版次没有与仓库产线关联，则物理删除当前版次下的所有日历 --*/
		// // 获取版次号集合
		// List<String> verlist =
		// baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("workCalendarPC.getVersionCode");
		// List<CkxCalendarVersion> ckxCalendarVersionlist = new
		// ArrayList<CkxCalendarVersion>();
		// CkxCalendarGroup group = new CkxCalendarGroup();
		// for (String versionCode : verlist) {
		// if("NAO1KD01".equals(versionCode)||"NA01AX01".equals(versionCode)||"NA01LX01 ".equals(versionCode)){
		// continue;
		// }
		// //将版次号带入查询关联
		// group.setRilbc(versionCode);
		// int count = (Integer)
		// baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("workCalendarPC.selectCountByBanc",
		// group);
		// if(count == 0){
		// CkxCalendarVersion bean = new CkxCalendarVersion();
		// bean.setBanc(versionCode);
		// ckxCalendarVersionlist.add(bean);
		// }
		// }
		// CkxCalendarVersion bean = new CkxCalendarVersion();
		// bean.setRiq(Calendar.getInstance().get(Calendar.YEAR)+"");
		// ckxCalendarVersionlist.add(bean);
		// baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).executeBatch("workCalendarPC.deleteCkxCalendarVersion",
		// ckxCalendarVersionlist);
		// /* -- 删除相隔一年以前的所有日期，如：今年是2011年，如果存在2009年的数据，则删除 -- */
		// // Integer year = Calendar.getInstance().get(Calendar.YEAR);
		// //
		// baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("workCalendarPC.deleteCkxCalendarVersion",
		// year);

		/***************** by liuhuixi 2014-11-18 ***********************/
		/*-- 将所有版次号与仓库产线的版次进行对比，如果当前版次非（NA01KD01，NA01AX01，NA01LX01）创建时间超出15天且没有与仓库产线关联，则物理删除当前版次下的所有日历 --*/																									// 2014-11-18
		CkxCalendarVersion bean = new CkxCalendarVersion();
		bean.setStrNA01Banc("'NA01KD01','NA01AX01','NA01LX01'");
		bean.setRiq(Calendar.getInstance().get(Calendar.YEAR) + "");
		// 批量删除日历版次创建日期超出15天且没有与仓库产线关联
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute(
				"workCalendarPC.deleteCkxCalendarVersionByRiAndBanc", bean);
		logger.info("4230接口批量删除日历版次成功");
		// 批量删除工作时间编组创建日期超出15天且没有与仓库产线关联
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute(
				"workCalendarPC.deleteCkxCalendarTeamByDate");
		logger.info("4230接口批量删除工作时间编组成功");
	}
	
	//修改年周序方法
	@Transactional
	public String eidtNianzx(CkxCalendarVersion bean,LoginUser user){	
		//获取将要修改前具体数据
		CkxCalendarVersion version = (CkxCalendarVersion) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("workCalendarPC.getCkxCalendarVersion", bean);
		//获取将要修改后的具体数据
		CkxCalendarVersion version1 = (CkxCalendarVersion) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("workCalendarPC.getCkxCalendarVersionBynianzx", bean);
		//如果是还原年周序，则原年周序引用原来的，如果不是，则需要修改地原年周序改为将要修改地年周序
		if(!bean.getNianzx().equals(version.getNianzxTemp())){			
			if(version1 == null){
				version1 = version;
				version1.setNianzq(version1.getNianzq());
				version1.setNianzx(version1.getNianzx()+1);
				version1.setNianzx(version1.getZhoux()+1);
//				throw new ServiceException(GetMessageByKey.getMessage("wufzdsj",new String[]{bean.getBanc(),bean.getUsercenter(),bean.getNianzx()}));   //"无法找到版次："+bean.getBanc()+",用户中心："+bean.getUsercenter()+",年周序："+bean.getNianzx()+"的数据"
			}
			version.setNianzxTemp(version1.getNianzx());
			version.setNianzqTemp(version1.getNianzq());
			version.setZhouxTemp(version1.getZhoux());	
		}		
		String symbol = "";
		int spacing = 0;//修改后的年周序与修改前的间距（跨年为正负1）
		if(Integer.parseInt(bean.getNianzx())>Integer.parseInt(bean.getYnianzx())){
			symbol = " >= ";//如果修改后的年周序大于数据创建时的年周序 ，则取向下的数据   做修改	
			spacing = getSpacing(version,bean.getNianzx(),1);		//向下为拆	
			if(version1 != null){
				version.setNianzq(version1.getNianzq());     //将需要拆分的年周期更新年周序
			}			
		}else if(Integer.parseInt(bean.getNianzx()) == Integer.parseInt(bean.getYnianzx())){
			return GetMessageByKey.getMessage("weixgnzx");  //未修改年周序
		}else{
			symbol = " <= ";//否则，取向上的数据      做修改	
			spacing = getSpacing(version,bean.getNianzx(),-1);		//向上为合	
		}
		version.setCreator(spacing+"");
		version.setEditor(user.getUsername());//修改人
		version.setEditTime(DateTimeUtil.getAllCurrTime());	
		//更新年周序
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("workCalendarPC.updateNianzxUD", version);
		//更新原年周期需要更新的周序的数据
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("workCalendarPC.updateZhouqZhouxUD", version);
		
		version.setCreator(symbol+"'"+version.getRiq()+"'");
		//更新原年周序需要更新的年周期、周序、年周序的数据
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("workCalendarPC.updateCkxCalendarVersionZhouxUD", version);
		return GetMessageByKey.getMessage("nianzxgxcg");//年周序更新成功
	}
	/**
	 * 获取后面的周序需要修改地间距
	 * @param version
	 * @param xnianzx
	 * @param spacing
	 * @return
	 */
	public int getSpacing(CkxCalendarVersion version,String xnianzx,int spacing){
		int param = 0;
		String riq = "";
		if(0 > spacing){
			//取得该年周序的结束日
			 riq = (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("workCalendarPC.getMaxRiqByNianzx", version);			
		}else{
			//取得该年周序的开始日
			 riq = (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("workCalendarPC.getMinRiqByNianzx", version);	
		}
		if(version.getNianzx().substring(0,4).equals(xnianzx.substring(0,4))){
			if(Math.abs(Integer.parseInt(version.getNianzx())-Integer.parseInt(xnianzx))>1){
				throw new ServiceException(GetMessageByKey.getMessage("feifczjjtd"));
			}
		}
		if(riq.equals(version.getRiq()) ){			
			if(!xnianzx.equals(version.getNianzxTemp())&& 0 < spacing && version.getNianzx().substring(0,4).equals(xnianzx.substring(0,4))){
				throw new ServiceException(GetMessageByKey.getMessage("feifcz"));
			}
			param = spacing;
		}else if(0 < spacing){
			param = spacing;
		}
		return  param;
	}
	/**
	 * 根据当前年份获取年型码
	 * @param currentYear
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getNianxm(String currentYear){
		Object obj=cacheManager.getCacheInfo("queryNianxm").getCacheValue();
		List<CacheValue> l=(List<CacheValue>)obj;
		Map<String,String> map=new HashMap<String,String>();
		for (CacheValue value : l) {
			map.put(value.getValue(), value.getKey());
		}
		return map.get(currentYear)== null ? "N":map.get(currentYear);
	}
}