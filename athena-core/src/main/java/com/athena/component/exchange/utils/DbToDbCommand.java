package com.athena.component.exchange.utils;

import com.athena.component.exchange.DataExchange;
import com.athena.component.runner.Command;
/**
 * 执行数据分发的	Command
 * @author chenlei
 * @vesion 1.0
 * @date 2012-5-24
 */
public class DbToDbCommand extends Command<String> {
	
	private String infaceId;
	private DataExchange dataExchange;
	
	@Override
	public void execute() {
		dataExchange.doExchange(infaceId,"");		
	}

	public DbToDbCommand(String infaceId, DataExchange dataExchange) {
		super();
		this.infaceId = infaceId;
		this.dataExchange = dataExchange;
	}

	@Override
	public String result() {	
		return infaceId;
	}

}
