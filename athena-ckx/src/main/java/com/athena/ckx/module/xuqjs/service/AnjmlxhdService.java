package com.athena.ckx.module.xuqjs.service;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.athena.ckx.entity.xuqjs.Anjmlxhd;
import com.athena.component.service.BaseService;
import com.athena.db.ConstantDbCode;
import com.athena.module.common.CommonFun;
import com.toft.core3.container.annotation.Component;

@Component
public class AnjmlxhdService extends BaseService {
	
	/**
	 * 查询按件目录卸货点
	 * @param anjmlxhd 按件目录卸货点
	 * @return 查询结果
	 */
	public Map queryAnjmlxhd(Anjmlxhd anjmlxhd){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectPages("anjmlxhd.queryAnjmlxhd",anjmlxhd,anjmlxhd);
	}
	
	/***
	 * 查询按件目录卸货点
	 * @param anjmlxhd 按件目录卸货点
	 * @return 查询结果
	 */
	public Anjmlxhd selectAnjmlxhd(Anjmlxhd anjmlxhd){
		return (Anjmlxhd) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anjmlxhd.queryAnjmlxhd",anjmlxhd,anjmlxhd);
	}
	
	/**
	 * 保存按件目录卸货点
	 * @param anjmlxhd 按件目录卸货点
	 * @return 保存结果
	 */
	public int saveAnjmlxhd(Anjmlxhd anjmlxhd){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("anjmlxhd.saveAnjmlxhd", anjmlxhd);
	}
	
	/**
	 * 更新按件目录卸货点
	 * @param anjmlxhd 按件目录卸货点
	 * @return 更新结果
	 */
	public int updateAnjmlxhd(Anjmlxhd anjmlxhd){
		return baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("anjmlxhd.updateAnjmlxhd", anjmlxhd);
	}
	
	/**
	 * 校验仓库是否存在
	 * @return 按件目录卸货点
	 */
	public int checkCangkbh(Anjmlxhd anjmlxhd){
		String str = (String) baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("anjmlxhd.checkCangkbh",anjmlxhd);
		return Integer.valueOf(StringUtils.defaultString(str, "0"));
	}

}
