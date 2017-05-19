package com.athena.ckx.module.xuqjs;

import java.util.ArrayList;

import org.junit.Test;

import com.athena.ckx.entity.xuqjs.Baoz;
import com.athena.ckx.module.xuqjs.service.BaozService;
import com.athena.ckx.util.DBUtilRemove;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

/**
 * @description 包装
 * @author denggq
 * @date 2012-4-27
 */

@Component
public class BaozTest extends AbstractCompomentTests{
	
	@Inject
	private BaozService service;

	@Test
	public void test(){
		ArrayList<Baoz> insert = new ArrayList<Baoz>();
		Baoz baoz1 = new Baoz();
		baoz1.setBaozlx("00000");
		baoz1.setBaozmc("包装0");
		baoz1.setChangd(11);
		baoz1.setKuand(11);
		baoz1.setGaod(11);
		baoz1.setBaozzl(11.0);
		baoz1.setCaiz("1");
		baoz1.setShifhs("1");
		baoz1.setLeib("1");
		baoz1.setZhedgd(11);
		baoz1.setDuidcs(11);
		baoz1.setBaiffx("1");
		baoz1.setBiaos("1");
		insert.add(baoz1);
		
		ArrayList<Baoz> update = new ArrayList<Baoz>();
		Baoz baoz2 = new Baoz();
		baoz2.setBaozlx("00000");
		baoz2.setChangd(22);
		baoz2.setKuand(22);
		baoz2.setGaod(22);
		baoz2.setBaozzl(22.0);
		baoz2.setCaiz("2");
		baoz2.setShifhs("2");
		baoz2.setLeib("2");
		baoz2.setZhedgd(22);
		baoz2.setDuidcs(22);
		baoz2.setBaiffx("2");
		update.add(baoz2);
		
		logger.info("--------------test1 Baoz----------------");
		logger.info(service.save(insert, update, update, "12345"));
		logger.info("--------------test1 Baoz----------------");
		
		String[] args = new String[1];
		args[0]= "delete from DEV_CKX_TEST.ckx_baoz where baozlx = '00000'";
		DBUtilRemove.remove(args);
	}
	
	
	@Test
	public void test2(){
		ArrayList<Baoz> insert = new ArrayList<Baoz>();
		logger.info("--------------test2 Baoz----------------");
		logger.info(service.save(insert, insert, insert, "12345"));
		logger.info("--------------test2 Baoz----------------");
	}
	
}
