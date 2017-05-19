/**
 * 代码声明
 */
package com.athena.ckx.module.workCalendar.service;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.athena.authority.entity.LoginUser;
import com.athena.ckx.entity.cangk.Cangk;
import com.athena.ckx.entity.cangk.Xiehztbz;
import com.athena.ckx.entity.workCalendar.CkxCalendarGroup;
import com.athena.ckx.entity.workCalendar.CkxCalendarTeam;
import com.athena.ckx.entity.workCalendar.CkxCalendarVersion;
import com.athena.ckx.entity.xuqjs.Shengcx;
import com.athena.ckx.util.DBUtil;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.BaseService;
import com.athena.component.service.Message;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.dao.DataAccessException;
import com.toft.core3.transaction.annotation.Transactional;
/**
 * 工作时间关联
 * @author kong
 */
@Component
public class CkxCalendarGroupService extends BaseService<CkxCalendarGroup>{
	//获取命名空间
	@Override
	protected String getNamespace() {
		return "workCalendar";
	}
	
	/**
	 * 添加工作时间关联
	 * @param bean
	 * @return
	 */
	@Transactional
	public String addGroup(CkxCalendarGroup bean,LoginUser user){
		//bean.setAppobj(bean.getAppobj().toUpperCase());//仓库或产线编号
		bean.setCreateTime(DateTimeUtil.getAllCurrTime());//当前时间
		bean.setCreator(user.getUsername());//创建人
		bean.setEditor(user.getUsername());//修改人
		bean.setEditTime(DateTimeUtil.getAllCurrTime());//修改时间
		checkDate(bean);
		int flg = 0;
		//编组号和未来编组号不能重复
		//工作时间编组和未来编组号不能重复
		if(bean.getWeilbzh()!=null&&bean.getWeilbzh().equals(bean.getBianzh())){
			throw new ServiceException("工作时间编组和未来编组号不能重复");
		}
		flg = bean.getAppobj().length();			
		String mes = GetMessageByKey.getMessage("cansdesj",new String[]{bean.getAppobj()});	
		if(5 == flg){	
			Shengcx scx=new Shengcx();
			scx.setUsercenter(bean.getUsercenter());
			scx.setShengcxbh(bean.getAppobj());
			scx.setBiaos("1");
			if(!DBUtil.checkCount(baseDao,"ts_ckx.getCountShengcx", scx)){
				throw new ServiceException(GetMessageByKey.getMessage("shengcxzbczscxbh")+mes);
			}
//			sql += "ckx_shengcx where 1=1 and shengcxbh = '"+bean.getAppobj()+"' and usercenter = '"+bean.getUsercenter()+"'" ;			
//			mes1 =  GetMessageByKey.getMessage("shengcxzbczscxbh");
		}else if(3 == flg){
			Cangk cangk =new Cangk();
			cangk.setUsercenter(bean.getUsercenter());
			cangk.setCangkbh(bean.getAppobj());
			cangk.setBiaos("1");
			if(!DBUtil.checkCount(baseDao,"ts_ckx.getCountCangk", cangk)){
				throw new ServiceException(GetMessageByKey.getMessage("cangkzbczckbh")+mes);
			}
//			sql += "ckx_cangk where 1=1 and cangkbh = '"+bean.getAppobj()+"' and usercenter = '"+bean.getUsercenter()+"'" ;			
//			mes1 = GetMessageByKey.getMessage("cangkzbczckbh");
		}else if(4 == flg){
			Xiehztbz xiehztbz=new Xiehztbz();
			xiehztbz.setUsercenter(bean.getUsercenter());
			xiehztbz.setXiehztbzh(bean.getAppobj());
			xiehztbz.setBiaos("1");
			if(!DBUtil.checkCount(baseDao,"ts_ckx.getCountXiehztbz", xiehztbz)){
				throw new ServiceException(GetMessageByKey.getMessage("xiehztbzzbczxhztbzh")+mes);
			}
//			sql += "ckx_xiehztbz where 1=1 and xiehztbzh = '"+bean.getAppobj()+"' and usercenter = '"+bean.getUsercenter()+"'" ;				
//			//#卸货站台编组表中不存在此用户中心下卸货站台编组号为：
//			mes1 = GetMessageByKey.getMessage("xiehztbzzbczxhztbzh");
		}
//		sql += " and biaos='1'" ;	
//		mes1 +=		
//		DBUtilRemove.checkBH(sql, mes1);
		
		//验证编组号
		CkxCalendarTeam team=new CkxCalendarTeam();
		team.setBianzh(bean.getBianzh());
		team.setBiaos("1");
		String mes1 = GetMessageByKey.getMessage("gongzsjzbczsj",new String[]{bean.getBianzh()});
		if(!DBUtil.checkCount(baseDao,"workCalendar.getCountTeam", team)){
			throw new ServiceException(mes1);
		}
		
		//验证未来编组号
		if(bean.getWeilbzh()!=null&&0 != bean.getWeilbzh().length()){
			team.setBianzh(bean.getWeilbzh());
			if(!DBUtil.checkCount(baseDao,"workCalendar.getCountTeam", team)){
				throw new ServiceException(mes1);
			}
		}
		
				
//		map2.put("tableName", "ckx_calendar_team");
//		map2.put("bianzh",bean.getBianzh());
//		map2.put("biaos", "1");
//		DBUtilRemove.checkYN(map2, mes2);
//		map2.clear();
		
		//验证版次号
		CkxCalendarVersion version = new CkxCalendarVersion();
		version.setUsercenter(bean.getUsercenter());
		version.setBanc(bean.getRilbc());
		version.setBiaos("1");
		if(!DBUtil.checkCount(baseDao,"workCalendar.getCountVersion", version)){
			String mes3 = GetMessageByKey.getMessage("rilbczbczsj",new String[]{bean.getRilbc()});
			throw new ServiceException(mes3);
		}
		
		
		
//		map3.put("tableName", "ckx_calendar_version");
//		map3.put("banc",bean.getRilbc());
//		map3.put("biaos", "1");
//		map3.put("usercenter", bean.getUsercenter());
//		DBUtilRemove.checkYN(map3, mes3);
//		map3.clear();
		
//		map4.put("tableName", "ckx_calendar_team");
//		map4.put("bianzh",bean.getWeilbzh());
//		map4.put("biaos", "1");
//		if(bean.getWeilbzh()!=null&&0 != bean.getWeilbzh().length()){
//			DBUtilRemove.checkYN(map4, mes2);
//		}
//		map4.clear();
		try {			
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("workCalendar.insertCkxCalendarGroup", bean);//保存
		} catch (DataAccessException e) {
			//数据已存在
			return new Message("dataExistent","i18n.ckx.workCalendar.i18n_workCalendar").getMessage();
		}
		//保存成功
		return new Message("addSuccess","i18n.ckx.workCalendar.i18n_workCalendar").getMessage();
	}
	/**
	 * 数据保存
	 * @param bean
	 * @param user
	 * @return
	 */
	public String save(CkxCalendarGroup bean,LoginUser user){
//		bean.setAppobj(bean.getAppobj().toUpperCase());//仓库或产线编号
		bean.setEditor(user.getUsername());//修改人
		bean.setEditTime(DateTimeUtil.getAllCurrTime());//当前时间
		checkDate(bean);
		
		if(bean.getWeilbzh()!=null&&!"".equals(bean.getWeilbzh())){
			//编组号和未来编组号不能重复
			if(bean.getWeilbzh().equals(bean.getBianzh())){
				throw new ServiceException(GetMessageByKey.getMessage("工作时间编组和未来编组号不能重复"));
//				throw new ServiceException(GetMessageByKey.getMessage("bianzhhwlbzhbncf"));
			}
			//验证未来编组号
			CkxCalendarTeam team=new CkxCalendarTeam();
			team.setBianzh(bean.getWeilbzh());
			team.setBiaos("1");
			if(!DBUtil.checkCount(baseDao,"workCalendar.getCountTeam", team)){
				String mes1 = GetMessageByKey.getMessage("gongzsjzbczsj",new String[]{bean.getBianzh()});
				throw new ServiceException(mes1);
			}
		}
		
		
//		Map<String,String> map4 = new HashMap<String,String>();	
//		String mes4 = GetMessageByKey.getMessage("gongzsjzbczsj",new String[]{bean.getBianzh()});
//		map4.put("tableName", "ckx_calendar_team");
//		map4.put("bianzh",bean.getWeilbzh());	
//		map4.put("biaos", "1");
//		if(bean.getWeilbzh()!=null&&0 != bean.getWeilbzh().length()){
//			DBUtilRemove.checkYN(map4, mes4);
//		}
//		map4.clear();
		
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("workCalendar.updateCkxCalendarGroup", bean);//修改
		return new Message("saveSuccess","i18n.ckx.workCalendar.i18n_workCalendar").getMessage();
	}
	
	
	/**
	 * 根据对象编号获取工作时间(一周工作时间)
	 * @param user
	 * @param app
	 * @return
	 */
	public String getTotalTime(LoginUser user,String app){
		Map<String,String> map=new HashMap<String,String>();
		map.put("usercenter", user.getUsercenter());
		map.put("app", app);
		map.put("days", "86400");//返回时间单位 （时：24 ，分：1440  ， 秒：86400） 
		Object o=baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("workCalendar.getTotalTime",map);
		return o==null?"0":o.toString();
	}
	/**
	 * 检查生效时间是否是将来时间
	 * @param shengxsj
	 */
	private void checkDate(CkxCalendarGroup bean){
		String shengxsj = bean.getShengxsj();
		String shengxsj2 = bean.getShengxsj2();
		String shengxsj3 = bean.getShengxsj3();
		String shengxsj4 = bean.getShengxsj4();
		if(shengxsj!=null&&!"".equals(shengxsj)){
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			Date shengxTime = null;
			try {
				shengxTime = sf.parse(shengxsj);
			} catch (Exception e) {
				throw new ServiceException(GetMessageByKey.getMessage("shijgscw"));
			} 
			if(shengxTime.getTime()<= date.getTime()){
				throw new ServiceException(GetMessageByKey.getMessage("shengxsjbxsjlsj"));
			}
		}
		//验证生效时间2
		if(shengxsj2!=null&&!"".equals(shengxsj2)){
			if(shengxsj==null||"".equals(shengxsj)){	//前一个生效时间必须存在
				throw new ServiceException(GetMessageByKey.getMessage("qingyctxwlbzh"));	
			}
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			Date shengxTime = null;
			Date shengxTime2 = null;
			try {
				shengxTime = sf.parse(shengxsj);
				shengxTime2 = sf.parse(shengxsj2);
			} catch (Exception e) {
				throw new ServiceException(GetMessageByKey.getMessage("shijgscw"));
			} 
			if(shengxTime2.getTime()<= shengxTime.getTime()){
				throw new ServiceException(GetMessageByKey.getMessage("shengxsjbxdyqyg",new String[]{"2",""}));
			}
		}
		//验证生效时间3
		if(shengxsj3!=null&&!"".equals(shengxsj3)){
			if(shengxsj2==null||"".equals(shengxsj2)){	//前一个生效时间必须存在
				throw new ServiceException(GetMessageByKey.getMessage("qingyctxwlbzh"));	
			}
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			Date shengxTime2 = null;
			Date shengxTime3 = null;
			try {
				shengxTime2 = sf.parse(shengxsj2);
				shengxTime3 = sf.parse(shengxsj3);
			} catch (Exception e) {
				throw new ServiceException(GetMessageByKey.getMessage("shijgscw"));
			} 
			if(shengxTime3.getTime()<= shengxTime2.getTime()){
				throw new ServiceException(GetMessageByKey.getMessage("shengxsjbxdyqyg",new String[]{"3","2"}));
			}
		}
		//验证生效时间4
		if(shengxsj4!=null&&!"".equals(shengxsj4)){
			if(shengxsj3==null||"".equals(shengxsj3)){	//前一个生效时间必须存在
				throw new ServiceException(GetMessageByKey.getMessage("qingyctxwlbzh"));	
			}
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			Date shengxTime3 = null;
			Date shengxTime4 = null;
			try {
				shengxTime3 = sf.parse(shengxsj3);
				shengxTime4 = sf.parse(shengxsj4);
			} catch (Exception e) {
				throw new ServiceException(GetMessageByKey.getMessage("shijgscw"));
			} 
			if(shengxTime4.getTime()<= shengxTime3.getTime()){
				throw new ServiceException(GetMessageByKey.getMessage("shengxsjbxdyqyg",new String[]{"4","3"}));
			}
		}
	}
}