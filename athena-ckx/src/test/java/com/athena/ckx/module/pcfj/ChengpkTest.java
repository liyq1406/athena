package com.athena.ckx.module.pcfj;

import java.util.ArrayList;

import org.junit.Test;

import com.athena.ckx.entity.paicfj.Ckx_chengpk;
import com.athena.ckx.module.paicfj.service.Ckx_chengpkService;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Inject;

public class ChengpkTest extends AbstractCompomentTests {

	@Inject
	private Ckx_chengpkService chengpkService;
	@Test
	public void save(){
		ArrayList<Ckx_chengpk> list=new ArrayList<Ckx_chengpk>();
		
		chengpkService.save(list,list,list,"张三");
	}
	@Test
	public void save1(){
		ArrayList<Ckx_chengpk> list=new ArrayList<Ckx_chengpk>();
		Ckx_chengpk chengpk=new Ckx_chengpk();
		chengpk.setUsercenter("UN");
		chengpk.setShixts("1");
		list.add(chengpk);
		
		Ckx_chengpk chengpk1=new Ckx_chengpk();
		chengpk1.setUsercenter("UZ");
		chengpk1.setShixts("1");
		list.add(chengpk1);
		Ckx_chengpk chengpk2=new Ckx_chengpk();
		chengpk2.setUsercenter("UM");
		chengpk2.setShixts("1");
		list.add(chengpk2);
		
		chengpkService.save(list,list,list,"张三");
	}
}
