package com.athena.ckx.module.cangk;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Test;

import com.athena.ckx.entity.cangk.Cangk;
import com.athena.ckx.entity.cangk.Zick;
import com.athena.ckx.module.cangk.service.ZickService;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core2.dao.database.DbUtils;
import com.toft.core3.container.annotation.Inject;

/***
 * 测试类测试子仓库-增加，删除，修改，查询
 * 继承AbstractCompomentTests,该类会自动初始化SDC环境
 * @author denggq
 * @date 2012-2-18
 */
public class ZickTest extends AbstractCompomentTests{

	@Inject
	private ZickService service;
	
	@Test
	public void save(){
		
		Cangk cangk = new Cangk();
		cangk.setUsercenter("UX");
		cangk.setCangkbh("999");
		
		//insert
		Zick zick1=new Zick();
		zick1.setZickbh("999");
		zick1.setGuanlyzbh("999");
		zick1.setZhantbh("999999");
		zick1.setShifelgl("1");
		zick1.setBaohd(60);
		zick1.setBiaos("1");
		ArrayList<Zick> insert=new ArrayList<Zick>();
		insert.add(zick1);
		
		//update
		Zick zick2=new Zick();
		zick2.setZickbh("999");
		zick2.setBaohd(77);
		zick2.setGuanlyzbh("10101");
		zick2.setZhantbh("555555");
		ArrayList<Zick> update=new ArrayList<Zick>();
		update.add(zick2);

		//save(逻辑删除)
//		service.save(insert, update, update, "user001",cangk);
		
		//delete(physical)
		try{
			String sql = "delete from ckx_zick where usercenter = 'UX' and cangkbh = '999' and zickbh = '999' ";
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
		Zick zick=new Zick();
		zick.setUsercenter("UX");
		zick.setZickbh("999");
		service.select(zick);
	}
	
}
