package com.athena.ckx.module.cangk;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Test;

import com.athena.ckx.entity.cangk.Xingzysts;
import com.athena.ckx.module.cangk.service.XingzystsService;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core2.dao.database.DbUtils;
import com.toft.core3.container.annotation.Inject;

/***
 * 测试类测试行政验收提示验收项-增加，删除，修改，查询
 * @author denggq
 * @date 2012-2-17
 */
public class XingzystsTest extends AbstractCompomentTests{

	@Inject
	private XingzystsService service;
	
	@Test
	public void save(){
		//insert
		Xingzysts xzysts1=new Xingzysts();
		xzysts1.setYansxbh("999");
		xzysts1.setYansxsm("验收项1");
		xzysts1.setBiaos("1");
		ArrayList<Xingzysts> insert=new ArrayList<Xingzysts>();
		insert.add(xzysts1);
		
		//update
		Xingzysts xzysts2=new Xingzysts();
		xzysts2.setYansxbh("999");
		xzysts2.setYansxsm("验收项MOD");
		ArrayList<Xingzysts> update=new ArrayList<Xingzysts>();
		update.add(xzysts2);
		
		//save(逻辑删除)
		service.save(insert, update, update, "user001");
		
		//delete(physical)
		try{
			String sql = "delete from ckx_xingzysts where yansxbh = '999' ";
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
	public void query(){
		Xingzysts xzysts=new Xingzysts();
		xzysts.setYansxbh("999");
		service.select(xzysts);
	}
	
}
