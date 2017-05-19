/**
 * 代码声明
 */
package com.athena.ckx.module.workCalendar.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.ckx.entity.workCalendar.CkxCalendarGroup;
import com.athena.ckx.entity.workCalendar.CkxCalendarTeam;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.BaseService;
import com.athena.component.service.Message;
import com.athena.db.ConstantDbCode;
import com.athena.util.cache.CacheManager;
import com.athena.util.cache.CacheValue;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.dao.DataAccessException;
import com.toft.core3.transaction.annotation.Transactional;
/**
 * 工作时间编组
 * @author kong
 *
 */
@Component
public class PcCalendarTeamService extends BaseService<CkxCalendarTeam>{
	@Inject
	private  CacheManager cacheManager;//缓存
	//获取命名空间
	@Override
	protected String getNamespace() {
		return "workCalendarPC";
	}
	/**
	 * 添加工作时间
	 * @param bean
	 * @param usercode
	 * @return
	 */
	@Transactional
	public String addTeam(CkxCalendarTeam bean,String usercode,LoginUser user){
		if("".equals(usercode)&&bean.getBianzh()==null){
			//编组号或指定码不能为空
			return new Message("notNullCode","i18n.ckx.workCalendar.i18n_workCalendar").getMessage();
		}
		//验证开始时间结束时间
		if(bean.getJiezsj().compareTo(bean.getKaissj())<0){
			//开始时间不能大于结束时间
			return new Message("starbigError","i18n.ckx.workCalendar.i18n_workCalendar").getMessage();
		}
		//生成新的编组号
		if(!"".equals(usercode)){
			//get month
			String string=DateTimeUtil.getCurrYearMonth();//当前时间
			String s=string.substring(string.indexOf('.')+1);//截取
			String month=Integer.toHexString(Integer.parseInt(s)).toUpperCase();//将月份转换为16进制的编码
			//获取最大编组序号和当前月份
			Object o=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("workCalendarPC.getMaxTeamCode",month);
			String no="01";
			if(o!=null){//本组只有上一条数据
				no=o.toString();
				no=String.valueOf(Integer.valueOf(no)+1);
				no=no.length()==1?"0"+no:no;
			}
			
			//生成编组号 :1年型+1月+2序号+4用户指定
			String currentYear = DateTimeUtil.getCurrDate().substring(0,4);
			String banc=getNianxm(currentYear)+month.toUpperCase()+no+usercode.toUpperCase();
			bean.setBianzh(banc);//编组号
		}
		//获取同一编组同一天的最大序号的值
		Object o=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("workCalendarPC.getMaxXuhCode",bean);
		String no="01";
		CkxCalendarTeam t=null;
		if(o!=null){//本组有上一条数据
			t=(CkxCalendarTeam) o;
			no=t.getXuh();//获取序号
			no=String.valueOf(Integer.valueOf(no)+1);
			no=no.length()==1?"0"+no:no;
			
			//上一条夸天了，下一套必须也跨天
			if (t.getTiaozsj() == 1 && bean.getTiaozsj() != 1) {
				return GetMessageByKey.getMessage("gongzsjdbxkt");
			}


			//验证时间是否重复
			if(t.getJiezsj().compareTo(bean.getKaissj())>=0&&bean.getTiaozsj()!=1){
				//上一条的结束时间大于下一条的开始时间
				//msg:"编组内同一天的工作时间有重叠!";
//				return new Message("groupTimeRepeating","i18n.ckx.workCalendar.i18n_workCalendar").getMessage();
				return GetMessageByKey.getMessage("tongytgzsjycd");
			}
		}
		bean.setCreateTime(DateTimeUtil.getAllCurrTime());//创建时间
		bean.setCreator(user.getUsername());//创建人
		bean.setEditor(user.getUsername());//修改人
		bean.setEditTime(DateTimeUtil.getAllCurrTime());//修改时间
		bean.setXuh(no);//设置序号
		bean.setBan(bean.getBan().toUpperCase());//版次大写
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("workCalendarPC.insertCkxCalendarTeam", bean);//插入 
		return new Message("addSuccess","i18n.ckx.workCalendar.i18n_workCalendar").getMessage();
	}
	/**
	 * 保存工作时间
	 * @param editList
	 * @param user
	 * @return
	 */
	public String edit(List<CkxCalendarTeam> editList,LoginUser user){
		for (CkxCalendarTeam team : editList) {
			//验证开始时间结束时间
			if(team.getJiezsj().compareTo(team.getKaissj())<0){
				//开始时间不能大于结束时间
				return new Message("starbigError","i18n.ckx.workCalendar.i18n_workCalendar").getMessage();
			}
			//当前数据上一条
			Object before=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("workCalendarPC.getTeamBefore",team);
			if(before!=null){
				CkxCalendarTeam t=(CkxCalendarTeam) before;
				
				//上一条夸天了，下一套必须也跨天
				if(t.getTiaozsj()==1&&team.getTiaozsj()!=1){
					return GetMessageByKey.getMessage("xuhbxkt",new String[]{team.getXuh()});
				}
				
				
				//验证时间是否重复 上一条的结束时间大于下一条的开始时间&&是同一天的
				if(t.getJiezsj().compareTo(team.getKaissj())>0&&team.getTiaozsj().equals(t.getTiaozsj())){
					//msg:"编组内同一天的工作时间有重叠!";
					return new Message("groupTimeRepeating","i18n.ckx.workCalendar.i18n_workCalendar").getMessage();
//					return "请确定时间是否跨天";
				}
				
			}
			//当前数据下一条
			Object after=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("workCalendarPC.getTeamAfter",team);
			if(after!=null){
				CkxCalendarTeam t=(CkxCalendarTeam) after;
				//验证时间是否重复
				if(t.getKaissj().compareTo(team.getJiezsj())<0&&team.getTiaozsj().equals(t.getTiaozsj())){
					//下一条的开始时间小于上一条的结束时间 &&是同一天的
					return new Message("timeError","i18n.ckx.workCalendar.i18n_workCalendar").getMessage();
				}
			}
			team.setEditor(user.getUsername());//修改人
			team.setEditTime(DateTimeUtil.getAllCurrTime());//修改时间
			team.setBan(team.getBan().toUpperCase());//班  大写
			super.baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("workCalendarPC.updateCkxCalendarTeam", team);
		}
		return new Message("saveSuccess","i18n.ckx.workCalendar.i18n_workCalendar").getMessage();
	}
	/**
	 * 根据编组号复制工作时间组
	 * @param usercode
	 * @param teamcode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public String copyTeam(String usercode,String teamcode,LoginUser user){
		CkxCalendarTeam team =new CkxCalendarTeam();
		team.setBianzh(teamcode.toUpperCase());
		List<CkxCalendarTeam> list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("workCalendarPC.queryCkxCalendarTeam",team);
		if(list==null||list.size()==0){
			//未找到编组号{0}
			return new Message("notFindGroupObj","i18n.ckx.workCalendar.i18n_workCalendar").getMessage(new String[]{teamcode});
		}
		/*  生成新编组号  */
		//get month
		String string=DateTimeUtil.getCurrYearMonth();//当前时间
		String s=string.substring(string.indexOf('.')+1);//截取月份
		String month = Integer.toHexString(Integer.parseInt(s));//将月份转换为16进制的编码
		//获取最大编组序号和当前月份
		Object o=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("workCalendarPC.getMaxTeamCode",month);
		String no="01";
		if(o!=null){//本组有上一条数据
			no=o.toString();
			no=String.valueOf(Integer.valueOf(no)+1);
			no=no.length()==1?"0"+no:no;
		}
	
