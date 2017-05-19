package com.athena.ckx.module.cangk;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Test;

import com.athena.ckx.entity.cangk.Rongqc;
import com.athena.ckx.module.cangk.service.RongqcService;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core2.dao.database.DbUtils;
import com.toft.core3.container.annotation.Inject;

/***
 * 测试类测试容器场-增加，删除，修改，查询
 * @author denggq
 * @date 2012-2-17
 */
public class RongqcTest extends AbstractCompomentTests{

	@Inject
	private RongqcService service;
	
	@Test
	public void save(){
		//insert
		Rongqc kongrqc1=new Rongqc();
		kongrqc1.setUsercenter("UX");
		kongrqc1.setRongqcbh("1111111111");
		kongrqc1.setMiaos("KRQC");
		kongrqc1.setUsercenter("UX");
		kongrqc1.setRongqcbh("1111111111");
		kongrqc1.setBiaos("1");
		ArrayList<Rongqc> insert=new ArrayList<Rongqc>();
		insert.add(kongrqc1);
		
		//update
		Rongqc kongrqc2=new Rongqc();
		kongrqc2.setUsercenter("UX");
		kongrqc2.setRongqcbh("1111111111");
		kongrqc2.setMiaos("KRQCMOD");
		ArrayList<Rongqc> update=new ArrayList<Rongqc>();
		update.add(kongrqc2);
		
		//save(逻辑删除)
		//service.save(insert, update, update, "user001");
		
		//delete(physical)
		try{
			String sql = "delete from ckx_rongqc where usercenter = 'UX' and Rongqcbh = '1111111111' ";
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
		Rongqc rongqc=new Rongqc();
		rongqc.setUsercenter("UX");
		rongqc.setRongqcbh("1111111111");
		service.select(rongqc);
	}
	
}
