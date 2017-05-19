package com.athena.print.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.log4j.Logger;

import com.athena.print.controller.GetPrintService;

public class PrintTestServlet extends HttpServlet {
	
	private static Logger logger=Logger.getLogger(PrintTestServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			
			
			String path = request.getRequestURL().toString();
			
			String tempPath = "print/GetPrintService";
			path = path.replace("printTest", tempPath);
			
			//logger.info("**************"+path);
			//http://localhost:8085/athena-print-service/print/GetPrintService
			
			JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
	        factory.setAddress(path);  
	        
	        GetPrintService client = factory.create(GetPrintService.class);

		
			//打印模板解析组件测试
			//String printUser = "user1"; 		//打印用户编号
			//String printSheet = "danjz1";		//单据组编号
			//String printCangk = "C01";			//打印仓库编号
			//String deviceGroup = "1A1A1A";			//打印机组编号
			//子任务编号
			//String seq="b000326";
			
			//打印参数 JSON 分区域的参数
			//区域1
			//String printContent = "[{\"1-2\": \"No:\", \"1-3\": \"b000326\", \"1-5\": \"abc\", \"1-7\": \"C01\", \"1-9\": \"\", \"1-11\": \"gys0000002\", \"1-13\": \"1\", \"1-15\": \"kc1\", \"1-17\": \"ck\", \"1-19\": \"\"}]";
			//区域2
			//String printContent1="[{\"2-1\": \"b000439\", \"2-2\": \"b00000279\", \"2-3\": \"fdfds\", \"2-4\": \"fdfdf\", \"2-5\": \"16\", \"2-6\": \"lj00000001\", \"2-7\": \"10.000000\"}]";
			//区域3
			//String printContent2="[{\"3-2\": \"\", \"3-4\": \"\", \"3-6\": \"\"}]";
			
			//String printModel = "p19";  	//打印模板编号 printModule1 
			//int printNum = 1;						//打印份数
			//int printCount = 1;						//打印联数
			//String printType = "A";					//打印样式     A 121212;  B 111222;
			//int count = 4;            // 子任务数量
			//String usercenter = "UW";            // 用户中心
			
			//准备数据的插入
			//for (int i = 0; i < 5; i++) {
//				PrintQtaskmain printQtaskmain =client.addTaskSequence(printUser, printSheet, printCangk, deviceGroup,
//						printContent, printModel, printNum,printCount, printType,usercenter,seq);
				//执行打印
	        	String qid = request.getParameter("printContent");
	        	//logger.info("××××××××××××××××"+qid);
				client.getQtaskController(qid);
			//}
			
				//logger.info("××××××××××××××××打印完毕");
			
			//DemoClient.main(new String[]{"1"});
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		request.getRequestDispatcher("/index.jsp").forward(request, response);
	}

}
