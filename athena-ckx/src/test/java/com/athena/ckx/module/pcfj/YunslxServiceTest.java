/**
 * 代码声明
 */
package com.athena.ckx.module.pcfj;

import java.util.ArrayList;

import org.junit.Test;

import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Inject;

import com.athena.ckx.entity.paicfj.Yunslx;
import com.athena.ckx.entity.paicfj.YunslxJiaof;
import com.athena.ckx.module.paicfj.service.YunslxService;
import com.athena.ckx.util.DBUtilRemove;

public class YunslxServiceTest extends AbstractCompomentTests{
	
	@Inject
	private YunslxService yunslxService;

	/**
	 * 保存
	 */
	@Test
	public void testSave() {
		String []sql = new String[1];
		sql[0] = "delete from ckx_yunslx where usercenter='UW' and yunslxbh='111' ";
		DBUtilRemove.remove(sql);
		
		Yunslx yunslx = new Yunslx();
		yunslx.setUsercenter("UW");
		yunslx.setYunslxbh("111");
		yunslx.setYunslxmc("运输路线111");
		yunslx.setZuidtqfysj(11.0);
		yunslx.setCreator("张三");
		yunslx.setEditor("张三");
		ArrayList<YunslxJiaof> list = new ArrayList<YunslxJiaof>();
		ArrayList<YunslxJiaof> list1 = new ArrayList<YunslxJiaof>();
		
		YunslxJiaof yunslxJiaof = new YunslxJiaof();
		yunslxJiaof.setXingqxh("1");
		yunslxJiaof.setJiaofsk("12:23");
		list.add(yunslxJiaof);
		yunslxService.save(yunslx, 1, list, list);
		yunslxService.save(yunslx, 2, list1, list1);
		
	}
	
}