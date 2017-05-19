package com.athena.ckx.module.xuqjs;

import org.junit.Test;

import com.athena.ckx.entity.xuqjs.Gongcy;
import com.athena.ckx.module.xuqjs.service.GongcyService;
import com.athena.ckx.util.DBUtilRemove;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

/**
 * @description 供应商-承运商-运输商
 * @author denggq
 * @date 2012-4-27
 */

@Component
public class GongysTest extends AbstractCompomentTests{
	
	@Inject
	private GongcyService service;

	@Test
	public void test(){
		Gongcy gongys1 = new Gongcy();
		gongys1.setUsercenter("QQ");
		gongys1.setGcbh("0000000000");
		gongys1.setLeix("1");
		gongys1.setGonghlx("97W");
		gongys1.setGongsmc("公司名称1");
		gongys1.setDiz("地址1");
		gongys1.setYoub("555555");
		gongys1.setLianxr("人名1");
		gongys1.setDianh("13600000000");
		gongys1.setChuanz("020-00000000");
		gongys1.setBeihzq(1.0);
		gongys1.setFayzq(1.0);
		gongys1.setKacbztj(100.0);
		gongys1.setSonghzdpc(9);
		gongys1.setSonghzxpc(1);
		gongys1.setNeibgys_cangkbh("111");
		gongys1.setEdzzl(1.0);
		gongys1.setBodfdxs(1.0);
		gongys1.setBiaos("1");
		gongys1.setFayd("111111");
		gongys1.setNeibyhzx("UX");
		
		logger.info("--------------test1 add Gongys----------------");
		logger.info(service.save(gongys1, 1, "12345"));
		logger.info("--------------test1 add Gongys----------------");
		
		Gongcy gongys2 = new Gongcy();
		gongys2.setUsercenter("QQ");
		gongys2.setGcbh("0000000000");
		gongys2.setLeix("1");
		gongys2.setGonghlx("97X");
		gongys2.setGongsmc("公司名称2");
		gongys2.setDiz("地址2");
		gongys2.setYoub("555552");
		gongys2.setLianxr("人名2");
		gongys2.setDianh("13600000002");
		gongys2.setChuanz("020-00000002");
		gongys2.setBeihzq(2.0);
		gongys2.setFayzq(2.0);
		gongys2.setKacbztj(200.0);
		gongys2.setSonghzdpc(99);
		gongys2.setSonghzxpc(2);
		gongys2.setNeibgys_cangkbh("222");
		gongys2.setEdzzl(9.9);
		gongys2.setBodfdxs(2.0);
		gongys2.setFayd("222222");
		gongys2.setNeibyhzx("UW");
		
		logger.info("--------------test2 update Gongys----------------");
		logger.info(service.save(gongys2, 2, "12345"));
		logger.info("--------------test2 update Gongys----------------");
		
		service.removes(gongys2, "12345");
		
		String[] args = new String[1];
		args[0]= "delete from DEV_CKX_TEST.ckx_gongys where usercenter = 'QQ' and gcbh = '0000000000'";
		DBUtilRemove.remove(args);
	}
	
}
