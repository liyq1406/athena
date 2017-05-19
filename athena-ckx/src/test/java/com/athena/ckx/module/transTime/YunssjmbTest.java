package com.athena.ckx.module.transTime;



import java.util.ArrayList;


import org.junit.Test;

import com.athena.ckx.entity.transTime.CkxYunssjMb;
import com.athena.ckx.module.transTime.service.CkxYunssjMbService;
import com.athena.component.test.AbstractCompomentTests;
import com.athena.util.exception.ServiceException;
import com.toft.core3.container.annotation.Inject;

public class YunssjmbTest extends AbstractCompomentTests{
    @Inject
	private CkxYunssjMbService ckxYunssjMbService;
   
    @Test
    public void effect(){
    	ckxYunssjMbService.select_Temp(new CkxYunssjMb());
//    	ckxYunssjMbService.effect("张三");
    }
   
    @Test
    public void edit_Temp(){
    	
    	ckxYunssjMbService.insertTemp("UW");
    	ArrayList<CkxYunssjMb> list= new ArrayList<CkxYunssjMb>();
    	CkxYunssjMb bean1 = new CkxYunssjMb();
    	bean1.setUsercenter("UW");
    	bean1.setXiehztbh("W21");
    	bean1.setGcbh("M810010000");
    	bean1.setXuh(1.0);
    	bean1.setFacsj(-123.0);
    	list.add(bean1);
    	try {
			ckxYunssjMbService.edit_Temp(list, "张三");
		} catch (ServiceException e) {
			
		}
    }
    @Test
    public void save(){
    	ArrayList<CkxYunssjMb> list= new ArrayList<CkxYunssjMb>();
    	CkxYunssjMb bean1 = new CkxYunssjMb();
    	bean1.setUsercenter("UW");
    	bean1.setXiehztbh("W21");
    	bean1.setGcbh("M810010000");
    	bean1.setXuh(1.0);
    	bean1.setFacsj(-123.0);
    	bean1.setDaohsj(12.0);
    	list.add(bean1);
    	try {
			ckxYunssjMbService.save(list,list,list,"张三");
		} catch (Exception e) {
			
		}
    }
}
