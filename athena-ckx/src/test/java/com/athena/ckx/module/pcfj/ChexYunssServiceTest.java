/**
 * 代码声明
 */
package com.athena.ckx.module.pcfj;

import java.util.ArrayList;

import org.junit.Test;

import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Inject;

import com.athena.ckx.entity.paicfj.ChexYunss;
import com.athena.ckx.module.paicfj.service.ChexYunssService;

public class ChexYunssServiceTest extends AbstractCompomentTests{
	
	@Inject
	private ChexYunssService chexYunssService;
	
	/**
	 * 保存
	 */
	@Test
	public void testSave1() {
		chexYunssService.save(new ArrayList<ChexYunss>(),
				new ArrayList<ChexYunss>(),
				new ArrayList<ChexYunss>(),"张三"
				);
	}
	@Test
	public void testSave2() {
		ArrayList<ChexYunss> list = new ArrayList<ChexYunss>();
		ChexYunss chexYunss = new ChexYunss();
		chexYunss.setChexbh("cx00000006");
		chexYunss.setUsercenter("UW");
		chexYunss.setYunssbh("3434343434");
		chexYunss.setZuidsl("123");
		chexYunssService.save(list,	list,list,"张三"	);
	}
	
}