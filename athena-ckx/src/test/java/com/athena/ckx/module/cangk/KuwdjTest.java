package com.athena.ckx.module.cangk;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Test;

import com.athena.ckx.entity.cangk.Kuwdj;
import com.athena.ckx.module.cangk.service.KuwdjService;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core2.dao.database.DbUtils;
import com.toft.core3.container.annotation.Inject;

/***
 * 测试类测试库位等级-增加，删除，修改，查询
 * @author denggq
 * @date 2012-2-17
 */
public class KuwdjTest extends AbstractCompomentTests{

	@Inject
	private KuwdjService service;
	
	@Test
	public void save(){
		//insert
		Kuwdj kuwdj1=new Kuwdj();
		kuwdj1.setUsercenter("UX");
		kuwdj1.setCangkbh("999");
		kuwdj1.setKuwdjbh("99999");
		kuwdj1.setChang(55.55);
		kuwdj1.setKuan(55.55);
		kuwdj1.setGao(55.55);
		kuwdj1.setBiaos("1");
		ArrayList<Kuwdj> insert=new ArrayList<Kuwdj>();
		insert.add(kuwdj1);
		
		//update
		Kuwdj kuwdj2=new Kuwdj();
		kuwdj2.setUsercenter("UX");
		kuwdj2.setCangkbh("999");
		kuwdj2.setKuwdjbh("99999");
		kuwdj2.setChang(99.99);
		kuwdj2.setKuan(99.99);
		kuwdj2.setGao(99.99);
		ArrayList<Kuwdj> update=new ArrayList<Kuwdj>();
		update.add(kuwdj2);
		
		//save(logical)
		service.save(insert, update, update, "user001");
		
		//delete(physical)
		try{
			String sql = "delete from ckx_kuwdj where usercenter = 'UX' and cangkbh = '999' and kuwdjbh = '99999' ";
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
		Kuwdj kuwdj=new Kuwdj();
		service.select(kuwdj);
	}
	
}
