package com.athena.component.exchange.utils;

import java.util.ArrayList;
import java.util.List;

import com.athena.component.exchange.DataExchange;
import com.athena.component.runner.Runner;

/**
 * 对给定的 
 * @author chenlei
 * @vesion 1.0
 * @date 2012-5-24
 */
public class DbToDbUtils {
	
	public static void SendData(DataExchange dataExchange,List<String> infaceIds){
		Runner runner = new Runner(10);	
		try{
			List<DbToDbCommand> commandList = new ArrayList<DbToDbCommand>();		
			for(String id :infaceIds){
				commandList.add(new DbToDbCommand(id,dataExchange));		
			}		
			runner.addCommands(commandList.toArray(new DbToDbCommand[commandList.size()]));
			runner.finish(true);
		}catch (Exception e) {
			
		}finally{
			runner.shutdown();
		}		
	}
}
