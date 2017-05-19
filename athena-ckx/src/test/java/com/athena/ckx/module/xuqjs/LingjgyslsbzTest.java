package com.athena.ckx.module.xuqjs;

import java.util.ArrayList;

import org.junit.Test;

import com.athena.ckx.entity.xuqjs.Lingjgyslsbz;
import com.athena.ckx.module.xuqjs.service.LingjgyslsbzService;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

/**
 * @description 零件供应商-临时包装
 * @author denggq
 * @date 2012-4-27
 */
@Component
public class LingjgyslsbzTest extends AbstractCompomentTests{
	
	@Inject
	private LingjgyslsbzService service;
	
	@Test
	public void test(){
		ArrayList<Lingjgyslsbz> insert = new ArrayList<Lingjgyslsbz>();
		service.save(insert,insert,insert,"test");
		
		Lingjgyslsbz bean1 = new Lingjgyslsbz();
		bean1.setUsercenter("QQ");
		bean1.setGongysbh("0000000000");
		bean1.setLingjbh("0000000000");
		bean1.setShengxsj("2012-12-12");
		bean1.setShixsj("2012-12-12");
		bean1.setXuh(1);
		bean1.setUcbzlx("00000");
		bean1.setUcrl(100.0);
		bean1.setUabzlx("00000");
		bean1.setUaucgs(100);
		bean1.setGaib("1");
		bean1.setNeic("1");
		insert.add(bean1);
		
		ArrayList<Lingjgyslsbz> update = new ArrayList<Lingjgyslsbz>();
		Lingjgyslsbz bean2 = new Lingjgyslsbz();
		bean2.setUsercenter("QQ");
		bean2.setGongysbh("0000000000");
		bean2.setLingjbh("0000000000");
		bean2.setShengxsj("2012-12-13");
		bean2.setShixsj("2012-12-13");
		bean2.setXuh(1);
		bean2.setUcbzlx("00001");
		bean2.setUcrl(101.0);
		bean2.setUabzlx("00001");
		bean2.setUaucgs(200);
		bean2.setGaib("2");
		bean2.setNeic("2");
		update.add(bean2);
		
		service.save(insert,update,update,"test");
	}
}
