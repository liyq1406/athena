package com.athena.ckx.module.xuqjs;

import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.athena.ckx.entity.xuqjs.Usercenter;
import com.athena.ckx.module.xuqjs.service.UsercenterService;
import com.athena.component.test.AbstractCompomentTests;
import com.athena.module.athena.SelectService;
import com.toft.core2.dao.database.DbUtils;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

/**
 * @description 用户中心
 * @author denggq
 * @date 2012-4-24
 */

@Component
public class UsercenterTest extends AbstractCompomentTests{
	
	@Inject
	private SelectService selectService;
	@Inject
	private UsercenterService service;
	
	//日志
	private static final Logger logger = Logger.getLogger(UsercenterTest.class);
	

	@Test
	public void test(){
		
		selectService.selectMap("queryUserCenterMap", "select.queryUserCenterMap", "用户中心");
		
		logger.info("test");
		
		ArrayList<Usercenter> insert = new ArrayList<Usercenter>();
		ArrayList<Usercenter> edit = new ArrayList<Usercenter>();
		
		try{
			Usercenter u1 = new Usercenter();
			u1.setUsercenter("000");
			u1.setCentername("000");
			u1.setBiaos("1");
			insert.add(u1);
			
			Usercenter u2 = new Usercenter();
			u2.setUsercenter("000");
			u2.setCentername("001");
			u2.setBiaos("1");
			edit.add(u2);
			logger.info("before save");
			service.save(insert, edit, edit, "12345");
			logger.info("after save");
			//删除
			String sql = "delete from DEV_CKX_TEST.ckx_usercenter where usercenter = '000' and centername = '001' ";
			DbUtils.execute(sql, DbUtils.getConnection("1"));
			DbUtils.getConnection("1").commit();
		}catch( Exception e){
			try {
				DbUtils.getConnection("1").rollback();
			} catch (SQLException e1) {
				logger.error(e1.getMessage());
				throw new RuntimeException(e1.getMessage());
			}
			logger.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}
	
}
