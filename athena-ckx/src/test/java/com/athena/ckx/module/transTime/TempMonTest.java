package com.athena.ckx.module.transTime;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

import com.athena.authority.entity.LoginUser;
import com.athena.ckx.entity.transTime.CkxTempMon;
import com.athena.ckx.module.transTime.service.CkxTempMonService;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Inject;

public class TempMonTest extends AbstractCompomentTests{

    
    @Inject
    private CkxTempMonService tempMon;

    
    @Test
    public void insertsJis(){
    	ArrayList<CkxTempMon> list = new ArrayList<CkxTempMon>();
    	CkxTempMon ckx=new CkxTempMon();
    	ckx.setUsercenter("UW");
    	ckx.setXiehztbh("WH0006");
    	ckx.setDingszt("2");
    	list.add(ckx);
    	
    	LoginUser loginuser = new LoginUser();
    	loginuser.setUsername("张三");
    	loginuser.setUsercenter("UW");
    	tempMon.save(list, new ArrayList<CkxTempMon>(),"2", loginuser,new HashMap());
    }
//    @Test
    public void dingsjs(){
    	ArrayList<CkxTempMon> list = new ArrayList<CkxTempMon>();
    	CkxTempMon ckx=new CkxTempMon();
    	ckx.setXiehztbh("W21");
    	ckx.setDingszt("1");
    	list.add(ckx);
    	CkxTempMon ckx1=new CkxTempMon();
    	ckx1.setXiehztbh("WH1");
    	ckx1.setDingszt("1");
    	list.add(ckx1);
    	ArrayList<CkxTempMon> list1 = new ArrayList<CkxTempMon>();    	 
    	LoginUser loginuser = new LoginUser();
    	loginuser.setUsername("张三");
    	loginuser.setUsercenter("UW");
    	tempMon.save(list1, list,"1", loginuser,new HashMap());
    	tempMon.save(list, list1,"1", loginuser,new HashMap());
    }
}
