package com.athena.print.module.sys.service;

import java.util.ArrayList;

import org.junit.Test;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.db.ConstantDbCode;
import com.athena.print.DBUtilRemove;
import com.athena.print.entity.sys.PrintDictinfos;
import com.athena.print.entity.sys.Xitcsdy;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.ibatis.support.AbstractIBatisDao;


public class PrintDictinfosServiceTest extends AbstractCompomentTests{

	@Inject
	private PrintDictinfosService printDictinfosService;
	
	@Inject 
	protected AbstractIBatisDao baseDao;
	
	@Test
	public void testAddPrintDictinfos(){
		
		PrintDictinfos printDictinfos = new PrintDictinfos();
		Xitcsdy  xitcsdy = new Xitcsdy();



		//插入主表 参考系数据字典表
		xitcsdy.setUsercenter("UW");
		xitcsdy.setZidbm("49");
		xitcsdy.setZidlx("DJLX");
		
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("sys.insertXitcsdy",xitcsdy);
		
		//新增、修改
		ArrayList<PrintDictinfos> update = new ArrayList<PrintDictinfos>();
		printDictinfos.setUsercenter("UW");
		printDictinfos.setZidbm("49");
		printDictinfos.setZidmc("质检单49A");
		printDictinfos.setDanjzbh("W49A");
		update.add(printDictinfos );
		ArrayList<PrintDictinfos> insert=new ArrayList<PrintDictinfos>();
		ArrayList<PrintDictinfos> delete=new ArrayList<PrintDictinfos>();
		printDictinfosService.saves(insert,update,delete,"张三");
		
		//删除新增的
		baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).execute("sys.deleteDictinfos",printDictinfos);	

		//删除参考系数据字典表
		String[] args = new String[1];
		args[0] = "delete from ckx_xitcsdy where  usercenter='UW' and zidbm='49' and zidlx='DJLX'";
		DBUtilRemove.remove(args);	
		
	}
}
