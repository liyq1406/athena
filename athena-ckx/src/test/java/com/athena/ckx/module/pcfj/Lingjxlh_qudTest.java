package com.athena.ckx.module.pcfj;

import java.util.ArrayList;

import org.junit.Test;

import com.athena.ckx.entity.paicfj.Ckx_lingjxlh_qud;

import com.athena.ckx.util.DBUtilRemove;
import com.athena.component.test.AbstractCompomentTests;


public class Lingjxlh_qudTest extends AbstractCompomentTests {

//	@Inject
//	private Ckx_lingjxlh_qudService ckx_lingjxlh_qudService;
	@Test
	public void save(){
//		ArrayList<Ckx_lingjxlh_qud> list = new ArrayList<Ckx_lingjxlh_qud>();
//		ckx_lingjxlh_qudService.save(list, list, list, "张三");
	}
	@Test
	public void save1(){
		String[] sql = new String[3];
		sql[0] = "delete ckx_lingjxlh_qud where usercenter='UL' and lingjbh='lj0000001'";
		sql[1] = "delete ckx_lingjxlh_qud where usercenter='UL' and lingjbh='lj0000002'";
		sql[2] = "delete ckx_lingjxlh_qud where usercenter='UL' and lingjbh='lj0000003'";
		DBUtilRemove.remove(sql);
		
		ArrayList<Ckx_lingjxlh_qud> list = new ArrayList<Ckx_lingjxlh_qud>();
		
		Ckx_lingjxlh_qud lingj = new Ckx_lingjxlh_qud();
		lingj.setUsercenter("UL");
		lingj.setLingjbh("lj0000001");
		lingj.setXulhqz("qz001");
		list.add(lingj);
		
		Ckx_lingjxlh_qud lingj1 = new Ckx_lingjxlh_qud();
		lingj1.setUsercenter("UL");
		lingj1.setLingjbh("lj0000002");
		lingj1.setXulhqz("qz001");
		list.add(lingj1);
		
		Ckx_lingjxlh_qud lingj2 = new Ckx_lingjxlh_qud();
		lingj2.setUsercenter("UL");
		lingj2.setLingjbh("lj0000003");
		lingj2.setXulhqz("qz001");
		list.add(lingj2);
		
//		ckx_lingjxlh_qudService.save(list, list, list, "张三");
	
	}
}
