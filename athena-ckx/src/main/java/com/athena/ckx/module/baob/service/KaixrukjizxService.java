package com.athena.ckx.module.baob.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.ckx.entity.baob.Kaixrukjizx;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

@SuppressWarnings("rawtypes")
@Component
public class KaixrukjizxService extends BaseService<Kaixrukjizx> {
	
	
	
	/**
	 * 获得命名空间
	 * @author xryuan
	 * @date 2013-3-26
	 */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}
	
	@SuppressWarnings("unchecked")
	public Map<String,Object> query(Kaixrukjizx bean,String exportXls,Map<String,String> map){
		if("exportXls".equals(exportXls)){
			List<Kaixrukjizx> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.querykaixrukjizx",map);
			Map<String,Object> m = new HashMap<String, Object>();
			m.put("total", list.size());
			m.put("rows", list);
			return m;
		}
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ts_ckx.querykaixrukjizx",map,bean);
	}
	
	@Transactional
	public String timingAddkaixruk() throws ParseException{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar c = Calendar.getInstance();
		c.setTime(sdf.parse(DateTimeUtil.getAllCurrTime()));
		c.set(Calendar.MONTH, c.get(Calendar.MONTH)-3);
		String qiysj = sdf.format(c.getTime());
		Kaixrukjizx kxrk = new Kaixrukjizx();
		kxrk.setQiysj(qiysj);
		//1.删除归档表中归档时间超过超过90天的数据
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.delxqjstc21543",kxrk);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deldaohtzd21543",kxrk);
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deluabq21543",kxrk);
		//2.删除结果表中创建时间超过超过90天的数据
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.delkaixruk",kxrk);
		//3.插入结果表
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.insertRepkaixruk",sdf.format(new Date()));
		return "success";
	}
	
}
