package com.athena.print.client;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

import com.athena.print.controller.GetPrintService;


public class DemoClient {
	/**
	 * @param args
	 */
	public static void main(String[] args){
		
//		ClientProxyFactoryBean factory = new ClientProxyFactoryBean();
//		factory.setServiceClass(GetPrintService.class);
//		factory.setAddress("http://localhost:8086/athena-print-service/services/GetPrintService");
//		GetPrintService client = (GetPrintService) factory.create();
		
		
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setAddress("http://localhost:8085/athena-print-service/print/GetPrintService");
        GetPrintService client = factory.create(GetPrintService.class);
		
//	    JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
//        factory.setAddress("http://10.26.171.210:7001/athena-print-service-1.0.0-SNAPSHOT/print/GetPrintService");
//        http://localhost:8086/athena-print-service/printTest
//        GetPrintService client = factory.create(GetPrintService.class);
//
//	
//		//打印模板解析组件测试
//		String printUser = "user114"; 		//打印用户编号
//		String printSheet = "s01";		//打印单据编号
//		String printCangk = "001";			//打印仓库编号
//		String deviceGroup = "1A1A1A";			//打印机组编号
//		
//		//打印参数 JSON
//		String printContent = "[{\"1\":\"UC:\",\"2\":\"2180\",\"3\":\"BL\",\"4\":\"51\",\"5\":\"EL:\",\"6\":\"7903233049\",\"7\":\"liujiaofalanluomu\",\"8\":\"area:\",\"9\":\"7W0513-14AADF1K\",\"10\":\"orderID:\",\"11\":\"RQ05110630\",\"12\":\"Type:\",\"13\":\"RP/RI/KP/KI\",\"14\":\"StoTime:\",\"15\":\"11/05/11 08/46\",\"16\":\"Address:\",\"17\":\"WA003B OC1002\",\"18\":\"US:\",\"19\":\"SW00017250\",\"20\":\"USNO-001\",\"21\":\"1B00218551\",\"22\":\"kanban\",\"23\":\"7903233049\",\"24\":\"liujiaofalanluomu\",\"25\":\"area:\",\"26\":\"W0513-14AADF1K\",\"27\":\"Stotime:\",\"28\":\"11/05/11 08/46\",\"29\":\"CDE:\",\"30\":\"RP/RI/KP/KI\",\"31\":\"Address:\",\"32\":\"WA003B 0C1002\"}]";
//		
//		String printModel = "p1";  	//打印模板编号 printModule1 
//		int printNum = 1;						//打印份数
//		//int printCount = 1;						//打印联数
//		String printType = "A";					//打印样式     A 121212;  B 111222;
//		int count = 4;            // 子任务数量
//		String usercenter = "UW";            // 用户中心
//		
//		//准备数据的插入
//		//for (int i = 0; i < 5; i++) {
//			PrintQtaskmain printQtaskmain =client.addTaskSequence(printUser, printSheet, printCangk, deviceGroup,
//					printContent, printModel, printNum, printType,count,usercenter);
			//执行打印
			client.getQtaskController("W0000000233");
//		//}
//		
			System.out.println("××××××××××××××××打印完毕");
//		
		
	}

}
