package com.athena.xqjs.module.ppl;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.component.test.TestData;
import com.athena.xqjs.entity.ilorder.Maoxq;
import com.athena.xqjs.entity.ilorder.Maoxqmx;
import com.athena.xqjs.module.common.Const;
import com.athena.xqjs.module.ilorder.service.MaoxqService;
import com.athena.xqjs.module.ilorder.service.MaoxqmxService;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.ibatis.support.AbstractIBatisDao;
/**
 * PPL预告毛需求测试用例
 * @author Xiahui
 * @CreateTime 2012-2-13
 */
@TestData(locations = {"classpath:testdata/xqjs/pplmxq.xls"})
public class MaoxqTest extends AbstractCompomentTests{
	@Inject
	private MaoxqService maoxqService;
	@Inject
	private MaoxqmxService maoxqmxService;
	@Inject
	private  AbstractIBatisDao baseDao;
	private  Map<String ,String> params;
	private  Maoxq  maoxq;

    @Rule
     public   ExternalResource resource= new ExternalResource(){
    	@Override
    	protected void before() throws Throwable{
    		maoxq=new  Maoxq();
    		//数据准备
    		params=new HashMap<String ,String>();
    	}
	};
	/**
	 * 测试查询所有版次的毛需求信息
	 * @author  Xiahui
	 * @date    2012-2-13 
	 */
   @Test
	public  void   testSelectMaoxq(){
	    //查询毛需求
		 Map<String, Object> resultMap= maoxqService.select(maoxq, params);
		 
		 ArrayList<Object>  ls = (ArrayList<Object>) resultMap.get("rows");
		 //获取查询结果，判断结果
        assertEquals("170757",((Maoxq)ls.get(0)).getXuqbc());

	}
	/**
	 * 通过页面上的选择的制作PPL类型获取及毛需求的版次得到毛需求明细的信息
	 *@author  Xiahui
	 *@date    2012-2-13 
	 */
	@Test
	//@TestData(locations = {"classpath:testdata/xqjs/pplmxq.xls"})
	public  void   testSelectMaoxqmx(){
		//设置参数
		params.put("xuqbc", "94916");
		params.put(Const.PARAMS_PPL_LINGJLEIXING, "IL");
		 Map<String, Object> resultMap=maoxqmxService.select(maoxq, params);
		 ArrayList<Object>  ls = (ArrayList<Object>) resultMap.get("rows");
		 //获取查询结果，判断结果
        assertEquals("94916",((Maoxqmx)ls.get(0)).getXuqbc());

		
	}
}
