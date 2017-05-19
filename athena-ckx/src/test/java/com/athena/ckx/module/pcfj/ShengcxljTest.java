package com.athena.ckx.module.pcfj;

import java.util.ArrayList;

import org.junit.Test;

import com.athena.ckx.entity.paicfj.Ckx_shengcx_lingj;
import com.athena.ckx.module.paicfj.service.Ckx_shengcx_lingjService;
import com.athena.component.test.AbstractCompomentTests;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Inject;

public class ShengcxljTest extends AbstractCompomentTests{

	@Inject
	private Ckx_shengcx_lingjService shengcxljService;
	@Test
	public void save(){
		ArrayList<Ckx_shengcx_lingj> list=new ArrayList<Ckx_shengcx_lingj>();
		shengcxljService.save(list,list,list,"张三");
	}
	@Test
	public void save1(){
		try {
			ArrayList<Ckx_shengcx_lingj> list=new ArrayList<Ckx_shengcx_lingj>();
			ArrayList<Ckx_shengcx_lingj> list1=new ArrayList<Ckx_shengcx_lingj>();
			Ckx_shengcx_lingj shengcxlj=new Ckx_shengcx_lingj();
			shengcxlj.setUsercenter("UL");
			shengcxlj.setLingjbh("3333333333");
			shengcxlj.setShengcxbh("UXFL1");
//			shengcxlj.setShengcbl(20.0);
			list.add(shengcxlj);
			Ckx_shengcx_lingj shengcxlj1=new Ckx_shengcx_lingj();
			shengcxlj1.setUsercenter("UL");
			shengcxlj1.setLingjbh("3333333333");
			shengcxlj1.setShengcxbh("UXFL2");
//			shengcxlj1.setShengcbl(80.0);
			list.add(shengcxlj1);
			shengcxljService.save(list,list1,list1,"张三");
			shengcxljService.save(list1,list,list,"张三");
			
		} catch (ServiceException e) {
		
		}
	}
	@Test
	public void save2(){
		ArrayList<Ckx_shengcx_lingj> list=new ArrayList<Ckx_shengcx_lingj>();
		ArrayList<Ckx_shengcx_lingj> list1=new ArrayList<Ckx_shengcx_lingj>();
		Ckx_shengcx_lingj shengcxlj=new Ckx_shengcx_lingj();
		shengcxlj.setUsercenter("UL");
		shengcxlj.setLingjbh("4444444444");
		shengcxlj.setShengcxbh("UXFL7");
		shengcxlj.setZhuxfx("0");
//		shengcxlj.setShengcbl(36.0);
		list.add(shengcxlj);
		try {
			shengcxljService.save(list1,list,list,"张三");
		} catch (Exception e) {
//			System.out.println("-------------------" +e.getMessage().lastIndexOf("同一用户中心下的同一零件的生产比例之和应该为100%"));
			if(e.getMessage().lastIndexOf("同一用户中心下的同一零件的生产比例之和应该为100%")<0){
				throw new ServiceException(e.getMessage());
			}
		}
	}
	

}
