package com.athena.ckx.module.xuqjs;

import org.junit.Test;

import com.athena.ckx.entity.xuqjs.CkxLingj;
import com.athena.ckx.module.xuqjs.service.CkxLingjService;
import com.athena.ckx.util.DBUtilRemove;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

/**
 * @description 零件
 * @author denggq
 * @date 2012-4-27
 */

@Component
public class CkxLingjTest extends AbstractCompomentTests{
	
	@Inject
	private CkxLingjService service;

	@Test
	public void test(){
		CkxLingj lingj1 = new CkxLingj();
		lingj1.setUsercenter("QQ");
		lingj1.setLingjbh("0000000000");
		lingj1.setZhongwmc("零件1");
		lingj1.setFawmc("法文1");
		lingj1.setLingjlx("A");
		lingj1.setLingjsx("A");
		lingj1.setJihy("111");
		lingj1.setDanw("CM");
		lingj1.setZhizlx("97W");
		lingj1.setKaisrq("2012-02-03");
		lingj1.setJiesrq("2012-04-28");
		lingj1.setZhuangcxs(1.0);
		lingj1.setAnqm("111");
		lingj1.setLingjzl(1.0);
		lingj1.setBiaos("1");
		lingj1.setGuanjljjb("1");
		lingj1.setDinghcj("UW1");
		lingj1.setGongybm("123");
		lingj1.setZongcldm("123");
		lingj1.setDiycqysj("2012-02-03");
		
		logger.info("--------------test1 add Lingj----------------");
		service.save(lingj1, 1, "12345");
		logger.info("--------------test1 add Lingj----------------");
		
		CkxLingj lingj2 = new CkxLingj();
		lingj2.setUsercenter("QQ");
		lingj2.setLingjbh("0000000000");
		lingj2.setZhongwmc("零件2");
		lingj2.setFawmc("法文2");
		lingj2.setLingjlx("B");
		lingj2.setLingjsx("K");
		lingj2.setJihy("222");
		lingj2.setDanw("KG");
		lingj2.setZhizlx("97X");
		lingj2.setKaisrq("2012-02-04");
		lingj2.setJiesrq("2012-04-29");
		lingj2.setZhuangcxs(2.0);
		lingj2.setAnqm("222");
		lingj2.setLingjzl(2.0);
		lingj2.setBiaos("0");
		lingj2.setGuanjljjb("2");
		lingj2.setDinghcj("UW3");
		lingj2.setGongybm("222");
		lingj2.setZongcldm("222");
		lingj2.setDiycqysj("2012-02-04");
		
		logger.info("--------------test1 update Lingj----------------");
		service.save(lingj2, 2, "12345");
		logger.info("--------------test1 update Lingj----------------");
		
		service.doDelete(lingj2);
		
		String[] args = new String[1];
		args[0]= "delete from DEV_CKX_TEST.ckx_lingj where usercenter = 'QQ' and lingjbh = '0000000000'";
		DBUtilRemove.remove(args);
	}
	
}
