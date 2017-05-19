package com.athena.ckx.module.xuqjs;

import java.util.ArrayList;

import org.junit.Test;

import com.athena.ckx.entity.xuqjs.Shengcx;
import com.athena.ckx.entity.xuqjs.Fenpq;
import com.athena.ckx.module.xuqjs.service.ShengcxService;
import com.athena.ckx.util.DBUtilRemove;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

/**
 * @description 生产线-分配区
 * @author denggq
 * @date 2012-4-27
 */

@Component
public class ShengcxTest extends AbstractCompomentTests{
	
	@Inject
	private ShengcxService shengcxService;

	@Test
	public void test(){
		Shengcx shengcx1 = new Shengcx();
		shengcx1.setUsercenter("QQ");
		shengcx1.setShengcxbh("00000");
		shengcx1.setShengcxbh("00000");
		shengcx1.setShengcjp("10");
		shengcx1.setWeilscjp("10");
		shengcx1.setQiehsj("2012-04-29");
		shengcx1.setChults("10");
		shengcx1.setBiaos("1");
		
		ArrayList<Fenpq> insert = new ArrayList<Fenpq>();
			
		ArrayList<Fenpq> edit = new ArrayList<Fenpq>();
		
		ArrayList<Fenpq> delete = new ArrayList<Fenpq>();
		
		logger.info("-----------add Shengcx------------");
		shengcxService.save(shengcx1, 1, edit, edit, edit, "12345");
		logger.info("-----------add Shengcx------------");
		
		Shengcx shengcx2 = new Shengcx();
		shengcx2.setUsercenter("QQ");
		shengcx2.setShengcxbh("00000");
		shengcx2.setShengcxbh("00000");
		shengcx2.setShengcjp("11");
		shengcx2.setWeilscjp("11");
		shengcx2.setQiehsj("2012-04-28");
		shengcx2.setChults("11");
		
		logger.info("-----------edit Shengcx------------");
		shengcxService.save(shengcx2, 2, edit, edit, edit, "12345");
		logger.info("-----------edit Shengcx------------");
		
		Fenpq fenpq1 = new Fenpq();
		fenpq1.setFenpqh("0000");
		fenpq1.setFenpqmc("分配区1");
		fenpq1.setBiaos("1");
		insert.add(fenpq1);	
		logger.info("-----------add Fenpq------------");
		shengcxService.save(shengcx2, 2, insert, edit, edit, "12345");
		logger.info("-----------add Fenpq------------");
		
		logger.info("-----------edit Fenpq------------");
		Fenpq Fenpq2 = new Fenpq();
		fenpq1.setFenpqh("0000");
		fenpq1.setFenpqmc("分配区2");
		edit.add(Fenpq2);
		shengcxService.save(shengcx2, 2, delete, edit, edit, "12345");
		logger.info("-----------edit Fenpq------------");
		
		shengcxService.doDelete(shengcx2);
		
		String[] args = new String[2];
		args[0]= "delete from DEV_CKX_TEST.ckx_shengcx where usercenter = 'QQ' and shengcxbh = '00000'";
		args[1]= "delete from DEV_CKX_TEST.ckx_fenpq where usercenter = 'QQ' and shengcxbh = '00000' and fenpqh = '0000'";
		DBUtilRemove.remove(args);
	}
	
}
