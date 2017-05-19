package com.athena.ckx.module.xuqjs;

import org.junit.Test;

import com.athena.ckx.entity.xuqjs.Lingjck;
import com.athena.ckx.module.xuqjs.service.LingjckService;
import com.athena.ckx.util.DateTimeUtil;
import com.athena.component.test.AbstractCompomentTests;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

/**
 * @description 零件仓库设置
 * @author denggq
 * @date 2012-4-27
 */
@Component
public class LingjckTest extends AbstractCompomentTests{
	
	@Inject
	private LingjckService service;

	@Test
	public void save(){
		Lingjck bean = new Lingjck();
		bean.setUsercenter("QQ");
		bean.setLingjbh("0000000000");  
		bean.setCangkbh("111"); 
		bean.setZickbh("111");  
		bean.setXiehztbh("111");    
		bean.setAnqkcts(1.0);  
		bean.setAnqkcsl(1.0);    
		bean.setZuidkcts(1.0);   
		bean.setZuidkcsl(1.0);    
		bean.setDingdbdzl(1.0);    
		bean.setYijfzl(1.0);    
		bean.setXittzz(1.0);    
		bean.setDanqkbh("111");    
		bean.setDanqkw("111111");    
		bean.setZuidsx(2);    
		bean.setZuixxx(1);    
		bean.setJistzz(1.0);   
		bean.setShengxsj(DateTimeUtil.getCurrDate());    
		bean.setZuixqdl(1.0);   
		bean.setShifxlh("1");    
		bean.setBeiykbh("111");    
		bean.setDingzkw("111111");    
		bean.setUsbzlx("11111");    
		bean.setUsbzrl(1.0);    
		bean.setUclx("11111");    
		bean.setUcrl(1.0);   
		bean.setFifo("1");    
		bean.setDingdzzzl(1.0);    
		bean.setZidfhbz("1"); 
		bean.setYuanckbh("000");
		
		logger.info("--------------test1 add Lingjck----------------");
		service.save(bean, 1,"12345");
		logger.info("--------------test1 add Lingjck----------------");
		
		Lingjck bean2 = new Lingjck();
		bean2.setUsercenter("QQ");
		bean2.setLingjbh("0000000000");  
		bean2.setCangkbh("111"); 
		bean2.setZickbh("222");  
		bean2.setXiehztbh("222");    
		bean2.setAnqkcts(2.0);  
		bean2.setAnqkcsl(2.0);    
		bean2.setZuidkcts(2.0);   
		bean2.setZuidkcsl(2.0);    
		bean2.setDingdbdzl(2.0);    
		bean2.setYijfzl(2.0);    
		bean2.setXittzz(2.0);    
		bean2.setDanqkbh("222");    
		bean2.setDanqkw("222222");    
		bean2.setZuidsx(4);    
		bean2.setZuixxx(3);    
		bean2.setJistzz(2.0);   
		bean2.setShengxsj(DateTimeUtil.getCurrDate());    
		bean2.setZuixqdl(2.0);   
		bean2.setShifxlh("0");    
		bean2.setBeiykbh("222");    
		bean2.setDingzkw("222222");    
		bean2.setUsbzlx("22222");    
		bean2.setUsbzrl(2.0);    
		bean2.setUclx("22222");    
		bean2.setUcrl(2.0);   
		bean2.setFifo("0");    
		bean2.setDingdzzzl(2.0);    
		bean2.setZidfhbz("0"); 
		bean.setYuanckbh("001");
		
		logger.info("--------------test2 update Lingjck----------------");
		service.save(bean2,2, "12345");
		service.doDelete(bean2);
		logger.info("--------------test2 update Lingjck----------------");
		
		service.doDelete(bean2);
	}
	
}
