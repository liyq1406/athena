/**
 * 代码声明
 */
package com.athena.ckx.module.paicfj.service;

import java.util.List;
import java.util.Date;

import com.athena.ckx.entity.paicfj.Yunslx;
import com.athena.ckx.entity.paicfj.YunslxJiaof;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.toft.core3.container.annotation.Component;
/**
 * 运输路线交付时刻service
 * @author dsimedd001
 *
 */
@Component
public class YunslxJiaofService extends BaseService<YunslxJiaof>{
	
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}
	public String inserts(List<YunslxJiaof> insert,Yunslx yunslx){
		Date date = new Date();
		for (YunslxJiaof bean : insert) {
			bean.setUsercenter(yunslx.getUsercenter());
			bean.setYunslxbh(yunslx.getYunslxbh());
			bean.setCreator(yunslx.getCreator());
			bean.setCreate_time(date);
			bean.setEditor(yunslx.getEditor());
			bean.setEdit_time(date);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertYunslxJiaof",bean);
		}
		return "";
	}
	public String removes(List<YunslxJiaof> delete,Yunslx yunslx){
		for (YunslxJiaof bean : delete) {
			bean.setUsercenter(yunslx.getUsercenter());
			bean.setYunslxbh(yunslx.getYunslxbh());
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteYunslxJiaof",bean);
		}
		return "";
	}
	
}