package com.athena.ckx.module.xuqjs;

import java.util.ArrayList;

import org.junit.Test;

import com.athena.ckx.entity.xuqjs.Lingjgys;
import com.athena.ckx.module.xuqjs.service.LingjgysService;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

/**
 * @description 零件供应商
 * @author denggq
 * @date 2012-4-27
 */
@Component
public class LingjgysTest extends AbstractCompomentTests{
	@Inject
	private LingjgysService service;
	
	@Test
	public void test(){
		ArrayList<Lingjgys> insert = new ArrayList<Lingjgys>();
		Lingjgys bean1 = new Lingjgys();
		bean1.setUsercenter("QQ");
		bean1.setGongysbh("0000000000");
		bean1.setLingjbh("0000000000");
		bean1.setGongyhth("888888");
		bean1.setGongyfe(100.0);
		bean1.setYouxq(100);
		bean1.setFayd("100");
		bean1.setShengxsj("2012-12-12");
		bean1.setShixsj("2012-12-12");
		bean1.setZuixqdl(100.0);
		bean1.setCankfz(100.0);
		bean1.setZhijcjbl("1");
		bean1.setShifyzpch("1");
		bean1.setUcbzlx("00000");
		bean1.setUcrl(111.0);
		bean1.setUabzlx("10000");
		bean1.setUaucgs(111);
		bean1.setGaib("1");
		bean1.setNeic("1");
		bean1.setShifczlsbz("1");
		insert.add(bean1);
		
		ArrayList<Lingjgys> update = new ArrayList<Lingjgys>();
		Lingjgys bean2 = new Lingjgys();
		bean2.setUsercenter("QQ");
		bean2.setGongysbh("0000000000");
		bean2.setLingjbh("0000000000");
		bean2.setGongyhth("88888888");
		bean2.setGongyfe(90.0);
		bean2.setYouxq(50);
		bean2.setFayd("100");
		bean2.setShengxsj("2012-12-13");
		bean2.setShixsj("2012-12-13");
		bean2.setZuixqdl(0.0);
		bean2.setCankfz(31.0);
		bean2.setZhijcjbl("20");
		bean2.setShifyzpch("0");
		bean2.setUcbzlx("00001");
		bean2.setUcrl(112.0);
		bean2.setUabzlx("10001");
		bean2.setUaucgs(113);
		bean2.setGaib("2");
		bean2.setNeic("2");
		bean2.setShifczlsbz("1");
		update.add(bean2);
		
		service.save(insert,update,update,"QQ");
	}
}
