package com.athena.truck.component;

import com.athena.truck.module.queue.service.QueueService;
import com.toft.core3.container.annotation.Component;
import com.toft.core3.container.annotation.Inject;

@Component
public class QueueTimeController {
	@Inject
	private  QueueService queueService;
	//定时调用 排队 总控制器
	public void run() throws Exception  {
		queueService.queueRCTask();
		
	}

}
