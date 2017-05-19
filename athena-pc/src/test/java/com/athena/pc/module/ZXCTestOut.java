package com.athena.pc.module;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;

import com.athena.component.test.AbstractCompomentTests;
import com.athena.pc.entity.Beic;
import com.athena.pc.module.exchange.ExportFactory;
import com.athena.pc.module.exchange.ExporterHeadInfo;
import com.toft.core3.container.annotation.Inject;
import com.toft.core3.ibatis.support.AbstractIBatisDao;

import com.athena.db.ConstantDbCode;

public class ZXCTestOut extends AbstractCompomentTests {

	@Inject
	protected AbstractIBatisDao baseDao;

	private Map<String,String> params;
	private Map<String, Object> jsonResult;
	private ExporterHeadInfo headerInfo;

	private int sNum = 0;
	@Inject
	private ExportFactory exportFactory;

    @Rule  
    public ExternalResource resource= new ExternalResource() {  
        @Override  
        protected void before() throws Throwable {  
        	params = new HashMap<String,String>();
        	params.put("FilePath", "e:/users/athOut");
        	params.put("FileName", "zxc_test_out.xls");
        	params.put("UC", "UL");
        	params.put("CREATOR", "INUL");

        };   
    }; 

//	@Test
//	public void testCKX_KEHCZM(){
//		String [] head = {"用户中心","客户编号","账户","跟踪成本","客户类型","标识 1有效 0失效","创建人","创建时间","修改人","修改时间"};
//		String [] properties = {"USERCENTER","KEHBH","ZHANGH","CAOZM","KEHLX","BIAOS","CREATOR","CREATE_TIME","EDITOR","EDIT_TIME"};
//		headerInfo = new ExporterHeadInfo();
//		headerInfo.setHeaders(head);
//		headerInfo.setProperties(properties);
//		
//		params.put("sheetName", "CKX_KEHCZM-");
//		params.put("msg", "客户操作码-单据打印:单据打印表中不存在对应的客户");
//		jsonResult = new HashMap<String, Object>();
//		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputTestzxc.queryCKX_KEHCZM_nokeh", params));
//		exportFactory.export("", headerInfo, jsonResult, params);
//
//	}

/*	@Test
	public void testCKX_DANJDY(){
		String [] head = {"用户中心","客户编号","6:位仓库+子仓库","UC,US,备货单,发货通知","是否打印","打印联数","打印份数","创建人","创建时间","修改人","修改时间"};
		String [] properties = {"USERCENTER","KEHBH","CANGKBH","DANJLX","SHIFDY","DAYLS","DAYFS","CREATOR","CREATE_TIME","EDITOR","EDIT_TIME"};
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);
		
		params.put("sheetName", "CKX_DANJDY-");
		params.put("msg", "客户操作码-单据打印：打印单据中的仓库编号不存在");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputTestzxc.queryCKX_DANJDY_nocangk", params));
		exportFactory.export("", headerInfo, jsonResult, params);

	}*/

	@Test
	public void testCKX_KUW(){
		String [] head = {"用户中心","仓库编号","子仓库编号","库位编号","库位等级编号","库位状态","创建人","创建时间","修改人","修改时间"};
		String [] properties = {"USERCENTER","CANGKBH","ZICKBH","KUWBH","KUWDJBH","KUWZT","CREATOR","CREATE_TIME","EDITOR","EDIT_TIME"};
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);
		
		params.put("sheetName", "CKX_KUW-");
		params.put("msg", "库位: 仓库 不存在");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputTestzxc.queryCKX_KUW_nocangk", params));
		exportFactory.export("", headerInfo, jsonResult, params);