		//生成编组号 :1年型+1月+2序号+4用户指定
		String currentYear = DateTimeUtil.getCurrDate().substring(0,4);
		String bianzh=getNianxm(currentYear)+month.toUpperCase()+no+usercode.toUpperCase();
		for (CkxCalendarTeam t : list) {
			team =new CkxCalendarTeam();
			team.setBianzh(bianzh);//编组号
			team.setXingqxh(t.getXingqxh());//星期序号
			team.setXuh(t.getXuh());//序号
			team.setKaissj(t.getKaissj());//开始时间
			team.setJiezsj(t.getJiezsj());//结束时间
			team.setBan(t.getBan());//班
			team.setTiaozsj(t.getTiaozsj());//调整时间
			team.setCreateTime(DateTimeUtil.getAllCurrTime());//创建时间
			team.setCreator(user.getUsername());//创建人
			team.setEditor(user.getUsername());//修改人
			team.setEditTime(DateTimeUtil.getAllCurrTime());//修改时间
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("workCalendarPC.insertCkxCalendarTeam", team);
		}
		return new Message("copySuccess","i18n.ckx.workCalendar.i18n_workCalendar").getMessage();
	}
	/**
	 * 根据编组逻辑删除
	 * @param teamcode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public String delTeam(String teamcode,LoginUser user){
		//查找数据
		CkxCalendarTeam team =new CkxCalendarTeam();
		team.setBianzh(teamcode.toUpperCase());//编组号
		team.setEditor(user.getUsername());//修改人
		team.setEditTime(DateTimeUtil.getAllCurrTime());//修改时间
		List<CkxCalendarTeam> list=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("workCalendarPC.queryCkxCalendarTeam",team);
		if(list==null||list.size()==0){
			//未找到编组号{0}
			return new Message("notFindGroupObj","i18n.ckx.workCalendar.i18n_workCalendar").getMessage(new String[]{teamcode});
		}
		//检查此版次号是否与仓库产线有关联
		CkxCalendarGroup group=new CkxCalendarGroup();
		group.setBianzh(teamcode.toUpperCase());//编组号
		List<CkxCalendarGroup> glist=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("workCalendarPC.queryCkxCalendarGroup",group);
		if(glist!=null&&glist.size()!=0){
			return new Message("objGroupDelError","i18n.ckx.workCalendar.i18n_workCalendar").getMessage(new String[]{teamcode});
		}
		//逻辑删除
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("workCalendarPC.deleteCkxCalendarTeamLogic",team);
		return new Message("deleteSuccess","i18n.ckx.workCalendar.i18n_workCalendar").getMessage();
	}
	/**
	 * 物理删除一行数据
	 * @param bean
	 * @return
	 * mantis12 0002695
	 */
	@Transactional
	public String physicalTeam(CkxCalendarTeam bean){
		try {
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("workCalendarPC.deleteCkxCalendarTeam",bean);
		} catch (DataAccessException e) {
			return GetMessageByKey.getMessage("physicalDeletefail");
		}
		return GetMessageByKey.getMessage("physicalDeletesuccess");
	}
	
	/**
	 * 获取编组号分组数据
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CkxCalendarTeam> getSelectTeamCode(CkxCalendarTeam bean){
		List<CkxCalendarTeam> list =new ArrayList<CkxCalendarTeam>();
		List<String> slist=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("workCalendarPC.getSelectTeamCode",bean);
		for (String string : slist) {//编组号信息集合
			CkxCalendarTeam c=new CkxCalendarTeam();
			c.setBianzh(string);
			list.add(c);
		}
		return list;
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