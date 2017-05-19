package com.athena.ckx.module.pcfj;

import org.junit.Test;

import com.athena.ckx.entity.paicfj.Ckx_keh_chengpk;
import com.athena.ckx.module.paicfj.service.Ckx_keh_chengpkService;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Inject;

public class Keh_chengpkTest extends AbstractCompomentTests {

	@Inject
	private Ckx_keh_chengpkService keh_chengpkService;
	
	@Test
	public void save(){
		
		Ckx_keh_chengpk keh_chengpk = new Ckx_keh_chengpk();
		keh_chengpk.setUsercenter("UL");
		keh_chengpk.setCangkbh("ck1");
		keh_chengpk.setChengysbh("gys000009");
		keh_chengpk.setKehbh("kh001");
		keh_chengpk.setBeihlx("1");
		keh_chengpkService.save(keh_chengpk, 1, "123");
		
		Ckx_keh_chengpk keh_chengpk1 = new Ckx_keh_chengpk();
		keh_chengpk1.setUsercenter("UL");
		keh_chengpk1.setCangkbh("ck1");
		keh_chengpk1.setChengysbh("gys000009");
		keh_chengpk1.setKehbh("kh001");
		keh_chengpk1.setBeihlx("2");
		keh_chengpk1.setDingdtqq("1");
		keh_chengpk1.setYaoctqq("2");
		keh_chengpkService.save(keh_chengpk1, 2, "123");
		keh_chengpkService.doDelete(keh_chengpk1);
		
		
		
	}
}