		params.put("sheetName", "CKX_KUW--");
		params.put("msg", "库位: 子仓库  不存在");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputTestzxc.queryCKX_KUW_noZICK", params));
		exportFactory.export("", headerInfo, jsonResult, params);

		
/*		params.put("sheetName", "CKX_KUW---");
		params.put("msg", "库位: 库位等级不存在");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputTestzxc.queryCKX_KUW_nokuwdj", params));
		exportFactory.export("", headerInfo, jsonResult, params);*/
	}

	@Test
	public void testCKX_LINGJCK(){
		String [] head = {"用户中心","零件编号","仓库编号","子仓库编号","原仓库编号","卸货站台编号","安全库存天数","安全库存数量","最大库存天数","最大库存数量","订单表达总量","订单终止总量","已交付总量","系统调整值","单取库编号","单取库位编号","单取库位最大上限","单取库位最小下限","计算调整数值","最小起订量 (仓库)","是否有零件序列号","备用子仓库编号","定置库位","仓库US的包装类型","仓库US的包装容量","上线UC类型","上线UC容量","FIFO","看板是否自动发送","创建人","创建时间","修改人","修改时间","组号"};
		String [] properties = {"USERCENTER","LINGJBH","CANGKBH","ZICKBH","YUANCKBH","XIEHZTBH","ANQKCTS","ANQKCSL","ZUIDKCTS","ZUIDKCSL","DINGDBDZL","DINGDZZZL","YIJFZL","XITTZZ","DANQKBH","DANQKW","ZUIDSX","ZUIXXX","JISTZZ","ZUIXQDL","SHIFXLH","BEIYKBH","DINGZKW","USBZLX","USBZRL","UCLX","UCRL","FIFO","ZIDFHBZ","CREATOR","CREATE_TIME","EDITOR","EDIT_TIME","ZUH"};
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);
		
		params.put("sheetName", "PC_CKX_LINGJCK-");
		params.put("msg", "零件仓库: 零件仓库中定制库位不存在");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputTestzxc.queryCKX_KUW_nolingjck", params));
		exportFactory.export("", headerInfo, jsonResult, params);
	}
	
/*	@Test
	public void testCKX_KUWDJ(){
		String [] head = {"用户中心","仓库编号","库位等级编号","长","宽","高","标识 1有效 0失效","创建人","创建时间","修改人","修改时间"};
		String [] properties = {"USERCENTER","CANGKBH","KUWDJBH","CHANG","KUAN","GAO","BIAOS","CREATOR","CREATE_TIME","EDITOR","EDIT_TIME"};
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);
		
		params.put("sheetName", "CKX_KUWDJ-");
		params.put("msg", "库位等级:仓库 不存在");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputTestzxc.queryCKX_KUWDJ_nocangk", params));
		exportFactory.export("", headerInfo, jsonResult, params);
		
		params.put("sheetName", "CKX_KUWDJ--");
		params.put("msg", "库位等级: 库位等级对应的库位等级包装不存在");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputTestzxc.queryCKX_KUWDJ_nokuwdj", params));
		exportFactory.export("", headerInfo, jsonResult, params);
	}*/

/*	@Test
	public void testCKX_KUWDJ_BAOZ(){
		String [] head = {"用户中心","仓库编号","库位等级编号","包装类型","包装个数","创建人","创建时间","修改人","修改时间"};
		String [] properties = {"USERCENTER","CANGKBH","KUWDJBH","BAOZLX","BAOZGS","CREATOR","CREATE_TIME","EDITOR","EDIT_TIME"};
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);
		
		params.put("sheetName", "CKX_KUWDJ_BAOZ-");
		params.put("msg", "库位等级包装：仓库 不存在");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputTestzxc.queryCKX_KUWDJ_BAOZ_nocangk", params));
		exportFactory.export("", headerInfo, jsonResult, params);
		
		params.put("sheetName", "CKX_KUWDJ_BAOZ-");
		params.put("msg", "库位等级包装：库位等级不存在");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputTestzxc.queryCKX_KUWDJ_BAOZ_nokuwdj", params));
		exportFactory.export("", headerInfo, jsonResult, params);
		
		params.put("sheetName", "CKX_KUWDJ_BAOZ-");
		params.put("msg", "库位等级包装：包装类型 不存在");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputTestzxc.queryCKX_KUWDJ_BAOZ_nobaoz", params));
		exportFactory.export("", headerInfo, jsonResult, params);
	}*/

