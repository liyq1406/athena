package com.athena.ckx.module.cangk;

import java.util.ArrayList;

import org.junit.Test;

import com.athena.ckx.entity.cangk.KuwdjBaoz;
import com.athena.ckx.module.cangk.service.KuwdjBaozService;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Inject;

/***
 * 测试类测试库位等级与包装-增加，删除，修改，查询
 * @author denggq
 * @date 2012-2-17
 */
public class KuwdjBaozTest extends AbstractCompomentTests{

	@Inject
	private KuwdjBaozService service;
	
	@Test
	public void save(){
		//insert
		KuwdjBaoz kuwdjBaoz1=new KuwdjBaoz();
		kuwdjBaoz1.setUsercenter("UX");
		kuwdjBaoz1.setCangkbh("999");
		kuwdjBaoz1.setKuwdjbh("99999");
		kuwdjBaoz1.setBaozlx("99999");
		kuwdjBaoz1.setBaozgs(99999);
		ArrayList<KuwdjBaoz> insert=new ArrayList<KuwdjBaoz>();
		insert.add(kuwdjBaoz1);
		
		//update
		KuwdjBaoz kuwdjBaoz2=new KuwdjBaoz();
		kuwdjBaoz2.setUsercenter("UX");
		kuwdjBaoz2.setCangkbh("999");
		kuwdjBaoz2.setKuwdjbh("99999");
		kuwdjBaoz2.setBaozlx("99999");
		kuwdjBaoz2.setBaozgs(22222);
		ArrayList<KuwdjBaoz> update=new ArrayList<KuwdjBaoz>();
		update.add(kuwdjBaoz2);
		
		//save(物理删除)
		service.save(insert, update, update, "user001");
		
	}
	
	@Test
	public void query(){
		KuwdjBaoz kuwdjBaoz=new KuwdjBaoz();
		service.select(kuwdjBaoz);
	}
	
}
