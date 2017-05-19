package com.athena.ckx.module.pcfj;

import java.util.ArrayList;

import org.junit.Test;

import com.athena.ckx.entity.paicfj.Ckx_yongh_chengys;
import com.athena.ckx.module.paicfj.service.Ckx_yongh_chengysService;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Inject;

public class YonghcysTest extends AbstractCompomentTests{

	@Inject
	private Ckx_yongh_chengysService yonghcysService;
	@Test
	public void save(){
		ArrayList<Ckx_yongh_chengys> list = new ArrayList<Ckx_yongh_chengys>();
		
		yonghcysService.save(list, list, "张三");
	}
	@Test
	public void save1(){
		ArrayList<Ckx_yongh_chengys> list = new ArrayList<Ckx_yongh_chengys>();
		Ckx_yongh_chengys yonghcys = new Ckx_yongh_chengys();
		yonghcys.setUsercenter("UL");
		yonghcys.setYonghbh("yh000004");
		yonghcys.setChengysbh("cys0001");
		list.add(yonghcys);
		Ckx_yongh_chengys yonghcys1 = new Ckx_yongh_chengys();
		yonghcys1.setUsercenter("UL");
		yonghcys1.setYonghbh("yh000005");
		yonghcys1.setChengysbh("cys0001");
		list.add(yonghcys1);
		Ckx_yongh_chengys yonghcys2 = new Ckx_yongh_chengys();
		yonghcys2.setUsercenter("UL");
		yonghcys2.setYonghbh("yh000006");
		yonghcys2.setChengysbh("cys0001");
		list.add(yonghcys2);
		yonghcysService.save(list, list, "张三");
	}
}
