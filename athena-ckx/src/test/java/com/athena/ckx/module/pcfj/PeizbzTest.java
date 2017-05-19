package com.athena.ckx.module.pcfj;

import java.util.ArrayList;

import org.junit.Test;

import com.athena.ckx.entity.paicfj.Ckx_peizbz;
import com.athena.ckx.entity.paicfj.Ckx_peizbzbh;
import com.athena.ckx.module.paicfj.service.Ckx_peizbzService;
import com.athena.ckx.util.DBUtilRemove;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Inject;

public class PeizbzTest extends AbstractCompomentTests {

	@Inject
	private Ckx_peizbzService peizbzService;
	@Test
	public void save(){
		ArrayList<Ckx_peizbzbh> list=new ArrayList<Ckx_peizbzbh>();
		
		Ckx_peizbz peizbz=new Ckx_peizbz();
		
		peizbzService.save(peizbz, 1, list,  list,"张三");
		
	}
	
	@Test
	public void save1(){
		String[] args = new String[1];
		args[0] = "delete from ckx_peizbz where baozzbh = 'bzz00015' ";
		DBUtilRemove.remove(args);
		ArrayList<Ckx_peizbzbh> list=new ArrayList<Ckx_peizbzbh>();
		
		Ckx_peizbz peizbz=new Ckx_peizbz();
		peizbz.setBaozzbh("bzz00015");
		peizbz.setBaozzmc("包装组99");
		peizbz.setBaozbsjs("2");
		
		Ckx_peizbzbh peizbzbh=new Ckx_peizbzbh();
		peizbzbh.setBaozlx("LX002");
		list.add(peizbzbh);
		
		Ckx_peizbzbh peizbzbh1=new Ckx_peizbzbh();
		peizbzbh1.setBaozlx("LX003");
		list.add(peizbzbh1);
		
		Ckx_peizbzbh peizbzbh2=new Ckx_peizbzbh();
		peizbzbh2.setBaozlx("LX004");
		list.add(peizbzbh2);
		
		peizbzService.save(peizbz, 1, list,  list,"张三");
		peizbzService.remove(peizbz, "张三");
		ArrayList<Ckx_peizbzbh> list1=new ArrayList<Ckx_peizbzbh>();
		peizbzService.save(peizbz, 2, list1,  list1,"张三");
		
		
		
	}
	
}