//	@Test
//	public void testCKX_RONGQC(){
//		String [] head = {"用户中心","容器场编号","描述","标识 1有效 0失效","创建人","创建时间","修改人","修改时间","容器场类型 R, F, J","记账区编号"};
//		String [] properties = {"USERCENTER","RONGQCBH","MIAOS","BIAOS","CREATOR","CREATE_TIME","EDITOR","EDIT_TIME","SHIFFK","JIZQBH"};
//		headerInfo = new ExporterHeadInfo();
//		headerInfo.setHeaders(head);
//		headerInfo.setProperties(properties);
//		
//		params.put("sheetName", "CKX_RONGQC-");
//		params.put("msg", "容器场：工业");
//		jsonResult = new HashMap<String, Object>();
//		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputTestzxc.queryCKX_KUWDJ_BAOZ_nocangk", params));
//		exportFactory.export("", headerInfo, jsonResult, params);
//	}
	
/*	@Test
	public void testCKX_WULDRQGX(){
		String [] head = {"用户中心","","1-产线，2-其他","容器区编号","创建人","创建时间","修改人","修改时间"};
		String [] properties = {"USERCENTER","WULD","WULDLX","WULD2","CREATOR","CREATE_TIME","EDITOR","EDIT_TIME"};
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);
		
		params.put("sheetName", "CKX_WULDRQGX-");
		params.put("msg", "物理点容器场:  如果是产线  1  看物理点(产线)是否存在");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputTestzxc.queryCKX_WULDRQGX_noshengcx", params));
		exportFactory.export("", headerInfo, jsonResult, params);
		
		params.put("sheetName", "CKX_WULDRQGX--");
		params.put("msg", "物理点容器场:  如果是其他  2 容器场编号(其他)是否存在");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputTestzxc.queryCKX_WULDRQGX_norongqc", params));
		exportFactory.export("", headerInfo, jsonResult, params);
	}*/
	
/*	@Test
	public void testCKX_GONGYXHD(){
		String [] head = {"工艺消耗点","距EMON点的车身数量","车身数*(节拍的倒数)*60","按需截止流水号","工艺是否失效,1有效,0失效","是否失效,1有效,0失效","创建人","创建时间","修改人","修改时间","组号","生产线编号","容器场编号"};
		String [] properties = {"GONGYXHD","CHESSL","PIANYSJ","LIUSH","GONGYBS","BIAOS","CREATOR","CREATE_TIME","EDITOR","EDIT_TIME","ZUH","SHENGCXBH","RONGQCBH"};
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);
		
		params.put("sheetName", "CKX_GONGYXHD-");
		params.put("msg", "工艺消耗点:  执行层工艺消耗点在准备层数据中不存在");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputTestzxc.queryCKX_GONGYXHD_nozbc", params));
		exportFactory.export("", headerInfo, jsonResult, params);
		
		params.put("sheetName", "CKX_GONGYXHD--");
		params.put("msg", "工艺消耗点:  工艺消耗点中对应的容器场不存在");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputTestzxc.queryCKX_GONGYXHD_norongqc", params));
		exportFactory.export("", headerInfo, jsonResult, params);
	}*/
	

/*    @Test
    public void testCKX_KEH_CHENGPK(){
    	String[] head = {"用户中心(出发地)","仓库编号(出发地)","客户编号(目的地)","供应商编号/承运商编号/运输商编号","运输路线编号","要车提前期(分钟)","客户提前期(分钟)","是否需要配载","备货类型","订单提前期","创建人","创建时间","修改人","修改时间"};
    	String[] properties = {"USERCENTER","CANGKBH","KEHBH","CHENGYSBH","YUNSLXBH","YAOCTQQ","KEHTQQ","SHIFPZ","BEIHLX","DINGDTQQ","CREATOR","CREATE_TIME","EDITOR","EDIT_TIME"};
    	headerInfo = new ExporterHeadInfo();
    	headerInfo.setHeaders(head);
    	headerInfo.setProperties(properties);
    	
//    	params.put("sheetName", "CKX_KEH_CHENGPK");
//		params.put("msg", "客户成品库关系 : CKX_KEH_CHENGPK  运输路线编号不为null ，看运输线路是否存在 (也可以为null)");
//		jsonResult = new HashMap<String, Object>();
//		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputTestzxc.queryckx_keh_chengpk", params));
//		exportFactory.export("", headerInfo, jsonResult, params);

		params.put("sheetName", "CKX_KEH_CHENGPK-");
		params.put("msg", "客户成品库关系:仓库编号不存在对应的表中");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputTestzxc.queryckx_keh_chengpk_nocangk", params));
		exportFactory.export("", headerInfo, jsonResult, params);
		
		params.put("sheetName", "CKX_KEH_CHENGPK--");
		params.put("msg", "客户成品库关系:客户编号 不存在对应的表中");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputTestzxc.queryckx_keh_chengpk_nokeh", params));
		exportFactory.export("", headerInfo, jsonResult, params);
		
		params.put("sheetName", "CKX_KEH_CHENGPK---");
		params.put("msg", "客户成品库关系:承运商编号 不存在对应的表中");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputTestzxc.queryckx_keh_chengpk_nocys", params));
		exportFactory.export("", headerInfo, jsonResult, params);
    }*/
    
    
