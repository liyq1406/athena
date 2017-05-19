package com.athena.ckx.module.baob.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.athena.ckx.entity.baob.Liulbb;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;


@Component
public class LiulbbService extends BaseService<Liulbb> {
	
	
	
	/**
	 * 获得命名空间
	 * @author xryuan
	 * @date 2013-5-9
	 */
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}
	
	@Transactional
	public String timingAddLiulBB() throws ParseException{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
		Calendar c = Calendar.getInstance();
		c.setTime(sdf.parse(DateTimeUtil.getAllCurrTime()));
		c.set(Calendar.MONTH, c.get(Calendar.MONTH)-1);
		String huizsj = sdf.format(c.getTime());
		c.set(Calendar.MONTH, c.get(Calendar.MONTH)-12);
		String cleansj = sdf.format(c.getTime());
		//1.删除结果表中汇总时间超过12个月的数据
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.delliulbb",cleansj);
		Map<String,String> map = new HashMap<String, String>();
        map.put("create_time", DateTimeUtil.getAllCurrTime());
        map.put("huizsj",huizsj);
		//2.插入结果表
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.insertRepLiubb",map);
		return "success";
	}
	
}
