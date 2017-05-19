package com.athena.xqjs.kdorder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.xqjs.entity.ilorder.Maoxq;
import com.athena.xqjs.module.ilorder.service.MaoxqService;
import com.toft.core3.container.annotation.Inject;
/**
 * 
 * @author 李智
 *
 */
@TestData(locations = {"classpath:testData/kdOrder/kdOrderJs.xls"})
public class KdOrderTest extends AbstractCompomentTests{
	@Inject
	private MaoxqService maoxqService;
	
	@Test
	public void testQueryMaoxqByXqlx(){
		Map<String,String> map = new HashMap<String, String>();
		//
		map.put("xuqlx","Z");
		map.put("xuqly", "DKS");
		
		Map resultmap = maoxqService.queryMaoxqByXqlx(new Maoxq(), map);
		
		List<Maoxq> list = (ArrayList<Maoxq>)resultmap.get("rows");
		Maoxq maoxq = list.get(0);
		//java.lang.AssertionError: 
//		expected:<Maoxq [beiz=null, xuqcfsj=0012-03-09, xuqlx=Z, shengxbz=1, xuqbc=A003, xuqly=DKS]> but was:
//			     <Maoxq [beiz=null, xuqcfsj=2012-03-09, xuqlx=Z, shengxbz=1, xuqbc=A003, xuqly=DKS]>
	
		Maoxq assertMaoxq = new Maoxq();
		assertMaoxq.setXuqcfsj("2012-03-09");
		assertMaoxq.setXuqlx("Z");
		assertMaoxq.setShengxbz("1");
		assertMaoxq.setXuqbc("A003");
		assertMaoxq.setXuqly("DKS");
		assertMaoxq.setBeiz(null);
		
		org.junit.Assert.assertEquals(maoxq, assertMaoxq);
	}
}
