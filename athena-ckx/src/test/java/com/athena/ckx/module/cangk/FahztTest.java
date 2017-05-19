package com.athena.ckx.module.cangk;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Test;

import com.athena.ckx.entity.cangk.Fahzt;
import com.athena.ckx.module.cangk.service.FahztService;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core2.dao.database.DbUtils;
import com.toft.core3.container.annotation.Inject;

/***
 * 测试类测试发货站台-增加，删除，修改，查询
 * 继承AbstractCompomentTests,该类会自动初始化SDC环境
 * @author denggq
 * @date 2012-2-17
 */
public class FahztTest extends AbstractCompomentTests{

	@Inject
	private FahztService service;
	
	@Test
	public void save(){
		//insert
		Fahzt fahzt1=new Fahzt();
		fahzt1.setUsercenter("UX");
		fahzt1.setFahztbh("999");
		fahzt1.setFahztmc("FHZT9");
		fahzt1.setBiaos("1");
		ArrayList<Fahzt> insert=new ArrayList<Fahzt>();
		insert.add(fahzt1);
		
		//update
		Fahzt fahzt2=new Fahzt();
		fahzt2.setUsercenter("UX");
		fahzt2.setFahztbh("999");
		fahzt2.setFahztmc("FHZTMOD");
		ArrayList<Fahzt> update=new ArrayList<Fahzt>();
		update.add(fahzt2);

		//save(逻辑删除)
		service.save(insert, update, update, "user001");
		
		//delete(physical)
		try{
			String sql = "delete from ckx_fahzt where usercenter = 'UX' and fahztbh = '999'   ";
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
	
}
