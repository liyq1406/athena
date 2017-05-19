/**
 * 代码声明
 */
package com.athena.ckx.module.cangk.service;

import java.util.ArrayList;
import java.util.List;

import com.athena.ckx.entity.cangk.PrintStrogdict;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.date.DateUtil;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.transaction.annotation.Transactional;

@Component
public class PrintStrogdictService extends BaseService<PrintStrogdict>{
	
	@Override
	//返回sqlMap的命名空间
	protected String getNamespace() {
		return "ts_ckx";
	}
	
	//单表行编辑的 增 删改 操作
	@SuppressWarnings("unchecked")
	@Transactional
	public String saves(ArrayList<PrintStrogdict> edit,PrintStrogdict bean ) throws ServiceException{
			//标识为0的表示是 需要在保存后中   新增的数据  
			List<PrintStrogdict> strogdictList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("ts_ckx.queryPrintStrogdict",bean);
			if(0!=strogdictList.size()){
				for (PrintStrogdict printStrogdict : strogdictList) {
					//取得标识为 0 的状态的 数据
					if("0".equals(printStrogdict.getActive())){
						printStrogdict.setCreate_time(DateUtil.curDateTime());
						//执行插入的操作 
						baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertPrintStrogdict", printStrogdict);
					}
				}
			}
		//批量的修改操作
		edits(edit);
		return "success";
	}
	
	/**
	 * 私有批量update方法
	 * @author wy
	 * @date 2011-2-9
	 * @param update
	 * @return ""
	 */
	private String edits(List<PrintStrogdict> update) throws ServiceException{
		//判断是否为空
		if(null!=update){
			for(PrintStrogdict printStrogdict:update){
				//设置修改时间
				printStrogdict.setEdit_time(DateUtil.curDateTime());
				//更新操作前 来判断下单据是否   停用
				if("0".equals(printStrogdict.getBiaos())&&null!=printStrogdict.getDanjzbh()){
					//如果单据标识修改为停用 则必须清空单据所在组
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updatePrintStrogdicts", printStrogdict);
				}else{
					//执行批量的更新
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updatePrintStrogdict", printStrogdict);
				}
			}
		}
		return "Data change success";
	}
	
}