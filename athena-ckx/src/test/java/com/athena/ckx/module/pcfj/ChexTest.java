package com.athena.ckx.module.pcfj;


import java.util.ArrayList;

import org.junit.Test;

import com.athena.ckx.entity.paicfj.Ckx_chex;
import com.athena.ckx.module.paicfj.service.Ckx_chexService;
import com.athena.ckx.util.DBUtilRemove;
import com.athena.component.test.AbstractCompomentTests;

import com.toft.core3.container.annotation.Inject;

public class ChexTest extends AbstractCompomentTests{

	@Inject
	private Ckx_chexService chexService;
	@Test
	public void save(){
		ArrayList<Ckx_chex> list = new ArrayList<Ckx_chex>();
		chexService.save(list, list, list, "张三");
	}
	
	@Test
	public void save1(){
		String[] args = new String[3];
		args[0]= "delete from ckx_chex where chexbh = 'cx00000007' ";
		args[1]= "delete from ckx_chex where chexbh = 'cx00000008' ";
		args[2]= "delete from ckx_chex where chexbh = 'cx00000009' ";
		DBUtilRemove.remove(args);
		
		ArrayList<Ckx_chex> list = new ArrayList<Ckx_chex>();
		
		Ckx_chex chex=new Ckx_chex();
		chex.setChexbh("cx00000007");
		list.add(chex);
		
		Ckx_chex chex1=new Ckx_chex();
		chex1.setChexbh("cx00000008");
		list.add(chex1);
		
		Ckx_chex chex2=new Ckx_chex();
		chex2.setChexbh("cx00000009");
		list.add(chex2);
		
		chexService.save(list, list, list, "张三");
		
		
	
		
	
	}
}
