package com.athena.ckx.module.baob.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.ckx.entity.baob.Faycg100t;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;


@Component
public class Faycg100tService extends BaseService<Faycg100t> {
	
	
	
	/**
	 * 获得命名空间
	 * @author xryuan
	 * @date 2013-3-29
	 */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}
	
	@SuppressWarnings("unchecked")
	public Map<String,Object> query(Faycg100t bean,String exportXls,Map<String,String> map){
		if("exportXls".equals(exportXls)){
			List<Faycg100t> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryfaycg100t",map);
			Map<String,Object> m = new HashMap<String, Object>();
			m.put("total", list.size());
			m.put("rows", list);
			return m;
		}
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ts_ckx.queryfaycg100t",map,bean);
	}
	
	public List queryztsx() {
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryallzhuangtsx");
	}
	
	@Transactional
	public String timingAddFaycg100t() throws ParseException{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar c = Calendar.getInstance();
		c.setTime(sdf.parse(DateTimeUtil.getAllCurrTime()));
		c.set(Calendar.MONTH, c.get(Calendar.MONTH)-2);
		String qiysj = sdf.format(c.getTime());
		Faycg100t faycg100t = new Faycg100t();
		faycg100t.setQiysj(qiysj);
		//1.删除归档表中归档时间超过超过60天的数据
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.delyaohl21591",faycg100t);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deluabq21591",faycg100t);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.delusbq21591",faycg100t);
		//2.删除结果表中创建时间超过超过60天的数据
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.delfaycg100t",faycg100t);
		//3.插入结果表
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.insertRepfaycg100t",sdf.format(new Date()));
		return "success";
	}
	
}
