package com.athena.ckx.module.cangk;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Test;

import com.athena.ckx.entity.cangk.Cangk;
import com.athena.ckx.entity.cangk.Zick;
import com.athena.ckx.module.cangk.service.CangkService;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core2.dao.database.DbUtils;
import com.toft.core3.container.annotation.Inject;

/***
 * 测试类测试仓库-增加，删除，修改，查询
 * 继承AbstractCompomentTests,该类会自动初始化SDC环境
 * @author denggq
 * @date 2012-2-18
 */
public class CangkTest extends AbstractCompomentTests{

	@Inject
	private CangkService service;
	
	@Test
	public void save(){
		//insert
		Cangk cangk1=new Cangk();
		cangk1.setUsercenter("UX");
		cangk1.setCangkbh("999");
		cangk1.setCangklx("2");
		cangk1.setDaoctqq(65.0);
		cangk1.setBiaos("1");
		ArrayList<Cangk> insert=new ArrayList<Cangk>();
		insert.add(cangk1);
		
		//update
		Cangk cangk2=new Cangk();
		cangk2.setUsercenter("UX");
		cangk2.setCangkbh("999");
		cangk2.setDaoctqq(80.0);
		ArrayList<Zick> update=new ArrayList<Zick>();
//		update.add(cangk2);

		//save(1-增加   2-修改)
//		service.save(cangk1,1,update, update, update, "root");
//		service.save(cangk2,2,update, update, update, "root");
		
		//失效
		service.doDelete(cangk2);
		
		//删除
		try{
			String sql = "delete from ckx_cangk where usercenter = 'UX' and cangkbh = '999' ";
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
		Cangk cangk=new Cangk();
		cangk.setUsercenter("UX");
		cangk.setCangkbh("001");
	}
	
	@Test
	public void query(){
		Cangk cangk=new Cangk();
		service.select(cangk);
	}
	
}
