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
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;
/**
 * 运输路线service
 * @author hj
 *
 */
@Component
public class YunslxService extends BaseService<Yunslx>{
	@Inject
	private YunslxJiaofService yunslxJiaofService;
	@Override
	protected String getNamespace() {
		return "ts_ckx";
	}
	@Transactional
	public String save(Yunslx bean,Integer operant,List<YunslxJiaof> insert,List<YunslxJiaof> delete){
		if(1 == operant){
			insert(bean);
		}else{
			edit(bean);
		}
		yunslxJiaofService.inserts(insert, bean);
		yunslxJiaofService.removes(delete, bean);
		return "success";
	}
	private String insert(Yunslx bean){
		bean.setCreate_time(new Date());	
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertYunslx",bean);
		return "";
	}
	private String edit(Yunslx bean){
		bean.setEdit_time(new Date());
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateYunslx",bean);
		return "";
	}
	@Transactional
	public String remove(Yunslx bean){
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteYunslx",bean);
		return "success";
	}
}