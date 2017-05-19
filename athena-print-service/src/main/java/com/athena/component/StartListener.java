/**
 * 
 */
package com.athena.component;

import com.athena.print.SysmonitorService;
import com.athena.print.controller.GetQtaskTimeController;
import com.athena.print.controller.PrintMonitorTimeController;
import org.apache.log4j.Logger;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.context.SdcContextEvent;
import com.toft.core3.context.SdcContextListener;
import java.util.concurrent.*;


/**
 * <p>Title:</p>
 *
 * <p>Description:</p>
 *
 * <p>Copyright:Copyright (c) 2011.9</p>
 *
 * <p>Company:iSoftstone</p>
 *
 * @author Administrator
 * @version 1.0
 */
@Component
public class StartListener  implements SdcContextListener{

	private static Logger logger=Logger.getLogger(StartListener.class);
	
	public void contextDestroyed(SdcContextEvent contextEvent) {
		contextEvent.getSdcContext().destroySingleton("printListner");
	}
	
	public void contextInitialized(final SdcContextEvent contextEvent) {

        ScheduledExecutorService schedule = Executors.newScheduledThreadPool(5);

        schedule.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    PrintMonitorTimeController controller = contextEvent.getSdcContext().getComponent(PrintMonitorTimeController.class);
                    controller.run();
                } catch (Exception e){
                    logger.error("1秒监控打印机状态出现异常");
                    logger.error(e);
                }
            }
        }, 1,1, TimeUnit.SECONDS);

        schedule.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    GetQtaskTimeController controller = contextEvent.getSdcContext().getComponent(GetQtaskTimeController.class);
                    controller.run();
                } catch (Exception e){
                    logger.error("10秒轮询出现异常");
                    logger.error(e);
                }

            }
        }, 10, 10, TimeUnit.SECONDS);
		logger.info("监听器运行...");


        schedule.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    SysmonitorService service = new SysmonitorService();
                    service.SendHeartbeat("8000", "ATHU3PRI");
                } catch (Exception e) {
                    logger.error("==>>发送心跳出错：" + e.getMessage());
                }
            }
        }, 1,5, TimeUnit.MINUTES);
	}
	

}
