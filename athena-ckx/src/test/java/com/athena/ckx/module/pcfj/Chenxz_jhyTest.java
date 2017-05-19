package com.athena.ckx.module.pcfj;

import java.util.ArrayList;

import org.junit.Test;

import com.athena.ckx.entity.paicfj.Ckx_chanxz_jhy;
import com.athena.ckx.module.paicfj.service.Ckx_chanxz_jhyService;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Inject;

public class Chenxz_jhyTest extends AbstractCompomentTests {

	@Inject
	private Ckx_chanxz_jhyService jhyService;
	@Test
	public void save(){
		ArrayList<Ckx_chanxz_jhy> list=new ArrayList<Ckx_chanxz_jhy>();
		Ckx_chanxz_jhy jhy=new Ckx_chanxz_jhy();
		jhy.setUsercenter("UL");
		jhy.setChanxzbh("UXFL3");
		jhy.setJihybh("jhy0004");
		list.add(jhy);
		Ckx_chanxz_jhy jhy1=new Ckx_chanxz_jhy();
		jhy1.setUsercenter("UL");
		jhy1.setChanxzbh("UXFL3");
		jhy1.setJihybh("jhy0005");
		list.add(jhy1);
		Ckx_chanxz_jhy jhy2=new Ckx_chanxz_jhy();
		jhy2.setUsercenter("UL");
		jhy2.setChanxzbh("UXFL3");
		jhy2.setJihybh("jhy0006");
		list.add(jhy2);
		System.out.println(jhyService.save(list, list, list, "张三"));
	}
	@Test
	public void save1(){
		ArrayList<Ckx_chanxz_jhy> list=new ArrayList<Ckx_chanxz_jhy>();
		System.out.println(jhyService.save(list, list, list, "张三"));
	}
	
}
