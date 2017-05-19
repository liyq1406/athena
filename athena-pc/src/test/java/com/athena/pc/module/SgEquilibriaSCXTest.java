package com.athena.pc.module;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.pc.common.CollectionUtil;
import com.athena.pc.entity.LineTimeUpdate;
import com.athena.pc.entity.LingjDayVolume;
import com.athena.pc.entity.Yuemn;
import com.athena.pc.module.service.SgEquilibriaSCXService;
import com.toft.core3.container.annotation.Inject;

public class SgEquilibriaSCXTest  extends AbstractCompomentTests{

	@Inject
	private SgEquilibriaSCXService sgEquilibriaSCXService;
	private List<LineTimeUpdate> gstzList ;
	private Map<String,String> params;
    @Rule  
    public ExternalResource resource= new ExternalResource() {  
        @Override  
        protected void before() throws Throwable {  
        	//基本参数
    		params = new HashMap<String,String>();
    		gstzList= new ArrayList<LineTimeUpdate>();
    		params.put("usercenter", "UW");
    		params.put("startTime", "2012-07-09");
    		params.put("kaissj","2012-07-01");
			params.put("jiessj", "2012-7-29");
    		params.put("biaos", "Y");
    		params.put("lineNum", "UW5L1");
    		params.put("chanxh", "'UW5L1','UW5L2','UW5L3'");
    		List<Map<String,String>> gongzbhBList = sgEquilibriaSCXService.selectYuedmnjhmxOfGongzbh(params);
    		String gongzbh = CollectionUtil.chanxListToString(gongzbhBList, "GONGZBH");
			params.put("gongzbh", gongzbh); //工作编号
			List<Map<String,String>> jihhBList =  sgEquilibriaSCXService.selectYuedmnjhmxOfJihh(params);
			String jihh = CollectionUtil.chanxListToString(jihhBList, "YUEMNJHH");
			params.put("yuemnjhh", jihh); //计划号
			//根据计划员查询产线-零件表获得计划员所管零件
			List<Map<String,String>> lingjbhList = sgEquilibriaSCXService.selectCKX_SHENGCX_LINGJOFLingjbh(params);
			String lingjbh = CollectionUtil.chanxListToString(lingjbhList, "LINGJBH");
			params.put("lingjbh", lingjbh); //零件编号
			params.put("chanxzbh", "UW5L");
    		//页面传值
    		LineTimeUpdate lineTime1 = new LineTimeUpdate();
    		LineTimeUpdate lineTime2 = new LineTimeUpdate();
    		lineTime1.setUsercenter("UW");
    		lineTime1.setLinegroup("UW5L");
    		lineTime1.setLineNum("UW5L1");
    		lineTime1.setStartTime("2012-07-09");
    		lineTime1.setEndTime("2012-07-15");
    		lineTime1.setWorkTime(new BigDecimal(10.00));
    		lineTime2.setUsercenter("UW");
    		lineTime2.setLinegroup("UW5L");
    		lineTime2.setLineNum("UW5L2");
    		lineTime2.setStartTime("2012-07-09");
    		lineTime2.setEndTime("2012-07-15");
    		lineTime2.setWorkTime(new BigDecimal(9.00));
    		gstzList.add(lineTime1);
    		gstzList.add(lineTime2);
    		
        };   
    }; 
    
    /**
     *  查询PC_YUEDMNJHMX表，获得工作编号集合
     * @author 贺志国
     * @date 2012-7-1
     */
    @Test
    public void testSelectYuedmnjhmxOfGongzbh(){
    	List<Map<String,String>> list = sgEquilibriaSCXService.selectYuedmnjhmxOfGongzbh(params);
    	assertEquals("UWUW5L120120702",list.get(0).get("GONGZBH"));
    	assertEquals("UWUW5L120120703",list.get(1).get("GONGZBH"));
    }
    

