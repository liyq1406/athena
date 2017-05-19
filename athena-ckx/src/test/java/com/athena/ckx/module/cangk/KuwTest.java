package com.athena.ckx.module.cangk;

import java.util.ArrayList;

import org.junit.Test;

import com.athena.ckx.entity.cangk.Kuw;
import com.athena.ckx.module.cangk.service.KuwService;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Inject;

/***
 * 测试类测试库位-增加,查询
 * @author denggq
 * @date 2012-2-17
 */
public class KuwTest extends AbstractCompomentTests{

	@Inject
	private KuwService service;
	
	@Test
	public void insert(){
		//insert
		Kuw kuw1=new Kuw();
		kuw1.setUsercenter("UX");
		kuw1.setCangkbh("999");
		kuw1.setZickbh("999");
		kuw1.setMian("A9");
		kuw1.setCeng("N9");
		kuw1.setQislh("8");
		kuw1.setJieslh("12");
		kuw1.setKuwdjbh("99999");
		ArrayList<Kuw> insert=new ArrayList<Kuw>();
		insert.add(kuw1);
		
		//insert
		service.inserts(insert, kuw1, "user001");
		
		//物理删除
		doDelete();
	}
	
	@Test
	public void query(){
		Kuw kuw=new Kuw();
		service.select(kuw);
	}
	
	//物理删除  
	public void doDelete(){
		Kuw kuw1=new Kuw();
		kuw1.setUsercenter("UX");
		kuw1.setCangkbh("999");
		kuw1.setZickbh("999");
		for(int i=8;i<13;i++){
			if(i<10){
				kuw1.setKuwbh("A90"+i+"N9");
			}else{
				kuw1.setKuwbh("A9"+i+"N9");
			}
			kuw1.setKuwdjbh("99999");
			service.doDelete(kuw1);
		}
	}
	
}
