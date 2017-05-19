package com.athena.ckx.module.cangk;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Test;

import com.athena.ckx.entity.cangk.Danjdy;
import com.athena.ckx.entity.cangk.Kehczm;
import com.athena.ckx.module.cangk.service.KehczmService;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core2.dao.database.DbUtils;
import com.toft.core3.container.annotation.Inject;

/***
 * 测试类测试仓库-增加，删除，修改，查询
 * 继承AbstractCompomentTests,该类会自动初始化SDC环境
 * @author denggq
 * @date 2012-2-18
 */
public class KehczmTest extends AbstractCompomentTests{

	@Inject
	private KehczmService service;
	
	@Test
	public void save(){
		//insert
		Kehczm kehczm1=new Kehczm();
		kehczm1.setUsercenter("UX");
		kehczm1.setKehbh("999");
		kehczm1.setCaozm("A36");
		kehczm1.setZhangh("1234567");
		kehczm1.setBiaos("1");
		ArrayList<Danjdy> insert=new ArrayList<Danjdy>();
		
		
		//update
		Kehczm kehczm2=new Kehczm();
		kehczm2.setUsercenter("UX");
		kehczm2.setKehbh("999");
		kehczm2.setCaozm("A35");
		kehczm2.setZhangh("99999");
		
		

		//save(1-增加   2-修改)
		service.save(kehczm1,1,insert, insert, insert, "root");
		service.save(kehczm2,2,insert, insert, insert, "root");
		
		//失效
		service.doDelete(kehczm2);
		
		//删除
		try{
			String sql = "delete from ckx_kehczm where usercenter = 'UX' and kehbh = '999' ";
			DbUtils.execute(sql, DbUtils.getConnection("1"));
			DbUtils.getConnection("1").commit();
		}catch( Exception e){
			try {
				DbUtils.getConnection("1").rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}
	
	@Test
	public void doDelete(){
		Kehczm kehczm=new Kehczm();
		kehczm.setUsercenter("UX");
		kehczm.setKehbh("005");
		service.doDelete(kehczm);
	}
	
	
	@Test
	public void query(){
		Kehczm kehczm=new Kehczm();
		service.select(kehczm);
	}
	
}
