package com.athena.ckx.module.pcfj;



import java.util.ArrayList;

import org.junit.Test;


import com.athena.ckx.entity.paicfj.Ckx_chanxz;
import com.athena.ckx.entity.paicfj.Ckx_chanxz_paiccs;
import com.athena.ckx.module.paicfj.service.Ckx_chanxzService;
import com.athena.ckx.util.DBUtilRemove;
import com.athena.component.test.AbstractCompomentTests;

import com.toft.core3.container.annotation.Inject;

public class ChanxzTest extends AbstractCompomentTests {

	@Inject
	private Ckx_chanxzService chanxzService;
	@Test
	public void save(){
		ArrayList<Ckx_chanxz_paiccs> list=new ArrayList<Ckx_chanxz_paiccs>();
		Ckx_chanxz c=new Ckx_chanxz();
		chanxzService.save(c, 1, list, list, list, "张三");
	}
	@Test
	public void save1(){
		String[] args = new String[2];
		args[0]= "delete from ckx_chanxz where usercenter = 'UW2' and chanxzbh = 'U91'";
		args[1]= "delete from ckx_chanxz_paiccs where usercenter = 'UW2' and chanxzbh = 'U91' and banb = to_date('2015-06-19','yyyy-MM-dd')";
		DBUtilRemove.remove(args);
		
		ArrayList<Ckx_chanxz_paiccs> list=new ArrayList<Ckx_chanxz_paiccs>();
		ArrayList<Ckx_chanxz_paiccs> list1=new ArrayList<Ckx_chanxz_paiccs>();
		ArrayList<Ckx_chanxz_paiccs> list2=new ArrayList<Ckx_chanxz_paiccs>();
		Ckx_chanxz c=new Ckx_chanxz();		//main
		c.setUsercenter("UW2");				//key
		c.setChanxzbh("U91");				//key
		c.setChanxzmc("产线组2");
		
		Ckx_chanxz_paiccs paiccs1=new Ckx_chanxz_paiccs();   //sub1
		paiccs1.setBanb("2015-06-17");		//key
		paiccs1.setDagdw(1.0);
		list.add(paiccs1);
		list2.add(paiccs1);
		Ckx_chanxz_paiccs paiccs2=new Ckx_chanxz_paiccs();    //sub2
		paiccs2.setBanb("2015-06-18");      
		paiccs2.setDagdw(1.0);
		list.add(paiccs2);
		list2.add(paiccs2);
		Ckx_chanxz_paiccs paiccs3=new Ckx_chanxz_paiccs();    //sub3
		paiccs3.setBanb("2015-06-19");
		paiccs3.setDagdw(1.1);
		list.add(paiccs3);
		
		chanxzService.save(c, 1, list, list, list1, "张三");
		chanxzService.save(c, 2, list1, list1, list2, "张三");
	
	}
	

	@Test
	public void save2(){
		ArrayList<Ckx_chanxz_paiccs> list = new ArrayList<Ckx_chanxz_paiccs>();
		ArrayList<Ckx_chanxz_paiccs> list1 = new ArrayList<Ckx_chanxz_paiccs>();
		Ckx_chanxz c=new Ckx_chanxz();
		c.setUsercenter("UL");
		c.setChanxzbh("UF2");
		c.setChanxzmc("产线组5");
		Ckx_chanxz_paiccs paiccs1=new Ckx_chanxz_paiccs();
		paiccs1.setBanb("2012-01-17");
		paiccs1.setDagdw(1.5);
		list.add(paiccs1);
		Ckx_chanxz_paiccs paiccs2=new Ckx_chanxz_paiccs();
		paiccs2.setBanb("2012-03-18");
		paiccs2.setDagdw(1.6);
		list.add(paiccs2);
		Ckx_chanxz_paiccs paiccs3=new Ckx_chanxz_paiccs();
		paiccs3.setBanb("2012-03-19 2222222222");
		paiccs3.setDagdw(1.4);
//		list.add(paiccs3);
		list1.add(paiccs3);
		try {
			chanxzService.save(c, 1, list, list, list, "张三");
		} catch (Exception e) {
//			e.printStackTrace();
		}
		try {
			chanxzService.save(c, 1, list1, list, list, "张三");
		} catch (Exception e) {
//			e.printStackTrace();
		}
//		String[] args = new String[1];
//		args[0]= "delete from ckx_chanxz where usercenter = 'UL' and chanxzbh = 'UXFL5'";
//		DBUtilRemove.remove(args);
	}
	@Test
	public void save3(){
		ArrayList<Ckx_chanxz_paiccs> list=new ArrayList<Ckx_chanxz_paiccs>();
		ArrayList<Ckx_chanxz_paiccs> list1=new ArrayList<Ckx_chanxz_paiccs>();
		Ckx_chanxz c=new Ckx_chanxz();
		c.setUsercenter("UX");
		c.setChanxzbh("UF3");
		c.setChanxzmc("产线组5");
		Ckx_chanxz_paiccs paiccs1=new Ckx_chanxz_paiccs();
		paiccs1.setBanb("2012-1-5");
		paiccs1.setDagdw(1.3);
		list.add(paiccs1);
		try {
			chanxzService.save(c, 2, list1, list1, list, "张三");
		} catch (Exception e) {
//			e.printStackTrace();
		}
//		String[] args = new String[1];
//		args[0]= "delete from ckx_chanxz where usercenter = 'UW2' and chanxzbh = 'UXFL2'";
//		DBUtilRemove.remove(args);
	}
	@Test
	public void save4(){
		ArrayList<Ckx_chanxz_paiccs> list=new ArrayList<Ckx_chanxz_paiccs>();
		ArrayList<Ckx_chanxz_paiccs> list1=new ArrayList<Ckx_chanxz_paiccs>();
		Ckx_chanxz c=new Ckx_chanxz();
		c.setUsercenter("UX");
		c.setChanxzbh("UF2");
		c.setChanxzmc("产线组5");
		Ckx_chanxz_paiccs paiccs1=new Ckx_chanxz_paiccs();
		paiccs1.setBanb("2012-1-5sdfasdfadf");
		paiccs1.setDagdw(1.2);
		list.add(paiccs1);
		try {
			chanxzService.save(c, 2, list1, list1, list, "张三");
		} catch (Exception e) {
//			e.printStackTrace();
		}
	}
//	@Test
//	public void save5(){
//		thrown.expect(ServiceException.class);
//		ArrayList<Ckx_chanxz_paiccs> list=new ArrayList<Ckx_chanxz_paiccs>();
//		Ckx_chanxz c=new Ckx_chanxz();
//		c.setUsercenter("UL");
//		c.setChanxzbh("UXFL5");
//		c.setChanxzmc("产线组5");
//		Ckx_chanxz_paiccs paiccs1=new Ckx_chanxz_paiccs();
//		paiccs1.setBanb("2012-03-17");
//		paiccs1.setDagdw("1");
//		list.add(paiccs1);
//		Ckx_chanxz_paiccs paiccs2=new Ckx_chanxz_paiccs();
//		paiccs2.setBanb("2012-03-18");
//		paiccs2.setDagdw("1");
//		list.add(paiccs2);
//		Ckx_chanxz_paiccs paiccs3=new Ckx_chanxz_paiccs();
//		paiccs3.setBanb("20120319");
//		paiccs3.setDagdw("1");
//		list.add(paiccs3);
//		chanxzService.save(c, 2, list, list, list, "张三");
//	}
	
}