//	@Test
//	public void testCKX_LINGJKH(){
//		String [] head = {"用户中心","零件编号","客户编号","客户零件号","客户零件名称","发货站台","零件包装UA","UA包装中零件数量","零件包装UC","UC包装中零件数量","生效日期","失效日期","创建人","创建时间","修改人","修改时间"};
//		String [] properties = {"USERCENTER","LINGJBH","KEHBH","KEHLJH","KEHLJMC","FAHZT","UABAOZ","UALJSL","UCBAOZ","UCLJSL","SHENGXRQ","SHIXRQ","CREATOR","CREATE_TIME","EDITOR","EDIT_TIME"};
//		headerInfo = new ExporterHeadInfo();
//		headerInfo.setHeaders(head);
//		headerInfo.setProperties(properties);
//		
//		params.put("sheetName", "CKX_LINGJKH");
//		params.put("msg", "零件客户关系");
//		jsonResult = new HashMap<String, Object>();
//		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputTestzxc.queryckx_lingjkh", params));
//		exportFactory.export("", headerInfo, jsonResult, params);
//	}
	
	
	@Test
	public void testCKX_PEISLB(){
		String [] head = {"配送类型","配送类型名称","包装类型","包装零件数量/流水号数量","最长等待车位数","同步/集配/包装同步/标识","上线点（物流消耗点）","配套上线标识","备货提前期","小货车车厢层","是否归集(按零件) 1是 0否","备注(左/右/车型等)","是否生成备货单 1是 0否","仓库编号","标识","创建人","创建时间","修改人","修改时间","子仓库编号","物流工艺员","用户中心"};
		String [] properties = {"PEISLX","PEISLXMC","BAOZLX","BAOZSL","ZUICDDCWS","TONGBJPBS","SHANGXD","PEITSXBS","BEIHTQQ","XIAOHCCXC","SHIFGJ","BEIZ","SHIFBHD","CANGKBH","BIAOS","CREATOR","CREATE_TIME","EDITOR","EDIT_TIME","ZICKBH","WLGYY","USERCENTER"};
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);
		
		params.put("sheetName", "CKX_PEISLB-");
		params.put("msg", "配送类别:包装类型 不存在");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputTestzxc.queryckx_peislb_nobaoz", params));
		exportFactory.export("", headerInfo, jsonResult, params);
		
		params.put("sheetName", "CKX_PEISLB--");
		params.put("msg", "配送类别:仓库、子仓库 不存在");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputTestzxc.queryckx_peislb_nocangk", params));
		exportFactory.export("", headerInfo, jsonResult, params);
		
	}
	
