package com.athena.ckx.module.cangk;

import java.util.ArrayList;

import org.junit.Test;

import com.athena.ckx.entity.cangk.Yansbllj;
import com.athena.ckx.module.cangk.service.YansblljService;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Inject;

/***
 * 测试类测试零件验收比例设置-增加，删除，修改，查询
 * @author denggq
 * @date 2012-2-17
 */
public class YansblljTest extends AbstractCompomentTests{

	@Inject
	private YansblljService service;
	
	@Test
	public void save(){
		//insert
		Yansbllj yansbllj1=new Yansbllj();
		yansbllj1.setUsercenter("UL");
		yansbllj1.setLingjbh("9999999999");
		yansbllj1.setGongysbh("GYS0000001");
		yansbllj1.setYansxbh("999");
		yansbllj1.setYansxsm("SM999");
		yansbllj1.setYansbl(55);
		ArrayList<Yansbllj> insert=new ArrayList<Yansbllj>();
		insert.add(yansbllj1);
		
		//update
		Yansbllj yansbllj2=new Yansbllj();
		yansbllj2.setUsercenter("UL");
		yansbllj2.setLingjbh("9999999999");
		yansbllj2.setGongysbh("GYS0000001");
		yansbllj2.setYansxbh("999");
		yansbllj2.setYansxsm("SM999");
		yansbllj2.setYansbl(100);
		ArrayList<Yansbllj> update=new ArrayList<Yansbllj>();
		update.add(yansbllj2);
		
		//save(物理删除)
		service.save(insert, update, update, "user001");
		
	}
	
	@Test
	public void query(){
		Yansbllj yansbllj=new Yansbllj();
		service.select(yansbllj);
	}
	
	@Test
	public void commit(){
		service.commit("UW", "test");
	}
}
