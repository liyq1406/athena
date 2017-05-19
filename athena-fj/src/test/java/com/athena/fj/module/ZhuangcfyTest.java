/**
 * 
 */
package com.athena.fj.module;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.db.ConstantDbCode;
import com.athena.fj.entity.Zhuangcfy;
import com.athena.fj.entity.Zhuangcmy;
import com.athena.fj.interfaces.MyBaseDao;
import com.athena.fj.module.service.ZhuangcfyService;
import com.athena.print.Constants;
import com.athena.util.date.DateUtil;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.ibatis.support.AbstractIBatisDao;

/**
 * 装车发运测试用例
 * @author 贺志国
 * @CreateTime 2012-2-4
 */
//@TestData(locations = {"classpath:testdata/fj/zhuangc.xls"})
public class ZhuangcfyTest extends AbstractCompomentTests {

	@Inject
	private ZhuangcfyService zhuangcfyService;
	@Inject
	private AbstractIBatisDao baseDao;
	private Map<String,String> params;
	 @Rule  
	    public ExternalResource resource= new ExternalResource() {  
	        @Override  
	        protected void before() throws Throwable {  
	    		params = new HashMap<String,String>();
	    		params.put("usercenter", "UW");
	    		params.put("uch", "UC2012782101");
	    		params.put("peizdh", "PW0001");
	    		params.put("zhuangcdh", "zcd123457");
	        };   
	    }; 
	/**
	 * 配载单下的UC标签记录查询，单条记录
	 */
	//@Test
	public void testQueryUAInfo(){
		List<Map<String,String>> uaList  = zhuangcfyService.queryUCInfo(params);
		assertEquals("UC2012782101",uaList.get(0).get("UCH"));
		assertEquals("US30392308",uaList.get(0).get("USH"));
		assertEquals("PW0001",uaList.get(0).get("PEIZDH"));
		assertEquals("201112208",uaList.get(0).get("YAOHLH"));
		assertEquals("001",uaList.get(0).get("CANGKBH"));
		assertEquals("7903233014",uaList.get(0).get("LINGJBH"));
		assertEquals("23201",uaList.get(0).get("UAXH"));
		assertEquals("kh01",uaList.get(0).get("KEH"));
		assertEquals("u20392",uaList.get(0).get("XIEHD"));
	}
	
	/**
	 * 获取UC列表，查询关键字为in
	 */
	//@Test
	public void testQueryUCList(){
		Map<String,String> UCHMap = new HashMap<String,String>();
		UCHMap.put("uch", "'UC2012782101','UC2012782102'");
		List<Zhuangcmy>  zhuangcmyList =zhuangcfyService.queryUCList(UCHMap);
//		assertEquals("UC2012782101",zhuangcmyList.get(0).getUch());
		assertEquals("bhl001",zhuangcmyList.get(0).getBeihlh());
//		assertEquals("UC2012782102",zhuangcmyList.get(1).getUch());
		assertEquals("bhl001",zhuangcmyList.get(1).getBeihlh());
	}
	
	/**
	 * 查询配载单车牌，条件为配载单号
	 */
	//@Test
	public void testQueryPeizdChep(){
		List<Map<String,String>> peizdList = zhuangcfyService.queryPeizdChep(params);
		assertEquals("鄂A10005",peizdList.get(0).get("CHEP"));
		assertEquals("DC",peizdList.get(0).get("JIHCX"));
		assertEquals("CYS001",peizdList.get(0).get("YUNSSBM"));
	}
	
	/**
	 * 查询UC表供应商编码
	 */
	//@Test
	public void testQueryUCGongysdm(){
		String gongysdm = zhuangcfyService.queryUCGongysdm("UC2012782101");
		assertEquals("gys01",gongysdm);
	}
	
