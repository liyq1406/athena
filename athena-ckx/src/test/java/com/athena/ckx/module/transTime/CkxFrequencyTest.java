package com.athena.ckx.module.transTime;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.athena.authority.entity.LoginUser;
import com.athena.ckx.entity.transTime.CkxGongysChengysXiehzt;
import com.athena.ckx.module.transTime.service.CkxFrequencyService;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.ibatis.support.AbstractIBatisDao;

public class CkxFrequencyTest extends AbstractCompomentTests{
	@Inject
	private CkxFrequencyService service;
	@Inject 
	protected static AbstractIBatisDao baseDao;
	 @Test
	 public void test(){
		 //执行SQL脚本
		/* try {
			SqlFileExecutor.execute(baseDao.getConnection(),"");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		 
		 
		 
		LoginUser user =new LoginUser();
		user.setUsername("TEST");
		user.setUsercenter("UW");
		
		
		service.compute(user,"UW");
		
		
		List<CkxGongysChengysXiehzt> editList=new ArrayList<CkxGongysChengysXiehzt>();
		CkxGongysChengysXiehzt  x= new CkxGongysChengysXiehzt();
		x.setGcbh("M810010000");
		x.setXiehztbh("WH1");
	
		x.setBiaos("1");
		x.setShengxpc(9);
		
		editList.add(x);
		service.edit(editList, user);
		
		
		
		CkxGongysChengysXiehzt bean =new CkxGongysChengysXiehzt();
		bean.setUsercenter(user.getUsercenter());
		service.listGcbhORXiehzt(bean);
		
		
		
	 }
}
