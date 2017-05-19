//package com.auto;
//import org.junit.Test;
//
//import holmos.webtest.Allocator;
//import holmos.webtest.EngineType;
//import holmos.webtest.basetools.HolmosWindow;
//import org.pentaho.di.core.exception.KettleException;
//import org.pentaho.di.core.util.EnvUtil;
////import org.pentaho.di.trans.StepLoader;
//import org.pentaho.di.trans.Trans;
//import org.pentaho.di.trans.TransMeta;
//
//import org.pentaho.di.core.KettleEnvironment;
//import org.pentaho.di.core.exception.KettleException;
//import org.pentaho.di.core.util.EnvUtil;
//import org.pentaho.di.trans.Trans;
//import org.pentaho.di.trans.TransMeta;
//
//public class AthenaTest {
//	AthenaPage athenaPage = new AthenaPage();
//	
//	@Test
//	public void test() {
////		HolmosWindow.openNewWindow(EngineType.WebDriverIE,"http://10.26.202.47/athena/login.jsp");
////		//athenaPage.nameText.setText("root");
////		//athenaPage.pwdTest.setText("123456");
////		HolmosWindow.maxSizeWindow();
////		athenaPage.usercenterCom.selectByValue("UW");
////		athenaPage.loginBut.click();
////		//System.out.print(Allocator.getInstance().getWindows());
//	}
//	@Test
////		 public void callNativeTrans() throws KettleException {  
//			  // 初始化   
////		        EnvUtil.environmentInit();  
////		        //StepLoader.init();  
////		        // 转换元对象   
////		        TransMeta transMeta = new TransMeta("d:/maoxq_BJP.ktr");
////		        Trans trans = new Trans(transMeta);  
////		        // 执行转换   
////		        trans.execute(null);  
////		        // 等待转换执行结束   
////		        trans.waitUntilFinished();  
//		        
////		        try {
////                    KettleEnvironment.init();
////                    EnvUtil.environmentInit();
////                    TransMeta transMeta = new TransMeta("F:\\SpoonRep\\cdcBasedOnTrigger.ktr");
////                    Trans trans = new Trans(transMeta);
////                   
////                    trans.execute(null); // You can pass arguments instead of null.
////                    trans.waitUntilFinished();
////                    if ( trans.getErrors() > 0 )
////                    {
////                      throw new RuntimeException( "There were errors during transformation execution." );
////                    }
////                  }
////                  catch ( KettleException e ) {
////                    // TODO Put your exception-handling code here.
////                    System.out.println(e);
////                  }
//
////		 }
//	
//}