	/**
	 * 返回某一天零件的消耗量
	 * @author 贺志国
	 * @date 2012-7-1
	 */
    @Test
    public void testSelectLingjMaoxq(){
    	Map<String,String> maoxqQaram = new HashMap<String,String>();
    	maoxqQaram.put("usercenter", "UW");
    	maoxqQaram.put("biaos", "Y");
    	maoxqQaram.put("riq", "2012-07-25");
    	List<Map<String,String>> maoxqList = sgEquilibriaSCXService.selectLingjMaoxq(maoxqQaram);
    	assertEquals("2012-07-25",maoxqList.get(0).get("SHIJ"));
    	assertEquals("LJ001",maoxqList.get(0).get("LINGJBH"));
    	assertEquals("255",maoxqList.get(0).get("LINGJSL"));
    }
    
 
    /**
     * 计算下一天的零件毛需求
     * @author 贺志国
     * @date 2012-7-1
     */
    @Test
    public void testNextLingjMaoxq(){
    	Map<String,Integer> nextLjMaoxq = sgEquilibriaSCXService.nextLingjMaoxq("2012-07-26", params);
    	assertEquals("255",String.valueOf(nextLjMaoxq.get("LJ001")));
    	assertEquals("109",String.valueOf(nextLjMaoxq.get("LJ002")));
    	assertEquals("104",String.valueOf(nextLjMaoxq.get("LJ003")));
    }
    
    
    /**
     * 查询并封装零件第一天的期初库存
     * @author 贺志国
     * @date 2012-7-5
     */
    @Test
    public void testGetCurrQickcOfLingj(){
    	//取第一天所有零件的期初库存
    	Map<String,Integer> firstQCKC = sgEquilibriaSCXService.getFirstQickcOfLingj(params);
    	assertEquals("1006",String.valueOf(firstQCKC.get("LJ001")));
    	assertEquals("600",String.valueOf(firstQCKC.get("LJ002")));
    	assertEquals("396",String.valueOf(firstQCKC.get("LJ003")));
    	assertEquals("500",String.valueOf(firstQCKC.get("LJ004")));
    	assertEquals("613",String.valueOf(firstQCKC.get("LJ005")));
    	
    }
    
    
    /**
     * 计算当天期初库存
     * @author 贺志国
     * @date 2012-7-5
     */
    @Test
    public void testCaculateCurrQickc(){
    	Yuemn yue = new Yuemn();
    	List<Yuemn> yueList = new ArrayList<Yuemn>();
    	yue.setRiq("2012-07-26");
    	yue.setGongzbh("UWUW5L120120726");
    	yueList.add(yue);
    	List<LingjDayVolume> days = sgEquilibriaSCXService.getLingjDayVolume(yueList,params);
    	Map<String,Integer> currQCKC = sgEquilibriaSCXService.caculateCurrQickc(days);
    	assertEquals("822",String.valueOf(currQCKC.get("LJ001")));
    	assertEquals("341",String.valueOf(currQCKC.get("LJ003")));
    	
    	
    }
  
    
    /**
     * 计算零件下一天期初库存（期初库存=当日库存+排产量-消耗量）
     * @author 贺志国
     * @date 2012-7-2
     */
    @Test
   public void testCaculateNextQickcOfLingj(){
    	Yuemn yue = new Yuemn();
    	List<Yuemn> yueList = new ArrayList<Yuemn>();
    	yue.setRiq("2012-07-26");
    	yue.setGongzbh("UWUW5L120120726");
    	yueList.add(yue);
    	//查询当天的消耗量
		Map<String,String> currDayParam  = new HashMap<String,String>();
		currDayParam.put("riq", yue.getRiq());
		currDayParam.put("biaos", params.get("biaos"));
		currDayParam.put("usercenter", params.get("usercenter"));
    	//List<LingjDayVolume> days = sgEquilibriaSCXService.getLingjDayVolume(yueList,params);
    	Map<String,Integer> firstQCKC = sgEquilibriaSCXService.getFirstQickcOfLingj(params);
    	//计算当天的期初库存
    	Map<String,Integer> ljPCL = sgEquilibriaSCXService.caculatePaicl("'UXUX5L120120730', 'UXUX5L220120730'","Y");;
    	Map<String,Integer> ljXHL = sgEquilibriaSCXService.wrapNextMaoxq(sgEquilibriaSCXService.selectLingjMaoxq(currDayParam));
    	sgEquilibriaSCXService.caculateNextQickcOfLingj(ljPCL,ljXHL,firstQCKC);
    	
    }

    /**
     * 返回某条产线所有工作日
     * @author 贺志国
     * @date 2012-7-2这
     */
    @Test
    public void testGetWorkCalendarCX(){
    	List<String> gzrList = sgEquilibriaSCXService.getWorkCalendarCX(params);
    	assertEquals(gzrList.get(0),"2012-07-09");
    }
    
    
    @Test
    public void testB(){
    	BigDecimal w = new BigDecimal("11.3");
    	boolean flag = true;
    	double minTime = 0.25;
    	double t = w.doubleValue()%minTime;
    	int s = (int) (w.doubleValue()/minTime);
    	System.out.println(t+"$$$$$$$$"+s);
    	if(t!=0){
    		System.out.println("不成功");
    	}
    	if(flag){
    		System.out.println("不成功"+flag);
    	}
    }
    
    /**
     * 手工调整工时
     * @author 贺志国
     * @date 2012-7-1
     */
   //@Test
    public void testStickOfequilibria(){
    	sgEquilibriaSCXService.stickOfequilibria(params, gstzList);
    }
   
}
