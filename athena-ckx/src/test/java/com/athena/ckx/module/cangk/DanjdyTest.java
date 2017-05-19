package com.athena.ckx.module.cangk;

import java.util.ArrayList;

import org.junit.Test;

import com.athena.ckx.entity.cangk.Danjdy;
import com.athena.ckx.entity.cangk.Kehczm;
import com.athena.ckx.module.cangk.service.DanjdyService;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Inject;

/***
 * 测试类测试单据打印-增加，删除，修改，查询
 * 继承AbstractCompomentTests,该类会自动初始化SDC环境
 * @author denggq
 * @date 2012-2-18
 */
public class DanjdyTest extends AbstractCompomentTests{

	@Inject
	private DanjdyService service;
	
	@Test
	public void save(){
		
		Kehczm kehczm = new Kehczm();
		kehczm.setUsercenter("UX");
		kehczm.setKehbh("999");
		
		//insert
		Danjdy danjdy1=new Danjdy();
		danjdy1.setCangkbh("999");
		danjdy1.setDanjlx("2");
		danjdy1.setShifdy("1");
		danjdy1.setDayls(2);
		danjdy1.setDayfs(9);
		ArrayList<Danjdy> insert=new ArrayList<Danjdy>();
		insert.add(danjdy1);
		
		//update
		Danjdy danjdy2=new Danjdy();
		danjdy2.setCangkbh("999");
		danjdy2.setDanjlx("2");
		danjdy2.setShifdy("0");
		danjdy2.setDayls(3);
		danjdy2.setDayfs(5);
		ArrayList<Danjdy> update=new ArrayList<Danjdy>();
		update.add(danjdy2);

		//save(物理删除)
		service.save(insert, update, update, "user001",kehczm);
		
	}
	
	@Test
	public void query(){
		Danjdy danjdy=new Danjdy();
		service.select(danjdy);
	}
	
}
