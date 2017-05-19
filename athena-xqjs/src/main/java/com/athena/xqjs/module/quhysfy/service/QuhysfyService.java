package com.athena.xqjs.module.quhysfy.service;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.util.exception.ServiceException;
import com.athena.xqjs.entity.quhyuns.Rukmx;
import com.athena.xqjs.entity.quhyuns.Yunsfyhz;
import com.toft.core3.container.annotation.Component;

/**
 * 取货运输
 * @author denggq
 * 2012-3-19
 */
@Component
public class QuhysfyService extends BaseService<Yunsfyhz>{

	@Override
	public String getNamespace() {
		return "ts_quhysfy";
	}

	public Yunsfyhz getMaxDanjh(Yunsfyhz bean) {
		return  (Yunsfyhz) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectObject("ts_quhysfy.queryMaxDanjh", bean);
	}
	
	public String chehuiYunsfyhz(Yunsfyhz bean,String loginuser) {
			String flag="失败";
			   Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			   bean.setEditor(loginuser);//修改人
			   bean.setEdit_time(formatter.format(new Date()));//修改时间
				Yunsfyhz yunsfyh  =(Yunsfyhz) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).selectObject("ts_quhysfy.getYunsfyhz", bean);
				if(StringUtils.isNotBlank(yunsfyh.getFlag()) && yunsfyh.getFlag().equals("1")){
					throw new ServiceException("单据号"+yunsfyh.getDanjh()+"目前在重算中，不能操作该单据");
				}
			//预审状态撤回
			if(bean.getShenhzt().equals("2") ){
				bean.setBiaos("0");
				
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ts_quhysfy.yuschYunsfyhz", bean);
				Rukmx rukmx=new Rukmx();
				rukmx.setDanjh(bean.getDanjh());
				rukmx.setShenhzt("1");
				rukmx.setUsercenter(bean.getUsercenter());
				rukmx.setEditor(loginuser);//修改人
				rukmx.setEdit_time(formatter.format(new Date()));//修改时间
				rukmx.setDanjlx(bean.getDanjlx());
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ts_quhysfy.yuschRukmx", rukmx);
			}else if(bean.getShenhzt().equals("3")){              //预审状态撤回
				if(StringUtils.isNotBlank(bean.getJinjjdjh())){
					baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ts_quhysfy.yuschJinjYunsfyhz", bean);
				}
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ts_quhysfy.chuschYunsfyhz", bean);
			}else if(bean.getShenhzt().equals("4")){
				baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ts_quhysfy.zhongschYunsfyhz", bean);
			}	
			 flag="成功";
		return flag;
	}
	
	public String zhongshenYunsfyhz(Yunsfyhz bean,String loginuser) {
			 Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			 bean.setZhongsr(loginuser);//终审人
			 bean.setZhongs_time(formatter.format(new Date()));//终审时间
			 bean.setShenhzt("4");
			 baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_XQJS).execute("ts_quhysfy.zhongshenYunsfyhz", bean);
		return "成功";
	}
}
