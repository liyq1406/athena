package com.athena.ckx.module.cangk;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Test;

import com.athena.ckx.entity.cangk.Xiehzt;
import com.athena.ckx.module.cangk.service.XiehztService;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core2.dao.database.DbUtils;
import com.toft.core3.container.annotation.Inject;

/***
 * 测试类测试卸货站台-增加，删除，修改，查询
 * @author denggq
 * @date 2012-2-17
 */
public class XiehztTest extends AbstractCompomentTests{

	@Inject
	private XiehztService service;
	
//	@Test
	public void save(){
		//insert
		Xiehzt xiehzt1=new Xiehzt();
		xiehzt1.setUsercenter("UX");
		xiehzt1.setXiehztbh("111111");
		xiehzt1.setXiehztmc("XHZT1");
		xiehzt1.setShjgsj(60);
		xiehzt1.setYunxtqdhsj(30);
		xiehzt1.setBiaos("1");
		ArrayList<Xiehzt> insert=new ArrayList<Xiehzt>();
		insert.add(xiehzt1);
		
		//update
		Xiehzt xiehzt2=new Xiehzt();
		xiehzt2.setUsercenter("UX");
		xiehzt2.setXiehztbh("111111");
		xiehzt2.setXiehztmc("XHZT1");
		xiehzt2.setShjgsj(90);
		xiehzt2.setYunxtqdhsj(60);
		ArrayList<Xiehzt> update=new ArrayList<Xiehzt>();
		update.add(xiehzt2);
		
		//save(逻辑删除)
//		service.save(insert, update, update, "user001");
		
		//delete(physical)
		try{
			String sql = "delete from ckx_xiehzt where usercenter = 'UX' and xiehztbh = '111111' and xiehztmc = 'XHZT1' ";
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
		Xiehzt xiehzt=new Xiehzt();
		xiehzt.setXiehztbh("111111");
		service.select(xiehzt);
	}
	
}
