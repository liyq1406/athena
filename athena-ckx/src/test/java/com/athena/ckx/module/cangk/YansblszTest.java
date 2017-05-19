package com.athena.ckx.module.cangk;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Test;

import com.athena.ckx.entity.cangk.Yansblsz;
import com.athena.ckx.module.cangk.service.YansblszService;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core2.dao.database.DbUtils;
import com.toft.core3.container.annotation.Inject;

/***
 * 测试类测试零件类型验收比例设置-增加，删除，修改，查询
 * @author denggq
 * @date 2012-2-17
 */
public class YansblszTest extends AbstractCompomentTests{

	@Inject
	private YansblszService service;
	
	@Test
	public void save(){
		//insert
		Yansblsz yansblsz1=new Yansblsz();
		yansblsz1.setUsercenter("UX");
		yansblsz1.setLingjlx("A");
		yansblsz1.setYansxbh("999");
		yansblsz1.setYansbl(50);
		ArrayList<Yansblsz> insert=new ArrayList<Yansblsz>();
		insert.add(yansblsz1);
		
		//update
		Yansblsz yansblsz2=new Yansblsz();
		yansblsz2.setUsercenter("UX");
		yansblsz2.setLingjlx("A");
		yansblsz2.setYansxbh("999");
		yansblsz2.setYansbl(80);
		ArrayList<Yansblsz> update=new ArrayList<Yansblsz>();
		update.add(yansblsz2);
		
		//save(逻辑删除)
		service.save(insert, update, update, "user001");
		
		//delete(physical)
		try{
			String sql = "delete from ckx_yansblsz where usercenter = 'UX' and lingjlx = 'A' and yansxbh = '999' ";
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
		Yansblsz yansblsz=new Yansblsz();
		service.select(yansblsz);
	}
	
}
