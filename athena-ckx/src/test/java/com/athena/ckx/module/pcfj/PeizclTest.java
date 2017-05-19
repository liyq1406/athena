package com.athena.ckx.module.pcfj;

import java.util.ArrayList;

import org.junit.Test;

import com.athena.ckx.entity.paicfj.Ckx_peizcl;
import com.athena.ckx.entity.paicfj.Ckx_peizclzb;
import com.athena.ckx.module.paicfj.service.Ckx_peizclService;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Inject;


public class PeizclTest extends AbstractCompomentTests {

	@Inject
	private Ckx_peizclService peizclService;
	@Test
	public void save(){
		ArrayList<Ckx_peizclzb> list = new ArrayList<Ckx_peizclzb>();
		Ckx_peizcl peizcl=new Ckx_peizcl();
		peizclService.save(peizcl, 1, list, list, list, "张三");
	}
	
	@Test
	public void save1(){
		ArrayList<Ckx_peizclzb> list = new ArrayList<Ckx_peizclzb>();
		Ckx_peizcl peizcl=new Ckx_peizcl();
		peizcl.setUsercenter("UL");
		peizcl.setCelbh("cl00012");
		peizcl.setCelmc("策略12");
		
		Ckx_peizclzb peizclzb=new Ckx_peizclzb();
		peizclzb.setBaozzbh("bzz0012");
		peizclzb.setBaozsl("11");
		list.add(peizclzb);
		
		Ckx_peizclzb peizclzb1=new Ckx_peizclzb();
		peizclzb1.setBaozzbh("bzz0013");
		peizclzb1.setBaozsl("11");
		list.add(peizclzb1);
		
		Ckx_peizclzb peizclzb2=new Ckx_peizclzb();
		peizclzb2.setBaozzbh("bzz0014");
		peizclzb2.setBaozsl("11");
		list.add(peizclzb2);
		
		peizclService.save(peizcl, 1, list, list, list, "张三");
		ArrayList<Ckx_peizclzb> list1 = new ArrayList<Ckx_peizclzb>();
		peizclService.save(peizcl, 2, list1, list1, list1, "张三");
		peizclService.remove(peizcl);
		
		
//		String[] args = new String[1];
//		args[0] = "delete from ckx_peizcl where celbh = 'cl00012' and usercenter = 'UL'";
//		DBUtilRemove.remove(args);
		
		
	}
}