/*	@Test
	public void testCKX_LINGJGYS(){
		String [] head = {"用户中心","供应商编号/承运商编号/运输商编号","零件编号","供应合同号","供应份额","有效期","发运地","生效时间","失效时间","最小起订量","参考阀值","质检抽检比例","是否验证批次号","供应商UC的包装类型","供应商UC的容量","供应商UA的包装类型","供应商UA里UC的个数","盖板","内衬","是否存在临时包装 1是 0否","创建人","创建时间","修改人","修改时间","标识（1 有效","0 失效）","是否失效管理"};
		String [] properties = {"USERCENTER","GONGYSBH","LINGJBH","GONGYHTH","GONGYFE","YOUXQ","FAYD","SHENGXSJ","SHIXSJ","ZUIXQDL","CANKFZ","ZHIJCJBL","SHIFYZPCH","UCBZLX","UCRL","UABZLX","UAUCGS","GAIB","NEIC","SHIFCZLSBZ","CREATOR","CREATE_TIME","EDITOR","EDIT_TIME","BIAOS","SHIFSXGL"};
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);
		
		params.put("sheetName", "CKX_LINGJGYS-");
		params.put("msg", "零件供应商： 零件编号不存在");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputTestzxc.queryckx_lingjgys_nolingj", params));
		exportFactory.export("", headerInfo, jsonResult, params);

		params.put("sheetName", "CKX_LINGJGYS--");
		params.put("msg", "零件供应商： 供应商编号 不存在 ");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputTestzxc.queryckx_lingjgys_nogongys", params));
		exportFactory.export("", headerInfo, jsonResult, params);
	}*/
	
	@Test
	public void testCKX_TONGBJPLJ(){
		String [] head = {"用户中心","零件编号","生产线编号","CLASS","VALUE","零件名称","配送类型","前后车标识,-1前车,0当前车","集配包装位置","创建人","创建时间","修改人","修改时间","组号"};
		String [] properties = {"USERCENTER","LINGJBH","SHENGCXBH","NCLASS","NVALUE","LINGJMC","PEISLX","QIANHCBS","JIPBZWZ","CREATOR","CREATE_TIME","EDITOR","EDIT_TIME","ZUH"};
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);
		
		params.put("sheetName", "CKX_TONGBJPLJ-");
		params.put("msg", "同步集配零件:零件编号 不存在");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputTestzxc.queryckx_tongbjplj_nolingj", params));
		exportFactory.export("", headerInfo, jsonResult, params);
		
		params.put("sheetName", "CKX_TONGBJPLJ--");
		params.put("msg", "同步集配零件:生产线编号不存在");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputTestzxc.queryckx_tongbjplj_noshengcx", params));
		exportFactory.export("", headerInfo, jsonResult, params);
		
		params.put("sheetName", "CKX_TONGBJPLJ---");
		params.put("msg", "同步集配零件:配送类型 不存在");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputTestzxc.queryckx_tongbjplj_nopeislb", params));
		exportFactory.export("", headerInfo, jsonResult, params);
	}
	
/*	@Test
	public void testCKX_YANSBLLJ(){
		String [] head = {"用户中心","零件编号","供应商编号","验收项编号","验收项说明","验收比例","创建人","创建时间","修改人","修改时间"};
		String [] properties = {"USERCENTER","LINGJBH","GONGYSBH","YANSXBH","YANSXSM","YANSBL","CREATOR","CREATE_TIME","EDITOR","EDIT_TIME"};
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);
		
		params.put("sheetName", "CKX_YANSBLLJ-");
		params.put("msg", "零件验收比例设置:零件编号不存在");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputTestzxc.queryckx_yansbllj_nolingj", params));
		exportFactory.export("", headerInfo, jsonResult, params);
		
		params.put("sheetName", "CKX_YANSBLLJ--");
		params.put("msg", "零件验收比例设置：供应商编号不存在");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputTestzxc.queryckx_yansbllj_nogongys", params));
		exportFactory.export("", headerInfo, jsonResult, params);
		
//		params.put("sheetName", "CKX_YANSBLLJ---");
//		params.put("msg", "零件验收比例设置：验收项编号 不存在");
//		jsonResult = new HashMap<String, Object>();
//		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputTestzxc.queryckx_yansbllj_noyansbh", params));
//		exportFactory.export("", headerInfo, jsonResult, params);

	}*/

/*	@Test
	public void testCKX_PRINT_DEVICEGROU(){
		String [] head = {"用户中心","打印机组编号","打印机组名称","打印机组描述","是否启用优先级,1为启用，0为不启用，默认0","标识,0无效，1有效，默认1","创建人","创建时间","修改人","修改时间"};
		String [] properties = {"USERCENTER","SPCODES","SNAME","SDESC","NFLAG","BIAOS","CREATOR","CREATE_TIME","EDITOR","EDIT_TIME"};
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);
		
		params.put("sheetName", "CKX_PRINT_DEVICEGROU");
		params.put("msg", "打印设备(打印机组和打印机设备):打印机组在打印机设备中不存在");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputTestzxc.queryckx_print_devicegrou", params));
		exportFactory.export("", headerInfo, jsonResult, params);

		
	}
	
	@Test
	public void testCKX_PRINT_USERS(){
		String [] head = {"用户中心","用户组编号","用户组名称","标识 0无效,1有效,默认1有效","创建人","创建时间","修改人","修改时间"};
		String [] properties = {"USERCENTER","USERSCODE","USERS","BIAOS","CREATOR","CREATE_TIME","EDITOR","EDIT_TIME"};
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);
		
		params.put("sheetName", "CKX_PRINT_USERS");
		params.put("msg", "打印用户组和用户: 用户在用户组中不存在");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputTestzxc.queryckx_print_users", params));
		exportFactory.export("", headerInfo, jsonResult, params);

		
	}*/
	
