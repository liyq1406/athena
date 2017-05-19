package com.athena.xqjs.ilorder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.xqjs.entity.ilorder.Maoxq;
import com.athena.xqjs.entity.ilorder.Ziykzb;
import com.athena.xqjs.module.ilorder.service.MaoxqService;
import com.toft.core3.container.annotation.Inject;
/**
 * 
 * @author 李智
 *
 */
@TestData(locations = {"classpath:testData/ilOrder/IlorderJs.xls"})
public class IlOrderTest extends AbstractCompomentTests{
	@Inject
	private MaoxqService maoxqService;
	
	@Test
	public void testOrderservice(){
		Map<String,String> map = new HashMap<String, String>();
		//
		map.put("xuqlx0","PP");
		map.put("zhizlx","97W");
		//map.put("xuqlx","Z");
		//map.put("xuqlx","R");
		//map.put("xuqlx","AX");
		
		Map resultmap = maoxqService.queryMaoxqByXqlx(new Maoxq(), map);
		List<Maoxq> list = (ArrayList<Maoxq>)resultmap.get("rows");
		Maoxq maoxq = list.get(0);
		Maoxq assertMaoxq = new Maoxq();
		assertMaoxq.setXuqcfsj("2012-03-07");
		assertMaoxq.setXuqlx("ZQ");
		assertMaoxq.setShengxbz("1");
		assertMaoxq.setXuqbc("A001");
		assertMaoxq.setXuqly("DIP");
		
		org.junit.Assert.assertEquals(maoxq, assertMaoxq);
	}
	
	@Test
	public void testOrderservice2(){
		Map resultmap = maoxqService.queryMaoxqByXqlx(new Maoxq(), null);
		List<Maoxq> list = (ArrayList<Maoxq>)resultmap.get("rows");
		Maoxq maoxq = list.get(0);
		Maoxq assertMaoxq = new Maoxq();
		assertMaoxq.setXuqcfsj("2012-03-07");
		assertMaoxq.setXuqlx("ZQ");
		assertMaoxq.setShengxbz("1");
		assertMaoxq.setXuqbc("A001");
		assertMaoxq.setXuqly("DIP");
		
		org.junit.Assert.assertEquals(maoxq, assertMaoxq);
	}
	
	@Test
	public void testOrderservice3(){
		Map<String,String> map = new HashMap<String, String>();
		List<Ziykzb> list = maoxqService.queryZiykzb(map);
		
		Ziykzb ziykzb = list.get(0);
		String ziyhqrq = ziykzb.getZiyhqrq();
		String assertZiyhqrq = "2012-02-06";
		
		org.junit.Assert.assertEquals(ziyhqrq, assertZiyhqrq);
	}
	
//	@Test
//	public void testOrderservice4(){
//		Map<String,String> map = new HashMap<String, String>();
//		maoxqService.queryjislx(map);
//	}
}
