package com.athena.ckx.module.transTime;

import org.junit.Test;

import com.athena.ckx.entity.transTime.CkxYunssk;
import com.athena.ckx.module.transTime.service.CkxYunsskService;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Inject;

public class YunsskTest extends AbstractCompomentTests{

	@Inject
	private CkxYunsskService ckxYunsskService;
	@Test
	public void insert(){
		//多线程模式下无法运行此方法 如要测试，修改com.athena.ckx.module.transTime.service。CkxYunssjMbService类中的
		//getYunsskList方法最后一行代码
		ckxYunsskService.insertTimeOut("test");
	}
	@Test
	public void save(){
		try {
			CkxYunssk bean = new CkxYunssk();
			bean.setUsercenter("UW");
			bean.setXiehztbh("WH1");
			bean.setGcbh("M100100000");
			bean.setXuh(8);
			bean.setDaohsj("2012-8-15 11:52:00");
			bean.setFacsj("2012-8-15 11:52:00");
			ckxYunsskService.remove(bean);
			ckxYunsskService.save(bean, 1);
			ckxYunsskService.save(bean, 2);
		} catch (Exception e) {
			
		}
	}
}
