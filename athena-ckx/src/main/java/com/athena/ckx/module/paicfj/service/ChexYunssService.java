/**
 * 代码声明
 */
package com.athena.ckx.module.paicfj.service;


import java.util.Date;
import java.util.List;

import com.athena.ckx.entity.paicfj.ChexYunss;
import com.athena.ckx.entity.paicfj.Ckx_chex;
import com.athena.ckx.entity.paicfj.Ckx_gongys;
import com.athena.ckx.util.GetMessageByKey;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.transaction.annotation.Transactional;
/**
 * 车型运输商关系Service
 * @author hj
 *
 */
@Component
public class ChexYunssService extends BaseService<ChexYunss>{
	@Inject
	private Ckx_chexService ckx_chexService;//车型service
	@Inject
	private Ckx_gongysService ckx_gongysService;//运输商service
	@Override
	protected String getNamespace() {//获取sqlmap的表空间
		return "ts_ckx";
	}
	/**
	 * 数据操作
	 * @param insert
	 * @param edit
	 * @param delete
	 * @param userID
	 * @return
	 * @throws ServiceException
	 */
	@Transactional
	public String save(List<ChexYunss> insert,	List<ChexYunss> edit,List<ChexYunss> delete,String userID)throws ServiceException{
		//如果没有做任何操作，则直接跳出此方法
		if(0==insert.size() && 0== edit.size()	&& 	0== delete.size()){
			return "null";
		}
		inserts(insert,userID);
		edits(edit, userID);
		removes(delete);
		return "success";
	}
	/**
	 * 数据录入
	 * @param insert
	 * @param userID
	 * @return
	 */
	private String inserts(List<ChexYunss> insert,String userID){
		Date date = new Date();
		for (ChexYunss chexYunss : insert) {
			checkChexYunss(chexYunss);//检查是否存在此车型的车型编号和运输商的运输商编号
			chexYunss.setCreator(userID);
			chexYunss.setCreate_time(date);
			chexYunss.setEditor(userID);
			chexYunss.setEdit_time(date);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.insertChexYunss",chexYunss);
		}
		return "";
	}
	/**
	 * 数据编辑
	 * @param edit
	 * @param userID
	 * @return
	 */
	private String edits(List<ChexYunss> edit,String userID){
		Date date = new Date();
		for (ChexYunss chexYunss : edit) {
			chexYunss.setEditor(userID);
			chexYunss.setEdit_time(date);
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.updateChexYunss",chexYunss);
		}
		return "";
	}
	/**
	 * 数据删除（物理删除）
	 * @param delete
	 * @return
	 */
	private String removes(List<ChexYunss> delete){
		for (ChexYunss chexYunss : delete) {			
			baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("ts_ckx.deleteChexYunss",chexYunss);
		}
		return "";
	}
	/**
	 * 检查是否存在此车型的车型编号和运输商的运输商编号
	 * @param bean
	 * @return
	 */
	private String checkChexYunss(ChexYunss bean){
		Ckx_chex chex = new Ckx_chex();
		chex.setChexbh(bean.getChexbh());
		chex.setBiaos("1");
		//查询车型表，若不存在此车型编号，则方法执行中断
		if(0 == ckx_chexService.list(chex).size()) {
			//不存在此车型编号或数据已失效
			throw new ServiceException(GetMessageByKey.getMessage("bczcxbh"));
		}
		Ckx_gongys gongys = new Ckx_gongys();
		gongys.setUsercenter(bean.getUsercenter());
		gongys.setBiaos("1");
		gongys.setGcbh(bean.getYunssbh());
		gongys.setLeix("3");//只差运输商的数据
		//查询运输商表，若不存在此运输商编号，则方法执行中断
		if(0 == ckx_gongysService.list(gongys).size()){
			//不存在此运输商编号或数据已失效
			throw new ServiceException(GetMessageByKey.getMessage("bczyssbh"));
		}
		return "";
	}
	
}