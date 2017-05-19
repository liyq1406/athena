package com.athena.ckx.module.cangk;


import java.sql.SQLException;

import org.junit.Test;

import com.athena.ckx.module.cangk.service.YansblljInterface;
import com.athena.ckx.util.DBUtilRemove;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core2.dao.database.DbUtils;
import com.toft.core3.container.annotation.Inject;

/***
 * 测试类测试零件供应商接口
 * 继承AbstractCompomentTests,该类会自动初始化SDC环境
 * @author denggq
 * @date 2012-2-17
 */
public class YansblljInterfaceTest extends AbstractCompomentTests{

	@Inject
	private YansblljInterface service;
	
	@Test
	public void test(){
		
		try {
			
			//生成零件验收比例
			service.commit("UX","1111111111","GYS999","root1");
			//service.commit("UW","9681755780","M301280000","root1");
		
			//物理删除
			String[] args = new String[1];
			args[0]= "delete from ckx_yansbllj where usercenter = 'UX' and lingjbh = '1111111111' and gongysbh = 'GYS999' ";
			DBUtilRemove.remove(args);
			
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