	/**
	 * 装车和装车明细表中插入数据
	 */
	//@Test
	public void testInsertZhuangc(){
		Zhuangcfy zhuangcfyBean = new Zhuangcfy();
		//zhuangcfyBean.setZhuangcdh("zcd123457");//需要规则生成,在service中生成
		zhuangcfyBean.setUsercenter("UW");
		zhuangcfyBean.setPeizdh("PW0001");
		zhuangcfyBean.setYunssbm("yun01");
		zhuangcfyBean.setJihcx("cx001");
		zhuangcfyBean.setChep("鄂A100uf");
		zhuangcfyBean.setGongysbm("gys01");
		zhuangcfyBean.setCreator("123");
		zhuangcfyBean.setCreateTime(DateUtil.curDateTime());
		zhuangcfyBean.setEditor("123");
		zhuangcfyBean.setEditTime(DateUtil.curDateTime());
		
		Map<String,String> UCHMap = new HashMap<String,String>();
		UCHMap.put("uch", "'UC2012782101','UC2012782102'");
		UCHMap.put("xiehd", "B");
		List<Zhuangcmy>  zhuangcmyList =zhuangcfyService.queryUCList(UCHMap);
		List<String> xiehdList = new ArrayList<String>();
		xiehdList.add("XHD001");
		xiehdList.add("XHD002");
		zhuangcfyService.insertZhuangc(UCHMap, zhuangcfyBean,xiehdList,"123");
		
		//测试是否插入数据库成功
		Zhuangcfy zhuangcfy = (Zhuangcfy)baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).selectObject("zhuangcfyTest.queryZhuangcfy",params);
		//assertEquals("zcd123457",zhuangcfy.getZhuangcdh());
		assertEquals("UW",zhuangcfyBean.getUsercenter());
		assertEquals("PW0001",zhuangcfyBean.getPeizdh());
		
