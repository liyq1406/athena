package com.athena.ckx.module.carry;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.athena.authority.entity.LoginUser;
import com.athena.ckx.entity.carry.CkxInnerPathAndModle;
import com.athena.ckx.entity.xuqjs.CkxLingjxhd;
import com.athena.ckx.module.carry.service.CkxInnerAndModleService;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Inject;

public class CkxInnerAndModleTest extends AbstractCompomentTests {
	@Inject
	private CkxInnerAndModleService service;

	
	 @Test
	 public void test(){
		 LoginUser user =new LoginUser();
			user.setUsername("KONG");
		 
		
		 
		 
		 CkxInnerPathAndModle inner=new CkxInnerPathAndModle();
		 inner.setXianbk("T01");
		 //增加第一条数据
		 System.out.println(service.addCkxInnerPath(inner, getList(1), user));
		 
		 inner.setDinghk("T02");
		 
		 //增加第二条数据
		 System.out.println(service.addCkxInnerPath(inner, getList(2), user));
		 //修改其中一条
		 System.out.println(service.saveCkxInnerPath(getInner("F0001"), user));
	 
		 ArrayList<CkxInnerPathAndModle> ckxInnerPathAndModle = new ArrayList<CkxInnerPathAndModle>();
		 ckxInnerPathAndModle.add(inner);
		 //删除两条
		 System.out.println(service.deleteCkxInnerPath(ckxInnerPathAndModle));
		 System.out.println(service.deleteCkxInnerPath(ckxInnerPathAndModle));
	 }
	 
	 
	 /**
	  * 获取零件消耗点集合
	  * @return
	  */
	 private List<CkxLingjxhd> getList(int i){
		 List<CkxLingjxhd> list=new ArrayList<CkxLingjxhd>();
		 if(i==1){
			CkxLingjxhd lx=new CkxLingjxhd();
			 lx.setLingjbh("TEST000001");
			 lx.setUsercenter("TS");
			 lx.setFenpqbh("F0002");
			 list.add(lx);
		 }else{
			 CkxLingjxhd lx=new CkxLingjxhd();
			 lx.setLingjbh("TEST000001");
			 lx.setUsercenter("TS");
			 lx.setFenpqbh("F0001");
			 list.add(lx);
		 }
		
		 return list;
	 }
	 
	 
	 /**
	  * 获取内部物流实体类
	  * @return
	  */
	 private CkxInnerPathAndModle getInner(String f){
		 CkxInnerPathAndModle inner=new CkxInnerPathAndModle();
		 inner.setUsercenter("TS");
		 inner.setLingjbh("TEST000001");
		 inner.setFenpqh(f);
		 inner.setXianbk1("T01");//原线边库
		 inner.setXianbk("T11");//新线边库
		 inner.setDinghk("T02");
		 inner.setZhidgys("G000000002");
		 return inner;
	 }
}
