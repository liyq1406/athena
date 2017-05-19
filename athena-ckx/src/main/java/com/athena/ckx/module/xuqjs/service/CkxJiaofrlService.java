package com.athena.ckx.module.xuqjs.service;

import com.athena.ckx.entity.workCalendar.CkxCalendarCenter;
import com.athena.ckx.entity.xuqjs.CkxJiaofrl;
import com.athena.ckx.util.DBUtil;

import com.athena.ckx.util.DateTimeUtil;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

/**
 * 交互码字典
 * @author denggq
 * @date 2012-4-6
 */
@Component
public class CkxJiaofrlService extends BaseService<CkxJiaofrl>{

	/**
	 * 获得命名空间
	 * @author denggq
	 * @date 2012-4-6
	 */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}

	/**
	 * @author denggq
	 * @date 2012-4-6
	 * @param year
	 * @param username
	 * @return String
	 */
	@Transactional
	public String calculate(String usercenter,String year, String userId) throws ServiceException{
		//所算年是否存在中心日历
//		String sql1 = "select count(*) from "+DBUtilRemove.getdbSchemal()+"ckx_calendar_center where usercenter = '"+usercenter+"' and riq like '"+year+"%' and biaos = '1'";
//		DBUtilRemove.checkBH(sql1, GetMessageByKey.getMessage("yonghuzhongxin")+usercenter+ GetMessageByKey.getMessage("xia")+year+ GetMessageByKey.getMessage("zhongxinrlsx"));
		CkxCalendarCenter bean = new CkxCalendarCenter();
		bean.setUsercenter(usercenter);
		bean.setEditor(year);
		DBUtil.checkCount(baseDao, "workCalendar.getCountCkxCalendarCenter", bean, GetMessageByKey.getMessage("yonghuzhongxin")+usercenter+ GetMessageByKey.getMessage("xia")+year+ GetMessageByKey.getMessage("zhongxinrlsx"));
		CkxJiaofrl j = new CkxJiaofrl();//交付日历
		j.setUsercenter(usercenter);//用户中心
		j.setRi(year);//年
		j.setCreator(userId);//增加人
		j.setCreate_time(DateTimeUtil.getAllCurrTime());//增加时间
		j.setEditor(userId);//修改人
		j.setEdit_time(DateTimeUtil.getAllCurrTime());//修改时间
		
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteJiaofrl",j);//清空交付日历对应年的数据
		
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertJiaofrl",j);//根据中心日历获得交付日历
		
		return "success";
	}
	
	
	
}