//	@Test
//	public void testCKX_PRINT_DICTINFOS(){
//		String [] head = {"用户中心","单据编号","单据类型名称","单据组编号","创建人","创建时间","修改人","修改时间","字典类型","标识","服务编号"};
//		String [] properties = {"USERCENTER","ZIDBM","ZIDMC","DANJZBH","CREATOR","CREATE_TIME","EDITOR","EDIT_TIME","ZIDLX","BIAOS","FUWBH"};
//		headerInfo = new ExporterHeadInfo();
//		headerInfo.setHeaders(head);
//		headerInfo.setProperties(properties);
//		
//		params.put("sheetName", "CKX_PRINT_DICTINFOS");
//		params.put("msg", "单据组初始化");
//		jsonResult = new HashMap<String, Object>();
//		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputTestzxc.queryckx_print_dictinfos", params));
//		exportFactory.export("", headerInfo, jsonResult, params);
//	}
	
//	@Test
//	public void testCKX_PRINT_STROGDICT(){
//		String [] head = {"用户中心","仓库编号","单据编号","单据类型名称","单据组编号","打印份数","启用标识 0不启用,1启用，默认1","创建人","创建时间","修改人","修改时间"};
//		String [] properties = {"USERCENTER","CANGKBH","ZIDBM","ZIDMC","DANJZBH","PRINTNUMBER","BIAOS","CREATOR","CREATE_TIME","EDITOR","EDIT_TIME"};
//		headerInfo = new ExporterHeadInfo();
//		headerInfo.setHeaders(head);
//		headerInfo.setProperties(properties);
//		
//		params.put("sheetName", "CKX_PRINT_STROGDICT");
//		params.put("msg", "仓库-单据");
//		jsonResult = new HashMap<String, Object>();
//		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputTestzxc.queryckx_print_strogdict", params));
//		exportFactory.export("", headerInfo, jsonResult, params);
//	}
	
/*	@Test
	public void testCKX_PRINT_RIGHT(){
		String [] head = {"用户中心","用户组","单据组","仓库编号","打印机组","创建人","创建时间","修改人","修改时间"};
		String [] properties = {"USERCENTER","USERSCODE","SCODES","STORAGESCODE","SPCODES","CREATOR","CREATE_TIME","EDITOR","EDIT_TIME"};
		headerInfo = new ExporterHeadInfo();
		headerInfo.setHeaders(head);
		headerInfo.setProperties(properties);
		
		params.put("sheetName", "CKX_PRINT_RIGHT-");
		params.put("msg", "打印权限的设置: 用户组不存在");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputTestzxc.queryckx_print_right", params));
		exportFactory.export("", headerInfo, jsonResult, params);
		// wgs_test 暂时不校验
//		params.put("sheetName", "CKX_PRINT_RIGHT--");
//		params.put("msg", "打印权限的设置: 单据不存在");
//		jsonResult = new HashMap<String, Object>();
//		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputTestzxc.queryckx_print_right_nostrogdict", params));
//		exportFactory.export("", headerInfo, jsonResult, params);
		
		params.put("sheetName", "CKX_PRINT_RIGHT---");
		params.put("msg", "打印权限的设置: 仓库不存在");
		jsonResult = new HashMap<String, Object>();
		jsonResult.put("rows", baseDao.getSdcDataSource(ConstantDbCode.DATASOURCE_CKX).select("outputTestzxc.queryckx_print_right_nocangk", params));
		exportFactory.export("", headerInfo, jsonResult, params);
	}*/
	
}