		List<Zhuangcmy> zcmyList = baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("zhuangcfyTest.queryZhuangcmy",UCHMap);
//		assertEquals("UC2012782101",zcmyList.get(0).getUch());
		//assertEquals("zcd123457",zcmyList.get(0).getZhuangcdh());
		assertEquals("US30392308",zcmyList.get(0).getUskh());
//		assertEquals("UC2012782102",zcmyList.get(1).getUch());
		//assertEquals("zcd123457",zcmyList.get(1).getZhuangcdh());
		assertEquals("US30392309",zcmyList.get(1).getUskh());
	
	}
	
	/**
	 * 封装客户运输路线组
	 * @author 贺志国
	 * @date 2012-7-10
	 */
	//@Test
	public void  testWrapKehLxz(){
		List<Map<String,String>> list = zhuangcfyService.selectKehLxz(params);
		Map<String,List<String>> map = zhuangcfyService.wrapKehLxz(list);
		assertEquals(map.get("LX1").get(0),"W01");
		assertEquals(map.get("LX2").get(0),"KH00000001");
	}
	

	/**
	 * 装车单号
	 * @author 贺志国
	 * @date 2012-7-26
	 */
	//@Test
	public void testGetZhungcdh(){
		String zcd = zhuangcfyService.getZhungcdh("UX", "1", 8);
		assertEquals("X00000001",zcd);
	}
	
	
	/**
	 * mock测试，模拟SQL查询结果与预期结果对比
	 * @author 贺志国
	 * @date 2012-9-22
	 */
	@Test
	public void testGetaohlhOfUCH(){
		//创建返回对象
		List<String> listYhl = new ArrayList<String>();
		//设置查询参数 ，可以不用设值，只需new一个对象即可
		Map<String,String> param = new HashMap<String,String>();
		/*param.put("usercenter", "UX");
	    param.put("uch", "'X20010020','X20010021,'X20010022'");*/
		//设置实体值(即实际预期值)
		listYhl.add("X20010020");
		listYhl.add("X20010021");
		listYhl.add("X20010022");
		//创建mock对象，以接口形式创建
		MyBaseDao daoMock = EasyMock.createMock(MyBaseDao.class);
		//设定预期和返回，查询预期值得到所设定的预期结果。注意：daoMock所调用的方法的参数要与Service中所调用的方法参数相同，否则报AssertionError错。
		EasyMock.expect(daoMock.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ)).andReturn(daoMock);//获得dao对象，否则报空指针
		EasyMock.expect(daoMock.select("zhuangcfy.queryYaohlhOfUCH", param)).andReturn(listYhl);
		//结束录制
		EasyMock.replay(daoMock);
		//比较service调用的值是否与设定的值相同
		ZhuangcfyService mservice = new ZhuangcfyService();
		mservice.setBaseDao(daoMock);
		List<String> list = mservice.getYaohlhOfUCH(param);
		assertNotNull(list);
		assertEquals("X20010020",list.get(0));
		assertEquals("X20010021",list.get(1));
		assertEquals("X20010022",list.get(2));

	}
	
	
	/**
	 * mock测试，模拟SQL查询后再计算得到的结果测试
	 * @author 贺志国
	 * @date 2012-9-22
	 */
	@Test
	public void testGetCalculateResult(){
		//创建返回对象
		List<String> listYhl = new ArrayList<String>();
		listYhl.add("1");
		listYhl.add("2");
		listYhl.add("3");
		//设置查询参数 ，可以不用设值，只需new一个对象即可
		Map<String,String> param = new HashMap<String,String>();
		//创建mock对象
		MyBaseDao daoMock = EasyMock.createMock(MyBaseDao.class);
		//设置预期值
		EasyMock.expect(daoMock.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ)).andReturn(daoMock).times(2);//获得dao对象的次数为2次，因为后面要调用dao两次
		EasyMock.expect(daoMock.select("zhuangcfy.queryXiehdOfUCH", param)).andReturn(listYhl);
		EasyMock.expect(daoMock.select("zhuangcfy.queryYaohlhOfUCH", param)).andReturn(listYhl);
		//结束录制
		EasyMock.replay(daoMock);
		//比较service调用的值是否与设定的值相同
		ZhuangcfyService mservice = new ZhuangcfyService();
		mservice.setBaseDao(daoMock);
		int expect = mservice.getCalculateResult(param);
		assertEquals(4,expect);
		
	}
	
	/**
	 * 测试获得打印机组号和单据组编号
	 * @author 贺志国
	 * @date 2012-9-25
	 */
	@Test
	public void testGetDayjzbh(){
		//创建返回对象
		Map<String,String> expect = new HashMap<String,String>();
		expect.put("djzbh", "dnajz1");
		expect.put("dayjzbh", "1B1B1B");
		//设置查询参数 
		Map<String,String> param = new HashMap<String,String>();
		//创建easyMock对象
		MyBaseDao daoMock = EasyMock.createMock(MyBaseDao.class);
		//设定预期值
		EasyMock.expect(daoMock.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ)).andReturn(daoMock).times(3);
		EasyMock.expect((String)daoMock.selectObject("zhuangcfy.queryYhzbhOfPrintUserInfo", param)).andReturn("WSL");
		EasyMock.expect((String)daoMock.selectObject("zhuangcfy.queryDjzbhOfPrintDictInfo", param)).andReturn("dnajz1");
		EasyMock.expect((String)daoMock.selectObject("zhuangcfy.queryDyjzbhOfPrintRight", param)).andReturn("1B1B1B");
		//结束录制
		EasyMock.replay(daoMock);
		//创建service对象，比较service调用的值是否与设定的值相同
		ZhuangcfyService mservice = new ZhuangcfyService();
		mservice.setBaseDao(daoMock);
		Zhuangcfy bean = new Zhuangcfy();
		Map<String,String> result = mservice.getDayjzbh(bean, "zhuangc",param);
		assertEquals(expect,result);
		EasyMock.verify(daoMock);
		
	}
	
	/**
	 * 测试获得用户组编号
	 * @author 贺志国
	 * @date 2012-9-25
	 */
	@Test
	public void testGetYhzbh(){
		//设置查询参数 
		Map<String,String> param = new HashMap<String,String>();
		//创建easyMock对象
		MyBaseDao daoMock = EasyMock.createMock(MyBaseDao.class);
		//设定预期值
		EasyMock.expect(daoMock.getSdcDataSource(ConstantDbCode.DATASOURCE_FJ)).andReturn(daoMock);
		EasyMock.expect((String)daoMock.selectObject("zhuangcfy.queryYhzbhOfPrintUserInfo", param)).andReturn("WSL");
		
		//结束录制
		EasyMock.replay(daoMock);
		//创建service对象，比较service调用的值是否与设定的值相同
		ZhuangcfyService mservice = new ZhuangcfyService();
		mservice.setBaseDao(daoMock);
		String yhzbh = mservice.getYhzbh(param);
		assertEquals("WSL",yhzbh);
		
	}
	 
	
	/**
	 * 测试读取配置文件
	 * @author 贺志国
	 * @date 2012-9-25
	 */
	@Test
	public void testGetPropertiesValue(){
		String fileName = "config/print.properties";
		String address = zhuangcfyService.getPropertiesValue(fileName,"print_address");
		String pAddress = "http://10.26.171.249:8085/athena-print-service/print/GetPrintService";
		assertEquals(pAddress,address);
	}
	
	

}
