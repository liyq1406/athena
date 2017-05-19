package com.athena.ckx.module.baob.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.athena.ckx.entity.baob.Beihlgz;
import com.athena.ckx.entity.baob.Lingjjhygys;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

@Component
public class BeihlgzService extends BaseService<Beihlgz> {
	
	
	
	/**
	 * 获得命名空间
	 * @author denggq
	 * @date 2012-1-12
	 */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}
	
	@SuppressWarnings("unchecked")
	public Map<String,Object> query(Beihlgz bean,String exportXls){
		if("exportXls".equals(exportXls)){
			List<Lingjjhygys> list = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.querybeihlgz",bean);
			Map<String,Object> m = new HashMap<String, Object>();
			m.put("total", list.size());
			m.put("rows", list);
			return m;
		}
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("ts_ckx.querybeihlgz",bean,bean);
	}
	@Transactional
	public String timingDeleteBeihlgz() throws ParseException{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar c = Calendar.getInstance();
		c.setTime(sdf.parse(DateTimeUtil.getAllCurrTime()));
		c.set(Calendar.MONTH, c.get(Calendar.MONTH)-3);
		String createtime = sdf.format(c.getTime());
		Beihlgz bz = new Beihlgz();
		bz.setCreatetime(createtime);
		//1.删除备货单表中超过90天的数据（CK_BEIHD_21552）
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteBeihd",bz);
		//2.删除备货单明细表中超过90天的数据（CK_BEIHDMX_21552）
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteBeihdmx",bz);
		return "success";
	}
	
}
