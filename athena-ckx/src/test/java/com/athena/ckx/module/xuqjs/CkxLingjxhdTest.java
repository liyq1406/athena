package com.athena.ckx.module.xuqjs;

import java.util.ArrayList;

import org.junit.Test;

import com.athena.ckx.entity.xuqjs.CkxLingjxhd;
import com.athena.ckx.module.xuqjs.service.CkxLingjxhdService;
import com.athena.ckx.util.DBUtilRemove;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

/**
 * @description 消耗点零件
 * @author denggq
 * @date 2012-4-27
 */

@Component
public class CkxLingjxhdTest extends AbstractCompomentTests{
	
	@Inject
	private CkxLingjxhdService service;

	@Test
	public void test(){
		CkxLingjxhd lingjxhd1 = new CkxLingjxhd();
		lingjxhd1.setUsercenter("QQ");
		lingjxhd1.setLingjbh("0000000000");
		lingjxhd1.setXiaohdbh("000000000");
		lingjxhd1.setWulbh("0000");
		lingjxhd1.setFenpqbh("11111");
		lingjxhd1.setShengcxbh("11111");
		lingjxhd1.setPeislxbh("1111");
		lingjxhd1.setQianhcbs("1");
		lingjxhd1.setJipbzwz("11");
		lingjxhd1.setShengxr("2012-04-29");
		lingjxhd1.setJiesr("9999-12-31");
		lingjxhd1.setGuanlybh("111");
		lingjxhd1.setTiqjsxhbl(90.0);
		lingjxhd1.setXiaohbl(100.0);
		lingjxhd1.setGongysbh("1111111111");
		lingjxhd1.setShunxglbz("1");
		lingjxhd1.setZidfhbz("1");
		lingjxhd1.setXianbyhlx("K");
		lingjxhd1.setYancsxbz("1");
		lingjxhd1.setBeihdbz("1");
		lingjxhd1.setXiaohcbh("11111");
		lingjxhd1.setXiaohccxbh("1111");
		lingjxhd1.setYuanxhdbh("000000000");
		lingjxhd1.setAnqkcts(111.0);
		lingjxhd1.setAnqkcs(111.0);
		lingjxhd1.setXianbllkc(111.0);
		lingjxhd1.setYifyhlzl(111.0);
		lingjxhd1.setJiaofzl(111.0);
		lingjxhd1.setXittzz(111.0);
		lingjxhd1.setPdsbz("1");
		lingjxhd1.setBiaos("1");
		lingjxhd1.setZhongzzl(111.0);
		
		logger.info("--------------test1 add Lingjxhd----------------");
		service.save(lingjxhd1, "test", 1,null);
		logger.info("--------------test1 add Lingjxhd----------------");
		
		CkxLingjxhd lingjxhd2 = new CkxLingjxhd();
		lingjxhd2.setUsercenter("QQ");
		lingjxhd2.setLingjbh("0000000000");
		lingjxhd2.setXiaohdbh("000000000");
		lingjxhd2.setWulbh("0001");
		lingjxhd2.setFenpqbh("11110");
		lingjxhd2.setShengcxbh("11110");
		lingjxhd2.setPeislxbh("1110");
		lingjxhd2.setQianhcbs("0");
		lingjxhd2.setJipbzwz("10");
		lingjxhd2.setShengxr("2012-04-20");
		lingjxhd2.setJiesr("9999-12-30");
		lingjxhd2.setGuanlybh("110");
		lingjxhd2.setTiqjsxhbl(90.9);
		lingjxhd2.setXiaohbl(0.0);
		lingjxhd2.setGongysbh("1111111110");
		lingjxhd2.setShunxglbz("0");
		lingjxhd2.setZidfhbz("0");
		lingjxhd2.setXianbyhlx("P");
		lingjxhd2.setYancsxbz("0");
		lingjxhd2.setBeihdbz("0");
		lingjxhd2.setXiaohcbh("11110");
		lingjxhd2.setXiaohccxbh("1110");
		lingjxhd2.setYuanxhdbh("000000001");
		lingjxhd2.setAnqkcts(111.9);
		lingjxhd2.setAnqkcs(111.9);
		lingjxhd2.setXianbllkc(111.9);
		lingjxhd2.setYifyhlzl(111.9);
		lingjxhd2.setJiaofzl(111.9);
		lingjxhd2.setXittzz(111.9);
		lingjxhd2.setPdsbz("0");
		lingjxhd2.setZhongzzl(111.9);
		
		logger.info("--------------test2 update Lingjxhd----------------");
		service.save(lingjxhd2, "test", 2,null);
		logger.info("--------------test2 update Lingjxhd----------------");
		
		logger.info("--------------test3 invalidate Lingjxhd----------------");
		service.doDelete(lingjxhd2, "test");
		logger.info("--------------test3 invalidate Lingjxhd----------------");
		
		service.doDelete(lingjxhd2, "test");
		
		String[] args = new String[1];
		args[0]= "delete from DEV_CKX_TEST.ckx_lingjxhd where usercenter = 'QQ' and lingjbh= '0000000000' and xiaohdbh = '000000000'";
		DBUtilRemove.remove(args);
		
		ArrayList<CkxLingjxhd> insert = new ArrayList<CkxLingjxhd>();
		insert.add(lingjxhd1);
		
		ArrayList<CkxLingjxhd> edit = new ArrayList<CkxLingjxhd>();
		edit.add(lingjxhd2);
		
		service.saves(insert, edit, edit, "QQ", "test");
		
		String[] args1 = new String[1];
		args1[0]= "delete from DEV_CKX_TEST.ckx_lingjxhd where usercenter = 'QQ' and lingjbh= '0000000000' and xiaohdbh = '000000000'";
		DBUtilRemove.remove(args1);
	}
	
}
